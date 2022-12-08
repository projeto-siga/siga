ALTER TABLE sigawf.wf_movimentacao ADD DEFT_TP_PRIORIDADE_DE VARCHAR(12) NULL;
ALTER TABLE sigawf.wf_movimentacao ADD DEFT_TP_PRIORIDADE_PARA VARCHAR(12) NULL;
ALTER TABLE sigawf.wf_movimentacao MODIFY (MOVI_TP_DESIGNACAO VARCHAR(12));
-- COMMENT ON COLUMN sigawf.wf_movimentacao.MOVI_TP_DESIGNACAOMOVI_TP_DESIGNACAO IS 'Numa designação, indica o tipo de designação da movimentação';

ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_TP_PRIORIDADE VARCHAR(12));
COMMENT ON COLUMN sigawf.wf_procedimento.PROC_TP_PRIORIDADE IS 'Prioridade do procedimento';
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_ST_CORRENTE VARCHAR(12));
COMMENT ON COLUMN sigawf.wf_procedimento.PROC_ST_CORRENTE IS 'Status atual do procedimento';
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_CD_PRINCIPAL VARCHAR(32));
COMMENT ON COLUMN sigawf.wf_procedimento.PROC_CD_PRINCIPAL IS 'Código do principal relacionado ao procedimento, no caso de um documento, é algo do tipo JFRJ-MEM-2020/000001';
ALTER TABLE sigawf.wf_procedimento MODIFY (PROC_TP_PRINCIPAL VARCHAR(12));
COMMENT ON COLUMN sigawf.wf_procedimento.PROC_TP_PRINCIPAL IS 'Tipo do principal do procedimento';

ALTER TABLE sigawf.wf_def_responsavel MODIFY (DEFR_TP VARCHAR(64));
COMMENT ON COLUMN sigawf.wf_def_responsavel.DEFR_TP IS 'Tipo da definição de responsável';

ALTER TABLE sigawf.wf_def_variavel MODIFY (DEFV_TP VARCHAR(12));
COMMENT ON COLUMN sigawf.wf_def_variavel.DEFV_TP  IS 'Tipo da definição de variável';
ALTER TABLE sigawf.wf_def_variavel MODIFY (DEFV_TP_ACESSO VARCHAR(32));
COMMENT ON COLUMN sigawf.wf_def_variavel.DEFV_TP_ACESSO IS 'Tipo de acesso da definição de variável';

ALTER TABLE sigawf.wf_variavel MODIFY (VARI_NM VARCHAR(32));
COMMENT ON COLUMN sigawf.wf_variavel.VARI_NM IS 'Nome da variável';
