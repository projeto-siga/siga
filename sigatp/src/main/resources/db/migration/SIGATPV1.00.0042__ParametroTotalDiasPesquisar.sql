insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao, id_orgao_usu) 
values 
	(51, 'total.dias.pesquisa', '7', current_timestamp, 'Numero de dias para pesquisar requisicoes ou missoes.', null);
	
insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao, id_orgao_usu) 
values 
	(52, 'total.dias.pesquisa', '15', current_timestamp, 'Numero de dias para pesquisar requisicoes ou missoes TRF.', 3);	