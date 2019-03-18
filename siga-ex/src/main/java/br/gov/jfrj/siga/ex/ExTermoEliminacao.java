package br.gov.jfrj.siga.ex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExTermoEliminacao {

	private ExDao dao() {
		return ExDao.getInstance();
	}

	private ExDocumento doc;

	private ExEditalEliminacao edital;

	public ExDocumento getDoc() {
		return this.doc;
	}

	public void setDoc(ExDocumento termo) {
		this.doc = termo;
	}

	public ExEditalEliminacao getEdital() {
		return this.edital;
	}

	public ExTermoEliminacao() {

	}

	public ExTermoEliminacao(ExDocumento termo) {
		setDoc(termo);
		try {
			Long idEdital = Long.valueOf(termo.getForm().get(
					"edital_expedienteSel.id"));
			ExMobil mobEdital = dao().consultar(idEdital, ExMobil.class, false);
			this.edital = new ExEditalEliminacao(mobEdital.getExDocumento());
		} catch (Exception e) {
			this.edital = new ExEditalEliminacao();
		}

	}

	public String getPaginaPublicacaoEdital() {
		try {
			ExMovimentacao dispon = getEdital()
					.getDoc()
					.getMobilGeral()
					.getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO);
			;
			return dispon.getPagPublicacao();
		} catch (Exception e) {
			return "";
		}
	}

	public String getDataPublicacaoEdital() {
		try {
			ExMovimentacao dispon = getEdital()
					.getDoc()
					.getMobilGeral()
					.getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO);
			Date dt = dispon.getDtIniMov();
			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(dt);
			return new SimpleDateFormat("dd/MM/yyyy").format(DJE
					.getDataPublicacao());
		} catch (Exception e) {
			return "";
		}
	}

	public String getDataPublicacaoEditalExtenso() {
		try {
			final SimpleDateFormat dfEntrada = new SimpleDateFormat(
					"dd/MM/yyyy");
			final SimpleDateFormat dfSaida = new SimpleDateFormat(
					"dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
			Date dt = dfEntrada
					.parse(getDoc().getForm().get("data_publicacao"));
			return dfSaida.format(dt).toLowerCase();
		} catch (Exception e) {
			return "";
		}

	}

	public String getCodigoEdital() {
		try {
			return getEdital().getDoc().getCodigo();
		} catch (Exception e) {
			return "";
		}
	}

	public void gravar() throws AplicacaoException {
		ExDocumento editalIncluso = getEdital().getDoc();
		//Edson: comentando pra facilitar homologacao
		//if (!editalIncluso.isDJEPublicado())
			//throw new AplicacaoException(
			//		"O edital não pode ser selecionado pois não está publicado.");
	}

	public void eliminarInclusos() {
		ExMovimentacao assinatura = getDoc()
				.getMobilGeral()
				.getUltimaMovimentacaoNaoCancelada(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO);
		if (assinatura == null)
			assinatura = getDoc()
					.getMobilGeral()
					.getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO);

		for (ExItemDestinacao o : getEdital().getEfetivamenteInclusosDoPeriodo()) {
			for (ExMobil mobAEliminar : o.getMob()
					.getArvoreMobilesParaAnaliseDestinacao())
				if (!mobAEliminar.isEliminado())
					Ex.getInstance()
							.getBL()
							.eliminar(assinatura.getCadastrante(),
									assinatura.getLotaCadastrante(),
									mobAEliminar, null,
									getDoc().getSubscritor(),
									getDoc().getMobilGeral());
		}
	}

}
