insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(15, 'missao.linhas.total', '2', current_timestamp, 'Total de linhas que indica quantas missoes serao exibidas na celula.');
	
insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(16, 'requisicao.linhas.total', '2', current_timestamp, 'Total de linhas que indica quantas requisicoes serao exibidas na celula.');
	
	
insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(60, 'cron.agendarTarefas', 'false', current_timestamp, 'Agendar ou nao tarefas.');
	

insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao, id_orgao_usu) 
values 
	(51, 'total.dias.pesquisa', '7', current_timestamp, 'Numero de dias para pesquisar requisicoes ou missoes.', null);
	
insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao, id_orgao_usu) 
values 
	(52, 'total.dias.pesquisa', '15', current_timestamp, 'Numero de dias para pesquisar requisicoes ou missoes TRF.', 3);	