CREATE TABLE OccupationRegistration (
    OccupationId INT PRIMARY KEY AUTO_INCREMENT,
    OccupationTypeId INT NOT NULL,
    StudentId INT NOT NULL,
    FOREIGN KEY (OccupationTypeId) REFERENCES OccupationType(OccupationTypeId) ON DELETE CASCADE,
    FOREIGN KEY (StudentId) REFERENCES Student(StudentId) ON DELETE CASCADE,
    UNIQUE (OccupationTypeId,StudentId)
);

INSERT INTO OccupationRegistration (OccupationTypeId, StudentId) VALUES
(1, 1),  -- 张三登记数学辅导
(2, 2),  -- 李四登记英语辅导
(3, 3),  -- 王五登记物理辅导
(4, 4),  -- 赵六登记化学辅导
(5, 5),  -- 陈七登记编程辅导
(1, 6),  -- 刘八登记数学辅导
(2, 7),  -- 黄九登记英语辅导
(3, 8),  -- 周十登记物理辅导
(4, 9),  -- 吴十一登记化学辅导
(5, 10), -- 徐十二登记编程辅导
(1, 2),  -- 李四额外登记数学辅导
(2, 3),  -- 王五额外登记英语辅导
(3, 4),  -- 赵六额外登记物理辅导
(4, 5),  -- 陈七额外登记化学辅导
(5, 6);  -- 刘八额外登记编程辅导
