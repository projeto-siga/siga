insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select NVL(max(id_conteudo), 1) from sigagc.gc_arquivo), 'text/html', 'Template para Erro Conhecido', null);
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (2, 'Erro Conhecido', (select max(id_conteudo) from sigagc.gc_arquivo));
commit;

insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select max(id_conteudo)+1 from sigagc.gc_arquivo), 'text/html', 'Template para Procedimento', null);
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (3, 'Procedimento', (select max(id_conteudo) from sigagc.gc_arquivo));
commit;