UPDATE CORPORATIVO.CP_SERVICO SET sigla_servico='SIGA-GI-GDISTR',desc_servico='Gerenciar grupos de distribuição' where sigla_servico='SIGA-GI-GEMAIL';
update corporativo.cp_tipo_grupo set desc_tp_grupo = 'Grupo de Distribuição' where desc_tp_grupo = 'Grupo de Email';

insert into corporativo.cp_tipo_configuracao (id_tp_configuracao,dsc_tp_configuracao,id_sit_configuracao) values (206,'Gerenciar Grupo',2);

--Insert into CP_SERVICO (ID_SERVICO,SIGLA_SERVICO,DESC_SERVICO,ID_SERVICO_PAI,ID_TP_SERVICO) values (21,'SIGA-GI-GEMAIL','Gerenciar grupos de email',15,2);