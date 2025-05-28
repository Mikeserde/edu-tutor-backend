-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tutorservicedb
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `occupationregistration`
--

DROP TABLE IF EXISTS `occupationregistration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `occupationregistration` (
  `OccupationId` int NOT NULL AUTO_INCREMENT,
  `OccupationTypeId` int NOT NULL,
  `StudentId` int NOT NULL,
  PRIMARY KEY (`OccupationId`),
  UNIQUE KEY `OccupationTypeId` (`OccupationTypeId`,`StudentId`),
  KEY `StudentId` (`StudentId`),
  CONSTRAINT `occupationregistration_ibfk_1` FOREIGN KEY (`OccupationTypeId`) REFERENCES `occupationtype` (`OccupationTypeId`) ON DELETE CASCADE,
  CONSTRAINT `occupationregistration_ibfk_2` FOREIGN KEY (`StudentId`) REFERENCES `student` (`StudentId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationregistration`
--

LOCK TABLES `occupationregistration` WRITE;
/*!40000 ALTER TABLE `occupationregistration` DISABLE KEYS */;
INSERT INTO `occupationregistration` VALUES (1,1,1),(11,1,2),(6,1,6),(2,2,2),(12,2,3),(7,2,7),(3,3,3),(13,3,4),(8,3,8),(4,4,4),(14,4,5),(9,4,9),(5,5,5),(15,5,6),(10,5,10);
/*!40000 ALTER TABLE `occupationregistration` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `AfterOccupationRegistrationInsert` AFTER INSERT ON `occupationregistration` FOR EACH ROW BEGIN
    -- 自动插入 Payment 记录
    INSERT INTO Payment (OccupationId, PaymentDate, Amount)
    VALUES (NEW.OccupationId, CURRENT_DATE(), 200.00);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `occupationschedule`
--

DROP TABLE IF EXISTS `occupationschedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `occupationschedule` (
  `ScheduleId` int NOT NULL AUTO_INCREMENT,
  `OccupationId` int NOT NULL,
  `TeacherId` int NOT NULL,
  `Date` date NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  PRIMARY KEY (`ScheduleId`),
  UNIQUE KEY `OccupationId` (`OccupationId`,`TeacherId`,`Date`,`StartTime`,`EndTime`),
  KEY `idx_Schedule_Date` (`Date`),
  KEY `idx_Schedule_TeacherDate` (`TeacherId`,`Date`),
  CONSTRAINT `occupationschedule_ibfk_1` FOREIGN KEY (`OccupationId`) REFERENCES `occupationregistration` (`OccupationId`) ON DELETE CASCADE,
  CONSTRAINT `occupationschedule_ibfk_2` FOREIGN KEY (`TeacherId`) REFERENCES `teacher` (`TeacherId`) ON DELETE CASCADE,
  CONSTRAINT `chk_endtime_after_starttime` CHECK ((`EndTime` >= `StartTime`))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationschedule`
--

LOCK TABLES `occupationschedule` WRITE;
/*!40000 ALTER TABLE `occupationschedule` DISABLE KEYS */;
INSERT INTO `occupationschedule` VALUES (1,1,1,'2025-10-05','09:00:00','11:00:00'),(4,2,2,'2025-10-06','13:00:00','15:00:00'),(7,3,3,'2025-10-07','10:00:00','12:00:00'),(10,4,4,'2025-10-08','14:00:00','16:00:00'),(13,5,5,'2025-10-09','16:00:00','18:00:00'),(2,6,1,'2025-10-12','14:00:00','16:00:00'),(5,7,2,'2025-10-15','15:00:00','17:00:00'),(8,8,3,'2025-10-18','16:00:00','18:00:00'),(11,9,4,'2025-10-20','10:00:00','12:00:00'),(14,10,5,'2025-10-25','09:00:00','11:00:00'),(3,11,1,'2025-11-01','10:00:00','12:00:00'),(6,12,2,'2025-11-05','09:30:00','11:30:00'),(9,13,3,'2025-11-10','14:00:00','16:00:00'),(12,14,4,'2025-11-15','13:00:00','15:00:00'),(15,15,5,'2025-11-20','15:00:00','17:00:00');
/*!40000 ALTER TABLE `occupationschedule` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_occupation_schedule_insert` AFTER INSERT ON `occupationschedule` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_occupation_schedule_update` AFTER UPDATE ON `occupationschedule` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_occupation_schedule_delete` BEFORE DELETE ON `occupationschedule` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `occupationtype`
--

DROP TABLE IF EXISTS `occupationtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `occupationtype` (
  `OccupationTypeId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`OccupationTypeId`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationtype`
--

LOCK TABLES `occupationtype` WRITE;
/*!40000 ALTER TABLE `occupationtype` DISABLE KEYS */;
INSERT INTO `occupationtype` VALUES (4,'化学辅导'),(1,'数学辅导'),(3,'物理辅导'),(5,'编程辅导'),(2,'英语辅导');
/*!40000 ALTER TABLE `occupationtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `PaymentId` int NOT NULL AUTO_INCREMENT,
  `OccupationId` int NOT NULL,
  `PaymentDate` date NOT NULL,
  `Amount` decimal(10,2) NOT NULL DEFAULT '200.00',
  PRIMARY KEY (`PaymentId`),
  KEY `OccupationId` (`OccupationId`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`OccupationId`) REFERENCES `occupationregistration` (`OccupationId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,'2025-05-28',200.00),(2,2,'2025-05-28',200.00),(3,3,'2025-05-28',200.00),(4,4,'2025-05-28',200.00),(5,5,'2025-05-28',200.00),(6,6,'2025-05-28',200.00),(7,7,'2025-05-28',200.00),(8,8,'2025-05-28',200.00),(9,9,'2025-05-28',200.00),(10,10,'2025-05-28',200.00),(11,11,'2025-05-28',200.00),(12,12,'2025-05-28',200.00),(13,13,'2025-05-28',200.00),(14,14,'2025-05-28',200.00),(15,15,'2025-05-28',200.00);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary` (
  `TeacherId` int NOT NULL,
  `Month` char(7) NOT NULL COMMENT '格式: YYYY-MM',
  `TotalHours` decimal(10,2) NOT NULL DEFAULT '0.00',
  `TotalAmount` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`TeacherId`,`Month`),
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`TeacherId`) REFERENCES `teacher` (`TeacherId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary` DISABLE KEYS */;
INSERT INTO `salary` VALUES (1,'2025-10',4.00,600.00),(1,'2025-11',2.00,300.00),(2,'2025-10',4.00,800.00),(2,'2025-11',2.00,400.00),(3,'2025-10',4.00,720.00),(3,'2025-11',2.00,360.00),(4,'2025-10',4.00,880.00),(4,'2025-11',2.00,440.00),(5,'2025-10',4.00,760.00),(5,'2025-11',2.00,380.00);
/*!40000 ALTER TABLE `salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `StudentId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Gender` varchar(2) NOT NULL,
  `Address` varchar(20) NOT NULL,
  `ContactPhone` varchar(20) NOT NULL,
  PRIMARY KEY (`StudentId`),
  UNIQUE KEY `ContactPhone` (`ContactPhone`),
  CONSTRAINT `student_chk_1` CHECK ((`Gender` in (_utf8mb4'男',_utf8mb4'女')))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'张三','男','北京市朝阳区','18812345678'),(2,'李四','女','上海市浦东新区','18823456789'),(3,'王五','男','广州市天河区','18834567890'),(4,'赵六','女','深圳市福田区','18845678901'),(5,'陈七','男','杭州市西湖区','18856789012'),(6,'刘八','女','成都市锦江区','18867890123'),(7,'黄九','男','武汉市江汉区','18878901234'),(8,'周十','女','南京市鼓楼区','18889012345'),(9,'吴十一','男','西安市雁塔区','18890123456'),(10,'徐十二','女','重庆市渝中区','18801234567');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `TeacherId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Gender` varchar(2) NOT NULL,
  `Phone` varchar(20) NOT NULL,
  `HourlyFee` decimal(10,2) NOT NULL,
  PRIMARY KEY (`TeacherId`),
  UNIQUE KEY `Phone` (`Phone`),
  CONSTRAINT `teacher_chk_1` CHECK ((`Gender` in (_utf8mb4'男',_utf8mb4'女'))),
  CONSTRAINT `teacher_chk_2` CHECK ((`HourlyFee` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'张老师','男','13900001111',150.00),(2,'李老师','女','13900002222',200.00),(3,'王老师','男','13900003333',180.00),(4,'赵老师','女','13900004444',220.00),(5,'陈老师','男','13900005555',190.00);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-28 22:34:01
