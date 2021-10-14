package com.sys.market.controller.api;


import com.sys.market.BaseIntegrationTest;
import com.sys.market.entity.Message;
import com.sys.market.entity.User;
import com.sys.market.enums.UserRole;
import com.sys.market.service.ChatService;
import com.sys.market.service.MessageService;
import com.sys.market.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest extends BaseIntegrationTest {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Test
    void 채팅방_조회() throws Exception{
        userSetup("anotherUser");
        String chatId = chatService.createChatRoom("user", "anotherUser");

        ResultActions resultActions = mvc.perform(get("/api/chats/" + "anotherUser" + "/id")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(chatId));

        resultActions = mvc.perform(get("/api/chats/" + "notFound" + "/id")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 채팅방_리스트_조회() throws Exception{
        userSetup("anotherUser");
        for(int i = 0; i < 5; i++){
            userSetup("anotherUser" + i);
            chatService.createChatRoom("user", "anotherUser" + i);
        }

        // 자신의 모든 채팅방
        ResultActions resultActions = mvc.perform(get("/api/chats/")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(5));
    }

    @Test
    void 채팅방_메시지_조회() throws Exception{
        userSetup("anotherUser");
        String chatId = chatService.createChatRoom("user", "anotherUser");
        for(int i = 0; i < 15; i++){
            Message message = new Message();
            message.setMessage("안녕" + (i+1));
            message.setChatId(chatId);
            message.setFrom("user");
            messageService.saveMessage(message);
        }

        ResultActions resultActions = mvc.perform(get("/api/chats/" + chatId + "/messages")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(10))
                .andExpect(jsonPath("$.list[0].message").value("안녕1"));

        resultActions = mvc.perform(get("/api/chats/" + chatId + "/messages")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("limit", "12")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(12))
                .andExpect(jsonPath("$.list[0].message").value("안녕1"));
    }

    @Test
    void 채팅방_메시지_조회_실패() throws Exception{
        // 존재하지 않는 채팅방
        ResultActions resultActions = mvc.perform(get("/api/chats/" + "notFound" + "/messages")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isForbidden());

        userSetup("anotherUser1");
        userSetup("anotherUser2");
        // 권한없는 유저(해당 채팅방에 돌어가있지 않은 유저)가 접근
        String chatId = chatService.createChatRoom("anotherUser1", "anotherUser2");

        resultActions = mvc.perform(get("/api/chats/" + chatId + "/messages")
                .cookie(accessCookie)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isForbidden());
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

