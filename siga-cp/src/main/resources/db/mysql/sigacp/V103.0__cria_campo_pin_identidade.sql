-- -------------------------------------------------------------------------    
--  Coluna Hash SHA-256 do PIN definido pelo Usuário como Segundo Fator de Autenticação
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.cp_identidade ADD PIN_IDENTIDADE varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;
