/** SOLUÇÃO NÃO IMPLEMENTADA DEVIDO A COMPORTAMENTO NÃO ADEQUADO EM ORACLE RAC.
--DROP ALL SEQUENCES OF NUMERACAO DOCUMENTO 
begin
  for sq in (select sequence_name 
              from   all_sequences 
              where  sequence_name like 'SQ_EX_DOC%'
             )
  loop
    execute immediate 'DROP SEQUENCE "SIGA"."'|| sq.sequence_name ||'"';
  end loop;             
end;
/

DECLARE
  V_NUM NUMBER;
BEGIN     
  SELECT COUNT(1)
  INTO   V_NUM
  FROM   USER_OBJECTS
  WHERE  OBJECT_NAME = 'CREATE_SQ_NUMERACAO_DOCUMENTO'
  AND    OBJECT_TYPE = 'PROCEDURE'; 

  IF V_NUM > 0 THEN
    EXECUTE IMMEDIATE 'DROP PROCEDURE "SIGA"."CREATE_SQ_NUMERACAO_DOCUMENTO"';
  END IF;

END;
/

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
    FOR UPDATE;  -- Lock Registro para MUTEX

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
--  ALTERACAO PARA TRIGGER NAO PRECISAR SER AUTONOMA
--------------------------------------------------------	          

create or replace Trigger SIGA_SEC_POLICY.EX_DOCUMENTO_BLOCK
Before Update or Delete on SIGA.EX_DOCUMENTO
For Each Row
WHEN (Old.dt_finalizacao is not null)
DECLARE
  --PRAGMA AUTONOMOUS_TRANSACTION; -- Trigger em transação autônoma.
  QTD_DOC_VAR  number(10,0):= 0;        
begin
        if updating then
            if  :Old.dt_finalizacao <> :New.dt_finalizacao
               then
                 raise_application_error( -20101,'Não é permitido alterar uma DATA de FINALIZACAO já existente' );
             end if;
 
             if :Old.fg_eletronico = 'N' then
                if dbms_lob.compare( :New.conteudo_blob_doc,:Old.conteudo_blob_doc)=0 then
                    if
                      :old.ID_DOC <> :New.ID_DOC OR
                      :old.NUM_EXPEDIENTE <> :New.NUM_EXPEDIENTE OR
                      :old.ANO_EMISSAO <> :New.ANO_EMISSAO OR
                      :old.ID_TP_DOC <> :New.ID_TP_DOC OR
                      :old.ID_CADASTRANTE <> :New.ID_CADASTRANTE OR
                      :old.ID_LOTA_CADASTRANTE <> :New.ID_LOTA_CADASTRANTE OR
                      :old.ID_SUBSCRITOR <> :New.ID_SUBSCRITOR OR
                      :old.ID_LOTA_SUBSCRITOR <> :New.ID_LOTA_SUBSCRITOR OR
                      :old.DESCR_DOCUMENTO <> :New.DESCR_DOCUMENTO OR
                      :old.DT_DOC <> :New.DT_DOC OR
                      :old.DT_REG_DOC <> :New.DT_REG_DOC OR
                      :old.NM_SUBSCRITOR_EXT <> :New.NM_SUBSCRITOR_EXT OR
                      :old.NUM_EXT_DOC <> :New.NUM_EXT_DOC OR
              --        dbms_lob.compare(:old.CONTEUDO_BLOB_DOC,:New.CONTEUDO_BLOB_DOC) <> 0 OR
                      :old.NM_ARQ_DOC <> :New.NM_ARQ_DOC OR
                      :old.CONTEUDO_TP_DOC <> :New.CONTEUDO_TP_DOC OR
                      :old.ID_DESTINATARIO <> :New.ID_DESTINATARIO OR
                      :old.ID_LOTA_DESTINATARIO <> :New.ID_LOTA_DESTINATARIO OR
                      :old.NM_DESTINATARIO <> :New.NM_DESTINATARIO OR
                      :old.DT_FINALIZACAO <> :New.DT_FINALIZACAO OR
                      :old.ID_MOD <> :New.ID_MOD OR
                      :old.ID_ORGAO_USU <> :New.ID_ORGAO_USU OR
                      :old.ID_CLASSIFICACAO <> :New.ID_CLASSIFICACAO OR
                      :old.ID_FORMA_DOC <> :New.ID_FORMA_DOC OR
                      :old.FG_PESSOAL <> :New.FG_PESSOAL OR
                      :old.ID_ORGAO_DESTINATARIO <> :New.ID_ORGAO_DESTINATARIO OR
                      :old.ID_ORGAO <> :New.ID_ORGAO OR
                      :old.OBS_ORGAO_DOC <> :New.OBS_ORGAO_DOC OR
                      :old.NM_ORGAO_DESTINATARIO <> :New.NM_ORGAO_DESTINATARIO OR
                      :old.FG_SIGILOSO <> :New.FG_SIGILOSO OR
                      :old.NM_FUNCAO_SUBSCRITOR <> :New.NM_FUNCAO_SUBSCRITOR OR
                      :old.FG_ELETRONICO <> :New.FG_ELETRONICO OR
                      :old.NUM_ANTIGO_DOC <> :New.NUM_ANTIGO_DOC OR
                      :old.ID_LOTA_TITULAR <> :New.ID_LOTA_TITULAR OR
                      :old.ID_TITULAR <> :New.ID_TITULAR OR
                      :old.NUM_AUX_DOC <> :New.NUM_AUX_DOC OR
                      :old.DSC_CLASS_DOC <> :New.DSC_CLASS_DOC OR
                --      :old.ID_NIVEL_ACESSO <> :New.ID_NIVEL_ACESSO OR
                      :old.ID_DOC_PAI <> :New.ID_DOC_PAI OR
                      :old.NUM_VIA_DOC_PAI <> :New.NUM_VIA_DOC_PAI OR
                      :old.ID_DOC_ANTERIOR <> :New.ID_DOC_ANTERIOR OR
                      :old.ID_MOB_PAI <> :New.ID_MOB_PAI OR
                      :old.NUM_SEQUENCIA <> :New.NUM_SEQUENCIA OR
                      :old.NUM_PAGINAS <> :New.NUM_PAGINAS OR
                      :old.DT_DOC_ORIGINAL <> :New.DT_DOC_ORIGINAL OR
                      :old.ID_MOB_AUTUADO <> :New.ID_MOB_AUTUADO OR
                      dbms_lob.compare(:old.ASSINATURA_BLOB_DOC,:New.ASSINATURA_BLOB_DOC) <> 0
                  then
                    raise_application_error( -20101,'Não é permitido alterar: não eletrônico com data de finalizacao e com conteúdo.' );
                  end if;
                else
                    raise_application_error( -20101,'Não é permitido alterar: não eletrônico com data de finalizacao e com conteúdo.' );
                end if;
             elsif :Old.fg_eletronico = 'S' then
                            SELECT count(1)
                              INTO QTD_DOC_VAR
					          FROM siga.ex_movimentacao m,
					               siga.ex_mobil mb 
					         WHERE mb.id_doc = :old.id_doc
					           AND m.id_tp_mov = 11 --TODO: 20191015 - Verificar a necessidade de colocar a mov = 58 - Assinar com Senha
					           AND m.id_mov_canceladora IS NULL
					           AND m.id_mobil = mb.id_mobil;

                    if  QTD_DOC_VAR <> 0 then
                        if
                          :old.ID_DOC <> :New.ID_DOC OR
                          :old.NUM_EXPEDIENTE <> :New.NUM_EXPEDIENTE OR
                          :old.ANO_EMISSAO <> :New.ANO_EMISSAO OR
                          :old.ID_TP_DOC <> :New.ID_TP_DOC OR
                          :old.ID_CADASTRANTE <> :New.ID_CADASTRANTE OR
                          :old.ID_LOTA_CADASTRANTE <> :New.ID_LOTA_CADASTRANTE OR
                          :old.ID_SUBSCRITOR <> :New.ID_SUBSCRITOR OR
                          :old.ID_LOTA_SUBSCRITOR <> :New.ID_LOTA_SUBSCRITOR OR
                          :old.DESCR_DOCUMENTO <> :New.DESCR_DOCUMENTO OR
                          :old.DT_DOC <> :New.DT_DOC OR
                          :old.DT_REG_DOC <> :New.DT_REG_DOC OR
                          :old.NM_SUBSCRITOR_EXT <> :New.NM_SUBSCRITOR_EXT OR
                          :old.NUM_EXT_DOC <> :New.NUM_EXT_DOC OR
                          :old.NM_ARQ_DOC <> :New.NM_ARQ_DOC OR
                          :old.CONTEUDO_TP_DOC <> :New.CONTEUDO_TP_DOC OR
                          :old.ID_DESTINATARIO <> :New.ID_DESTINATARIO OR
                          :old.ID_LOTA_DESTINATARIO <> :New.ID_LOTA_DESTINATARIO OR
                          :old.NM_DESTINATARIO <> :New.NM_DESTINATARIO OR
                          :old.DT_FINALIZACAO <> :New.DT_FINALIZACAO OR
                          :old.ID_MOD <> :New.ID_MOD OR
                          :old.ID_ORGAO_USU <> :New.ID_ORGAO_USU OR
                          :old.ID_CLASSIFICACAO <> :New.ID_CLASSIFICACAO OR
                          :old.ID_FORMA_DOC <> :New.ID_FORMA_DOC OR
                          :old.FG_PESSOAL <> :New.FG_PESSOAL OR
                          :old.ID_ORGAO_DESTINATARIO <> :New.ID_ORGAO_DESTINATARIO OR
                          :old.ID_ORGAO <> :New.ID_ORGAO OR
                          :old.OBS_ORGAO_DOC <> :New.OBS_ORGAO_DOC OR
                          :old.NM_ORGAO_DESTINATARIO <> :New.NM_ORGAO_DESTINATARIO OR
                          :old.FG_SIGILOSO <> :New.FG_SIGILOSO OR
                          :old.NM_FUNCAO_SUBSCRITOR <> :New.NM_FUNCAO_SUBSCRITOR OR
                          :old.FG_ELETRONICO <> :New.FG_ELETRONICO OR
                          :old.NUM_ANTIGO_DOC <> :New.NUM_ANTIGO_DOC OR
                          :old.ID_LOTA_TITULAR <> :New.ID_LOTA_TITULAR OR
                          :old.ID_TITULAR <> :New.ID_TITULAR OR
                          :old.NUM_AUX_DOC <> :New.NUM_AUX_DOC OR
                          :old.DSC_CLASS_DOC <> :New.DSC_CLASS_DOC OR
                      --    :old.ID_NIVEL_ACESSO <> :New.ID_NIVEL_ACESSO OR
                          :old.ID_DOC_PAI <> :New.ID_DOC_PAI OR
                          :old.NUM_VIA_DOC_PAI <> :New.NUM_VIA_DOC_PAI OR
                          :old.ID_DOC_ANTERIOR <> :New.ID_DOC_ANTERIOR OR
                          :old.ID_MOB_PAI <> :New.ID_MOB_PAI OR
                          :old.NUM_SEQUENCIA <> :New.NUM_SEQUENCIA OR
                          :old.NUM_PAGINAS <> :New.NUM_PAGINAS OR
                          :old.DT_DOC_ORIGINAL <> :New.DT_DOC_ORIGINAL OR
                          :old.ID_MOB_AUTUADO <> :New.ID_MOB_AUTUADO OR
                          dbms_lob.compare(:old.CONTEUDO_BLOB_DOC,:New.CONTEUDO_BLOB_DOC) <> 0 OR
                          dbms_lob.compare(:old.ASSINATURA_BLOB_DOC,:New.ASSINATURA_BLOB_DOC) <> 0
                        then      
                          raise_application_error( -20101,'Não é permitido alterar: eletrônico, com conteúdo, tipo mov. 11 e sem mov. canceladora.' );
                        end if;
                   end if;
             end if;
        elsif deleting then
             raise_application_error( -20101,'Não é permitido excluir: data de finalizacao existente.' );
        end if;
end;  
/



TRUNCATE TABLE ex_documento_numeracao;

INSERT INTO ex_documento_numeracao (id_orgao_usu, id_forma_doc, ano_emissao, nr_documento, nr_inicial)
SELECT org.id_orgao_usu ,
       frm.id_forma_doc ,
       doc.ano_emissao ,
       Max(doc.num_expediente),
       Max(doc.num_expediente)
FROM ex_documento doc
INNER JOIN ex_forma_documento frm ON doc.id_forma_doc = frm.id_forma_doc
INNER JOIN corporativo.cp_orgao_usuario org ON doc.id_orgao_usu = org.id_orgao_usu
WHERE doc.num_expediente IS NOT NULL
GROUP BY org.id_orgao_usu,
         frm.id_forma_doc,
         doc.ano_emissao;	

**/
