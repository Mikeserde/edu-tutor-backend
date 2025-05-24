# 数据库开发分支

## 1. 需求分析

## 2. E-R图绘制

## 3. 表结构定义
**表名和字段名全部采用大驼峰方式命名，确保和后端代码的命名一致，方便数据操控**

### 3.1. 教师表 (Teacher)

| 字段名    | 数据类型    | 主键/外键 | 允许空值 | 默认值 | 说明         | 约束/备注                       |
| --------- | ----------- | --------- | -------- | ------ | ------------ | ------------------------------- |
| TeacherId | INT         | 主键      | 否       | 自增   | 教师唯一标识 | 自增主键                        |
| Name      | VARCHAR(50) | -         | 否       | -      | 教师姓名     |                                 |
| Gender    | VARCHAR(2)  | -         | 否       | -      | 性别         | `CHECK (Gender IN ('男','女'))` |
| Phone     | VARCHAR(20) | -         | 否       | -      | 联系电话     | 唯一约束                        |

------

### 3.2. 职业类型表 (OccupationType)

| 字段名           | 数据类型      | 主键/外键 | 允许空值 | 默认值 | 说明                           | 约束/备注               |
| ---------------- | ------------- | --------- | -------- | ------ | ------------------------------ | ----------------------- |
| OccupationTypeId | VARCHAR(10)   | 主键      | 否       | -      | 职业类型唯一标识（如`OCC001`） |                         |
| Name             | VARCHAR(50)   | -         | 否       | -      | 职业类型名称（如“数学辅导”）   | 唯一约束，不可重复      |
| HourlyFee        | DECIMAL(10,2) | -         | 否       | -      | 每小时课时费                   | `CHECK (HourlyFee > 0)` |

------

### 3.3. 职业登记表 (OccupationRegistration)

| 字段名           | 数据类型     | 主键/外键 | 允许空值 | 默认值 | 说明                              | 约束/备注                    |
| ---------------- | ------------ | --------- | -------- | ------ | --------------------------------- | ---------------------------- |
| OccupationId     | VARCHAR(10)  | 主键      | 否       | -      | 职业登记唯一标识（如`OCCREG001`） |                              |
| OccupationTypeId | VARCHAR(10)  | 外键      | 否       | -      | 关联的职业类型                    | 外键引用 `OccupationType` 表 |
| Address          | VARCHAR(100) | -         | 否       | -      | 上课地址                          |                              |
| ContactPhone     | VARCHAR(20)  | -         | 否       | -      | 联系电话                          |                              |

------

### 3.4. 职业作息表 (OccupationSchedule)

| 字段名       | 数据类型    | 主键/外键 | 允许空值 | 默认值 | 说明                       | 约束/备注                        |
| ------------ | ----------- | --------- | -------- | ------ | -------------------------- | -------------------------------- |
| ScheduleId   | INT         | 主键      | 否       | 自增   | 作息唯一标识               | 自增主键                         |
| OccupationId | VARCHAR(10) | 外键      | 否       | -      | 关联的职业登记             | 外键引用OccupationRegistration表 |
| TeacherId    | INT         | 外键      | 否       | -      | 关联的教师                 | 外键引用Teacher表                |
| Date         | DATE        | -         | 否       | -      | 上课日期（如`2023-10-01`） |                                  |
| StartTime    | TIME        | -         | 否       | -      | 开始时间（如`09:00`）      |                                  |
| EndTime      | TIME        | -         | 否       | -      | 结束时间（如`11:00`）      |                                  |

------

### 3.5. 工资表 (Salary)

| 字段名      | 数据类型      | 主键/外键 | 允许空值 | 默认值 | 说明                           | 约束/备注              |
| ----------- | ------------- | --------- | -------- | ------ | ------------------------------ | ---------------------- |
| SalaryId    | INT           | 主键      | 否       | 自增   | 工资单唯一标识                 | 自增主键               |
| TeacherId   | INT           | 外键      | 否       | -      | 关联的教师                     | 外键引用Teacher表      |
| PaymentDate | DATE          | -         | 否       | -      | 工资发放日期（如`2023-10-05`） |                        |
| TotalHours  | DECIMAL(10,2) | -         | 否       | -      | 总授课时长（小时）             | 通过存储过程计算       |
| TotalAmount | DECIMAL(10,2) | -         | 否       | -      | 应发工资总额                   | TotalHours × HourlyFee |

------

### 3.6. 收费表 (Payment)

| 字段名        | 数据类型      | 主键/外键 | 允许空值 | 默认值 | 说明                         | 约束/备注                        |
| ------------- | ------------- | --------- | -------- | ------ | ---------------------------- | -------------------------------- |
| PaymentId     | INT           | 主键      | 否       | 自增   | 收费记录唯一标识             | 自增主键                         |
| OccupationId  | VARCHAR(10)   | 外键      | 否       | -      | 关联的职业登记               | 外键引用OccupationRegistration表 |
| PaymentDate   | DATE          | -         | 否       | -      | 缴费日期（如`2023-10-02`）   |                                  |
| Amount        | DECIMAL(10,2) | -         | 否       | -      | 缴费金额                     | 必须大于0                        |
| PaymentMethod | VARCHAR(20)   | -         | 否       | -      | 支付方式（如“支付宝”“现金”） |                                  |
| Status        | VARCHAR(10)   | -         | 否       | 未支付 | 支付状态（已支付/未支付）    | ENUM('已支付','未支付')          |

------
## 4. MySQL编写
- 创建数据库

```mysql
CREATE DATABASE TutorServiceManagement;
USE TutorServiceManagement;
```
- 建表

1. 教师表 (Teacher)

```mysql
CREATE TABLE Teacher (
    TeacherId INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Gender VARCHAR(2) NOT NULL CHECK (Gender IN ('男', '女')),
    Phone VARCHAR(20) NOT NULL UNIQUE
);
```
2. 职业类型表 (OccupationType)

```mysql
CREATE TABLE OccupationType (
    OccupationTypeId VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL UNIQUE,
    HourlyFee DECIMAL(10,2) NOT NULL CHECK (HourlyFee > 0)
);
```
3. 职业登记表 (OccupationRegistration)

```mysql
CREATE TABLE OccupationRegistration (
    OccupationId VARCHAR(10) PRIMARY KEY,
    OccupationTypeId VARCHAR(10) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    ContactPhone VARCHAR(20) NOT NULL,
    FOREIGN KEY (OccupationTypeId) REFERENCES OccupationType(OccupationTypeId)
);
```
4. 职业作息表 (OccupationSchedule)

```mysql
CREATE TABLE OccupationSchedule (
    ScheduleId INT PRIMARY KEY AUTO_INCREMENT,
    OccupationId VARCHAR(10) NOT NULL,
    TeacherId INT NOT NULL,
    ScheduleDate DATE NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    ActualHours DECIMAL(5,2) COMMENT '实际授课小时数',
    Status VARCHAR(20) DEFAULT '已安排' CHECK (Status IN ('已安排', '已完成', '已取消')),
    Remarks VARCHAR(200),
    FOREIGN KEY (OccupationId) REFERENCES OccupationRegistration(OccupationId),
    FOREIGN KEY (TeacherId) REFERENCES Teacher(TeacherId),
    CHECK (EndTime > StartTime)
);
```

5. 工资表 (Salary)

```mysql
CREATE TABLE Salary (
    SalaryId INT PRIMARY KEY AUTO_INCREMENT,
    TeacherId INT NOT NULL,
    SalaryMonth CHAR(6) NOT NULL COMMENT '格式:YYYYMM',
    TotalHours DECIMAL(10,2) NOT NULL,
    BaseSalary DECIMAL(10,2) NOT NULL,
    Bonus DECIMAL(10,2) DEFAULT 0,
    Deduction DECIMAL(10,2) DEFAULT 0,
    Tax DECIMAL(10,2) DEFAULT 0,
    NetSalary DECIMAL(10,2) NOT NULL,
    PaymentStatus TINYINT DEFAULT 0 COMMENT '0-未支付, 1-已支付',
    PaymentDate DATETIME,
    Remarks VARCHAR(200),
    FOREIGN KEY (TeacherId) REFERENCES Teacher(TeacherId)
);
```
6. 收费表 (Payment)

```mysql
CREATE TABLE Payment (
    PaymentId INT PRIMARY KEY AUTO_INCREMENT,
    OccupationId VARCHAR(10) NOT NULL,
    PaymentNo VARCHAR(20) NOT NULL UNIQUE,
    Amount DECIMAL(10,2) NOT NULL CHECK (Amount > 0),
    PaymentDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    PaymentMethod VARCHAR(20) NOT NULL,
    InvoiceNo VARCHAR(50),
    Status VARCHAR(10) NOT NULL DEFAULT '未支付' CHECK (Status IN ('已支付', '未支付', '已退款')),
    Remarks VARCHAR(200),
    FOREIGN KEY (OccupationId) REFERENCES OccupationRegistration(OccupationId)
);
```
​	7.创建索引提高查询性能
```mysql
CREATE INDEX idx_teacher_phone ON Teacher(Phone);
CREATE INDEX idx_occupation_type ON OccupationType(Name);
CREATE INDEX idx_occupation_reg ON OccupationRegistration(OccupationTypeId, Status);
CREATE INDEX idx_schedule_date ON OccupationSchedule(ScheduleDate, TeacherId);
CREATE INDEX idx_salary_month ON Salary(SalaryMonth, TeacherId);
CREATE INDEX idx_payment_date ON Payment(PaymentDate, Status);
```