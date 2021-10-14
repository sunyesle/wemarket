package com.sys.market.websocket;

import com.sys.market.advice.exception.CAccessDeniedException;
import com.sys.market.config.security.JwtTokenProvider;
import com.sys.market.entity.Message;
import com.sys.market.service.ChatService;
import com.sys.market.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

// publisher
@Controller
@RequiredArgsConstructor
public class MessagePubController {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ChatService chatService;
    private final MessageService messageService;
    private final ChannelTopic channelTopic;
    private final JwtTokenProvider jwtTokenProvider;

    // pub/chat/message
    @MessageMapping("/chat/message")
    public void message(Message message, @Header("token")String token){
        String userId = jwtTokenProvider.getUserPk(token);
        boolean isExist = chatService.findChatExist(message.getChatId(), userId);

        // 자신이 속해있는 채팅방이 아닐경우 접근 거부
        if(!isExist){
            throw new CAccessDeniedException();
        }else{
            message.setFrom(userId);
        }
        // redisTemplate을 통해 channelTopic으로 메시지를 발행한다.
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        // 채팅 내용을 저장한다.
        messageService.saveMessage(message);
    }
}
