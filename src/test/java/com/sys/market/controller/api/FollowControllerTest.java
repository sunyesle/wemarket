package com.sys.market.controller.api;


import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.User;
import com.sys.market.enums.UserRole;
import com.sys.market.service.FollowService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerTest extends BaseIntegrationTest {
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    @Test
    void 팔로우리스트_조회() throws Exception{
        userSetup("user2");
        userSetup("user3");

        followService.addFollow("user2", "user");
        followService.addFollow("user3", "user");
        followService.addFollow("user", "user2");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "user");

        ResultActions resultActions = mvc.perform(get("/api/followers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(2));

        params.clear();
        params.add("followerId", "user");

        resultActions = mvc.perform(get("/api/followers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1));

        params.clear();
        params.add("userId", "noContent");

        resultActions = mvc.perform(get("/api/followers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params))
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.list.length()").value(0));

    }

    @Test
    void 팔로우_추가() throws Exception{
        String userId = "anotherUser";
        userSetup(userId);

        FollowController.FollowRequest followRequest = new FollowController.FollowRequest();
        followRequest.setUserId(userId);

        ResultActions resultActions = mvc.perform(post("/api/followers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(followRequest))
                .cookie(accessCookie))
                .andDo(print());

        resultActions.andExpect(status().isCreated());
        then(followService.followExists("user", userId)).isTrue();
    }

    @Test
    void 팔로우_중복추가_실패() throws Exception{
        String userId = "anotherUser";
        userSetup(userId);
        followService.addFollow("user", userId);

        ResultActions resultActions = mvc.perform(post("/api/followers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userId)
                .cookie(accessCookie))
                .andDo(print());

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 팔로우_삭제() throws Exception{
        String userId = "anotherUser";
        userSetup(userId);
        followService.addFollow("user", userId);

        ResultActions resultActions = mvc.perform(delete("/api/followers/" + userId)
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());
        then(followService.followExists("user", userId)).isFalse();
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
