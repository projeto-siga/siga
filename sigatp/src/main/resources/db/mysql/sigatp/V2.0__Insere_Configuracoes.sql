CREATE USER IF NOT EXISTS 'sigatp' IDENTIFIED BY 'sigatp';
grant select on corporativo.cp_tipo_servico to sigatp;
grant select on corporativo.cp_tipo_configuracao to sigatp;
grant select on corporativo.cp_identidade to sigatp;
grant select on corporativo.cp_tipo_identidade to sigatp;
grant select on corporativo.dp_pessoa to sigatp;
grant select on corporativo.dp_lotacao to sigatp;
grant select on corporativo.cp_marcador to sigatp;
grant select on corporativo.cp_tipo_marca to sigatp;
grant select on corporativo.cp_tipo_marcador to sigatp;
grant select on corporativo.cp_complexo to sigatp;
grant select on corporativo.dp_cargo to sigatp;
grant select on corporativo.dp_funcao_confianca to sigatp;
grant select, insert on corporativo.cp_servico to sigatp;
grant select on corporativo.cp_grupo to sigatp;
grant select on corporativo.cp_situacao_configuracao to sigatp;
grant select on corporativo.cp_feriado to sigatp;
grant select on corporativo.cp_localidade to sigatp;
grant select on corporativo.cp_modelo to sigatp;
grant select on corporativo.cp_orgao to sigatp;
grant select on corporativo.cp_papel to sigatp;
grant select on corporativo.cp_personalizacao to sigatp;
grant select on corporativo.cp_servico to sigatp;
grant select on corporativo.cp_tipo_configuracao to sigatp;
grant select on corporativo.cp_tipo_grupo to sigatp;
grant select on corporativo.cp_tipo_identidade to sigatp;
grant select on corporativo.cp_tipo_lotacao to sigatp;
grant select on corporativo.cp_tipo_papel to sigatp;
grant select on corporativo.cp_tipo_pessoa to sigatp;
grant select on corporativo.cp_uf to sigatp;
grant select on corporativo.dp_substituicao to sigatp;
grant select on corporativo.cp_marca to sigatp;
grant select, insert on corporativo.cp_configuracao to sigatp;
grant select on corporativo.cp_orgao_usuario to sigatp;
grant select, update, delete, insert on corporativo.cp_marca to sigatp;
grant select, update, insert on corporativo.cp_configuracao to sigatp;
grant select on corporativo.cp_contrato  to sigatp;
grant select on corporativo.cp_token  to sigatp;
grant select on corporativo.dp_visualizacao to sigatp;
grant select on corporativo.dp_visualizacao_acesso to sigatp;


-- inclusão do complexo: todas as requisições de transporte serao inicialmente direcionadas para ele, 
-- podendo ser encaminhadas posteriormente pelo administrador. Para incluir o complexo inicial, deve-se:
-- * observar a unicidade do ID_COMPLEXO
-- * dar um nome sugestivo - por exemplo, "PREDIO SEDE"
-- * identificar a localidade através do campo ID_LOCALIDADE (tabela corporativo.CP_LOCALIDADE)
-- * ID do órgão que avalia a utilização do módulo
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (99999, 'TESTE', 10, 999999999);

insert into corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) values (400, 'Utilizar complexo', 1);

SET @IDMAX = (Select max(id_configuracao) from corporativo.cp_configuracao);
SET @IDMAX = @IDMAX + 1;
Insert into corporativo.cp_configuracao (id_configuracao, dt_ini_vig_configuracao, dt_fim_vig_configuracao, his_dt_ini, id_orgao_usu, id_lotacao,
 id_cargo, id_funcao_confianca, id_pessoa, id_sit_configuracao, id_tp_configuracao, id_servico, id_grupo, nm_email, desc_formula, id_tp_lotacao,
 id_identidade, his_idc_ini, his_idc_fim, his_dt_fim, his_id_ini, id_complexo, id_orgao_objeto) values (@IDMAX,
 null, null, CURRENT_TIMESTAMP, 999999999, null, null, null, null, '5', '400', null, null, null, null, null, null, null, null, null, @IDMAX, '99999', null);

-- Caso o módulo já tenha sido executado juntamente com o SIGA, 
-- o registro criado pela primeira linha desta sequência de inserts 
-- já existirá no banco. Verifique anteriormente a necessidade de 
-- executa-lo usando a seguinte consulta:
--
-- select id_servico from corporativo.cp_servico where sigla_servico='SIGA-TP'
--
-- Os demais comandos devem ser executados.
SET @IDSIGA = (select id_servico from corporativo.cp_servico where sigla_servico='SIGA');
SET @IDMAX_SER = (select max(id_servico) from corporativo.cp_servico);
SET @IDMAX_SER = @IDMAX_SER + 1;
SET @ID_TP = @IDMAX_SER;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP', 'Módulo de Transportes', @IDSIGA, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
SET @ID_ADM = @IDMAX_SER;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-ADM', 'Administrar', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-APR', 'Aprovador', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-GAB', 'Gabinete', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-ADMGAB', 'AdminGabinete', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-AGN', 'Agente', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-ADMFROTA', 'AdministrarFrota', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-ADMMISSAO', 'AdministrarMissao', @ID_TP, 2);
SET @IDMAX_SER = @IDMAX_SER + 1;
insert into corporativo.cp_servico (id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (@IDMAX_SER, 'SIGA-TP-ADMMISSAOCOMPLEXO', 'AdministrarMissaoporComplexo', @ID_TP, 2);
SET @IDMAX = @IDMAX + 1;

-- Nesta configuração, devem ser alterados os campos de acordo com o órgão que avalia a utilização do módulo
insert into corporativo.cp_configuracao (
	id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
	dt_ini_vig_configuracao, id_lotacao
) values(
	@IDMAX,
	@IDMAX,
	200, 
	1, 
	@ID_TP,
	null, 
	(select id_lotacao from corporativo.dp_lotacao where sigla_lotacao = 'LTEST' and data_fim_lot is null and id_orgao_usu = 999999999)
);

SET @IDMAX = @IDMAX + 1;

-- Esta configuração deve ser alterada para definir o usuário que será o administrador do módulo, inicialmente
insert into corporativo.cp_configuracao (
	id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, 
	dt_ini_vig_configuracao, id_pessoa
) values(
	@IDMAX,
	@IDMAX,
	200, 
	1, 
	@ID_ADM,
	null, 
	(select id_pessoa from corporativo.dp_pessoa where nome_pessoa = 'USUARIO TESTE' and data_fim_pessoa is null and id_orgao_usu = 999999999)
);
