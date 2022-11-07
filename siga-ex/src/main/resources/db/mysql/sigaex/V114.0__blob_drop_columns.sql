-- -------------------------------------------------------------------------    
--  Remove as colunas de blobs
-- -------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS tmpproc;
DELIMITER //
CREATE PROCEDURE tmpproc ()
BEGIN
	IF (select count(*) from `siga`.`ex_documento` where CONTEUDO_BLOB_DOC is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_DOCUMENTO';
	END IF;
	IF (select count(*) from `siga`.`ex_movimentacao` where CONTEUDO_BLOB_MOV is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_MOVIMENTACAO';
	END IF;
	IF (select count(*) from `siga`.`ex_modelo` where CONTEUDO_BLOB_MOD is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_MODELO';
	END IF;
	IF (select count(*) from `siga`.`ex_preenchimento` where PREENCHIMENTO_BLOB is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_PREENCHIMENTO';
	END IF;
END  //
DELIMITER ;
call tmpproc();
DROP PROCEDURE tmpproc;

ALTER TABLE `siga`.`ex_documento` 
DROP COLUMN `CONTEUDO_TP_DOC`,
DROP COLUMN `CONTEUDO_BLOB_DOC`;

ALTER TABLE `siga`.`ex_movimentacao` 
DROP COLUMN `CONTEUDO_TP_MOV`,
DROP COLUMN `CONTEUDO_BLOB_MOV`;

ALTER TABLE `siga`.`ex_modelo` 
DROP COLUMN `CONTEUDO_TP_BLOB`,
DROP COLUMN `CONTEUDO_BLOB_MOD`;

ALTER TABLE `siga`.`ex_preenchimento` 
DROP COLUMN `PREENCHIMENTO_BLOB`;