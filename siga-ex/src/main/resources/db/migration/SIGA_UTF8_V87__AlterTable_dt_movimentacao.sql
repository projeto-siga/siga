---------------------------------------------------------------------------    
--  Coluna com Timestamp para permitir ordenar movimentacoes sem o ID_MOV
---------------------------------------------------------------------------
ALTER TABLE SIGA.EX_MOVIMENTACAO ADD DT_TIMESTAMP TIMESTAMP (9) DEFAULT SYSTIMESTAMP;

COMMENT ON COLUMN "SIGA"."EX_MOVIMENTACAO"."DT_TIMESTAMP" IS 'Timestamp para permitir ordenar as movimentacoes sem utilizar o ID.'; 

---------------------------------------------------------------------------    
--  Carga Inicial da Coluna com o timestamp. Sera a data em DT_INI_MOV com o 
--  ID_MOV (limitado nos 6 digitos a direita) nos nanosegundos, para que as movimentacoes
--  de um mobil fiquem na mesma ordem do ID_MOV
---------------------------------------------------------------------------
UPDATE SIGA.EX_MOVIMENTACAO MOV SET DT_TIMESTAMP = 
	TO_TIMESTAMP(TO_TIMESTAMP(DT_INI_MOV, 'dd-mm-yyyy hh24:mi:ss')  
    			|| '.' || LPAD ( TO_CHAR (MOV.ID_MOV), 9, '0')
    	,  'dd-mm-yyyy hh24:mi:ss:ff9');