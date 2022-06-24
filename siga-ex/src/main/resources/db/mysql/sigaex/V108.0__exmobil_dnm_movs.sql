-- -------------------------------------------------------------------------    
--  Colunas com sigla do documento, id da última movimentação e data da última mov na ex_mobil
-- -------------------------------------------------------------------------
ALTER TABLE siga.ex_mobil ADD (DNM_SIGLA VARCHAR(40), ID_ULT_MOV INT UNSIGNED DEFAULT NULL, DNM_DT_ULT_MOV DATETIME);

-- -------------------------------------------------------------------------    
--  Carga inicial das colunas
--
--  Este script deve ser executado com os servidores fora do ar
--  Estimar o tempo de execução e rodar aos poucos limitando por data ou órgão se necessário
--  Somente habilitar a versão nova da mesa para algum usuário depois de atualizados todos os mobils
-- -------------------------------------------------------------------------
/*

SET SQL_SAFE_UPDATES = 0;
UPDATE siga.ex_mobil MOB 
	SET ID_ULT_MOV = (SELECT MOV.ID_MOV FROM siga.ex_movimentacao MOV 
					WHERE MOV.ID_MOBIL = MOB.ID_MOBIL 
						AND MOV.ID_MOV = (SELECT MOVULT.ID_MOV FROM siga.ex_movimentacao MOVULT 
													WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
													AND MOVULT.ID_MOV_CANCELADORA IS NULL
													ORDER BY MOVULT.DT_TIMESTAMP DESC, MOVULT.ID_MOV DESC
													LIMIT 1)), 
	DNM_DT_ULT_MOV = (SELECT COALESCE((SELECT MOV.DT_INI_MOV FROM siga.ex_movimentacao MOV 
					WHERE MOV.ID_MOBIL = MOB.ID_MOBIL 
					AND MOV.ID_MOV = (SELECT MOVULT.ID_MOV FROM siga.ex_movimentacao MOVULT 
											WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
											AND MOVULT.ID_MOV_CANCELADORA IS NULL
											ORDER BY MOVULT.DT_TIMESTAMP DESC, MOVULT.ID_MOV DESC
											LIMIT 1)),
					(SELECT DOC.HIS_DT_ALT FROM siga.ex_documento DOC WHERE DOC.ID_DOC = MOB.ID_DOC))
							FROM DUAL),
	DNM_SIGLA = (SELECT  
			CASE
			    WHEN DOC.ID_MOB_PAI IS NOT NULL AND DOC.NUM_SEQUENCIA IS NOT NULL THEN 
					(SELECT  
					        -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
					        CONCAT(ORG.SIGLA_ORGAO_USU, '-', FRM.SIGLA_FORMA_DOC, '-', DOCPAI.ANO_EMISSAO, '/', LPAD(DOCPAI.NUM_EXPEDIENTE, 5, 0),
					        (CASE MOBPAI.ID_TIPO_MOBIL 
								WHEN 1 THEN ''
					            WHEN 2 THEN CONCAT('-',
					                CASE MOBPAI.NUM_SEQUENCIA WHEN 1 THEN 'A' WHEN 2 THEN 'B' WHEN 3 THEN 'C' WHEN 4 THEN 'D' WHEN 5 THEN 'E' WHEN 6 THEN 'F' WHEN 7 THEN 'G' WHEN 8 THEN 'H' WHEN 9 THEN 'I'  
					                    WHEN 10 THEN 'J' WHEN 11 THEN 'L' WHEN 12 THEN 'M' WHEN 13 THEN 'N' WHEN 14 THEN 'O' WHEN 15 THEN 'P' WHEN 16 THEN 'Q' WHEN 17 THEN 'R' 
					                    WHEN 18 THEN 'S' WHEN 19 THEN 'T' WHEN 20 THEN 'U' WHEN 21 THEN 'Z' END)
					            WHEN 4 THEN 
					            	CONCAT('-' , 'V', LPAD(MOB.NUM_SEQUENCIA, 2, 0))
					            END), 
							'.', LPAD(DOC.NUM_SEQUENCIA, 2, 0)) 
						FROM (SELECT * FROM siga.ex_mobil MOBPAI WHERE MOBPAI.ID_MOBIL = DOC.ID_MOB_PAI) MOBPAI
                        INNER JOIN siga.ex_documento DOCPAI ON MOBPAI.ID_DOC = DOCPAI.ID_DOC
						INNER JOIN corporativo.cp_orgao_usuario ORG ON ORG.ID_ORGAO_USU = DOCPAI.ID_ORGAO_USU
						LEFT OUTER JOIN siga.ex_modelo MODELO ON MODELO.ID_MOD = DOCPAI.ID_MOD
						LEFT OUTER JOIN siga.ex_forma_documento FRM ON FRM.ID_FORMA_DOC = MODELO.ID_FORMA_DOC) 
			    WHEN DOC.NUM_EXPEDIENTE IS NOT NULL THEN 
			        -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
			        CONCAT(ORG.SIGLA_ORGAO_USU, '-', FRM.SIGLA_FORMA_DOC, '-', DOC.ANO_EMISSAO, '/', LPAD(DOC.NUM_EXPEDIENTE, 5, 0),
						CASE MOB.ID_TIPO_MOBIL 
							WHEN 1 THEN '' 
							WHEN 2 THEN CONCAT('-', 
								CASE MOB.NUM_SEQUENCIA WHEN 1 THEN 'A' WHEN 2 THEN 'B' WHEN 3 THEN 'C' WHEN 4 THEN 'D' WHEN 5 THEN 'E' WHEN 6 THEN 'F' WHEN 7 THEN 'G' WHEN 8 THEN 'H' WHEN 9 THEN 'I'  
									WHEN 10 THEN 'J' WHEN 11 THEN 'L' WHEN 12 THEN 'M' WHEN 13 THEN 'N' WHEN 14 THEN 'O' WHEN 15 THEN 'P' WHEN 16 THEN 'Q' WHEN 17 THEN 'R' 
									WHEN 18 THEN 'S' WHEN 19 THEN 'T' WHEN 20 THEN 'U' WHEN 21 THEN 'Z' END)
							WHEN 4 THEN 
								CONCAT('-' , 'V', LPAD(MOB.NUM_SEQUENCIA, 2, 0))
							END
					)
			    ELSE 
			        CONCAT('TMP-', LPAD(DOC.ID_DOC, 5, 0))
			    END AS SIGLA_DOC
			FROM siga.ex_documento DOC 
			INNER JOIN corporativo.cp_orgao_usuario ORG on ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU
			LEFT OUTER JOIN siga.ex_modelo MODELO on MODELO.ID_MOD = DOC.ID_MOD
			LEFT OUTER JOIN siga.ex_forma_documento FRM on FRM.ID_FORMA_DOC = MODELO.ID_FORMA_DOC
			WHERE MOB.ID_DOC=DOC.ID_DOC);

SET SQL_SAFE_UPDATES = 1; 						
			
*/
