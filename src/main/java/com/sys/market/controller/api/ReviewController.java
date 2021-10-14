package com.sys.market.controller.api;

import com.sys.market.advice.exception.CConflictException;
import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.dto.IdInfo;
import com.sys.market.dto.criteria.ReviewSearchCriteria;
import com.sys.market.entity.Review;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.enums.ReviewType;
import com.sys.market.service.ResponseService;
import com.sys.market.service.ReviewService;
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
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ResponseService responseService;

    @Operation(summary = "리뷰 조회")
    @GetMapping("/{id}")
    public SingleResult<Review> review(@PathVariable Integer id){
        Review review  = reviewService.findReview(id);
        if(review==null) throw new CNotFoundException("존재하지 않는 리뷰입니다.");

        return responseService.getSingleResult(review);
    }

    @Operation(summary = "리뷰 리스트 조회")
    @GetMapping
    public PageResult<Review> reviewList(@ModelAttribute ReviewSearchCriteria criteria, HttpServletResponse response){
        List<Review> reviewList = new ArrayList<>();

        int totalReviewCount = reviewService.findReviewCount(criteria);
        if(totalReviewCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(reviewList, criteria);
        }

        criteria.paging(totalReviewCount);
        reviewList = reviewService.findReviewList(criteria);
        return responseService.getPageResult(reviewList, criteria);
    }

    @Operation(summary = "리뷰 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult reviewSave(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                   @RequestBody @Valid ReviewRequest reviewRequest){
        // 한 유저에게는 한개의 리뷰만 작성할 수 있다.
        ReviewSearchCriteria criteria = new ReviewSearchCriteria();
        criteria.setWriterId(user.getId());
        criteria.setUserId(reviewRequest.getUserId());
        int count = reviewService.findReviewCount(criteria);
        if(count > 0){
            throw new CConflictException("이미 해당 유저에게 리뷰를 작성했습니다.");
        }

        Review reviewInfo = ReviewRequest.toEntity(reviewRequest);
        reviewInfo.setWriterId(user.getId());

        Integer reviewId = reviewService.addReview(reviewInfo);
        return responseService.getSingleResult(new IdInfo<>(reviewId));
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @reviewService.ownedByUser(principal.id, #id)")
    public CommonResult reviewUpdate(@PathVariable Integer id,
                                     @RequestBody ReviewUpdateRequest reviewUpdateRequest){
        Review reviewInfo = ReviewUpdateRequest.toEntity(reviewUpdateRequest);
        reviewInfo.setId(id);
        reviewService.modifyReview(reviewInfo);

        return responseService.getSuccessResult();
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @reviewService.ownedByUser(principal.id, #id)")
    public CommonResult reviewRemove(@PathVariable Integer id){
        reviewService.removeReview(id);

        return responseService.getSuccessResult();
    }


    @Getter
    @Setter
    public static class ReviewRequest {
        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private String userId;

        @NotNull(message = "별점을 입력해주세요.")
        @Min(value = 1, message = "별점은 1~5사이의 값만 입력할 수 있습니다.")
        @Max(value = 5, message = "별점은 1~5사이의 값만 입력할 수 있습니다.")
        private Integer rating;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max=400, message = "내용은 최대 400자까지 입력할 수 있습니다.")
        private String content;

        @NotNull(message = "리뷰 타입을 입력해주세요.")
        private ReviewType reviewType;

        public static Review toEntity(ReviewRequest r){
            Review review = new Review();
            review.setUserId(r.getUserId());
            review.setRating(r.getRating());
            review.setContent(r.getContent());
            review.setReviewType(r.getReviewType());
            return review;
        }
    }

    @Getter
    @Setter
    public static class ReviewUpdateRequest {
        private Integer rating;
        private String content;
        private ReviewType reviewType;

        public static Review toEntity(ReviewUpdateRequest r){
            Review review = new Review();
            review.setRating(r.getRating());
            review.setContent(r.getContent());
            review.setReviewType(r.getReviewType());
            return review;
        }
    }

}
