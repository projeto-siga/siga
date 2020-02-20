--------------------------------------------------------------------------
--	SCRIPT: Novos campos de auditoria
--------------------------------------------------------------------------

alter table siga.ex_movimentacao add (ID_IDENTIDADE_AUDIT NUMBER(10,0) null);
alter table siga.ex_movimentacao add constraint MOV_CP_IDENTIDADE_FK foreign key (ID_IDENTIDADE_AUDIT) references CORPORATIVO.CP_IDENTIDADE (ID_IDENTIDADE);
alter table siga.ex_movimentacao add  (IP_AUDIT varchar2(20) null);
alter table siga.ex_movimentacao add  (HASH_AUDIT varchar2(1024) null);