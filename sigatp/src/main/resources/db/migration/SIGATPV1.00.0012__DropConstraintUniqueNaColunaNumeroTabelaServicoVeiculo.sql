declare
 fName varchar2(255 char);
begin
 SELECT x.constraint_name into fName FROM all_constraints x
 JOIN all_cons_columns c ON
 c.table_name = x.table_name AND c.constraint_name = x.constraint_name
 WHERE x.table_name = 'SERVICOVEICULO'  AND x.CONSTRAINT_TYPE='U' AND c.column_name ='NUMERO';
 IF fName IS NOT NULL THEN
  execute immediate 'alter table SERVICOVEICULO drop constraint "' || fName || '"';
 end if;
end;