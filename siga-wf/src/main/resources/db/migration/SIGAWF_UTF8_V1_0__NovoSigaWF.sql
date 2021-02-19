ALTER SESSION SET CURRENT_SCHEMA=sigawf;

-- DROP TABLE wf_responsavel;
-- DROP TABLE wf_variavel;
-- DROP TABLE wf_movimentacao;
-- DROP TABLE wf_procedimento;
-- DROP TABLE wf_def_variavel;
-- DROP TABLE wf_def_desvio;
-- DROP TABLE wf_def_tarefa;
-- DROP TABLE wf_def_responsavel;
-- DROP TABLE wf_def_procedimento;
-- DROP FUNCTION remove_acento;

DROP SEQUENCE sigawf.hibernate_sequence;

CREATE SEQUENCE  sigawf.hibernate_sequence  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--
-- Table structure for table wf_def_procedimento
--
CREATE TABLE sigawf.wf_def_procedimento (
  DEFP_ID NUMBER,
  ORGU_ID NUMBER NULL,
  DEFP_ANO int NULL,
  DEFP_DS varchar(256) NOT NULL,
  DEFP_NM varchar(256) NOT NULL,
  DEFP_NR int NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  CONSTRAINT PK_DEF_ID PRIMARY KEY (DEFP_ID)
);

COMMENT ON COLUMN wf_def_procedimento.DEFP_ID IS 'Identificador da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.ORGU_ID IS 'Identificador do órgao usuário ao qual se vincula a definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.DEFP_ANO IS 'Ano da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.DEFP_DS IS 'Descrição da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.DEFP_NM IS 'Nome da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.DEFP_NR IS 'Número da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_ID_INI IS 'Identificador inicial da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_ATIVO IS 'Indica se está ativa a definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_DT_INI IS 'Data de início da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_DT_FIM IS 'Data de fim da definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_IDC_INI IS 'Identificador do cadastrante que criou a definição de procedimento';
COMMENT ON COLUMN wf_def_procedimento.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou a definição de procedimento';

--
-- Table structure for table wf_def_responsavel
--
CREATE TABLE sigawf.wf_def_responsavel (
  DEFR_ID NUMBER,
  DEFR_DS varchar(256) NULL,
  DEFR_NM varchar(256) NULL,
  DEFR_TP varchar(255) NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  CONSTRAINT DEFR PRIMARY KEY (DEFR_ID)
) ;

COMMENT ON COLUMN wf_def_responsavel.DEFR_ID IS 'Identificador da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.DEFR_DS IS 'Descrição da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.DEFR_NM IS 'Nome da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.DEFR_TP IS 'Tipo da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_ID_INI IS 'Identificador inicial da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_ATIVO IS 'Indica se está ativa a definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_DT_INI IS 'Data de início da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_DT_FIM IS 'Data de fim da definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_IDC_INI IS 'Identificador do cadastrante que criou a definição de responsável';
COMMENT ON COLUMN wf_def_responsavel.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou a definição de responsável';

--
-- Table structure for table wf_def_tarefa
--
CREATE TABLE sigawf.wf_def_tarefa (
  DEFT_ID NUMBER,
  DEFP_ID NUMBER NULL,
  DEFR_ID NUMBER NULL,
  LOTA_ID NUMBER NULL,
  PESS_ID NUMBER NULL,
  DEFT_ID_SEGUINTE NUMBER NULL,
  DEFT_TX_ASSUNTO varchar(256) NULL,
  DEFT_TX_CONTEUDO varchar(2048) NULL,
  DEFT_NM varchar(256) NULL,
  DEFT_DS_REF varchar(256) NULL,
  DEFT_ID_REF NUMBER NULL,
  DEFT_SG_REF varchar(32) NULL,
  DEFT_TP_RESPONSAVEL varchar(255) NULL,
  DEFT_TP_TAREFA varchar(255) NULL,
  DEFT_FG_ULTIMO NUMBER(1,0) NULL,
  DEFT_NR_ORDEM int NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  PRIMARY KEY (DEFT_ID),
  CONSTRAINT FK_DEFT_DEFT_ID_SEGUINTE FOREIGN KEY (DEFT_ID_SEGUINTE) REFERENCES wf_def_tarefa (DEFT_ID),
  CONSTRAINT FK_DEFT_DEFP_ID FOREIGN KEY (DEFP_ID) REFERENCES wf_def_procedimento (DEFP_ID),
  CONSTRAINT FK_DEFT_DEFR_ID FOREIGN KEY (DEFR_ID) REFERENCES wf_def_responsavel (DEFR_ID)
) ;

COMMENT ON COLUMN wf_def_tarefa.DEFT_ID IS 'Identificador da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFP_ID IS 'Identificador do procedimento ao qual se liga a definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFR_ID IS 'Identificador do responsável pela execução da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.LOTA_ID IS 'Identificador da lotação responsável da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.PESS_ID IS 'Identificador da pessoa responsável dadefinição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_ID_SEGUINTE IS 'Identificador definição de tarefa seguinte a definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_TX_ASSUNTO IS 'Assunto da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_TX_CONTEUDO IS 'Conteúdo da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_NM IS 'Nome da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_DS_REF IS 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';
COMMENT ON COLUMN wf_def_tarefa.DEFT_ID_REF IS 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';
COMMENT ON COLUMN wf_def_tarefa.DEFT_SG_REF IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';
COMMENT ON COLUMN wf_def_tarefa.DEFT_TP_RESPONSAVEL IS 'Tipo do responsável na definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_TP_TAREFA IS 'Tipo de tarefa da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.DEFT_FG_ULTIMO IS 'Indica que é a última da definição de tarefa do procedimento';
COMMENT ON COLUMN wf_def_tarefa.DEFT_NR_ORDEM IS 'Número usado para ordenar a definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_ID_INI IS 'Identificador inicial da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_ATIVO IS 'Indica se está ativa a definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_DT_INI IS 'Data de início da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_DT_FIM IS 'Data de fim da definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_IDC_INI IS 'Identificador do cadastrante que criou a definição de tarefa';
COMMENT ON COLUMN wf_def_tarefa.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou aa definição de tarefa';

CREATE TABLE sigawf.wf_def_desvio (
  DEFD_ID NUMBER,
  DEFD_TX_CONDICAO varchar(256) NULL,
  DEFD_NM varchar(256) NULL,
  DEFD_NR_ORDEM NUMBER(3,0) NULL,
  DEFD_FG_ULTIMO NUMBER(1,0) NULL,
  DEFT_ID NUMBER NULL,
  DEFT_ID_SEGUINTE NUMBER NULL,
  HIS_DT_FIM DATE NULL,
  HIS_DT_INI DATE NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_IDC_FIM NUMBER NULL,
  HIS_IDC_INI NUMBER NULL,
  CONSTRAINT PK_DEFD_ID PRIMARY KEY (DEFD_ID),
  CONSTRAINT FK_DEFD_DEFT_ID FOREIGN KEY (DEFT_ID) REFERENCES wf_def_tarefa (DEFT_ID),
  CONSTRAINT FK_DEFD_DEFT_ID_SEGUINTE FOREIGN KEY (DEFT_ID_SEGUINTE) REFERENCES wf_def_tarefa (DEFT_ID)
);

COMMENT ON COLUMN wf_def_desvio.DEFD_ID IS 'Identificador da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFD_TX_CONDICAO IS 'Fórmula matemática para habilitar a definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFD_NM IS 'Nome da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFD_NR_ORDEM IS 'Número para ordenação da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFD_FG_ULTIMO IS 'Indica se essa é a última definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFT_ID IS 'Identificador da definição de tarefa ligada a definição de desvio';
COMMENT ON COLUMN wf_def_desvio.DEFT_ID_SEGUINTE IS 'Identificador da definição de tarefa alvo desta definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_DT_FIM IS 'Data de fim da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_DT_INI IS 'Data de início da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_ID_INI IS 'Identificador inicial da definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_ATIVO IS 'Indica se está ativa a definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou a definição de desvio';
COMMENT ON COLUMN wf_def_desvio.HIS_IDC_INI IS 'Identificador do cadastrante que criou a definição de desvio';


--
-- Table structure for table wf_def_variavel
--
CREATE TABLE sigawf.wf_def_variavel (
  DEFV_ID NUMBER,
  DEFT_ID NUMBER NULL,
  DEFV_CD varchar(32) NULL,
  DEFV_NM varchar(256) NULL,
  DEFV_TP varchar(255) NULL,
  DEFV_TP_ACESSO varchar(255) NULL,
  DEFV_NR_ORDEM int NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  CONSTRAINT DEFV PRIMARY KEY (DEFV_ID),
  CONSTRAINT FK_DEFV_DEFT_ID FOREIGN KEY (DEFT_ID) REFERENCES wf_def_tarefa (DEFT_ID)
) ;

COMMENT ON COLUMN wf_def_variavel.DEFV_ID IS 'Identificador da definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFT_ID IS 'Identificador definição de tarefa a qual se relaciona a definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFV_CD IS 'Código da definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFV_NM IS 'Nome da definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFV_TP IS 'Tipo da definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFV_TP_ACESSO IS 'Tipo de acesso da definição de variável';
COMMENT ON COLUMN wf_def_variavel.DEFV_NR_ORDEM IS 'Número para ordenar a definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_ID_INI IS 'Identificador inicial da definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_ATIVO IS 'Indica se está ativa a definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_DT_INI IS 'Data de início da definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_DT_FIM IS 'Data de fim da definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_IDC_INI IS 'Identificador do cadastrante da criação da definição de variável';
COMMENT ON COLUMN wf_def_variavel.HIS_IDC_FIM IS 'Identificador do cadastrante da finalização da definição de variável';

--
-- Table structure for table wf_procedimento
--
CREATE TABLE sigawf.wf_procedimento (
  PROC_ID NUMBER,
  ORGU_ID NUMBER NULL,
  DEFP_ID NUMBER NULL,
  LOTA_ID_EVENTO NUMBER NULL,
  PESS_ID_EVENTO NUMBER NULL,
  LOTA_ID_TITULAR NUMBER NULL,
  PESS_ID_TITULAR NUMBER NULL,
  PROC_ANO int NULL,
  PROC_NR int NULL,
  PROC_TP_PRIORIDADE varchar(255) NULL,
  PROC_NR_CORRENTE int NULL,
  PROC_ST_CORRENTE varchar(255) NULL,
  PROC_TS_EVENTO DATE NULL,
  PROC_NM_EVENTO varchar(255) NULL,
  PROC_CD_PRINCIPAL varchar(255) NULL,
  PROC_TP_PRINCIPAL varchar(255) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  CONSTRAINT PROC PRIMARY KEY (PROC_ID),
  CONSTRAINT FK_PROC_DEFP_ID FOREIGN KEY (DEFP_ID) REFERENCES wf_def_procedimento (DEFP_ID)
) ;
COMMENT ON COLUMN wf_procedimento.PROC_ID IS 'Identificador do procedimento';
COMMENT ON COLUMN wf_procedimento.ORGU_ID IS 'Identificador do órgão usuário ao qual se relaciona o procedimento';
COMMENT ON COLUMN wf_procedimento.DEFP_ID IS 'Identificador da definição do procedimento';
COMMENT ON COLUMN wf_procedimento.LOTA_ID_EVENTO IS 'Identificador da lotação responsável pelo evento atual do procedimento';
COMMENT ON COLUMN wf_procedimento.PESS_ID_EVENTO IS 'Identificador da pessoa responsável pelo evento atual do procedimento';
COMMENT ON COLUMN wf_procedimento.LOTA_ID_TITULAR IS 'Identificador da lotação do titular que criou o procedimento';
COMMENT ON COLUMN wf_procedimento.PESS_ID_TITULAR IS 'Identificador da pessoa que criou o procedimento do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_ANO IS 'Ano do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_NR IS 'Número do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_TP_PRIORIDADE IS 'Prioridade do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_NR_CORRENTE IS 'Número do passo em que se encontra o procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_ST_CORRENTE IS 'Status atual do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_TS_EVENTO IS 'Data e hora do evento do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_NM_EVENTO IS 'Nome do evento do procedimento';
COMMENT ON COLUMN wf_procedimento.PROC_CD_PRINCIPAL IS 'Código do principal relacionado ao procedimento, no caso de um documento, é algo do tipo JFRJ-MEM-2020/000001';
COMMENT ON COLUMN wf_procedimento.PROC_TP_PRINCIPAL IS 'Tipo do principal do procedimento';
COMMENT ON COLUMN wf_procedimento.HIS_DT_INI IS 'Data de fim do procedimento';
COMMENT ON COLUMN wf_procedimento.HIS_DT_FIM IS 'Data de início do procedimento';
COMMENT ON COLUMN wf_procedimento.HIS_IDC_INI IS 'Identificador do cadastrante da criação do procedimento';


--
-- Table structure for table wf_movimentacao
--
CREATE TABLE sigawf.wf_movimentacao (
  MOVI_ID NUMBER,
  MOVI_TP varchar(31) NOT NULL,
  PROC_ID NUMBER NULL,
  LOTA_ID_TITULAR NUMBER NULL,
  PESS_ID_TITULAR NUMBER NULL,
  DEFT_ID_DE NUMBER NULL,
  DEFT_ID_PARA NUMBER NULL,
  LOTA_ID_DE NUMBER NULL,
  LOTA_ID_PARA NUMBER NULL,
  PESS_ID_DE NUMBER NULL,
  PESS_ID_PARA NUMBER NULL,
  MOVI_DS varchar(400) NULL,
  MOVI_TP_DESIGNACAO varchar(255) NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  CONSTRAINT MOVI PRIMARY KEY (MOVI_ID),
  CONSTRAINT FK_MOVI_DEFT_ID_DE FOREIGN KEY (DEFT_ID_DE) REFERENCES wf_def_tarefa (DEFT_ID),
  CONSTRAINT FK_MOVI_DEFT_ID_PARA FOREIGN KEY (DEFT_ID_PARA) REFERENCES wf_def_tarefa (DEFT_ID),
  CONSTRAINT FK_MOVI_PROC_ID FOREIGN KEY (PROC_ID) REFERENCES wf_procedimento (PROC_ID)
) ;

COMMENT ON COLUMN wf_movimentacao.MOVI_ID IS 'Identificador da movimentação';
COMMENT ON COLUMN wf_movimentacao.MOVI_TP IS 'Tipo da movimentação';
COMMENT ON COLUMN wf_movimentacao.PROC_ID IS 'Identificador do procedimento ao qual se relaciona a movimentação';
COMMENT ON COLUMN wf_movimentacao.LOTA_ID_TITULAR IS 'Identificador da lotação do titular da movimentação';
COMMENT ON COLUMN wf_movimentacao.PESS_ID_TITULAR IS 'Identificador da pessoa do titular da movimentação';
COMMENT ON COLUMN wf_movimentacao.DEFT_ID_DE IS 'Identificador da definição de tarefa origem da movimentação';
COMMENT ON COLUMN wf_movimentacao.DEFT_ID_PARA IS 'Identificador da definição de tarefa destino da movimentação';
COMMENT ON COLUMN wf_movimentacao.LOTA_ID_DE IS 'Identificador da lotação origem da movimentação';
COMMENT ON COLUMN wf_movimentacao.LOTA_ID_PARA IS 'Identificador da lotação destino da movimentação';
COMMENT ON COLUMN wf_movimentacao.PESS_ID_DE IS 'Identificador da pessoa origem da movimentação';
COMMENT ON COLUMN wf_movimentacao.PESS_ID_PARA IS 'Identificador da pessoa destino da movimentação';
COMMENT ON COLUMN wf_movimentacao.MOVI_DS IS 'Descrição da movimentação';
COMMENT ON COLUMN wf_movimentacao.MOVI_TP_DESIGNACAO IS 'Numa designação, indica o tipo de designação da movimentação';
COMMENT ON COLUMN wf_movimentacao.HIS_ID_INI IS 'Identificador inicial da movimentação';
COMMENT ON COLUMN wf_movimentacao.HIS_ATIVO IS 'Indica se a movimentação está ativa';
COMMENT ON COLUMN wf_movimentacao.HIS_DT_INI IS 'Data de início da movimentação';
COMMENT ON COLUMN wf_movimentacao.HIS_DT_FIM IS 'Data de fim da movimentação';
COMMENT ON COLUMN wf_movimentacao.HIS_IDC_INI IS 'Identificador do cadastrante que criou a movimentação';
COMMENT ON COLUMN wf_movimentacao.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou a movimentação';


--
-- Table structure for table wf_responsavel
--
CREATE TABLE sigawf.wf_responsavel (
  RESP_ID NUMBER,
  DEFR_ID NUMBER NULL,
  ORGU_ID NUMBER NULL,
  LOTA_ID NUMBER NULL,
  PESS_ID NUMBER NULL,
  HIS_ID_INI NUMBER NULL,
  HIS_ATIVO NUMBER(1,0) NULL,
  HIS_DT_INI DATE NULL,
  HIS_DT_FIM DATE NULL,
  HIS_IDC_INI NUMBER NULL,
  HIS_IDC_FIM NUMBER NULL,
  CONSTRAINT RESP PRIMARY KEY (RESP_ID),
  CONSTRAINT FK_RESP_DEFR_ID FOREIGN KEY (DEFR_ID) REFERENCES wf_def_responsavel (DEFR_ID)
) ;
COMMENT ON COLUMN wf_responsavel.RESP_ID IS 'Identificador do responsável';
COMMENT ON COLUMN wf_responsavel.DEFR_ID IS 'Identificador da definição do responsável';
COMMENT ON COLUMN wf_responsavel.ORGU_ID IS 'Identificador do órgão vinculado ao responsável';
COMMENT ON COLUMN wf_responsavel.LOTA_ID IS 'Identificador da lotação vinculada ao responsável';
COMMENT ON COLUMN wf_responsavel.PESS_ID IS 'Identificador da pessoa vinculada ao responsável';
COMMENT ON COLUMN wf_responsavel.HIS_ID_INI IS 'Identificador inicial do responsável';
COMMENT ON COLUMN wf_responsavel.HIS_ATIVO IS 'Indica se está ativo o responsável';
COMMENT ON COLUMN wf_responsavel.HIS_DT_INI IS 'Data de início do responsável';
COMMENT ON COLUMN wf_responsavel.HIS_DT_FIM IS 'Data de fim do responsável';
COMMENT ON COLUMN wf_responsavel.HIS_IDC_INI IS 'Identificador do cadastrante que criou o responsável';
COMMENT ON COLUMN wf_responsavel.HIS_IDC_FIM IS 'Identificador do cadastrante que finalizou o responsável';


--
-- Table structure for table wf_variavel
--
CREATE TABLE sigawf.wf_variavel (
  VARI_ID NUMBER,
  PROC_ID NUMBER NULL,
  VARI_FG NUMBER(1,0) NULL,
  VARI_DF DATE NULL,
  VARI_NM varchar(256) NULL,
  VARI_NR NUMBER(19,10) NULL,
  VARI_TX varchar(255) NULL,
  CONSTRAINT VARI PRIMARY KEY (VARI_ID),
  CONSTRAINT FK_VARI_PROC_ID FOREIGN KEY (PROC_ID) REFERENCES wf_procedimento (PROC_ID)
) ;
COMMENT ON COLUMN wf_variavel.VARI_ID IS 'Identificador da variável';
COMMENT ON COLUMN wf_variavel.PROC_ID IS 'Identificador do procedimento relacionado a variável';
COMMENT ON COLUMN wf_variavel.VARI_FG IS 'Conteúdo da variável quando é um booleano';
COMMENT ON COLUMN wf_variavel.VARI_DF IS 'Conteúdo da variável quando é uma data';
COMMENT ON COLUMN wf_variavel.VARI_NM IS 'Nome da variável';
COMMENT ON COLUMN wf_variavel.VARI_NR IS 'Conteúdo da variável quando é um número';
COMMENT ON COLUMN wf_variavel.VARI_TX IS 'Conteúdo da variável quando é um texto';

--------------------------------------------------------
--  DDL for Function REMOVE_ACENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION SIGAWF.REMOVE_ACENTO 
    (acentuado in
    VARCHAR2)

--	Enter the parameters for the function in
--	the brackets above.  If this function has
--	no parameters then delete the line

--  ***************************************************
--	*                                                 *
--	*   Author                                        *
--	*   Creation Date                                 *
--	*   Comments                                      *
--	*                                                 *
--  ***************************************************

RETURN  VARCHAR2

IS

--	Enter all variables cursors and constants following
--	this line
sem_acento VARCHAR2(4000);

begin

--	Enter the code for the function following
--	this line

      sem_acento := CONVERT(TRANSLATE(UPPER( acentuado ),'ÃÕÑ','AON'),'US7ASCII');

	return	sem_acento;
			
exception

--	Enter the code to handle exception conditions
--	following this line


	when others then
		null;
			
end;
 
/