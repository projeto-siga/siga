

ALTER SESSION SET CURRENT_SCHEMA = corporativo;
-- - - - - - - - - - - - - - - - - - - - - - 
-- - - Marcadores- - - - - - - - - - - - - -
-- - - - - - - - - - - - - - - - - - - - - - 
	
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (300, 	'A Receber',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (301,	'Em Andamento',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (302,	'Fechado',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (303,	'Pendente',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (304,	'Cancelado',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (305,	'Em Pré-atendimento',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (306,	'Em Pós-atendimento',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (307,	'Como cadastrante',	1);
	insert into cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (308,	'Como solicitante',	1);
  
	insert into cp_tipo_marca(id_tp_marca, descr_tp_marca) values (2, 'SIGA-SR');
  
-- - - - - - - - - - - - - - - - - - - - - - 
-- - - tipo de configuração -
-- - - - - - - - - - - - - - - - - - - - - -	
	
	insert into cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao) values (300,	'Designação');
	insert into cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao) values (301,	'Associação');
	

	
-- - - - - - - - - - - - - - - - - - - - - - 
-- - - Grants- - - - - - - - - - - - - - - -
-- - - - - - - - - - - - - - - - - - - - - - 

	grant select on CP_TIPO_CONFIGURACAO to sigasr;
	grant select on CP_IDENTIDADE to sigasr;
	grant select on CP_TIPO_IDENTIDADE to sigasr;
	grant select on DP_PESSOA to sigasr;
	grant select on DP_LOTACAO to sigasr;
	grant select on CP_MARCADOR to sigasr;
	grant select on CP_TIPO_MARCA to sigasr;
	grant select on CP_TIPO_MARCADOR to sigasr;
	grant select on CP_COMPLEXO to sigasr;
	grant select on DP_CARGO to sigasr;
	grant select on DP_FUNCAO_CONFIANCA to sigasr;
	grant select on CP_SERVICO to sigasr;
	grant select on CP_GRUPO to sigasr;
	grant select on CP_SITUACAO_CONFIGURACAO to sigasr;
	grant select on cp_feriado to sigasr;
	grant select on cp_localidade to sigasr;
	grant select on cp_modelo to sigasr;
	grant select on cp_orgao to sigasr;
	grant select on cp_papel to sigasr;
	grant select on cp_personalizacao to sigasr;
	grant select on cp_servico to sigasr;
	grant select on cp_tipo_configuracao to sigasr;
	grant select on cp_tipo_grupo to sigasr;
	grant select on cp_tipo_identidade to sigasr;
	grant select on cp_tipo_lotacao to sigasr;
	grant select on cp_tipo_papel to sigasr;
	grant select on cp_tipo_pessoa to sigasr;
	grant select on cp_uf to sigasr;
	grant select on dp_substituicao to sigasr;
	grant select on cp_modelo_seq to sigasr;
	grant select on CP_IDENTIDADE_SEQ to sigasr;
	grant select on cp_orgao_usuario to sigasr;
	grant select on cp_tipo_servico to sigasr;
	
	grant select, update, delete, insert on cp_marca to sigasr;
	grant select, update, insert on CP_CONFIGURACAO to sigasr;
	
	grant select on CP_CONFIGURACAO_SEQ to sigasr;

-- - - - - - - - - - - - - - - - - - - - - - 
-- - - Serviços- - - - - - - - - - - - - - -
-- - - - - - - - - - - - - - - - - - - - - - 

	--Existindo no banco um serviço SIGA-SR com ID 4:
	--enquanto estiver dando erro o iniciarTransacao(), fazer o seguinte:
	insert into cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (cp_servico_seq.nextval, 'SIGA-SR-ADM', 'Administrar', 4, 2);

	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, id_lotacao
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-SR-ADM'),
		sysdate, 
		(select id_lotacao from corporativo.dp_lotacao where sigla_lotacao = 'SESIA' and data_fim_lot is null and id_orgao_usu = 1)
	);
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, id_lotacao
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-SR'),
		sysdate, 
		(select id_lotacao from corporativo.dp_lotacao where sigla_lotacao = 'SESIA' and data_fim_lot is null and id_orgao_usu = 1)
	);
  commit;
	
	
	
	
	
	
ALTER SESSION SET CURRENT_SCHEMA = sigasr;

-- - - - - - - - - - - - - - - - - - - - - - 
-- - - Tabelas do sigasr - - - - - - - - - - 
-- - - - - - - - - - - - - - - - - - - - - - 

	CREATE SEQUENCE SR_ANDAMENTO_SEQ;
	CREATE SEQUENCE SR_SOLICITACAO_SEQ;
	CREATE SEQUENCE SR_ARQUIVO_SEQ;
	CREATE SEQUENCE SR_ATRIBUTO_SEQ;
	CREATE SEQUENCE SR_ITEM_CONFIGURACAO_SEQ;
	CREATE SEQUENCE SR_SERVICO_SEQ;
	CREATE SEQUENCE SR_TIPO_ATRIBUTO_SEQ;

	CREATE TABLE "SIGASR"."SR_ITEM_CONFIGURACAO"
	(
		"ID_ITEM_CONFIGURACAO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"            NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6),
		"HIS_ID_INI"               NUMBER(19,0),
		"DESCR_ITEM_CONFIGURACAO"  VARCHAR2(255 CHAR),
		"SIGLA_ITEM_CONFIGURACAO"  VARCHAR2(255 CHAR),
		"TITULO_ITEM_CONFIGURACAO" VARCHAR2(255 CHAR),
		PRIMARY KEY ("ID_ITEM_CONFIGURACAO")
	);
  
	CREATE TABLE "SIGASR"."SR_SERVICO"
	(
		"ID_SERVICO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"  NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6),
		"HIS_ID_INI"     NUMBER(19,0),
		"DESCR_SERVICO"  VARCHAR2(255 CHAR),
		"SIGLA_SERVICO"  VARCHAR2(255 CHAR),
		"TITULO_SERVICO" VARCHAR2(255 CHAR),
		PRIMARY KEY ("ID_SERVICO")
	);
	
	CREATE TABLE "SIGASR"."SR_ARQUIVO"
	(
		"ID_ARQUIVO" NUMBER(19,0) NOT NULL,
		"BLOB" BLOB,
		"DESCRICAO"    VARCHAR2(255 CHAR),
		"MIME"         VARCHAR2(255 CHAR),
		"NOME_ARQUIVO" VARCHAR2(255 CHAR),
		PRIMARY KEY ("ID_ARQUIVO")
	);
	
	CREATE TABLE "SIGASR"."SR_SOLICITACAO"
	(
		"ID_SOLICITACAO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"      NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6),
		"HIS_ID_INI" NUMBER(19,0),
		"DESCR_SOLICITACAO" CLOB,
		"DT_REG" TIMESTAMP (6),
		"FORMAACOMPANHAMENTO"  NUMBER(10,0),
		"GRAVIDADE"            NUMBER(10,0),
		"NUM_SEQUENCIA"        NUMBER(19,0),
		"NUM_SOLICITACAO"      NUMBER(19,0),
		"TEL_PRINCIPAL"        VARCHAR2(255 CHAR),
		"TENDENCIA"            NUMBER(10,0),
		"URGENCIA"             NUMBER(10,0),
		"ID_ARQUIVO"           NUMBER(19,0),
		"ID_CADASTRANTE"       NUMBER(19,0),
		"ID_ITEM_CONFIGURACAO" NUMBER(19,0),
		"ID_COMPLEXO"          NUMBER(19,0),
		"ID_LOTA_CADASTRANTE"  NUMBER(19,0),
		"ID_LOTA_SOLICITANTE"  NUMBER(19,0),
		"ID_ORGAO_USU"         NUMBER(19,0),
		"ID_SERVICO"           NUMBER(19,0),
		"ID_SOLICITACAO_PAI"   NUMBER(19,0),
		"ID_SOLICITANTE"       NUMBER(19,0),
		PRIMARY KEY ("ID_SOLICITACAO"),
		CONSTRAINT "SOLICITACAO_SERVICO_FK" FOREIGN KEY ("ID_SERVICO") REFERENCES "SIGASR"."SR_SERVICO" ("ID_SERVICO"),
		CONSTRAINT "SOLICITACAO_ITEM_FK" FOREIGN KEY ("ID_ITEM_CONFIGURACAO") REFERENCES "SIGASR"."SR_ITEM_CONFIGURACAO" ("ID_ITEM_CONFIGURACAO"),
		CONSTRAINT "SOLICITACAO_ARQUIVO_FK" FOREIGN KEY ("ID_ARQUIVO") REFERENCES "SIGASR"."SR_ARQUIVO" ("ID_ARQUIVO"),
		CONSTRAINT "SOLICITACAO_SOLICITACAOPAI_FK" FOREIGN KEY ("ID_SOLICITACAO_PAI") REFERENCES "SIGASR"."SR_SOLICITACAO" ("ID_SOLICITACAO"),
		CONSTRAINT "SOLICITACAO_SOLICITACAOINI_FK" FOREIGN KEY ("HIS_ID_INI") REFERENCES "SIGASR"."SR_SOLICITACAO" ("ID_SOLICITACAO")
	);
 
	CREATE TABLE "SIGASR"."SR_TIPO_ATRIBUTO"
	(
		"ID_TIPO_ATRIBUTO" NUMBER(19,0) NOT NULL,
		"HIS_ATIVO"        NUMBER(10,0),
		"HIS_DT_FIM" TIMESTAMP (6),
		"HIS_DT_INI" TIMESTAMP (6),
		"HIS_ID_INI" NUMBER(19,0),
		"DESCRICAO"  VARCHAR2(255 CHAR),
		"NOME"       VARCHAR2(255 CHAR),
		PRIMARY KEY ("ID_TIPO_ATRIBUTO")
	);
	
	CREATE TABLE "SIGASR"."SR_ATRIBUTO"
	(
		"ID_ATRIBUTO"      NUMBER(19,0) NOT NULL,
		"VALOR_ATRIBUTO"   VARCHAR2(255 CHAR),
		"ID_SOLICITACAO"   NUMBER(19,0),
		"ID_TIPO_ATRIBUTO" NUMBER(19,0),
		PRIMARY KEY ("ID_ATRIBUTO"),
		CONSTRAINT "ATRIBUTO_SOLICITACAO_FK" FOREIGN KEY ("ID_SOLICITACAO") REFERENCES "SIGASR"."SR_SOLICITACAO" ("ID_SOLICITACAO"),
		CONSTRAINT "ATRIBUTO_TIPOATRIBUTO_FK" FOREIGN KEY ("ID_TIPO_ATRIBUTO") REFERENCES "SIGASR"."SR_TIPO_ATRIBUTO" ("ID_TIPO_ATRIBUTO")
	);
	
	CREATE TABLE "SIGASR"."SR_ANDAMENTO"
	(
		"ID_ANDAMENTO"    NUMBER(19,0) NOT NULL,
		"DESCR_ANDAMENTO" VARCHAR2(255 CHAR),
		"DT_CANCELAMENTO" TIMESTAMP (6),
		"DT_REG" TIMESTAMP (6),
		"ESTADO"              NUMBER(10,0),
		"ID_ARQUIVO"          NUMBER(19,0),
		"ID_ATENDENTE"        NUMBER(19,0),
		"ID_CADASTRANTE"      NUMBER(19,0) NOT NULL,
		"ID_CANCELADOR"       NUMBER(19,0),
		"ID_LOTA_ATENDENTE"   NUMBER(19,0) NOT NULL,
		"ID_LOTA_CADASTRANTE" NUMBER(19,0) NOT NULL,
		"ID_LOTA_CANCELADOR"  NUMBER(19,0),
		"ID_SOLICITACAO"      NUMBER(19,0),
		PRIMARY KEY ("ID_ANDAMENTO"),
		CONSTRAINT "ANDAMENTO_SOLICITACAO_FK" FOREIGN KEY ("ID_SOLICITACAO") REFERENCES "SIGASR"."SR_SOLICITACAO" ("ID_SOLICITACAO"),
		CONSTRAINT "ANDAMENTO_ARQUIVO_FK" FOREIGN KEY ("ID_ARQUIVO") REFERENCES "SIGASR"."SR_ARQUIVO" ("ID_ARQUIVO")
	);
	
	CREATE TABLE "SIGASR"."SR_CONFIGURACAO"
	(
		"ID_CONFIGURACAO_SR"      NUMBER(19,0) NOT NULL,
		"FG_ATRIBUTO_OBRIGATORIO" CHAR(1 CHAR),
		"FORMA_ACOMPANHAMENTO"    NUMBER(10,0),
		"GRAVIDADE"               NUMBER(10,0),
		"PESQUISA_SATISFACAO"     CHAR(1 CHAR),
		"TENDENCIA"               NUMBER(10,0),
		"URGENCIA"                NUMBER(10,0),
		"ID_ATENDENTE"             NUMBER(19,0),
		"ID_ITEM_CONFIGURACAO"    NUMBER(19,0),
		"ID_POS_ATENDENTE"        NUMBER(19,0),
		"ID_PRE_ATENDENTE"        NUMBER(19,0),
		"ID_SERVICO"              NUMBER(19,0),
		"ID_TIPO_ATRIBUTO"        NUMBER(19,0),
		PRIMARY KEY ("ID_CONFIGURACAO_SR"),
		CONSTRAINT "CONFIGURACAO_SERVICO_FK" FOREIGN KEY ("ID_SERVICO") REFERENCES "SIGASR"."SR_SERVICO" ("ID_SERVICO"),
		CONSTRAINT "CONFIGURACAO_ITEM_FK" FOREIGN KEY ("ID_ITEM_CONFIGURACAO") REFERENCES "SIGASR"."SR_ITEM_CONFIGURACAO" ("ID_ITEM_CONFIGURACAO"),
		CONSTRAINT "CONFIGURACAO_TIPOATRIBUTO_FK" FOREIGN KEY ("ID_TIPO_ATRIBUTO") REFERENCES "SIGASR"."SR_TIPO_ATRIBUTO" ("ID_TIPO_ATRIBUTO")
	);