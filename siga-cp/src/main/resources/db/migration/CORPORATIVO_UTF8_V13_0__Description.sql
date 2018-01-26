-------------------------------------------
--	SCRIPT:COMPLEX ORGAO USU
-------------------------------------------

alter table corporativo.cp_complexo add (ID_ORGAO_USU NUMBER(10,0));
update corporativo.cp_complexo set id_orgao_usu = 9999999999