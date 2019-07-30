ALTER SESSION SET CURRENT_SCHEMA=corporativo;

--------------------------------------------------------
--  DDL for Index LOGIN_IDENTIDADE
--------------------------------------------------------

CREATE INDEX "CORPORATIVO"."IDX_LOGIN_IDENTIDADE" ON "CORPORATIVO"."CP_IDENTIDADE" ("LOGIN_IDENTIDADE");
