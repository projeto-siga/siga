package br.gov.jfrj.siga.tp.tag;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

/**
 * Tag responsavel por gerar o link de erro na tela.
 * 
 * @author db1
 *
 */
public class ErroLinkTag extends TagSupport {

	private static final long serialVersionUID = 4803838233286747288L;
	private static final String VEICULO = "Veiculo";
	private static final String CONDUTOR = "Condutor";
	private static final String ESCALA_DE_TRABALHO = "EscalaDeTrabalho";
	private static final String IMG_LINKNOVAJANELAICON = "/sigatp/public/images/linknovajanelaicon.png";
	private static final String caminhoUrl = "/sigatp/app/missao/buscarPelaSequence/false";

	private String message;
	private String classe;
	private String comando;

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
		String[] sequenciasMissao = message.replace("?", "").split(",");
		StringBuffer saida = iniciarBufferPorClasse();
		String delimitador = "";

		for (int i = 0; i < sequenciasMissao.length; i++) {
			saida.append(delimitador);
			String urlTransformada = MessageFormat.format("{0}/{1}", caminhoUrl, sequenciasMissao[i]);
			
			saida.append(MessageFormat.format("{0} <a href=\"#\" onclick=\"javascript:window.open(''{1}'');\">", sequenciasMissao[i], urlTransformada));
			saida.append(MessageFormat.format("<img src=\"{0}\" alt=\"Abrir em uma nova janela\" title=\"Abrir em uma nova janela\"></a>", ErroLinkTag.IMG_LINKNOVAJANELAICON));
			delimitador = " , ";
		}
		saida.append(". </li>");

		out.write(saida.toString());
		out.flush();
	}

	private StringBuffer iniciarBufferPorClasse() {
		StringBuffer saida = new StringBuffer();
		if (classe.contains(VEICULO)) {
			saida.append("<li> O ve&iacute;culo est&aacute; ocupado nas miss&otilde;es: ");
		} else if (classe.contains(CONDUTOR)) {
			saida.append("<li> O condutor est&aacute; ocupado nas miss&otilde;es: ");
		} else if (classe.contains(ESCALA_DE_TRABALHO)) {
			saida.append("<li> N&atilde;o est&aacute; mais dispon&iacute;vel, para a escala abaixo, o condutor escalado nas miss&otilde;es: ");
		}
		return saida;
	}

	private void validarAtributos() throws JspException {
		if (message == null || classe == null || comando == null) {
			throw new JspException(MessagesBundle.getMessage("tpTags.parametrosInvalidos.exception"));
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}
}