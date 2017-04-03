package br.gov.jfrj.siga.ex.util.BIE;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ConversorDeExDocParaMateria {

	private enum ParteDaMateria {
		NUMERO, CORPO, ABERTURA, FECHO, ASSINATURA, TIPO_MATERIA, LOCALIDADE
	}
	
	private static String SIGLA_FORMA_DOC_ANEXO = "ANE";
	
	public List<Materia> converter(List<ExDocumento> docs){
		List<Materia> materias = new ArrayList<Materia>();
		for (ExDocumento doc : docs){
			materias.add(converter(doc));
			for (ExDocumento filho : doc.getExDocumentoFilhoSet())
				if (!filho.isPendenteDeAssinatura() && filho.getExFormaDocumento().getSiglaFormaDoc().equals(SIGLA_FORMA_DOC_ANEXO))
					materias.add(converter(filho));
		}
		return materias;
	}

	public Materia converter(ExDocumento doc) {

		Materia m = new Materia();
				
		m.setNumero(getNumero(doc) + " de " + doc.getDtD()
				+ " de " + doc.getDtMMMM() + " de " + doc.getDtYYYY());
		
		m.setAbertura(getAberturaEnxugada(doc));
		
		m.setConteudo(getCorpoEnxugado(doc));
		
		m.setFecho(getFechoEnxugadoComAssinatura(doc));
		
		m.setDt(doc.getDtDoc());
		
		m.setLocalidade(getLocalidade(doc));
		
		if (m.getLocalidade() == null || m.getLocalidade().equals(""))
			m.setLocalidade(doc.getLocalidadeString());
		
		//Edson: como o código é usado para ordenação e o tipo de matéria para
		//agrupamento, define que o código e o tipo de matéria do doc filho são 
		//os do pai, de modo que os dois permaneçam na mesma categoria e em sequência
		
		m.setCodigo(doc.getPai() != null ? doc.getPai().getCodigo()+"#" : doc.getCodigo());
		
		ExDocumento docBase = doc.getPai() != null ? doc.getPai() : doc;
		m.setTipoMateria(getTipoMateria(docBase));
		if (m.getTipoMateria() == null || m.getTipoMateria().trim().equals(""))
			m.setTipoMateria(docBase.getExFormaDocumento().getSiglaFormaDoc() 
					+ "|"+ docBase.getExFormaDocumento().getDescrFormaDoc());
		
		return m;
	}

	private String enxugaHtml(String html) {
		int aPartirDe = 0;
		int posIni = html.indexOf("<p");
		int posFim = html.indexOf(">", posIni);

		while (posIni != -1) {
			String sub = html.substring(posIni, posFim);
			if (sub.indexOf("style=\"") != -1)
				sub = sub.replace("style=\"",
						"style=\"margin-bottom:7px; margin-top:7px;");
			else
				sub = sub + " style=\"margin-bottom:7px; margin-top:7px;\" ";

			String[] atributos = sub.split(";");

			for (String atributo : atributos) {
				if (atributo.contains("text-indent:")) {
					sub = sub.replace(atributo + ";", "");
				}
				if (atributo.contains("text-align:left")) {
					sub = sub.replace(atributo, "text-align:justify");
				}
			}

			html = html.substring(0, posIni) + sub
					+ html.substring(posFim, html.length());
			aPartirDe = posFim + 1;
			posIni = html.indexOf("<p", aPartirDe);
			posFim = html.indexOf(">", posIni);
		}
		html = html.replace("&nbsp;", " ");
		return html;

	}

	private String getTrecho(ExDocumento doc, ParteDaMateria parte) {
		String regex = ".*<!--\\sINICIO\\s" + parte + "\\s(?:-->)*"
				+ "(?<conteudo>.*?)" 
				+ "(?:<!--)*\\sFIM\\s" + parte + "\\s-->.*";
		Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(
				doc.getConteudoBlobHtmlString());
		if (m.matches())
			return m.group("conteudo");
		return "";
	}

	private String getCorpo(ExDocumento doc) {
		return getTrecho(doc, ParteDaMateria.CORPO);
	}

	private String getAssinatura(ExDocumento doc) {
		return getTrecho(doc, ParteDaMateria.ASSINATURA);
	}

	private String getNumero(ExDocumento doc) {
		return getTrecho(doc, ParteDaMateria.NUMERO);
	}

	private String getAberturaEnxugada(ExDocumento doc) {
		return enxugaHtml(getTrecho(doc, ParteDaMateria.ABERTURA));
	}
	
	private String getFecho(ExDocumento doc) {
		return getTrecho(doc, ParteDaMateria.FECHO);
	}

	private String getCorpoEnxugado(ExDocumento doc) {
		return enxugaHtml(getCorpo(doc));
	}
	
	private String getTipoMateria(ExDocumento doc){
		return getTrecho(doc, ParteDaMateria.TIPO_MATERIA);
	}
	
	private String getLocalidade(ExDocumento doc){
		return getTrecho(doc, ParteDaMateria.LOCALIDADE);
	}

	private String getFechoEnxugadoComAssinatura(ExDocumento doc) {
		return enxugaHtml(getFecho(doc))
				+ enxugaHtml(getAssinatura(doc));
	}

}
