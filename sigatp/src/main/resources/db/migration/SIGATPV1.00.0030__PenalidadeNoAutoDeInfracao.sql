alter table SIGATP.AutoDeInfracao drop (codigoDaAutuacao, codigoDaPenalidade, descricao, gravidade, enquadramento, quantidadeDePontos);
alter table SIGATP.AutoDeInfracao_AUD drop (codigoDaAutuacao, codigoDaPenalidade, descricao, gravidade, enquadramento, quantidadeDePontos);

alter table SIGATP.AutoDeInfracao add (penalidade_id number(19,0) not null);
alter table SIGATP.AutoDeInfracao_AUD add (penalidade_id number(19,0) not null);

alter table SIGATP.AutoDeInfracao add constraint FK_AUTINF_PEN foreign key (penalidade_id) references SIGATP.Penalidade;