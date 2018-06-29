 create table SIGATP.ControleGabinete (id number(19,0) not null, dataHora timestamp, dataHoraSaida timestamp, dataHoraRetorno timestamp, itinerario varchar2(255 char), odometroEmKmRetorno double precision not null, odometroEmKmSaida double precision not null, condutor_id number(19,0) not null, responsavel_ID_PESSOA number(19,0) not null, veiculo_id number(19,0) not null, primary key (id));

 alter table SIGATP.ControleGabinete add constraint FK3222637118C04BB6 foreign key (veiculo_id) references SIGATP.Veiculo;
 
 alter table SIGATP.ControleGabinete add constraint FK322263713BF35A5E foreign key (condutor_id) references SIGATP.Condutor;
 
 create table SIGATP.ControleGabinete_AUD (id number(19,0) not null, REV number(10,0) not null, REVTYPE number(3,0), dataHora timestamp, dataHoraSaida timestamp, dataHoraRetorno timestamp, itinerario varchar2(255 char), odometroEmKmRetorno double precision, odometroEmKmSaida double precision, condutor_id number(19,0), responsavel_ID_PESSOA number(19,0), veiculo_id number(19,0), primary key (id, REV));
     
 alter table SIGATP.ControleGabinete_AUD add constraint FKCA9D70C2DF74E053 foreign key (REV) references SIGATP.REVINFO;
 
 grant select, insert, update, delete on SIGATP.ControleGabinete to SIGATP_CON;
 grant select, insert on SIGATP.ControleGabinete_AUD to SIGATP_CON;