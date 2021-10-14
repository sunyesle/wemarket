package com.sys.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Offer {
    private Integer id;
    private Integer itemId;
    private String sellerId;
    private String buyerId;
    private Integer price;
    private Boolean isAccepted; // null 값은 open 상태를 나타냄
    private Boolean isCompleted;
    private Date createDate;
    private Date updateDate;

    private String itemTitle;
    private UserInfo buyer;
}
