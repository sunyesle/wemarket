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

    @Operation(summary = "์ํ ์กฐํ")
    @GetMapping(value = "/{id}")
    public SingleResult<ItemDetailResponse> item(@PathVariable Integer id){
        Item item = itemService.findItem(id);

        if(item == null) throw new CNotFoundException();

        User user = userService.findUser(item.getUserId());
        ItemDetailResponse itemDetailResponse = item.toItemDetailResponse();
        itemDetailResponse.setUser(user.toUserInfo());

        return responseService.getSingleResult(itemDetailResponse);
    }

    @Operation(summary = "์ํ ๋ฆฌ์คํธ ์กฐํ")
    @GetMapping
    public PageResult<ItemResponse> itemList(@ModelAttribute @Valid ItemSearchCriteria criteria,
                                             HttpServletResponse response){
        List<ItemResponse> itemList = new ArrayList<>();
        int totalItemCount = itemService.findItemCount(criteria);
        if(totalItemCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(itemList,criteria);
        }

        criteria.paging(totalItemCount); //ํ์ด์ง
        itemList = itemService.findItemList(criteria).stream().map(Item::toItemResponse).collect(Collectors.toList());

        return responseService.getPageResult(itemList,criteria);
    }


    @Operation(summary = "์ํ ๋ฑ๋ก")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult itemAdd(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                @RequestBody @Valid ItemRequest itemRequest){
        Item itemInfo = ItemRequest.toEntity(itemRequest);
        itemInfo.setUserId(user.getId()); // user id๋ jwt์์

        Integer itemId = itemService.addItem(itemInfo);
        return responseService.getSingleResult(new IdInfo<>(itemId));
    }

    @Operation(summary = "์ํ ์์?")
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

    // imageIds๊ฐ์ด -1์ผ๊ฒฝ์ฐ ์๋ก์ด ์ด๋ฏธ์ง ์๋ก๋ ์๋๊ฒฝ์ฐ ๊ธฐ์กด ์ด๋ฏธ์ง ์์?. files์ imageIds ๋ฐฐ์ด์ ๊ธธ์ด๊ฐ ๊ฐ์์ผํจ
    @Operation(summary = "์ํ ์ด๋ฏธ์ง ๋ฑ๋ก")
    @PutMapping(value="/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated() and (@itemService.ownedByUser(principal.id, #id))")
    public CommonResult itemImageModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                        @PathVariable Integer id,
                                        @RequestParam(value = "files", required = false)MultipartFile[] files,
                                        @RequestParam(value = "imageIds", required = false)Integer[] imageIds){
        itemService.modifyItemImage(id, files, imageIds);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "์ํ์ ์ด๋ฏธ์ง id ๋ฆฌ์คํธ ์กฐํ")
    @GetMapping(value="/{id}/imageIds")
    public ListResult<Integer> itemImageIdList(@PathVariable Integer id){
        List<Integer> itemImageIdList = itemService.findItemImageIdList(id);
        return responseService.getListResult(itemImageIdList);
    }

    @Operation(summary = "์ํ ์ญ์?")
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
        @NotNull(message = "์นดํ๊ณ?๋ฆฌ๋ฅผ ์๋?ฅํด์ฃผ์ธ์.")
        @Min(value = 0, message = "์นดํ๊ณ?๋ฆฌ์ ์ฌ๋ฐ๋ฅธ ๊ฐ์ ์๋?ฅํด์ฃผ์ธ์.")
        @Max(value = 14, message = "์นดํ๊ณ?๋ฆฌ์ ์ฌ๋ฐ๋ฅธ ๊ฐ์ ์๋?ฅํด์ฃผ์ธ์.")
        private Integer categoryId;

        @NotNull(message = "๊ฐ๊ฒฉ์ ์๋?ฅํด์ฃผ์ธ์.")
        @Min(value=0, message = "๊ฐ๊ฒฉ์ 0~999999999 ์ฌ์ด์ ๊ฐ๋ง ์๋?ฅํ? ์ ์์ต๋๋ค.")
        @Max(value=999999999, message = "๊ฐ๊ฒฉ์ 0~999999999 ์ฌ์ด์ ๊ฐ๋ง ์๋?ฅํ? ์ ์์ต๋๋ค.")
        private Integer price;

        @NotBlank(message = "์?๋ชฉ์ ์๋?ฅํด์ฃผ์ธ์.")
        @Size(max=40, message = "์?๋ชฉ์ ์ต๋ 40์๊น์ง ์๋?ฅํ? ์ ์์ต๋๋ค.")
        private String title;

        @NotBlank(message = "๋ด์ฉ์ ์๋?ฅํด์ฃผ์ธ์.")
        @Size(max=4000, message = "๋ด์ฉ์ ์ต๋ 4000์๊น์ง ์๋?ฅํ? ์ ์์ต๋๋ค.")
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
