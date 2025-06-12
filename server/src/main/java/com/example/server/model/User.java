package com.example.server.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId // 表示name为主键
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password; // 存储加密后的哈希值
}