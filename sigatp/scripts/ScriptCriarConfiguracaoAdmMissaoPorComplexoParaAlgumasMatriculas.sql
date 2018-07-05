insert into cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (cp_servico_seq.nextval, 'SIGA-TP-ADMMISSAOCOMPLEXO', 'AdministrarMissaoporComplexo', (select ID_SERVICO from cp_servico where sigla_servico = 'SIGA-TP'), 2);
	
-- 10596 - WLAMIR ADMINISTRADOR COMPLEXO ALMIRANTE BARROSO
	
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, his_idc_ini,id_pessoa, id_complexo
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP-ADMMISSAOCOMPLEXO'),
		sysdate,
		13332,
		(select id_pessoa from corporativo.dp_pessoa where matricula = '10596' and data_fim_pessoa is null and id_orgao_usu = 1),
		1
	);	
	
-- 10596 - ADRIANO RANGEL COSTA - Complexo EQUADOR - 20
	
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, his_idc_ini,id_pessoa, id_complexo
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP-ADMMISSAOCOMPLEXO'),
		sysdate,
		13332,
		(select id_pessoa from corporativo.dp_pessoa where matricula = '12595' and data_fim_pessoa is null and id_orgao_usu = 1),
		20
	);	
	
-- 13265 - JOSE VIEIRA NETO - Complexo Venezuela - 23
	
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, his_idc_ini,id_pessoa, id_complexo
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP-ADMMISSAOCOMPLEXO'),
		sysdate,
		13332,
		(select id_pessoa from corporativo.dp_pessoa where matricula = '13265' and data_fim_pessoa is null and id_orgao_usu = 1),
		23
	);	
	
	
	-- 13271 - ANDERSON MOUZINHO VIEIRA - Complexo Rio Branco - 16
	
	insert into corporativo.cp_configuracao (
		id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
		dt_ini_vig_configuracao, his_idc_ini,id_pessoa, id_complexo
	) values(
		corporativo.cp_configuracao_seq.nextval, 
		corporativo.cp_configuracao_seq.currval,
		200, 
		1, 
		(select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP-ADMMISSAOCOMPLEXO'),
		sysdate,
		13332,
		(select id_pessoa from corporativo.dp_pessoa where matricula = '13271' and data_fim_pessoa is null and id_orgao_usu = 1),
		16
	);
	
	