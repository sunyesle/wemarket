package com.sys.market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Chat implements Serializable {
    private static final long serialVersionUID = -5358305399854037992L;
    private String id;
    private String to;
    private String toNickname;
    private Date createDate;
    private Date updateDate;
}
