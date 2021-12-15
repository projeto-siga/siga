-- Script do Oracle V55 que ampliava o campo para 10 posicoes nao constava para MySQL
--
ALTER TABLE corporativo.dp_pessoa
CHANGE COLUMN `SESB_PESSOA` `SESB_PESSOA` VARCHAR(10);