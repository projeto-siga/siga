-------------------------------------------
--	SCRIPT: DEFINE A ORDEM DOS MARCADORES A SEREM ATRIBUÍDOS AOS DOCUMENTOS
--	ORDENAÇÃO DEFINIDA NO TRELLO 1446
-------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

update corporativo.cp_marcador set ORD_MARCADOR = 1100 where ID_MARCADOR = 1006; -- COVID-19
update corporativo.cp_marcador set ORD_MARCADOR = 1200 where ID_MARCADOR = 1005; -- Documento Analisado
update corporativo.cp_marcador set ORD_MARCADOR = 1300 where ID_MARCADOR = 1001; -- Idoso
update corporativo.cp_marcador set ORD_MARCADOR = 1400 where ID_MARCADOR = 1007; -- Nota de Empenho
update corporativo.cp_marcador set ORD_MARCADOR = 1500 where ID_MARCADOR = 1010; -- Demanda Judicial Prioridade Alta
update corporativo.cp_marcador set ORD_MARCADOR = 1600 where ID_MARCADOR = 1009; -- Demanda Judicial Prioridade Média
update corporativo.cp_marcador set ORD_MARCADOR = 1700 where ID_MARCADOR = 1008; -- Demanda Judicial Prioridade Baixa
update corporativo.cp_marcador set ORD_MARCADOR = 1800 where ID_MARCADOR = 1003; -- Prioritário
update corporativo.cp_marcador set ORD_MARCADOR = 1900 where ID_MARCADOR = 1004; -- Restrição de Acesso
update corporativo.cp_marcador set ORD_MARCADOR = 2000 where ID_MARCADOR = 1000; -- Urgente
