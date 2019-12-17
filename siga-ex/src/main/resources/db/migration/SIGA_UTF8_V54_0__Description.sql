-------------------------------------------
--	SCRIPT:Cria Estrutura para Controle de Numeração de Documentos
-------------------------------------------
  ALTER SESSION SET CURRENT_SCHEMA=siga;
--------------------------------------------------------
--  DDL for Table EX_DOCUMENTO_NUMERACAO
--------------------------------------------------------

  CREATE TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" 
   (	"ID_DOCUMENTO_NUMERACAO" NUMBER(*,0), 
		"ID_ORGAO_USU" NUMBER(*,0), 
		"ID_FORMA_DOC" NUMBER(*,0), 
		"ANO_EMISSAO" NUMBER(*,0), 
		"NR_DOCUMENTO" NUMBER(*,0), 
		"NR_INICIAL" NUMBER(15,0) DEFAULT 1, 
		"NR_FINAL" NUMBER(15,0), 
		"FL_ATIVO" CHAR(1 BYTE) DEFAULT 1
   ) ;
   
   
--------------------------------------------------------
--  DDL for Sequence EX_DOCUMENTO_NUMERACAO_SEQ
--------------------------------------------------------

  CREATE SEQUENCE  "SIGA"."EX_DOCUMENTO_NUMERACAO_SEQ"  MINVALUE 1 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;   
--------------------------------------------------------
--  DDL for Index EX_DOCUMENTO_NUMERACAO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."EX_DOCUMENTO_NUMERACAO_PK" ON "SIGA"."EX_DOCUMENTO_NUMERACAO" ("ID_DOCUMENTO_NUMERACAO");
--------------------------------------------------------
--  DDL for Index EX_DOCUMENTO_NUMERACAO_CHAVE
--------------------------------------------------------

  CREATE UNIQUE INDEX "SIGA"."EX_DOCUMENTO_NUMERACAO_CHAVE" ON "SIGA"."EX_DOCUMENTO_NUMERACAO" ("ID_ORGAO_USU", "ID_FORMA_DOC", "ANO_EMISSAO");
--------------------------------------------------------
--  DDL for Trigger EX_DOCUMENTO_NUM_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_DOCUMENTO_NUM_INSERT_TRG" BEFORE INSERT 
  ON siga.EX_DOCUMENTO_NUMERACAO 
  FOR EACH ROW 
  begin
	if :new.ID_DOCUMENTO_NUMERACAO is null then
		select EX_DOCUMENTO_NUMERACAO_SEQ.nextval into :new.ID_DOCUMENTO_NUMERACAO from dual;
	end if;
  end;
  /
  ALTER TRIGGER "SIGA"."EX_DOCUMENTO_NUM_INSERT_TRG" ENABLE;
--------------------------------------------------------
--  Constraints for Table EX_DOCUMENTO_NUMERACAO
--------------------------------------------------------

  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" ADD CONSTRAINT "EX_DOCUMENTO_NUMERACAO_PK" PRIMARY KEY ("ID_DOCUMENTO_NUMERACAO");
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("NR_INICIAL" NOT NULL ENABLE);
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("NR_DOCUMENTO" NOT NULL ENABLE);
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("ANO_EMISSAO" NOT NULL ENABLE);
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("ID_FORMA_DOC" NOT NULL ENABLE);
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("ID_ORGAO_USU" NOT NULL ENABLE);
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" MODIFY ("ID_DOCUMENTO_NUMERACAO" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table EX_DOCUMENTO_NUMERACAO
--------------------------------------------------------

  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" ADD CONSTRAINT "EX_DOCUMENTO_NUMERACAO_FORMA" FOREIGN KEY ("ID_FORMA_DOC")
	  REFERENCES "SIGA"."EX_FORMA_DOCUMENTO" ("ID_FORMA_DOC") ENABLE;
  ALTER TABLE "SIGA"."EX_DOCUMENTO_NUMERACAO" ADD CONSTRAINT "EX_DOCUMENTO_NUMERACAO_ORGAO" FOREIGN KEY ("ID_ORGAO_USU")
	  REFERENCES "CORPORATIVO"."CP_ORGAO_USUARIO" ("ID_ORGAO_USU") ENABLE;
	  
	  
--------------------------------------------------------
--  DDL for Function FUN_NUMERACAO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGA"."FUN_NUMERACAO_DOCUMENTO" 
	(pId_orgao_usu IN NUMBER, 
	pId_forma_doc IN NUMBER, 
	pAno_emissao  IN NUMBER) 
	RETURN NUMBER 
	IS 
	  v_numero_documento NUMBER := 1; 
	  v_id_doc_num NUMBER; 
	BEGIN 
	    SELECT id_documento_numeracao 
	    INTO   v_id_doc_num 
	    FROM   ex_documento_numeracao 
	    WHERE  id_orgao_usu = pId_orgao_usu 
	           AND id_forma_doc = pId_forma_doc 
	           AND ano_emissao = pAno_emissao
	           AND fl_ativo = 1
	    FOR UPDATE;  /* Lock Registro para MUTEX */ 
	
	    UPDATE ex_documento_numeracao 
	    SET    nr_documento = nr_documento + 1
	    WHERE  id_documento_numeracao = v_id_doc_num
	    RETURNING nr_documento
	    INTO v_numero_documento; 
	
	    RETURN v_numero_documento; 
	
	    EXCEPTION  WHEN NO_DATA_FOUND THEN
	    	--TODO: Criar rotina para verificar se número reseta todo ano. Se não, número incial igual ao numero final do ano anterior
	        INSERT INTO ex_documento_numeracao 
	                       (id_orgao_usu, id_forma_doc, ano_emissao, nr_documento) 
	               VALUES  (pid_orgao_usu, pid_forma_doc, pano_emissao, v_numero_documento); 
	        RETURN v_numero_documento;       
	
	END; 
	
	/
	
--------------------------------------------------------
--  DDL for Trigger EX_DOCUMENTO_UPDATE_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_DOCUMENTO_UPDATE_TRG" BEFORE UPDATE ON "EX_DOCUMENTO" FOR EACH ROW 
	begin
	    if ( (:old.NUM_EXPEDIENTE is null) ) then
	        if (  (:new.ID_ORGAO_USU is not null) and (:new.ID_FORMA_DOC is not null) and (:new.ANO_EMISSAO is not null) ) then
	            DECLARE
	              PID_ORGAO_USU NUMBER;
	              PID_FORMA_DOC NUMBER;
	              PANO_EMISSAO NUMBER;
	              v_Return NUMBER;
	            BEGIN
	              PID_ORGAO_USU := :new.ID_ORGAO_USU;
	              PID_FORMA_DOC := :new.ID_FORMA_DOC;
	              PANO_EMISSAO := :new.ANO_EMISSAO;
	
	                v_Return := FUN_NUMERACAO_DOCUMENTO(
	                            PID_ORGAO_USU => PID_ORGAO_USU,
	                            PID_FORMA_DOC => PID_FORMA_DOC,
	                            PANO_EMISSAO => PANO_EMISSAO
	                          );
	
	              :new.NUM_EXPEDIENTE := v_Return;
	
	            END;
	        end if;
	    else
	        --Mantém numeração
	       :new.NUM_EXPEDIENTE := :old.NUM_EXPEDIENTE;
	    end if;
	end;
	/
	ALTER TRIGGER "SIGA"."EX_DOCUMENTO_UPDATE_TRG" ENABLE;
	
--------------------------------------------------------
--  DDL for Trigger EX_DOCUMENTO_INSERT_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" BEFORE INSERT ON "EX_DOCUMENTO" FOR EACH ROW 
	begin
	    if :new.ID_DOC is null then
	        select EX_DOCUMENTO_SEQ.nextval into :new.ID_DOC from dual;
	    end if;
	    
	
	    if (  (:new.ID_ORGAO_USU is not null) and (:new.ID_FORMA_DOC is not null) and (:new.ANO_EMISSAO is not null) ) then
	        DECLARE
	          PID_ORGAO_USU NUMBER;
	          PID_FORMA_DOC NUMBER;
	          PANO_EMISSAO NUMBER;
	          v_Return NUMBER;
	        BEGIN
	          PID_ORGAO_USU := :new.ID_ORGAO_USU;
	          PID_FORMA_DOC := :new.ID_FORMA_DOC;
	          PANO_EMISSAO := :new.ANO_EMISSAO;
	
	          v_Return := FUN_NUMERACAO_DOCUMENTO(
	                        PID_ORGAO_USU => PID_ORGAO_USU,
	                        PID_FORMA_DOC => PID_FORMA_DOC,
	                        PANO_EMISSAO => PANO_EMISSAO
	                      );
	
	          :new.NUM_EXPEDIENTE := v_Return;
	
	        END;
	    end if;
	end;
	/
	ALTER TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" ENABLE;

	
--------------------------------------------------------
--  FINAL DO SCRIPT DE ESTRUTURA
--------------------------------------------------------	  

--------------------------------------------------------
--  CARGA INICIAL PARA ESTRUTURA DE NUMERACAO
--------------------------------------------------------	  
	TRUNCATE TABLE siga.ex_documento_numeracao;
	
	INSERT INTO siga.ex_documento_numeracao (id_orgao_usu, id_forma_doc, ano_emissao, nr_documento, nr_inicial)
	SELECT org.id_orgao_usu ,
	       frm.id_forma_doc ,
	       doc.ano_emissao ,
	       Max(doc.num_expediente),
	       Max(doc.num_expediente)
	FROM siga.ex_documento doc
	INNER JOIN siga.ex_forma_documento frm ON doc.id_forma_doc = frm.id_forma_doc
	INNER JOIN corporativo.cp_orgao_usuario org ON doc.id_orgao_usu = org.id_orgao_usu
	WHERE doc.num_expediente IS NOT NULL
	GROUP BY org.id_orgao_usu,
	         frm.id_forma_doc,
	         doc.ano_emissao	  
	  
--------------------------------------------------------
--  FIM
--------------------------------------------------------		  
