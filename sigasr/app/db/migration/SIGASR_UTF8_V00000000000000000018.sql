--adicionando prioridade a solicitacao
alter table sigasr.sr_solicitacao add PRIORIDADE number(10,0);

alter session set current schema = corporativo;
update corporativo.cp_marcador set descr_marcador = 'A Fechar' where id_marcador = 53;