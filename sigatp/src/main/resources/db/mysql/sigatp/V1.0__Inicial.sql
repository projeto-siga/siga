CREATE DATABASE  IF NOT EXISTS `sigatp` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sigatp`;
-- MySQL dump 10.13  Distrib 8.0.16, for Linux (x86_64)
--
-- Host: localhost    Database: sigatp
-- ------------------------------------------------------
-- Server version	8.0.16-commercial

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `abastecimento`
--

DROP TABLE IF EXISTS `abastecimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `abastecimento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `consumomedioemkmporlitro` bigint(126) NOT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `distanciapercorridaemkm` bigint(126) NOT NULL,
  `niveldecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerodanotafiscal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkm` bigint(126) NOT NULL,
  `precoporlitro` bigint(126) NOT NULL,
  `quantidadeemlitros` bigint(126) NOT NULL,
  `tipodecombustivel` varchar(255) COLLATE utf8_bin NOT NULL,
  `valortotaldanotafiscal` bigint(126) NOT NULL,
  `condutor_id` bigint(20) NOT NULL,
  `fornecedor_id` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `id_solicitante` bigint(20) DEFAULT NULL,
  `id_titular` bigint(20) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk229dbc718c04bb6` (`veiculo_id`),
  KEY `fk229dbc7d56685be` (`fornecedor_id`),
  KEY `fk229dbc73bf35a5e` (`condutor_id`),
  CONSTRAINT `fk229dbc718c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`),
  CONSTRAINT `fk229dbc73bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`),
  CONSTRAINT `fk229dbc7d56685be` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=679301 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `abastecimento_aud`
--

DROP TABLE IF EXISTS `abastecimento_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `abastecimento_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `consumomedioemkmporlitro` bigint(126) DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `distanciapercorridaemkm` bigint(126) DEFAULT NULL,
  `niveldecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerodanotafiscal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkm` bigint(126) DEFAULT NULL,
  `precoporlitro` bigint(126) DEFAULT NULL,
  `quantidadeemlitros` bigint(126) DEFAULT NULL,
  `tipodecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valortotaldanotafiscal` bigint(126) DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  `fornecedor_id` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `id_solicitante` bigint(20) DEFAULT NULL,
  `id_titular` bigint(20) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkdee4ce18df74e053` (`rev`),
  CONSTRAINT `fkdee4ce18df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `afastamento`
--

DROP TABLE IF EXISTS `afastamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `afastamento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahorafim` datetime(6) NOT NULL,
  `datahorainicio` datetime(6) NOT NULL,
  `descricao` varchar(255) COLLATE utf8_bin NOT NULL,
  `condutor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk9f72316d3bf35a5e` (`condutor_id`),
  CONSTRAINT `fk9f72316d3bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=629701 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `afastamento_aud`
--

DROP TABLE IF EXISTS `afastamento_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `afastamento_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk4f1de0bedf74e053` (`rev`),
  CONSTRAINT `fk4f1de0bedf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `andamento`
--

DROP TABLE IF EXISTS `andamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `andamento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dataandamento` datetime(6) NOT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `estadorequisicao` varchar(255) COLLATE utf8_bin NOT NULL,
  `missao_id` bigint(20) DEFAULT NULL,
  `requisicaotransporte_id` bigint(20) NOT NULL,
  `responsavel_id_pessoa` bigint(20) NOT NULL,
  `datanotificacaoworkflow` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk9dc64787f99ac59e` (`missao_id`),
  KEY `fk9dc64787aebda4de` (`requisicaotransporte_id`),
  CONSTRAINT `fk9dc64787aebda4de` FOREIGN KEY (`requisicaotransporte_id`) REFERENCES `requisicaotransporte` (`id`),
  CONSTRAINT `fk9dc64787f99ac59e` FOREIGN KEY (`missao_id`) REFERENCES `missao` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40170354 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `andamento_aud`
--

DROP TABLE IF EXISTS `andamento_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `andamento_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `dataandamento` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `estadorequisicao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `missao_id` bigint(20) DEFAULT NULL,
  `requisicaotransporte_id` bigint(20) DEFAULT NULL,
  `responsavel_id_pessoa` bigint(20) DEFAULT NULL,
  `datanotificacaoworkflow` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk3ae559d8df74e053` (`rev`),
  CONSTRAINT `fk3ae559d8df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `autodeinfracao`
--

DROP TABLE IF EXISTS `autodeinfracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `autodeinfracao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datadepagamento` datetime(6) DEFAULT NULL,
  `datadevencimento` datetime(6) DEFAULT NULL,
  `datadoprocesso` datetime(6) DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datalimiteapresentacao` datetime(6) DEFAULT NULL,
  `foirecebido` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `local` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `memorando` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerodoprocesso` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valor` bigint(126) NOT NULL,
  `valorcomdesconto` bigint(126) NOT NULL,
  `condutor_id` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `penalidade_id` bigint(20) NOT NULL,
  `tipodenotificacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkd80212d13bf35a5e` (`condutor_id`),
  KEY `fk_autinf_pen` (`penalidade_id`),
  KEY `fkd80212d118c04bb6` (`veiculo_id`),
  CONSTRAINT `fk_autinf_pen` FOREIGN KEY (`penalidade_id`) REFERENCES `penalidade` (`id`),
  CONSTRAINT `fkd80212d118c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`),
  CONSTRAINT `fkd80212d13bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=803260 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `autodeinfracao_aud`
--

DROP TABLE IF EXISTS `autodeinfracao_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `autodeinfracao_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datadepagamento` datetime(6) DEFAULT NULL,
  `datadevencimento` datetime(6) DEFAULT NULL,
  `datadoprocesso` datetime(6) DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datalimiteapresentacao` datetime(6) DEFAULT NULL,
  `foirecebido` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `local` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `memorando` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerodoprocesso` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valor` bigint(126) DEFAULT NULL,
  `valorcomdesconto` bigint(126) DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `penalidade_id` bigint(20) DEFAULT NULL,
  `tipodenotificacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk10567022df74e053` (`rev`),
  CONSTRAINT `fk10567022df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `avaria`
--

DROP TABLE IF EXISTS `avaria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `avaria` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dataderegistro` datetime(6) DEFAULT NULL,
  `datadesolucao` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `podecircular` varchar(255) COLLATE utf8_bin DEFAULT 'SIM',
  PRIMARY KEY (`id`),
  KEY `fk7597a6de18c04bb6` (`veiculo_id`),
  CONSTRAINT `fk7597a6de18c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `avaria_aud`
--

DROP TABLE IF EXISTS `avaria_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `avaria_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `dataderegistro` datetime(6) DEFAULT NULL,
  `datadesolucao` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `podecircular` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk1b935afdf74e053` (`rev`),
  CONSTRAINT `fk1b935afdf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `condutor`
--

DROP TABLE IF EXISTS `condutor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `condutor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoriacnh` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `celularinstitucional` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `celularpessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `datavencimentocnh` datetime(6) DEFAULT NULL,
  `telefoneinstitucional` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `telefonepessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `dppessoa_id_pessoa` bigint(20) DEFAULT NULL,
  `observacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `emailpessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `conteudoimagemblob` longblob,
  `numerocnh` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=803269 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `condutor_aud`
--

DROP TABLE IF EXISTS `condutor_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `condutor_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `categoriacnh` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `celularinstitucional` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `celularpessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `datavencimentocnh` datetime(6) DEFAULT NULL,
  `telefoneinstitucional` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `telefonepessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `dppessoa_id_pessoa` bigint(20) DEFAULT NULL,
  `observacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `emailpessoal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `conteudoimagemblob` longblob,
  `numerocnh` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkab0aaed5df74e053` (`rev`),
  CONSTRAINT `fkab0aaed5df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controlegabinete`
--

DROP TABLE IF EXISTS `controlegabinete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `controlegabinete` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahora` datetime(6) DEFAULT NULL,
  `datahorasaida` datetime(6) DEFAULT NULL,
  `datahoraretorno` datetime(6) DEFAULT NULL,
  `itinerario` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkmretorno` bigint(126) NOT NULL,
  `odometroemkmsaida` bigint(126) NOT NULL,
  `condutor_id` bigint(20) NOT NULL,
  `id_solicitante` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `naturezadoservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ocorrencias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_titular` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk3222637118c04bb6` (`veiculo_id`),
  KEY `fk322263713bf35a5e` (`condutor_id`),
  CONSTRAINT `fk3222637118c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`),
  CONSTRAINT `fk322263713bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controlegabinete_aud`
--

DROP TABLE IF EXISTS `controlegabinete_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `controlegabinete_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datahorasaida` datetime(6) DEFAULT NULL,
  `datahoraretorno` datetime(6) DEFAULT NULL,
  `itinerario` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkmretorno` bigint(126) DEFAULT NULL,
  `odometroemkmsaida` bigint(126) DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  `id_solicitante` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `naturezadoservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ocorrencias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_titular` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkca9d70c2df74e053` (`rev`),
  CONSTRAINT `fkca9d70c2df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cor`
--

DROP TABLE IF EXISTS `cor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `cor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cor_aud`
--

DROP TABLE IF EXISTS `cor_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `cor_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk9c1e1437df74e053` (`rev`),
  CONSTRAINT `fk9c1e1437df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `diadetrabalho`
--

DROP TABLE IF EXISTS `diadetrabalho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `diadetrabalho` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `diaentrada` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `diasaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `horaentrada` datetime(6) DEFAULT NULL,
  `horasaida` datetime(6) DEFAULT NULL,
  `escaladetrabalho_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk9f560b2ed10e8c3e` (`escaladetrabalho_id`),
  CONSTRAINT `fk9f560b2ed10e8c3e` FOREIGN KEY (`escaladetrabalho_id`) REFERENCES `escaladetrabalho` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=827401 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `diadetrabalho_aud`
--

DROP TABLE IF EXISTS `diadetrabalho_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `diadetrabalho_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `diaentrada` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `diasaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `horaentrada` datetime(6) DEFAULT NULL,
  `horasaida` datetime(6) DEFAULT NULL,
  `escaladetrabalho_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fka20cf1ffdf74e053` (`rev`),
  CONSTRAINT `fka20cf1ffdf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `escaladetrabalho`
--

DROP TABLE IF EXISTS `escaladetrabalho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `escaladetrabalho` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datavigenciafim` datetime(6) DEFAULT NULL,
  `datavigenciainicio` datetime(6) DEFAULT NULL,
  `condutor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk3de938b33bf35a5e` (`condutor_id`),
  CONSTRAINT `fk3de938b33bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=827351 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `escaladetrabalho_aud`
--

DROP TABLE IF EXISTS `escaladetrabalho_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `escaladetrabalho_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datavigenciafim` datetime(6) DEFAULT NULL,
  `datavigenciainicio` datetime(6) DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk4093d504df74e053` (`rev`),
  CONSTRAINT `fk4093d504df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `finalidaderequisicao`
--

DROP TABLE IF EXISTS `finalidaderequisicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `finalidaderequisicao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_ori` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_c00414650` (`descricao`)
) ENGINE=InnoDB AUTO_INCREMENT=803370 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `finalidaderequisicao_aud`
--

DROP TABLE IF EXISTS `finalidaderequisicao_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `finalidaderequisicao_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_ori` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk54f9930bdf74e053` (`rev`),
  CONSTRAINT `fk54f9930bdf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fornecedor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bairro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cep` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cnpj` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `complemento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enderecovirtual` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fax` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `localidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logradouro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nomecontato` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ramodeatividade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `razaosocial` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `telefone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `uf` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=803257 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fornecedor_aud`
--

DROP TABLE IF EXISTS `fornecedor_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fornecedor_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `bairro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cep` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cnpj` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `complemento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enderecovirtual` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `fax` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `localidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logradouro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nomecontato` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ramodeatividade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `razaosocial` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `telefone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `uf` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkb5e860d6df74e053` (`rev`),
  CONSTRAINT `fkb5e860d6df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupoveiculo`
--

DROP TABLE IF EXISTS `grupoveiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `grupoveiculo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caracteristicas` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `finalidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `letra` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupoveiculo_aud`
--

DROP TABLE IF EXISTS `grupoveiculo_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `grupoveiculo_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `caracteristicas` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `finalidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `letra` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fka9ddee57df74e053` (`rev`),
  CONSTRAINT `fka9ddee57df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lotacaoatdrequisicao`
--

DROP TABLE IF EXISTS `lotacaoatdrequisicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lotacaoatdrequisicao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_lota_solicitante` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lotacaoatdrequisicao_aud`
--

DROP TABLE IF EXISTS `lotacaoatdrequisicao_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lotacaoatdrequisicao_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_lota_solicitante` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkab96b70adf74e053` (`rev`),
  CONSTRAINT `fkab96b70adf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lotacaoveiculo`
--

DROP TABLE IF EXISTS `lotacaoveiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lotacaoveiculo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) NOT NULL,
  `id_lota_solicitante` bigint(20) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  `odometroemkm` bigint(126) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk886d624e18c04bb6` (`veiculo_id`),
  CONSTRAINT `fk886d624e18c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=674851 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lotacaoveiculo_aud`
--

DROP TABLE IF EXISTS `lotacaoveiculo_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lotacaoveiculo_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `id_lota_solicitante` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `odometroemkm` bigint(126) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkf363b91fdf74e053` (`rev`),
  CONSTRAINT `fkf363b91fdf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `missao`
--

DROP TABLE IF EXISTS `missao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `missao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avariasaparentesretorno` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `avariasaparentessaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaoabastecimento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaosaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaoseguro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `consumoemlitros` bigint(126) NOT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datahoraretorno` datetime(6) DEFAULT NULL,
  `datahorasaida` datetime(6) NOT NULL,
  `estadomissao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `estepe` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `extintor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ferramentas` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `itinerariocompleto` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `justificativa` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licenca` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `limpeza` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nivelcombustivelretorno` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nivelcombustivelsaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `ocorrencias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroretornoemkm` bigint(126) NOT NULL,
  `odometrosaidaemkm` bigint(126) NOT NULL,
  `triangulos` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `condutor_id` bigint(20) NOT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `iniciorapido` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk8962ce4a18c04bb6` (`veiculo_id`),
  KEY `fk8962ce4a3bf35a5e` (`condutor_id`),
  CONSTRAINT `fk8962ce4a18c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`),
  CONSTRAINT `fk8962ce4a3bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40170451 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `missao_aud`
--

DROP TABLE IF EXISTS `missao_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `missao_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `avariasaparentesretorno` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `avariasaparentessaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaoabastecimento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaosaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cartaoseguro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `consumoemlitros` bigint(126) DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datahoraretorno` datetime(6) DEFAULT NULL,
  `datahorasaida` datetime(6) DEFAULT NULL,
  `estadomissao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `estepe` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `extintor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ferramentas` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `itinerariocompleto` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `justificativa` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licenca` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `limpeza` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nivelcombustivelretorno` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nivelcombustivelsaida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `ocorrencias` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroretornoemkm` bigint(126) DEFAULT NULL,
  `odometrosaidaemkm` bigint(126) DEFAULT NULL,
  `triangulos` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  `iniciorapido` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk638ac71bdf74e053` (`rev`),
  CONSTRAINT `fk638ac71bdf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `missao_requistransporte`
--

DROP TABLE IF EXISTS `missao_requistransporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `missao_requistransporte` (
  `missao_id` bigint(20) NOT NULL,
  `requisicaotransporte_id` bigint(20) NOT NULL,
  KEY `fk488f75b2f99ac59e` (`missao_id`),
  KEY `fk488f75b2aebda4de` (`requisicaotransporte_id`),
  CONSTRAINT `fk488f75b2aebda4de` FOREIGN KEY (`requisicaotransporte_id`) REFERENCES `requisicaotransporte` (`id`),
  CONSTRAINT `fk488f75b2f99ac59e` FOREIGN KEY (`missao_id`) REFERENCES `missao` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `missao_requistransporte_aud`
--

DROP TABLE IF EXISTS `missao_requistransporte_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `missao_requistransporte_aud` (
  `rev` bigint(20) NOT NULL,
  `requisicaotransporte_id` bigint(20) NOT NULL,
  `missao_id` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  PRIMARY KEY (`rev`,`missao_id`,`requisicaotransporte_id`),
  CONSTRAINT `fke3c57a83df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro`
--

DROP TABLE IF EXISTS `parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `parametro` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_lotacao` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  `nomeparametro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valorparametro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `datainicio` datetime(6) DEFAULT NULL,
  `datafim` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_aud`
--

DROP TABLE IF EXISTS `parametro_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `parametro_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_lotacao` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  `nomeparametro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valorparametro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `datainicio` datetime(6) DEFAULT NULL,
  `datafim` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkparametrorev` (`rev`),
  CONSTRAINT `fkparametrorev` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `penalidade`
--

DROP TABLE IF EXISTS `penalidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `penalidade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigoinfracao` varchar(255) COLLATE utf8_bin NOT NULL,
  `artigoctb` varchar(255) COLLATE utf8_bin NOT NULL,
  `classificacao` varchar(255) COLLATE utf8_bin NOT NULL,
  `descricaoinfracao` varchar(255) COLLATE utf8_bin NOT NULL,
  `infrator` varchar(255) COLLATE utf8_bin NOT NULL,
  `valor` bigint(126) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_c00414647` (`codigoinfracao`)
) ENGINE=InnoDB AUTO_INCREMENT=803227 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `penalidade_aud`
--

DROP TABLE IF EXISTS `penalidade_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `penalidade_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `codigoinfracao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `artigoctb` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `classificacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `descricaoinfracao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `infrator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valor` bigint(126) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plantao`
--

DROP TABLE IF EXISTS `plantao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `plantao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `condutor_id` bigint(20) NOT NULL,
  `referencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk45c88fb93bf35a5e` (`condutor_id`),
  CONSTRAINT `fk45c88fb93bf35a5e` FOREIGN KEY (`condutor_id`) REFERENCES `condutor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=826831 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plantao_aud`
--

DROP TABLE IF EXISTS `plantao_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `plantao_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `condutor_id` bigint(20) DEFAULT NULL,
  `referencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkb44390adf74e053` (`rev`),
  CONSTRAINT `fkb44390adf74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `relatoriodiario`
--

DROP TABLE IF EXISTS `relatoriodiario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `relatoriodiario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cartoes` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `data` datetime(6) DEFAULT NULL,
  `equipamentoobrigatorio` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `niveldecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `observacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkm` bigint(126) NOT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fkdd7f8e3118c04bb6` (`veiculo_id`),
  CONSTRAINT `fkdd7f8e3118c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `relatoriodiario_aud`
--

DROP TABLE IF EXISTS `relatoriodiario_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `relatoriodiario_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `cartoes` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `data` datetime(6) DEFAULT NULL,
  `equipamentoobrigatorio` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `niveldecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `observacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `odometroemkm` bigint(126) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkd9e73b82df74e053` (`rev`),
  CONSTRAINT `fkd9e73b82df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requisicao_tipopassageiro`
--

DROP TABLE IF EXISTS `requisicao_tipopassageiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `requisicao_tipopassageiro` (
  `requisicaotransporte_id` bigint(20) NOT NULL,
  `tipopassageiro` varchar(255) COLLATE utf8_bin NOT NULL,
  KEY `fk140b2242aebda4de` (`requisicaotransporte_id`),
  CONSTRAINT `fk140b2242aebda4de` FOREIGN KEY (`requisicaotransporte_id`) REFERENCES `requisicaotransporte` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requisicao_tipopassageiro_aud`
--

DROP TABLE IF EXISTS `requisicao_tipopassageiro_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `requisicao_tipopassageiro_aud` (
  `rev` bigint(20) NOT NULL,
  `requisicaotransporte_id` bigint(20) NOT NULL,
  `tipopassageiro` varchar(255) COLLATE utf8_bin NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  PRIMARY KEY (`rev`,`requisicaotransporte_id`,`tipopassageiro`),
  CONSTRAINT `fkf9785f13df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requisicaotransporte`
--

DROP TABLE IF EXISTS `requisicaotransporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `requisicaotransporte` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahora` datetime(6) DEFAULT NULL,
  `datahoraretornoprevisto` datetime(6) DEFAULT NULL,
  `datahorasaidaprevista` datetime(6) DEFAULT NULL,
  `finalidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `itinerarios` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `passageiros` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tiporequisicao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_solicitante` bigint(20) DEFAULT NULL,
  `id_finalidade` bigint(20) DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  `numerodepassageiros` mediumint(8) unsigned DEFAULT NULL,
  `origemexterna` smallint(6) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk450568a580c02ed3` (`id_finalidade`),
  CONSTRAINT `fk450568a580c02ed3` FOREIGN KEY (`id_finalidade`) REFERENCES `finalidaderequisicao` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40170301 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requisicaotransporte_aud`
--

DROP TABLE IF EXISTS `requisicaotransporte_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `requisicaotransporte_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datahoraretornoprevisto` datetime(6) DEFAULT NULL,
  `datahorasaidaprevista` datetime(6) DEFAULT NULL,
  `finalidade` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `itinerarios` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `passageiros` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tiporequisicao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_solicitante` bigint(20) DEFAULT NULL,
  `id_finalidade` bigint(20) DEFAULT NULL,
  `id_complexo` bigint(20) DEFAULT NULL,
  `numerodepassageiros` mediumint(8) unsigned DEFAULT NULL,
  `origemexterna` smallint(6) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkfd52bbf6df74e053` (`rev`),
  CONSTRAINT `fkfd52bbf6df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revinfo`
--

DROP TABLE IF EXISTS `revinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `revinfo` (
  `rev` bigint(20) NOT NULL AUTO_INCREMENT,
  `revtstmp` bigint(20) DEFAULT NULL,
  `enderecoip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `matricula` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `motivolog` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`rev`)
) ENGINE=InnoDB AUTO_INCREMENT=40171301 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `schema_version` (
  `version_rank` bigint(38) NOT NULL,
  `installed_rank` bigint(38) NOT NULL,
  `version` varchar(50) COLLATE utf8_bin NOT NULL,
  `description` varchar(200) COLLATE utf8_bin NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL,
  `script` varchar(1000) COLLATE utf8_bin NOT NULL,
  `checksum` bigint(38) DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8_bin NOT NULL,
  `installed_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` bigint(38) NOT NULL,
  `success` smallint(6) unsigned NOT NULL,
  PRIMARY KEY (`version`),
  KEY `schema_version_s_idx` (`success`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_vr_idx` (`version_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `servicoveiculo`
--

DROP TABLE IF EXISTS `servicoveiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `servicoveiculo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datahora` datetime(6) DEFAULT NULL,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorafimprevisto` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `datahorainicioprevisto` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `motivocancelamento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `situacaoservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tiposdeservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ultimaalteracao` datetime(6) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `requisicaotransporte_id` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk6e008870aebda4de` (`requisicaotransporte_id`),
  KEY `fk6e00887018c04bb6` (`veiculo_id`),
  CONSTRAINT `fk6e00887018c04bb6` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculo` (`id`),
  CONSTRAINT `fk6e008870aebda4de` FOREIGN KEY (`requisicaotransporte_id`) REFERENCES `requisicaotransporte` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=622801 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `servicoveiculo_aud`
--

DROP TABLE IF EXISTS `servicoveiculo_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `servicoveiculo_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `datahora` datetime(6) DEFAULT NULL,
  `datahorafim` datetime(6) DEFAULT NULL,
  `datahorafimprevisto` datetime(6) DEFAULT NULL,
  `datahorainicio` datetime(6) DEFAULT NULL,
  `datahorainicioprevisto` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `motivocancelamento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `situacaoservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tiposdeservico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ultimaalteracao` datetime(6) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `id_pessoa` bigint(20) DEFAULT NULL,
  `requisicaotransporte_id` bigint(20) DEFAULT NULL,
  `veiculo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fkf0d2fe41df74e053` (`rev`),
  CONSTRAINT `fkf0d2fe41df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarioteste`
--

DROP TABLE IF EXISTS `usuarioteste`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuarioteste` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bairro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numero` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `veiculo`
--

DROP TABLE IF EXISTS `veiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `veiculo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `anofabricacao` bigint(20) NOT NULL,
  `anomodelo` bigint(20) NOT NULL,
  `categoriacnh` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `chassi` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dataalienacao` datetime(6) DEFAULT NULL,
  `dataaquisicao` datetime(6) DEFAULT NULL,
  `datagarantia` datetime(6) DEFAULT NULL,
  `direcao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dpvat` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licenciamentoanual` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `marca` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modelo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `motor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerocartaoabastecimento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerocartaoseguro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `outros` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `patrimonio` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `placa` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneumedida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneupressaodianteira` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneupressaotraseira` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `potencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `processoalienacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `renavam` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `situacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tanque` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `temabs` smallint(6) unsigned NOT NULL,
  `temairbag` smallint(6) unsigned NOT NULL,
  `tembancosemcouro` smallint(6) unsigned NOT NULL,
  `temcamerademarchare` smallint(6) unsigned NOT NULL,
  `temcdplayer` smallint(6) unsigned NOT NULL,
  `temcontroledetracao` smallint(6) unsigned NOT NULL,
  `temdvdplayer` smallint(6) unsigned NOT NULL,
  `temebd` smallint(6) unsigned NOT NULL,
  `temfreioadisconasquatrorodas` smallint(6) unsigned NOT NULL,
  `temgps` smallint(6) unsigned NOT NULL,
  `temoutros` smallint(6) unsigned NOT NULL,
  `tempilotoautomatico` smallint(6) unsigned NOT NULL,
  `temrodadeligaleve` smallint(6) unsigned NOT NULL,
  `temsensordemarchare` smallint(6) unsigned NOT NULL,
  `temtelalcdpapoiocabeca` smallint(6) unsigned NOT NULL,
  `termoalienacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tipodeblindagem` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tipodecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `transmissao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `usocomum` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `validadecartaoabastecimento` datetime(6) DEFAULT NULL,
  `validadecartaoseguro` datetime(6) DEFAULT NULL,
  `valoraquisicao` bigint(126) DEFAULT NULL,
  `cor_id` bigint(20) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `fornecedor_id` bigint(20) DEFAULT NULL,
  `grupo_id` bigint(20) DEFAULT NULL,
  `temarcondicionado` smallint(6) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk77a7c7efd2aa1c76` (`grupo_id`),
  KEY `fk77a7c7efd56685be` (`fornecedor_id`),
  KEY `fk77a7c7ef2db35256` (`cor_id`),
  CONSTRAINT `fk77a7c7ef2db35256` FOREIGN KEY (`cor_id`) REFERENCES `cor` (`id`),
  CONSTRAINT `fk77a7c7efd2aa1c76` FOREIGN KEY (`grupo_id`) REFERENCES `grupoveiculo` (`id`),
  CONSTRAINT `fk77a7c7efd56685be` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=674801 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `veiculo_aud`
--

DROP TABLE IF EXISTS `veiculo_aud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `veiculo_aud` (
  `id` bigint(20) NOT NULL,
  `rev` bigint(20) NOT NULL,
  `revtype` smallint(6) unsigned DEFAULT NULL,
  `anofabricacao` bigint(20) DEFAULT NULL,
  `anomodelo` bigint(20) DEFAULT NULL,
  `categoriacnh` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `chassi` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dataalienacao` datetime(6) DEFAULT NULL,
  `dataaquisicao` datetime(6) DEFAULT NULL,
  `datagarantia` datetime(6) DEFAULT NULL,
  `direcao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dpvat` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licenciamentoanual` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `marca` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `modelo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `motor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerocartaoabastecimento` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `numerocartaoseguro` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `outros` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `patrimonio` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `placa` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneumedida` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneupressaodianteira` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pneupressaotraseira` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `potencia` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `processoalienacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `renavam` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `situacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tanque` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `temabs` smallint(6) unsigned DEFAULT NULL,
  `temairbag` smallint(6) unsigned DEFAULT NULL,
  `tembancosemcouro` smallint(6) unsigned DEFAULT NULL,
  `temcamerademarchare` smallint(6) unsigned DEFAULT NULL,
  `temcdplayer` smallint(6) unsigned DEFAULT NULL,
  `temcontroledetracao` smallint(6) unsigned DEFAULT NULL,
  `temdvdplayer` smallint(6) unsigned DEFAULT NULL,
  `temebd` smallint(6) unsigned DEFAULT NULL,
  `temfreioadisconasquatrorodas` smallint(6) unsigned DEFAULT NULL,
  `temgps` smallint(6) unsigned DEFAULT NULL,
  `temoutros` smallint(6) unsigned DEFAULT NULL,
  `tempilotoautomatico` smallint(6) unsigned DEFAULT NULL,
  `temrodadeligaleve` smallint(6) unsigned DEFAULT NULL,
  `temsensordemarchare` smallint(6) unsigned DEFAULT NULL,
  `temtelalcdpapoiocabeca` smallint(6) unsigned DEFAULT NULL,
  `termoalienacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tipodeblindagem` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tipodecombustivel` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `transmissao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `usocomum` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `validadecartaoabastecimento` datetime(6) DEFAULT NULL,
  `validadecartaoseguro` datetime(6) DEFAULT NULL,
  `valoraquisicao` bigint(126) DEFAULT NULL,
  `cor_id` bigint(20) DEFAULT NULL,
  `id_orgao_usu` bigint(20) DEFAULT NULL,
  `fornecedor_id` bigint(20) DEFAULT NULL,
  `grupo_id` bigint(20) DEFAULT NULL,
  `temarcondicionado` smallint(6) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`rev`),
  KEY `fk4dc06640df74e053` (`rev`),
  CONSTRAINT `fk4dc06640df74e053` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'sigatp'
--
/*!50003 DROP FUNCTION IF EXISTS `remove_acento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `remove_acento`(acentuado VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8 COLLATE utf8_bin
    DETERMINISTIC
BEGIN
	
	SET @textvalue = acentuado ;

    -- ACCENTS
    SET @withaccents = 'ŠšŽžÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÑÒÓÔÕÖØÙÚÛÜÝŸÞàáâãäåæçèéêëìíîïñòóôõöøùúûüýÿþƒ';
    SET @withoutaccents = 'SsZzAAAAAAACEEEEIIIINOOOOOOUUUUYYBaaaaaaaceeeeiiiinoooooouuuuyybf';
    SET @count = LENGTH(@withaccents);

	WHILE @count > 0 DO
        SET @textvalue = REPLACE(@textvalue, SUBSTRING(@withaccents, @count, 1), SUBSTRING(@withoutaccents, @count, 1));
        SET @count = @count - 1;
    END WHILE;
    
    RETURN UPPER(@textvalue);

END ;;
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

-- Dump completed on 2021-06-25 11:23:53

-- cores
Insert into sigatp.cor (id,nome) values (1,'AMARELA');
Insert into sigatp.cor (id,nome) values (2,'AZUL');
Insert into sigatp.cor (id,nome) values (3,'BEGE');
Insert into sigatp.cor (id,nome) values (4,'BRANCA');
Insert into sigatp.cor (id,nome) values (5,'CINZA');
Insert into sigatp.cor (id,nome) values (6,'DOURADA');
Insert into sigatp.cor (id,nome) values (7,'GRENA');
Insert into sigatp.cor (id,nome) values (8,'LARANJA');
Insert into sigatp.cor (id,nome) values (9,'MARROM');
Insert into sigatp.cor (id,nome) values (10,'PRATA');
Insert into sigatp.cor (id,nome) values (11,'PRETA');
Insert into sigatp.cor (id,nome) values (12,'ROSA');
Insert into sigatp.cor (id,nome) values (13,'ROXA');
Insert into sigatp.cor (id,nome) values (14,'VERDE');
Insert into sigatp.cor (id,nome) values (15,'VERMELHA');
Insert into sigatp.cor (id,nome) values (16,'FANTASIA');

-- grupos de veiculos

Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (1,'VEÍCULOS DE MÉDIO PORTE, TIPO SEDAN, COR PRETA, COM  
CAPACIDADE DE TRANSPORTE DE ATÉ 5 (CINCO) PASSAGEIROS, MOTOR DE POTÊNCIA MÍNIMA  
DE 116 CV E MÁXIMA DE 159 CV (GASOLINA) E ITENS DE SEGURANÇA CONDIZENTES COM  
O SERVIÇO','TRANSPORTE DOS PRESIDENTES, DOS VICE-PRESIDENTES E DOS  
CORREGEDORES DOS TRIBUNAIS REGIONAIS FEDERAIS','A','REPRESENTAÇÃO');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (2,'VEÍCULOS DE MÉDIO PORTE, TIPO SEDAN, COR PRETA, COM
CAPACIDADE DE TRANSPORTE DE ATÉ 5 (CINCO) PASSAGEIROS, MOTOR DE POTÊNCIA MÍNIMA
DE 116 CV E MÁXIMA DE 159 CV (GASOLINA) E ITENS DE SEGURANÇA CONDIZENTES COM
O SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DOS JUÍZES DE SEGUNDO GRAU E
DOS JUÍZES DIRETORES DE FORO E DIRETORES DE SUBSEÇÕES JUDICIÁRIAS','B','TRANSPORTE INSTITUCIONAL');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (3,'VEÍCULOS DE PEQUENO PORTE, COM CAPACIDADE DE ATÉ 5 (CINCO)
OCUPANTES, MOTOR COM POTÊNCIA MÍNIMA DE 80 CV E MÁXIMA DE 112 CV (GASOLINA)
E ITENS DE SEGURANÇA CONDIZENTES COM O SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DE JUÍZES DE PRIMEIRO GRAU E
SERVIDORES NO DESEMPENHO DE ATIVIDADES EXTERNAS DE INTERESSE DA ADMINISTRAÇÃO','C','SERVIÇO COMUM');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (4,'PICK-UPS CABINE DUPLA, VANS COM CAPACIDADE MÍNIMA DE 12  
(DOZE) OCUPANTES, MICROÔNIBUS E ÔNIBUS, MOTOR COM POTÊNCIA CONDIZENTE COM O  
SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DE MAGISTRADOS E SERVIDORES NO','D','TRANSPORTE COLETIVO');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (5,'FURGÕES, PICK-UPS DE CABINE SIMPLES, REBOQUES E SEMIREBOQUES,  
MOTOR DE POTÊNCIA CONDIZENTE COM O SERVIÇO','TRANSPORTE DE CARGAS LEVES NO DESEMPENHO DE ATIVIDADES EXTERNAS  
DE INTERESSE DA ADMINISTRAÇÃO','E','CARGA LEVE');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values(6,'VEÍCULOS TIPO CAMINHÃO, MOTOR DE POTÊNCIA CONDIZENTE COM O','TRANSPORTE DE CARGAS PESADAS','F','CARGA PESADA');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values(7,'AMBULÂNCIAS E VEÍCULOS COM DISPOSITIVO DE ALARME E LUZ','ATENDIMENTO, EM CARÁTER DE SOCORRO MÉDICO OU DE APOIO ÀS','G','APOIO ESPECIAL');
Insert into sigatp.grupoveiculo (id,caracteristicas,finalidade,letra,nome) values (8,'TRANSPORTE DO TIPO BLINDADO','ATENDIMENTO DE AUTORIDADES','H','BLINDADO');

-- finalidade 'OUTRA'

insert into sigatp.finalidaderequisicao (id, descricao, id_orgao_ori) values (-1, 'OUTRA', 999999999);

-- parametro: mostrar tipo de passageiro 'CARGA'?

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) values (1, 'mostrarTipoPassageiroCarga', 'true', CURRENT_TIMESTAMP, 'Mostrar ou nao o tipo de passageiro "CARGA". Possivel bloquear (ou liberar) para um orgao especifico usando o campo id_orgao_usu');

-- parametros: envio automatico de e-mail e tamanho limite de imagem para upload

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (10, 'cron.executa', 'false', CURRENT_TIMESTAMP, 'EmailNotificacoes : Executar ou nao o cron que envia emails');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (11, 'cron.enviarParaLista', 'false', CURRENT_TIMESTAMP, 'EmailNotificacoes : Enviar email para a lista (caso "false": enviar para o usuario)');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (12, 'cron.listaEmail', 'administrador@sigatp.teste.docker', CURRENT_TIMESTAMP, 'EmailNotificacoes: Lista de emails a enviar');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (13, 'cron.inicio', '0 0 9 ? 1/1 2#1 *', CURRENT_TIMESTAMP, 'EmailNotificacoes: Configuracoes de inicializacao do servico.');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (14, 'cron.executa.notificarMissoesProgramadas', 'false', CURRENT_TIMESTAMP, 'Enviar ou nao e-mails aos condutores reclamando das missoes programadas e nao iniciadas.');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (20, 'cron.executaw', 'false', CURRENT_TIMESTAMP, 'Workflow: Executar ou nao o cron que envia emails');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (21, 'cron.enviarParaListaW', 'false', CURRENT_TIMESTAMP, 'Workflow: Enviar email para a lista (caso "false": enviar para o usuario)');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (22, 'cron.listaEmailw', 'administrador@sigatp.teste.docker', CURRENT_TIMESTAMP, 'Workflow: Lista de emails a enviar');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (23, 'cron.iniciow', '0 0/10 9-19 1/1 * ? *', CURRENT_TIMESTAMP, 'Workflow: Configuracoes de inicializacao do servico.');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao)  
VALUES (24, 'cron.dataInicioPesquisaw', '01/04/2014', CURRENT_TIMESTAMP, 'Workflow: Data de inicio das requisicoes transporte para notificacao');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (25, 'caminhoHostnameStandalone', 'sigaidp.crossdomain.url', CURRENT_TIMESTAMP, 'Workflow: nome do parametro no arquivo standalone.xml que contem o hostname do servidor');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (30, 'imagem.filesize', '1', CURRENT_TIMESTAMP, 'Tamanho maximo em MB da imagem para upload');

insert into sigatp.parametro(id, nomeparametro, valorparametro, datainicio, descricao) 
values (40, 'mostrarTipoFinalidadeOutra', 'true', CURRENT_TIMESTAMP, 'Mostrar ou nao o tipo de finalidade OUTRA e permitir detalhar finalidade. Possivel bloquear (ou liberar) para um orgao especifico usando o campo id_orgao_usu');

insert into sigatp.parametro(id, nomeparametro, valorparametro, datainicio, descricao) 
values (50, 'cron.nodeQueExecutaEnvioEmail', 'coloque_aqui_o_node_name_que_envia_email', CURRENT_TIMESTAMP, 'Conteudo do parametro jboss.node.name do node que sera responsavel por enviar os e-mails das tarefas agendadas.');


