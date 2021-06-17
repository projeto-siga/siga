-- -------------------------------------------------------------------------    
--  Coluna Hash SHA-256 do PIN definido pelo Usuário como Segundo Fator de Autenticação
-- -------------------------------------------------------------------------
ALTER TABLE corporativo.cp_identidade ADD pin_identidade VARCHAR(64) DEFAULT NULL;
