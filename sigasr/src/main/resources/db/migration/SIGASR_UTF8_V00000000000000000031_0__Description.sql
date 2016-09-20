ALTER SESSION SET CURRENT_SCHEMA=sigasr;

------------------------------------------------
--- acrescentando historico
------------------------------------------------
ALTER TABLE "SIGASR"."SR_ATRIBUTO_SOLICITACAO" 
	ADD (	"HIS_DT_INI" TIMESTAMP (6),
        	"HIS_DT_FIM" TIMESTAMP (6),
        	"HIS_ID_INI" NUMBER (19,0),
        	"ID_CADASTRANTE" NUMBER (19,0),
        	"ID_LOTA_CADASTRANTE" NUMBER (19,0)
		); 

------------------------------------------------
--- alterando o limite maximo da coluna 
------------------------------------------------

ALTER TABLE "SIGASR"."SR_ATRIBUTO" 
	MODIFY "DESCR_PRE_DEFINIDO" VARCHAR2(4000 CHAR);