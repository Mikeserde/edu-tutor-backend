-- 日期范围查询索引
CREATE INDEX idx_Schedule_Date ON OccupationSchedule(Date);

-- 高频查询复合索引（教师 + 日期）
CREATE INDEX idx_Schedule_TeacherDate ON OccupationSchedule(TeacherId, Date);