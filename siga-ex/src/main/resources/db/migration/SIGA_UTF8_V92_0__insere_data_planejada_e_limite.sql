ALTER TABLE siga.ex_movimentacao ADD (DT_PARAM1 DATE);
COMMENT ON COLUMN siga.ex_movimentacao.DT_PARAM1 IS 'Primeiro parâmetro opcional do tipo data';

ALTER TABLE siga.ex_movimentacao ADD (DT_PARAM2 DATE);
COMMENT ON COLUMN siga.ex_movimentacao.DT_PARAM2 IS 'Segundo parâmetro opcional do tipo data';
