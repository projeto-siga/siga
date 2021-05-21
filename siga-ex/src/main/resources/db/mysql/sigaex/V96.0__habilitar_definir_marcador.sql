-- -------------------------------------------------------    
--  Configuracao default para habilitar "Definir Marcador"
-- -------------------------------------------------------	          
update corporativo.cp_configuracao set HIS_DT_FIM = current_date(), HIS_IDC_FIM = '1' where ID_CONFIGURACAO = 56;
