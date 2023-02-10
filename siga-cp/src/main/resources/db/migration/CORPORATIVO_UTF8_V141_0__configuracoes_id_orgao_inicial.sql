-- configurações de orgãos que já foram versionados que precisam converter o id_orgao_usu para id_inicial
update corporativo.cp_configuracao c set id_orgao_usu = (select id_orgao_usu_inicial from corporativo.cp_orgao_usuario where id_orgao_usu = c.id_orgao_usu)
where id_configuracao in (
select id_configuracao from corporativo.cp_configuracao conf
inner join corporativo.cp_orgao_usuario org on org.id_orgao_usu = conf.id_orgao_usu
where conf.HIS_DT_FIM is null  and org.HIS_DT_FIM is null and id_orgao_usu_inicial <> org.id_orgao_usu and id_orgao_usu_inicial in (
select id_orgao_usu_inicial from corporativo.cp_orgao_usuario group by id_orgao_usu_inicial having count(1) > 1));
