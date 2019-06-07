------------------------------------------
--	SCRIPT:CORPORATIVO_CONFIGURACAO_IDS_OBJETO
-------------------------------------------
ALTER TABLE CORPORATIVO.cp_configuracao ADD (ID_LOTACAO_OBJETO number(19,0));
ALTER TABLE CORPORATIVO.cp_configuracao ADD (ID_COMPLEXO_OBJETO number(19,0));
ALTER TABLE CORPORATIVO.cp_configuracao ADD (ID_CARGO_OBJETO number(19,0));
ALTER TABLE CORPORATIVO.cp_configuracao ADD (ID_FUNCAO_CONFIANCA_OBJETO number(19,0));
ALTER TABLE CORPORATIVO.cp_configuracao ADD (ID_PESSOA_OBJETO number(19,0));
