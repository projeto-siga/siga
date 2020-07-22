--------------------------------------------------------------------------------------------------
--	SCRIPT: CONFIGURAÇÃO TRAMITAR SEM CAPTURADO.
--          NÃO PERMITIR A TRAMITAÇÃO DO DOCUMENTO SEM UM DOCUMENTO FILHO CAPTURADO
--          CONFIGURAÇÃO CRIAR NOVO EXTERNO
--          HABILITAR O BOTÃO DE CRIAR NOVO PARA ORGÃO EXTERNO		
--------------------------------------------------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;
Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (52,'Tramitar sem capturado', 1);
Insert into CORPORATIVO.CP_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO,DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) values (53,'Criar novo externo', 2);