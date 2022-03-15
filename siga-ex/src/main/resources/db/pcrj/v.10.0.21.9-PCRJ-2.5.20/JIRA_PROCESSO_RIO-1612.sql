-- Desabilitar opção  Externo folha de rosto no modelo oficio - jira processo_rio-1612
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
DELETE FROM SIGA.EX_TP_FORMA_DOC WHERE ID_FORMA_DOC=1 AND ID_TP_DOC =3;
COMMIT;