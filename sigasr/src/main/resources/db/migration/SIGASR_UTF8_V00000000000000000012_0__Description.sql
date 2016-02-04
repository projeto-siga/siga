
ALTER SESSION SET CURRENT_SCHEMA = corporativo;
insert into corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) values (302, 'Usar Lista', 2);
commit;

ALTER SESSION SET CURRENT_SCHEMA = sigasr;
alter table sigasr.sr_configuracao add 
(
	ID_LISTA				NUMBER(19,0),
	CONSTRAINT SR_CONF_LISTA_FK FOREIGN KEY (ID_LISTA) REFERENCES SIGASR.SR_LISTA(ID_LISTA)
);