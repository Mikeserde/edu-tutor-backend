6.存储过程
6.1 创建系统备份记录表
CREATE TABLE BackupRecord (
    BackupId INT PRIMARY KEY AUTO_INCREMENT,
    BackupTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Comment VARCHAR(255) COMMENT '备份说明'
);
6.2 创建基本表的备份记录表
1 学生备份表（Student_Backup）
CREATE TABLE Student_Backup (
    BackupRecordId INT NOT NULL,
    StudentId INT,
    Name VARCHAR(50),
    Gender VARCHAR(2),
    Address VARCHAR(20),
    ContactPhone VARCHAR(20),
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
);
2 教师备份表（Teacher_Backup）
CREATE TABLE Teacher_Backup (
    BackupRecordId INT NOT NULL,
    TeacherId INT,
    Name VARCHAR(50),
    Gender VARCHAR(2),
    Phone VARCHAR(20),
    HourlyFee DECIMAL(10,2),
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
);
3 职业类型备份表（OccupationType_Backup）
`CREATE TABLE OccupationType_Backup (
    BackupRecordId INT NOT NULL,
    OccupationTypeId INT,
    Name VARCHAR(50),
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
);`
4 职业登记备份表(OccupationRegistration_Backup)
CREATE TABLE OccupationRegistration_Backup (
    BackupRecordId INT NOT NULL,
    OccupationId INT,
    OccupationTypeId INT,
    StudentId INT,
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
); 
5 职业作息备份表（OccupationSchedule_Backup）
CREATE TABLE OccupationSchedule_Backup (
    BackupRecordId INT NOT NULL,
    ScheduleId INT,
    OccupationId INT,
    TeacherId INT,
    Date DATE,
    StartTime TIME,
    EndTime TIME,
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
);
6 收费备份表（Payment_Backup）
CREATE TABLE Payment_Backup (
    BackupRecordId INT NOT NULL,
    PaymentId INT,
    OccupationId INT,
    PaymentDate DATE,
    Amount DECIMAL(10,2),
    FOREIGN KEY (BackupRecordId) REFERENCES BackupRecord(BackupId) ON DELETE CASCADE
);
6.3 创建备份/恢复存储过程
1 创建备份存储过程
存储过程的创建
DELIMITER //
CREATE PROCEDURE BackupDatabase(IN comment_text VARCHAR(255))
BEGIN
    DECLARE backup_id INT;

    -- 创建备份记录
    INSERT INTO BackupRecord (Comment) VALUES (comment_text);
    SET backup_id = LAST_INSERT_ID();

    -- 备份所有表数据
    INSERT INTO Student_Backup (BackupRecordId, StudentId, Name, Gender, Address, ContactPhone)
    SELECT backup_id, StudentId, Name, Gender, Address, ContactPhone FROM Student;

    INSERT INTO Teacher_Backup (BackupRecordId, TeacherId, Name, Gender, Phone, HourlyFee)
    SELECT backup_id, TeacherId, Name, Gender, Phone, HourlyFee FROM Teacher;

    INSERT INTO OccupationType_Backup (BackupRecordId, OccupationTypeId, Name)
    SELECT backup_id, OccupationTypeId, Name FROM OccupationType;

    INSERT INTO OccupationRegistration_Backup (BackupRecordId, OccupationId, OccupationTypeId, StudentId)
    SELECT backup_id, OccupationId, OccupationTypeId, StudentId FROM OccupationRegistration;

    INSERT INTO OccupationSchedule_Backup (BackupRecordId, ScheduleId, OccupationId, TeacherId, Date, StartTime, EndTime)
    SELECT backup_id, ScheduleId, OccupationId, TeacherId, Date, StartTime, EndTime FROM OccupationSchedule;

    INSERT INTO Payment_Backup (BackupRecordId, PaymentId, OccupationId, PaymentDate, Amount)
    SELECT backup_id, PaymentId, OccupationId, PaymentDate, Amount FROM Payment;

    SELECT backup_id AS BackupId; -- 返回备份ID
END //
DELIMITER ;
调用方法
CALL BackupDatabase('具体备份事件的描述');
2 创建恢复存储过程
存储过程的创建
DELIMITER //
CREATE PROCEDURE RestoreDatabase(IN backup_id INT)
BEGIN
    -- 禁用外键检查
    SET FOREIGN_KEY_CHECKS = 0;

    -- 删除当前数据(按依赖顺序)
    DELETE FROM Salary;
    DELETE FROM Payment;
    DELETE FROM OccupationSchedule;
    DELETE FROM OccupationRegistration;
    DELETE FROM Student;
    DELETE FROM Teacher;
    DELETE FROM OccupationType;

    -- 恢复数据(按依赖顺序)
    INSERT INTO OccupationType (OccupationTypeId, Name)
    SELECT OccupationTypeId, Name 
    FROM OccupationType_Backup 
    WHERE BackupRecordId = backup_id;

    INSERT INTO Student (StudentId, Name, Gender, Address, ContactPhone)
    SELECT StudentId, Name, Gender, Address, ContactPhone 
    FROM Student_Backup 
    WHERE BackupRecordId = backup_id;

    INSERT INTO Teacher (TeacherId, Name, Gender, Phone, HourlyFee)
    SELECT TeacherId, Name, Gender, Phone, HourlyFee 
    FROM Teacher_Backup 
    WHERE BackupRecordId = backup_id;

    INSERT INTO OccupationRegistration (OccupationId, OccupationTypeId, StudentId)
    SELECT OccupationId, OccupationTypeId, StudentId 
    FROM OccupationRegistration_Backup 
    WHERE BackupRecordId = backup_id;

    INSERT INTO OccupationSchedule (ScheduleId, OccupationId, TeacherId, Date, StartTime, EndTime)
    SELECT ScheduleId, OccupationId, TeacherId, Date, StartTime, EndTime 
    FROM OccupationSchedule_Backup 
    WHERE BackupRecordId = backup_id;

    INSERT INTO Payment (PaymentId, OccupationId, PaymentDate, Amount)
    SELECT PaymentId, OccupationId, PaymentDate, Amount 
    FROM Payment_Backup 
    WHERE BackupRecordId = backup_id;
    
    -- 启用外键检查
    SET FOREIGN_KEY_CHECKS = 1;
    
    SELECT '恢复成功' AS Result;
END //
DELIMITER ;

-- 5创建查看备份记录存储过程 
DELIMITER //
CREATE PROCEDURE ListBackups()
BEGIN
    SELECT 
        BackupId, 
        DATE_FORMAT(BackupTime, '%Y-%m-%d %H:%i:%s') AS BackupTime, 
        Comment
    FROM BackupRecord
    ORDER BY BackupTime DESC;
END //
DELIMITER ;
调用方法
CALL RestoreDatabase(备份id); -- 使用备份ID
3 创建清理存储过程
DELIMITER //
CREATE PROCEDURE CleanBackupData(IN keep_count INT, IN keep_days INT)
BEGIN
    -- 清理超过保留数量的备份记录
    IF keep_count > 0 THEN
        -- 使用用户变量计算需要删除的记录数
        SET @delete_count = GREATEST(0, (SELECT COUNT(*) FROM BackupRecord) - keep_count);
        
        -- 仅当有记录需要删除时才执行
        IF @delete_count > 0 THEN
            -- 使用动态SQL解决LIMIT变量问题
            SET @sql = CONCAT(
                'CREATE TEMPORARY TABLE TempOldBackups AS ',
                'SELECT BackupId ',
                'FROM BackupRecord ',
                'ORDER BY BackupTime ASC ',
                'LIMIT ', @delete_count
            );
            
            PREPARE stmt FROM @sql;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
            
            -- 删除备份记录
            DELETE br FROM BackupRecord br
            JOIN TempOldBackups tob ON br.BackupId = tob.BackupId;
            
            -- 删除临时表
            DROP TEMPORARY TABLE IF EXISTS TempOldBackups;
        END IF;
    END IF;
    
    -- 清理超过保留天数的备份记录
    IF keep_days > 0 THEN
        DELETE FROM BackupRecord 
        WHERE BackupTime < DATE_SUB(NOW(), INTERVAL keep_days DAY);
    END IF;
    
    -- 返回清理结果
    SELECT 
        CONCAT('备份清理完成: ',
            IF(keep_count > 0, CONCAT('保留最近', keep_count, '次备份'), ''),
            IF(keep_count > 0 AND keep_days > 0, ' 和 ', ''),
            IF(keep_days > 0, CONCAT('保留', keep_days, '天内备份'), ''),
            ' | 剩余备份数: ', (
                SELECT COUNT(*) 
                FROM BackupRecord
            )
        ) AS Result;
END //
DELIMITER ;
4 创建备份记录的查看的存储过程
存储过程的创建
DELIMITER //
CREATE PROCEDURE ListBackups()
BEGIN
    SELECT 
        BackupId, 
        DATE_FORMAT(BackupTime, '%Y-%m-%d %H:%i:%s') AS BackupTime, 
        Comment
    FROM BackupRecord
    ORDER BY BackupTime DESC;
END //
DELIMITER ;
调用方法
CALL ListBackups();