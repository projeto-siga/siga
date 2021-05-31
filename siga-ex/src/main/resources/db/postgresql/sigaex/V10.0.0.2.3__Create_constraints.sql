/*
 * PK
 */

ALTER TABLE ONLY siga.ex_boletim_doc ADD CONSTRAINT ex_boletim_doc_pkey PRIMARY KEY (id_boletim_doc);

ALTER TABLE ONLY siga.ex_classificacao ADD CONSTRAINT ex_classificacao_pkey PRIMARY KEY (id_classificacao);

ALTER TABLE ONLY siga.ex_competencia ADD CONSTRAINT ex_competencia_pkey PRIMARY KEY (fg_competencia);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_pkey PRIMARY KEY (id_configuracao_ex);

ALTER TABLE ONLY siga.ex_documento_numeracao ADD CONSTRAINT ex_documento_numeracao_pkey PRIMARY KEY (id_documento_numeracao);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_pkey PRIMARY KEY (id_doc);

ALTER TABLE ONLY siga.ex_email_notificacao ADD CONSTRAINT ex_email_notificacao_pkey PRIMARY KEY (id_email_notificacao);

ALTER TABLE ONLY siga.ex_estado_doc ADD CONSTRAINT ex_estado_doc_pkey PRIMARY KEY (id_estado_doc);

ALTER TABLE ONLY siga.ex_estado_tp_mov ADD CONSTRAINT ex_estado_tp_mov_pkey PRIMARY KEY (id_estado_doc, id_tp_mov);

ALTER TABLE ONLY siga.ex_forma_documento ADD CONSTRAINT ex_forma_documento_pkey PRIMARY KEY (id_forma_doc);

ALTER TABLE ONLY siga.ex_mobil ADD CONSTRAINT ex_mobil_pkey PRIMARY KEY (id_mobil);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_pkey PRIMARY KEY (id_mod);

ALTER TABLE ONLY siga.ex_modelo_tp_doc_publicacao ADD CONSTRAINT ex_modelo_tp_doc_publicacao_pkey PRIMARY KEY (id_mod, id_doc_publicacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_pkey PRIMARY KEY (id_mov);

ALTER TABLE ONLY siga.ex_nivel_acesso ADD CONSTRAINT ex_nivel_acesso_pkey PRIMARY KEY (id_nivel_acesso);

ALTER TABLE ONLY siga.ex_numeracao ADD CONSTRAINT ex_numeracao_pkey PRIMARY KEY (id_orgao_usu, id_forma_doc, ano_emissao);

ALTER TABLE ONLY siga.ex_papel ADD CONSTRAINT ex_papel_pkey PRIMARY KEY (id_papel);

ALTER TABLE ONLY siga.ex_preenchimento ADD CONSTRAINT ex_preenchimento_pkey PRIMARY KEY (id_preenchimento);

ALTER TABLE ONLY siga.ex_protocolo ADD CONSTRAINT ex_protocolo_pkey PRIMARY KEY (id_protocolo);

ALTER TABLE ONLY siga.ex_sequencia ADD CONSTRAINT ex_sequencia_pkey PRIMARY KEY (id_seq);

ALTER TABLE ONLY siga.ex_situacao_configuracao ADD CONSTRAINT ex_situacao_configuracao_pkey PRIMARY KEY (id_sit_configuracao);

ALTER TABLE ONLY siga.ex_temporalidade ADD CONSTRAINT ex_temporalidade_pkey PRIMARY KEY (id_temporalidade);

ALTER TABLE ONLY siga.ex_tipo_despacho ADD CONSTRAINT ex_tipo_despacho_pkey PRIMARY KEY (id_tp_despacho);

ALTER TABLE ONLY siga.ex_tipo_destinacao ADD CONSTRAINT ex_tipo_destinacao_pkey PRIMARY KEY (id_tp_destinacao);

ALTER TABLE ONLY siga.ex_tipo_documento ADD CONSTRAINT ex_tipo_documento_pkey PRIMARY KEY (id_tp_doc);

ALTER TABLE ONLY siga.ex_tipo_forma_documento ADD CONSTRAINT ex_tipo_forma_documento_pkey PRIMARY KEY (id_tipo_forma_doc);

ALTER TABLE ONLY siga.ex_tipo_mobil ADD CONSTRAINT ex_tipo_mobil_pkey PRIMARY KEY (id_tipo_mobil);

ALTER TABLE ONLY siga.ex_tipo_movimentacao ADD CONSTRAINT ex_tipo_movimentacao_pkey PRIMARY KEY (id_tp_mov);

ALTER TABLE ONLY siga.ex_tp_doc_publicacao ADD CONSTRAINT ex_tp_doc_publicacao_pkey PRIMARY KEY (id_doc_publicacao);

ALTER TABLE ONLY siga.ex_tp_mov_estado ADD CONSTRAINT ex_tp_mov_estado_pkey PRIMARY KEY (id_tp_mov, id_estado_doc);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_pkey PRIMARY KEY (id_via);

/*
 * FK
 */

ALTER TABLE ONLY siga.ex_boletim_doc ADD CONSTRAINT ex_boletim_doc_id_boletim_fkey FOREIGN KEY (id_boletim) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_boletim_doc ADD CONSTRAINT ex_boletim_doc_id_doc_fkey FOREIGN KEY (id_doc) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_classificacao ADD CONSTRAINT ex_classificacao_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_classificacao ADD CONSTRAINT ex_classificacao_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_classificacao ADD CONSTRAINT ex_classificacao_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_competencia ADD CONSTRAINT ex_competencia_id_cargo_fkey FOREIGN KEY (id_cargo) REFERENCES corporativo.dp_cargo(id_cargo);

ALTER TABLE ONLY siga.ex_competencia ADD CONSTRAINT ex_competencia_id_funcao_confianca_fkey FOREIGN KEY (id_funcao_confianca) REFERENCES corporativo.dp_funcao_confianca(id_funcao_confianca);

ALTER TABLE ONLY siga.ex_competencia ADD CONSTRAINT ex_competencia_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_competencia ADD CONSTRAINT ex_competencia_id_pessoa_fkey FOREIGN KEY (id_pessoa) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_classificacao_fkey FOREIGN KEY (id_classificacao) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_mod_fkey FOREIGN KEY (id_mod) REFERENCES siga.ex_modelo(id_mod);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_nivel_acesso_fkey FOREIGN KEY (id_nivel_acesso) REFERENCES siga.ex_nivel_acesso(id_nivel_acesso);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_papel_fkey FOREIGN KEY (id_papel) REFERENCES siga.ex_papel(id_papel);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_tp_doc_fkey FOREIGN KEY (id_tp_doc) REFERENCES siga.ex_tipo_documento(id_tp_doc);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_tp_forma_doc_fkey FOREIGN KEY (id_tp_forma_doc) REFERENCES siga.ex_tipo_forma_documento(id_tipo_forma_doc);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_tp_mov_fkey FOREIGN KEY (id_tp_mov) REFERENCES siga.ex_tipo_movimentacao(id_tp_mov);

ALTER TABLE ONLY siga.ex_configuracao ADD CONSTRAINT ex_configuracao_id_via_fkey FOREIGN KEY (id_via) REFERENCES siga.ex_via(id_via);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_arq_fkey FOREIGN KEY (id_arq) REFERENCES corporativo.cp_arquivo(id_arq);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_cadastrante_fkey FOREIGN KEY (id_cadastrante) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_classificacao_fkey FOREIGN KEY (id_classificacao) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_lota_titular_fkey FOREIGN KEY (id_lota_titular) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_mob_autuado_fkey FOREIGN KEY (id_mob_autuado) REFERENCES siga.ex_mobil(id_mobil);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_mob_pai_fkey FOREIGN KEY (id_mob_pai) REFERENCES siga.ex_mobil(id_mobil);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_mod_fkey FOREIGN KEY (id_mod) REFERENCES siga.ex_modelo(id_mod);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_nivel_acesso_fkey FOREIGN KEY (id_nivel_acesso) REFERENCES siga.ex_nivel_acesso(id_nivel_acesso);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_orgao_destinatario_fkey FOREIGN KEY (id_orgao_destinatario) REFERENCES corporativo.cp_orgao(id_orgao);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_orgao_fkey FOREIGN KEY (id_orgao) REFERENCES corporativo.cp_orgao(id_orgao);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_subscritor_fkey FOREIGN KEY (id_subscritor) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_titular_fkey FOREIGN KEY (id_titular) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_documento ADD CONSTRAINT ex_documento_id_tp_doc_fkey FOREIGN KEY (id_tp_doc) REFERENCES siga.ex_tipo_documento(id_tp_doc);

ALTER TABLE ONLY siga.ex_documento_numeracao ADD CONSTRAINT ex_documento_numeracao_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_documento_numeracao ADD CONSTRAINT ex_documento_numeracao_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY siga.ex_estado_tp_mov ADD CONSTRAINT ex_estado_tp_mov_id_estado_doc_fkey FOREIGN KEY (id_estado_doc) REFERENCES siga.ex_estado_doc(id_estado_doc);

ALTER TABLE ONLY siga.ex_estado_tp_mov ADD CONSTRAINT ex_estado_tp_mov_id_tp_mov_fkey FOREIGN KEY (id_tp_mov) REFERENCES siga.ex_tipo_movimentacao(id_tp_mov);

ALTER TABLE ONLY siga.ex_forma_documento ADD CONSTRAINT ex_forma_documento_id_tipo_forma_doc_fkey FOREIGN KEY (id_tipo_forma_doc) REFERENCES siga.ex_tipo_forma_documento(id_tipo_forma_doc);

ALTER TABLE ONLY siga.ex_mobil ADD CONSTRAINT ex_mobil_id_doc_fkey FOREIGN KEY (id_doc) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_mobil ADD CONSTRAINT ex_mobil_id_tipo_mobil_fkey FOREIGN KEY (id_tipo_mobil) REFERENCES siga.ex_tipo_mobil(id_tipo_mobil);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_id_arq_fkey FOREIGN KEY (id_arq) REFERENCES corporativo.cp_arquivo(id_arq);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_id_class_criacao_via_fkey FOREIGN KEY (id_class_criacao_via) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_id_classificacao_fkey FOREIGN KEY (id_classificacao) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_modelo ADD CONSTRAINT ex_modelo_id_nivel_acesso_fkey FOREIGN KEY (id_nivel_acesso) REFERENCES siga.ex_nivel_acesso(id_nivel_acesso);

ALTER TABLE ONLY siga.ex_modelo_tp_doc_publicacao ADD CONSTRAINT ex_modelo_tp_doc_publicacao_id_doc_publicacao_fkey FOREIGN KEY (id_doc_publicacao) REFERENCES siga.ex_tp_doc_publicacao(id_doc_publicacao);

ALTER TABLE ONLY siga.ex_modelo_tp_doc_publicacao ADD CONSTRAINT ex_modelo_tp_doc_publicacao_id_mod_fkey FOREIGN KEY (id_mod) REFERENCES siga.ex_modelo(id_mod);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_arq_fkey FOREIGN KEY (id_arq) REFERENCES corporativo.cp_arquivo(id_arq);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_cadastrante_fkey FOREIGN KEY (id_cadastrante) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_classificacao_fkey FOREIGN KEY (id_classificacao) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_destino_final_fkey FOREIGN KEY (id_destino_final) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_doc_fkey FOREIGN KEY (id_doc) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_doc_pai_fkey FOREIGN KEY (id_doc_pai) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_doc_ref_fkey FOREIGN KEY (id_doc_ref) REFERENCES siga.ex_documento(id_doc);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_estado_doc_fkey FOREIGN KEY (id_estado_doc) REFERENCES siga.ex_estado_doc(id_estado_doc);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_identidade_audit_fkey FOREIGN KEY (id_identidade_audit) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_lota_cadastrante_fkey FOREIGN KEY (id_lota_cadastrante) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_lota_destino_final_fkey FOREIGN KEY (id_lota_destino_final) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_lota_resp_fkey FOREIGN KEY (id_lota_resp) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_lota_subscritor_fkey FOREIGN KEY (id_lota_subscritor) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_lota_titular_fkey FOREIGN KEY (id_lota_titular) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_mob_ref_fkey FOREIGN KEY (id_mob_ref) REFERENCES siga.ex_mobil(id_mobil);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_mobil_fkey FOREIGN KEY (id_mobil) REFERENCES siga.ex_mobil(id_mobil);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_mov_canceladora_fkey FOREIGN KEY (id_mov_canceladora) REFERENCES siga.ex_movimentacao(id_mov);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_mov_ref_fkey FOREIGN KEY (id_mov_ref) REFERENCES siga.ex_movimentacao(id_mov);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_nivel_acesso_fkey FOREIGN KEY (id_nivel_acesso) REFERENCES siga.ex_nivel_acesso(id_nivel_acesso);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_orgao_fkey FOREIGN KEY (id_orgao) REFERENCES corporativo.cp_orgao(id_orgao);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_papel_fkey FOREIGN KEY (id_papel) REFERENCES siga.ex_papel(id_papel);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_resp_fkey FOREIGN KEY (id_resp) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_subscritor_fkey FOREIGN KEY (id_subscritor) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_titular_fkey FOREIGN KEY (id_titular) REFERENCES corporativo.dp_pessoa(id_pessoa);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_tp_despacho_fkey FOREIGN KEY (id_tp_despacho) REFERENCES siga.ex_tipo_despacho(id_tp_despacho);

ALTER TABLE ONLY siga.ex_movimentacao ADD CONSTRAINT ex_movimentacao_id_tp_mov_fkey FOREIGN KEY (id_tp_mov) REFERENCES siga.ex_tipo_movimentacao(id_tp_mov);

ALTER TABLE ONLY siga.ex_numeracao ADD CONSTRAINT ex_numeracao_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_numeracao ADD CONSTRAINT ex_numeracao_id_orgao_usu_fkey FOREIGN KEY (id_orgao_usu) REFERENCES corporativo.cp_orgao_usuario(id_orgao_usu);

ALTER TABLE ONLY siga.ex_preenchimento ADD CONSTRAINT ex_preenchimento_id_arq_fkey FOREIGN KEY (id_arq) REFERENCES corporativo.cp_arquivo(id_arq);

ALTER TABLE ONLY siga.ex_preenchimento ADD CONSTRAINT ex_preenchimento_id_lotacao_fkey FOREIGN KEY (id_lotacao) REFERENCES corporativo.dp_lotacao(id_lotacao);

ALTER TABLE ONLY siga.ex_preenchimento ADD CONSTRAINT ex_preenchimento_id_mod_fkey FOREIGN KEY (id_mod) REFERENCES siga.ex_modelo(id_mod);

ALTER TABLE ONLY siga.ex_temporalidade ADD CONSTRAINT ex_temporalidade_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_temporalidade ADD CONSTRAINT ex_temporalidade_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_temporalidade ADD CONSTRAINT ex_temporalidade_id_unidade_medida_fkey FOREIGN KEY (id_unidade_medida) REFERENCES corporativo.cp_unidade_medida(id_unidade_medida);

ALTER TABLE ONLY siga.ex_tp_forma_doc ADD CONSTRAINT ex_tp_forma_doc_id_forma_doc_fkey FOREIGN KEY (id_forma_doc) REFERENCES siga.ex_forma_documento(id_forma_doc);

ALTER TABLE ONLY siga.ex_tp_forma_doc ADD CONSTRAINT ex_tp_forma_doc_id_tp_doc_fkey FOREIGN KEY (id_tp_doc) REFERENCES siga.ex_tipo_documento(id_tp_doc);

ALTER TABLE ONLY siga.ex_tp_mov_estado ADD CONSTRAINT ex_tp_mov_estado_id_tp_mov_fkey FOREIGN KEY (id_tp_mov) REFERENCES siga.ex_tipo_movimentacao(id_tp_mov);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_his_id_ini_fkey FOREIGN KEY (his_id_ini) REFERENCES siga.ex_via(id_via);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_his_idc_fim_fkey FOREIGN KEY (his_idc_fim) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_his_idc_ini_fkey FOREIGN KEY (his_idc_ini) REFERENCES corporativo.cp_identidade(id_identidade);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_id_classificacao_fkey FOREIGN KEY (id_classificacao) REFERENCES siga.ex_classificacao(id_classificacao);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_id_destinacao_fkey FOREIGN KEY (id_destinacao) REFERENCES siga.ex_tipo_destinacao(id_tp_destinacao);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_id_temporal_arq_cor_fkey FOREIGN KEY (id_temporal_arq_cor) REFERENCES siga.ex_temporalidade(id_temporalidade);

ALTER TABLE ONLY siga.ex_via ADD CONSTRAINT ex_via_id_temporal_arq_int_fkey FOREIGN KEY (id_temporal_arq_int) REFERENCES siga.ex_temporalidade(id_temporalidade);

/*
 * SCHEMA CORPORATIVO FK
 */

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_mobil_fkey FOREIGN KEY (id_mobil) REFERENCES siga.ex_mobil(id_mobil);

ALTER TABLE ONLY corporativo.cp_marca ADD CONSTRAINT cp_marca_id_mov_fkey FOREIGN KEY (id_mov) REFERENCES siga.ex_movimentacao(id_mov);

