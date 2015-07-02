package br.gov.jfrj.siga.ex.ext;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.FOP;
import br.gov.jfrj.itextpdf.Nheengatu;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;

public class ConversorHTMLFactory extends AbstractConversorHTMLFactory {

	public static final int EXT_CONVERSOR = 2;

	/**
	 * Retorna um conversor. Se tiver a configuração permitir o uso de extensão
	 * de conversor html, retorna o PD4ML.
	 */
	@Override
	public ConversorHtml getConversor(final ExConfiguracaoBL conf,
			final ExDocumento doc, final String strHtml) throws Exception {
		ConversorHtml conversor;
		if (conf.podePorConfiguracao(doc.getCadastrante(),
				doc.getLotaCadastrante(), doc.getExModelo(),
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_EXTENSAO_CONVERSOR_HTML))
			try {
			conversor = getExtensaoConversorHTML();
			} catch (Exception e) {
				conversor = getConversorPadrao();
			}
		else if (strHtml.contains("<!-- FOP -->"))
			conversor = new FOP();
		else
			conversor = getConversorPadrao();
		return conversor;
	}

	@Override
	public ConversorHtml getConversor(final int conversor) throws Exception {
		if (conversor == EXT_CONVERSOR) {
			return getExtensaoConversorHTML();
		}

		if (conversor == CONVERSOR_FOP) {
			return new FOP();
		}

		if (conversor == CONVERSOR_NHEENGATU) {
			return new Nheengatu();
		}

		return null;

	}

	@Override
	public ConversorHtml getConversorPadrao() throws Exception {
		return new Nheengatu();
	}

	@Override
	public ConversorHtml getExtensaoConversorHTML() throws AplicacaoException {
		try {
			return (ConversorHtml) Class.forName(
					SigaExProperties.getExtensaoConversorHTML()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AplicacaoException("Não foi possível carregar a extensão do conversor HTML para PDF: " + e.getMessage());
		}
	}
}
