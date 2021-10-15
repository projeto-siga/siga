-- -------------------------------------------------------------------------    
--  Indices para as tabelas com FK para CpArquivo
-- -------------------------------------------------------------------------
CREATE INDEX SIGA.FK_DOC_ARQ
          ON SIGA.EX_DOCUMENTO 
             (ID_ARQ);

CREATE INDEX SIGA.FK_MOD_ARQ
          ON SIGA.EX_MODELO 
             (ID_ARQ);

CREATE INDEX SIGA.FK_MOV_ARQ
          ON SIGA.EX_MOVIMENTACAO 
             (ID_ARQ);

CREATE INDEX SIGA.FK_PRE_ARQ
          ON SIGA.EX_PREENCHIMENTO 
             (ID_ARQ);