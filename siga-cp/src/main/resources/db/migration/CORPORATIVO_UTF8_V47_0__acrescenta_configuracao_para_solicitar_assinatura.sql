-------------------------------------------------------------------
--	SCRIPT: ACRESCENTA CONFIGURACAO PARA SOLICITACAO DE ASSINATURA
-------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (39,'Pode Assinar sem Solicitação', 1);