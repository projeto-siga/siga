alter table SIGA.EX_DOCUMENTO add DT_ACAO_PROGRAMADA DATE;
COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."DT_ACAO_PROGRAMADA" IS 'Indica que deve realizar uma acao neste documento na data indicada.';
