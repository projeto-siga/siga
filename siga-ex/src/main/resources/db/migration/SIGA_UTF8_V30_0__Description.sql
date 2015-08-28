Insert into SIGA.EX_TIPO_DOCUMENTO (ID_TP_DOC,DESCR_TIPO_DOCUMENTO) values (4,'Capturado');

Insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC,DESCR_FORMA_DOC,SIGLA_FORMA_DOC,ID_TIPO_FORMA_DOC) values (100,'Documento Capturado','CAP',4);

Insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC,ID_TP_DOC) values (100,4);

Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO) values (741,'Nota Fiscal','Nota Fiscal','template/freemarker',null,2005,100,null,null);

Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,CONTEUDO_TP_BLOB,NM_ARQ_MOD,ID_CLASSIFICACAO,ID_FORMA_DOC,ID_CLASS_CRIACAO_VIA,ID_NIVEL_ACESSO,HIS_ID_INI,HIS_DT_INI,HIS_DT_FIM,HIS_IDC_INI,HIS_IDC_FIM,HIS_ATIVO) values (742,'Certificado Negativa de Débitos','Certificado Negativa de Débitos','template/freemarker',null,2005,100,null,null,742,sysdate,null,1,null,1);

update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 741;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 741 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
[@entrevista]
  [@grupo]
    [@texto titulo="Número" var="numero" largura=20 default=numeroValue /]
    [@texto titulo="Empresa" var="empresa" largura=30 /]
    [@data titulo="Data de Vencimento" var="dataVencimento" /]
    [@moeda titulo="Valor total" var="valorTotal" /]
  [/@grupo]
[/@entrevista]
[@descricao]
  <!-- descricao -->
  {NF: ${empresa!}/${numero!} - R$${valorTotal!} - Vencimento: ${dataVencimento!}} <!-- /descricao  -->
[/@descricao]
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);