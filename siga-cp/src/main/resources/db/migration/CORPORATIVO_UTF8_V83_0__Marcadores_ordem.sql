-------------------------------------------
--	SCRIPT: DEFINE A ORDEM DOS MARCADORES A SEREM ATRIBUÍDOS AOS DOCUMENTOS
--	ORDENAÇÃO DEFINIDA NO TRELLO 1446
-------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

update corporativo.cp_marcador set ORD_MARCADOR = 1100 where ID_MARCADOR = 1006;
update corporativo.cp_marcador set ORD_MARCADOR = 1200 where ID_MARCADOR = 1005;
update corporativo.cp_marcador set ORD_MARCADOR = 1300 where ID_MARCADOR = 1001;
update corporativo.cp_marcador set ORD_MARCADOR = 1400 where ID_MARCADOR = 1007;
update corporativo.cp_marcador set ORD_MARCADOR = 1500 where ID_MARCADOR = 1010;
update corporativo.cp_marcador set ORD_MARCADOR = 1600 where ID_MARCADOR = 1009;
update corporativo.cp_marcador set ORD_MARCADOR = 1700 where ID_MARCADOR = 1008;
update corporativo.cp_marcador set ORD_MARCADOR = 1800 where ID_MARCADOR = 1003;
update corporativo.cp_marcador set ORD_MARCADOR = 1900 where ID_MARCADOR = 1004;
update corporativo.cp_marcador set ORD_MARCADOR = 2000 where ID_MARCADOR = 1000;
