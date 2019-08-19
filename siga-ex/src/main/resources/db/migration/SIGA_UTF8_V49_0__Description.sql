-------------------------------------------------
--	SCRIPT: Revisão de documentos
-------------------------------------------------

--insert into SIGA.EX_TIPO_MOVIMENTACAO (ID_TP_MOV,DESCR_TIPO_MOVIMENTACAO) values (65,'Solicitação de Assinatura');
update SIGA.EX_TIPO_MOVIMENTACAO set DESCR_TIPO_MOVIMENTACAO = 'Solicitação de Assinatura' where ID_TP_MOV = 65;
insert into CORPORATIVO.CP_MARCADOR values(71, 'Pronto para Assinar', 2, 25);