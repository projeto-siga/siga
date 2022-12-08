-- Permissao para usar a Pesquisa Avançada - Se já existir, não faz nada
Insert into corporativo.cp_servico (SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
   SELECT  'SIGA-DOC-PESQ', 'Pesquisar',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE SIGLA_SERVICO = 'SIGA-DOC'
   ON DUPLICATE KEY UPDATE SIGLA_SERVICO = 'SIGA-DOC-PESQ';   

-- PERMISSAO DEFAULT DE ACESSO A TODOS PARA O SERVICO SIGA-WEB DE PESQUISA AVANCADA
Insert into corporativo.cp_configuracao (HIS_DT_INI, ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO,ID_SERVICO,HIS_IDC_INI)
values (current_timestamp(), '1', '200', (SELECT max(ID_SERVICO) FROM corporativo.cp_servico WHERE SIGLA_SERVICO = 'SIGA-DOC-PESQ'), '1'); 