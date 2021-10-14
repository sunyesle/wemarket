package com.sys.market.entity;

import com.sys.market.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String id;
    private String nickname;
    private String address;
    private UserStatus status;
    private Double rating;
}
