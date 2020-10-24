-- -------------------------------------------------------------------------    
--  Coluna com Timestamp para permitir ordenar movimentacoes sem o ID_MOV
-- -------------------------------------------------------------------------
ALTER TABLE siga.ex_movimentacao ADD DT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp para permitir ordenar as movimentacoes sem utilizar o ID.';

-- -------------------------------------------------------------------------    
--  Carga Inicial da Coluna com o timestamp. Sera a data em DT_INI_MOV com o 
--  ID_MOV (limitado nos 6 digitos a direita) nos nanosegundos, para que as movimentacoes
--  de um mobil fiquem na mesma ordem do ID_MOV
-- -------------------------------------------------------------------------
SET SQL_SAFE_UPDATES = 0;
UPDATE siga.ex_movimentacao MOV SET DT_TIMESTAMP = DT_INI_MOV;
SET SQL_SAFE_UPDATES = 1;
