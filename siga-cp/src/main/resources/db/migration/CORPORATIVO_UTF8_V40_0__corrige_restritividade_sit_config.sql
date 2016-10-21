ALTER SESSION SET CURRENT_SCHEMA = CORPORATIVO;
update corporativo.cp_situacao_configuracao set restritividade_sit_conf = 5 where id_sit_configuracao = 5;
update corporativo.cp_situacao_configuracao set restritividade_sit_conf = 6 where id_sit_configuracao = 6;
update corporativo.cp_situacao_configuracao set restritividade_sit_conf = 4 where id_sit_configuracao = 4;