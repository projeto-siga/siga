--------------------------------------------------------    
--  Coluna indicativa de documentos compostos na tabela de especies e modelos
--------------------------------------------------------	          
ALTER TABLE SIGA.EX_FORMA_DOCUMENTO ADD (IS_COMPOSTO NUMBER(1,0));
ALTER TABLE SIGA.EX_MODELO ADD (IS_COMPOSTO NUMBER(1,0));
