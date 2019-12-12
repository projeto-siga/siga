-------------------------------------------------------------------
--	SCRIPT: ACRESCENTA CONFIGURACAO PARA PERMITIR INCLUSAO DE DOCUMENTO EM AVULSOS 
-------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (45,'Incluir em documento avulso', 1);