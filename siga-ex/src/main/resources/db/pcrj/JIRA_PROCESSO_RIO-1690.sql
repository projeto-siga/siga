-- insert para criar numeracao sequencial do sicop
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- excluir as marcas
DELETE  FROM CORPORATIVO.CP_MARCA A WHERE A.ID_REF=12059;
-- excluir movimentacao
DELETE FROM SIGA.EX_MOVIMENTACAO em  WHERE em.ID_MOBIL=12059;
-- excluir mobil
DELETE FROM SIGA.EX_MOBIL em  WHERE em.ID_DOC=6079;
-- excluir boletm interno
DELETE FROM EX_BOLETIM_DOC ebd WHERE ebd.ID_DOC=6079;
-- excluir documento
DELETE FROM SIGA.EX_DOCUMENTO ed WHERE ed.ID_DOC=6079;