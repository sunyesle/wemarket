package com.sys.market.mapper;

import com.sys.market.entity.User;

import java.util.List;

public interface UserMapper {
    User selectById(String id);

    int selectCountById(String id);

    void insertUser(User userInfo);

    void insertUserRole(String userId, List<String> roles);

    void updateUser(User userInfo);

    void updateUserStatus(String id, String status);

    void updateUserPassword(String id, String password);

    void updateUserProfileImage(String id, String profileImage);

    void deleteUserRole(String userId);
}
