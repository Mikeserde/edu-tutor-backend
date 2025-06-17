package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.UserMapper;
import com.example.server.model.User;
import com.example.server.utils.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        if (this.getById(user.getUsername()) != null) {
            throw new UserException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        return user;
    }

    @Override
    public boolean verifyPassword(String name, String password) {
        User user = this.getById(name);
        if (user == null) {
            throw new UserException("用户不存在");
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean updatePassword(String name,String newPassword) {
        User user = this.getById(name);
        if (user == null) {
            throw new UserException("用户不存在");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        return this.updateById(user);
    }
}