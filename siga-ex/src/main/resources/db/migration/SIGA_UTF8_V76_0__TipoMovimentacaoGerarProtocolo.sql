insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (73, 'Gerar Protocolo');
ALTER TABLE	SIGA.EX_DOCUMENTO ADD (CHAVE_DOC VARCHAR2(10));
ALTER TABLE SIGA.EX_DOCUMENTO
ADD CONSTRAINT unique_chave_doc UNIQUE(chave_doc);