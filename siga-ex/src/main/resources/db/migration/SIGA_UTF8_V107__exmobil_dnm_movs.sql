---------------------------------------------------------------------------    
--  Colunas com sigla do documento e ids da última movimentação e último trâmite/recebimento na ex_mobil
---------------------------------------------------------------------------
ALTER TABLE SIGA.EX_MOBIL ADD (DNM_SIGLA VARCHAR2(40), ID_ULT_MOV NUMBER(10,0), ID_ULT_MOV_POSSE NUMBER(10,0));

COMMENT ON COLUMN "SIGA"."EX_MOBIL"."DNM_SIGLA" IS 'Sigla do mobil denormalizada ja calculada'; 
COMMENT ON COLUMN "SIGA"."EX_MOBIL"."ID_ULT_MOV" IS 'Id da ultima movimentacao realizada'; 
COMMENT ON COLUMN "SIGA"."EX_MOBIL"."ID_ULT_MOV_POSSE" IS 'Id da ultima mov de posse realizada (criacao, tramite, recebimento)'; 

ALTER TABLE SIGA.EX_MOBIL ADD CONSTRAINT MOB_ULT_MOV_FK FOREIGN KEY (ID_ULT_MOV) REFERENCES SIGA.EX_MOVIMENTACAO(ID_MOV);
ALTER TABLE SIGA.EX_MOBIL ADD CONSTRAINT MOB_ULT_MOV_POSSE_FK FOREIGN KEY (ID_ULT_MOV_POSSE) REFERENCES SIGA.EX_MOVIMENTACAO(ID_MOV);
		
---------------------------------------------------------------------------    
--  Carga inicial das colunas
--
--  Este script deve ser executado com os servidores fora do ar
--  Estimar o tempo de execução e rodar aos poucos limitando por data ou órgão se necessário
--  Somente habilitar a versão nova da mesa para algum usuário depois de atualizados todos os mobils
---------------------------------------------------------------------------
/*
UPDATE SIGA.EX_MOBIL MOB 
	SET ID_ULT_MOV = (SELECT ID_MOV FROM SIGA.EX_MOVIMENTACAO MOV 
		WHERE MOV.ID_MOBIL = MOB.ID_MOBIL 
			AND MOV.DT_TIMESTAMP = (SELECT MAX(MOVULT.DT_TIMESTAMP) FROM SIGA.EX_MOVIMENTACAO MOVULT 
										WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
										AND MOVULT.ID_MOV_CANCELADORA IS NULL)),
	ID_ULT_MOV_POSSE = (SELECT ID_MOV FROM SIGA.EX_MOVIMENTACAO MOV 
						WHERE MOV.ID_MOBIL = MOB.ID_MOBIL 
							AND MOV.DT_TIMESTAMP = (SELECT MAX(MOVULT.DT_TIMESTAMP) FROM SIGA.EX_MOVIMENTACAO MOVULT 
														WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
														AND MOVULT.ID_MOV_CANCELADORA IS NULL
														AND (MOVULT.ID_TP_MOV = 1 -- CRIACAO
															OR MOVULT.ID_TP_MOV = 3 -- TRANSFERENCIA
															OR MOVULT.ID_TP_MOV = 4 -- RECEBIMENTO
															OR MOVULT.ID_TP_MOV = 6 -- DESPACHO_TRANSFERENCIA 
															OR MOVULT.ID_TP_MOV = 8 -- DESPACHO_INTERNO_TRANSFERENCIA
															OR MOVULT.ID_TP_MOV = 17 -- TRANSFERENCIA_EXTERNA
															OR MOVULT.ID_TP_MOV = 18 -- DESPACHO_TRANSFERENCIA_EXTERNA
															OR MOVULT.ID_TP_MOV = 82 -- TRAMITE_PARALELO
															OR MOVULT.ID_TP_MOV = 83 ))), -- NOTIFICACAO
	DNM_SIGLA = (SELECT  
			CASE
			    WHEN DOC.ID_MOB_PAI IS NOT NULL AND DOC.NUM_SEQUENCIA IS NOT NULL THEN 
					NULL
			    WHEN DOC.NUM_EXPEDIENTE IS NOT NULL THEN 
			        -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
			        ORG.SIGLA_ORGAO_USU || '-' || FRM.SIGLA_FORMA_DOC || '-' || DOC.ANO_EMISSAO || '/' || TO_CHAR(DOC.NUM_EXPEDIENTE, 'FM99900000') ||  
			        CASE MOB.ID_TIPO_MOBIL 
			            WHEN 2 THEN '-' || 
			                CASE MOB.NUM_SEQUENCIA WHEN 1 THEN 'A' WHEN 2 THEN 'B' WHEN 3 THEN 'C' WHEN 4 THEN 'D' WHEN 5 THEN 'E' WHEN 6 THEN 'F' WHEN 7 THEN 'G' WHEN 8 THEN 'H' WHEN 9 THEN 'I'  
			                    WHEN 10 THEN 'J' WHEN 11 THEN 'L' WHEN 12 THEN 'M' WHEN 13 THEN 'N' WHEN 14 THEN 'O' WHEN 15 THEN 'P' WHEN 16 THEN 'Q' WHEN 17 THEN 'R' 
			                    WHEN 18 THEN 'S' WHEN 19 THEN 'T' WHEN 20 THEN 'U' WHEN 21 THEN 'Z' END
			            WHEN 4 THEN 
			                '-' || 'V' || TO_CHAR(MOB.NUM_SEQUENCIA, 'FM999999900') 
			            END
			    ELSE 
			        'TMP-' || TO_CHAR(DOC.ID_DOC, 'FM99900000')
			    END AS SIGLA_DOC
			FROM SIGA.EX_DOCUMENTO DOC 
			INNER JOIN CORPORATIVO.CP_ORGAO_USUARIO ORG on ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU
			LEFT OUTER JOIN SIGA.EX_MODELO MOD on MOD.ID_MOD = DOC.ID_MOD
			LEFT OUTER JOIN SIGA.EX_FORMA_DOCUMENTO FRM on FRM.ID_FORMA_DOC = MOD.ID_FORMA_DOC
			WHERE MOB.ID_DOC=DOC.ID_DOC)
*/