-------------------------------------------
--	SCRIPT: CAMPOS CP_ORGAO_USUARIO - Comentários para as novas colunas
--	RESOLUÇÃO DO TRELLO 1390 (Tabela cp_orgao_usuario - incluir campos)
-------------------------------------------
COMMENT ON COLUMN CP_ORGAO_USUARIO.HIS_DT_INI IS 'Data/hora da inserção/ativação do órgão usuário';
COMMENT ON COLUMN CP_ORGAO_USUARIO.HIS_DT_FIM IS 'Data/hora da inativação do órgão usuário';
COMMENT ON COLUMN CP_ORGAO_USUARIO.HIS_IDC_INI IS 'ID do Usuário responsável pela inserção/ativação do órgão usuário';
COMMENT ON COLUMN CP_ORGAO_USUARIO.HIS_IDC_FIM IS 'ID do Usuário responsável pela inativação do órgão usuário';
COMMENT ON COLUMN CP_ORGAO_USUARIO.HIS_ATIVO IS 'Indica se órgão usuário está ativo ou não. 1 = Ativo, 0 = Inativo';