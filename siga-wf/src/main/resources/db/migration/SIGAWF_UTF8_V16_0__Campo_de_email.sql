ALTER TABLE sigawf.wf_def_tarefa ADD DEFT_TX_EMAIL VARCHAR(256) NULL;
COMMENT ON COLUMN sigawf.wf_def_tarefa.DEFT_TX_EMAIL IS 'Email do destinatario';
