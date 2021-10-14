package com.sys.market.websocket;

import com.sys.market.advice.exception.CAccessDeniedException;
import com.sys.market.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 헤더의 accessToken 확인
        if((StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand())
                && !jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"))){
            throw new CAccessDeniedException();
        }

        return message;
    }
}
