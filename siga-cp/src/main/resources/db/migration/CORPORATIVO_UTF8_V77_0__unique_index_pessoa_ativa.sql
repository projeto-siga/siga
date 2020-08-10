--------------------------------------------------------------------------------------------------
--	SCRIPT: PERMITE APENAS UMA PESSOA ATIVA POR ID_PESSOA_INICIAL
--------------------------------------------------------------------------------------------------
CREATE UNIQUE INDEX corporativo.dp_pessoa_unique_pessoa_ativa on CORPORATIVO.DP_PESSOA (CASE WHEN DATA_FIM_PESSOA IS NULL THEN ID_PESSOA_INICIAL END);