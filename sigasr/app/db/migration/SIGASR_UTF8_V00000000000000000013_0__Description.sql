ALTER SESSION SET CURRENT_SCHEMA = corporativo;
insert into corporativo.cp_marcador (id_marcador, descr_marcador, id_tp_marcador) values (55, 'Agendado', 1);
commit;

ALTER SESSION SET CURRENT_SCHEMA = sigasr;
alter table 
	sr_movimentacao 
add (
	HOR_INI_AGENDAMENTO VARCHAR2(20 BYTE), 
	DT_INI_AGENDAMENTO VARCHAR2(20 BYTE),
	DT_AGENDAMENTO TIMESTAMP (6)
);