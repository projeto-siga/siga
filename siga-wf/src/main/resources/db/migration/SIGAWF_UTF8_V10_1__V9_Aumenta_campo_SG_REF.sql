ALTER TABLE SIGAWF.WF_DEF_TAREFA MODIFY (DEFT_SG_REF varchar(256));
ALTER TABLE SIGAWF.WF_DEF_TAREFA MODIFY (DEFT_SG_REF2 varchar(256));
COMMENT ON COLUMN SIGAWF.WF_DEF_TAREFA.DEFT_SG_REF IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';
COMMENT ON COLUMN SIGAWF.WF_DEF_TAREFA.DEFT_SG_REF2 IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar preenchimentos';
