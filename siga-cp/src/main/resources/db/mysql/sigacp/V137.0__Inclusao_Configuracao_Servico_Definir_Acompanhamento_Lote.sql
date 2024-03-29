-- CADASTRO DE SERVICO SIGA-DOC-DEFLOTE
Insert into corporativo.cp_servico (SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
   SELECT  'SIGA-DOC-DEFLOTE','Definir Acompanhamento em Lote',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE SIGLA_SERVICO = 'SIGA-DOC'   
   ON DUPLICATE KEY UPDATE SIGLA_SERVICO = 'SIGA-DOC-DEFLOTE';

-- PERMISSAO DEFAULT NAO PODE A TODOS PARA O SERVICO SIGA-DOC-DEFLOTE
INSERT INTO corporativo.cp_configuracao (HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO,ID_SERVICO,HIS_IDC_INI)
VALUES (current_timestamp(), '2', '200', (SELECT ID_SERVICO FROM corporativo.cp_servico WHERE SIGLA_SERVICO = 'SIGA-DOC-DEFLOTE'), '1');	
