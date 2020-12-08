-- Insere um valor inicial em cp_marcador.his_ativo
SET SQL_SAFE_UPDATES = 0;
update corporativo.cp_marcador set his_ativo = case when his_dt_fim is null then 1 else 0 end;
update corporativo.cp_marcador set his_id_ini = id_marcador where his_id_ini is null;
SET SQL_SAFE_UPDATES = 1;
