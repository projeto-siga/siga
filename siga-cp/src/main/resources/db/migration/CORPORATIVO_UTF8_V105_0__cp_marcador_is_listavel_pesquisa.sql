-- Criacao da coluna is_listavel_pesquisa
alter session set current_schema=corporativo;

alter table corporativo.cp_marcador add is_listavel_pesquisa number(1, 0);
-- Seta marcadores que devem ser listados na pesquisa
update corporativo.cp_marcador set is_listavel_pesquisa = 1 where id_marcador <> 3 and id_marcador <> 14 and id_marcador <> 25;
-- Seta marcadores que nao devem ser listados na pesquisa
update corporativo.cp_marcador set is_listavel_pesquisa = 0 where id_marcador = 3 or id_marcador = 14 or id_marcador = 25;

comment on column "CORPORATIVO"."CP_MARCADOR"."IS_LISTAVEL_PESQUISA" is '1 = lista na pesquisa de docs - 0 ou null = nao lista';
