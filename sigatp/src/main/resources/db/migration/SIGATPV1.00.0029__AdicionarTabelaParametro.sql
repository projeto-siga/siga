 create table SIGATP.Parametro (id number(19,0) not null, ID_ORGAO_USU number(19,0), ID_LOTACAO number(19,0), ID_PESSOA number(19,0), ID_COMPLEXO number(19,0), nomeParametro varchar2(255 char), valorParametro varchar2(255 char), dataInicio timestamp, dataFim timestamp, primary key (id));

 create table SIGATP.Parametro_AUD (id number(19,0) not null, REV number(10,0) not null, REVTYPE number(3,0), ID_ORGAO_USU number(19,0), ID_LOTACAO number(19,0), ID_PESSOA number(19,0), ID_COMPLEXO number(19,0), nomeParametro varchar2(255 char), valorParametro varchar2(255 char), dataInicio timestamp, dataFim timestamp, primary key (id, REV));
     
 alter table SIGATP.Parametro_AUD add constraint FKPARAMETROREV foreign key (REV) references SIGATP.REVINFO;
 
 grant select, insert, update, delete on SIGATP.Parametro to SIGATP_CON;
 grant select, insert on SIGATP.Parametro_AUD to SIGATP_CON;