
ALTER SESSION SET CURRENT_SCHEMA = corporativo;
insert into cp_marcador(id_marcador, descr_marcador, id_tp_marcador) values (53, 'Fechado Parcialmente', 1);
insert into cp_marcador(id_marcador, descr_marcador, id_tp_marcador) values (54, 'Em Controle de Qualidade', 1);

ALTER SESSION SET CURRENT_SCHEMA = sigasr;
insert into sr_tipo_movimentacao (id_tipo_movimentacao, nome_tipo_movimentacao) values (15, 'Fechamento Parcial');
insert into sr_tipo_movimentacao (id_tipo_movimentacao, nome_tipo_movimentacao) values (16, 'Avaliação');
insert into sr_tipo_movimentacao (id_tipo_movimentacao, nome_tipo_movimentacao) values (17, 'Início do Controle de Qualidade');
