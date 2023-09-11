-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: pialedadb
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `client_address` varchar(255) NOT NULL,
  `client_agent` varchar(255) NOT NULL,
  `bus_style` varchar(255) DEFAULT NULL,
  `client_city_address` varchar(255) NOT NULL,
  `client_name` varchar(255) NOT NULL,
  `terms` varchar(255) DEFAULT NULL,
  `client_tin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'sadasda','dresc','construction','aswe','testclient','NA','221412');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection_receipt`
--

DROP TABLE IF EXISTS `collection_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection_receipt` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `amount_due` double NOT NULL,
  `cash` varchar(255) DEFAULT NULL,
  `cashier_name` varchar(255) DEFAULT NULL,
  `check_no` varchar(255) DEFAULT NULL,
  `client_address` varchar(255) DEFAULT NULL,
  `client_bus` varchar(255) DEFAULT NULL,
  `client_payment` double NOT NULL,
  `client_sum_of` varchar(255) DEFAULT NULL,
  `client_tin` varchar(255) DEFAULT NULL,
  `collection_receipt_date` varchar(255) DEFAULT NULL,
  `collection_receipt_num` bigint NOT NULL,
  `ewt` double NOT NULL,
  `partial_payment_for` varchar(255) DEFAULT NULL,
  `recv_from` varchar(255) DEFAULT NULL,
  `supplier_address` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `supplier_tin` varchar(255) DEFAULT NULL,
  `total` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection_receipt`
--

LOCK TABLES `collection_receipt` WRITE;
/*!40000 ALTER TABLE `collection_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection_receipt_list_invoice`
--

DROP TABLE IF EXISTS `collection_receipt_list_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection_receipt_list_invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `collection_receipt_num` bigint NOT NULL,
  `invoice` varchar(255) DEFAULT NULL,
  `invoice_amount` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection_receipt_list_invoice`
--

LOCK TABLES `collection_receipt_list_invoice` WRITE;
/*!40000 ALTER TABLE `collection_receipt_list_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection_receipt_list_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `current_invoice`
--

DROP TABLE IF EXISTS `current_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `current_invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `current_invoice`
--

LOCK TABLES `current_invoice` WRITE;
/*!40000 ALTER TABLE `current_invoice` DISABLE KEYS */;
INSERT INTO `current_invoice` VALUES (1,'21425325');
/*!40000 ALTER TABLE `current_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `add_vat` double NOT NULL,
  `amount_net_of_vat` double NOT NULL,
  `cashier` varchar(255) DEFAULT NULL,
  `client_address` varchar(255) DEFAULT NULL,
  `client_bus_style` varchar(255) DEFAULT NULL,
  `invoice_client_contact_person` varchar(255) NOT NULL,
  `client_name` varchar(255) DEFAULT NULL,
  `client_payment` double NOT NULL,
  `client_terms` varchar(255) DEFAULT NULL,
  `client_tin` varchar(255) DEFAULT NULL,
  `invoice_date_created` date NOT NULL,
  `grand_total` double NOT NULL,
  `supplier_invoice_number` varchar(255) NOT NULL,
  `invoice_purchase_order_number` varchar(255) NOT NULL,
  `sale_invoice_date` date NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `supplier_address` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `supplier_tin` varchar(255) DEFAULT NULL,
  `total_sales_vat_inc` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,456.86,3807.14,'test, marketing','sadasda, aswe','construction','dresc','testclient',0,'NA','221412','2023-08-01',4264,'21425325','PO810143','2023-08-01','Unpaid','wewqadsd, adaswa','testsupplier','24124',4264);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_product_info`
--

DROP TABLE IF EXISTS `invoice_product_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_product_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `articles` varchar(255) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `purchase_order_number` varchar(255) DEFAULT NULL,
  `qty` int NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `unit_price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_product_info`
--

LOCK TABLES `invoice_product_info` WRITE;
/*!40000 ALTER TABLE `invoice_product_info` DISABLE KEYS */;
INSERT INTO `invoice_product_info` VALUES (1,4264,'test','21425325','PO810143',2,'pcs',2132);
/*!40000 ALTER TABLE `invoice_product_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `official_receipt`
--

DROP TABLE IF EXISTS `official_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `official_receipt` (
  `id` int NOT NULL AUTO_INCREMENT,
  `add_vat` double NOT NULL,
  `amount` double NOT NULL,
  `amount_due` double NOT NULL,
  `cash` varchar(255) DEFAULT NULL,
  `cashier_name` varchar(255) DEFAULT NULL,
  `check_no` varchar(255) DEFAULT NULL,
  `client_address` varchar(255) DEFAULT NULL,
  `client_bus` varchar(255) DEFAULT NULL,
  `client_payment` double NOT NULL,
  `client_sum_of` varchar(255) DEFAULT NULL,
  `client_tin` varchar(255) DEFAULT NULL,
  `ewt` double NOT NULL,
  `less_with_hold_tax` double NOT NULL,
  `official_receipt_date` varchar(255) DEFAULT NULL,
  `official_receipt_num` bigint NOT NULL,
  `partial_payment_for` varchar(255) DEFAULT NULL,
  `recv_from` varchar(255) DEFAULT NULL,
  `supplier_address` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `supplier_tin` varchar(255) DEFAULT NULL,
  `total` double NOT NULL,
  `total_sales` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `official_receipt`
--

LOCK TABLES `official_receipt` WRITE;
/*!40000 ALTER TABLE `official_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `official_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `official_receipt_list_invoice`
--

DROP TABLE IF EXISTS `official_receipt_list_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `official_receipt_list_invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_amount` double NOT NULL,
  `invoice_no` varchar(255) DEFAULT NULL,
  `official_receipt_num` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `official_receipt_list_invoice`
--

LOCK TABLES `official_receipt_list_invoice` WRITE;
/*!40000 ALTER TABLE `official_receipt_list_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `official_receipt_list_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier_address` varchar(255) NOT NULL,
  `supplier_atp` varchar(255) DEFAULT NULL,
  `supplier_city_address` varchar(255) NOT NULL,
  `supplier_cor_date` varchar(255) DEFAULT NULL,
  `supplier_cor_num` varchar(255) DEFAULT NULL,
  `supplier_limit` double DEFAULT NULL,
  `supplier_name` varchar(255) NOT NULL,
  `supplier_sec_num` varchar(255) DEFAULT NULL,
  `supplier_sec_year` varchar(255) DEFAULT NULL,
  `supplier_tin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'wewqadsd','NA','adaswa','NA','NA',500000,'testsupplier','24121','2022','24124');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_email` varchar(45) NOT NULL,
  `user_firstname` varchar(45) NOT NULL,
  `user_lastname` varchar(45) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_role` varchar(45) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_33uo7vet9c79ydfuwg1w848f` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@gmail.com','admin','admin','$2a$05$MRR/iTTQO97lJzKvxHMJMev2y3zaa/j/RPMIp3KgFzIpy4GLYsVcy','admin',NULL),(2,'marketing@gmail.com','marketing','test','$2a$05$go.yQkn95DC/EQXLIoFf0e.N9HsGp35zfyTG8etf5.r1jb2Qxacqm','marketing',NULL),(3,'vrtest@gmail.com','vrtest','staff','$2a$05$zgGqtW4144icrJEpa.aRbupBVzV6O5MSyo6IvDm1xU1M57e7BUxV2','vr-staff',NULL);
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

-- Dump completed on 2023-09-10  3:15:00
