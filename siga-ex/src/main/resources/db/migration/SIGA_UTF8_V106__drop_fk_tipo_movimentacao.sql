-- Remoção das FKs da EX_TIPO_MOVIMENTACAO que virou ENUM

ALTER TABLE "SIGA"."EX_CONFIGURACAO"
DROP CONSTRAINT "FK_TP_MOV";

ALTER TABLE "SIGA"."EX_MOVIMENTACAO"
DROP CONSTRAINT "MOV_TP_MOV_FK";

ALTER TABLE "SIGA"."EX_ESTADO_TP_MOV"
DROP CONSTRAINT "TP_MOV_ESTADO_TPMOV_FK";

ALTER TABLE "SIGA"."EX_TP_MOV_ESTADO"
DROP CONSTRAINT "TP_MOV_TPMOV_ESTADO_FK";