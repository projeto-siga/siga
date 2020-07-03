--------------------------------------------------------------------------------------
--	SCRIPT: ACRESCENTA CONFIGURACAO PARA MOSTRAR OS TEMPORARIOS NA MESA2 VISÃO LOTAÇÃO 
--------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (49,'Mostrar Temporários para Lotação', 1);