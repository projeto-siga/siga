-- Permissao para filtrar documentos na mesa 
Insert into corporativo.cp_servico (ID_SERVICO, SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
    values (corporativo.cp_servico_seq.nextval, 'SIGA-DOC-MESA2-FILTRO', 'Permitir usar o filtro de pesquisa da mesa', 
    (select id_servico from corporativo.cp_servico WHERE SIGLA_SERVICO = 'SIGA-DOC-MESA2') , '2');

-- Permissao default para todos poderem filtrar a mesa
Insert into corporativo.cp_configuracao (ID_CONFIGURACAO, HIS_DT_INI, ID_SIT_CONFIGURACAO, ID_TP_CONFIGURACAO, ID_SERVICO, HIS_IDC_INI)
	values (corporativo.cp_configuracao_seq.nextval, sysdate, '1', '200', 
	(SELECT ID_SERVICO FROM corporativo.cp_servico WHERE SIGLA_SERVICO = 'SIGA-DOC-MESA2-FILTRO'), '1');