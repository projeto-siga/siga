-- Criacao da coluna is_LISTAVEL_PESQUISA_DEFAULT
ALTER TABLE corporativo.cp_marcador ADD LISTAVEL_PESQUISA_DEFAULT tinyint(4) COMMENT '1 = lista na pesquisa de docs - 0 ou null = nao lista';

-- Seta marcadores que devem ser listados na pesquisa
UPDATE corporativo.cp_marcador SET LISTAVEL_PESQUISA_DEFAULT = 1 WHERE ID_MARCADOR <> 3 AND ID_MARCADOR <> 14 AND ID_MARCADOR <> 25;
-- Seta marcadores que nao devem ser listados na pesquisa
UPDATE corporativo.cp_marcador SET LISTAVEL_PESQUISA_DEFAULT = 0 WHERE ID_MARCADOR = 3 OR ID_MARCADOR = 14 OR ID_MARCADOR = 25;