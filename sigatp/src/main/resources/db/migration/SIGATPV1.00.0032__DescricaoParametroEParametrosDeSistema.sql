alter table SIGATP.Parametro add (descricao varchar2(255 char));
alter table SIGATP.Parametro_AUD add (descricao varchar2(255 char));

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(10, 'cron.executa', 'false', current_timestamp, 'Executar ou nao o cron que envia emails');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(11, 'cron.flagEmail', 'false', current_timestamp, 'Enviar email para o usuario (caso "false": enviar para lista)');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(12, 'cron.listaEmail', 'todos.nusad@trf2.jus.br', current_timestamp, 'Lista de emails a enviar');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(20, 'cron.executaw', 'false', current_timestamp, 'Workflow: Executar ou nao o cron que envia emails');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(21, 'cron.flagEmailw', 'false', current_timestamp, 'Workflow: Enviar email para o usuario (caso "false": enviar para lista)');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(22, 'cron.listaEmailw', 'todos.nusad@trf2.jus.br', current_timestamp, 'Workflow: Lista de emails a enviar');

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(30, 'imagem.filesize', '1', current_timestamp, 'Tamanho maximo em MB da imagem para upload');

