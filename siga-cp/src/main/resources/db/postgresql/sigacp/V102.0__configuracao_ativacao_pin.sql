CREATE INDEX cp_token_id_ref ON corporativo.cp_token (ID_REF);

INSERT INTO corporativo.cp_tipo_token (ID_TP_TOKEN, DESCR_TP_TOKEN) VALUES ('2', 'Token para Reset PIN');
INSERT INTO corporativo.cp_tipo_configuracao (ID_TP_CONFIGURACAO, DSC_TP_CONFIGURACAO,ID_SIT_CONFIGURACAO) VALUES (208, 'PIN como Segundo Fator de Autenticação',2);
