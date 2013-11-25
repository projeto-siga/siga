
alter table sr_configuracao add ID_EQUIPE_QUALIDADE number(19,0);
alter table sr_configuracao add constraint SR_CONFIGURACAO_EQUIPE_QUALIDADE_FK 
  foreign key (ID_EQUIPE_QUALIDADE) references CORPORATIVO.DP_LOTACAO (ID_LOTACAO);