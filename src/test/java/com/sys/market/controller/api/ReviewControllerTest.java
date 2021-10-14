package com.sys.market.controller.api;


import com.jayway.jsonpath.JsonPath;
import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.Review;
import com.sys.market.entity.User;
import com.sys.market.enums.ReviewType;
import com.sys.market.enums.UserRole;
import com.sys.market.service.ReviewService;
import com.sys.market.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends BaseIntegrationTest {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @Test
    void 리뷰_조회() throws Exception{
        userSetup("anotherUser");
        Review reviewInfo = reviewSetup("anotherUser", "user");

        ResultActions resultActions = mvc.perform(get("/api/reviews/"+ reviewInfo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(reviewInfo.getId()))
                .andExpect(jsonPath("$.data.rating").value(reviewInfo.getRating()))
                .andExpect(jsonPath("$.data.reviewType").value(reviewInfo.getReviewType().name()));
    }

    @Test
    void 리뷰리스트_조회() throws Exception {
        for(int i = 0; i < 15; i++){
            userSetup("anotherUser" + i);
            reviewSetup("user", "anotherUser" + i);
        }
        for(int i = 0; i < 5; i++){
            reviewSetup("anotherUser" + i, "user");
        }

        //userId 검색
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user");
        params.add("pageSize", "20");

        ResultActions resultActions = mvc.perform(get("/api/reviews")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(15));

        //writerId 검색
        params.clear();
        params.add("writerId","user");

        resultActions = mvc.perform(get("/api/reviews")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(5));

        //reviewType검색
        params.clear();
        params.add("reviewType", ReviewType.SELL.name());


        resultActions = mvc.perform(get("/api/reviews")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(params)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.list.length()").value(0));
    }

    @Test
    void 리뷰_저장() throws Exception{
        ReviewController.ReviewRequest reviewRequest = new ReviewController.ReviewRequest();
        reviewRequest.setUserId("user");
        reviewRequest.setRating(5);
        reviewRequest.setContent("친절해요");
        reviewRequest.setReviewType(ReviewType.SELL);

        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest))
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isCreated());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        Integer reviewId = ((Number) JsonPath.parse(content).read("$.data.id")).intValue();

        Review review = reviewService.findReview(reviewId);
        then(review.getUserId()).isEqualTo(reviewRequest.getUserId());
        then(review.getRating()).isEqualTo(reviewRequest.getRating());
        then(review.getContent()).isEqualTo(reviewRequest.getContent());
        then(review.getReviewType()).isEqualTo(reviewRequest.getReviewType());
    }

    @Test
    void 리뷰_수정() throws Exception {
        userSetup("anotherUser");
        Review reviewInfo = reviewSetup("anotherUser","user");

        ReviewController.ReviewUpdateRequest reviewRequest = new ReviewController.ReviewUpdateRequest();
        reviewRequest.setRating(1);
        reviewRequest.setContent("...");
        reviewRequest.setReviewType(ReviewType.SELL);

        ResultActions resultActions = mvc.perform(put("/api/reviews/"+reviewInfo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest))
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Review review = reviewService.findReview(reviewInfo.getId());
        then(review.getRating()).isEqualTo(reviewRequest.getRating());
        then(review.getContent()).isEqualTo(reviewRequest.getContent());
        then(review.getReviewType()).isEqualTo(reviewRequest.getReviewType());
    }

    @Test
    void 리뷰_삭제() throws Exception{
        userSetup("anotherUser");
        Review reviewInfo = reviewSetup("anotherUser", "user");

        ResultActions resultActions = mvc.perform(delete("/api/reviews/" + reviewInfo.getId())
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Review review = reviewService.findReview(reviewInfo.getId());
        then(review).isNull();
    }

    public Review reviewSetup(String userId, String writerId){
        Review reviewInfo = new Review();
        reviewInfo.setUserId(userId);
        reviewInfo.setWriterId(writerId);
        reviewInfo.setRating(5);
        reviewInfo.setContent("친절해요");
        reviewInfo.setReviewType(ReviewType.BUY);
        Integer reviewId = reviewService.addReview(reviewInfo);
        return reviewService.findReview(reviewId);
    }

    public void userSetup(String id){
        User userInfo = User.builder()
                .id(id)
                .password("passw0rd")
                .nickname("nickname")
                .addressCode("1111010100")
                .email("email@email.com")
                .role(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();
        userService.addUser(userInfo);
    }
}

