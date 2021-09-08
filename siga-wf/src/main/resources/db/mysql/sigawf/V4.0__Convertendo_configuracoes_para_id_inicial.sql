SET SQL_SAFE_UPDATES = 0;
update sigawf.wf_configuracao c inner join sigawf.wf_def_procedimento o on c.DEFP_ID = o.DEFP_ID set c.DEFP_ID = o.HIS_ID_INI;
SET SQL_SAFE_UPDATES = 1;
