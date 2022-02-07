-- id_mov_ref correto - jira processo_rio-1600
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
UPDATE SIGA.ex_movimentacao SET ID_MOV_REF=412373 WHERE ID_MOV=412796;
COMMIT;