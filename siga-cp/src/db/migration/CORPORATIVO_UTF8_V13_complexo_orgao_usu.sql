
alter table cp_complexo add (ID_ORGAO_USU NUMBER(10,0));
update cp_complexo set id_orgao_usu = 1
commit;