-- --------------------------------------------------------
--  ACRESCENTANDO CAMPOS PARA ESPECIFICAR O TIPO DE VINCULO
-- --------------------------------------------------------
ALTER TABLE siga.ex_movimentacao
 ADD TP_VINCULO INT UNSIGNED DEFAULT NULL COMMENT 'Tipo de vínculo';
 
UPDATE siga.ex_movimentacao SET tp_vinculo = 1 WHERE id_tp_mov = 16;
 