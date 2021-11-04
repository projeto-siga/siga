ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_DS_REF2 varchar(256) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_DS_REF2 IS 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';

ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_ID_REF2 NUMBER(19,0) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_ID_REF2 IS 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';

ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_SG_REF2 varchar(32) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_SG_REF2 IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';
