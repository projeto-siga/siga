ALTER SESSION SET CURRENT_SCHEMA=sigasr;
-----------------------------------------------
--- DDL para criar tabela de horario
----------------------------------------------
CREATE TABLE "SIGASR"."SR_HORARIO"
(
  "ID_HORARIO"  NUMBER(19,0) NOT NULL,
  "HORA_INICIO" NUMBER(2,0) NOT NULL,
  "HORA_FIM"    NUMBER(2,0) NOT NULL,
  "DIA_SEMANA_INICIO" VARCHAR2(15 CHAR),
  "DIA_SEMANA_FIM"    VARCHAR2(15 CHAR),
  "DT_INICIO" DATE,
  "DT_FIM"    DATE,
  "DESCR_HORARIO" VARCHAR2(255 CHAR),
  PRIMARY KEY ("ID_HORARIO")
);

CREATE SEQUENCE  "SIGASR"."SR_HORARIO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;

alter table sigasr.sr_configuracao add 
(
	ID_HORARIO	NUMBER(19,0),
	CONSTRAINT SR_HORARIO_FK FOREIGN KEY (ID_HORARIO) REFERENCES SIGASR.SR_HORARIO(ID_HORARIO)
);

----------------------------------------------
--- inserir horários utilizados
----------------------------------------------
insert into sigasr.sr_horario 
  values(sigasr.sr_horario_seq.nextval,11,19,'Segunda-feira','Sexta-feira',
        to_date('01/01/2015','dd/MM/yyyy'), null,
        'Horário padrão da Justiça Federal');
insert into sigasr.sr_horario 
  values(sigasr.sr_horario_seq.nextval,10,19,'Segunda-feira','Sexta-feira',
        to_date('01/01/2015','dd/MM/yyyy'), null,
        'Horário do Suporte Local');
insert into sigasr.sr_horario 
  values(sigasr.sr_horario_seq.nextval,8,20,'Segunda-feira','Sexta-feira',
        to_date('01/01/2015','dd/MM/yyyy'), null,
        'Horário da Central de Serviços da STI');

commit;

ALTER SESSION SET CURRENT_SCHEMA=corporativo;
----------------------------------------------
--- inserir tipo de configuracao
----------------------------------------------
insert into CORPORATIVO.cp_tipo_configuracao(id_tp_configuracao, dsc_tp_configuracao)
  values (307, 'Definição do horário da equipe');
/*
---------------------------------------------
--- inserir configuracao de horario
---------------------------------------------
---- Padrão
insert into corporativo.cp_configuracao (id_configuracao,his_dt_ini,id_tp_configuracao,his_id_ini)
  values(corporativo.cp_configuracao_seq.nextval, 
        (select sysdate from dual), 
        (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe'),
        corporativo.cp_configuracao_seq.currval);
      
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

insert into sigasr.sr_configuracao(fg_atributo_obrigatorio,id_configuracao_sr,id_horario)
  values('N', 
    (select id_configuracao from corporativo.cp_configuracao where  
      id_tp_configuracao = (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe')
    ), 1);
    
    
ALTER SESSION SET CURRENT_SCHEMA=corporativo;    
---- Suporte Local   
insert into corporativo.cp_configuracao (id_configuracao,his_dt_ini,id_tp_configuracao,id_grupo,his_id_ini)
  values(corporativo.cp_configuracao_seq.nextval, 
        (select sysdate from dual), 
        (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe'),
        (select id_grupo from corporativo.cp_grupo where sigla_grupo = 'STI_SUPORTE_LOCAL'),
        corporativo.cp_configuracao_seq.currval);
 
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

insert into sigasr.sr_configuracao(fg_atributo_obrigatorio,id_configuracao_sr,id_horario)
  values('N', 
    (select id_configuracao from corporativo.cp_configuracao where 
      id_grupo = (select id_grupo from corporativo.cp_grupo where sigla_grupo = 'STI_SUPORTE_LOCAL') and 
      id_tp_configuracao = (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe')
    ), 2);
    
    
ALTER SESSION SET CURRENT_SCHEMA=corporativo; 
--- Central
insert into corporativo.cp_configuracao (id_configuracao,his_dt_ini,id_tp_configuracao,id_grupo,his_id_ini)
  values(corporativo.cp_configuracao_seq.nextval, 
        (select sysdate from dual), 
        (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe'),
        (select id_grupo from corporativo.cp_grupo where sigla_grupo = 'STI_CENTRAL'),
        corporativo.cp_configuracao_seq.currval);
    
ALTER SESSION SET CURRENT_SCHEMA=sigasr;

insert into sigasr.sr_configuracao(fg_atributo_obrigatorio,id_configuracao_sr,id_horario)
  values('N', 
    (select id_configuracao from corporativo.cp_configuracao where 
      id_grupo = (select id_grupo from corporativo.cp_grupo where sigla_grupo = 'STI_CENTRAL') and 
      id_tp_configuracao = (select id_tp_configuracao from corporativo.cp_tipo_configuracao where dsc_tp_configuracao = 'Definição do horário da equipe')
    ), 3);
*/
commit;
  