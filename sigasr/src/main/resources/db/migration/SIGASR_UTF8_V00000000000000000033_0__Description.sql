-- Adiciona a coluna UST à tabela sr_tipo_acao 
-- O campo UST indica um número de UST para o tipo de ação  
alter table sigasr.sr_tipo_acao add UST NUMBER default 0;
