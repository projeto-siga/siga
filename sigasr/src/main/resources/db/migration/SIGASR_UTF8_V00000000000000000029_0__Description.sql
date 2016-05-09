ALTER SESSION SET CURRENT_SCHEMA=sigasr;

----------Atualizar base pra não ter repetições no número---------------------------------------------
UPDATE SIGASR.SR_SOLICITACAO SOL SET DESCR_CODIGO = REGEXP_REPLACE(DESCR_CODIGO, '/[0-5]', '/9') where id_solicitacao in (
  select id from (
    select descr_codigo, min(id_solicitacao) id from sigasr.sr_solicitacao sol where exists(
      select 1 from sigasr.sr_solicitacao where id_solicitacao <> sol.id_solicitacao and decode(his_dt_fim, sol.his_dt_fim, 1, 0) = 1 and descr_codigo = sol.descr_codigo
    ) group by descr_codigo 
  )
);
UPDATE SIGASR.SR_SOLICITACAO SOL SET DESCR_CODIGO = REGEXP_REPLACE(DESCR_CODIGO, 'TMPSR-', 'TMPSR-9') where id_solicitacao in (
  select id from (
    select descr_codigo, min(id_solicitacao) id from sigasr.sr_solicitacao sol where exists(
      select 1 from sigasr.sr_solicitacao where id_solicitacao <> sol.id_solicitacao and decode(his_dt_fim, sol.his_dt_fim, 1, 0) = 1 and descr_codigo = sol.descr_codigo
    ) group by descr_codigo 
  )
);
commit;

--Testar se ainda há repetições
select id_solicitacao, dt_reg, descr_codigo from sigasr.sr_solicitacao sol where exists(
	select 1 from sigasr.sr_solicitacao where id_solicitacao <> sol.id_solicitacao and decode(his_dt_fim, sol.his_dt_fim, 1, 0) = 1 and descr_codigo = sol.descr_codigo
) order by dt_reg desc;

--Gerar constraint
ALTER TABLE SIGASR.SR_SOLICITACAO ADD CONSTRAINT UNIQUE_SOL_NUM_IDX UNIQUE (DESCR_CODIGO, HIS_DT_FIM);
commit;

alter table sigasr.sr_movimentacao add (CONHECIMENTO VARCHAR2(10));
