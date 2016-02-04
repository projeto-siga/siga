alter table sigasr.sr_configuracao add ID_EQUIPE_QUALIDADE number(19,0);
alter session set current_schema=corporativo;

GRANT REFERENCES ON CORPORATIVO.DP_LOTACAO TO SIGASR;

alter table sigasr.sr_configuracao add constraint SR_CONFIG_EQUIPE_QUALIDADE_FK 
  foreign key (ID_EQUIPE_QUALIDADE) references CORPORATIVO.DP_LOTACAO (ID_LOTACAO);
  

