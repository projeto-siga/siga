-- CADASTRO DO SERVICO DO MÓDULO SECC
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SECC',' Módulo serviço de compra e contratação',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SECC'; 
   
-- PERMISSAO DEFAULT NAO PODE AO SERVICO DO MÓDULO SECC
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SECC'), '1'); 