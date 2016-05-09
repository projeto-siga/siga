-- cria novo tipo de conhecimento Processo de Trabalho e associa a novo template (gc_arquivo)

insert into sigagc.gc_arquivo (id_conteudo, conteudo_tipo, titulo, conteudo) values ((select max(id_conteudo)+1 from sigagc.gc_arquivo), 'text/html', 'Template para Processo de Trabalho', null);
insert into sigagc.gc_tipo_informacao (id_tipo_informacao, nome_tipo_informacao, arquivo) values ((select max(id_tipo_informacao)+1 from sigagc.gc_tipo_informacao), 'Processo de Trabalho', (select max(id_conteudo) from sigagc.gc_arquivo));
commit;