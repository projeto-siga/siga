ALTER TABLE `sigawf`.`wf_def_procedimento` 
ADD COLUMN `DEFP_TP_EDICAO` VARCHAR(45) NOT NULL DEFAULT 'ACESSO_LOTACAO' COMMENT 'Tipo de permissão para edição desta definição de procedimento' AFTER `DEFP_NR`;

ALTER TABLE `sigawf`.`wf_def_procedimento` 
CHANGE COLUMN `ORGU_ID` `ORGU_ID` INT NULL DEFAULT NULL COMMENT 'Identificador do órgao usuário ao qual se vincula a definição de procedimento';

ALTER TABLE `sigawf`.`wf_def_procedimento` 
ADD COLUMN `GRUP_ID_EDICAO` INT NULL DEFAULT NULL COMMENT 'Referência ao grupo de pessoas/lotações com permissão de edição' AFTER `DEFP_TP_EDICAO`;

-- ALTER TABLE `sigawf`.`wf_def_procedimento` 
-- ADD CONSTRAINT `FK_DEFP_GROUP_ID_EDICAO`
--   FOREIGN KEY (`GRUP_ID_EDICAO`)
--   REFERENCES `corporativo`.`cp_grupo` (`ID_GRUPO`),
-- ADD CONSTRAINT `FK_DEFP_ORGU_ID`
--   FOREIGN KEY (`ORGU_ID`)
--   REFERENCES `corporativo`.`cp_orgao_usuario` (`ID_ORGAO_USU`);