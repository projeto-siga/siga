--Alterações estruturais
/*
create table EX_TIPO_MOBIL (
  ID_TIPO_MOBIL    number,
  DESC_TIPO_MOBIL varchar2(20),
  primary key (ID_TIPO_MOBIL)
);


create table EX_MOBIL (
  ID_MOBIL    number not null,
  ID_DOC number not null,
  ID_TIPO_MOBIL number not null,
  NUM_SEQUENCIA number(2) not null,
  primary key (ID_MOBIL),
  foreign key (ID_DOC) references EX_DOCUMENTO(ID_DOC),
  foreign key (ID_TIPO_MOBIL) references EX_TIPO_MOBIL(ID_TIPO_MOBIL)
);

create table EX_TIPO_FORMA_DOCUMENTO (
  ID_TIPO_FORMA_DOC    number,
  DESC_TIPO_FORMA_DOC varchar2(60),
  NUMERACAO_UNICA number(1),
  primary key (ID_TIPO_FORMA_DOC)
);


ALTER TABLE EX_MOVIMENTACAO ADD ID_MOBIL number null;
ALTER TABLE EX_MOVIMENTACAO ADD foreign key (ID_MOBIL) references EX_MOBIL(ID_MOBIL);

*/
ALTER TABLE EX_MOVIMENTACAO ADD ID_MOB_REF number null;
ALTER TABLE EX_MOVIMENTACAO ADD foreign key (ID_MOB_REF) references EX_MOBIL(ID_MOBIL);
/*
ALTER TABLE EX_MOVIMENTACAO ADD ID_MOB_PAI number null;
ALTER TABLE EX_MOVIMENTACAO ADD foreign key (ID_MOB_PAI) references EX_MOBIL(ID_MOBIL);
ALTER TABLE EX_MOVIMENTACAO ADD ID_MOB_MESTRE number null;
ALTER TABLE EX_MOVIMENTACAO ADD foreign key (ID_MOB_MESTRE) references EX_MOBIL(ID_MOBIL);
*/
ALTER TABLE EX_MOVIMENTACAO MODIFY ID_DOC number null;
ALTER TABLE EX_MOVIMENTACAO MODIFY ID_ESTADO_DOC number null;
ALTER TABLE EX_MOVIMENTACAO ADD NUM_PAGINAS number(4) null;
ALTER TABLE EX_MOVIMENTACAO ADD NUM_PAGINAS_ORI number(4) null;
--ALTER TABLE EX_FORMA_DOCUMENTO ADD ID_TIPO_FORMA_DOC number null;
--ALTER TABLE EX_FORMA_DOCUMENTO MODIFY ID_TIPO_FORMA_DOC number not null;
--ALTER TABLE EX_FORMA_DOCUMENTO ADD foreign key (ID_TIPO_FORMA_DOC) references EX_TIPO_FORMA_DOCUMENTO(ID_TIPO_FORMA_DOC);

ALTER TABLE EX_DOCUMENTO ADD ID_MOB_PAI number null;
ALTER TABLE EX_DOCUMENTO ADD foreign key (ID_MOB_PAI) references EX_MOBIL(ID_MOBIL);

ALTER TABLE EX_DOCUMENTO ADD NUM_SEQUENCIA number(2) null;
ALTER TABLE EX_DOCUMENTO ADD NUM_PAGINAS number(4) null;

CREATE SEQUENCE EX_MOBIL_SEQ;

CREATE INDEX id_mobil_idx ON ex_movimentacao(id_mobil);
CREATE INDEX id_mobil_ref_idx ON ex_movimentacao(id_mob_ref);
/*
CREATE INDEX id_mobil_pai_idx ON ex_movimentacao(id_mob_pai);
CREATE INDEX id_mobil_mestre_idx ON ex_movimentacao(id_mob_mestre);
*/
CREATE INDEX id_doc_idx ON ex_mobil(id_doc);

DROP INDEX PRIMEIRA_PAGINA;
DROP INDEX PRIMEIRA_PAGINA_L;
DROP INDEX PRIMEIRA_PAGINA_2;
DROP INDEX PRIMEIRA_PAGINA_2L;
DROP INDEX PRIMEIRA_PAGINA_3;
DROP INDEX PRIMEIRA_PAGINA_3L;
CREATE INDEX MOVIMENTACAO_LOTA_RESP_E_DATA  ON EX_MOVIMENTACAO (ID_LOTA_RESP, DT_INI_MOV) COMPUTE STATISTICS;

--CREATE INDEX id_mob_pai_idx ON ex_documento(id_mob_pai);
CREATE INDEX id_mov_ref_idx ON ex_movimentacao(id_mov_ref);


--Inserção de dados
/*
insert into EX_TIPO_MOBIL values(1, 'Geral');
insert into EX_TIPO_MOBIL values(2, 'Via');
insert into EX_TIPO_MOBIL values(3, 'Cópia');
insert into EX_TIPO_MOBIL values(4, 'Volume');

insert into EX_TIPO_FORMA_DOCUMENTO values(1, 'Expediente',0);
insert into EX_TIPO_FORMA_DOCUMENTO values(2, 'Processo Administrativo',1);
*/

--update EX_FORMA_DOCUMENTO set ID_TIPO_FORMA_DOC = 1;

--insert into EX_FORMA_DOCUMENTO(id_forma_doc, id_tipo_forma_doc, descr_forma_doc, sigla_forma_doc)  values (55, 2, 'Processo Administrativo','ADM');
--insert into EX_FORMA_DOCUMENTO(id_forma_doc, id_tipo_forma_doc, descr_forma_doc, sigla_forma_doc)  values (56, 2, 'Processo de Pessoal','RHU');
--insert into EX_FORMA_DOCUMENTO(id_forma_doc, id_tipo_forma_doc, descr_forma_doc, sigla_forma_doc)  values (57, 2, 'Processo de Execução Orçamentária e Financeira','EOF');
--insert into EX_FORMA_DOCUMENTO(id_forma_doc, id_tipo_forma_doc, descr_forma_doc, sigla_forma_doc)  values (58, 2, 'Processo do Conselho Consultivo','CCO');
/* inseri tipo Pauta */
insert into SIGA.EX_FORMA_DOCUMENTO (ID_FORMA_DOC, DESCR_FORMA_DOC, SIGLA_FORMA_DOC, ID_TIPO_FORMA_DOC) values (63,'Pauta','PTA',1);

insert into EX_TP_FORMA_DOC values(55,1);
insert into EX_TP_FORMA_DOC values(56,1);
insert into EX_TP_FORMA_DOC values(57,1);
insert into EX_TP_FORMA_DOC values(58,1);
insert into EX_TP_FORMA_DOC values(55,2);
insert into EX_TP_FORMA_DOC values(56,2);
insert into EX_TP_FORMA_DOC values(57,2);
insert into EX_TP_FORMA_DOC values(58,2);
/*inserindo tipo Pauta na tabela EX_TP_FORMA_DOC*/
insert into SIGA.EX_TP_FORMA_DOC (ID_FORMA_DOC, ID_TP_DOC) values (63, 1);

insert into EX_MODELO values(514, 'Processo Administrativo', null, null, null, 'processoAdministrativo.jsp', null, 55, null, null);
/* inserindo modelo Pauta */
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (536,'Pauta','pauta.jsp',63);
/*inserindo parecer*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_CLASSIFICACAO, ID_FORMA_DOC) values (519,'Parecer','parecer.jsp',171,14);
/*inserindo Carta de Intimação*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (520,'Carta de intimação','cartaIntimacao.jsp',11);
/*inserindo Mandado*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_CLASSIFICACAO, ID_FORMA_DOC) values (522,'Mandado','mandado.jsp',1368,44);
/*inserindo Solicitação de fornecimento registro de preços*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (543,'Solicitação de fornecimento registro de preços','solicitacaoFornRegPrecos.jsp',13);
/*inserindo Registro de preços */
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (544,'Registro de preços','registroPrecos.jsp',3);
/*inserindo Certidão de encerramendo de volume*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (545,'Certidão de encerramendo de volume','certidaoEncerramentoVolume.jsp',15);
/*inserindo Folha inicial de volume - EOF*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (546,'Folha inicial de volume - EOF','folhaInicialVolume.jsp',3);
/*inserindo Atesto*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (547,'Atesto','atesto.jsp',3);
/*inserindo Juntada */
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (528,'Juntada','juntada.jsp',3);
/*inserindo Certidão de desentranhamento*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (529,'Certidão de desentranhamento','certidaoDesentranhamento.jsp',15 );
/*inserindo Certidão*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (532,'Certidão','certidaoGeral.jsp',15 );
/*inserindo Processo de Pessoal*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (533,'Processo de Pessoal','processoAdministrativo.jsp',56 );
/*inserindo Processo de Execução Orçamentária Financeira*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (534,'Processo de Execução Orçamentária Financeira','processoAdministrativo.jsp',57 );
/*inserindo Processo do Conselho Consultivo*/
insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_FORMA_DOC) values (535,'Processo do Conselho Consultivo','processoAdministrativo.jsp',58 );

insert into EX_TIPO_MOVIMENTACAO values(41,'Apensação');
insert into EX_TIPO_MOVIMENTACAO values(42,'Desapensação');

insert into EX_TIPO_MOVIMENTACAO values(43,'Encerramento');

insert into SIGA.EX_TIPO_CONFIGURACAO (ID_TP_CONFIGURACAO, DSC_TP_CONFIGURACAO, ID_SIT_CONFIGURACAO) values (29, 'Cancelar Movimentação', 2);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 2, sysdate, 1, 29);

insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 5, sysdate, 1, 29);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 6, sysdate, 1, 29);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 7, sysdate, 1, 29);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 8, sysdate, 1, 29);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_MOD, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao),529,2,2);
insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_MOD, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao),545,2,2);

/*
 * Script que converte a base de dados pré_mobil para a versão com mobil
 * 
 */

DROP TABLE LOG_CONVERSAO_MOBIL;
DROP SEQUENCE SEQ_LOG_CONVERSAO_MOBIL;
CREATE TABLE LOG_CONVERSAO_MOBIL (ID_LOG INTEGER, TEXTO VARCHAR(100));
CREATE SEQUENCE SEQ_LOG_CONVERSAO_MOBIL;

/* PROCEDURE DESABILITADA POR TER PRIVILÉGIOS INSUFICIENTES */

/*CREATE PROCEDURE LOGAR(TEXTO IN VARCHAR(100)) AUTHID CURRENT_USER IS

BEGIN

    INSERT INTO LOG_CONVERSAO_MOBIL VALUES (SEQ_LOG_CONVERSAO_MOBIL.NEXTVAL,TEXTO);

END LOGAR;
*/


DECLARE
  TYPE REF_CURSOR IS REF CURSOR;
    QRY_MOB_GERAL REF_CURSOR;
    QRY_MOB_GERAL_ID_MOBIL EX_MOBIL.ID_MOBIL%TYPE;

    QRY_MOB REF_CURSOR;
    QRY_MOB_ID_MOBIL EX_MOBIL.ID_MOBIL%TYPE;

    QRY_MOV REF_CURSOR;
    QRY_MOV_RESULT EX_MOVIMENTACAO%ROWTYPE;

    counter NUMBER := 0;

  CURSOR QRY_DOC IS
    SELECT * FROM EX_DOCUMENTO; 


BEGIN

  /*LOG*/INSERT INTO LOG_CONVERSAO_MOBIL VALUES (SEQ_LOG_CONVERSAO_MOBIL.NEXTVAL,'---->INICIANDO CONVERSÃO...');

  FOR DOC IN QRY_DOC LOOP
    OPEN QRY_MOB_GERAL FOR SELECT ID_MOBIL FROM EX_MOBIL WHERE ID_TIPO_MOBIL=1 AND ID_DOC=DOC.ID_DOC;
    FETCH QRY_MOB_GERAL INTO QRY_MOB_GERAL_ID_MOBIL;
    IF QRY_MOB_GERAL%notfound THEN
      /*CRIA MOBIL GERAL CASO NÃO EXISTA*/
      INSERT INTO EX_MOBIL (ID_MOBIL,ID_DOC,ID_TIPO_MOBIL,NUM_SEQUENCIA) VALUES (EX_MOBIL_SEQ.NEXTVAL,DOC.ID_DOC,1, 1);
    END IF;

    OPEN QRY_MOV FOR SELECT * FROM EX_MOVIMENTACAO WHERE ID_MOBIL IS null AND NUM_VIA <> 0 AND ID_DOC=DOC.ID_DOC;

    LOOP

      FETCH QRY_MOV INTO QRY_MOV_RESULT;
      EXIT WHEN QRY_MOV%notfound;

      OPEN QRY_MOB FOR SELECT ID_MOBIL FROM EX_MOBIL WHERE ID_DOC = DOC.ID_DOC AND NUM_SEQUENCIA = QRY_MOV_RESULT.NUM_VIA AND ID_TIPO_MOBIL = 2;
      FETCH QRY_MOB into QRY_MOB_ID_MOBIL;

      IF QRY_MOB%notfound THEN
        INSERT INTO EX_MOBIL (ID_MOBIL,ID_DOC,ID_TIPO_MOBIL,NUM_SEQUENCIA) VALUES (EX_MOBIL_SEQ.NEXTVAL,DOC.ID_DOC,2,QRY_MOV_RESULT.NUM_VIA);

        /*ATUALIZA O CURSOR COM O NOVO EX_MOBIL CRIADO*/
        CLOSE QRY_MOB;
        OPEN QRY_MOB FOR SELECT ID_MOBIL FROM EX_MOBIL WHERE ID_DOC = DOC.ID_DOC AND NUM_SEQUENCIA = QRY_MOV_RESULT.NUM_VIA AND ID_TIPO_MOBIL = 2;
        FETCH QRY_MOB into QRY_MOB_ID_MOBIL;

      END IF;

      UPDATE EX_MOVIMENTACAO SET ID_MOBIL=QRY_MOB_ID_MOBIL WHERE ID_MOV=QRY_MOV_RESULT.ID_MOV;
	  
      close QRY_MOB;

    END LOOP;

    close QRY_MOV;

  END LOOP;

  /*LOG*/INSERT INTO LOG_CONVERSAO_MOBIL VALUES (SEQ_LOG_CONVERSAO_MOBIL.NEXTVAL,'---->CONVERSÃO FINALIZADA!');
  COMMIT;

  /*LOG*/INSERT INTO LOG_CONVERSAO_MOBIL VALUES (SEQ_LOG_CONVERSAO_MOBIL.NEXTVAL,'---->ATUALIZANDO REFERÊNCIA DE ID_DOC_PAI, ID_DOC_REF, NUM_VA_DOC_PAI,NUM_VIA_DOC_REF!');

	UPDATE EX_MOVIMENTACAO MOV SET ID_MOB_REF=(SELECT ID_MOBIL FROM EX_MOBIL WHERE ID_DOC = MOV.ID_DOC_REF AND NUM_SEQUENCIA = MOV.NUM_VIA_DOC_REF AND ID_TIPO_MOBIL = 2);
	UPDATE EX_MOVIMENTACAO MOV SET ID_MOB_REF=(SELECT ID_MOBIL FROM EX_MOBIL WHERE ID_DOC = MOV.ID_DOC_PAI AND NUM_SEQUENCIA = MOV.NUM_VIA_DOC_PAI AND ID_TIPO_MOBIL = 2) WHERE ID_MOB_REF IS NULL AND ID_TP_MOV=12;
	UPDATE EX_MOVIMENTACAO MOV SET ID_MOB_REF=(SELECT ID_MOB_REF FROM EX_MOVIMENTACAO M WHERE M.ID_MOV = MOV.ID_MOV_REF) WHERE ID_MOB_REF IS NULL AND ID_TP_MOV=13;
  --ATUALIZA REFERÊNCIA DE ID_DOC_PAI, ID_DOC_REF, NUM_VA_DOC_PAI,NUM_VIA_DOC_REF
  
  /*LOG*/INSERT INTO LOG_CONVERSAO_MOBIL VALUES (SEQ_LOG_CONVERSAO_MOBIL.NEXTVAL,'---->ATUALIZAÇÃO DE REFERÊNCIA DE ID_DOC_PAI, ID_DOC_REF, NUM_VA_DOC_PAI,NUM_VIA_DOC_REF FINALIZADA!');
  COMMIT;
  
  
  /*SELECT * FROM LOG_CONVERSAO_MOBIL ORDER BY ID_LOG;*/
END;


--inclusão das marcas no bd corporativo


-- A ser executado pelo usuário SIGA
grant REFERENCES on EX_MOBIL to CORPORATIVO;

-- A ser executado pelo usuário CORPORATIVO

create table CORPORATIVO.CP_TIPO_MARCADOR (
  ID_TP_MARCADOR    number,
  DESCR_TIPO_MARCADOR varchar2(30),
  primary key (ID_TP_MARCADOR)
);

create table CORPORATIVO.CP_MARCADOR (
  ID_MARCADOR   number,
  DESCR_MARCADOR varchar2(30),
  ID_TP_MARCADOR number,
  primary key (ID_MARCADOR),
  foreign key (ID_TP_MARCADOR) references CORPORATIVO.CP_TIPO_MARCADOR(ID_TP_MARCADOR)
);

grant REFERENCES on CP_MARCADOR to SIGA;
grant SELECT on CP_MARCADOR to SIGA;

insert into CP_TIPO_MARCADOR values(1, 'Sistema');
insert into CP_TIPO_MARCADOR values(2, 'Geral');
insert into CP_TIPO_MARCADOR values(3, 'Lotação e sublotações');
insert into CP_TIPO_MARCADOR values(4, 'Lotação');
insert into CP_TIPO_MARCADOR values(5, 'Pessoa');


insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (1,	'Em Elaboração', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (2,	'Em Andamento', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (3,	'A Receber', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (4,	'Extraviado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (5,	'A Arquivar', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (6,	'Arquivado Corrente', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (7,	'A Descartar', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (8,	'Descartado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (9,	'Juntado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (10, 'Cancelado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (11, 'Transferido para Órgão Externo', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (12, 'Arquivado Intermediário', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (13, 'Arquivado Permanente', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (14, 'Caixa de Entrada', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (15, 'Pendente de Assinatura', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (16, 'Juntado a Documento Externo', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (18, 'Remetido para Publicação', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (20, 'Publicado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (21, 'Publicação solicitada', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (22, 'Disponibilizado', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (23, 'Em Trânsito', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (24, 'Em Trânsito Eletrônico', 1);
insert into CORPORATIVO.CP_MARCADOR (ID_MARCADOR, DESCR_MARCADOR, ID_TP_MARCADOR) VALUES (25, 'Como Subscritor', 1);
insert into CP_MARCADOR values(26 ,'Apensado',1);
insert into CP_MARCADOR values(27 ,'Como Gestor',1);
insert into CP_MARCADOR values(28 ,'Como Interessado',1);
create table CP_TIPO_MARCA(
	ID_TP_MARCA number,
	DESCR_TP_MARCA varchar2(30),
	primary key (ID_TP_MARCA)
);

insert into CP_TIPO_MARCA values (1, 'SIGA-EX');

create table CP_MARCA (
  ID_MARCA number,
  DT_INI_MARCA date,
  DT_FIM_MARCA date,
  ID_MARCADOR number,
  ID_PESSOA_INI number,
  ID_LOTACAO_INI number,
  ID_MOBIL number,
  ID_TP_MARCA number,
  primary key (ID_MARCA),
  foreign key (ID_MARCADOR) references CORPORATIVO.CP_MARCADOR(ID_MARCADOR),
  foreign key (ID_PESSOA_INI) references CORPORATIVO.DP_PESSOA(ID_PESSOA),
  foreign key (ID_LOTACAO_INI) references CORPORATIVO.DP_LOTACAO(ID_LOTACAO),
  foreign key (ID_MOBIL) references SIGA.EX_MOBIL(ID_MOBIL),
  foreign key (ID_TP_MARCA) references CORPORATIVO.CP_TIPO_MARCA(ID_TP_MARCA)
);

CREATE INDEX pessoa ON cp_marca(id_pessoa_ini);
CREATE INDEX lotacao ON cp_marca(id_lotacao_ini);

create sequence CP_MARCA_SEQ
increment by 1
start with 10
maxvalue 99999999
nocycle;

grant REFERENCES on CP_MARCA to SIGA;
grant SELECT on CP_MARCA to SIGA;
grant INSERT on CP_MARCA to SIGA;
grant UPDATE on CP_MARCA to SIGA;
grant DELETE on CP_MARCA to SIGA;
Grant select on CORPORATIVO.CP_MARCA_SEQ  to SIGA;

-- A ser executado pelo usuário SIGA

-- INCLUSÃO DO PAPEL

CREATE TABLE EX_PAPEL (
  ID_PAPEL NUMBER,
  DESC_PAPEL VARCHAR2 (20),
  PRIMARY KEY (ID_PAPEL)
);

ALTER TABLE EX_MOVIMENTACAO ADD ID_PAPEL NUMBER NULL;
ALTER TABLE EX_MOVIMENTACAO ADD FOREIGN KEY (ID_PAPEL) REFERENCES EX_PAPEL;

INSERT INTO EX_PAPEL (ID_PAPEL,DESC_PAPEL) VALUES (1,'Gestor');
INSERT INTO EX_PAPEL (ID_PAPEL,DESC_PAPEL) VALUES (2,'Interessado');

INSERT INTO EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) VALUES (44,'Vinculação de Papel');

insert into SIGA.EX_CONFIGURACAO (ID_CONFIGURACAO, ID_TP_MOV,  DT_INI_REG, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO) values ((select max(id_configuracao) + 1 from siga.ex_configuracao), 44, sysdate, 1, 29);


