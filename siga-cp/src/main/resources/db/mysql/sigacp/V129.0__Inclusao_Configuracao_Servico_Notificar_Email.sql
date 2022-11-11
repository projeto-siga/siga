-- CADASTRO DOS SERVICOS DO MÓDULO SIGA-CEMAIL
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL',' Módulo de notificação por email',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL'; 
   
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL-DOCMARC','Notificações de marcadores destinados a minha unidade',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA-CEMAIL'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL-DOCMARC';  
   
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL-DOCUN','Documento foi tramitado para a minha unidade',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA-CEMAIL'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL-DOCUN';  
   
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL-DOCUSU','Tramitar para o meu usuário',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA-CEMAIL'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL-DOCUSU';  
   
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL-COSSIG','Fui incluído como cossignatário de um documento',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA-CEMAIL'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL-COSSIG'; 
   
Insert into corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
   SELECT  'SIGA-CEMAIL-RESPASSI','Fui incluído como responsável pela assinatura',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE sigla_servico = 'SIGA-CEMAIL'   
   ON DUPLICATE KEY UPDATE sigla_servico = 'SIGA-CEMAIL-RESPASSI'; 
        
-- PERMISSAO DEFAULT NAO PODE A TODOS PARA OS SERVICOS DO MÓDULO SIGA-CEMAIL
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL'), '1'); 
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCMARC'), '1');
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCUN'), '1');
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCUSU'), '1');
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-COSSIG'), '1');
INSERT INTO corporativo.cp_configuracao (his_dt_ini, id_sit_configuracao, id_tp_configuracao, id_servico, his_idc_ini)
VALUES (current_timestamp(), '2', '200', (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-RESPASSI'), '1');
        
