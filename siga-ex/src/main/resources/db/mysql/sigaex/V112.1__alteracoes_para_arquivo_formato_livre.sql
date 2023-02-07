-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_modelo para armazenar as extensoes de arquivo permitidas para doc capturado
-- -------------------------------------------------------------------------
ALTER TABLE siga.`ex_modelo` ADD `EXTENSOES_ARQUIVO` varchar(50);

-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_documento para armazenar id da cp_arquivo referente ao arquivo de formato livre (permitindo grandes volumes)
-- -------------------------------------------------------------------------
ALTER TABLE siga.`ex_documento` ADD `ID_ARQ_FMT_LIVRE` INT UNSIGNED DEFAULT NULL;
insert into siga.`ex_tipo_documento` values (6,'Externo Capturado Formato Livre'), (7,'Interno Capturado Formato Livre');