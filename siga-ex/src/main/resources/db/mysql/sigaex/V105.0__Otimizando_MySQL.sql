-- -------------------------------------------------------
--  OTIMIZANDO MYSQL
-- -------------------------------------------------------
ALTER TABLE siga.ex_modelo ADD INDEX MOD_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE siga.ex_modelo ADD INDEX IX_ATIVO_NM (HIS_ATIVO ASC, NM_MOD ASC) VISIBLE;
ALTER TABLE siga.ex_temporalidade ADD INDEX TEMP_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;

-- -----------------------------------------------------------------
--  REMOVE_ACENTO NÃO É MAIS NECESSÁRIA POIS O LIKE JÁ COMPARA ASSIM
-- -----------------------------------------------------------------
drop function if exists remove_acento;
delimiter //
create function remove_acento( textvalue varchar(16383) )
returns varchar(16383) DETERMINISTIC
begin

return textvalue;

end;//
DELIMITER ;
