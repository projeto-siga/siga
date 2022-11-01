--
-- movimentações as quais a data fim é anterior a data de inicio, e o seu respectivo documento
--
select m.dt_ini_mov, m.dt_fim_mov,
--       m.dt_fim_mov - m.dt_ini_mov as duração,
       m.id_tp_mov,
       (select tm.descr_tipo_movimentacao from siga.ex_tipo_movimentacao tm where tm.id_tp_mov = m.id_tp_mov) as Evento,
       m.descr_mov, m.dt_mov,
       (
         select distinct -- relação de todos os processos 
                u.acronimo_orgao_usu  || '-' ||
                f.sigla_forma_doc     || '-' ||
                d.ano_emissao         || '/' ||
                lpad (d.num_expediente, 5, '0') "NºDOCUMENTO" 
           from siga.ex_documento d 
                inner join siga.ex_mobil mb                    on (mb.id_doc             = d.id_doc) 
                inner join siga.ex_movimentacao m2             on (m2.id_mobil           = mb.id_mobil) 
                inner join siga.ex_tipo_movimentacao tm        on (tm.id_tp_mov          = m.id_tp_mov)     -- IGS 
                inner join siga.ex_classificacao c             on (c.id_classificacao    = d.id_classificacao) 
                inner join corporativo.dp_lotacao l            on (d.id_lota_cadastrante = l.id_lotacao)      
                inner join corporativo.cp_orgao_usuario u      on (l.id_orgao_usu        = u.id_orgao_usu) 
                inner join siga.ex_forma_documento f           on (f.id_forma_doc        = d.id_forma_doc) 
                inner join siga.ex_modelo md                   on (md.id_mod             = d.id_mod) 
          where m.id_mobil = m2.id_mobil
       ) as Nro_doc,
       m.obs_orgao_mov
from siga.ex_movimentacao m
where m.dt_ini_mov > m.dt_fim_mov
  and extract (year from m.dt_ini_mov) >= 2022
order by m.dt_ini_mov desc
;


