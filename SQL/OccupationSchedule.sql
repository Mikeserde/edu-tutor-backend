CREATE TABLE OccupationSchedule (
    ScheduleId INT PRIMARY KEY AUTO_INCREMENT,
    OccupationId INT NOT NULL,
    TeacherId INT NOT NULL,
    Date DATE NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    FOREIGN KEY (OccupationId) REFERENCES OccupationRegistration(OccupationId) ON DELETE CASCADE,
    FOREIGN KEY (TeacherId) REFERENCES Teacher(TeacherId) ON DELETE CASCADE,
    UNIQUE (OccupationId,TeacherId,Date,StartTime,EndTime),
    CONSTRAINT chk_endtime_after_starttime CHECK (EndTime >= StartTime)
);
INSERT INTO OccupationSchedule (OccupationId, TeacherId, Date, StartTime, EndTime) VALUES
-- 数学辅导排课
(1, 1, '2023-10-05', '09:00', '11:00'),
(6, 1, '2023-10-12', '14:00', '16:00'),
(11, 1, '2023-11-01', '10:00', '12:00'),
-- 英语辅导排课
(2, 2, '2023-10-06', '13:00', '15:00'),
(7, 2, '2023-10-15', '15:00', '17:00'),
(12, 2, '2023-11-05', '09:30', '11:30'),
-- 物理辅导排课
(3, 3, '2023-10-07', '10:00', '12:00'),
(8, 3, '2023-10-18', '16:00', '18:00'),
(13, 3, '2023-11-10', '14:00', '16:00'),
-- 化学辅导排课
(4, 4, '2023-10-08', '14:00', '16:00'),
(9, 4, '2023-10-20', '10:00', '12:00'),
(14, 4, '2023-11-15', '13:00', '15:00'),
-- 编程辅导排课
(5, 5, '2023-10-09', '16:00', '18:00'),
(10, 5, '2023-10-25', '09:00', '11:00'),
(15, 5, '2023-11-20', '15:00', '17:00');