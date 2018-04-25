ALTER SESSION SET CURRENT_SCHEMA=corporativo;
INSERT INTO corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) VALUES (
  (SELECT MAX(id_complexo)+1 FROM corporativo.cp_complexo), 'Visconde de Inhaúma', 15 , 3);
COMMIT;