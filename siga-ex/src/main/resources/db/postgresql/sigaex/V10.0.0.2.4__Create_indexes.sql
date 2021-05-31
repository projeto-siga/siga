CREATE INDEX ex_boletim_doc_bol_boletim_fk ON siga.ex_boletim_doc USING btree (id_boletim);

CREATE UNIQUE INDEX ex_boletim_doc_bol_doc_uk ON siga.ex_boletim_doc USING btree (id_doc);

CREATE INDEX ex_classificacao_his_id_ini ON siga.ex_classificacao USING btree (his_id_ini);

CREATE INDEX ex_classificacao_his_idc_fim ON siga.ex_classificacao USING btree (his_idc_fim);

CREATE INDEX ex_classificacao_his_idc_ini ON siga.ex_classificacao USING btree (his_idc_ini);

CREATE INDEX ex_classificacao_iacs_ex_classificacao_00001 ON siga.ex_classificacao USING btree (his_idc_fim);

CREATE INDEX ex_classificacao_iacs_ex_classificacao_00002 ON siga.ex_classificacao USING btree (his_idc_ini);

CREATE INDEX ex_competencia_competencia_cargo_fk ON siga.ex_competencia USING btree (id_cargo);

CREATE INDEX ex_competencia_competencia_func_conf_fk ON siga.ex_competencia USING btree (id_funcao_confianca);

CREATE INDEX ex_competencia_competencia_lotacao_fk ON siga.ex_competencia USING btree (id_lotacao);

CREATE INDEX ex_competencia_competencia_pessoa_fk ON siga.ex_competencia USING btree (id_pessoa);

CREATE INDEX ex_configuracao_fk_classificacao ON siga.ex_configuracao USING btree (id_classificacao);

CREATE INDEX ex_configuracao_fk_forma_doc ON siga.ex_configuracao USING btree (id_forma_doc);

CREATE INDEX ex_configuracao_fk_mod ON siga.ex_configuracao USING btree (id_mod);

CREATE INDEX ex_configuracao_fk_nivel_acesso ON siga.ex_configuracao USING btree (id_nivel_acesso);

CREATE INDEX ex_configuracao_fk_papel ON siga.ex_configuracao USING btree (id_papel);

CREATE INDEX ex_configuracao_fk_tp_doc ON siga.ex_configuracao USING btree (id_tp_doc);

CREATE INDEX ex_configuracao_fk_tp_forma_doc ON siga.ex_configuracao USING btree (id_tp_forma_doc);

CREATE INDEX ex_configuracao_fk_tp_mov ON siga.ex_configuracao USING btree (id_tp_mov);

CREATE INDEX ex_configuracao_fk_via ON siga.ex_configuracao USING btree (id_via);

CREATE INDEX ex_documento_doc_forma_num_ano_ix ON siga.ex_documento USING btree (id_forma_doc, num_expediente, ano_emissao);

CREATE INDEX ex_documento_doc_tp_doc_fk ON siga.ex_documento USING btree (id_tp_doc);

CREATE UNIQUE INDEX ex_documento_documento_pk ON siga.ex_documento USING btree (id_doc);

CREATE INDEX ex_documento_ex_documento_idx_011 ON siga.ex_documento USING btree (id_orgao_usu, ano_emissao);

CREATE INDEX ex_documento_id_arq ON siga.ex_documento USING btree (id_arq);

CREATE INDEX ex_documento_ixcf_doc_cadastrante_pessoa_fk ON siga.ex_documento USING btree (id_cadastrante);

CREATE INDEX ex_documento_ixcf_doc_classificacao_fk ON siga.ex_documento USING btree (id_classificacao);

CREATE INDEX ex_documento_ixcf_doc_cp_orgao_dest_fk ON siga.ex_documento USING btree (id_orgao_destinatario);

CREATE INDEX ex_documento_ixcf_doc_cp_orgao_fk ON siga.ex_documento USING btree (id_orgao);

CREATE INDEX ex_documento_ixcf_doc_lota_tit_lotacao_fk ON siga.ex_documento USING btree (id_lota_titular);

CREATE INDEX ex_documento_ixcf_doc_nivel_acesso_fk ON siga.ex_documento USING btree (id_nivel_acesso);

CREATE INDEX ex_documento_ixcf_doc_subscritor_pessoa_fk ON siga.ex_documento USING btree (id_subscritor);

CREATE INDEX ex_documento_ixcf_doc_titular_pessoa_fk ON siga.ex_documento USING btree (id_titular);

CREATE INDEX ex_documento_ixcf_documento_modelo_fk ON siga.ex_documento USING btree (id_mod);

CREATE UNIQUE INDEX ex_documento_numeracao_ex_documento_numeracao_chave ON siga.ex_documento_numeracao USING btree (id_orgao_usu, id_forma_doc, ano_emissao);

CREATE INDEX ex_documento_numeracao_ex_documento_numeracao_forma ON siga.ex_documento_numeracao USING btree (id_forma_doc);

CREATE UNIQUE INDEX ex_documento_numeracao_ex_documento_numeracao_pk ON siga.ex_documento_numeracao USING btree (id_documento_numeracao);

CREATE INDEX ex_documento_siga_exdoc_mob_autuado_id_ix ON siga.ex_documento USING btree (id_mob_autuado);

CREATE INDEX ex_documento_siga_exdoc_mob_pai_id_doc_ix ON siga.ex_documento USING btree (id_mob_pai, id_doc);

CREATE UNIQUE INDEX ex_documento_unique_chave_doc ON siga.ex_documento USING btree (chave_doc);

CREATE UNIQUE INDEX ex_documento_unique_doc_num_idx ON siga.ex_documento USING btree (id_forma_doc, num_expediente, ano_emissao, id_orgao_usu);

CREATE INDEX ex_estado_tp_mov_tp_mov_estado_tpmov_fk ON siga.ex_estado_tp_mov USING btree (id_tp_mov);

CREATE INDEX ex_forma_documento_ex_forma_documento_idx_021 ON siga.ex_forma_documento USING btree (sigla_forma_doc, id_forma_doc, descr_forma_doc);

CREATE INDEX ex_forma_documento_id_tipo_forma_doc ON siga.ex_forma_documento USING btree (id_tipo_forma_doc);

CREATE INDEX ex_mobil_iacs_ex_mobil_00001 ON siga.ex_mobil USING btree (id_doc, id_mobil);

CREATE INDEX ex_mobil_id_tipo_mobil ON siga.ex_mobil USING btree (id_tipo_mobil);

CREATE UNIQUE INDEX ex_mobil_idx_mobil_unique ON siga.ex_mobil USING btree (id_doc, id_tipo_mobil, num_sequencia);

CREATE INDEX ex_modelo_ex_modelo_idx_014 ON siga.ex_modelo USING btree (id_classificacao);

CREATE INDEX ex_modelo_ex_modelo_idx_015 ON siga.ex_modelo USING btree (id_class_criacao_via);

CREATE INDEX ex_modelo_ex_modelo_idx_016 ON siga.ex_modelo USING btree (id_forma_doc);

CREATE INDEX ex_modelo_ex_modelo_idx_017 ON siga.ex_modelo USING btree (id_nivel_acesso);

CREATE INDEX ex_modelo_his_idc_fim ON siga.ex_modelo USING btree (his_idc_fim);

CREATE INDEX ex_modelo_his_idc_ini ON siga.ex_modelo USING btree (his_idc_ini);

CREATE INDEX ex_modelo_iacs_ex_modelo_00001 ON siga.ex_modelo USING btree (his_idc_ini);

CREATE INDEX ex_modelo_iacs_ex_modelo_00002 ON siga.ex_modelo USING btree (his_idc_fim);

CREATE INDEX ex_modelo_id_arq ON siga.ex_modelo USING btree (id_arq);

CREATE INDEX ex_modelo_tp_doc_publicacao_mod_publ_id_doc_publicacao_fk ON siga.ex_modelo_tp_doc_publicacao USING btree (id_doc_publicacao);

CREATE INDEX ex_movimentacao_ex_movimentacao_idx_001 ON siga.ex_movimentacao USING btree (id_mov_ref, dt_ini_mov);

CREATE INDEX ex_movimentacao_iacs_ex_movimentacao_00001 ON siga.ex_movimentacao USING btree (id_mobil, id_tp_mov, id_mov_canceladora);

CREATE INDEX ex_movimentacao_id_arq ON siga.ex_movimentacao USING btree (id_arq);

CREATE INDEX ex_movimentacao_id_mobil_idx ON siga.ex_movimentacao USING btree (id_mobil);

CREATE INDEX ex_movimentacao_id_mobil_ref_idx ON siga.ex_movimentacao USING btree (id_mob_ref);

CREATE INDEX ex_movimentacao_id_mov_ref_idx ON siga.ex_movimentacao USING btree (id_mov_ref);

CREATE INDEX ex_movimentacao_ixcf_mov_dest_fim_lota_fk ON siga.ex_movimentacao USING btree (id_lota_destino_final);

CREATE INDEX ex_movimentacao_ixcf_mov_dest_fim_pessoa_fk ON siga.ex_movimentacao USING btree (id_destino_final);

CREATE INDEX ex_movimentacao_ixcf_mov_doc_fk ON siga.ex_movimentacao USING btree (id_doc);

CREATE INDEX ex_movimentacao_ixcf_mov_doc_pai_fk ON siga.ex_movimentacao USING btree (id_doc_pai);

CREATE INDEX ex_movimentacao_ixcf_mov_doc_ref_fk ON siga.ex_movimentacao USING btree (id_doc_ref);

CREATE INDEX ex_movimentacao_ixcf_mov_dp_lota_sin_cad_fk ON siga.ex_movimentacao USING btree (id_lota_cadastrante);

CREATE INDEX ex_movimentacao_ixcf_mov_dp_lota_sin_sub_fk ON siga.ex_movimentacao USING btree (id_lota_subscritor);

CREATE INDEX ex_movimentacao_ixcf_mov_dp_pessoa_sin_cad_fk ON siga.ex_movimentacao USING btree (id_cadastrante);

CREATE INDEX ex_movimentacao_ixcf_mov_dp_pessoa_sin_sub_fk ON siga.ex_movimentacao USING btree (id_subscritor);

CREATE INDEX ex_movimentacao_ixcf_mov_estado_doc_fk ON siga.ex_movimentacao USING btree (id_estado_doc);

CREATE INDEX ex_movimentacao_ixcf_mov_lota_tit_lota_fk ON siga.ex_movimentacao USING btree (id_lota_titular);

CREATE INDEX ex_movimentacao_ixcf_mov_mov_cancelada_fk ON siga.ex_movimentacao USING btree (id_mov_canceladora);

CREATE INDEX ex_movimentacao_ixcf_mov_nivel_acesso_fk ON siga.ex_movimentacao USING btree (id_nivel_acesso);

CREATE INDEX ex_movimentacao_ixcf_mov_orgao_fk ON siga.ex_movimentacao USING btree (id_orgao);

CREATE INDEX ex_movimentacao_ixcf_mov_pessoa_resp_fk ON siga.ex_movimentacao USING btree (id_resp);

CREATE INDEX ex_movimentacao_ixcf_mov_titular_pessoa_fk ON siga.ex_movimentacao USING btree (id_titular);

CREATE INDEX ex_movimentacao_ixcf_mov_tp_despacho_fk ON siga.ex_movimentacao USING btree (id_tp_despacho);

CREATE INDEX ex_movimentacao_ixcf_mov_tp_mov_fk ON siga.ex_movimentacao USING btree (id_tp_mov);

CREATE INDEX ex_movimentacao_ixcf_sys_c009983 ON siga.ex_movimentacao USING btree (id_papel);

CREATE INDEX ex_movimentacao_mov_cp_identidade_fk ON siga.ex_movimentacao USING btree (id_identidade_audit);

CREATE INDEX ex_movimentacao_mov_ex_classificacao_fk ON siga.ex_movimentacao USING btree (id_classificacao);

CREATE INDEX ex_movimentacao_movimentacao_lota_resp_e_data ON siga.ex_movimentacao USING btree (id_lota_resp, dt_ini_mov);

CREATE INDEX ex_numeracao_numeracao_forma_doc_fk ON siga.ex_numeracao USING btree (id_forma_doc);

CREATE INDEX ex_preenchimento_ex_preenchimento_idx_012 ON siga.ex_preenchimento USING btree (id_lotacao);

CREATE INDEX ex_preenchimento_ex_preenchimento_idx_013 ON siga.ex_preenchimento USING btree (id_mod);

CREATE INDEX ex_preenchimento_id_arq ON siga.ex_preenchimento USING btree (id_arq);

CREATE INDEX ex_temporalidade_his_idc_fim ON siga.ex_temporalidade USING btree (his_idc_fim);

CREATE INDEX ex_temporalidade_his_idc_ini ON siga.ex_temporalidade USING btree (his_idc_ini);

CREATE INDEX ex_temporalidade_iacs_ex_temporalidade_00001 ON siga.ex_temporalidade USING btree (his_idc_ini);

CREATE INDEX ex_temporalidade_iacs_ex_temporalidade_00002 ON siga.ex_temporalidade USING btree (his_idc_fim);

CREATE INDEX ex_temporalidade_id_unidade_medida ON siga.ex_temporalidade USING btree (id_unidade_medida);

CREATE INDEX ex_tp_forma_doc_tp_forma_doc_forma_doc_fk ON siga.ex_tp_forma_doc USING btree (id_forma_doc);

CREATE INDEX ex_tp_forma_doc_tp_forma_doc_tp_doc_fk ON siga.ex_tp_forma_doc USING btree (id_tp_doc);

CREATE UNIQUE INDEX ex_tp_mov_estado_tp_mov_estado_pk ON siga.ex_tp_mov_estado USING btree (id_estado_doc, id_tp_mov);

CREATE INDEX ex_via_his_id_ini ON siga.ex_via USING btree (his_id_ini);

CREATE INDEX ex_via_his_idc_fim ON siga.ex_via USING btree (his_idc_fim);

CREATE INDEX ex_via_his_idc_ini ON siga.ex_via USING btree (his_idc_ini);

CREATE INDEX ex_via_iacs_ex_via_00001 ON siga.ex_via USING btree (his_idc_fim);

CREATE INDEX ex_via_iacs_ex_via_00002 ON siga.ex_via USING btree (his_idc_ini);

CREATE INDEX ex_via_via_classificacao_fk ON siga.ex_via USING btree (id_classificacao);

CREATE INDEX ex_via_via_tmp_corrente_fk ON siga.ex_via USING btree (id_temporal_arq_cor);

CREATE INDEX ex_via_via_tmp_intermediario_fk ON siga.ex_via USING btree (id_temporal_arq_int);

CREATE INDEX ex_via_via_tp_destinacao_fk ON siga.ex_via USING btree (id_destinacao);
