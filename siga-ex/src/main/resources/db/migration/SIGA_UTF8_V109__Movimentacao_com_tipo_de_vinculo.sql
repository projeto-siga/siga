---------------------------------------------------------
-- ACRESCENTANDO CAMPOS PARA FAZER O LINK COM O PRINCIPAL
---------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=siga;

ALTER TABLE siga.ex_movimentacao ADD (TP_VINCULO NUMBER(2,0));
COMMENT ON COLUMN siga.ex_movimentacao.TP_VINCULO IS 'Tipo do vínculo';

UPDATE siga.ex_movimentacao SET tp_vinculo = 1 WHERE id_tp_mov = 16;