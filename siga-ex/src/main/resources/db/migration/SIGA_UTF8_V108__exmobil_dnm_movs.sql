---------------------------------------------------------------------------    
--  Colunas com sigla do documento, id da última movimentação e data da última mov na ex_mobil
---------------------------------------------------------------------------
ALTER TABLE SIGA.EX_MOBIL ADD (DNM_SIGLA VARCHAR2(40), ID_ULT_MOV NUMBER(10,0), DNM_DT_ULT_MOV DATE);

COMMENT ON COLUMN "SIGA"."EX_MOBIL"."DNM_SIGLA" IS 'Sigla do mobil denormalizada ja calculada'; 
COMMENT ON COLUMN "SIGA"."EX_MOBIL"."ID_ULT_MOV" IS 'Id da ultima movimentacao realizada.'; 
COMMENT ON COLUMN "SIGA"."EX_MOBIL"."DNM_DT_ULT_MOV" IS 'Data da ultima movimentacao realizada. Se nao houver movs, data da criacao.'; 

-- -------------------------------------------------------------------------    
--  Carga inicial das colunas
--
--  Este script deve ser executado com os servidores fora do ar
--  Estimar o tempo de execução e rodar aos poucos limitando por data ou órgão se necessário
--  Somente habilitar a versão nova da mesa para algum usuário depois de atualizados todos os mobils
-- -------------------------------------------------------------------------
/*
UPDATE SIGA.EX_MOBIL MOB 
	SET ID_ULT_MOV = (SELECT MOV.ID_MOV FROM (SELECT MOVULT.ID_MOV FROM siga.ex_movimentacao MOVULT 
													WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
													AND MOVULT.ID_MOV_CANCELADORA IS NULL
													ORDER BY MOVULT.DT_TIMESTAMP DESC, MOVULT.ID_MOV DESC
													) MOV WHERE ROWNUM = 1), 
	DNM_DT_ULT_MOV = (SELECT COALESCE((SELECT MOV.DT_INI_MOV FROM (SELECT MOVULT.DT_INI_MOV FROM siga.ex_movimentacao MOVULT 
											WHERE MOVULT.ID_MOBIL = MOB.ID_MOBIL
											AND MOVULT.ID_MOV_CANCELADORA IS NULL
											ORDER BY MOVULT.DT_TIMESTAMP DESC, MOVULT.ID_MOV DESC
											) MOV WHERE ROWNUM =1),
					(SELECT DOC.HIS_DT_ALT FROM siga.ex_documento DOC WHERE DOC.ID_DOC = MOB.ID_DOC))
				FROM DUAL),
	DNM_SIGLA = (SELECT  
			CASE
			    WHEN DOC.ID_MOB_PAI IS NOT NULL AND DOC.NUM_SEQUENCIA IS NOT NULL THEN 
					(SELECT  
					        -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
					        ORG.SIGLA_ORGAO_USU || '-' || FRM.SIGLA_FORMA_DOC || '-' || DOCPAI.ANO_EMISSAO || '/' || TO_CHAR(DOCPAI.NUM_EXPEDIENTE, 'FM99900000') ||  
					        (CASE MOBPAI.ID_TIPO_MOBIL 
					            WHEN 2 THEN '-' || 
					                CASE MOBPAI.NUM_SEQUENCIA WHEN 1 THEN 'A' WHEN 2 THEN 'B' WHEN 3 THEN 'C' WHEN 4 THEN 'D' WHEN 5 THEN 'E' WHEN 6 THEN 'F' WHEN 7 THEN 'G' WHEN 8 THEN 'H' WHEN 9 THEN 'I'  
					                    WHEN 10 THEN 'J' WHEN 11 THEN 'L' WHEN 12 THEN 'M' WHEN 13 THEN 'N' WHEN 14 THEN 'O' WHEN 15 THEN 'P' WHEN 16 THEN 'Q' WHEN 17 THEN 'R' 
					                    WHEN 18 THEN 'S' WHEN 19 THEN 'T' WHEN 20 THEN 'U' WHEN 21 THEN 'Z' END
					            WHEN 4 THEN 
					                '-' || 'V' || TO_CHAR(MOBPAI.NUM_SEQUENCIA, 'FM999999900')
					            END) || '.' || TO_CHAR(DOC.NUM_SEQUENCIA, 'FM999999900') AS SIGLA_DOC
						FROM (SELECT * FROM siga.ex_mobil MOBPAI WHERE MOBPAI.ID_MOBIL = DOC.ID_MOB_PAI) MOBPAI
						INNER JOIN siga.ex_documento DOCPAI ON MOBPAI.ID_DOC = DOCPAI.ID_DOC
						INNER JOIN corporativo.cp_orgao_usuario ORG on ORG.ID_ORGAO_USU = DOCPAI.ID_ORGAO_USU
						LEFT OUTER JOIN siga.ex_modelo MODELO on MODELO.ID_MOD = DOCPAI.ID_MOD
						LEFT OUTER JOIN siga.ex_forma_documento FRM on FRM.ID_FORMA_DOC = MODELO.ID_FORMA_DOC
						WHERE MOBPAI.ID_MOBIL = DOC.ID_MOB_PAI)
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
			FROM siga.ex_documento DOC 
			INNER JOIN corporativo.cp_orgao_usuario ORG on ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU
			LEFT OUTER JOIN siga.ex_modelo modelo on MODELO.ID_MOD = DOC.ID_MOD
			LEFT OUTER JOIN siga.ex_forma_documento FRM on FRM.ID_FORMA_DOC = MODELO.ID_FORMA_DOC
			WHERE MOB.ID_DOC=DOC.ID_DOC);
*/

-- ----------------------------------------------------------------------------------------------   
-- O código abaixo substitui o update acima na versão 11 do Oracle. Foi executado no TRF2. 
-- ----------------------------------------------------------------------------------------------   
/*
 merge into SIGA.EX_MOBIL f
 using (
 select b.ID_MOBIL, b.ID_MOV, b.DT_INI_MOV from 
  (SELECT b.ID_MOBIL, b.ID_MOV, b.DT_INI_MOV, row_number() over (partition by b.ID_MOBIL order by b.DT_TIMESTAMP desc) rn
   FROM SIGA.EX_MOVIMENTACAO b where b.ID_MOV_CANCELADORA IS NULL ) b 
 where b.rn = 1
 ) b
 on (f.ID_MOBIL = b.ID_MOBIL)
 when matched then update set
   f.ID_ULT_MOV = b.ID_MOV,
   f.DNM_DT_ULT_MOV = b.DT_INI_MOV;   
   
  merge into SIGA.EX_MOBIL f
  using (
  SELECT MOB.ID_MOBIL, 
    CASE
      WHEN DOC.ID_MOB_PAI   IS NOT NULL
      AND DOC.NUM_SEQUENCIA IS NOT NULL
      THEN
        (SELECT
          -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
          ORG.SIGLA_ORGAO_USU
          || '-'
          || FRM.SIGLA_FORMA_DOC
          || '-'
          || DOCPAI.ANO_EMISSAO
          || '/'
          || TO_CHAR(DOCPAI.NUM_EXPEDIENTE, 'FM99900000')
          || (
          CASE MOBPAI.ID_TIPO_MOBIL
            WHEN 2
            THEN '-'
              ||
              CASE MOBPAI.NUM_SEQUENCIA
                WHEN 1
                THEN 'A'
                WHEN 2
                THEN 'B'
                WHEN 3
                THEN 'C'
                WHEN 4
                THEN 'D'
                WHEN 5
                THEN 'E'
                WHEN 6
                THEN 'F'
                WHEN 7
                THEN 'G'
                WHEN 8
                THEN 'H'
                WHEN 9
                THEN 'I'
                WHEN 10
                THEN 'J'
                WHEN 11
                THEN 'L'
                WHEN 12
                THEN 'M'
                WHEN 13
                THEN 'N'
                WHEN 14
                THEN 'O'
                WHEN 15
                THEN 'P'
                WHEN 16
                THEN 'Q'
                WHEN 17
                THEN 'R'
                WHEN 18
                THEN 'S'
                WHEN 19
                THEN 'T'
                WHEN 20
                THEN 'U'
                WHEN 21
                THEN 'Z'
              END
            WHEN 4
            THEN '-'
              || 'V'
              || TO_CHAR(MOBPAI.NUM_SEQUENCIA, 'FM999999900')
          END)
          || '.'
          || TO_CHAR(DOC.NUM_SEQUENCIA, 'FM999999900') AS SIGLA_DOC
        FROM
           siga.ex_documento DOCPAI   
        INNER JOIN corporativo.cp_orgao_usuario ORG
        ON ORG.ID_ORGAO_USU = DOCPAI.ID_ORGAO_USU
        LEFT OUTER JOIN siga.ex_modelo MODELO
        ON MODELO.ID_MOD = DOCPAI.ID_MOD
        LEFT OUTER JOIN siga.ex_forma_documento FRM
        ON FRM.ID_FORMA_DOC   = MODELO.ID_FORMA_DOC
        WHERE MOBPAI.ID_MOBIL = DOC.ID_MOB_PAI AND
              MOBPAI.ID_DOC = DOCPAI.ID_DOC
        )
      WHEN DOC.NUM_EXPEDIENTE IS NOT NULL
      THEN
        -- ORG.ACRONIMO_ORGAO_USU -- PARA QUEM UTILIZA PROPERTY codigo.acronimo.ano.inicial = true
        ORG.SIGLA_ORGAO_USU
        || '-'
        || FRM.SIGLA_FORMA_DOC
        || '-'
        || DOC.ANO_EMISSAO
        || '/'
        || TO_CHAR(DOC.NUM_EXPEDIENTE, 'FM99900000')
        ||
        CASE MOB.ID_TIPO_MOBIL
          WHEN 2
          THEN '-'
            ||
            CASE MOB.NUM_SEQUENCIA
              WHEN 1
              THEN 'A'
              WHEN 2
              THEN 'B'
              WHEN 3
              THEN 'C'
              WHEN 4
              THEN 'D'
              WHEN 5
              THEN 'E'
              WHEN 6
              THEN 'F'
              WHEN 7
              THEN 'G'
              WHEN 8
              THEN 'H'
              WHEN 9
              THEN 'I'
              WHEN 10
              THEN 'J'
              WHEN 11
              THEN 'L'
              WHEN 12
              THEN 'M'
              WHEN 13
              THEN 'N'
              WHEN 14
              THEN 'O'
              WHEN 15
              THEN 'P'
              WHEN 16
              THEN 'Q'
              WHEN 17
              THEN 'R'
              WHEN 18
              THEN 'S'
              WHEN 19
              THEN 'T'
              WHEN 20
              THEN 'U'
              WHEN 21
              THEN 'Z'
            END
          WHEN 4
          THEN '-'
            || 'V'
            || TO_CHAR(MOB.NUM_SEQUENCIA, 'FM999999900')
        END
      ELSE 'TMP-'
        || TO_CHAR(DOC.ID_DOC, 'FM99900000')
    END AS SIGLA_DOC
  FROM siga.ex_documento DOC
  INNER JOIN SIGA.EX_MOBIL MOB 
  ON DOC.ID_DOC = MOB.ID_DOC
  LEFT OUTER JOIN siga.ex_mobil MOBPAI ON MOBPAI.ID_MOBIL = DOC.ID_MOB_PAI
  INNER JOIN corporativo.cp_orgao_usuario ORG
  ON ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU
  LEFT OUTER JOIN siga.ex_modelo modelo
  ON MODELO.ID_MOD = DOC.ID_MOD
  LEFT OUTER JOIN siga.ex_forma_documento FRM
  ON FRM.ID_FORMA_DOC = MODELO.ID_FORMA_DOC


  ) b
  on (f.ID_MOBIL = b.ID_MOBIL)
  when matched then update set
     f.DNM_SIGLA = b.SIGLA_DOC;
  
 
*/
