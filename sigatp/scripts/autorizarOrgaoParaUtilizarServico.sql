insert into cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (cp_servico_seq.nextval, 'SIGA-TP-ADM', 'Administrar', 2864, 2);

	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, id_lotacao
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP-ADM'),
		sysdate, 
		(select id_lotacao from corporativo.dp_lotacao where sigla_lotacao = 'NUSAD' and data_fim_lot is null and id_orgao_usu = 3)
	);
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, id_lotacao
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP'),
		sysdate, 
		(select id_lotacao from corporativo.dp_lotacao where sigla_lotacao = 'NUSAD' and data_fim_lot is null and id_orgao_usu = 3)
	);