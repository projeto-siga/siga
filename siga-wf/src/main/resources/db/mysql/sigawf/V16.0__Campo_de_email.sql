ALTER TABLE `sigawf`.`wf_def_tarefa` 
ADD COLUMN `DEFT_TX_EMAIL` VARCHAR(256) NULL DEFAULT NULL AFTER `DEFT_ID_SEGUINTE` COMMENT 'Email do destinatario';