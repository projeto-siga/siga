-- http://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-539
-- Criação de Modelo Auto de Advertência SMDEIS
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- numeracao = 22
-- 
INSERT INTO SIGA.EX_DOCUMENTO_NUMERACAO
( ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO, NR_DOCUMENTO, NR_INICIAL, NR_FINAL, FL_ATIVO)
VALUES( 5200, 320, 2022, 22, 1, NULL, '1');
commit;
