-- ------------------------------------------------------
--  DDL for Index na DP_PESSOA e IDENTIDADE para evitar Full Table Scan envio email
-- ------------------------------------------------------

CREATE INDEX CP_IDENTIDADE_IDPESSOA ON corporativo.cp_identidade (ID_PESSOA);
CREATE INDEX DP_PESSOA_CPF_ORGAO ON corporativo.dp_pessoa (CPF_PESSOA, ID_ORGAO_USU);
CREATE INDEX DP_PESSOA_ORGAO ON corporativo.dp_pessoa (ID_ORGAO_USU);