package com.example.server.utils;

import com.example.server.mapper.UserMapper;
import com.example.server.model.User;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public AdminInitializer(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String adminName = "admin";
        String rawPassword = "123456";

        try {
            // 检查admin用户是否已存在
            User existingAdmin = userMapper.selectById(adminName);

            if (existingAdmin == null) {
                // 如果不存在，创建新用户
                createAdminUser(adminName, rawPassword);
                System.out.println("✅ 管理员账号创建成功: " + adminName);
            } else {
                // 如果存在，确保能正常登录
                boolean valid = checkAdminLogin(adminName, rawPassword);

                if (!valid) {
                    // 如果密码不正确，重置为默认密码
                    updateAdminPassword(existingAdmin, rawPassword);
                    System.out.println("🔄 管理员密码重置为: " + rawPassword);
                } else {
                    System.out.println("ℹ️ 管理员账号已存在: " + adminName);
                }

                // 测试生成管理员token
                String token = JwtUtils.generateToken(adminName);
                System.out.println("🔑 管理员Token: " + token);
            }
        } catch (Exception e) {
            System.err.println("❌ 初始化管理员失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createAdminUser(String username, String password) {
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(userService.getPasswordEncoder().encode(password));
        userMapper.insert(admin);
    }

    private void updateAdminPassword(User admin, String newPassword) {
        admin.setPassword(userService.getPasswordEncoder().encode(newPassword));
        userMapper.updateById(admin);
    }

    private boolean checkAdminLogin(String username, String password) {
        try {
            return userService.verifyPassword(username, password);
        } catch (Exception e) {
            return false;
        }
    }
}