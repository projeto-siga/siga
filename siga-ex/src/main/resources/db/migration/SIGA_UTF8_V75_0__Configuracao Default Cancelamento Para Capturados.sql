--------------------------------------------------------    
--  Configuracao default para Movimentar/Tornar Sem Efeito/Capturados
--  Para manter compatibilidade com funcionamento anterior (Nao permitia cancelar capturados)
--------------------------------------------------------	          
Insert into CORPORATIVO.CP_CONFIGURACAO (ID_CONFIGURACAO,HIS_DT_INI,ID_SIT_CONFIGURACAO,ID_TP_CONFIGURACAO) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.nextval,sysdate,'2',(select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Movimentar'));
Insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO_EX,ID_TP_MOV,ID_TP_DOC) values (CORPORATIVO.CP_CONFIGURACAO_SEQ.currval,(select id_tp_mov from siga.ex_tipo_movimentacao where descr_tipo_movimentacao = 'Cancelamento' or descr_tipo_movimentacao = 'Tornar sem Efeito'),4);
