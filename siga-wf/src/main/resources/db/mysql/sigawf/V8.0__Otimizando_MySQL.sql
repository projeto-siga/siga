-- -------------------------------------------------------
--  OTIMIZANDO MYSQL
-- -------------------------------------------------------
ALTER TABLE sigawf.wf_def_desvio ADD INDEX IX_DEFD_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_def_procedimento ADD INDEX IX_DEFP_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_def_responsavel ADD INDEX IX_DEFR_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_def_tarefa ADD INDEX IX_DEFT_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_def_variavel ADD INDEX IX_DEFV_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_movimentacao ADD INDEX IX_MOVI_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;
ALTER TABLE sigawf.wf_responsavel ADD INDEX IX_RESP_ID_INI (HIS_ID_INI, HIS_DT_FIM) VISIBLE;

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
