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
  `algorithm` varchar(100) NOT NULL,
  `complexity` varchar(60) NOT NULL,
  `algo_field_id` int(10) NOT NULL,
  `algo_paradigm_id` int(10) NOT NULL,
  PRIMARY KEY (`algorithm_id`),
  UNIQUE KEY `algorithm` (`algorithm`),
  KEY `fk_field_algorithm` (`algo_field_id`),
  KEY `algo_paradigm_id` (`algo_paradigm_id`),
  CONSTRAINT `fk_field_algorithm` FOREIGN KEY (`algo_field_id`) REFERENCES `field_of_study` (`field_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_paradigm_algorithm` FOREIGN KEY (`algo_paradigm_id`) REFERENCES `design_paradigm` (`paradigm_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm`
--

LOCK TABLES `algorithm` WRITE;
/*!40000 ALTER TABLE `algorithm` DISABLE KEYS */;
INSERT INTO `algorithm` VALUES (1,'Kruskal\'s algorithm','O(E*α(V))',12,3),(2,'Prim\'s algorithm','O(E*lg(V))',12,3),(5,'Dijkstra\'s algorithm','O(E*lg(V))',1,3),(6,'Bellman-Ford algorithm','O(V*E)',1,2),(7,'Floyd-Warshall algorithm','O(V^3)',1,2),(8,'Binary search','O(lg(n))',2,1),(14,'Topological sorting','O(V + E)',1,4),(15,'Ford-Fulkerson algorithm','O(E*|f|)',17,4),(17,'Dinic\'s algorithm','O(V^2*E)',17,4),(18,'Push-relabel','O(V^2*E)',17,4),(19,'Improved push-relabel','O(V^3)',17,4),(20,'Relabel-to-front','O(V^3)',17,4),(21,'Quicksort','O(lg(n)) average, O(n^2) worst',3,1),(22,'Merge sort','O(lg(n))',3,1),(23,'0-1 knapsack','O(n*W)',14,2),(24,'Edmonds-Karp algorithm','O(V*E^2)',17,4),(25,'Simplex','exponential',5,4),(27,'A*','exponential worst case',1,3);
/*!40000 ALTER TABLE `algorithm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `algorithm_application`
--

DROP TABLE IF EXISTS `algorithm_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algorithm_application` (
  `app_algorithm_id` int(10) NOT NULL,
  `app_area_id` int(10) NOT NULL,
  PRIMARY KEY (`app_algorithm_id`,`app_area_id`),
  KEY `fk_area_application` (`app_area_id`),
  CONSTRAINT `fk_algorithm_application` FOREIGN KEY (`app_algorithm_id`) REFERENCES `algorithm` (`algorithm_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_area_application` FOREIGN KEY (`app_area_id`) REFERENCES `area_of_use` (`area_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm_application`
--

LOCK TABLES `algorithm_application` WRITE;
/*!40000 ALTER TABLE `algorithm_application` DISABLE KEYS */;
INSERT INTO `algorithm_application` VALUES (1,1),(2,1),(5,2),(27,2);
/*!40000 ALTER TABLE `algorithm_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `algorithm_reference`
--

DROP TABLE IF EXISTS `algorithm_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algorithm_reference` (
  `ref_algorithm_id` int(10) NOT NULL,
  `ref_book_id` int(11) NOT NULL,
  PRIMARY KEY (`ref_algorithm_id`,`ref_book_id`),
  KEY `fk_book_reference` (`ref_book_id`),
  CONSTRAINT `fk_algorithm_reference` FOREIGN KEY (`ref_algorithm_id`) REFERENCES `algorithm` (`algorithm_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_book_reference` FOREIGN KEY (`ref_book_id`) REFERENCES `book` (`book_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algorithm_reference`
--

LOCK TABLES `algorithm_reference` WRITE;
/*!40000 ALTER TABLE `algorithm_reference` DISABLE KEYS */;
INSERT INTO `algorithm_reference` VALUES (1,1);
/*!40000 ALTER TABLE `algorithm_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `area_of_use`
--

DROP TABLE IF EXISTS `area_of_use`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area_of_use` (
  `area_id` int(10) NOT NULL AUTO_INCREMENT,
  `area` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  UNIQUE KEY `area_UNIQUE` (`area`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area_of_use`
--

LOCK TABLES `area_of_use` WRITE;
/*!40000 ALTER TABLE `area_of_use` DISABLE KEYS */;
INSERT INTO `area_of_use` VALUES (1,'Electrical circuit design','Optiman interconnection of electrical pins with minimum amount of wire'),(2,'Shortest path problem','In graph theory, the shortest path problem is the problem of finding a path between two vertices (or nodes) in a graph such that the sum of the weights of its constituent edges is minimized.');
/*!40000 ALTER TABLE `area_of_use` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `author_id` int(10) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'Donald','Knuth'),(2,'Thomas','Cormen'),(3,'Charles','Leiserson'),(4,'Ronald','Rivest'),(5,'Clifford','Stein'),(6,'Thomas','Cut'),(7,'Harry','Truman');
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
  `volume` tinyint(4) DEFAULT NULL,
  `edition` smallint(6) NOT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Introduction to algorithms',NULL,3),(2,'The art of computer programming',1,3),(3,'The art of computer programming',2,3),(4,'The art of computer programming',3,2);
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
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`paradigm_id`),
  UNIQUE KEY `paradigm` (`paradigm`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `design_paradigm`
--

LOCK TABLES `design_paradigm` WRITE;
/*!40000 ALTER TABLE `design_paradigm` DISABLE KEYS */;
INSERT INTO `design_paradigm` VALUES (1,'Divide and conquer',NULL),(2,'Dynamic programming',NULL),(3,'Greedy strategy',NULL),(4,'No design paradigm','Algorithm conforms to no design paradigm'),(16,'Brute force','Brute force is one of the easiest and straight forward approach to solve a problem. \nBased on trying all possible solutions.\nGenerally most expensive approach.\nExamples: Sequential search, factors of number.'),(17,'Backtracking','Backtracking is a technique used to solve problems with a large search space, by systematically trying and eliminating possibilities.\nBased on depth-first recursive search Examples : Find path through maze, eight queens problem.'),(18,'dbdfbfjdbdf',NULL),(19,'cbdfjhbdfjbfdj',NULL);
/*!40000 ALTER TABLE `design_paradigm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_of_study`
--

DROP TABLE IF EXISTS `field_of_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_of_study` (
  `field_id` int(10) NOT NULL AUTO_INCREMENT,
  `field` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `field` (`field`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_of_study`
--

LOCK TABLES `field_of_study` WRITE;
/*!40000 ALTER TABLE `field_of_study` DISABLE KEYS */;
INSERT INTO `field_of_study` VALUES (1,'Graph theory',NULL),(2,'Searching',NULL),(3,'Sorting',NULL),(4,'Number theory',NULL),(5,'Linear programming',NULL),(6,'Matrix operations',NULL),(7,'Multithreaded algorithms',NULL),(8,'Computational geometry',NULL),(9,'String algorithms',NULL),(10,'Approximation algorithms',NULL),(11,'Data structures',NULL),(12,'Combinatorial optimization',NULL),(13,'No field of study','Algorithm belongs to no modern field of study'),(14,'Optimization',NULL),(16,'Combinatorics',NULL),(17,'Flow networks',NULL);
/*!40000 ALTER TABLE `field_of_study` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `txtbk_book_id` int(10) NOT NULL,
  `txtbk_author_id` int(10) NOT NULL,
  PRIMARY KEY (`txtbk_book_id`,`txtbk_author_id`),
  KEY `fk_author_textbook` (`txtbk_author_id`),
  CONSTRAINT `fk_author_textbook` FOREIGN KEY (`txtbk_author_id`) REFERENCES `author` (`author_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_book_textbook` FOREIGN KEY (`txtbk_book_id`) REFERENCES `book` (`book_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (2,1),(3,1),(4,1),(1,2),(1,3),(1,4),(1,5);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-17 12:49:06
