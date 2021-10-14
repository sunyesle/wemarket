package com.sys.market.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ItemImage {
    private Integer id;
    private Integer itemId;
    private String path;
    private Integer idx;
    private Date createDate;
}
