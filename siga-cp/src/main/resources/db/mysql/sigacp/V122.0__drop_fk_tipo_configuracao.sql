-- Remoção das FKs da CP_TIPO_CONFIGURACAO que virou ENUM
ALTER TABLE `corporativo`.`cp_configuracao` 
DROP FOREIGN KEY `CP_CONF_CP_TP_CONF_ID_FK`;

ALTER TABLE `corporativo`.`cp_tipo_servico` 
DROP FOREIGN KEY `CP_TP_SERV_ID_SIT_CONF_FK`;