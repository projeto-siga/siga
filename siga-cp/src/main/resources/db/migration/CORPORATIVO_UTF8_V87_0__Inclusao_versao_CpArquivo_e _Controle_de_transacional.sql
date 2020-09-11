/* Inclusão de coluna versão para controlar gravação concorrente e criação de tabela para controle transacional dos documentos */

alter table corporativo.cp_arquivo add versao number default 1 not null;
COMMENT ON COLUMN "CORPORATIVO"."CP_ARQUIVO"."VERSAO" IS 'Coluna de versão para uso do Lock Otimista do JPA';

create table corporativo.CP_ARQUIVO_EXCLUIR (
  CAMINHO VARCHAR2(255) NOT NULL PRIMARY KEY
);