CREATE TABLE "CORPORATIVO"."CP_CONTRATO" 
   (	"ID_ORGAO_USU" NUMBER(10,0), 
	"DT_CONTRATO" DATE 
   ) ;
   
COMMENT ON COLUMN "CORPORATIVO"."CP_CONTRATO"."DT_CONTRATO" IS 'Data de assinatura de contrato do órgão para fins de cobrança.'; 

--------------------------------------------------------
--  DDL for Index CP_CONTRATO_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "CORPORATIVO"."CP_CONTRATO_PK" ON "CORPORATIVO"."CP_CONTRATO" ("ID_ORGAO_USU");
