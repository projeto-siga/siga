/* Inclusão de coluna versão para controlar gravação concorrente e criação de tabela para controle transacional dos documentos */

CREATE TABLE corporativo.cp_arquivo_excluir (
  CAMINHO varchar(255) NOT NULL COMMENT 'Coluna com o caminho do arquivo binário para exclusão',
  PRIMARY KEY (`CAMINHO`),
  UNIQUE KEY cp_arq_excluir_caminho_pk (CAMINHO)
);
