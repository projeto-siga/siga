--adicionando prioridade a solicitacao
alter table sigasr.sr_solicitacao add PRIORIDADE number(10,0);

alter session set current_schema = corporativo;
update corporativo.cp_marcador set descr_marcador = 'A Fechar' where id_marcador = 53;

INSERT INTO corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) 
	VALUES (303, 'Inclus�o autom�tica em lista', 1);
alter session set current_schema = SIGASR;
ALTER TABLE SIGASR.SR_ATRIBUTO RENAME TO SR_ATRIBUTO_SOLICITACAO;
ALTER TABLE SIGASR.SR_TIPO_ATRIBUTO RENAME TO SR_ATRIBUTO;

ALTER TABLE SIGASR.SR_ATRIBUTO_SOLICITACAO RENAME COLUMN ID_ATRIBUTO TO ID_ATRIBUTO_SOLICITACAO;
ALTER TABLE SIGASR.SR_ATRIBUTO_SOLICITACAO RENAME COLUMN VALOR_ATRIBUTO TO VALOR_ATRIBUTO_SOLICITACAO;
ALTER TABLE SIGASR.SR_ATRIBUTO_SOLICITACAO RENAME COLUMN ID_TIPO_ATRIBUTO TO ID_ATRIBUTO;

ALTER TABLE SIGASR.SR_ATRIBUTO RENAME COLUMN ID_TIPO_ATRIBUTO TO ID_ATRIBUTO;
ALTER TABLE SIGASR.SR_ATRIBUTO RENAME COLUMN FORMATOCAMPO TO TIPO_ATRIBUTO;

CREATE SEQUENCE SR_ATRIBUTO_SOLICITACAO_SEQ;
DROP SEQUENCE SR_ATRIBUTO_SEQ;
CREATE SEQUENCE SR_ATRIBUTO_SEQ;
DROP SEQUENCE SR_TIPO_ATRIBUTO_SEQ;


--adicionando coluna para registro se a solicitacao pai deve ser fechada automaticamente quando todas as filhas forem fechadas
alter table sigasr.sr_solicitacao add FECHADO_AUTOMATICAMENTE CHAR(1 CHAR);

INSERT INTO corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) 
	VALUES (304, 'Abrangência de Acordo', 1);
alter table sigasr.sr_configuracao add PRIORIDADE number(10,0);

commit;

CREATE TABLE "SIGASR"."SR_OBJETIVO_ATRIBUTO"
(
	"ID_OBJETIVO" NUMBER,
	"DESCR_OBJETIVO" VARCHAR2(255 CHAR),
  primary key (ID_OBJETIVO)
);

insert into sigasr.sr_objetivo_atributo(id_objetivo, descr_objetivo) values (1, 'Solicitação');
insert into sigasr.sr_objetivo_atributo(id_objetivo, descr_objetivo) values (2, 'Acordo');
insert into sigasr.sr_objetivo_atributo(id_objetivo, descr_objetivo) values (3, 'Indicador');
insert into sigasr.sr_objetivo_atributo(id_objetivo, descr_objetivo) values (4, 'Item de Configuração');
commit;

alter table sigasr.sr_atributo
add (
	ID_OBJETIVO NUMBER,
	CODIGO_ATRIBUTO VARCHAR2(255 CHAR),
	CONSTRAINT "ATRIBUTO_OBJETIVO_FK" 
			FOREIGN KEY ("ID_OBJETIVO") 
			REFERENCES "SIGASR"."SR_OBJETIVO_ATRIBUTO" ("ID_OBJETIVO")
);

CREATE TABLE "SIGASR"."SR_ACORDO"
	(
		"ID_ACORDO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"            NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6) NOT NULL,
		"HIS_ID_INI"               NUMBER(19,0),
		"NOME_ACORDO" VARCHAR2(255 CHAR) NOT NULL,
		"DESCR_ACORDO"  VARCHAR2(255 CHAR),
		PRIMARY KEY ("ID_ACORDO")
	);
CREATE SEQUENCE SR_ACORDO_SEQ;

CREATE TABLE "SIGASR"."SR_ATRIBUTO_ACORDO"
	(
		"ID_ATRIBUTO_ACORDO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"            NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6) NOT NULL,
		"HIS_ID_INI"               NUMBER(19,0),
		"OPERADOR" NUMBER(10,0),
		"VALOR"     NUMBER(19,0),
		"UNIDADE_MEDIDA" NUMBER(19,0),
		"ID_ATRIBUTO" NUMBER(19,0) NOT NULL,
		"ID_ACORDO" NUMBER(19,0),
		PRIMARY KEY ("ID_ATRIBUTO_ACORDO"),
		CONSTRAINT "ATRIBUTO_ACORDO_ACORDO_FK" 
			FOREIGN KEY ("ID_ACORDO") 
			REFERENCES "SIGASR"."SR_ACORDO" ("ID_ACORDO"),
		CONSTRAINT "PARAMETRO_ACORDO_TIPO_FK" 
			FOREIGN KEY ("ID_ATRIBUTO") 
			REFERENCES "SIGASR"."SR_ATRIBUTO" ("ID_ATRIBUTO")
	);
CREATE SEQUENCE SR_ATRIBUTO_ACORDO_SEQ;

create table SR_SOLICITACAO_ACORDO
(
  ID_ACORDO NUMBER(19) not null,
  ID_SOLICITACAO      NUMBER(19) not null
);

-- Cria��o das PKs e FKs
alter table SR_SOLICITACAO_ACORDO
  add constraint PK_SR_SOLICITACAO_ACORDO primary key (ID_ACORDO, ID_SOLICITACAO);
alter table SR_SOLICITACAO_ACORDO
  add constraint FK_SOL_ACORDO_SOLICITACAO foreign key (ID_SOLICITACAO)
  references SR_SOLICITACAO (ID_SOLICITACAO);
alter table SR_SOLICITACAO_ACORDO
  add constraint FK_SOL_ACORDO_ACORDO foreign key (ID_ACORDO)
  references SR_ACORDO (ID_ACORDO);


update sigasr.sr_atributo set id_objetivo = 1 where id_objetivo is null;	
	
insert into sigasr.sr_ATRIBUTO(ID_ATRIBUTO, NOME, CODIGO_ATRIBUTO, ID_OBJETIVO) values (sr_atributo_seq.nextval, 'Tempo de Cadastramento', 'tempoDecorridoCadastramento', 2);
insert into sigasr.sr_ATRIBUTO(ID_ATRIBUTO, NOME, CODIGO_ATRIBUTO, ID_OBJETIVO) values (sr_atributo_seq.nextval, 'Tempo de Escalonamento', 'tempoDecorridoEscalonamento', 2);
insert into sigasr.sr_ATRIBUTO(ID_ATRIBUTO, NOME, CODIGO_ATRIBUTO, ID_OBJETIVO) values (sr_atributo_seq.nextval, 'Tempo de Atendimento', 'tempoDecorridoAtendimento', 2);
insert into sigasr.sr_ATRIBUTO(ID_ATRIBUTO, NOME, CODIGO_ATRIBUTO, ID_OBJETIVO) values (sr_atributo_seq.nextval, 'Resultado da Pesquisa de Satisfa��o', 'resultadoPesquisaSatisfacao', 2);

INSERT INTO CORPORATIVO.CP_UNIDADE_MEDIDA (ID_UNIDADE_MEDIDA, DESCR_UNIDADE_MEDIDA) VALUES (5, 'Minuto');
INSERT INTO CORPORATIVO.CP_UNIDADE_MEDIDA (ID_UNIDADE_MEDIDA, DESCR_UNIDADE_MEDIDA) VALUES (6, 'Segundo');
commit;

alter session set current_schema = corporativo;
insert into cp_marcador(descr_marcador, id_marcador, id_tp_marcador) values ( 'Fora do Prazo', 65, 1);
commit;

alter session set current_schema = sigasr;
alter table SR_CONFIGURACAO add (ID_ACORDO number(19, 0),
	CONSTRAINT "CONFIGURACAO_ACORDO_FK" 
			FOREIGN KEY ("ID_ACORDO") 
			REFERENCES "SIGASR"."SR_ACORDO" ("ID_ACORDO"));

--adicionado para aceitar a movimentacao de escalonamento
alter table sigasr.sr_movimentacao add ID_ITEM_CONFIGURACAO NUMBER(19,0);
alter table sigasr.sr_movimentacao add ID_ACAO NUMBER(19,0);
alter table sigasr.sr_movimentacao add MOTIVOESCALONAMENTO NUMBER(10,0);
insert into sigasr.sr_tipo_movimentacao (id_tipo_movimentacao, nome_tipo_movimentacao) values (24, 'Escalonamento');
