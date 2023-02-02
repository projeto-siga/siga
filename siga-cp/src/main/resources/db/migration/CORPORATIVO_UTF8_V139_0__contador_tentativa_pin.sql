-- Implementação de contador de Tentativas Malsucedidas de Match com o PIN da Identidade
ALTER TABLE CORPORATIVO.CP_IDENTIDADE 
ADD (PIN_CONTADOR_TENTATIVA SMALLINT DEFAULT 0 NOT NULL);

COMMENT ON COLUMN CORPORATIVO.CP_IDENTIDADE.PIN_CONTADOR_TENTATIVA 
IS 'Contador de tentativas malsucedidas e consecutivas de match com o PIN da identidade. Default 0 e aplicação controla quantas tentativas são permitidas';
