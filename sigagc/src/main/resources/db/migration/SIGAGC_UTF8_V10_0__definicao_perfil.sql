-- cria movimentação de definição de perfil

ALTER SESSION SET CURRENT_SCHEMA=sigagc;

CREATE TABLE SIGAGC.GC_PAPEL (ID_PAPEL NUMBER(2) PRIMARY KEY, DESC_PAPEL VARCHAR2(40) NOT NULL);
INSERT INTO SIGAGC.GC_PAPEL(2, 'Executor');
INSERT INTO SIGAGC.GC_PAPEL(1, 'Interessado');

insert into sigagc.gc_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (15, 'Definição de Perfil');

