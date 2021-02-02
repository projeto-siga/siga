ALTER TABLE sigawf.wf_def_procedimento ADD PESS_ID_RESP NUMBER(10) NULL;
COMMENT ON COLUMN sigawf.wf_def_procedimento.PESS_ID_RESP IS 'Pessoa responsável pela definição de procedimento';

ALTER TABLE sigawf.wf_def_procedimento ADD LOTA_ID_RESP NUMBER(5) NULL;
COMMENT ON COLUMN sigawf.wf_def_procedimento.LOTA_ID_RESP IS 'Lotação responsável pela definição de procedimento';