DECLARE
  dest_blob BLOB;
  src_blob BLOB;
  
BEGIN

select conteudo_blob_mod into dest_blob from CORPORATIVO.cp_modelo where his_id_ini = 1 and his_ativo = 1 for update;

src_blob := utl_raw.cast_to_raw(convert('
[#macro complementoHEAD][/#macro]','WE8ISO8859P1'));

dbms_lob.append(dest_blob, src_blob);

commit;
END;
/