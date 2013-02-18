package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;


import models.ManipuladorHistorico;
import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSubTipoConfiguracao;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Historico;

public class Util {



	public static SrConfiguracao getConfiguracao(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo) throws Exception {

		SrConfiguracao conf = new SrConfiguracao(pess, item, servico, JPA.em()
				.find(CpTipoConfiguracao.class, idTipo), subTipo);

		return SrConfiguracaoBL.get().buscarConfiguracao(conf);
	}

	public static List<SrConfiguracao> getConfiguracoes(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo) throws Exception {
		return getConfiguracoes(pess, item, servico, idTipo, subTipo,
				new int[] {});
	}

	public static List<SrConfiguracao> getConfiguracoes(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo, int atributoDesconsideradoFiltro[])
			throws Exception {
		SrConfiguracao conf = new SrConfiguracao(pess, item, servico, JPA.em()
				.find(CpTipoConfiguracao.class, idTipo), subTipo);
		return SrConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
				atributoDesconsideradoFiltro);
	}

	public static void copiar(Object dest, Object orig) {
		for (Method getter : orig.getClass().getDeclaredMethods()) {
			try {
				String getterName = getter.getName();
				if (!getterName.startsWith("get"))
					continue;
				if (Collection.class.isAssignableFrom(getter.getReturnType()))
					continue;
				String setterName = getterName.replace("get", "set");
				Object origValue = getter.invoke(orig);
				dest.getClass().getMethod(setterName, getter.getReturnType())
						.invoke(dest, origValue);
			} catch (NoSuchMethodException nSME) {
				int a = 0;
			} catch (IllegalAccessException iae) {
				int a = 0;
			} catch (IllegalArgumentException iae) {
				int a = 0;
			} catch (InvocationTargetException nfe) {
				int a = 0;
			}

		}
	}

	// Este método é usado por classes para as quais o mapeamento de sequence
	// não está funcionando. Isso está ocorrendo porque, quando a opção jpa.ddl
	// está setada como validate (em vez de create-drop, por exemplo), o
	// Hibernate tenta conferir erroneamente (JIRA HHH-2508) se uma sequence
	// mapeada está entre as user_sequences, ou seja, entre as sequences do
	// banco cujo owner é sigasr. Como há sequences do usuário Corporativo, não
	// do sigasr, a aplicação sigasr não inicia por erro de validação do
	// Hibernate. Comentei os mapeamentos de índice por anotação (que é o modo
	// de mapear usado pelo sigasr) no Corporativo, pra não dar erro de
	// validação. Ver solução melhor o mais
	// rápido possível. Ainda, como o sigasr precisa usar sequences do
	// Corporativo (em SrMarca e SrConfiguracao) e as anotações não estão
	// presentes, este método é necessário.
	public static Long nextVal(String sequence) {
		Long newId;
		return Long.valueOf(JPA.em()
				.createNativeQuery("select " + sequence + ".nextval from dual")
				.getSingleResult().toString());
	}

}
