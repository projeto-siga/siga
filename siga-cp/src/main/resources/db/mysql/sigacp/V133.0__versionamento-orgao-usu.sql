ALTER TABLE `corporativo`.`cp_orgao_usuario` ADD (ID_ORGAO_USU_INICIAL INT UNSIGNED);
ALTER TABLE `corporativo`.`cp_orgao_usuario` ADD (DT_ALTERACAO DATE);
ALTER TABLE `corporativo`.`cp_orgao_usuario` ADD (MARCO_REGULATORIO varchar(500));

ALTER TABLE `corporativo`.`cp_orgao_usuario` add constraint ORGAO_USU_INICIAL_ORGAO_USU_FK foreign key(ID_ORGAO_USU_INICIAL) references cp_orgao_usuario(ID_ORGAO_USU);

SET SQL_SAFE_UPDATES = 0;
UPDATE `corporativo`.`cp_orgao_usuario` SET ID_ORGAO_USU_INICIAL = ID_ORGAO_USU where ID_ORGAO_USU_INICIAL  is null;
SET SQL_SAFE_UPDATES = 1;