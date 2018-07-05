create table SIGATP.Penalidade (id number(19,0) not null, codigoInfracao varchar2(255 char) not null unique, primary key (id));
create table SIGATP.Penalidade_AUD (id number(19,0) not null, REV number(10,0) not null, REVTYPE number(3,0), codigoInfracao varchar2(255 char), primary key (id, REV));

grant select, insert, update, delete on SIGATP.Penalidade to SIGATP_CON;
grant select, insert on SIGATP.Penalidade_AUD to SIGATP_CON;