/** SOLUÇÃO NÃO IMPLEMENTADA DEVIDO A COMPORTAMENTO NÃO ADEQUADO EM ORACLE RAC.

-------------------------------------------
--	SCRIPT:Readequação da Estrutura de Numeração do Documento
-------------------------------------------
  ALTER SESSION SET CURRENT_SCHEMA=siga;
--------------------------------------------------------

  
drop trigger "SIGA"."EX_DOCUMENTO_UPDATE_TRG";
drop function "SIGA"."FUN_NUMERACAO_DOCUMENTO";
  
  
create or replace Trigger SIGA_SEC_POLICY.EX_DOCUMENTO_BLOCK
Before Update or Delete on SIGA.EX_DOCUMENTO
For Each Row
WHEN (Old.dt_finalizacao is not null)
DECLARE
  PRAGMA AUTONOMOUS_TRANSACTION; -- Trigger em transa��o aut�noma.
  QTD_DOC_VAR  number(10,0):= 0;
        
begin
        if updating then
            if  :Old.dt_finalizacao <> :New.dt_finalizacao
               then
                 raise_application_error( -20101,'N�o � permitido alterar uma DATA de FINALIZACAO j� existente' );
             end if;
 
             if :Old.fg_eletronico = 'N' then
                if    dbms_lob.compare( :New.conteudo_blob_doc,:Old.conteudo_blob_doc)=0 then
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
                      dbms_lob.compare(:old.ASSINATURA_BLOB_DOC,:New.ASSINATURA_BLOB_DOC) <> 0 OR
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
                      :old.ID_MOB_AUTUADO <> :New.ID_MOB_AUTUADO
                  then
                    raise_application_error( -20101,'N�o � permitido alterar: n�o eletr�nico com data de finalizacao e com conte�do.' );
                  end if;
                else
                    raise_application_error( -20101,'N�o � permitido alterar: n�o eletr�nico com data de finalizacao e com conte�do.' );
                end if;
             elsif :Old.fg_eletronico = 'S' then
					SELECT COUNT(1) into QTD_DOC_VAR
					  FROM siga.ex_documento doc
					 WHERE EXISTS (
					          SELECT 'x'
					          FROM siga.ex_movimentacao m,
					               siga.ex_mobil mb 
					         WHERE mb.id_doc = :old.id_doc
					           AND m.id_tp_mov = 11 --TODO: 20191015 - Verificar a necessidade de colocar a mov = 58 - Assinar com Senha
					           AND m.id_mov_canceladora IS NULL
					           AND m.id_mobil = mb.id_mobil
					      )
					   and doc.id_doc = :old.id_doc;

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
                          dbms_lob.compare(:old.CONTEUDO_BLOB_DOC,:New.CONTEUDO_BLOB_DOC) <> 0 OR
                          :old.NM_ARQ_DOC <> :New.NM_ARQ_DOC OR
                          :old.CONTEUDO_TP_DOC <> :New.CONTEUDO_TP_DOC OR
                          :old.ID_DESTINATARIO <> :New.ID_DESTINATARIO OR
                          :old.ID_LOTA_DESTINATARIO <> :New.ID_LOTA_DESTINATARIO OR
                          :old.NM_DESTINATARIO <> :New.NM_DESTINATARIO OR
                          :old.DT_FINALIZACAO <> :New.DT_FINALIZACAO OR
                          dbms_lob.compare(:old.ASSINATURA_BLOB_DOC,:New.ASSINATURA_BLOB_DOC) <> 0 OR
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
                          :old.ID_MOB_AUTUADO <> :New.ID_MOB_AUTUADO
                        then      
                          raise_application_error( -20101,'N�o � permitido alterar: eletr�nico, com conte�do, tipo mov. 11 e sem mov. canceladora.' );
                        end if;
                   end if;
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
	
                  -- TODO: Verificar e criar sequence 
                  
                  --Obt�m numeracao atraves da SQ do ORGAO+FORMA+ANO 
                  sq_name := 'SQ_EX_DOC_' || PID_ORGAO_USU || '_' || PID_FORMA_DOC || '_' || PANO_EMISSAO;
                  stmt := 'SELECT "SIGA"."' || sq_name || '".NEXTVAL FROM DUAL ';
                  dbms_output.put_line (stmt);
                  EXECUTE IMMEDIATE stmt INTO v_Return;
	
	              :new.NUM_EXPEDIENTE := v_Return;
	
	            END;
	        end if;
        elsif deleting then
             raise_application_error( -20101,'N�o � permitido excluir: data de finalizacao existente.' );
        end if;
end;
/

**/