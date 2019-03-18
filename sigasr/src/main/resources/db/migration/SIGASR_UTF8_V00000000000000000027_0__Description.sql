
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

alter table SR_ATRIBUTO_ACORDO add ("PARAMETRO" NUMBER(10,0));
alter table SR_ATRIBUTO_ACORDO modify (ID_ATRIBUTO null);

create table SR_MOVIMENTACAO_ACORDO
(
  ID_ACORDO NUMBER(19) not null,
  ID_MOVIMENTACAO      NUMBER(19) not null
);

-- Cria��o das PKs e FKs
alter table SR_MOVIMENTACAO_ACORDO
  add constraint PK_SR_MOVIMENTACAO_ACORDO primary key (ID_ACORDO, ID_MOVIMENTACAO);
alter table SR_MOVIMENTACAO_ACORDO
  add constraint FK_SOL_ACORDO_MOVIMENTACAO foreign key (ID_MOVIMENTACAO)
  references SR_MOVIMENTACAO (ID_MOVIMENTACAO);
alter table SR_MOVIMENTACAO_ACORDO
  add constraint FK_MOV_ACORDO_ACORDO foreign key (ID_ACORDO)
  references SR_ACORDO (ID_ACORDO);

  
----------Atualizando base pra ter o id_item e id_acao em cada movimentação-----------------------------

--Fazer com que cada movimentação tenha um item, 
--buscando sempre da anterior ou da solicitação
update sigasr.sr_movimentacao mov set id_item_configuracao = (
  select id_item_configuracao 
  from sigasr.sr_movimentacao movAnterior
  where id_movimentacao = (
    select max(id_movimentacao)
    from sigasr.sr_movimentacao
    where id_solicitacao = mov.id_solicitacao
    and id_movimentacao < mov.id_movimentacao
    and id_item_configuracao is not null
  )
) where id_item_configuracao is null;

update sigasr.sr_movimentacao mov set id_item_configuracao = (
  select id_item_configuracao
  from sigasr.sr_solicitacao
  where id_solicitacao = mov.id_solicitacao
) where id_item_configuracao is null;
  
  
--Fazer com que cada movimentação tenha uma ação, 
--buscando sempre da anterior ou da solicitação
update sigasr.sr_movimentacao mov set id_acao = (
  select id_acao 
  from sigasr.sr_movimentacao movAnterior
  where id_movimentacao = (
    select max(id_movimentacao)
    from sigasr.sr_movimentacao
    where id_solicitacao = mov.id_solicitacao
    and id_movimentacao < mov.id_movimentacao
    and id_acao is not null
  )
) where id_acao is null;

update sigasr.sr_movimentacao mov set id_acao = (
  select id_acao
  from sigasr.sr_solicitacao
  where id_solicitacao = mov.id_solicitacao
) where id_acao is null;


--Fazer com que cada movimentação tenha uma prioridade, 
--buscando sempre da anterior ou da solicitação
update sigasr.sr_movimentacao mov set prioridade = (
  select prioridade 
  from sigasr.sr_movimentacao movAnterior
  where id_movimentacao = (
    select max(id_movimentacao)
    from sigasr.sr_movimentacao
    where id_solicitacao = mov.id_solicitacao
    and id_movimentacao < mov.id_movimentacao
    and prioridade is not null
  )
) where prioridade is null;

update sigasr.sr_movimentacao mov set prioridade = (
  select prioridade
  from sigasr.sr_solicitacao
  where id_solicitacao = mov.id_solicitacao
) where prioridade is null;
commit;

ALTER TABLE SIGASR.SR_SOLICITACAO ADD DNM_TEMPO_DECORRIDO_CADASTRO NUMBER;
ALTER TABLE SIGASR.SR_MOVIMENTACAO ADD DNM_TEMPO_DECORRIDO_ATENDMTO NUMBER;
COMMIT;
