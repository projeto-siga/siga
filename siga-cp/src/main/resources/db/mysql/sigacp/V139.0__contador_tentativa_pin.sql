-- Implementação de contador de Tentativas Malsucedidas de Match com o PIN da Identidade
ALTER TABLE corporativo.cp_identidade 
ADD COLUMN PIN_CONTADOR_TENTATIVA TINYINT UNSIGNED NOT NULL DEFAULT 0
COMMENT 'Contador de tentativas malsucedidas e consecutivas de match com o PIN da identidade. Default 0 e aplicação controla quantas tentativas são permitidas';


