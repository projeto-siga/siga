
ALTER SESSION SET CURRENT_SCHEMA=sigasr;	

INSERT INTO corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) 
	VALUES (305, 'Associação de Configuração com Pesquisa', 1);
COMMIT;

CREATE TABLE "SIGASR"."SR_TIPO_ACAO"
	(
		"ID_TIPO_ACAO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"  NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6),
		"HIS_ID_INI"     NUMBER(19,0),
		"DESCR_TIPO_ACAO"  VARCHAR2(255 CHAR),
		"SIGLA_TIPO_ACAO"  VARCHAR2(255 CHAR),
		"TITULO_TIPO_ACAO" VARCHAR2(255 CHAR),
		"ID_PAI" 	NUMBER(19,0),
		PRIMARY KEY ("ID_TIPO_ACAO"),
		CONSTRAINT "TIPO_ACAO_TIPO_ACAO_PAI_FK" 
			FOREIGN KEY ("ID_PAI") REFERENCES "SIGASR"."SR_TIPO_ACAO" ("ID_TIPO_ACAO")
	);
	
CREATE SEQUENCE  "SIGASR"."SR_TIPO_ACAO_SEQ"
  MINVALUE 1 
  MAXVALUE 9999999999999999999999999999 
  INCREMENT BY 1 
  START WITH 1;
	
update sigasr.sr_acao set tipo_acao = null where tipo_acao is not null;
commit;

alter table sigasr.sr_acao 
ADD FOREIGN KEY (TIPO_ACAO) REFERENCES sigasr.sr_tipo_acao(id_tipo_acao);

--Copiar para a SR_TIPO_ACAO os dados da SR_ACAO
insert into sigasr.sr_tipo_acao (id_tipo_acao, his_id_ini, his_ativo, his_dt_ini, sigla_tipo_acao, titulo_tipo_acao, descr_tipo_acao) 
select (sigasr.sr_tipo_acao_seq.nextval), (sigasr.sr_tipo_acao_seq.currval), 1, sysdate, sigla_acao, titulo_acao, descr_acao from (
  select sigla_acao, titulo_acao, descr_acao from sigasr.sr_acao where his_dt_fim is null
);

--SETAR OS ID_PAI NA SR_TIPO_ACAO
update sigasr.sr_tipo_acao t set id_pai = (
  select id_tipo_acao from sigasr.sr_tipo_acao t2 where sigla_tipo_acao = (
    select sigla_acao from sigasr.sr_acao a where id_acao = (
      select id_pai from sigasr.sr_acao a2 where id_acao = (
        select max(id_acao) from sigasr.sr_acao where tipo_acao = t.id_tipo_acao
      )
    )
  )
);

--LIGAR AS DUAS TABELAS
update sigasr.sr_acao a set tipo_acao = (
  select max(id_tipo_acao) from sigasr.sr_tipo_acao where sigla_tipo_acao = a.sigla_acao 
) where his_dt_fim is null;

--LIGAR AS INST�NCIAS ANTIGAS
update sigasr.sr_acao a set tipo_acao = (
  select tipo_acao from sigasr.sr_acao where his_id_ini = a.his_id_ini and his_dt_fim is null
) where his_dt_fim is not null;

commit;

CREATE TABLE SR_PRIORIDADE_SOLICITACAO(
   ID_PRIORIDADE_SOLICITACAO NUMBER(19,0) primary key,
   ID_LISTA NUMBER(19,0) references SR_LISTA(ID_LISTA) NOT NULL,
   ID_SOLICITACAO NUMBER(19,0) references SR_SOLICITACAO(ID_SOLICITACAO) NOT NULL,
   NUM_POSICAO NUMBER(19,0),
   PRIORIDADE NUMBER(1),
   NAO_REPOSICIONAR_AUTOMATICO CHAR(1)  
  );
  
CREATE SEQUENCE SR_PRIORIDADE_SOLICITACAO_SEQ start with 1;

ALTER TABLE sr_configuracao ADD (prioridade_lista NUMBER(1));