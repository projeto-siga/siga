-------------------------------------------
--	SCRIPT:RENOMEIA CONFIGURAÇÕES
-------------------------------------------
UPDATE CORPORATIVO.CP_TIPO_CONFIGURACAO SET DSC_TP_CONFIGURACAO = 'Utilizar Extensão de Conversor HTML' WHERE DSC_TP_CONFIGURACAO = 'Utilizar PD4ML';
UPDATE CORPORATIVO.CP_TIPO_CONFIGURACAO SET DSC_TP_CONFIGURACAO = 'Utilizar Extensão de Editor' WHERE DSC_TP_CONFIGURACAO = 'Utilizar XStandard';