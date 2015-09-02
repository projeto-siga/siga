
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

alter table SR_SOLICITACAO add (
	"DNM_ID_ITEM_CONFIGURACAO" NUMBER(19,0),
	"DNM_ID_ACAO" NUMBER(19,0),
	"DNM_ID_ULT_MOV" NUMBER(19,0)
	"DNM_PRIORIDADE_TECNICA" NUMBER(19,0)
)

update sigasr.sr_solicitacao sol set dnm_id_ult_mov = (
  select max(id_movimentacao) from sigasr.sr_movimentacao where id_solicitacao = sol.id_solicitacao
);
update sigasr.sr_solicitacao sol set dnm_id_item_configuracao = (
  select id_item_configuracao from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) from sigasr.sr_movimentacao where id_solicitacao = sol.id_solicitacao and id_tipo_movimentacao = 24
  )
); 
update sigasr.sr_solicitacao sol set dnm_id_acao = (
  select id_acao from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) from sigasr.sr_movimentacao where id_solicitacao = sol.id_solicitacao and id_tipo_movimentacao = 24
  )
); 
update sigasr.sr_solicitacao sol set dnm_prioridade_tecnica = (
  select prioridade from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) from sigasr.sr_movimentacao where id_solicitacao = sol.id_solicitacao and id_tipo_movimentacao = 21
  )
);
update sigasr.sr_solicitacao sol set dnm_prioridade_tecnica = prioridade where dnm_prioridade_tecnica is null
commit;