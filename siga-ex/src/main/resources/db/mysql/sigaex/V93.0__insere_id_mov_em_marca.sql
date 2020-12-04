ALTER TABLE corporativo.cp_marca ADD ID_MOV INT UNSIGNED COMMENT 'Referencia a movimentação que produziu essa marca';
ALTER TABLE corporativo.cp_marca ADD CONSTRAINT MARCA_MOVIMENTACAO_FK FOREIGN KEY (ID_MOV) REFERENCES siga.ex_movimentacao (ID_MOV);
  