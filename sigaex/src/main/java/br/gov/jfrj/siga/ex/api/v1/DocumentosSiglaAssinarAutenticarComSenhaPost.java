package br.gov.jfrj.siga.ex.api.v1;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

abstract class DocumentosSiglaAssinarAutenticarComSenhaPost {

	private final boolean autenticar;

	/**
	 * 
	 * @param autenticar Indica se o documento será autnticado (<code>true</code>)
	 *                   ou assinado (<code>false</code>).
	 */
	protected DocumentosSiglaAssinarAutenticarComSenhaPost(boolean autenticar) {
		this.autenticar = autenticar;
	}

	/**
	 * Valida o documento.
	 * 
	 * @param titular     Usuário logado no webservice.
	 * @param lotaTitular Lotação do titular.
	 * @param mob         {@link ExMobil} representando o documento
	 * 
	 * @throws Exception Se o documento não estiver válido.
	 */
	protected abstract void assertDocumento(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob)
			throws Exception;

	/**
	 * Executa a Autenticação ou Assinatura do documento.
	 * 
	 * @param sigla               Sigla (temporária) do Documento a ser assinado.
	 * @param preenchedorResposta Vai preencher a resposta com a
	 *                            {@link ExDocumento#getSigla() Sigla do Documento}
	 *                            assinada e com o retorno da Assinatura (ou
	 *                            <code>OK</code> se esse retorno estiver vazio).
	 * @param ctx2
	 * @throws Exception Se houver algo de errado.
	 */
	protected void executar(String sigla, BiConsumer<String, String> preenchedorResposta, ExApiV1Context ctx)
			throws Exception {
		// Necessário pois é chamado o método "realPath" durante a criação do
		// PDF.
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = cadastrante;
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = getMob(sigla);

		assertAcesso(titular, lotaTitular, mob);
		assertDocumento(titular, lotaTitular, mob);

		String retornoAssinatura = Ex.getInstance().getBL().assinarDocumentoComSenha(cadastrante, lotaTitular,
				mob.doc(), null, cadastrante.getSiglaCompleta(), null, false, false, titular, this.autenticar, null,
				false, false);

		preenchedorResposta.accept(mob.doc().getCodigo(), Objects.toString(retornoAssinatura, "OK"));
	}

	private ExMobil getMob(String sigla) {
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		flt.setSigla(sigla);

		return ExDao.getInstance().consultarPorSigla(flt);
	}

	private void assertAcesso(DpPessoa titular, DpLotacao lotaTitular, final ExMobil mob) throws Exception {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob)) {
			String s = "";
			s += mob.doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " " + mob.doc().getExNivelAcessoAtual().getNmNivelAcesso() + " " + s;

			Map<ExPapel, List<Object>> mapa = mob.doc().getPerfis();
			boolean isInteressado = false;

			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();

				if ((exPapel != null) && (exPapel.getIdPapel().equals(ExPapel.PAPEL_INTERESSADO))) {
					while (it.hasNext() && !isInteressado) {
						Object item = it.next();
						isInteressado = item.toString().equals(titular.getSigla()) ? true : false;
					}
				}
			}

			if (mob.doc().isSemEfeito()) {
				if (!mob.doc().getCadastrante().equals(titular) && !mob.doc().getSubscritor().equals(titular)
						&& !isInteressado) {
					throw new AplicacaoException("Documento " + mob.getSigla() + " cancelado ");
				}
			} else {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + "." + s);
			}
		}
	}

}
