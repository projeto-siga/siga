-- -------------------------------------------------------------------------    
--  Coluna de number 0 ou 1 para informar se lotação esta suspensa
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.cp_orgao_usuario ADD IS_SUSPENSA tinyint(4) DEFAULT 0;