-- Criacao da coluna is_listavel_pesquisa
ALTER TABLE corporativo.cp_marcador ADD IS_LISTAVEL_PESQUISA tinyint(4) COMMENT '1 = lista na pesquisa de docs - 0 ou null = nao lista';

-- Seta marcadores que devem ser listados na pesquisa
UPDATE corporativo.cp_marcador SET IS_LISTAVEL_PESQUISA = 1 WHERE ID_MARCADOR <> 3 AND ID_MARCADOR <> 14 AND ID_MARCADOR <> 25;
-- Seta marcadores que nao devem ser listados na pesquisa
UPDATE corporativo.cp_marcador SET IS_LISTAVEL_PESQUISA = 0 WHERE ID_MARCADOR = 3 OR ID_MARCADOR = 14 OR ID_MARCADOR = 25;