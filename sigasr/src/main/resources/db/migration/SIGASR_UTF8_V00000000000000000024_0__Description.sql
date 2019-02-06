
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

insert into corporativo.cp_tipo_configuracao(id_tp_configuracao, dsc_tp_configuracao) 
	values (306, 'Escalonar Por Solicitacao Filha');
commit;