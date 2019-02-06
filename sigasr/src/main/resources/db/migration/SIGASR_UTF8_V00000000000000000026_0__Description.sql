
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

alter table SR_SOLICITACAO add (
	"DNM_ID_ITEM_CONFIGURACAO" NUMBER(19,0),
	"DNM_ID_ACAO" NUMBER(19,0),
	"DNM_ID_ULT_MOV" NUMBER(19,0),
	"DNM_PRIORIDADE_TECNICA" NUMBER(19,0)
);

update sigasr.sr_solicitacao sol set dnm_id_ult_mov = (
  select max(id_movimentacao) from sigasr.sr_movimentacao where id_solicitacao = sol.id_solicitacao
);
update sigasr.sr_solicitacao sol set dnm_id_item_configuracao = (
  select id_item_configuracao from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) 
    from sigasr.sr_movimentacao 
    where id_solicitacao = sol.id_solicitacao 
    and id_tipo_movimentacao = 24
    and id_mov_canceladora is null
  )
); 
update sigasr.sr_solicitacao sol set dnm_id_acao = (
  select id_acao from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) 
    from sigasr.sr_movimentacao 
    where id_solicitacao = sol.id_solicitacao 
    and id_tipo_movimentacao = 24
    and id_mov_canceladora is null
  )
); 
update sigasr.sr_solicitacao sol set dnm_prioridade_tecnica = (
  select prioridade from sigasr.sr_movimentacao where id_movimentacao = (
    select max(id_movimentacao) 
    from sigasr.sr_movimentacao 
    where id_solicitacao = sol.id_solicitacao 
    and id_tipo_movimentacao = 21
    and id_mov_canceladora is null
  )
);
update sigasr.sr_solicitacao sol set dnm_prioridade_tecnica = prioridade where dnm_prioridade_tecnica is null;
update sigasr.sr_solicitacao sol set dnm_id_item_configuracao = id_item_configuracao where dnm_id_item_configuracao is null;
update sigasr.sr_solicitacao sol set dnm_id_acao = id_acao where dnm_id_acao is null;
update sigasr.sr_solicitacao set id_lota_titular = id_lota_cadastrante where id_lota_titular is null;
commit;

-------------------------------------------------

update sigasr.sr_tipo_movimentacao set nome_tipo_movimentacao = 'Reclassificação' where id_tipo_movimentacao = 23;
commit;

-------------------------------------------------

ALTER TABLE SR_MOVIMENTACAO 
ADD (MOTIVOFECHAMENTO NUMBER(10, 0));

insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-OPENSAVE', 'Salvar Solicitação Ao Abrir', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);