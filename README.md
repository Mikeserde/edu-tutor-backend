# 家教服务数据库管理系统

## 1.总体要求

​	分组：按课题组方式开发，每组 2-3 人，组长一名，角色划分、任务分配。

​	每组学生（2-3 人）从下面 48 个题目中任选一个作为课程设计，调查分析一个具体的 或模拟的实例。同一个班中的不同组同学不允许选择相同的题目。选好题目后发给班长或学 习汇总，产生选题冲突时， 由班长/委员协调解决。

​	每组独立完成课程设计要求的相关内容，包括：

1. 通过调查研究和运用 Internet ，收集和调查有关资料、最新技术信息。
2. 基本掌握撰写设计报告的基本步骤和写作方法。
3. 根据课题的要求基本理解和掌握 E-R 图的设计方法和关系模式的转换。
4. 根据 E-R 图生成数据库表。
5. 数据库完整性、安全性保证措施。
6. 数据库的物理设计。
7. 数据库实施维护计划。
8. 应用系统的设计和实现。

数据库平台：MySQL

开发平台：J2EE 

软件架构：B/S

## 2.具体要求

​	课题组选题后，进入需求分析阶段。通过网上查阅、内部讨论、社会调查等手段，明确课题需求，包括：数据需求、功能需求及其他需求。需求分析由课题组共同完成！

​	在系统设计阶段， 由课题组长进行任务分配。课题的所有成员均需完成一定的设计任 务。

​	编程实现阶段，每一位成员均需完成一定的程序量。课题组中若存在没有参与编程的 成员，除了该生得成绩为不及格外，组长的成绩也将降档处理。

## 3.课题验收

在规定的时间内进行课题验收，验收按课题组为单位进行。课题组全体成员验收时必 须参加，特殊情况需作书面说明。

课题验收的主要参考指标（含要求）：

-  需求分析（课题组成员通力合作，力求需求分析的全面、有效！）
-  数据库设计（数据涵盖系统的数据需求，逻辑结构均达到 3NF，性能优化自己思考）
-  数据完整性（分析关键表中的关键数据，制定自己的完整性约束规则！）
-  安全性（必须具有基本的用户及其权限的控制手段！数据库的备份和恢复）
-  视图（从操作的方便性、数据的安全性、数据的逻辑独立性等方面综合考虑！）
-  触发器（根据对应用的理解，是否采用触发器？带来的好处？）
-  存储过程（在充分理解系统业务处理的前提下，作出自己的判断，要运用得当！）
-  索引（需结合数据量的估算，做出合理的设计）
-  系统功能（在需求分析的基础上尽可能细致一些！）
-  用户界面友好性（多从用户的角度考虑！）

## 4. 课题

**某学校家教服务管理系统**

基本功能要求：

1. 实现教师信息、职业类型管理；
2. 实现职业登记（职业号、名称、地址、电话等）；
3. 实现职业作息登记（职业号、 日期、开始时间、结束时间、教师）；
4. 实现工资管理和收费管理；
5. 创建存储过程统计指定日期范围内各的时刻时间总和；
6. 创建存储过程统计各种职业的需求次数；
7. 创建 check 约束限制教师性别必须输入‘男 ’或‘女 ’；
8. 具有数据备份和数据恢复功能。

数据库表结构：
1. 教师信息表(Teacher)
功能：实现教师信息管理

字段：

teacher_id INT PRIMARY KEY IDENTITY(1,1) - 教师ID，主键，自增

name NVARCHAR(50) NOT NULL - 教师姓名

gender CHAR(2) NOT NULL CHECK (gender IN ('男', '女')) - 性别，有CHECK约束

birth_date DATE - 出生日期

phone VARCHAR (20) NOT NULL -电话


2. 职业类型表(ProfessionType)
功能：实现职业类型管理

字段：

type_id INT PRIMARY KEY IDENTITY(1,1) - 类型ID，主键，自增

type_name NVARCHAR(50) NOT NULL - 类型名称

description NVARCHAR(200) - 类型描述



3. 职业登记表(Profession)
功能：实现职业登记管理

字段：

profession_id INT PRIMARY KEY IDENTITY(1,1) -  - 职业名称
职业ID，主键，自增

profession_no VARCHAR(20) NOT NULL UNIQUE - 职业编号，唯一

name NVARCHAR(100) NOT NULL -姓名

type_id INT FOREIGN KEY REFERENCES ProfessionType(type_id) - 职业类型ID，外键

contact_person NVARCHAR(50) NOT NULL - 联系人

phone VARCHAR(20) NOT NULL - 联系电话

place VARCHAR(20) - 地址

4. 职业作息登记表(Schedule)
功能：实现职业作息登记

字段：

schedule_id INT PRIMARY KEY IDENTITY(1,1) - 作息ID，主键，自增

profession_id INT FOREIGN KEY REFERENCES Profession(profession_id) - 职业ID，外键

teacher_id INT FOREIGN KEY REFERENCES Teacher(teacher_id) - 教师ID，外键

schedule_date DATE NOT NULL - 作息日期

start_time TIME NOT NULL - 开始时间

end_time TIME NOT NULL - 结束时间


5. 工资管理表(Salary)
功能：实现工资管理

字段：

salary_id INT PRIMARY KEY IDENTITY(1,1) - 工资ID，主键，自增

teacher_id INT FOREIGN KEY REFERENCES Teacher(teacher_id) - 教师ID，外键

salary_month CHAR(6) NOT NULL - 工资月份(YYYYMM)

base_salary DECIMAL(10,2) - 基本工资

bonus DECIMAL(10,2) DEFAULT 0 - 奖金

deduction DECIMAL(10,2) DEFAULT 0 - 扣款

tax DECIMAL(10,2) DEFAULT 0 - 税费

net_salary DECIMAL(10,2) - 实发工资


6. 收费管理表(Payment)
功能：实现收费管理

字段：

payment_id INT PRIMARY KEY IDENTITY(1,1) - 收费ID，主键，自增

profession_id INT FOREIGN KEY REFERENCES Profession(profession_id) - 职业ID，外键

payment_no VARCHAR(20) NOT NULL UNIQUE - 收费单号

amount DECIMAL(10,2) NOT NULL - 收费金额

payment_date DATETIME DEFAULT GETDATE() - 支付日期

i