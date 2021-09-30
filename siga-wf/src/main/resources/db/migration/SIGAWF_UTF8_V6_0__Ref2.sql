ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_DS_REF varchar(256) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_DS_REF IS 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';

ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_ID_REF NUMBER NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_ID_REF IS 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';

ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_SG_REF varchar(32) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_SG_REF IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';
