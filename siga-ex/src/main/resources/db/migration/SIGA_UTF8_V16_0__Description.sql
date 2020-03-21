create or replace
Trigger SIGA.EX_DOCUMENTO_BLOCK
Before Update or Delete on SIGA.EX_DOCUMENTO
For Each Row
 declare 
 PRAGMA AUTONOMOUS_TRANSACTION; -- Permite transações externas
 QTD_DOC_VAR  number(10,0):= 0;
 Conteudo_blob_var blob;
        
begin
        if updating then
             if :Old.dt_fechamento is not null 
               and :Old.dt_fechamento <> :New.dt_fechamento 
               then
                 raise_application_error( -20101,'Não é permitido alterar uma DATA de FECHAMENTO já existente' );
             end if; 
                --conteudo_blob_var := empty_blob(); 
             if    dbms_lob.compare( :New.conteudo_blob_doc,empty_blob())<>0 and
                   :Old.fg_eletronico = 'N' and 
                   :Old.dt_fechamento is not null  
               then
                  raise_application_error( -20101,'Não é permitido alterar: não eletrônico com data de fechamento e com conteúdo.' );
             end if;
    
                if dbms_lob.compare( :New.conteudo_blob_doc,empty_blob())<>0 and
                :Old.fg_eletronico = 'S'  --and
                then 
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
                                          (m.id_tp_mov = 11 or m.id_tp_mov = 58) and
                                          m.id_mov_canceladora is null and
                                          m.id_mobil = mb.id_mobil);
                
                if  QTD_DOC_VAR <> 0
                then
                     raise_application_error( -20101,'Não é permitido alterar: eletrônico, com conteúdo, tipo mov. 11 ou 58 e sem mov. canceladora.' );
                end if;
             end if;
                
        elsif deleting then
             if :Old.dt_fechamento is not null then
                 raise_application_error( -20101,'Não é permitido excluir: data de fechamento existente.' );
             end if;
                
        end if;
end;
