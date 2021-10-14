package com.sys.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Wishlist {
    private String userId;
    private long itemId;
    private Date createDate;
}
