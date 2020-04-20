---------------------------------------------------------------------------    
-- CADASTRO DE SERVICO (SIGA-DOC-FE-PAINEL) 
---------------------------------------------------------------------------
Insert into CORPORATIVO.CP_SERVICO (ID_SERVICO,SIGLA_SERVICO,DESC_SERVICO,ID_SERVICO_PAI,ID_TP_SERVICO) values (CORPORATIVO.CP_SERVICO_SEQ.nextval,'SIGA-DOC-FE-PAINEL','Painel Administrativo - Pesquisa de Documentos',(SELECT ID_SERVICO FROM CORPORATIVO.CP_SERVICO WHERE SIGLA_SERVICO = 'SIGA-DOC-FE'),'2');
