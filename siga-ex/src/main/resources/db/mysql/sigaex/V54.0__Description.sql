-- -----------------------------------------
--	SCRIPT:Cria Estrutura para Controle de Numeração de Documentos
-- -----------------------------------------
-- ----------------------------------------------------
--  DDL for Table EX_DOCUMENTO_NUMERACAO
-- ------------------------------------------------------

  CREATE TABLE siga.ex_documento_numeracao
   (	ID_DOCUMENTO_NUMERACAO INT UNSIGNED NOT NULL AUTO_INCREMENT, 
		ID_ORGAO_USU INT UNSIGNED NOT NULL, 
		ID_FORMA_DOC INT UNSIGNED NOT NULL, 
		ANO_EMISSAO INT UNSIGNED NOT NULL, 
		NR_DOCUMENTO INT UNSIGNED NOT NULL, 
		NR_INICIAL INT UNSIGNED NOT NULL DEFAULT 1, 
		NR_FINAL INT UNSIGNED, 
		FL_ATIVO tinyint(4) DEFAULT 1,
        CONSTRAINT EX_DOCUMENTO_NUMERACAO_PK PRIMARY KEY (ID_DOCUMENTO_NUMERACAO),
        UNIQUE INDEX EX_DOCUMENTO_NUMERACAO_PK (ID_DOCUMENTO_NUMERACAO),
        UNIQUE INDEX EX_DOCUMENTO_NUMERACAO_CHAVE (ID_ORGAO_USU, ID_FORMA_DOC, ANO_EMISSAO),
        CONSTRAINT EX_DOCUMENTO_NUMERACAO_FORMA FOREIGN KEY (ID_FORMA_DOC) REFERENCES siga.ex_forma_documento (ID_FORMA_DOC),
        CONSTRAINT EX_DOCUMENTO_NUMERACAO_ORGAO FOREIGN KEY (ID_ORGAO_USU) REFERENCES corporativo.cp_orgao_usuario (ID_ORGAO_USU)
   ) ;
   
-- ------------------------------------------------------
--  CARGA INICIAL PARA ESTRUTURA DE NUMERACAO
-- ------------------------------------------------------	  
	TRUNCATE TABLE siga.ex_documento_numeracao;
	
	INSERT INTO siga.ex_documento_numeracao (id_orgao_usu, id_forma_doc, ano_emissao, nr_documento, nr_inicial)
	SELECT org.id_orgao_usu ,
	       frm.id_forma_doc ,
	       doc.ano_emissao ,
	       Max(doc.num_expediente),
	       Max(doc.num_expediente)
	FROM siga.ex_documento doc
	INNER JOIN siga.ex_forma_documento frm ON doc.id_forma_doc = frm.id_forma_doc
	INNER JOIN corporativo.cp_orgao_usuario org ON doc.id_orgao_usu = org.id_orgao_usu
	WHERE doc.num_expediente IS NOT NULL
	GROUP BY org.id_orgao_usu,
	         frm.id_forma_doc,
	         doc.ano_emissao;
