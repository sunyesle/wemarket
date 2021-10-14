package com.sys.market.controller.api;


import com.sys.market.BaseIntegrationTest;
import com.sys.market.config.security.JwtTokenProvider;
import com.sys.market.entity.User;
import com.sys.market.enums.UserRole;
import com.sys.market.enums.UserStatus;
import com.sys.market.service.AddressService;
import com.sys.market.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressService addressService;

    @Test
    void 회원가입() throws Exception{
        //given
        UserController.UserRequest userRequest = new UserController.UserRequest();
        userRequest.setId("testId");
        userRequest.setPassword("passw0rd");
        userRequest.setAddressCode("1111010100");
        userRequest.setEmail("testEmail@sunys.xyz");
        userRequest.setNickname("testUser");

        //when
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isCreated());

        String requestAddressName = addressService.findAddress(userRequest.getAddressCode()).getName();
        User user = userService.findUser(userRequest.getId());

        then(userRequest.getId()).isEqualTo(user.getId());
        then(true).isEqualTo(passwordEncoder.matches(userRequest.getPassword(), user.getPassword()));
        then(requestAddressName).isEqualTo(user.getAddress());
        then(userRequest.getEmail()).isEqualTo(user.getEmail());
        then(userRequest.getNickname()).isEqualTo(user.getNickname());
        then(Collections.singletonList(UserRole.ROLE_UNVERIFIED.name())).containsAll(user.getRole());
    }

    @Test
    void 로그인_로그아웃() throws Exception{
        //로그인
        //given
        UserController.UserRequest userRequest = new UserController.UserRequest();
        userRequest.setId("testId");
        userRequest.setPassword("passw0rd");
        userRequest.setAddressCode("1111010100");
        userRequest.setEmail("testEmail@sunys.xyz");
        userRequest.setNickname("testUser");
        userSetup(userRequest);

        UserController.UserLoginRequest loginRequest = new UserController.UserLoginRequest();
        loginRequest.setId(userRequest.getId());
        loginRequest.setPassword(userRequest.getPassword());

        //when
        ResultActions resultActions = mvc.perform(post("/api/users/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(cookie().exists(JwtTokenProvider.ACCESS_TOKEN_NAME))
                .andExpect(cookie().exists(JwtTokenProvider.REFRESH_TOKEN_NAME));


        //로그아웃
        //given
        MvcResult mvcResult = resultActions.andReturn();
        String accessToken = mvcResult.getResponse().getCookie(JwtTokenProvider.ACCESS_TOKEN_NAME).getValue();
        String refreshToken = mvcResult.getResponse().getCookie(JwtTokenProvider.REFRESH_TOKEN_NAME).getValue();

        //when
        ResultActions resultActions2 = mvc.perform(post("/api/users/signOut")
                .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken))
                .cookie(new Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshToken))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions2
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("accessToken",0))
                .andExpect(cookie().maxAge("refreshToken",0));
    }

    @Test
    void 유저_조회() throws Exception{
        //given
        UserController.UserRequest userRequest = new UserController.UserRequest();
        userRequest.setId("testId");
        userRequest.setPassword("passw0rd");
        userRequest.setAddressCode("1111010100");
        userRequest.setEmail("testEmail@sunys.xyz");
        userRequest.setNickname("testUser");
        userSetup(userRequest);

        //when
        ResultActions resultActions = mvc.perform(get("/api/users/"+userRequest.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userRequest.getId()))
                .andExpect(jsonPath("$.data.address").exists())
                .andExpect(jsonPath("$.data.rating").value(0));
    }

    @Test
    void 유저_수정_삭제() throws Exception{
        //현재 사용자만 수정, 삭제가 가능하기 때문에 로그인 먼저 수행
        UserController.UserRequest userRequest = new UserController.UserRequest();
        userRequest.setId("testId");
        userRequest.setPassword("passw0rd");
        userRequest.setAddressCode("1111010100");
        userRequest.setEmail("testEmail@sunys.xyz");
        userRequest.setNickname("testUser");
        userSetup(userRequest);

        UserController.UserLoginRequest loginRequest = new UserController.UserLoginRequest();
        loginRequest.setId(userRequest.getId());
        loginRequest.setPassword(userRequest.getPassword());

        MvcResult mvcResult = mvc.perform(post("/api/users/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String accessToken = mvcResult.getResponse().getCookie(JwtTokenProvider.ACCESS_TOKEN_NAME).getValue();
        String refreshToken = mvcResult.getResponse().getCookie(JwtTokenProvider.REFRESH_TOKEN_NAME).getValue();

        //수정
        //given
        UserController.UserUpdateRequest userUpdateRequest = new UserController.UserUpdateRequest();
        userUpdateRequest.setNickname("update");
        userUpdateRequest.setAddressCode("1111010200");
        userUpdateRequest.setBio("hello");

        //when
        ResultActions resultActions = mvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken))
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());

        String updateRequestAddressName = addressService.findAddress(userUpdateRequest.getAddressCode()).getName();
        User user = userService.findUser(userRequest.getId());

        then(userUpdateRequest.getNickname()).isEqualTo(user.getNickname());
        then(updateRequestAddressName).isEqualTo(user.getAddress());
        then(userUpdateRequest.getBio()).isEqualTo(user.getBio());


        //비밀번호 수정
        //given
        UserController.UserPasswordRequest userPasswordRequest = new UserController.UserPasswordRequest();
        userPasswordRequest.setOldPassword(userRequest.getPassword());
        userPasswordRequest.setPassword("updatePassw0rd");

        //when
        ResultActions resultActions2 = mvc.perform(put("/api/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken))
                .content(objectMapper.writeValueAsString(userPasswordRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions2
                .andExpect(status().isOk());

        user = userService.findUser(userRequest.getId());
        then(true).isEqualTo(passwordEncoder.matches(userPasswordRequest.getPassword(), user.getPassword()));


        //삭제
        //when
        UserController.UserDeleteRequest userDeleteRequest = new UserController.UserDeleteRequest();
        userDeleteRequest.setPassword(userPasswordRequest.getPassword());

        ResultActions resultActions3 = mvc.perform(delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken))
                .content(objectMapper.writeValueAsString(userDeleteRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions3
                .andExpect(status().isOk());

        user = userService.findUser(userRequest.getId());

        then(user.getStatus()).isEqualTo(UserStatus.DELETED);
    }

    public void userSetup(UserController.UserRequest userRequest){
        User userInfo = User.builder()
                .id(userRequest.getId())
                .password(userRequest.getPassword())
                .nickname(userRequest.getNickname())
                .addressCode(userRequest.getAddressCode())
                .email(userRequest.getEmail())
                .role(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();
        userService.addUser(userInfo);
    }
}
