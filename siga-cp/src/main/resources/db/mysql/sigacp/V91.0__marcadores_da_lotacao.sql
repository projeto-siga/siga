-- -----------------------------------------------------------------------------------
-- CRIA COLUNAS, INDICES E SEQUENCE PARA PERMITIR MARCADORES PERSONALIZADOS DA LOTAÇÃO
-- -----------------------------------------------------------------------------------
ALTER TABLE corporativo.cp_marcador ADD ID_LOTACAO_INI INT UNSIGNED COMMENT 'Id inicial da lotação que o marcador pertence (para marcas da lotação)';
ALTER TABLE corporativo.cp_marcador ADD DESCR_DETALHADA VARCHAR(255) COMMENT 'Descrição do marcador';
ALTER TABLE corporativo.cp_marcador ADD ID_COR tinyint(4) COMMENT 'Cor (hexa) do marcador';
ALTER TABLE corporativo.cp_marcador ADD ID_ICONE tinyint(4) COMMENT 'Código do ícone do marcador';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_APLICACAO_MARCADOR tinyint(4) COMMENT 'Id do tipo de mobil em que será aplicado o Marcador';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_DATA_PLANEJADA tinyint(4) COMMENT 'Id do tipo de exibição da data planejada';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_DATA_LIMITE tinyint(4) COMMENT 'Id do tipo de exibição da data limite';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_OPCAO_EXIBICAO tinyint(4) COMMENT 'Id do tipo de exibição do Marcador';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_TEXTO tinyint(4) COMMENT 'Id do tipo de exibição do texto';
ALTER TABLE corporativo.cp_marcador ADD ID_TP_INTERESSADO tinyint(4) COMMENT 'Id do tipo de opção de exibição do interessado';
ALTER TABLE corporativo.cp_marcador ADD HIS_ID_INI INT UNSIGNED COMMENT 'Id do marcador inicial deste marcador';
ALTER TABLE corporativo.cp_marcador ADD HIS_DT_INI DATETIME COMMENT 'Data de criação ou alteração do marcador';
ALTER TABLE corporativo.cp_marcador ADD HIS_IDC_INI INT UNSIGNED COMMENT 'Id da identidade da pessoa que cadastrou o marcador';
ALTER TABLE corporativo.cp_marcador ADD HIS_DT_FIM DATETIME COMMENT 'Data de finalização ou fim de determinada situação do marcador';
ALTER TABLE corporativo.cp_marcador ADD HIS_IDC_FIM INT UNSIGNED COMMENT 'Id da identidade da pessoa que finalizou o marcador';
ALTER TABLE corporativo.cp_marcador ADD HIS_ATIVO tinyint(4) COMMENT 'Identifica se está ativo ou não';

CREATE INDEX CP_MARCADOR_LOTA_ID_INI ON corporativo.cp_marcador (ID_LOTACAO_INI, HIS_DT_FIM);
CREATE INDEX CP_MARCADOR_HIS_ID_INI ON corporativo.cp_marcador (HIS_ID_INI);
 