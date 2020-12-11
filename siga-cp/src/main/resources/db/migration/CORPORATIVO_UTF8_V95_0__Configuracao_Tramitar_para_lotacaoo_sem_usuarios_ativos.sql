--------------------------------------------------------------------------------------------------
--	SCRIPT: CONFIGURAÇÃO NÃO PERMITIR TRAMITAR DOCUMENTOS PARA LOTAÇÃO SEM USUÁRIOS SUBSTITUTOS ATIVOS	
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;
Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (54,'Tramitar para lotação sem usuários ativos', 2);
