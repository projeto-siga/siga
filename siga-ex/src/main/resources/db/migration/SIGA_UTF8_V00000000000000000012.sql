-------------------------------------------
--	SCRIPT:AUTUAR
-------------------------------------------

alter table SIGA.EX_DOCUMENTO add (ID_MOB_AUTUADO NUMBER);

alter table SIGA.EX_DOCUMENTO add constraint SIGA_EXDOC_MOB_AUTUADO_ID_IX
Foreign Key (ID_MOB_AUTUADO) REFERENCES SIGA.EX_MOBIL (ID_MOBIL);

insert into corporativo.cp_tipo_configuracao values(35, 'Autuável', 2);