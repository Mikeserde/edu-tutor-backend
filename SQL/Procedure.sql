-- 统计各种职业的需求次数
DELIMITER //
CREATE PROCEDURE CountOccupationDemand()
BEGIN
    SELECT 
        ot.OccupationTypeId,
        ot.Name AS OccupationName,
        COUNT(orr.OccupationId) AS DemandCount
    FROM OccupationType ot
    LEFT JOIN OccupationRegistration orr ON ot.OccupationTypeId = orr.OccupationTypeId
    GROUP BY ot.OccupationTypeId, ot.Name
    ORDER BY DemandCount DESC;
END //
DELIMITER ;

-- 统计指定日期范围内各教师的授课时间总和
DELIMITER //
CREATE PROCEDURE CalculateTeacherHours(
    IN start_date DATE,
    IN end_date DATE
)
BEGIN
	-- 校验日期顺序
    IF start_date > end_date THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '开始日期不能晚于结束日期';
    END IF;
    SELECT 
        os.TeacherId,
        t.Name AS TeacherName,
        SUM(TIMESTAMPDIFF(HOUR, os.StartTime, os.EndTime)) AS TotalHours
    FROM OccupationSchedule os
    JOIN Teacher t ON os.TeacherId = t.TeacherId
    WHERE os.Date BETWEEN start_date AND end_date
    GROUP BY os.TeacherId, t.Name
    ORDER BY TotalHours DESC;
END //
DELIMITER ;