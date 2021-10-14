package com.sys.market.controller.api;

import com.sys.market.advice.exception.CConflictException;
import com.sys.market.entity.Item;
import com.sys.market.dto.criteria.WishlistSearchCriteria;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.service.ResponseService;
import com.sys.market.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlists")
public class WishlistController {
    private final WishlistService wishlistService;
    private final ResponseService responseService;

    @Operation(summary = "관심 상품 조회")
    @GetMapping
    public PageResult<ItemController.ItemResponse> wishlist(@ModelAttribute WishlistSearchCriteria criteria,
                                                            HttpServletResponse response){
        List<ItemController.ItemResponse> wishlistList = new ArrayList<>();

        int totalWishlistCount = wishlistService.findWishlistCount(criteria);
        if(totalWishlistCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(wishlistList, criteria);
        }

        criteria.paging(totalWishlistCount);
        wishlistList = wishlistService.findWishlistList(criteria).stream().map(Item::toItemResponse).collect(Collectors.toList());
        return responseService.getPageResult(wishlistList, criteria);
    }

    @Operation(summary = "현재 사용자의 관심 상품 목록에 지정한 상품의 존재 여부 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{itemId}/exists")
    public CommonResult wishlistExists(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                       @PathVariable Integer itemId){
        return responseService.getSingleResult(wishlistService.wishlistExists(user.getId(), itemId));
    }

    @Operation(summary = "관심 상품 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult wishlistAdd(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                    @RequestBody @Valid WishlistRequest wishlistRequest){
        if(wishlistService.wishlistExists(user.getId(), wishlistRequest.getItemId())){
            throw new CConflictException("이미 관심상품에 추가한 아이템입니다.");
        }
        wishlistService.addWishlist(user.getId(), wishlistRequest.getItemId());
        return responseService.getSuccessResult();
    }

    @Operation(summary = "관심 상품 삭제")
    @DeleteMapping("/{itemId}")
    public CommonResult wishlistRemove(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                       @PathVariable Integer itemId){
        wishlistService.removeWishlist(user.getId(), itemId);
        return responseService.getSuccessResult();
    }


    @Getter
    @Setter
    public static class WishlistRequest {
        @NotNull(message = "상품 아이디를 입력해주세요.")
        private Integer itemId;
    }
}
