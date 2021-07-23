CREATE DATABASE  IF NOT EXISTS `sigasr` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sigasr`;

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
-- Table structure for table `sr_acao`
--

DROP TABLE IF EXISTS `sr_acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_acao` (
  `id_acao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descr_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sigla_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `titulo_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_pai` INT UNSIGNED DEFAULT NULL,
  `tipo_acao` INT UNSIGNED DEFAULT NULL,
  `tipo_execucao` INT UNSIGNED DEFAULT NULL,
  `forma_atendimento` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_acao`),
  KEY `tipo_acao` (`tipo_acao`),
  CONSTRAINT `sr_acao_ibfk_1` FOREIGN KEY (`tipo_acao`) REFERENCES `sr_tipo_acao` (`id_tipo_acao`)
) ENGINE=InnoDB AUTO_INCREMENT=607 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_acordo`
--

DROP TABLE IF EXISTS `sr_acordo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_acordo` (
  `id_acordo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) NOT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `nome_acordo` varchar(255) COLLATE utf8_bin NOT NULL,
  `descr_acordo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_acordo`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_arquivo`
--

DROP TABLE IF EXISTS `sr_arquivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_arquivo` (
  `id_arquivo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `blob` longblob,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nome_arquivo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_arquivo`)
) ENGINE=InnoDB AUTO_INCREMENT=356 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_atributo`
--

DROP TABLE IF EXISTS `sr_atributo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_atributo` (
  `id_atributo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descricao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tipo_atributo` INT UNSIGNED DEFAULT NULL,
  `descr_pre_definido` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `id_objetivo` INT UNSIGNED DEFAULT NULL,
  `codigo_atributo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_atributo`),
  KEY `atributo_objetivo_fk` (`id_objetivo`),
  CONSTRAINT `atributo_objetivo_fk` FOREIGN KEY (`id_objetivo`) REFERENCES `sr_objetivo_atributo` (`id_objetivo`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_atributo_acordo`
--

DROP TABLE IF EXISTS `sr_atributo_acordo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_atributo_acordo` (
  `id_atributo_acordo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) NOT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `operador` INT UNSIGNED DEFAULT NULL,
  `valor` INT UNSIGNED DEFAULT NULL,
  `unidade_medida` INT UNSIGNED DEFAULT NULL,
  `id_atributo` INT UNSIGNED DEFAULT NULL,
  `id_acordo` INT UNSIGNED DEFAULT NULL,
  `parametro` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_atributo_acordo`),
  KEY `atributo_acordo_acordo_fk` (`id_acordo`),
  KEY `parametro_acordo_tipo_fk` (`id_atributo`),
  CONSTRAINT `atributo_acordo_acordo_fk` FOREIGN KEY (`id_acordo`) REFERENCES `sr_acordo` (`id_acordo`),
  CONSTRAINT `parametro_acordo_tipo_fk` FOREIGN KEY (`id_atributo`) REFERENCES `sr_atributo` (`id_atributo`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_atributo_solicitacao`
--

DROP TABLE IF EXISTS `sr_atributo_solicitacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_atributo_solicitacao` (
  `id_atributo_solicitacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `valor_atributo_solicitacao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_solicitacao` INT UNSIGNED DEFAULT NULL,
  `id_atributo` INT UNSIGNED DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `id_cadastrante` INT UNSIGNED DEFAULT NULL,
  `id_lota_cadastrante` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_atributo_solicitacao`),
  KEY `atributo_solicitacao_fk` (`id_solicitacao`),
  CONSTRAINT `atributo_solicitacao_fk` FOREIGN KEY (`id_solicitacao`) REFERENCES `sr_solicitacao` (`id_solicitacao`)
) ENGINE=InnoDB AUTO_INCREMENT=814 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_configuracao`
--

DROP TABLE IF EXISTS `sr_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_configuracao` (
  `id_configuracao_sr` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fg_atributo_obrigatorio` char(1) COLLATE utf8_bin DEFAULT NULL,
  `forma_acompanhamento` INT UNSIGNED DEFAULT NULL,
  `gravidade` INT UNSIGNED DEFAULT NULL,
  `id_pesquisa` INT UNSIGNED DEFAULT NULL,
  `tendencia` INT UNSIGNED DEFAULT NULL,
  `urgencia` INT UNSIGNED DEFAULT NULL,
  `id_atendente` INT UNSIGNED DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED DEFAULT NULL,
  `id_pos_atendente` INT UNSIGNED DEFAULT NULL,
  `id_pre_atendente` INT UNSIGNED DEFAULT NULL,
  `id_acao` INT UNSIGNED DEFAULT NULL,
  `id_tipo_atributo` INT UNSIGNED DEFAULT NULL,
  `id_equipe_qualidade` INT UNSIGNED DEFAULT NULL,
  `id_lista` INT UNSIGNED DEFAULT NULL,
  `sla_pre_atendimento_quant` INT UNSIGNED DEFAULT NULL,
  `id_unidade_pre_atendimento` INT UNSIGNED DEFAULT NULL,
  `sla_atendimento_quant` INT UNSIGNED DEFAULT NULL,
  `id_unidade_atendimento` INT UNSIGNED DEFAULT NULL,
  `sla_pos_atendimento_quant` INT UNSIGNED DEFAULT NULL,
  `id_unidade_pos_atendimento` INT UNSIGNED DEFAULT NULL,
  `margem_seguranca` INT UNSIGNED DEFAULT NULL,
  `observacao_sla` longtext COLLATE utf8_bin,
  `fg_divulgar_sla` char(1) COLLATE utf8_bin DEFAULT NULL,
  `fg_notificar_gestor` char(1) COLLATE utf8_bin DEFAULT NULL,
  `fg_notificar_solicitante` char(1) COLLATE utf8_bin DEFAULT NULL,
  `fg_notificar_cadastrante` char(1) COLLATE utf8_bin DEFAULT NULL,
  `fg_notificar_interlocutor` char(1) COLLATE utf8_bin DEFAULT NULL,
  `fg_notificar_atendente` char(1) COLLATE utf8_bin DEFAULT NULL,
  `tipo_permissao` smallint(6) unsigned DEFAULT NULL,
  `prioridade` INT UNSIGNED DEFAULT NULL,
  `id_acordo` INT UNSIGNED DEFAULT NULL,
  `prioridade_lista` smallint(6) unsigned DEFAULT NULL,
  `id_horario` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_configuracao_sr`),
  KEY `sr_conf_lista_fk` (`id_lista`),
  KEY `configuracao_acordo_fk` (`id_acordo`),
  KEY `configuracao_servico_fk` (`id_acao`),
  KEY `configuracao_item_fk` (`id_item_configuracao`),
  KEY `sr_horario_fk` (`id_horario`),
  KEY `id_unidade_pos_atendimento` (`id_unidade_pos_atendimento`),
  KEY `id_unidade_atendimento` (`id_unidade_atendimento`),
  KEY `id_unidade_pre_atendimento` (`id_unidade_pre_atendimento`),
  KEY `sr_conf_equipe_qualidade_fk` (`id_equipe_qualidade`),
  CONSTRAINT `configuracao_acordo_fk` FOREIGN KEY (`id_acordo`) REFERENCES `sr_acordo` (`id_acordo`),
  CONSTRAINT `configuracao_item_fk` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`),
  CONSTRAINT `configuracao_servico_fk` FOREIGN KEY (`id_acao`) REFERENCES `sr_acao` (`id_acao`),
  CONSTRAINT `sr_conf_equipe_qualidade_fk` FOREIGN KEY (`id_equipe_qualidade`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_conf_lista_fk` FOREIGN KEY (`id_lista`) REFERENCES `sr_lista` (`id_lista`),
  CONSTRAINT `sr_configuracao_ibfk_1` FOREIGN KEY (`id_unidade_pos_atendimento`) REFERENCES `corporativo`.`cp_unidade_medida` (`id_unidade_medida`),
  CONSTRAINT `sr_configuracao_ibfk_2` FOREIGN KEY (`id_unidade_atendimento`) REFERENCES `corporativo`.`cp_unidade_medida` (`id_unidade_medida`),
  CONSTRAINT `sr_configuracao_ibfk_3` FOREIGN KEY (`id_unidade_pre_atendimento`) REFERENCES `corporativo`.`cp_unidade_medida` (`id_unidade_medida`),
  CONSTRAINT `sr_horario_fk` FOREIGN KEY (`id_horario`) REFERENCES `sr_horario` (`id_horario`)
) ENGINE=InnoDB AUTO_INCREMENT=796703 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_configuracao_acao`
--

DROP TABLE IF EXISTS `sr_configuracao_acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_configuracao_acao` (
  `id_configuracao` INT UNSIGNED NOT NULL,
  `id_acao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_configuracao`,`id_acao`),
  KEY `fk_configuracao_acao` (`id_acao`),
  CONSTRAINT `fk_config_acao_configuracao` FOREIGN KEY (`id_configuracao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `fk_configuracao_acao` FOREIGN KEY (`id_acao`) REFERENCES `sr_acao` (`id_acao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_configuracao_ignorada`
--

DROP TABLE IF EXISTS `sr_configuracao_ignorada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_configuracao_ignorada` (
  `id_configuracao_ignorada` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_item_configuracao` INT UNSIGNED NOT NULL,
  `id_configuracao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_configuracao_ignorada`),
  KEY `fk_conf_ign_configuracao` (`id_configuracao`),
  KEY `fk_conf_ign_item` (`id_item_configuracao`),
  CONSTRAINT `fk_conf_ign_configuracao` FOREIGN KEY (`id_configuracao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `fk_conf_ign_item` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_configuracao_item`
--

DROP TABLE IF EXISTS `sr_configuracao_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_configuracao_item` (
  `id_item_configuracao` INT UNSIGNED NOT NULL,
  `id_configuracao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_item_configuracao`,`id_configuracao`),
  KEY `fk_config_configuracao` (`id_configuracao`),
  CONSTRAINT `fk_config_configuracao` FOREIGN KEY (`id_configuracao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `fk_config_item_configuracao` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_configuracao_permissao`
--

DROP TABLE IF EXISTS `sr_configuracao_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_configuracao_permissao` (
  `id_configuracao` INT UNSIGNED NOT NULL,
  `tipo_permissao` smallint(6) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_disponibilidade`
--

DROP TABLE IF EXISTS `sr_disponibilidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_disponibilidade` (
  `id_disponibilidade` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `his_dt_ini` datetime NOT NULL,
  `his_dt_fim` datetime DEFAULT NULL,
  `dt_hr_ini` datetime(6) DEFAULT NULL,
  `dt_hr_fim` datetime(6) DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED NOT NULL,
  `id_orgao_usu` INT UNSIGNED NOT NULL,
  `tp_disponibilidade` smallint(6) unsigned DEFAULT NULL,
  `msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `det_tecnico` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_disponibilidade`),
  KEY `his_id_ini` (`his_id_ini`),
  KEY `id_item_configuracao` (`id_item_configuracao`),
  CONSTRAINT `sr_disponibilidade_ibfk_1` FOREIGN KEY (`his_id_ini`) REFERENCES `sr_disponibilidade` (`id_disponibilidade`),
  CONSTRAINT `sr_disponibilidade_ibfk_2` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_equipe`
--

DROP TABLE IF EXISTS `sr_equipe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_equipe` (
  `id_equipe` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_lota_equipe` INT UNSIGNED NOT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_equipe`),
  KEY `fk_equipe_lotacao` (`id_lota_equipe`),
  CONSTRAINT `fk_equipe_lotacao` FOREIGN KEY (`id_lota_equipe`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_excecao_horario`
--

DROP TABLE IF EXISTS `sr_excecao_horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_excecao_horario` (
  `id_excecao_horario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_equipe` INT UNSIGNED NOT NULL,
  `dia_semana` smallint(6) unsigned DEFAULT NULL,
  `dt_especifica` datetime(6) DEFAULT NULL,
  `hora_ini` datetime(6) NOT NULL,
  `hora_fim` datetime(6) NOT NULL,
  `inter_ini` datetime(6) NOT NULL,
  `inter_fim` datetime(6) NOT NULL,
  PRIMARY KEY (`id_excecao_horario`),
  KEY `fk_ex_hor_equipe` (`id_equipe`),
  CONSTRAINT `fk_ex_hor_equipe` FOREIGN KEY (`id_equipe`) REFERENCES `sr_equipe` (`id_equipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_fator_multiplicacao`
--

DROP TABLE IF EXISTS `sr_fator_multiplicacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_fator_multiplicacao` (
  `id_fator_multiplicacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `num_fator_multiplicacao` INT UNSIGNED NOT NULL DEFAULT '1',
  `id_pessoa` INT UNSIGNED DEFAULT NULL,
  `id_lotacao` INT UNSIGNED DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_fator_multiplicacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_gestor_item`
--

DROP TABLE IF EXISTS `sr_gestor_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_gestor_item` (
  `id_gestor_item` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_pessoa` INT UNSIGNED DEFAULT NULL,
  `id_lotacao` INT UNSIGNED DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_gestor_item`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_horario`
--

DROP TABLE IF EXISTS `sr_horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_horario` (
  `id_horario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `hora_inicio` smallint(6) unsigned NOT NULL,
  `hora_fim` smallint(6) unsigned NOT NULL,
  `dia_semana_inicio` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `dia_semana_fim` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `dt_inicio` datetime DEFAULT NULL,
  `dt_fim` datetime DEFAULT NULL,
  `descr_horario` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_horario`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_item_configuracao`
--

DROP TABLE IF EXISTS `sr_item_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_item_configuracao` (
  `id_item_configuracao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descr_item_configuracao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sigla_item_configuracao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `titulo_item_configuracao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_pai` INT UNSIGNED DEFAULT NULL,
  `descr_similaridade` longtext COLLATE utf8_bin,
  `num_fator_multiplicacao_geral` INT UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_item_configuracao`)
) ENGINE=InnoDB AUTO_INCREMENT=936 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_lista`
--

DROP TABLE IF EXISTS `sr_lista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_lista` (
  `id_lista` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dt_reg` datetime(6) DEFAULT NULL,
  `nome_lista` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_lota_cadastrante` INT UNSIGNED NOT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `descr_abrangencia` longtext COLLATE utf8_bin,
  `descr_justificativa` longtext COLLATE utf8_bin,
  `descr_priorizacao` longtext COLLATE utf8_bin,
  PRIMARY KEY (`id_lista`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_lista_configuracao`
--

DROP TABLE IF EXISTS `sr_lista_configuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_lista_configuracao` (
  `id_configuracao` INT UNSIGNED NOT NULL,
  `id_lista` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_configuracao`,`id_lista`),
  KEY `sr_lista_configuracao_fk2` (`id_lista`),
  CONSTRAINT `sr_lista_configuracao_fk1` FOREIGN KEY (`id_configuracao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `sr_lista_configuracao_fk2` FOREIGN KEY (`id_lista`) REFERENCES `sr_lista` (`id_lista`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_movimentacao`
--

DROP TABLE IF EXISTS `sr_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_movimentacao` (
  `id_movimentacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descr_movimentacao` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `dt_ini_mov` datetime(6) DEFAULT NULL,
  `id_arquivo` INT UNSIGNED DEFAULT NULL,
  `id_atendente` INT UNSIGNED DEFAULT NULL,
  `id_cadastrante` INT UNSIGNED NOT NULL,
  `id_lota_atendente` INT UNSIGNED DEFAULT NULL,
  `id_lota_cadastrante` INT UNSIGNED NOT NULL,
  `id_solicitacao` INT UNSIGNED DEFAULT NULL,
  `num_sequencia` smallint(6) unsigned DEFAULT NULL,
  `id_prioridade` INT UNSIGNED DEFAULT NULL,
  `id_tipo_movimentacao` INT UNSIGNED NOT NULL,
  `id_mov_canceladora` INT UNSIGNED DEFAULT NULL,
  `id_lista` INT UNSIGNED DEFAULT NULL,
  `id_pesquisa` INT UNSIGNED DEFAULT NULL,
  `hor_ini_agendamento` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `dt_ini_agendamento` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `dt_agendamento` datetime(6) DEFAULT NULL,
  `motivopendencia` INT UNSIGNED DEFAULT NULL,
  `id_mov_finalizadora` INT UNSIGNED DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED DEFAULT NULL,
  `id_acao` INT UNSIGNED DEFAULT NULL,
  `motivoescalonamento` INT UNSIGNED DEFAULT NULL,
  `id_solicitacao_referencia` INT UNSIGNED DEFAULT NULL,
  `id_designacao` INT UNSIGNED DEFAULT NULL,
  `id_titular` INT UNSIGNED DEFAULT NULL,
  `id_lota_titular` INT UNSIGNED DEFAULT NULL,
  `prioridade` INT UNSIGNED DEFAULT NULL,
  `motivofechamento` INT UNSIGNED DEFAULT NULL,
  `dnm_tempo_decorrido_atendmto` INT UNSIGNED DEFAULT NULL,
  `conhecimento` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_movimentacao`),
  KEY `id_designacao` (`id_designacao`),
  KEY `sr_mov_pesquisa_fk` (`id_pesquisa`),
  KEY `id_acao` (`id_acao`),
  KEY `id_item_configuracao` (`id_item_configuracao`),
  KEY `id_solicitacao_referencia` (`id_solicitacao_referencia`),
  KEY `sr_mov_lista_fk` (`id_lista`),
  KEY `id_mov_finalizadora` (`id_mov_finalizadora`),
  KEY `sr_mov_tipo_mov_fk` (`id_tipo_movimentacao`),
  KEY `andamento_solicitacao_fk` (`id_solicitacao`),
  KEY `andamento_arquivo_fk` (`id_arquivo`),
  KEY `sr_mov_canceladora_fk` (`id_mov_canceladora`),
  KEY `id_lota_atendente` (`id_lota_atendente`),
  KEY `id_atendente` (`id_atendente`),
  KEY `id_lota_cadastrante` (`id_lota_cadastrante`),
  KEY `id_cadastrante` (`id_cadastrante`),
  KEY `id_lota_titular` (`id_lota_titular`),
  KEY `id_titular` (`id_titular`),
  CONSTRAINT `andamento_arquivo_fk` FOREIGN KEY (`id_arquivo`) REFERENCES `sr_arquivo` (`id_arquivo`),
  CONSTRAINT `andamento_solicitacao_fk` FOREIGN KEY (`id_solicitacao`) REFERENCES `sr_solicitacao` (`id_solicitacao`),
  CONSTRAINT `sr_mov_canceladora_fk` FOREIGN KEY (`id_mov_canceladora`) REFERENCES `sr_movimentacao` (`id_movimentacao`),
  CONSTRAINT `sr_mov_lista_fk` FOREIGN KEY (`id_lista`) REFERENCES `sr_lista` (`id_lista`),
  CONSTRAINT `sr_mov_pesquisa_fk` FOREIGN KEY (`id_pesquisa`) REFERENCES `sr_pesquisa` (`id_pesquisa`),
  CONSTRAINT `sr_mov_tipo_mov_fk` FOREIGN KEY (`id_tipo_movimentacao`) REFERENCES `sr_tipo_movimentacao` (`id_tipo_movimentacao`),
  CONSTRAINT `sr_movimentacao_ibfk_1` FOREIGN KEY (`id_designacao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `sr_movimentacao_ibfk_10` FOREIGN KEY (`id_lota_titular`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_movimentacao_ibfk_11` FOREIGN KEY (`id_titular`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_movimentacao_ibfk_2` FOREIGN KEY (`id_acao`) REFERENCES `sr_acao` (`id_acao`),
  CONSTRAINT `sr_movimentacao_ibfk_3` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`),
  CONSTRAINT `sr_movimentacao_ibfk_4` FOREIGN KEY (`id_solicitacao_referencia`) REFERENCES `sr_solicitacao` (`id_solicitacao`),
  CONSTRAINT `sr_movimentacao_ibfk_5` FOREIGN KEY (`id_mov_finalizadora`) REFERENCES `sr_movimentacao` (`id_movimentacao`),
  CONSTRAINT `sr_movimentacao_ibfk_6` FOREIGN KEY (`id_lota_atendente`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_movimentacao_ibfk_7` FOREIGN KEY (`id_atendente`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_movimentacao_ibfk_8` FOREIGN KEY (`id_lota_cadastrante`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_movimentacao_ibfk_9` FOREIGN KEY (`id_cadastrante`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`)
) ENGINE=InnoDB AUTO_INCREMENT=2298 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_movimentacao_acordo`
--

DROP TABLE IF EXISTS `sr_movimentacao_acordo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_movimentacao_acordo` (
  `id_acordo` INT UNSIGNED NOT NULL,
  `id_movimentacao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_acordo`,`id_movimentacao`),
  KEY `fk_sol_acordo_movimentacao` (`id_movimentacao`),
  CONSTRAINT `fk_mov_acordo_acordo` FOREIGN KEY (`id_acordo`) REFERENCES `sr_acordo` (`id_acordo`),
  CONSTRAINT `fk_sol_acordo_movimentacao` FOREIGN KEY (`id_movimentacao`) REFERENCES `sr_movimentacao` (`id_movimentacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_objetivo_atributo`
--

DROP TABLE IF EXISTS `sr_objetivo_atributo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_objetivo_atributo` (
  `id_objetivo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descr_objetivo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_objetivo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_pergunta`
--

DROP TABLE IF EXISTS `sr_pergunta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_pergunta` (
  `id_pergunta` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) NOT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descr_pergunta` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ordem_pergunta` INT UNSIGNED DEFAULT NULL,
  `id_tipo_pergunta` INT UNSIGNED NOT NULL,
  `id_pesquisa` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_pergunta`),
  KEY `pergunta_pesquisa_fk` (`id_pesquisa`),
  KEY `pergunta_tipo_fk` (`id_tipo_pergunta`),
  CONSTRAINT `pergunta_pesquisa_fk` FOREIGN KEY (`id_pesquisa`) REFERENCES `sr_pesquisa` (`id_pesquisa`),
  CONSTRAINT `pergunta_tipo_fk` FOREIGN KEY (`id_tipo_pergunta`) REFERENCES `sr_tipo_pergunta` (`id_tipo_pergunta`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_pesquisa`
--

DROP TABLE IF EXISTS `sr_pesquisa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_pesquisa` (
  `id_pesquisa` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) NOT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `nome_pesquisa` varchar(255) COLLATE utf8_bin NOT NULL,
  `descr_pesquisa` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_pesquisa`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_prioridade_solicitacao`
--

DROP TABLE IF EXISTS `sr_prioridade_solicitacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_prioridade_solicitacao` (
  `id_prioridade_solicitacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_lista` INT UNSIGNED NOT NULL,
  `id_solicitacao` INT UNSIGNED NOT NULL,
  `num_posicao` INT UNSIGNED DEFAULT NULL,
  `prioridade` smallint(6) unsigned DEFAULT NULL,
  `nao_reposicionar_automatico` char(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_prioridade_solicitacao`),
  KEY `id_lista` (`id_lista`),
  KEY `id_solicitacao` (`id_solicitacao`),
  CONSTRAINT `sr_prioridade_solicitacao_ibfk_1` FOREIGN KEY (`id_lista`) REFERENCES `sr_lista` (`id_lista`),
  CONSTRAINT `sr_prioridade_solicitacao_ibfk_2` FOREIGN KEY (`id_solicitacao`) REFERENCES `sr_solicitacao` (`id_solicitacao`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_resposta`
--

DROP TABLE IF EXISTS `sr_resposta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_resposta` (
  `id_resposta` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descr_resposta` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `valor_resposta` INT UNSIGNED DEFAULT NULL,
  `id_pergunta` INT UNSIGNED DEFAULT NULL,
  `id_avaliacao` INT UNSIGNED DEFAULT NULL,
  `id_movimentacao` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id_resposta`),
  KEY `resposta_pergunta_fk` (`id_pergunta`),
  KEY `resposta_avaliacao_fk` (`id_avaliacao`),
  KEY `resposta_movimentacao_fk` (`id_movimentacao`),
  CONSTRAINT `resposta_avaliacao_fk` FOREIGN KEY (`id_avaliacao`) REFERENCES `sr_movimentacao` (`id_movimentacao`),
  CONSTRAINT `resposta_movimentacao_fk` FOREIGN KEY (`id_movimentacao`) REFERENCES `sr_movimentacao` (`id_movimentacao`),
  CONSTRAINT `resposta_pergunta_fk` FOREIGN KEY (`id_pergunta`) REFERENCES `sr_pergunta` (`id_pergunta`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_solicitacao`
--

DROP TABLE IF EXISTS `sr_solicitacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_solicitacao` (
  `id_solicitacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descr_solicitacao` longtext COLLATE utf8_bin,
  `dt_reg` datetime(6) DEFAULT NULL,
  `formaacompanhamento` INT UNSIGNED DEFAULT NULL,
  `gravidade` INT UNSIGNED DEFAULT NULL,
  `num_sequencia` INT UNSIGNED DEFAULT NULL,
  `num_solicitacao` INT UNSIGNED DEFAULT NULL,
  `tel_principal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tendencia` INT UNSIGNED DEFAULT NULL,
  `urgencia` INT UNSIGNED DEFAULT NULL,
  `id_arquivo` INT UNSIGNED DEFAULT NULL,
  `id_cadastrante` INT UNSIGNED DEFAULT NULL,
  `id_item_configuracao` INT UNSIGNED DEFAULT NULL,
  `id_complexo` INT UNSIGNED DEFAULT NULL,
  `id_lota_cadastrante` INT UNSIGNED DEFAULT NULL,
  `id_lota_solicitante` INT UNSIGNED DEFAULT NULL,
  `id_orgao_usu` INT UNSIGNED DEFAULT NULL,
  `id_acao` INT UNSIGNED DEFAULT NULL,
  `id_solicitacao_pai` INT UNSIGNED DEFAULT NULL,
  `id_solicitante` INT UNSIGNED DEFAULT NULL,
  `prioridade` INT UNSIGNED DEFAULT NULL,
  `id_interlocutor` INT UNSIGNED DEFAULT NULL,
  `meiocomunicacao` INT UNSIGNED DEFAULT NULL,
  `fg_rascunho` char(1) COLLATE utf8_bin DEFAULT NULL,
  `dt_edicao_ini` datetime(6) DEFAULT NULL,
  `dt_origem` datetime(6) DEFAULT NULL,
  `descr_codigo` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `fechado_automaticamente` char(1) COLLATE utf8_bin DEFAULT NULL,
  `json_solicitacao` longtext COLLATE utf8_bin,
  `dt_atualizacao_json` datetime(6) DEFAULT NULL,
  `id_designacao` INT UNSIGNED DEFAULT NULL,
  `id_titular` INT UNSIGNED DEFAULT NULL,
  `id_lota_titular` INT UNSIGNED DEFAULT NULL,
  `dnm_id_item_configuracao` INT UNSIGNED DEFAULT NULL,
  `dnm_id_acao` INT UNSIGNED DEFAULT NULL,
  `dnm_id_ult_mov` INT UNSIGNED DEFAULT NULL,
  `dnm_prioridade_tecnica` INT UNSIGNED DEFAULT NULL,
  `dnm_tempo_decorrido_cadastro` INT UNSIGNED DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_solicitacao`),
  UNIQUE KEY `unique_sol_num_idx` (`descr_codigo`,`his_dt_fim`),
  KEY `id_orgao_usu` (`id_orgao_usu`),
  KEY `solicitacao_servico_fk` (`id_acao`),
  KEY `solicitacao_item_fk` (`id_item_configuracao`),
  KEY `solicitacao_arquivo_fk` (`id_arquivo`),
  KEY `solicitacao_solicitacaopai_fk` (`id_solicitacao_pai`),
  KEY `solicitacao_solicitacaoini_fk` (`his_id_ini`),
  KEY `id_designacao` (`id_designacao`),
  KEY `id_complexo` (`id_complexo`),
  KEY `id_interlocutor` (`id_interlocutor`),
  KEY `id_lota_solicitante` (`id_lota_solicitante`),
  KEY `id_solicitante` (`id_solicitante`),
  KEY `id_lota_cadastrante` (`id_lota_cadastrante`),
  KEY `id_cadastrante` (`id_cadastrante`),
  KEY `id_lota_titular` (`id_lota_titular`),
  KEY `id_titular` (`id_titular`),
  CONSTRAINT `solicitacao_arquivo_fk` FOREIGN KEY (`id_arquivo`) REFERENCES `sr_arquivo` (`id_arquivo`),
  CONSTRAINT `solicitacao_item_fk` FOREIGN KEY (`id_item_configuracao`) REFERENCES `sr_item_configuracao` (`id_item_configuracao`),
  CONSTRAINT `solicitacao_servico_fk` FOREIGN KEY (`id_acao`) REFERENCES `sr_acao` (`id_acao`),
  CONSTRAINT `solicitacao_solicitacaoini_fk` FOREIGN KEY (`his_id_ini`) REFERENCES `sr_solicitacao` (`id_solicitacao`),
  CONSTRAINT `solicitacao_solicitacaopai_fk` FOREIGN KEY (`id_solicitacao_pai`) REFERENCES `sr_solicitacao` (`id_solicitacao`),
  CONSTRAINT `sr_solicitacao_ibfk_1` FOREIGN KEY (`id_orgao_usu`) REFERENCES `corporativo`.`cp_orgao_usuario` (`id_orgao_usu`),
  CONSTRAINT `sr_solicitacao_ibfk_10` FOREIGN KEY (`id_titular`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_solicitacao_ibfk_2` FOREIGN KEY (`id_designacao`) REFERENCES `sr_configuracao` (`id_configuracao_sr`),
  CONSTRAINT `sr_solicitacao_ibfk_3` FOREIGN KEY (`id_complexo`) REFERENCES `corporativo`.`cp_complexo` (`id_complexo`),
  CONSTRAINT `sr_solicitacao_ibfk_4` FOREIGN KEY (`id_interlocutor`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_solicitacao_ibfk_5` FOREIGN KEY (`id_lota_solicitante`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_solicitacao_ibfk_6` FOREIGN KEY (`id_solicitante`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_solicitacao_ibfk_7` FOREIGN KEY (`id_lota_cadastrante`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`),
  CONSTRAINT `sr_solicitacao_ibfk_8` FOREIGN KEY (`id_cadastrante`) REFERENCES `corporativo`.`dp_pessoa` (`id_pessoa`),
  CONSTRAINT `sr_solicitacao_ibfk_9` FOREIGN KEY (`id_lota_titular`) REFERENCES `corporativo`.`dp_lotacao` (`id_lotacao`)
) ENGINE=InnoDB AUTO_INCREMENT=1621 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_solicitacao_acordo`
--

DROP TABLE IF EXISTS `sr_solicitacao_acordo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_solicitacao_acordo` (
  `id_acordo` INT UNSIGNED NOT NULL,
  `id_solicitacao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_acordo`,`id_solicitacao`),
  KEY `fk_sol_acordo_solicitacao` (`id_solicitacao`),
  CONSTRAINT `fk_sol_acordo_acordo` FOREIGN KEY (`id_acordo`) REFERENCES `sr_acordo` (`id_acordo`),
  CONSTRAINT `fk_sol_acordo_solicitacao` FOREIGN KEY (`id_solicitacao`) REFERENCES `sr_solicitacao` (`id_solicitacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_tipo_acao`
--

DROP TABLE IF EXISTS `sr_tipo_acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_tipo_acao` (
  `id_tipo_acao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `his_ativo` INT UNSIGNED DEFAULT NULL,
  `his_dt_fim` datetime(6) DEFAULT NULL,
  `his_dt_ini` datetime(6) DEFAULT NULL,
  `his_id_ini` INT UNSIGNED DEFAULT NULL,
  `descr_tipo_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sigla_tipo_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `titulo_tipo_acao` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `id_pai` INT UNSIGNED DEFAULT NULL,
  `ust` decimal(10,5) DEFAULT '0.00000',
  PRIMARY KEY (`id_tipo_acao`),
  KEY `tipo_acao_tipo_acao_pai_fk` (`id_pai`),
  CONSTRAINT `tipo_acao_tipo_acao_pai_fk` FOREIGN KEY (`id_pai`) REFERENCES `sr_tipo_acao` (`id_tipo_acao`)
) ENGINE=InnoDB AUTO_INCREMENT=514 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_tipo_movimentacao`
--

DROP TABLE IF EXISTS `sr_tipo_movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_tipo_movimentacao` (
  `id_tipo_movimentacao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome_tipo_movimentacao` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id_tipo_movimentacao`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_tipo_pergunta`
--

DROP TABLE IF EXISTS `sr_tipo_pergunta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_tipo_pergunta` (
  `id_tipo_pergunta` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome_tipo_pergunta` varchar(255) COLLATE utf8_bin NOT NULL,
  `descr_tipo_pergunta` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_tipo_pergunta`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sr_tipo_permissao_lista`
--

DROP TABLE IF EXISTS `sr_tipo_permissao_lista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sr_tipo_permissao_lista` (
  `id_tipo_permissao` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descr_tipo_permissao` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id_tipo_permissao`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'sigagc'
--
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

insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (1, 'Início do Atendimento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (2, 'Andamento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (3, 'Inclusão em Lista');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (4, 'Início do Pré-Atendimento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (5, 'Início do Pós-Atendimento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (6, 'Retirada de Lista');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (7, 'Fechamento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (8, 'Cancelamento de Solicitação');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (9, 'Início de Pendência');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (10, 'Reabertura');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (11, 'Término de Pendência');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (12, 'Anexação de Arquivo');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (13, 'Alteração de Prioridade em Lista');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (14, 'Cancelamento de Movimentação');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (15, 'Fechamento Parcial');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (16, 'Avaliação');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (17, 'Início do Controle de Qualidade');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (18, 'Junta à outra Solicitação');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (19, 'Desentranhamento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (20, 'Vinculo de Solicitação');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (21, 'Replanejamento');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (22, 'Em Elaboração');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) VALUES (23, 'Exclusão');
insert into sigasr.sr_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (24, 'Escalonamento');

insert into sigasr.sr_tipo_pergunta values (1, 'Texto Livre', null);
insert into sigasr.sr_tipo_pergunta values (2, 'Nota de 1 a 5', null);

insert into sigasr.sr_horario(HORA_INICIO, HORA_FIM, DIA_SEMANA_INICIO, DIA_SEMANA_FIM, DT_INICIO, DT_FIM, DESCR_HORARIO) values(11,19,'Segunda-feira','Sexta-feira','2015-01-01', null,'Horário padrão da Justiça Federal');
insert into sigasr.sr_horario(HORA_INICIO, HORA_FIM, DIA_SEMANA_INICIO, DIA_SEMANA_FIM, DT_INICIO, DT_FIM, DESCR_HORARIO) values(10,19,'Segunda-feira','Sexta-feira','2015-01-01', null,'Horário do Suporte Local');
insert into sigasr.sr_horario(HORA_INICIO, HORA_FIM, DIA_SEMANA_INICIO, DIA_SEMANA_FIM, DT_INICIO, DT_FIM, DESCR_HORARIO) values(8,20,'Segunda-feira','Sexta-feira','2015-01-01', null,'Horário da Central de Serviços da STI');

insert into sigasr.sr_tipo_permissao_lista (ID_TIPO_PERMISSAO, DESCR_TIPO_PERMISSAO) VALUES ('1', 'Gestão');
insert into sigasr.sr_tipo_permissao_lista (ID_TIPO_PERMISSAO, DESCR_TIPO_PERMISSAO) VALUES ('2', 'Priorização');
insert into sigasr.sr_tipo_permissao_lista (ID_TIPO_PERMISSAO, DESCR_TIPO_PERMISSAO) VALUES ('3', 'Inclusão');
insert into sigasr.sr_tipo_permissao_lista (ID_TIPO_PERMISSAO, DESCR_TIPO_PERMISSAO) VALUES ('4', 'Consulta');

insert into sigasr.sr_objetivo_atributo(ID_OBJETIVO, DESCR_OBJETIVO) values (1, 'Solicitação');
insert into sigasr.sr_objetivo_atributo(ID_OBJETIVO, DESCR_OBJETIVO) values (2, 'Acordo');
insert into sigasr.sr_objetivo_atributo(ID_OBJETIVO, DESCR_OBJETIVO) values (3, 'Indicador');
insert into sigasr.sr_objetivo_atributo(ID_OBJETIVO, DESCR_OBJETIVO) values (4, 'Item de Configuração');


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

-- Dump completed on 2021-06-25 11:23:06
