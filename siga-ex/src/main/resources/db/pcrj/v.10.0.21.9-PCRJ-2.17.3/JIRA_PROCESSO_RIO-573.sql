-- http://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-573
-- Criação de Modelo Averbação SMDEIS
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- numeracao = 1030
-- espécie Averbação - AVB // id=340
-- 
INSERT INTO SIGA.EX_DOCUMENTO_NUMERACAO
( ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO, NR_DOCUMENTO, NR_INICIAL, NR_FINAL, FL_ATIVO)
VALUES( 5200, 340, 2022, 1030, 1, NULL, '1');
commit;
