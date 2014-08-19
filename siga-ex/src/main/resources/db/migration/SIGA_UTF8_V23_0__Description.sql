--Cria campos denormalizados para armazenar as informações de controle de acesso

alter table siga.ex_documento add (DNM_DT_ACESSO date null);
alter table siga.ex_documento add (DNM_ACESSO varchar2(256) null);
alter table siga.ex_documento add (DNM_ID_NIVEL_ACESSO number(10) null);
alter table siga.ex_mobil add (DNM_ULTIMA_ANOTACAO varchar2(400) null);

--	SCRIPT:MOVIMENTACAO DE PENDENCIA DE ANEXO

insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (57, 'Pendência de Anexação');
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (60,'Pendente de Anexação',1);
