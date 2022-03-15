-- jira processo_rio-2196
-- altera lotação da transf. (id_lota_resp) do ultimo movimento, para a lotação do cadastrante (id_lota_cadastrante).
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
UPDATE SIGA.EX_MOVIMENTACAO  SET ID_LOTA_RESP =24334 WHERE ID_LOTA_RESP =26873 AND ID_MOV =1536317;
COMMIT;