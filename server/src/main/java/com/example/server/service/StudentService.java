package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.Student;

public interface StudentService extends IService<Student> {
    boolean isPhoneUnique(String phone, Integer excludeId);
}