package com.example.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherHoursDTO {
    private Integer teacherId;
    private String teacherName;
    private BigDecimal totalHours;
}