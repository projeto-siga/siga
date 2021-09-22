-- ----------------------------------------------------------    
--  Configuracao default para desabilitar "Restringir Acesso"
-- ----------------------------------------------------------	          
Insert into corporativo.cp_configuracao (HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO) values (current_date(), 2,(select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Movimentar'));
Insert into siga.ex_configuracao (ID_CONFIGURACAO_EX,ID_TP_MOV) values (LAST_INSERT_ID(),(select id_tp_mov from siga.ex_tipo_movimentacao where descr_tipo_movimentacao = 'Restringir Acesso'));
