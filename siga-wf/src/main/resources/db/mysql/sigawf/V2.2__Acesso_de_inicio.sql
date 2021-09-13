ALTER TABLE `sigawf`.`wf_def_procedimento` 
ADD COLUMN `DEFP_TP_INICIO` VARCHAR(45) NOT NULL DEFAULT 'ACESSO_LOTACAO' COMMENT 'Tipo de permissão para iniciar procedimentos desta definição de procedimento' AFTER `DEFP_TP_EDICAO`;
