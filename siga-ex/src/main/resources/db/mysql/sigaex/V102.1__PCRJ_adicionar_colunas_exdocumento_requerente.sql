alter table ex_documento  add column CNPJ_REQUERENTE bigint DEFAULT null;
alter table ex_documento  add column CPF_REQUERENTE bigint DEFAULT null;
alter table ex_documento  add column NM_REQUERENTE varchar(60) DEFAULT null;
alter table ex_documento  add column MATRICULA_REQUERENTE int DEFAULT null;
alter table ex_documento  add column TP_LOGRADOURO_REQUERENTE varchar(100) DEFAULT null;
alter table ex_documento  add column LOGRADOURO_REQUERENTE varchar(300) DEFAULT null;
alter table ex_documento  add column NUM_LOGRADOURO_REQUERENTE varchar(100) DEFAULT null;
alter table ex_documento  add column COMPLEMENTO_LOGRADOURO_REQUERENTE varchar(100) DEFAULT null;
alter table ex_documento  add column BAIRRO_REQUERENTE varchar(50) DEFAULT null;
alter table ex_documento  add column CIDADE_REQUERENTE varchar(30) DEFAULT null;
alter table ex_documento  add column UF_REQUERENTE varchar(2) DEFAULT null;
alter table ex_documento  add column CEP_REQUERENTE varchar(8) DEFAULT null;

alter table ex_forma_documento add column IS_PERMITEREQUERENTE tinyint DEFAULT NULL;