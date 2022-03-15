ALTER TABLE `sigawf`.`wf_def_tarefa` 
CHANGE COLUMN `DEFT_SG_REF` `DEFT_SG_REF` VARCHAR(256) NULL DEFAULT NULL COMMENT 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos' ,
CHANGE COLUMN `DEFT_SG_REF2` `DEFT_SG_REF2` VARCHAR(256) NULL DEFAULT NULL COMMENT 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos' ;
