ALTER TABLE corporativo.cp_marca ADD (ID_MOV  NUMBER(10,0);
COMMENT ON COLUMN corporativo.cp_marca.ID_MOV IS 'Referencia a movimentação que produziu essa marca';
ALTER TABLE corporativo.cp_marca ADD CONSTRAINT MARCA_MOVIMENTACAO_FK FOREIGN KEY (ID_MOV) REFERENCES siga.ex_movimentacao (ID_MOV);
  