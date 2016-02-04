

ALTER SESSION SET CURRENT_SCHEMA = corporativo;
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (41,	'A Receber', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (42,	'Em Andamento', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (43,	'Fechado', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (44,	'Pendente', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (45,	'Cancelado', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (46,	'Em Pré-atendimento', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (47,	'Em Pós-atendimento', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (48,	'Como cadastrante', 1);
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (49,	'Como solicitante', 1);
update corporativo.cp_marca set id_marcador = id_marcador - 259 where id_marcador >=300;
delete from corporativo.cp_marcador where id_marcador >=300;
commit;