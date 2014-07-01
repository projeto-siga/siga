--Cria campos denormalizados para armazenar as informações de controle de acesso

alter table siga.ex_documento add (DNM_DT_ACESSO date null);
alter table siga.ex_documento add (DNM_ACESSO varchar2(256) null);