
insert into GC_TIPO_INFORMACAO (ID_TIPO_INFORMACAO, NOME_TIPO_INFORMACAO, arquivo) values (4, 'Ponto de Entrada', NULL)
commit;

insert into sigagc.gc_acesso values (7, 'Lotação e Grupo');
commit;
alter table sigagc.gc_informacao add id_grupo number (19,0) constraint gc_informacao_grupo_fk references corporativo.cp_grupo(id_grupo);

