-- -----------------------------------------------------------------
-- CRIA COLUNAS PARA REPRESENTAR AS DATAS DE REFERENCIA DE UMA MARCA
-- -----------------------------------------------------------------
ALTER TABLE corporativo.cp_marca ADD HIS_DT_REF1 DATETIME COMMENT 'Data de referência da marca 1 - Planejada';
ALTER TABLE corporativo.cp_marca ADD HIS_DT_REF2 DATETIME COMMENT 'Data de referência da marca 2 - Limite';
 