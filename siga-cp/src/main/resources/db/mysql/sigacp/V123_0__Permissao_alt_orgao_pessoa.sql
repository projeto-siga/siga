INSERT INTO corporativo.cp_servico ( SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
    SELECT 'SIGA-GI-CAD_PESSOA-ALT', 'Alterar Órgão Cadastro Pessoa',  max(id_servico) , '2'  
    FROM corporativo.cp_servico
    WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_PESSOA' ;
