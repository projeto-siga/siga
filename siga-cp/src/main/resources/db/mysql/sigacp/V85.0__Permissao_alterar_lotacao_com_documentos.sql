/* Permissões para alterar nome e sigla da lotação após cadastro de documento */

INSERT INTO corporativo.cp_servico ( SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
   SELECT  'SIGA-GI-CAD_LOTACAO-ALT', 'Alterar Lotação com Documentos',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_LOTACAO';    

ALTER TABLE corporativo.dp_lotacao ADD (HIS_IDC_INI INT UNSIGNED COMMENT 'Id identidade da pessoa que cadastrou');
ALTER TABLE corporativo.dp_lotacao ADD (HIS_IDC_FIM INT UNSIGNED COMMENT 'Id identidade da pessoa que finalizou');