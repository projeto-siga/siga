-- -----------------------------------------------------------------------------------------------
--  SCRIPT: ALTERA A QUANTIDADE DE CAMPOS SIGLA DE 20 PARA 30.
-- -----------------------------------------------------------------------------------------------
DECLARE
    index_count INTEGER;
BEGIN
    SELECT Count(1)
    INTO   index_count
    FROM   all_indexes
    WHERE  index_name = 'DP_LOTACAO_IDX_006';

    IF index_count > 0 THEN
      EXECUTE IMMEDIATE 'DROP INDEX CORPORATIVO.DP_LOTACAO_IDX_006';
    END IF;
END;
/

DECLARE
    index_count INTEGER;
BEGIN
    SELECT Count(1)
    INTO   index_count
    FROM   all_indexes
    WHERE  index_name = 'SIGLA_LOTACAO_DP_LOTACAO_UK';

    IF index_count > 0 THEN
      EXECUTE IMMEDIATE
'ALTER TABLE CORPORATIVO.DP_LOTACAO DROP CONSTRAINT SIGLA_LOTACAO_DP_LOTACAO_UK'
  ;

  EXECUTE IMMEDIATE 'DROP INDEX CORPORATIVO.SIGLA_LOTACAO_DP_LOTACAO_UK';
END IF;
END;
/

ALTER TABLE corporativo.dp_lotacao
  MODIFY (sigla_lotacao VARCHAR2(30));

CREATE INDEX corporativo.dp_lotacao_idx_006
  ON corporativo.dp_lotacao (id_orgao_usu ASC, sigla_lotacao ASC, id_lotacao ASC
);

CREATE UNIQUE INDEX corporativo.sigla_lotacao_dp_lotacao_uk
  ON corporativo.dp_lotacao (sigla_lotacao ASC, id_orgao_usu ASC, data_fim_lot
ASC);

ALTER TABLE "CORPORATIVO"."dp_lotacao"
  ADD CONSTRAINT "SIGLA_LOTACAO_DP_LOTACAO_UK" UNIQUE ("sigla_lotacao",
  "id_orgao_usu", "data_fim_lot") ENABLE; 