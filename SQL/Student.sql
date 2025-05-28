CREATE TABLE Student (
    StudentId INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Gender VARCHAR(2) NOT NULL CHECK (Gender IN ('男', '女')),
    Address VARCHAR(20) NOT NULL,
    ContactPhone VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO Student (Name, Gender, Address, ContactPhone) VALUES
('张三', '男', '北京市朝阳区', '18812345678'),
('李四', '女', '上海市浦东新区', '18823456789'),
('王五', '男', '广州市天河区', '18834567890'),
('赵六', '女', '深圳市福田区', '18845678901'),
('陈七', '男', '杭州市西湖区', '18856789012'),
('刘八', '女', '成都市锦江区', '18867890123'),
('黄九', '男', '武汉市江汉区', '18878901234'),
('周十', '女', '南京市鼓楼区', '18889012345'),
('吴十一', '男', '西安市雁塔区', '18890123456'),
('徐十二', '女', '重庆市渝中区', '18801234567');