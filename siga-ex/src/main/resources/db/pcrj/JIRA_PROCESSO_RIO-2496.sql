-- http://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-2496
-- Carga de usuários do RIOCENTRO no RHUPAG 
-- connect corporativo@srv000764.infra.rio.gov.br:1521/sigadoc.pcrj;
-- Alterar número do SICI em função da nova estrutura organizacional

UPDATE CORPORATIVO.cp_orgao_usuario
SET
    id_orgao_usu = '1052'
WHERE
    id_orgao_usu = '3352'
    AND nm_orgao_usu = 'Centro de Feiras, Exposições e Congressos do Rio de Janeiro / RIOCENTRO';
COMMIT;    
