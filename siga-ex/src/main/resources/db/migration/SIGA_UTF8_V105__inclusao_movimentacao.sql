---------------------------------------------------------
-- ACRESCENTANDO MOVIMENTAÇÃO
---------------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=siga;

INSERT INTO SIGA.EX_TIPO_MOVIMENTACAO VALUES (85, 'Envio ao SIAFEM');
INSERT INTO SIGA.EX_TIPO_SEQUENCIA VALUES (SIGA.EX_TIPO_SEQUENCIA_SEQ.NEXTVAL, 'Formulário de aquisição - SIAFEM' , 1);    
