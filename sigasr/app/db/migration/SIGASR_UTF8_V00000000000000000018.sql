--adicionando prioridade a solicitacao
alter table sigasr.sr_solicitacao add PRIORIDADE number(10,0);

INSERT INTO corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) 
	VALUES (303, 'Inclusão automática em lista', 1);
  