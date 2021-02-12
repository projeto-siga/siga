-- Substitui os vários campos no cadastro de marcadores por um único campo chamado "Finalidade".
-- Garante que só existem marcadores de sistema (1) ou Gerais (2), convertendo os outros para Gerais.

UPDATE corporativo.cp_marcador set ID_TP_MARCADOR = 2 where ID_TP_MARCADOR > 2;

ALTER TABLE `corporativo`.`cp_marcador` 
DROP FOREIGN KEY `cp_marcador_ibfk_1`;
ALTER TABLE `corporativo`.`cp_marcador` 
DROP COLUMN `ID_TP_INTERESSADO`,
DROP COLUMN `ID_TP_TEXTO`,
DROP COLUMN `ID_TP_OPCAO_EXIBICAO`,
DROP COLUMN `ID_TP_DATA_LIMITE`,
DROP COLUMN `ID_TP_DATA_PLANEJADA`,
DROP COLUMN `ID_TP_APLICACAO_MARCADOR`,
CHANGE COLUMN `DESCR_MARCADOR` `DESCR_MARCADOR` VARCHAR(40) CHARACTER SET 'utf8' NOT NULL ,
CHANGE COLUMN `ID_TP_MARCADOR` `ID_FINALIDADE_MARCADOR` INT UNSIGNED NOT NULL;  