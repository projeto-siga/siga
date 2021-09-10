-- -----------------------------------------
--	SCRIPT: CAMPOS CP_ORGAO_USUARIO
--	RESOLUÇÃO DO TRELLO 1390 (Tabela cp_orgao_usuario - incluir campos)
-- -----------------------------------------

ALTER TABLE corporativo.cp_orgao_usuario ADD (HIS_DT_INI DATE);
ALTER TABLE corporativo.cp_orgao_usuario ADD (HIS_DT_FIM DATE);
ALTER TABLE corporativo.cp_orgao_usuario ADD (HIS_IDC_INI INT UNSIGNED DEFAULT NULL);
ALTER TABLE corporativo.cp_orgao_usuario ADD (HIS_IDC_FIM INT UNSIGNED DEFAULT NULL);
ALTER TABLE corporativo.cp_orgao_usuario ADD (HIS_ATIVO  tinyint(4) NOT NULL);

alter table corporativo.cp_orgao_usuario add constraint CP_ORG_USU_CP_IDENT_ID_INI_FK foreign key(HIS_IDC_INI) references cp_identidade(ID_IDENTIDADE);
alter table corporativo.cp_orgao_usuario add constraint CP_ORG_USU_CP_IDENT_ID_FIM_FK foreign key(HIS_IDC_FIM) references cp_identidade(ID_IDENTIDADE);
