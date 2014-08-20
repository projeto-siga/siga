-------------------------------------------
--	SCRIPT: ADICIONAR SERVIÇO
-------------------------------------------

-- Adicionar serviço para acesso a pasta de videoconferência
insert into corporativo.cp_servico (id_servico,sigla_servico,desc_servico,id_servico_pai,id_tp_servico)
      values (corporativo.cp_servico_seq.nextval,'FS-VIDEO','Acesso ao Diretório de Videoconferência', 
                (select cp.id_servico from corporativo.cp_servico cp where cp.sigla_servico = 'FS'), -- id_servico_pai = 5
                (select tp.id_tp_servico from corporativo.cp_tipo_servico tp where tp.desc_tp_servico = 'Diretório') -- id_tp_servico = 1
             );
             
-- Adicionar serviço para acesso a pasta de audiência
insert into corporativo.cp_servico (id_servico,sigla_servico,desc_servico,id_servico_pai,id_tp_servico)
      values (corporativo.cp_servico_seq.nextval,'FS-AUD','Acesso ao Diretório de Audiência', 
              (select cp.id_servico from corporativo.cp_servico cp where cp.sigla_servico = 'FS'), -- id_servico_pai = 5
              (select tp.id_tp_servico from corporativo.cp_tipo_servico tp where tp.desc_tp_servico = 'Diretório') -- id_tp_servico = 1
             );
             

-------------------------------------------
--	SCRIPT: ADICIONAR CONFIGURAÇÃO
-------------------------------------------

insert into corporativo.cp_configuracao (id_configuracao,his_dt_ini,id_sit_configuracao,id_tp_configuracao,id_servico,id_tp_lotacao)
    values (corporativo.cp_configuracao_seq.nextval,
              (select sysdate from dual),
              (select sit.id_sit_configuracao from corporativo.cp_situacao_configuracao sit where sit.dsc_sit_configuracao = 'Pode'),202,
              (select cp.id_servico from corporativo.cp_servico cp where cp.sigla_servico = 'FS-AUD'),
              (select tp.id_tp_lotacao from corporativo.cp_tipo_lotacao tp where tp.sigla_tp_lotacao = 'JUD')
           );  
             
insert into corporativo.cp_configuracao (id_configuracao,his_dt_ini,id_sit_configuracao,id_tp_configuracao,id_servico,id_tp_lotacao)
    values (corporativo.cp_configuracao_seq.nextval,
              (select sysdate from dual),
              (select sit.id_sit_configuracao from corporativo.cp_situacao_configuracao sit where sit.dsc_sit_configuracao = 'Pode'),202,
              (select cp.id_servico from corporativo.cp_servico cp where cp.sigla_servico = 'FS-VIDEO'),
              (select tp.id_tp_lotacao from corporativo.cp_tipo_lotacao tp where tp.sigla_tp_lotacao = 'JUD')
           ); 