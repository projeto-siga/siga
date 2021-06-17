CREATE INDEX wf_configuracao_fk_defp ON sigawf.wf_configuracao USING btree (defp_id);

CREATE INDEX wf_def_desvio_fk_defd_deft_id ON sigawf.wf_def_desvio USING btree (deft_id);

CREATE INDEX wf_def_desvio_fk_defd_deft_id_seguinte ON sigawf.wf_def_desvio USING btree (deft_id_seguinte);

CREATE INDEX wf_def_tarefa_fk_deft_defp_id ON sigawf.wf_def_tarefa USING btree (defp_id);

CREATE INDEX wf_def_tarefa_fk_deft_defr_id ON sigawf.wf_def_tarefa USING btree (defr_id);

CREATE INDEX wf_def_tarefa_fk_deft_deft_id_seguinte ON sigawf.wf_def_tarefa USING btree (deft_id_seguinte);

CREATE INDEX wf_def_variavel_fk_defv_deft_id ON sigawf.wf_def_variavel USING btree (deft_id);

CREATE INDEX wf_movimentacao_fk_movi_deft_id_de ON sigawf.wf_movimentacao USING btree (deft_id_de);

CREATE INDEX wf_movimentacao_fk_movi_deft_id_para ON sigawf.wf_movimentacao USING btree (deft_id_para);

CREATE INDEX wf_movimentacao_fk_movi_proc_id ON sigawf.wf_movimentacao USING btree (proc_id);

CREATE INDEX wf_procedimento_fk_proc_defp_id ON sigawf.wf_procedimento USING btree (defp_id);

CREATE INDEX wf_responsavel_fk_resp_defr_id ON sigawf.wf_responsavel USING btree (defr_id);

CREATE INDEX wf_variavel_fk_vari_proc_id ON sigawf.wf_variavel USING btree (proc_id);
