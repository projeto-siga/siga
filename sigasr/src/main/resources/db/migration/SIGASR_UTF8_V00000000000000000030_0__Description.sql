
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-OPENPRIOR', 'Priorizar ao Abrir', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);