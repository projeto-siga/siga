-- Atentar para a versão do MySQL que suporta o CASE: MySQL 8.0.13 and newer
ALTER TABLE `corporativo`.`dp_pessoa` 
ADD UNIQUE INDEX `DP_PESSOA_UNIQUE_PESSOA_ATIVA` ((CASE WHEN DATA_FIM_PESSOA IS NULL THEN ID_PESSOA_INICIAL END));