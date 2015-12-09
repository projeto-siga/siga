
ALTER SESSION SET CURRENT_SCHEMA = CORPORATIVO;

insert into corporativo.cp_localidade (id_localidade, nm_localidade, id_uf) values (25, 'Serra', 8);
insert into corporativo.cp_localidade (id_localidade, nm_localidade, id_uf) values (26, 'Colatina', 8);
insert into corporativo.cp_localidade (id_localidade, nm_localidade, id_uf) values (27, 'Linhares', 8);

insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (33, 'Serra', 25, 2);
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (34, 'Colatina', 26, 2);
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (35, 'São Mateus', 18,	2);
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (36, 'Linhares',	27, 2);
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (37, 'Cachoeiro de Itapemirim', 3,	2);

-- Incluindo marcadores gerails para documentos
insert into cp_marcador values(69, 'Necessita Providência', 1, null);

commit;