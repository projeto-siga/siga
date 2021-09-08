SET SQL_SAFE_UPDATES = 0;
update siga.ex_configuracao c inner join siga.ex_modelo o on c.ID_MOD = o.ID_MOD set c.ID_MOD = o.HIS_ID_INI;
SET SQL_SAFE_UPDATES = 1;
