package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface UserService extends IService<User> {
    User register(User user);
    boolean verifyPassword(String name, String password);
    boolean updatePassword(String name,String newPassword);
    BCryptPasswordEncoder getPasswordEncoder();
}