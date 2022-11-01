-- -------------------------------------------------------------------------    
--  Aumento do tamanho da coluna na ex_modelo para armazenar as extensoes de arquivo permitidas para doc capturado
-- -------------------------------------------------------------------------
ALTER TABLE siga.`ex_modelo` MODIFY `EXTENSOES_ARQUIVO` varchar(200);