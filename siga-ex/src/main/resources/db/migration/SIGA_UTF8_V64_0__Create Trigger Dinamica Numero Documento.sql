/* SOLUÇÃO NÃO IMPLEMENTADA DEVIDO A COMPORTAMENTO NÃO ADEQUADO EM ORACLE RAC.

-------------------------------------------
--	SCRIPT:Readequação da Estrutura de Numeração do Documento
-------------------------------------------
  ALTER SESSION SET CURRENT_SCHEMA=siga;
--------------------------------------------------------

create or replace PROCEDURE "SIGA"."CREATE_SQ_NUMERACAO_DOCUMENTO"  
( pId_orgao_usu IN VARCHAR2, 
  pId_forma_doc IN VARCHAR2, 
  pAno_emissao  IN VARCHAR2,
  pNumInicial  IN VARCHAR2)
AUTHID CURRENT_USER
AS
  v_exist_sq NUMBER := 0; 
  stmt VARCHAR2(500);
  sq_name VARCHAR2(30);
BEGIN 
    sq_name := 'SQ_EX_DOC_' || pId_orgao_usu || '_' || pId_forma_doc || '_' || pAno_emissao;

    SELECT 1 
    INTO v_exist_sq 
    FROM all_sequences 
    WHERE sequence_name = sq_name;

    EXCEPTION WHEN NO_DATA_FOUND THEN
         stmt := 'CREATE SEQUENCE "SIGA"."' || sq_name || '" MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH '|| pNumInicial ||' NOCACHE NOORDER NOCYCLE';
         EXECUTE IMMEDIATE stmt;

END CREATE_SQ_NUMERACAO_DOCUMENTO;
/


*/