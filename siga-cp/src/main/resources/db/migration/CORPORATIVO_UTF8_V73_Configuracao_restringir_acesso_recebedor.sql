--------------------------------------------------------------------------------------------------
--	SCRIPT: CONFIGURAÇÃO RESTRINGIR ACESSO AO RECEBER O DOCUMENTO.
--          APÓS RECEBIMENTO DE DOCUMENTO TRAMITADO PARA LOTAÇÃO, OS OUTROS USUÁRIOS DA LOTAÇÃO  
--          NÃO TERÃO MAIS ACESSO AO DOCUMENTO, SOMENTE O USUÁRIO QUE RECEBEU.
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;
Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (50,'Restringir acesso ao receber o documento', 2);