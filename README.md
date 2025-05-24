# 数据库开发分支

## 1. 需求分析

## 2. E-R图绘制

## 3. 表结构定义

### **3.1 教师表（Teacher）**

**用途**：存储教师基本信息
​**​结构​**​：
`Teacher (teacher_id, name, gender, phone, address)`

- **主键**：`teacher_id`
- **CHECK约束**：`gender` 必须为 `男` 或 `女`

------

### **3.2 职业类型表（OccupationType）**

**用途**：定义家教服务的职业分类及课时费
​**​结构​**​：
`OccupationType (occupation_type_id, name, description, hourly_fee)`

- **主键**：`occupation_type_id`

------

### **3.3 职业登记表（OccupationRegistration）**

**用途**：记录具体的职业开班信息
​**​结构​**​：
`OccupationRegistration (occupation_id, occupation_type_id, address, contact_phone, status)`

- **主键**：`occupation_id`
- **外键**：`occupation_type_id` 关联 `OccupationType(occupation_type_id)`
- **状态字段**：`status`（如 `开放`/`关闭`）

------

### **3.4 职业作息表（OccupationSchedule）**

**用途**：记录每个职业的排班计划
​**​结构​**​：
`OccupationSchedule (schedule_id, occupation_id, teacher_id, date, start_time, end_time)`

- **主键**：`schedule_id`
- **外键**：
  - `occupation_id` 关联 `OccupationRegistration(occupation_id)`
  - `teacher_id` 关联 `Teacher(teacher_id)`

------

### **3.5 工资表（Salary）**

**用途**：记录教师工资发放明细
​**​结构​**​：
`Salary (salary_id, teacher_id, payment_date, total_hours, total_amount)`

- **主键**：`salary_id`
- **外键**：`teacher_id` 关联 `Teacher(teacher_id)`

------

### **3.6 收费表（Payment）**

**用途**：记录学生/家长缴费记录
​**​结构​**​：
`Payment (payment_id, occupation_id, payment_date, amount, payment_method, status)`

- **主键**：`payment_id`
- **外键**：`occupation_id` 关联 `OccupationRegistration(occupation_id)`

## 4. MySQL编写
