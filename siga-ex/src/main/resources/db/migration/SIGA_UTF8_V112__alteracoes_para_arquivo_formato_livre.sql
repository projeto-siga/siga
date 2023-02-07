-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_modelo para armazenar as extensoes de arquivo permitidas para doc capturado
-- -------------------------------------------------------------------------
ALTER TABLE SIGA.EX_MODELO ADD EXTENSOES_ARQUIVO VARCHAR (50);

COMMENT ON COLUMN "SIGA"."EX_MODELO"."EXTENSOES_ARQUIVO" IS 'Extensões de arquivo permitidas para doc capturado. Se preenchido, permite captura de arquivos não PDF.';

-- -------------------------------------------------------------------------    
--  Criacao da coluna na ex_documento para armazenar id da cp_arquivo referente ao arquivo de formato livre (permitindo grandes volumes)
-- -------------------------------------------------------------------------
ALTER TABLE SIGA.EX_DOCUMENTO ADD (ID_ARQ_FMT_LIVRE number(19,0) NULL);
COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ID_ARQ_FMT_LIVRE" IS 'Identificador do arquivo de formato livre';
insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC, DESCR_TIPO_DOCUMENTO) values (6,'Externo Capturado Formato Livre');
insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC, DESCR_TIPO_DOCUMENTO) values (7,'Interno Capturado Formato Livre');