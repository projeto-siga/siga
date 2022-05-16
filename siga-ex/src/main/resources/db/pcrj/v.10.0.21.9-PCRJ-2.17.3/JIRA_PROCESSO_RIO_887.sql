-- http://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-538
-- Criação de Modelo Auto de Advertência SMDEIS
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- numeracao = 168
-- 
INSERT INTO SIGA.EX_DOCUMENTO_NUMERACAO
( ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO, NR_DOCUMENTO, NR_INICIAL, NR_FINAL, FL_ATIVO)
VALUES( 5200, 301, 2022, 168, 1, NULL, '1');
commit;