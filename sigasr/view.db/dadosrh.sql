CREATE  OR REPLACE FORCE VIEW "SIGARH"."DADOS_RH" ("PESSOA_ID", "PESSOA_CPF", "PESSOA_NOME", "PESSOA_SEXO", "PESSOA_DATA_NASCIMENTO", "PESSOA_RUA", "PESSOA_BAIRRO", "PESSOA_CIDADE", "PESSOA_UF", "PESSOA_CEP", "PESSOA_MATRICULA", "PESSOA_DATA_INICIO_EXERCICIO", "PESSOA_ATO_NOMEACAO", "PESSOA_DATA_NOMEACAO", "PESSOA_DT_PUBL_NOMEACAO", "PESSOA_DATA_POSSE", "PESSOA_PADRAO_REFERENCIA", "PESSOA_SITUACAO", "PESSOA_RG", "PESSOA_RG_ORGAO", "PESSOA_RG_UF", "PESSOA_DATA_EXPEDICAO_RG", "PESSOA_ESTADO_CIVIL", "PESSOA_SIGLA", "PESSOA_EMAIL", "PESSOA_GRAU_DE_INSTRUCAO", "PESSOA_TP_SANGUINEO", "PESSOA_NATURALIDADE", "PESSOA_NACIONALIDADE", "TIPO_RH", "CARGO_ID", "CARGO_NOME", "CARGO_SIGLA", "FUNCAO_ID", "FUNCAO_NOME", "FUNCAO_SIGLA", "LOTACAO_ID", "LOTACAO_NOME", "LOTACAO_SIGLA", "LOTACAO_ID_PAI", "LOTACAO_TIPO", "LOTACAO_TIPO_PAPEL", "PAPEL_ID") AS 
SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'SERVIDOR'                                                                                                                                                                                                                                   AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_SERVIDOR S
  ON S.ID_CAD_SERVIDOR=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO             =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO       = 1
  AND U.ID_CAD_ORGAO          = 1
  AND PF.ID_CAD_TP_SANGUINEO  = TPS.ID_CAD_TP_SANGUINEO
  AND PF.ID_CAD_NACIONALIDADE = NAC.ID_CAD_NACIONALIDADE
  UNION
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'SERVIDOR'                                                                                                                                                                                                                                   AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    'Unidade da Administração'                                                                                                                                                                                                                   AS LOTACAO_TIPO,
    'Funcional'                                                                                                                                                                                                                                  AS LOTACAO_TIPO_PAPEL,
    LAF.ID_LOT_LOTACAO                                                                                                                                                                                                                           AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_SERVIDOR S
  ON S.ID_CAD_SERVIDOR=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_ALOCACAO_FUNCIONAL LAF
  ON LAF.ID_CAD_RECURSO_HUMANO = RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LAF.id_lot_lotacao
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LAF.ID_LOT_UNIDADE
  INNER JOIN SIGARH.lot_equip_trab_funcional O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.LOT_EQUIP_TRAB_FUNCIONAL LETF
  ON LETF.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA_FUNCIONAL LTCF
  ON LTCF.ID_LOT_TP_CATEGORIA_FUNCIONAL = LETF.ID_LOT_TP_CATEGORIA_FUNCIONAL
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO             =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO       = 1
  AND U.ID_CAD_ORGAO          = 1
  AND PF.ID_CAD_TP_SANGUINEO  = TPS.ID_CAD_TP_SANGUINEO
  AND PF.ID_CAD_NACIONALIDADE = NAC.ID_CAD_NACIONALIDADE
  UNION
  --SERVIDOR REQUISITADO DA CARREIRA JUDICIÁRIA
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'SERVIDOR'                                                                                                                                                                                                                                   AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_SERV_CARGO_VAGA SCV
  ON SCV.ID_CAD_SERV_CARGO_VAGA=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION
  --SERVIDOR REQUISITADO SEM VÍNCULO
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'SERVIDOR'                                                                                                                                                                                                                                   AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_SERVIDOR_SEM_VINCULO SSV
  ON SSV.ID_CAD_SERVIDOR_SEM_VINCULO=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION
  --SERVIDOR REQUISITADO DE OUTROS ÓRGÃOS
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'SERVIDOR'                                                                                                                                                                                                                                   AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_SERV_SEM_CARREIRA_JF SSCJ
  ON SSCJ.ID_CAD_SERV_SEM_CARREIRA_JF =RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION
  --ESTAGIARIO
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'ESTAGIÁRIO'                                                                                                                                                                                                                                 AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_ESTAGIARIO E
  ON E.ID_CAD_ESTAGIARIO=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION
  -- ESTAGIARIO-EST
    SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'ESTAGIÁRIO'                                                                                                                                                                                                                                 AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  LEFT OUTER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  LEFT OUTER  JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.EST_ESTAGIARIO E
  ON E.ESTA_ID=RH.ID_CAD_RECURSO_HUMANO
  LEFT OUTER  JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  LEFT OUTER  JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION  
  -- TERCEIRIZADOS
  SELECT CRE.ID_CAD_RECURSO_EXTERNO AS PESSOA_ID,
    PF.CPF                          AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				 	AS PESSOA_NOME,
    PF.SEXO                         AS PESSOA_SEXO,
    PF.DT_NASCIMENTO                AS PESSOA_DATA_NASCIMENTO,
    NULL                            AS PESSOA_RUA,
    NULL                            AS PESSOA_BAIRRO,
    NULL                            AS PESSOA_CIDADE,
    NULL                            AS PESSOA_UF,
    NULL                            AS PESSOA_CEP,
    CRE.MATRICULA                   AS PESSOA_MATRICULA,
    NULL                            AS PESSOA_DATA_INICIO_EXERCICIO,
    NULL                            AS PESSOA_ATO_NOMEACAO,
    NULL                            AS PESSOA_DATA_NOMEACAO,
    NULL                            AS PESSOA_DT_PUBL_NOMEACAO,
    NULL                            AS PESSOA_DATA_POSSE,
    NULL                            AS PESSOA_PADRAO_REFERENCIA,
    1                               AS PESSOA_SITUACAO,
    PF.NUM_RG                       AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG              AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                 AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL          AS PESSOA_ESTADO_CIVIL,
    CRE.SG_RECURSO_EXTERNO          AS PESSOA_SIGLA,
    DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL) AS PESSOA_EMAIL,
    NULL                            AS PESSOA_GRAU_DE_INSTRUCAO,
    TPS.NM_TP_SANGUINEO             AS PESSOA_TP_SANGUINEO,
    NULL                            AS PESSOA_NATURALIDADE,
    NULL                            AS PESSOA_NACIONALIDADE,
    'TERCEIRIZADO'                  AS TIPO_RH,
    999                             AS CARGO_ID,
    NULL                            AS CARGO_NOME,
    NULL                            AS CARGO_SIGLA,
    NULL                            AS FUNCAO_ID,
    NULL                            AS FUNCAO_NOME,
    NULL                            AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                AS LOTACAO_ID,
    U.NM_UNIDADE                    AS LOTACAO_NOME,
    U.SG_UNIDADE                    AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE            AS LOTACAO_ID_PAI,
    'Agrupamento Operacional'       AS LOTACAO_TIPO,
    'Funcional'                     AS LOTACAO_TIPO_PAPEL,
    LAF.ID_LOT_LOTACAO              AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_EXTERNO CRE
  ON CRE.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TERCEIRIZADO CT
  ON CT.ID_CAD_TERCEIRIZADO = CRE.ID_CAD_RECURSO_EXTERNO
  INNER JOIN SIGARH.CAD_CONTRATO CTT
  ON CTT.ID_CAD_CONTRATO=CT.ID_CAD_CONTRATO
  INNER JOIN SIGARH.LOT_ALOCACAO_FUNCIONAL LAF
  ON laf.id_cad_recurso_externo = cre.id_cad_recurso_externo
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=laf.id_lot_lotacao
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE = laf.id_lot_unidade
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE = HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.LOT_EQUIP_TRAB_FUNCIONAL LETF
  ON LETF.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA_FUNCIONAL LTCF
  ON LTCF.ID_LOT_TP_CATEGORIA_FUNCIONAL = LETF.ID_LOT_TP_CATEGORIA_FUNCIONAL
  WHERE CRE.ID_CAD_ORGAO                = 1
  AND U.ID_CAD_ORGAO                    = 1
  AND CRE.DT_INICIO<=SYSDATE
  AND (CRE.DT_FIM IS NULL OR CRE.DT_FIM>=SYSDATE)
  AND CTT.DT_INICIO<=SYSDATE
  AND (CTT.DT_TERMINO IS NULL OR CTT.DT_TERMINO>=SYSDATE)
  UNION
  -- TERCEIRIZADOS COM LOTACAO OFICIAL
  SELECT CRE.ID_CAD_RECURSO_EXTERNO AS PESSOA_ID,
    PF.CPF                          AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  	AS PESSOA_NOME,
    PF.SEXO                         AS PESSOA_SEXO,
    PF.DT_NASCIMENTO                AS PESSOA_DATA_NASCIMENTO,
    NULL                            AS PESSOA_RUA,
    NULL                            AS PESSOA_BAIRRO,
    NULL                            AS PESSOA_CIDADE,
    NULL                            AS PESSOA_UF,
    NULL                            AS PESSOA_CEP,
    CRE.MATRICULA                   AS PESSOA_MATRICULA,
    NULL                            AS PESSOA_DATA_INICIO_EXERCICIO,
    NULL                            AS PESSOA_ATO_NOMEACAO,
    NULL                            AS PESSOA_DATA_NOMEACAO,
    NULL                            AS PESSOA_DT_PUBL_NOMEACAO,
    NULL                            AS PESSOA_DATA_POSSE,
    NULL                            AS PESSOA_PADRAO_REFERENCIA,
    1                               AS PESSOA_SITUACAO,
    PF.NUM_RG                       AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG              AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                 AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL          AS PESSOA_ESTADO_CIVIL,
    CRE.SG_RECURSO_EXTERNO          AS PESSOA_SIGLA,
    DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL) AS PESSOA_EMAIL,
    NULL                            AS PESSOA_GRAU_DE_INSTRUCAO,
    TPS.NM_TP_SANGUINEO             AS PESSOA_TP_SANGUINEO,
    NULL                            AS PESSOA_NATURALIDADE,
    NULL                            AS PESSOA_NACIONALIDADE,
    'TERCEIRIZADO'                  AS TIPO_RH,
    999                             AS CARGO_ID,
    NULL                            AS CARGO_NOME,
    NULL                            AS CARGO_SIGLA,
    NULL                            AS FUNCAO_ID,
    NULL                            AS FUNCAO_NOME,
    NULL                            AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                AS LOTACAO_ID,
    U.NM_UNIDADE                    AS LOTACAO_NOME,
    U.SG_UNIDADE                    AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE            AS LOTACAO_ID_PAI,
    'Agrupamento Operacional'       AS LOTACAO_TIPO,
    'Funcional'                     AS LOTACAO_TIPO_PAPEL,
    LAF.ID_LOT_LOTACAO              AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_EXTERNO CRE
  ON CRE.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TERCEIRIZADO CT
  ON CT.ID_CAD_TERCEIRIZADO = CRE.ID_CAD_RECURSO_EXTERNO
  INNER JOIN SIGARH.CAD_CONTRATO CTT
  ON CTT.ID_CAD_CONTRATO=CT.ID_CAD_CONTRATO
  INNER JOIN SIGARH.LOT_ALOCACAO_FUNCIONAL LAF
  ON laf.id_cad_recurso_externo = cre.id_cad_recurso_externo
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=laf.id_lot_lotacao
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE = laf.id_lot_unidade
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE = HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.LOT_Organizacional LETF
  ON LETF.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA LTCF
  ON LTCF.ID_LOT_TIPO_CATEGORIA = LETF.ID_LOT_TIPO_CATEGORIA
  WHERE CRE.ID_CAD_ORGAO                = 1
  AND U.ID_CAD_ORGAO                    = 1
  AND CRE.DT_INICIO<=SYSDATE
  AND (CRE.DT_FIM IS NULL OR CRE.DT_FIM>=SYSDATE)
  AND CTT.DT_INICIO<=SYSDATE
  AND (CTT.DT_TERMINO IS NULL OR CTT.DT_TERMINO>=SYSDATE)
  UNION
  --JUIZ COM LOTAÇÃO TITULAR EXIBINDO SOMENTE A LOTAÇÃO TITULAR
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'MAGISTRADO'                                                                                                                                                                                                                                 AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_JUIZ J
  ON J.ID_CAD_JUIZ=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  AND LO.FG_TITULAR          ='S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO       =TP.ID_FUNCAO
  WHERE RH.ID_CAD_ORGAO = 1
  AND U.ID_CAD_ORGAO    = 1
  UNION
  --JUIZ SOMENTE COM LOTAÇÕES SUBSTITUTAS EXIBINDO SOMENTE A LOTAÇÃO SUBSTITUTA COM MAIOR DATA DE INÍCIO
  SELECT RH.ID_CAD_RECURSO_HUMANO AS PESSOA_ID,
    PF.CPF                        AS PESSOA_CPF,
    NVL(PF.NOME_SOCIAL, 
    PF.NM_PESSOA) 				  AS PESSOA_NOME,
    PF.SEXO                       AS PESSOA_SEXO,
    PF.DT_NASCIMENTO              AS PESSOA_DATA_NASCIMENTO,
    NVL((PF.NM_LOGRADOURO
    || ' '
    || PF.NUM_LOGRADOURO
    || ' '
    || PF.COMPLEMENTO_LOGRADOURO), ' ') AS PESSOA_RUA,
    PF.BAIRRO                           AS PESSOA_BAIRRO,
    PF.CIDADE                           AS PESSOA_CIDADE,
    PF.UF_LOGRADOURO                    AS PESSOA_UF,
    PF.CEP                              AS PESSOA_CEP,
    RH.MATRICULA                        AS PESSOA_MATRICULA,
    TP.DT_EXERCICIO                     AS PESSOA_DATA_INICIO_EXERCICIO,
    TP.ATO_NOMEACAO                     AS PESSOA_ATO_NOMEACAO,
    TP.DT_NOMEACAO                      AS PESSOA_DATA_NOMEACAO,
    TP.DT_PUBL_ATO_NOMEACAO             AS PESSOA_DT_PUBL_NOMEACAO,
    TP.DT_POSSE                         AS PESSOA_DATA_POSSE,
    CASE
      WHEN C.SG_CLASSE IS NOT NULL
      THEN C.SG_CLASSE
        || '-'
        || P.SG_PADRAO
      ELSE ''
    END                                                                                                                                                                                                                                          AS PESSOA_PADRAO_REFERENCIA ,
    TP.ID_SITUACAO_FUNCIONAL                                                                                                                                                                                                                     AS PESSOA_SITUACAO ,
    PF.NUM_RG                                                                                                                                                                                                                                    AS PESSOA_RG,
    PF.ORG_EXPEDIDOR                                                                                                                                                                                                                             AS PESSOA_RG_ORGAO,
    PF.UF_EXPEDIDOR_RG                                                                                                                                                                                                                           AS PESSOA_RG_UF,
    PF.DT_EXPEDIDOR                                                                                                                                                                                                                              AS PESSOA_DATA_EXPEDICAO_RG,
    PF.ID_CAD_ESTADO_CIVIL                                                                                                                                                                                                                       AS PESSOA_ESTADO_CIVIL,
    RH.SG_SERVIDOR                                                                                                                                                                                                                               AS PESSOA_SIGLA,
    DECODE (RH.EMAIL_INSTITUCIONAL,NULL,DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),'',DECODE (PF.EMAIL_PESSOAL,NULL,PF.EMAIL_PESSOAL2,'',PF.EMAIL_PESSOAL2,PF.EMAIL_PESSOAL),RH.EMAIL_INSTITUCIONAL) AS PESSOA_EMAIL,
    NC.NIVEL_CURSO                                                                                                                                                                                                                               AS PESSOA_GRAU_DE_INSTRUCAO,
    NVL(TPS.NM_TP_SANGUINEO,' ')                                                                                                                                                                                                                 AS PESSOA_TP_SANGUINEO,
    NVL(PF.NATURALIDADE, ' ')                                                                                                                                                                                                                    AS PESSOA_NATURALIDADE,
    NVL(NAC.DSC_NACIONALIDADE, ' ')                                                                                                                                                                                                              AS PESSOA_NACIONALIDADE,
    'MAGISTRADO'                                                                                                                                                                                                                                 AS TIPO_RH,
    TP.ID_CARGO                                                                                                                                                                                                                                  AS CARGO_ID,
    MC.NM_CARGO                                                                                                                                                                                                                                  AS CARGO_NOME,
    MC.COD_CARGO2                                                                                                                                                                                                                                AS CARGO_SIGLA,
    MF.ID_FUNCAO                                                                                                                                                                                                                                 AS FUNCAO_ID,
    MF.DSC_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_NOME,
    MF.COD_FUNCAO                                                                                                                                                                                                                                AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE                                                                                                                                                                                                                             AS LOTACAO_ID,
    U.NM_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_NOME,
    U.SG_UNIDADE                                                                                                                                                                                                                                 AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE                                                                                                                                                                                                                         AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END               AS LOTACAO_TIPO,
    'Principal'       AS LOTACAO_TIPO_PAPEL,
    LO.ID_LOT_LOTACAO AS PAPEL_ID
  FROM SIGARH.CAD_PESSOA_FISICA PF
  INNER JOIN SIGARH.CAD_RECURSO_HUMANO RH
  ON RH.ID_CAD_PESSOA_FISICA=PF.ID_CAD_PESSOA_FISICA
  LEFT OUTER JOIN SIGARH.CAD_TP_SANGUINEO TPS
  ON TPS.ID_CAD_TP_SANGUINEO=PF.ID_CAD_TP_SANGUINEO
  INNER JOIN SIGARH.CAD_NACIONALIDADE NAC
  ON NAC.ID_CAD_NACIONALIDADE=PF.ID_CAD_NACIONALIDADE
  INNER JOIN SIGARH.CAD_TEMP_PROVIMENTO TP
  ON TP.ID_CAD_TEMP_PROVIMENTO=RH.ID_CAD_TEMP_PROVIMENTO
  INNER JOIN SIGARH.CAD_JUIZ J
  ON J.ID_CAD_JUIZ=RH.ID_CAD_RECURSO_HUMANO
  INNER JOIN SIGARH.MUMPS_CARGO MC
  ON MC.ID_CARGO=TP.ID_CARGO
  INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
  ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
  AND LO.FG_ACERTO          <> 'S'
  INNER JOIN SIGARH.LOT_LOTACAO L
  ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
  AND L.DT_FIM      IS NULL
  INNER JOIN SIGARH.LOT_UNIDADE U
  ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  INNER JOIN SIGARH.CAD_NIVEL_CURSO NC
  ON NC.ID_CAD_NIVEL_CURSO=RH.GRAU_INSTRUCAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO_SERVIDOR CPS
  ON CPS.ID_CAD_SERVIDOR   =RH.ID_CAD_RECURSO_HUMANO
  AND CPS.DT_FIM_VIGENCIA IS NULL
  AND CPS.DT_EXCLUSAO IS NULL
  LEFT OUTER JOIN SIGARH.CAD_CLASSE_PADRAO CP
  ON CP.ID_CAD_CLASSE_PADRAO=CPS.ID_CAD_CLASSE_PADRAO
  LEFT OUTER JOIN SIGARH.CAD_CLASSE C
  ON C.ID_CAD_CLASSE=CP.ID_CAD_CLASSE
  LEFT OUTER JOIN SIGARH.CAD_PADRAO P
  ON P.ID_CAD_PADRAO=CP.ID_CAD_PADRAO
  LEFT OUTER JOIN SIGARH.MUMPS_FUNCAO_SHF MF
  ON MF.ID_FUNCAO=TP.ID_FUNCAO
  INNER JOIN
    (SELECT RH.ID_CAD_RECURSO_HUMANO AS ID,
      MIN(L.DT_INICIO)               AS DT_INI
    FROM SIGARH.CAD_RECURSO_HUMANO RH
    INNER JOIN SIGARH.CAD_JUIZ J
    ON J.ID_CAD_JUIZ=RH.ID_CAD_RECURSO_HUMANO
    INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
    ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
    AND LO.FG_ACERTO          <> 'S'
    AND LO.FG_TITULAR         <>'S'
    INNER JOIN SIGARH.LOT_LOTACAO L
    ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
    AND L.DT_FIM      IS NULL
    INNER JOIN SIGARH.LOT_UNIDADE U
    ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
    INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
    ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
    INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
    ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
    INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
    ON HH.DT_FIM              IS NULL
    AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
    INNER JOIN SIGARH.LOT_UNIDADE U_PAI
    ON U_PAI.ID_LOT_UNIDADE             =HH.LOT_UNIDADE_SUPERIOR
    WHERE RH.ID_CAD_RECURSO_HUMANO NOT IN
      (SELECT RH.ID_CAD_RECURSO_HUMANO
      FROM SIGARH.CAD_RECURSO_HUMANO RH
      INNER JOIN SIGARH.CAD_JUIZ J
      ON J.ID_CAD_JUIZ=RH.ID_CAD_RECURSO_HUMANO
      INNER JOIN SIGARH.LOT_LOTACAO_OFICIAL LO
      ON LO.ID_CAD_RECURSO_HUMANO=RH.ID_CAD_RECURSO_HUMANO
      AND LO.FG_ACERTO          <> 'S'
      AND LO.FG_TITULAR          ='S'
      INNER JOIN SIGARH.LOT_LOTACAO L
      ON L.ID_LOT_LOTACAO=LO.ID_LOT_LOTACAO
      AND L.DT_FIM      IS NULL
      INNER JOIN SIGARH.LOT_UNIDADE U
      ON U.ID_LOT_UNIDADE =LO.ID_LOT_ORGANIZACIONAL
      INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
      ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
      INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
      ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
      INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
      ON HH.DT_FIM              IS NULL
      AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
      INNER JOIN SIGARH.LOT_UNIDADE U_PAI
      ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
      )
    GROUP BY RH.ID_CAD_RECURSO_HUMANO
    ) FILTRO ON FILTRO.ID =RH.ID_CAD_RECURSO_HUMANO
  AND FILTRO.DT_INI       =L.DT_INICIO
  WHERE RH.ID_CAD_ORGAO   = 1
  AND U.ID_CAD_ORGAO      = 1
  UNION
  --UNIDADES OFICIAIS ATIVAS
  SELECT NULL            AS PESSOA_ID,
    NULL                 AS PESSOA_CPF,
    NULL                 AS PESSOA_NOME,
    NULL                 AS PESSOA_SEXO,
    NULL                 AS PESSOA_DATA_NASCIMENTO,
    NULL                 AS PESSOA_RUA,
    NULL                 AS PESSOA_BAIRRO,
    NULL                 AS PESSOA_CIDADE,
    NULL                 AS PESSOA_UF,
    NULL                 AS PESSOA_CEP,
    NULL                 AS PESSOA_MATRICULA,
    NULL                 AS PESSOA_DATA_INICIO_EXERCICIO,
    NULL                 AS PESSOA_ATO_NOMEACAO,
    NULL                 AS PESSOA_DATA_NOMEACAO,
    NULL                 AS PESSOA_DT_PUBL_NOMEACAO,
    NULL                 AS PESSOA_DATA_POSSE,
    NULL                 AS PESSOA_PADRAO_REFERENCIA ,
    NULL                 AS PESSOA_SITUACAO ,
    NULL                 AS PESSOA_RG,
    NULL                 AS PESSOA_RG_ORGAO,
    NULL                 AS PESSOA_RG_UF,
    NULL                 AS PESSOA_DATA_EXPEDICAO_RG,
    NULL                 AS PESSOA_ESTADO_CIVIL,
    NULL                 AS PESSOA_SIGLA,
    NULL                 AS PESSOA_EMAIL,
    NULL                 AS PESSOA_GRAU_DE_INSTRUCAO,
    NULL                 AS PESSOA_TP_SANGUINEO,
    NULL                 AS PESSOA_NATURALIDADE,
    NULL                 AS PESSOA_NACIONALIDADE,
    NULL                 AS TIPO_RH,
    NULL                 AS CARGO_ID,
    NULL                 AS CARGO_NOME,
    NULL                 AS FUNCAO_ID,
    NULL                 AS CARGO_SIGLA,
    NULL                 AS FUNCAO_NOME,
    NULL                 AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE     AS LOTACAO_ID,
    U.NM_UNIDADE         AS LOTACAO_NOME,
    U.SG_UNIDADE         AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE AS LOTACAO_ID_PAI,
    CASE TC.NM_TIPO_CATEGORIA
      WHEN 'TURMA'
      THEN 'Unidade Judicial'
      WHEN 'VARA'
      THEN 'Unidade Judicial'
      WHEN 'JUIZADO ESPECIAL'
      THEN 'Unidade Judicial'
      ELSE 'Unidade da Administração'
    END         AS LOTACAO_TIPO,
    'Principal' AS LOTACAO_TIPO_PAPEL,
    NULL        AS PAPEL_ID
  FROM SIGARH.LOT_UNIDADE U
  INNER JOIN SIGARH.LOT_ORGANIZACIONAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA TC
  ON TC.ID_LOT_TIPO_CATEGORIA=O.ID_LOT_TIPO_CATEGORIA
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  WHERE U.ID_CAD_ORGAO    = 1
  UNION
    --UNIDADES DO TIPO AGRUPAMENTO OFICIAL
  SELECT NULL            AS PESSOA_ID,
    NULL                 AS PESSOA_CPF,
    NULL                 AS PESSOA_NOME,
    NULL                 AS PESSOA_SEXO,
    NULL                 AS PESSOA_DATA_NASCIMENTO,
    NULL                 AS PESSOA_RUA,
    NULL                 AS PESSOA_BAIRRO,
    NULL                 AS PESSOA_CIDADE,
    NULL                 AS PESSOA_UF,
    NULL                 AS PESSOA_CEP,
    NULL                 AS PESSOA_MATRICULA,
    NULL                 AS PESSOA_DATA_INICIO_EXERCICIO,
    NULL                 AS PESSOA_ATO_NOMEACAO,
    NULL                 AS PESSOA_DATA_NOMEACAO,
    NULL                 AS PESSOA_DT_PUBL_NOMEACAO,
    NULL                 AS PESSOA_DATA_POSSE,
    NULL                 AS PESSOA_PADRAO_REFERENCIA ,
    NULL                 AS PESSOA_SITUACAO ,
    NULL                 AS PESSOA_RG,
    NULL                 AS PESSOA_RG_ORGAO,
    NULL                 AS PESSOA_RG_UF,
    NULL                 AS PESSOA_DATA_EXPEDICAO_RG,
    NULL                 AS PESSOA_ESTADO_CIVIL,
    NULL                 AS PESSOA_SIGLA,
    NULL                 AS PESSOA_EMAIL,
    NULL                 AS PESSOA_GRAU_DE_INSTRUCAO,
    NULL                 AS PESSOA_TP_SANGUINEO,
    NULL                 AS PESSOA_NATURALIDADE,
    NULL                 AS PESSOA_NACIONALIDADE,
    NULL                 AS TIPO_RH,
    NULL                 AS CARGO_ID,
    NULL                 AS CARGO_NOME,
    NULL                 AS FUNCAO_ID,
    NULL                 AS CARGO_SIGLA,
    NULL                 AS FUNCAO_NOME,
    NULL                 AS FUNCAO_SIGLA,
    U.ID_LOT_UNIDADE     AS LOTACAO_ID,
    U.NM_UNIDADE         AS LOTACAO_NOME,
    U.SG_UNIDADE         AS LOTACAO_SIGLA,
    U_PAI.ID_LOT_UNIDADE AS LOTACAO_ID_PAI,
    'Agrupamento Formal' AS LOTACAO_TIPO,
    'Funcional' AS LOTACAO_TIPO_PAPEL,
    NULL        AS PAPEL_ID
  FROM SIGARH.LOT_UNIDADE U
  INNER JOIN SIGARH.LOT_EQUIP_TRAB_OFICIAL O
  ON O.ID_LOT_UNIDADE=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_TIPO_CATEGORIA_OFICIAL TC
  ON TC.ID_LOT_TIPO_CATEGORIA_OFICIAL=O.ID_LOT_TIPO_CATEGORIA_OFICIAL
  INNER JOIN SIGARH.LOT_HIST_HIERARQUIA HH
  ON HH.DT_FIM              IS NULL
  AND HH.LOT_UNIDADE_INFERIOR=U.ID_LOT_UNIDADE
  INNER JOIN SIGARH.LOT_UNIDADE U_PAI
  ON U_PAI.ID_LOT_UNIDADE =HH.LOT_UNIDADE_SUPERIOR
  WHERE U.ID_CAD_ORGAO    = 1
  ORDER BY PESSOA_MATRICULA;
-- new object type path: SCHEMA_EXPORT/VIEW/GRANT/OWNER_GRANT/OBJECT_GRANT
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGASR_CON";
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGARH_SELECT";
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGATP_CON";
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGASR";
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGASR_SELECT";
GRANT SELECT ON "SIGARH"."DADOS_RH" TO "SIGASR_READ_CON" WITH GRANT OPTION;
