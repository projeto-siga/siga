-- -------------------------------------------------------
--  OTIMIZANDO MYSQL
-- -------------------------------------------------------
ALTER TABLE corporativo.cp_modelo ADD INDEX IX_ORG_DT_FIM (HIS_DT_FIM ASC, ID_ORGAO_USU ASC) VISIBLE;


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
