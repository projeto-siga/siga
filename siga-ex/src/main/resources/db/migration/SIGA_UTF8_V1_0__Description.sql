-------------------------------------------
--	SCRIPT:CRIACAO DO BANCO
-------------------------------------------

--SET DEFINE OFF;

ALTER SESSION SET CURRENT_SCHEMA=siga;

CREATE SEQUENCE  "SIGA"."EX_ANEXACAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_CLASSIFICACAO_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 2545 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_COMPETENCIA_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_DOCUMENTO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_ESTADO_DOC_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_FORMA_DOCUMENTO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 100 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_LOCALIZACAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_MOBIL_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_MODELO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 740 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_MOVIMENTACAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_PREENCHIMENTO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TEMPORALIDADE_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 178 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TIPO_DESPACHO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 18 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TIPO_DESTINACAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 83 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TIPO_DOCUMENTO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 4 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TIPO_MOVIMENTACAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 46 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_TRANSICAO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."EX_VIA_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 9845 CACHE 20 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SIGA"."HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;


  CREATE TABLE "SIGA"."EX_BOLETIM_DOC" 
   (	"ID_BOLETIM_DOC" NUMBER, 
	"ID_DOC" NUMBER, 
	"ID_BOLETIM" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EX_CLASSIFICACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_CLASSIFICACAO" 
   (	"ID_CLASSIFICACAO" NUMBER(10,0), 
	"COD_ASSUNTO_PRINCIPAL" NUMBER(1,0), 
	"COD_ASSUNTO_SECUNDARIO" NUMBER(1,0), 
	"COD_CLASSE" NUMBER(2,0), 
	"COD_SUBCLASSE" NUMBER(2,0), 
	"COD_ATIVIDADE" NUMBER(2,0), 
	"DESCR_CLASSIFICACAO" VARCHAR2(4000) DEFAULT NULL, 
	"FACILITADOR_CLASS" VARCHAR2(4000) DEFAULT NULL, 
	"ID_REG_INI" NUMBER(10,0), 
	"DT_INI_REG" DATE, 
	"DT_FIM_REG" DATE, 
	"COD_ASSUNTO" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."ID_CLASSIFICACAO" IS 'Identificador interno do assunto nosistema, é um número sequencial gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."COD_ASSUNTO_PRINCIPAL" IS 'Código de clasificação do assunto principal. ';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."COD_ASSUNTO_SECUNDARIO" IS 'Código de classificação do código secundário';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."COD_CLASSE" IS 'Código da Classe funcional do documento. São dez classes principais, de acordo com o foco administrativo.';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."COD_SUBCLASSE" IS 'Código da sub-classe do documento ou processo. ';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."COD_ATIVIDADE" IS 'Código da atividade funcional do documento ou processo.';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."DESCR_CLASSIFICACAO" IS 'Descrição do escopo do assunto a que se refere o documento ou processo.';
 
   COMMENT ON COLUMN "SIGA"."EX_CLASSIFICACAO"."FACILITADOR_CLASS" IS 'Texto explicando as reggras pré estabelecidas para a criação deste tipo de documento.';
 
   COMMENT ON TABLE "SIGA"."EX_CLASSIFICACAO"  IS 'Armazena os códigos de classificacao de assuntos de expedientes segundo a tabela de temporalidade proposta na Gestão Documental.';
--------------------------------------------------------
--  DDL for Table EX_COMPETENCIA
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_COMPETENCIA" 
   (	"FG_COMPETENCIA" VARCHAR2(1) DEFAULT NULL, 
	"ID_PESSOA" NUMBER(10,0), 
	"ID_CARGO" NUMBER(3,0), 
	"ID_LOTACAO" NUMBER(5,0), 
	"DT_INI_VIG_COMPETENCIA" DATE, 
	"DT_FIM_VIG_COMPETENCIA" DATE, 
	"ID_COMPETENCIA" NUMBER(10,0), 
	"ID_FUNCAO_CONFIANCA" NUMBER(3,0), 
	"ID_FORMA_DOC" NUMBER(10,0)
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_COMPETENCIA"."DT_INI_VIG_COMPETENCIA" IS 'Data de início da competencia, tem a função de preservar o histórico.';
 
   COMMENT ON COLUMN "SIGA"."EX_COMPETENCIA"."DT_FIM_VIG_COMPETENCIA" IS 'Data de fim da competencia, tem a função de preservar o histórico.';
 
   COMMENT ON COLUMN "SIGA"."EX_COMPETENCIA"."ID_COMPETENCIA" IS 'Número sequencial gerado automaticamente que identifica internamente a competência.';
 
   COMMENT ON TABLE "SIGA"."EX_COMPETENCIA"  IS 'Esta tabela tem a função de identificar se o usuário tem competência para gerar ou acessar determinado tipo de documento. Armazena também o histórico de competência.';
--------------------------------------------------------
--  DDL for Table EX_CONFIGURACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_CONFIGURACAO" 
   (	"ID_CONFIGURACAO_EX" NUMBER(10,0), 
	"ID_TP_MOV" NUMBER(10,0), 
	"ID_TP_DOC" NUMBER(10,0), 
	"ID_TP_FORMA_DOC" NUMBER, 
	"ID_FORMA_DOC" NUMBER(10,0), 
	"ID_MOD" NUMBER(10,0), 
	"ID_CLASSIFICACAO" NUMBER(10,0), 
	"ID_VIA" NUMBER(2,0), 
	"ID_NIVEL_ACESSO" NUMBER(10,0), 
	"ID_PAPEL" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EX_DOCUMENTO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_DOCUMENTO" 
   (	"ID_DOC" NUMBER(10,0), 
	"NUM_EXPEDIENTE" NUMBER(10,0), 
	"ANO_EMISSAO" NUMBER(10,0), 
	"ID_TP_DOC" NUMBER(10,0), 
	"ID_CADASTRANTE" NUMBER(10,0), 
	"ID_LOTA_CADASTRANTE" NUMBER(5,0), 
	"ID_SUBSCRITOR" NUMBER(10,0), 
	"ID_LOTA_SUBSCRITOR" NUMBER(5,0), 
	"DESCR_DOCUMENTO" VARCHAR2(4000) DEFAULT NULL, 
	"DT_DOC" DATE, 
	"DT_REG_DOC" DATE, 
	"NM_SUBSCRITOR_EXT" VARCHAR2(256), 
	"NUM_EXT_DOC" VARCHAR2(32), 
	"CONTEUDO_BLOB_DOC" BLOB, 
	"NM_ARQ_DOC" VARCHAR2(256), 
	"CONTEUDO_TP_DOC" VARCHAR2(128), 
	"ID_DESTINATARIO" NUMBER(10,0), 
	"ID_LOTA_DESTINATARIO" NUMBER(5,0), 
	"NM_DESTINATARIO" VARCHAR2(256), 
	"DT_FECHAMENTO" DATE, 
	"ASSINATURA_BLOB_DOC" BLOB, 
	"ID_MOD" NUMBER(15,0), 
	"ID_ORGAO_USU" NUMBER(10,0), 
	"ID_CLASSIFICACAO" NUMBER(10,0), 
	"ID_FORMA_DOC" NUMBER(2,0), 
	"FG_PESSOAL" VARCHAR2(1) DEFAULT 'N', 
	"ID_ORGAO_DESTINATARIO" NUMBER(10,0), 
	"ID_ORGAO" NUMBER(10,0), 
	"OBS_ORGAO_DOC" VARCHAR2(256), 
	"NM_ORGAO_DESTINATARIO" VARCHAR2(256), 
	"FG_SIGILOSO" VARCHAR2(1) DEFAULT 'N', 
	"NM_FUNCAO_SUBSCRITOR" VARCHAR2(128), 
	"FG_ELETRONICO" VARCHAR2(1) DEFAULT 'N', 
	"NUM_ANTIGO_DOC" VARCHAR2(32), 
	"ID_LOTA_TITULAR" NUMBER(10,0), 
	"ID_TITULAR" NUMBER(10,0), 
	"NUM_AUX_DOC" VARCHAR2(32), 
	"DSC_CLASS_DOC" VARCHAR2(4000), 
	"ID_NIVEL_ACESSO" NUMBER(10,0), 
	"ID_DOC_PAI" NUMBER, 
	"NUM_VIA_DOC_PAI" NUMBER, 
	"ID_DOC_ANTERIOR" NUMBER(10,0), 
	"ID_MOB_PAI" NUMBER, 
	"NUM_SEQUENCIA" NUMBER(4,0), 
	"NUM_PAGINAS" NUMBER(4,0)
   ) ;

   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ID_DOC" IS 'Numero sequencial de identificação interna do documento. É gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."NUM_EXPEDIENTE" IS 'Número de identificação do expediente para a consulta pelos usuários (sequencial zerado na mudança do ano).';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ANO_EMISSAO" IS 'Ano de emissão do documento.';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."FG_SIGILOSO" IS 'Flag que indica se o doccumento é sigiloso ou não, independente da lotação.';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."NM_FUNCAO_SUBSCRITOR" IS 'Campo livre onde o usuario descreve uma função (pode ser informal ou temporaria) do subscritor';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."FG_ELETRONICO" IS 'Esta flag indica se o documento é eletrînoco ou não.';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ID_LOTA_TITULAR" IS 'identifica a lotação que está sofrendo a documentação.';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ID_TITULAR" IS 'identifica o titular no caso de um documento gerado por substituto';
 
   COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."DSC_CLASS_DOC" IS 'O campo é para guardar uma descrição de classificação (informada pelo
usuário), quando o usuário selecionar uma classificação intermediária (sem
vias). Isso vai ocorrer quando ele não encontrar uma classificação adequada
ao documento, escolhendo uma classificação aproximada.
';
 
   COMMENT ON TABLE "SIGA"."EX_DOCUMENTO"  IS 'Esta tabela tem como objetivo armazenar os documentos cadastrados no sistema.';
--------------------------------------------------------
--  DDL for Table EX_EMAIL_NOTIFICACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_EMAIL_NOTIFICACAO" 
   (	"ID_EMAIL_NOTIFICACAO" NUMBER, 
	"ID_LOTACAO" NUMBER, 
	"EMAIL" VARCHAR2(60)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_ESTADO_DOC
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_ESTADO_DOC" 
   (	"ID_ESTADO_DOC" NUMBER(10,0), 
	"DESC_ESTADO_DOC" VARCHAR2(128) DEFAULT NULL, 
	"ORDEM_ESTADO_DOC" NUMBER(2,0)
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_ESTADO_DOC"."ID_ESTADO_DOC" IS 'Número sequencial que identifica o estado od documento internamente no sistema. (Gerado Automaticamente)';
 
   COMMENT ON COLUMN "SIGA"."EX_ESTADO_DOC"."DESC_ESTADO_DOC" IS 'Descrição do estado do documento.';
 
   COMMENT ON TABLE "SIGA"."EX_ESTADO_DOC"  IS 'Tabela que armazena os possíveis estados do documento durante o seu ciclo de vida.';
--------------------------------------------------------
--  DDL for Table EX_ESTADO_TP_MOV
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_ESTADO_TP_MOV" 
   (	"ID_ESTADO_DOC" NUMBER(10,0), 
	"ID_TP_MOV" NUMBER(10,0)
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_ESTADO_TP_MOV"."ID_ESTADO_DOC" IS 'Código de identificação do estado de um documento é chave estrangeira, refere-se a datela de estados (EX_ESTADO_DOC)';
 
   COMMENT ON COLUMN "SIGA"."EX_ESTADO_TP_MOV"."ID_TP_MOV" IS 'Código do tipo de movimentação que o documento pode sofrer, encontrando-se no estado apresentado na mesma tupla.';
 
   COMMENT ON TABLE "SIGA"."EX_ESTADO_TP_MOV"  IS 'Tabela que parametriza as possíveis movimentações que um documento pode sofrer de acordo com o estado em que se encontra.';
--------------------------------------------------------
--  DDL for Table EX_FORMA_DOCUMENTO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_FORMA_DOCUMENTO" 
   (	"ID_FORMA_DOC" NUMBER(2,0), 
	"DESCR_FORMA_DOC" VARCHAR2(64) DEFAULT NULL, 
	"SIGLA_FORMA_DOC" VARCHAR2(3) DEFAULT NULL, 
	"ID_TIPO_FORMA_DOC" NUMBER
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_FORMA_DOCUMENTO"."ID_FORMA_DOC" IS 'Numero que identifica internamente a forma no sistema. Gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_FORMA_DOCUMENTO"."DESCR_FORMA_DOC" IS 'Descrição da forma que um documento pode ser apresentado ou veiculado.';
 
   COMMENT ON COLUMN "SIGA"."EX_FORMA_DOCUMENTO"."SIGLA_FORMA_DOC" IS 'Sigla da forma do documento.';
 
   COMMENT ON TABLE "SIGA"."EX_FORMA_DOCUMENTO"  IS 'Tabela que apresenta as formas em que um documento pode ser apresentado ou veiculado.';
--------------------------------------------------------
--  DDL for Table EX_MOBIL
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_MOBIL" 
   (	"ID_MOBIL" NUMBER, 
	"ID_DOC" NUMBER, 
	"ID_TIPO_MOBIL" NUMBER, 
	"NUM_SEQUENCIA" NUMBER(2,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_MODELO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_MODELO" 
   (	"ID_MOD" NUMBER(15,0), 
	"NM_MOD" VARCHAR2(128) DEFAULT NULL, 
	"DESC_MOD" VARCHAR2(256) DEFAULT NULL, 
	"CONTEUDO_BLOB_MOD" BLOB, 
	"CONTEUDO_TP_BLOB" VARCHAR2(128) DEFAULT NULL, 
	"NM_ARQ_MOD" VARCHAR2(256) DEFAULT NULL, 
	"ID_CLASSIFICACAO" NUMBER(10,0), 
	"ID_FORMA_DOC" NUMBER(2,0), 
	"ID_CLASS_CRIACAO_VIA" NUMBER(10,0), 
	"ID_NIVEL_ACESSO" NUMBER(10,0)
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_MODELO"."ID_MOD" IS 'Numero de identificação interna do modelo no sistema. Gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."NM_MOD" IS 'Nome do modelo é aquele que será apresentado ao usuário.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."DESC_MOD" IS 'Descrição do modelo apresenta a utilização e características do modelo.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."CONTEUDO_BLOB_MOD" IS 'É o modelo em si, o arquivo que será apresentado ao usuário.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."CONTEUDO_TP_BLOB" IS 'Descrição do CONTENT TYPE do arquivo armazenado, que é uma string independente do tipo de arquivo declarado na extensão (ex.: *.DOC, *.RTF, etc) utilizado como identificador de tipo de arquivo na emissão de  arquivos na internet.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."NM_ARQ_MOD" IS 'Nome do arquivo eletrônico do modelo.';
 
   COMMENT ON COLUMN "SIGA"."EX_MODELO"."ID_NIVEL_ACESSO" IS 'identificador do tipó de sigilo que um modelo pode ser associado';
 
   COMMENT ON TABLE "SIGA"."EX_MODELO"  IS 'Tabela que armazena modelos de documentos, não é obrigatória a utilização de um documento fornrcido pelo sistema.';
--------------------------------------------------------
--  DDL for Table EX_MODELO_TP_DOC_PUBLICACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_MODELO_TP_DOC_PUBLICACAO" 
   (	"ID_MOD" NUMBER, 
	"ID_DOC_PUBLICACAO" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EX_MOVIMENTACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_MOVIMENTACAO" 
   (	"ID_MOV" NUMBER(10,0), 
	"ID_DOC" NUMBER, 
	"ID_DOC_PAI" NUMBER(10,0), 
	"ID_TP_MOV" NUMBER(10,0), 
	"ID_ESTADO_DOC" NUMBER, 
	"ID_TP_DESPACHO" NUMBER(10,0), 
	"ID_CADASTRANTE" NUMBER(10,0), 
	"ID_LOTA_CADASTRANTE" NUMBER(5,0), 
	"ID_SUBSCRITOR" NUMBER(10,0), 
	"ID_LOTA_SUBSCRITOR" NUMBER(5,0), 
	"DT_MOV" DATE, 
	"DT_INI_MOV" DATE, 
	"NUM_VIA" NUMBER(2,0), 
	"CONTEUDO_BLOB_MOV" BLOB, 
	"ID_MOV_CANCELADORA" NUMBER(10,0), 
	"NM_ARQ_MOV" VARCHAR2(256), 
	"CONTEUDO_TP_MOV" VARCHAR2(128), 
	"DT_FIM_MOV" DATE, 
	"ID_LOTA_RESP" NUMBER(5,0), 
	"ID_RESP" NUMBER(10,0), 
	"DESCR_MOV" VARCHAR2(400), 
	"ASSINATURA_BLOB_MOV" BLOB, 
	"ID_DESTINO_FINAL" NUMBER(10,0), 
	"ID_LOTA_DESTINO_FINAL" NUMBER(5,0), 
	"NUM_VIA_DOC_PAI" NUMBER(2,0), 
	"ID_DOC_REF" NUMBER(10,0), 
	"NUM_VIA_DOC_REF" NUMBER(2,0), 
	"OBS_ORGAO_MOV" VARCHAR2(256), 
	"ID_ORGAO" NUMBER(10,0), 
	"ID_MOV_REF" NUMBER(10,0), 
	"ID_LOTA_TITULAR" NUMBER(10,0), 
	"ID_TITULAR" NUMBER(10,0), 
	"NM_FUNCAO_SUBSCRITOR" VARCHAR2(128), 
	"NUM_PROC_ADM" NUMBER(*,0), 
	"ID_NIVEL_ACESSO" NUMBER(10,0), 
	"DT_DISP_PUBLICACAO" DATE, 
	"DT_EFETIVA_PUBLICACAO" DATE, 
	"DT_EFETIVA_DISP_PUBLICACAO" DATE, 
	"PAG_PUBLICACAO" VARCHAR2(15), 
	"NUM_TRF_PUBLICACAO" NUMBER(10,0), 
	"CADERNO_PUBLICACAO_DJE" VARCHAR2(1), 
	"ID_MOBIL" NUMBER, 
	"ID_MOB_REF" NUMBER, 
	"NUM_PAGINAS" NUMBER(4,0), 
	"NUM_PAGINAS_ORI" NUMBER(4,0), 
	"ID_PAPEL" NUMBER
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_MOV" IS 'Código sequencial interno, gerado automaticamente pelo sistema.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_DOC" IS 'Código identificador do documento a que se refere a movimentação.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_DOC_PAI" IS 'Código do documento principal, caso o documenro mensionado por ID_DOC seja anexado, ou juntado a outro documento.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."NUM_VIA" IS 'Número da via do documento mensionado por ID_DOC a que se refere a movimentação.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_MOV_CANCELADORA" IS 'Código da movimentação que cancela esta movimentação.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."NUM_VIA_DOC_PAI" IS 'Número da via do documento principal a que o documento mensionado por ID_DOC foi anexado ou juntado.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_DOC_REF" IS 'Código do documento que serve de referência para o doc mensionado por ID_DOC';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."NUM_VIA_DOC_REF" IS 'Número da via do documento que serve de referência para o doc mensionado por ID_DOC';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_LOTA_TITULAR" IS 'identifica a lotação que está em substituição';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_TITULAR" IS 'identifica se a moviemntação foi efetuada por sub. de funcao';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."NM_FUNCAO_SUBSCRITOR" IS 'Campo livre onde o usuario descreve uma função (pode ser informal ou temporaria) do subscritor';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_NIVEL_ACESSO" IS 'identificador do sigilo a ser redefinido para um documento ou cópia.';
 
   COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."DT_DISP_PUBLICACAO" IS 'será usada na movimentação de agendamento de publicação de expedientes. Nessa movimentação, define-se uma data chamada --data de disponibilização da publicação.';
 
   COMMENT ON TABLE "SIGA"."EX_MOVIMENTACAO"  IS 'Tabela que armazena o histórico de movimentações do documento, incluindo responsáveis, tipo de movimentação, estado em que o documento se encontra, anexo, via alterada e etc.';
--------------------------------------------------------
--  DDL for Table EX_NIVEL_ACESSO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_NIVEL_ACESSO" 
   (	"ID_NIVEL_ACESSO" NUMBER(10,0), 
	"NM_NIVEL_ACESSO" VARCHAR2(50), 
	"DSC_NIVEL_ACESSO" VARCHAR2(256), 
	"GRAU_NIVEL_ACESSO" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_NUMERACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_NUMERACAO" 
   (	"ID_ORGAO_USU" NUMBER(10,0), 
	"ID_FORMA_DOC" NUMBER(2,0), 
	"ANO_EMISSAO" NUMBER(10,0), 
	"NUM_EXPEDIENTE" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_PAPEL
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_PAPEL" 
   (	"ID_PAPEL" NUMBER, 
	"DESC_PAPEL" VARCHAR2(20)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_PREENCHIMENTO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_PREENCHIMENTO" 
   (	"ID_PREENCHIMENTO" NUMBER(10,0), 
	"ID_LOTACAO" NUMBER(10,0), 
	"ID_MOD" NUMBER(10,0), 
	"EX_NOME_PREENCHIMENTO" VARCHAR2(256) DEFAULT NULL, 
	"PREENCHIMENTO_BLOB" BLOB
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_PREENCHIMENTO"."ID_PREENCHIMENTO" IS 'Campo sequencial de identificaçãom interna do preenchimento. Numero gerado autumaticamente pelo sistema';
 
   COMMENT ON COLUMN "SIGA"."EX_PREENCHIMENTO"."EX_NOME_PREENCHIMENTO" IS 'Nome ou descrição do preenchimento, tem a finalidade de facilitar a escolha do preenchimento pelo usuário.';
 
   COMMENT ON COLUMN "SIGA"."EX_PREENCHIMENTO"."PREENCHIMENTO_BLOB" IS 'conteudo do preenchimento a ser preservado';
 
   COMMENT ON TABLE "SIGA"."EX_PREENCHIMENTO"  IS 'Esta tabela possui o objetivo de salvar a
configuração do preenchimento da tela de cadastro de expediente, para
facilitar a criação de novos expedientes que venham a ter preenchimento
semelhante dos campos.';
--------------------------------------------------------
--  DDL for Table EX_SITUACAO_CONFIGURACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_SITUACAO_CONFIGURACAO" 
   (	"ID_SIT_CONFIGURACAO" NUMBER, 
	"DSC_SIT_CONFIGURACAO" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TEMPORALIDADE
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TEMPORALIDADE" 
   (	"ID_TEMPORALIDADE" NUMBER(3,0), 
	"DESC_TEMPORALIDADE" VARCHAR2(128), 
	"PERMANENCIA_ARQUIVO" NUMBER(5,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TIPO_DESPACHO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_DESPACHO" 
   (	"ID_TP_DESPACHO" NUMBER(10,0), 
	"DESC_TP_DESPACHO" VARCHAR2(256), 
	"FG_ATIVO_TP_DESPACHO" CHAR(1)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TIPO_DESTINACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_DESTINACAO" 
   (	"ID_TP_DESTINACAO" NUMBER(10,0), 
	"DESCR_TIPO_DESTINACAO" VARCHAR2(256) DEFAULT NULL, 
	"FACILITADOR_DEST" VARCHAR2(4000) DEFAULT NULL
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_TIPO_DESTINACAO"."ID_TP_DESTINACAO" IS 'Numero sequencial de identificação interna. Gerado automaticamente pelo sistema';
 
   COMMENT ON COLUMN "SIGA"."EX_TIPO_DESTINACAO"."DESCR_TIPO_DESTINACAO" IS 'Descrição do tipo de destinação final da via do documento (ex.: Eliminação, Guarda Permanente)';
 
   COMMENT ON COLUMN "SIGA"."EX_TIPO_DESTINACAO"."FACILITADOR_DEST" IS 'Texto que esclarece o siginificado do tipo de destinação. Campo livre.';
 
   COMMENT ON TABLE "SIGA"."EX_TIPO_DESTINACAO"  IS 'Tabela de códigos dos tipos de destinação previstas para as vias dos expedientes';
--------------------------------------------------------
--  DDL for Table EX_TIPO_DOCUMENTO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_DOCUMENTO" 
   (	"ID_TP_DOC" NUMBER(10,0), 
	"DESCR_TIPO_DOCUMENTO" VARCHAR2(256) DEFAULT NULL
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_TIPO_DOCUMENTO"."ID_TP_DOC" IS 'Numero sequencial do tipo de documento, é gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_TIPO_DOCUMENTO"."DESCR_TIPO_DOCUMENTO" IS 'Descrição do tipo de documeto.';
 
   COMMENT ON TABLE "SIGA"."EX_TIPO_DOCUMENTO"  IS 'Tabela de códigos dos tipos de documentos (memorando, informação, referência, ato...)';
--------------------------------------------------------
--  DDL for Table EX_TIPO_FORMA_DOCUMENTO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_FORMA_DOCUMENTO" 
   (	"ID_TIPO_FORMA_DOC" NUMBER, 
	"DESC_TIPO_FORMA_DOC" VARCHAR2(60), 
	"NUMERACAO_UNICA" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TIPO_MOBIL
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_MOBIL" 
   (	"ID_TIPO_MOBIL" NUMBER, 
	"DESC_TIPO_MOBIL" VARCHAR2(20)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TIPO_MOVIMENTACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TIPO_MOVIMENTACAO" 
   (	"ID_TP_MOV" NUMBER(10,0), 
	"DESCR_TIPO_MOVIMENTACAO" VARCHAR2(256) DEFAULT NULL
   ) ;
 

   COMMENT ON COLUMN "SIGA"."EX_TIPO_MOVIMENTACAO"."ID_TP_MOV" IS 'Numero sequencial de identificação interna do tipo de movimentaçã, é gerado automaticamente.';
 
   COMMENT ON COLUMN "SIGA"."EX_TIPO_MOVIMENTACAO"."DESCR_TIPO_MOVIMENTACAO" IS 'Descrição do tipo de movimentanção.';
 
   COMMENT ON TABLE "SIGA"."EX_TIPO_MOVIMENTACAO"  IS 'Tabela de códigos dos tipos de movimentacao (abertura, transferência, associacao, recadastro...)';
--------------------------------------------------------
--  DDL for Table EX_TP_DOC_PUBLICACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TP_DOC_PUBLICACAO" 
   (	"ID_DOC_PUBLICACAO" NUMBER, 
	"NM_DOC_PUBLICACAO" VARCHAR2(256), 
	"CARATER" VARCHAR2(1)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TP_FORMA_DOC
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TP_FORMA_DOC" 
   (	"ID_FORMA_DOC" NUMBER(2,0), 
	"ID_TP_DOC" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_TP_MOV_ESTADO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_TP_MOV_ESTADO" 
   (	"ID_TP_MOV" NUMBER(10,0), 
	"ID_ESTADO_DOC" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EX_VIA
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_VIA" 
   (	"ID_VIA" NUMBER(10,0), 
	"ID_CLASSIFICACAO" NUMBER(10,0), 
	"ID_TP_DESTINACAO" NUMBER(10,0), 
	"COD_VIA" VARCHAR2(1), 
	"ID_TMP_CORRENTE" NUMBER(3,0), 
	"ID_TMP_INTERMEDIARIO" NUMBER(3,0), 
	"OBS" VARCHAR2(4000), 
	"FG_MAIOR" CHAR(1), 
	"ID_DESTINACAO_FINAL" NUMBER(2,0), 
	"ID_REG_INI" NUMBER(10,0), 
	"DT_INI_REG" DATE, 
	"DT_FIM_REG" DATE
   ) ;

--REM INSERTING into SIGA.EX_BOLETIM_DOC

--REM INSERTING into SIGA.EX_CLASSIFICACAO
Insert into SIGA.EX_CLASSIFICACAO (ID_CLASSIFICACAO,COD_ASSUNTO_PRINCIPAL,COD_ASSUNTO_SECUNDARIO,COD_CLASSE,COD_SUBCLASSE,COD_ATIVIDADE,DESCR_CLASSIFICACAO,FACILITADOR_CLASS,ID_REG_INI,DT_INI_REG,DT_FIM_REG,COD_ASSUNTO) values (1,1,0,0,0,0,'ORGANIZAÇÃO E FUNCIONAMENTO',null,1,to_date('13/03/09','DD/MM/RR'),null,null);
Insert into SIGA.EX_CLASSIFICACAO (ID_CLASSIFICACAO,COD_ASSUNTO_PRINCIPAL,COD_ASSUNTO_SECUNDARIO,COD_CLASSE,COD_SUBCLASSE,COD_ATIVIDADE,DESCR_CLASSIFICACAO,FACILITADOR_CLASS,ID_REG_INI,DT_INI_REG,DT_FIM_REG,COD_ASSUNTO) values (2,1,1,0,0,0,'REGULAMENTAÇÃO',null,2,to_date('13/03/09','DD/MM/RR'),null,null);
Insert into SIGA.EX_CLASSIFICACAO (ID_CLASSIFICACAO,COD_ASSUNTO_PRINCIPAL,COD_ASSUNTO_SECUNDARIO,COD_CLASSE,COD_SUBCLASSE,COD_ATIVIDADE,DESCR_CLASSIFICACAO,FACILITADOR_CLASS,ID_REG_INI,DT_INI_REG,DT_FIM_REG,COD_ASSUNTO) values (3,1,1,1,0,0,'Projetos, estudos, planejamento estratégico e similares',null,3,to_date('13/03/09','DD/MM/RR'),null,null);
Insert into SIGA.EX_CLASSIFICACAO (ID_CLASSIFICACAO,COD_ASSUNTO_PRINCIPAL,COD_ASSUNTO_SECUNDARIO,COD_CLASSE,COD_SUBCLASSE,COD_ATIVIDADE,DESCR_CLASSIFICACAO,FACILITADOR_CLASS,ID_REG_INI,DT_INI_REG,DT_FIM_REG,COD_ASSUNTO) values (4,1,1,1,1,0,'Modernização Administrativa',null,4,to_date('09/10/09','DD/MM/RR'),null,null);

--REM INSERTING into SIGA.EX_COMPETENCIA

--REM INSERTING into SIGA.EX_CONFIGURACAO
GRANT SELECT ON CORPORATIVO.CP_CONFIGURACAO_SEQ TO SIGA;
-- INÍCIO EX_CONFIGURACAO+CP_CONFIGURACAO --

	--nível mínimo de acesso a documentos
	Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,DT_INI_VIG_CONFIGURACAO,DT_FIM_VIG_CONFIGURACAO,HIS_DT_INI,ID_ORGAO_USU,ID_LOTACAO,ID_CARGO,ID_FUNCAO_CONFIANCA,ID_PESSOA,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO,ID_SERVICO,ID_GRUPO,NM_EMAIL,DESC_FORMULA,ID_TP_LOTACAO,ID_IDENTIDADE,HIS_IDC_INI,HIS_IDC_FIM) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,to_date('22/03/12','DD/MM/RR'),null,null,null,null,null,null,null,null,18,null,null,null,null,null,null,null,null);
	Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV,ID_TP_DOC,ID_TP_FORMA_DOC,ID_FORMA_DOC,ID_MOD,ID_CLASSIFICACAO,ID_VIA,ID_NIVEL_ACESSO,ID_PAPEL) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,null,null,null,null,null,null,null,5,null);

	--nível máximo de acesso a documentos
	Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,DT_INI_VIG_CONFIGURACAO,DT_FIM_VIG_CONFIGURACAO,HIS_DT_INI,ID_ORGAO_USU,ID_LOTACAO,ID_CARGO,ID_FUNCAO_CONFIANCA,ID_PESSOA,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO,ID_SERVICO,ID_GRUPO,NM_EMAIL,DESC_FORMULA,ID_TP_LOTACAO,ID_IDENTIDADE,HIS_IDC_INI,HIS_IDC_FIM) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,to_date('22/03/12','DD/MM/RR'),null,null,null,null,null,null,null,null,19,null,null,null,null,null,null,null,null);
	Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV,ID_TP_DOC,ID_TP_FORMA_DOC,ID_FORMA_DOC,ID_MOD,ID_CLASSIFICACAO,ID_VIA,ID_NIVEL_ACESSO,ID_PAPEL) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,null,null,null,null,null,null,null,6,null);

	-- todos os documentos tem por opçõa escolher entre digital e físico
	Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,DT_INI_VIG_CONFIGURACAO,DT_FIM_VIG_CONFIGURACAO,HIS_DT_INI,ID_ORGAO_USU,ID_LOTACAO,ID_CARGO,ID_FUNCAO_CONFIANCA,ID_PESSOA,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO,ID_SERVICO,ID_GRUPO,NM_EMAIL,DESC_FORMULA,ID_TP_LOTACAO,ID_IDENTIDADE,HIS_IDC_INI,HIS_IDC_FIM) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,to_date('22/03/12','DD/MM/RR'),null,null,null,null,null,null,null,4,4,null,null,null,null,null,null,null,null);
	
-- FIM EX_CONFIGURACAO+CP_CONFIGURACAO --

--REM INSERTING into SIGA.EX_DOCUMENTO
--REM INSERTING into SIGA.EX_EMAIL_NOTIFICACAO
--REM INSERTING into SIGA.EX_ESTADO_DOC
--REM INSERTING into SIGA.EX_ESTADO_TP_MOV

--REM INSERTING into SIGA.EX_TIPO_DOCUMENTO
Insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC,DESCR_TIPO_DOCUMENTO) values (3,'Externo');
Insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC,DESCR_TIPO_DOCUMENTO) values (1,'Interno Produzido');
Insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC,DESCR_TIPO_DOCUMENTO) values (2,'Interno Importado');

--REM INSERTING into SIGA.EX_FORMA_DOCUMENTO
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (1,'Ofício','OFI',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (2,'Memorando','MEM',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (3,'Formulário','FOR',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (4,'Informação','INF',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (5,'Externo','EXT',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (8,'Despacho','DES',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (9,'Contrato','CON',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (12,'Requerimento','REQ',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (13,'Solicitação','SOL',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (14,'Parecer','PAR',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (15,'Certidão','CER',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (54,'Boletim Interno','BIE',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (55,'Processo Administrativo','ADM',2);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (56,'Processo de Pessoal','RHU',2);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (57,'Processo de Execução Orçamentária e Financeira','EOF',2);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (97,'Memória de Reunião','MRU',1);
Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (98,'Relatório','REL',1);

--REM INSERTING into SIGA.EX_TP_FORMA_DOC
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (1,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (1,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (2,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (2,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (3,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (3,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (4,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (4,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (5,3);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (8,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (8,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (9,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (9,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (12,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (12,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (13,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (13,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (14,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (14,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (54,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (55,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (56,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (57,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (97,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (97,2);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (98,1);
Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (98,2);


--REM INSERTING into SIGA.EX_MOBIL

--REM INSERTING into SIGA.EX_MODELO
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (545,'Certidão de encerramendo de volume',null,null,'certidaoEncerramentoVolume.jsp',null,15,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (546,'Folha inicial de volume - EOF',null,null,'folhaInicialVolume.jsp',null,3,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (529,'Certidão de desentranhamento',null,null,'certidaoDesentranhamento.jsp',null,15,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (533,'Processo de Pessoal',null,null,'processoAdministrativo.jsp',null,56,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (534,'Processo de Execução Orçamentária e Financeira','Processo de Execução Orçamentária e Financeira','template-file/jsp','processoAdministrativo.jsp',null,57,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (2,'Ofício','Ofício','template/freemarker','oficio.jsp',null,1,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (28,'Documento Externo',null,null,'externo.jsp',null,5,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (29,'Interno Importado',null,null,'interno_antigo.jsp',null,null,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (26,'Memorando','Memorando','template/freemarker','memorando.jsp',null,2,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (78,'Despacho','Despacho','template/freemarker',null,null,8,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (519,'Parecer','Parecer','template/freemarker',null,null,14,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (27,'Informação','Informação','template/freemarker',null,null,4,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (241,'Boletim Interno',null,null,'boletimInterno.jsp',null,54,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (181,'Contrato','Contrato','template-file/jsp','contrato.jsp',null,9,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (663,'Memória de Reunião','Memória de Reunião','template/freemarker',null,null,97,null,null);
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (665,'Despacho Automático','Despacho gerado automaticamente pela transferência','template/freemarker',null,null,8,null,null);

DECLARE
  dest_blob_ex_mod BLOB;
  src_blob_ex_mod BLOB;
  
BEGIN

-- ofício
update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 2;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 2 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@oficio/]
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);

-- memorando
update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 26;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 26 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@entrevista]
	[@grupo titulo="Texto a ser inserido no corpo do memorando"]
		[@grupo]
			[@editor titulo="" var="texto_memorando" /]
		[/@grupo]
	[/@grupo]
	[@grupo]
	        [@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
	[/@grupo]
	[@grupo]
       		[@selecao titulo="Fecho" var="fecho" opcoes="Atenciosamente;Cordialmente;Respeitosamente" /]
	[/@grupo]
[/@entrevista]

[@documento]
        [@memorando texto=texto_memorando! fecho=(fecho!)+"," tamanhoLetra=tamanhoLetra! /]
[/@documento]
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);

-- informação
update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 27;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 27 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@entrevista]
    [@grupo titulo="Dados do Documento de Origem"]
        [@grupo]
            [@texto titulo="Tipo de documento" var="tipoDeDocumentoOrigem" largura=20 default=tipoDeDocumentoValue /]
            [@texto titulo="Número" var="numeroOrigem" largura=20 default=numeroValue /]
        [/@grupo]
        [@grupo]
            [@data titulo="Data" var="dataOrigem" default=dataValue /]
            [@texto titulo="Nome do Órgão" var="orgaoOrigem" largura=30 default=orgaoValue /]
        [/@grupo]
    [/@grupo]
    [@grupo]
        [@texto titulo="Vocativo" var="vocativo" largura="100" /]
    [/@grupo]
    [@grupo titulo="Texto da informação"]
        [@editor titulo="" var="texto_informacao" /]
    [/@grupo]
    [@grupo]
        [@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
    [/@grupo]
[/@entrevista]

[@documento margemDireita="3cm"]
    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]

    [@estiloBrasaoCentralizado tipo="INFORMAÇÃO" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
        <div style="font-family: Arial; font-size: ${tl};">
            [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != ""]
                Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>
            [/#if]
            Assunto: ${(doc.exClassificacao.descrClassificacao)!}

            <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">${vocativo!}</span></p>
                <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">${texto_informacao}</span></p>
        </div>
    [/@estiloBrasaoCentralizado]
[/@documento]

','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);

-- despacho
update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 78;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 78 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[#-- Se existir uma variável chamada ''texto'' copiar seu valor para ''texto_depacho'', pois a macro vai destruir o conteúdo da variável --]
[@entrevista]
  [@grupo titulo="Órgão de destino"]
    [#assign orgao_dest_atual = (doc.lotaDestinatario.nomeLotacao)!/]
    [#if orgao_dest_ult! != orgao_dest_atual]
      [#assign orgao_dest = orgao_dest_atual/]
      [#assign orgao_dest_ult = orgao_dest_atual/]
    [/#if]
    [@oculto var="orgao_dest_ult"/]
    [@grupo]
      [@selecao titulo="" var="combinacao" opcoes="A(o);À;Ao" /]
      [@texto titulo="Nome (opcional)" var="orgao_dest" largura=30 /]
    [/@grupo]
  [/@grupo]
  [@grupo titulo="Texto do despacho"]
    [@grupo]
      [#if !texto_padrao??]
        [#assign texto_padrao = "Para as providências cabíveis."/]
      [/#if]
      [@selecao titulo="Texto" opcoes="A pedido.;Arquive-se.;Autorizo.;Ciente. Arquive-se.;De acordo.;Expeça-se memorando.;Expeça-se memorando-circular.;Expeça-se ofício-circular.;Intime-se.;Junte-se ao dossiê.;Junte-se ao processo.;Oficie-se.;Para as providências cabíveis.;Para atendimento.;Para atendimento e encaminhamento direto.;Para ciência.;Para publicação.;Para verificar a possibilidade de atendimento.;[Outro]" var="texto_padrao" reler=true /]
    [/@grupo]
  [/@grupo]
  [@grupo depende="textopadrao" esconder=((texto_padrao!"") != "[Outro]")]
    [@editor titulo="" var="texto_despacho" /]
  [/@grupo]
  [@grupo]
    [@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
  [/@grupo]
  [@grupo titulo="Dados do Documento de Origem" esconder=doc.pai??]
    [#if postback?? && !doc.idDoc?? && doc.pai??]
      [#assign tipoDeDocumentoValue = doc.pai.descrFormaDoc /]
      [#assign numeroValue = doc.pai.sigla /]
      [#assign dataValue = doc.pai.dtDocDDMMYY /]
      [#assign orgaoValue = doc.pai.orgaoUsuario.acronimoOrgaoUsu /]
    [#else]
      [#assign tipoDeDocumentoValue = tipoDeDocumentoOrigem! /]
      [#assign numeroValue = numeroOrigem! /]
      [#assign dataValue = dataOrigem! /]
      [#assign orgaoValue = orgaoOrigem! /]
    [/#if]
    [@grupo]
      [@texto titulo="Tipo de documento" var="tipoDeDocumentoOrigem" largura=20 default=tipoDeDocumentoValue /]
      [@texto titulo="Número" var="numeroOrigem" largura=20 default=numeroValue /]
    [/@grupo]
    [@grupo]
      [@data titulo="Data" var="dataOrigem" default=dataValue /]
      [@texto titulo="Nome do Órgão" var="orgaoOrigem" largura=30 default=orgaoValue /]
    [/@grupo]
  [/@grupo]
[/@entrevista]
[@documento margemDireita="3cm"]
  [#if param.tamanhoLetra! == "Normal"]
    [#assign tl = "11pt" /]
  [#elseif param.tamanhoLetra! == "Pequeno"]
    [#assign tl = "9pt" /]
  [#elseif param.tamanhoLetra! == "Grande"]
    [#assign tl = "13pt" /]
  [#else]
    [#assign tl = "11pt"/]
  [/#if]
  [@estiloBrasaoCentralizado tipo="DESPACHO" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
    <div style="font-family: Arial; font-size: ${tl};">
      [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != ""]
        Referência: ${tipoDeDocumentoOrigem!} Nº ${numeroOrigem!}
        [#if dataOrigem?? && dataOrigem != ""]
          , ${dataOrigem!}
        [/#if]
        [#if orgaoOrigem?? && orgaoOrigem != ""]
          - ${orgaoOrigem!}.
        [/#if]
        <br />
      [/#if]
      Assunto: ${(doc.exClassificacao.descrClassificacao)!}
    </div>
    <div style="font-family: Arial; font-size: ${tl};">
      [#if orgao_dest?? && orgao_dest != ""]
        <p style="TEXT-INDENT: 2cm">
          <span style="font-size: ${tl}">${combinacao} ${orgao_dest!},</span>
        </p>
      [#elseif (doc.lotaDestinatario.nomeLotacao)?? && (doc.lotaDestinatario.nomeLotacao) != ""]
        <p style="TEXT-INDENT: 2cm">
          <span style="font-size: ${tl}">${combinacao} ${(doc.lotaDestinatario.nomeLotacao)!},</span>
        </p>
      [/#if]
      [#if (texto_padrao!"") != "[Outro]"]
        <p style="TEXT-INDENT: 2cm">
          <span style="font-size: ${tl!}">${texto_padrao!}</span>
        </p>
      [#else]
        <p style="TEXT-INDENT: 2cm">
          <span style="font-size: ${tl!}">${texto_despacho!}</span>
        </p>
      [/#if]
    </div>
  [/@estiloBrasaoCentralizado]
[/@documento]
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);


update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 519;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 519 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@entrevista]
    [@grupo titulo="Dados do Documento de Origem"]
        [@grupo]
            [@texto titulo="Tipo de documento" var="tipoDeDocumentoOrigem" largura=20 default=tipoDeDocumentoValue /]
            [@texto titulo="Número" var="numeroOrigem" largura=20 default=numeroValue /]
        [/@grupo]
        [@grupo]
            [@data titulo="Data" var="dataOrigem" default=dataValue /]
            [@texto titulo="Nome do Órgão" var="orgaoOrigem" largura=30 default=orgaoValue /]
        [/@grupo]
    [/@grupo]
    [@grupo]
        [@texto titulo="Vocativo" var="vocativo" largura="100" /]
    [/@grupo]
    [@grupo titulo="Texto do Parecer"]
        [@editor titulo="" var="texto_parecer" /]
    [/@grupo]
    [@grupo]
        [@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
    [/@grupo]
[/@entrevista]

[@documento margemDireita="3cm"]
    [#if param.tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif param.tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif param.tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]

    [@estiloBrasaoCentralizado tipo="PARECER" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
        [#if tipoDeDocumentoOrigem?? && tipoDeDocumentoOrigem != ""]
            Referência: ${tipoDeDocumentoOrigem!} N&ordm; ${numeroOrigem!}, ${dataOrigem!} - ${orgaoOrigem!}.<br/>
        [/#if]
        Assunto: ${(doc.exClassificacao.descrClassificacao)!}

        <div style="font-family: Arial; font-size: 10pt;">
            <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">${vocativo!}</span></p>
            <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">${texto_parecer}</span></p>
            <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">É o Parecer.</span></p>
        </div>
    [/@estiloBrasaoCentralizado]
[/@documento]

','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);

update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 663;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 663 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@entrevista]
 [@grupo titulo="Informações Gerais"]
  [@grupo]
   [@texto titulo="Objetivo da reunião" var="objReuniao" largura="84" maxcaracteres="84"/]
  [/@grupo]
  [@texto titulo="Horário" var="horReuniao" obrigatorio="Sim" largura="4" maxcaracteres="5"/]
  [@texto titulo="Local" var="locReuniao" obrigatorio="Sim" largura="60" maxcaracteres="60"/]
[#--
  [@grupo]
   [@memo titulo="Pendências (reuniões anteriores)" var="pendencias" colunas="78" linhas="2"/]
  [/@grupo]
--]
  [@separador /]
  [@selecao titulo="Participantes" var="numParticipantes" reler=true idAjax="numParticipantesAjax" opcoes="0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"/]
  [@grupo depende="numParticipantesAjax"]
   [#if numParticipantes! != ''0'']
    [#list 1..(numParticipantes)?number as i]
     [@grupo]
      [@pessoa titulo="" var="participantes"+i/]
     [/@grupo]
    [/#list]
   [/#if]
  [/@grupo]
  [@separador /]
  [@grupo]
   [@selecao titulo="Participantes (extra)" var="numParticipantesExtra" reler=true idAjax="numPartExtraAjax" opcoes="0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"/]
  [/@grupo]
  [@grupo depende="numPartExtraAjax"]
   [#if numParticipantesExtra! != ''0'']
    [#list 1..(numParticipantesExtra)?number as i]
     [@grupo]
      [@texto titulo="Nome" var="participantesExtra"+i largura="50" maxcaracteres="101"/]
      [@texto titulo="Email" var="participantesExtraEmail"+i largura="51" maxcaracteres="101"/]
     [/@grupo]
     [@grupo]
      [@texto titulo="Função" var="participantesExtraFuncao"+i largura="50" maxcaracteres="101"/]
      [@texto titulo="Unidade" var="participantesExtraUnidade"+i largura="51" maxcaracteres="101"/]
     [/@grupo]
    [/#list]
   [/#if]
  [/@grupo]
 [/@grupo]
 [@separador /]
 [@grupo]
  [@selecao titulo="Quantidade de itens da pauta" var="qtdItePauta" reler=true idAjax="qtdItePautaAjax" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"/]
 [/@grupo]
 [@grupo depende="qtdItePautaAjax"]
  [#list 1..(qtdItePauta)?number as i]
   [@grupo]
    [@texto titulo="<b>Item ${i}</b>" var="itePauta"+i largura="96" maxcaracteres="101"/]
   [/@grupo]
   [@memo titulo="Comentários" var="comentario"+i colunas="78" linhas="2"/]
   [@grupo]
    [@selecao titulo="Quantidade de ações" var="qtdAcoes"+i reler=true idAjax="qtdAcoesAjax"+i opcoes="0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15"/]
   [/@grupo]
   [@grupo depende="qtdAcoesAjax"+i]
    [#if (.vars[''qtdAcoes''+i])! != ''0'']
     [#list 1..(.vars[''qtdAcoes''+i])?number as j]
      [@grupo]      
       [@texto titulo="Ação ${j}" var="acoes"+i+j largura="95" maxcaracteres="75"/]
      [/@grupo]
      [@grupo]
       [@texto titulo="Responsável" var="responsavel"+i+j largura="61" maxcaracteres="55"/]
       [@data titulo="Data prevista" var="datPrevista"+i+j/] 
      [/@grupo]
     [/#list]
    [/#if]
   [/@grupo]
   [@separador /]  
  [/#list] 
 [/@grupo]
[/@entrevista]

[@documento]
 [#if tamanhoLetra! == "Normal"]
		[#assign tl = "11pt" /]
 [#elseif tamanhoLetra! == "Pequeno"]
		[#assign tl = "9pt" /]
 [#elseif tamanhoLetra! == "Grande"]
		[#assign tl = "13pt" /]
 [#else]		
		[#assign tl = "11pt"]
 [/#if]
 [@estiloBrasaoCentralizado tipo="MEMÓRIA DE REUNIÃO" tamanhoLetra=tl formatarOrgao=true]
   <p align="left">
    <b>Objetivo da reunião:</b>&nbsp;${objReuniao!}<br/>
    Horário e local: ${horReuniao!} - ${locReuniao!}<br/>
     Assunto: ${(doc.exClassificacao.descrClassificacao)!}</p><br/>
[#--
   [#if pendencias! != ""]
    <p><b>Pendências (reuniões anteriores):</b>&nbsp;${pendencias!}</p><br/>
   [/#if]
--]
   <table width="100%" border="1" cellpadding="5">
    <tr>
     <td width="50%"><b>Participantes</b></td>
     <td width="25%"><b>Função</b></td>
     <td width="25%"><b>Unidade</b></td>
    </tr>
    [#list 1..(numParticipantes)?number as i]
     [#if .vars[''participantes'' + i + ''_pessoaSel.id'']?? && .vars[''participantes'' + i + ''_pessoaSel.id''] != ""]
      [#assign participante = func.pessoa(.vars[''participantes'' + i + ''_pessoaSel.id'']?number) /]
      <tr>
       <td>${func.maiusculasEMinusculas(participante.descricao)}</td>
       <td>${(participante.funcaoConfianca.descricao)!}</td>
       <td>${participante.lotacao.sigla}</td>
      </tr>
     [/#if]
    [/#list]

    [#list 1..(numParticipantesExtra)?number as i] 
     [#if numParticipantesExtra! != ''0'']
      <tr>
       <td>${.vars[''participantesExtra''+i]!}
        [#if .vars[''participantesExtraEmail''+i]??] (${.vars[''participantesExtraEmail''+i]!})[/#if]</td>
       <td>${.vars[''participantesExtraFuncao''+i]!}</td>
       <td>${.vars[''participantesExtraUnidade''+i]!}</td>
      </tr>
     [/#if]    
    [/#list]
   </table> 
   <br/>


  
   <p><b>Pauta</b></p>
     [#list 1..(qtdItePauta)?number as i] 
        <p>
          <b>${i}. ${.vars[''itePauta''+i]!}:</b> ${.vars[''comentario''+i]!}
        </p>       
     [/#list]  


  <br/> 

  [#assign fAcoes = false/]
  [#list 1..(qtdItePauta)?number as i]
   [#if .vars[''qtdAcoes''+i] != ''0'']
    [#assign fAcoes = true/]
   [/#if] 
  [/#list]

  [#if fAcoes]
   <table width="100%" border="1" cellpadding="5">
     <tr>
      <td width="10%"><b>Ref.</b></td>
      <td width="50%"><b>Próximas Ações</b></td>
      <td width="20%"><b>Responsável</b></td>
      <td width="20%" align="center"><b>Data Prevista</b></td>
     </tr>
     [#list 1..(qtdItePauta)?number as i]
      [#if .vars[''qtdAcoes''+i] != ''0'']
       [#list 1..(.vars[''qtdAcoes''+i])?number as j]
        <tr>
         <td>${i}.${j}</td>
         <td>${.vars[''acoes''+i+j]!}</td>
         <td>${.vars[''responsavel''+i+j]!}</td>
         <td align="center">${.vars[''datPrevista''+i+j]!}</td>
        </tr>
       [/#list]
      [/#if]
     [/#list]
   </table>   
  [/#if]
 [/@estiloBrasaoCentralizado]
[/@documento]

','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);

update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 665;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 665 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@documento margemDireita="3cm"]
    [#assign tl="11pt"/]
    [@estiloBrasaoCentralizado tipo="DESPACHO" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
        <div style="font-family: Arial; font-size: ${tl};">
          Referência: ${doc.codigo} de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}[#if doc.lotaTitular??] - ${(doc.lotaTitular.descricao)!}[/#if].<br/>
            Assunto: ${(doc.exClassificacao.descrClassificacao)!}
        </div>

        <div style="font-family: Arial; font-size: ${tl};">
            [#if mov.lotaResp?? && (mov.lotaResp.idLotacaoIni != mov.lotaCadastrante.idLotacaoIni)]
                <p style="TEXT-INDENT: 0cm">
                  <span style="font-size: ${tl}">À ${(mov.lotaResp.descricao)!},</span>
                </p>
            [/#if]
            [#if despachoTexto??]
                <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">${despachoTexto}</span></p>
            [/#if]
            ${despachoHtml!}
        </div>
    [/@estiloBrasaoCentralizado]
[/@documento]

','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);


END;
/


--REM INSERTING into SIGA.EX_MOVIMENTACAO
--REM INSERTING into SIGA.EX_NIVEL_ACESSO
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (1,'Limitado ao órgão (padrão)','Dá acesso a todos do órgão ao qual o documento pertence, bem como a toda a lotação de qualquer órgão para onde for enviado',20);
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (2,'Limitado de divisão para pessoa','Dá acesso a toda a divisão onde o documento foi criado e também a qualquer lotação (ou apenas pessoa específica) para onde for enviado',40);
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (3,'Limitado entre lotações','Dá acesso a toda a lotação do cadastrante do documento e à lotação (nunca somente à pessoa) para onde for enviado',60);
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (5,'Limitado entre pessoas','Dá acesso somente ao cadastrante do documento e à lotação (ou apenas pessoa específica) para onde for enviado',100);
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (6,'Público','Dá acesso a todos, independentemente do órgão',10);
Insert into SIGA.EX_NIVEL_ACESSO (ID_NIVEL_ACESSO,NM_NIVEL_ACESSO,DSC_NIVEL_ACESSO,GRAU_NIVEL_ACESSO) values (7,'Limitado de pessoa para divisão','Dá acesso somente ao cadastrante do documento e a toda a divisão para onde for enviado',30);

--REM INSERTING into SIGA.EX_NUMERACAO

--REM INSERTING into SIGA.EX_PAPEL
Insert into SIGA.EX_PAPEL (ID_PAPEL,DESC_PAPEL) values (1,'Gestor');
Insert into SIGA.EX_PAPEL (ID_PAPEL,DESC_PAPEL) values (2,'Interessado');

--REM INSERTING into SIGA.EX_PREENCHIMENTO

--REM INSERTING into SIGA.EX_SITUACAO_CONFIGURACAO
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (1,'Pode');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (2,'Não Pode');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (3,'Obrigatório');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (4,'Opcional');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (5,'Default');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (6,'Não default');
Insert into SIGA.EX_SITUACAO_CONFIGURACAO (ID_SIT_CONFIGURACAO,DSC_SIT_CONFIGURACAO) values (7,'Proibido');

--REM INSERTING into SIGA.EX_TEMPORALIDADE
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (81,'1 ano',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (82,'1 ano após a atualização',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (83,'10 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (84,'15 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (85,'2 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (86,'2 anos após a devolução do documento / processo',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (87,'2 anos após o encerramento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (88,'2 anos após o encerramento com devolução',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (89,'20 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (90,'3 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (92,'3 anos após o encerramento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (93,'30 dias',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (94,'35 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (95,'4 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (96,'5 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (97,'5 anos após o encerramento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (98,'50 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (99,'51 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (100,'6 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (101,'7 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (102,'71 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (103,'90 dias',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (104,'95 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (105,'Até a alienação',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (106,'Até a aposentadoria ou o desligamento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (107,'Até a atualização',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (108,'Até a conclusão da apuração',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (109,'Até a devolução',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (110,'Até a inclusão',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (111,'Até a informatização ou alienação',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (112,'Até a prestação de contas',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (113,'Até a quitação da dívida',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (114,'Até a restauração da obra',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (115,'Até alienação do bem',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (116,'Até o emplacamento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (117,'Até o encerramento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (118,'Até o pagamento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (119,'Até o trânsito em julgado',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (120,'Até vigência',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (121,'Até vigência + 6 anos',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (122,'Duração obra',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (123,'Durante vigência',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (124,'Enquanto permanece a ocupação',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (125,'Enquanto vigora',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (126,'Guarda Permanente',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (127,'Indeterminado',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (128,'Julg. TCU',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (129,'No momento do recolhimento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (130,'Para utilização',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (131,'Prazo da pasta',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (132,'Prazo do assent.',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (133,'Prazo do dossiê',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (134,'Prazo do precatório',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (135,'Prazo do processo',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (136,'Prazo do prontuário',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (137,'Transitado em julgado',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (138,'Validade Concurso',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (139,'Validade contrato',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (140,'Validade credenciamento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (141,'Validade Projeto',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (142,'Vigência',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (143,'Vigência cadastro',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (144,'Vigência da pensão',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (161,'Até a devolução do bem',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (162,'Até a posse',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (163,'Até a próxima atualização',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (164,'Até a publicação ',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (165,'Até o desfazimento do bem',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (166,'Até o encerramento do processo de execução penal',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (167,'Até o vitaliciamento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (168,'Até revogação',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (169,'Até vigência do contrato ou Julg. TCU',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (170,'Eliminação no momento do recebimento',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (171,'Enquanto durar a ocupação',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (172,'Enquanto durar a pesquisa',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (173,'Enquanto o bem estiver alienado',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (174,'Enquanto vigente',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (175,'Imediatamente após a produção',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (176,'Prazo da licença',null);
Insert into SIGA.EX_TEMPORALIDADE (ID_TEMPORALIDADE,DESC_TEMPORALIDADE,PERMANENCIA_ARQUIVO) values (177,'Validade do concurso',null);

--REM INSERTING into SIGA.EX_TIPO_DESPACHO
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (17,'A pedido','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (1,'De acordo.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (2,'Para ciência.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (3,'Para as providências cabíveis','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (4,'Intime-se.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (5,'Autorizo.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (6,'Para atendimento.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (7,'Para verificar a possibilidade de atendimento.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (8,'Oficie-se.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (9,'Expeça-se ofício-circular.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (10,'Expeça-se memorando.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (11,'Expeça-se memorando-circular.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (12,'Arquive-se.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (13,'Junte-se ao dossiê.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (14,'Junte-se ao processo.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (15,'Ciente. Arquive-se.','S');
Insert into SIGA.EX_TIPO_DESPACHO (ID_TP_DESPACHO,DESC_TP_DESPACHO,FG_ATIVO_TP_DESPACHO) values (16,'Para atendimento e encaminhamento direto','S');

--REM INSERTING into SIGA.EX_TIPO_DESTINACAO
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (21,'5 anos',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (22,'Agência',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (23,'Arquivo',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (24,'Arquivo Intermediário',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (26,'Arquivo Permanente',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (27,'Assentamento Funcional',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (28,'Banco',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (29,'Biblioteca',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (30,'Candidato',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (31,'Clínica',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (32,'Correios',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (33,'Devolvida',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (34,'Doador',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (35,'Dossiê',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (36,'Dossiê do curso',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (37,'Dossiê do Evento',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (38,'Empresa',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (39,'Estagiário',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (40,'Executor',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (41,'Imprensa',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (42,'Interessado',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (43,'Julg. TCU',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (44,'Magistrado',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (45,'Participante',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (46,'Participante / Instrutor',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (47,'Pasta do Evento',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (48,'Pasta do projeto',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (49,'Precatório',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (50,'Processo',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (51,'Processo de origem',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (52,'Prontuário Médico',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (53,'Receita Federal',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (54,'Remetido',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (55,'Servidor',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (57,'Setor',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (58,'Setor Competente',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (59,'Setores ',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (60,'Setores / Interessados',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (61,'TCU',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (62,'Unidade Geradora',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (81,'Magistrado / Servidor',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (82,'Processo Judicial',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (1,'Eliminação',null);
Insert into SIGA.EX_TIPO_DESTINACAO (ID_TP_DESTINACAO,DESCR_TIPO_DESTINACAO,FACILITADOR_DEST) values (2,'Guarda Permanente',null);

--REM INSERTING into SIGA.EX_TIPO_FORMA_DOCUMENTO
Insert into SIGA.EX_TIPO_FORMA_DOCUMENTO (ID_TIPO_FORMA_DOC,DESC_TIPO_FORMA_DOC,NUMERACAO_UNICA) values (1,'Expediente',0);
Insert into SIGA.EX_TIPO_FORMA_DOCUMENTO (ID_TIPO_FORMA_DOC,DESC_TIPO_FORMA_DOC,NUMERACAO_UNICA) values (2,'Processo Administrativo',1);

--REM INSERTING into SIGA.EX_TIPO_MOBIL
Insert into SIGA.EX_TIPO_MOBIL (ID_TIPO_MOBIL,DESC_TIPO_MOBIL) values (1,'Geral');
Insert into SIGA.EX_TIPO_MOBIL (ID_TIPO_MOBIL,DESC_TIPO_MOBIL) values (2,'Via');
Insert into SIGA.EX_TIPO_MOBIL (ID_TIPO_MOBIL,DESC_TIPO_MOBIL) values (3,'Cópia');
Insert into SIGA.EX_TIPO_MOBIL (ID_TIPO_MOBIL,DESC_TIPO_MOBIL) values (4,'Volume');

--REM INSERTING into SIGA.EX_TIPO_MOVIMENTACAO
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (27,'Atualização');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (28,'Anotação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (38,'Pedido de Publicação no DJE');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (39,'Revolvimento Unidirecional');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (37,'Publicação do Boletim');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (19,'Arquivamento Permanente');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (20,'Arquivamento Intermediário');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (21,'Desarquivamento');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (22,'Assinatura de Movimentação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (23,'Recebimento Transitório');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (24,'Inclusão de Co-signatário');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (25,'Registro de Assinatura');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (26,'Registro de Assinatura de Movimentação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (33,'Remessa para Publicação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (30,'Registro de Acesso Alheio');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (32,'Agendamento de Publicação no DJE');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (34,'Confirmação de Remessa Manual');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (35,'Disponibilização');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (40,'Notificação de Publicação no Boletim');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (18,'Despacho com Transferência Externa');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (16,'Vinculação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (17,'Transferência Externa');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (1,'Criação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (2,'Anexação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (3,'Transferência');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (4,'Recebimento');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (5,'Despacho');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (6,'Despacho com Transferência');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (8,'Despacho Interno com Transferência');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (7,'Despacho Interno');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (9,'Arquivamento Corrente');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (10,'Descarte');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (11,'Assinatura');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (12,'Juntada');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (13,'Desentranhamento');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (14,'Cancelamento de Movimentação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (15,'Extravio');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (29,'Redefinição de Sigilo');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (31,'Juntada a Documento Externo');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (41,'Apensação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (42,'Desapensação');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (43,'Encerramento');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (44,'Definição de Perfil');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (45,'Conferência de Cópia de Documento');
Insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (36,'Solicitação de Publicação no Boletim');


--REM INSERTING into SIGA.EX_TP_DOC_PUBLICACAO
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (1,'Ato Ordinatório','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (2,'Decisão','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (3,'Despacho','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (4,'Sentença','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (5,'Edital (Teor Judicial)','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (6,'Informação de Secretaria',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (7,'Ordem de Serviço',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (8,'Portaria',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (9,'Acórdão','J');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (85,'Extratos de Contratos','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (86,'Extrato de dispensa de licitação','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (90,'Extrato de inexigibilidade de licitação','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (108,'Extrato de termo aditivo','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (107,'Extrato de rescisão contratual','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (40,'Aviso de Licitação','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (147,'Aviso de Pregão','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (104,'Extrato de Registro de Preços','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (106,'Extrato de Rescisão','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (26,'Aviso de Aditamento','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (18,'Outros','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (25,'Aviso','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (73,'Errata',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (83,'Extrato de convênio','A');
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (158,'Ato da Presidência',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (159,'Edital da Presidência',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (160,'Portaria da Presidência',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (161,'Ordem de Serviço da presidência',null);
Insert into SIGA.EX_TP_DOC_PUBLICACAO (ID_DOC_PUBLICACAO,NM_DOC_PUBLICACAO,CARATER) values (162,'Resolução da Presidência',null);

--REM INSERTING into SIGA.EX_TP_MOV_ESTADO
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (1,1);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (4,2);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (5,2);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (7,2);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (13,2);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (3,3);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (6,3);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (8,3);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (15,4);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (9,6);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (10,8);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (12,9);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (17,11);
Insert into SIGA.EX_TP_MOV_ESTADO (ID_TP_MOV,ID_ESTADO_DOC) values (18,11);

--REM INSERTING into SIGA.EX_VIA
Insert into SIGA.EX_VIA (ID_VIA,ID_CLASSIFICACAO,ID_TP_DESTINACAO,COD_VIA,ID_TMP_CORRENTE,ID_TMP_INTERMEDIARIO,OBS,FG_MAIOR,ID_DESTINACAO_FINAL,ID_REG_INI,DT_INI_REG,DT_FIM_REG) values (1,4,58,'1',85,null,'-','N',1,1,to_date('13/03/09','DD/MM/RR'),null);

--------------------------------------------------------
--  DDL for Index VIA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."VIA_PK" ON "SIGA"."EX_VIA" ("ID_VIA") 
  ;
--------------------------------------------------------
--  DDL for Index EX_CLASSIFICACAO_IDX_020
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_CLASSIFICACAO_IDX_020" ON "SIGA"."EX_CLASSIFICACAO" ("COD_CLASSE", "COD_SUBCLASSE", "COD_ATIVIDADE", "COD_ASSUNTO_PRINCIPAL", "COD_ASSUNTO_SECUNDARIO") 
  ;
--------------------------------------------------------
--  DDL for Index ID_MOBIL_REF_IDX
--------------------------------------------------------

  CREATE INDEX "SIGA"."ID_MOBIL_REF_IDX" ON "SIGA"."EX_MOVIMENTACAO" ("ID_MOB_REF") 
  ;
--------------------------------------------------------
--  DDL for Index BOL_DOC_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."BOL_DOC_UK" ON "SIGA"."EX_BOLETIM_DOC" ("ID_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index EX_DOCUMENTO_IDX_011
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_DOCUMENTO_IDX_011" ON "SIGA"."EX_DOCUMENTO" ("ID_ORGAO_USU", "ANO_EMISSAO") 
  ;
--------------------------------------------------------
--  DDL for Index EX_EMAIL_NOTIFICACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."EX_EMAIL_NOTIFICACAO_PK" ON "SIGA"."EX_EMAIL_NOTIFICACAO" ("ID_EMAIL_NOTIFICACAO") 
  ;
--------------------------------------------------------
--  DDL for Index EX_PREENCHIMENTO_IDX_013
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_PREENCHIMENTO_IDX_013" ON "SIGA"."EX_PREENCHIMENTO" ("ID_MOD") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MOVIMENTACAO_IDX_001
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_MOVIMENTACAO_IDX_001" ON "SIGA"."EX_MOVIMENTACAO" ("ID_MOV_REF", "DT_INI_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index ID_MOBIL_IDX
--------------------------------------------------------

  CREATE INDEX "SIGA"."ID_MOBIL_IDX" ON "SIGA"."EX_MOVIMENTACAO" ("ID_MOBIL") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MOBIL_UNIQUE
--------------------------------------------------------
  
  CREATE UNIQUE INDEX SIGA.IDX_MOBIL_UNIQUE ON SIGA.EX_MOBIL (ID_DOC ASC, ID_TIPO_MOBIL ASC, NUM_SEQUENCIA ASC);
--------------------------------------------------------
--  DDL for Index DOC_FORMA_NUM_ANO_IX
--------------------------------------------------------

  CREATE INDEX "SIGA"."DOC_FORMA_NUM_ANO_IX" ON "SIGA"."EX_DOCUMENTO" ("ID_FORMA_DOC", "NUM_EXPEDIENTE", "ANO_EMISSAO") 
  ;
  
  --------------------------------------------------------
--  DDL for Index UNIQUE_DOC_NUM_IDX
--------------------------------------------------------
  CREATE UNIQUE INDEX SIGA.UNIQUE_DOC_NUM_IDX ON SIGA.EX_DOCUMENTO (CASE 
  WHEN ID_ORGAO_USU IS NULL OR ID_FORMA_DOC IS NULL OR ANO_EMISSAO IS NULL OR NUM_EXPEDIENTE IS NULL THEN NULL 
  ELSE ID_ORGAO_USU 
END ASC, CASE 
  WHEN ID_ORGAO_USU IS NULL OR ID_FORMA_DOC IS NULL OR ANO_EMISSAO IS NULL OR NUM_EXPEDIENTE IS NULL THEN NULL 
  ELSE ID_FORMA_DOC 
END ASC, CASE 
  WHEN ID_ORGAO_USU IS NULL OR ID_FORMA_DOC IS NULL OR ANO_EMISSAO IS NULL OR NUM_EXPEDIENTE IS NULL THEN NULL 
  ELSE ANO_EMISSAO 
END ASC, CASE 
  WHEN ID_ORGAO_USU IS NULL OR ID_FORMA_DOC IS NULL OR ANO_EMISSAO IS NULL OR NUM_EXPEDIENTE IS NULL THEN NULL 
  ELSE NUM_EXPEDIENTE 
END ASC);
--------------------------------------------------------
--  DDL for Index EX_CLASSIFICACAO_IDX_019
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_CLASSIFICACAO_IDX_019" ON "SIGA"."EX_CLASSIFICACAO" ("COD_ASSUNTO_SECUNDARIO", "COD_CLASSE", "COD_SUBCLASSE", "COD_ATIVIDADE", "COD_ASSUNTO_PRINCIPAL") 
  ;
--------------------------------------------------------
--  DDL for Index EX_PREENCHIMENTO_IDX_012
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_PREENCHIMENTO_IDX_012" ON "SIGA"."EX_PREENCHIMENTO" ("ID_LOTACAO") 
  ;
--------------------------------------------------------
--  DDL for Index NIVEL_ACESSO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."NIVEL_ACESSO_PK" ON "SIGA"."EX_NIVEL_ACESSO" ("ID_NIVEL_ACESSO") 
  ;
--------------------------------------------------------
--  DDL for Index CLASSIFICACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."CLASSIFICACAO_PK" ON "SIGA"."EX_CLASSIFICACAO" ("ID_CLASSIFICACAO") 
  ;
--------------------------------------------------------
--  DDL for Index MOVIMENTACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."MOVIMENTACAO_PK" ON "SIGA"."EX_MOVIMENTACAO" ("ID_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index BOLETIM_DOC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."BOLETIM_DOC_PK" ON "SIGA"."EX_BOLETIM_DOC" ("ID_BOLETIM_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index TP_MOV_ESTADO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TP_MOV_ESTADO_PK" ON "SIGA"."EX_TP_MOV_ESTADO" ("ID_ESTADO_DOC", "ID_TP_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MODELO_IDX_014
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_MODELO_IDX_014" ON "SIGA"."EX_MODELO" ("ID_CLASSIFICACAO") 
  ;
--------------------------------------------------------
--  DDL for Index NUMERACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."NUMERACAO_PK" ON "SIGA"."EX_NUMERACAO" ("ID_ORGAO_USU", "ID_FORMA_DOC", "ANO_EMISSAO") 
  ;
--------------------------------------------------------
--  DDL for Index DOCUMENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."DOCUMENTO_PK" ON "SIGA"."EX_DOCUMENTO" ("ID_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index ESTADO_TP_MOV_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."ESTADO_TP_MOV_PK" ON "SIGA"."EX_ESTADO_TP_MOV" ("ID_ESTADO_DOC", "ID_TP_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index MODELO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."MODELO_PK" ON "SIGA"."EX_MODELO" ("ID_MOD") 
  ;
--------------------------------------------------------
--  DDL for Index PREENCHIMENTO_FK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."PREENCHIMENTO_FK" ON "SIGA"."EX_PREENCHIMENTO" ("ID_PREENCHIMENTO") 
  ;
--------------------------------------------------------
--  DDL for Index ID_MOV_REF_IDX
--------------------------------------------------------

  CREATE INDEX "SIGA"."ID_MOV_REF_IDX" ON "SIGA"."EX_MOVIMENTACAO" ("ID_MOV_REF") 
  ;
--------------------------------------------------------
--  DDL for Index ESTADO_DOC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."ESTADO_DOC_PK" ON "SIGA"."EX_ESTADO_DOC" ("ID_ESTADO_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index TIPO_DUCUMENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TIPO_DUCUMENTO_PK" ON "SIGA"."EX_TIPO_DOCUMENTO" ("ID_TP_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index FORMA_DOCUMENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."FORMA_DOCUMENTO_PK" ON "SIGA"."EX_FORMA_DOCUMENTO" ("ID_FORMA_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index TP_DOC_PUBLICACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TP_DOC_PUBLICACAO_PK" ON "SIGA"."EX_TP_DOC_PUBLICACAO" ("ID_DOC_PUBLICACAO") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MODELO_IDX_017
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_MODELO_IDX_017" ON "SIGA"."EX_MODELO" ("ID_NIVEL_ACESSO") 
  ;
--------------------------------------------------------
--  DDL for Index TIPO_DESTINACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TIPO_DESTINACAO_PK" ON "SIGA"."EX_TIPO_DESTINACAO" ("ID_TP_DESTINACAO") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MODELO_IDX_016
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_MODELO_IDX_016" ON "SIGA"."EX_MODELO" ("ID_FORMA_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index EX_SITUACAO_CONFIGURACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."EX_SITUACAO_CONFIGURACAO_PK" ON "SIGA"."EX_SITUACAO_CONFIGURACAO" ("ID_SIT_CONFIGURACAO") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MODELO_TP_DOC_PUBLICACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."EX_MODELO_TP_DOC_PUBLICACAO_PK" ON "SIGA"."EX_MODELO_TP_DOC_PUBLICACAO" ("ID_MOD", "ID_DOC_PUBLICACAO") 
  ;
--------------------------------------------------------
--  DDL for Index MOVIMENTACAO_LOTA_RESP_E_DATA
--------------------------------------------------------

  CREATE INDEX "SIGA"."MOVIMENTACAO_LOTA_RESP_E_DATA" ON "SIGA"."EX_MOVIMENTACAO" ("ID_LOTA_RESP", "DT_INI_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index EX_MODELO_IDX_015
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_MODELO_IDX_015" ON "SIGA"."EX_MODELO" ("ID_CLASS_CRIACAO_VIA") 
  ;
--------------------------------------------------------
--  DDL for Index TEMPORALIDADE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TEMPORALIDADE_PK" ON "SIGA"."EX_TEMPORALIDADE" ("ID_TEMPORALIDADE") 
  ;
--------------------------------------------------------
--  DDL for Index SIGA_EXDOC_MOB_PAI_ID_DOC_IX
--------------------------------------------------------

  CREATE INDEX "SIGA"."SIGA_EXDOC_MOB_PAI_ID_DOC_IX" ON "SIGA"."EX_DOCUMENTO" ("ID_MOB_PAI", "ID_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index ID_DOC_IDX
--------------------------------------------------------

  CREATE INDEX "SIGA"."ID_DOC_IDX" ON "SIGA"."EX_MOBIL" ("ID_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index EX_FORMA_DOCUMENTO_IDX_021
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_FORMA_DOCUMENTO_IDX_021" ON "SIGA"."EX_FORMA_DOCUMENTO" ("SIGLA_FORMA_DOC", "ID_FORMA_DOC", "DESCR_FORMA_DOC") 
  ;
--------------------------------------------------------
--  DDL for Index EX_CLASSIFICACAO_IDX_018
--------------------------------------------------------

  CREATE INDEX "SIGA"."EX_CLASSIFICACAO_IDX_018" ON "SIGA"."EX_CLASSIFICACAO" ("COD_ASSUNTO_PRINCIPAL") 
  ;
--------------------------------------------------------
--  DDL for Index TIPO_MOVIMENTACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TIPO_MOVIMENTACAO_PK" ON "SIGA"."EX_TIPO_MOVIMENTACAO" ("ID_TP_MOV") 
  ;
--------------------------------------------------------
--  DDL for Index TIPO_DESPACHO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."TIPO_DESPACHO_PK" ON "SIGA"."EX_TIPO_DESPACHO" ("ID_TP_DESPACHO") 
  ;
--------------------------------------------------------
--  DDL for Trigger EX_CLASSIFICACAO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_CLASSIFICACAO_INSERT_TRG" BEFORE INSERT 
ON siga.EX_CLASSIFICACAO 
FOR EACH ROW 
begin
if :new.ID_CLASSIFICACAO is null then
select EX_CLASSIFICACAO_SEQ.nextval into :new.ID_CLASSIFICACAO from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_CLASSIFICACAO_INSERT_TRG" ENABLE;

--------------------------------------------------------
--  DDL for Trigger EX_COMPETENCIA_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_COMPETENCIA_INSERT_TRG" BEFORE INSERT
ON SIGA.EX_Competencia
FOR EACH ROW
begin
if :new.ID_Competencia is null then
select EX_Competencia_SEQ.nextval into :new.ID_Competencia from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_COMPETENCIA_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_DOCUMENTO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" BEFORE INSERT ON "EX_DOCUMENTO" FOR EACH ROW 
begin
if :new.ID_DOC is null then
select EX_DOCUMENTO_SEQ.nextval into :new.ID_DOC from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_ESTADO_DOC_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_ESTADO_DOC_INSERT_TRG" before insert
on EX_ESTADO_DOC
for each row
begin
if :new.ID_ESTADO_DOC is null then
select EX_ESTADO_DOC_SEQ.nextval into :new.ID_ESTADO_DOC from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_ESTADO_DOC_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_FORMA_DOCUMENTO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_FORMA_DOCUMENTO_INSERT_TRG" before insert
on EX_FORMA_DOCUMENTO
for each row
begin
if :new.id_forMa_doc is null then
select EX_FORMA_DOCUMENTO_SEQ.nextval into :new.id_forMa_doc from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_FORMA_DOCUMENTO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_MODELO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_MODELO_INSERT_TRG" before insert
on EX_modelo
for each row
begin
if :new.ID_MOD is null then
select EX_modelo_SEQ.nextval into :new.ID_MOD from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_MODELO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_MOVIMENTACAO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_MOVIMENTACAO_INSERT_TRG" before insert
on EX_MOVIMENTACAO
for each row
begin
if :new.ID_MOV is null then
select EX_MOVIMENTACAO_SEQ.nextval into :new.ID_MOV from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_MOVIMENTACAO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_PREENCHIMENTO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_PREENCHIMENTO_INSERT_TRG" BEFORE INSERT
ON SIGA.EX_PREENCHIMENTO
FOR EACH ROW
begin
if :new.ID_PREENCHIMENTO is null then
select EX_PREENCHIMENTO_SEQ.nextval into :new.ID_PREENCHIMENTO from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_PREENCHIMENTO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_TEMPORALIDADE_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_TEMPORALIDADE_INSERT_TRG" before insert
on EX_TEMPORALIDADE
for each row
begin
if :new.ID_TEMPORALIDADE is null then
select EX_TEMPORALIDADE_SEQ.nextval into :new.ID_TEMPORALIDADE from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_TEMPORALIDADE_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_TIPO_DESPACHO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_TIPO_DESPACHO_INSERT_TRG" before insert
on EX_TIPO_DESPACHO
for each row
begin
if :new.ID_TP_DESPACHO is null then
select EX_TIPO_DESPACHO_SEQ.nextval into :new.ID_TP_DESPACHO from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_TIPO_DESPACHO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_TIPO_DESTINACAO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_TIPO_DESTINACAO_INSERT_TRG" before insert
on EX_TIPO_DESTINACAO
for each row
begin
if :new.ID_TP_DESTINACAO is null then
select EX_TIPO_DESTINACAO_SEQ.nextval into :new.ID_TP_DESTINACAO from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_TIPO_DESTINACAO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_TIPO_DOCUMENTO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_TIPO_DOCUMENTO_INSERT_TRG" before insert
on EX_TIPO_DOCUMENTO
for each row
begin
if :new.ID_TP_DOC is null then
select EX_TIPO_DOCUMENTO_SEQ.nextval into :new.ID_TP_DOC from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_TIPO_DOCUMENTO_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_TP_MOV_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_TP_MOV_INSERT_TRG" before insert
on EX_TIPO_MOVIMENTACAO
for each row
begin
if :new.ID_TP_MOV is null then
select EX_TIPO_MOVIMENTACAO_SEQ.nextval into :new.ID_TP_MOV from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_TP_MOV_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger EX_VIA_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_VIA_INSERT_TRG" before insert
on EX_VIA
for each row
begin
if :new.ID_VIA is null then
select EX_VIA_SEQ.nextval into :new.ID_VIA from dual;
end if;
end;
/
ALTER TRIGGER "SIGA"."EX_VIA_INSERT_TRG" ENABLE;

--------------------------------------------------------
--  DDL for Function NUM_EXPEDIENTE_FUN
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGA"."NUM_EXPEDIENTE_FUN" 
(pID_ORGAO_USU IN NUMBER,
 pID_FORMA_DOC IN NUMBER,
 pANO_EMISSAO IN NUMBER)

 RETURN NUMBER

IS
 NR_EXPEDIENTE NUMBER := 0 ;
 --PID_NUMERACAO NUMBER := PID_FORMA_DOC ;

BEGIN

     SELECT COUNT(NUM_EXPEDIENTE) into NR_EXPEDIENTE
     FROM   EX_NUMERACAO
     WHERE  ID_ORGAO_USU = pID_ORGAO_USU
     AND    ID_FORMA_DOC = pID_FORMA_DOC
     AND    ANO_EMISSAO = pANO_EMISSAO;

     IF NR_EXPEDIENTE = 0 then
        NR_EXPEDIENTE := 1;
        INSERT INTO EX_NUMERACAO(ID_ORGAO_USU,ID_FORMA_DOC,ANO_EMISSAO,NUM_EXPEDIENTE)
        VALUES(pID_ORGAO_USU,pID_FORMA_DOC,pANO_EMISSAO,NR_EXPEDIENTE);

     ELSE

        SELECT NUM_EXPEDIENTE into NR_EXPEDIENTE
        FROM   EX_NUMERACAO
        WHERE  ID_ORGAO_USU = pID_ORGAO_USU
        AND    ID_FORMA_DOC = pID_FORMA_DOC
        AND    ANO_EMISSAO = pANO_EMISSAO;

        NR_EXPEDIENTE := NR_EXPEDIENTE + 1;

        Update EX_NUMERACAO
        SET NUM_EXPEDIENTE = NR_EXPEDIENTE
        WHERE ID_ORGAO_USU = pID_ORGAO_USU
        AND   ID_FORMA_DOC = pID_FORMA_DOC
        AND   ANO_EMISSAO  = pANO_EMISSAO;
     END IF;
     COMMIT ;
     RETURN NR_EXPEDIENTE;

END;

 
/

--------------------------------------------------------
--  DDL for Function REMOVE_ACENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGA"."REMOVE_ACENTO" 
    (acentuado in
    VARCHAR2)

--	Enter the parameters for the function in
--	the brackets above.  If this function has
--	no parameters then delete the line

--  ***************************************************
--	*                                                 *
--	*   Author                                        *
--	*   Creation Date                                 *
--	*   Comments                                      *
--	*                                                 *
--  ***************************************************

RETURN  VARCHAR2

IS

--	Enter all variables cursors and constants following
--	this line
sem_acento VARCHAR2(4000);

begin

--	Enter the code for the function following
--	this line

      sem_acento := CONVERT(TRANSLATE(UPPER( acentuado ),'ÃÕÑ','AON'),'US7ASCII');

	return	sem_acento;
			
exception

--	Enter the code to handle exception conditions
--	following this line


	when others then
		null;
			
end;
 
/




  GRANT REFERENCES ON CORPORATIVO.DP_CARGO TO siga;
  GRANT REFERENCES ON CORPORATIVO.DP_FUNCAO_CONFIANCA TO siga;
  GRANT REFERENCES ON CORPORATIVO.DP_PESSOA TO siga;
  GRANT REFERENCES ON CORPORATIVO.DP_LOTACAO TO siga;
  GRANT REFERENCES ON CORPORATIVO.CP_ORGAO TO siga;
  GRANT REFERENCES ON CORPORATIVO.CP_ORGAO_USUARIO TO siga;

  GRANT CREATE JOB TO "SIGA";
GRANT CREATE PROCEDURE TO "SIGA";
GRANT UNLIMITED TABLESPACE TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_APLICACAO_FERIADO" TO "SIGA";
GRANT ALTER ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT DEBUG ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT DELETE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT FLASHBACK ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT INDEX ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT INSERT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT ON COMMIT REFRESH ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT QUERY REWRITE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT UPDATE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO_SEQ" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_FERIADO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_FERIADO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_GRUPO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_IDENTIDADE" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_LOCALIDADE" TO "SIGA";
GRANT DELETE ON "CORPORATIVO"."CP_MARCA" TO "SIGA";
GRANT INSERT ON "CORPORATIVO"."CP_MARCA" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_MARCA" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_MARCA" TO "SIGA";
GRANT UPDATE ON "CORPORATIVO"."CP_MARCA" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_MARCADOR" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_MARCADOR" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_MARCA_SEQ" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_MODELO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_OCORRENCIA_FERIADO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_ORGAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_ORGAO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_ORGAO_USUARIO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_ORGAO_USUARIO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_PAPEL" TO "SIGA";
GRANT DELETE ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGA";
GRANT INSERT ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGA";
GRANT UPDATE ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGA";
GRANT INSERT ON "CORPORATIVO"."CP_SERVICO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO" TO "SIGA";
GRANT UPDATE ON "CORPORATIVO"."CP_SERVICO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO_SEQ" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_SITUACAO_CONFIGURACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_CONFIGURACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_GRUPO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_IDENTIDADE" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_LOTACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_MARCA" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_MARCADOR" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_PAPEL" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_PESSOA" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_SERVICO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_SERVICO_SITUACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."CP_UF" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_CARGO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_CARGO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_FUNCAO_CONFIANCA" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_FUNCAO_CONFIANCA" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_LOTACAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_LOTACAO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_PESSOA" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_PESSOA" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_PROVIMENTO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_PROVIMENTO" TO "SIGA";
GRANT DELETE ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGA";
GRANT INSERT ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGA";
GRANT REFERENCES ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGA";
GRANT SELECT ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGA";
GRANT UPDATE ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGA";
GRANT EXECUTE ON "CTXSYS"."CTX_DDL" TO "SIGA";
GRANT "CONNECT" TO "SIGA";
GRANT "CTXAPP" TO "SIGA";
GRANT "RESOURCE" TO "SIGA";

GRANT REFERENCES ON "SIGA"."EX_MOBIL" TO "CORPORATIVO";
