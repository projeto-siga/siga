-- insert para criar numeracao sequencial do sicop
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
INSERT INTO SIGA.EX_SEQUENCIA
(ID_SEQ, ANO_EMISSAO, NUMERO, NR_INICIAL, NR_FINAL, FL_ATIVO, TIPO_SEQUENCIA, ZERAR_INICIO_ANO)
VALUES(1, 2022, 1, 1, 999999, '1', 2, '1');
COMMIT;