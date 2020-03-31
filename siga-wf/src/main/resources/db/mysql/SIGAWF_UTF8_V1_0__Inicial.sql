CREATE DATABASE  IF NOT EXISTS `sigawf` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sigawf`;
-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: sigawf
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
-- Table structure for table `wf_def_desvio`
--

DROP TABLE IF EXISTS `wf_def_desvio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_def_desvio` (
  `DEFD_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `DEFD_TX_CONDICAO` varchar(256) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `DEFD_NM` varchar(256) DEFAULT NULL,
  `DEFD_NR_ORDEM` int(11) DEFAULT NULL,
  `DEFD_FG_ULTIMO` bit(1) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `DEFT_ID` bigint(20) DEFAULT NULL,
  `DEFT_ID_SEGUINTE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DEFD_ID`),
  KEY `FK813vkemf782rmlocu7vvc7dqr` (`DEFT_ID`),
  KEY `FKra1qiio7tqe6pi8oi26ohpr6o` (`DEFT_ID_SEGUINTE`),
  CONSTRAINT `FK813vkemf782rmlocu7vvc7dqr` FOREIGN KEY (`DEFT_ID`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FKra1qiio7tqe6pi8oi26ohpr6o` FOREIGN KEY (`DEFT_ID_SEGUINTE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_def_procedimento`
--

DROP TABLE IF EXISTS `wf_def_procedimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_def_procedimento` (
  `DEFP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `DEFP_ANO` int(11) DEFAULT NULL,
  `DEFP_DS` varchar(256) NOT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `DEFP_NM` varchar(256) NOT NULL,
  `DEFP_NR` int(11) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `ORGU_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DEFP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_def_responsavel`
--

DROP TABLE IF EXISTS `wf_def_responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_def_responsavel` (
  `DEFR_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `DEFR_DS` varchar(256) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `DEFR_NM` varchar(256) DEFAULT NULL,
  `DEFR_TP` varchar(255) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DEFR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_def_tarefa`
--

DROP TABLE IF EXISTS `wf_def_tarefa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_def_tarefa` (
  `DEFT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `DEFT_TX_ASSUNTO` varchar(256) DEFAULT NULL,
  `DEFT_TX_CONTEUDO` varchar(2048) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `DEFT_NM` varchar(256) DEFAULT NULL,
  `DEFT_NR_ORDEM` int(11) DEFAULT NULL,
  `DEFT_TP_RESPONSAVEL` varchar(255) DEFAULT NULL,
  `DEFT_TP_TAREFA` varchar(255) DEFAULT NULL,
  `DEFT_FG_ULTIMO` bit(1) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `DEFP_ID` bigint(20) DEFAULT NULL,
  `DEFR_ID` bigint(20) DEFAULT NULL,
  `LOTA_ID` bigint(20) DEFAULT NULL,
  `PESS_ID` bigint(20) DEFAULT NULL,
  `DEFT_ID_SEGUINTE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DEFT_ID`),
  KEY `FK78imh70w24xwrsxtm1i1l3kgq` (`DEFP_ID`),
  KEY `FKqel8oog2x3uh24xtigwcapkes` (`DEFR_ID`),
  KEY `FK3q8wg6gcbprluvelqdvhr97n5` (`DEFT_ID_SEGUINTE`),
  CONSTRAINT `FK3q8wg6gcbprluvelqdvhr97n5` FOREIGN KEY (`DEFT_ID_SEGUINTE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK78imh70w24xwrsxtm1i1l3kgq` FOREIGN KEY (`DEFP_ID`) REFERENCES `wf_def_procedimento` (`DEFP_ID`),
  CONSTRAINT `FKqel8oog2x3uh24xtigwcapkes` FOREIGN KEY (`DEFR_ID`) REFERENCES `wf_def_responsavel` (`DEFR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_def_variavel`
--

DROP TABLE IF EXISTS `wf_def_variavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_def_variavel` (
  `DEFV_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `DEFV_TP_ACESSO` varchar(255) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `DEFV_CD` varchar(32) DEFAULT NULL,
  `DEFV_NM` varchar(256) DEFAULT NULL,
  `DEFV_NR_ORDEM` int(11) DEFAULT NULL,
  `DEFV_TP` varchar(255) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `DEFT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DEFV_ID`),
  KEY `FK8g9ym68b7nhtn6js08hi238kq` (`DEFT_ID`),
  CONSTRAINT `FK8g9ym68b7nhtn6js08hi238kq` FOREIGN KEY (`DEFT_ID`) REFERENCES `wf_def_tarefa` (`DEFT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_movimentacao`
--

DROP TABLE IF EXISTS `wf_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_movimentacao` (
  `MOVI_TP` varchar(31) NOT NULL,
  `MOVI_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `MOVI_DS` varchar(400) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `MOVI_TP_DESIGNACAO` varchar(255) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `LOTA_ID_TITULAR` bigint(20) DEFAULT NULL,
  `PROC_ID` bigint(20) DEFAULT NULL,
  `PESS_ID_TITULAR` bigint(20) DEFAULT NULL,
  `DEFT_ID_DE` bigint(20) DEFAULT NULL,
  `DEFT_ID_PARA` bigint(20) DEFAULT NULL,
  `LOTA_ID_DE` bigint(20) DEFAULT NULL,
  `LOTA_ID_PARA` bigint(20) DEFAULT NULL,
  `PESS_ID_DE` bigint(20) DEFAULT NULL,
  `PESS_ID_PARA` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`MOVI_ID`),
  KEY `FK8lotmruole2bkagssr0l8k0xy` (`PROC_ID`),
  KEY `FK3wba9weje1vrg6f1k5lmi1135` (`DEFT_ID_DE`),
  KEY `FK71qoltrijfornvk1so8okwepc` (`DEFT_ID_PARA`),
  CONSTRAINT `FK3wba9weje1vrg6f1k5lmi1135` FOREIGN KEY (`DEFT_ID_DE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK71qoltrijfornvk1so8okwepc` FOREIGN KEY (`DEFT_ID_PARA`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK8lotmruole2bkagssr0l8k0xy` FOREIGN KEY (`PROC_ID`) REFERENCES `wf_procedimento` (`PROC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_procedimento`
--

DROP TABLE IF EXISTS `wf_procedimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_procedimento` (
  `PROC_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROC_ANO` int(11) DEFAULT NULL,
  `PROC_TS_EVENTO` datetime(6) DEFAULT NULL,
  `PROC_NM_EVENTO` varchar(255) DEFAULT NULL,
  `PROC_NR_CORRENTE` int(11) DEFAULT NULL,
  `PROC_NR` int(11) DEFAULT NULL,
  `PROC_CD_PRINCIPAL` varchar(255) DEFAULT NULL,
  `PROC_TP_PRIORIDADE` varchar(255) DEFAULT NULL,
  `PROC_ST_CORRENTE` varchar(255) DEFAULT NULL,
  `PROC_TP_PRINCIPAL` varchar(255) DEFAULT NULL,
  `DEFP_ID` bigint(20) DEFAULT NULL,
  `LOTA_ID_EVENTO` bigint(20) DEFAULT NULL,
  `ORGU_ID` bigint(20) DEFAULT NULL,
  `PESS_ID_EVENTO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`PROC_ID`),
  KEY `FK5kmlrwsmko0wlon3x48rfexbe` (`DEFP_ID`),
  CONSTRAINT `FK5kmlrwsmko0wlon3x48rfexbe` FOREIGN KEY (`DEFP_ID`) REFERENCES `wf_def_procedimento` (`DEFP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_responsavel`
--

DROP TABLE IF EXISTS `wf_responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_responsavel` (
  `RESP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HIS_DT_FIM` datetime(6) DEFAULT NULL,
  `HIS_DT_INI` datetime(6) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `HIS_ATIVO` int(11) DEFAULT NULL,
  `HIS_IDC_FIM` bigint(20) DEFAULT NULL,
  `HIS_IDC_INI` bigint(20) DEFAULT NULL,
  `DEFR_ID` bigint(20) DEFAULT NULL,
  `LOTA_ID` bigint(20) DEFAULT NULL,
  `ORGU_ID` bigint(20) DEFAULT NULL,
  `PESS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`RESP_ID`),
  KEY `FKq7hquvl1qalkhp43j8qa8x96f` (`DEFR_ID`),
  CONSTRAINT `FKq7hquvl1qalkhp43j8qa8x96f` FOREIGN KEY (`DEFR_ID`) REFERENCES `wf_def_responsavel` (`DEFR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wf_variavel`
--

DROP TABLE IF EXISTS `wf_variavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wf_variavel` (
  `VARI_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VARI_FG` bit(1) DEFAULT NULL,
  `VARI_DF` datetime(6) DEFAULT NULL,
  `VARI_NM` varchar(256) DEFAULT NULL,
  `VARI_NR` double DEFAULT NULL,
  `VARI_TX` varchar(255) DEFAULT NULL,
  `PROC_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`VARI_ID`),
  KEY `FK9it3v7ops0efd7b5g02pxjc3l` (`PROC_ID`),
  CONSTRAINT `FK9it3v7ops0efd7b5g02pxjc3l` FOREIGN KEY (`PROC_ID`) REFERENCES `wf_procedimento` (`PROC_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'sigawf'
--

--
-- Dumping routines for database 'sigawf'
--
/*!50003 DROP FUNCTION IF EXISTS `remove_acento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `remove_acento`( textvalue varchar(20000) ) RETURNS varchar(20000) CHARSET utf8
    DETERMINISTIC
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

end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-31 17:03:46
