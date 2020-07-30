---------------------------------------------------------------------------    
--  Coluna com Timestamp para permitir ordenar movimentacoes sem o ID_MOV
---------------------------------------------------------------------------


ALTER TABLE SIGA.EX_MOVIMENTACAO DROP COLUMN  DT_TIMESTAMP;

ALTER TABLE SIGA.EX_MOVIMENTACAO ADD DT_TIMESTAMP TIMESTAMP (6) DEFAULT SYSTIMESTAMP;

COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."DT_TIMESTAMP" IS 'Timestamp para permitir ordenar as movimentacoes sem utilizar o ID.'; 

---------------------------------------------------------------------------    
--  Carga Inicial da Coluna com o timestamp. Sera o systimestamp com precisao de 6 miliseconds 
--  para manter compatibilidade com bancos mysql também
---------------------------------------------------------------------------
DECLARE
     linhas NUMBER(6) := 0;
     CURSOR c_movimentacoes IS
            SELECT 
                m.id_mov
            FROM
                siga.ex_movimentacao m
            ORDER BY id_mov;
 BEGIN
    FOR r_movimentacao IN c_movimentacoes LOOP
        UPDATE siga.ex_movimentacao SET  dt_timestamp = systimestamp where id_mov = r_movimentacao.id_mov;
      	linhas := linhas + 1;
        IF linhas >= 1000 THEN
           COMMIT;
           linhas :=0;
        END IF;
    END LOOP;
    COMMIT;
 end;
 /