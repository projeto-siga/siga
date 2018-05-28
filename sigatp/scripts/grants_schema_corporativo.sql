	ALTER SESSION SET CURRENT_SCHEMA = corporativo;
	
	grant select on CP_TIPO_SERVICO to sigatp, SIGATP_CON;
	grant select on CP_TIPO_CONFIGURACAO to sigatp, SIGATP_CON;
	grant select on CP_IDENTIDADE to sigatp, SIGATP_CON;
	grant select on CP_TIPO_IDENTIDADE to sigatp, SIGATP_CON;
	grant select on DP_PESSOA to sigatp, SIGATP_CON;
	grant select on DP_LOTACAO to sigatp, SIGATP_CON;
	grant select on CP_MARCADOR to sigatp, SIGATP_CON;
	grant select on CP_TIPO_MARCA to sigatp, SIGATP_CON;
	grant select on CP_TIPO_MARCADOR to sigatp, SIGATP_CON;
	grant select on CP_COMPLEXO to sigatp, SIGATP_CON;
	grant select on DP_CARGO to sigatp, SIGATP_CON;
	grant select on DP_FUNCAO_CONFIANCA to sigatp, SIGATP_CON;
	grant select,insert on CP_SERVICO to sigatp, SIGATP_CON;
	grant select on CP_GRUPO to sigatp, SIGATP_CON;
	grant select on CP_SITUACAO_CONFIGURACAO to sigatp, SIGATP_CON;
	grant select on cp_feriado to sigatp, SIGATP_CON;
	grant select on cp_localidade to sigatp, SIGATP_CON;
	grant select on cp_modelo to sigatp, SIGATP_CON;
	grant select on cp_orgao to sigatp, SIGATP_CON;
	grant select on cp_papel to sigatp, SIGATP_CON;
	grant select on cp_personalizacao to sigatp, SIGATP_CON;
	grant select on cp_servico to sigatp, SIGATP_CON;
	grant select on cp_tipo_configuracao to sigatp, SIGATP_CON;
	grant select on cp_tipo_grupo to sigatp, SIGATP_CON;
	grant select on cp_tipo_identidade to sigatp, SIGATP_CON;
	grant select on cp_tipo_lotacao to sigatp, SIGATP_CON;
	grant select on cp_tipo_papel to sigatp, SIGATP_CON;
	grant select on cp_tipo_pessoa to sigatp, SIGATP_CON;
	grant select on cp_uf to sigatp, SIGATP_CON;
	grant select on dp_substituicao to sigatp, SIGATP_CON;
	grant select on cp_modelo_seq to sigatp, SIGATP_CON;
	grant select on CP_IDENTIDADE_SEQ to sigatp, SIGATP_CON;
	
	grant select on cp_marca to sigatp, SIGATP_CON;
	grant select,insert on CP_CONFIGURACAO to sigatp, SIGATP_CON;
	grant select  on CP_ORGAO_USUARIO to sigatp, SIGATP_CON;
	
	grant select, update, delete, insert on cp_marca to sigatp, SIGATP_CON;
	grant select, update, insert on CP_CONFIGURACAO to sigatp, SIGATP_CON;
	
	grant select on CP_SERVICO_SEQ to sigatp, SIGATP_CON;
	grant select on CP_CONFIGURACAO_SEQ to sigatp, SIGATP_CON;
	
	grant execute on CORPORATIVO.REMOVE_ACENTO TO SIGATP,SIGATP_CON;
	
	create or replace public synonym REMOVE_ACENTO for CORPORATIVO.REMOVE_ACENTO;