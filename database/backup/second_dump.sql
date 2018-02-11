-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: algorithms
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `algorithm`
--

DROP TABLE IF EXISTS `algorithm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algorithm` (
  `algorithm_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `complexity` varchar(60) NOT NULL,
  `algo_paradigm_id` int(10) NOT NULL,
  `algo_implementation_id` int(10) NOT NULL,
  PRIMARY KEY (`algorithm_id`),
  KEY `algo_paradigm_id` (`algo_paradigm_id`),
  KEY `algo_implementation_id` (`algo_implementation_id`),
  CONSTRAINT `algorithm_ibfk_1` FOREIGN KEY (`algo_paradigm_id`) REFERENCES `design_paradigm` (`paradigm_id`),
  CONSTRAINT `algorithm_ibfk_2` FOREIGN KEY (`algo_implementation_id`) REFERENCES `implementation_type` (`implementation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm`
--

LOCK TABLES `algorithm` WRITE;
/*!40000 ALTER TABLE `algorithm` DISABLE KEYS */;
/*!40000 ALTER TABLE `algorithm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `algorithm_application`
--

DROP TABLE IF EXISTS `algorithm_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algorithm_application` (
  `application_id` int(10) NOT NULL AUTO_INCREMENT,
  `app_algorithm_id` int(10) NOT NULL,
  `app_field_id` int(10) NOT NULL,
  PRIMARY KEY (`application_id`),
  KEY `app_algorithm_id` (`app_algorithm_id`),
  KEY `app_field_id` (`app_field_id`),
  CONSTRAINT `algorithm_application_ibfk_1` FOREIGN KEY (`app_algorithm_id`) REFERENCES `algorithm` (`algorithm_id`),
  CONSTRAINT `algorithm_application_ibfk_2` FOREIGN KEY (`app_field_id`) REFERENCES `field` (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm_application`
--

LOCK TABLES `algorithm_application` WRITE;
/*!40000 ALTER TABLE `algorithm_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `algorithm_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `algorithm_reference`
--

DROP TABLE IF EXISTS `algorithm_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algorithm_reference` (
  `reference_id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_algorithm_id` int(10) NOT NULL,
  `ref_textbook_id` int(10) NOT NULL,
  PRIMARY KEY (`reference_id`),
  KEY `ref_algorithm_id` (`ref_algorithm_id`),
  KEY `ref_textbook_id` (`ref_textbook_id`),
  CONSTRAINT `algorithm_reference_ibfk_1` FOREIGN KEY (`ref_algorithm_id`) REFERENCES `algorithm` (`algorithm_id`),
  CONSTRAINT `ref_textbook_id` FOREIGN KEY (`ref_textbook_id`) REFERENCES `textbook` (`textbook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm_reference`
--

LOCK TABLES `algorithm_reference` WRITE;
/*!40000 ALTER TABLE `algorithm_reference` DISABLE KEYS */;
/*!40000 ALTER TABLE `algorithm_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `author_id` int(10) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `book_id` int(10) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `year_published` smallint(6) NOT NULL,
  `edition` smallint(6) NOT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `design_paradigm`
--

DROP TABLE IF EXISTS `design_paradigm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `design_paradigm` (
  `paradigm_id` int(10) NOT NULL AUTO_INCREMENT,
  `paradigm` varchar(50) NOT NULL,
  PRIMARY KEY (`paradigm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `design_paradigm`
--

LOCK TABLES `design_paradigm` WRITE;
/*!40000 ALTER TABLE `design_paradigm` DISABLE KEYS */;
/*!40000 ALTER TABLE `design_paradigm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `field_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `implementation_type`
--

DROP TABLE IF EXISTS `implementation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `implementation_type` (
  `implementation_id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`implementation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `implementation_type`
--

LOCK TABLES `implementation_type` WRITE;
/*!40000 ALTER TABLE `implementation_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `implementation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `textbook`
--

DROP TABLE IF EXISTS `textbook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `textbook` (
  `textbook_id` int(10) NOT NULL AUTO_INCREMENT,
  `txtbk_book_id` int(10) NOT NULL,
  `txtbk_author_id` int(10) NOT NULL,
  PRIMARY KEY (`textbook_id`),
  KEY `txtbk_book_id` (`txtbk_book_id`),
  KEY `txtbk_author_id` (`txtbk_author_id`),
  CONSTRAINT `textbook_ibfk_1` FOREIGN KEY (`txtbk_book_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `textbook_ibfk_2` FOREIGN KEY (`txtbk_author_id`) REFERENCES `author` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `textbook`
--

LOCK TABLES `textbook` WRITE;
/*!40000 ALTER TABLE `textbook` DISABLE KEYS */;
/*!40000 ALTER TABLE `textbook` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-02 17:29:21
