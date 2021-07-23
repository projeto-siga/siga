CREATE TABLE corporativo.cad_sit_funcional (
    id_cad_sit_funcional integer NOT NULL,
    dsc_sit_funcional character varying(60),
    id_mumps integer
);

CREATE SEQUENCE corporativo.cad_sit_funcional_id_cad_sit_funcional_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_acesso (
    id_acesso integer NOT NULL,
    id_identidade integer NOT NULL,
    dt_ini timestamp with time zone NOT NULL,
    dt_fim timestamp with time zone,
    tp_acesso smallint NOT NULL,
    ip_audit character varying(256)
);

CREATE SEQUENCE corporativo.cp_acesso_id_acesso_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_aplicacao_feriado (
    id_aplicacao integer NOT NULL,
    id_orgao_usu integer,
    id_lotacao integer,
    id_localidade integer,
    id_ocorrencia_feriado integer,
    feriado character varying(1)
);

CREATE SEQUENCE corporativo.cp_aplicacao_feriado_id_aplicacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_arquivo (
    id_arq integer NOT NULL,
    id_orgao_usu integer,
    conteudo_tp_arq character varying(256),
    tp_armazenamento character varying(20),
    caminho character varying(255),
    tamanho_arq integer NOT NULL
);

COMMENT ON COLUMN corporativo.cp_arquivo.tp_armazenamento IS 'Content-Type do arquivo';

COMMENT ON COLUMN corporativo.cp_arquivo.caminho IS 'Caminho do arquivo no mecanismo de armazenamento';

COMMENT ON COLUMN corporativo.cp_arquivo.tamanho_arq IS 'Tamanho do arquivo em bytes';

CREATE TABLE corporativo.cp_arquivo_blob (
    id_arq_blob bigint NOT NULL,
    conteudo_arq_blob bytea
);

CREATE TABLE corporativo.cp_arquivo_excluir (
    id_arq_exc bigint NOT NULL,
    caminho character varying(255) NOT NULL
);

COMMENT ON COLUMN corporativo.cp_arquivo_excluir.caminho IS 'Coluna com o caminho do arquivo binário para exclusão';

CREATE SEQUENCE corporativo.cp_arquivo_id_arq_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_complexo (
    id_complexo integer NOT NULL,
    nome_complexo character varying(100),
    id_localidade integer,
    id_orgao_usu integer
);

CREATE TABLE corporativo.cp_configuracao (
    id_configuracao integer NOT NULL,
    dt_ini_vig_configuracao timestamp with time zone,
    dt_fim_vig_configuracao timestamp with time zone,
    his_dt_ini timestamp with time zone,
    id_orgao_usu integer,
    id_lotacao integer,
    id_cargo integer,
    id_funcao_confianca integer,
    id_pessoa integer,
    id_sit_configuracao integer,
    id_tp_configuracao integer,
    id_servico integer,
    id_grupo integer,
    nm_email character varying(50),
    desc_formula character varying(1024),
    id_tp_lotacao integer,
    id_identidade integer,
    his_idc_ini integer,
    his_idc_fim integer,
    his_dt_fim timestamp with time zone,
    his_id_ini integer,
    id_complexo integer,
    id_orgao_objeto integer,
    descr_configuracao character varying(255),
    id_lotacao_objeto integer,
    id_complexo_objeto integer,
    id_cargo_objeto integer,
    id_funcao_confianca_objeto integer,
    id_pessoa_objeto integer
);

CREATE SEQUENCE corporativo.cp_configuracao_id_configuracao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_feriado (
    id_feriado integer NOT NULL,
    dsc_feriado character varying(256) NOT NULL
);

CREATE SEQUENCE corporativo.cp_feriado_id_feriado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_grupo (
    id_grupo integer NOT NULL,
    id_tp_grupo integer NOT NULL,
    id_orgao_usu integer NOT NULL,
    id_grupo_pai integer,
    sigla_grupo character varying(20),
    desc_grupo character varying(200),
    his_id_ini integer,
    his_dt_ini timestamp with time zone NOT NULL,
    his_idc_ini integer,
    his_dt_fim timestamp with time zone,
    his_idc_fim integer,
    his_ativo smallint NOT NULL
);

CREATE SEQUENCE corporativo.cp_grupo_id_grupo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_identidade (
    id_identidade integer NOT NULL,
    id_tp_identidade integer,
    id_pessoa integer,
    data_criacao_identidade timestamp with time zone,
    data_expiracao_identidade timestamp with time zone,
    data_cancelamento_identidade timestamp with time zone,
    id_orgao_usu integer,
    login_identidade character varying(20),
    senha_identidade character varying(40),
    senha_identidade_cripto character varying(255),
    senha_identidade_cripto_sinc character varying(255),
    his_id_ini integer,
    his_dt_ini timestamp with time zone NOT NULL,
    his_idc_ini integer,
    his_dt_fim timestamp with time zone,
    his_idc_fim integer,
    his_ativo smallint NOT NULL
);

CREATE SEQUENCE corporativo.cp_identidade_id_identidade_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_localidade (
    id_localidade integer NOT NULL,
    nm_localidade character varying(256) NOT NULL,
    id_uf integer NOT NULL
);

CREATE SEQUENCE corporativo.cp_localidade_id_localidade_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_marca (
    id_marca integer NOT NULL,
    dt_ini_marca timestamp with time zone,
    dt_fim_marca timestamp with time zone,
    id_marcador integer,
    id_pessoa_ini integer,
    id_lotacao_ini integer,
    id_mobil integer,
    id_tp_marca integer,
    id_ref integer,
    id_mov integer
);

COMMENT ON COLUMN corporativo.cp_marca.id_mov IS 'Referencia a movimentação que produziu essa marca';

CREATE SEQUENCE corporativo.cp_marca_id_marca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_marcador (
    id_marcador integer NOT NULL,
    descr_marcador character varying(40) NOT NULL,
    id_finalidade_marcador integer NOT NULL,
    ord_marcador integer,
    grupo_marcador smallint,
    id_lotacao_ini integer,
    descr_detalhada character varying(255),
    id_cor smallint,
    id_icone smallint,
    his_id_ini integer,
    his_dt_ini timestamp with time zone,
    his_idc_ini integer,
    his_dt_fim timestamp with time zone,
    his_idc_fim integer,
    his_ativo smallint
);

COMMENT ON COLUMN corporativo.cp_marcador.grupo_marcador IS 'Grupo (pasta) do Marcador na mesa virtual';

COMMENT ON COLUMN corporativo.cp_marcador.id_lotacao_ini IS 'Id inicial da lotação que o marcador pertence (para marcas da lotação)';

COMMENT ON COLUMN corporativo.cp_marcador.descr_detalhada IS 'Descrição do marcador';

COMMENT ON COLUMN corporativo.cp_marcador.id_cor IS 'Cor (hexa) do marcador';

COMMENT ON COLUMN corporativo.cp_marcador.id_icone IS 'Código do ícone do marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_id_ini IS 'Id do marcador inicial deste marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_dt_ini IS 'Data de criação ou alteração do marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_idc_ini IS 'Id da identidade da pessoa que cadastrou o marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_dt_fim IS 'Data de finalização ou fim de determinada situação do marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_idc_fim IS 'Id da identidade da pessoa que finalizou o marcador';

COMMENT ON COLUMN corporativo.cp_marcador.his_ativo IS 'Identifica se está ativo ou não';

CREATE SEQUENCE corporativo.cp_marcador_id_marcador_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_modelo (
    id_modelo integer NOT NULL,
    id_orgao_usu integer,
    id_arq bigint,
    conteudo_blob_mod bytea,
    his_id_ini integer,
    his_dt_ini timestamp with time zone NOT NULL,
    his_idc_ini integer,
    his_dt_fim timestamp with time zone,
    his_idc_fim integer,
    his_ativo smallint NOT NULL
);

CREATE SEQUENCE corporativo.cp_modelo_id_modelo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_ocorrencia_feriado (
    id_ocorrencia integer NOT NULL,
    dt_ini_feriado timestamp with time zone,
    dt_fim_feriado timestamp with time zone,
    id_feriado integer
);

CREATE SEQUENCE corporativo.cp_ocorrencia_feriado_id_ocorrencia_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_orgao (
    id_orgao integer NOT NULL,
    nm_orgao character varying(256) NOT NULL,
    cgc_orgao integer,
    razao_social_orgao character varying(256),
    end_orgao character varying(256),
    bairro_orgao character varying(50),
    municipio_orgao character varying(50),
    cep_orgao character varying(8),
    dsc_tipo_orgao character varying(100),
    nome_responsavel_orgao character varying(60),
    email_responsavel_orgao character varying(60),
    nome_contato_orgao character varying(60),
    email_contato_orgao character varying(60),
    tel_contato_orgao character varying(10),
    sigla_orgao character varying(30),
    uf_orgao character varying(2),
    id_orgao_usu integer NOT NULL,
    fg_ativo character varying(1),
    his_id_ini integer,
    his_ide character varying(256),
    his_dt_ini timestamp with time zone,
    his_dt_fim timestamp with time zone,
    his_ativo integer
);

CREATE SEQUENCE corporativo.cp_orgao_id_orgao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_orgao_usuario (
    id_orgao_usu integer NOT NULL,
    nm_orgao_usu character varying(256) NOT NULL,
    cgc_orgao_usu bigint,
    razao_social_orgao_usu character varying(256),
    end_orgao_usu character varying(256),
    bairro_orgao_usu character varying(50),
    municipio_orgao_usu character varying(50),
    cep_orgao_usu character varying(8),
    nm_resp_orgao_usu character varying(60),
    tel_orgao_usu character varying(10),
    sigla_orgao_usu character varying(15),
    uf_orgao_usu character varying(2),
    cod_orgao_usu integer,
    acronimo_orgao_usu character varying(12),
    is_externo_orgao_usu smallint DEFAULT 0,
    his_dt_ini date,
    his_dt_fim date,
    his_idc_ini integer,
    his_idc_fim integer,
    his_ativo smallint NOT NULL
);

CREATE SEQUENCE corporativo.cp_orgao_usuario_id_orgao_usu_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_papel (
    id_papel integer NOT NULL,
    id_tp_papel integer NOT NULL,
    id_pessoa integer NOT NULL,
    id_lotacao integer NOT NULL,
    id_funcao_confianca integer,
    id_cargo integer NOT NULL,
    id_orgao_usu integer NOT NULL,
    his_id_ini integer,
    his_ide character varying(256),
    his_dt_ini timestamp with time zone NOT NULL,
    his_dt_fim timestamp with time zone,
    his_ativo smallint NOT NULL
);

CREATE SEQUENCE corporativo.cp_papel_id_papel_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_personalizacao (
    id_pessoa integer NOT NULL,
    id_papel_ativo integer,
    id_substituindo_pessoa integer,
    id_substituindo_lotacao integer,
    id_substituindo_papel integer,
    nm_simulando_usuario character varying(15),
    id_personalizacao integer NOT NULL
);

CREATE SEQUENCE corporativo.cp_personalizacao_id_personalizacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_sede (
    id_sede integer NOT NULL,
    nm_sede character varying(256) NOT NULL,
    dsc_sede character varying(512),
    id_orgao_usu integer NOT NULL
);

CREATE SEQUENCE corporativo.cp_sede_id_sede_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_servico (
    id_servico integer NOT NULL,
    sigla_servico character varying(40) NOT NULL,
    desc_servico character varying(200) NOT NULL,
    id_servico_pai integer,
    id_tp_servico integer NOT NULL,
    label_servico character varying(35)
);

CREATE SEQUENCE corporativo.cp_servico_id_servico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_situacao_configuracao (
    id_sit_configuracao integer NOT NULL,
    dsc_sit_configuracao character varying(255),
    restritividade_sit_conf bigint NOT NULL
);

CREATE SEQUENCE corporativo.cp_situacao_configuracao_id_sit_configuracao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_configuracao (
    id_tp_configuracao integer NOT NULL,
    dsc_tp_configuracao character varying(255),
    id_sit_configuracao integer
);

CREATE SEQUENCE corporativo.cp_tipo_configuracao_id_tp_configuracao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_grupo (
    id_tp_grupo integer NOT NULL,
    desc_tp_grupo character varying(200)
);

CREATE SEQUENCE corporativo.cp_tipo_grupo_id_tp_grupo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_identidade (
    id_tp_identidade integer NOT NULL,
    desc_tp_identidade character varying(60)
);

CREATE SEQUENCE corporativo.cp_tipo_identidade_id_tp_identidade_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_lotacao (
    id_tp_lotacao integer NOT NULL,
    sigla_tp_lotacao character varying(40),
    desc_tp_lotacao character varying(200),
    id_tp_lotacao_pai integer
);

CREATE SEQUENCE corporativo.cp_tipo_lotacao_id_tp_lotacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_marca (
    id_tp_marca integer NOT NULL,
    descr_tp_marca character varying(30)
);

CREATE SEQUENCE corporativo.cp_tipo_marca_id_tp_marca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_marcador (
    id_tp_marcador integer NOT NULL,
    descr_tipo_marcador character varying(30)
);

CREATE SEQUENCE corporativo.cp_tipo_marcador_id_tp_marcador_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_papel (
    id_tp_papel integer NOT NULL,
    desc_tp_papel character varying(60)
);

CREATE SEQUENCE corporativo.cp_tipo_papel_id_tp_papel_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_pessoa (
    id_tp_pessoa integer NOT NULL,
    desc_tp_pessoa character varying(60)
);

CREATE SEQUENCE corporativo.cp_tipo_pessoa_id_tp_pessoa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_servico (
    id_tp_servico integer NOT NULL,
    desc_tp_servico character varying(60),
    id_sit_configuracao integer
);

CREATE SEQUENCE corporativo.cp_tipo_servico_id_tp_servico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_tipo_servico_situacao (
    id_tp_servico integer DEFAULT 0 NOT NULL,
    id_sit_configuracao integer DEFAULT 0 NOT NULL
);

CREATE TABLE corporativo.cp_tipo_token (
    id_tp_token integer NOT NULL,
    descr_tp_token character varying(30)
);

CREATE SEQUENCE corporativo.cp_tipo_token_id_tp_token_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_token (
    id_token integer NOT NULL,
    id_ref integer NOT NULL,
    id_tp_token integer NOT NULL,
    dt_iat date NOT NULL,
    dt_exp date,
    token character varying(256) NOT NULL
);

CREATE SEQUENCE corporativo.cp_token_id_token_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_uf (
    id_uf integer NOT NULL,
    nm_uf character varying(256) NOT NULL
);

CREATE SEQUENCE corporativo.cp_uf_id_uf_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_unidade_medida (
    id_unidade_medida integer NOT NULL,
    descr_unidade_medida character varying(10)
);

CREATE SEQUENCE corporativo.cp_unidade_medida_id_unidade_medida_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_cargo (
    id_cargo integer NOT NULL,
    nome_cargo character varying(100) NOT NULL,
    id_orgao_usu integer NOT NULL,
    dt_fim_cargo timestamp with time zone,
    dt_ini_cargo timestamp with time zone,
    id_cargo_inicial integer,
    ide_cargo character varying(256),
    sigla_cargo character varying(30)
);

CREATE SEQUENCE corporativo.dp_cargo_id_cargo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_estado_civil (
    id_estado_civil integer NOT NULL,
    nm_estado_civil character varying(20) NOT NULL
);

CREATE SEQUENCE corporativo.dp_estado_civil_id_estado_civil_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_funcao_confianca (
    id_funcao_confianca integer NOT NULL,
    nome_funcao_confianca character varying(100) NOT NULL,
    nivel_funcao_confianca smallint,
    cod_folha_funcao_confianca smallint,
    dt_ini_funcao_confianca timestamp with time zone,
    dt_fim_funcao_confianca timestamp with time zone,
    id_funcao_confianca_pai integer,
    categoria_funcao_confianca character varying(15),
    id_orgao_usu integer NOT NULL,
    ide_funcao_confianca character varying(256),
    id_fun_conf_ini integer,
    sigla_funcao_confianca character varying(30)
);

CREATE SEQUENCE corporativo.dp_funcao_confianca_id_funcao_confianca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_lotacao (
    id_lotacao integer NOT NULL,
    data_ini_lot timestamp with time zone NOT NULL,
    data_fim_lot timestamp with time zone,
    nome_lotacao character varying(120) NOT NULL,
    id_lotacao_pai integer,
    sigla_lotacao character varying(20),
    id_orgao_usu integer NOT NULL,
    ide_lotacao character varying(256),
    id_lotacao_ini integer,
    id_tp_lotacao integer,
    id_localidade integer,
    is_externa_lotacao smallint DEFAULT 0,
    his_idc_ini integer,
    his_idc_fim integer,
    is_suspensa smallint DEFAULT 0
);

COMMENT ON COLUMN corporativo.dp_lotacao.his_idc_ini IS 'Id identidade da pessoa que cadastrou';

COMMENT ON COLUMN corporativo.dp_lotacao.his_idc_fim IS 'Id identidade da pessoa que finalizou';

CREATE SEQUENCE corporativo.dp_lotacao_id_lotacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_padrao_referencia (
    id_padrao_referencia integer NOT NULL,
    id_padrao_referencia_pai smallint,
    dsc_padrao character varying(3) NOT NULL,
    dsc_classe character varying(20),
    dsc_nivel character varying(2),
    padrao_referencia_dt_fim timestamp with time zone,
    id_orgao_usu integer DEFAULT 1 NOT NULL
);

CREATE SEQUENCE corporativo.dp_padrao_referencia_id_padrao_referencia_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_pessoa (
    id_pessoa integer NOT NULL,
    data_ini_pessoa timestamp with time zone NOT NULL,
    data_fim_pessoa timestamp with time zone,
    cpf_pessoa bigint NOT NULL,
    nome_pessoa character varying(60) NOT NULL,
    data_nasc_pessoa timestamp with time zone,
    matricula integer NOT NULL,
    id_lotacao integer NOT NULL,
    id_cargo integer,
    id_funcao_confianca integer,
    sesb_pessoa character varying(5),
    email_pessoa character varying(60),
    tp_servidor_pessoa smallint,
    sigla_pessoa character varying(20),
    sexo_pessoa character varying(1),
    grau_instrucao_pessoa character varying(50),
    tp_sanguineo_pessoa character varying(3),
    nacionalidade_pessoa character varying(60),
    data_posse_pessoa timestamp with time zone,
    data_nomeacao_pessoa timestamp with time zone,
    data_publicacao_pessoa timestamp with time zone,
    data_inicio_exercicio_pessoa timestamp with time zone,
    ato_nomeacao_pessoa character varying(50),
    situacao_funcional_pessoa character varying(50),
    id_provimento smallint,
    naturalidade_pessoa character varying(60),
    fg_imprime_end character varying(1),
    dsc_padrao_referencia_pessoa character varying(16),
    id_orgao_usu integer NOT NULL,
    ide_pessoa character varying(256),
    id_pessoa_inicial integer,
    endereco_pessoa character varying(100),
    bairro_pessoa character varying(50),
    cidade_pessoa character varying(30),
    cep_pessoa character varying(8),
    telefone_pessoa character varying(30),
    rg_pessoa character varying(20),
    rg_orgao_pessoa character varying(50),
    rg_data_expedicao_pessoa timestamp with time zone,
    rg_uf_pessoa character varying(2),
    id_estado_civil integer,
    id_tp_pessoa integer,
    nome_exibicao character varying(255),
    his_idc_ini integer,
    his_idc_fim integer,
    nome_pessoa_ai character varying(60)
);

CREATE SEQUENCE corporativo.dp_pessoa_id_pessoa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_provimento (
    id_provimento integer NOT NULL,
    dsc_provimento character varying(60) NOT NULL
);

CREATE SEQUENCE corporativo.dp_provimento_id_provimento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_substituicao (
    id_substituicao integer NOT NULL,
    id_titular integer,
    id_lota_titular integer NOT NULL,
    id_substituto integer,
    id_lota_substituto integer NOT NULL,
    dt_ini_subst timestamp with time zone NOT NULL,
    dt_fim_subst timestamp with time zone,
    dt_ini_reg timestamp with time zone,
    dt_fim_reg timestamp with time zone,
    id_reg_ini integer,
    tp_substituicao character(1)
);

CREATE SEQUENCE corporativo.dp_substituicao_id_substituicao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.dp_visualizacao (
    id_visualizacao integer NOT NULL,
    id_titular integer,
    id_deleg integer,
    dt_ini_deleg date,
    dt_fim_deleg date,
    dt_ini_reg date,
    dt_fim_reg date,
    id_reg_ini integer
);

CREATE TABLE corporativo.dp_visualizacao_acesso (
    id_visualizacao_acesso integer NOT NULL,
    id_visualizacao integer,
    id_titular integer,
    id_deleg integer,
    dt_acesso date,
    id_doc integer
);

CREATE SEQUENCE corporativo.dp_visualizacao_acesso_id_visualizacao_acesso_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE corporativo.dp_visualizacao_id_visualizacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE corporativo.cp_contrato (
    ID_ORGAO_USU BIGINT not null,
    DT_CONTRATO timestamp without time zone,
);

ALTER TABLE ONLY corporativo.cad_sit_funcional ALTER COLUMN id_cad_sit_funcional SET DEFAULT nextval('corporativo.cad_sit_funcional_id_cad_sit_funcional_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_acesso ALTER COLUMN id_acesso SET DEFAULT nextval('corporativo.cp_acesso_id_acesso_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ALTER COLUMN id_aplicacao SET DEFAULT nextval('corporativo.cp_aplicacao_feriado_id_aplicacao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_arquivo ALTER COLUMN id_arq SET DEFAULT nextval('corporativo.cp_arquivo_id_arq_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_configuracao ALTER COLUMN id_configuracao SET DEFAULT nextval('corporativo.cp_configuracao_id_configuracao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_feriado ALTER COLUMN id_feriado SET DEFAULT nextval('corporativo.cp_feriado_id_feriado_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_grupo ALTER COLUMN id_grupo SET DEFAULT nextval('corporativo.cp_grupo_id_grupo_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_identidade ALTER COLUMN id_identidade SET DEFAULT nextval('corporativo.cp_identidade_id_identidade_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_localidade ALTER COLUMN id_localidade SET DEFAULT nextval('corporativo.cp_localidade_id_localidade_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_marca ALTER COLUMN id_marca SET DEFAULT nextval('corporativo.cp_marca_id_marca_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_marcador ALTER COLUMN id_marcador SET DEFAULT nextval('corporativo.cp_marcador_id_marcador_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_modelo ALTER COLUMN id_modelo SET DEFAULT nextval('corporativo.cp_modelo_id_modelo_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_ocorrencia_feriado ALTER COLUMN id_ocorrencia SET DEFAULT nextval('corporativo.cp_ocorrencia_feriado_id_ocorrencia_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_orgao ALTER COLUMN id_orgao SET DEFAULT nextval('corporativo.cp_orgao_id_orgao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_orgao_usuario ALTER COLUMN id_orgao_usu SET DEFAULT nextval('corporativo.cp_orgao_usuario_id_orgao_usu_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_papel ALTER COLUMN id_papel SET DEFAULT nextval('corporativo.cp_papel_id_papel_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_personalizacao ALTER COLUMN id_personalizacao SET DEFAULT nextval('corporativo.cp_personalizacao_id_personalizacao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_sede ALTER COLUMN id_sede SET DEFAULT nextval('corporativo.cp_sede_id_sede_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_servico ALTER COLUMN id_servico SET DEFAULT nextval('corporativo.cp_servico_id_servico_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_situacao_configuracao ALTER COLUMN id_sit_configuracao SET DEFAULT nextval('corporativo.cp_situacao_configuracao_id_sit_configuracao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_configuracao ALTER COLUMN id_tp_configuracao SET DEFAULT nextval('corporativo.cp_tipo_configuracao_id_tp_configuracao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_grupo ALTER COLUMN id_tp_grupo SET DEFAULT nextval('corporativo.cp_tipo_grupo_id_tp_grupo_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_identidade ALTER COLUMN id_tp_identidade SET DEFAULT nextval('corporativo.cp_tipo_identidade_id_tp_identidade_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_lotacao ALTER COLUMN id_tp_lotacao SET DEFAULT nextval('corporativo.cp_tipo_lotacao_id_tp_lotacao_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_marca ALTER COLUMN id_tp_marca SET DEFAULT nextval('corporativo.cp_tipo_marca_id_tp_marca_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_marcador ALTER COLUMN id_tp_marcador SET DEFAULT nextval('corporativo.cp_tipo_marcador_id_tp_marcador_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_papel ALTER COLUMN id_tp_papel SET DEFAULT nextval('corporativo.cp_tipo_papel_id_tp_papel_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_pessoa ALTER COLUMN id_tp_pessoa SET DEFAULT nextval('corporativo.cp_tipo_pessoa_id_tp_pessoa_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_servico ALTER COLUMN id_tp_servico SET DEFAULT nextval('corporativo.cp_tipo_servico_id_tp_servico_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_tipo_token ALTER COLUMN id_tp_token SET DEFAULT nextval('corporativo.cp_tipo_token_id_tp_token_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_token ALTER COLUMN id_token SET DEFAULT nextval('corporativo.cp_token_id_token_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_uf ALTER COLUMN id_uf SET DEFAULT nextval('corporativo.cp_uf_id_uf_seq'::regclass);

ALTER TABLE ONLY corporativo.cp_unidade_medida ALTER COLUMN id_unidade_medida SET DEFAULT nextval('corporativo.cp_unidade_medida_id_unidade_medida_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_cargo ALTER COLUMN id_cargo SET DEFAULT nextval('corporativo.dp_cargo_id_cargo_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_estado_civil ALTER COLUMN id_estado_civil SET DEFAULT nextval('corporativo.dp_estado_civil_id_estado_civil_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_funcao_confianca ALTER COLUMN id_funcao_confianca SET DEFAULT nextval('corporativo.dp_funcao_confianca_id_funcao_confianca_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_lotacao ALTER COLUMN id_lotacao SET DEFAULT nextval('corporativo.dp_lotacao_id_lotacao_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_padrao_referencia ALTER COLUMN id_padrao_referencia SET DEFAULT nextval('corporativo.dp_padrao_referencia_id_padrao_referencia_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_pessoa ALTER COLUMN id_pessoa SET DEFAULT nextval('corporativo.dp_pessoa_id_pessoa_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_provimento ALTER COLUMN id_provimento SET DEFAULT nextval('corporativo.dp_provimento_id_provimento_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_substituicao ALTER COLUMN id_substituicao SET DEFAULT nextval('corporativo.dp_substituicao_id_substituicao_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_visualizacao ALTER COLUMN id_visualizacao SET DEFAULT nextval('corporativo.dp_visualizacao_id_visualizacao_seq'::regclass);

ALTER TABLE ONLY corporativo.dp_visualizacao_acesso ALTER COLUMN id_visualizacao_acesso SET DEFAULT nextval('corporativo.dp_visualizacao_acesso_id_visualizacao_acesso_seq'::regclass);

CREATE OR REPLACE FUNCTION public.remove_acento(
	acentuado character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
 sem_acento VARCHAR(4000);
 BEGIN
    sem_acento := translate($1, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC');
	RETURN	sem_acento;
 EXCEPTION
	WHEN OTHERS THEN
		null;		
 END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fun_numeracao_documento(
	pid_orgao_usu bigint,
	pid_forma_doc bigint,
	pano_emissao bigint)
    RETURNS bigint
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
	  v_numero_documento bigint := 1; 
	  v_id_doc_num bigint; 
	BEGIN 
	    SELECT id_documento_numeracao 
	    INTO   v_id_doc_num 
	    FROM   ex_documento_numeracao 
	    WHERE  id_orgao_usu = pId_orgao_usu 
	           AND id_forma_doc = pId_forma_doc 
	           AND ano_emissao = pAno_emissao
	           AND fl_ativo = 1
	    FOR UPDATE;  /* Lock Registro para MUTEX */ 
	
	    UPDATE ex_documento_numeracao 
	    SET    nr_documento = nr_documento + 1
	    WHERE  id_documento_numeracao = v_id_doc_num
	    RETURNING nr_documento
	    INTO v_numero_documento; 
	
	    RETURN v_numero_documento; 
	
	    EXCEPTION  WHEN NO_DATA_FOUND THEN
	    	--TODO: Criar rotina para verificar se número reseta todo ano. Se não, número incial igual ao numero final do ano anterior
	        INSERT INTO ex_documento_numeracao 
	                       (id_orgao_usu, id_forma_doc, ano_emissao, nr_documento) 
	               VALUES  (pid_orgao_usu, pid_forma_doc, pano_emissao, v_numero_documento); 
	        RETURN v_numero_documento;       
	
	END;
$BODY$;

CREATE OR REPLACE FUNCTION public.num_expediente_fun(
	pid_orgao_usu bigint,
	pid_forma_doc bigint,
	pano_emissao bigint)
    RETURNS bigint
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
 NR_EXPEDIENTE bigint := 0 ;
 --PID_NUMERACAO bigint := PID_FORMA_DOC ;
 BEGIN
     SELECT COUNT(NUM_EXPEDIENTE) into NR_EXPEDIENTE
     FROM   EX_NUMERACAO
     WHERE  ID_ORGAO_USU = pID_ORGAO_USU
     AND    ID_FORMA_DOC = pID_FORMA_DOC
     AND    ANO_EMISSAO = pANO_EMISSAO;

     IF NR_EXPEDIENTE = 0 then
        NR_EXPEDIENTE := 1;
        INSERT INTO EX_NUMERACAO(ID_ORGAO_USU,ID_FORMA_DOC,ANO_EMISSAO,NUM_EXPEDIENTE)
        VALUES(pID_ORGAO_USU,pID_FORMA_DOC,pANO_EMISSAO,NR_EXPEDIENTE);

     ELSE

        SELECT NUM_EXPEDIENTE into NR_EXPEDIENTE
        FROM   EX_NUMERACAO
        WHERE  ID_ORGAO_USU = pID_ORGAO_USU
        AND    ID_FORMA_DOC = pID_FORMA_DOC
        AND    ANO_EMISSAO = pANO_EMISSAO;

        NR_EXPEDIENTE := NR_EXPEDIENTE + 1;

        Update EX_NUMERACAO
        SET NUM_EXPEDIENTE = NR_EXPEDIENTE
        WHERE ID_ORGAO_USU = pID_ORGAO_USU
        AND   ID_FORMA_DOC = pID_FORMA_DOC
        AND   ANO_EMISSAO  = pANO_EMISSAO;
     END IF;
     COMMIT ;
     RETURN NR_EXPEDIENTE;

 END;
$BODY$;
