-------------------------------------------------
--	SCRIPT: Estrutura de Marcas para Portal da Transparência. Adequação da Estrutura atual.
-------------------------------------------------
ALTER SESSION SET CURRENT_SCHEMA=corporativo;

/* Criação do Tipo de Marcador para Categorização gerenciada pela administração do sistema */
INSERT INTO CORPORATIVO.cp_tipo_marcador (id_tp_marcador,descr_tipo_marcador) VALUES (6,'Taxonomia Gerenciada');
commit;

/* Ajuste do Marcador Portal da Transparência para Marcador de Sistema */
INSERT INTO CORPORATIVO.CP_MARCADOR values(73, 'Portal da Transparência', 1, null);
UPDATE corporativo.cp_marca set id_marcador = 73 where id_marcador = 1007;
DELETE FROM CORPORATIVO.CP_MARCADOR where id_marcador = 1007;
commit;

/* Reordenação do Marcador Nota de Empenho de 1008 para 1007 */
INSERT INTO CORPORATIVO.CP_MARCADOR values(1007, 'Nota de Empenho', 6, null);
UPDATE corporativo.cp_marca set id_marcador = 1007 where id_marcador = 1008;
DELETE FROM CORPORATIVO.CP_MARCADOR where id_marcador = 1008;
commit;

/* Alteração do Marcador COVID-19 para Taxonomia Administrada */
UPDATE CORPORATIVO.CP_MARCADOR set id_tp_marcador = 6 where id_marcador = 1006;
commit;

