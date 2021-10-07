ALTER TABLE `sigawf`.`wf_def_tarefa` 
ADD COLUMN `DEFT_DS_REF2` varchar(256) DEFAULT NULL COMMENT 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos' AFTER `DEFT_SG_REF`,
ADD COLUMN `DEFT_ID_REF2` bigint DEFAULT NULL COMMENT 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos',
ADD COLUMN `DEFT_SG_REF2` varchar(32) DEFAULT NULL COMMENT 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';
