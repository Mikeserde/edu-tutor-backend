CREATE TABLE Salary (
    TeacherId INT NOT NULL,
    Month CHAR(7) NOT NULL COMMENT '格式: YYYY-MM',
    TotalHours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    TotalAmount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (TeacherId, Month),  -- 联合主键
    FOREIGN KEY (TeacherId) REFERENCES Teacher(TeacherId) ON DELETE CASCADE
);