CREATE TABLE sigawf.wf_configuracao (
    conf_id integer NOT NULL,
    defp_id bigint
);

CREATE SEQUENCE sigawf.wf_configuracao_conf_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_def_desvio (
    defd_id integer NOT NULL,
    defd_tx_condicao character varying(256),
    defd_nm character varying(256),
    defd_nr_ordem integer,
    defd_fg_ultimo bit(1),
    deft_id bigint,
    deft_id_seguinte bigint,
    his_dt_fim date,
    his_dt_ini date,
    his_id_ini bigint,
    his_ativo integer,
    his_idc_fim bigint,
    his_idc_ini bigint
);

COMMENT ON COLUMN sigawf.wf_def_desvio.defd_id IS 'Identificador da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.defd_tx_condicao IS 'Fórmula matemática para habilitar a definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.defd_nm IS 'Nome da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.defd_nr_ordem IS 'Número para ordenação da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.defd_fg_ultimo IS 'Indica se essa é a última definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.deft_id IS 'Identificador da definição de tarefa ligada a definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.deft_id_seguinte IS 'Identificador da definição de tarefa alvo desta definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_dt_fim IS 'Data de fim da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_dt_ini IS 'Data de início da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_id_ini IS 'Identificador inicial da definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_ativo IS 'Indica se está ativa a definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_idc_fim IS 'Identificador do cadastrante que finalizou a definição de desvio';

COMMENT ON COLUMN sigawf.wf_def_desvio.his_idc_ini IS 'Identificador do cadastrante que criou a definição de desvio';

CREATE SEQUENCE sigawf.wf_def_desvio_defd_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_def_procedimento (
    defp_id integer NOT NULL,
    orgu_id integer,
    defp_ano integer,
    defp_ds character varying(256) NOT NULL,
    defp_nm character varying(256) NOT NULL,
    defp_nr integer,
    defp_tp_edicao character varying(45) DEFAULT 'ACESSO_LOTACAO'::character varying NOT NULL,
    defp_tp_inicio character varying(45) DEFAULT 'ACESSO_LOTACAO'::character varying NOT NULL,
    pess_id_resp bigint,
    lota_id_resp bigint,
    grup_id_edicao integer,
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_id IS 'Identificador da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.orgu_id IS 'Identificador do órgao usuário ao qual se vincula a definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_ano IS 'Ano da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_ds IS 'Descrição da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_nm IS 'Nome da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_nr IS 'Número da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_tp_edicao IS 'Tipo de permissão para edição desta definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.defp_tp_inicio IS 'Tipo de permissão para iniciar procedimentos desta definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.pess_id_resp IS 'Pessoa responsável pela definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.lota_id_resp IS 'Lotação responsável pela definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.grup_id_edicao IS 'Referência ao grupo de pessoas/lotações com permissão de edição';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_id_ini IS 'Identificador inicial da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_ativo IS 'Indica se está ativa a definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_dt_ini IS 'Data de início da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_dt_fim IS 'Data de fim da definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_idc_ini IS 'Identificador do cadastrante que criou a definição de procedimento';

COMMENT ON COLUMN sigawf.wf_def_procedimento.his_idc_fim IS 'Identificador do cadastrante que finalizou a definição de procedimento';

CREATE SEQUENCE sigawf.wf_def_procedimento_defp_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_def_responsavel (
    defr_id integer NOT NULL,
    defr_ds character varying(256),
    defr_nm character varying(256),
    defr_tp character varying(255),
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_def_responsavel.defr_id IS 'Identificador da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.defr_ds IS 'Descrição da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.defr_nm IS 'Nome da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.defr_tp IS 'Tipo da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_id_ini IS 'Identificador inicial da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_ativo IS 'Indica se está ativa a definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_dt_ini IS 'Data de início da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_dt_fim IS 'Data de fim da definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_idc_ini IS 'Identificador do cadastrante que criou a definição de responsável';

COMMENT ON COLUMN sigawf.wf_def_responsavel.his_idc_fim IS 'Identificador do cadastrante que finalizou a definição de responsável';

CREATE SEQUENCE sigawf.wf_def_responsavel_defr_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_def_tarefa (
    deft_id integer NOT NULL,
    defp_id bigint,
    defr_id bigint,
    lota_id bigint,
    pess_id bigint,
    deft_id_seguinte bigint,
    deft_tx_assunto character varying(256),
    deft_tx_conteudo character varying(2048),
    deft_nm character varying(256),
    deft_ds_ref character varying(256),
    deft_id_ref bigint,
    deft_sg_ref character varying(32),
    deft_tp_responsavel character varying(255),
    deft_tp_tarefa character varying(255),
    deft_fg_ultimo bit(1),
    deft_nr_ordem integer,
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_id IS 'Identificador da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.defp_id IS 'Identificador do procedimento ao qual se liga a definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.defr_id IS 'Identificador do responsável pela execução da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.lota_id IS 'Identificador da lotação responsável da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.pess_id IS 'Identificador da pessoa responsável dadefinição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_id_seguinte IS 'Identificador definição de tarefa seguinte a definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_tx_assunto IS 'Assunto da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_tx_conteudo IS 'Conteúdo da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_nm IS 'Nome da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_ds_ref IS 'Descrição de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_id_ref IS 'Identificador de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_sg_ref IS 'Sigla de alguma entidade externa relacionada a definição de tarefa usado por exemplo para referenciar modelos';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_tp_responsavel IS 'Tipo do responsável na definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_tp_tarefa IS 'Tipo de tarefa da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_fg_ultimo IS 'Indica que é a última da definição de tarefa do procedimento';

COMMENT ON COLUMN sigawf.wf_def_tarefa.deft_nr_ordem IS 'Número usado para ordenar a definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_id_ini IS 'Identificador inicial da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_ativo IS 'Indica se está ativa a definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_dt_ini IS 'Data de início da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_dt_fim IS 'Data de fim da definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_idc_ini IS 'Identificador do cadastrante que criou a definição de tarefa';

COMMENT ON COLUMN sigawf.wf_def_tarefa.his_idc_fim IS 'Identificador do cadastrante que finalizou aa definição de tarefa';

CREATE SEQUENCE sigawf.wf_def_tarefa_deft_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_def_variavel (
    defv_id integer NOT NULL,
    deft_id bigint,
    defv_cd character varying(32),
    defv_nm character varying(256),
    defv_tp character varying(255),
    defv_tp_acesso character varying(255),
    defv_nr_ordem integer,
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_id IS 'Identificador da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.deft_id IS 'Identificador definição de tarefa a qual se relaciona a definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_cd IS 'Código da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_nm IS 'Nome da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_tp IS 'Tipo da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_tp_acesso IS 'Tipo de acesso da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.defv_nr_ordem IS 'Número para ordenar a definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_id_ini IS 'Identificador inicial da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_ativo IS 'Indica se está ativa a definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_dt_ini IS 'Data de início da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_dt_fim IS 'Data de fim da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_idc_ini IS 'Identificador do cadastrante da criação da definição de variável';

COMMENT ON COLUMN sigawf.wf_def_variavel.his_idc_fim IS 'Identificador do cadastrante da finalização da definição de variável';

CREATE SEQUENCE sigawf.wf_def_variavel_defv_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_movimentacao (
    movi_id integer NOT NULL,
    movi_tp character varying(31) NOT NULL,
    proc_id bigint,
    lota_id_titular bigint,
    pess_id_titular bigint,
    deft_id_de bigint,
    deft_id_para bigint,
    lota_id_de bigint,
    lota_id_para bigint,
    pess_id_de bigint,
    pess_id_para bigint,
    movi_ds character varying(400),
    movi_tp_designacao character varying(255),
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_movimentacao.movi_id IS 'Identificador da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.movi_tp IS 'Tipo da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.proc_id IS 'Identificador do procedimento ao qual se relaciona a movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.lota_id_titular IS 'Identificador da lotação do titular da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.pess_id_titular IS 'Identificador da pessoa do titular da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.deft_id_de IS 'Identificador da definição de tarefa origem da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.deft_id_para IS 'Identificador da definição de tarefa destino da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.lota_id_de IS 'Identificador da lotação origem da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.lota_id_para IS 'Identificador da lotação destino da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.pess_id_de IS 'Identificador da pessoa origem da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.pess_id_para IS 'Identificador da pessoa destino da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.movi_ds IS 'Descrição da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.movi_tp_designacao IS 'Numa designação, indica o tipo de designação da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_id_ini IS 'Identificador inicial da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_ativo IS 'Indica se a movimentação está ativa';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_dt_ini IS 'Data de início da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_dt_fim IS 'Data de fim da movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_idc_ini IS 'Identificador do cadastrante que criou a movimentação';

COMMENT ON COLUMN sigawf.wf_movimentacao.his_idc_fim IS 'Identificador do cadastrante que finalizou a movimentação';

CREATE SEQUENCE sigawf.wf_movimentacao_movi_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_procedimento (
    proc_id integer NOT NULL,
    orgu_id bigint,
    defp_id bigint,
    lota_id_evento bigint,
    pess_id_evento bigint,
    lota_id_titular bigint,
    pess_id_titular bigint,
    proc_ano integer,
    proc_nr integer,
    proc_tp_prioridade character varying(255),
    proc_nr_corrente integer,
    proc_st_corrente character varying(255),
    proc_ts_evento date,
    proc_nm_evento character varying(255),
    proc_cd_principal character varying(255),
    proc_tp_principal character varying(255),
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint
);

COMMENT ON COLUMN sigawf.wf_procedimento.proc_id IS 'Identificador do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.orgu_id IS 'Identificador do órgão usuário ao qual se relaciona o procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.defp_id IS 'Identificador da definição do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.lota_id_evento IS 'Identificador da lotação responsável pelo evento atual do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.pess_id_evento IS 'Identificador da pessoa responsável pelo evento atual do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.lota_id_titular IS 'Identificador da lotação do titular que criou o procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.pess_id_titular IS 'Identificador da pessoa que criou o procedimento do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_ano IS 'Ano do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_nr IS 'Número do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_tp_prioridade IS 'Prioridade do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_nr_corrente IS 'Número do passo em que se encontra o procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_st_corrente IS 'Status atual do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_ts_evento IS 'Data e hora do evento do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_nm_evento IS 'Nome do evento do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_cd_principal IS 'Código do principal relacionado ao procedimento, no caso de um documento, é algo do tipo JFRJ-MEM-2020/000001';

COMMENT ON COLUMN sigawf.wf_procedimento.proc_tp_principal IS 'Tipo do principal do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.his_dt_ini IS 'Data de fim do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.his_dt_fim IS 'Data de início do procedimento';

COMMENT ON COLUMN sigawf.wf_procedimento.his_idc_ini IS 'Identificador do cadastrante da criação do procedimento';

CREATE SEQUENCE sigawf.wf_procedimento_proc_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_responsavel (
    resp_id integer NOT NULL,
    defr_id bigint,
    orgu_id bigint,
    lota_id bigint,
    pess_id bigint,
    his_id_ini bigint,
    his_ativo integer,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini bigint,
    his_idc_fim bigint
);

COMMENT ON COLUMN sigawf.wf_responsavel.resp_id IS 'Identificador do responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.defr_id IS 'Identificador da definição do responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.orgu_id IS 'Identificador do órgão vinculado ao responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.lota_id IS 'Identificador da lotação vinculada ao responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.pess_id IS 'Identificador da pessoa vinculada ao responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_id_ini IS 'Identificador inicial do responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_ativo IS 'Indica se está ativo o responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_dt_ini IS 'Data de início do responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_dt_fim IS 'Data de fim do responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_idc_ini IS 'Identificador do cadastrante que criou o responsável';

COMMENT ON COLUMN sigawf.wf_responsavel.his_idc_fim IS 'Identificador do cadastrante que finalizou o responsável';

CREATE SEQUENCE sigawf.wf_responsavel_resp_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE sigawf.wf_variavel (
    vari_id integer NOT NULL,
    proc_id bigint,
    vari_fg bit(1),
    vari_df date,
    vari_nm character varying(256),
    vari_nr money,
    vari_tx character varying(255)
);

COMMENT ON COLUMN sigawf.wf_variavel.vari_id IS 'Identificador da variável';

COMMENT ON COLUMN sigawf.wf_variavel.proc_id IS 'Identificador do procedimento relacionado a variável';

COMMENT ON COLUMN sigawf.wf_variavel.vari_fg IS 'Conteúdo da variável quando é um booleano';

COMMENT ON COLUMN sigawf.wf_variavel.vari_df IS 'Conteúdo da variável quando é uma data';

COMMENT ON COLUMN sigawf.wf_variavel.vari_nm IS 'Nome da variável';

COMMENT ON COLUMN sigawf.wf_variavel.vari_nr IS 'Conteúdo da variável quando é um número';

COMMENT ON COLUMN sigawf.wf_variavel.vari_tx IS 'Conteúdo da variável quando é um texto';

CREATE SEQUENCE sigawf.wf_variavel_vari_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY sigawf.wf_configuracao ALTER COLUMN conf_id SET DEFAULT nextval('sigawf.wf_configuracao_conf_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_def_desvio ALTER COLUMN defd_id SET DEFAULT nextval('sigawf.wf_def_desvio_defd_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_def_procedimento ALTER COLUMN defp_id SET DEFAULT nextval('sigawf.wf_def_procedimento_defp_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_def_responsavel ALTER COLUMN defr_id SET DEFAULT nextval('sigawf.wf_def_responsavel_defr_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_def_tarefa ALTER COLUMN deft_id SET DEFAULT nextval('sigawf.wf_def_tarefa_deft_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_def_variavel ALTER COLUMN defv_id SET DEFAULT nextval('sigawf.wf_def_variavel_defv_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_movimentacao ALTER COLUMN movi_id SET DEFAULT nextval('sigawf.wf_movimentacao_movi_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_procedimento ALTER COLUMN proc_id SET DEFAULT nextval('sigawf.wf_procedimento_proc_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_responsavel ALTER COLUMN resp_id SET DEFAULT nextval('sigawf.wf_responsavel_resp_id_seq'::regclass);

ALTER TABLE ONLY sigawf.wf_variavel ALTER COLUMN vari_id SET DEFAULT nextval('sigawf.wf_variavel_vari_id_seq'::regclass);
