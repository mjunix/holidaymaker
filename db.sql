-- MySQL dump 10.13  Distrib 5.7.29, for Win64 (x86_64)
--
-- Host: localhost    Database: holidaymaker
-- ------------------------------------------------------
-- Server version	5.7.29-log

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
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customers` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `city` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `country` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (61,'Homer','homer@example.com','12345','12 Fake Street','Springfield','USA'),(62,'Bart','bart@example.com','12345678','32 Fake Street','Springfield','USA'),(63,'Lisa','lisa@example.com','12345','21 Fake Street','Springfield','USA'),(64,'Maggie','maggie@example.com','12345','46 Fake Street','Springfield','USA'),(65,'Marge','marge@example.com','12345','27 Fake Street','Springfield','USA');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facility_profiles`
--

DROP TABLE IF EXISTS `facility_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `facility_profiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pool` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `evening_entertainment` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `kids_club` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `restaurant` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facility_profiles`
--

LOCK TABLES `facility_profiles` WRITE;
/*!40000 ALTER TABLE `facility_profiles` DISABLE KEYS */;
INSERT INTO `facility_profiles` VALUES (1,1,1,0,1),(2,1,1,1,1);
/*!40000 ALTER TABLE `facility_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotels`
--

DROP TABLE IF EXISTS `hotels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hotels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `city` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `country` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `facility_profile` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_facility_profile_idx` (`facility_profile`),
  CONSTRAINT `fk_hotels_facility_profiles` FOREIGN KEY (`facility_profile`) REFERENCES `facility_profiles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'The Ritz','150 Piccadilly','London','United Kingdom',1),(2,'Waldorf Astoria','	301 Park Avenue, Manhattan','New York City','United States of America',1),(3,'Plaza Hotel','768 Fifth Avenue, Manhattan','New York City','United States of America',1),(4,'Savoy Hotel','The Strand','London','United Kingdom',2),(5,'Grand Hôtel','Södra Blasieholmshamnen 8','Stockholm','Sweden',2),(6,'Upper House','Mässans gata 24','Gothenburg','Sweden',2);
/*!40000 ALTER TABLE `hotels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `customer` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_customer_idx` (`customer`),
  CONSTRAINT `fk_reservations_customers` FOREIGN KEY (`customer`) REFERENCES `customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (15,'2020-06-01','2020-06-07',62),(16,'2020-06-03','2020-06-12',65),(17,'2020-06-28','2020-07-02',61),(18,'2020-07-05','2020-07-10',63),(19,'2020-07-15','2020-07-22',61),(20,'2020-07-01','2020-07-31',64),(21,'2020-07-20','2020-07-30',61),(22,'2020-07-22','2020-07-29',63),(23,'2020-07-27','2020-07-28',62);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_reservations`
--

DROP TABLE IF EXISTS `room_reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room_reservations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `room` int(10) unsigned NOT NULL,
  `reservation` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_room_idx` (`room`),
  KEY `fk_room_reservations_rooms_idx` (`room`),
  KEY `fk_room_reservations_bookings_idx` (`reservation`),
  CONSTRAINT `fk_room_reservations_reservations` FOREIGN KEY (`reservation`) REFERENCES `reservations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_room_reservations_rooms` FOREIGN KEY (`room`) REFERENCES `rooms` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_reservations`
--

LOCK TABLES `room_reservations` WRITE;
/*!40000 ALTER TABLE `room_reservations` DISABLE KEYS */;
INSERT INTO `room_reservations` VALUES (3,29,15),(4,30,15),(5,31,15),(6,35,16),(7,11,17),(8,23,18),(9,24,18),(10,27,18),(11,5,19),(12,3,19),(13,2,19),(14,1,19),(15,16,20),(16,17,20),(17,18,20),(18,15,20),(19,4,21),(20,7,21),(21,36,22),(22,37,22),(23,40,22),(24,38,23),(25,39,23),(26,41,23),(27,42,23);
/*!40000 ALTER TABLE `room_reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `room_reservations_with_dates`
--

DROP TABLE IF EXISTS `room_reservations_with_dates`;
/*!50001 DROP VIEW IF EXISTS `room_reservations_with_dates`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `room_reservations_with_dates` AS SELECT 
 1 AS `start_date`,
 1 AS `end_date`,
 1 AS `room_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `room_types`
--

DROP TABLE IF EXISTS `room_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room_types` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `designation` varchar(255) COLLATE utf8mb4_swedish_ci NOT NULL,
  `max_num_guests` int(10) unsigned NOT NULL,
  `price_per_night` decimal(10,2) unsigned NOT NULL,
  `hotel` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_room_types_hotels_idx` (`hotel`),
  CONSTRAINT `fk_room_types_hotels` FOREIGN KEY (`hotel`) REFERENCES `hotels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_types`
--

LOCK TABLES `room_types` WRITE;
/*!40000 ALTER TABLE `room_types` DISABLE KEYS */;
INSERT INTO `room_types` VALUES (1,'Single',1,2000.00,1),(2,'Double',2,4000.00,1),(3,'Triple',3,6000.00,1),(4,'Quad',4,8000.00,1),(5,'Single',1,4000.00,2),(6,'Double',2,8000.00,2),(7,'Triple',3,12000.00,2),(8,'Quad',4,16000.00,2),(9,'Single',1,3000.00,3),(10,'Double',2,6000.00,3),(11,'Triple',3,9000.00,3),(12,'Quad',4,12000.00,3),(13,'Single',1,2500.00,4),(14,'Double',2,5000.00,4),(15,'Triple',3,7500.00,4),(16,'Quad',4,10000.00,4),(17,'Single',1,1000.00,5),(18,'Double',2,2000.00,5),(19,'Triple',3,3000.00,5),(20,'Quad',4,4000.00,5),(21,'Double',2,2500.00,6);
/*!40000 ALTER TABLE `room_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rooms` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `room_number` int(10) unsigned NOT NULL,
  `room_type` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rooms_room_types_idx` (`room_type`),
  CONSTRAINT `fk_rooms_room_types` FOREIGN KEY (`room_type`) REFERENCES `room_types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,1,1),(2,2,2),(3,3,2),(4,4,2),(5,5,3),(6,6,4),(7,7,4),(8,1,6),(9,2,6),(10,3,7),(11,4,8),(12,5,8),(13,6,8),(14,7,8),(15,1,9),(16,2,10),(17,3,10),(18,4,10),(19,5,11),(20,6,12),(21,7,12),(22,1,13),(23,2,14),(24,3,14),(25,4,16),(26,5,16),(27,6,16),(28,7,16),(29,1,17),(30,2,18),(31,3,18),(32,4,18),(33,5,18),(34,6,20),(35,7,20),(36,1,21),(37,2,21),(38,3,21),(39,4,21),(40,5,21),(41,6,21),(42,7,21);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `rooms_with_room_types`
--

DROP TABLE IF EXISTS `rooms_with_room_types`;
/*!50001 DROP VIEW IF EXISTS `rooms_with_room_types`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `rooms_with_room_types` AS SELECT 
 1 AS `id`,
 1 AS `room_number`,
 1 AS `max_num_guests`,
 1 AS `designation`,
 1 AS `hotel`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `room_reservations_with_dates`
--

/*!50001 DROP VIEW IF EXISTS `room_reservations_with_dates`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `room_reservations_with_dates` AS select `reservations`.`start_date` AS `start_date`,`reservations`.`end_date` AS `end_date`,`room_reservations`.`room` AS `room_id` from (`reservations` join `room_reservations` on((`reservations`.`id` = `room_reservations`.`reservation`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `rooms_with_room_types`
--

/*!50001 DROP VIEW IF EXISTS `rooms_with_room_types`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `rooms_with_room_types` AS select `rooms`.`id` AS `id`,`rooms`.`room_number` AS `room_number`,`room_types`.`max_num_guests` AS `max_num_guests`,`room_types`.`designation` AS `designation`,`room_types`.`hotel` AS `hotel` from (`rooms` join `room_types` on((`rooms`.`room_type` = `room_types`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-23 22:45:17
