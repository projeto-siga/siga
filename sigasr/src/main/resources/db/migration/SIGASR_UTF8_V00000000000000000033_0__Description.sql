-- Adiciona a coluna UST à tabela sr_tipo_acao 
-- O campo UST indica um número de UST para o tipo de ação  
alter table sigasr.sr_tipo_acao add UST NUMBER default 0;
comment on column SIGASR.SR_TIPO_ACAO.UST is 'O campo UST (Unidade de Serviço Técnico) mede a complexidade de uma determinada ação a ser executada por uma empresa contratada';
