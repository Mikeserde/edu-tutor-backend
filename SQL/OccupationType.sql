CREATE TABLE OccupationType (
    OccupationTypeId INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO OccupationType (Name) VALUES
('数学辅导'),
('英语辅导'),
('物理辅导'),
('化学辅导'),
('编程辅导');