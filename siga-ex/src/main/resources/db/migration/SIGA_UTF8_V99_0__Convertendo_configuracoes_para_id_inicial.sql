update siga.ex_configuracao c set c.ID_MOD = (select o.HIS_ID_INI from siga.ex_modelo o where c.ID_MOD = o.ID_MOD);
