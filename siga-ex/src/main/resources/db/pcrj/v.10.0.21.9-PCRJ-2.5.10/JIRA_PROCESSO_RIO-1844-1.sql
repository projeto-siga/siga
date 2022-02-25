-- remover origem capturado interno - jira processo_rio-1844
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
DELETE FROM SIGA.EX_TP_FORMA_DOC WHERE ID_FORMA_DOC=100 AND ID_TP_DOC IN (5);
commit;