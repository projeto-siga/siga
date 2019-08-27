package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.RiBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.Historico;

public class Util {

	private static String acronimoOrgao = null;
	private static final int CONTROLE_LINK_HASH_TAG = 2;
	private static final String URL_SIGA_DOC = "/sigaex/expediente/doc/exibir.action?sigla=";
	private static final String URL_SIGA_SR = "/sigasr/solicitacao/exibir?sigla=";
	private static final String URL_SIGA_GC = "/sigagc/exibir?sigla=";

	// public static void salvar(Historico o) throws Exception {
	// o.setHisDtIni(new Date());
	// o.setHisDtFim(null);
	// if (o.getId() == null) {
	// ((GenericModel) o).save();
	// o.setHisIdIni(o.getId());
	// } else {
	// JPA.em().detach(o);
	// // Edson: Abaixo, nÃ£o funcionou findById, por algum motivo
	// // relacionado a esse esquema de sobrescrever o ObjetoBase
	// Historico oAntigo = (Historico) JPA.em().find(o.getClass(),
	// o.getId());
	// ((ManipuladorHistorico) oAntigo).finalizar();
	// o.setHisIdIni(oAntigo.getHisIdIni());
	// o.setId(null);
	// }
	// ((GenericModel) o).save();
	// }

	public static void finalizar(Historico o) throws Exception {
		o.setHisDtFim(new Date());
		((GenericModel) o).save();
	}

	// public static GcConfiguracao getConfiguracao(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo) throws Exception {
	//
	// GcConfiguracao conf = new GcConfiguracao(pess, item, servico, JPA.em()
	// .find(CpTipoConfiguracao.class, idTipo), subTipo);
	//
	// return GcConfiguracaoBL.get().buscarConfiguracao(conf);
	// }
	//
	// public static List<GcConfiguracao> getConfiguracoes(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo) throws Exception {
	// return getConfiguracoes(pess, item, servico, idTipo, subTipo,
	// new int[] {});
	// }
	//
	// public static List<GcConfiguracao> getConfiguracoes(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo, int atributoDesconsideradoFiltro[])
	// throws Exception {
	// GcConfiguracao conf = new GcConfiguracao(pess, item, servico, JPA.em()
	// .find(CpTipoConfiguracao.class, idTipo), subTipo);
	// return GcConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
	// atributoDesconsideradoFiltro);
	// }

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

	// Este mÃ©todo Ã© usado por classes para as quais o mapeamento de sequence
	// nÃ£o estÃ¡ funcionando. Isso estÃ¡ ocorrendo porque, quando a opÃ§Ã£o
	// jpa.ddl
	// estÃ¡ setada como validate (em vez de create-drop, por exemplo), o
	// Hibernate tenta conferir erroneamente (JIRA HHH-2508) se uma sequence
	// mapeada estÃ¡ entre as user_sequences, ou seja, entre as sequences do
	// banco cujo owner Ã© sigasr. Como hÃ¡ sequences do usuÃ¡rio Corporativo,
	// nÃ£o
	// do sigasr, a aplicaÃ§Ã£o sigasr nÃ£o inicia por erro de validaÃ§Ã£o do
	// Hibernate. Comentei os mapeamentos de Ã­ndice por anotaÃ§Ã£o (que Ã© o
	// modo
	// de mapear usado pelo sigasr) no Corporativo, pra nÃ£o dar erro de
	// validaÃ§Ã£o. Ver soluÃ§Ã£o melhor o mais
	// rÃ¡pido possÃ­vel. Ainda, como o sigasr precisa usar sequences do
	// Corporativo (em SrMarca e GcConfiguracao) e as anotaÃ§Ãµes nÃ£o estÃ£o
	// presentes, este mÃ©todo Ã© necessÃ¡rio.
	public static Long nextVal(String sequence) {
		Long newId;
		return Long.valueOf(JPA.em()
				.createNativeQuery("select " + sequence + ".nextval from dual")
				.getSingleResult().toString());
	}

	/**
	 * Cria um link referenciando automaticamente um
	 * documento/serviço/conhecimento quando é acrescentado o seu código no
	 * campo de conteúdo da informação. Ex: Estou editando um conhecimento, no
	 * seu campo texto quero referenciar o seguinte documento
	 * JFRJ-OFI-2013/00003. Quando acrescento esse código do ofício e mando
	 * salvar as alterações do conhecimento é criado um link que leva direto ao
	 * documento referenciado.
	 * 
	 * Além disso, também identifica e cria links para hashTags. Esses hashTags
	 * são inseridos no campo de classificação do conhecimento.
	 * 
	 **/
	public static String marcarLinkNoConteudo(String conteudo) throws Exception {

		if (acronimoOrgao == null) {
			acronimoOrgao = "";
			List<String> acronimo = CpOrgaoUsuario.find(
					"select acronimoOrgaoUsu from CpOrgaoUsuario").fetch();
			for (String ao : acronimo)
				acronimoOrgao += (acronimoOrgao.isEmpty() ? "" : "|") + ao;
		}
		conteudo = findSigla(conteudo);
		return findHashTag(conteudo, null, CONTROLE_LINK_HASH_TAG);
	}

	private static String findSigla(String conteudo) throws Exception {
		String sigla = null;
		StringBuffer sb = new StringBuffer();

		// lembrar de retirar o RJ quando for para a produção.
		Pattern padraoSigla = Pattern.compile(
		// reconhece tais tipos de códigos: JFRJ-EOF-2013/01494.01,
		// JFRJ-REQ-2013/03579-A, JFRJ-EOF-2013/01486.01-V01,
		// TRF2-PRO-2013/00001-V01
				"(?i)(?:(?:RJ|"
						+ acronimoOrgao
						+ ")-([A-Za-z]{2,3})-[0-9]{4}/[0-9]{5}(?:.[0-9]{2})?(?:-V[0-9]{2})?(?:-[A-Za-z]{1})?)");

		Matcher matcherSigla = padraoSigla.matcher(conteudo);
		while (matcherSigla.find()) {
			// identifica que é um código de um conhecimento, ou serviço ou
			// documento
			if (matcherSigla.group(1) != null) {
				sigla = matcherSigla.group(0).toUpperCase().trim();
				// conhecimento
				if (matcherSigla.group(1).toUpperCase().equals("GC")) {
					matcherSigla.appendReplacement(sb, "[[" + URL_SIGA_GC
							+ URLEncoder.encode(sigla, "UTF-8") + "|" + sigla
							+ "]]");
				}
				// serviço
				else if (matcherSigla.group(1).toUpperCase().equals("SR")) {
					matcherSigla.appendReplacement(sb, "[[" + URL_SIGA_SR
							+ URLEncoder.encode(sigla, "UTF-8") + "|" + sigla
							+ "]]");
				}
				// documento
				else {
					matcherSigla.appendReplacement(sb, "[[" + URL_SIGA_DOC
							+ URLEncoder.encode(sigla, "UTF-8") + "|" + sigla
							+ "]]");
				}
			}
		}
		matcherSigla.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Método que encontra uma hashTag. Quando o parâmetro controle é igual a 1,
	 * a classificação é atualizada para poder ser gravada. Quando o controle é
	 * igual a 2, o conteudo é marcado com os links das hashTags encontradas. O
	 * conteudo não é gravado com os links.
	 */
	public static String findHashTag(String conteudo, String classificacao,
			int controle) {
		StringBuffer sb = new StringBuffer();
		String hashTag = new String();

		Pattern padraoHashTag = Pattern.compile(
		// reconhece uma hashTag (#)
				"(#[\\w-]+)");

		Matcher matcherHashTag = padraoHashTag.matcher(conteudo);
		while (matcherHashTag.find()) {
			if (controle == 1)
				hashTag += (hashTag.isEmpty() ? "" : ", ")
						+ matcherHashTag.group(0);
			else if (controle == 2) {
				matcherHashTag.appendReplacement(sb,
						"[[/sigagc/listar?filtro.pesquisa=true&filtro.tag.sigla="
								+ matcherHashTag.group(0).substring(1)
								+ "|$0]]");
			}
		}
		if (controle == 1) {
			if (classificacao != null)
				// remove todas as hashTag da classificacao, caso exista.
				// Necessário para manter a classificacao
				// atualizada. Ao final serão inseridas as hashTags que foram
				// acrescentadas/mantidas no conteudo
				classificacao = classificacao
						.replaceAll("[,\\s]*#[,\\w-]+", "").trim();
			else
				classificacao = "";
			return RiBL.atualizarClassificacao(classificacao, hashTag);
		} else if (controle == 2) {
			matcherHashTag.appendTail(sb);
			return sb.toString();
		} else
			return null;
	}
}
