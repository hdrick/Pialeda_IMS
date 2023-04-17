-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: pialeda_db
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cashier` varchar(255) DEFAULT NULL,
  `client_address` varchar(255) DEFAULT NULL,
  `client_bus_style` varchar(255) DEFAULT NULL,
  `invoice_client_contact_person` varchar(255) NOT NULL,
  `client_name` varchar(255) DEFAULT NULL,
  `client_terms` varchar(255) DEFAULT NULL,
  `client_tin` varchar(255) DEFAULT NULL,
  `invoice_date_created` varchar(255) NOT NULL,
  `grand_total` double NOT NULL,
  `supplier_invoice_number` varchar(255) NOT NULL,
  `invoice_purchase_order_number` varchar(255) NOT NULL,
  `supplier_address` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `supplier_tin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'Hendwrick Gonzales','123 Main Street, Suite 100, Anytown, USA 12345, General Trias','NA','Jane Doe, Accounts Payable Manager','XYZ Corporation','NA','123-456-789','March 12, 2023',87000,'INV-23123-2346361','PO-1678633937532-610','28th Floor Antel Global Corporate Center, Julia Vargas Ave., Ortigas Center San Antonio, 1605 City of Passig NCR, Second District Philippines','AXENCARE CORP.','608-127289-00000'),(2,'Daniel Dimen','233 Main Street, Aguinaldo 100, Lancaster, USA 2345, Makati City','','Ronnie Doe, Accounts Payable Manager','ABC Inc.','','452-123-098','March 12, 2023',32000,'INV-23123-7893922','PO-1678634149918-4162','28th Floor Antel Global Corporate Center, Julia Vargas Ave., Ortigas Center San Antonio, 1605 City of Passig NCR, Second District Philippines','AXENCARE CORP.','608-127289-00000');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-13  0:54:21
