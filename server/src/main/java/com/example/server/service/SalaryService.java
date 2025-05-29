package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.Salary;

public interface SalaryService extends IService<Salary> {
    boolean existsByTeacherAndMonth(Integer teacherId, String month);
    Salary getByCompositeKey(Integer teacherId, String month);
}