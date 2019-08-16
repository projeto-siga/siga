--Transferindo os blobs para uma tabela separada 
ALTER TABLE siga.ex_documento ADD (id_arq number(10,0) NULL comment 'Identificador do arquivo');
COMMENT ON COLUMN "SIGA"."EX_DOCUMENTO"."ID_ARQ" IS 'Identificador do arquivo';
ALTER TABLE siga.ex_documento ADD CONSTRAINT "DOC_ARQUIVO_FK" FOREIGN KEY ("ID_ARQ") REFERENCES corporativo.cp_arquivo("ID_ARQ") enable;

ALTER TABLE siga.ex_movimentacao ADD (id_arq number(10,0) NULL comment 'Identificador do arquivo');
COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."ID_ARQ" IS 'Identificador do arquivo';
ALTER TABLE siga.ex_movimentacao ADD CONSTRAINT "MOV_ARQUIVO_FK" FOREIGN KEY ("ID_ARQ") REFERENCES corporativo.cp_arquivo("ID_ARQ") enable;

ALTER TABLE siga.ex_modelo ADD (id_arq number(10,0) NULL comment 'Identificador do arquivo');
COMMENT ON COLUMN "SIGA"."EX_MODELO"."ID_ARQ" IS 'Identificador do arquivo';
ALTER TABLE siga.ex_modelo ADD CONSTRAINT "MOD_ARQUIVO_FK" FOREIGN KEY ("ID_ARQ") REFERENCES corporativo.cp_arquivo("ID_ARQ") enable;