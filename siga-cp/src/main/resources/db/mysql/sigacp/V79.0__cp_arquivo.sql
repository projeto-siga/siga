ALTER TABLE corporativo.cp_arquivo ADD TP_ARMAZENAMENTO VARCHAR(20) COMMENT 'Content-Type do arquivo';
ALTER TABLE corporativo.cp_arquivo ADD CAMINHO VARCHAR(255) COMMENT 'Caminho do arquivo no mecanismo de armazenamento';
ALTER TABLE corporativo.cp_arquivo ADD TAMANHO_ARQ INT UNSIGNED NOT NULL COMMENT 'Tamanho do arquivo em bytes';
