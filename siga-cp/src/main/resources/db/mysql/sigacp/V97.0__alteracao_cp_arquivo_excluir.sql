use corporativo;

DROP TABLE IF EXISTS cp_arquivo_excluir2;

CREATE TABLE cp_arquivo_excluir2 AS SELECT * FROM cp_arquivo_excluir;

DROP TABLE cp_arquivo_excluir;

/* Tabela para registrar os arquivos a serem excluidos no servidor de aquivos e permitir um controle transacional caso haja um rollback da transação de banco */
CREATE TABLE cp_arquivo_excluir (
  `ID_ARQ_EXC` BIGINT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `CAMINHO` varchar(255) NOT NULL COMMENT 'Coluna com o caminho do arquivo binário para exclusão',
  PRIMARY KEY (`ID_ARQ_EXC`),
  UNIQUE KEY CP_ARQ_EXCLUIR_ID_ARQ_EXC_PK (ID_ARQ_EXC)
)
ENGINE=InnoDB;

INSERT INTO cp_arquivo_excluir (CAMINHO) SELECT * FROM cp_arquivo_excluir2;

DROP TABLE cp_arquivo_excluir2;

ALTER TABLE cp_arquivo_excluir CHANGE COLUMN `ID_ARQ_EXC` `ID_ARQ_EXC` BIGINT UNSIGNED   NOT NULL;