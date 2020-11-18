-- ------------------------------------------------------    
--  Coluna indicativa de documentos compostos na tabela de especies e modelos
-- ------------------------------------------------------	          
ALTER TABLE siga.ex_forma_documento ADD (IS_COMPOSTO tinyint(4));
ALTER TABLE siga.ex_modelo ADD (IS_COMPOSTO tinyint(4));
