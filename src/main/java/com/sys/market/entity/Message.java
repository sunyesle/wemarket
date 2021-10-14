package com.sys.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Message {
    private Integer id;
    private String chatId;
    private String from;
    private String message;
    private Date createDate;
}
