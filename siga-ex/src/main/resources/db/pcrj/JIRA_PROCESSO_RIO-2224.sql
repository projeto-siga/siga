-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- excluir as marcas onde marcador é inativo
DELETE  FROM CORPORATIVO.CP_MARCA cm 
WHERE cm.ID_MARCADOR  IN (
SELECT ID_MARCADOR  FROM CORPORATIVO.CP_MARCADOR cma
WHERE cma.HIS_ATIVO =0);

