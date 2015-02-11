
ALTER SESSION SET CURRENT_SCHEMA = sigasr;
alter table SIGASR.SR_SERVICO rename to SR_ACAO;
rename SR_SERVICO_SEQ to SR_ACAO_SEQ;
alter table SIGASR.SR_ACAO rename column ID_SERVICO to ID_ACAO;
alter table SIGASR.SR_ACAO rename column SIGLA_SERVICO to SIGLA_ACAO;
alter table SIGASR.SR_ACAO rename column DESCR_SERVICO to DESCR_ACAO;
alter table SIGASR.SR_ACAO rename column TITULO_SERVICO to TITULO_ACAO;

alter table SIGASR.SR_SOLICITACAO rename column ID_SERVICO to ID_ACAO;
alter table SIGASR.SR_CONFIGURACAO rename column ID_SERVICO to ID_ACAO;