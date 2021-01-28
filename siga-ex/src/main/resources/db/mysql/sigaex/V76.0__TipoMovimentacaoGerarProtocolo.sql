insert into siga.ex_tipo_movimentacao (id_tp_mov, DESCR_TIPO_MOVIMENTACAO) values (73, 'Gerar Protocolo');
ALTER TABLE	siga.ex_documento ADD (CHAVE_DOC varchar(10));
ALTER TABLE siga.ex_documento ADD CONSTRAINT unique_chave_doc UNIQUE(chave_doc);