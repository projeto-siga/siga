CREATE DATABASE  IF NOT EXISTS `siga` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `siga`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: siga
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
-- Table structure for table `ex_boletim_doc`
--

DROP TABLE IF EXISTS `ex_boletim_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_boletim_doc` (
  `ID_BOLETIM_DOC` double NOT NULL AUTO_INCREMENT,
  `ID_DOC` double DEFAULT NULL,
  `ID_BOLETIM` double DEFAULT NULL,
  PRIMARY KEY (`ID_BOLETIM_DOC`),
  UNIQUE KEY `BOL_DOC_UK` (`ID_DOC`),
  KEY `BOL_BOLETIM_FK` (`ID_BOLETIM`),
  CONSTRAINT `BOL_BOLETIM_FK` FOREIGN KEY (`ID_BOLETIM`) REFERENCES `ex_documento` (`ID_DOC`),
  CONSTRAINT `BOL_DOC_FK` FOREIGN KEY (`ID_DOC`) REFERENCES `ex_documento` (`ID_DOC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_boletim_doc`
--

LOCK TABLES `ex_boletim_doc` WRITE;
/*!40000 ALTER TABLE `ex_boletim_doc` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_boletim_doc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_classificacao`
--

DROP TABLE IF EXISTS `ex_classificacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_classificacao` (
  `ID_CLASSIFICACAO` bigint(20) NOT NULL AUTO_INCREMENT,
  `codificacao` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DESCR_CLASSIFICACAO` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `OBS` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HIS_IDC_INI` double DEFAULT NULL,
  `HIS_IDC_FIM` double DEFAULT NULL,
  `HIS_ATIVO` double NOT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_CLASSIFICACAO`),
  KEY `HIS_IDC_INI` (`HIS_IDC_INI`),
  KEY `HIS_IDC_FIM` (`HIS_IDC_FIM`),
  KEY `HIS_ID_INI` (`HIS_ID_INI`),
  CONSTRAINT `ex_classificacao_ibfk_1` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_classificacao_ibfk_2` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_classificacao_ibfk_3` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_classificacao`
--

LOCK TABLES `ex_classificacao` WRITE;
/*!40000 ALTER TABLE `ex_classificacao` DISABLE KEYS */;
INSERT INTO `ex_classificacao` VALUES (1,'00.00.00.00','ORGANIZAÇÃO E FUNCIONAMENTO',NULL,NULL,NULL,1,1,'2009-03-13 00:00:00',NULL),(2,'00.01.00.00','REGULAMENTAÇÃO',NULL,NULL,NULL,1,2,'2009-03-13 00:00:00',NULL),(3,'00.01.01.00','ORGANIZAÇÃO ADMINISTRATIVA',NULL,NULL,NULL,1,3,'2009-03-13 00:00:00',NULL),(4,'00.01.01.01','Modernização Administrativa',NULL,NULL,NULL,1,4,'2009-03-13 00:00:00',NULL),(5,'00.06.00.00','FISCALIZAÇÃO CONTÁBIL, FINANCEIRA, ORÇAMENTÁRIA',NULL,NULL,NULL,1,5,'2009-03-13 00:00:00',NULL),(6,'00.06.01.00','AUDITORIA',NULL,NULL,NULL,1,6,'2009-03-13 00:00:00',NULL),(7,'00.06.01.01','Auditoria externa',NULL,NULL,NULL,1,7,'2009-03-13 00:00:00',NULL),(8,'00.06.01.02','Auditoria  interna',NULL,NULL,NULL,1,8,'2009-03-13 00:00:00',NULL),(10,'00.06.02.00','APRESTAÇÃO DE CONTAS',NULL,NULL,NULL,1,10,'2009-03-13 00:00:00',NULL),(11,'00.06.02.01','Tomada de contas especial',NULL,NULL,NULL,1,11,'2009-03-13 00:00:00',NULL),(12,'00.06.02.03','Decisão do TCU sobre as contas',NULL,NULL,NULL,1,12,'2009-03-13 00:00:00',NULL);
/*!40000 ALTER TABLE `ex_classificacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_competencia`
--

DROP TABLE IF EXISTS `ex_competencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_competencia` (
  `FG_COMPETENCIA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ID_PESSOA` double DEFAULT NULL,
  `ID_CARGO` bigint(20) DEFAULT NULL,
  `ID_LOTACAO` int(11) DEFAULT NULL,
  `DT_INI_VIG_COMPETENCIA` datetime NOT NULL,
  `DT_FIM_VIG_COMPETENCIA` datetime DEFAULT NULL,
  `ID_COMPETENCIA` bigint(20) DEFAULT NULL,
  `ID_FUNCAO_CONFIANCA` double DEFAULT NULL,
  `ID_FORMA_DOC` bigint(20) NOT NULL,
  PRIMARY KEY (`FG_COMPETENCIA`),
  KEY `COMPETENCIA_CARGO_FK` (`ID_CARGO`),
  KEY `COMPETENCIA_FUNC_CONF_FK` (`ID_FUNCAO_CONFIANCA`),
  KEY `COMPETENCIA_LOTACAO_FK` (`ID_LOTACAO`),
  KEY `COMPETENCIA_PESSOA_FK` (`ID_PESSOA`),
  CONSTRAINT `COMPETENCIA_CARGO_FK` FOREIGN KEY (`ID_CARGO`) REFERENCES `corporativo`.`dp_cargo` (`ID_CARGO`),
  CONSTRAINT `COMPETENCIA_FUNC_CONF_FK` FOREIGN KEY (`ID_FUNCAO_CONFIANCA`) REFERENCES `corporativo`.`dp_funcao_confianca` (`ID_FUNCAO_CONFIANCA`),
  CONSTRAINT `COMPETENCIA_LOTACAO_FK` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `COMPETENCIA_PESSOA_FK` FOREIGN KEY (`ID_PESSOA`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_competencia`
--

LOCK TABLES `ex_competencia` WRITE;
/*!40000 ALTER TABLE `ex_competencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_competencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_configuracao`
--

DROP TABLE IF EXISTS `ex_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_configuracao` (
  `ID_CONFIGURACAO_EX` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_TP_MOV` bigint(20) DEFAULT NULL,
  `ID_TP_DOC` bigint(20) DEFAULT NULL,
  `ID_TP_FORMA_DOC` double DEFAULT NULL,
  `ID_FORMA_DOC` bigint(20) DEFAULT NULL,
  `ID_MOD` bigint(20) DEFAULT NULL,
  `ID_CLASSIFICACAO` bigint(20) DEFAULT NULL,
  `ID_VIA` bigint(20) DEFAULT NULL,
  `ID_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  `ID_PAPEL` double DEFAULT NULL,
  PRIMARY KEY (`ID_CONFIGURACAO_EX`),
  KEY `FK_CLASSIFICACAO` (`ID_CLASSIFICACAO`),
  KEY `FK_FORMA_DOC` (`ID_FORMA_DOC`),
  KEY `FK_MOD` (`ID_MOD`),
  KEY `FK_NIVEL_ACESSO` (`ID_NIVEL_ACESSO`),
  KEY `FK_PAPEL` (`ID_PAPEL`),
  KEY `FK_TP_DOC` (`ID_TP_DOC`),
  KEY `FK_TP_FORMA_DOC` (`ID_TP_FORMA_DOC`),
  KEY `FK_TP_MOV` (`ID_TP_MOV`),
  KEY `FK_VIA` (`ID_VIA`),
  CONSTRAINT `FK_CLASSIFICACAO` FOREIGN KEY (`ID_CLASSIFICACAO`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `FK_FORMA_DOC` FOREIGN KEY (`ID_FORMA_DOC`) REFERENCES `ex_forma_documento` (`ID_FORMA_DOC`),
  CONSTRAINT `FK_MOD` FOREIGN KEY (`ID_MOD`) REFERENCES `ex_modelo` (`ID_MOD`),
  CONSTRAINT `FK_NIVEL_ACESSO` FOREIGN KEY (`ID_NIVEL_ACESSO`) REFERENCES `ex_nivel_acesso` (`ID_NIVEL_ACESSO`),
  CONSTRAINT `FK_PAPEL` FOREIGN KEY (`ID_PAPEL`) REFERENCES `ex_papel` (`ID_PAPEL`),
  CONSTRAINT `FK_TP_DOC` FOREIGN KEY (`ID_TP_DOC`) REFERENCES `ex_tipo_documento` (`ID_TP_DOC`),
  CONSTRAINT `FK_TP_FORMA_DOC` FOREIGN KEY (`ID_TP_FORMA_DOC`) REFERENCES `ex_tipo_forma_documento` (`ID_TIPO_FORMA_DOC`),
  CONSTRAINT `FK_TP_MOV` FOREIGN KEY (`ID_TP_MOV`) REFERENCES `ex_tipo_movimentacao` (`ID_TP_MOV`),
  CONSTRAINT `FK_VIA` FOREIGN KEY (`ID_VIA`) REFERENCES `ex_via` (`ID_VIA`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_configuracao`
--

LOCK TABLES `ex_configuracao` WRITE;
/*!40000 ALTER TABLE `ex_configuracao` DISABLE KEYS */;
INSERT INTO `ex_configuracao` VALUES (27,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,NULL),(28,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,NULL,NULL,NULL,NULL,78,NULL,NULL,NULL,NULL),(31,NULL,NULL,NULL,NULL,529,NULL,NULL,NULL,NULL),(32,NULL,NULL,NULL,NULL,545,NULL,NULL,NULL,NULL),(33,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(34,58,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(35,59,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(36,60,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `ex_configuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_documento`
--

DROP TABLE IF EXISTS `ex_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_documento` (
  `ID_DOC` double NOT NULL AUTO_INCREMENT,
  `NUM_EXPEDIENTE` bigint(20) DEFAULT NULL,
  `ANO_EMISSAO` bigint(20) DEFAULT NULL,
  `ID_TP_DOC` bigint(20) NOT NULL,
  `ID_CADASTRANTE` double NOT NULL,
  `ID_LOTA_CADASTRANTE` int(11) NOT NULL,
  `ID_SUBSCRITOR` double DEFAULT NULL,
  `ID_LOTA_SUBSCRITOR` int(11) DEFAULT NULL,
  `DESCR_DOCUMENTO` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DT_DOC` datetime DEFAULT NULL,
  `DT_REG_DOC` datetime NOT NULL,
  `NM_SUBSCRITOR_EXT` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NUM_EXT_DOC` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTEUDO_BLOB_DOC` blob,
  `NM_ARQ_DOC` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTEUDO_TP_DOC` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_DESTINATARIO` bigint(20) DEFAULT NULL,
  `ID_LOTA_DESTINATARIO` int(11) DEFAULT NULL,
  `NM_DESTINATARIO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DT_FINALIZACAO` datetime DEFAULT NULL,
  `ASSINATURA_BLOB_DOC` blob,
  `ID_MOD` bigint(20) DEFAULT NULL,
  `ID_ORGAO_USU` bigint(20) DEFAULT NULL,
  `ID_CLASSIFICACAO` bigint(20) DEFAULT NULL,
  `ID_FORMA_DOC` bigint(20) DEFAULT NULL,
  `FG_PESSOAL` enum('S','N') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'N',
  `ID_ORGAO_DESTINATARIO` bigint(20) DEFAULT NULL,
  `ID_ORGAO` bigint(20) DEFAULT NULL,
  `OBS_ORGAO_DOC` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NM_ORGAO_DESTINATARIO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FG_SIGILOSO` enum('S','N') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'N',
  `NM_FUNCAO_SUBSCRITOR` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FG_ELETRONICO` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'N',
  `NUM_ANTIGO_DOC` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_LOTA_TITULAR` int(11) DEFAULT NULL,
  `ID_TITULAR` double DEFAULT NULL,
  `NUM_AUX_DOC` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DSC_CLASS_DOC` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  `ID_DOC_PAI` double DEFAULT NULL,
  `NUM_VIA_DOC_PAI` double DEFAULT NULL,
  `ID_DOC_ANTERIOR` double DEFAULT NULL,
  `ID_MOB_PAI` double DEFAULT NULL,
  `NUM_SEQUENCIA` smallint(6) DEFAULT NULL,
  `NUM_PAGINAS` smallint(6) DEFAULT NULL,
  `DT_DOC_ORIGINAL` datetime DEFAULT NULL,
  `ID_MOB_AUTUADO` double DEFAULT NULL,
  `DNM_DT_ACESSO` datetime DEFAULT NULL,
  `DNM_ACESSO` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DNM_ID_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  `HIS_DT_ALT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_DOC`),
  UNIQUE KEY `DOCUMENTO_PK` (`ID_DOC`),
  UNIQUE KEY `UNIQUE_DOC_NUM_IDX` (`ID_FORMA_DOC`,`NUM_EXPEDIENTE`,`ANO_EMISSAO`,`ID_ORGAO_USU`),
  KEY `EX_DOCUMENTO_IDX_011` (`ID_ORGAO_USU`,`ANO_EMISSAO`),
  KEY `DOC_FORMA_NUM_ANO_IX` (`ID_FORMA_DOC`,`NUM_EXPEDIENTE`,`ANO_EMISSAO`),
  KEY `SIGA_EXDOC_MOB_PAI_ID_DOC_IX` (`ID_MOB_PAI`,`ID_DOC`),
  KEY `DOC_TP_DOC_FK` (`ID_TP_DOC`),
  KEY `IXCF_DOC_CP_ORGAO_DEST_FK` (`ID_ORGAO_DESTINATARIO`),
  KEY `IXCF_DOC_CP_ORGAO_FK` (`ID_ORGAO`),
  KEY `IXCF_DOC_TITULAR_PESSOA_FK` (`ID_TITULAR`),
  KEY `IXCF_DOC_CADASTRANTE_PESSOA_FK` (`ID_CADASTRANTE`),
  KEY `IXCF_DOC_SUBSCRITOR_PESSOA_FK` (`ID_SUBSCRITOR`),
  KEY `IXCF_DOC_NIVEL_ACESSO_FK` (`ID_NIVEL_ACESSO`),
  KEY `IXCF_DOC_CLASSIFICACAO_FK` (`ID_CLASSIFICACAO`),
  KEY `IXCF_DOC_LOTA_TIT_LOTACAO_FK` (`ID_LOTA_TITULAR`),
  KEY `IXCF_DOCUMENTO_MODELO_FK` (`ID_MOD`),
  KEY `SIGA_EXDOC_MOB_AUTUADO_ID_IX` (`ID_MOB_AUTUADO`),
  CONSTRAINT `DOCUMENTO_MODELO_FK` FOREIGN KEY (`ID_MOD`) REFERENCES `ex_modelo` (`ID_MOD`),
  CONSTRAINT `DOC_CADASTRANTE_PESSOA_FK` FOREIGN KEY (`ID_CADASTRANTE`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `DOC_CLASSIFICACAO_FK` FOREIGN KEY (`ID_CLASSIFICACAO`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `DOC_CP_ORGAO_DEST_FK` FOREIGN KEY (`ID_ORGAO_DESTINATARIO`) REFERENCES `corporativo`.`cp_orgao` (`ID_ORGAO`),
  CONSTRAINT `DOC_CP_ORGAO_FK` FOREIGN KEY (`ID_ORGAO`) REFERENCES `corporativo`.`cp_orgao` (`ID_ORGAO`),
  CONSTRAINT `DOC_FORMA_DOC_FK` FOREIGN KEY (`ID_FORMA_DOC`) REFERENCES `ex_forma_documento` (`ID_FORMA_DOC`),
  CONSTRAINT `DOC_LOTA_TITULAR_LOTACAO_FK` FOREIGN KEY (`ID_LOTA_TITULAR`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `DOC_NIVEL_ACESSO_FK` FOREIGN KEY (`ID_NIVEL_ACESSO`) REFERENCES `ex_nivel_acesso` (`ID_NIVEL_ACESSO`),
  CONSTRAINT `DOC_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `corporativo`.`cp_orgao_usuario` (`ID_ORGAO_USU`),
  CONSTRAINT `DOC_SUBSCRITOR_PESSOA_FK` FOREIGN KEY (`ID_SUBSCRITOR`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `DOC_TITULAR_PESSOA_FK` FOREIGN KEY (`ID_TITULAR`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `DOC_TP_DOC_FK` FOREIGN KEY (`ID_TP_DOC`) REFERENCES `ex_tipo_documento` (`ID_TP_DOC`),
  CONSTRAINT `SIGA_EXDOC_MOB_AUTUADO_ID_IX` FOREIGN KEY (`ID_MOB_AUTUADO`) REFERENCES `ex_mobil` (`ID_MOBIL`),
  CONSTRAINT `ex_documento_ibfk_1` FOREIGN KEY (`ID_MOB_PAI`) REFERENCES `ex_mobil` (`ID_MOBIL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_documento`
--

LOCK TABLES `ex_documento` WRITE;
/*!40000 ALTER TABLE `ex_documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_documento` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `EX_DOCUMENTO_BLOCK_UPD` BEFORE UPDATE ON `ex_documento` FOR EACH ROW BEGIN

    DECLARE QTD_DOC_VAR  BIGINT;
    DECLARE Conteudo_blob_var blob;
	DECLARE PID_ORGAO_USU BIGINT;
	DECLARE PID_FORMA_DOC BIGINT;
	DECLARE PANO_EMISSAO INT;
	DECLARE v_Return DOUBLE;
	
	SET QTD_DOC_VAR = 0;
	if OLD.dt_finalizacao is not null and OLD.dt_finalizacao <> NEW.dt_finalizacao THEN
		   signal sqlstate value '20101' set message_text = 'Não é permitido alterar uma DATA de Finalização já existente';
	end if; 
	if OLD.fg_eletronico = 'N' then
		if  NEW.conteudo_blob_doc <> OLD.conteudo_blob_doc then
			signal sqlstate value '20101' set message_text = 'Não é permitido alterar: não eletrônico com data de fechamento e com conteúdo.';
		end if;
		if OLD.ID_DOC <> NEW.ID_DOC OR
		  OLD.NUM_EXPEDIENTE <> NEW.NUM_EXPEDIENTE OR
		  OLD.ANO_EMISSAO <> NEW.ANO_EMISSAO OR
		  OLD.ID_TP_DOC <> NEW.ID_TP_DOC OR
		  OLD.ID_CADASTRANTE <> NEW.ID_CADASTRANTE OR
		  OLD.ID_LOTA_CADASTRANTE <> NEW.ID_LOTA_CADASTRANTE OR
		  OLD.ID_SUBSCRITOR <> NEW.ID_SUBSCRITOR OR
		  OLD.ID_LOTA_SUBSCRITOR <> NEW.ID_LOTA_SUBSCRITOR OR
		  OLD.DESCR_DOCUMENTO <> NEW.DESCR_DOCUMENTO OR
		  OLD.DT_DOC <> NEW.DT_DOC OR
		  OLD.DT_REG_DOC <> NEW.DT_REG_DOC OR
		  OLD.NM_SUBSCRITOR_EXT <> NEW.NM_SUBSCRITOR_EXT OR
		  OLD.NUM_EXT_DOC <> NEW.NUM_EXT_DOC OR
		  OLD.NM_ARQ_DOC <> NEW.NM_ARQ_DOC OR
		  OLD.CONTEUDO_TP_DOC <> NEW.CONTEUDO_TP_DOC OR
		  OLD.ID_DESTINATARIO <> NEW.ID_DESTINATARIO OR
		  OLD.ID_LOTA_DESTINATARIO <> NEW.ID_LOTA_DESTINATARIO OR
		  OLD.NM_DESTINATARIO <> NEW.NM_DESTINATARIO OR
		  OLD.dt_finalizacao <> NEW.dt_finalizacao OR
		  OLD.ASSINATURA_BLOB_DOC <> NEW.ASSINATURA_BLOB_DOC OR
		  OLD.ID_MOD <> NEW.ID_MOD OR
		  OLD.ID_ORGAO_USU <> NEW.ID_ORGAO_USU OR
		  OLD.ID_CLASSIFICACAO <> NEW.ID_CLASSIFICACAO OR
		  OLD.ID_FORMA_DOC <> NEW.ID_FORMA_DOC OR
		  OLD.FG_PESSOAL <> NEW.FG_PESSOAL OR
		  OLD.ID_ORGAO_DESTINATARIO <> NEW.ID_ORGAO_DESTINATARIO OR
		  OLD.ID_ORGAO <> NEW.ID_ORGAO OR
		  OLD.OBS_ORGAO_DOC <> NEW.OBS_ORGAO_DOC OR
		  OLD.NM_ORGAO_DESTINATARIO <> NEW.NM_ORGAO_DESTINATARIO OR
		  OLD.FG_SIGILOSO <> NEW.FG_SIGILOSO OR
		  OLD.NM_FUNCAO_SUBSCRITOR <> NEW.NM_FUNCAO_SUBSCRITOR OR
		  OLD.FG_ELETRONICO <> NEW.FG_ELETRONICO OR
		  OLD.NUM_ANTIGO_DOC <> NEW.NUM_ANTIGO_DOC OR
		  OLD.ID_LOTA_TITULAR <> NEW.ID_LOTA_TITULAR OR
		  OLD.ID_TITULAR <> NEW.ID_TITULAR OR
		  OLD.NUM_AUX_DOC <> NEW.NUM_AUX_DOC OR
		  OLD.DSC_CLASS_DOC <> NEW.DSC_CLASS_DOC OR
		  OLD.ID_DOC_PAI <> NEW.ID_DOC_PAI OR
		  OLD.NUM_VIA_DOC_PAI <> NEW.NUM_VIA_DOC_PAI OR
		  OLD.ID_DOC_ANTERIOR <> NEW.ID_DOC_ANTERIOR OR
		  OLD.ID_MOB_PAI <> NEW.ID_MOB_PAI OR
		  OLD.NUM_SEQUENCIA <> NEW.NUM_SEQUENCIA OR
		  OLD.NUM_PAGINAS <> NEW.NUM_PAGINAS OR
		  OLD.DT_DOC_ORIGINAL <> NEW.DT_DOC_ORIGINAL OR
		  OLD.ID_MOB_AUTUADO <> NEW.ID_MOB_AUTUADO 
		then
			signal sqlstate value '20101' set message_text = 'Não é permitido alterar: não eletrônico com data de finalização e com conteúdo.';
		end if;
	elseif OLD.fg_eletronico = 'S' then
	    SET QTD_DOC_VAR = ( select count(*) from siga.ex_documento doc
			where  EXISTS (select * from siga.ex_movimentacao m, 
								  siga.ex_mobil mb
							where mb.id_doc = doc.id_doc and
								  mb.id_doc = OLD.id_doc and
								  (m.id_tp_mov = 11 ) and
								  m.id_mov_canceladora is null and
								  m.id_mobil = mb.id_mobil) );
		if  QTD_DOC_VAR <> 0 then
			if OLD.ID_DOC <> NEW.ID_DOC OR
			  OLD.NUM_EXPEDIENTE <> NEW.NUM_EXPEDIENTE OR
			  OLD.ANO_EMISSAO <> NEW.ANO_EMISSAO OR
			  OLD.ID_TP_DOC <> NEW.ID_TP_DOC OR
			  OLD.ID_CADASTRANTE <> NEW.ID_CADASTRANTE OR
			  OLD.ID_LOTA_CADASTRANTE <> NEW.ID_LOTA_CADASTRANTE OR
			  OLD.ID_SUBSCRITOR <> NEW.ID_SUBSCRITOR OR
			  OLD.ID_LOTA_SUBSCRITOR <> NEW.ID_LOTA_SUBSCRITOR OR
			  OLD.DESCR_DOCUMENTO <> NEW.DESCR_DOCUMENTO OR
			  OLD.DT_DOC <> NEW.DT_DOC OR
			  OLD.DT_REG_DOC <> NEW.DT_REG_DOC OR
			  OLD.NM_SUBSCRITOR_EXT <> NEW.NM_SUBSCRITOR_EXT OR
			  OLD.NUM_EXT_DOC <> NEW.NUM_EXT_DOC OR
			  OLD.CONTEUDO_BLOB_DOC <> NEW.CONTEUDO_BLOB_DOC OR
			  OLD.NM_ARQ_DOC <> NEW.NM_ARQ_DOC OR
			  OLD.CONTEUDO_TP_DOC <> NEW.CONTEUDO_TP_DOC OR
			  OLD.ID_DESTINATARIO <> NEW.ID_DESTINATARIO OR
			  OLD.ID_LOTA_DESTINATARIO <> NEW.ID_LOTA_DESTINATARIO OR
			  OLD.NM_DESTINATARIO <> NEW.NM_DESTINATARIO OR
			  OLD.dt_finalizacao <> NEW.dt_finalizacao OR
			  OLD.ASSINATURA_BLOB_DOC <> NEW.ASSINATURA_BLOB_DOC OR
			  OLD.ID_MOD <> NEW.ID_MOD OR
			  OLD.ID_ORGAO_USU <> NEW.ID_ORGAO_USU OR
			  OLD.ID_CLASSIFICACAO <> NEW.ID_CLASSIFICACAO OR
			  OLD.ID_FORMA_DOC <> NEW.ID_FORMA_DOC OR
			  OLD.FG_PESSOAL <> NEW.FG_PESSOAL OR
			  OLD.ID_ORGAO_DESTINATARIO <> NEW.ID_ORGAO_DESTINATARIO OR
			  OLD.ID_ORGAO <> NEW.ID_ORGAO OR
			  OLD.OBS_ORGAO_DOC <> NEW.OBS_ORGAO_DOC OR
			  OLD.NM_ORGAO_DESTINATARIO <> NEW.NM_ORGAO_DESTINATARIO OR
			  OLD.FG_SIGILOSO <> NEW.FG_SIGILOSO OR
			  OLD.NM_FUNCAO_SUBSCRITOR <> NEW.NM_FUNCAO_SUBSCRITOR OR
			  OLD.FG_ELETRONICO <> NEW.FG_ELETRONICO OR
			  OLD.NUM_ANTIGO_DOC <> NEW.NUM_ANTIGO_DOC OR
			  OLD.ID_LOTA_TITULAR <> NEW.ID_LOTA_TITULAR OR
			  OLD.ID_TITULAR <> NEW.ID_TITULAR OR
			  OLD.NUM_AUX_DOC <> NEW.NUM_AUX_DOC OR
			  OLD.DSC_CLASS_DOC <> NEW.DSC_CLASS_DOC OR
			  OLD.ID_DOC_PAI <> NEW.ID_DOC_PAI OR
			  OLD.NUM_VIA_DOC_PAI <> NEW.NUM_VIA_DOC_PAI OR
			  OLD.ID_DOC_ANTERIOR <> NEW.ID_DOC_ANTERIOR OR
			  OLD.ID_MOB_PAI <> NEW.ID_MOB_PAI OR
			  OLD.NUM_SEQUENCIA <> NEW.NUM_SEQUENCIA OR
			  OLD.NUM_PAGINAS <> NEW.NUM_PAGINAS OR
			  OLD.DT_DOC_ORIGINAL <> NEW.DT_DOC_ORIGINAL OR
			  OLD.ID_MOB_AUTUADO <> NEW.ID_MOB_AUTUADO 
			then       
			  signal sqlstate value '20101' set message_text = 'Não é permitido alterar: eletrônico, com conteúdo, tipo mov. 11 e sem mov. canceladora.';
			end if;
	   end if;
	end if;

end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `EX_DOCUMENTO_BLOCK_DEL` BEFORE DELETE ON `ex_documento` FOR EACH ROW BEGIN

	if OLD.dt_finalizacao is not null then
		signal sqlstate value '20101' set message_text = 'Não é permitido excluir: data de finalização existente.';
	end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ex_email_notificacao`
--

DROP TABLE IF EXISTS `ex_email_notificacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_email_notificacao` (
  `ID_EMAIL_NOTIFICACAO` double NOT NULL AUTO_INCREMENT,
  `ID_LOTACAO` int(11) DEFAULT NULL,
  `ID_PESSOA` double DEFAULT NULL,
  `ID_LOTA_EMAIL` int(11) DEFAULT NULL,
  `ID_PESSOA_EMAIL` double DEFAULT NULL,
  `EMAIL` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_EMAIL_NOTIFICACAO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_email_notificacao`
--

LOCK TABLES `ex_email_notificacao` WRITE;
/*!40000 ALTER TABLE `ex_email_notificacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_email_notificacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_estado_doc`
--

DROP TABLE IF EXISTS `ex_estado_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_estado_doc` (
  `ID_ESTADO_DOC` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESC_ESTADO_DOC` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ORDEM_ESTADO_DOC` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_ESTADO_DOC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_estado_doc`
--

LOCK TABLES `ex_estado_doc` WRITE;
/*!40000 ALTER TABLE `ex_estado_doc` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_estado_doc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_estado_tp_mov`
--

DROP TABLE IF EXISTS `ex_estado_tp_mov`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_estado_tp_mov` (
  `ID_ESTADO_DOC` bigint(20) NOT NULL,
  `ID_TP_MOV` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_ESTADO_DOC`,`ID_TP_MOV`),
  KEY `TP_MOV_ESTADO_TPMOV_FK` (`ID_TP_MOV`),
  CONSTRAINT `ESTADO_TPMOV_ESTADO_FK` FOREIGN KEY (`ID_ESTADO_DOC`) REFERENCES `ex_estado_doc` (`ID_ESTADO_DOC`),
  CONSTRAINT `TP_MOV_ESTADO_TPMOV_FK` FOREIGN KEY (`ID_TP_MOV`) REFERENCES `ex_tipo_movimentacao` (`ID_TP_MOV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_estado_tp_mov`
--

LOCK TABLES `ex_estado_tp_mov` WRITE;
/*!40000 ALTER TABLE `ex_estado_tp_mov` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_estado_tp_mov` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_forma_documento`
--

DROP TABLE IF EXISTS `ex_forma_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_forma_documento` (
  `ID_FORMA_DOC` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCR_FORMA_DOC` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `SIGLA_FORMA_DOC` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ID_TIPO_FORMA_DOC` double NOT NULL,
  PRIMARY KEY (`ID_FORMA_DOC`),
  KEY `EX_FORMA_DOCUMENTO_IDX_021` (`SIGLA_FORMA_DOC`,`ID_FORMA_DOC`,`DESCR_FORMA_DOC`),
  KEY `ID_TIPO_FORMA_DOC` (`ID_TIPO_FORMA_DOC`),
  CONSTRAINT `ex_forma_documento_ibfk_1` FOREIGN KEY (`ID_TIPO_FORMA_DOC`) REFERENCES `ex_tipo_forma_documento` (`ID_TIPO_FORMA_DOC`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_forma_documento`
--

LOCK TABLES `ex_forma_documento` WRITE;
/*!40000 ALTER TABLE `ex_forma_documento` DISABLE KEYS */;
INSERT INTO `ex_forma_documento` VALUES (1,'Ofício','OFI',1),(2,'Memorando','MEM',1),(3,'Formulário','FOR',1),(4,'Informação','INF',1),(5,'Externo','EXT',1),(8,'Despacho','DES',1),(9,'Contrato','CON',1),(12,'Requerimento','REQ',1),(13,'Solicitação','SOL',1),(14,'Parecer','PAR',1),(15,'Certidão','CER',1),(54,'Boletim Interno','BIE',1),(55,'Processo Administrativo','ADM',2),(56,'Processo de Pessoal','RHU',2),(57,'Processo de Execução Orçamentária e Financeira','EOF',2),(97,'Memória de Reunião','MRU',1),(98,'Relatório','REL',1),(99,'Documento Capturado','CAP',1);
/*!40000 ALTER TABLE `ex_forma_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_mobil`
--

DROP TABLE IF EXISTS `ex_mobil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_mobil` (
  `ID_MOBIL` double NOT NULL AUTO_INCREMENT,
  `ID_DOC` double NOT NULL,
  `ID_TIPO_MOBIL` double NOT NULL,
  `NUM_SEQUENCIA` tinyint(4) NOT NULL,
  `DNM_ULTIMA_ANOTACAO` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DNM_NUM_PRIMEIRA_PAGINA` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_MOBIL`),
  UNIQUE KEY `IDX_MOBIL_UNIQUE` (`ID_DOC`,`ID_TIPO_MOBIL`,`NUM_SEQUENCIA`),
  KEY `ID_TIPO_MOBIL` (`ID_TIPO_MOBIL`),
  CONSTRAINT `ex_mobil_ibfk_1` FOREIGN KEY (`ID_DOC`) REFERENCES `ex_documento` (`ID_DOC`),
  CONSTRAINT `ex_mobil_ibfk_2` FOREIGN KEY (`ID_TIPO_MOBIL`) REFERENCES `ex_tipo_mobil` (`ID_TIPO_MOBIL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_mobil`
--

LOCK TABLES `ex_mobil` WRITE;
/*!40000 ALTER TABLE `ex_mobil` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_mobil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_modelo`
--

DROP TABLE IF EXISTS `ex_modelo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_modelo` (
  `ID_MOD` bigint(20) NOT NULL AUTO_INCREMENT,
  `NM_MOD` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESC_MOD` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTEUDO_BLOB_MOD` blob,
  `CONTEUDO_TP_BLOB` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NM_ARQ_MOD` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_CLASSIFICACAO` bigint(20) DEFAULT NULL,
  `ID_FORMA_DOC` bigint(20) DEFAULT NULL,
  `ID_CLASS_CRIACAO_VIA` bigint(20) DEFAULT NULL,
  `ID_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_INI` double DEFAULT NULL,
  `HIS_IDC_FIM` double DEFAULT NULL,
  `HIS_ATIVO` double NOT NULL,
  `NM_DIRETORIO` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HIS_IDE` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_MOD`),
  KEY `EX_MODELO_IDX_014` (`ID_CLASSIFICACAO`),
  KEY `EX_MODELO_IDX_017` (`ID_NIVEL_ACESSO`),
  KEY `EX_MODELO_IDX_016` (`ID_FORMA_DOC`),
  KEY `EX_MODELO_IDX_015` (`ID_CLASS_CRIACAO_VIA`),
  KEY `HIS_IDC_INI` (`HIS_IDC_INI`),
  KEY `HIS_IDC_FIM` (`HIS_IDC_FIM`),
  CONSTRAINT `MOD_CLASSIFICACAO_FK` FOREIGN KEY (`ID_CLASSIFICACAO`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `MOD_CLASS_VIA_FK` FOREIGN KEY (`ID_CLASS_CRIACAO_VIA`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `MOD_FORMA_DOC_FK` FOREIGN KEY (`ID_FORMA_DOC`) REFERENCES `ex_forma_documento` (`ID_FORMA_DOC`),
  CONSTRAINT `MOD_NIVEL_ACESSO_FK` FOREIGN KEY (`ID_NIVEL_ACESSO`) REFERENCES `ex_nivel_acesso` (`ID_NIVEL_ACESSO`),
  CONSTRAINT `ex_modelo_ibfk_1` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_modelo_ibfk_2` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=667 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_modelo`
--

LOCK TABLES `ex_modelo` WRITE;
/*!40000 ALTER TABLE `ex_modelo` DISABLE KEYS */;
INSERT INTO `ex_modelo` VALUES (2,'Ofício','Ofício',' \n[@oficio/]\n','template/freemarker','oficio.jsp',NULL,1,NULL,NULL,2,NULL,NULL,NULL,NULL,1,NULL,NULL),(26,'Memorando','Memorando','\n[@entrevista]\n	[@grupo titulo=\"Texto a ser inserido no corpo do memorando\"]\n		[@grupo]\n			[@editor titulo=\"\" var=\"texto_memorando\" /]\n		[/@grupo]\n	[/@grupo]\n	[@grupo]\n	        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n	[/@grupo]\n	[@grupo]\n       		[@selecao titulo=\"Fecho\" var=\"fecho\" opcoes=\"Atenciosamente;Cordialmente;Respeitosamente\" /]\n	[/@grupo]\n[/@entrevista]\n\n[@documento]\n        [@memorando texto=texto_memorando! fecho=(fecho!)+\",\" tamanhoLetra=tamanhoLetra! /]\n[/@documento]\n','template/freemarker','memorando.jsp',NULL,2,NULL,NULL,26,NULL,NULL,NULL,NULL,1,NULL,NULL),(27,'Informação','Informação','\n[@entrevista]\n    [@grupo titulo=\"Dados do Documento de Origem\"]\n        [@grupo]\n            [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n            [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n        [/@grupo]\n        [@grupo]\n            [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n            [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n        [/@grupo]\n    [/@grupo]\n    [@grupo]\n        [@texto titulo=\"Vocativo\" var=\"vocativo\" largura=\"100\" /]\n    [/@grupo]\n    [@grupo titulo=\"Texto da informação\"]\n        [@editor titulo=\"\" var=\"texto_informacao\" /]\n    [/@grupo]\n    [@grupo]\n        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n    [/@grupo]\n[/@entrevista]\n\n[@documento margemDireita=\"3cm\"]\n    [#if tamanhoLetra! == \"Normal\"]\n        [#assign tl = \"11pt\" /]\n    [#elseif tamanhoLetra! == \"Pequeno\"]\n        [#assign tl = \"9pt\" /]\n    [#elseif tamanhoLetra! == \"Grande\"]\n        [#assign tl = \"13pt\" /]\n    [#else]     \n        [#assign tl = \"11pt\"]\n    [/#if]\n\n    [@estiloBrasaoCentralizado tipo=\"INFORMAÇÃO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n            [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n                Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>\n            [/#if]\n            Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${vocativo!}</span></p>\n                <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${texto_informacao}</span></p>\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n','template/freemarker',NULL,NULL,4,NULL,NULL,27,NULL,NULL,NULL,NULL,1,NULL,NULL),(28,'Documento Externo',NULL,NULL,NULL,'externo.jsp',NULL,5,NULL,NULL,28,NULL,NULL,NULL,NULL,1,NULL,NULL),(29,'Interno Importado',NULL,NULL,NULL,'interno_antigo.jsp',NULL,NULL,NULL,NULL,29,NULL,NULL,NULL,NULL,1,NULL,NULL),(78,'Despacho','Despacho','\n[#-- Se existir uma variável chamada \'texto\' copiar seu valor para \'texto_depacho\', pois a macro vai destruir o conteúdo da variável --]\n[@entrevista]\n  [@grupo titulo=\"Órgão de destino\"]\n    [#assign orgao_dest_atual = (doc.lotaDestinatario.nomeLotacao)!/]\n    [#if orgao_dest_ult! != orgao_dest_atual]\n      [#assign orgao_dest = orgao_dest_atual/]\n      [#assign orgao_dest_ult = orgao_dest_atual/]\n    [/#if]\n    [@oculto var=\"orgao_dest_ult\"/]\n    [@grupo]\n      [@selecao titulo=\"\" var=\"combinacao\" opcoes=\"A(o);À;Ao\" /]\n      [@texto titulo=\"Nome (opcional)\" var=\"orgao_dest\" largura=30 /]\n    [/@grupo]\n  [/@grupo]\n  [@grupo titulo=\"Texto do despacho\"]\n    [@grupo]\n      [#if !texto_padrao??]\n        [#assign texto_padrao = \"Para as providências cabíveis.\"/]\n      [/#if]\n      [@selecao titulo=\"Texto\" opcoes=\"A pedido.;Arquive-se.;Autorizo.;Ciente. Arquive-se.;De acordo.;Expeça-se memorando.;Expeça-se memorando-circular.;Expeça-se ofício-circular.;Intime-se.;Junte-se ao dossiê.;Junte-se ao processo.;Oficie-se.;Para as providências cabíveis.;Para atendimento.;Para atendimento e encaminhamento direto.;Para ciência.;Para publicação.;Para verificar a possibilidade de atendimento.;[Outro]\" var=\"texto_padrao\" reler=true /]\n    [/@grupo]\n  [/@grupo]\n  [@grupo depende=\"textopadrao\" esconder=((texto_padrao!\"\") != \"[Outro]\")]\n    [@editor titulo=\"\" var=\"texto_despacho\" /]\n  [/@grupo]\n  [@grupo]\n    [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n  [/@grupo]\n  [@grupo titulo=\"Dados do Documento de Origem\" esconder=doc.pai??]\n    [#if postback?? && !doc.idDoc?? && doc.pai??]\n      [#assign tipoDeDocumentoValue = doc.pai.descrFormaDoc /]\n      [#assign numeroValue = doc.pai.sigla /]\n      [#assign dataValue = doc.pai.dtDocDDMMYY /]\n      [#assign orgaoValue = doc.pai.orgaoUsuario.acronimoOrgaoUsu /]\n    [#else]\n      [#assign tipoDeDocumentoValue = tipoDeDocumentoOrigem! /]\n      [#assign numeroValue = numeroOrigem! /]\n      [#assign dataValue = dataOrigem! /]\n      [#assign orgaoValue = orgaoOrigem! /]\n    [/#if]\n    [@grupo]\n      [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n      [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n    [/@grupo]\n    [@grupo]\n      [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n      [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n    [/@grupo]\n  [/@grupo]\n[/@entrevista]\n[@documento margemDireita=\"3cm\"]\n  [#if param.tamanhoLetra! == \"Normal\"]\n    [#assign tl = \"11pt\" /]\n  [#elseif param.tamanhoLetra! == \"Pequeno\"]\n    [#assign tl = \"9pt\" /]\n  [#elseif param.tamanhoLetra! == \"Grande\"]\n    [#assign tl = \"13pt\" /]\n  [#else]\n    [#assign tl = \"11pt\"/]\n  [/#if]\n  [@estiloBrasaoCentralizado tipo=\"DESPACHO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n    <div style=\"font-family: Arial; font-size: ${tl};\">\n      [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n        Referência: ${tipoDeDocumentoOrigem!} Nº ${numeroOrigem!}\n        [#if dataOrigem?? && dataOrigem != \"\"]\n          , ${dataOrigem!}\n        [/#if]\n        [#if orgaoOrigem?? && orgaoOrigem != \"\"]\n          - ${orgaoOrigem!}.\n        [/#if]\n        <br />\n      [/#if]\n      Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n    </div>\n    <div style=\"font-family: Arial; font-size: ${tl};\">\n      [#if orgao_dest?? && orgao_dest != \"\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl}\">${combinacao} ${orgao_dest!},</span>\n        </p>\n      [#elseif (doc.lotaDestinatario.nomeLotacao)?? && (doc.lotaDestinatario.nomeLotacao) != \"\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl}\">${combinacao} ${(doc.lotaDestinatario.nomeLotacao)!},</span>\n        </p>\n      [/#if]\n      [#if (texto_padrao!\"\") != \"[Outro]\"]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl!}\">${texto_padrao!}</span>\n        </p>\n      [#else]\n        <p style=\"TEXT-INDENT: 2cm\">\n          <span style=\"font-size: ${tl!}\">${texto_despacho!}</span>\n        </p>\n      [/#if]\n    </div>\n  [/@estiloBrasaoCentralizado]\n[/@documento]\n','template/freemarker',NULL,NULL,8,NULL,NULL,78,NULL,NULL,NULL,NULL,1,NULL,NULL),(181,'Contrato','Contrato',NULL,'template-file/jsp','contrato.jsp',NULL,9,NULL,NULL,181,NULL,'2020-02-12 14:52:59',NULL,NULL,0,NULL,NULL),(241,'Boletim Interno',NULL,'ELABORE SEU MODELO DE BOLETIM INTERNO NO MENU FERRAMENTAS/CADASTRO DE MODELOS','template/freemarker','boletimInterno.jsp',NULL,54,NULL,NULL,241,NULL,NULL,NULL,NULL,1,NULL,NULL),(519,'Parecer','Parecer','\n[@entrevista]\n    [@grupo titulo=\"Dados do Documento de Origem\"]\n        [@grupo]\n            [@texto titulo=\"Tipo de documento\" var=\"tipoDeDocumentoOrigem\" largura=20 default=tipoDeDocumentoValue /]\n            [@texto titulo=\"Número\" var=\"numeroOrigem\" largura=20 default=numeroValue /]\n        [/@grupo]\n        [@grupo]\n            [@data titulo=\"Data\" var=\"dataOrigem\" default=dataValue /]\n            [@texto titulo=\"Nome do Órgão\" var=\"orgaoOrigem\" largura=30 default=orgaoValue /]\n        [/@grupo]\n    [/@grupo]\n    [@grupo]\n        [@texto titulo=\"Vocativo\" var=\"vocativo\" largura=\"100\" /]\n    [/@grupo]\n    [@grupo titulo=\"Texto do Parecer\"]\n        [@editor titulo=\"\" var=\"texto_parecer\" /]\n    [/@grupo]\n    [@grupo]\n        [@selecao titulo=\"Tamanho da letra\" var=\"tamanhoLetra\" opcoes=\"Normal;Pequeno;Grande\" /]\n    [/@grupo]\n[/@entrevista]\n\n[@documento margemDireita=\"3cm\"]\n    [#if param.tamanhoLetra! == \"Normal\"]\n        [#assign tl = \"11pt\" /]\n    [#elseif param.tamanhoLetra! == \"Pequeno\"]\n        [#assign tl = \"9pt\" /]\n    [#elseif param.tamanhoLetra! == \"Grande\"]\n        [#assign tl = \"13pt\" /]\n    [#else]     \n        [#assign tl = \"11pt\"]\n    [/#if]\n\n    [@estiloBrasaoCentralizado tipo=\"PARECER\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != \"\"]\n            Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>\n        [/#if]\n        Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n\n        <div style=\"font-family: Arial; font-size: 10pt;\">\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${vocativo!}</span></p>\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${texto_parecer}</span></p>\n            <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">É o Parecer.</span></p>\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n\n','template/freemarker',NULL,NULL,14,NULL,NULL,519,NULL,NULL,NULL,NULL,1,NULL,NULL),(529,'Certidão de desentranhamento',NULL,' Certidão de desentranhamento','template/freemarker','certidaoDesentranhamento.jsp',NULL,15,NULL,NULL,529,NULL,NULL,NULL,NULL,1,NULL,NULL),(533,'Processo de Pessoal',NULL,NULL,NULL,'processoAdministrativo.jsp',NULL,56,NULL,NULL,533,NULL,NULL,NULL,NULL,1,NULL,NULL),(534,'Processo de Execução Orçamentária e Financeira','Processo de Execução Orçamentária e Financeira',NULL,'template-file/jsp','processoAdministrativo.jsp',NULL,57,NULL,NULL,534,NULL,NULL,NULL,NULL,1,NULL,NULL),(545,'Certidão de encerramendo de volume',NULL,NULL,NULL,'certidaoEncerramentoVolume.jsp',NULL,15,NULL,NULL,545,NULL,NULL,NULL,NULL,1,NULL,NULL),(546,'Folha inicial de volume - EOF',NULL,NULL,NULL,'folhaInicialVolume.jsp',NULL,3,NULL,NULL,546,NULL,'2020-02-12 14:52:59',NULL,NULL,0,NULL,NULL),(663,'Memória de Reunião','Memória de Reunião','\n[@entrevista]\n [@grupo titulo=\"Informações Gerais\"]\n  [@grupo]\n   [@texto titulo=\"Objetivo da reunião\" var=\"objReuniao\" largura=\"84\" maxcaracteres=\"84\"/]\n  [/@grupo]\n  [@texto titulo=\"Horário\" var=\"horReuniao\" obrigatorio=\"Sim\" largura=\"4\" maxcaracteres=\"5\"/]\n  [@texto titulo=\"Local\" var=\"locReuniao\" obrigatorio=\"Sim\" largura=\"60\" maxcaracteres=\"60\"/]\n[#--\n  [@grupo]\n   [@memo titulo=\"Pendências (reuniões anteriores)\" var=\"pendencias\" colunas=\"78\" linhas=\"2\"/]\n  [/@grupo]\n--]\n  [@separador /]\n  [@selecao titulo=\"Participantes\" var=\"numParticipantes\" reler=true idAjax=\"numParticipantesAjax\" opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n  [@grupo depende=\"numParticipantesAjax\"]\n   [#if numParticipantes! != \'0\']\n    [#list 1..(numParticipantes)?number as i]\n     [@grupo]\n      [@pessoa titulo=\"\" var=\"participantes\"+i/]\n     [/@grupo]\n    [/#list]\n   [/#if]\n  [/@grupo]\n  [@separador /]\n  [@grupo]\n   [@selecao titulo=\"Participantes (extra)\" var=\"numParticipantesExtra\" reler=true idAjax=\"numPartExtraAjax\" opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n  [/@grupo]\n  [@grupo depende=\"numPartExtraAjax\"]\n   [#if numParticipantesExtra! != \'0\']\n    [#list 1..(numParticipantesExtra)?number as i]\n     [@grupo]\n      [@texto titulo=\"Nome\" var=\"participantesExtra\"+i largura=\"50\" maxcaracteres=\"101\"/]\n      [@texto titulo=\"Email\" var=\"participantesExtraEmail\"+i largura=\"51\" maxcaracteres=\"101\"/]\n     [/@grupo]\n     [@grupo]\n      [@texto titulo=\"Função\" var=\"participantesExtraFuncao\"+i largura=\"50\" maxcaracteres=\"101\"/]\n      [@texto titulo=\"Unidade\" var=\"participantesExtraUnidade\"+i largura=\"51\" maxcaracteres=\"101\"/]\n     [/@grupo]\n    [/#list]\n   [/#if]\n  [/@grupo]\n [/@grupo]\n [@separador /]\n [@grupo]\n  [@selecao titulo=\"Quantidade de itens da pauta\" var=\"qtdItePauta\" reler=true idAjax=\"qtdItePautaAjax\" opcoes=\"1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n [/@grupo]\n [@grupo depende=\"qtdItePautaAjax\"]\n  [#list 1..(qtdItePauta)?number as i]\n   [@grupo]\n    [@texto titulo=\"<b>Item ${i}</b>\" var=\"itePauta\"+i largura=\"96\" maxcaracteres=\"101\"/]\n   [/@grupo]\n   [@memo titulo=\"Comentários\" var=\"comentario\"+i colunas=\"78\" linhas=\"2\"/]\n   [@grupo]\n    [@selecao titulo=\"Quantidade de ações\" var=\"qtdAcoes\"+i reler=true idAjax=\"qtdAcoesAjax\"+i opcoes=\"0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15\"/]\n   [/@grupo]\n   [@grupo depende=\"qtdAcoesAjax\"+i]\n    [#if (.vars[\'qtdAcoes\'+i])! != \'0\']\n     [#list 1..(.vars[\'qtdAcoes\'+i])?number as j]\n      [@grupo]      \n       [@texto titulo=\"Ação ${j}\" var=\"acoes\"+i+j largura=\"95\" maxcaracteres=\"75\"/]\n      [/@grupo]\n      [@grupo]\n       [@texto titulo=\"Responsável\" var=\"responsavel\"+i+j largura=\"61\" maxcaracteres=\"55\"/]\n       [@data titulo=\"Data prevista\" var=\"datPrevista\"+i+j/] \n      [/@grupo]\n     [/#list]\n    [/#if]\n   [/@grupo]\n   [@separador /]  \n  [/#list] \n [/@grupo]\n[/@entrevista]\n\n[@documento]\n [#if tamanhoLetra! == \"Normal\"]\n		[#assign tl = \"11pt\" /]\n [#elseif tamanhoLetra! == \"Pequeno\"]\n		[#assign tl = \"9pt\" /]\n [#elseif tamanhoLetra! == \"Grande\"]\n		[#assign tl = \"13pt\" /]\n [#else]		\n		[#assign tl = \"11pt\"]\n [/#if]\n [@estiloBrasaoCentralizado tipo=\"MEMÓRIA DE REUNIÃO\" tamanhoLetra=tl formatarOrgao=true]\n   <p align=\"left\">\n    <b>Objetivo da reunião:</b>&nbsp;${objReuniao!}<br/>\n    Horário e local: ${horReuniao!} - ${locReuniao!}<br/>\n     Assunto: ${(doc.exClassificacao.descrClassificacao)!}</p><br/>\n[#--\n   [#if pendencias! != \"\"]\n    <p><b>Pendências (reuniões anteriores):</b>&nbsp;${pendencias!}</p><br/>\n   [/#if]\n--]\n   <table width=\"100%\" border=\"1\" cellpadding=\"5\">\n    <tr>\n     <td width=\"50%\"><b>Participantes</b></td>\n     <td width=\"25%\"><b>Função</b></td>\n     <td width=\"25%\"><b>Unidade</b></td>\n    </tr>\n    [#list 1..(numParticipantes)?number as i]\n     [#if .vars[\'participantes\' + i + \'_pessoaSel.id\']?? && .vars[\'participantes\' + i + \'_pessoaSel.id\'] != \"\"]\n      [#assign participante = func.pessoa(.vars[\'participantes\' + i + \'_pessoaSel.id\']?number) /]\n      <tr>\n       <td>${func.maiusculasEMinusculas(participante.descricao)}</td>\n       <td>${(participante.funcaoConfianca.descricao)!}</td>\n       <td>${participante.lotacao.sigla}</td>\n      </tr>\n     [/#if]\n    [/#list]\n\n    [#list 1..(numParticipantesExtra)?number as i] \n     [#if numParticipantesExtra! != \'0\']\n      <tr>\n       <td>${.vars[\'participantesExtra\'+i]!}\n        [#if .vars[\'participantesExtraEmail\'+i]??] (${.vars[\'participantesExtraEmail\'+i]!})[/#if]</td>\n       <td>${.vars[\'participantesExtraFuncao\'+i]!}</td>\n       <td>${.vars[\'participantesExtraUnidade\'+i]!}</td>\n      </tr>\n     [/#if]    \n    [/#list]\n   </table> \n   <br/>\n\n\n  \n   <p><b>Pauta</b></p>\n     [#list 1..(qtdItePauta)?number as i] \n        <p>\n          <b>${i}. ${.vars[\'itePauta\'+i]!}:</b> ${.vars[\'comentario\'+i]!}\n        </p>       \n     [/#list]  \n\n\n  <br/> \n\n  [#assign fAcoes = false/]\n  [#list 1..(qtdItePauta)?number as i]\n   [#if .vars[\'qtdAcoes\'+i] != \'0\']\n    [#assign fAcoes = true/]\n   [/#if] \n  [/#list]\n\n  [#if fAcoes]\n   <table width=\"100%\" border=\"1\" cellpadding=\"5\">\n     <tr>\n      <td width=\"10%\"><b>Ref.</b></td>\n      <td width=\"50%\"><b>Próximas Ações</b></td>\n      <td width=\"20%\"><b>Responsável</b></td>\n      <td width=\"20%\" align=\"center\"><b>Data Prevista</b></td>\n     </tr>\n     [#list 1..(qtdItePauta)?number as i]\n      [#if .vars[\'qtdAcoes\'+i] != \'0\']\n       [#list 1..(.vars[\'qtdAcoes\'+i])?number as j]\n        <tr>\n         <td>${i}.${j}</td>\n         <td>${.vars[\'acoes\'+i+j]!}</td>\n         <td>${.vars[\'responsavel\'+i+j]!}</td>\n         <td align=\"center\">${.vars[\'datPrevista\'+i+j]!}</td>\n        </tr>\n       [/#list]\n      [/#if]\n     [/#list]\n   </table>   \n  [/#if]\n [/@estiloBrasaoCentralizado]\n[/@documento]\n\n','template/freemarker',NULL,NULL,97,NULL,NULL,663,NULL,'2020-02-12 14:52:59',NULL,NULL,0,NULL,NULL),(665,'Despacho Automático','Despacho gerado automaticamente pela transferência','\n[@documento margemDireita=\"3cm\"]\n    [#assign tl=\"11pt\"/]\n    [@estiloBrasaoCentralizado tipo=\"DESPACHO\" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n          Referência: ${doc.codigo} de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}[#if doc.lotaTitular??] - ${(doc.lotaTitular.descricao)!}[/#if].<br/>\n            Assunto: ${(doc.exClassificacao.descrClassificacao)!}\n        </div>\n\n        <div style=\"font-family: Arial; font-size: ${tl};\">\n            [#if mov.lotaResp?? && (mov.lotaResp.idLotacaoIni != mov.lotaCadastrante.idLotacaoIni)]\n                <p style=\"TEXT-INDENT: 0cm\">\n                  <span style=\"font-size: ${tl}\">À ${(mov.lotaResp.descricao)!},</span>\n                </p>\n            [/#if]\n            [#if despachoTexto??]\n                <p style=\"TEXT-INDENT: 2cm\"><span style=\"font-size: ${tl}\">${despachoTexto}</span></p>\n            [/#if]\n            ${despachoHtml!}\n        </div>\n    [/@estiloBrasaoCentralizado]\n[/@documento]\n\n','template/freemarker',NULL,NULL,8,NULL,NULL,665,NULL,NULL,NULL,NULL,1,NULL,NULL),(666,'Planta','Planta',' ','template/freemarker',NULL,NULL,99,NULL,NULL,0,NULL,NULL,NULL,NULL,1,NULL,NULL);
/*!40000 ALTER TABLE `ex_modelo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_modelo_tp_doc_publicacao`
--

DROP TABLE IF EXISTS `ex_modelo_tp_doc_publicacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_modelo_tp_doc_publicacao` (
  `ID_MOD` bigint(20) NOT NULL,
  `ID_DOC_PUBLICACAO` double NOT NULL,
  PRIMARY KEY (`ID_MOD`,`ID_DOC_PUBLICACAO`),
  KEY `MOD_PUBL_ID_DOC_PUBLICACAO_FK` (`ID_DOC_PUBLICACAO`),
  CONSTRAINT `MOD_PUBL_ID_DOC_PUBLICACAO_FK` FOREIGN KEY (`ID_DOC_PUBLICACAO`) REFERENCES `ex_tp_doc_publicacao` (`ID_DOC_PUBLICACAO`),
  CONSTRAINT `MOD_PUBL_ID_MOD_FK` FOREIGN KEY (`ID_MOD`) REFERENCES `ex_modelo` (`ID_MOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_modelo_tp_doc_publicacao`
--

LOCK TABLES `ex_modelo_tp_doc_publicacao` WRITE;
/*!40000 ALTER TABLE `ex_modelo_tp_doc_publicacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_modelo_tp_doc_publicacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_movimentacao`
--

DROP TABLE IF EXISTS `ex_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_movimentacao` (
  `ID_MOV` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_DOC` double DEFAULT NULL,
  `ID_DOC_PAI` double DEFAULT NULL,
  `ID_TP_MOV` bigint(20) NOT NULL,
  `ID_ESTADO_DOC` bigint(20) DEFAULT NULL,
  `ID_TP_DESPACHO` bigint(20) DEFAULT NULL,
  `ID_CADASTRANTE` double DEFAULT NULL,
  `ID_LOTA_CADASTRANTE` int(11) DEFAULT NULL,
  `ID_SUBSCRITOR` double DEFAULT NULL,
  `ID_LOTA_SUBSCRITOR` int(11) DEFAULT NULL,
  `DT_MOV` datetime NOT NULL,
  `DT_INI_MOV` datetime NOT NULL,
  `NUM_VIA` tinyint(4) DEFAULT NULL,
  `CONTEUDO_BLOB_MOV` blob,
  `ID_MOV_CANCELADORA` bigint(20) DEFAULT NULL,
  `NM_ARQ_MOV` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTEUDO_TP_MOV` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DT_FIM_MOV` datetime DEFAULT NULL,
  `ID_LOTA_RESP` int(11) DEFAULT NULL,
  `ID_RESP` double DEFAULT NULL,
  `DESCR_MOV` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ASSINATURA_BLOB_MOV` blob,
  `ID_DESTINO_FINAL` double DEFAULT NULL,
  `ID_LOTA_DESTINO_FINAL` int(11) DEFAULT NULL,
  `NUM_VIA_DOC_PAI` tinyint(4) DEFAULT NULL,
  `ID_DOC_REF` double DEFAULT NULL,
  `NUM_VIA_DOC_REF` tinyint(4) DEFAULT NULL,
  `OBS_ORGAO_MOV` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_ORGAO` bigint(20) DEFAULT NULL,
  `ID_MOV_REF` bigint(20) DEFAULT NULL,
  `ID_LOTA_TITULAR` int(11) DEFAULT NULL,
  `ID_TITULAR` double DEFAULT NULL,
  `NM_FUNCAO_SUBSCRITOR` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NUM_PROC_ADM` double DEFAULT NULL,
  `ID_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  `DT_DISP_PUBLICACAO` datetime DEFAULT NULL,
  `DT_EFETIVA_PUBLICACAO` datetime DEFAULT NULL,
  `DT_EFETIVA_DISP_PUBLICACAO` datetime DEFAULT NULL,
  `PAG_PUBLICACAO` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NUM_TRF_PUBLICACAO` bigint(20) DEFAULT NULL,
  `CADERNO_PUBLICACAO_DJE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_MOBIL` double DEFAULT NULL,
  `ID_MOB_REF` double DEFAULT NULL,
  `NUM_PAGINAS` smallint(6) DEFAULT NULL,
  `NUM_PAGINAS_ORI` smallint(6) DEFAULT NULL,
  `ID_PAPEL` double DEFAULT NULL,
  `ID_CLASSIFICACAO` bigint(20) DEFAULT NULL,
  `ID_MARCADOR` double DEFAULT NULL,
  `ID_IDENTIDADE_AUDIT` double DEFAULT NULL,
  `IP_AUDIT` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HASH_AUDIT` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_MOV`),
  KEY `ID_MOBIL_REF_IDX` (`ID_MOB_REF`),
  KEY `EX_MOVIMENTACAO_IDX_001` (`ID_MOV_REF`,`DT_INI_MOV`),
  KEY `ID_MOBIL_IDX` (`ID_MOBIL`),
  KEY `ID_MOV_REF_IDX` (`ID_MOV_REF`),
  KEY `MOVIMENTACAO_LOTA_RESP_E_DATA` (`ID_LOTA_RESP`,`DT_INI_MOV`),
  KEY `IXCF_MOV_DP_LOTA_SIN_CAD_FK` (`ID_LOTA_CADASTRANTE`),
  KEY `IXCF_MOV_DP_LOTA_SIN_SUB_FK` (`ID_LOTA_SUBSCRITOR`),
  KEY `IXCF_MOV_DP_PESSOA_SIN_CAD_FK` (`ID_CADASTRANTE`),
  KEY `IXCF_MOV_DP_PESSOA_SIN_SUB_FK` (`ID_SUBSCRITOR`),
  KEY `IXCF_MOV_ORGAO_FK` (`ID_ORGAO`),
  KEY `IXCF_MOV_NIVEL_ACESSO_FK` (`ID_NIVEL_ACESSO`),
  KEY `IXCF_MOV_MOV_CANCELADA_FK` (`ID_MOV_CANCELADORA`),
  KEY `IXCF_MOV_DEST_FIM_LOTA_FK` (`ID_LOTA_DESTINO_FINAL`),
  KEY `IXCF_MOV_DEST_FIM_PESSOA_FK` (`ID_DESTINO_FINAL`),
  KEY `IXCF_MOV_DOC_FK` (`ID_DOC`),
  KEY `IXCF_MOV_DOC_PAI_FK` (`ID_DOC_PAI`),
  KEY `IXCF_MOV_DOC_REF_FK` (`ID_DOC_REF`),
  KEY `IXCF_MOV_ESTADO_DOC_FK` (`ID_ESTADO_DOC`),
  KEY `IXCF_MOV_LOTA_TIT_LOTA_FK` (`ID_LOTA_TITULAR`),
  KEY `IXCF_MOV_PESSOA_RESP_FK` (`ID_RESP`),
  KEY `IXCF_MOV_TITULAR_PESSOA_FK` (`ID_TITULAR`),
  KEY `IXCF_MOV_TP_DESPACHO_FK` (`ID_TP_DESPACHO`),
  KEY `IXCF_MOV_TP_MOV_FK` (`ID_TP_MOV`),
  KEY `IXCF_SYS_C009983` (`ID_PAPEL`),
  KEY `MOV_EX_CLASSIFICACAO_FK` (`ID_CLASSIFICACAO`),
  KEY `MOV_CP_IDENTIDADE_FK` (`ID_IDENTIDADE_AUDIT`),
  CONSTRAINT `MOVIMENTACAO_ESTADO_DOC_FK` FOREIGN KEY (`ID_ESTADO_DOC`) REFERENCES `ex_estado_doc` (`ID_ESTADO_DOC`),
  CONSTRAINT `MOV_CP_IDENTIDADE_FK` FOREIGN KEY (`ID_IDENTIDADE_AUDIT`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `MOV_DEST_FIM_LOTA_FK` FOREIGN KEY (`ID_LOTA_DESTINO_FINAL`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `MOV_DEST_FIM_PESSOA_FK` FOREIGN KEY (`ID_DESTINO_FINAL`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `MOV_DOC_FK` FOREIGN KEY (`ID_DOC`) REFERENCES `ex_documento` (`ID_DOC`),
  CONSTRAINT `MOV_DOC_PAI_FK` FOREIGN KEY (`ID_DOC_PAI`) REFERENCES `ex_documento` (`ID_DOC`),
  CONSTRAINT `MOV_DOC_REF_FK` FOREIGN KEY (`ID_DOC_REF`) REFERENCES `ex_documento` (`ID_DOC`),
  CONSTRAINT `MOV_DP_LOTA_SIN_CAD_FK` FOREIGN KEY (`ID_LOTA_CADASTRANTE`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `MOV_DP_LOTA_SIN_SUB_FK` FOREIGN KEY (`ID_LOTA_SUBSCRITOR`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `MOV_DP_PESSOA_SIN_CAD_FK` FOREIGN KEY (`ID_CADASTRANTE`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `MOV_DP_PESSOA_SIN_SUB_FK` FOREIGN KEY (`ID_SUBSCRITOR`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `MOV_EX_CLASSIFICACAO_FK` FOREIGN KEY (`ID_CLASSIFICACAO`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `MOV_LOTA_RESP_FK` FOREIGN KEY (`ID_LOTA_RESP`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `MOV_LOTA_TITULAR_LOTACAO_FK` FOREIGN KEY (`ID_LOTA_TITULAR`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `MOV_MOV_CANCELADA_FK` FOREIGN KEY (`ID_MOV_CANCELADORA`) REFERENCES `ex_movimentacao` (`ID_MOV`),
  CONSTRAINT `MOV_NIVEL_ACESSO_FK` FOREIGN KEY (`ID_NIVEL_ACESSO`) REFERENCES `ex_nivel_acesso` (`ID_NIVEL_ACESSO`),
  CONSTRAINT `MOV_ORGAO_FK` FOREIGN KEY (`ID_ORGAO`) REFERENCES `corporativo`.`cp_orgao` (`ID_ORGAO`),
  CONSTRAINT `MOV_PESSOA_RESP_FK` FOREIGN KEY (`ID_RESP`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `MOV_REF_MOV_FK` FOREIGN KEY (`ID_MOV_REF`) REFERENCES `ex_movimentacao` (`ID_MOV`),
  CONSTRAINT `MOV_TITULAR_PESSOA_FK` FOREIGN KEY (`ID_TITULAR`) REFERENCES `corporativo`.`dp_pessoa` (`ID_PESSOA`),
  CONSTRAINT `MOV_TP_DESPACHO_FK` FOREIGN KEY (`ID_TP_DESPACHO`) REFERENCES `ex_tipo_despacho` (`ID_TP_DESPACHO`),
  CONSTRAINT `MOV_TP_MOV_FK` FOREIGN KEY (`ID_TP_MOV`) REFERENCES `ex_tipo_movimentacao` (`ID_TP_MOV`),
  CONSTRAINT `ex_movimentacao_ibfk_1` FOREIGN KEY (`ID_MOBIL`) REFERENCES `ex_mobil` (`ID_MOBIL`),
  CONSTRAINT `ex_movimentacao_ibfk_2` FOREIGN KEY (`ID_MOB_REF`) REFERENCES `ex_mobil` (`ID_MOBIL`),
  CONSTRAINT `ex_movimentacao_ibfk_3` FOREIGN KEY (`ID_PAPEL`) REFERENCES `ex_papel` (`ID_PAPEL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_movimentacao`
--

LOCK TABLES `ex_movimentacao` WRITE;
/*!40000 ALTER TABLE `ex_movimentacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_movimentacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_nivel_acesso`
--

DROP TABLE IF EXISTS `ex_nivel_acesso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_nivel_acesso` (
  `ID_NIVEL_ACESSO` bigint(20) NOT NULL AUTO_INCREMENT,
  `NM_NIVEL_ACESSO` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `DSC_NIVEL_ACESSO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GRAU_NIVEL_ACESSO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID_NIVEL_ACESSO`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_nivel_acesso`
--

LOCK TABLES `ex_nivel_acesso` WRITE;
/*!40000 ALTER TABLE `ex_nivel_acesso` DISABLE KEYS */;
INSERT INTO `ex_nivel_acesso` VALUES (1,'Limitado ao órgão (padrão)','Dá acesso a todos do órgão ao qual o documento pertence, bem como a toda a lotação de qualquer órgão para onde for enviado',20),(2,'Limitado de divisão para pessoa','Dá acesso a toda a divisão onde o documento foi criado e também a qualquer lotação (ou apenas pessoa específica) para onde for enviado',40),(3,'Limitado entre lotações','Dá acesso a toda a lotação do cadastrante do documento e à lotação (nunca somente à pessoa) para onde for enviado',60),(5,'Limitado entre pessoas','Dá acesso somente ao cadastrante do documento e à lotação (ou apenas pessoa específica) para onde for enviado',100),(6,'Público','Dá acesso a todos, independentemente do órgão',10),(7,'Limitado de pessoa para divisão','Dá acesso somente ao cadastrante do documento e a toda a divisão para onde for enviado',30);
/*!40000 ALTER TABLE `ex_nivel_acesso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_numeracao`
--

DROP TABLE IF EXISTS `ex_numeracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_numeracao` (
  `ID_ORGAO_USU` bigint(20) NOT NULL,
  `ID_FORMA_DOC` bigint(20) NOT NULL,
  `ANO_EMISSAO` bigint(20) NOT NULL,
  `NUM_EXPEDIENTE` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_ORGAO_USU`,`ID_FORMA_DOC`,`ANO_EMISSAO`),
  KEY `NUMERACAO_FORMA_DOC_FK` (`ID_FORMA_DOC`),
  CONSTRAINT `NUMERACAO_FORMA_DOC_FK` FOREIGN KEY (`ID_FORMA_DOC`) REFERENCES `ex_forma_documento` (`ID_FORMA_DOC`),
  CONSTRAINT `NUMERACAO_ORGAO_USU_FK` FOREIGN KEY (`ID_ORGAO_USU`) REFERENCES `corporativo`.`cp_orgao_usuario` (`ID_ORGAO_USU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_numeracao`
--

LOCK TABLES `ex_numeracao` WRITE;
/*!40000 ALTER TABLE `ex_numeracao` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_numeracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_papel`
--

DROP TABLE IF EXISTS `ex_papel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_papel` (
  `ID_PAPEL` double NOT NULL AUTO_INCREMENT,
  `DESC_PAPEL` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_PAPEL`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_papel`
--

LOCK TABLES `ex_papel` WRITE;
/*!40000 ALTER TABLE `ex_papel` DISABLE KEYS */;
INSERT INTO `ex_papel` VALUES (1,'Gestor'),(2,'Interessado'),(5,'Liquidante'),(6,'Autorizador'),(7,'Revisor');
/*!40000 ALTER TABLE `ex_papel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_preenchimento`
--

DROP TABLE IF EXISTS `ex_preenchimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_preenchimento` (
  `ID_PREENCHIMENTO` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_LOTACAO` int(11) NOT NULL,
  `ID_MOD` bigint(20) NOT NULL,
  `EX_NOME_PREENCHIMENTO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PREENCHIMENTO_BLOB` blob,
  PRIMARY KEY (`ID_PREENCHIMENTO`),
  KEY `EX_PREENCHIMENTO_IDX_013` (`ID_MOD`),
  KEY `EX_PREENCHIMENTO_IDX_012` (`ID_LOTACAO`),
  CONSTRAINT `PREENCHIMENTO_LOTACAO_FK` FOREIGN KEY (`ID_LOTACAO`) REFERENCES `corporativo`.`dp_lotacao` (`ID_LOTACAO`),
  CONSTRAINT `PREENCHIMENTO_MODELO_FK` FOREIGN KEY (`ID_MOD`) REFERENCES `ex_modelo` (`ID_MOD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_preenchimento`
--

LOCK TABLES `ex_preenchimento` WRITE;
/*!40000 ALTER TABLE `ex_preenchimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ex_preenchimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_situacao_configuracao`
--

DROP TABLE IF EXISTS `ex_situacao_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_situacao_configuracao` (
  `ID_SIT_CONFIGURACAO` double NOT NULL AUTO_INCREMENT,
  `DSC_SIT_CONFIGURACAO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_SIT_CONFIGURACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_situacao_configuracao`
--

LOCK TABLES `ex_situacao_configuracao` WRITE;
/*!40000 ALTER TABLE `ex_situacao_configuracao` DISABLE KEYS */;
INSERT INTO `ex_situacao_configuracao` VALUES (1,'Pode'),(2,'Não Pode'),(3,'Obrigatório'),(4,'Opcional'),(5,'Default'),(6,'Não default'),(7,'Proibido');
/*!40000 ALTER TABLE `ex_situacao_configuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_temporalidade`
--

DROP TABLE IF EXISTS `ex_temporalidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_temporalidade` (
  `ID_TEMPORALIDADE` smallint(6) NOT NULL AUTO_INCREMENT,
  `DESC_TEMPORALIDADE` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `VALOR_TEMPORALIDADE` double DEFAULT NULL,
  `ID_UNIDADE_MEDIDA` double DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_INI` double DEFAULT NULL,
  `HIS_IDC_FIM` double DEFAULT NULL,
  `HIS_ATIVO` double NOT NULL,
  PRIMARY KEY (`ID_TEMPORALIDADE`),
  KEY `ID_UNIDADE_MEDIDA` (`ID_UNIDADE_MEDIDA`),
  KEY `HIS_IDC_INI` (`HIS_IDC_INI`),
  KEY `HIS_IDC_FIM` (`HIS_IDC_FIM`),
  CONSTRAINT `ex_temporalidade_ibfk_1` FOREIGN KEY (`ID_UNIDADE_MEDIDA`) REFERENCES `corporativo`.`cp_unidade_medida` (`ID_UNIDADE_MEDIDA`),
  CONSTRAINT `ex_temporalidade_ibfk_2` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_temporalidade_ibfk_3` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_temporalidade`
--

LOCK TABLES `ex_temporalidade` WRITE;
/*!40000 ALTER TABLE `ex_temporalidade` DISABLE KEYS */;
INSERT INTO `ex_temporalidade` VALUES (81,'1 ano',1,1,81,NULL,NULL,NULL,NULL,1),(82,'1 ano após a atualização',1,1,82,NULL,NULL,NULL,NULL,1),(83,'10 anos',10,1,83,NULL,NULL,NULL,NULL,1),(84,'15 anos',15,1,84,NULL,NULL,NULL,NULL,1),(85,'2 anos',2,1,85,NULL,NULL,NULL,NULL,1),(86,'2 anos após a devolução do documento / processo',2,1,86,NULL,NULL,NULL,NULL,1),(87,'2 anos após o encerramento',2,1,87,NULL,NULL,NULL,NULL,1),(88,'2 anos após o encerramento com devolução',2,1,88,NULL,NULL,NULL,NULL,1),(89,'20 anos',20,1,89,NULL,NULL,NULL,NULL,1),(90,'3 anos',3,1,90,NULL,NULL,NULL,NULL,1),(92,'3 anos após o encerramento',3,1,92,NULL,NULL,NULL,NULL,1),(93,'30 dias',30,3,93,NULL,NULL,NULL,NULL,1),(94,'35 anos',35,1,94,NULL,NULL,NULL,NULL,1),(95,'4 anos',4,1,95,NULL,NULL,NULL,NULL,1),(96,'5 anos',5,1,96,NULL,NULL,NULL,NULL,1),(97,'5 anos após o encerramento',5,1,97,NULL,NULL,NULL,NULL,1),(98,'50 anos',50,1,98,NULL,NULL,NULL,NULL,1),(99,'51 anos',51,1,99,NULL,NULL,NULL,NULL,1),(100,'6 anos',6,1,100,NULL,NULL,NULL,NULL,1),(101,'7 anos',7,1,101,NULL,NULL,NULL,NULL,1),(102,'71 anos',71,1,102,NULL,NULL,NULL,NULL,1),(103,'90 dias',90,3,103,NULL,NULL,NULL,NULL,1),(104,'95 anos',95,1,104,NULL,NULL,NULL,NULL,1),(105,'Até a alienação',NULL,NULL,105,NULL,NULL,NULL,NULL,1),(106,'Até a aposentadoria ou o desligamento',NULL,NULL,106,NULL,NULL,NULL,NULL,1),(107,'Até a atualização',NULL,NULL,107,NULL,NULL,NULL,NULL,1),(108,'Até a conclusão da apuração',NULL,NULL,108,NULL,NULL,NULL,NULL,1),(109,'Até a devolução',NULL,NULL,109,NULL,NULL,NULL,NULL,1),(110,'Até a inclusão',NULL,NULL,110,NULL,NULL,NULL,NULL,1),(111,'Até a informatização ou alienação',NULL,NULL,111,NULL,NULL,NULL,NULL,1),(112,'Até a prestação de contas',NULL,NULL,112,NULL,NULL,NULL,NULL,1),(113,'Até a quitação da dívida',NULL,NULL,113,NULL,NULL,NULL,NULL,1),(114,'Até a restauração da obra',NULL,NULL,114,NULL,NULL,NULL,NULL,1),(115,'Até alienação do bem',NULL,NULL,115,NULL,NULL,NULL,NULL,1),(116,'Até o emplacamento',NULL,NULL,116,NULL,NULL,NULL,NULL,1),(117,'Até o encerramento',NULL,NULL,117,NULL,NULL,NULL,NULL,1),(118,'Até o pagamento',NULL,NULL,118,NULL,NULL,NULL,NULL,1),(119,'Até o trânsito em julgado',NULL,NULL,119,NULL,NULL,NULL,NULL,1),(120,'Até vigência',NULL,NULL,120,NULL,NULL,NULL,NULL,1),(121,'Até vigência + 6 anos',6,1,121,NULL,NULL,NULL,NULL,1),(122,'Duração obra',NULL,NULL,122,NULL,NULL,NULL,NULL,1),(123,'Durante vigência',NULL,NULL,123,NULL,NULL,NULL,NULL,1),(124,'Enquanto permanece a ocupação',NULL,NULL,124,NULL,NULL,NULL,NULL,1),(125,'Enquanto vigora',NULL,NULL,125,NULL,NULL,NULL,NULL,1),(126,'Guarda Permanente',NULL,NULL,126,NULL,NULL,NULL,NULL,1),(127,'Indeterminado',NULL,NULL,127,NULL,NULL,NULL,NULL,1),(128,'Julg. TCU',NULL,NULL,128,NULL,NULL,NULL,NULL,1),(129,'No momento do recolhimento',NULL,NULL,129,NULL,NULL,NULL,NULL,1),(130,'Para utilização',NULL,NULL,130,NULL,NULL,NULL,NULL,1),(131,'Prazo da pasta',NULL,NULL,131,NULL,NULL,NULL,NULL,1),(132,'Prazo do assent.',NULL,NULL,132,NULL,NULL,NULL,NULL,1),(133,'Prazo do dossiê',NULL,NULL,133,NULL,NULL,NULL,NULL,1),(134,'Prazo do precatório',NULL,NULL,134,NULL,NULL,NULL,NULL,1),(135,'Prazo do processo',NULL,NULL,135,NULL,NULL,NULL,NULL,1),(136,'Prazo do prontuário',NULL,NULL,136,NULL,NULL,NULL,NULL,1),(137,'Transitado em julgado',NULL,NULL,137,NULL,NULL,NULL,NULL,1),(138,'Validade Concurso',NULL,NULL,138,NULL,NULL,NULL,NULL,1),(139,'Validade contrato',NULL,NULL,139,NULL,NULL,NULL,NULL,1),(140,'Validade credenciamento',NULL,NULL,140,NULL,NULL,NULL,NULL,1),(141,'Validade Projeto',NULL,NULL,141,NULL,NULL,NULL,NULL,1),(142,'Vigência',NULL,NULL,142,NULL,NULL,NULL,NULL,1),(143,'Vigência cadastro',NULL,NULL,143,NULL,NULL,NULL,NULL,1),(144,'Vigência da pensão',NULL,NULL,144,NULL,NULL,NULL,NULL,1),(161,'Até a devolução do bem',NULL,NULL,161,NULL,NULL,NULL,NULL,1),(162,'Até a posse',NULL,NULL,162,NULL,NULL,NULL,NULL,1),(163,'Até a próxima atualização',NULL,NULL,163,NULL,NULL,NULL,NULL,1),(164,'Até a publicação ',NULL,NULL,164,NULL,NULL,NULL,NULL,1),(165,'Até o desfazimento do bem',NULL,NULL,165,NULL,NULL,NULL,NULL,1),(166,'Até o encerramento do processo de execução penal',NULL,NULL,166,NULL,NULL,NULL,NULL,1),(167,'Até o vitaliciamento',NULL,NULL,167,NULL,NULL,NULL,NULL,1),(168,'Até revogação',NULL,NULL,168,NULL,NULL,NULL,NULL,1),(169,'Até vigência do contrato ou Julg. TCU',NULL,NULL,169,NULL,NULL,NULL,NULL,1),(170,'Eliminação no momento do recebimento',NULL,NULL,170,NULL,NULL,NULL,NULL,1),(171,'Enquanto durar a ocupação',NULL,NULL,171,NULL,NULL,NULL,NULL,1),(172,'Enquanto durar a pesquisa',NULL,NULL,172,NULL,NULL,NULL,NULL,1),(173,'Enquanto o bem estiver alienado',NULL,NULL,173,NULL,NULL,NULL,NULL,1),(174,'Enquanto vigente',NULL,NULL,174,NULL,NULL,NULL,NULL,1),(175,'Imediatamente após a produção',NULL,NULL,175,NULL,NULL,NULL,NULL,1),(176,'Prazo da licença',NULL,NULL,176,NULL,NULL,NULL,NULL,1),(177,'Validade do concurso',NULL,NULL,177,NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `ex_temporalidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_despacho`
--

DROP TABLE IF EXISTS `ex_tipo_despacho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_despacho` (
  `ID_TP_DESPACHO` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESC_TP_DESPACHO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `FG_ATIVO_TP_DESPACHO` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_DESPACHO`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_despacho`
--

LOCK TABLES `ex_tipo_despacho` WRITE;
/*!40000 ALTER TABLE `ex_tipo_despacho` DISABLE KEYS */;
INSERT INTO `ex_tipo_despacho` VALUES (1,'De acordo.','S'),(2,'Para ciência.','S'),(3,'Para as providências cabíveis','S'),(4,'Intime-se.','S'),(5,'Autorizo.','S'),(6,'Para atendimento.','S'),(7,'Para verificar a possibilidade de atendimento.','S'),(8,'Oficie-se.','S'),(9,'Expeça-se ofício-circular.','S'),(10,'Expeça-se memorando.','S'),(11,'Expeça-se memorando-circular.','S'),(12,'Arquive-se.','S'),(13,'Junte-se ao dossiê.','S'),(14,'Junte-se ao processo.','S'),(15,'Ciente. Arquive-se.','S'),(16,'Para atendimento e encaminhamento direto','S'),(17,'A pedido','S');
/*!40000 ALTER TABLE `ex_tipo_despacho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_destinacao`
--

DROP TABLE IF EXISTS `ex_tipo_destinacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_destinacao` (
  `ID_TP_DESTINACAO` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCR_TIPO_DESTINACAO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `FACILITADOR_DEST` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_DESTINACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_destinacao`
--

LOCK TABLES `ex_tipo_destinacao` WRITE;
/*!40000 ALTER TABLE `ex_tipo_destinacao` DISABLE KEYS */;
INSERT INTO `ex_tipo_destinacao` VALUES (1,'Eliminação',NULL),(2,'Guarda Permanente',NULL),(21,'5 anos',NULL),(22,'Agência',NULL),(23,'Arquivo',NULL),(24,'Arquivo Intermediário',NULL),(26,'Arquivo Permanente',NULL),(27,'Assentamento Funcional',NULL),(28,'Banco',NULL),(29,'Biblioteca',NULL),(30,'Candidato',NULL),(31,'Clínica',NULL),(32,'Correios',NULL),(33,'Devolvida',NULL),(34,'Doador',NULL),(35,'Dossiê',NULL),(36,'Dossiê do curso',NULL),(37,'Dossiê do Evento',NULL),(38,'Empresa',NULL),(39,'Estagiário',NULL),(40,'Executor',NULL),(41,'Imprensa',NULL),(42,'Interessado',NULL),(43,'Julg. TCU',NULL),(44,'Magistrado',NULL),(45,'Participante',NULL),(46,'Participante / Instrutor',NULL),(47,'Pasta do Evento',NULL),(48,'Pasta do projeto',NULL),(49,'Precatório',NULL),(50,'Processo',NULL),(51,'Processo de origem',NULL),(52,'Prontuário Médico',NULL),(53,'Receita Federal',NULL),(54,'Remetido',NULL),(55,'Servidor',NULL),(57,'Setor',NULL),(58,'Setor Competente',NULL),(59,'Setores ',NULL),(60,'Setores / Interessados',NULL),(61,'TCU',NULL),(62,'Unidade Geradora',NULL),(81,'Magistrado / Servidor',NULL),(82,'Processo Judicial',NULL);
/*!40000 ALTER TABLE `ex_tipo_destinacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_documento`
--

DROP TABLE IF EXISTS `ex_tipo_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_documento` (
  `ID_TP_DOC` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCR_TIPO_DOCUMENTO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID_TP_DOC`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_documento`
--

LOCK TABLES `ex_tipo_documento` WRITE;
/*!40000 ALTER TABLE `ex_tipo_documento` DISABLE KEYS */;
INSERT INTO `ex_tipo_documento` VALUES (1,'Interno Produzido'),(2,'Interno Folha de Rosto'),(3,'Externo Folha de Rosto'),(4,'Externo Capturado'),(5,'Interno Capturado');
/*!40000 ALTER TABLE `ex_tipo_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_forma_documento`
--

DROP TABLE IF EXISTS `ex_tipo_forma_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_forma_documento` (
  `ID_TIPO_FORMA_DOC` double NOT NULL AUTO_INCREMENT,
  `DESC_TIPO_FORMA_DOC` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NUMERACAO_UNICA` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_TIPO_FORMA_DOC`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_forma_documento`
--

LOCK TABLES `ex_tipo_forma_documento` WRITE;
/*!40000 ALTER TABLE `ex_tipo_forma_documento` DISABLE KEYS */;
INSERT INTO `ex_tipo_forma_documento` VALUES (1,'Expediente',0),(2,'Processo Administrativo',1);
/*!40000 ALTER TABLE `ex_tipo_forma_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_mobil`
--

DROP TABLE IF EXISTS `ex_tipo_mobil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_mobil` (
  `ID_TIPO_MOBIL` double NOT NULL AUTO_INCREMENT,
  `DESC_TIPO_MOBIL` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TIPO_MOBIL`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_mobil`
--

LOCK TABLES `ex_tipo_mobil` WRITE;
/*!40000 ALTER TABLE `ex_tipo_mobil` DISABLE KEYS */;
INSERT INTO `ex_tipo_mobil` VALUES (1,'Geral'),(2,'Via'),(3,'Cópia'),(4,'Volume');
/*!40000 ALTER TABLE `ex_tipo_mobil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tipo_movimentacao`
--

DROP TABLE IF EXISTS `ex_tipo_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tipo_movimentacao` (
  `ID_TP_MOV` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCR_TIPO_MOVIMENTACAO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_TP_MOV`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tipo_movimentacao`
--

LOCK TABLES `ex_tipo_movimentacao` WRITE;
/*!40000 ALTER TABLE `ex_tipo_movimentacao` DISABLE KEYS */;
INSERT INTO `ex_tipo_movimentacao` VALUES (1,'Criação'),(2,'Anexação'),(3,'Transferência'),(4,'Recebimento'),(5,'Despacho'),(6,'Despacho com Transferência'),(7,'Despacho Interno'),(8,'Despacho Interno com Transferência'),(9,'Arquivamento Corrente'),(10,'Eliminação'),(11,'Assinatura'),(12,'Juntada'),(13,'Desentranhamento'),(14,'Cancelamento de Movimentação'),(15,'Extravio'),(16,'Vinculação'),(17,'Transferência Externa'),(18,'Despacho com Transferência Externa'),(19,'Recolhimento ao Arq. Permanente'),(20,'Arquivamento Intermediário'),(21,'Desarquivamento'),(22,'Assinatura de Movimentação'),(23,'Recebimento Transitório'),(24,'Inclusão de Cossignatário'),(25,'Registro de Assinatura'),(26,'Registro de Assinatura de Movimentação'),(27,'Atualização'),(28,'Anotação'),(29,'Redefinição de Sigilo'),(30,'Registro de Acesso Alheio'),(31,'Juntada a Documento Externo'),(32,'Agendamento de Publicação no DJE'),(33,'Remessa para Publicação'),(34,'Confirmação de Remessa Manual'),(35,'Disponibilização'),(36,'Solicitação de Publicação no Boletim'),(37,'Publicação do Boletim'),(38,'Pedido de Publicação no DJE'),(39,'Revolvimento Unidirecional'),(40,'Notificação de Publicação no Boletim'),(41,'Apensação'),(42,'Desapensação'),(43,'Encerramento de Volume'),(44,'Definição de Perfil'),(45,'Autenticação de Documento'),(46,'Sobrestar'),(47,'Desobrestar'),(48,'Tornar sem Efeito'),(49,'Indicação para Guarda Permanente'),(50,'Reversão de Ind. para Guarda Permanente'),(51,'Reclassificação'),(52,'Avaliação'),(53,'Avaliação com Reclassificação'),(54,'Inclusão em Edital de Eliminação'),(55,'Retirada de Edital de Eliminação'),(56,'Desarquivamento Intermediário'),(57,'Pendência de Anexação'),(58,'Assinatura com senha'),(59,'Assinatura de movimentação com senha'),(60,'Autenticação de Documento com senha'),(61,'Controle de Coloboração'),(62,'Marcação'),(63,'Inclusão de Cópia'),(64,'Anexação de Arquivo Auxiliar'),(67,'Autuar');
/*!40000 ALTER TABLE `ex_tipo_movimentacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tp_doc_publicacao`
--

DROP TABLE IF EXISTS `ex_tp_doc_publicacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tp_doc_publicacao` (
  `ID_DOC_PUBLICACAO` double NOT NULL AUTO_INCREMENT,
  `NM_DOC_PUBLICACAO` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CARATER` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_DOC_PUBLICACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tp_doc_publicacao`
--

LOCK TABLES `ex_tp_doc_publicacao` WRITE;
/*!40000 ALTER TABLE `ex_tp_doc_publicacao` DISABLE KEYS */;
INSERT INTO `ex_tp_doc_publicacao` VALUES (1,'Ato Ordinatório','J'),(2,'Decisão','J'),(3,'Despacho','J'),(4,'Sentença','J'),(5,'Edital (Teor Judicial)','J'),(6,'Informação de Secretaria',NULL),(7,'Ordem de Serviço',NULL),(8,'Portaria',NULL),(9,'Acórdão','J'),(18,'Outros','A'),(25,'Aviso','A'),(26,'Aviso de Aditamento','A'),(40,'Aviso de Licitação','A'),(73,'Errata',NULL),(83,'Extrato de convênio','A'),(85,'Extratos de Contratos','A'),(86,'Extrato de dispensa de licitação','A'),(90,'Extrato de inexigibilidade de licitação','A'),(104,'Extrato de Registro de Preços','A'),(106,'Extrato de Rescisão','A'),(107,'Extrato de rescisão contratual','A'),(108,'Extrato de termo aditivo','A'),(147,'Aviso de Pregão','A'),(158,'Ato da Presidência',NULL),(159,'Edital da Presidência',NULL),(160,'Portaria da Presidência',NULL),(161,'Ordem de Serviço da presidência',NULL),(162,'Resolução da Presidência',NULL);
/*!40000 ALTER TABLE `ex_tp_doc_publicacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tp_forma_doc`
--

DROP TABLE IF EXISTS `ex_tp_forma_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tp_forma_doc` (
  `ID_FORMA_DOC` bigint(20) NOT NULL,
  `ID_TP_DOC` bigint(20) NOT NULL,
  KEY `TP_FORMA_DOC_FORMA_DOC_FK` (`ID_FORMA_DOC`),
  KEY `TP_FORMA_DOC_TP_DOC_FK` (`ID_TP_DOC`),
  CONSTRAINT `TP_FORMA_DOC_FORMA_DOC_FK` FOREIGN KEY (`ID_FORMA_DOC`) REFERENCES `ex_forma_documento` (`ID_FORMA_DOC`),
  CONSTRAINT `TP_FORMA_DOC_TP_DOC_FK` FOREIGN KEY (`ID_TP_DOC`) REFERENCES `ex_tipo_documento` (`ID_TP_DOC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tp_forma_doc`
--

LOCK TABLES `ex_tp_forma_doc` WRITE;
/*!40000 ALTER TABLE `ex_tp_forma_doc` DISABLE KEYS */;
INSERT INTO `ex_tp_forma_doc` VALUES (1,1),(2,1),(3,1),(4,1),(5,3),(8,2),(8,1),(9,1),(9,2),(12,1),(12,2),(13,1),(13,2),(14,1),(14,2),(54,1),(55,1),(56,1),(57,1),(97,1),(97,2),(98,1),(98,2),(99,4);
/*!40000 ALTER TABLE `ex_tp_forma_doc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_tp_mov_estado`
--

DROP TABLE IF EXISTS `ex_tp_mov_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_tp_mov_estado` (
  `ID_TP_MOV` bigint(20) NOT NULL,
  `ID_ESTADO_DOC` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_TP_MOV`,`ID_ESTADO_DOC`),
  UNIQUE KEY `TP_MOV_ESTADO_PK` (`ID_ESTADO_DOC`,`ID_TP_MOV`),
  CONSTRAINT `TP_MOV_TPMOV_ESTADO_FK` FOREIGN KEY (`ID_TP_MOV`) REFERENCES `ex_tipo_movimentacao` (`ID_TP_MOV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_tp_mov_estado`
--

LOCK TABLES `ex_tp_mov_estado` WRITE;
/*!40000 ALTER TABLE `ex_tp_mov_estado` DISABLE KEYS */;
INSERT INTO `ex_tp_mov_estado` VALUES (1,1),(3,3),(4,2),(5,2),(6,3),(7,2),(8,3),(9,6),(10,8),(12,9),(13,2),(15,4),(17,11),(18,11);
/*!40000 ALTER TABLE `ex_tp_mov_estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ex_via`
--

DROP TABLE IF EXISTS `ex_via`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ex_via` (
  `ID_VIA` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_CLASSIFICACAO` bigint(20) NOT NULL,
  `ID_DESTINACAO` bigint(20) DEFAULT NULL,
  `COD_VIA` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_TEMPORAL_ARQ_COR` smallint(6) DEFAULT NULL,
  `ID_TEMPORAL_ARQ_INT` smallint(6) DEFAULT NULL,
  `OBS` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ID_DESTINACAO_FINAL` tinyint(4) DEFAULT NULL,
  `HIS_ID_INI` bigint(20) DEFAULT NULL,
  `HIS_DT_INI` datetime DEFAULT NULL,
  `HIS_DT_FIM` datetime DEFAULT NULL,
  `HIS_IDC_INI` double DEFAULT NULL,
  `HIS_IDC_FIM` double DEFAULT NULL,
  `HIS_ATIVO` double NOT NULL,
  PRIMARY KEY (`ID_VIA`),
  KEY `HIS_IDC_INI` (`HIS_IDC_INI`),
  KEY `HIS_IDC_FIM` (`HIS_IDC_FIM`),
  KEY `HIS_ID_INI` (`HIS_ID_INI`),
  KEY `VIA_CLASSIFICACAO_FK` (`ID_CLASSIFICACAO`),
  KEY `VIA_TMP_CORRENTE_FK` (`ID_TEMPORAL_ARQ_COR`),
  KEY `VIA_TMP_INTERMEDIARIO_FK` (`ID_TEMPORAL_ARQ_INT`),
  KEY `VIA_TP_DESTINACAO_FK` (`ID_DESTINACAO`),
  CONSTRAINT `VIA_CLASSIFICACAO_FK` FOREIGN KEY (`ID_CLASSIFICACAO`) REFERENCES `ex_classificacao` (`ID_CLASSIFICACAO`),
  CONSTRAINT `VIA_TMP_CORRENTE_FK` FOREIGN KEY (`ID_TEMPORAL_ARQ_COR`) REFERENCES `ex_temporalidade` (`ID_TEMPORALIDADE`),
  CONSTRAINT `VIA_TMP_INTERMEDIARIO_FK` FOREIGN KEY (`ID_TEMPORAL_ARQ_INT`) REFERENCES `ex_temporalidade` (`ID_TEMPORALIDADE`),
  CONSTRAINT `VIA_TP_DESTINACAO_FK` FOREIGN KEY (`ID_DESTINACAO`) REFERENCES `ex_tipo_destinacao` (`ID_TP_DESTINACAO`),
  CONSTRAINT `ex_via_ibfk_1` FOREIGN KEY (`HIS_IDC_INI`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_via_ibfk_2` FOREIGN KEY (`HIS_IDC_FIM`) REFERENCES `corporativo`.`cp_identidade` (`ID_IDENTIDADE`),
  CONSTRAINT `ex_via_ibfk_3` FOREIGN KEY (`HIS_ID_INI`) REFERENCES `ex_via` (`ID_VIA`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ex_via`
--

LOCK TABLES `ex_via` WRITE;
/*!40000 ALTER TABLE `ex_via` DISABLE KEYS */;
INSERT INTO `ex_via` VALUES (1,4,58,'1',85,NULL,'-',1,1,'2019-03-13 00:00:00',NULL,NULL,NULL,1),(2,7,58,'1',85,NULL,'-',1,2,'2009-03-13 00:00:00',NULL,NULL,NULL,1),(3,8,58,'1',85,NULL,'-',1,3,'2009-03-13 00:00:00',NULL,NULL,NULL,1),(4,11,58,'1',85,NULL,'-',1,4,'2009-03-13 00:00:00',NULL,NULL,NULL,1),(5,12,58,'1',85,NULL,'-',1,5,'2009-03-13 00:00:00',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `ex_via` ENABLE KEYS */;
UNLOCK TABLES;

drop function if exists remove_acento;
delimiter //
create function remove_acento( textvalue varchar(20000) )
returns varchar(20000) DETERMINISTIC
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
-- Dumping routines for database 'siga'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-12 13:05:54
