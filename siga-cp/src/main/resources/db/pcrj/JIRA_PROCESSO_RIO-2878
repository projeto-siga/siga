INSERT INTO dp_substituicao (
    ID_SUBSTITUICAO,
    id_titular,
    id_lota_titular,
    id_substituto,
    id_lota_substituto,
    dt_ini_subst,
    dt_fim_subst,
    dt_ini_reg,
    dt_fim_reg,
    tp_substituicao,
    ID_REG_INI
) VALUES (
    1695,       --  select max(id_substituicao) from corporativo.dp_substituicao; -- último id da corporativo.dp_substituicao;
    NULL,       -- não usado, já que o titular é um setor e não uma pessoa
    '6323',     --  id da lotação SELECT * FROM corporativo.DP_LOTACAO WHERE SIGLA_LOTACAO='51973';
    '7412616',  -- id da pessoa  SELECT * FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%LUCAS PAULO DE ALMEIDA COSTA%' and data_fim_pessoa is null;
    27412,      -- ID DA LOTACAO SELECT ID_LOTACAO FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%LUCAS PAULO DE ALMEIDA COSTA%' and data_fim_pessoa is null; 
    timestamp '2022-05-17 00:00:00.000', 
    timestamp '2024-05-16 00:00:00.000',
    timestamp '2022-05-17 00:00:00.000',
    NULL,
    NULL,
    1695            --  select max(id_substituicao) from corporativo.dp_substituicao; -- último id da corporativo.dp_substituicao;
);
commit;
 SELECT * FROM corporativo.DP_LOTACAO WHERE SIGLA_LOTACAO='51275';
 SELECT * FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%HENRIQUE DI SPAGNA DAINESE%' and data_fim_pessoa is null;
 SELECT ID_LOTACAO FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%LUCAS PAULO DE ALMEIDA COSTA%' and data_fim_pessoa is null; 
 select max(id_substituicao) from corporativo.dp_substituicao;
