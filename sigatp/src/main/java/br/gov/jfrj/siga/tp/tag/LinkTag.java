package br.gov.jfrj.siga.tp.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

public class LinkTag extends TagSupport {

	private static final long serialVersionUID = -333366995935848194L;
	private static final String IMG_LINKNOVAJANELAICON = "/sigatp/public/images/linknovajanelaicon.png";

	private String texto;
	private String parteTextoLink;
	private String comando;
	private String comandoEditavel = "";
	private boolean ehEditavel;

	@Override
	public int doStartTag() throws JspException {
		try {
			validarAtributos();
			gerarConteudoTag();
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	private void gerarConteudoTag() throws IOException {
		JspWriter out = pageContext.getOut();

		if (ehEditavel)
			comandoEditavel = comando.replace("true", "false");

		if (!comandoEditavel.isEmpty())
			out.append("<a href='" + comandoEditavel + "'>" + parteTextoLink + "</a>");
		else
			out.append(parteTextoLink);

		out.append(" <a href=\"#\" onclick=\"javascript:window.open('" + comando + "');\">");
		out.append("<img src=\"" + IMG_LINKNOVAJANELAICON + "\" alt=\"Abrir em uma nova janela\" title=\"Abrir em uma nova janela\"></a> ");
		out.append(texto.replace(parteTextoLink, ""));

		out.println();
		out.flush();
	}

	private void validarAtributos() throws JspException {
		if((null == comando || "".equals(comando)) || (null == parteTextoLink || "".equals(parteTextoLink)))
			throw new JspException(MessagesBundle.getMessage("tpTags.parametrosInvalidos.exception"));
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setParteTextoLink(String parteTextoLink) {
		this.parteTextoLink = parteTextoLink;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public void setComandoEditavel(String comandoEditavel) {
		this.comandoEditavel = comandoEditavel;
	}

	public void setEhEditavel(Boolean ehEditavel) {
		this.ehEditavel = ehEditavel;
	}
}
