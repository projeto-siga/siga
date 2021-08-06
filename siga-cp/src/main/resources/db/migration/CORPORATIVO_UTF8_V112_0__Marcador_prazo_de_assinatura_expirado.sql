-- Inclusão do marcador Prazo de Assinatura Expirado e Assinado
--
Insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_finalidade_marcador, ord_marcador, grupo_marcador,
		id_cor, id_icone, his_id_ini, his_dt_ini, his_idc_ini, his_ativo, listavel_pesquisa_default) 
    values (74, 'Prazo de Assinatura Expirado', 1, 250, 2,
    	1, 25, 74, CURRENT_TIMESTAMP, 1, 1, 1);
Insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_finalidade_marcador, ord_marcador, grupo_marcador,
		id_cor, id_icone, his_id_ini, his_dt_ini, his_idc_ini, his_ativo, listavel_pesquisa_default) 
    values (75, 'Assinado', 1, 260, 7,
    	1, 20, 75, CURRENT_TIMESTAMP, 1, 1, 1);