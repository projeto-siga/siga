ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_TX_PARAM varchar(256) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_TX_PARAM IS 'Parâmetro adicional para execução da tarefa';

ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_TX_PARAM2 varchar(256) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_TX_PARAM2 IS 'Parâmetro adicional para execução da tarefa';
