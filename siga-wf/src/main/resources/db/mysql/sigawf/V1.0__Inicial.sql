CREATE DATABASE  IF NOT EXISTS `sigawf` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sigawf`;
-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: sigawf
-- ------------------------------------------------------
-- Server version	8.0.19

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
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_def_desvio` (
  `DEFD_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da definição de desvio',
  `DEFD_TX_CONDICAO` varchar(256) DEFAULT NULL COMMENT 'Fórmula matemática para habilitar a definição de desvio',
  `DEFD_NM` varchar(256) DEFAULT NULL COMMENT 'Nome da definição de desvio',
  `DEFD_NR_ORDEM` int DEFAULT NULL COMMENT 'Número para ordenação da definição de desvio',
  `DEFD_FG_ULTIMO` bit(1) DEFAULT NULL COMMENT 'Indica se essa é a última definição de desvio',
  `DEFT_ID` bigint DEFAULT NULL COMMENT 'Identificador da definição de tarefa ligada a definição de desvio',
  `DEFT_ID_SEGUINTE` bigint DEFAULT NULL COMMENT 'Identificador da definição de tarefa alvo desta definição de desvio',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da definição de desvio',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da definição de desvio',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da definição de desvio',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativa a definição de desvio',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou a definição de desvio',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou a definição de desvio',
  PRIMARY KEY (`DEFD_ID`),
  KEY `FK_DEFD_DEFT_ID` (`DEFT_ID`),
  KEY `FK_DEFD_DEFT_ID_SEGUINTE` (`DEFT_ID_SEGUINTE`),
  CONSTRAINT `FK_DEFD_DEFT_ID` FOREIGN KEY (`DEFT_ID`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK_DEFD_DEFT_ID_SEGUINTE` FOREIGN KEY (`DEFT_ID_SEGUINTE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_def_desvio`
--

LOCK TABLES `wf_def_desvio` WRITE;
/*!40000 ALTER TABLE `wf_def_desvio` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_def_desvio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_def_procedimento`
--

DROP TABLE IF EXISTS `wf_def_procedimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_def_procedimento` (
  `DEFP_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da definição de procedimento',
  `ORGU_ID` bigint DEFAULT NULL COMMENT 'Identificador do órgao usuário ao qual se vincula a definição de procedimento',
  `DEFP_ANO` int DEFAULT NULL COMMENT 'Ano da definição de procedimento',
  `DEFP_DS` varchar(256) NOT NULL COMMENT 'Descrição da definição de procedimento',
  `DEFP_NM` varchar(256) NOT NULL COMMENT 'Nome da definição de procedimento',
  `DEFP_NR` int DEFAULT NULL COMMENT 'Número da definição de procedimento',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da definição de procedimento',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativa a definição de procedimento',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da definição de procedimento',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da definição de procedimento',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou a definição de procedimento',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou a definição de procedimento',
  PRIMARY KEY (`DEFP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_def_procedimento`
--

LOCK TABLES `wf_def_procedimento` WRITE;
/*!40000 ALTER TABLE `wf_def_procedimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_def_procedimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_def_responsavel`
--

DROP TABLE IF EXISTS `wf_def_responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_def_responsavel` (
  `DEFR_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da definição de responsável',
  `DEFR_DS` varchar(256) DEFAULT NULL COMMENT 'Descrição da definição de responsável',
  `DEFR_NM` varchar(256) DEFAULT NULL COMMENT 'Nome da definição de responsável',
  `DEFR_TP` varchar(255) DEFAULT NULL COMMENT 'Tipo da definição de responsável',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da definição de responsável',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativa a definição de responsável',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da definição de responsável',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da definição de responsável',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou a definição de responsável',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou a definição de responsável',
  PRIMARY KEY (`DEFR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_def_responsavel`
--

LOCK TABLES `wf_def_responsavel` WRITE;
/*!40000 ALTER TABLE `wf_def_responsavel` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_def_responsavel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_def_tarefa`
--

DROP TABLE IF EXISTS `wf_def_tarefa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_def_tarefa` (
  `DEFT_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da definição de tarefa',
  `DEFP_ID` bigint DEFAULT NULL COMMENT 'Identificador do procedimento ao qual se liga a definição de tarefa',
  `DEFR_ID` bigint DEFAULT NULL COMMENT 'Identificador do responsável pela execução da definição de tarefa',
  `LOTA_ID` bigint DEFAULT NULL COMMENT 'Identificador da lotação responsável da definição de tarefa',
  `PESS_ID` bigint DEFAULT NULL COMMENT 'Identificador da pessoa responsável dadefinição de tarefa',
  `DEFT_ID_SEGUINTE` bigint DEFAULT NULL COMMENT 'Identificador definição de tarefa seguinte a definição de tarefa',
  `DEFT_TX_ASSUNTO` varchar(256) DEFAULT NULL COMMENT 'Assunto da definição de tarefa',
  `DEFT_TX_CONTEUDO` varchar(2048) DEFAULT NULL COMMENT 'Conteúdo da definição de tarefa',
  `DEFT_NM` varchar(256) DEFAULT NULL COMMENT 'Nome da definição de tarefa',
  `DEFT_DS_REF` varchar(256) DEFAULT NULL COMMENT 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos',
  `DEFT_ID_REF` bigint DEFAULT NULL COMMENT 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos',
  `DEFT_SG_REF` varchar(32) DEFAULT NULL COMMENT 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos',
  `DEFT_TP_RESPONSAVEL` varchar(255) DEFAULT NULL COMMENT 'Tipo do responsável na definição de tarefa',
  `DEFT_TP_TAREFA` varchar(255) DEFAULT NULL COMMENT 'Tipo de tarefa da definição de tarefa',
  `DEFT_FG_ULTIMO` bit(1) DEFAULT NULL COMMENT 'Indica que é a última da definição de tarefa do procedimento',
  `DEFT_NR_ORDEM` int DEFAULT NULL COMMENT 'Número usado para ordenar a definição de tarefa',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da definição de tarefa',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativa a definição de tarefa',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da definição de tarefa',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da definição de tarefa',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou a definição de tarefa',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou aa definição de tarefa',
  PRIMARY KEY (`DEFT_ID`),
  KEY `FK_DEFT_DEFP_ID` (`DEFP_ID`),
  KEY `FK_DEFT_DEFR_ID` (`DEFR_ID`),
  KEY `FK_DEFT_DEFT_ID_SEGUINTE` (`DEFT_ID_SEGUINTE`),
  CONSTRAINT `FK_DEFT_DEFT_ID_SEGUINTE` FOREIGN KEY (`DEFT_ID_SEGUINTE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK_DEFT_DEFP_ID` FOREIGN KEY (`DEFP_ID`) REFERENCES `wf_def_procedimento` (`DEFP_ID`),
  CONSTRAINT `FK_DEFT_DEFR_ID` FOREIGN KEY (`DEFR_ID`) REFERENCES `wf_def_responsavel` (`DEFR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_def_tarefa`
--

LOCK TABLES `wf_def_tarefa` WRITE;
/*!40000 ALTER TABLE `wf_def_tarefa` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_def_tarefa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_def_variavel`
--

DROP TABLE IF EXISTS `wf_def_variavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_def_variavel` (
  `DEFV_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da definição de variável',
  `DEFT_ID` bigint DEFAULT NULL COMMENT 'Identificador definição de tarefa a qual se relaciona a definição de variável',
  `DEFV_CD` varchar(32) DEFAULT NULL COMMENT 'Código da definição de variável',
  `DEFV_NM` varchar(256) DEFAULT NULL COMMENT 'Nome da definição de variável',
  `DEFV_TP` varchar(255) DEFAULT NULL COMMENT 'Tipo da definição de variável',
  `DEFV_TP_ACESSO` varchar(255) DEFAULT NULL COMMENT 'Tipo de acesso da definição de variável',
  `DEFV_NR_ORDEM` int DEFAULT NULL COMMENT 'Número para ordenar a definição de variável',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da definição de variável',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativa a definição de variável',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da definição de variável',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da definição de variável',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante da criação da definição de variável',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante da finalização da definição de variável',
  PRIMARY KEY (`DEFV_ID`),
  KEY `FK_DEFV_DEFT_ID` (`DEFT_ID`),
  CONSTRAINT `FK_DEFV_DEFT_ID` FOREIGN KEY (`DEFT_ID`) REFERENCES `wf_def_tarefa` (`DEFT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_def_variavel`
--

LOCK TABLES `wf_def_variavel` WRITE;
/*!40000 ALTER TABLE `wf_def_variavel` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_def_variavel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_movimentacao`
--

DROP TABLE IF EXISTS `wf_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_movimentacao` (
  `MOVI_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da movimentação',
  `MOVI_TP` varchar(31) NOT NULL COMMENT 'Tipo da movimentação',
  `PROC_ID` bigint DEFAULT NULL COMMENT 'Identificador do procedimento ao qual se relaciona a movimentação',
  `LOTA_ID_TITULAR` bigint DEFAULT NULL COMMENT 'Identificador da lotação do titular da movimentação',
  `PESS_ID_TITULAR` bigint DEFAULT NULL COMMENT 'Identificador da pessoa do titular da movimentação',
  `DEFT_ID_DE` bigint DEFAULT NULL COMMENT 'Identificador da definição de tarefa origem da movimentação',
  `DEFT_ID_PARA` bigint DEFAULT NULL COMMENT 'Identificador da definição de tarefa destino da movimentação',
  `LOTA_ID_DE` bigint DEFAULT NULL COMMENT 'Identificador da lotação origem da movimentação',
  `LOTA_ID_PARA` bigint DEFAULT NULL COMMENT 'Identificador da lotação destino da movimentação',
  `PESS_ID_DE` bigint DEFAULT NULL COMMENT 'Identificador da pessoa origem da movimentação',
  `PESS_ID_PARA` bigint DEFAULT NULL COMMENT 'Identificador da pessoa destino da movimentação',
  `MOVI_DS` varchar(400) DEFAULT NULL COMMENT 'Descrição da movimentação',
  `MOVI_TP_DESIGNACAO` varchar(255) DEFAULT NULL COMMENT 'Numa designação, indica o tipo de designação da movimentação',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial da movimentação',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se a movimentação está ativa',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início da movimentação',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim da movimentação',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou a movimentação',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou a movimentação',
  PRIMARY KEY (`MOVI_ID`),
  KEY `FK_MOVI_PROC_ID` (`PROC_ID`),
  KEY `FK_MOVI_DEFT_ID_DE` (`DEFT_ID_DE`),
  KEY `FK_MOVI_DEFT_ID_PARA` (`DEFT_ID_PARA`),
  CONSTRAINT `FK_MOVI_DEFT_ID_DE` FOREIGN KEY (`DEFT_ID_DE`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK_MOVI_DEFT_ID_PARA` FOREIGN KEY (`DEFT_ID_PARA`) REFERENCES `wf_def_tarefa` (`DEFT_ID`),
  CONSTRAINT `FK_MOVI_PROC_ID` FOREIGN KEY (`PROC_ID`) REFERENCES `wf_procedimento` (`PROC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_movimentacao`
--

LOCK TABLES `wf_movimentacao` WRITE;
/*!40000 ALTER TABLE `wf_movimentacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_movimentacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_procedimento`
--

DROP TABLE IF EXISTS `wf_procedimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_procedimento` (
  `PROC_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador do procedimento',
  `ORGU_ID` bigint DEFAULT NULL COMMENT 'Identificador do órgão usuário ao qual se relaciona o procedimento',
  `DEFP_ID` bigint DEFAULT NULL COMMENT 'Identificador da definição do procedimento',
  `LOTA_ID_EVENTO` bigint DEFAULT NULL COMMENT 'Identificador da lotação responsável pelo evento atual do procedimento',
  `PESS_ID_EVENTO` bigint DEFAULT NULL COMMENT 'Identificador da pessoa responsável pelo evento atual do procedimento',
  `LOTA_ID_TITULAR` bigint DEFAULT NULL COMMENT 'Identificador da lotação do titular que criou o procedimento',
  `PESS_ID_TITULAR` bigint DEFAULT NULL COMMENT 'Identificador da pessoa que criou o procedimento do procedimento',
  `PROC_ANO` int DEFAULT NULL COMMENT 'Ano do procedimento',
  `PROC_NR` int DEFAULT NULL COMMENT 'Número do procedimento',
  `PROC_TP_PRIORIDADE` varchar(255) DEFAULT NULL COMMENT 'Prioridade do procedimento',
  `PROC_NR_CORRENTE` int DEFAULT NULL COMMENT 'Número do passo em que se encontra o procedimento',
  `PROC_ST_CORRENTE` varchar(255) DEFAULT NULL COMMENT 'Status atual do procedimento',
  `PROC_TS_EVENTO` datetime(6) DEFAULT NULL COMMENT 'Data e hora do evento do procedimento',
  `PROC_NM_EVENTO` varchar(255) DEFAULT NULL COMMENT 'Nome do evento do procedimento',
  `PROC_CD_PRINCIPAL` varchar(255) DEFAULT NULL COMMENT 'Código do principal relacionado ao procedimento, no caso de um documento, é algo do tipo JFRJ-MEM-2020/000001',
  `PROC_TP_PRINCIPAL` varchar(255) DEFAULT NULL COMMENT 'Tipo do principal do procedimento',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de fim do procedimento',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de início do procedimento',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante da criação do procedimento',
  PRIMARY KEY (`PROC_ID`),
  KEY `FK_PROC_DEFP_ID` (`DEFP_ID`),
  CONSTRAINT `FK_PROC_DEFP_ID` FOREIGN KEY (`DEFP_ID`) REFERENCES `wf_def_procedimento` (`DEFP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_procedimento`
--

LOCK TABLES `wf_procedimento` WRITE;
/*!40000 ALTER TABLE `wf_procedimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_procedimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_responsavel`
--

DROP TABLE IF EXISTS `wf_responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_responsavel` (
  `RESP_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador do responsável',
  `DEFR_ID` bigint DEFAULT NULL COMMENT 'Identificador da definição do responsável',
  `ORGU_ID` bigint DEFAULT NULL COMMENT 'Identificador do órgão vinculado ao responsável',
  `LOTA_ID` bigint DEFAULT NULL COMMENT 'Identificador da lotação vinculada ao responsável',
  `PESS_ID` bigint DEFAULT NULL COMMENT 'Identificador da pessoa vinculada ao responsável',
  `HIS_ID_INI` bigint DEFAULT NULL COMMENT 'Identificador inicial do responsável',
  `HIS_ATIVO` int DEFAULT NULL COMMENT 'Indica se está ativo o responsável',
  `HIS_DT_INI` datetime(6) DEFAULT NULL COMMENT 'Data de início do responsável',
  `HIS_DT_FIM` datetime(6) DEFAULT NULL COMMENT 'Data de fim do responsável',
  `HIS_IDC_INI` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que criou o responsável',
  `HIS_IDC_FIM` bigint DEFAULT NULL COMMENT 'Identificador do cadastrante que finalizou o responsável',
  PRIMARY KEY (`RESP_ID`),
  KEY `FK_RESP_DEFR_ID` (`DEFR_ID`),
  CONSTRAINT `FK_RESP_DEFR_ID` FOREIGN KEY (`DEFR_ID`) REFERENCES `wf_def_responsavel` (`DEFR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_responsavel`
--

LOCK TABLES `wf_responsavel` WRITE;
/*!40000 ALTER TABLE `wf_responsavel` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_responsavel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_variavel`
--

DROP TABLE IF EXISTS `wf_variavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE `wf_variavel` (
  `VARI_ID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Identificador da variável',
  `PROC_ID` bigint DEFAULT NULL COMMENT 'Identificador do procedimento relacionado a variável',
  `VARI_FG` bit(1) DEFAULT NULL COMMENT 'Conteúdo da variável quando é um booleano',
  `VARI_DF` datetime(6) DEFAULT NULL COMMENT 'Conteúdo da variável quando é uma data',
  `VARI_NM` varchar(256) DEFAULT NULL COMMENT 'Nome da variável',
  `VARI_NR` double DEFAULT NULL COMMENT 'Conteúdo da variável quando é um número',
  `VARI_TX` varchar(255) DEFAULT NULL COMMENT 'Conteúdo da variável quando é um texto',
  PRIMARY KEY (`VARI_ID`),
  KEY `FK_VARI_PROC_ID` (`PROC_ID`),
  CONSTRAINT `FK_VARI_PROC_ID` FOREIGN KEY (`PROC_ID`) REFERENCES `wf_procedimento` (`PROC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_variavel`
--

LOCK TABLES `wf_variavel` WRITE;
/*!40000 ALTER TABLE `wf_variavel` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_variavel` ENABLE KEYS */;
UNLOCK TABLES;

drop function if exists remove_acento;
delimiter //
create function remove_acento( textvalue varchar(10000) )
returns varchar(10000) DETERMINISTIC
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
-- Dumping events for database 'sigawf'
--

--
-- Dumping routines for database 'sigawf'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-06 14:57:41
