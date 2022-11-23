-- https://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-2701
-- Modelo - Licença Municipal Prévia e de Instalação - LPI
-- PROCESSO_RIO-2701
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- Em siga.ex_forma_documento o códico 443 é Licença Municipal Prévia e de Instalação - LPI

INSERT INTO SIGA.EX_DOCUMENTO_NUMERACAO
( ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO, NR_DOCUMENTO, NR_INICIAL, NR_FINAL, FL_ATIVO)
VALUES( 5200, 443, 2022, 133, 1, NULL, '1');
commit;
