
--Atualizando referencias a solicitacoes, visto que movs, atributos, 
--marcas e solicitacoes filhas passam a apontar para a solicitacao inicial
update sigasr.sr_movimentacao m set id_solicitacao = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = m.id_solicitacao);
update sigasr.sr_movimentacao m set id_lista = (select his_id_ini from sigasr.sr_lista where id_lista = m.id_lista) where id_lista is not null;
update corporativo.cp_marca mar set id_ref = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = mar.id_ref) where id_marcador in (41, 42, 43, 44, 45, 46, 47, 48, 49, 53, 54, 55);
update sigasr.sr_solicitacao sol set id_solicitacao_pai = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = sol.id_solicitacao_pai) where id_solicitacao_pai is not null;

update sigasr.sr_tipo_movimentacao set nome_tipo_movimentacao = 'Alteração de Prazo' where id_tipo_movimentacao = 21

alter table sr_item_configuracao add ID_PAI number(19,0);
alter table sr_acao add ID_PAI number(19,0);

-- Criando a tabela do relacionamento entre a Configuração e o Item de configuração
create table SR_CONFIGURACAO_ITEM
(
  ID_ITEM_CONFIGURACAO NUMBER(19) not null,
  ID_CONFIGURACAO      NUMBER(19) not null
)

-- Criação das PKs e FKs
alter table SR_CONFIGURACAO_ITEM
  add constraint PK_SR_CONFIGURACAO_ITEM primary key (ID_ITEM_CONFIGURACAO, ID_CONFIGURACAO)
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

-- Create/Recreate primary, unique and foreign key constraints 
alter table SR_CONFIGURACAO_ACAO
  add constraint PK_SR_CONFIGURACAO_ACAO primary key (ID_CONFIGURACAO, ID_ACAO)
alter table SR_CONFIGURACAO_ACAO
  add constraint FK_CONFIGURACAO_ACAO foreign key (ID_ACAO)
  references SR_ACAO (ID_ACAO);
alter table SR_CONFIGURACAO_ACAO
  add constraint FK_CONFIG_ACAO_CONFIGURACAO foreign key (ID_CONFIGURACAO)
  references SR_CONFIGURACAO (ID_CONFIGURACAO_SR);
  

insert into sigasr.sr_configuracao_item (id_configuracao, id_item_configuracao) select id_configuracao_sr, id_item_configuracao from sigasr.sr_configuracao where id_item_configuracao is not null;
insert into sigasr.sr_configuracao_acao (id_configuracao, id_acao) select id_configuracao_sr, id_acao from sigasr.sr_configuracao where id_acao is not null; 
  
-- OSI_FS0004 - Item 39
-- Remove a PK
alter table SR_LISTA_CONFIGURACAO
  drop constraint SR_LISTA_CONFIGURACAO_PK cascade;
-- Remove a coluna
alter table SR_LISTA_CONFIGURACAO drop column ID_LISTA_CONFIGURACAO;

-- Adiciona a nova PK
alter table SR_LISTA_CONFIGURACAO
  add constraint SR_LISTA_CONFIGURACAO_PK primary key (ID_CONFIGURACAO, ID_LISTA)

-- OSI_FS0004 - Item 34 Criando tela de gerenciamento de equipe
-- Create table
create table SR_EQUIPE
(
  ID_EQUIPE      NUMBER(19) not null,
  ID_LOTA_EQUIPE NUMBER(19) not null
)
-- Create/Recreate primary, unique and foreign key constraints 
alter table SR_EQUIPE
  add constraint PK_SR_EQUIPE primary key (ID_EQUIPE)
alter table SR_EQUIPE
  add constraint FK_EQUIPE_LOTACAO foreign key (ID_LOTA_EQUIPE)
  references CORPORATIVO.DP_LOTACAO (ID_LOTACAO);

-- Create table
create table SR_EXCECAO_HORARIO
(
  ID_EXCECAO_HORARIO NUMBER(19) not null,
  ID_EQUIPE          NUMBER(19) not null,
  DIA_SEMANA         NUMBER(1),
  DT_ESPECIFICA      TIMESTAMP(6),
  HORA_INI           TIMESTAMP(6) not null,
  HORA_FIM           TIMESTAMP(6) not null,
  INTER_INI          TIMESTAMP(6) not null,
  INTER_FIM          TIMESTAMP(6) not null
)
-- Create/Recreate primary, unique and foreign key constraints 
alter table SR_EXCECAO_HORARIO
  add constraint PF_EXCECAO_HORARIO primary key (ID_EXCECAO_HORARIO)
alter table SR_EXCECAO_HORARIO
  add constraint FK_EX_HOR_EQUIPE foreign key (ID_EQUIPE)
  references SR_EQUIPE (ID_EQUIPE);

-- Create sequence 
create sequence SR_EQUIPE_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SR_EXCECAO_HORARIO_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- OSI_FS0004 - Item 37
ALTER TABLE SR_SOLICITACAO 
	ADD (
		DESCR_CODIGO VARCHAR2(100 CHAR) 
	);
