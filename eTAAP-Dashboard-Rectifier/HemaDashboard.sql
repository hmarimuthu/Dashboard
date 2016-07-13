-- MySQL dump 10.13  Distrib 5.7.11, for osx10.9 (x86_64)
--
-- Host: localhost    Database: dashboard
-- ------------------------------------------------------
-- Server version	5.7.11

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
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `app_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  `default_env_id` int(11) DEFAULT NULL,
  `default_bed_id` int(11) DEFAULT NULL,
  `default_suite_id` int(11) DEFAULT NULL,
  `quarter_starting_month_id` int(11) DEFAULT NULL,
  `quarter_starting_month_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (8,'NDB','2016-04-29 13:19:39','2016-04-29 15:10:14',1,0,0,0,4,'April');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_system_map`
--

DROP TABLE IF EXISTS `application_system_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_system_map` (
  `map_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `sys_id` int(11) DEFAULT NULL,
  `url_alias` varchar(255) DEFAULT NULL,
  `env_id` int(11) DEFAULT NULL,
  `suite_id` int(11) DEFAULT NULL,
  `bed_id` int(11) DEFAULT NULL,
  `is_default` int(11) DEFAULT NULL,
  `is_active` int(11) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_system_map`
--

LOCK TABLES `application_system_map` WRITE;
/*!40000 ALTER TABLE `application_system_map` DISABLE KEYS */;
INSERT INTO `application_system_map` VALUES (13,8,8,'SPlunkPoc',4,2,2,1,1,'QA'),(16,8,7,'4',0,0,0,0,1,'Dev'),(17,8,7,'EBAY',4,0,0,1,1,'QA');
/*!40000 ALTER TABLE `application_system_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field`
--

DROP TABLE IF EXISTS `custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field` (
  `custom_field_id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_id` int(11) DEFAULT NULL,
  `custom_field_key` varchar(255) DEFAULT NULL,
  `custom_field_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`custom_field_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field`
--

LOCK TABLES `custom_field` WRITE;
/*!40000 ALTER TABLE `custom_field` DISABLE KEYS */;
INSERT INTO `custom_field` VALUES (19,7,'customfield_10402','Environment'),(20,7,'customfield_10301','Severity');
/*!40000 ALTER TABLE `custom_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `environment`
--

DROP TABLE IF EXISTS `environment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `environment` (
  `env_id` int(11) NOT NULL AUTO_INCREMENT,
  `env_name` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`env_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `environment`
--

LOCK TABLES `environment` WRITE;
/*!40000 ALTER TABLE `environment` DISABLE KEYS */;
INSERT INTO `environment` VALUES (3,'Dev','2016-04-29 13:06:32','2016-04-29 13:06:32',1),(4,'QA','2016-04-29 13:06:39','2016-04-29 13:06:39',1);
/*!40000 ALTER TABLE `environment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jenkins`
--

DROP TABLE IF EXISTS `jenkins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenkins` (
  `app_id` int(11) NOT NULL,
  `env_id` int(11) NOT NULL,
  `suite_id` int(11) NOT NULL,
  `bed_id` int(11) NOT NULL,
  `build_id` varchar(255) DEFAULT NULL,
  `build_name` varchar(255) DEFAULT NULL,
  `build_number` int(11) NOT NULL,
  `build_url` varchar(255) DEFAULT NULL,
  `build_date` datetime DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `fail_count` int(11) DEFAULT NULL,
  `pass_count` int(11) DEFAULT NULL,
  `skip_count` int(11) DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenkins`
--

LOCK TABLES `jenkins` WRITE;
/*!40000 ALTER TABLE `jenkins` DISABLE KEYS */;
INSERT INTO `jenkins` VALUES (8,4,2,2,'18','SPlunkPoc #18',18,'http://localhost:8080/job/SPlunkPoc/18/','2016-04-25 11:43:26','UNSTABLE',1,0,0,1),(8,4,2,2,'17','SPlunkPoc #17',17,'http://localhost:8080/job/SPlunkPoc/17/','2016-04-19 10:17:43','UNSTABLE',1,0,0,1);
/*!40000 ALTER TABLE `jenkins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira`
--

DROP TABLE IF EXISTS `jira`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `env_id` int(11) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `active` int(11) DEFAULT '1',
  `key` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `severity` varchar(255) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `issue_type` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira`
--

LOCK TABLES `jira` WRITE;
/*!40000 ALTER TABLE `jira` DISABLE KEYS */;
INSERT INTO `jira` VALUES (1,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(2,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(3,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(4,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(5,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(6,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(7,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-22 00:00:00','2016-04-22 00:00:00'),(8,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(9,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(10,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(11,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(12,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(13,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(14,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(15,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(16,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(17,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(18,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(19,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(20,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(21,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(22,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(23,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(24,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(25,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(26,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(27,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(28,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(29,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(30,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(31,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(32,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(33,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(34,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(35,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(36,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(37,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(38,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(39,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(40,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-20 00:00:00','2016-04-20 00:00:00'),(41,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P1: High','Open','Bug','2016-04-14 00:00:00','2016-04-14 00:00:00'),(42,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Minor','P2: Medium','Verify','Bug','2016-04-14 00:00:00','2016-04-14 00:00:00'),(43,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Verify','Bug','2015-11-05 00:00:00','2016-04-14 00:00:00'),(44,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Verify','Bug','2015-11-05 00:00:00','2016-04-14 00:00:00'),(45,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Minor','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(46,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(47,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(48,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(49,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(50,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Major','P2: Medium','Open','Bug','2015-11-05 00:00:00','2015-11-05 00:00:00'),(51,8,4,'2016-04-29 15:11:35','2016-04-29 15:11:35',1,'','EBAYPOC','Blocker','P2: Medium','Open','Bug','2015-11-04 00:00:00','2015-11-04 00:00:00');
/*!40000 ALTER TABLE `jira` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira_sprint`
--

DROP TABLE IF EXISTS `jira_sprint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira_sprint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `rapidview_id` int(11) DEFAULT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `sprint_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `start_datetime_mili` bigint(50) DEFAULT '0',
  `end_datetime_mili` bigint(50) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `appIdRapidviewIdSprintId` (`app_id`,`rapidview_id`,`sprint_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira_sprint`
--

LOCK TABLES `jira_sprint` WRITE;
/*!40000 ALTER TABLE `jira_sprint` DISABLE KEYS */;
INSERT INTO `jira_sprint` VALUES (1,8,'2016-04-29 14:43:56','2016-04-29 15:11:36',4,1,'September2015','CLOSED','2015-08-26 10:00:00','2015-09-16 10:00:00',NULL,1440583200993,1442397600000);
/*!40000 ALTER TABLE `jira_sprint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira_storypoint`
--

DROP TABLE IF EXISTS `jira_storypoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira_storypoint` (
  `userstory_id` int(11) DEFAULT NULL,
  `increment` int(11) DEFAULT NULL,
  `decrement` int(11) DEFAULT NULL,
  `status_code` int(11) DEFAULT NULL,
  `isAdded` tinyint(4) DEFAULT '0',
  `isDeleted` tinyint(4) DEFAULT '0',
  `status_message` varchar(255) DEFAULT NULL,
  `activity_mili` bigint(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira_storypoint`
--

LOCK TABLES `jira_storypoint` WRITE;
/*!40000 ALTER TABLE `jira_storypoint` DISABLE KEYS */;
INSERT INTO `jira_storypoint` VALUES (1,0,0,10002,0,0,NULL,1440432337000),(1,0,0,0,1,0,'Scope change.<br>Issue added to sprint.',1440505926494),(1,432000,0,0,0,0,'Scope change.<br>Estimate of 432000 has been added.<br>Story Points: 432000',1440514810695),(1,0,0,10000,0,0,'Scope change.<br>Issue created.',1445945742000);
/*!40000 ALTER TABLE `jira_storypoint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira_userstory`
--

DROP TABLE IF EXISTS `jira_userstory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira_userstory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `rapidview_id` int(11) DEFAULT NULL,
  `rapidview_name` varchar(255) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `jira_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira_userstory`
--

LOCK TABLES `jira_userstory` WRITE;
/*!40000 ALTER TABLE `jira_userstory` DISABLE KEYS */;
INSERT INTO `jira_userstory` VALUES (1,8,'2016-04-29 14:43:56','2016-04-29 15:11:36',4,NULL,'ETAAPDEV-90','Keyword Driven Framework - v0.1','P2: Medium','Done',1,NULL,13416);
/*!40000 ALTER TABLE `jira_userstory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira_userstory_status`
--

DROP TABLE IF EXISTS `jira_userstory_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira_userstory_status` (
  `userstory_id` int(11) NOT NULL,
  `status_code` int(11) DEFAULT NULL,
  `status_datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira_userstory_status`
--

LOCK TABLES `jira_userstory_status` WRITE;
/*!40000 ALTER TABLE `jira_userstory_status` DISABLE KEYS */;
INSERT INTO `jira_userstory_status` VALUES (1,10000,'2015-10-27 11:35:42');
/*!40000 ALTER TABLE `jira_userstory_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira_velocity`
--

DROP TABLE IF EXISTS `jira_velocity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira_velocity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `rapidview_id` int(11) DEFAULT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `estimated` int(11) DEFAULT NULL,
  `completed` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `appIdRapidviewIdSprintId` (`app_id`,`rapidview_id`,`sprint_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira_velocity`
--

LOCK TABLES `jira_velocity` WRITE;
/*!40000 ALTER TABLE `jira_velocity` DISABLE KEYS */;
INSERT INTO `jira_velocity` VALUES (1,8,'2016-04-29 14:43:56','2016-04-29 15:11:36',4,1,432000,432000,NULL);
/*!40000 ALTER TABLE `jira_velocity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `priority`
--

DROP TABLE IF EXISTS `priority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `priority` (
  `priority_id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_id` int(11) DEFAULT NULL,
  `priority_name` varchar(255) DEFAULT NULL,
  `equivalent_to` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`priority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `priority`
--

LOCK TABLES `priority` WRITE;
/*!40000 ALTER TABLE `priority` DISABLE KEYS */;
INSERT INTO `priority` VALUES (19,7,'P1: High','P1'),(20,7,'P2: Medium','P2');
/*!40000 ALTER TABLE `priority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `user_role_UNIQUE` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (3,'admin'),(1,'guest'),(2,'user');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedulerjobs`
--

DROP TABLE IF EXISTS `schedulerjobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedulerjobs` (
  `pk_jobId` bigint(20) NOT NULL AUTO_INCREMENT,
  `jobName` varchar(200) NOT NULL,
  `api_name` varchar(50) NOT NULL,
  `job_interval` int(2) NOT NULL,
  `job_status` varchar(20) NOT NULL,
  PRIMARY KEY (`pk_jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedulerjobs`
--

LOCK TABLES `schedulerjobs` WRITE;
/*!40000 ALTER TABLE `schedulerjobs` DISABLE KEYS */;
INSERT INTO `schedulerjobs` VALUES (1,'Jenkins','JenkinsDataPullAPI',6,'1'),(2,'Jira','JiraDataPullAPI',6,'1');
/*!40000 ALTER TABLE `schedulerjobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedulerjobsrecords`
--

DROP TABLE IF EXISTS `schedulerjobsrecords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedulerjobsrecords` (
  `pk_recordId` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_jobId` bigint(20) NOT NULL,
  `executionDateValue` varchar(50) NOT NULL,
  `currentdate` datetime NOT NULL,
  `status` varchar(50) NOT NULL,
  `log` text,
  PRIMARY KEY (`pk_recordId`),
  KEY `fk_jobId` (`fk_jobId`),
  CONSTRAINT `schedulerJobsRecords_ibfk_1` FOREIGN KEY (`fk_jobId`) REFERENCES `schedulerjobs` (`pk_jobId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedulerjobsrecords`
--

LOCK TABLES `schedulerjobsrecords` WRITE;
/*!40000 ALTER TABLE `schedulerjobsrecords` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedulerjobsrecords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_id` int(11) DEFAULT NULL,
  `status_name` varchar(255) DEFAULT NULL,
  `equivalent_to` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (57,7,'Open','New'),(58,7,'Closed','Closed'),(59,7,'In Progress','In Progress'),(60,7,'Verify','Verify');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_api`
--

DROP TABLE IF EXISTS `system_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_api` (
  `sys_id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_name` varchar(255) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `api_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  `is_dev` int(11) DEFAULT '0',
  `is_qa` int(11) DEFAULT '0',
  `is_operations` int(11) DEFAULT '0',
  PRIMARY KEY (`sys_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_api`
--

LOCK TABLES `system_api` WRITE;
/*!40000 ALTER TABLE `system_api` DISABLE KEYS */;
INSERT INTO `system_api` VALUES (6,'jira_intuit',2,'Jira','https://jira.intuit.com','nbheda','RBRFZVV17PwqeMVaWcfRsQ==','2015-09-24 10:41:39','2015-09-24 10:41:39',1,1,1,0),(7,'Jira_etouch',2,'Jira','https://etouch.atlassian.net','rpandey','hJI2iy1YRn8vjIn78jJA/g==','2015-09-24 10:42:46','2016-04-29 13:46:06',1,0,1,0),(8,'Jenkins',1,'Jenkins','http://127.0.0.1:8080','','','2015-09-24 10:43:10','2016-04-29 13:18:16',1,0,1,0);
/*!40000 ALTER TABLE `system_api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tcm`
--

DROP TABLE IF EXISTS `tcm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tcm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `suite_id` int(11) DEFAULT NULL,
  `test_case_type` varchar(255) DEFAULT NULL,
  `test_case_count` int(11) DEFAULT NULL,
  `quarter_start_date` datetime DEFAULT NULL,
  `quarter_end_date` datetime DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tcm`
--

LOCK TABLES `tcm` WRITE;
/*!40000 ALTER TABLE `tcm` DISABLE KEYS */;
INSERT INTO `tcm` VALUES (3,8,2,'Automated',8,'2016-04-01 00:00:00','2016-06-30 23:59:59','2016-04-29 15:14:50','2016-04-29 15:14:50',1),(4,8,2,'Manual',10,'2016-04-01 00:00:00','2016-06-30 23:59:59','2016-04-29 15:14:50','2016-04-29 15:14:50',1);
/*!40000 ALTER TABLE `tcm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_bed`
--

DROP TABLE IF EXISTS `test_bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_bed` (
  `bed_id` int(11) NOT NULL AUTO_INCREMENT,
  `bed_name` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`bed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_bed`
--

LOCK TABLES `test_bed` WRITE;
/*!40000 ALTER TABLE `test_bed` DISABLE KEYS */;
INSERT INTO `test_bed` VALUES (2,'Chrome','2016-04-29 13:07:09','2016-04-29 13:07:09',1);
/*!40000 ALTER TABLE `test_bed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suite`
--

DROP TABLE IF EXISTS `test_suite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suite` (
  `suite_id` int(11) NOT NULL AUTO_INCREMENT,
  `suite_name` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`suite_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suite`
--

LOCK TABLES `test_suite` WRITE;
/*!40000 ALTER TABLE `test_suite` DISABLE KEYS */;
INSERT INTO `test_suite` VALUES (2,'Smoke','2016-04-29 13:06:58','2016-04-29 13:06:58',1);
/*!40000 ALTER TABLE `test_suite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_period`
--

DROP TABLE IF EXISTS `time_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_period` (
  `period_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `month_id` int(11) NOT NULL,
  `month_name` varchar(255) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`period_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_period`
--

LOCK TABLES `time_period` WRITE;
/*!40000 ALTER TABLE `time_period` DISABLE KEYS */;
/*!40000 ALTER TABLE `time_period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user_roles_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_roles_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `role_id_idx` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1,3),(2,2,2),(3,3,3),(4,4,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `password` varchar(65) NOT NULL,
  `fname` varchar(45) NOT NULL,
  `lname` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (3,'admin','70fe0348b3d0a73121232f297a57a5a743894a0e4a801fc3','admin','admin');
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

-- Dump completed on 2016-05-02 11:25:09
