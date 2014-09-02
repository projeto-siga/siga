-------------------------------------------
--	SCRIPT:CORPORATIVO_DESTINACAO
-------------------------------------------

--update corporativo.cp_marcador set descr_marcador = 'Encerrado' where id_marcador = 6;
update corporativo.cp_marcador set descr_marcador = 'A Eliminar' where id_marcador = 7;
update corporativo.cp_marcador set descr_marcador = 'Eliminado' where id_marcador = 8;
update corporativo.cp_tipo_configuracao set dsc_tp_configuracao = 'Refazer' where id_tp_configuracao = 8;

insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (50, 'A Recolher para Arq. Permanente', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (51, 'A Transferir para Arq. Intermediário', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (52, 'Em Edital de Eliminação', 1);