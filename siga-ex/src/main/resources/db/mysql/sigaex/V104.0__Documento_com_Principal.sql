-- -------------------------------------------------------
--  ACRESCENTANDO CAMPOS PARA FAZER O LINK COM O PRINCIPAL
-- -------------------------------------------------------
ALTER TABLE siga.ex_documento
 ADD CD_PRINCIPAL varchar(32) DEFAULT NULL COMMENT 'Código do principal relacionado ao documento, no caso de um procedimento, é algo do tipo JFRJ-WF-2020/000001',
 ADD TP_PRINCIPAL INT UNSIGNED DEFAULT NULL COMMENT 'Tipo do principal do documento';
 