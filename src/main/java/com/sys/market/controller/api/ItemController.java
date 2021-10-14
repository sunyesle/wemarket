package com.sys.market.controller.api;

import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.dto.IdInfo;
import com.sys.market.entity.UserInfo;
import com.sys.market.dto.criteria.ItemSearchCriteria;
import com.sys.market.entity.Item;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.ListResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.enums.ItemStatus;
import com.sys.market.service.ItemService;
import com.sys.market.service.OfferService;
import com.sys.market.service.ResponseService;
import com.sys.market.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/items")
public class ItemController {
    private final ItemService itemService;
    private final OfferService offerService;
    private final UserService userService;
    private final ResponseService responseService;

    @Operation(summary = "상품 조회")
    @GetMapping(value = "/{id}")
    public SingleResult<ItemDetailResponse> item(@PathVariable Integer id){
        Item item = itemService.findItem(id);

        if(item == null) throw new CNotFoundException();

        User user = userService.findUser(item.getUserId());
        ItemDetailResponse itemDetailResponse = item.toItemDetailResponse();
        itemDetailResponse.setUser(user.toUserInfo());

        return responseService.getSingleResult(itemDetailResponse);
    }

    @Operation(summary = "상품 리스트 조회")
    @GetMapping
    public PageResult<ItemResponse> itemList(@ModelAttribute @Valid ItemSearchCriteria criteria,
                                             HttpServletResponse response){
        List<ItemResponse> itemList = new ArrayList<>();
        int totalItemCount = itemService.findItemCount(criteria);
        if(totalItemCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(itemList,criteria);
        }

        criteria.paging(totalItemCount); //페이징
        itemList = itemService.findItemList(criteria).stream().map(Item::toItemResponse).collect(Collectors.toList());

        return responseService.getPageResult(itemList,criteria);
    }


    @Operation(summary = "상품 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult itemAdd(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                @RequestBody @Valid ItemRequest itemRequest){
        Item itemInfo = ItemRequest.toEntity(itemRequest);
        itemInfo.setUserId(user.getId()); // user id는 jwt에서

        Integer itemId = itemService.addItem(itemInfo);
        return responseService.getSingleResult(new IdInfo<>(itemId));
    }

    @Operation(summary = "상품 수정")
    @PutMapping(value="/{id}")
    @PreAuthorize("isAuthenticated() and (@itemService.ownedByUser(principal.id, #id))")
    public CommonResult itemModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                   @PathVariable Integer id,
                                   @RequestBody @Valid ItemRequest itemRequest){
        Item itemInfo = ItemRequest.toEntity(itemRequest);
        itemInfo.setId(id);

        itemService.modifyItem(itemInfo);
        return responseService.getSuccessResult();
    }

    // imageIds값이 -1일경우 새로운 이미지 업로드 아닐경우 기존 이미지 수정. files와 imageIds 배열은 길이가 같아야함
    @Operation(summary = "상품 이미지 등록")
    @PutMapping(value="/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated() and (@itemService.ownedByUser(principal.id, #id))")
    public CommonResult itemImageModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                        @PathVariable Integer id,
                                        @RequestParam(value = "files", required = false)MultipartFile[] files,
                                        @RequestParam(value = "imageIds", required = false)Integer[] imageIds){
        itemService.modifyItemImage(id, files, imageIds);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "상품의 이미지 id 리스트 조회")
    @GetMapping(value="/{id}/imageIds")
    public ListResult<Integer> itemImageIdList(@PathVariable Integer id){
        List<Integer> itemImageIdList = itemService.findItemImageIdList(id);
        return responseService.getListResult(itemImageIdList);
    }

    @Operation(summary = "상품 삭제")
    @PreAuthorize("isAuthenticated() and (@itemService.ownedByUser(principal.id, #id))")
    @DeleteMapping(value="/{id}")
    public CommonResult itemRemove(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                   @PathVariable Integer id){
        itemService.removeItem(id);
        return responseService.getSuccessResult();
    }


    @Getter
    @Setter
    public static class ItemRequest{
        @NotNull(message = "카테고리를 입력해주세요.")
        @Min(value = 0, message = "카테고리에 올바른 값을 입력해주세요.")
        @Max(value = 14, message = "카테고리에 올바른 값을 입력해주세요.")
        private Integer categoryId;

        @NotNull(message = "가격을 입력해주세요.")
        @Min(value=0, message = "가격은 0~999999999 사이의 값만 입력할 수 있습니다.")
        @Max(value=999999999, message = "가격은 0~999999999 사이의 값만 입력할 수 있습니다.")
        private Integer price;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max=40, message = "제목은 최대 40자까지 입력할 수 있습니다.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max=4000, message = "내용은 최대 4000자까지 입력할 수 있습니다.")
        private String content;

        public static Item toEntity(ItemRequest r){
            Item item = new Item();
            item.setCategoryId(r.getCategoryId());
            item.setPrice(r.getPrice());
            item.setTitle(r.getTitle());
            item.setContent(r.getContent());
            return item;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemDetailResponse{
        private Integer id;
        private String userId;
        private Integer categoryId;
        private Integer price;
        private String title;
        private String content;
        private Date createDate;
        private Date updateDate;
        private ItemStatus status;

        private String category;
        private Integer offerCount;
        private List<String> images;

        private UserInfo user;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemResponse{
        private Integer id;
        private String userId;
        private Integer categoryId;
        private Integer price;
        private String title;
        private Date createDate;
        private Date updateDate;
        private ItemStatus status;

        private String nickname;
        private String address;
        private String category;
        private Integer offerCount;
        private String thumbImage;
    }
}
