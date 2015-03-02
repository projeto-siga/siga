package br.gov.jfrj.siga.dp.dao;

import java.io.UnsupportedEncodingException;

import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class CpDaoUtil {

	public static java.sql.Blob createBlob(String conteudo) {
		try {
			return HibernateUtil.getSessao().getLobHelper().createBlob(conteudo.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}
	}

}
