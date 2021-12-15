--------------------------------------------------------
--  TABELA EX_TIPO_SEQUENCIA
--------------------------------------------------------
CREATE TABLE SIGA.EX_TIPO_SEQUENCIA (
    ID_TIPO_SEQ NUMBER(15,0),
    NOME VARCHAR2(255) NOT NULL,
    ZERAR_INICIO_ANO CHAR(1 BYTE) DEFAULT 1
);

--------------------------------------------------------
--  SEQUENCE EX_TIPO_SEQUENCIA_SEQ
--------------------------------------------------------
CREATE SEQUENCE  SIGA.EX_TIPO_SEQUENCIA_SEQ  MINVALUE 1 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;   

--------------------------------------------------------
--  INDICE EX_TIPO_SEQUENCIA_PK
--------------------------------------------------------
CREATE UNIQUE INDEX SIGA.EX_TIPO_SEQUENCIA_PK ON SIGA.EX_TIPO_SEQUENCIA (ID_TIPO_SEQ);

--------------------------------------------------------
--  Constraints for Table EX_TIPO_SEQUENCIA
--------------------------------------------------------
ALTER TABLE SIGA.EX_TIPO_SEQUENCIA ADD CONSTRAINT EX_TIPO_SEQUENCIA_PK PRIMARY KEY (ID_TIPO_SEQ);