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
  `Address` varchar(100) NOT NULL,
  `ContactPhone` varchar(20) NOT NULL,
  PRIMARY KEY (`OccupationId`),
  KEY `idx_occupation_reg` (`OccupationTypeId`),
  CONSTRAINT `occupationregistration_ibfk_1` FOREIGN KEY (`OccupationTypeId`) REFERENCES `occupationtype` (`OccupationTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationregistration`
--

LOCK TABLES `occupationregistration` WRITE;
/*!40000 ALTER TABLE `occupationregistration` DISABLE KEYS */;
INSERT INTO `occupationregistration` VALUES (1,1,'北京市海淀区中关村大街1号','010-12345678'),(2,2,'上海市浦东新区陆家嘴路100号','021-23456789'),(3,3,'广州市天河区体育西路200号','020-34567890'),(4,4,'深圳市南山区科技园路50号','0755-45678901'),(5,5,'杭州市西湖区文三路300号','0571-56789012'),(6,6,'南京市鼓楼区中山北路400号','025-67890123'),(7,7,'武汉市武昌区中南路500号','027-78901234'),(8,8,'成都市锦江区春熙路600号','028-89012345'),(9,9,'西安市雁塔区小寨路700号','029-90123456'),(10,10,'重庆市渝中区解放碑800号','023-01234567'),(11,11,'天津市和平区南京路900号','022-11223344'),(12,12,'青岛市市南区香港中路1000号','0532-22334455'),(13,13,'大连市中山区人民路1100号','0411-33445566'),(14,14,'厦门市思明区湖滨南路1200号','0592-44556677'),(15,15,'长沙市芙蓉区五一大道1300号','0731-55667788'),(16,16,'郑州市金水区花园路1400号','0371-66778899'),(17,17,'苏州市姑苏区干将路1500号','0512-77889900'),(18,18,'无锡市梁溪区中山路1600号','0510-88990011'),(19,19,'宁波市鄞州区钱湖北路1700号','0574-99001122'),(20,20,'佛山市南海区桂城路1800号','0757-00112233');
/*!40000 ALTER TABLE `occupationregistration` ENABLE KEYS */;
UNLOCK TABLES;

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
  UNIQUE KEY `OccupationId` (`OccupationId`,`TeacherId`),
  KEY `idx_schedule_date` (`Date`,`TeacherId`),
  KEY `idx_schedule_teacher` (`TeacherId`),
  CONSTRAINT `occupationschedule_ibfk_1` FOREIGN KEY (`OccupationId`) REFERENCES `occupationregistration` (`OccupationId`),
  CONSTRAINT `occupationschedule_ibfk_2` FOREIGN KEY (`TeacherId`) REFERENCES `teacher` (`TeacherId`),
  CONSTRAINT `occupationschedule_chk_1` CHECK ((`EndTime` > `StartTime`))
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationschedule`
--

LOCK TABLES `occupationschedule` WRITE;
/*!40000 ALTER TABLE `occupationschedule` DISABLE KEYS */;
INSERT INTO `occupationschedule` VALUES (1,1,1,'2023-10-01','09:00:00','14:00:00'),(2,2,1,'2023-10-02','10:00:00','15:00:00'),(3,3,2,'2023-10-03','08:00:00','12:00:00'),(4,4,2,'2023-10-04','13:00:00','17:00:00'),(5,5,2,'2023-10-05','09:30:00','13:30:00'),(6,6,3,'2023-10-06','10:00:00','15:00:00'),(7,7,3,'2023-10-07','14:00:00','19:00:00'),(8,8,3,'2023-10-08','08:00:00','13:00:00'),(9,9,4,'2023-10-09','09:00:00','13:00:00'),(10,10,4,'2023-10-10','14:00:00','18:00:00'),(11,11,5,'2023-10-11','08:00:00','12:00:00'),(12,12,5,'2023-10-12','13:00:00','17:00:00'),(13,13,5,'2023-10-13','09:00:00','13:00:00'),(14,14,5,'2023-10-14','14:00:00','18:00:00'),(15,15,5,'2023-10-15','10:00:00','14:00:00'),(16,16,6,'2023-10-05','09:00:00','15:00:00'),(17,17,6,'2023-10-06','10:00:00','16:00:00'),(18,18,6,'2023-10-07','08:00:00','14:00:00'),(19,19,7,'2023-10-08','09:00:00','14:00:00'),(20,20,7,'2023-10-09','10:00:00','15:00:00'),(21,1,7,'2023-10-10','13:00:00','18:00:00'),(22,2,7,'2023-10-11','08:00:00','13:00:00'),(23,3,7,'2023-10-12','14:00:00','19:00:00'),(24,4,8,'2023-10-13','08:00:00','12:00:00'),(25,5,8,'2023-10-14','13:00:00','17:00:00'),(26,6,8,'2023-10-15','09:30:00','13:30:00'),(27,7,8,'2023-10-16','14:30:00','18:30:00'),(28,8,8,'2023-10-17','10:00:00','14:00:00'),(29,9,8,'2023-10-18','15:00:00','17:00:00'),(30,10,9,'2023-10-19','08:00:00','12:00:00'),(31,11,9,'2023-10-20','13:00:00','17:00:00'),(32,12,9,'2023-10-21','09:00:00','13:00:00'),(33,13,9,'2023-10-22','14:00:00','18:00:00'),(34,14,9,'2023-10-23','08:30:00','12:30:00'),(35,15,9,'2023-10-24','13:30:00','17:30:00'),(36,16,9,'2023-10-25','10:00:00','14:00:00'),(37,17,9,'2023-10-26','15:00:00','17:00:00'),(38,18,10,'2023-10-27','09:00:00','14:00:00'),(39,19,10,'2023-10-28','10:00:00','15:00:00'),(40,20,10,'2023-10-29','08:00:00','13:00:00'),(41,1,11,'2023-10-30','09:00:00','13:00:00'),(42,2,11,'2023-10-31','14:00:00','18:00:00'),(43,3,11,'2023-11-01','10:30:00','14:30:00'),(44,4,12,'2023-11-02','08:00:00','12:00:00'),(45,5,12,'2023-11-03','13:00:00','17:00:00'),(46,6,12,'2023-11-04','09:00:00','13:00:00'),(47,7,12,'2023-11-05','14:00:00','16:00:00'),(48,8,13,'2023-11-06','10:00:00','15:00:00'),(49,9,13,'2023-11-07','14:00:00','19:00:00'),(50,10,14,'2023-11-08','08:00:00','12:00:00'),(51,11,14,'2023-11-09','13:00:00','17:00:00'),(52,12,14,'2023-11-10','09:00:00','13:00:00'),(53,13,14,'2023-11-11','14:00:00','18:00:00'),(54,14,14,'2023-11-12','08:30:00','12:30:00'),(55,15,14,'2023-11-13','13:30:00','15:30:00'),(56,16,15,'2023-11-14','09:00:00','13:00:00'),(57,17,15,'2023-11-15','14:00:00','18:00:00'),(58,18,15,'2023-11-16','10:00:00','14:00:00'),(59,19,15,'2023-11-17','15:00:00','19:00:00'),(60,20,15,'2023-11-18','08:00:00','10:00:00'),(61,1,16,'2023-11-19','09:00:00','13:00:00'),(62,2,16,'2023-11-20','14:00:00','18:00:00'),(63,3,16,'2023-11-21','10:30:00','14:30:00'),(64,4,16,'2023-11-22','15:30:00','17:30:00'),(65,5,17,'2023-11-23','08:00:00','12:00:00'),(66,6,17,'2023-11-24','13:00:00','17:00:00'),(67,7,17,'2023-11-25','09:00:00','13:00:00'),(68,8,17,'2023-11-26','14:00:00','18:00:00'),(69,9,17,'2023-11-27','08:30:00','12:30:00'),(70,10,17,'2023-11-28','13:30:00','17:30:00'),(71,11,17,'2023-11-29','10:00:00','12:00:00'),(72,12,18,'2023-11-30','09:00:00','13:00:00'),(73,13,18,'2023-12-01','14:00:00','18:00:00'),(74,14,18,'2023-12-02','08:00:00','12:00:00'),(75,15,18,'2023-12-03','13:00:00','17:00:00'),(76,16,18,'2023-12-04','09:30:00','11:30:00'),(77,17,19,'2023-12-05','08:00:00','12:00:00'),(78,18,19,'2023-12-06','13:00:00','17:00:00'),(79,19,19,'2023-12-07','09:00:00','13:00:00'),(80,20,19,'2023-12-08','14:00:00','16:00:00'),(81,1,20,'2023-12-09','09:00:00','13:00:00'),(82,2,20,'2023-12-10','14:00:00','18:00:00'),(83,3,20,'2023-12-11','10:30:00','14:30:00');
/*!40000 ALTER TABLE `occupationschedule` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupationtype`
--

LOCK TABLES `occupationtype` WRITE;
/*!40000 ALTER TABLE `occupationtype` DISABLE KEYS */;
INSERT INTO `occupationtype` VALUES (9,'书法课程'),(11,'体育训练'),(7,'化学实验'),(15,'围棋培训'),(10,'声乐训练'),(16,'摄影教学'),(1,'数学辅导'),(12,'日语教学'),(14,'机器人编程'),(13,'法语课程'),(20,'演讲培训'),(17,'烹饪课程'),(6,'物理辅导'),(18,'瑜伽教学'),(3,'编程教学'),(5,'美术培训'),(8,'舞蹈教学'),(2,'英语培训'),(19,'金融理财'),(4,'钢琴课程');
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
  `Amount` decimal(10,2) NOT NULL,
  `PaymentMethod` varchar(20) NOT NULL,
  `Status` enum('已支付','未支付') NOT NULL DEFAULT '未支付',
  PRIMARY KEY (`PaymentId`),
  KEY `idx_payment_date` (`PaymentDate`,`Status`),
  KEY `idx_payment_occupation` (`OccupationId`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`OccupationId`) REFERENCES `occupationregistration` (`OccupationId`),
  CONSTRAINT `payment_chk_1` CHECK ((`Amount` > 0)),
  CONSTRAINT `payment_chk_2` CHECK ((`PaymentMethod` in (_utf8mb4'支付宝',_utf8mb4'现金',_utf8mb4'银行卡')))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,'2023-10-02',500.00,'支付宝','已支付'),(2,2,'2023-10-03',400.00,'现金','已支付'),(3,3,'2023-10-04',600.00,'银行卡','已支付'),(4,4,'2023-10-05',300.00,'支付宝','未支付'),(5,5,'2023-10-06',450.00,'现金','已支付'),(6,6,'2023-10-07',350.00,'银行卡','未支付'),(7,7,'2023-10-08',700.00,'支付宝','已支付'),(8,8,'2023-10-09',250.00,'现金','未支付'),(9,9,'2023-10-10',550.00,'银行卡','已支付'),(10,10,'2023-10-11',200.00,'支付宝','未支付'),(11,11,'2023-10-12',480.00,'现金','已支付'),(12,12,'2023-10-13',320.00,'银行卡','未支付'),(13,13,'2023-10-14',650.00,'支付宝','已支付'),(14,14,'2023-10-15',280.00,'现金','未支付'),(15,15,'2023-10-16',750.00,'银行卡','已支付'),(16,16,'2023-10-17',180.00,'支付宝','未支付'),(17,17,'2023-10-18',420.00,'现金','已支付'),(18,18,'2023-10-19',380.00,'银行卡','未支付'),(19,19,'2023-10-20',800.00,'支付宝','已支付'),(20,20,'2023-10-21',220.00,'现金','未支付');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary` (
  `SalaryId` int NOT NULL AUTO_INCREMENT,
  `TeacherId` int NOT NULL,
  `TotalHours` decimal(10,2) NOT NULL,
  `PaymentDate` datetime DEFAULT NULL,
  `TotalAmount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`SalaryId`),
  KEY `idx_salary_payment` (`TeacherId`,`PaymentDate`),
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`TeacherId`) REFERENCES `teacher` (`TeacherId`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary` DISABLE KEYS */;
INSERT INTO `salary` VALUES (1,1,10.00,'2023-10-05 10:00:00',1600.00),(2,2,12.00,'2023-10-06 11:00:00',2160.00),(3,3,15.00,'2023-10-07 12:00:00',2250.00),(4,4,8.00,'2023-10-08 13:00:00',1600.00),(5,5,20.00,'2023-10-09 14:00:00',3400.00),(6,6,18.00,'2023-10-10 15:00:00',3420.00),(7,7,25.00,'2023-10-11 16:00:00',3500.00),(8,8,22.00,'2023-10-12 17:00:00',4620.00),(9,9,30.00,'2023-10-13 18:00:00',3900.00),(10,10,15.00,'2023-10-14 19:00:00',3300.00),(11,11,12.00,'2023-10-15 20:00:00',1980.00),(12,12,16.00,'2023-10-16 21:00:00',2960.00),(13,13,10.00,'2023-10-17 22:00:00',1550.00),(14,14,24.00,'2023-10-18 23:00:00',4680.00),(15,15,18.00,'2023-10-19 00:00:00',3150.00),(16,16,14.00,'2023-10-20 01:00:00',2870.00),(17,17,28.00,'2023-10-21 02:00:00',4060.00),(18,18,20.00,'2023-10-22 03:00:00',4300.00),(19,19,16.00,'2023-10-23 04:00:00',2160.00),(20,20,12.00,'2023-10-24 05:00:00',2700.00);
/*!40000 ALTER TABLE `salary` ENABLE KEYS */;
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
  KEY `idx_teacher_phone` (`Phone`),
  CONSTRAINT `teacher_chk_1` CHECK ((`Gender` in (_utf8mb4'男',_utf8mb4'女'))),
  CONSTRAINT `teacher_chk_2` CHECK ((`HourlyFee` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'云缨','女','13812345678',160.00),(2,'澜','男','13923456789',180.00),(3,'貂蝉','女','15034567890',150.00),(4,'吕布','男','15145678901',200.00),(5,'孙尚香','女','15256789012',170.00),(6,'诸葛亮','男','15567890123',190.00),(7,'大乔','女','17778901234',140.00),(8,'铠','男','18889012345',210.00),(9,'瑶','女','19990123456',130.00),(10,'李白','男','13501234567',220.00),(11,'公孙离','女','13611223344',165.00),(12,'韩信','男','13722334455',185.00),(13,'小乔','女','13833445566',155.00),(14,'赵云','男','13944556677',195.00),(15,'芈月','女','15055667788',175.00),(16,'百里守约','男','15166778899',205.00),(17,'露娜','女','15277889900',145.00),(18,'马可波罗','男','15588990011',215.00),(19,'西施','女','17799001122',135.00),(20,'花木兰','女','18800112233',225.00);
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

-- Dump completed on 2025-05-26 12:52:50
