--------------------------------------------------------------------------------------------------
--	SCRIPT: CONFIGURAÇÃO GERAR MOVIMENTAÇÕES POR WEBSERVICE
--          AO UTILIZAR, PASSA-SE A CONFIAR NO USUÁRIO AUTENTICADO NO WS PARA MOVIMENTAR EM NOME DO USUÁRIO PASSADO POR PARÂMETRO 
--
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;
Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (51,'Autorizar Movimentações via WS', 2);