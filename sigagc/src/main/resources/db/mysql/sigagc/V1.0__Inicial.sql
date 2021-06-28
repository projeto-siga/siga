-- MySQL dump 10.13  Distrib 8.0.16, for Linux (x86_64)
--
-- Host: localhost    Database: sigagc
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
-- Table structure for table `gc_acesso`
--

DROP TABLE IF EXISTS `gc_acesso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_acesso` (
  `id_acesso` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_acesso` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_acesso`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_arquivo`
--

DROP TABLE IF EXISTS `gc_arquivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_arquivo` (
  `id_conteudo` bigint(20) NOT NULL AUTO_INCREMENT,
  `classificacao` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `conteudo` longblob,
  `conteudo_tipo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `titulo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_conteudo`)
) ENGINE=InnoDB AUTO_INCREMENT=4836 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_configuracao`
--

DROP TABLE IF EXISTS `gc_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_configuracao` (
  `id_configuracao_gc` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_tipo_informacao` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_configuracao_gc`),
  KEY `fk5b6c283297a881ad` (`id_tipo_informacao`),
  CONSTRAINT `fk5b6c283297a881ad` FOREIGN KEY (`id_tipo_informacao`) REFERENCES `gc_tipo_informacao` (`id_tipo_informacao`),
  CONSTRAINT `fk5b6c2832f6a487c3` FOREIGN KEY (`id_configuracao_gc`) REFERENCES `corporativo`.`cp_configuracao` (`id_configuracao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_informacao`
--

DROP TABLE IF EXISTS `gc_informacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_informacao` (
  `id_informacao` bigint(20) NOT NULL AUTO_INCREMENT,
  `ano` bigint(20) DEFAULT NULL,
  `dt_elaboracao_fim` datetime(6) DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `id_arquivo` bigint(20) NOT NULL,
  `id_pessoa_titular` bigint(20) NOT NULL,
  `id_acesso_edicao` bigint(20) NOT NULL,
  `his_idc_ini` bigint(20) NOT NULL,
  `id_informacao_pai` bigint(20) DEFAULT NULL,
  `id_lotacao_titular` bigint(20) NOT NULL,
  `id_orgao_usuario` bigint(20) NOT NULL,
  `id_tipo_informacao` bigint(20) NOT NULL,
  `id_acesso` bigint(20) NOT NULL,
  `id_grupo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_informacao`),
  KEY `fk8b6611fc97a881ad` (`id_tipo_informacao`),
  KEY `fk8b6611fc8deefe82` (`id_acesso_edicao`),
  KEY `fk8b6611fc14a981c` (`id_acesso`),
  KEY `fk8b6611fc5c8d8b16` (`id_arquivo`),
  KEY `fk8b6611fc9139cd43` (`id_informacao_pai`),
  KEY `fk8b6611fcfc20d6a3` (`his_idc_ini`),
  KEY `fk8b6611fc4a634b1a` (`id_lotacao_titular`),
  KEY `fk8b6611fcc8778546` (`id_pessoa_titular`),
  KEY `fk8b6611fce2b26866` (`id_orgao_usuario`),
  KEY `gc_informacao_grupo_fk` (`id_grupo`),
  CONSTRAINT `fk8b6611fc14a981c` FOREIGN KEY (`id_acesso`) REFERENCES `gc_acesso` (`id_acesso`),
  CONSTRAINT `fk8b6611fc4a634b1a` FOREIGN KEY (`id_lotacao_titular`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `fk8b6611fc5c8d8b16` FOREIGN KEY (`id_arquivo`) REFERENCES `gc_arquivo` (`id_conteudo`),
  CONSTRAINT `fk8b6611fc8deefe82` FOREIGN KEY (`id_acesso_edicao`) REFERENCES `gc_acesso` (`id_acesso`),
  CONSTRAINT `fk8b6611fc9139cd43` FOREIGN KEY (`id_informacao_pai`) REFERENCES `gc_arquivo` (`id_conteudo`),
  CONSTRAINT `fk8b6611fc97a881ad` FOREIGN KEY (`id_tipo_informacao`) REFERENCES `gc_tipo_informacao` (`id_tipo_informacao`),
  CONSTRAINT `fk8b6611fcc8778546` FOREIGN KEY (`id_pessoa_titular`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `fk8b6611fce2b26866` FOREIGN KEY (`id_orgao_usuario`) REFERENCES `corporativo`.`cp_orgao_usuario` (`id_orgao_usu`),
  CONSTRAINT `fk8b6611fcfc20d6a3` FOREIGN KEY (`his_idc_ini`) REFERENCES `corporativo`.`cp_identidade` (`id_identidade`),
  CONSTRAINT `gc_informacao_grupo_fk` FOREIGN KEY (`id_grupo`) REFERENCES `corporativo`.`cp_grupo` (`id_grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=4831 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_movimentacao`
--

DROP TABLE IF EXISTS `gc_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_movimentacao` (
  `id_movimentacao` bigint(20) NOT NULL AUTO_INCREMENT,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `id_conteudo` bigint(20) DEFAULT NULL,
  `his_idc_ini` bigint(20) NOT NULL,
  `id_informacao` bigint(20) NOT NULL,
  `id_lotacao_atendente` bigint(20) DEFAULT NULL,
  `id_lotacao_titular` bigint(20) DEFAULT NULL,
  `id_movimentacao_canceladora` bigint(20) DEFAULT NULL,
  `id_movimentacao_ref` bigint(20) DEFAULT NULL,
  `id_pessoa_atendente` bigint(20) DEFAULT NULL,
  `id_pessoa_titular` bigint(20) DEFAULT NULL,
  `id_tipo_movimentacao` bigint(20) NOT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_papel` bigint(20) DEFAULT NULL,
  `id_grupo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_movimentacao`),
  KEY `fk3157f386d38b6636` (`id_informacao`),
  KEY `fk3157f386e56b561e` (`id_movimentacao_ref`),
  KEY `fk3157f3864101359e` (`id_conteudo`),
  KEY `fk3157f386a3658f2c` (`id_movimentacao_canceladora`),
  KEY `fk3157f3861debb381` (`id_tipo_movimentacao`),
  KEY `mov_papel_fk` (`id_papel`),
  KEY `fk3157f386fc20d6a3` (`his_idc_ini`),
  KEY `fk3157f386c8778546` (`id_pessoa_titular`),
  KEY `fk3157f3864a634b1a` (`id_lotacao_titular`),
  KEY `fk3157f386bc9a64c1` (`id_pessoa_atendente`),
  KEY `fk3157f3869b156615` (`id_lotacao_atendente`),
  KEY `mov_grupo_fk` (`id_grupo`),
  CONSTRAINT `fk3157f3861debb381` FOREIGN KEY (`id_tipo_movimentacao`) REFERENCES `gc_tipo_movimentacao` (`id_tipo_movimentacao`),
  CONSTRAINT `fk3157f3864101359e` FOREIGN KEY (`id_conteudo`) REFERENCES `gc_arquivo` (`id_conteudo`),
  CONSTRAINT `fk3157f3864a634b1a` FOREIGN KEY (`id_lotacao_titular`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `fk3157f3869b156615` FOREIGN KEY (`id_lotacao_atendente`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `fk3157f386a3658f2c` FOREIGN KEY (`id_movimentacao_canceladora`) REFERENCES `gc_movimentacao` (`id_movimentacao`),
  CONSTRAINT `fk3157f386bc9a64c1` FOREIGN KEY (`id_pessoa_atendente`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `fk3157f386c8778546` FOREIGN KEY (`id_pessoa_titular`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `fk3157f386d38b6636` FOREIGN KEY (`id_informacao`) REFERENCES `gc_informacao` (`id_informacao`),
  CONSTRAINT `fk3157f386e56b561e` FOREIGN KEY (`id_movimentacao_ref`) REFERENCES `gc_movimentacao` (`id_movimentacao`) ON DELETE SET NULL,
  CONSTRAINT `fk3157f386fc20d6a3` FOREIGN KEY (`his_idc_ini`) REFERENCES `corporativo`.`cp_identidade` (`id_identidade`),
  CONSTRAINT `mov_grupo_fk` FOREIGN KEY (`id_grupo`) REFERENCES `corporativo`.`cp_grupo` (`id_grupo`),
  CONSTRAINT `mov_papel_fk` FOREIGN KEY (`id_papel`) REFERENCES `gc_papel` (`id_papel`)
) ENGINE=InnoDB AUTO_INCREMENT=4843 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_papel`
--

DROP TABLE IF EXISTS `gc_papel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_papel` (
  `id_papel` bigint(20) NOT NULL AUTO_INCREMENT,
  `desc_papel` varchar(40) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_papel`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_tag`
--

DROP TABLE IF EXISTS `gc_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_tag` (
  `id_tag` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `titulo` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tipo_id_tipo_tag` bigint(20) NOT NULL,
  PRIMARY KEY (`id_tag`),
  KEY `fk7d04ad97f23572ce` (`tipo_id_tipo_tag`),
  CONSTRAINT `fk7d04ad97f23572ce` FOREIGN KEY (`tipo_id_tipo_tag`) REFERENCES `gc_tipo_tag` (`id_tipo_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=4718 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_tag_x_informacao`
--

DROP TABLE IF EXISTS `gc_tag_x_informacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_tag_x_informacao` (
  `id_informacao` bigint(20) NOT NULL,
  `id_tag` bigint(20) NOT NULL,
  PRIMARY KEY (`id_informacao`,`id_tag`),
  KEY `fk6dcf9ea8635922f0` (`id_tag`),
  CONSTRAINT `fk6dcf9ea8635922f0` FOREIGN KEY (`id_tag`) REFERENCES `gc_tag` (`id_tag`),
  CONSTRAINT `fk6dcf9ea8d38b6636` FOREIGN KEY (`id_informacao`) REFERENCES `gc_informacao` (`id_informacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_tipo_informacao`
--

DROP TABLE IF EXISTS `gc_tipo_informacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_tipo_informacao` (
  `id_tipo_informacao` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_tipo_informacao` varchar(255) COLLATE utf8_bin NOT NULL,
  `arquivo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_informacao`),
  KEY `tipo_informacao_arquivo_fk` (`arquivo`),
  CONSTRAINT `tipo_informacao_arquivo_fk` FOREIGN KEY (`arquivo`) REFERENCES `gc_arquivo` (`id_conteudo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_tipo_movimentacao`
--

DROP TABLE IF EXISTS `gc_tipo_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_tipo_movimentacao` (
  `id_tipo_movimentacao` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_tipo_movimentacao` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo_movimentacao`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gc_tipo_tag`
--

DROP TABLE IF EXISTS `gc_tipo_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gc_tipo_tag` (
  `id_tipo_tag` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome_tipo_tag` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ht_cp_configuracao`
--

DROP TABLE IF EXISTS `ht_cp_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ht_cp_configuracao` (
  `id_configuracao` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ht_gc_configuracao`
--

DROP TABLE IF EXISTS `ht_gc_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ht_gc_configuracao` (
  `id_configuracao_gc` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ri_atualizacao`
--

DROP TABLE IF EXISTS `ri_atualizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ri_atualizacao` (
  `id_atualizacao` bigint(20) NOT NULL AUTO_INCREMENT,
  `desempate_atualizacao` bigint(20) DEFAULT NULL,
  `nome_atualizacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sigla_atualizacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `dt_ultima_atualizacao` datetime(6) DEFAULT NULL,
  `url_atualizacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_atualizacao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ri_configuracao`
--

DROP TABLE IF EXISTS `ri_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ri_configuracao` (
  `id_configuracao_ri` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id_configuracao_ri`),
  CONSTRAINT `fk830c72b7f6a4891e` FOREIGN KEY (`id_configuracao_ri`) REFERENCES `corporativo`.`cp_configuracao` (`id_configuracao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'sigagc'
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

-- Dump completed on 2021-06-25 11:23:06
