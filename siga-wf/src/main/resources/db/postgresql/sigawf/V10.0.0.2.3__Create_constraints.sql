/*
 * PK
 */

ALTER TABLE ONLY sigawf.wf_configuracao ADD CONSTRAINT wf_configuracao_pkey PRIMARY KEY (conf_id);

ALTER TABLE ONLY sigawf.wf_def_desvio ADD CONSTRAINT wf_def_desvio_pkey PRIMARY KEY (defd_id);

ALTER TABLE ONLY sigawf.wf_def_procedimento ADD CONSTRAINT wf_def_procedimento_pkey PRIMARY KEY (defp_id);

ALTER TABLE ONLY sigawf.wf_def_responsavel ADD CONSTRAINT wf_def_responsavel_pkey PRIMARY KEY (defr_id);

ALTER TABLE ONLY sigawf.wf_def_tarefa ADD CONSTRAINT wf_def_tarefa_pkey PRIMARY KEY (deft_id);

ALTER TABLE ONLY sigawf.wf_def_variavel ADD CONSTRAINT wf_def_variavel_pkey PRIMARY KEY (defv_id);

ALTER TABLE ONLY sigawf.wf_movimentacao ADD CONSTRAINT wf_movimentacao_pkey PRIMARY KEY (movi_id);

ALTER TABLE ONLY sigawf.wf_procedimento ADD CONSTRAINT wf_procedimento_pkey PRIMARY KEY (proc_id);

ALTER TABLE ONLY sigawf.wf_responsavel ADD CONSTRAINT wf_responsavel_pkey PRIMARY KEY (resp_id);

ALTER TABLE ONLY sigawf.wf_variavel ADD CONSTRAINT wf_variavel_pkey PRIMARY KEY (vari_id);

/*
 * FK
 */

ALTER TABLE ONLY sigawf.wf_configuracao ADD CONSTRAINT wf_configuracao_defp_id_fkey FOREIGN KEY (defp_id) REFERENCES sigawf.wf_def_procedimento(defp_id);

ALTER TABLE ONLY sigawf.wf_def_desvio ADD CONSTRAINT wf_def_desvio_deft_id_fkey FOREIGN KEY (deft_id) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_def_desvio ADD CONSTRAINT wf_def_desvio_deft_id_seguinte_fkey FOREIGN KEY (deft_id_seguinte) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_def_tarefa ADD CONSTRAINT wf_def_tarefa_defp_id_fkey FOREIGN KEY (defp_id) REFERENCES sigawf.wf_def_procedimento(defp_id);

ALTER TABLE ONLY sigawf.wf_def_tarefa ADD CONSTRAINT wf_def_tarefa_defr_id_fkey FOREIGN KEY (defr_id) REFERENCES sigawf.wf_def_responsavel(defr_id);

ALTER TABLE ONLY sigawf.wf_def_tarefa ADD CONSTRAINT wf_def_tarefa_deft_id_seguinte_fkey FOREIGN KEY (deft_id_seguinte) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_def_variavel ADD CONSTRAINT wf_def_variavel_deft_id_fkey FOREIGN KEY (deft_id) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_movimentacao ADD CONSTRAINT wf_movimentacao_deft_id_de_fkey FOREIGN KEY (deft_id_de) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_movimentacao ADD CONSTRAINT wf_movimentacao_deft_id_para_fkey FOREIGN KEY (deft_id_para) REFERENCES sigawf.wf_def_tarefa(deft_id);

ALTER TABLE ONLY sigawf.wf_movimentacao ADD CONSTRAINT wf_movimentacao_proc_id_fkey FOREIGN KEY (proc_id) REFERENCES sigawf.wf_procedimento(proc_id);

ALTER TABLE ONLY sigawf.wf_procedimento ADD CONSTRAINT wf_procedimento_defp_id_fkey FOREIGN KEY (defp_id) REFERENCES sigawf.wf_def_procedimento(defp_id);

ALTER TABLE ONLY sigawf.wf_responsavel ADD CONSTRAINT wf_responsavel_defr_id_fkey FOREIGN KEY (defr_id) REFERENCES sigawf.wf_def_responsavel(defr_id);

ALTER TABLE ONLY sigawf.wf_variavel ADD CONSTRAINT wf_variavel_proc_id_fkey FOREIGN KEY (proc_id) REFERENCES sigawf.wf_procedimento(proc_id);
