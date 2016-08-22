-- MySQL dump 10.16  Distrib 10.1.14-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: objects
-- ------------------------------------------------------
-- Server version	10.1.14-MariaDB

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
-- Table structure for table `authority`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authority` (
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tenant`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant` (
  `id` binary(16) NOT NULL,
  `active` bit(1) NOT NULL,
  `created` datetime DEFAULT NULL,
  `lastModified` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dcxf3ksi0gyn1tieeq0id96lm` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metadataOwner`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadataOwner` (
  `internalId` binary(16) NOT NULL,
  `id` binary(16) NOT NULL,
  `tenantId` binary(16) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`internalId`),
  UNIQUE KEY `UK_n30427w7go3uuy7gqup3hplbk` (`type`,`id`,`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metadata`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadata` (
  `keyName` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `dataType` int(11) NOT NULL,
  `lastModified` datetime NOT NULL,
  `value` varchar(767) DEFAULT NULL,
  `owner_internalId` binary(16) NOT NULL,
  PRIMARY KEY (`keyName`,`owner_internalId`),
  KEY `FK_r0ry5rei784snrsbqoh9lb1v1` (`owner_internalId`),
  CONSTRAINT `FK_r0ry5rei784snrsbqoh9lb1v1` FOREIGN KEY (`owner_internalId`) REFERENCES `metadataOwner` (`internalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `relationship`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `id` binary(16) NOT NULL,
  `created` datetime DEFAULT NULL,
  `lastModified` datetime NOT NULL,
  `relationshipType` varchar(255) NOT NULL,
  `sourceId` binary(16) NOT NULL,
  `sourceType` varchar(255) NOT NULL,
  `targetId` binary(16) NOT NULL,
  `targetType` varchar(255) NOT NULL,
  `tenantId` binary(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` binary(16) NOT NULL,
  `active` bit(1) NOT NULL,
  `created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastModified` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `tenantId` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k84ra9fx3bt8vhs35jb634m6e` (`name`,`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `thing`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thing` (
  `id` binary(16) NOT NULL,
  `active` bit(1) NOT NULL,
  `created` datetime DEFAULT NULL,
  `lastModified` datetime NOT NULL,
  `tenantId` binary(16) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_51ftm4jj6o6r8i4rxlv35mjdo` (`id`,`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` binary(16) NOT NULL,
  `active` bit(1) NOT NULL,
  `created` datetime DEFAULT NULL,
  `emailAddress` varchar(255) NOT NULL,
  `givenName` varchar(255) DEFAULT NULL,
  `lastModified` datetime NOT NULL,
  `password` varchar(255) NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `tenantId` binary(16) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user` binary(16) NOT NULL,
  `role` binary(16) NOT NULL,
  PRIMARY KEY (`user`,`role`),
  KEY `FK_c519w0l613l023tayy895chpd` (`role`),
  CONSTRAINT `FK_c519w0l613l023tayy895chpd` FOREIGN KEY (`role`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_oanch5nixy8aa46y2jexkvo5w` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_user` (
  `role_id` binary(16) NOT NULL,
  `users_id` binary(16) NOT NULL,
  PRIMARY KEY (`role_id`,`users_id`),
  KEY `FK_kfajhki6vd9okapq5eov6tk9c` (`users_id`),
  CONSTRAINT `FK_1p4td69hdcg4iaau4pvj20m3h` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_kfajhki6vd9okapq5eov6tk9c` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_authorities`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_authorities` (
  `role` binary(16) NOT NULL,
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`role`,`authority`),
  KEY `FK_mvxsigs0pjmtokrtegpq3srgt` (`authority`),
  CONSTRAINT `FK_mvxsigs0pjmtokrtegpq3srgt` FOREIGN KEY (`authority`) REFERENCES `authority` (`authority`),
  CONSTRAINT `FK_ri5esjrabynhwunpqy4tx3auj` FOREIGN KEY (`role`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-20 10:20:42
