-- -------------------------------------------------------------------------    
--  Coluna de number 0 ou 1 para informar se orgao externo
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.cp_orgao_usuario ADD IS_EXTERNO_ORGAO_USU tinyint(4) DEFAULT 0;
