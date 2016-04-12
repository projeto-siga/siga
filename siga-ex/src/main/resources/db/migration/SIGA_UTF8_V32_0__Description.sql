-- Criando coluna para representar a data e hora da ultima atualizacao de um documento

alter table siga.ex_documento add (HIS_DT_ALT date default sysdate not null);
