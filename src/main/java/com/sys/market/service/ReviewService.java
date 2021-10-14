package com.sys.market.service;

import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.dto.criteria.ReviewSearchCriteria;
import com.sys.market.entity.Review;
import com.sys.market.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    public Review findReview(Integer id) {
        return reviewMapper.selectReviewById(id);
    }

    public List<Review> findReviewList(ReviewSearchCriteria criteria) {
        return reviewMapper.selectReviewList(criteria);
    }

    public int findReviewCount(ReviewSearchCriteria criteria){
        return reviewMapper.selectReviewCount(criteria);
    }

    public Integer addReview(Review reviewInfo) {
        reviewMapper.insertReview(reviewInfo);
        return reviewInfo.getId();
    }

    public void modifyReview(Review reviewInfo) {
        reviewMapper.updateReview(reviewInfo);
    }

    public void removeReview(Integer id) {
        reviewMapper.deleteReview(id);
    }

    public boolean ownedByUser(String userId, Integer reviewId){
        Review review = reviewMapper.selectReviewById(reviewId);
        if(review == null)throw new CNotFoundException();
        return (review.getWriterId().equals(userId));
    }
}
