package com.sys.market.service;

import com.sys.market.advice.exception.CSigninFailedException;
import com.sys.market.advice.exception.CUserExistsException;
import com.sys.market.entity.User;
import com.sys.market.enums.UserRole;
import com.sys.market.enums.UserStatus;
import com.sys.market.mapper.UserMapper;
import com.sys.market.util.file.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileUtil fileUtil;

    public User signin(String id, String password) {
        User user = userMapper.selectById(id);

        if(user == null || user.getStatus() != UserStatus.ACTIVE || !passwordEncoder.matches(password, user.getPassword())){
            throw new CSigninFailedException();
        }
        return user;
    }

    public User findUser(String id){
        return userMapper.selectById(id);
    }

    public boolean idExists(String id) {
        return userMapper.selectCountById(id) == 1;
    }

    @Transactional
    public void addUser(User userInfo) {
        if(idExists(userInfo.getId())) throw new CUserExistsException();

        String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(encodedPassword);

        userMapper.insertUser(userInfo);
        userMapper.insertUserRole(userInfo.getId(), userInfo.getRole());
    }

    public void modifyUser(User userInfo) {
        userMapper.updateUser(userInfo);
    }

    @Transactional
    public void modifyUserRole(String userId, List<String> role){
        userMapper.deleteUserRole(userId);
        userMapper.insertUserRole(userId, role);
    }

    @Transactional
    public void modifyUserPassword(String id, String oldPassword, String password) {
        signin(id, oldPassword);
        userMapper.updateUserPassword(id, passwordEncoder.encode(password));
    }

    @Transactional
    public void modifyUserEmail(String id, String email){
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        userMapper.updateUser(user);

        userMapper.deleteUserRole(id);
        userMapper.insertUserRole(id, Collections.singletonList(UserRole.ROLE_UNVERIFIED.name()));
    }

    @Transactional
    public void modifyUserProfileImage(String id, MultipartFile file){
        String path = null;
        // 기존이미지 삭제
        String oldPath = userMapper.selectById(id).getProfileImage();
        if(oldPath != null){
            fileUtil.deleteImage(oldPath);
        }

        if(file != null && file.getSize() > 0){
            path = fileUtil.uploadProfileImage(file, id);
        }
        userMapper.updateUserProfileImage(id, path);
    }

    public void removeUser(User userInfo) {
        userInfo.setNickname("탈퇴한 회원");
        userInfo.setEmail("");
        userInfo.setBio("");
        userMapper.updateUser(userInfo);
        userMapper.updateUserStatus(userInfo.getId(), UserStatus.DELETED.name());
    }
}
