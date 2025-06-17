package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.model.User;
import com.example.server.service.UserService;
import com.example.server.utils.JwtUtils;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "注册新用户")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return Result.ok()
                    .message("用户注册成功")
                    .data("user", registeredUser);
        } catch (Exception e) {
            return Result.error()
                    .message(e.getMessage());
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        try {
            // 验证用户凭证
            if (userService.verifyPassword(user.getUsername(), user.getPassword())) {
                // 生成JWT令牌
                String token = JwtUtils.generateToken(user.getUsername());
                return Result.ok()
                        .message("登录成功")
                        .data("token", token);
            } else {
                return Result.error()
                        .message("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error()
                    .message(e.getMessage());
        }
    }


    @Operation(summary = "获取当前登录的用户信息")
    @GetMapping("/info")
    public Result info(@RequestParam String token) {
        try {
            // 从JWT令牌中解析用户名
            String username = JwtUtils.getClaimsByToken(token).getSubject();

            // 从数据库获取用户详细信息
            User user = userService.getById(username);
            if (user == null) {
                return Result.error()
                        .message("用户不存在");
            }

            // 构建用户信息响应
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", username);
            userInfo.put("roles", new String[]{"admin"});
            userInfo.put("introduction", "系统管理员");
            userInfo.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

            return Result.ok()
                    .message("用户信息获取成功")
                    .data(userInfo);
        } catch (Exception e) {
            return Result.error()
                    .message("无效的令牌: " + e.getMessage())
                    .code(401);
        }
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok()
                .message("注销成功");
    }

    @Operation(summary = "更新密码")
    @PutMapping("/{name}/password")
    public Result updatePassword(
            @PathVariable String name,
            @RequestParam String newPassword) {
        try {
            boolean result = userService.updatePassword(name,newPassword);
            return Result.ok()
                    .message("密码更新成功")
                    .data("updated", result);
        } catch (Exception e) {
            return Result.error()
                    .message(e.getMessage());
        }
    }


    @Operation(summary = "删除用户")
    @DeleteMapping("/{name}")
    public Result deleteUser(@PathVariable String name) {
        try {
            if (userService.getById(name) == null) {
                return Result.error()
                        .message("用户不存在")
                        .code(404);
            }
            boolean result = userService.removeById(name);
            return Result.ok()
                    .message("用户删除成功")
                    .data("deleted", result);
        } catch (Exception e) {
            return Result.error()
                    .message("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分页查询用户列表")
    @GetMapping
    public Result getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search) {

        try {
            // 构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();

            // 过滤掉admin账户
            queryWrapper.ne("username", "admin");

            // 添加搜索过滤
            if (search != null && !search.isEmpty()) {
                queryWrapper.like("username", search);
            }

            // 创建分页对象
            Page<User> pageQuery = new Page<>(page, limit);

            // 执行分页查询
            IPage<User> userPage = userService.page(pageQuery, queryWrapper);

            // 过滤用户信息
            List<Map<String, Object>> safeList = new ArrayList<>();
            for (User user : userPage.getRecords()) {
                Map<String, Object> safeUser = new HashMap<>();
                safeUser.put("username", user.getUsername());
                safeList.add(safeUser);
            }

            // 构建分页响应
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", safeList);
            pageData.put("total", userPage.getTotal());
            pageData.put("pages", userPage.getPages());
            pageData.put("current", userPage.getCurrent());
            pageData.put("size", userPage.getSize());

            return Result.ok()
                    .message("用户列表获取成功")
                    .data("page", pageData);
        } catch (Exception e) {
            return Result.error()
                    .message("获取用户列表失败: " + e.getMessage())
                    .code(500);
        }
    }
}
