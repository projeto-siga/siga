ALTER TABLE sigawf.wf_movimentacao ADD DEFT_TP_PRIORIDADE_DE VARCHAR2(12) NULL;
ALTER TABLE sigawf.wf_movimentacao ADD DEFT_TP_PRIORIDADE_PARA VARCHAR2(12) NULL;

ALTER TABLE sigawf.wf_movimentacao MODIFY (MOVI_TP_DESIGNACAO varchar2(255));

ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_TP_PRIORIDADE varchar2(12));
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_ST_CORRENTE varchar2(12));
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_CD_PRINCIPAL varchar2(32));
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_TP_PRINCIPAL varchar2(12));

ALTER TABLE sigawf.wf_def_responsavel MODIFY (DEFR_TP varchar2(64));

ALTER TABLE sigawf.wf_def_variavel MODIFY (DEFV_TP varchar2(12));
ALTER TABLE sigawf.wf_def_variavel MODIFY (DEFV_TP_ACESSO varchar2(32));

ALTER TABLE sigawf.wf_variavel MODIFY (VARI_NM varchar2(32));
