-- Check Constraint
-- Se tipo de configuração igual a 200 (Configuração de Serviço) não permite que serviço seja null

-- Pré-Script: Encerramento de Configurações tipo 200 sem Serviço Especificado
UPDATE CORPORATIVO.CP_CONFIGURACAO SET HIS_DT_FIM = sysdate 
WHERE
		ID_TP_CONFIGURACAO = 200 
    AND ID_SERVICO IS NULL
    AND HIS_DT_FIM IS NULL;
COMMIT;

ALTER TABLE CORPORATIVO.CP_CONFIGURACAO
ADD CONSTRAINT CHK_CONFIGURACAO_SERVICO
CHECK (
	   (ID_TP_CONFIGURACAO != 200) 
    OR (ID_TP_CONFIGURACAO = 200 AND (ID_SERVICO IS NOT NULL OR HIS_DT_FIM IS NOT NULL ))
);