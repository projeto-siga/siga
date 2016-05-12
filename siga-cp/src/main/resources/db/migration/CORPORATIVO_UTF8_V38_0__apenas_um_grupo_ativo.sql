/*
 * 
 * SCRIPT PARA DESATIVAR CP_GRUPO QUE ESTEJAM DUPLICADOS.
 * ISSUE #777: https://github.com/projeto-siga/siga/issues/777 
 * 
 * */
ALTER SESSION SET CURRENT_SCHEMA = CORPORATIVO;

--DESATIVA REGISTROS QUE ESTAO ATIVOS ERRONEAMENTE (GRUPOS DUPLICADOS)   
UPDATE CORPORATIVO.CP_GRUPO G1 SET HIS_ATIVO = 0, HIS_DT_FIM = SYSDATE 
  WHERE EXISTS (
                  SELECT * FROM CORPORATIVO.CP_GRUPO G2 
                    WHERE G1.ID_ORGAO_USU = G2.ID_ORGAO_USU  
                      AND G1.ID_TP_GRUPO = G2.ID_TP_GRUPO 
                      AND G1.SIGLA_GRUPO = G2.SIGLA_GRUPO 
                  HAVING SUM(HIS_ATIVO) > 1 
                  GROUP BY ID_ORGAO_USU,ID_TP_GRUPO,SIGLA_GRUPO
               )
  AND ID_GRUPO NOT IN (
                        SELECT MAX(ID_GRUPO) FROM CORPORATIVO.CP_GRUPO G3 
                        WHERE EXISTS (
                                        SELECT * FROM CORPORATIVO.CP_GRUPO G4 
                                          WHERE G3.ID_ORGAO_USU = G4.ID_ORGAO_USU  
                                            AND G3.ID_TP_GRUPO = G4.ID_TP_GRUPO  
                                            AND G3.SIGLA_GRUPO = G4.SIGLA_GRUPO 
                                          HAVING SUM(HIS_ATIVO) > 1 
                                          GROUP BY ID_ORGAO_USU,ID_TP_GRUPO,SIGLA_GRUPO
                                     )
                        AND HIS_ATIVO = 1 
                        GROUP BY ID_ORGAO_USU,ID_TP_GRUPO,SIGLA_GRUPO
                      );


CREATE UNIQUE INDEX apenas_um_grupo_ativo on CORPORATIVO.CP_GRUPO (CASE WHEN HIS_ATIVO=1 THEN ID_ORGAO_USU || '_' || ID_TP_GRUPO || '_' || SIGLA_GRUPO END);

COMMIT;