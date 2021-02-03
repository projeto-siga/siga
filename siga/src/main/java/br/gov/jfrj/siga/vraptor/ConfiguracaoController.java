package br.gov.jfrj.siga.vraptor;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoComparator;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class ConfiguracaoController extends SigaController {

	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Cadastrar Configurações";

	/**
	 * @deprecated CDI eyes only
	 */
	public ConfiguracaoController() {
		super();
	}

	@Inject
	public ConfiguracaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/configuracao/listar")
	public void lista(Long idTpConfiguracao, Long idOrgaoUsu) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		if (idTpConfiguracao == null)
			idTpConfiguracao = CpTipoDeConfiguracao.UTILIZAR_SERVICO.getId();
		CpTipoDeConfiguracao tpconf = CpTipoDeConfiguracao.getById(idTpConfiguracao);

		result.include("tipoDeConfiguracao", tpconf);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());
	}

	@Get("app/configuracao/listar_cadastradas")
	public void listaCadastradas(Long idTpConfiguracao, Long idOrgaoUsu, Long idTpMov, Long idFormaDoc, Long idMod,
			String nmTipoRetorno, boolean campoFixo) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		CpConfiguracao config = new CpConfiguracao();

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(dao().consultar(idTpConfiguracao, CpTipoConfiguracao.class, false));
		} else {
			result.include("err", "Tipo de configuração não informado");
			result.use(Results.page()).forwardTo("/WEB-INF/page/erro.jsp");
			return;
		}

		config.setCpTipoConfiguracao(dao().consultar(idTpConfiguracao, CpTipoConfiguracao.class, false));

		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);

		List<CpConfiguracao> listConfig = Cp.getInstance().getConf().buscarConfiguracoesVigentes(config);

		Collections.sort(listConfig, new CpConfiguracaoComparator());

		result.include("configuracao", config);
		CpTipoDeConfiguracao tpconf = CpTipoDeConfiguracao.getById(idTpConfiguracao);

		result.include("tipoDeConfiguracao", tpconf);
		result.include("listConfig", listConfig);
		result.include("tpConfiguracao", config.getCpTipoConfiguracao());

	}

	@Get("app/configuracao/editar")
	public void edita(Long id, boolean campoFixo, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Long idSituacao, Long idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, DpPessoaSelecao pessoaObjetoSel, DpLotacaoSelecao lotacaoObjetoSel,
			DpCargoSelecao cargoObjetoSel, DpFuncaoConfiancaSelecao funcaoObjetoSel, Long idOrgaoObjeto,
			Long idTpLotacao, String nmTipoRetorno, Long idDefinicaoDeProcedimento) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		CpConfiguracao config = new CpConfiguracao();

		if (id != null) {
			config = dao().consultar(id, CpConfiguracao.class, false);
		} else if (campoFixo) {
			config = new CpConfiguracaoBuilder(CpConfiguracaoBuilder.class, dao).setIdSituacao(idSituacao)
					.setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel)
					.setCargoSel(cargoSel).setFuncaoSel(funcaoSel).setPessoaObjetoSel(pessoaObjetoSel)
					.setLotacaoObjetoSel(lotacaoObjetoSel).setCargoObjetoSel(cargoObjetoSel)
					.setFuncaoObjetoSel(funcaoObjetoSel).setIdOrgaoObjeto(idOrgaoObjeto).setIdTpLotacao(idTpLotacao)
					.construir();
		}
		CpConfiguracaoHelper.escreverForm(config, result);
		if (idTpConfiguracao == null && config != null && config.getCpTipoConfiguracao() != null)
			idTpConfiguracao = config.getCpTipoConfiguracao().getIdTpConfiguracao();
		if (idTpConfiguracao == null)
			throw new RuntimeException("Tipo de configuração deve ser informado");
		CpTipoDeConfiguracao tpconf = CpTipoDeConfiguracao.getById(idTpConfiguracao);

		result.include("tipoDeConfiguracao", tpconf);
		result.include("situacoes", tpconf.getSituacoes());
		result.include("id", id);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
//		result.include("listaSituacao", getListaSituacao());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("listaTiposLotacao", CpConfiguracaoHelper.getListaTiposLotacao(dao));
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("config", config);
		result.include("campoFixo", campoFixo);
		result.include("configuracao", config);
	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/excluir")
	public void excluir(Long id, String nmTipoRetorno, Long idMod, Long idFormaDoc) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		if (id != null) {
			CpConfiguracao config = dao().consultar(id, CpConfiguracao.class, false);
			config.setHisDtFim(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			result.redirectTo(this).lista(config.getCpTipoConfiguracao().getIdTpConfiguracao(), null);
		} else
			throw new AplicacaoException("ID não informada");

	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/editar_gravar")
	public void editarGravar(Long id, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idTpFormaDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Long idSituacao, Long idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, DpPessoaSelecao pessoaObjeto_pessoaSel,
			DpLotacaoSelecao lotacaoObjeto_lotacaoSel, DpCargoSelecao cargoObjeto_cargoSel,
			DpFuncaoConfiancaSelecao funcaoObjeto_funcaoSel, Long idOrgaoObjeto, Long idTpLotacao, String nmTipoRetorno,
			boolean campoFixo) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		final CpConfiguracao config = new CpConfiguracaoBuilder(CpConfiguracaoBuilder.class, dao).setId(id)
				.setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel)
				.setLotacaoSel(lotacaoSel).setCargoSel(cargoSel).setFuncaoSel(funcaoSel).setIdOrgaoObjeto(idOrgaoObjeto)
				.setPessoaObjetoSel(pessoaObjeto_pessoaSel).setLotacaoObjetoSel(lotacaoObjeto_lotacaoSel)
				.setCargoObjetoSel(cargoObjeto_cargoSel).setFuncaoObjetoSel(funcaoObjeto_funcaoSel)
				.setIdOrgaoUsu(idOrgaoUsu).setIdTpLotacao(idTpLotacao).construir();

		CpConfiguracaoHelper.gravarConfiguracao(idTpConfiguracao, idSituacao, config, dao, getIdentidadeCadastrante());
		result.redirectTo(this).lista(idTpConfiguracao, null);
	}

	@SuppressWarnings("all")
	private CpTipoDeConfiguracao[] getListaTiposConfiguracao() throws Exception {
		return CpTipoDeConfiguracao.values();
	}
}
