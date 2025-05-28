CREATE TABLE Teacher (
    TeacherId INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Gender VARCHAR(2) NOT NULL CHECK (Gender IN ('男', '女')),
    Phone VARCHAR(20) NOT NULL UNIQUE,
    HourlyFee DECIMAL(10,2) NOT NULL CHECK (HourlyFee > 0)
);

INSERT INTO Teacher (Name, Gender, Phone, HourlyFee) VALUES
('张老师', '男', '13900001111', 150.00),
('李老师', '女', '13900002222', 200.00),
('王老师', '男', '13900003333', 180.00),
('赵老师', '女', '13900004444', 220.00),
('陈老师', '男', '13900005555', 190.00);