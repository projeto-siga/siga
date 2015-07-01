-------------------------------------------
--	SCRIPT:CORPORATIVO_DESTINACAO
-- Cria nova coluna ORD_MARCADOR para ordenar os marcadores de expedientes da página inicial do Siga-doc
-------------------------------------------

alter table corporativo.cp_marcador add ORD_MARCADOR number;

update corporativo.cp_marcador set ord_marcador = 10 where id_marcador = 1;
update corporativo.cp_marcador set ord_marcador = 20 where id_marcador = 39;
update corporativo.cp_marcador set ord_marcador = 30 where id_marcador = 25;
update corporativo.cp_marcador set ord_marcador = 40 where id_marcador = 15;
update corporativo.cp_marcador set ord_marcador = 50 where id_marcador = 60;
update corporativo.cp_marcador set ord_marcador = 60 where id_marcador = 30;
update corporativo.cp_marcador set ord_marcador = 70 where id_marcador = 29;
update corporativo.cp_marcador set ord_marcador = 80 where id_marcador = 14;
update corporativo.cp_marcador set ord_marcador = 90 where id_marcador = 3;
update corporativo.cp_marcador set ord_marcador = 100 where id_marcador = 56;
update corporativo.cp_marcador set ord_marcador = 110 where id_marcador = 58;
update corporativo.cp_marcador set ord_marcador = 120 where id_marcador = 57;
update corporativo.cp_marcador set ord_marcador = 130 where id_marcador = 59;
update corporativo.cp_marcador set ord_marcador = 140 where id_marcador = 2;
update corporativo.cp_marcador set ord_marcador = 150 where id_marcador = 27;
update corporativo.cp_marcador set ord_marcador = 160 where id_marcador = 28;
update corporativo.cp_marcador set ord_marcador = 170 where id_marcador = 23;
update corporativo.cp_marcador set ord_marcador = 180 where id_marcador = 24;
update corporativo.cp_marcador set ord_marcador = 190 where id_marcador = 31;
update corporativo.cp_marcador set ord_marcador = 200 where id_marcador = 6;
update corporativo.cp_marcador set ord_marcador = 210 where id_marcador = 51;
update corporativo.cp_marcador set ord_marcador = 220 where id_marcador = 50;
update corporativo.cp_marcador set ord_marcador = 230 where id_marcador = 7;
update corporativo.cp_marcador set ord_marcador = 240 where id_marcador = 52;