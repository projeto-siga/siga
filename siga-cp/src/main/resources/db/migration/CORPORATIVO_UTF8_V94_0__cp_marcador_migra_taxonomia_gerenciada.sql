-- Compatibilização dos marcadores de taxonomia gerenciada para o novo formato de marcador
--
-- Altera os marcadores de taxonomia gerenciada (tipo 6) para geral (tipo 2) 
-- e os de Demanda Judicial precisam mostrar data limite 
update corporativo.cp_marcador set id_tp_marcador = 2, id_tp_aplicacao_marcador = 3, id_tp_data_planejada = 1, id_tp_data_limite = 1,
	id_tp_opcao_exibicao = 1
	where id_tp_marcador = 6;
update corporativo.cp_marcador set id_tp_data_limite = 2, id_tp_opcao_exibicao = 2
	where id_marcador in (1008, 1009, 1010);