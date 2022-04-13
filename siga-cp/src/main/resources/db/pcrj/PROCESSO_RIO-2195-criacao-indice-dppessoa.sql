-- drop index corporativo.cp_id_pessoa_inicial_idx;
create index corporativo.cp_id_pessoa_inicial_idx on corporativo.dp_pessoa (id_pessoa_inicial)
compute statistics
tablespace corporativo_index
;
