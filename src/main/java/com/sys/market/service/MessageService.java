package com.sys.market.service;

import com.sys.market.entity.Message;
import com.sys.market.mapper.ChatMapper;
import com.sys.market.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    ChatMapper chatMapper;
    @Autowired
    MessageMapper messageMapper;

    public List<Message> findMessageListByChatId(String chatId, Integer limit){
        return messageMapper.selectMessageListByChatId(chatId, limit);
    }

    @Transactional
    public void saveMessage(Message messageInfo){
        messageMapper.insertMessage(messageInfo);
        chatMapper.updateChat(messageInfo.getChatId());
    }
}
