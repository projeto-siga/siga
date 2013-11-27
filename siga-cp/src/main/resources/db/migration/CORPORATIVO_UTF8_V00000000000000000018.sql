
--Incluindo CpComplexo na CpConfiguracao
alter table corporativo.cp_configuracao add ID_COMPLEXO number(19,0);
alter table corporativo.CP_COMPLEXO ADD PRIMARY KEY (ID_COMPLEXO);
alter table corporativo.cp_configuracao add constraint CP_CONFIGURACAO_COMPLEXO_FK 
  foreign key (ID_COMPLEXO) references corporativo.CP_COMPLEXO (ID_COMPLEXO);