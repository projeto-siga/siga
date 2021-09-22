--------------------------------------------------------------------------------------------------
--  Criação do Tipo de Movimentação: Tramite paralelo e notificacao 
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=siga;
insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (82, 'Trâmite Paralelo');
insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (83, 'Notificação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (84, 'Conclusão de Trâmite');
