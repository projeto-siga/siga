--------------------------------------------------------------------------------------------------
--	SCRIPT: EVITA A DUPLICIDADE DE PESSOAS COM O MESMO Órgão, Cargo, Função, Unidade e CPF.
--------------------------------------------------------------------------------------------------
CREATE INDEX user_index_for_un ON CORPORATIVO.DP_PESSOA (ID_ORGAO_USU, ID_CARGO, ID_FUNCAO_CONFIANCA, ID_LOTACAO, CPF_PESSOA, DATA_FIM_PESSOA);
alter table CORPORATIVO.DP_PESSOA add constraint siga_valid_unique unique (ID_ORGAO_USU, ID_CARGO, ID_FUNCAO_CONFIANCA, ID_LOTACAO, CPF_PESSOA, DATA_FIM_PESSOA) ENABLE NOVALIDATE;