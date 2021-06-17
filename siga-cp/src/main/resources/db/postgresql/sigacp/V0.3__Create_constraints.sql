/*
 * PK
 */

ALTER TABLE ONLY corporativo.cad_sit_funcional ADD CONSTRAINT cad_sit_funcional_pkey PRIMARY KEY (id_cad_sit_funcional);

ALTER TABLE ONLY corporativo.cp_acesso ADD CONSTRAINT cp_acesso_pkey PRIMARY KEY (id_acesso);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ADD CONSTRAINT cp_aplicacao_feriado_pkey PRIMARY KEY (id_aplicacao);

ALTER TABLE ONLY corporativo.cp_arquivo_blob ADD CONSTRAINT cp_arquivo_blob_pkey PRIMARY KEY (id_arq_blob);

ALTER TABLE ONLY corporativo.cp_arquivo_excluir ADD CONSTRAINT cp_arquivo_excluir_pkey PRIMARY KEY (id_arq_exc);

ALTER TABLE ONLY corporativo.cp_arquivo ADD CONSTRAINT cp_arquivo_pkey PRIMARY KEY (id_arq);

ALTER TABLE ONLY corporativo.cp_complexo ADD CONSTRAINT cp_complexo_pkey PRIMARY KEY (id_complexo);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_pkey PRIMARY KEY (id_configuracao);

ALTER TABLE ONLY corporativo.cp_feriado ADD CONSTRAINT cp_feriado_pkey PRIMARY KEY (id_feriado);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_pkey PRIMARY KEY (id_grupo);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_pkey PRIMARY KEY (id_identidade);

ALTER TABLE ONLY corporativo.cp_localidade ADD CONSTRAINT cp_localidade_pkey PRIMARY KEY (id_localidade);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_pkey PRIMARY KEY (id_marca);

ALTER TABLE ONLY corporativo.cp_marcador ADD CONSTRAINT cp_marcador_pkey PRIMARY KEY (id_marcador);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_pkey PRIMARY KEY (id_modelo);

ALTER TABLE ONLY corporativo.cp_ocorrencia_feriado ADD CONSTRAINT cp_ocorrencia_feriado_pkey PRIMARY KEY (id_ocorrencia);

ALTER TABLE ONLY corporativo.cp_orgao ADD CONSTRAINT cp_orgao_pkey PRIMARY KEY (id_orgao);

ALTER TABLE ONLY corporativo.cp_orgao_usuario ADD CONSTRAINT cp_orgao_usuario_pkey PRIMARY KEY (id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_pkey PRIMARY KEY (id_papel);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_pkey PRIMARY KEY (id_personalizacao);

ALTER TABLE ONLY corporativo.cp_sede ADD CONSTRAINT cp_sede_pkey PRIMARY KEY (id_sede);

ALTER TABLE ONLY corporativo.cp_servico ADD CONSTRAINT cp_servico_pkey PRIMARY KEY (id_servico);

ALTER TABLE ONLY corporativo.cp_situacao_configuracao ADD CONSTRAINT cp_situacao_configuracao_pkey PRIMARY KEY (id_sit_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_configuracao ADD CONSTRAINT cp_tipo_configuracao_pkey PRIMARY KEY (id_tp_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_grupo ADD CONSTRAINT cp_tipo_grupo_pkey PRIMARY KEY (id_tp_grupo);

ALTER TABLE ONLY corporativo.cp_tipo_identidade ADD CONSTRAINT cp_tipo_identidade_pkey PRIMARY KEY (id_tp_identidade);

ALTER TABLE ONLY corporativo.cp_tipo_lotacao ADD CONSTRAINT cp_tipo_lotacao_pkey PRIMARY KEY (id_tp_lotacao);

ALTER TABLE ONLY corporativo.cp_tipo_marca ADD CONSTRAINT cp_tipo_marca_pkey PRIMARY KEY (id_tp_marca);

ALTER TABLE ONLY corporativo.cp_tipo_marcador ADD CONSTRAINT cp_tipo_marcador_pkey PRIMARY KEY (id_tp_marcador);

ALTER TABLE ONLY corporativo.cp_tipo_papel ADD CONSTRAINT cp_tipo_papel_pkey PRIMARY KEY (id_tp_papel);

ALTER TABLE ONLY corporativo.cp_tipo_pessoa ADD CONSTRAINT cp_tipo_pessoa_pkey PRIMARY KEY (id_tp_pessoa);

ALTER TABLE ONLY corporativo.cp_tipo_servico ADD CONSTRAINT cp_tipo_servico_pkey PRIMARY KEY (id_tp_servico);

ALTER TABLE ONLY corporativo.cp_tipo_servico_situacao ADD CONSTRAINT cp_tipo_servico_situacao_pkey PRIMARY KEY (id_tp_servico, id_sit_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_token ADD CONSTRAINT cp_tipo_token_pkey PRIMARY KEY (id_tp_token);

ALTER TABLE ONLY corporativo.cp_token ADD CONSTRAINT cp_token_pkey PRIMARY KEY (id_token);

ALTER TABLE ONLY corporativo.cp_uf ADD CONSTRAINT cp_uf_pkey PRIMARY KEY (id_uf);

ALTER TABLE ONLY corporativo.cp_unidade_medida ADD CONSTRAINT cp_unidade_medida_pkey PRIMARY KEY (id_unidade_medida);

ALTER TABLE ONLY corporativo.dp_cargo ADD CONSTRAINT dp_cargo_pkey PRIMARY KEY (id_cargo);

ALTER TABLE ONLY corporativo.dp_estado_civil ADD CONSTRAINT dp_estado_civil_pkey PRIMARY KEY (id_estado_civil);

ALTER TABLE ONLY corporativo.dp_funcao_confianca ADD CONSTRAINT dp_funcao_confianca_pkey PRIMARY KEY (id_funcao_confianca);

ALTER TABLE ONLY corporativo.dp_lotacao ADD CONSTRAINT dp_lotacao_pkey PRIMARY KEY (id_lotacao);

ALTER TABLE ONLY corporativo.dp_padrao_referencia ADD CONSTRAINT dp_padrao_referencia_pkey PRIMARY KEY (id_padrao_referencia);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_pkey PRIMARY KEY (id_pessoa);

ALTER TABLE ONLY corporativo.dp_provimento ADD CONSTRAINT dp_provimento_pkey PRIMARY KEY (id_provimento);

ALTER TABLE ONLY corporativo.dp_substituicao ADD CONSTRAINT dp_substituicao_pkey PRIMARY KEY (id_substituicao);

ALTER TABLE ONLY corporativo.dp_visualizacao_acesso ADD CONSTRAINT dp_visualizacao_acesso_pkey PRIMARY KEY (id_visualizacao_acesso);

ALTER TABLE ONLY corporativo.dp_visualizacao ADD CONSTRAINT dp_visualizacao_pkey PRIMARY KEY (id_visualizacao);

/*
 * FK
 */

ALTER TABLE ONLY corporativo.cp_acesso ADD CONSTRAINT cp_acesso_id_identidade_fkey FOREIGN KEY (id_identidade) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ADD CONSTRAINT cp_aplicacao_feriado_id_localidade_fkey FOREIGN KEY (id_localidade) REFERENCES corporativo.cp_localidade(id_localidade);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ADD CONSTRAINT cp_aplicacao_feriado_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ADD CONSTRAINT cp_aplicacao_feriado_id_ocorrencia_feriado_fkey FOREIGN KEY (id_ocorrencia_feriado) REFERENCES corporativo.cp_ocorrencia_feriado(id_ocorrencia);

ALTER TABLE ONLY corporativo.cp_aplicacao_feriado ADD CONSTRAINT cp_aplicacao_feriado_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_arquivo ADD CONSTRAINT cp_arquivo_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_complexo ADD CONSTRAINT cp_complexo_id_localidade_fkey FOREIGN KEY (id_localidade) REFERENCES corporativo.cp_localidade(id_localidade);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_cargo_fkey FOREIGN KEY (id_cargo) REFERENCES corporativo.dp_cargo(id_cargo);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_complexo_fkey FOREIGN KEY (id_complexo) REFERENCES corporativo.cp_complexo(id_complexo);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_funcao_confianca_fkey FOREIGN KEY (id_funcao_confianca) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_grupo_fkey FOREIGN KEY (id_grupo) REFERENCES corporativo.cp_grupo(id_grupo);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_identidade_fkey FOREIGN KEY (id_identidade) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_servico_fkey FOREIGN KEY (id_servico) REFERENCES corporativo.cp_servico(id_servico);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_sit_configuracao_fkey FOREIGN KEY (id_sit_configuracao) REFERENCES corporativo.cp_situacao_configuracao(id_sit_configuracao);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_tp_configuracao_fkey FOREIGN KEY (id_tp_configuracao) REFERENCES corporativo.cp_tipo_configuracao(id_tp_configuracao);

ALTER TABLE ONLY corporativo.cp_configuracao ADD CONSTRAINT cp_configuracao_id_tp_lotacao_fkey FOREIGN KEY (id_tp_lotacao) REFERENCES corporativo.cp_tipo_lotacao(id_tp_lotacao);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES corporativo.cp_grupo(id_grupo);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_id_grupo_pai_fkey FOREIGN KEY (id_grupo_pai) REFERENCES corporativo.cp_grupo(id_grupo);

ALTER TABLE ONLY corporativo.cp_grupo ADD CONSTRAINT cp_grupo_id_tp_grupo_fkey FOREIGN KEY (id_tp_grupo) REFERENCES corporativo.cp_tipo_grupo(id_tp_grupo);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_identidade ADD CONSTRAINT cp_identidade_id_tp_identidade_fkey FOREIGN KEY (id_tp_identidade) REFERENCES corporativo.cp_tipo_identidade(id_tp_identidade);

ALTER TABLE ONLY corporativo.cp_localidade ADD CONSTRAINT cp_localidade_id_uf_fkey FOREIGN KEY (id_uf) REFERENCES corporativo.cp_uf(id_uf);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_lotacao_ini_fkey FOREIGN KEY (id_lotacao_ini) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_marcador_fkey FOREIGN KEY (id_marcador) REFERENCES corporativo.cp_marcador(id_marcador);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_pessoa_ini_fkey FOREIGN KEY (id_pessoa_ini) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_tp_marca_fkey FOREIGN KEY (id_tp_marca) REFERENCES corporativo.cp_tipo_marca(id_tp_marca);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES corporativo.cp_modelo(id_modelo);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_id_arq_fkey FOREIGN KEY (id_arq) REFERENCES corporativo.cp_arquivo(id_arq);

ALTER TABLE ONLY corporativo.cp_modelo ADD CONSTRAINT cp_modelo_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_ocorrencia_feriado ADD CONSTRAINT cp_ocorrencia_feriado_id_feriado_fkey FOREIGN KEY (id_feriado) REFERENCES corporativo.cp_feriado(id_feriado);

ALTER TABLE ONLY corporativo.cp_orgao ADD CONSTRAINT cp_orgao_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_orgao_usuario ADD CONSTRAINT cp_orgao_usuario_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_orgao_usuario ADD CONSTRAINT cp_orgao_usuario_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES corporativo.cp_papel(id_papel);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_cargo_fkey FOREIGN KEY (id_cargo) REFERENCES corporativo.dp_cargo(id_cargo);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_funcao_confianca_fkey FOREIGN KEY (id_funcao_confianca) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_papel ADD CONSTRAINT cp_papel_id_tp_papel_fkey FOREIGN KEY (id_tp_papel) REFERENCES corporativo.cp_tipo_papel(id_tp_papel);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_id_papel_ativo_fkey FOREIGN KEY (id_papel_ativo) REFERENCES corporativo.cp_papel(id_papel);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_id_substituindo_lotacao_fkey FOREIGN KEY (id_substituindo_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_id_substituindo_papel_fkey FOREIGN KEY (id_substituindo_papel) REFERENCES corporativo.cp_papel(id_papel);

ALTER TABLE ONLY corporativo.cp_personalizacao ADD CONSTRAINT cp_personalizacao_id_substituindo_pessoa_fkey FOREIGN KEY (id_substituindo_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.cp_sede ADD CONSTRAINT cp_sede_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.cp_servico ADD CONSTRAINT cp_servico_id_tp_servico_fkey FOREIGN KEY (id_tp_servico) REFERENCES corporativo.cp_tipo_servico(id_tp_servico);

ALTER TABLE ONLY corporativo.cp_tipo_configuracao ADD CONSTRAINT cp_tipo_configuracao_id_sit_configuracao_fkey FOREIGN KEY (id_sit_configuracao) REFERENCES corporativo.cp_situacao_configuracao(id_sit_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_servico ADD CONSTRAINT cp_tipo_servico_id_sit_configuracao_fkey FOREIGN KEY (id_sit_configuracao) REFERENCES corporativo.cp_tipo_configuracao(id_tp_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_servico_situacao ADD CONSTRAINT cp_tipo_servico_situacao_id_sit_configuracao_fkey FOREIGN KEY (id_sit_configuracao) REFERENCES corporativo.cp_situacao_configuracao(id_sit_configuracao);

ALTER TABLE ONLY corporativo.cp_tipo_servico_situacao ADD CONSTRAINT cp_tipo_servico_situacao_id_tp_servico_fkey FOREIGN KEY (id_tp_servico) REFERENCES corporativo.cp_tipo_servico(id_tp_servico);

ALTER TABLE ONLY corporativo.cp_token ADD CONSTRAINT cp_token_id_tp_token_fkey FOREIGN KEY (id_tp_token) REFERENCES corporativo.cp_tipo_token(id_tp_token);

ALTER TABLE ONLY corporativo.dp_cargo ADD CONSTRAINT dp_cargo_id_cargo_inicial_fkey FOREIGN KEY (id_cargo_inicial) REFERENCES corporativo.dp_cargo(id_cargo);

ALTER TABLE ONLY corporativo.dp_cargo ADD CONSTRAINT dp_cargo_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.dp_funcao_confianca ADD CONSTRAINT dp_funcao_confianca_id_fun_conf_ini_fkey FOREIGN KEY (id_fun_conf_ini) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY corporativo.dp_funcao_confianca ADD CONSTRAINT dp_funcao_confianca_id_funcao_confianca_pai_fkey FOREIGN KEY (id_funcao_confianca_pai) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY corporativo.dp_funcao_confianca ADD CONSTRAINT dp_funcao_confianca_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.dp_lotacao ADD CONSTRAINT dp_lotacao_id_localidade_fkey FOREIGN KEY (id_localidade) REFERENCES corporativo.cp_localidade(id_localidade);

ALTER TABLE ONLY corporativo.dp_lotacao ADD CONSTRAINT dp_lotacao_id_lotacao_pai_fkey FOREIGN KEY (id_lotacao_pai) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.dp_lotacao ADD CONSTRAINT dp_lotacao_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.dp_lotacao ADD CONSTRAINT dp_lotacao_id_tp_lotacao_fkey FOREIGN KEY (id_tp_lotacao) REFERENCES corporativo.cp_tipo_lotacao(id_tp_lotacao);

ALTER TABLE ONLY corporativo.dp_padrao_referencia ADD CONSTRAINT dp_padrao_referencia_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_cargo_fkey FOREIGN KEY (id_cargo) REFERENCES corporativo.dp_cargo(id_cargo);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_estado_civil_fkey FOREIGN KEY (id_estado_civil) REFERENCES corporativo.dp_estado_civil(id_estado_civil);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_funcao_confianca_fkey FOREIGN KEY (id_funcao_confianca) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_pessoa_inicial_fkey FOREIGN KEY (id_pessoa_inicial) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_provimento_fkey FOREIGN KEY (id_provimento) REFERENCES corporativo.dp_provimento(id_provimento);

ALTER TABLE ONLY corporativo.dp_pessoa ADD CONSTRAINT dp_pessoa_id_tp_pessoa_fkey FOREIGN KEY (id_tp_pessoa) REFERENCES corporativo.cp_tipo_pessoa(id_tp_pessoa);

ALTER TABLE ONLY corporativo.dp_substituicao ADD CONSTRAINT dp_substituicao_id_substituto_fkey FOREIGN KEY (id_substituto) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY corporativo.dp_substituicao ADD CONSTRAINT dp_substituicao_id_titular_fkey FOREIGN KEY (id_titular) REFERENCES corporativo.dp_pessoa(id_pessoa);
