package com.sys.market.entity;

import com.sys.market.enums.ReviewType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Review {
    private Integer id;
    private String userId;
    private String writerId;
    private Integer rating;
    private String content;
    private ReviewType reviewType;
    private Date createDate;
    private Date updateDate;

    private String writerNickname;
}
