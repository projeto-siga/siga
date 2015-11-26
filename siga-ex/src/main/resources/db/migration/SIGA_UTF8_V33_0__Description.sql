-- Criando colunas para viabilizar a importação e exportação do acervo de modelos

alter table siga.ex_modelo add (NM_DIRETORIO varchar(128) null);
alter table siga.ex_modelo add (UUID) varchar(48) null);