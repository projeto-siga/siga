/* Permissões para alterar nome e sigla da lotação após cadastro de documento */

ALTER TABLE corporativo.dp_lotacao ADD (HIS_IDC_INI INT UNSIGNED COMMENT 'Id identidade da pessoa que cadastrou');
ALTER TABLE corporativo.dp_lotacao ADD (HIS_IDC_FIM INT UNSIGNED COMMENT 'Id identidade da pessoa que finalizou');