--Atualizando referencias a solicitacoes, visto que movs, atributos, 
--marcas e solicitacoes filhas passam a apontar para a solicitacao inicial
update sigasr.sr_movimentacao m set id_solicitacao = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = m.id_solicitacao);
update sigasr.sr_movimentacao m set id_lista = (select his_id_ini from sigasr.sr_lista where id_lista = m.id_lista) where id_lista is not null;
update corporativo.cp_marca mar set id_ref = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = mar.id_ref) where id_marcador in (41, 42, 43, 44, 45, 46, 47, 48, 49, 53, 54, 55)
update sigasr.sr_solicitacao sol set id_solicitacao_pai = (select his_id_ini from sigasr.sr_solicitacao where id_solicitacao = sol.id_solicitacao_pai) where id_solicitacao_pai is not null

alter table sr_item_configuracao add ID_PAI number(19,0)
alter table sr_acao add ID_PAI number(19,0)