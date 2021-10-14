package com.sys.market.mapper;

import com.sys.market.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> selectMessageListByChatId(String chatId, Integer limit);

    void insertMessage(Message messageInfo);
}
