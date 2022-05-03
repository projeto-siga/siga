-- jira processo_rio-2063
-- Método de geração da numeração de documentos.
-- connect siga@srv000767.infra.rio.gov.br:1521/sigadoc.pcrj;

-- 
--------------------------------------------------------
--  CARGA INICIAL PARA ESTRUTURA DE NUMERACAO
--------------------------------------------------------	  
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
	COMMIT;  
--------------------------------------------------------
--  FIM
--------------------------------------------------------	
