package br.gov.jfrj.siga.ex.api.v1;

import static java.util.Objects.isNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class ExApiV1Context extends ApiContextSupport {
	private static final String DOC_MÓDULO_DE_DOCUMENTOS = "DOC:Módulo de Documentos;";

	public void atualizarCacheDeConfiguracoes() throws Exception {
		Ex.getInstance().getConf().limparCacheSeNecessario();
	}

	public CpDao inicializarDao() {
		return ExDao.getInstance();
	}

	public EntityManager criarEntityManager() {
		return ExStarter.emf.createEntityManager();
	}

	/**
	 * Verifica se o usuário tem acesso ao serviço
	 * <code>{@value #DOC_MÓDULO_DE_DOCUMENTOS}<code> e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public void assertAcesso(String acesso) throws Exception {
		getSigaObjects().assertAcesso(DOC_MÓDULO_DE_DOCUMENTOS + acesso);
	}

	static void assertAcesso(final ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob)) {
			String s = "";
			s += mob.doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " " + mob.doc().getExNivelAcessoAtual().getNmNivelAcesso() + " " + s;

			Map<ExPapel, List<Object>> mapa = mob.doc().getPerfis();
			boolean isInteressado = false;

			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();

				if ((exPapel != null) && (exPapel.getIdPapel() == ExPapel.PAPEL_INTERESSADO)) {
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

	public static ExMovimentacao getMov(ExMobil mob, String id) {
		Long movId = Long.parseLong(id);
		ExMovimentacao mov = ExDao.getInstance().consultar(movId, ExMovimentacao.class, false);
		if (!mov.getExMobil().equals(mob))
			throw new AplicacaoException("Movimentação não se refere ao mobil informado");
		return mov;
	}

	/**
	 * Retorna um {@link ExMobil Mobil} relacionado a uma certa
	 * {@link ExMobil#getSigla() sigla} contanto que esse exista e que o usuário
	 * tenha autorização para acessá-lo.
	 * 
	 * @param sigla              Sigla do documeto solicitado
	 * @param so                 SigaObjects previamente carregado via
	 *                           {@link #getSigaObjects()} . Será usado na
	 *                           validação.
	 * @param req                Requisição do Swagger. Usado no disparo da exceção.
	 * @param resp               Resposta do Swagger. Usado no disparo da exceção.
	 * @param descricaoDocumento Descrição do documento a ser usada em caso de erro.
	 * @return Mobil relacionado a sigla soliciatada.
	 * @throws SwaggerException Se não achar um Mobil com a sigla solicitada (
	 *                          {@link SwaggerException#getStatus() Status} 404) ou
	 *                          se o usário não tiver autorização para tratá-lo
	 *                          ({@link SwaggerException#getStatus() Status} 403)
	 */
	ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp,
			String descricaoDocumento) throws Exception {
		final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
		filter.setSigla(sigla);
		ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);

		if (isNull(mob)) {
			throw new SwaggerException("Número do " + descricaoDocumento + " não existe", 404, null, req, resp, null);
		}
		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob))
			throw new SwaggerException("Acesso ao " + descricaoDocumento + " " + mob.getSigla()
					+ " permitido somente a usuários autorizados. (" + getTitular().getSigla() + "/"
					+ getLotaTitular().getSiglaCompleta() + ")", 403, null, req, resp, null);

		return mob;
	}
	
	public ExMobil buscarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp,
            String descricaoDocumento) throws SwaggerException {
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setSigla(sigla);
        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);

 

        if (isNull(mob)) {
            throw new SwaggerException("Número do " + descricaoDocumento + " não existe", 404, null, req, resp, null);
        }
        return mob;
    }

	/**
	 * Retorna um {@link ExMobil Mobil} relacionado a uma certa
	 * {@link ExMobil#getSigla() sigla} contanto que esse exista e que o usuário
	 * tenha autorização para acessá-lo.
	 * 
	 * @param sigla Sigla do documeto solicitado
	 * @param req   Requisição do Swagger. Usado no disparo da exceção.
	 * @param resp  Resposta do Swagger. Usado no disparo da exceção.
	 * @return Mobil relacionado a sigla soliciatada.
	 * @throws SwaggerException Se não achar um Mobil com a sigla solicitada (
	 *                          {@link SwaggerException#getStatus() Status} 404) ou
	 *                          se o usário não tiver autorização para tratá-lo
	 *                          ({@link SwaggerException#getStatus() Status} 403).
	 * @see #buscarEValidarMobil(String, SigaObjects, ISwaggerRequest,
	 *      ISwaggerResponse, String)
	 */
	ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp) throws Exception {
		return buscarEValidarMobil(sigla, req, resp, "Documento");
	}

}
