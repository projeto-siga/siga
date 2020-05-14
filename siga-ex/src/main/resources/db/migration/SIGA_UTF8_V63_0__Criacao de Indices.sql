CREATE INDEX corporativo.iacs_cp_aplicacao_feriad_00001 
  ON corporativo.cp_aplicacao_feriado (id_lotacao) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_conf_cp_ident_idc_fim_fk 
  ON corporativo.cp_configuracao (his_idc_fim) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_conf_cp_ident_idc_ini_fk 
  ON corporativo.cp_configuracao (his_idc_ini) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_conf_cp_ident_id_ident_fk 
  ON corporativo.cp_configuracao (id_identidade) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_conf_dp_lot_id_lotacao_fk 
  ON corporativo.cp_configuracao (id_lotacao) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_conf_dp_pes_id_pessoa_fk 
  ON corporativo.cp_configuracao (id_pessoa) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_grp_cp_ident_idc_fim_fk 
  ON corporativo.cp_grupo (his_idc_fim) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_grp_cp_ident_idc_ini_fk 
  ON corporativo.cp_grupo (his_idc_ini) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_ident_cp_ident_idc_fim_fk 
  ON corporativo.cp_identidade (his_idc_fim) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_ident_cp_ident_idc_ini_fk 
  ON corporativo.cp_identidade (his_idc_ini) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_ident_cp_ident_id_ini_fk 
  ON corporativo.cp_identidade (his_id_ini) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.iacs_cp_marca_00002 
  ON corporativo.cp_marca (id_mobil) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.iacs_cp_modelo_00001 
  ON corporativo.cp_modelo (his_idc_ini) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.iacs_cp_modelo_00002 
  ON corporativo.cp_modelo (his_idc_fim) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_papel_dp_lot_id_lot_fk 
  ON corporativo.cp_papel (id_lotacao) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_papel_dp_pess_id_pess_fk 
  ON corporativo.cp_papel (id_pessoa) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_person_dp_lot_id_subst_fk 
  ON corporativo.cp_personalizacao (id_substituindo_lotacao) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.cp_person_dp_pess_id_subst_fk 
  ON corporativo.cp_personalizacao (id_substituindo_pessoa) INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.substituicao_pesoa_subst_fk 
  ON corporativo.dp_substituicao (id_substituto) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX corporativo.substituicao_pesoa_titular_fk 
  ON corporativo.dp_substituicao (id_titular) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_classificacao_00001 
  ON siga.ex_classificacao (his_idc_fim) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_classificacao_00002 
  ON siga.ex_classificacao (his_idc_ini) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.competencia_lotacao_fk 
  ON siga.ex_competencia (id_lotacao) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.competencia_pessoa_fk 
  ON siga.ex_competencia (id_pessoa) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.fk_mod 
  ON siga.ex_configuracao (id_mod) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.siga_exdoc_mob_autuado_id_ix 
  ON siga.ex_documento (id_mob_autuado) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_modelo_00001 
  ON siga.ex_modelo (his_idc_ini) INITRANS 
20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_modelo_00002 
  ON siga.ex_modelo (his_idc_fim) INITRANS 
20 compute STATISTICS nologging; 

CREATE INDEX siga.mov_cp_identidade_fk 
  ON siga.ex_movimentacao (id_identidade_audit) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_temporalidade_00001 
  ON siga.ex_temporalidade (his_idc_ini) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_temporalidade_00002 
  ON siga.ex_temporalidade (his_idc_fim) 
INITRANS 20 compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_via_00001 
  ON siga.ex_via (his_idc_fim) INITRANS 20 
compute STATISTICS nologging; 

CREATE INDEX siga.iacs_ex_via_00002 
  ON siga.ex_via (his_idc_ini) INITRANS 20 
compute STATISTICS nologging; 

CREATE INDEX siga.bol_boletim_fk 
  ON siga.ex_boletim_doc (id_boletim) 
INITRANS 20 compute STATISTICS nologging;
