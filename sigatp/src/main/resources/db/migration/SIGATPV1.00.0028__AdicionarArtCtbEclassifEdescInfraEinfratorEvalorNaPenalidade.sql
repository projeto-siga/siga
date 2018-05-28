alter table SIGATP.Penalidade add (artigoCTB varchar2(255 char) not null);
alter table SIGATP.Penalidade add (classificacao varchar2(255 char) not null);
alter table SIGATP.Penalidade add (descricaoInfracao varchar2(255 char) not null);
alter table SIGATP.Penalidade add (infrator varchar2(255 char) not null);
alter table SIGATP.Penalidade add (valor double precision not null);

alter table SIGATP.Penalidade_AUD add (artigoCTB varchar2(255 char));
alter table SIGATP.Penalidade_AUD add (classificacao varchar2(255 char));
alter table SIGATP.Penalidade_AUD add (descricaoInfracao varchar2(255 char));
alter table SIGATP.Penalidade_AUD add (infrator varchar2(255 char));
alter table SIGATP.Penalidade_AUD add (valor double precision);