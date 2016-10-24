-- cria movimentação de definição de perfil

ALTER SESSION SET CURRENT_SCHEMA=sigagc;

CREATE TABLE SIGAGC.GC_PAPEL (ID_PAPEL NUMBER(2) PRIMARY KEY, DESC_PAPEL VARCHAR2(40) NOT NULL);
INSERT INTO SIGAGC.GC_PAPEL(id_papel, desc_papel) values (2, 'Executor');
INSERT INTO SIGAGC.GC_PAPEL(id_papel, desc_papel) values (1, 'Interessado');

alter table sigagc.gc_movimentacao add (ID_PAPEL NUMBER(2), CONSTRAINT MOV_PAPEL_FK FOREIGN KEY(ID_PAPEL) REFERENCES GC_PAPEL(ID_PAPEL));
alter table sigagc.gc_movimentacao add (ID_GRUPO NUMBER, CONSTRAINT MOV_GRUPO_FK FOREIGN KEY(ID_GRUPO) REFERENCES CORPORATIVO.CP_GRUPO(ID_GRUPO));

insert into sigagc.gc_tipo_movimentacao (ID_TIPO_MOVIMENTACAO, NOME_TIPO_MOVIMENTACAO) values (15, 'Definição de Perfil');

