/* Inclusão de coluna versão para controlar gravação concorrente e criação de tabela para controle transacional dos documentos */

CREATE TABLE "CORPORATIVO"."CP_ARQUIVO_EXCLUIR" (
  CAMINHO VARCHAR2(255) NOT NULL PRIMARY KEY
);

COMMENT ON COLUMN "CORPORATIVO"."CP_ARQUIVO_EXCLUIR"."CAMINHO" IS 'Coluna com o caminho do arquivo binário para exclusão';

GRANT SELECT ON "CORPORATIVO"."CP_ARQUIVO_EXCLUIR" to siga;