/*
    Notas para o Jira PROCESSO_RIO-2878
    Dar acesso a servidora BARBARA JORGINA MACEDO PINHEIRO DE SOUZA, login SMG1462803, ID 10150195 em 52127 / GO/CEPE/SEC / ID 27481
    
    SELECT * FROM corporativo.DP_LOTACAO WHERE SIGLA_LOTACAO='52127'
    select max(id_substituicao) from corporativo.dp_substituicao; 
    SELECT * FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%BARBARA JORGINA MACEDO PINHEIRO DE SOUZA%' and data_fim_pessoa is null;    

    https://jeap.rio.rj.gov.br/jira/browse/PROCESSO_RIO-2878

*/



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
    2311,
    -- ID_SUBSTITUICAO // codigo sequencial gerado automaticamente pelo sistema. select max(id_substituicao) from corporativo.dp_substituicao; // último id da corporativo.dp_substituicao;
    NULL,
    -- id_titular  // chave estrangeira do titular, aponta para DP_PESSOA  // manter null se for um titular setor SELECT * FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%BARBARA JORGINA MACEDO PINHEIRO DE SOUZA%' and data_fim_pessoa is null;
    '27482',
    -- id_lota_titular  // refere-se a lotação do titular, no momento da substituição //nunca nulo //  id da lotação SELECT * FROM corporativo.DP_LOTACAO WHERE SIGLA_LOTACAO='51973';
    '10150195',
    -- id_substituto // chave estrangeira do substituto, refere-se a tabela DP_PESSOA  id_substituto  id da pessoa  SELECT * FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%LUCAS PAULO DE ALMEIDA COSTA%' and data_fim_pessoa is null;
    27482,
    -- id_lota_substituto // refere-se a lotação do substituto, no momento da substituição //  nunca nulo // ID DA LOTACAO SELECT ID_LOTACAO FROM corporativo.DP_PESSOA WHERE NOME_PESSOA LIKE '%LUCAS PAULO DE ALMEIDA COSTA%' and data_fim_pessoa is null; 
    timestamp '2022-09-16 00:00:00.000',
    -- dt_ini_subst, inicio da validade da substituição, valor informado na tela preenchida pelo cadastrante
    timestamp '2024-09-15 00:00:00.000',
    -- dt_fim_subst fim da validade da substituição, valor informado na tela preenchida pelo cadastrante
    timestamp '2022-09-16 00:00:00.000',
    -- dt_ini_reg se refere ao início da validade do registro, se a criada da substituicao o atributo sera preenchimento com a data corrente
    NULL,
    -- dt_fim_reg se refere ao fim da validade do registro, se a substituição for apagada receberá exclusão lógica com o preenchimento da data corrente neste atributo.
    NULL,
    -- tp_substituicao, sempre vazio em todas as tuplas
    2311
     -- ID_REG_INI, sem = ID_SUBSTITUICAO usado para o histórico no caso de alterações // sql para obter o valorselect max(id_substituicao) from corporativo.dp_substituicao; -- último id da corporativo.dp_substituicao;, 
);
commit;
