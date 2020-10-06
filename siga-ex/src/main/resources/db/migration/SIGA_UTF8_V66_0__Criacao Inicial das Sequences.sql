/** SOLUÇÃO NÃO IMPLEMENTADA DEVIDO A COMPORTAMENTO NÃO ADEQUADO EM ORACLE RAC.
-------------------------------------------
--	SCRIPT:Readequação da Estrutura de Numeração do Documento
-------------------------------------------
  ALTER SESSION SET CURRENT_SCHEMA=siga;
--------------------------------------------------------
  
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

-- CREATE ALL SEQUENCES NUMERACAO DOCUMENTO 
begin
  for doc in (SELECT org.id_orgao_usu ,
                       frm.id_forma_doc ,
                       doc.ano_emissao ,
                       Max(doc.num_expediente) + 1 starwith
                FROM ex_documento doc
                INNER JOIN ex_forma_documento frm ON doc.id_forma_doc = frm.id_forma_doc
                INNER JOIN corporativo.cp_orgao_usuario org ON doc.id_orgao_usu = org.id_orgao_usu
                WHERE doc.num_expediente IS NOT NULL
                GROUP BY org.id_orgao_usu,
                         frm.id_forma_doc,
                         doc.ano_emissao)
  loop
      CREATE_SQ_NUMERACAO_DOCUMENTO(
         PID_ORGAO_USU => to_char(doc.id_orgao_usu),
         PID_FORMA_DOC => to_char(doc.id_forma_doc),
         PANO_EMISSAO => to_char(doc.ano_emissao),
         PNUMINICIAL => to_char(doc.starwith)
      );

  end loop;             
end;
/
**/