CREATE UNIQUE INDEX cad_sit_funcional_cad_sit_funcional_pk ON corporativo.cad_sit_funcional USING btree (id_cad_sit_funcional);

CREATE INDEX cp_acesso_cp_acesso_identidade_fk ON corporativo.cp_acesso USING btree (id_identidade);

CREATE INDEX cp_aplicacao_feriado_iacs_cp_aplicacao_feriad_00001 ON corporativo.cp_aplicacao_feriado USING btree (id_lotacao);

CREATE INDEX cp_aplicacao_feriado_id_localidade ON corporativo.cp_aplicacao_feriado USING btree (id_localidade);

CREATE INDEX cp_aplicacao_feriado_id_lotacao ON corporativo.cp_aplicacao_feriado USING btree (id_lotacao);

CREATE INDEX cp_aplicacao_feriado_id_ocorrencia_feriado ON corporativo.cp_aplicacao_feriado USING btree (id_ocorrencia_feriado);

CREATE INDEX cp_aplicacao_feriado_id_orgao_usu ON corporativo.cp_aplicacao_feriado USING btree (id_orgao_usu);

CREATE UNIQUE INDEX cp_arquivo_excluir_cp_arq_excluir_id_arq_exc_pk ON corporativo.cp_arquivo_excluir USING btree (id_arq_exc);

CREATE INDEX cp_arquivo_id_orgao_usu ON corporativo.cp_arquivo USING btree (id_orgao_usu);

CREATE INDEX cp_complexo_id_localidade ON corporativo.cp_complexo USING btree (id_localidade);

CREATE INDEX cp_configuracao_cp_conf_cp_grup_id_grp_fk ON corporativo.cp_configuracao USING btree (id_grupo);

CREATE INDEX cp_configuracao_cp_conf_cp_ident_id_ident_fk ON corporativo.cp_configuracao USING btree (id_identidade);

CREATE INDEX cp_configuracao_cp_conf_cp_ident_idc_fim_fk ON corporativo.cp_configuracao USING btree (his_idc_fim);

CREATE INDEX cp_configuracao_cp_conf_cp_ident_idc_ini_fk ON corporativo.cp_configuracao USING btree (his_idc_ini);

CREATE INDEX cp_configuracao_cp_conf_cp_org_usu_id_fk ON corporativo.cp_configuracao USING btree (id_orgao_usu);

CREATE INDEX cp_configuracao_cp_conf_cp_serv_id_servico_fk ON corporativo.cp_configuracao USING btree (id_servico);

CREATE INDEX cp_configuracao_cp_conf_cp_sit_conf_id_fk ON corporativo.cp_configuracao USING btree (id_sit_configuracao);

CREATE INDEX cp_configuracao_cp_conf_cp_tp_conf_id_fk ON corporativo.cp_configuracao USING btree (id_tp_configuracao);

CREATE INDEX cp_configuracao_cp_conf_cp_tplot_id_tp_lot_fk ON corporativo.cp_configuracao USING btree (id_tp_lotacao);

CREATE INDEX cp_configuracao_cp_conf_dp_cargo_id_cargo_fk ON corporativo.cp_configuracao USING btree (id_cargo);

CREATE INDEX cp_configuracao_cp_conf_dp_f_confianca_id_fk ON corporativo.cp_configuracao USING btree (id_funcao_confianca);

CREATE INDEX cp_configuracao_cp_conf_dp_lot_id_lotacao_fk ON corporativo.cp_configuracao USING btree (id_lotacao);

CREATE INDEX cp_configuracao_cp_conf_dp_pes_id_pessoa_fk ON corporativo.cp_configuracao USING btree (id_pessoa);

CREATE UNIQUE INDEX cp_configuracao_cp_conf_id_configuracao_pk ON corporativo.cp_configuracao USING btree (id_configuracao);

CREATE INDEX cp_configuracao_cp_configuracao_complexo_fk ON corporativo.cp_configuracao USING btree (id_complexo);

CREATE UNIQUE INDEX cp_feriado_feriado_pk ON corporativo.cp_feriado USING btree (id_feriado);

CREATE INDEX cp_grupo_cp_grp_cp_grp_id_ini_fk ON corporativo.cp_grupo USING btree (his_id_ini);

CREATE INDEX cp_grupo_cp_grp_cp_grp_id_pai_fk ON corporativo.cp_grupo USING btree (id_grupo_pai);

CREATE INDEX cp_grupo_cp_grp_cp_ident_idc_fim_fk ON corporativo.cp_grupo USING btree (his_idc_fim);

CREATE INDEX cp_grupo_cp_grp_cp_ident_idc_ini_fk ON corporativo.cp_grupo USING btree (his_idc_ini);

CREATE INDEX cp_grupo_cp_grp_cp_tp_grp_id_tp_grp_fk ON corporativo.cp_grupo USING btree (id_tp_grupo);

CREATE UNIQUE INDEX cp_grupo_cp_grupo_pk ON corporativo.cp_grupo USING btree (id_grupo);

CREATE INDEX cp_identidade_cp_ident_cp_ident_id_ini_fk ON corporativo.cp_identidade USING btree (his_id_ini);

CREATE INDEX cp_identidade_cp_ident_cp_ident_idc_fim_fk ON corporativo.cp_identidade USING btree (his_idc_fim);

CREATE INDEX cp_identidade_cp_ident_cp_ident_idc_ini_fk ON corporativo.cp_identidade USING btree (his_idc_ini);

CREATE INDEX cp_identidade_cp_ident_cp_tpidt_id_tp_idt_fk ON corporativo.cp_identidade USING btree (id_tp_identidade);

CREATE INDEX cp_identidade_cp_ident_cporgusu_id_orgusu_fk ON corporativo.cp_identidade USING btree (id_orgao_usu);

CREATE INDEX cp_identidade_cp_ident_dp_pess_id_pess_fk ON corporativo.cp_identidade USING btree (id_pessoa);

CREATE INDEX cp_identidade_cp_identidade_idpessoa ON corporativo.cp_identidade USING btree (id_pessoa);

CREATE INDEX cp_identidade_idx_login_identidade ON corporativo.cp_identidade USING btree (login_identidade);

CREATE UNIQUE INDEX cp_localidade_localidade_fk ON corporativo.cp_localidade USING btree (id_localidade);

CREATE INDEX cp_localidade_uf_localidade_fk ON corporativo.cp_localidade USING btree (id_uf);

CREATE INDEX cp_marca_cp_marca_idx_004 ON corporativo.cp_marca USING btree (id_ref);

CREATE INDEX cp_marca_iacs_cp_marca_00002 ON corporativo.cp_marca USING btree (id_mobil);

CREATE INDEX cp_marca_id_marcador ON corporativo.cp_marca USING btree (id_marcador);

CREATE INDEX cp_marca_id_mobil ON corporativo.cp_marca USING btree (id_mobil);

CREATE INDEX cp_marca_id_tp_marca ON corporativo.cp_marca USING btree (id_tp_marca);

CREATE INDEX cp_marca_lotacao ON corporativo.cp_marca USING btree (id_lotacao_ini);

CREATE INDEX cp_marca_marca_movimentacao_fk ON corporativo.cp_marca USING btree (id_mov);

CREATE INDEX cp_marca_pessoa ON corporativo.cp_marca USING btree (id_pessoa_ini);

CREATE INDEX cp_marcador_cp_marcador_his_id_ini ON corporativo.cp_marcador USING btree (his_id_ini);

CREATE INDEX cp_marcador_cp_marcador_lota_id_ini ON corporativo.cp_marcador USING btree (id_lotacao_ini, his_dt_fim);

CREATE INDEX cp_marcador_id_tp_marcador ON corporativo.cp_marcador USING btree (id_finalidade_marcador);

CREATE INDEX cp_modelo_his_id_ini ON corporativo.cp_modelo USING btree (his_id_ini);

CREATE INDEX cp_modelo_his_idc_fim ON corporativo.cp_modelo USING btree (his_idc_fim);

CREATE INDEX cp_modelo_his_idc_ini ON corporativo.cp_modelo USING btree (his_idc_ini);

CREATE INDEX cp_modelo_iacs_cp_modelo_00001 ON corporativo.cp_modelo USING btree (his_idc_ini);

CREATE INDEX cp_modelo_iacs_cp_modelo_00002 ON corporativo.cp_modelo USING btree (his_idc_fim);

CREATE INDEX cp_modelo_id_arq ON corporativo.cp_modelo USING btree (id_arq);

CREATE INDEX cp_modelo_id_orgao_usu ON corporativo.cp_modelo USING btree (id_orgao_usu);

CREATE INDEX cp_ocorrencia_feriado_id_feriado ON corporativo.cp_ocorrencia_feriado USING btree (id_feriado);

CREATE INDEX cp_orgao_orgao_orgao_usu_fk ON corporativo.cp_orgao USING btree (id_orgao_usu);

CREATE UNIQUE INDEX cp_orgao_orgao_pk ON corporativo.cp_orgao USING btree (id_orgao);

CREATE INDEX cp_orgao_usuario_cp_org_usu_cp_ident_id_fim_fk ON corporativo.cp_orgao_usuario USING btree (his_idc_fim);

CREATE INDEX cp_orgao_usuario_cp_org_usu_cp_ident_id_ini_fk ON corporativo.cp_orgao_usuario USING btree (his_idc_ini);

CREATE UNIQUE INDEX cp_orgao_usuario_orgao_usu_pk ON corporativo.cp_orgao_usuario USING btree (id_orgao_usu);

CREATE INDEX cp_papel_cp_papel_cp_papel_id_ini_fk ON corporativo.cp_papel USING btree (his_id_ini);

CREATE INDEX cp_papel_cp_papel_cporgusu_idorgusu_fk ON corporativo.cp_papel USING btree (id_orgao_usu);

CREATE INDEX cp_papel_cp_papel_cptppap_id_tppap_fk ON corporativo.cp_papel USING btree (id_tp_papel);

CREATE INDEX cp_papel_cp_papel_dp_cargo_id_cargo_fk ON corporativo.cp_papel USING btree (id_cargo);

CREATE INDEX cp_papel_cp_papel_dp_fc_id_fc_fk ON corporativo.cp_papel USING btree (id_funcao_confianca);

CREATE INDEX cp_papel_cp_papel_dp_lot_id_lot_fk ON corporativo.cp_papel USING btree (id_lotacao);

CREATE INDEX cp_papel_cp_papel_dp_pess_id_pess_fk ON corporativo.cp_papel USING btree (id_pessoa);

CREATE UNIQUE INDEX cp_papel_cp_papel_pk ON corporativo.cp_papel USING btree (id_papel);

CREATE INDEX cp_personalizacao_cp_person_cp_pap_id_ativo_fk ON corporativo.cp_personalizacao USING btree (id_papel_ativo);

CREATE INDEX cp_personalizacao_cp_person_cp_pap_id_subst_fk ON corporativo.cp_personalizacao USING btree (id_substituindo_papel);

CREATE INDEX cp_personalizacao_cp_person_dp_lot_id_subst_fk ON corporativo.cp_personalizacao USING btree (id_substituindo_lotacao);

CREATE INDEX cp_personalizacao_cp_person_dp_pess_id_pess_fk ON corporativo.cp_personalizacao USING btree (id_pessoa);

CREATE INDEX cp_personalizacao_cp_person_dp_pess_id_subst_fk ON corporativo.cp_personalizacao USING btree (id_substituindo_pessoa);

CREATE INDEX cp_sede_id_orgao_usu ON corporativo.cp_sede USING btree (id_orgao_usu);

CREATE INDEX cp_servico_cp_serv_cptpserv_id_tpserv_fk ON corporativo.cp_servico USING btree (id_tp_servico);

CREATE UNIQUE INDEX cp_servico_cp_servico_id_servico_pk ON corporativo.cp_servico USING btree (id_servico);

CREATE UNIQUE INDEX cp_servico_sigla_servico_idx ON corporativo.cp_servico USING btree (sigla_servico);

CREATE UNIQUE INDEX cp_situacao_configuracao_cp_sit_conf_id_sit_conf_pk ON corporativo.cp_situacao_configuracao USING btree (id_sit_configuracao);

CREATE INDEX cp_tipo_configuracao_cp_tp_conf_cp_sit_conf_id_fk ON corporativo.cp_tipo_configuracao USING btree (id_sit_configuracao);

CREATE UNIQUE INDEX cp_tipo_configuracao_cp_tp_conf_id_tp_conf_pk ON corporativo.cp_tipo_configuracao USING btree (id_tp_configuracao);

CREATE UNIQUE INDEX cp_tipo_grupo_cp_tipo_grupo_pk ON corporativo.cp_tipo_grupo USING btree (id_tp_grupo);

CREATE UNIQUE INDEX cp_tipo_identidade_cp_tipo_identidade_pk ON corporativo.cp_tipo_identidade USING btree (id_tp_identidade);

CREATE UNIQUE INDEX cp_tipo_lotacao_cp_tipo_lotacao_pk ON corporativo.cp_tipo_lotacao USING btree (id_tp_lotacao);

CREATE UNIQUE INDEX cp_tipo_papel_cp_tipo_papel_pk ON corporativo.cp_tipo_papel USING btree (id_tp_papel);

CREATE UNIQUE INDEX cp_tipo_pessoa_cp_tipo_pessoa_pk ON corporativo.cp_tipo_pessoa USING btree (id_tp_pessoa);

CREATE UNIQUE INDEX cp_tipo_servico_cp_tipo_servico_pk ON corporativo.cp_tipo_servico USING btree (id_tp_servico);

CREATE INDEX cp_tipo_servico_cp_tp_serv_id_sit_conf_fk ON corporativo.cp_tipo_servico USING btree (id_sit_configuracao);

CREATE INDEX cp_tipo_servico_situacao_cp_tpservsit_id_sitconf_fk ON corporativo.cp_tipo_servico_situacao USING btree (id_sit_configuracao);

CREATE INDEX cp_token_cp_token_tp_idx ON corporativo.cp_token USING btree (id_tp_token, token);

CREATE UNIQUE INDEX cp_uf_uf_pk ON corporativo.cp_uf USING btree (id_uf);

CREATE INDEX dp_cargo_dp_cargo_idx_011 ON corporativo.dp_cargo USING btree (id_cargo_inicial);

CREATE INDEX dp_cargo_dp_cargo_idx_012 ON corporativo.dp_cargo USING btree (id_orgao_usu);

CREATE UNIQUE INDEX dp_estado_civil_estado_civil_pk ON corporativo.dp_estado_civil USING btree (id_estado_civil);

CREATE INDEX dp_funcao_confianca_dp_funcao_confianca_idx_008 ON corporativo.dp_funcao_confianca USING btree (id_fun_conf_ini);

CREATE INDEX dp_funcao_confianca_dp_funcao_confianca_idx_009 ON corporativo.dp_funcao_confianca USING btree (id_orgao_usu);

CREATE INDEX dp_funcao_confianca_dp_funcao_confianca_idx_010 ON corporativo.dp_funcao_confianca USING btree (id_funcao_confianca_pai);

CREATE INDEX dp_lotacao_dp_lot_cp_tp_lot_id_tp_lot_fk ON corporativo.dp_lotacao USING btree (id_tp_lotacao);

CREATE INDEX dp_lotacao_dp_lotacao_idx_006 ON corporativo.dp_lotacao USING btree (id_orgao_usu);

CREATE INDEX dp_lotacao_dp_lotacao_idx_007 ON corporativo.dp_lotacao USING btree (id_lotacao_pai);

CREATE INDEX dp_lotacao_id_localidade ON corporativo.dp_lotacao USING btree (id_localidade);

CREATE INDEX dp_lotacao_lot_id_ini ON corporativo.dp_lotacao USING btree (id_lotacao_ini, data_fim_lot);

CREATE UNIQUE INDEX dp_lotacao_sigla_lotacao_dp_lotacao_uk ON corporativo.dp_lotacao USING btree (sigla_lotacao, id_orgao_usu, data_fim_lot);

CREATE INDEX dp_padrao_referencia_padrao_ref_orgao_usu_fk ON corporativo.dp_padrao_referencia USING btree (id_orgao_usu);

CREATE UNIQUE INDEX dp_padrao_referencia_padrao_referencia_pk ON corporativo.dp_padrao_referencia USING btree (id_padrao_referencia);

CREATE INDEX dp_pessoa_dp_pess_cptppess_id_tppess_fk ON corporativo.dp_pessoa USING btree (id_tp_pessoa);

CREATE INDEX dp_pessoa_dp_pessoa_cpf_orgao ON corporativo.dp_pessoa USING btree (cpf_pessoa, id_orgao_usu);

CREATE INDEX dp_pessoa_dp_pessoa_idx_001 ON corporativo.dp_pessoa USING btree (id_orgao_usu);

CREATE INDEX dp_pessoa_dp_pessoa_idx_002 ON corporativo.dp_pessoa USING btree (id_cargo, id_lotacao, id_funcao_confianca, id_orgao_usu);

CREATE INDEX dp_pessoa_dp_pessoa_idx_003 ON corporativo.dp_pessoa USING btree (id_funcao_confianca);

CREATE INDEX dp_pessoa_dp_pessoa_idx_004 ON corporativo.dp_pessoa USING btree (id_lotacao);

CREATE INDEX dp_pessoa_dp_pessoa_idx_005 ON corporativo.dp_pessoa USING btree (id_provimento);

CREATE INDEX dp_pessoa_dp_pessoa_idx_014 ON corporativo.dp_pessoa USING btree (matricula, sesb_pessoa, data_fim_pessoa);

CREATE INDEX dp_pessoa_dp_pessoa_idx_015 ON corporativo.dp_pessoa USING btree (data_fim_pessoa, nome_pessoa);

CREATE INDEX dp_pessoa_dp_pessoa_nm_pessoa_ix ON corporativo.dp_pessoa USING btree (nome_pessoa);

CREATE INDEX dp_pessoa_dp_pessoa_orgao ON corporativo.dp_pessoa USING btree (id_orgao_usu);

CREATE INDEX dp_pessoa_pes_id_ini ON corporativo.dp_pessoa USING btree (id_pessoa_inicial, data_fim_pessoa);

CREATE INDEX dp_pessoa_pessoa_estado_civil_fk ON corporativo.dp_pessoa USING btree (id_estado_civil);

CREATE INDEX dp_substituicao_substituicao_pesoa_subst_fk ON corporativo.dp_substituicao USING btree (id_substituto);

CREATE INDEX dp_substituicao_substituicao_pesoa_titular_fk ON corporativo.dp_substituicao USING btree (id_titular);
