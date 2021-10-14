package com.sys.market.mapper;

import com.sys.market.entity.Chat;
import com.sys.market.dto.criteria.ChatCriteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {
    Chat selectChat(String chatId, String userId);

    List<Chat> selectChatList(ChatCriteria criteria);

    int selectChatCount(String userId);

    void insertChat(String chatRoomId);

    void insertChatUser(String chatId, String userId);

    void updateChat(String chatRoomId);

    String selectChatId(String userId1, String userId2);

    boolean selectChatExist(String chatId, String userId);
}
