-- Retirar (padrão) na frase Limitado ao órgão (padrão)
-- connect siga@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
UPDATE SIGA.EX_NIVEL_ACESSO SET NM_NIVEL_ACESSO ='Limitado ao órgão'  WHERE ID_NIVEL_ACESSO =1;
commit;
