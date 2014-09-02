-------------------------------------------
--	SCRIPT:TAMANHO NUM EXP
-------------------------------------------

alter table SIGA.ex_forma_documento modify (id_forma_doc NUMBER(3,0));

alter table SIGA.ex_modelo modify (id_forma_doc NUMBER(3,0));

drop index SIGA.unique_doc_num_idx;

alter table SIGA.ex_documento modify (id_forma_doc NUMBER(3,0));

create unique index siga.unique_doc_num_idx on siga.ex_documento
(case when id_orgao_usu is null or id_forma_doc is null or ano_emissao is null or num_expediente is null then null else id_orgao_usu end,
 case when id_orgao_usu is null or id_forma_doc is null or ano_emissao is null or num_expediente is null then null else id_forma_doc end,
 case when id_orgao_usu is null or id_forma_doc is null or ano_emissao is null or num_expediente is null then null else ano_emissao end,
 case when id_orgao_usu is null or id_forma_doc is null or ano_emissao is null or num_expediente is null then null else num_expediente end);

alter table SIGA.ex_tp_forma_doc modify (id_forma_doc NUMBER(3,0));
