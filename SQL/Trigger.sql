-- 插入课程安排后的触发器
DELIMITER //
CREATE TRIGGER after_occupation_schedule_insert
AFTER INSERT ON OccupationSchedule
FOR EACH ROW
BEGIN
    DECLARE v_hours DECIMAL(10,2);
    DECLARE v_hourly_fee DECIMAL(10,2);

    -- 计算课程时长（小时）
    SET v_hours = TIME_TO_SEC(TIMEDIFF(NEW.EndTime, NEW.StartTime)) / 3600;

    -- 获取教师当前课时费
    SELECT HourlyFee INTO v_hourly_fee FROM Teacher WHERE TeacherId = NEW.TeacherId;

    -- 更新工资表，存在则累加，否则插入新记录
    INSERT INTO Salary (TeacherId, Month, TotalHours, TotalAmount)
    VALUES (
        NEW.TeacherId,
        DATE_FORMAT(NEW.Date, '%Y-%m'),
        v_hours,
        v_hours * v_hourly_fee
    )
    ON DUPLICATE KEY UPDATE
        TotalHours = TotalHours + v_hours,
        TotalAmount = TotalAmount + (v_hours * v_hourly_fee);
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_occupation_schedule_update
AFTER UPDATE ON OccupationSchedule
FOR EACH ROW
BEGIN
    DECLARE v_old_hours, v_new_hours DECIMAL(10,2);
    DECLARE v_hourly_fee DECIMAL(10,2);
    DECLARE v_month CHAR(7);
    DECLARE v_total_hours DECIMAL(10,2);

    -- 计算新旧课程时长
    SET v_old_hours = TIME_TO_SEC(TIMEDIFF(OLD.EndTime, OLD.StartTime)) / 3600;
    SET v_new_hours = TIME_TO_SEC(TIMEDIFF(NEW.EndTime, NEW.StartTime)) / 3600;

    -- 获取当前费率和月份
    SELECT HourlyFee INTO v_hourly_fee FROM Teacher WHERE TeacherId = NEW.TeacherId;
    SET v_month = DATE_FORMAT(NEW.Date, '%Y-%m');

    -- 查询当前总时长（带行锁）
    SELECT TotalHours INTO v_total_hours
    FROM Salary
    WHERE TeacherId = NEW.TeacherId AND Month = v_month
    FOR UPDATE;

    -- 核心逻辑：处理记录存在性
    IF v_total_hours IS NULL THEN
        -- 场景1：无原记录，直接插入新数据
        INSERT INTO Salary (TeacherId, Month, TotalHours, TotalAmount)
        VALUES (NEW.TeacherId, v_month, v_new_hours, v_new_hours * v_hourly_fee);
    ELSE
        -- 场景2：有原记录，计算净变化
        SET v_total_hours = v_total_hours - v_old_hours + v_new_hours;

        -- 判断是否删除或更新
        IF v_total_hours <= 0 THEN
            DELETE FROM Salary
            WHERE TeacherId = NEW.TeacherId AND Month = v_month;
        ELSE
            UPDATE Salary
            SET
                TotalHours = v_total_hours,
                TotalAmount = v_total_hours * v_hourly_fee
            WHERE
                TeacherId = NEW.TeacherId
                AND Month = v_month;
        END IF;
    END IF;
END//
DELIMITER ;


DELIMITER //
CREATE TRIGGER before_occupation_schedule_delete
BEFORE DELETE ON OccupationSchedule
FOR EACH ROW
BEGIN
    DECLARE v_old_hours DECIMAL(10,2);
    DECLARE v_old_month CHAR(7);
    DECLARE v_current_hours DECIMAL(10,2);

    -- 计算被删除课程的时长和月份
    SET v_old_hours = TIME_TO_SEC(TIMEDIFF(OLD.EndTime, OLD.StartTime)) / 3600;
    SET v_old_month = DATE_FORMAT(OLD.Date, '%Y-%m');

    -- 查询当前总时长
    SELECT TotalHours INTO v_current_hours
    FROM Salary
    WHERE
        TeacherId = OLD.TeacherId
        AND Month = v_old_month;

    -- 如果删除后总时长 ≤ 0，则直接删除工资记录
    IF (v_current_hours - v_old_hours <= 0) THEN
        DELETE FROM Salary
        WHERE
            TeacherId = OLD.TeacherId
            AND Month = v_old_month;
    ELSE
        -- 否则更新工资记录
        UPDATE Salary
        SET
            TotalHours = TotalHours - v_old_hours,
            TotalAmount = TotalAmount - (v_old_hours * (SELECT HourlyFee FROM Teacher WHERE TeacherId = OLD.TeacherId))
        WHERE
            TeacherId = OLD.TeacherId
            AND Month = v_old_month;
    END IF;
END//
DELIMITER ;

-- 插入职业登记后的触发器
DELIMITER //
CREATE TRIGGER AfterOccupationRegistrationInsert
AFTER INSERT ON OccupationRegistration
FOR EACH ROW
BEGIN
    -- 自动插入 Payment 记录
    INSERT INTO Payment (OccupationId, PaymentDate, Amount)
    VALUES (NEW.OccupationId, CURRENT_DATE(), 200.00);
END //
DELIMITER ;