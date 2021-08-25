--------------------------------------------------------
--  TABELA EX_TIPO_SEQUENCIA
--------------------------------------------------------
CREATE TABLE `ex_tipo_sequencia` (
    `ID_TIPO_SEQ` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `NOME` VARCHAR(255) NOT NULL,
    `ZERAR_INICIO_ANO` CHAR(1) DEFAULT 1,
    CONSTRAINT EX_TIPO_SEQUENCIA_PK PRIMARY KEY (`ID_TIPO_SEQ`)
);