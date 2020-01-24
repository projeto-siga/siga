--------------------------------------------------------
--  File created - Thursday-January-23-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table WF_DEF_DESVIO
--------------------------------------------------------

  CREATE TABLE "WF_DEF_DESVIO" 
   (	"DESV_ID" NUMBER(19,0), 
	"DESV_TX_CONDICAO" VARCHAR2(256 CHAR), 
	"DESV_NM" VARCHAR2(256 CHAR), 
	"DESV_NR_ORDEM" NUMBER(10,0), 
	"DESV_FG_ULTIMO" NUMBER(1,0), 
	"DEFT_ID" NUMBER(19,0), 
	"DEFT_ID_SEGUINTE" NUMBER(19,0)
   )
--------------------------------------------------------
--  DDL for Table WF_DEF_PROCEDIMENTO
--------------------------------------------------------

  CREATE TABLE "WF_DEF_PROCEDIMENTO" 
   (	"DEFP_ID" NUMBER(19,0), 
	"HIS_DT_FIM" TIMESTAMP (6), 
	"HIS_DT_INI" TIMESTAMP (6), 
	"HIS_ID_INI" NUMBER(19,0), 
	"DEFP_NM" VARCHAR2(256 CHAR), 
	"HIS_IDC_FIM" NUMBER(19,0), 
	"HIS_IDC_INI" NUMBER(19,0)
   )
--------------------------------------------------------
--  DDL for Table WF_DEF_RESPONSAVEL
--------------------------------------------------------

  CREATE TABLE "WF_DEF_RESPONSAVEL" 
   (	"DEFR_ID" NUMBER(19,0), 
	"DEFR_DS" VARCHAR2(256 CHAR), 
	"DEFR_NM" VARCHAR2(256 CHAR), 
	"DEFR_TP" NUMBER(10,0)
   )
--------------------------------------------------------
--  DDL for Table WF_DEF_TAREFA
--------------------------------------------------------

  CREATE TABLE "WF_DEF_TAREFA" 
   (	"DEFT_ID" NUMBER(19,0), 
	"DEFT_TX_ASSUNTO" VARCHAR2(256 CHAR), 
	"DEFT_TX_CONTEUDO" VARCHAR2(2048 CHAR), 
	"DEFT_NM" VARCHAR2(256 CHAR), 
	"DEFT_NR_ORDEM" NUMBER(10,0), 
	"DEFT_TP_RESPONSAVEL" NUMBER(10,0), 
	"DEFT_TP_TAREFA" NUMBER(10,0), 
	"DEFT_FG_ULTIMO" NUMBER(1,0), 
	"DEFP_ID" NUMBER(19,0), 
	"DEFR_ID" NUMBER(19,0), 
	"DEFT_ID_SEGUINTE" NUMBER(19,0)
   )
--------------------------------------------------------
--  DDL for Table WF_DEF_VARIAVEL
--------------------------------------------------------

  CREATE TABLE "WF_DEF_VARIAVEL" 
   (	"DEFV_ID" NUMBER(19,0), 
	"DEFV_TP_ACESSO" NUMBER(10,0), 
	"DEFV_CD" VARCHAR2(32 CHAR), 
	"DEFV_NM" VARCHAR2(256 CHAR), 
	"DEFV_TP" NUMBER(10,0), 
	"DEFT_ID" NUMBER(19,0)
   )
--------------------------------------------------------
--  DDL for Table WF_MOVIMENTACAO
--------------------------------------------------------

  CREATE TABLE "WF_MOVIMENTACAO" 
   (	"MOVI_TP" VARCHAR2(31 CHAR), 
	"MOVI_ID" NUMBER(19,0), 
	"HIS_DT_FIM" TIMESTAMP (6), 
	"HIS_DT_INI" TIMESTAMP (6), 
	"HIS_ID_INI" NUMBER(19,0), 
	"MOVI_DS" VARCHAR2(400 CHAR), 
	"HIS_ATIVO" NUMBER(10,0), 
	"MOVI_TP_DESIGNACAO" NUMBER(10,0), 
	"HIS_IDC_FIM" NUMBER(19,0), 
	"HIS_IDC_INI" NUMBER(19,0), 
	"PROC_ID" NUMBER(19,0), 
	"DEFT_ID_DE" NUMBER(19,0), 
	"DEFT_ID_PARA" NUMBER(19,0), 
	"LOTA_ID_DE" NUMBER(19,0), 
	"LOTA_ID_PARA" NUMBER(19,0), 
	"PESS_ID_DE" NUMBER(19,0), 
	"PESS_ID_PARA" NUMBER(19,0)
   )
--------------------------------------------------------
--  DDL for Table WF_PROCEDIMENTO
--------------------------------------------------------

  CREATE TABLE "WF_PROCEDIMENTO" 
   (	"PROC_ID" NUMBER(19,0), 
	"PROC_TS_EVENTO" TIMESTAMP (6), 
	"PROC_NM_EVENTO" VARCHAR2(255 CHAR), 
	"PROC_NR_CORRENTE" NUMBER(10,0), 
	"LOTA_ID_EVENTO" RAW(255), 
	"PESS_ID_EVENTO" RAW(255), 
	"PROC_CD_PRINCIPAL" VARCHAR2(255 CHAR), 
	"PROC_TP_PRIORIDADE" NUMBER(10,0), 
	"PROC_ST_CORRENTE" NUMBER(10,0), 
	"PROC_TP_PRINCIPAL" NUMBER(10,0), 
	"DEFP_ID" NUMBER(19,0)
   )
   
   
   --------------------------------------------------------
--  DDL for Function REMOVE_ACENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGAWF"."REMOVE_ACENTO" 
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
 


GRANT DELETE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT INSERT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT UPDATE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO_SEQ" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_GRUPO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."CP_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_ORGAO_USUARIO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_PAPEL" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGAWF";
GRANT INSERT ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT UPDATE ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO_SEQ" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SITUACAO_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_GRUPO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_LOTACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_PAPEL" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_CARGO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_FUNCAO_CONFIANCA" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."DP_LOTACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_LOTACAO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."DP_PESSOA" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_PESSOA" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_PESSOA" TO "SIGAWF";
GRANT "CONNECT" TO "SIGAWF";