-- Desabilitar opções Externo e Interno Capturado no modelo Processo - jira processo_rio-1177
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
DELETE FROM SIGA.EX_TP_FORMA_DOC WHERE ID_FORMA_DOC=160 AND ID_TP_DOC IN (4,5);
commit;