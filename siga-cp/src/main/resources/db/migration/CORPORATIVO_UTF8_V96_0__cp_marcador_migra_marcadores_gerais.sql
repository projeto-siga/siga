-- Compatibilização dos marcadores gerais para o novo formato de marcador
--
update corporativo.cp_marcador set descr_detalhada = descr_marcador, id_cor = 1, 
	id_tp_aplicacao_marcador = 3, id_tp_data_planejada = 1, id_tp_data_limite = 1,
	id_tp_opcao_exibicao = 1, id_tp_texto = 1, id_tp_interessado = 1, his_dt_ini = sysdate
	where id_tp_marcador = 2 AND his_ativo = 1;
update corporativo.cp_marcador set id_tp_data_limite = 3, id_tp_opcao_exibicao = 2
	where id_marcador in (1008, 1009, 1010);