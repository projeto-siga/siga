-- Criacao da coluna LISTAVEL_PESQUISA_DEFAULT

alter table corporativo.cp_marcador add LISTAVEL_PESQUISA_DEFAULT number(1, 0);
-- Seta marcadores que devem ser listados na pesquisa
update corporativo.cp_marcador set LISTAVEL_PESQUISA_DEFAULT = 1 where id_marcador <> 3 and id_marcador <> 14 and id_marcador <> 25;
-- Seta marcadores que nao devem ser listados na pesquisa
update corporativo.cp_marcador set LISTAVEL_PESQUISA_DEFAULT = 0 where id_marcador = 3 or id_marcador = 14 or id_marcador = 25;

comment on column "CORPORATIVO"."CP_MARCADOR"."LISTAVEL_PESQUISA_DEFAULT" is '1 = lista na pesquisa de docs - 0 ou null = nao lista';
