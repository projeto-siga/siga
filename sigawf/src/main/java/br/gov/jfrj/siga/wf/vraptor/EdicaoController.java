package br.gov.jfrj.siga.wf.vraptor;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.SigaSelecionavelControllerSupport;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.bl.Wf;

@Controller
public class EdicaoController
		extends SigaSelecionavelControllerSupport<WfDefinicaoDeProcedimento, DaoFiltroSelecionavel> {

	private static final String SUBDIRETORIO = "-subdiretorio-";
	private static final String VERIFICADOR_ACESSO = "MOD:Gerenciar modelos";
	private static final String UTF8 = "utf-8";
	private static final Logger LOGGER = Logger.getLogger(EdicaoController.class);

	/**
	 * @deprecated CDI eyes only
	 */
	public EdicaoController() {
		super();
	}

	@Inject
	public EdicaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/definicao-de-procedimento/listar")
	public void lista() throws Exception {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<WfDefinicaoDeProcedimento> modelos = dao().listarTodos(WfDefinicaoDeProcedimento.class, null);
			result.include("itens", modelos);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AplicacaoException(ex.getMessage(), 0, ex);
		}
	}

	@Get("app/pd/editar")
	public void edita(final Long id, final Integer postback) throws UnsupportedEncodingException {
		assertAcesso(VERIFICADOR_ACESSO);
		if (postback == null) {
			WfDefinicaoDeProcedimento pd = buscar(id);
			result.include("id", id);
			result.include("nome", pd.getNome());
			result.include("tarefas", pd.getTarefa());
		}
	}

	@Transacional
	@Post("app/pd/gravar")
	public void editarGravar(final Long id, final String nome, final List<WfDefinicaoDeTarefa> tarefa)
			throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		WfDefinicaoDeProcedimento pd = buscar(id);
		pd.setNome(nome);
		// TODO alterar também as tarefas e usar siga-sinc-lib para fazer a gravação
		final WfDefinicaoDeProcedimento pdAntiga = buscarAntiga(pd.getIdInicial());
		Wf.getInstance().getBL().gravar(pd, pdAntiga, null, getIdentidadeCadastrante());
		if ("Aplicar".equals(param("submit"))) {
			result.redirectTo("editar?id=" + pd.getId());
			return;
		}
		result.redirectTo(EdicaoController.class).lista();
	}

	@Transacional
	@Get("app/pd/desativar")
	public void desativar(final Long id) throws Exception {
		ModeloDao.iniciarTransacao();
		assertAcesso(VERIFICADOR_ACESSO);
		if (id == null) {
			throw new AplicacaoException("ID não informada");
		}
		final WfDefinicaoDeProcedimento pd = dao().consultar(id, WfDefinicaoDeProcedimento.class, false);
		dao().excluirComHistorico(pd, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		ModeloDao.commitTransacao();

		result.redirectTo(EdicaoController.class).lista();
	}

	private WfDefinicaoDeProcedimento buscar(final Long id) {
		if (id != null) {
			return Wf.getInstance().getBL().getCopia(dao().consultar(id, WfDefinicaoDeProcedimento.class, false));
		}
		return new WfDefinicaoDeProcedimento();
	}

	private WfDefinicaoDeProcedimento buscarAntiga(final Long idInicial) {
		if (idInicial != null) {
			return dao().consultarAtivoPorIdInicial(WfDefinicaoDeProcedimento.class, idInicial);
		}
		return null;
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		return null;
	}

}
