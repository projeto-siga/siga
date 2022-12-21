-- Adicionado 2 campos para Persistência do Motivo da Inativação e Ativação. 
-- Campos são separados devido a estrutura de versionamento permitir Ativar com Motivo e com a mesma linha Inativar com Motivo adicionando 


ALTER TABLE CORPORATIVO.DP_LOTACAO 
ADD (MOTIVO_INATIVACAO VARCHAR2(500));

ALTER TABLE CORPORATIVO.DP_LOTACAO 
ADD (MOTIVO_ATIVACAO VARCHAR2(500));

COMMENT ON COLUMN CORPORATIVO.DP_LOTACAO.MOTIVO_INATIVACAO IS 'Armazena o motivo da Inativação da Lotação';
COMMENT ON COLUMN CORPORATIVO.DP_LOTACAO.MOTIVO_ATIVACAO IS 'Armazena o motivo da Ativação da Lotação';
