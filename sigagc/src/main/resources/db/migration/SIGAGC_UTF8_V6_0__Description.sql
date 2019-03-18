insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (4, 'Ponto de Entrada', NULL);
commit;

insert into sigagc.gc_acesso values (7, 'Lotação e Grupo');
commit;

insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) values (corporativo.cp_servico_seq.nextval, 'SIGA-GC-EDTCLASS', 'Editar Classificação', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-GC'), 2);
commit;