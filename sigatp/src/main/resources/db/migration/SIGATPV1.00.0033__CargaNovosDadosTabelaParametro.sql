insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (13, 'cron.inicio', '0 0 9 ? 1/1 2#1 *', current_timestamp, 'EmailNotificações : Configurações de inicialização do serviço (será utilizados na versão com vraptor).');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (23, 'cron.iniciow', '0 0/10 9-19 1/1 * ? *', current_timestamp, 'Workflow : Configurações de inicialização do serviço (será utilizados na versão com vraptor).');

insert into SIGATP.PARAMETRO (ID, NOMEPARAMETRO, VALORPARAMETRO, DATAINICIO, DESCRICAO) 
VALUES (24, 'cron.dataInicioPesquisaw', '01/04/2014', current_timestamp, 'Workflow : Data de início das requisições transporte para notificação');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (25, 'caminhoHostnameStandalone', 'sigaidp.crossdomain.url', current_timestamp, 'Workflow : Parâmetro no arquivo standalone.xml que contém o hostname do servidor');

UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'EmailNotificações : Executar ou nao o cron que envia emails' WHERE ID = 10;
UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'EmailNotificações : Enviar email para o usuario (caso "false": enviar para lista)' WHERE ID = 11;
UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'EmailNotificações :  Lista de emails a enviar' WHERE ID = 12;

UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'Workflow : Executar ou nao o cron que envia emails' WHERE ID = 20;
UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'Workflow : Enviar email para o usuario (caso "false": enviar para lista)' WHERE ID = 21;
UPDATE SIGATP.PARAMETRO SET DESCRICAO = 'Workflow :  Lista de emails a enviar' WHERE ID = 22;