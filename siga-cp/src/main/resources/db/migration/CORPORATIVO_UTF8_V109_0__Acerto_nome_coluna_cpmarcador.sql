DECLARE V_COLUMN_EXISTS NUMBER := 0;  
BEGIN
  SELECT COUNT(*) INTO  V_COLUMN_EXISTS
    FROM ALL_TAB_COLS
    WHERE UPPER(COLUMN_NAME) = 'ID_TP_MARCADOR'
      AND UPPER(TABLE_NAME) = 'CP_MARCADOR'
	  AND OWNER = 'CORPORATIVO';


  IF (V_COLUMN_EXISTS > 0) THEN
      EXECUTE IMMEDIATE 'ALTER TABLE CORPORATIVO.CP_MARCADOR RENAME COLUMN ID_TP_MARCADOR TO ID_FINALIDADE_MARCADOR';
  END IF;
END;
/