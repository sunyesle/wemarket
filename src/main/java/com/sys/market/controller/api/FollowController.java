package com.sys.market.controller.api;

import com.sys.market.advice.exception.CConflictException;
import com.sys.market.entity.Follow;
import com.sys.market.dto.criteria.FollowSearchCriteria;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.ListResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.service.FollowService;
import com.sys.market.service.ResponseService;
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
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/followers")
public class FollowController {
    private final FollowService followService;
    private final ResponseService responseService;

    @Operation(summary = "팔로워 리스트 조회")
    @GetMapping
    public ListResult<Follow> follows(@ModelAttribute FollowSearchCriteria criteria,
                                      HttpServletResponse response){
        List<Follow> followList = new ArrayList<>();

        int totalWishlistCount = followService.findFollowCount(criteria);
        if(totalWishlistCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(followList, criteria);
        }

        criteria.paging(totalWishlistCount);
        followList = followService.findFollowList(criteria);
        return responseService.getPageResult(followList, criteria);
    }

    @Operation(summary = "현재 사용자의 팔로잉 목록에 지정한 사용자의 존재 여부 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("{userId}/exists")
    public SingleResult<Boolean> followingExists(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                                 @PathVariable String userId){
        return responseService.getSingleResult(followService.followExists(user.getId(), userId));
    }

    // 현재 사용자를 지정된 사용자의 팔로워로 추가한다.
    @Operation(summary = "팔로우")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult follow(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                  @RequestBody @Valid FollowRequest followRequest){
        if(followService.followExists(user.getId(), followRequest.getUserId())){
            throw new CConflictException("이미 이 사용자를 팔로우했습니다.");
        }
        followService.addFollow(user.getId(), followRequest.getUserId());
        return responseService.getSuccessResult();
    }

    // 현재 사용자를 지정된 사용자의 팔로워에서 삭제한다.
    @Operation(summary = "언팔로우")
    @DeleteMapping("/{userId}")
    public CommonResult unfollow(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                      @PathVariable String userId){
        followService.removeFollow(user.getId(), userId);
        return responseService.getSuccessResult();
    }

    @Getter
    @Setter
    public static class FollowRequest{
        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private String userId;
    }
}