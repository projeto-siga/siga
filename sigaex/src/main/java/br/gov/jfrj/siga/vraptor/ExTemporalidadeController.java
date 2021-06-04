package br.gov.jfrj.siga.vraptor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;

@Controller
public class ExTemporalidadeController extends ExController {

	private static final String ACESSO_FE_TT = "FE:Ferramentas;TT:Tabela de Temporalidade";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExTemporalidadeController() {
		super();
	}

	@Inject
	public ExTemporalidadeController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/expediente/temporalidade/listar")
	public void listarTemporalidade() {
		assertAcesso(ACESSO_FE_TT);

		final List<ExTemporalidade> temporalidadeVigente = ExDao.getInstance().listarAtivos(ExTemporalidade.class, "descTemporalidade");

		result.include("temporalidadeVigente", temporalidadeVigente);
	}

	@Get("app/expediente/temporalidade/editar")
	public void editarTemporalidade(final Long idTemporalidade, final String acao) {
		assertAcesso(ACESSO_FE_TT);

		final ExTemporalidade exTemporal = buscarExTemporalidade(idTemporalidade);

		if (exTemporal == null && !"nova_temporalidade".equals(acao)) {
			throw new AplicacaoException("A temporalidade não está disponível. ID = " + idTemporalidade);
		}

		final List<CpUnidadeMedida> listaCpUnidade = ExDao.getInstance().listarUnidadesMedida();

		result.include("exTemporal", exTemporal);
		result.include("acao", acao);
		result.include("listaCpUnidade", listaCpUnidade);
	}

	@Transacional
	@Post("app/expediente/temporalidade/gravar")
	public void gravar(final Long idTemporalidade, final String acao, final String descTemporalidade, Integer valorTemporalidade, final Long idCpUnidade) {
		assertAcesso(ACESSO_FE_TT);
		
		if (valorTemporalidade == -1){
			valorTemporalidade = null;
		}

		if (descTemporalidade == null || descTemporalidade.trim().length() == 0) {
			throw new AplicacaoException("Você deve especificar uma descrição!");
		}
		if ((valorTemporalidade != null && valorTemporalidade >= 0) && idCpUnidade <= 0) {
			throw new AplicacaoException("Você deve especificar a unidade de medida do valor informado!");
		}
		if ((valorTemporalidade == null || valorTemporalidade <= 0) && idCpUnidade > 0) {
			throw new AplicacaoException("Você deve especificar um valor para a unidade de medida informada!");
		}

		ModeloDao.iniciarTransacao();

		ExTemporalidade exTemporal = buscarExTemporalidade(idTemporalidade);
		if ("nova_temporalidade".equals(acao)) {
			if (exTemporal != null) {
				throw new AplicacaoException("A temporalidade já existe: " + exTemporal.getDescTemporalidade());
			} else {
				exTemporal = new ExTemporalidade();
				fillEntity(descTemporalidade, valorTemporalidade, idCpUnidade, exTemporal);
				Ex.getInstance().getBL().incluirExTemporalidade(exTemporal, getIdentidadeCadastrante());
			}
		} else {
			ExTemporalidade exTempNovo = Ex.getInstance().getBL().getCopia(exTemporal);
			fillEntity(descTemporalidade, valorTemporalidade, idCpUnidade, exTempNovo);

			Ex.getInstance().getBL().alterarExTemporalidade(exTempNovo, exTemporal, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		}

		ModeloDao.commitTransacao();

		result.redirectTo("/app/expediente/temporalidade/listar");
	}

	private void fillEntity(final String descTemporalidade, final Integer valorTemporalidade, final Long idCpUnidade, final ExTemporalidade exTemporal) {
		exTemporal.setDescTemporalidade(descTemporalidade);
		exTemporal.setValorTemporalidade(valorTemporalidade);

		CpUnidadeMedida cpUnidade = null;
		if (idCpUnidade > 0) {
			cpUnidade = ExDao.getInstance().consultar(idCpUnidade, CpUnidadeMedida.class, false);

		}
		exTemporal.setCpUnidadeMedida(cpUnidade);
	}

	@Transacional
	@Get("app/expediente/temporalidade/excluir")
	public void excluir(final Long idTemporalidade) {
		assertAcesso(ACESSO_FE_TT);
		ModeloDao.iniciarTransacao();
		final ExTemporalidade exTemporal = buscarExTemporalidade(idTemporalidade);
		Date dt = dao().consultarDataEHoraDoServidor();

		if (exTemporal.getExViaArqCorrenteSet().size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (ExVia v : exTemporal.getExViaArqCorrenteSet()) {
				sb.append("(id: ");
				sb.append(v.getId());
				sb.append(") (Arquivo Corrente)");
				sb.append("<br/> Classificação: ");
				sb.append(v.getExClassificacao().getDescricaoCompleta());
				sb.append(" (Via ");
				sb.append(v.getLetraVia());
				sb.append(")<br/><br/>");
			}
			exTemporal.getExViaArqCorrenteSet().iterator().next();

			throw new AplicacaoException(
					"Não é possível excluir a temporalidade documental, pois está associada às seguintes classificações documentais:<br/><br/>" + sb.toString());
		}

		if (exTemporal.getExViaArqIntermediarioSet().size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (ExVia v : exTemporal.getExViaArqIntermediarioSet()) {
				sb.append("(id: ");
				sb.append(v.getId());
				sb.append(") (Arquivo Intermediario)");
				sb.append("<br/> Classificação: ");
				sb.append(v.getExClassificacao().getDescricaoCompleta());
				sb.append(" (Via ");
				sb.append(v.getLetraVia());
				sb.append(")<br/><br/>");
			}
			exTemporal.getExViaArqCorrenteSet().iterator().next();

			throw new AplicacaoException(
					"Não é possível excluir a temporalidade documental, pois está associada às seguintes classificações documentais:<br/><br/>" + sb.toString());
		}

		dao().excluirComHistorico(exTemporal, dt, getIdentidadeCadastrante());
		ModeloDao.commitTransacao();

		result.redirectTo("/app/expediente/temporalidade/listar");
	}

	private ExTemporalidade buscarExTemporalidade(final Long id) {
		if (id != null) {			
			return ExDao.getInstance().consultar(id, ExTemporalidade.class, false);
		}
		return null;
	}

}
