-------------------------------------------
--	SCRIPT:DESTINACAO
-------------------------------------------

--update siga.ex_tipo_movimentacao set descr_tipo_movimentacao = 'Encerramento' where id_tp_mov = 9;
--update siga.ex_tipo_movimentacao set descr_tipo_movimentacao = 'Reabertura' where id_tp_mov = 21;
update siga.ex_tipo_movimentacao set descr_tipo_movimentacao = 'Encerramento de Volume' where id_tp_mov = 43;
update siga.ex_tipo_movimentacao set descr_tipo_movimentacao = 'Recolhimento ao Arq. Permanente' where id_tp_mov = 19;
update siga.ex_tipo_movimentacao set descr_tipo_movimentacao = 'Eliminação' where id_tp_mov = 10;

ALTER TABLE SIGA.EX_DOCUMENTO RENAME COLUMN DT_FECHAMENTO TO DT_FINALIZACAO;

insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (49, 'Indicação para Guarda Permanente');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (50, 'Reversão de Ind. para Guarda Permanente');

insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (51, 'Reclassificação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (52, 'Avaliação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (53, 'Avaliação com Reclassificação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (54, 'Inclusão em Edital de Eliminação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (55, 'Retirada de Edital de Eliminação');
insert into siga.ex_tipo_movimentacao (id_tp_mov, descr_tipo_movimentacao) values (56, 'Desarquivamento Intermediário');

alter table siga.ex_movimentacao ADD ID_CLASSIFICACAO NUMBER(10,0);
alter table siga.ex_movimentacao add CONSTRAINT MOV_EX_CLASSIFICACAO_FK FOREIGN KEY (ID_CLASSIFICACAO)  REFERENCES SIGA.EX_CLASSIFICACAO (ID_CLASSIFICACAO);


update SIGA.ex_temporalidade set valor_temporalidade = 1, id_unidade_medida = 1 where desc_temporalidade = '1 ano';
update SIGA.ex_temporalidade set valor_temporalidade = 1, id_unidade_medida = 1 where desc_temporalidade = '1 ano após a atualização';
update SIGA.ex_temporalidade set valor_temporalidade = 10, id_unidade_medida = 1 where desc_temporalidade = '10 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 15, id_unidade_medida = 1 where desc_temporalidade = '15 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 2, id_unidade_medida = 1 where desc_temporalidade = '2 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 2, id_unidade_medida = 1 where desc_temporalidade = '2 anos após a devolução do documento / processo';
update SIGA.ex_temporalidade set valor_temporalidade = 2, id_unidade_medida = 1 where desc_temporalidade = '2 anos após o encerramento';
update SIGA.ex_temporalidade set valor_temporalidade = 2, id_unidade_medida = 1 where desc_temporalidade = '2 anos após o encerramento com devolução';
update SIGA.ex_temporalidade set valor_temporalidade = 20, id_unidade_medida = 1 where desc_temporalidade = '20 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 3, id_unidade_medida = 1 where desc_temporalidade = '3 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 3, id_unidade_medida = 1 where desc_temporalidade = '3 anos após o encerramento';
update SIGA.ex_temporalidade set valor_temporalidade = 30, id_unidade_medida = 3 where desc_temporalidade = '30 dias';
update SIGA.ex_temporalidade set valor_temporalidade = 35, id_unidade_medida = 1 where desc_temporalidade = '35 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 4, id_unidade_medida = 1 where desc_temporalidade = '4 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 5, id_unidade_medida = 1 where desc_temporalidade = '5 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 5, id_unidade_medida = 1 where desc_temporalidade = '5 anos após o encerramento';
update SIGA.ex_temporalidade set valor_temporalidade = 50, id_unidade_medida = 1 where desc_temporalidade = '50 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 51, id_unidade_medida = 1 where desc_temporalidade = '51 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 6, id_unidade_medida = 1 where desc_temporalidade = '6 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 7, id_unidade_medida = 1 where desc_temporalidade = '7 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 71, id_unidade_medida = 1 where desc_temporalidade = '71 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 90, id_unidade_medida = 3 where desc_temporalidade = '90 dias';
update SIGA.ex_temporalidade set valor_temporalidade = 95, id_unidade_medida = 1 where desc_temporalidade = '95 anos';
update SIGA.ex_temporalidade set valor_temporalidade = 6, id_unidade_medida = 1 where desc_temporalidade = 'Até vigência + 6 anos';
