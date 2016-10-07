
insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-VER_GESTOR_ITEM', 'Ver gestor do item de configuração ao abrir solicitação', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);
commit;