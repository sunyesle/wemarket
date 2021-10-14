package com.sys.market.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.market.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;

    public void sendMessage(String publishMessage) {
        try {
            // Message 객체로 맵핑
            Message chatMessage = objectMapper.readValue(publishMessage, Message.class);
            // 구독자에게 메시지 발행. chatId로 분기처리. publish
            messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getChatId(), chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
