ALTER TABLE sigawf.wf_def_procedimento ADD DEFP_TP_EDICAO VARCHAR(45) DEFAULT 'ACESSO_LOTACAO' NOT NULL;
COMMENT ON COLUMN sigawf.wf_def_procedimento.DEFP_TP_EDICAO IS 'Tipo de permissão para edição desta definição de procedimento';

--Foi alterado de bigint para int, não foi necessário alterar no Oracle
--ALTER TABLE `sigawf`.`wf_def_procedimento` 
--CHANGE COLUMN `ORGU_ID` `ORGU_ID` INT NULL DEFAULT NULL COMMENT 'Identificador do órgao usuário ao qual se vincula a definição de procedimento';

ALTER TABLE sigawf.wf_def_procedimento ADD GRUP_ID_EDICAO NUMBER(8) NULL;
COMMENT ON COLUMN sigawf.wf_def_procedimento.GRUP_ID_EDICAO IS 'Referência ao grupo de pessoas/lotações com permissão de edição';

-- ALTER TABLE `sigawf`.`wf_def_procedimento` 
-- ADD CONSTRAINT `FK_DEFP_GROUP_ID_EDICAO`
--   FOREIGN KEY (`GRUP_ID_EDICAO`)
--   REFERENCES `corporativo`.`cp_grupo` (`ID_GRUPO`),
-- ADD CONSTRAINT `FK_DEFP_ORGU_ID`
--   FOREIGN KEY (`ORGU_ID`)
--   REFERENCES `corporativo`.`cp_orgao_usuario` (`ID_ORGAO_USU`);