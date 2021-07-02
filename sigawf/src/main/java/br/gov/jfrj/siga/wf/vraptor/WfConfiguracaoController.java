package br.gov.jfrj.siga.wf.vraptor;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.vraptor.CpConfiguracaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfConfiguracaoComparator;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfConfiguracao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfParamCfg;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeConfiguracao;
import br.gov.jfrj.siga.wf.util.WfUtil;

@Controller
public class WfConfiguracaoController extends WfController {

	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Cadastrar Configurações";

	/**
	 * @deprecated CDI eyes only
	 */
	public WfConfiguracaoController() {
		super();
	}

	@Inject
	public WfConfiguracaoController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	@Get("app/configuracao/listar")
	public void lista(Integer idTpConfiguracao, Long idOrgaoUsu) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		if (idTpConfiguracao == null)
			idTpConfiguracao = WfTipoDeConfiguracao.INSTANCIAR_PROCEDIMENTO.getId();
		ITipoDeConfiguracao tpconf = WfTipoDeConfiguracao.getById(idTpConfiguracao);

		result.include("tipoDeConfiguracao", tpconf);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());
	}

	@Get("app/configuracao/listar_cadastradas")
	public void listaCadastradas(Integer idTpConfiguracao, Long idOrgaoUsu, Long idTpMov, Long idFormaDoc, Long idMod,
			String nmTipoRetorno, boolean campoFixo) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		WfConfiguracao config = new WfConfiguracao();

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(CpTipoDeConfiguracao.getById(idTpConfiguracao));
		} else {
			result.include("err", "Tipo de configuração não informado");
			result.use(Results.page()).forwardTo("/WEB-INF/page/erro.jsp");
			return;
		}

		config.setCpTipoConfiguracao(CpTipoDeConfiguracao.getById(idTpConfiguracao));

		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);

		List<WfConfiguracao> listConfig = Wf.getInstance().getConf().buscarConfiguracoesVigentes(config);

		for (CpConfiguracao cfg : listConfig)
			cfg.atualizarObjeto();

		Collections.sort(listConfig, new WfConfiguracaoComparator());

		ITipoDeConfiguracao tpconf = WfTipoDeConfiguracao.getById(idTpConfiguracao);
		CpConfiguracaoHelper.incluirAtributosDeListagem(result, tpconf, (List<CpConfiguracao>) (List) listConfig);

		result.include("configuracao", config);
		result.include("tpConfiguracao", config.getCpTipoConfiguracao());
	}

	protected void assertConfig(ITipoDeConfiguracao t, CpConfiguracao c) {
		CpConfiguracaoHelper.assertConfig(t, c);
		if (c instanceof WfConfiguracao) {
			WfConfiguracao cc = (WfConfiguracao) c;
			CpConfiguracaoHelper.assertConfig(t, c, cc.getDefinicaoDeProcedimento(),
					WfParamCfg.DEFINICAO_DE_PROCEDIMENTO);
		}
	}

	@Get("app/configuracao/editar")
	public void edita(Long id, boolean campoFixo, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Integer idSituacao, Integer idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, DpPessoaSelecao pessoaObjetoSel, DpLotacaoSelecao lotacaoObjetoSel,
			DpCargoSelecao cargoObjetoSel, DpFuncaoConfiancaSelecao funcaoObjetoSel, Long idOrgaoObjeto,
			Long idTpLotacao, String nmTipoRetorno, Long idDefinicaoDeProcedimento) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		WfConfiguracao config = new WfConfiguracao();

		if (id != null) {
			config = dao().consultar(id, WfConfiguracao.class, false);
		} else if (campoFixo) {
			config = new WfConfiguracaoBuilder().setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao)
					.setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel).setCargoSel(cargoSel).setFuncaoSel(funcaoSel)
					.setPessoaObjetoSel(pessoaObjetoSel).setLotacaoObjetoSel(lotacaoObjetoSel)
					.setCargoObjetoSel(cargoObjetoSel).setFuncaoObjetoSel(funcaoObjetoSel)
					.setIdOrgaoObjeto(idOrgaoObjeto).setIdTpLotacao(idTpLotacao)
					.setIdDefinicaoDeProcedimento(idDefinicaoDeProcedimento).construir();
		}
		escreverForm(config);
		if (idTpConfiguracao == null && config != null && config.getCpTipoConfiguracao() != null)
			idTpConfiguracao = config.getCpTipoConfiguracao().getId();
		if (idTpConfiguracao == null)
			throw new RuntimeException("Tipo de configuração deve ser informado");
		ITipoDeConfiguracao tpconf = WfTipoDeConfiguracao.getById(idTpConfiguracao);
		CpConfiguracaoHelper.incluirAtributosDeEdicao(result, tpconf, config);

		result.include("id", id);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("listaTiposLotacao", CpConfiguracaoHelper.getListaTiposLotacao(dao));
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("campoFixo", campoFixo);
		result.include("definicoesDeProcedimentos", getDefinicoesDeProcedimentos());
	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/excluir")
	public void excluir(Long id, String nmTipoRetorno, Long idMod, Long idFormaDoc) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		if (id != null) {
			WfConfiguracao config = dao().consultar(id, WfConfiguracao.class, false);
			config.setHisDtFim(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			result.redirectTo(this).lista(config.getCpTipoConfiguracao().getId(), null);
		} else
			throw new AplicacaoException("ID não informada");

	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/editar_gravar")
	public void editarGravar(Long id, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idTpFormaDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Integer idSituacao, Integer idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, DpPessoaSelecao pessoaObjeto_pessoaSel,
			DpLotacaoSelecao lotacaoObjeto_lotacaoSel, DpCargoSelecao cargoObjeto_cargoSel,
			DpFuncaoConfiancaSelecao funcaoObjeto_funcaoSel, Long idOrgaoObjeto, Long idTpLotacao, String nmTipoRetorno,
			boolean campoFixo, Long idDefinicaoDeProcedimento) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		final WfConfiguracao config = new WfConfiguracaoBuilder().setId(id).setIdSituacao(idSituacao)
				.setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel)
				.setCargoSel(cargoSel).setFuncaoSel(funcaoSel).setIdOrgaoObjeto(idOrgaoObjeto)
				.setPessoaObjetoSel(pessoaObjeto_pessoaSel).setLotacaoObjetoSel(lotacaoObjeto_lotacaoSel)
				.setCargoObjetoSel(cargoObjeto_cargoSel).setFuncaoObjetoSel(funcaoObjeto_funcaoSel)
				.setIdOrgaoUsu(idOrgaoUsu).setIdTpLotacao(idTpLotacao)
				.setIdDefinicaoDeProcedimento(idDefinicaoDeProcedimento).construir();

		CpConfiguracaoHelper.gravarConfiguracao(idTpConfiguracao, idSituacao, config, dao, getIdentidadeCadastrante());
		result.redirectTo(this).lista(idTpConfiguracao, null);
	}

	protected void escreverForm(WfConfiguracao c) throws Exception {
		CpConfiguracaoHelper.escreverForm(c, result);
		if (c.getDefinicaoDeProcedimento() != null)
			result.include("idDefinicaoDeProcedimento", c.getDefinicaoDeProcedimento().getId());
	}

	@SuppressWarnings("all")
	private WfTipoDeConfiguracao[] getListaTiposConfiguracao() throws Exception {
		return WfTipoDeConfiguracao.values();
	}

	private List<WfDefinicaoDeProcedimento> getDefinicoesDeProcedimentos() {
		return dao().listarDefinicoesDeProcedimentos();
	}

}
