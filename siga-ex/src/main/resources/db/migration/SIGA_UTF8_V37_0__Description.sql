-- Inclui um modelo de documento capturado (https://github.com/projeto-siga/docker/issues/29)
-- necessario chamar o nextval para acertar a numeracao da sequence com os modelos
select SIGA.EX_MODELO_SEQ.nextval from dual; 
Insert into SIGA.EX_MODELO (ID_MOD,NM_MOD,DESC_MOD,conteudo_blob_mod,CONTEUDO_TP_BLOB,ID_FORMA_DOC,HIS_ID_INI,HIS_ATIVO) values (SIGA.EX_MODELO_SEQ.nextval,'Planta','Planta',utl_raw.cast_to_raw(' '),'template/freemarker',(SELECT ID_FORMA_DOC FROM SIGA.EX_FORMA_DOCUMENTO WHERE DESCR_FORMA_DOC = 'Documento Capturado'),SIGA.EX_MODELO_SEQ.currval,'1');

