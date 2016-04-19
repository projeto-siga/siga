ALTER SESSION SET CURRENT_SCHEMA=corporativo;

-- inserindo o local Beneditinos para abertura de solicitações
insert into corporativo.cp_complexo (id_complexo, nome_complexo, id_localidade, id_orgao_usu) values (
  (select max(id_complexo)+1 from corporativo.cp_complexo), 'Beneditinos', 15 , 3);

  
-- alteração do local TRF 2ª REGIÃO para Acre  
update corporativo.cp_complexo set nome_complexo = 'Acre' where nome_complexo = 'TRF 2ª REGIÃO' and id_orgao_usu = 3;  
commit;