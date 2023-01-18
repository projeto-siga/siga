-- Adicionado 2 campos para Persistência do Motivo da Inativação e Ativação. 
-- Campos são separados devido a estrutura de versionamento permitir Ativar com Motivo e com a mesma linha Inativar com Motivo adicionando 

ALTER TABLE `corporativo`.`dp_lotacao`
ADD COLUMN `MOTIVO_INATIVACAO` VARCHAR(500) NULL AFTER `IS_SUSPENSA`,
ADD COLUMN `MOTIVO_ATIVACAO` VARCHAR(500) NULL AFTER `MOTIVO_INATIVACAO`;