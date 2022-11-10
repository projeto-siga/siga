-- CADASTRO DOS SERVICOS DO MÓDULO SIGA-CEMAIL
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL'),'2');
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCMARC'),'2');
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCUN'),'2');
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-DOCUSU'),'2');
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-COSSIG'),'2');
INSERT INTO corporativo.cp_servico (sigla_servico, desc_servico, id_servico_pai, id_tp_servico)
VALUES ('SIGA-CEMAIL',' Módulo de notificação por email',
        (SELECT id_servico FROM corporativo.cp_servico WHERE sigla_servico = 'SIGA-CEMAIL-RESPASSI'),'2');     
        
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
        
