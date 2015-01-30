alter session set current schema = corporativo;

insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-EDTCONH', 'Criar Conhecimentos', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);
commit;

alter session set current schema = sigasr;

alter table SR_SOLICITACAO ADD (
  "ID_DESIGNACAO" NUMBER(19,0),
  FOREIGN KEY ("ID_DESIGNACAO")
	  REFERENCES "SIGASR"."SR_CONFIGURACAO" ("ID_CONFIGURACAO_SR") ENABLE
);
alter table SR_MOVIMENTACAO ADD (
  "ID_DESIGNACAO" NUMBER(19,0),
  FOREIGN KEY ("ID_DESIGNACAO")
	  REFERENCES "SIGASR"."SR_CONFIGURACAO" ("ID_CONFIGURACAO_SR") ENABLE
);