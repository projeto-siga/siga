--Atualiza a trigger de proteção do siga-doc
--Usuário SIGA_SEC_POLICY
Create user SIGA_SEC_POLICY identified by SIGA_SEC_POLICY ;
--PRIVILÉGIOS de SISTEMA
Grant ALTER ANY TRIGGER to SIGA_SEC_POLICY;	
Grant ALTER SESSION to SIGA_SEC_POLICY;	
Grant CREATE ANY TRIGGER to SIGA_SEC_POLICY;	
Grant CREATE SESSION to SIGA_SEC_POLICY;	
Grant CREATE TRIGGER to SIGA_SEC_POLICY;	
Grant DROP ANY TRIGGER to SIGA_SEC_POLICY;	
--PRIVILÉGIOS de OBJETO
Grant SELECT	ON	SIGA.EX_DOCUMENTO to SIGA_SEC_POLICY;	
Grant SELECT	ON	SIGA.EX_MOBIL to SIGA_SEC_POLICY;	
Grant SELECT	ON	SIGA.EX_MOVIMENTACAO to SIGA_SEC_POLICY;
--Exclui a Trigger Anterior
Drop trigger SIGA.EX_DOCUMENTO_BLOCK;
--Cria a nova Trigger
create or replace
Trigger SIGA_SEC_POLICY.EX_DOCUMENTO_BLOCK
Before Update or Delete on SIGA.EX_DOCUMENTO
For Each Row
WHEN (Old.dt_fechamento is not null)
DECLARE 
  PRAGMA AUTONOMOUS_TRANSACTION; -- Trigger em transação autônoma.
  QTD_DOC_VAR  number(10,0):= 0;
         
begin
        if updating then
            if  :Old.dt_fechamento <> :New.dt_fechamento 
               then
                 raise_application_error( -20101,'Não é permitido alterar uma DATA de FECHAMENTO já existente' );
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
                      :old.DT_FECHAMENTO <> :New.DT_FECHAMENTO OR
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
                    raise_application_error( -20101,'Não é permitido alterar: não eletrônico com data de fechamento e com conteúdo.' );
                  end if;
                else
                    raise_application_error( -20101,'Não é permitido alterar: não eletrônico com data de fechamento e com conteúdo.' );
                end if;
             elsif :Old.fg_eletronico = 'S' then
                    select count(*) into QTD_DOC_VAR 
                    from siga.ex_documento doc
                    where  EXISTS (select * --d.id_doc   
                                    from siga.ex_movimentacao m, 
                                          siga.ex_mobil mb --,
                                          -- siga.ex_documento d   
                                    where --d.id_doc=fora.id_doc_var and
                                         -- d.id_doc=:old.id_doc and
                                          mb.id_doc = doc.id_doc and
                                          mb.id_doc = :old.id_doc and
                                          m.id_tp_mov = 11 and
                                          m.id_mov_canceladora is null and
                                          m.id_mobil = mb.id_mobil);
                
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
                          :old.DT_FECHAMENTO <> :New.DT_FECHAMENTO OR
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
                          raise_application_error( -20101,'Não é permitido alterar: eletrônico, com conteúdo, tipo mov. 11 e sem mov. canceladora.' );
                        end if;
                   end if;
             end if;
        elsif deleting then
             raise_application_error( -20101,'Não é permitido excluir: data de fechamento existente.' );
        end if;
end;
	


