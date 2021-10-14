package com.sys.market.entity;

import com.sys.market.controller.api.UserController;
import com.sys.market.enums.UserStatus;
import io.jsonwebtoken.Claims;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String password;
    private String nickname;
    private String email;
    private String addressCode;
    private String bio;
    private String profileImage;
    private UserStatus status;
    private List<String> role;
    private Date createDate;
    private Date updateDate;
    private Date passwordChangeDate;
    private Integer signinFailCount;

    private String address;
    private Double rating;

    public User(Claims claims){
        this.id = claims.getSubject();
        this.role = (List<String>) claims.get("roles");
    }

    public UserInfo toUserInfo(){
        UserInfo dto = new UserInfo();
        dto.setId(this.id);
        dto.setNickname(this.nickname);
        dto.setAddress(this.address);
        dto.setStatus(this.status);
        dto.setRating(this.rating);
        return dto;
    }

    public UserController.UserResponse toUserResponse(){
        UserController.UserResponse dto = new UserController.UserResponse();
        dto.setId(this.id);
        dto.setNickname(this.nickname);
        dto.setBio(this.bio);
        dto.setProfileImage(this.profileImage);
        dto.setStatus(this.status);
        dto.setRole(this.role);
        dto.setCreateDate(this.createDate);
        dto.setUpdateDate(this.updateDate);
        dto.setAddress(this.address);
        dto.setRating(this.rating);
        return dto;
    }

    public UserController.UserDetailResponse toUserDetailResponse(){
        UserController.UserDetailResponse dto = new UserController.UserDetailResponse();
        dto.setId(this.id);
        dto.setNickname(this.nickname);
        dto.setEmail(this.email);
        dto.setBio(this.bio);
        dto.setProfileImage(this.profileImage);
        dto.setStatus(this.status);
        dto.setRole(this.role);
        dto.setCreateDate(this.createDate);
        dto.setUpdateDate(this.updateDate);
        dto.setPasswordChangeDate(this.passwordChangeDate);
        dto.setAddress(this.address);
        dto.setRating(this.rating);
        return dto;
    }
}
