alter table sigatp.abastecimento add ( ID_ORGAO_USU NUMBER(19,0) );

alter table sigatp.abastecimento_AUD add ( ID_ORGAO_USU NUMBER(19,0) );

-- rodar na producao antes de atualizar a app

--update sigatp.abastecimento set ID_ORGAO_USU = 3;
--update sigatp.abastecimento_AUD set ID_ORGAO_USU = 3;
