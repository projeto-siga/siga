--Atualizando referencias a solicitacoes, visto que movs, atributos, 
--marcas e solicitacoes filhas passam a apontar para a solicitacao inicial
update sigasr.sr_movimentacao m set id_solicitacao = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = m.id_solicitacao);
update sigasr.sr_movimentacao m set id_lista = (select his_id_ini from sigasr.sr_lista where id_lista = m.id_lista) where id_lista is not null;
update corporativo.cp_marca mar set id_ref = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = mar.id_ref) where id_marcador in (41, 42, 43, 44, 45, 46, 47, 48, 49, 53, 54, 55);
update sigasr.sr_solicitacao sol set id_solicitacao_pai = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = sol.id_solicitacao_pai) where id_solicitacao_pai is not null;

alter table sr_item_configuracao add ID_PAI number(19,0);
alter table sr_acao add ID_PAI number(19,0);

-- Criando a tabela do relacionamento entre a Configuração e o Item de configuração
create table SR_CONFIGURACAO_ITEM
(
  ID_ITEM_CONFIGURACAO NUMBER(19) not null,
  ID_CONFIGURACAO      NUMBER(19) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Criação das PKs e FKs
alter table SR_CONFIGURACAO_ITEM
  add constraint PK_SR_CONFIGURACAO_ITEM primary key (ID_ITEM_CONFIGURACAO, ID_CONFIGURACAO)
  using index 
  tablespace KDB1
  pctfree 10
  initrans 2
  maxtrans 255;
alter table SR_CONFIGURACAO_ITEM
  add constraint FK_CONFIG_CONFIGURACAO foreign key (ID_CONFIGURACAO)
  references SR_CONFIGURACAO (ID_CONFIGURACAO_SR);
alter table SR_CONFIGURACAO_ITEM
  add constraint FK_CONFIG_ITEM_CONFIGURACAO foreign key (ID_ITEM_CONFIGURACAO)
  references SR_ITEM_CONFIGURACAO (ID_ITEM_CONFIGURACAO);

-- Criando a tabela de relacionamento entre o Item de Configuração e a Ação.
create table SR_CONFIGURACAO_ACAO
(
  ID_CONFIGURACAO NUMBER(19) not null,
  ID_ACAO         NUMBER(19) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SR_CONFIGURACAO_ACAO
  add constraint PK_SR_CONFIGURACAO_ACAO primary key (ID_CONFIGURACAO, ID_ACAO)
  using index 
  tablespace KDB1
  pctfree 10
  initrans 2
  maxtrans 255;
alter table SR_CONFIGURACAO_ACAO
  add constraint FK_CONFIGURACAO_ACAO foreign key (ID_ACAO)
  references SR_ACAO (ID_ACAO);
alter table SR_CONFIGURACAO_ACAO
  add constraint FK_CONFIG_ACAO_CONFIGURACAO foreign key (ID_CONFIGURACAO)
  references SR_CONFIGURACAO (ID_CONFIGURACAO_SR);
  
-- OSI_FS0004 - Item 39
-- Remove a PK
alter table SR_LISTA_CONFIGURACAO
  drop constraint SR_LISTA_CONFIGURACAO_PK cascade;
-- Remove a coluna
alter table SR_LISTA_CONFIGURACAO drop column ID_LISTA_CONFIGURACAO;

-- Adiciona a nova PK
alter table SR_LISTA_CONFIGURACAO
  add constraint SR_LISTA_CONFIGURACAO_PK primary key (ID_CONFIGURACAO, ID_LISTA)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
