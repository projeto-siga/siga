
--Incluindo CpComplexo na CpConfiguracao
alter table CP_COMPLEXO ADD PRIMARY KEY (ID_COMPLEXO);
alter table cp_configuracao add ID_COMPLEXO number(19,0);
alter table cp_configuracao add constraint CP_CONFIGURACAO_COMPLEXO_FK 
  foreign key (ID_COMPLEXO) references CP_COMPLEXO (ID_COMPLEXO);