create table SIGATP.FinalidadeRequisicao (id number(19,0) not null, descricao varchar2(255 char) unique, ID_ORGAO_ORI number(19,0), primary key (id));
create table SIGATP.FinalidadeRequisicao_AUD (id number(19,0) not null, REV number(10,0) not null, REVTYPE number(3,0), descricao varchar2(255 char), ID_ORGAO_ORI number(19,0), primary key (id, REV));

alter table SIGATP.RequisicaoTransporte add ID_FINALIDADE number(19,0);
alter table SIGATP.RequisicaoTransporte_AUD add ID_FINALIDADE number(19,0);

create table SIGATP.requisicao_tipopassageiro (requisicaoTransporte_Id number(19,0) not null, tipoPassageiro varchar2(255 char) not null);
create table SIGATP.requisicao_tipopassageiro_AUD (REV number(10,0) not null, requisicaoTransporte_Id number(19,0) not null, tipoPassageiro varchar2(255 char) not null, REVTYPE number(3,0), primary key (REV, requisicaoTransporte_Id, tipoPassageiro));

alter table SIGATP.FinalidadeRequisicao_AUD add constraint FK54F9930BDF74E053 foreign key (REV) references SIGATP.REVINFO;

alter table SIGATP.RequisicaoTransporte add constraint FK450568A580C02ED3 foreign key (ID_FINALIDADE) references SIGATP.FinalidadeRequisicao;

alter table SIGATP.requisicao_tipopassageiro add constraint FK140B2242AEBDA4DE foreign key (requisicaoTransporte_Id) references SIGATP.RequisicaoTransporte;
alter table SIGATP.requisicao_tipopassageiro_AUD add constraint FKF9785F13DF74E053 foreign key (REV) references SIGATP.REVINFO;

-- Finalidade OUTRA: ID 0, Orgao TRF-2
insert into SIGATP.FinalidadeRequisicao (id, descricao, id_orgao_ori) values (-1, 'OUTRA', 3);

-- Dar grants para usuario SIGATP_CON
grant select, insert, update, delete on SIGATP.FinalidadeRequisicao to SIGATP_CON;
grant select, insert on SIGATP.FinalidadeRequisicao_AUD to SIGATP_CON;

grant select, insert, update, delete on SIGATP.requisicao_tipopassageiro to SIGATP_CON;
grant select, insert on SIGATP.requisicao_tipopassageiro_AUD to SIGATP_CON;



