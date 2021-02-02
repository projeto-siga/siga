ALTER TABLE `sigawf`.`wf_def_procedimento` 
ADD COLUMN `PESS_ID_RESP` BIGINT NULL DEFAULT NULL COMMENT 'Pessoa responsável pela definição de procedimento' AFTER `DEFP_TP_EDICAO`,
ADD COLUMN `LOTA_ID_RESP` BIGINT NULL DEFAULT NULL COMMENT 'Lotação responsável pela definição de procedimento' AFTER `PESS_ID_RESP`;
