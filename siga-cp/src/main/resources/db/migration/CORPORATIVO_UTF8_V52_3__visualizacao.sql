--------------------------------------------------------
--  DDL for Table DP_VISUALIZACAO
--------------------------------------------------------

  CREATE TABLE "CORPORATIVO"."DP_VISUALIZACAO" 
   (	"ID_VISUALIZACAO" NUMBER(10,0), 
	"ID_TITULAR" NUMBER(10,0), 
	"ID_DELEG" NUMBER(10,0), 
	"DT_INI_DELEG" DATE, 
	"DT_FIM_DELEG" DATE, 
	"DT_INI_REG" DATE, 
	"DT_FIM_REG" DATE, 
	"ID_REG_INI" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table DP_VISUALIZACAO_ACESSO
--------------------------------------------------------

  CREATE TABLE "CORPORATIVO"."DP_VISUALIZACAO_ACESSO" 
   (	"ID_VISUALIZACAO_ACESSO" NUMBER(10,0), 
	"ID_VISUALIZACAO" NUMBER(10,0), 
	"ID_TITULAR" NUMBER(10,0), 
	"ID_DELEG" NUMBER(10,0), 
	"DT_ACESSO" DATE, 
	"ID_DOC" NUMBER(10,0)
   ) ;

--------------------------------------------------------
--  DDL for Sequence DP_VISUALIZACAO_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CORPORATIVO"."DP_VISUALIZACAO_SEQ"  MINVALUE 0 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 4 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DP_VISUALIZACAO_ACESSO_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CORPORATIVO"."DP_VISUALIZACAO_ACESSO_SEQ"  MINVALUE 0 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
