---------------------------------------------------------
-- ACRESCENTANDO CAMPOS PARA FAZER O LINK COM O PRINCIPAL
---------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=siga;

ALTER TABLE siga.ex_documento ADD (CD_PRINCIPAL varchar2(32));
COMMENT ON COLUMN siga.ex_documento.CD_PRINCIPAL IS 'Código do principal relacionado ao documento, no caso de um procedimento, é algo do tipo JFRJ-WF-2020/000001';

ALTER TABLE siga.ex_documento ADD (TP_PRINCIPAL NUMBER(2,0));
COMMENT ON COLUMN siga.ex_documento.TP_PRINCIPAL IS 'Tipo do principal do documento';