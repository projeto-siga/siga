ALTER TABLE `sigawf`.`wf_def_tarefa` 
ADD COLUMN `DEFT_TX_PARAM` varchar(256) DEFAULT NULL COMMENT 'Parâmetro adicional para execução da tarefa' AFTER `DEFT_SG_REF2`,
ADD COLUMN `DEFT_TX_PARAM2` varchar(256) DEFAULT NULL COMMENT 'Parâmetro adicional para execução da tarefa' AFTER `DEFT_TX_PARAM`;
