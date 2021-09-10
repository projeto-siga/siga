/* Remoção de Hash MD5 na CP_ARQUIVO para validar a alteração no documento */

ALTER TABLE CORPORATIVO.CP_ARQUIVO DROP COLUMN HASH_MD5;