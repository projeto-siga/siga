/* Permissões para botão de exportar dados para CSV */

-- Cadastro de Pessoa
INSERT INTO corporativo.cp_servico ( SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
    SELECT 'SIGA-GI-CAD_PESSOA-EXP_DADOS', 'Exportar Dados',  max(id_servico) , '2'  
    FROM corporativo.cp_servico
    WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_PESSOA' ;
    
-- Cadastro de Cargo
INSERT INTO corporativo.cp_servico (SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
    SELECT  'SIGA-GI-CAD_CARGO-EXP_DADOS', 'Exportar Dados', max(id_servico), '2'
    FROM corporativo.cp_servico 
    WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_CARGO';
    
-- Cadastro de Função de Confiança    
INSERT INTO corporativo.cp_servico (  SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
    SELECT  'SIGA-GI-CAD_FUNCAO-EXP_DADOS', 'Exportar Dados',  max(id_servico) , '2'
    FROM corporativo.cp_servico 
    WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_FUNCAO';    
    
-- Cadastro de Lotação   
INSERT INTO corporativo.cp_servico ( SIGLA_SERVICO, DESC_SERVICO, ID_SERVICO_PAI, ID_TP_SERVICO) 
   SELECT  'SIGA-GI-CAD_LOTACAO-EXP_DADOS', 'Exportar Dados',  max(id_servico) , '2'
   FROM corporativo.cp_servico 
   WHERE SIGLA_SERVICO = 'SIGA-GI-CAD_LOTACAO';    
