insert into sigagc.gc_acesso values (7, 'Grupo');
alter table sigagc.gc_informacao add id_grupo number (19,0) constraint gc_informacao_grupo_fk references corporativo.cp_grupo(id_grupo);