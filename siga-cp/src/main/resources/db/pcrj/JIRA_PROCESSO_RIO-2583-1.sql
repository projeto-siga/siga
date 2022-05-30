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
    1775,       --  select max(id_substituicao) from corporativo.dp_substituicao; -- último id da corporativo.dp_substituicao;
    NULL,       -- usar quando o titular da sunstituição for UMA PESSOA

        -- select max(id_lotacao) from corporativo.dp_lotacao where SIGLA_LOTACAO='15311';
        -- 12636

    '12636',     --  id da lotação DOADORA DAS PERMISSÕES --select max(id_lotacao) from corporativo.dp_lotacao where SIGLA_LOTACAO='15311';
-- 12636
    '7577732',  -- id_substituto IS DA PESSOA QUE RECEBE O PPODER DE SUBSTITUIÇÃO SELECT ID_PESSOA  FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%PALOMA DOS SANTOS FARAO%' and data_fim_pessoa is null;


    12633,      -- id_lota_substituto id da pessoa que recebe o poder de substituição ID DA LOTACAO 
                ---SELECT ID_LOTACAO FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%PALOMA DOS SANTOS FARAO%' and data_fim_pessoa is null

    timestamp '2022-05-30 00:00:00.000',   -- dt_ini_subst quando começa a substituição
    timestamp '2024-05-29 00:00:00.000',    -- dt_fim_subst quando termina a substituição
    timestamp '2022-05-30 00:00:00.000',    -- dt_ini_reg quando foi lançada a substituição
    NULL,           --  ( dt_fim_reg )é da data de fim da vigência da substituição, deve ser preenchida quando o usuário titula declinar da condição de substituição
    NULL,           -- tp_substituicao, usar nulo, parece que é uma função não implementada
    1775            --  ( ID_REG_INI ) select max(id_substituicao) from corporativo.dp_substituicao; -- último id da corporativo.dp_substituicao; mesmo valor de ID_SUBSTITUICAO
);
