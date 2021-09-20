update sigasr.sr_configuracao c set c.id_atendente = (select o.id_lotacao_ini from corporativo.dp_lotacao o where c.id_atendente = o.id_lotacao);
update sigasr.sr_configuracao c set c.id_tipo_atributo = (select o.his_id_ini from sigasr.sr_atributo o where c.id_tipo_atributo = o.id_atributo);
update sigasr.sr_configuracao c set c.id_pesquisa = (select o.his_id_ini from sigasr.sr_pesquisa o where c.id_pesquisa = o.id_pesquisa);
update sigasr.sr_configuracao c set c.id_lista = (select o.his_id_ini from sigasr.sr_lista o where c.id_lista = o.id_lista);
update sigasr.sr_configuracao c set c.id_acordo = (select o.his_id_ini from sigasr.sr_acordo o where c.id_acordo = o.id_acordo);
