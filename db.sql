CREATE DATABASE  IF NOT EXISTS `oncology` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `oncology`;
-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: oncology
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dept`
--

DROP TABLE IF EXISTS `dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept`
--

LOCK TABLES `dept` WRITE;
/*!40000 ALTER TABLE `dept` DISABLE KEYS */;
INSERT INTO `dept` VALUES (1,'Doctors');
/*!40000 ALTER TABLE `dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosis`
--

DROP TABLE IF EXISTS `diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosis`
--

LOCK TABLES `diagnosis` WRITE;
/*!40000 ALTER TABLE `diagnosis` DISABLE KEYS */;
INSERT INTO `diagnosis` VALUES (1,'test'),(2,'dia 2 '),(3,'dia 3 ');
/*!40000 ALTER TABLE `diagnosis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_screen`
--

DROP TABLE IF EXISTS `doctor_screen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_screen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pat_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_screen`
--

LOCK TABLES `doctor_screen` WRITE;
/*!40000 ALTER TABLE `doctor_screen` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor_screen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_cat`
--

DROP TABLE IF EXISTS `drug_cat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drug_cat` (
  `id` int NOT NULL,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_cat`
--

LOCK TABLES `drug_cat` WRITE;
/*!40000 ALTER TABLE `drug_cat` DISABLE KEYS */;
INSERT INTO `drug_cat` VALUES (1,'CHEMOTHERAPY'),(2,'SUPPORTIVE'),(3,'FLUID');
/*!40000 ALTER TABLE `drug_cat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drugs`
--

DROP TABLE IF EXISTS `drugs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drugs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `drug_name` varchar(45) DEFAULT NULL,
  `main_category` int DEFAULT NULL,
  `stock` float DEFAULT '0',
  `Strength` int DEFAULT NULL,
  `def_fluid` int DEFAULT NULL,
  `def_volume` int DEFAULT NULL,
  `Strength2` int DEFAULT '0',
  `Strength3` int DEFAULT '0',
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `drug-Cat_fk_idx` (`main_category`),
  CONSTRAINT `drug-Cat_fk` FOREIGN KEY (`main_category`) REFERENCES `drug_cat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drugs`
--

LOCK TABLES `drugs` WRITE;
/*!40000 ALTER TABLE `drugs` DISABLE KEYS */;
INSERT INTO `drugs` VALUES (75,'drug',1,9487,0,90,50,0,0,'nottttttttttttttttttttttttttt'),(76,'fl',3,185,0,0,0,0,0,'test note'),(77,'drug2',1,558,0,76,1,0,0,NULL),(78,'test ui',1,-79,3,76,3,0,0,NULL),(79,'test agian 2',1,158,6,80,5,0,0,NULL),(80,'fluid test change',3,471,2,0,0,NULL,NULL,NULL),(81,'supp',2,-199,5,0,0,NULL,NULL,NULL),(82,'a',1,19,4,0,0,NULL,NULL,NULL),(83,'b',1,-126,5,0,0,0,0,NULL),(84,'d',1,-1,10,0,0,4,3,'note'),(85,'drug 5',2,47,5,76,5,0,0,''),(86,'drug 6 ',1,1001,5,0,0,0,0,'test note on add drug ui '),(87,'f test save',3,-3,200,0,0,0,0,''),(88,'save fluid run ',3,0,20,0,0,0,0,''),(89,'last fu ',3,20,500,0,0,0,0,''),(90,'ffffffff',3,-318,4,0,0,0,0,''),(91,'cemo',1,-9241,2,0,0,0,0,'NO'),(92,'support',2,1983,2,0,0,0,0,''),(93,'fluid',3,21980,100,0,0,0,0,'');
/*!40000 ALTER TABLE `drugs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_group`
--

DROP TABLE IF EXISTS `lab_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_group`
--

LOCK TABLES `lab_group` WRITE;
/*!40000 ALTER TABLE `lab_group` DISABLE KEYS */;
INSERT INTO `lab_group` VALUES (1,'Group 11'),(2,'change group name'),(3,'تحديث اسم القروب'),(4,'Group 99'),(5,'group 5 '),(6,'group 89666'),(7,'group 66'),(8,'Group 33'),(9,'f'),(10,'group 6'),(11,'new group test'),(12,'message '),(13,'message test update'),(14,'تجربه الواجهه '),(15,'kjhg'),(16,'yh'),(17,'last');
/*!40000 ALTER TABLE `lab_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_order`
--

DROP TABLE IF EXISTS `lab_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pat_id` int DEFAULT NULL,
  `doctor` int DEFAULT NULL,
  `DATE` date DEFAULT NULL,
  `state` int DEFAULT '0',
  `note` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `movementNo` int DEFAULT NULL,
  `call` int DEFAULT '0',
  `calledTime` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pat_id_fk_idx` (`pat_id`),
  CONSTRAINT `pat_id_FK-LAB` FOREIGN KEY (`pat_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_order`
--

LOCK TABLES `lab_order` WRITE;
/*!40000 ALTER TABLE `lab_order` DISABLE KEYS */;
INSERT INTO `lab_order` VALUES (93,9,4,'2021-08-07',9,'',159,0,NULL),(94,13,4,'2021-08-13',9,'',184,0,NULL),(96,7,4,'2021-08-13',9,'',182,0,NULL),(98,9,4,'2021-08-13',0,'',186,0,NULL),(99,7,4,'2021-08-14',9,'',192,0,NULL),(100,11,4,'2021-08-14',9,'',193,0,NULL),(101,16,4,'2021-08-14',0,'',194,0,NULL),(102,20,4,'2021-08-14',0,'',195,0,NULL),(104,21,4,'2021-08-15',9,'',197,0,NULL),(105,13,4,'2021-08-15',9,'',198,0,NULL),(106,14,4,'2021-08-15',9,'',199,0,NULL),(107,18,4,'2021-08-15',9,'',200,0,NULL),(108,17,4,'2021-08-15',9,'',201,0,NULL),(109,7,4,'2021-08-15',9,'',202,0,NULL),(110,11,4,'2021-08-15',9,'',203,0,NULL),(111,10,4,'2021-08-15',9,'',204,0,NULL),(112,20,4,'2021-08-15',9,'',205,0,NULL),(114,23,4,'2021-08-15',9,'',207,0,NULL),(115,9,4,'2021-08-15',9,'',208,0,NULL),(116,12,4,'2021-08-16',9,'',209,0,NULL),(117,9,4,'2021-08-16',9,'',210,0,NULL),(118,9,4,'2021-08-17',9,'',215,0,NULL),(119,19,4,'2021-08-17',9,'',219,0,NULL),(120,23,4,'2021-08-17',9,'',224,0,NULL),(121,12,4,'2021-08-18',0,'',225,0,NULL),(122,14,4,'2021-08-18',0,'',227,0,NULL),(123,15,4,'2021-08-19',0,'',234,2,'20:34:20'),(124,27,4,'2021-08-19',0,'',235,1,'20:34:18'),(125,26,4,'2021-08-19',0,'',236,2,'20:34:19'),(126,27,4,'2021-08-20',9,'',237,0,NULL),(127,26,4,'2021-08-20',9,'',238,0,NULL),(129,25,4,'2021-08-20',0,'',239,0,NULL);
/*!40000 ALTER TABLE `lab_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_order_detail`
--

DROP TABLE IF EXISTS `lab_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_order_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order` int DEFAULT NULL,
  `test` int DEFAULT NULL,
  `result` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `finish` int DEFAULT '0',
  `resultFile` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `seen` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `lab_order_Fk_idx` (`order`),
  KEY `lab_test_FK_idx` (`test`),
  CONSTRAINT `lab_order_Fk` FOREIGN KEY (`order`) REFERENCES `lab_order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lab_test_FK` FOREIGN KEY (`test`) REFERENCES `lab_test` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_order_detail`
--

LOCK TABLES `lab_order_detail` WRITE;
/*!40000 ALTER TABLE `lab_order_detail` DISABLE KEYS */;
INSERT INTO `lab_order_detail` VALUES (107,93,7,NULL,1,NULL,0),(108,93,1,NULL,1,NULL,0),(109,94,7,NULL,1,NULL,0),(112,96,2,'فثسف',1,NULL,0),(113,96,6,'ففصسب',1,NULL,0),(114,96,10,'قبر',1,NULL,0),(115,96,4,'قق',1,NULL,0),(117,98,7,'ثثيشيسشششيسيشب',1,NULL,0),(118,98,10,NULL,1,NULL,0),(119,98,6,NULL,1,NULL,0),(121,99,7,NULL,1,'d:\\7\\1212021-08-14.docx',1),(122,99,4,NULL,1,NULL,0),(123,100,2,'ق',1,'d:\\11\\1232021-08-14.docx',1),(124,100,15,NULL,1,NULL,0),(125,100,5,'س',1,'d:\\11\\1252021-08-14.docx',1),(126,100,8,NULL,1,NULL,0),(127,101,4,NULL,1,NULL,0),(128,101,10,NULL,1,NULL,0),(129,101,16,NULL,1,NULL,0),(130,102,1,NULL,1,'d:\\20\\1302021-08-14.docx',1),(131,102,17,NULL,1,'d:\\20\\1312021-08-14.docx',1),(132,102,16,NULL,1,'d:\\20\\1322021-08-14.jpg',1),(136,104,6,NULL,1,NULL,0),(137,104,9,NULL,1,NULL,0),(138,104,15,NULL,1,NULL,0),(139,104,1,NULL,1,NULL,0),(140,105,7,NULL,1,NULL,0),(141,105,1,NULL,1,NULL,0),(142,105,16,NULL,1,NULL,0),(143,105,17,NULL,1,NULL,0),(144,106,7,NULL,1,NULL,0),(145,106,17,NULL,1,NULL,0),(146,107,1,NULL,1,NULL,0),(147,107,7,NULL,1,NULL,0),(148,107,14,'natiga',1,'d:\\18\\1482021-08-15.docx',1),(149,107,17,NULL,1,NULL,0),(150,108,1,NULL,1,NULL,0),(151,108,7,NULL,1,NULL,0),(152,108,17,NULL,1,NULL,0),(153,108,2,'ssssssss',1,'d:\\17\\1532021-08-15.docx',0),(154,109,1,NULL,1,NULL,0),(155,109,7,NULL,1,NULL,0),(156,110,1,NULL,1,NULL,0),(157,110,7,NULL,1,NULL,0),(158,110,6,NULL,1,NULL,0),(159,110,9,NULL,1,NULL,0),(160,111,1,NULL,1,NULL,0),(161,111,7,NULL,1,NULL,0),(162,111,16,NULL,1,NULL,0),(163,112,1,NULL,1,NULL,0),(164,112,7,NULL,1,NULL,0),(165,112,17,'message ',1,'d:\\20\\1652021-08-15.docx',1),(166,112,10,NULL,1,NULL,0),(167,112,4,NULL,1,NULL,0),(178,114,14,NULL,1,NULL,0),(179,114,6,'eeee',1,'d:\\23\\1792021-08-15.docx',1),(180,114,9,NULL,1,NULL,0),(181,115,7,NULL,1,NULL,0),(182,115,4,NULL,1,NULL,0),(183,115,16,NULL,1,NULL,0),(184,116,7,NULL,1,NULL,0),(185,117,7,NULL,1,'d:\\9\\1852021-08-16.docx',0),(186,117,18,NULL,1,'d:\\9\\1862021-08-16.docx',1),(187,117,1,NULL,1,'d:\\9\\1872021-08-16.docx',1),(188,118,7,'natiga',1,'d:\\9\\1882021-08-17.docx',1),(189,118,1,'natiga',1,NULL,0),(192,118,2,NULL,1,NULL,0),(193,118,6,NULL,1,NULL,0),(194,118,16,NULL,1,NULL,0),(195,119,7,NULL,1,NULL,0),(196,119,1,NULL,1,NULL,0),(197,120,7,'result',1,'d:\\23\\1972021-08-17.docx',0),(198,120,1,'result',1,'d:\\23\\1982021-08-17.docx',0),(199,121,7,NULL,0,NULL,0),(200,122,7,NULL,0,NULL,0),(201,123,7,NULL,0,NULL,0),(202,123,19,NULL,0,NULL,0),(203,124,8,NULL,0,NULL,0),(204,124,5,NULL,0,NULL,0),(205,125,20,NULL,0,NULL,0),(206,125,16,NULL,0,NULL,0),(207,126,19,NULL,1,NULL,0),(208,127,7,NULL,1,'d:\\26\\2082021-08-20.docx',0),(214,129,7,NULL,1,NULL,0),(215,129,19,NULL,1,NULL,0),(216,129,18,NULL,1,NULL,0),(217,129,1,NULL,1,NULL,0),(218,129,10,NULL,0,NULL,0);
/*!40000 ALTER TABLE `lab_order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_test`
--

DROP TABLE IF EXISTS `lab_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_test` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `group` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lab_gr_fk_idx` (`group`),
  CONSTRAINT `lab_gr_fk` FOREIGN KEY (`group`) REFERENCES `lab_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_test`
--

LOCK TABLES `lab_test` WRITE;
/*!40000 ALTER TABLE `lab_test` DISABLE KEYS */;
INSERT INTO `lab_test` VALUES (1,'name Change',1),(2,'test 2 ',2),(3,'test 3 ',3),(4,'test 4 ',4),(5,'test 5 ',6),(6,'test change',3),(7,'test 8',1),(8,'test 9',6),(9,'test 10',3),(10,'test 11',4),(14,'e',10),(15,'test',5),(16,'save new test',11),(17,'messgae test update',13),(18,'a',1),(19,'b',1),(20,'last',17);
/*!40000 ALTER TABLE `lab_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movments`
--

DROP TABLE IF EXISTS `movments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_type` int DEFAULT NULL,
  `pat_id` int DEFAULT NULL,
  `doctor` int DEFAULT NULL,
  `finish` int DEFAULT '0',
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pat_id_fk_idx` (`pat_id`),
  KEY `doctor_fk_idx` (`doctor`),
  CONSTRAINT `doctor_fk` FOREIGN KEY (`doctor`) REFERENCES `users` (`id`),
  CONSTRAINT `pat_id_fk` FOREIGN KEY (`pat_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movments`
--

LOCK TABLES `movments` WRITE;
/*!40000 ALTER TABLE `movments` DISABLE KEYS */;
INSERT INTO `movments` VALUES (142,2,5,4,1,'2021-07-31'),(144,2,17,4,1,'2021-07-31'),(145,2,9,4,1,'2021-07-31'),(147,2,9,4,1,'2021-08-01'),(149,2,17,4,1,'2021-08-02'),(150,2,12,4,1,'2021-08-02'),(152,2,9,4,1,'2021-08-03'),(153,2,10,4,1,'2021-08-03'),(154,2,5,4,1,'2021-08-03'),(157,2,5,4,1,'2021-08-07'),(159,3,9,4,2,'2021-08-07'),(160,2,11,4,1,'2021-08-07'),(164,1,7,4,0,'2021-08-09'),(165,1,10,4,0,'2021-08-09'),(168,2,9,4,1,'2021-08-10'),(169,2,12,4,1,'2021-08-10'),(170,2,11,4,1,'2021-08-10'),(171,2,13,4,1,'2021-08-10'),(172,2,15,4,1,'2021-08-10'),(175,2,12,4,1,'2021-08-11'),(176,2,13,4,1,'2021-08-11'),(177,2,14,4,1,'2021-08-11'),(178,2,16,4,1,'2021-08-11'),(179,2,5,4,1,'2021-08-12'),(180,2,9,4,1,'2021-08-12'),(181,2,7,4,1,'2021-08-12'),(182,2,7,4,1,'2021-08-13'),(184,2,13,4,1,'2021-08-13'),(186,2,9,4,1,'2021-08-13'),(188,1,11,4,0,'2021-08-13'),(189,2,9,4,1,'2021-08-14'),(190,2,13,4,1,'2021-08-14'),(191,2,14,4,1,'2021-08-14'),(192,2,7,4,1,'2021-08-14'),(193,3,11,4,2,'2021-08-14'),(194,4,16,4,2,'2021-08-14'),(195,4,20,4,2,'2021-08-14'),(197,5,21,4,9,'2021-08-15'),(198,5,13,4,9,'2021-08-15'),(199,5,14,4,9,'2021-08-15'),(200,5,18,4,9,'2021-08-15'),(201,5,17,4,9,'2021-08-15'),(202,5,7,4,9,'2021-08-15'),(203,2,11,4,1,'2021-08-15'),(204,2,10,4,1,'2021-08-15'),(205,2,20,4,1,'2021-08-15'),(207,2,23,4,1,'2021-08-15'),(208,3,9,4,2,'2021-08-15'),(209,2,12,4,1,'2021-08-16'),(210,2,9,4,1,'2021-08-16'),(211,2,14,4,1,'2021-08-16'),(212,2,21,4,1,'2021-08-16'),(213,2,18,4,1,'2021-08-16'),(214,2,7,4,1,'2021-08-16'),(215,2,9,4,1,'2021-08-17'),(216,2,12,4,1,'2021-08-17'),(217,2,17,4,1,'2021-08-17'),(218,2,15,4,1,'2021-08-17'),(219,2,19,4,1,'2021-08-17'),(220,2,18,4,1,'2021-08-17'),(221,2,14,4,1,'2021-08-17'),(222,2,16,4,1,'2021-08-17'),(223,5,20,4,9,'2021-08-17'),(224,2,23,4,1,'2021-08-17'),(225,2,12,4,1,'2021-08-18'),(226,1,19,4,0,'2021-08-18'),(227,2,14,4,1,'2021-08-18'),(228,5,14,4,9,'2021-08-19'),(231,5,19,4,9,'2021-08-19'),(232,2,13,4,1,'2021-08-19'),(233,2,21,4,1,'2021-08-19'),(234,4,15,4,2,'2021-08-19'),(235,4,27,4,2,'2021-08-19'),(236,4,26,4,2,'2021-08-19'),(237,2,27,4,1,'2021-08-20'),(238,2,26,4,1,'2021-08-20'),(239,2,25,4,1,'2021-08-20'),(240,2,29,4,1,'2021-08-20'),(241,2,28,4,1,'2021-08-20'),(242,2,12,4,1,'2021-08-20'),(243,2,14,4,1,'2021-08-20'),(244,2,13,4,1,'2021-08-20'),(245,2,23,4,1,'2021-08-20'),(246,2,9,4,1,'2021-08-20'),(247,2,16,4,1,'2021-08-20'),(248,2,11,4,1,'2021-08-20');
/*!40000 ALTER TABLE `movments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pat_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `region` int DEFAULT NULL,
  `entry` date DEFAULT NULL,
  `doctor` int DEFAULT NULL,
  `diagnosis` int DEFAULT NULL,
  `black_list` int DEFAULT '0',
  `icd10` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `surface` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `gender` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pat_id_UNIQUE` (`pat_id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `diagn_fk_idx` (`diagnosis`),
  KEY `region_fk_idx` (`region`),
  CONSTRAINT `diagn_fk` FOREIGN KEY (`diagnosis`) REFERENCES `diagnosis` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `region_fk` FOREIGN KEY (`region`) REFERENCES `region` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (5,'88555','تجربه اسم ','98555','1990-02-20',1,'2021-03-28',4,1,0,NULL,8,88,NULL),(7,'9988','stay alive','987789','2012-01-11',1,'2021-04-04',4,NULL,0,NULL,88,88,NULL),(9,'554','fgdfg54','tytyuu','2000-03-30',1,'2021-04-20',4,NULL,0,NULL,1,120,0),(10,'new id','new name test drug','966555','1990-12-12',1,'2021-05-23',4,1,0,NULL,8,52,NULL),(11,'test code','new name For test','966','1990-12-12',1,'2021-05-23',4,1,0,NULL,5,25,NULL),(12,'69887','today work ','9666666','2019-12-12',1,'2021-07-21',4,NULL,0,NULL,8,56,NULL),(13,'8522','mansour','85222','2020-12-12',1,'2021-07-21',4,NULL,0,NULL,8,52,NULL),(14,'611','ahmed mansiur','9777788','2021-07-06',1,'2021-07-22',4,NULL,0,'test',3,4,1),(15,'555','report','555','2012-12-12',1,'2021-07-23',4,NULL,0,'5',5,5,1),(16,'666','test last report ','66441','2021-06-29',1,'2021-07-23',4,NULL,0,'4',4,4,0),(17,'98','test icd','555','2003-06-18',1,'2021-07-24',4,1,0,'ffffffffff',4,4,1),(18,'888','test ph','63333','2017-07-19',1,'2021-07-28',4,NULL,0,'3er3',3,33,1),(19,'975553','nady maher','966666666','2012-07-20',2,'2021-07-30',4,NULL,0,'3',3,33,1),(20,'99788','lab person','95233','1990-12-12',1,'2021-08-14',4,NULL,0,'123',12,166,1),(21,'6624744','test lab move','33','1990-12-12',1,'2021-08-15',4,NULL,0,'',666,333,1),(22,'99333','مرييض له علاج','66666','1990-12-12',1,'2021-08-15',4,NULL,0,'2',2,2,1),(23,'8228633','مريض تحليل فقط ','66911','1990-12-12',1,'2021-08-15',4,NULL,0,'2',2,2,1),(24,'88','new nam','6555','2019-12-02',1,'2021-08-19',4,NULL,0,'',0,0,0),(25,'99888','mananana','88','2020-12-12',1,'2021-08-19',4,NULL,0,'699',88,666,1),(26,'668','kamel','55','2018-02-21',1,'2021-08-19',4,1,0,'',0,0,1),(27,'6333','كما','52525','2020-12-12',1,'2004-08-04',4,NULL,0,'',88,99,1),(28,'99822577','مريض خارج الاستوك ','963258','2019-12-12',1,'2021-08-20',4,NULL,0,'3',3,0,1),(29,'74665','مريض عادي','852','2012-02-14',1,'2021-08-20',4,NULL,0,'6',6,0,1);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permision`
--

DROP TABLE IF EXISTS `permision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permision` (
  `id` int NOT NULL,
  `users` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permision`
--

LOCK TABLES `permision` WRITE;
/*!40000 ALTER TABLE `permision` DISABLE KEYS */;
INSERT INTO `permision` VALUES (2,NULL),(4,0);
/*!40000 ALTER TABLE `permision` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_detail`
--

DROP TABLE IF EXISTS `prescription_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `drug` int NOT NULL,
  `dose` varchar(50) DEFAULT NULL,
  `fluid` int DEFAULT NULL,
  `volume` int DEFAULT NULL,
  `note` varchar(250) DEFAULT NULL,
  `prescription_no` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `is_checked` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `drug_idx` (`drug`),
  KEY `prescription_no_idx` (`prescription_no`),
  CONSTRAINT `drug` FOREIGN KEY (`drug`) REFERENCES `drugs` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `prescription_no` FOREIGN KEY (`prescription_no`) REFERENCES `prescription_no` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=854 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_detail`
--

LOCK TABLES `prescription_detail` WRITE;
/*!40000 ALTER TABLE `prescription_detail` DISABLE KEYS */;
INSERT INTO `prescription_detail` VALUES (653,77,'4',76,1,NULL,264,'2021-07-31',0),(656,85,'7',76,5,'',262,'2021-07-31',1),(671,92,'5',0,0,'',269,'2021-08-02',1),(672,93,'2',0,0,'',270,'2021-08-02',1),(674,80,'6',0,0,NULL,272,'2021-08-03',1),(682,90,'4',0,0,'',274,'2021-08-03',0),(683,88,'4',0,0,'',274,'2021-08-03',0),(684,87,'6',0,0,'',274,'2021-08-03',0),(685,75,'66',87,100,'nottttttttttttttttttttttttttt',274,'2021-08-03',0),(696,75,'44',90,50,'nottttttttttttttttttttttttttt',279,'2021-08-07',0),(697,77,'44',76,1,NULL,279,'2021-08-07',1),(698,90,'44',0,0,'',279,'2021-08-07',0),(699,78,'55',76,3,NULL,279,'2021-08-07',0),(700,84,'4',0,0,'note',279,'2021-08-07',1),(701,92,'5',0,0,'',279,'2021-08-07',0),(707,75,'4',90,50,'nottttttttttttttttttttttttttt',284,'2021-08-10',0),(708,85,'4',76,5,'',284,'2021-08-10',0),(709,87,'4',0,0,'',284,'2021-08-10',0),(710,78,'4',76,3,NULL,284,'2021-08-10',1),(711,93,'2',0,0,'',285,'2021-08-10',0),(712,77,'4',76,1,NULL,285,'2021-08-10',0),(713,78,'6',76,3,NULL,285,'2021-08-10',0),(714,79,'5',80,5,NULL,285,'2021-08-10',0),(716,85,'8',76,5,'',287,'2021-08-10',1),(717,90,'7',0,0,'',288,'2021-08-10',1),(718,91,'5555',0,0,'NO',286,'2021-08-10',1),(719,85,'5',76,5,'',286,'2021-08-10',0),(720,90,'5',0,0,'',286,'2021-08-10',0),(730,93,'2',0,0,'',291,'2021-08-11',0),(731,77,'4',76,1,NULL,291,'2021-08-11',0),(732,78,'6',76,3,NULL,291,'2021-08-11',0),(733,79,'5',80,5,NULL,291,'2021-08-11',1),(734,85,'8',76,5,'',292,'2021-08-11',0),(735,84,'3',0,0,'note',293,'2021-08-11',0),(736,86,'3',0,0,'test note on add drug ui ',293,'2021-08-11',0),(737,76,'3',0,0,'test note',293,'2021-08-11',1),(738,79,'3',80,5,NULL,293,'2021-08-11',0),(739,86,'3',0,0,'test note on add drug ui ',294,'2021-08-11',0),(740,90,'3',0,0,'',294,'2021-08-11',0),(741,88,'3',0,0,'',294,'2021-08-11',0),(742,75,'3',90,50,'nottttttttttttttttttttttttttt',294,'2021-08-11',1),(743,91,'4',0,0,'NO',295,'2021-08-12',0),(744,90,'4',0,0,'',295,'2021-08-12',0),(745,92,'3',0,0,'',295,'2021-08-12',1),(746,81,'3',0,0,NULL,295,'2021-08-12',0),(747,85,'3',76,5,'',295,'2021-08-12',0),(762,91,'6',0,0,'NO',298,'2021-08-13',1),(763,92,'5',0,0,'',298,'2021-08-13',0),(764,93,'6',0,0,'',298,'2021-08-13',1),(767,85,'8',76,5,'',300,'2021-08-13',0),(768,84,'4',0,0,'note',300,'2021-08-13',1),(769,90,'4',0,0,'',300,'2021-08-13',0),(770,93,'4',0,0,'',300,'2021-08-13',0),(774,75,'4',90,50,'nottttttttttttttttttttttttttt',302,'2021-08-13',0),(775,85,'4',76,5,'',302,'2021-08-13',0),(776,87,'4',0,0,'',302,'2021-08-13',0),(777,78,'4',76,3,NULL,302,'2021-08-13',0),(778,91,'4',0,0,'NO',303,'2021-08-14',1),(779,92,'3',0,0,'',303,'2021-08-14',0),(780,85,'8',76,5,'',304,'2021-08-14',0),(781,84,'4',0,0,'note',304,'2021-08-14',1),(782,90,'4',0,0,'',304,'2021-08-14',0),(783,93,'4',0,0,'',304,'2021-08-14',0),(784,92,'2',0,0,'',305,'2021-08-14',0),(785,87,'2',0,0,'',305,'2021-08-14',1),(786,91,'6',0,0,'NO',306,'2021-08-14',0),(787,92,'5',0,0,'',306,'2021-08-14',0),(788,93,'6',0,0,'',306,'2021-08-14',0),(789,91,'5555',0,0,'NO',307,'2021-08-15',1),(790,85,'5',76,5,'',307,'2021-08-15',0),(791,90,'5',0,0,'',307,'2021-08-15',1),(792,80,'6',0,0,NULL,308,'2021-08-15',1),(793,75,'3',90,50,'nottttttttttttttttttttttttttt',309,'2021-08-15',1),(795,75,'88',90,50,'nottttttttttttttttttttttttttt',311,'2021-08-15',1),(796,91,'4',0,0,'NO',312,'2021-08-16',0),(797,92,'3',0,0,'',312,'2021-08-16',0),(798,93,'2',0,0,'',313,'2021-08-16',0),(799,77,'4',76,1,NULL,313,'2021-08-16',0),(800,78,'6',76,3,NULL,313,'2021-08-16',0),(801,79,'5',80,5,NULL,313,'2021-08-16',0),(802,92,'2',0,0,'',314,'2021-08-16',0),(803,87,'2',0,0,'',314,'2021-08-16',0),(804,75,'ki',90,50,'nottttttttttttttttttttttttttt',315,'2021-08-16',0),(805,91,'6',0,0,'NO',316,'2021-08-16',0),(806,92,'5',0,0,'',316,'2021-08-16',0),(807,93,'6',0,0,'',316,'2021-08-16',0),(808,87,'9',0,0,'',317,'2021-08-16',0),(811,83,'12',0,0,NULL,318,'2021-08-17',0),(812,81,'100',0,0,NULL,318,'2021-08-17',0),(813,83,'88',0,0,NULL,319,'2021-08-17',0),(814,77,'8',76,1,NULL,320,'2021-08-17',0),(815,83,'8',0,0,NULL,320,'2021-08-17',0),(816,83,'6',0,0,NULL,321,'2021-08-17',0),(817,77,'6',76,1,NULL,321,'2021-08-17',0),(818,83,'9',0,0,NULL,322,'2021-08-17',0),(819,92,'2',0,0,'',323,'2021-08-17',0),(820,87,'2',0,0,'',323,'2021-08-17',0),(821,91,'1',0,0,'NO',324,'2021-08-17',0),(822,81,'2',0,0,NULL,324,'2021-08-17',0),(825,91,'8',0,0,'NO',327,'2021-08-17',0),(826,91,'8',0,0,'NO',328,'2021-08-18',0),(827,81,'8',0,0,NULL,329,'2021-08-18',0),(834,84,'1',0,0,'note',334,'2021-08-19',0),(835,91,'5',0,0,'NO',335,'2021-08-19',0),(836,91,'66',0,0,'NO',336,'2021-08-20',1),(837,86,'1',0,0,'test note on add drug ui ',337,'2021-08-20',1),(838,86,'1',0,0,'test note on add drug ui ',338,'2021-08-20',0),(841,91,'2',0,0,'NO',341,'2021-08-20',1),(842,91,'8',0,0,'NO',342,'2021-08-20',1),(843,84,'1',0,0,'note',343,'2021-08-20',1),(844,86,'5',0,0,'test note on add drug ui ',344,'2021-08-20',0),(845,83,'12',0,0,NULL,345,'2021-08-20',0),(846,81,'100',0,0,NULL,345,'2021-08-20',0),(847,91,'5',0,0,'NO',345,'2021-08-20',0),(848,85,'9',76,5,'',346,'2021-08-20',0),(849,86,'44',0,0,'test note on add drug ui ',346,'2021-08-20',0),(850,81,'8',0,0,NULL,347,'2021-08-20',0),(851,91,'8',0,0,'NO',348,'2021-08-20',0),(852,81,'08',0,0,NULL,348,'2021-08-20',0),(853,93,'8',0,0,'',349,'2021-08-20',0);
/*!40000 ALTER TABLE `prescription_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_no`
--

DROP TABLE IF EXISTS `prescription_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_no` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `accept_state` int DEFAULT '0',
  `prepare_time` datetime DEFAULT NULL,
  `screen_time` datetime DEFAULT NULL,
  `is_check` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `prescription_no_idx` (`patient_id`),
  CONSTRAINT `patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=350 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_no`
--

LOCK TABLES `prescription_no` WRITE;
/*!40000 ALTER TABLE `prescription_no` DISABLE KEYS */;
INSERT INTO `prescription_no` VALUES (262,5,'2021-07-31',4,1,'2021-07-31 09:54:36',NULL,1),(264,17,'2021-07-31',4,0,NULL,NULL,1),(269,17,'2021-08-02',4,1,'2021-08-02 20:00:32',NULL,1),(270,12,'2021-08-02',4,1,'2021-08-02 20:00:33',NULL,1),(272,10,'2021-08-03',4,1,'2021-08-03 18:04:32',NULL,1),(274,5,'2021-08-03',4,0,NULL,NULL,1),(279,11,'2021-08-07',4,1,'2021-08-07 19:41:35',NULL,0),(284,9,'2021-08-10',4,0,'2021-08-10 19:08:50',NULL,0),(285,12,'2021-08-10',4,0,'2021-08-10 19:43:39',NULL,0),(286,11,'2021-08-10',4,1,'2021-08-10 20:46:53',NULL,1),(287,13,'2021-08-10',4,1,'2021-08-10 20:39:47',NULL,1),(288,15,'2021-08-10',4,1,'2021-08-10 20:39:49',NULL,1),(291,12,'2021-08-11',4,1,'2021-08-11 09:02:34',NULL,1),(292,13,'2021-08-11',4,1,'2021-08-11 09:02:35',NULL,0),(293,16,'2021-08-11',4,1,'2021-08-11 09:21:20',NULL,1),(294,14,'2021-08-11',4,1,'2021-08-11 09:02:39',NULL,1),(295,5,'2021-08-12',4,1,'2021-08-12 19:03:09',NULL,1),(298,7,'2021-08-13',4,1,'2021-08-13 14:01:54',NULL,1),(300,13,'2021-08-13',4,1,'2021-08-13 14:03:05',NULL,1),(302,9,'2021-08-13',4,0,NULL,NULL,0),(303,9,'2021-08-14',4,1,'2021-08-14 09:13:24',NULL,1),(304,13,'2021-08-14',4,1,'2021-08-14 09:13:30',NULL,1),(305,14,'2021-08-14',4,1,'2021-08-14 10:47:16',NULL,1),(306,7,'2021-08-14',4,0,NULL,NULL,0),(307,11,'2021-08-15',4,1,'2021-08-15 21:09:59',NULL,1),(308,10,'2021-08-15',4,1,'2021-08-15 21:09:01',NULL,1),(309,20,'2021-08-15',4,1,'2021-08-15 21:10:50',NULL,1),(311,23,'2021-08-15',4,1,'2021-08-15 21:10:31',NULL,1),(312,9,'2021-08-16',4,0,'2021-08-16 00:22:59',NULL,0),(313,12,'2021-08-16',4,0,NULL,NULL,0),(314,14,'2021-08-16',4,0,NULL,NULL,0),(315,18,'2021-08-16',4,0,NULL,NULL,0),(316,7,'2021-08-16',4,0,NULL,NULL,0),(317,21,'2021-08-16',4,0,NULL,NULL,0),(318,9,'2021-08-17',4,0,NULL,NULL,0),(319,12,'2021-08-17',4,0,NULL,NULL,0),(320,17,'2021-08-17',4,0,NULL,NULL,0),(321,15,'2021-08-17',4,0,NULL,NULL,0),(322,18,'2021-08-17',4,5,NULL,NULL,0),(323,14,'2021-08-17',4,5,NULL,NULL,0),(324,19,'2021-08-17',4,0,'2021-08-17 19:56:08',NULL,0),(327,23,'2021-08-17',4,5,NULL,NULL,0),(328,12,'2021-08-18',4,0,NULL,NULL,0),(329,14,'2021-08-18',4,0,NULL,NULL,0),(334,13,'2021-08-19',4,0,NULL,NULL,0),(335,21,'2021-08-19',4,5,NULL,NULL,0),(336,28,'2021-08-20',4,1,'2021-08-20 21:03:07','2021-08-20 21:03:19',1),(337,29,'2021-08-20',4,1,'2021-08-20 21:03:08','2021-08-20 21:17:36',1),(338,27,'2021-08-20',4,1,'2021-08-20 20:59:32',NULL,0),(341,12,'2021-08-20',4,1,'2021-08-20 21:03:11','2021-08-20 21:03:53',1),(342,23,'2021-08-20',4,1,'2021-08-20 20:59:29','2021-08-20 21:03:41',1),(343,13,'2021-08-20',4,1,'2021-08-20 21:03:10','2021-08-20 21:03:37',1),(344,25,'2021-08-20',4,1,'2021-08-20 20:58:58',NULL,0),(345,9,'2021-08-20',4,1,'2021-08-20 21:09:42',NULL,0),(346,26,'2021-08-20',4,1,'2021-08-20 21:05:03',NULL,0),(347,14,'2021-08-20',4,1,'2021-08-20 21:06:42',NULL,0),(348,16,'2021-08-20',4,1,'2021-08-20 21:10:42',NULL,0),(349,11,'2021-08-20',4,1,'2021-08-20 21:12:05',NULL,0);
/*!40000 ALTER TABLE `prescription_no` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES (1,'test region'),(2,'tset');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `dept` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dept_fk_idx` (`dept`),
  CONSTRAINT `dept_fk` FOREIGN KEY (`dept`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'ayman','123',NULL),(4,'doctor','12',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-20 21:21:26
