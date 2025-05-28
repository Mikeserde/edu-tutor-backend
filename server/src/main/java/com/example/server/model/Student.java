package com.example.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@TableName("Student")
public class Student {

    @TableId(value = "StudentId", type = IdType.AUTO)
    private Integer studentId;

    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @TableField("Name")
    private String name;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "男|女", message = "性别只能是男或女")
    @TableField("Gender")
    private String gender;

    @NotBlank(message = "地址不能为空")
    @Size(max = 20, message = "地址长度不能超过20个字符")
    @TableField("Address")
    private String address;

    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    @TableField("ContactPhone")
    private String contactPhone;
}
