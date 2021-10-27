SET SQL_SAFE_UPDATES = 0;
update sigasr.sr_configuracao c inner join corporativo.dp_lotacao o on c.id_atendente = o.id_lotacao set c.id_atendente = o.id_lotacao_ini;
update sigasr.sr_configuracao c inner join sigasr.sr_atributo o on c.id_tipo_atributo = o.id_atributo set c.id_tipo_atributo = o.his_id_ini;
update sigasr.sr_configuracao c inner join sigasr.sr_pesquisa o on c.id_pesquisa = o.id_pesquisa set c.id_pesquisa = o.his_id_ini;
update sigasr.sr_configuracao c inner join sigasr.sr_lista o on c.id_lista = o.id_lista set c.id_lista = o.his_id_ini;
update sigasr.sr_configuracao c inner join sigasr.sr_acordo o on c.id_acordo = o.id_acordo set c.id_acordo = o.his_id_ini;
SET SQL_SAFE_UPDATES = 1;
