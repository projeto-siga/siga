-- -------------------------------------------------------------------------    
--  Coluna de number 0 ou 1 para informar se orgão é externo 
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.dp_lotacao ADD IS_EXTERNA_LOTACAO tinyint(4) DEFAULT 0;