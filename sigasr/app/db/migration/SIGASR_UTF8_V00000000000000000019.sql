
alter session set current schema = corporativo;

insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-EDTCONH', 'Criar Conhecimentos', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);
commit;

-- OSI_FS0008 - Adicionando campo descrição na designação
ALTER SESSION SET CURRENT_SCHEMA = CORPORATIVO;
ALTER TABLE CORPORATIVO.CP_CONFIGURACAO ADD DESCR_CONFIGURACAO VARCHAR2(255);

