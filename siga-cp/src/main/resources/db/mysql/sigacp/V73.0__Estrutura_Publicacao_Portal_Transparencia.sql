-- -----------------------------------------------
--	SCRIPT: CRIACAO DE TABELA DE GERAÇÃO DE TOKENS GENÉRICO
--  Criação preliminar para gerar Token URL permanente: public/app/{TIPO}/{TOKEN}
-- -----------------------------------------------
/* Criação da tabela Tipo de Token */
CREATE TABLE corporativo.cp_tipo_token
(
   ID_TP_TOKEN INT UNSIGNED NOT NULL AUTO_INCREMENT,
   DESCR_TP_TOKEN VARCHAR(30),
   CONSTRAINT CP_TP_TOKEN_PK PRIMARY KEY (ID_TP_TOKEN) 
);

/* Criação do Tipo de Token - Siga Link Permanente */
INSERT INTO corporativo.cp_tipo_token values(1, 'SIGA Link Permanente');

/* Criação da tabela de Token genérica */
CREATE TABLE corporativo.cp_token 
(	ID_TOKEN INT UNSIGNED NOT NULL AUTO_INCREMENT, 
    ID_REF INT UNSIGNED NOT NULL, 
    ID_TP_TOKEN INT UNSIGNED NOT NULL, 
    DT_IAT DATE NOT NULL, 
    DT_EXP DATE, 
    TOKEN VARCHAR(256) NOT NULL,
 	CONSTRAINT CP_TOKEN_PK PRIMARY KEY (ID_TOKEN),
    INDEX CP_TOKEN_TP_IDX (ID_TP_TOKEN, TOKEN), 
 	CONSTRAINT CP_TOKEN_TP_FK FOREIGN KEY (ID_TP_TOKEN) REFERENCES corporativo.cp_tipo_token(ID_TP_TOKEN)
);
