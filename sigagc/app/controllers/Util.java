package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.*;

//import javax.swing.event.ListSelectionEvent;
//import javax.persistence.Query;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import models.GcInformacao;
import models.SrSolicitacao;

public class Util {
	
	static String acronimoOrgao = "";

//	public static void salvar(Historico o) throws Exception {
//		o.setHisDtIni(new Date());
//		o.setHisDtFim(null);
//		if (o.getId() == null) {
//			((GenericModel) o).save();
//			o.setHisIdIni(o.getId());
//		} else {
//			JPA.em().detach(o);
//			// Edson: Abaixo, nÃ£o funcionou findById, por algum motivo
//			// relacionado a esse esquema de sobrescrever o ObjetoBase
//			Historico oAntigo = (Historico) JPA.em().find(o.getClass(),
//					o.getId());
//			((ManipuladorHistorico) oAntigo).finalizar();
//			o.setHisIdIni(oAntigo.getHisIdIni());
//			o.setId(null);
//		}
//		((GenericModel) o).save();
//	}

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
	 * Cria um link referenciando automaticamente um documento/serviço/conhecimento  
	 * quando é acrescentado o seu código no campo de conteúdo da informação.
	 * Ex: Estou editando um conhecimento, no seu campo texto quero referenciar o seguinte documento
	 * JFRJ-OFI-2013/00003. Quando acrescento esse código do ofício e mando salvar as alterações do
	 * conhecimento é criado um link que leva direto ao documento referenciado.
	 **/
	public static String marcarReferencia(String conteudo){

		StringBuffer sb = new StringBuffer();
			
		if(acronimoOrgao.isEmpty()) {
			List<String> acronimo = CpOrgaoUsuario.find("select acronimoOrgaoUsu from CpOrgaoUsuario").fetch();
			for(String ao : acronimo) 
				acronimoOrgao += (acronimoOrgao == "" ? "" : "|") + ao;
		}
 		
		//Pattern padrao = Pattern.compile("(\\[\\[http.*\\]\\])|(?i)((" + acronimoOrgao + ")-([A-Za-z]{2,3})-[0-9]{4}/([0-9]{5}))");
		Pattern padrao = Pattern.compile("(\\[\\[http.*\\]\\])|(?i)((RJ)-([A-Za-z]{2,3})-[0-9]{4}/([0-9]{5})");
		Matcher combinador = padrao.matcher(conteudo);
		
		while(combinador.find()){
			if(combinador.group(1) == null) {
				if(combinador.group(4).toUpperCase().equals("GC")){
					GcInformacao conhecimento = GcInformacao.find("byNumero",Integer.parseInt(combinador.group(5))).first();
					combinador.appendReplacement(sb, "[[http://localhost/sigagc/exibir?id=" + conhecimento.id + "|" + combinador.group(2).toUpperCase().trim() + " (" + conhecimento.arq.titulo + ")]]");	
				}
				else if(combinador.group(4).toUpperCase().equals("SR")) {
					SrSolicitacao solicitacao = SrSolicitacao.find("byNumSolicitacao",Long.parseLong(combinador.group(5))).first();
					combinador.appendReplacement(sb, "[[http://localhost/sigasr/solicitacao/exibir/" + solicitacao.idSolicitacao + "|" + combinador.group(2).toUpperCase().trim() + " (" + solicitacao.itemConfiguracao.getDescricao() + "-" + solicitacao.servico.getDescricao() + ")]]");
				}
				else {
					combinador.appendReplacement(sb,"[[http://localhost/sigaex/expediente/doc/exibir.action?sigla=" + combinador.group(2).toUpperCase().trim() + "|" + combinador.group(2).toUpperCase().trim() + "]]");
				}
			}
		}
		combinador.appendTail(sb);
		
		return sb.toString();
	}

	

}
