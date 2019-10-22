/** SOLUÇÃO NÃO IMPLEMENTADA DEVIDO A COMPORTAMENTO NÃO ADEQUADO EM ORACLE RAC.

-------------------------------------------
--	SCRIPT:Readequação da Estrutura de Numeração do Documento
-------------------------------------------
  ALTER SESSION SET CURRENT_SCHEMA=siga;
--------------------------------------------------------
create or replace TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" BEFORE INSERT ON "EX_DOCUMENTO" FOR EACH ROW 
	begin
	    if :new.ID_DOC is null then
	        select EX_DOCUMENTO_SEQ.nextval into :new.ID_DOC from dual;
	    end if;
	    
	
	    if (  (:new.ID_ORGAO_USU is not null) and (:new.ID_FORMA_DOC is not null) and (:new.ANO_EMISSAO is not null) ) then
	        DECLARE
	          PID_ORGAO_USU NUMBER;
	          PID_FORMA_DOC NUMBER;
	          PANO_EMISSAO NUMBER;
              stmt VARCHAR2(500);
              sq_name VARCHAR2(30);
	          v_Return NUMBER;
	        BEGIN
	          PID_ORGAO_USU := :new.ID_ORGAO_USU;
	          PID_FORMA_DOC := :new.ID_FORMA_DOC;
	          PANO_EMISSAO := :new.ANO_EMISSAO;
              
              --TODO: Verificar e criar sequence
	
              -- Obtém numeracao atraves da SQ do ORGAO+FORMA+ANO 
              sq_name := 'SQ_EX_DOC_' || PID_ORGAO_USU || '_' || PID_FORMA_DOC || '_' || PANO_EMISSAO;
              stmt := 'SELECT "SIGA"."' || sq_name || '".NEXTVAL FROM DUAL ';
              dbms_output.put_line (stmt);
              EXECUTE IMMEDIATE stmt INTO v_Return;
	
	          :new.NUM_EXPEDIENTE := v_Return;
	
	        END;
	    end if;
	end;
/
**/