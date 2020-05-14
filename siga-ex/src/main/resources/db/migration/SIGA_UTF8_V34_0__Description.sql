
--Após as alterações para as funcionalidades de destinação (commit 66e1044807065e2b7a), cada classificação deve
--estar ligada apenas a vias ativas, com data de fim não nula. Isso deveria ter sido feito naquela época.

--1. Para as classificações com data de fim nula que possuem via(s) com data de fim não nula, cria novo registro de 
--classificação tendo como data de início a data de início da última via, da classificação, que possui data de fim nula
insert into siga.ex_classificacao (
  id_classificacao,
  descr_classificacao, 
  obs, 
  his_id_ini, 
  his_dt_ini, 
  his_dt_fim, 
  his_idc_ini, 
  his_idc_fim, 
  his_ativo, 
  codificacao 
) select 
  (siga.ex_classificacao_seq.nextval) id_classificacao,
  c.descr_classificacao, 
  c.obs, 
  c.his_id_ini, 
  v.his_dt_ini, 
  null, 
  v.his_idc_ini, 
  null, 
  1, 
  c.codificacao 
from siga.ex_classificacao c inner join siga.ex_via v on c.id_classificacao = v.id_classificacao
where c.id_classificacao in (
  select distinct(c2.id_classificacao)
  from siga.ex_classificacao c2 inner join siga.ex_via v2 on c2.id_classificacao = v2.id_classificacao
  where c2.his_dt_fim is null 
  and v2.his_dt_fim is not null
)
and v.his_dt_fim is null
and c.id_classificacao = 2624
and not exists (
  select 1 from siga.ex_via
  where id_classificacao = v.id_classificacao
  and his_dt_ini > v.his_dt_ini
  and his_dt_fim is null
);

--2. Fecha todos os registros antigos das classificações abrangidas pelo script 1, acima
update siga.ex_classificacao c set his_dt_fim = (
  select his_dt_ini from siga.ex_classificacao where his_id_ini = c.his_id_ini and his_dt_fim is null and his_dt_ini > c.his_dt_ini
), his_ativo = 0 where his_dt_fim is null and exists (
	select 1 from siga.ex_classificacao where his_id_ini = c.his_id_ini and his_dt_fim is null and his_dt_ini > c.his_dt_ini
);

--3. Obtém todas as vias ativas ainda ligadas aos registros fechados pelo script 2 acima e as conecta aos novos regsitros
--correspondentes inseridos pelo script 1
update siga.ex_via v set id_classificacao = (
	select id_classificacao from siga.ex_classificacao where his_id_ini = (
		select his_id_ini from siga.ex_classificacao where id_classificacao = v.id_classificacao
	) and his_dt_fim is null
) where his_dt_fim is null 
and (
	select his_dt_fim from siga.ex_classificacao where id_classificacao = v.id_classificacao
) is not null;

commit;
