-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_modelo para armazenar as extensoes de arquivo permitidas para doc capturado
-- -------------------------------------------------------------------------
ALTER TABLE siga.`ex_modelo` ADD varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,

-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_documento para armazenar id da cp_arquivo referente ao arquivo de formato livre (permitindo grandes volumes)
-- -------------------------------------------------------------------------
ALTER TABLE siga.`ex_documento` ADD `ID_ARQ_FMT_LIVRE` BIGINT UNSIGNED DEFAULT NULL;
CONSTRAINT `ex_documento_ibfk_3` FOREIGN KEY (`ID_ARQ_FMT_LIVRE`) REFERENCES `corporativo`.`cp_arquivo` (`ID_ARQ`);
insert into siga.`ex_tipo_documento` (`ID_TP_DOC`, `DESCR_TIPO_DOCUMENTO`) values (6,'Externo Capturado Formato Livre');
insert into siga.`ex_tipo_documento' (`ID_TP_DOC`, `DESCR_TIPO_DOCUMENTO`) values (7,'Interno Capturado Formato Livre');