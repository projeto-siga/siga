
ALTER SESSION SET CURRENT_SCHEMA=corporativo;
-- Inserindo a localidade de Campo Grande
-- insert into CORPORATIVO.CP_LOCALIDADE (ID_LOCALIDADE,NM_LOCALIDADE,ID_UF) values (24,'Campo Grande',19);
  
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (27,'Campo Grande',24 , 1);

-- Inserindo servico para notificacao de atendente
insert into corporativo.cp_servico(id_servico, sigla_servico, desc_servico, id_servico_pai, id_tp_servico) 
	values (corporativo.cp_servico_seq.nextval, 'SIGA-SR-EMAILATEND', 'Receber Notificação Atendente', (select id_servico from corporativo.cp_servico where sigla_servico = 'SIGA-SR'), 2);

	
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

update sigasr.sr_tipo_movimentacao set nome_tipo_movimentacao = 'Alteração de Prioridade' where id_tipo_movimentacao = 21;
alter table sigasr.sr_movimentacao add PRIORIDADE NUMBER(10,0);
alter table sigasr.sr_movimentacao drop column ID_PRIORIDADE;