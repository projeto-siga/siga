-- Remoção das FKs da EX_TIPO_MOVIMENTACAO que virou ENUM

ALTER TABLE `siga`.`ex_configuracao` 
DROP FOREIGN KEY `FK_TP_MOV`;

ALTER TABLE `siga`.`ex_movimentacao` 
DROP FOREIGN KEY `MOV_TP_MOV_FK`;

ALTER TABLE `siga`.`ex_estado_tp_mov` 
DROP FOREIGN KEY `TP_MOV_ESTADO_TPMOV_FK`;

ALTER TABLE `siga`.`ex_tp_mov_estado`
DROP FOREIGN KEY `TP_MOV_TPMOV_ESTADO_FK`;