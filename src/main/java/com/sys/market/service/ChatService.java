package com.sys.market.service;

import com.sys.market.entity.Chat;
import com.sys.market.dto.criteria.ChatCriteria;
import com.sys.market.mapper.ChatMapper;
import com.sys.market.util.MyUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 1:1 채팅
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;

    public List<Chat> findChatList(ChatCriteria criteria){
        return chatMapper.selectChatList(criteria);
    }

    public int findChatCount(String userId){
        return chatMapper.selectChatCount(userId);
    }

    @Transactional
    public String createChatRoom(String currentUserId, String userId){
        String chatRoomId = MyUUID.getRandomString();
        chatMapper.insertChat(chatRoomId);
        chatMapper.insertChatUser(chatRoomId, currentUserId);
        chatMapper.insertChatUser(chatRoomId, userId);

        return chatRoomId;
    }

    // 두 유저의 채팅방이 존재하면 채팅방의 id 반환, 존재하지 않을경우 null 반환
    public String findChatId(String userId1, String userId2) {
        return chatMapper.selectChatId(userId1, userId2);
    }

    public boolean findChatExist(String chatId, String userId) {
        return chatMapper.selectChatExist(chatId, userId);
    }
}
