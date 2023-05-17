CREATE DATABASE  IF NOT EXISTS `corporativo` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `corporativo`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: corporativo
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `cad_sit_funcional`
--

DROP TABLE IF EXISTS `cad_sit_funcional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cad_sit_funcional` (
  `ID_CAD_SIT_FUNCIONAL` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DSC_SIT_FUNCIONAL` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_MUMPS` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_CAD_SIT_FUNCIONAL`),
  UNIQUE KEY `CAD_SIT_FUNCIONAL_PK` (`ID_CAD_SIT_FUNCIONAL`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cad_sit_funcional`
--

LOCK TABLES `cad_sit_funcional` WRITE;
/*!40000 ALTER TABLE `cad_sit_funcional` DISABLE KEYS */;
INSERT INTO `cad_sit_funcional` VALUES (1,'ATIVO',1),
(2,'CEDIDO',45),
(3,'POSSE EM CARGO INACUMULAVEL',25),
(4,'APOSENTADO',5),
(5,'EXONERADO',10),
(6,'DEMITIDO',15),
(7,'FALECIDO',20),
(9,'RETORNO PARA ORGAO DE ORIGEM',40),
(10,'TRANSFERIDO',35),
(11,'LICENCA PARA ACOMPANHAR CONJUGE SEM REMUNERACAO',65),
(12,'LICENCA PARA ACOMPANHAR CONJUGE COM EXERCICIO PROVISORIO',70),
(13,'LICENCA PARA MANDATO CLASSISTA',75),
(14,'LICENCA PARA ASSUNTOS PARTICULARES',80),
(15,'ESTAGIARIO DESLIGADO (SADES)',125),
(16,'DISPONIBILIDADE',50),
(17,'REMOVIDO',115),
(18,'AFASTAMENTO POR DECISAO JUDICIAL',100),
(19,'POSSE ANULADA',30),
(20,'REDISTRIBUIDO',55),
(21,'PROMOVIDO TRF',60),
(23,'LICENCA PARA CURSO DE FORMACAO',85),
(25,'LICENCA PARA ATIVIDADE POLITICA',90),
(26,'AFASTAMENTO PARA MANDATO ELETIVO',105),
(29,'LICENCA PARA CAPACITACAO',95),
(30,'AFASTAMENTO PARA ESTUDO/MISSAO EXTERIOR',110),
(31,'REMOVIDO - LEI 11.416/06',120);
/*!40000 ALTER TABLE `cad_sit_funcional` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_acesso`
--

DROP TABLE IF EXISTS `cp_acesso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_acesso` (
  `ID_ACESSO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_IDENTIDADE` INT UNSIGNED NOT NULL,
  `DT_INI` datetime NOT NULL,
  `DT_FIM` datetime DEFAULT NULL,
  `TP_ACESSO` tinyint(4) NOT NULL,
  `IP_AUDIT` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_ACESSO`),
  KEY `CP_ACESSO_IDENTIDADE_FK` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_ACESSO_IDENTIDADE_FK` FOREIGN KEY (`ID_IDENTIDADE`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_acesso`
--

LOCK TABLES `cp_acesso` WRITE;
/*!40000 ALTER TABLE `cp_acesso` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_acesso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_aplicacao_feriado`
--

DROP TABLE IF EXISTS `cp_aplicacao_feriado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_aplicacao_feriado` (
  `ID_APLICACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `ID_LOTACAO` INT UNSIGNED DEFAULT NULL,
  `ID_LOCALIDADE` INT UNSIGNED DEFAULT NULL,
  `ID_OCORRENCIA_FERIADO` INT UNSIGNED DEFAULT NULL,
  `FERIADO` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_APLICACAO`),
  KEY `ID_ORGAO_USU` (`ID_ORGAO_USU`),
  KEY `ID_LOTACAO` (`ID_LOTACAO`),
  KEY `ID_LOCALIDADE` (`ID_LOCALIDADE`),
  KEY `ID_OCORRENCIA_FERIADO` (`ID_OCORRENCIA_FERIADO`),
  CONSTRAINT `cp_aplicacao_feriado_ibfk_1` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `cp_aplicacao_feriado_ibfk_2` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `cp_aplicacao_feriado_ibfk_3` FOREIGN KEY (`ID_LOCALIDADE`) REFERENCES `cp_localidade` (`ID_LOCALIDADE`),
  CONSTRAINT `cp_aplicacao_feriado_ibfk_4` FOREIGN KEY (`ID_OCORRENCIA_FERIADO`) REFERENCES `cp_ocorrencia_feriado` (`ID_OCORRENCIA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_aplicacao_feriado`
--

LOCK TABLES `cp_aplicacao_feriado` WRITE;
/*!40000 ALTER TABLE `cp_aplicacao_feriado` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_aplicacao_feriado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_complexo`
--

DROP TABLE IF EXISTS `cp_complexo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_complexo` (
  `ID_COMPLEXO` INT UNSIGNED NOT NULL,
  `NOME_COMPLEXO` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_LOCALIDADE` INT UNSIGNED DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_COMPLEXO`),
  KEY `ID_LOCALIDADE` (`ID_LOCALIDADE`),
  CONSTRAINT `cp_complexo_ibfk_1` FOREIGN KEY (`ID_LOCALIDADE`) REFERENCES `cp_localidade` (`ID_LOCALIDADE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_complexo`
--

LOCK TABLES `cp_complexo` WRITE;
/*!40000 ALTER TABLE `cp_complexo` DISABLE KEYS */;
INSERT INTO `cp_complexo` VALUES (1,'Almirante Barroso',15,999999999),
(2,'Angra',1,999999999),
(3,'Barra do Piraí',2,999999999),
(4,'Campos',4,999999999),
(5,'Duque de Caxias',5,999999999),
(6,'Equador',5,999999999),
(7,'Friburgo',11,999999999),
(8,'Itaboraí',6,999999999),
(9,'Itaperuna',7,999999999),
(10,'Macaé',8,999999999),
(11,'Mage',9,999999999),
(12,'Niteroi',10,999999999),
(13,'Nova Iguaçu',12,999999999),
(14,'Petrópolis',13,999999999),
(15,'Resende',14,999999999),
(16,'Rio Branco',15,999999999),
(17,'Sao Gonçalo',16,999999999),
(18,'Sao Joao de Meriti',17,999999999),
(19,'Sao Pedro da Aldeia',19,999999999),
(20,'São Cristovão',15,999999999),
(21,'Teresópolis',20,999999999),
(22,'Tres Rios',21,999999999),
(23,'Venezuela',15,999999999),
(24,'Volta Redonda',23,999999999);
/*!40000 ALTER TABLE `cp_complexo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_configuracao`
--

DROP TABLE IF EXISTS `cp_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_configuracao` (
  `ID_CONFIGURACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DT_INI_VIG_CONFIGURACAO` datetime DEFAULT NULL,
  `DT_FIM_VIG_CONFIGURACAO` datetime DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `ID_LOTACAO` INT UNSIGNED DEFAULT NULL,
  `ID_CARGO` INT UNSIGNED DEFAULT NULL,
  `ID_FUNCAO_CONFIANCA` INT UNSIGNED DEFAULT NULL,
  `ID_PESSOA` INT UNSIGNED DEFAULT NULL,
  `ID_SIT_CONFIGURACAO` INT UNSIGNED DEFAULT NULL,
  `ID_TP_CONFIGURACAO` INT UNSIGNED DEFAULT NULL,
  `ID_SERVICO` INT UNSIGNED DEFAULT NULL,
  `ID_GRUPO` INT UNSIGNED DEFAULT NULL,
  `NM_EMAIL` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DESC_FORMULA` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_TP_LOTACAO` INT UNSIGNED DEFAULT NULL,
  `ID_IDENTIDADE` INT UNSIGNED DEFAULT NULL,
  `HIS_IDC_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_IDC_FIM` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `ID_COMPLEXO` INT UNSIGNED DEFAULT NULL,
  `ID_ORGAO_OBJETO` INT UNSIGNED DEFAULT NULL,
  `DESCR_CONFIGURACAO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_LOTACAO_OBJETO` INT UNSIGNED DEFAULT NULL,
  `ID_COMPLEXO_OBJETO` INT UNSIGNED DEFAULT NULL,
  `ID_CARGO_OBJETO` INT UNSIGNED DEFAULT NULL,
  `ID_FUNCAO_CONFIANCA_OBJETO` INT UNSIGNED DEFAULT NULL,
  `ID_PESSOA_OBJETO` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_CONFIGURACAO`),
  UNIQUE KEY `CP_CONF_ID_CONFIGURACAO_PK` (`ID_CONFIGURACAO`),
  KEY `CP_CONF_CP_GRUP_ID_GRP_FK` (`ID_GRUPO`),
  KEY `CP_CONF_CP_IDENT_IDC_FIM_FK` (`HIS_IDC_FIM`),
  KEY `CP_CONF_CP_IDENT_IDC_INI_FK` (`HIS_IDC_INI`),
  KEY `CP_CONF_CP_IDENT_ID_IDENT_FK` (`ID_IDENTIDADE`),
  KEY `CP_CONF_CP_ORG_USU_ID_FK` (`ID_ORGAO_USU`),
  KEY `CP_CONF_CP_SERV_ID_SERVICO_FK` (`ID_SERVICO`),
  KEY `CP_CONF_CP_SIT_CONF_ID_FK` (`ID_SIT_CONFIGURACAO`),
  KEY `CP_CONF_CP_TPLOT_ID_TP_LOT_FK` (`ID_TP_LOTACAO`),
  KEY `CP_CONF_CP_TP_CONF_ID_FK` (`ID_TP_CONFIGURACAO`),
  KEY `CP_CONF_DP_CARGO_ID_CARGO_FK` (`ID_CARGO`),
  KEY `CP_CONF_DP_F_CONFIANCA_ID_FK` (`ID_FUNCAO_CONFIANCA`),
  KEY `CP_CONF_DP_LOT_ID_LOTACAO_FK` (`ID_LOTACAO`),
  KEY `CP_CONF_DP_PES_ID_PESSOA_FK` (`ID_PESSOA`),
  KEY `CP_CONFIGURACAO_COMPLEXO_FK` (`ID_COMPLEXO`),
  CONSTRAINT `CP_CONFIGURACAO_COMPLEXO_FK` FOREIGN KEY (`ID_COMPLEXO`) REFERENCES `cp_complexo` (`ID_COMPLEXO`),
  CONSTRAINT `CP_CONF_CP_GRUP_ID_GRP_FK` FOREIGN KEY (`ID_GRUPO`) REFERENCES `cp_grupo` (`ID_GRUPO`),
  CONSTRAINT `CP_CONF_CP_IDENT_IDC_FIM_FK` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_CONF_CP_IDENT_IDC_INI_FK` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_CONF_CP_IDENT_ID_IDENT_FK` FOREIGN KEY (`ID_IDENTIDADE`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_CONF_CP_ORG_USU_ID_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `CP_CONF_CP_SERV_ID_SERVICO_FK` FOREIGN KEY (`ID_SERVICO`) REFERENCES `cp_servico` (`ID_SERVICO`),
  CONSTRAINT `CP_CONF_CP_SIT_CONF_ID_FK` FOREIGN KEY (`ID_SIT_CONFIGURACAO`) REFERENCES `cp_situacao_configuracao` (`ID_SIT_CONFIGURACAO`),
  CONSTRAINT `CP_CONF_CP_TPLOT_ID_TP_LOT_FK` FOREIGN KEY (`ID_TP_LOTACAO`) REFERENCES `cp_tipo_lotacao` (`ID_TP_LOTACAO`),
  CONSTRAINT `CP_CONF_CP_TP_CONF_ID_FK` FOREIGN KEY (`ID_TP_CONFIGURACAO`) REFERENCES `cp_tipo_configuracao` (`ID_TP_CONFIGURACAO`),
  CONSTRAINT `CP_CONF_DP_CARGO_ID_CARGO_FK` FOREIGN KEY (`ID_CARGO`) REFERENCES `dp_cargo` (`ID_CARGO`),
  CONSTRAINT `CP_CONF_DP_F_CONFIANCA_ID_FK` FOREIGN KEY (`ID_FUNCAO_CONFIANCA`) REFERENCES `dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`),
  CONSTRAINT `CP_CONF_DP_LOT_ID_LOTACAO_FK` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `CP_CONF_DP_PES_ID_PESSOA_FK` FOREIGN KEY (`ID_PESSOA`) REFERENCES `dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_configuracao`
--

LOCK TABLES `cp_configuracao` WRITE;
/*!40000 ALTER TABLE `cp_configuracao` DISABLE KEYS */;
INSERT INTO `cp_configuracao` VALUES 
(1,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,NULL,1,200,2,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(2,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,65,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(3,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,NULL,1,200,3,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(4,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,42,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(5,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,41,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(6,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,24,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(7,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,16,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(8,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,284,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(9,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,306,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(10,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,15,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(11,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,17,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(12,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,18,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(13,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,19,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(14,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,20,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(15,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,21,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(16,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,22,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(17,NULL,NULL,'2012-03-20 00:00:00',NULL,NULL,NULL,NULL,1,1,200,23,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(18,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,NULL,1,200,1,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(19,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,305,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(20,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,304,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(21,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,62,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(22,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,64,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(23,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,63,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(24,NULL,NULL,'2020-02-07 16:07:37',NULL,NULL,NULL,NULL,1,1,200,307,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(25,NULL,NULL,'2020-02-07 16:07:37',NULL,NULL,NULL,NULL,1,1,200,308,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(26,'2012-03-12 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,18,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(27,'2012-03-12 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,19,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(28,'2012-03-12 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,3,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(29,'2012-10-31 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,30,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(30,NULL,NULL,'2020-02-07 16:07:39',NULL,NULL,NULL,NULL,NULL,1,30,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(31,NULL,NULL,'2020-02-07 16:07:39',NULL,NULL,NULL,NULL,NULL,2,2,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(32,NULL,NULL,'2014-04-11 17:03:01',NULL,NULL,NULL,NULL,NULL,1,35,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(33,NULL,NULL,'2020-02-07 16:07:39',NULL,NULL,NULL,NULL,NULL,5,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(34,NULL,NULL,'2020-02-07 16:07:39',NULL,NULL,NULL,NULL,NULL,5,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(35,NULL,NULL,'2020-02-07 16:07:39',NULL,NULL,NULL,NULL,NULL,5,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(36,'2020-02-07 16:07:40',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,200,310,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,35,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(38,NULL,NULL,'2020-04-02 16:38:44',NULL,NULL,NULL,NULL,NULL,2,2,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(39,NULL,NULL,'2020-04-02 16:39:05',NULL,NULL,NULL,NULL,NULL,2,2,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,39,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(40,NULL,NULL,'2020-04-13 16:13:37',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,40,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(41,NULL,NULL,'2020-04-13 16:14:05',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,41,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(42,NULL,NULL,'2020-04-13 16:14:54',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,42,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(43,NULL,NULL,'2020-04-13 16:15:30',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,43,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(44,NULL,NULL,'2020-04-13 16:16:09',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(45,NULL,NULL,'2020-04-13 16:16:57',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,45,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(46,NULL,NULL,'2020-04-13 16:19:43',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,46,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(47,NULL,NULL,'2020-04-13 16:20:52',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,47,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(48,NULL,NULL,'2020-04-13 16:36:08',NULL,NULL,NULL,NULL,NULL,1,35,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,48,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(54,NULL,NULL,'2020-04-13 17:59:24',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,1,'2020-04-13 17:59:57',54,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(55,NULL,NULL,'2020-04-13 18:00:20',NULL,NULL,NULL,NULL,NULL,2,17,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,55,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(56,NULL,NULL,'2020-04-13 18:01:58',NULL,NULL,NULL,NULL,NULL,2,1,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,56,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(58,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,1,1,200,43,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(59,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,NULL,1,200,4,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
(60,NULL,NULL,'2012-03-22 00:00:00',NULL,NULL,NULL,NULL,NULL,1,200,437,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `cp_configuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_feriado`
--

DROP TABLE IF EXISTS `cp_feriado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_feriado` (
  `ID_FERIADO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DSC_FERIADO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID_FERIADO`),
  UNIQUE KEY `FERIADO_PK` (`ID_FERIADO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_feriado`
--

LOCK TABLES `cp_feriado` WRITE;
/*!40000 ALTER TABLE `cp_feriado` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_feriado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_grupo`
--

DROP TABLE IF EXISTS `cp_grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_grupo` (
  `ID_GRUPO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_TP_GRUPO` INT UNSIGNED NOT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `ID_GRUPO_PAI` INT UNSIGNED DEFAULT NULL,
  `SIGLA_GRUPO` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DESC_GRUPO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_INI` datetime NOT NULL,
  `HIS_IDC_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_FIM` INT UNSIGNED DEFAULT NULL,
  `HIS_ATIVO` tinyint(4) NOT NULL,
  PRIMARY KEY (`ID_GRUPO`),
  UNIQUE KEY `CP_GRUPO_PK` (`ID_GRUPO`),
  KEY `CP_GRP_CP_GRP_ID_INI_FK` (`HIS_ID_INI`),
  KEY `CP_GRP_CP_GRP_ID_PAI_FK` (`ID_GRUPO_PAI`),
  KEY `CP_GRP_CP_IDENT_IDC_FIM_FK` (`HIS_IDC_FIM`),
  KEY `CP_GRP_CP_IDENT_IDC_INI_FK` (`HIS_IDC_INI`),
  KEY `CP_GRP_CP_TP_GRP_ID_TP_GRP_FK` (`ID_TP_GRUPO`),
  CONSTRAINT `CP_GRP_CP_GRP_ID_INI_FK` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `cp_grupo` (`ID_GRUPO`),
  CONSTRAINT `CP_GRP_CP_GRP_ID_PAI_FK` FOREIGN KEY (`ID_GRUPO_PAI`) REFERENCES `cp_grupo` (`ID_GRUPO`),
  CONSTRAINT `CP_GRP_CP_IDENT_IDC_FIM_FK` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_GRP_CP_IDENT_IDC_INI_FK` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_GRP_CP_TP_GRP_ID_TP_GRP_FK` FOREIGN KEY (`ID_TP_GRUPO`) REFERENCES `cp_tipo_grupo` (`ID_TP_GRUPO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_grupo`
--

LOCK TABLES `cp_grupo` WRITE;
/*!40000 ALTER TABLE `cp_grupo` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_identidade`
--

DROP TABLE IF EXISTS `cp_identidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_identidade` (
  `ID_IDENTIDADE` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_TP_IDENTIDADE` INT UNSIGNED DEFAULT NULL,
  `ID_PESSOA` INT UNSIGNED DEFAULT NULL,
  `DATA_CRIACAO_IDENTIDADE` datetime DEFAULT NULL,
  `DATA_EXPIRACAO_IDENTIDADE` datetime DEFAULT NULL,
  `DATA_CANCELAMENTO_IDENTIDADE` datetime DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `LOGIN_IDENTIDADE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SENHA_IDENTIDADE` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SENHA_IDENTIDADE_CRIPTO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SENHA_IDENTIDADE_CRIPTO_SINC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_INI` datetime NOT NULL,
  `HIS_IDC_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_FIM` INT UNSIGNED DEFAULT NULL,
  `HIS_ATIVO` tinyint(4) NOT NULL,
  PRIMARY KEY (`ID_IDENTIDADE`),
  KEY `CP_IDENT_CPORGUSU_ID_ORGUSU_FK` (`ID_ORGAO_USU`),
  KEY `CP_IDENT_CP_IDENT_IDC_FIM_FK` (`HIS_IDC_FIM`),
  KEY `CP_IDENT_CP_IDENT_IDC_INI_FK` (`HIS_IDC_INI`),
  KEY `CP_IDENT_CP_IDENT_ID_INI_FK` (`HIS_ID_INI`),
  KEY `CP_IDENT_DP_PESS_ID_PESS_FK` (`ID_PESSOA`),
  KEY `IDX_LOGIN_IDENTIDADE` (`LOGIN_IDENTIDADE`),
  CONSTRAINT `CP_IDENT_CPORGUSU_ID_ORGUSU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `CP_IDENT_CP_IDENT_IDC_FIM_FK` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_IDENT_CP_IDENT_IDC_INI_FK` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_IDENT_CP_IDENT_ID_INI_FK` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `CP_IDENT_CP_TPIDT_ID_TP_IDT_FK` FOREIGN KEY (`ID_TP_IDENTIDADE`) REFERENCES `cp_tipo_identidade` (`ID_TP_IDENTIDADE`),
  CONSTRAINT `CP_IDENT_DP_PESS_ID_PESS_FK` FOREIGN KEY (`ID_PESSOA`) REFERENCES `dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_identidade`
--

LOCK TABLES `cp_identidade` WRITE;
/*!40000 ALTER TABLE `cp_identidade` DISABLE KEYS */;
INSERT INTO `cp_identidade` (
 `ID_IDENTIDADE`,
  `ID_TP_IDENTIDADE` ,
  `ID_PESSOA` ,
  `DATA_CRIACAO_IDENTIDADE` ,
  `DATA_EXPIRACAO_IDENTIDADE` ,
  `DATA_CANCELAMENTO_IDENTIDADE` ,
  `ID_ORGAO_USU` ,
  `LOGIN_IDENTIDADE` ,
  `SENHA_IDENTIDADE` ,
  `SENHA_IDENTIDADE_CRIPTO` ,
  `SENHA_IDENTIDADE_CRIPTO_SINC` ,
  `HIS_ID_INI` ,
  `HIS_DT_INI` ,
  `HIS_IDC_INI` ,
  `HIS_DT_FIM`,
  `HIS_IDC_FIM` ,
  `HIS_ATIVO`
  ) 
  VALUES (1,1,1,'2011-10-26 00:00:00',NULL,NULL,999999999,'ZZ99999','2ac9cb7dc02b3c0083eb70898e549b63','I20BhBeI7KavVeWdbIa8+g==','I20BhBeI7KavVeWdbIa8+g==',1,'2011-10-26 00:00:00',1,NULL,NULL,1),
         (2,1,2,'2011-10-26 00:00:00',NULL,NULL,999999999,'ZZ99998','2ac9cb7dc02b3c0083eb70898e549b63','I20BhBeI7KavVeWdbIa8+g==','I20BhBeI7KavVeWdbIa8+g==',2,'2011-10-26 00:00:00',1,NULL,NULL,1),
         (3,1,3,'2011-10-26 00:00:00',NULL,NULL,999999999,'ZZ99997','2ac9cb7dc02b3c0083eb70898e549b63','I20BhBeI7KavVeWdbIa8+g==','I20BhBeI7KavVeWdbIa8+g==',3,'2011-10-26 00:00:00',1,NULL,NULL,1);
/*!40000 ALTER TABLE `cp_identidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_localidade`
--

DROP TABLE IF EXISTS `cp_localidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_localidade` (
  `ID_LOCALIDADE` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_LOCALIDADE` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ID_UF` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID_LOCALIDADE`),
  UNIQUE KEY `LOCALIDADE_FK` (`ID_LOCALIDADE`),
  KEY `UF_LOCALIDADE_FK` (`ID_UF`),
  CONSTRAINT `UF_LOCALIDADE_FK` FOREIGN KEY (`ID_UF`) REFERENCES `cp_uf` (`ID_UF`)
) ENGINE=InnoDB AUTO_INCREMENT=672 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_localidade`
--

LOCK TABLES `cp_localidade` WRITE;
/*!40000 ALTER TABLE `cp_localidade` DISABLE KEYS */;
INSERT INTO `cp_localidade` VALUES (1,'Angra dos Reis',19),
(2,'Barra do Piraí',19),
(3,'Cachoeiro do Itapemirim',8),
(4,'Campos',19),
(5,'Duque de Caxias',19),
(6,'Itaboraí',19),
(7,'Itaperuna',19),
(8,'Macaé',19),
(9,'Magé',19),
(10,'Niterói',19),
(11,'Nova Friburgo',19),
(12,'Nova Iguaçu',19),
(13,'Petrópolis',19),
(14,'Resende',19),
(15,'Rio de Janeiro',19),
(16,'São Gonçalo',19),
(17,'São João de Meriti',19),
(18,'São Mateus',8),
(19,'São Pedro da Aldeia',19),
(20,'Teresópolis',19),
(21,'Três Rios',19),
(22,'Vitória',8),
(23,'Volta Redonda',19),
(24,'Campo Grande',19),
(25,'São Paulo',26),
(26,'Adamantina',26),
(27,'Adolfo',26),
(28,'Aguaí',26),
(29,'Águas da Prata',26),
(30,'Águas de Lindóia',26),
(31,'Águas de Santa Bárbara',26),
(32,'Águas de São Pedro',26),
(33,'Agudos',26),
(34,'Alambari',26),
(35,'Alfredo Marcondes',26),
(36,'Altair',26),
(37,'Altinópolis',26),
(38,'Alto Alegre',26),
(39,'Alumínio',26),
(40,'Álvares Florence',26),
(41,'Álvares Machado',26),
(42,'Álvaro de Carvalho',26),
(43,'Alvinlândia',26),
(44,'Americana',26),
(45,'Américo Brasiliense',26),
(46,'Américo de Campos',26),
(47,'Amparo',26),
(48,'Analândia',26),
(49,'Andradina',26),
(50,'Angatuba',26),
(51,'Anhembi',26),
(52,'Anhumas',26),
(53,'Aparecida',26),
(54,'Aparecida d\'Oeste',26),
(55,'Apiaí',26),
(56,'Araçariguama',26),
(57,'Araçatuba',26),
(58,'Araçoiaba da Serra',26),
(59,'Aramina',26),
(60,'Arandu',26),
(61,'Arapeí',26),
(62,'Araraquara',26),
(63,'Araras',26),
(64,'Arco-Íris',26),
(65,'Arealva',26),
(66,'Areias',26),
(67,'Areiópolis',26),
(68,'Ariranha',26),
(69,'Artur Nogueira',26),
(70,'Arujá',26),
(71,'Aspásia',26),
(72,'Assis',26),
(73,'Atibaia',26),
(74,'Auriflama',26),
(75,'Avaí',26),
(76,'Avanhandava',26),
(77,'Avaré',26),
(78,'Bady Bassitt',26),
(79,'Balbinos',26),
(80,'Bálsamo',26),
(81,'Bananal',26),
(82,'Barão de Antonina',26),
(83,'Barbosa',26),
(84,'Bariri',26),
(85,'Barra Bonita',26),
(86,'Barra do Chapéu',26),
(87,'Barra do Turvo',26),
(88,'Barretos',26),
(89,'Barrinha',26),
(90,'Barueri',26),
(91,'Bastos',26),
(92,'Batatais',26),
(93,'Bauru',26),
(94,'Bebedouro',26),
(95,'Bento de Abreu',26),
(96,'Bernardino de Campos',26),
(97,'Bertioga',26),
(98,'Bilac',26),
(99,'Birigui',26),
(100,'Biritiba-Mirim',26),
(101,'Boa Esperança do Sul',26),
(102,'Bocaina',26),
(103,'Bofete',26),
(104,'Boituva',26),
(105,'Bom Jesus dos Perdões',26),
(106,'Bom Sucesso de Itararé',26),
(107,'Borá',26),
(108,'Boracéia',26),
(109,'Borborema',26),
(110,'Borebi',26),
(111,'Botucatu',26),
(112,'Bragança Paulista',26),
(113,'Braúna',26),
(114,'Brejo Alegre',26),
(115,'Brodowski',26),
(116,'Brotas',26),
(117,'Buri',26),
(118,'Buritama',26),
(119,'Buritizal',26),
(120,'Cabrália Paulista',26),
(121,'Cabreúva',26),
(122,'Caçapava',26),
(123,'Cachoeira Paulista',26),
(124,'Caconde',26),
(125,'Cafelândia',26),
(126,'Caiabu',26),
(127,'Caieiras',26),
(128,'Caiuá',26),
(129,'Cajamar',26),
(130,'Cajati',26),
(131,'Cajobi',26),
(132,'Cajuru',26),
(133,'Campina do Monte Alegre',26),
(134,'Campinas',26),
(135,'Campo Limpo Paulista',26),
(136,'Campos do Jordão',26),
(137,'Campos Novos Paulista',26),
(138,'Cananéia',26),
(139,'Canas',26),
(140,'Cândido Mota',26),
(141,'Cândido Rodrigues',26),
(142,'Canitar',26),
(143,'Capão Bonito',26),
(144,'Capela do Alto',26),
(145,'Capivari',26),
(146,'Caraguatatuba',26),
(147,'Carapicuíba',26),
(148,'Cardoso',26),
(149,'Casa Branca',26),
(150,'Cássia dos Coqueiros',26),
(151,'Castilho',26),
(152,'Catanduva',26),
(153,'Catiguá',26),
(154,'Cedral',26),
(155,'Cerqueira César',26),
(156,'Cerquilho',26),
(157,'Cesário Lange',26),
(158,'Charqueada',26),
(159,'Chavantes',26),
(160,'Clementina',26),
(161,'Colina',26),
(162,'Colômbia',26),
(163,'Conchal',26),
(164,'Conchas',26),
(165,'Cordeirópolis',26),
(166,'Coroados',26),
(167,'Coronel Macedo',26),
(168,'Corumbataí',26),
(169,'Cosmópolis',26),
(170,'Cosmorama',26),
(171,'Cotia',26),
(172,'Cravinhos',26),
(173,'Cristais Paulista',26),
(174,'Cruzália',26),
(175,'Cruzeiro',26),
(176,'Cubatão',26),
(177,'Cunha',26),
(178,'Descalvado',26),
(179,'Diadema',26),
(180,'Dirce Reis',26),
(181,'Divinolândia',26),
(182,'Dobrada',26),
(183,'Dois Córregos',26),
(184,'Dolcinópolis',26),
(185,'Dourado',26),
(186,'Dracena',26),
(187,'Duartina',26),
(188,'Dumont',26),
(189,'Echaporã',26),
(190,'Eldorado',26),
(191,'Elias Fausto',26),
(192,'Elisiário',26),
(193,'Embaúba',26),
(194,'Embu',26),
(195,'Embu-Guaçu',26),
(196,'Emilianópolis',26),
(197,'Engenheiro Coelho',26),
(198,'Espírito Santo do Pinhal',26),
(199,'Espírito Santo do Turvo',26),
(200,'Estiva Gerbi',26),
(201,'Estrela do Norte',26),
(202,'Estrela d\'Oeste',26),
(203,'Euclides da Cunha Paulista',26),
(204,'Fartura',26),
(205,'Fernando Prestes',26),
(206,'Fernandópolis',26),
(207,'Fernão',26),
(208,'Ferraz de Vasconcelos',26),
(209,'Flora Rica',26),
(210,'Floreal',26),
(211,'Flórida Paulista',26),
(212,'Florínea',26),
(213,'Franca',26),
(214,'Francisco Morato',26),
(215,'Franco da Rocha',26),
(216,'Gabriel Monteiro',26),
(217,'Gália',26),
(218,'Garça',26),
(219,'Gastão Vidigal',26),
(220,'Gavião Peixoto',26),
(221,'General Salgado',26),
(222,'Getulina',26),
(223,'Glicério',26),
(224,'Guaiçara',26),
(225,'Guaimbê',26),
(226,'Guaíra',26),
(227,'Guapiaçu',26),
(228,'Guapiara',26),
(229,'Guará',26),
(230,'Guaraçaí',26),
(231,'Guaraci',26),
(232,'Guarani d\'Oeste',26),
(233,'Guarantã',26),
(234,'Guararapes',26),
(235,'Guararema',26),
(236,'Guaratinguetá',26),
(237,'Guareí',26),
(238,'Guariba',26),
(239,'Guarujá',26),
(240,'Guarulhos',26),
(241,'Guatapará',26),
(242,'Guzolândia',26),
(243,'Herculândia',26),
(244,'Holambra',26),
(245,'Hortolândia',26),
(246,'Iacanga',26),
(247,'Iacri',26),
(248,'Iaras',26),
(249,'Ibaté',26),
(250,'Ibirá',26),
(251,'Ibirarema',26),
(252,'Ibitinga',26),
(253,'Ibiúna',26),
(254,'Icém',26),
(255,'Iepê',26),
(256,'Igaraçu do Tietê',26),
(257,'Igarapava',26),
(258,'Igaratá',26),
(259,'Iguape',26),
(260,'Ilha Comprida',26),
(261,'Ilha Solteira',26),
(262,'Ilhabela',26),
(263,'Indaiatuba',26),
(264,'Indiana',26),
(265,'Indiaporã',26),
(266,'Inúbia Paulista',26),
(267,'Ipaussu',26),
(268,'Iperó',26),
(269,'Ipeúna',26),
(270,'Ipiguá',26),
(271,'Iporanga',26),
(272,'Ipuã',26),
(273,'Iracemápolis',26),
(274,'Irapuã',26),
(275,'Irapuru',26),
(276,'Itaberá',26),
(277,'Itaí',26),
(278,'Itajobi',26),
(279,'Itaju',26),
(280,'Itanhaém',26),
(281,'Itaóca',26),
(282,'Itapecerica da Serra',26),
(283,'Itapetininga',26),
(284,'Itapeva',26),
(285,'Itapevi',26),
(286,'Itapira',26),
(287,'Itapirapuã Paulista',26),
(288,'Itápolis',26),
(289,'Itaporanga',26),
(290,'Itapuí',26),
(291,'Itapura',26),
(292,'Itaquaquecetuba',26),
(293,'Itararé',26),
(294,'Itariri',26),
(295,'Itatiba',26),
(296,'Itatinga',26),
(297,'Itirapina',26),
(298,'Itirapuã',26),
(299,'Itobi',26),
(300,'Itu',26),
(301,'Itupeva',26),
(302,'Ituverava',26),
(303,'Jaborandi',26),
(304,'Jaboticabal',26),
(305,'Jacareí',26),
(306,'Jaci',26),
(307,'Jacupiranga',26),
(308,'Jaguariúna',26),
(309,'Jales',26),
(310,'Jambeiro',26),
(311,'Jandira',26),
(312,'Jardinópolis',26),
(313,'Jarinu',26),
(314,'Jaú',26),
(315,'Jeriquara',26),
(316,'Joanópolis',26),
(317,'João Ramalho',26),
(318,'José Bonifácio',26),
(319,'Júlio Mesquita',26),
(320,'Jumirim',26),
(321,'Jundiaí',26),
(322,'Junqueirópolis',26),
(323,'Juquiá',26),
(324,'Juquitiba',26),
(325,'Lagoinha',26),
(326,'Laranjal Paulista',26),
(327,'Lavínia',26),
(328,'Lavrinhas',26),
(329,'Leme',26),
(330,'Lençóis Paulista',26),
(331,'Limeira',26),
(332,'Lindóia',26),
(333,'Lins',26),
(334,'Lorena',26),
(335,'Lourdes',26),
(336,'Louveira',26),
(337,'Lucélia',26),
(338,'Lucianópolis',26),
(339,'Luiz Antônio',26),
(340,'Luiziânia',26),
(341,'Lupércio',26),
(342,'Lutécia',26),
(343,'Macatuba',26),
(344,'Macaubal',26),
(345,'Macedônia',26),
(346,'Magda',26),
(347,'Mairinque',26),
(348,'Mairiporã',26),
(349,'Manduri',26),
(350,'Marabá Paulista',26),
(351,'Maracaí',26),
(352,'Marapoama',26),
(353,'Mariápolis',26),
(354,'Marília',26),
(355,'Marinópolis',26),
(356,'Martinópolis',26),
(357,'Matão',26),
(358,'Mauá',26),
(359,'Mendonça',26),
(360,'Meridiano',26),
(361,'Mesópolis',26),
(362,'Miguelópolis',26),
(363,'Mineiros do Tietê',26),
(364,'Mira Estrela',26),
(365,'Miracatu',26),
(366,'Mirandópolis',26),
(367,'Mirante do Paranapanema',26),
(368,'Mirassol',26),
(369,'Mirassolândia',26),
(370,'Mococa',26),
(371,'Mogi das Cruzes',26),
(372,'Mogi Guaçu',26),
(373,'Moji Mirim',26),
(374,'Mombuca',26),
(375,'Monções',26),
(376,'Mongaguá',26),
(377,'Monte Alegre do Sul',26),
(378,'Monte Alto',26),
(379,'Monte Aprazível',26),
(380,'Monte Azul Paulista',26),
(381,'Monte Castelo',26),
(382,'Monte Mor',26),
(383,'Monteiro Lobato',26),
(384,'Morro Agudo',26),
(385,'Morungaba',26),
(386,'Motuca',26),
(387,'Murutinga do Sul',26),
(388,'Nantes',26),
(389,'Narandiba',26),
(390,'Natividade da Serra',26),
(391,'Nazaré Paulista',26),
(392,'Neves Paulista',26),
(393,'Nhandeara',26),
(394,'Nipoã',26),
(395,'Nova Aliança',26),
(396,'Nova Campina',26),
(397,'Nova Canaã Paulista',26),
(398,'Nova Castilho',26),
(399,'Nova Europa',26),
(400,'Nova Granada',26),
(401,'Nova Guataporanga',26),
(402,'Nova Independência',26),
(403,'Nova Luzitânia',26),
(404,'Nova Odessa',26),
(405,'Novais',26),
(406,'Novo Horizonte',26),
(407,'Nuporanga',26),
(408,'Ocauçu',26),
(409,'Óleo',26),
(410,'Olímpia',26),
(411,'Onda Verde',26),
(412,'Oriente',26),
(413,'Orindiúva',26),
(414,'Orlândia',26),
(415,'Osasco',26),
(416,'Oscar Bressane',26),
(417,'Osvaldo Cruz',26),
(418,'Ourinhos',26),
(419,'Ouro Verde',26),
(420,'Ouroeste',26),
(421,'Pacaembu',26),
(422,'Palestina',26),
(423,'Palmares Paulista',26),
(424,'Palmeira d\'Oeste',26),
(425,'Palmital',26),
(426,'Panorama',26),
(427,'Paraguaçu Paulista',26),
(428,'Paraibuna',26),
(429,'Paraíso',26),
(430,'Paranapanema',26),
(431,'Paranapuã',26),
(432,'Parapuã',26),
(433,'Pardinho',26),
(434,'Pariquera-Açu',26),
(435,'Parisi',26),
(436,'Patrocínio Paulista',26),
(437,'Paulicéia',26),
(438,'Paulínia',26),
(439,'Paulistânia',26),
(440,'Paulo de Faria',26),
(441,'Pederneiras',26),
(442,'Pedra Bela',26),
(443,'Pedranópolis',26),
(444,'Pedregulho',26),
(445,'Pedreira',26),
(446,'Pedrinhas Paulista',26),
(447,'Pedro de Toledo',26),
(448,'Penápolis',26),
(449,'Pereira Barreto',26),
(450,'Pereiras',26),
(451,'Peruíbe',26),
(452,'Piacatu',26),
(453,'Piedade',26),
(454,'Pilar do Sul',26),
(455,'Pindamonhangaba',26),
(456,'Pindorama',26),
(457,'Pinhalzinho',26),
(458,'Piquerobi',26),
(459,'Piquete',26),
(460,'Piracaia',26),
(461,'Piracicaba',26),
(462,'Piraju',26),
(463,'Pirajuí',26),
(464,'Pirangi',26),
(465,'Pirapora do Bom Jesus',26),
(466,'Pirapozinho',26),
(467,'Pirassununga',26),
(468,'Piratininga',26),
(469,'Pitangueiras',26),
(470,'Planalto',26),
(471,'Platina',26),
(472,'Poá',26),
(473,'Poloni',26),
(474,'Pompéia',26),
(475,'Pongaí',26),
(476,'Pontal',26),
(477,'Pontalinda',26),
(478,'Pontes Gestal',26),
(479,'Populina',26),
(480,'Porangaba',26),
(481,'Porto Feliz',26),
(482,'Porto Ferreira',26),
(483,'Potim',26),
(484,'Potirendaba',26),
(485,'Pracinha',26),
(486,'Pradópolis',26),
(487,'Praia Grande',26),
(488,'Pratânia',26),
(489,'Presidente Alves',26),
(490,'Presidente Bernardes',26),
(491,'Presidente Epitácio',26),
(492,'Presidente Prudente',26),
(493,'Presidente Venceslau',26),
(494,'Promissão',26),
(495,'Quadra',26),
(496,'Quatá',26),
(497,'Queiroz',26),
(498,'Queluz',26),
(499,'Quintana',26),
(500,'Rafard',26),
(501,'Rancharia',26),
(502,'Redenção da Serra',26),
(503,'Regente Feijó',26),
(504,'Reginópolis',26),
(505,'Registro',26),
(506,'Restinga',26),
(507,'Ribeira',26),
(508,'Ribeirão Bonito',26),
(509,'Ribeirão Branco',26),
(510,'Ribeirão Corrente',26),
(511,'Ribeirão do Sul',26),
(512,'Ribeirão dos Índios',26),
(513,'Ribeirão Grande',26),
(514,'Ribeirão Pires',26),
(515,'Ribeirão Preto',26),
(516,'Rifaina',26),
(517,'Rincão',26),
(518,'Rinópolis',26),
(519,'Rio Claro',26),
(520,'Rio das Pedras',26),
(521,'Rio Grande da Serra',26),
(522,'Riolândia',26),
(523,'Riversul',26),
(524,'Rosana',26),
(525,'Roseira',26),
(526,'Rubiácea',26),
(527,'Rubinéia',26),
(528,'Sabino',26),
(529,'Sagres',26),
(530,'Sales',26),
(531,'Sales Oliveira',26),
(532,'Salesópolis',26),
(533,'Salmourão',26),
(534,'Saltinho',26),
(535,'Salto',26),
(536,'Salto de Pirapora',26),
(537,'Salto Grande',26),
(538,'Sandovalina',26),
(539,'Santa Adélia',26),
(540,'Santa Albertina',26),
(541,'Santa Bárbara d\'Oeste',26),
(542,'Santa Branca',26),
(543,'Santa Clara d\'Oeste',26),
(544,'Santa Cruz da Conceição',26),
(545,'Santa Cruz da Esperança',26),
(546,'Santa Cruz das Palmeiras',26),
(547,'Santa Cruz do Rio Pardo',26),
(548,'Santa Ernestina',26),
(549,'Santa Fé do Sul',26),
(550,'Santa Gertrudes',26),
(551,'Santa Isabel',26),
(552,'Santa Lúcia',26),
(553,'Santa Maria da Serra',26),
(554,'Santa Mercedes',26),
(555,'Santa Rita do Passa Quatro',26),
(556,'Santa Rita d\'Oeste',26),
(557,'Santa Rosa de Viterbo',26),
(558,'Santa Salete',26),
(559,'Santana da Ponte Pensa',26),
(560,'Santana de Parnaíba',26),
(561,'Santo Anastácio',26),
(562,'Santo André',26),
(563,'Santo Antônio da Alegria',26),
(564,'Santo Antônio de Posse',26),
(565,'Santo Antônio do Aracanguá',26),
(566,'Santo Antônio do Jardim',26),
(567,'Santo Antônio do Pinhal',26),
(568,'Santo Expedito',26),
(569,'Santópolis do Aguapeí',26),
(570,'Santos',26),
(571,'São Bento do Sapucaí',26),
(572,'São Bernardo do Campo',26),
(573,'São Caetano do Sul',26),
(574,'São Carlos',26),
(575,'São Francisco',26),
(576,'São João da Boa Vista',26),
(577,'São João das Duas Pontes',26),
(578,'São João de Iracema',26),
(579,'São João do Pau d\'Alho',26),
(580,'São Joaquim da Barra',26),
(581,'São José da Bela Vista',26),
(582,'São José do Barreiro',26),
(583,'São José do Rio Pardo',26),
(584,'São José do Rio Preto',26),
(585,'São José dos Campos',26),
(586,'São Lourenço da Serra',26),
(587,'São Luiz do Paraitinga',26),
(588,'São Manuel',26),
(589,'São Miguel Arcanjo',26),
(591,'São Pedro',26),
(592,'São Pedro do Turvo',26),
(593,'São Roque',26),
(594,'São Sebastião',26),
(595,'São Sebastião da Grama',26),
(596,'São Simão',26),
(597,'São Vicente',26),
(598,'Sarapuí',26),
(599,'Sarutaiá',26),
(600,'Sebastianópolis do Sul',26),
(601,'Serra Azul',26),
(602,'Serra Negra',26),
(603,'Serrana',26),
(604,'Sertãozinho',26),
(605,'Sete Barras',26),
(606,'Severínia',26),
(607,'Silveiras',26),
(608,'Socorro',26),
(609,'Sorocaba',26),
(610,'Sud Mennucci',26),
(611,'Sumaré',26),
(612,'Suzanápolis',26),
(613,'Suzano',26),
(614,'Tabapuã',26),
(615,'Tabatinga',26),
(616,'Taboão da Serra',26),
(617,'Taciba',26),
(618,'Taguaí',26),
(619,'Taiaçu',26),
(620,'Taiúva',26),
(621,'Tambaú',26),
(622,'Tanabi',26),
(623,'Tapiraí',26),
(624,'Tapiratiba',26),
(625,'Taquaral',26),
(626,'Taquaritinga',26),
(627,'Taquarituba',26),
(628,'Taquarivaí',26),
(629,'Tarabai',26),
(630,'Tarumã',26),
(631,'Tatuí',26),
(632,'Taubaté',26),
(633,'Tejupá',26),
(634,'Teodoro Sampaio',26),
(635,'Terra Roxa',26),
(636,'Tietê',26),
(637,'Timburi',26),
(638,'Torre de Pedra',26),
(639,'Torrinha',26),
(640,'Trabiju',26),
(641,'Tremembé',26),
(642,'Três Fronteiras',26),
(643,'Tuiuti',26),
(644,'Tupã',26),
(645,'Tupi Paulista',26),
(646,'Turiúba',26),
(647,'Turmalina',26),
(648,'Ubarana',26),
(649,'Ubatuba',26),
(650,'Ubirajara',26),
(651,'Uchoa',26),
(652,'União Paulista',26),
(653,'Urânia',26),
(654,'Uru',26),
(655,'Urupês',26),
(656,'Valentim Gentil',26),
(657,'Valinhos',26),
(658,'Valparaíso',26),
(659,'Vargem',26),
(660,'Vargem Grande do Sul',26),
(661,'Vargem Grande Paulista',26),
(662,'Várzea Paulista',26),
(663,'Vera Cruz',26),
(664,'Vinhedo',26),
(665,'Viradouro',26),
(666,'Vista Alegre do Alto',26),
(667,'Vitória Brasil',26),
(668,'Votorantim',26),
(669,'Votuporanga',26),
(670,'Zacarias',26),
(671,'Campo Grande',19);
/*!40000 ALTER TABLE `cp_localidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_marca`
--

DROP TABLE IF EXISTS `cp_marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_marca` (
  `ID_MARCA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DT_INI_MARCA` datetime DEFAULT NULL,
  `DT_FIM_MARCA` datetime DEFAULT NULL,
  `ID_MARCADOR` INT UNSIGNED DEFAULT NULL,
  `ID_PESSOA_INI` INT UNSIGNED DEFAULT NULL,
  `ID_LOTACAO_INI` INT UNSIGNED DEFAULT NULL,
  `ID_MOBIL` INT UNSIGNED DEFAULT NULL,
  `ID_TP_MARCA` INT UNSIGNED DEFAULT NULL,
  `ID_REF` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_MARCA`),
  KEY `LOTACAO` (`ID_LOTACAO_INI`),
  KEY `PESSOA` (`ID_PESSOA_INI`),
  KEY `CP_MARCA_IDX_004` (`ID_REF`),
  KEY `ID_MARCADOR` (`ID_MARCADOR`),
  KEY `ID_TP_MARCA` (`ID_TP_MARCA`),
  KEY `ID_MOBIL` (`ID_MOBIL`),
  CONSTRAINT `cp_marca_ibfk_1` FOREIGN KEY (`ID_MARCADOR`) REFERENCES `cp_marcador` (`ID_MARCADOR`),
  CONSTRAINT `cp_marca_ibfk_2` FOREIGN KEY (`ID_PESSOA_INI`) REFERENCES `dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `cp_marca_ibfk_3` FOREIGN KEY (`ID_LOTACAO_INI`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `cp_marca_ibfk_4` FOREIGN KEY (`ID_TP_MARCA`) REFERENCES `cp_tipo_marca` (`ID_TP_MARCA`),
  CONSTRAINT `cp_marca_ibfk_5` FOREIGN KEY (`ID_MOBIL`) REFERENCES `siga`.`ex_mobil` (`ID_MOBIL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_marca`
--

LOCK TABLES `cp_marca` WRITE;
/*!40000 ALTER TABLE `cp_marca` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_marcador`
--

DROP TABLE IF EXISTS `cp_marcador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_marcador` (
  `ID_MARCADOR` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESCR_MARCADOR` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_TP_MARCADOR` INT UNSIGNED DEFAULT NULL,
  `ORD_MARCADOR` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_MARCADOR`),
  KEY `ID_TP_MARCADOR` (`ID_TP_MARCADOR`),
  CONSTRAINT `cp_marcador_ibfk_1` FOREIGN KEY (`ID_TP_MARCADOR`) REFERENCES `cp_tipo_marcador` (`ID_TP_MARCADOR`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_marcador`
--

LOCK TABLES `cp_marcador` WRITE;
/*!40000 ALTER TABLE `cp_marcador` DISABLE KEYS */;
INSERT INTO `cp_marcador` VALUES (1,'Em Elaboração',1,10),
(2,'Aguardando Andamento',1,140),
(3,'A Receber (Físico)',1,90),
(4,'Extraviado',1,NULL),
(5,'A Arquivar',1,NULL),
(6,'Arquivo Corrente',1,200),
(7,'A Eliminar',1,230),
(8,'Eliminado',1,NULL),
(9,'Juntado',1,NULL),
(10,'Cancelado',1,NULL),
(11,'Transferido para Órgão Externo',1,NULL),
(12,'Arquivo Intermediário',1,NULL),
(13,'Arquivo Permanente',1,NULL),
(14,'Caixa de Entrada (Digital)',1,80),
(15,'Pendente de Assinatura',1,40),
(16,'Juntado a Documento Externo',1,NULL),
(18,'Remetido para Publicação',1,NULL),
(20,'Publicado',1,NULL),
(21,'Publicação solicitada',1,NULL),
(22,'Disponibilizado',1,NULL),
(23,'Transferido',1,170),
(24,'Transferido (Digital)',1,180),
(25,'Como Subscritor',1,30),
(26,'Apensado',1,NULL),
(27,'Como Gestor',1,150),
(28,'Como Interessado',1,160),
(29,'Despacho Pendente de Assinatura',1,70),
(30,'Anexo Pendente Assinatura/Conferência',1,60),
(31,'Sobrestado',1,190),
(32,'Sem Efeito',1,NULL),
(36,'Ativo',1,NULL),
(37,'Novo',1,NULL),
(38,'Popular',1,NULL),
(39,'Revisar',1,NULL),
(40,'Tomar Ciência',1,NULL),
(41,'A Receber',1,NULL),
(42,'Em Andamento',1,NULL),
(43,'Fechado',1,NULL),
(44,'Pendente',1,NULL),
(45,'Cancelado',1,NULL),
(46,'Em Pré-atendimento',1,NULL),
(47,'Em Pós-atendimento',1,NULL),
(48,'Como cadastrante',1,NULL),
(49,'Como solicitante',1,NULL),
(50,'A Recolher para Arq. Permanente',1,220),
(51,'A Transferir para Arq. Intermediário',1,210),
(52,'Em Edital de Eliminação',1,240),
(53,'A Fechar',1,NULL),
(54,'Em Controle de Qualidade',1,NULL),
(55,'Agendado',1,NULL),
(56,'A devolver',1,100),
(57,'Aguardando devolução',1,120),
(58,'A devolver (Fora do prazo)',1,110),
(59,'Aguardando devolução (Fora do prazo)',1,130),
(60,'Pendente de Anexação',1,NULL),
(61,'Em Elabora?',1,NULL),
(62,'Documento Assinado com Senha',1,NULL),
(63,'Movimentação Assinada com Senha',1,NULL),
(64,'Movimentação Conferida com Senha',1,NULL),
(65,'Fora do Prazo',1,NULL),
(66,'Ativo',1,NULL),
(67,'Elaborar Parte de Documento Colaborativo',1,5),
(68,'Finalizar Documento Colaborativo',1,6),
(69,'Necessita Providência',1,NULL),
(70,'Como Executor',1,NULL),
(71,'Pronto para Assinar',2,25),
(72,'Como Revisor',2,25),
(300,'A Receber',1,NULL),
(301,'Em Andamento',1,NULL),
(302,'Fechado',1,NULL),
(303,'Pendente',1,NULL),
(304,'Cancelado',1,NULL),
(305,'Em Pré-atendimento',1,NULL),
(306,'Em Pós-atendimento',1,NULL),
(307,'Como cadastrante',1,NULL),
(308,'Como solicitante',1,NULL),
(1000,'Urgente',2,NULL),
(1001,'Idoso',2,NULL),
(1002,'Retenção de INSS',2,NULL);
/*!40000 ALTER TABLE `cp_marcador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_arquivo`
--

DROP TABLE IF EXISTS `cp_arquivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_arquivo` (
  `ID_ARQ` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `CONTEUDO_TP_ARQ` VARCHAR(256),
  PRIMARY KEY (`ID_ARQ`),
  KEY `ID_ORGAO_USU` (`ID_ORGAO_USU`),
  CONSTRAINT `cp_arq_ibfk_1` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_arquivo`
--

LOCK TABLES `cp_arquivo` WRITE;
/*!40000 ALTER TABLE `cp_arquivo` DISABLE KEYS */;
INSERT INTO `cp_arquivo` VALUES (1,null,'template/freemarker'),
(2,null,'template/freemarker'),
(3,null,'template/freemarker'),
(4,NULL,'template/freemarker'),
(5,NULL,'template/freemarker'),
(6,NULL,'template/freemarker'),
(519,NULL,'template/freemarker'),
(7,NULL,'template/freemarker'),
(8,NULL,'template/freemarker'),
(9,NULL,'template/freemarker'),
(10,NULL,'template/freemarker');
/*!40000 ALTER TABLE `cp_arquivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_arquivo_blob`
--

DROP TABLE IF EXISTS `cp_arquivo_blob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_arquivo_blob` (
  `ID_ARQ_BLOB` BIGINT UNSIGNED NOT NULL,
  `CONTEUDO_ARQ_BLOB` LONGBLOB DEFAULT NULL,
  PRIMARY KEY (`ID_ARQ_BLOB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_arquivo_blob`
--

LOCK TABLES `cp_arquivo_blob` WRITE;
/*!40000 ALTER TABLE `cp_arquivo_blob` DISABLE KEYS */;
INSERT INTO `cp_arquivo_blob` VALUES (1,' \n[@oficio/]\n'),
(2,'\n[@entrevista]\n	[@grupo titulo=\"Texto a ser inserido no corpo do memorando\"]\n		[@grupo]\n			[@editor titulo=\"\" var=\"texto_memorando\" /]\n		[/@grupo]\n	[/@grupo]\n	[@grupo]\n	        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n	[/@grupo]\n	[@grupo]\n       		[@selecao titulo=\"Fecho\" var=\"fecho\" opcoes=\"Atenciosamente;Cordialmente;Respeitosamente\" /]\n	[/@grupo]\n[/@entrevista]\n\n[@documento]\n        [@memorando texto=texto_memorando! fecho=(fecho!)+\",\" tamanhoLetra=tamanhoLetra! /]\n[/@documento]\n'),
(3,'\n[@entrevista]\n    [@grupo titulo=\"Dados do Documento de Origem\"]\n        [@grupo]\n            [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n            [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n        [/@grupo]\n        [@grupo]\n            [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n            [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n        [/@grupo]\n    [/@grupo]\n    [@grupo]\n        [@texto titulo=\"Vocativo\" var=\"vocativo\" largura=\"100\" /]\n    [/@grupo]\n    [@grupo titulo=\"Texto da informação\"]\n        [@editor titulo=\"\" var=\"texto_informacao\" /]\n    [/@grupo]\n    [@grupo]\n        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n    [/@grupo]\n[/@entrevista]\n\n[@documento margemDireita=\"3cm\"]\n    [#if tamanhoLetra! == \"Normal\"]\n        [#assign tl = \"11pt\" /]\n    [#elseif tamanhoLetra! == \"Pequeno\"]\n        [#assign tl = \"9pt\" /]\n    [#elseif tamanhoLetra! == \"Grande\"]\n        [#assign tl = \"13pt\" /]\n    [#else]     \n        [#assign tl = \"11pt\"]\n    [/#if]\n\n    [@estiloBrasaoCentralizado tipo=\"INFORMAÇÃO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n            [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n                Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>\n            [/#if]\n            Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${vocativo!}</span></p>\n                <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${texto_informacao}</span></p>\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n'),
(4,'\n[#-- Se existir uma variável chamada \'texto\' copiar seu valor para \'texto_depacho\', pois a macro vai destruir o conteúdo da variável --]\n[@entrevista]\n  [@grupo titulo=\"Órgão de destino\"]\n    [#assign orgao_dest_atual = (doc.lotaDestinatario.nomeLotacao)!/]\n    [#if orgao_dest_ult! != orgao_dest_atual]\n      [#assign orgao_dest = orgao_dest_atual/]\n      [#assign orgao_dest_ult = orgao_dest_atual/]\n    [/#if]\n    [@oculto var=\"orgao_dest_ult\"/]\n    [@grupo]\n      [@selecao titulo=\"\" var=\"combinacao\" opcoes=\"A(o);À;Ao\" /]\n      [@texto titulo=\"Nome (opcional)\" var=\"orgao_dest\" largura=30 /]\n    [/@grupo]\n  [/@grupo]\n  [@grupo titulo=\"Texto do despacho\"]\n    [@grupo]\n      [#if !texto_padrao??]\n        [#assign texto_padrao = \"Para as providências cabíveis.\"/]\n      [/#if]\n      [@selecao titulo=\"Texto\" opcoes=\"A pedido.;Arquive-se.;Autorizo.;Ciente. Arquive-se.;De acordo.;Expeça-se memorando.;Expeça-se memorando-circular.;Expeça-se ofício-circular.;Intime-se.;Junte-se ao dossiê.;Junte-se ao processo.;Oficie-se.;Para as providências cabíveis.;Para atendimento.;Para atendimento e encaminhamento direto.;Para ciência.;Para publicação.;Para verificar a possibilidade de atendimento.;[Outro]\" var=\"texto_padrao\" reler=true /]\n    [/@grupo]\n  [/@grupo]\n  [@grupo depende=\"textopadrao\" esconder=((texto_padrao!\"\") != \"[Outro]\")]\n    [@editor titulo=\"\" var=\"texto_despacho\" /]\n  [/@grupo]\n  [@grupo]\n    [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n  [/@grupo]\n  [@grupo titulo=\"Dados do Documento de Origem\" esconder=doc.pai??]\n    [#if postback?? && !doc.idDoc?? && doc.pai??]\n      [#assign tipoDeDocumentoValue = doc.pai.descrFormaDoc /]\n      [#assign numeroValue = doc.pai.sigla /]\n      [#assign dataValue = doc.pai.dtDocDDMMYY /]\n      [#assign orgaoValue = doc.pai.orgaoUsuario.acronimoOrgaoUsu /]\n    [#else]\n      [#assign tipoDeDocumentoValue = tipoDeDocumentoOrigem! /]\n      [#assign numeroValue = numeroOrigem! /]\n      [#assign dataValue = dataOrigem! /]\n      [#assign orgaoValue = orgaoOrigem! /]\n    [/#if]\n    [@grupo]\n      [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n      [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n    [/@grupo]\n    [@grupo]\n      [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n      [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n    [/@grupo]\n  [/@grupo]\n[/@entrevista]\n[@documento margemDireita=\"3cm\"]\n  [#if param.tamanhoLetra! == \"Normal\"]\n    [#assign tl = \"11pt\" /]\n  [#elseif param.tamanhoLetra! == \"Pequeno\"]\n    [#assign tl = \"9pt\" /]\n  [#elseif param.tamanhoLetra! == \"Grande\"]\n    [#assign tl = \"13pt\" /]\n  [#else]\n    [#assign tl = \"11pt\"/]\n  [/#if]\n  [@estiloBrasaoCentralizado tipo=\"DESPACHO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n    <div style=\"font-family: Arial; font-size: ${tl};\">\n      [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n        Referência: ${tipoDeDocumentoOrigem!} Nº ${numeroOrigem!}\n        [#if dataOrigem?? && dataOrigem != \"\"]\n          , ${dataOrigem!}\n        [/#if]\n        [#if orgaoOrigem?? && orgaoOrigem != \"\"]\n          - ${orgaoOrigem!}.\n        [/#if]\n        <br />\n      [/#if]\n      Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n    </div>\n    <div style=\"font-family: Arial; font-size: ${tl};\">\n      [#if orgao_dest?? && orgao_dest != \"\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl}\">${combinacao} ${orgao_dest!},</span>\n        </p>\n      [#elseif (doc.lotaDestinatario.nomeLotacao)?? && (doc.lotaDestinatario.nomeLotacao) != \"\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl}\">${combinacao} ${(doc.lotaDestinatario.nomeLotacao)!},</span>\n        </p>\n      [/#if]\n      [#if (texto_padrao!\"\") != \"[Outro]\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl!}\">${texto_padrao!}</span>\n        </p>\n      [#else]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl!}\">${texto_despacho!}</span>\n        </p>\n      [/#if]\n    </div>\n  [/@estiloBrasaoCentralizado]\n[/@documento]\n'),
(5,'ELABORE SEU MODELO DE BOLETIM INTERNO NO MENU FERRAMENTAS/CADASTRO DE MODELOS'),
(6,'\n[@entrevista]\n    [@grupo titulo=\"Dados do Documento de Origem\"]\n        [@grupo]\n            [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n            [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n        [/@grupo]\n        [@grupo]\n            [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n            [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n        [/@grupo]\n    [/@grupo]\n    [@grupo]\n        [@texto titulo=\"Vocativo\" var=\"vocativo\" largura=\"100\" /]\n    [/@grupo]\n    [@grupo titulo=\"Texto do Parecer\"]\n        [@editor titulo=\"\" var=\"texto_parecer\" /]\n    [/@grupo]\n    [@grupo]\n        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n    [/@grupo]\n[/@entrevista]\n\n[@documento margemDireita=\"3cm\"]\n    [#if param.tamanhoLetra! == \"Normal\"]\n        [#assign tl = \"11pt\" /]\n    [#elseif param.tamanhoLetra! == \"Pequeno\"]\n        [#assign tl = \"9pt\" /]\n    [#elseif param.tamanhoLetra! == \"Grande\"]\n        [#assign tl = \"13pt\" /]\n    [#else]     \n        [#assign tl = \"11pt\"]\n    [/#if]\n\n    [@estiloBrasaoCentralizado tipo=\"PARECER\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n            Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>\n        [/#if]\n        Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n\n        <div style=\"font-family: Arial; font-size: 10pt;\">\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${vocativo!}</span></p>\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${texto_parecer}</span></p>\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">É o Parecer.</span></p>\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n\n'),
(7,' Certidão de desentranhamento'),
(8,'\n[@entrevista]\n [@grupo titulo=\"Informações Gerais\"]\n  [@grupo]\n   [@texto titulo=\"Objetivo da reunião\" var=\"objReuniao\" largura=\"84\" maxcaracteres=\"84\"/]\n  [/@grupo]\n  [@texto titulo=\"Horário\" var=\"horReuniao\" obrigatorio=\"Sim\" largura=\"4\" maxcaracteres=\"5\"/]\n  [@texto titulo=\"Local\" var=\"locReuniao\" obrigatorio=\"Sim\" largura=\"60\" maxcaracteres=\"60\"/]\n[#--\n  [@grupo]\n   [@memo titulo=\"Pendências (reuniões anteriores)\" var=\"pendencias\" colunas=\"78\" linhas=\"2\"/]\n  [/@grupo]\n--]\n  [@separador /]\n  [@selecao titulo=\"Participantes\" var=\"numParticipantes\" reler=true idAjax=\"numParticipantesAjax\" opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n  [@grupo depende=\"numParticipantesAjax\"]\n   [#if numParticipantes! != \'0\']\n    [#list 1..(numParticipantes)?number as i]\n     [@grupo]\n      [@pessoa titulo=\"\" var=\"participantes\"+i/]\n     [/@grupo]\n    [/#list]\n   [/#if]\n  [/@grupo]\n  [@separador /]\n  [@grupo]\n   [@selecao titulo=\"Participantes (extra)\" var=\"numParticipantesExtra\" reler=true idAjax=\"numPartExtraAjax\" opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n  [/@grupo]\n  [@grupo depende=\"numPartExtraAjax\"]\n   [#if numParticipantesExtra! != \'0\']\n    [#list 1..(numParticipantesExtra)?number as i]\n     [@grupo]\n      [@texto titulo=\"Nome\" var=\"participantesExtra\"+i largura=\"50\" maxcaracteres=\"101\"/]\n      [@texto titulo=\"Email\" var=\"participantesExtraEmail\"+i largura=\"51\" maxcaracteres=\"101\"/]\n     [/@grupo]\n     [@grupo]\n      [@texto titulo=\"Função\" var=\"participantesExtraFuncao\"+i largura=\"50\" maxcaracteres=\"101\"/]\n      [@texto titulo=\"Unidade\" var=\"participantesExtraUnidade\"+i largura=\"51\" maxcaracteres=\"101\"/]\n     [/@grupo]\n    [/#list]\n   [/#if]\n  [/@grupo]\n [/@grupo]\n [@separador /]\n [@grupo]\n  [@selecao titulo=\"Quantidade de itens da pauta\" var=\"qtdItePauta\" reler=true idAjax=\"qtdItePautaAjax\" opcoes=\"1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n [/@grupo]\n [@grupo depende=\"qtdItePautaAjax\"]\n  [#list 1..(qtdItePauta)?number as i]\n   [@grupo]\n    [@texto titulo=\"<b>Item ${i}</b>\" var=\"itePauta\"+i largura=\"96\" maxcaracteres=\"101\"/]\n   [/@grupo]\n   [@memo titulo=\"Comentários\" var=\"comentario\"+i colunas=\"78\" linhas=\"2\"/]\n   [@grupo]\n    [@selecao titulo=\"Quantidade de ações\" var=\"qtdAcoes\"+i reler=true idAjax=\"qtdAcoesAjax\"+i opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n   [/@grupo]\n   [@grupo depende=\"qtdAcoesAjax\"+i]\n    [#if (.vars[\'qtdAcoes\'+i])! != \'0\']\n     [#list 1..(.vars[\'qtdAcoes\'+i])?number as j]\n      [@grupo]      \n       [@texto titulo=\"Ação ${j}\" var=\"acoes\"+i+j largura=\"95\" maxcaracteres=\"75\"/]\n      [/@grupo]\n      [@grupo]\n       [@texto titulo=\"Responsável\" var=\"responsavel\"+i+j largura=\"61\" maxcaracteres=\"55\"/]\n       [@data titulo=\"Data prevista\" var=\"datPrevista\"+i+j/] \n      [/@grupo]\n     [/#list]\n    [/#if]\n   [/@grupo]\n   [@separador /]  \n  [/#list] \n [/@grupo]\n[/@entrevista]\n\n[@documento]\n [#if tamanhoLetra! == \"Normal\"]\n		[#assign tl = \"11pt\" /]\n [#elseif tamanhoLetra! == \"Pequeno\"]\n		[#assign tl = \"9pt\" /]\n [#elseif tamanhoLetra! == \"Grande\"]\n		[#assign tl = \"13pt\" /]\n [#else]		\n		[#assign tl = \"11pt\"]\n [/#if]\n [@estiloBrasaoCentralizado tipo=\"MEMÓRIA DE REUNIÃO\" tamanhoLetra=tl formatarOrgao=true]\n   <p align=\"left\">\n    <b>Objetivo da reunião:</b>&nbsp;${objReuniao!}<br/>\n    Horário e local: ${horReuniao!} - ${locReuniao!}<br/>\n     Assunto: ${(doc.exClassificacao.descrClassificacao)!}</p><br/>\n[#--\n   [#if pendencias! != \"\"]\n    <p><b>Pendências (reuniões anteriores):</b>&nbsp;${pendencias!}</p><br/>\n   [/#if]\n--]\n   <table width=\"100%\" border=\"1\" cellpadding=\"5\">\n    <tr>\n     <td width=\"50%\"><b>Participantes</b></td>\n     <td width=\"25%\"><b>Função</b></td>\n     <td width=\"25%\"><b>Unidade</b></td>\n    </tr>\n    [#list 1..(numParticipantes)?number as i]\n     [#if .vars[\'participantes\' + i + \'_pessoaSel.id\']?? && .vars[\'participantes\' + i + \'_pessoaSel.id\'] != \"\"]\n      [#assign participante = func.pessoa(.vars[\'participantes\' + i + \'_pessoaSel.id\']?number) /]\n      <tr>\n       <td>${func.maiusculasEMinusculas(participante.descricao)}</td>\n       <td>${(participante.funcaoConfianca.descricao)!}</td>\n       <td>${participante.lotacao.sigla}</td>\n      </tr>\n     [/#if]\n    [/#list]\n\n    [#list 1..(numParticipantesExtra)?number as i] \n     [#if numParticipantesExtra! != \'0\']\n      <tr>\n       <td>${.vars[\'participantesExtra\'+i]!}\n        [#if .vars[\'participantesExtraEmail\'+i]??] (${.vars[\'participantesExtraEmail\'+i]!})[/#if]</td>\n       <td>${.vars[\'participantesExtraFuncao\'+i]!}</td>\n       <td>${.vars[\'participantesExtraUnidade\'+i]!}</td>\n      </tr>\n     [/#if]    \n    [/#list]\n   </table> \n   <br/>\n\n\n  \n   <p><b>Pauta</b></p>\n     [#list 1..(qtdItePauta)?number as i] \n        <p>\n          <b>${i}. ${.vars[\'itePauta\'+i]!}:</b> ${.vars[\'comentario\'+i]!}\n        </p>       \n     [/#list]  \n\n\n  <br/> \n\n  [#assign fAcoes = false/]\n  [#list 1..(qtdItePauta)?number as i]\n   [#if .vars[\'qtdAcoes\'+i] != \'0\']\n    [#assign fAcoes = true/]\n   [/#if] \n  [/#list]\n\n  [#if fAcoes]\n   <table width=\"100%\" border=\"1\" cellpadding=\"5\">\n     <tr>\n      <td width=\"10%\"><b>Ref.</b></td>\n      <td width=\"50%\"><b>Próximas Ações</b></td>\n      <td width=\"20%\"><b>Responsável</b></td>\n      <td width=\"20%\" align=\"center\"><b>Data Prevista</b></td>\n     </tr>\n     [#list 1..(qtdItePauta)?number as i]\n      [#if .vars[\'qtdAcoes\'+i] != \'0\']\n       [#list 1..(.vars[\'qtdAcoes\'+i])?number as j]\n        <tr>\n         <td>${i}.${j}</td>\n         <td>${.vars[\'acoes\'+i+j]!}</td>\n         <td>${.vars[\'responsavel\'+i+j]!}</td>\n         <td align=\"center\">${.vars[\'datPrevista\'+i+j]!}</td>\n        </tr>\n       [/#list]\n      [/#if]\n     [/#list]\n   </table>   \n  [/#if]\n [/@estiloBrasaoCentralizado]\n[/@documento]\n\n'),
(9,'\n[@documento margemDireita=\"3cm\"]\n    [#assign tl=\"11pt\"/]\n    [@estiloBrasaoCentralizado tipo=\"DESPACHO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n          Referência: ${doc.codigo} de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}[#if doc.lotaTitular??] - ${(doc.lotaTitular.descricao)!}[/#if].<br/>\n            Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n        </div>\n\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n            [#if mov.lotaResp?? && (mov.lotaResp.idLotacaoIni != mov.lotaCadastrante.idLotacaoIni)]\n                <p style=\"TEXT-INDENT: 0cm\">\n                  <span style=\"font-size: ${tl}\">À ${(mov.lotaResp.descricao)!},</span>\n                </p>\n            [/#if]\n            [#if despachoTexto??]\n                <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${despachoTexto}</span></p>\n            [/#if]\n            ${despachoHtml!}\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n\n'),
(10,' ');
/*!40000 ALTER TABLE `cp_arquivo_blob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_modelo`
--

DROP TABLE IF EXISTS `cp_modelo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_modelo` (
  `ID_MODELO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `ID_ARQ` BIGINT UNSIGNED DEFAULT NULL,
  `CONTEUDO_BLOB_MOD` blob,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_INI` datetime NOT NULL,
  `HIS_IDC_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_FIM` INT UNSIGNED DEFAULT NULL,
  `HIS_ATIVO` tinyint(4) NOT NULL,
  PRIMARY KEY (`ID_MODELO`),
  KEY `ID_ORGAO_USU` (`ID_ORGAO_USU`),
  KEY `HIS_IDC_INI` (`HIS_IDC_INI`),
  KEY `HIS_IDC_FIM` (`HIS_IDC_FIM`),
  KEY `HIS_ID_INI` (`HIS_ID_INI`),
  KEY `ID_ARQ` (`ID_ARQ`),
  CONSTRAINT `cp_modelo_ibfk_1` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `cp_modelo_ibfk_2` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `cp_modelo_ibfk_3` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `cp_modelo_ibfk_4` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `cp_modelo` (`ID_MODELO`),
  CONSTRAINT `cp_modelo_ibfk_5` FOREIGN KEY (`ID_ARQ`) REFERENCES `cp_arquivo` (`ID_ARQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_modelo`
--

LOCK TABLES `cp_modelo` WRITE;
/*!40000 ALTER TABLE `cp_modelo` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_modelo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_ocorrencia_feriado`
--

DROP TABLE IF EXISTS `cp_ocorrencia_feriado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_ocorrencia_feriado` (
  `ID_OCORRENCIA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DT_INI_FERIADO` datetime DEFAULT NULL,
  `DT_FIM_FERIADO` datetime DEFAULT NULL,
  `ID_FERIADO` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_OCORRENCIA`),
  KEY `ID_FERIADO` (`ID_FERIADO`),
  CONSTRAINT `cp_ocorrencia_feriado_ibfk_1` FOREIGN KEY (`ID_FERIADO`) REFERENCES `cp_feriado` (`ID_FERIADO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_ocorrencia_feriado`
--

LOCK TABLES `cp_ocorrencia_feriado` WRITE;
/*!40000 ALTER TABLE `cp_ocorrencia_feriado` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_ocorrencia_feriado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_orgao`
--

DROP TABLE IF EXISTS `cp_orgao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_orgao` (
  `ID_ORGAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_ORGAO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CGC_ORGAO` INT UNSIGNED DEFAULT NULL,
  `RAZAO_SOCIAL_ORGAO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `END_ORGAO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `BAIRRO_ORGAO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MUNICIPIO_ORGAO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CEP_ORGAO` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DSC_TIPO_ORGAO` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NOME_RESPONSAVEL_ORGAO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `EMAIL_RESPONSAVEL_ORGAO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NOME_CONTATO_ORGAO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `EMAIL_CONTATO_ORGAO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TEL_CONTATO_ORGAO` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SIGLA_ORGAO` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `UF_ORGAO` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `FG_ATIVO` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_IDE` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_ATIVO` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_ORGAO`),
  UNIQUE KEY `ORGAO_PK` (`ID_ORGAO`),
  KEY `ORGAO_ORGAO_USU_FK` (`ID_ORGAO_USU`),
  CONSTRAINT `ORGAO_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_orgao`
--

LOCK TABLES `cp_orgao` WRITE;
/*!40000 ALTER TABLE `cp_orgao` DISABLE KEYS */;
INSERT INTO `cp_orgao` VALUES (1,'PRESIDENCIA DA REPUBLICA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Pres. Rep.',NULL,999999999,'S',1,'1','2011-08-03 00:00:00',NULL,1);
/*!40000 ALTER TABLE `cp_orgao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_orgao_usuario`
--

DROP TABLE IF EXISTS `cp_orgao_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_orgao_usuario` (
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_ORGAO_USU` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CGC_ORGAO_USU` bigint(20) DEFAULT NULL,
  `RAZAO_SOCIAL_ORGAO_USU` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `END_ORGAO_USU` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `BAIRRO_ORGAO_USU` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MUNICIPIO_ORGAO_USU` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CEP_ORGAO_USU` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NM_RESP_ORGAO_USU` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TEL_ORGAO_USU` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SIGLA_ORGAO_USU` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `UF_ORGAO_USU` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `COD_ORGAO_USU` INT UNSIGNED DEFAULT NULL,
  `ACRONIMO_ORGAO_USU` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_ORGAO_USU`),
  UNIQUE KEY `ORGAO_USU_PK` (`ID_ORGAO_USU`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_orgao_usuario`
--

LOCK TABLES `cp_orgao_usuario` WRITE;
/*!40000 ALTER TABLE `cp_orgao_usuario` DISABLE KEYS */;
INSERT INTO `cp_orgao_usuario` VALUES (999999999,'ORGAO TESTE ZZ',NULL,NULL,NULL,NULL,'Rio de Janeiro',NULL,NULL,NULL,'ZZ',NULL,NULL,'OTZZ');
/*!40000 ALTER TABLE `cp_orgao_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_papel`
--

DROP TABLE IF EXISTS `cp_papel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_papel` (
  `ID_PAPEL` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_TP_PAPEL` INT UNSIGNED NOT NULL,
  `ID_PESSOA` INT UNSIGNED NOT NULL,
  `ID_LOTACAO` INT UNSIGNED NOT NULL,
  `ID_FUNCAO_CONFIANCA` INT UNSIGNED DEFAULT NULL,
  `ID_CARGO` INT UNSIGNED NOT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `HIS_ID_INI` INT UNSIGNED DEFAULT NULL,
  `HIS_IDE` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `HIS_DT_INI` datetime NOT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_ATIVO` tinyint(4) NOT NULL,
  PRIMARY KEY (`ID_PAPEL`),
  UNIQUE KEY `CP_PAPEL_PK` (`ID_PAPEL`),
  KEY `CP_PAPEL_CPORGUSU_IDORGUSU_FK` (`ID_ORGAO_USU`),
  KEY `CP_PAPEL_CPTPPAP_ID_TPPAP_FK` (`ID_TP_PAPEL`),
  KEY `CP_PAPEL_CP_PAPEL_ID_INI_FK` (`HIS_ID_INI`),
  KEY `CP_PAPEL_DP_CARGO_ID_CARGO_FK` (`ID_CARGO`),
  KEY `CP_PAPEL_DP_FC_ID_FC_FK` (`ID_FUNCAO_CONFIANCA`),
  KEY `CP_PAPEL_DP_LOT_ID_LOT_FK` (`ID_LOTACAO`),
  KEY `CP_PAPEL_DP_PESS_ID_PESS_FK` (`ID_PESSOA`),
  CONSTRAINT `CP_PAPEL_CPORGUSU_IDORGUSU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `CP_PAPEL_CPTPPAP_ID_TPPAP_FK` FOREIGN KEY (`ID_TP_PAPEL`) REFERENCES `cp_tipo_papel` (`ID_TP_PAPEL`),
  CONSTRAINT `CP_PAPEL_CP_PAPEL_ID_INI_FK` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `cp_papel` (`ID_PAPEL`),
  CONSTRAINT `CP_PAPEL_DP_CARGO_ID_CARGO_FK` FOREIGN KEY (`ID_CARGO`) REFERENCES `dp_cargo` (`ID_CARGO`),
  CONSTRAINT `CP_PAPEL_DP_FC_ID_FC_FK` FOREIGN KEY (`ID_FUNCAO_CONFIANCA`) REFERENCES `dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`),
  CONSTRAINT `CP_PAPEL_DP_LOT_ID_LOT_FK` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `CP_PAPEL_DP_PESS_ID_PESS_FK` FOREIGN KEY (`ID_PESSOA`) REFERENCES `dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_papel`
--

LOCK TABLES `cp_papel` WRITE;
/*!40000 ALTER TABLE `cp_papel` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_papel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_personalizacao`
--

DROP TABLE IF EXISTS `cp_personalizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_personalizacao` (
  `ID_PESSOA` INT UNSIGNED NOT NULL,
  `ID_PAPEL_ATIVO` INT UNSIGNED DEFAULT NULL,
  `ID_SUBSTITUINDO_PESSOA` INT UNSIGNED DEFAULT NULL,
  `ID_SUBSTITUINDO_LOTACAO` INT UNSIGNED DEFAULT NULL,
  `ID_SUBSTITUINDO_PAPEL` INT UNSIGNED DEFAULT NULL,
  `NM_SIMULANDO_USUARIO` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_PERSONALIZACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID_PERSONALIZACAO`),
  KEY `CP_PERSON_CP_PAP_ID_ATIVO_FK` (`ID_PAPEL_ATIVO`),
  KEY `CP_PERSON_CP_PAP_ID_SUBST_FK` (`ID_SUBSTITUINDO_PAPEL`),
  KEY `CP_PERSON_DP_LOT_ID_SUBST_FK` (`ID_SUBSTITUINDO_LOTACAO`),
  KEY `CP_PERSON_DP_PESS_ID_PESS_FK` (`ID_PESSOA`),
  KEY `CP_PERSON_DP_PESS_ID_SUBST_FK` (`ID_SUBSTITUINDO_PESSOA`),
  CONSTRAINT `CP_PERSON_CP_PAP_ID_ATIVO_FK` FOREIGN KEY (`ID_PAPEL_ATIVO`) REFERENCES `cp_papel` (`ID_PAPEL`),
  CONSTRAINT `CP_PERSON_CP_PAP_ID_SUBST_FK` FOREIGN KEY (`ID_SUBSTITUINDO_PAPEL`) REFERENCES `cp_papel` (`ID_PAPEL`),
  CONSTRAINT `CP_PERSON_DP_LOT_ID_SUBST_FK` FOREIGN KEY (`ID_SUBSTITUINDO_LOTACAO`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `CP_PERSON_DP_PESS_ID_PESS_FK` FOREIGN KEY (`ID_PESSOA`) REFERENCES `dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `CP_PERSON_DP_PESS_ID_SUBST_FK` FOREIGN KEY (`ID_SUBSTITUINDO_PESSOA`) REFERENCES `dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_personalizacao`
--

LOCK TABLES `cp_personalizacao` WRITE;
/*!40000 ALTER TABLE `cp_personalizacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_personalizacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_sede`
--

DROP TABLE IF EXISTS `cp_sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_sede` (
  `ID_SEDE` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_SEDE` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DSC_SEDE` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID_SEDE`),
  KEY `ID_ORGAO_USU` (`ID_ORGAO_USU`),
  CONSTRAINT `cp_sede_ibfk_1` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_sede`
--

LOCK TABLES `cp_sede` WRITE;
/*!40000 ALTER TABLE `cp_sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `cp_sede` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_servico`
--

DROP TABLE IF EXISTS `cp_servico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_servico` (
  `ID_SERVICO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `SIGLA_SERVICO` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESC_SERVICO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ID_SERVICO_PAI` INT UNSIGNED DEFAULT NULL,
  `ID_TP_SERVICO` INT UNSIGNED NOT NULL,
  `LABEL_SERVICO` varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_SERVICO`),
  UNIQUE KEY `CP_SERVICO_ID_SERVICO_PK` (`ID_SERVICO`),
  UNIQUE KEY `SIGLA_SERVICO_IDX` (`SIGLA_SERVICO`),
  KEY `CP_SERV_CPTPSERV_ID_TPSERV_FK` (`ID_TP_SERVICO`),
  CONSTRAINT `CP_SERV_CPTPSERV_ID_TPSERV_FK` FOREIGN KEY (`ID_TP_SERVICO`) REFERENCES `cp_tipo_servico` (`ID_TP_SERVICO`)
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_servico`
--

LOCK TABLES `cp_servico` WRITE;
/*!40000 ALTER TABLE `cp_servico` DISABLE KEYS */;
INSERT INTO `cp_servico` VALUES 
(1,'SIGA','Sistema Integrado de Gestão Administrativa',NULL,2,NULL),
(2,'SIGA-DOC','Módulo de Documentos',1,2,NULL),
(3,'SIGA-WF','Módulo de Workflow',1,2,NULL),
(4,'SIGA-SR','Módulo de Serviços',1,2,NULL),
(5,'FS','Acesso aos Diretórios do Sistema de Arquivos',NULL,1,NULL),
(9,'FS-PUB','Acesso ao Diretório Público',5,1,'(K) PÚBLICA'),
(10,'FS-RAIZ','Acesso ao Diretório Raiz da Unidade',5,1,'(K) RAIZ'),
(15,'SIGA-GI','Módulo de Gestão de Identidade',1,2,NULL),
(16,'SIGA-WF-INI','Iniciar',3,2,NULL),
(17,'SIGA-GI-PERMISSAO','Gerenciar permissões',15,2,NULL),
(18,'SIGA-GI-ID','Gerenciar identidades',15,2,NULL),
(19,'SIGA-GI-PERFIL','Gerenciar perfis de acesso',15,2,NULL),
(20,'SIGA-GI-PERFILJEE','Gerenciar perfis do JEE',15,2,NULL),
(21,'SIGA-GI-GDISTR','Gerenciar grupos de distribuição',15,2,NULL),
(22,'SIGA-GI-SELFSERVICE','Gerenciar serviços da própria lotação',15,2,NULL),
(23,'SIGA-GI-REL','Gerar relatórios',15,2,NULL),
(24,'SIGA-WF-FE','Ferramentas',3,2,NULL),
(41,'SIGA-WF-REL','Relatórios',3,2,NULL),
(42,'SIGA-WF-FE-DEFP','Cadastrar Diagramas',24,2,NULL),
(43,'SIGA-WF-FE-DEFR','Cadastrar Responsáveis',24,2,NULL),
(62,'SIGA-FE','Ferramentas',1,2,NULL),
(63,'SIGA-FE-MODVER','Visualizar modelos',62,2,NULL),
(64,'SIGA-FE-MODEDITAR','Editar modelos',62,2,NULL),
(65,'SIGA-DOC-MOD','Gerenciar modelos',2,2,NULL),
(284,'SIGA-DOC-REL','Gerar relatórios',2,2,NULL),
(304,'SIGA-DOC-REL-FORMS','Relação de formulários',284,2,NULL),
(305,'SIGA-DOC-REL-DATAS','Relação de documentos entre datas',284,2,NULL),
(306,'SIGA-DOC-REL-SUBORD','Relatório de documentos em setores subordinados',284,2,NULL),
(307,'SIGA-DOC-FE','Ferramentas',2,2,NULL),
(308,'SIGA-DOC-FE-CFG','Configurações',307,2,NULL),
(309,'SIGA-GC-EDTCLASS','Editar Classificação',NULL,2,NULL),
(310,'SIGA-SR-ADM','Administrar',4,2,NULL),
(311,'SIGA-SR-EDTCONH','Criar Conhecimentos',4,2,NULL),
(312,'SIGA-SR-EMAILATEND','Receber Notificação Atendente',4,2,NULL),
(313,'SIGA-SR-OPENSAVE','Salvar Solicitação Ao Abrir',4,2,NULL),
(314,'SIGA-SR-OPENPRIOR','Priorizar ao Abrir',4,2,NULL),
(315,'SIGA-SR-VER_GESTOR_I','Ver gestor do item de configuração ao abrir solicitação',4,2,NULL),
(400,'SIGA-GI-CAD_ORGAO_USUARIO','Cadastrar Orgãos Usuário',15,2,NULL),
(401,'SIGA-GI-CAD_CARGO','Cadastrar Cargo',15,2,NULL),
(402,'SIGA-GI-CAD_LOTACAO','Cadastrar Lotação',15,2,NULL),
(403,'SIGA-GI-CAD_FUNCAO','Cadastrar Função de Confiança',15,2,NULL),
(404,'SIGA-GI-CAD_PESSOA','Cadastrar Pessoa',15,2,NULL);
/*!40000 ALTER TABLE `cp_servico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_situacao_configuracao`
--

DROP TABLE IF EXISTS `cp_situacao_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_situacao_configuracao` (
  `ID_SIT_CONFIGURACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DSC_SIT_CONFIGURACAO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `RESTRITIVIDADE_SIT_CONF` double NOT NULL,
  PRIMARY KEY (`ID_SIT_CONFIGURACAO`),
  UNIQUE KEY `CP_SIT_CONF_ID_SIT_CONF_PK` (`ID_SIT_CONFIGURACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_situacao_configuracao`
--

LOCK TABLES `cp_situacao_configuracao` WRITE;
/*!40000 ALTER TABLE `cp_situacao_configuracao` DISABLE KEYS */;
INSERT INTO `cp_situacao_configuracao` VALUES 
(1,'Pode',0),
(2,'Não Pode',9),
(3,'Obrigatório',7),
(4,'Opcional',4),
(5,'Default',5),
(6,'Não default',6),
(7,'Proibido',10),
(8,'Só Leitura',8),
(9,'Ignorar configuração anterior',1);
/*!40000 ALTER TABLE `cp_situacao_configuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_configuracao`
--

DROP TABLE IF EXISTS `cp_tipo_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_configuracao` (
  `ID_TP_CONFIGURACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DSC_TP_CONFIGURACAO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_SIT_CONFIGURACAO` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_TP_CONFIGURACAO`),
  UNIQUE KEY `CP_TP_CONF_ID_TP_CONF_PK` (`ID_TP_CONFIGURACAO`),
  KEY `CP_TP_CONF_CP_SIT_CONF_ID_FK` (`ID_SIT_CONFIGURACAO`),
  CONSTRAINT `CP_TP_CONF_CP_SIT_CONF_ID_FK` FOREIGN KEY (`ID_SIT_CONFIGURACAO`) REFERENCES `cp_situacao_configuracao` (`ID_SIT_CONFIGURACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_configuracao`
--

LOCK TABLES `cp_tipo_configuracao` WRITE;
/*!40000 ALTER TABLE `cp_tipo_configuracao` DISABLE KEYS */;
INSERT INTO `cp_tipo_configuracao` VALUES 
(1,'Movimentar',1),
(2,'Criar',1),
(3,'Finalizar',1),
(4,'Eletrônico',7),
(5,'Nivel de Acesso',4),
(6,'Acessar',2),
(7,'Diretor do Foro',NULL),
(8,'Refazer',1),
(9,'Duplicar',1),
(10,'Editar',1),
(11,'Excluir',1),
(12,'Excluir Anexo',1),
(13,'Configurar',2),
(14,'Excluir Anotação',1),
(15,'Visualizar Impressão',1),
(16,'Cancelar Via',1),
(17,'Criar Via',1),
(18,'Nível de Acesso Máximo',4),
(19,'Nível de Acesso Mínimo',4),
(20,'Cadastrar Qualquer Subst',2),
(21,'Definir Publicadores',2),
(22,'Atender Pedido de Publicação',2),
(23,'Pode receber documento sem assinatura',2),
(24,'Pode criar documento filho',1),
(25,'Gerenciar Publicação Boletim',2),
(26,'Simular Usuário',2),
(27,'Notificar Por E-mail',1),
(28,'Corrigir Erro',2),
(29,'Cancelar Movimentação',2),
(30,'Incluir como Filho',2),
(31,'Destinatário',4),
(32,'Utilizar Extensão de Editor',2),
(33,'Utilizar Extensão de Conversor HTML',2),
(34,'Reiniciar Numeração Todo Ano',1),
(35,'Autuável',2),
(36,'Editar Data',2),
(37,'Editar Descrição',1),
(38,'Trâmite Automático',5),
(39,'Pode Assinar sem Solicitação',1),
(40,'Definição Automática de Perfil',1),
(41,'Incluir Documento',1),
(42,'Criar Como Novo',1),
(43,'Juntada Automática',1),
(100,'Instanciar Procedimento',2),
(101,'Designar Tarefa',2),
(200,'Utilizar Serviço',2),
(201,'Pode habilitar serviço',2),
(202,'Pode habilitar serviço de diretório',2),
(203,'Pertencer',1),
(204,'Fazer Login',1),
(205,'Utilizar Serviço de Outra Lotação',2),
(206,'Gerenciar Grupo',2),
(300,'Designação',NULL),
(301,'Associação',NULL),
(302,'Usar Lista',2),
(303,'Inclusão automática em lista',1),
(304,'Abrangência de Acordo',1),
(305,'Associação de Configuração com Pesquisa',1),
(306,'Escalonar Por Solicitacao Filha',NULL),
(307,'Definição do horário da equipe',NULL);
/*!40000 ALTER TABLE `cp_tipo_configuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_grupo`
--

DROP TABLE IF EXISTS `cp_tipo_grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_grupo` (
  `ID_TP_GRUPO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESC_TP_GRUPO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_GRUPO`),
  UNIQUE KEY `CP_TIPO_GRUPO_PK` (`ID_TP_GRUPO`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_grupo`
--

LOCK TABLES `cp_tipo_grupo` WRITE;
/*!40000 ALTER TABLE `cp_tipo_grupo` DISABLE KEYS */;
INSERT INTO `cp_tipo_grupo` VALUES (1,'Perfil de Acesso'),
(2,'Grupo de Distribuição'),
(3,'Perfil de Acesso do JEE');
/*!40000 ALTER TABLE `cp_tipo_grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_identidade`
--

DROP TABLE IF EXISTS `cp_tipo_identidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_identidade` (
  `ID_TP_IDENTIDADE` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESC_TP_IDENTIDADE` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_IDENTIDADE`),
  UNIQUE KEY `CP_TIPO_IDENTIDADE_PK` (`ID_TP_IDENTIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_identidade`
--

LOCK TABLES `cp_tipo_identidade` WRITE;
/*!40000 ALTER TABLE `cp_tipo_identidade` DISABLE KEYS */;
INSERT INTO `cp_tipo_identidade` VALUES (1,'Login e Senha'),
(2,'Certidão Digital ICP-Brasil');
/*!40000 ALTER TABLE `cp_tipo_identidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_lotacao`
--

DROP TABLE IF EXISTS `cp_tipo_lotacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_lotacao` (
  `ID_TP_LOTACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `SIGLA_TP_LOTACAO` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DESC_TP_LOTACAO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_TP_LOTACAO_PAI` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_TP_LOTACAO`),
  UNIQUE KEY `CP_TIPO_LOTACAO_PK` (`ID_TP_LOTACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_lotacao`
--

LOCK TABLES `cp_tipo_lotacao` WRITE;
/*!40000 ALTER TABLE `cp_tipo_lotacao` DISABLE KEYS */;
INSERT INTO `cp_tipo_lotacao` VALUES (1,'ADM','Unidade da Administração',NULL),
(100,'JUD','Unidade Judicial',NULL),
(101,'VF','Vara Federal',100),
(102,'VFCR','Vara Federal Criminal',100),
(200,'AGR','Agrupamento',NULL),
(201,'AGFM','Agrupamento Formal',200),
(202,'AGOP','Agrupamento Operacional',200),
(203,'AGFC','Agrupamento Funcional',200);
/*!40000 ALTER TABLE `cp_tipo_lotacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_marca`
--

DROP TABLE IF EXISTS `cp_tipo_marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_marca` (
  `ID_TP_MARCA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESCR_TP_MARCA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_MARCA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_marca`
--

LOCK TABLES `cp_tipo_marca` WRITE;
/*!40000 ALTER TABLE `cp_tipo_marca` DISABLE KEYS */;
INSERT INTO `cp_tipo_marca` VALUES (1,'SIGA-EX'),
(2,'SIGA-SR'),
(3,'SIGA-GC');
/*!40000 ALTER TABLE `cp_tipo_marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_marcador`
--

DROP TABLE IF EXISTS `cp_tipo_marcador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_marcador` (
  `ID_TP_MARCADOR` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESCR_TIPO_MARCADOR` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_MARCADOR`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_marcador`
--

LOCK TABLES `cp_tipo_marcador` WRITE;
/*!40000 ALTER TABLE `cp_tipo_marcador` DISABLE KEYS */;
INSERT INTO `cp_tipo_marcador` VALUES (1,'Sistema'),
(2,'Geral'),
(3,'Lotação e sublotações'),
(4,'Lotação'),
(5,'Pessoa');
/*!40000 ALTER TABLE `cp_tipo_marcador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_papel`
--

DROP TABLE IF EXISTS `cp_tipo_papel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_papel` (
  `ID_TP_PAPEL` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESC_TP_PAPEL` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_PAPEL`),
  UNIQUE KEY `CP_TIPO_PAPEL_PK` (`ID_TP_PAPEL`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_papel`
--

LOCK TABLES `cp_tipo_papel` WRITE;
/*!40000 ALTER TABLE `cp_tipo_papel` DISABLE KEYS */;
INSERT INTO `cp_tipo_papel` VALUES (1,'Principal'),
(2,'Funcional');
/*!40000 ALTER TABLE `cp_tipo_papel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_pessoa`
--

DROP TABLE IF EXISTS `cp_tipo_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_pessoa` (
  `ID_TP_PESSOA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESC_TP_PESSOA` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_PESSOA`),
  UNIQUE KEY `CP_TIPO_PESSOA_PK` (`ID_TP_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_pessoa`
--

LOCK TABLES `cp_tipo_pessoa` WRITE;
/*!40000 ALTER TABLE `cp_tipo_pessoa` DISABLE KEYS */;
INSERT INTO `cp_tipo_pessoa` VALUES (1,'Magistrado'),
(2,'Servidor'),
(3,'Estagiário'),
(4,'Terceirizado');
/*!40000 ALTER TABLE `cp_tipo_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_servico`
--

DROP TABLE IF EXISTS `cp_tipo_servico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_servico` (
  `ID_TP_SERVICO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESC_TP_SERVICO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_SIT_CONFIGURACAO` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_TP_SERVICO`),
  UNIQUE KEY `CP_TIPO_SERVICO_PK` (`ID_TP_SERVICO`),
  KEY `CP_TP_SERV_ID_SIT_CONF_FK` (`ID_SIT_CONFIGURACAO`),
  CONSTRAINT `CP_TP_SERV_ID_SIT_CONF_FK` FOREIGN KEY (`ID_SIT_CONFIGURACAO`) REFERENCES `cp_tipo_configuracao` (`ID_TP_CONFIGURACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_servico`
--

LOCK TABLES `cp_tipo_servico` WRITE;
/*!40000 ALTER TABLE `cp_tipo_servico` DISABLE KEYS */;
INSERT INTO `cp_tipo_servico` VALUES (1,'Diretório',2),
(2,'Sistema',2);
/*!40000 ALTER TABLE `cp_tipo_servico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_tipo_servico_situacao`
--

DROP TABLE IF EXISTS `cp_tipo_servico_situacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_tipo_servico_situacao` (
  `ID_TP_SERVICO` INT UNSIGNED NOT NULL DEFAULT '0',
  `ID_SIT_CONFIGURACAO` INT UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_TP_SERVICO`,`ID_SIT_CONFIGURACAO`),
  KEY `CP_TPSERVSIT_ID_SITCONF_FK` (`ID_SIT_CONFIGURACAO`),
  CONSTRAINT `CP_TPSERVSIT_ID_SITCONF_FK` FOREIGN KEY (`ID_SIT_CONFIGURACAO`) REFERENCES `cp_situacao_configuracao` (`ID_SIT_CONFIGURACAO`),
  CONSTRAINT `CP_TPSRVSIT_IDTPSRV_FK` FOREIGN KEY (`ID_TP_SERVICO`) REFERENCES `cp_tipo_servico` (`ID_TP_SERVICO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_tipo_servico_situacao`
--

LOCK TABLES `cp_tipo_servico_situacao` WRITE;
/*!40000 ALTER TABLE `cp_tipo_servico_situacao` DISABLE KEYS */;
INSERT INTO `cp_tipo_servico_situacao` VALUES (1,1),
(2,1),
(1,2),
(2,2);
/*!40000 ALTER TABLE `cp_tipo_servico_situacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_uf`
--

DROP TABLE IF EXISTS `cp_uf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_uf` (
  `ID_UF` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_UF` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID_UF`),
  UNIQUE KEY `UF_PK` (`ID_UF`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_uf`
--

LOCK TABLES `cp_uf` WRITE;
/*!40000 ALTER TABLE `cp_uf` DISABLE KEYS */;
INSERT INTO `cp_uf` VALUES (1,'AC'),
(2,'AL'),
(3,'AM'),
(4,'AP'),
(5,'BA'),
(6,'CE'),
(7,'DF'),
(8,'ES'),
(9,'GO'),
(10,'MA'),
(11,'MG'),
(12,'MS'),
(13,'MT'),
(14,'PA'),
(15,'PB'),
(16,'PE'),
(17,'PI'),
(18,'PR'),
(19,'RJ'),
(20,'RN'),
(21,'RO'),
(22,'RR'),
(23,'RS'),
(24,'SC'),
(25,'SE'),
(26,'SP'),
(27,'TO');
/*!40000 ALTER TABLE `cp_uf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cp_unidade_medida`
--

DROP TABLE IF EXISTS `cp_unidade_medida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `cp_unidade_medida` (
  `ID_UNIDADE_MEDIDA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DESCR_UNIDADE_MEDIDA` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_UNIDADE_MEDIDA`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_unidade_medida`
--

LOCK TABLES `cp_unidade_medida` WRITE;
/*!40000 ALTER TABLE `cp_unidade_medida` DISABLE KEYS */;
INSERT INTO `cp_unidade_medida` VALUES (1,'Ano'),
(2,'Mes'),
(3,'Dia'),
(4,'Hora'),
(5,'Minuto'),
(6,'Segundo');
/*!40000 ALTER TABLE `cp_unidade_medida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dp_cargo`
--

DROP TABLE IF EXISTS `dp_cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_cargo` (
  `ID_CARGO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NOME_CARGO` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `DT_FIM_CARGO` datetime DEFAULT NULL,
  `DT_INI_CARGO` datetime DEFAULT NULL,
  `ID_CARGO_INICIAL` INT UNSIGNED DEFAULT NULL,
  `IDE_CARGO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SIGLA_CARGO` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_CARGO`),
  KEY `DP_CARGO_IDX_012` (`ID_ORGAO_USU`),
  KEY `DP_CARGO_IDX_011` (`ID_CARGO_INICIAL`),
  CONSTRAINT `CARGO_INICIAL_CARGO_FK` FOREIGN KEY (`ID_CARGO_INICIAL`) REFERENCES `dp_cargo` (`ID_CARGO`),
  CONSTRAINT `CARGO_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_cargo`
--

LOCK TABLES `dp_cargo` WRITE;
/*!40000 ALTER TABLE `dp_cargo` DISABLE KEYS */;
INSERT INTO `dp_cargo` VALUES (1,'FUNCIONÁRIO',999999999,NULL,'2011-08-03 00:00:00',1,'203','500300');
/*!40000 ALTER TABLE `dp_cargo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `DP_CARGO_INSERT_TRG` BEFORE INSERT ON `dp_cargo` FOR EACH ROW BEGIN
    if NEW.id_cargo_inicial is null then
        SET NEW.ID_cargo = NEW.id_cargo_inicial;
    end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `dp_estado_civil`
--

DROP TABLE IF EXISTS `dp_estado_civil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_estado_civil` (
  `ID_ESTADO_CIVIL` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NM_ESTADO_CIVIL` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID_ESTADO_CIVIL`),
  UNIQUE KEY `ESTADO_CIVIL_PK` (`ID_ESTADO_CIVIL`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_estado_civil`
--

LOCK TABLES `dp_estado_civil` WRITE;
/*!40000 ALTER TABLE `dp_estado_civil` DISABLE KEYS */;
INSERT INTO `dp_estado_civil` VALUES (1,'SOLTEIRO'),
(2,'CASADO'),
(3,'SEPARADO'),
(4,'DIVORCIADO'),
(5,'VIUVO'),
(6,'UNIAO ESTAVEL'),
(7,'NÃO DECLARADO');
/*!40000 ALTER TABLE `dp_estado_civil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dp_funcao_confianca`
--

DROP TABLE IF EXISTS `dp_funcao_confianca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_funcao_confianca` (
  `ID_FUNCAO_CONFIANCA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `NOME_FUNCAO_CONFIANCA` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `NIVEL_FUNCAO_CONFIANCA` tinyint(4) DEFAULT NULL,
  `COD_FOLHA_FUNCAO_CONFIANCA` tinyint(4) DEFAULT NULL,
  `DT_INI_FUNCAO_CONFIANCA` datetime DEFAULT NULL,
  `DT_FIM_FUNCAO_CONFIANCA` datetime DEFAULT NULL,
  `ID_FUNCAO_CONFIANCA_PAI` INT UNSIGNED DEFAULT NULL,
  `CATEGORIA_FUNCAO_CONFIANCA` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `IDE_FUNCAO_CONFIANCA` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_FUN_CONF_INI` INT UNSIGNED DEFAULT NULL,
  `SIGLA_FUNCAO_CONFIANCA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_FUNCAO_CONFIANCA`),
  KEY `DP_FUNCAO_CONFIANCA_IDX_010` (`ID_FUNCAO_CONFIANCA_PAI`),
  KEY `DP_FUNCAO_CONFIANCA_IDX_009` (`ID_ORGAO_USU`),
  KEY `DP_FUNCAO_CONFIANCA_IDX_008` (`ID_FUN_CONF_INI`),
  CONSTRAINT `FUN_CONF_INI_FUN_CONF_PK` FOREIGN KEY (`ID_FUN_CONF_INI`) REFERENCES `dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`),
  CONSTRAINT `FUN_CONF_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `dp_funcao_confianca_ibfk_1` FOREIGN KEY (`ID_FUNCAO_CONFIANCA_PAI`) REFERENCES `dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_funcao_confianca`
--

LOCK TABLES `dp_funcao_confianca` WRITE;
/*!40000 ALTER TABLE `dp_funcao_confianca` DISABLE KEYS */;
/*!40000 ALTER TABLE `dp_funcao_confianca` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `DP_FUNC_CONF_INSERT_TRG` BEFORE INSERT ON `dp_funcao_confianca` FOR EACH ROW begin
if NEW.id_fun_conf_ini is null then
    SET NEW.ID_funcao_confianca = NEW.id_fun_conf_ini;
end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `dp_lotacao`
--

DROP TABLE IF EXISTS `dp_lotacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_lotacao` (
  `ID_LOTACAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DATA_INI_LOT` datetime NOT NULL,
  `DATA_FIM_LOT` datetime DEFAULT NULL,
  `NOME_LOTACAO` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ID_LOTACAO_PAI` INT UNSIGNED DEFAULT NULL,
  `SIGLA_LOTACAO` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `IDE_LOTACAO` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_LOTACAO_INI` INT UNSIGNED DEFAULT NULL,
  `ID_TP_LOTACAO` INT UNSIGNED DEFAULT NULL,
  `ID_LOCALIDADE` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_LOTACAO`),
  UNIQUE KEY `SIGLA_LOTACAO_DP_LOTACAO_UK` (`SIGLA_LOTACAO`,`ID_ORGAO_USU`,`DATA_FIM_LOT`),
  KEY `DP_LOTACAO_IDX_007` (`ID_LOTACAO_PAI`),
  KEY `LOT_ID_INI` (`ID_LOTACAO_INI`,`DATA_FIM_LOT`),
  KEY `DP_LOTACAO_IDX_006` (`ID_ORGAO_USU`),
  KEY `DP_LOT_CP_TP_LOT_ID_TP_LOT_FK` (`ID_TP_LOTACAO`),
  KEY `ID_LOCALIDADE` (`ID_LOCALIDADE`),
  CONSTRAINT `DP_LOT_CP_TP_LOT_ID_TP_LOT_FK` FOREIGN KEY (`ID_TP_LOTACAO`) REFERENCES `cp_tipo_lotacao` (`ID_TP_LOTACAO`),
  CONSTRAINT `LOTACAO_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `dp_lotacao_ibfk_1` FOREIGN KEY (`ID_LOTACAO_PAI`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `dp_lotacao_ibfk_2` FOREIGN KEY (`ID_LOCALIDADE`) REFERENCES `cp_localidade` (`ID_LOCALIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_lotacao`
--

LOCK TABLES `dp_lotacao` WRITE;
/*!40000 ALTER TABLE `dp_lotacao` DISABLE KEYS */;
INSERT INTO `dp_lotacao` VALUES (1,'2011-08-08 00:00:00',NULL,'LOTACAO TESTE',NULL,'LTEST',999999999,'1076',1,100,NULL);
INSERT INTO `dp_lotacao` VALUES (2,'2011-08-08 00:00:00',NULL,'LOTACAO TESTE 2',NULL,'LTEST2',999999999,'1077',2,100,NULL);
INSERT INTO `dp_lotacao` VALUES (3,'2011-08-08 00:00:00',NULL,'LOTACAO TESTE 3',NULL,'LTEST3',999999999,'1078',3,100,NULL);
/*!40000 ALTER TABLE `dp_lotacao` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `DP_LOTACAO_INSERT_TRG` BEFORE INSERT ON `dp_lotacao` FOR EACH ROW begin
if NEW.id_lotacao_ini is null then
    SET NEW.ID_lotacao = NEW.id_lotacao_ini;
end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `dp_padrao_referencia`
--

DROP TABLE IF EXISTS `dp_padrao_referencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_padrao_referencia` (
  `ID_PADRAO_REFERENCIA` tinyint(4) NOT NULL AUTO_INCREMENT,
  `ID_PADRAO_REFERENCIA_PAI` tinyint(4) DEFAULT NULL,
  `DSC_PADRAO` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DSC_CLASSE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DSC_NIVEL` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `PADRAO_REFERENCIA_DT_FIM` datetime DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID_PADRAO_REFERENCIA`),
  UNIQUE KEY `PADRAO_REFERENCIA_PK` (`ID_PADRAO_REFERENCIA`),
  KEY `PADRAO_REF_ORGAO_USU_FK` (`ID_ORGAO_USU`),
  CONSTRAINT `PADRAO_REF_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_padrao_referencia`
--

LOCK TABLES `dp_padrao_referencia` WRITE;
/*!40000 ALTER TABLE `dp_padrao_referencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `dp_padrao_referencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dp_pessoa`
--

DROP TABLE IF EXISTS `dp_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_pessoa` (
  `ID_PESSOA` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DATA_INI_PESSOA` datetime NOT NULL,
  `DATA_FIM_PESSOA` datetime DEFAULT NULL,
  `CPF_PESSOA` bigint(20) NOT NULL,
  `NOME_PESSOA` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DATA_NASC_PESSOA` datetime DEFAULT NULL,
  `MATRICULA` INT UNSIGNED NOT NULL,
  `ID_LOTACAO` INT UNSIGNED NOT NULL,
  `ID_CARGO` INT UNSIGNED DEFAULT NULL,
  `ID_FUNCAO_CONFIANCA` INT UNSIGNED DEFAULT NULL,
  `SESB_PESSOA` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `EMAIL_PESSOA` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TP_SERVIDOR_PESSOA` tinyint(4) DEFAULT NULL,
  `SIGLA_PESSOA` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SEXO_PESSOA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `GRAU_INSTRUCAO_PESSOA` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TP_SANGUINEO_PESSOA` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NACIONALIDADE_PESSOA` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DATA_POSSE_PESSOA` datetime DEFAULT NULL,
  `DATA_NOMEACAO_PESSOA` datetime DEFAULT NULL,
  `DATA_PUBLICACAO_PESSOA` datetime DEFAULT NULL,
  `DATA_INICIO_EXERCICIO_PESSOA` datetime DEFAULT NULL,
  `ATO_NOMEACAO_PESSOA` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SITUACAO_FUNCIONAL_PESSOA` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_PROVIMENTO` tinyint(4) DEFAULT NULL,
  `NATURALIDADE_PESSOA` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `FG_IMPRIME_END` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DSC_PADRAO_REFERENCIA_PESSOA` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ORGAO_USU` INT UNSIGNED NOT NULL,
  `IDE_PESSOA` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_PESSOA_INICIAL` INT UNSIGNED DEFAULT NULL,
  `ENDERECO_PESSOA` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `BAIRRO_PESSOA` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CIDADE_PESSOA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CEP_PESSOA` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TELEFONE_PESSOA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `RG_PESSOA` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `RG_ORGAO_PESSOA` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `RG_DATA_EXPEDICAO_PESSOA` datetime DEFAULT NULL,
  `RG_UF_PESSOA` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ID_ESTADO_CIVIL` INT UNSIGNED DEFAULT NULL,
  `ID_TP_PESSOA` INT UNSIGNED DEFAULT NULL,
  `NOME_EXIBICAO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID_PESSOA`),
  KEY `PES_ID_INI` (`ID_PESSOA_INICIAL`,`DATA_FIM_PESSOA`),
  KEY `DP_PESSOA_IDX_003` (`ID_FUNCAO_CONFIANCA`),
  KEY `DP_PESSOA_IDX_001` (`ID_ORGAO_USU`),
  KEY `DP_PESSOA_NM_PESSOA_IX` (`NOME_PESSOA`),
  KEY `DP_PESSOA_IDX_002` (`ID_CARGO`,`ID_LOTACAO`,`ID_FUNCAO_CONFIANCA`,`ID_ORGAO_USU`),
  KEY `DP_PESSOA_IDX_015` (`DATA_FIM_PESSOA`,`NOME_PESSOA`),
  KEY `DP_PESSOA_IDX_005` (`ID_PROVIMENTO`),
  KEY `DP_PESSOA_IDX_014` (`MATRICULA`,`SESB_PESSOA`,`DATA_FIM_PESSOA`),
  KEY `DP_PESSOA_IDX_004` (`ID_LOTACAO`),
  KEY `DP_PESS_CPTPPESS_ID_TPPESS_FK` (`ID_TP_PESSOA`),
  KEY `PESSOA_ESTADO_CIVIL_FK` (`ID_ESTADO_CIVIL`),
  CONSTRAINT `DP_PESS_CPTPPESS_ID_TPPESS_FK` FOREIGN KEY (`ID_TP_PESSOA`) REFERENCES `cp_tipo_pessoa` (`ID_TP_PESSOA`),
  CONSTRAINT `PESSOA_ESTADO_CIVIL_FK` FOREIGN KEY (`ID_ESTADO_CIVIL`) REFERENCES `dp_estado_civil` (`ID_ESTADO_CIVIL`),
  CONSTRAINT `PESSOA_INICIAL_PESSOAL_FK` FOREIGN KEY (`ID_PESSOA_INICIAL`) REFERENCES `dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `PESSOA_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `dp_pessoa_ibfk_1` FOREIGN KEY (`ID_CARGO`) REFERENCES `dp_cargo` (`ID_CARGO`),
  CONSTRAINT `dp_pessoa_ibfk_2` FOREIGN KEY (`ID_FUNCAO_CONFIANCA`) REFERENCES `dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`),
  CONSTRAINT `dp_pessoa_ibfk_3` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `dp_pessoa_ibfk_4` FOREIGN KEY (`ID_PROVIMENTO`) REFERENCES `dp_provimento` (`ID_PROVIMENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_pessoa`
--

LOCK TABLES `dp_pessoa` WRITE;
/*!40000 ALTER TABLE `dp_pessoa` DISABLE KEYS */;
INSERT INTO `dp_pessoa` VALUES (1,'2001-01-01 00:00:00',NULL,11111111111,'USUARIO TESTE','2001-01-01 00:00:00',99999,1,1,NULL,'ZZ','usuarioteste@jfrj.jus.br',NULL,'TST','M','ESPEC','A+','BRASILEIRO','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','0000/2001','1',NULL,NULL,NULL,'PAD-REF',999999999,'99999',1,'AV ALMTE BARROSO 78','CENTRO','RIO DE JANEIRO','20000000','9700','987654321','TESTE','2001-01-01 00:00:00','RJ',2,2,NULL);
INSERT INTO `dp_pessoa` VALUES (2,'2001-01-01 00:00:00',NULL,22222222222,'USUARIO TESTE 2','2001-01-01 00:00:00',99998,2,1,NULL,'ZZ','usuarioteste2@jfrj.jus.br',NULL,'TST','M','ESPEC','A+','BRASILEIRO','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','0000/2001','1',NULL,NULL,NULL,'PAD-REF',999999999,'99998',2,'AV ALMTE BARROSO 78','CENTRO','RIO DE JANEIRO','20000000','9702','987654321','TESTE','2001-01-01 00:00:00','RJ',2,2,NULL);
INSERT INTO `dp_pessoa` VALUES (3,'2001-01-01 00:00:00',NULL,33333333333,'USUARIO TESTE 3','2001-01-01 00:00:00',99997,3,1,NULL,'ZZ','usuarioteste3@jfrj.jus.br',NULL,'TST','M','ESPEC','A+','BRASILEIRO','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','2001-01-01 00:00:00','0000/2001','1',NULL,NULL,NULL,'PAD-REF',999999999,'99997',3,'AV ALMTE BARROSO 78','CENTRO','RIO DE JANEIRO','20000000','9703','987654321','TESTE','2001-01-01 00:00:00','RJ',2,2,NULL);
/*!40000 ALTER TABLE `dp_pessoa` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `DP_PESSOA_INSERT_TRG` BEFORE INSERT ON `dp_pessoa` FOR EACH ROW begin
if NEW.id_pessoa_inicial is null then
    SET NEW.ID_pessoa = NEW.id_pessoa_inicial;
end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `dp_provimento`
--

DROP TABLE IF EXISTS `dp_provimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_provimento` (
  `ID_PROVIMENTO` tinyint(4) NOT NULL AUTO_INCREMENT,
  `DSC_PROVIMENTO` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID_PROVIMENTO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_provimento`
--

LOCK TABLES `dp_provimento` WRITE;
/*!40000 ALTER TABLE `dp_provimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `dp_provimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dp_substituicao`
--

DROP TABLE IF EXISTS `dp_substituicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `dp_substituicao` (
  `ID_SUBSTITUICAO` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_TITULAR` INT UNSIGNED DEFAULT NULL,
  `ID_LOTA_TITULAR` INT UNSIGNED NOT NULL,
  `ID_SUBSTITUTO` INT UNSIGNED DEFAULT NULL,
  `ID_LOTA_SUBSTITUTO` INT UNSIGNED NOT NULL,
  `DT_INI_SUBST` datetime NOT NULL,
  `DT_FIM_SUBST` datetime DEFAULT NULL,
  `DT_INI_REG` datetime DEFAULT NULL,
  `DT_FIM_REG` datetime DEFAULT NULL,
  `ID_REG_INI` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID_SUBSTITUICAO`),
  KEY `SUBSTITUICAO_PESOA_SUBST_FK` (`ID_SUBSTITUTO`),
  KEY `SUBSTITUICAO_PESOA_TITULAR_FK` (`ID_TITULAR`),
  CONSTRAINT `SUBSTITUICAO_PESOA_SUBST_FK` FOREIGN KEY (`ID_SUBSTITUTO`) REFERENCES `dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `SUBSTITUICAO_PESOA_TITULAR_FK` FOREIGN KEY (`ID_TITULAR`) REFERENCES `dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dp_substituicao`
--

LOCK TABLES `dp_substituicao` WRITE;
/*!40000 ALTER TABLE `dp_substituicao` DISABLE KEYS */;
/*!40000 ALTER TABLE `dp_substituicao` ENABLE KEYS */;
UNLOCK TABLES;

drop function if exists remove_acento;
delimiter //
create function remove_acento( textvalue varchar(16383) )
returns varchar(16383) DETERMINISTIC
begin

set @textvalue = textvalue;

-- ACCENTS
set @withaccents = 'ŠšŽžÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÑÒÓÔÕÖØÙÚÛÜÝŸÞàáâãäåæçèéêëìíîïñòóôõöøùúûüýÿþƒ';
set @withoutaccents = 'SsZzAAAAAAACEEEEIIIINOOOOOOUUUUYYBaaaaaaaceeeeiiiinoooooouuuuyybf';
set @count = length(@withaccents);

while @count > 0 do
    set @textvalue = replace(@textvalue, substring(@withaccents, @count, 1), substring(@withoutaccents, @count, 1));
    set @count = @count - 1;
end while;

-- SPECIAL CHARS
set @special = '!@#$%¨&*()_+=§¹²³£¢¬"`´{[^~}]<,>.:;?/°ºª+*|\\''';
set @count = length(@special);
while @count > 0 do
    set @textvalue = replace(@textvalue, substring(@special, @count, 1), '');
    set @count = @count - 1;
end while;

return @textvalue;

end;//
DELIMITER ;


--
-- Dumping routines for database 'corporativo'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-12 13:03:52
