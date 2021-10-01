-- -----------------------------------------------------------------------------------------------
--	SCRIPT: ALTERA A QUANTIDADE DE CAMPOS SIGLA DE 20 PARA 30.
-- -----------------------------------------------------------------------------------------------
ALTER TABLE CORPORATIVO.DP_LOTACAO MODIFY (SIGLA_LOTACAO VARCHAR2(30));