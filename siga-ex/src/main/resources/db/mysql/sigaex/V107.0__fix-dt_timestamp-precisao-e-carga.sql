-- -------------------------------------------------------------------------    
--  Corrige precisão do campo DT_TIMESTAMP
-- -------------------------------------------------------------------------
ALTER TABLE `siga`.`ex_movimentacao` 
CHANGE COLUMN `DT_TIMESTAMP` `DT_TIMESTAMP` TIMESTAMP(6) NULL DEFAULT CURRENT_TIMESTAMP(6) 
COMMENT 'Timestamp para permitir ordenar as movimentacoes sem utilizar o ID.';

-- -------------------------------------------------------------------------    
--  Carga Inicial da Coluna com o timestamp. Sera a data em DT_INI_MOV com o 
--  ID_MOV (limitado nos 6 digitos a direita) nos microssegundos, para que as movimentacoes
--  de um mobil fiquem na mesma ordem correta
-- -------------------------------------------------------------------------
SET SQL_SAFE_UPDATES = 0;
update `siga`.`ex_movimentacao` set DT_TIMESTAMP = concat(DT_INI_MOV,'.',LPAD(ID_MOV,6,0));
SET SQL_SAFE_UPDATES = 1; 
