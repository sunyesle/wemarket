package com.sys.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Follow {
    private String followerId;
    private String userId;
    private Date createDate;

    private String userNickname;
}
