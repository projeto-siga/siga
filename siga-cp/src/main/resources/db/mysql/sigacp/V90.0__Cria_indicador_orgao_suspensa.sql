-- -------------------------------------------------------------------------    
--  Coluna de number 0 ou 1 para informar se lotação esta suspensa
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.dp_lotacao ADD IS_SUSPENSA tinyint(4) DEFAULT 0;
