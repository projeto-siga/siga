-- -------------------------------------------------------------------------    
--  Aumento do tamanho da coluna na ex_modelo para armazenar as extensoes de arquivo permitidas para doc capturado
-- -------------------------------------------------------------------------
ALTER TABLE SIGA.EX_MODELO MODIFY EXTENSOES_ARQUIVO VARCHAR (200);