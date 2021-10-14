package com.sys.market.mapper;

import com.sys.market.dto.criteria.ReviewSearchCriteria;
import com.sys.market.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    Review selectReviewById(Integer id);

    List<Review> selectReviewList(ReviewSearchCriteria criteria);

    int selectReviewCount(ReviewSearchCriteria criteria);

    void insertReview(Review reviewInfo);

    void updateReview(Review reviewInfo);

    void deleteReview(Integer id);
}
