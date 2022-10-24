-- https://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-2700
-- Modelo - Licença Municipal de Instalação e Operação - LIO
-- PROCESSO_RIO-2700
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- Em siga.ex_forma_documento o códico 441 é Licença Municipal de Operação - LMO

INSERT INTO SIGA.EX_DOCUMENTO_NUMERACAO
( ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO, NR_DOCUMENTO, NR_INICIAL, NR_FINAL, FL_ATIVO)
VALUES( 5200, 442, 2022, 97, 1, NULL, '1');
commit;
