--adicionando prioridade a solicitacao
alter table sigasr.sr_solicitacao add PRIORIDADE number(10,0);


alter session set current schema = corporativo;
update corporativo.cp_marcador set descr_marcador = 'A Fechar' where id_marcador = 53;


INSERT INTO corporativo.cp_tipo_configuracao (id_tp_configuracao, dsc_tp_configuracao, id_sit_configuracao) 
	VALUES (303, 'Inclusão automática em lista', 1);
  
ALTER TABLE SR_ATRIBUTO RENAME TO SR_ATRIBUTO_SOLICITACAO;
ALTER TABLE SR_TIPO_ATRIBUTO RENAME TO SR_ATRIBUTO;

ALTER TABLE SR_ATRIBUTO_SOLICITACAO RENAME COLUMN ID_ATRIBUTO TO ID_ATRIBUTO_SOLICITACAO;
ALTER TABLE SR_ATRIBUTO_SOLICITACAO RENAME COLUMN VALOR_ATRIBUTO TO VALOR_ATRIBUTO_SOLICITACAO;
ALTER TABLE SR_ATRIBUTO_SOLICITACAO RENAME COLUMN ID_TIPO_ATRIBUTO TO ID_ATRIBUTO;

ALTER TABLE SR_ATRIBUTO RENAME COLUMN ID_TIPO_ATRIBUTO TO ID_ATRIBUTO;
ALTER TABLE SR_ATRIBUTO RENAME COLUMN FORMATOCAMPO TO TIPO_ATRIBUTO;

rename SR_ATRIBUTO_SEQ to SR_ATRIBUTO_SOLICITACAO_SEQ;

--adicionando coluna para registro se a solicitacao pai deve ser fechada automaticamente quando todas as filhas forem fechadas
alter table sigasr.sr_solicitacao add FECHADO_AUTOMATICAMENTE CHAR(1 CHAR);


