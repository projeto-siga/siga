-- Inclusão do Tipo de Token para Reset de Senha
--
INSERT INTO corporativo.cp_tipo_token (ID_TP_TOKEN, DESCR_TP_TOKEN) VALUES ('3', 'Token para Reset Senha');

-- Alter id_ref para bigint 15
--
ALTER TABLE corporativo.cp_token
CHANGE COLUMN `ID_REF` `ID_REF` BIGINT(15) UNSIGNED NOT NULL ;