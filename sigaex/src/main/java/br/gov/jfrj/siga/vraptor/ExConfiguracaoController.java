package br.gov.jfrj.siga.vraptor;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoComparator;
import br.gov.jfrj.siga.ex.model.enm.ExParamCfg;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExConfiguracaoBuilder;

@Controller
public class ExConfiguracaoController extends ExController {

	private static final String AJAX = "ajax";
	private static final String FORMA = "forma";
	private static final String MODELO = "modelo";
	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExConfiguracaoController() {
		super();
	}

	@Inject
	public ExConfiguracaoController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/configuracao/listar")
	public void lista(Integer idTpConfiguracao, Long idOrgaoUsu) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		if (idTpConfiguracao == null)
			idTpConfiguracao = ExTipoDeConfiguracao.AUTUAVEL.getId();
		ITipoDeConfiguracao tpconf = ExTipoDeConfiguracao.getById(idTpConfiguracao);

		result.include("tipoDeConfiguracao", tpconf);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());
	}

	@Get("app/configuracao/listar_cadastradas")
	public void listaCadastradas(Integer idTpConfiguracao, Long idOrgaoUsu, Long idTpMov, Long idFormaDoc, Long idMod,
			String nmTipoRetorno, boolean campoFixo) throws Exception {

		assertAcesso(VERIFICADOR_ACESSO);

		ExConfiguracao config = new ExConfiguracao();

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(CpTipoDeConfiguracao.getById(idTpConfiguracao));
		} else {
			result.include("err", "Tipo de configuração não informado");
			result.use(Results.page()).forwardTo("/WEB-INF/page/erro.jsp");
			return;
		}

		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);

		if (idTpMov != null && idTpMov != 0) {
			config.setExTipoMovimentacao(dao().consultar(idTpMov, ExTipoMovimentacao.class, false));
		} else
			config.setExTipoMovimentacao(null);

		if (idFormaDoc != null && idFormaDoc != 0) {
			config.setExFormaDocumento(dao().consultar(idFormaDoc, ExFormaDocumento.class, false));
		} else
			config.setExFormaDocumento(null);

		if (idMod != null && idMod != 0) {
			config.setExModelo(dao().consultar(idMod, ExModelo.class, false));
		} else
			config.setExModelo(null);

		List<ExConfiguracao> listConfig = Ex.getInstance().getConf().buscarConfiguracoesVigentes(config);

		Collections.sort(listConfig, new ExConfiguracaoComparator());

		ITipoDeConfiguracao tpconf = ExTipoDeConfiguracao.getById(idTpConfiguracao);
		CpConfiguracaoHelper.incluirAtributosDeListagem(result, tpconf, (List<CpConfiguracao>) (List) listConfig);

		result.include("idMod", idMod);
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("campoFixo", campoFixo);
		result.include("configuracao", config);
		result.include("idFormaDoc", idFormaDoc);
		result.include("tpConfiguracao", config.getCpTipoConfiguracao());
	}

	protected void assertConfig(ITipoDeConfiguracao t, CpConfiguracao c) {
		CpConfiguracaoHelper.assertConfig(t, c);
		if (c instanceof ExConfiguracao) {
			ExConfiguracao cc = (ExConfiguracao) c;
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExClassificacao(), ExParamCfg.CLASSIFICACAO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExTipoDocumento(), ExParamCfg.TIPO_DOCUMENTO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExTipoFormaDoc(), ExParamCfg.TIPO_FORMA_DOCUMENTO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExFormaDocumento(), ExParamCfg.FORMA_DOCUMENTO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExModelo(), ExParamCfg.MODELO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExNivelAcesso(), ExParamCfg.NIVEL_DE_ACESSO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExPapel(), ExParamCfg.PAPEL);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExTipoMovimentacao(), ExParamCfg.TIPO_MOVIMENTACAO);
			CpConfiguracaoHelper.assertConfig(t, c, cc.getExVia(), ExParamCfg.VIA);
		}
	}

	@Get("app/configuracao/editar")
	public void edita(Long id, boolean campoFixo, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Integer idSituacao, Integer idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, ExClassificacaoSelecao classificacaoSel,
			DpPessoaSelecao pessoaObjetoSel, DpLotacaoSelecao lotacaoObjetoSel, DpCargoSelecao cargoObjetoSel,
			DpFuncaoConfiancaSelecao funcaoObjetoSel, Long idOrgaoObjeto, Long idTpLotacao, String nmTipoRetorno)
			throws Exception {

		ExConfiguracao config = new ExConfiguracao();

		if (id != null) {
			config = daoCon(id);
		} else if (campoFixo) {
			final ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder()
					.setIdNivelAcesso(idNivelAcesso).setIdPapel(idPapel).setIdTpMov(idTpMov).setIdTpDoc(idTpDoc)
					.setIdMod(idMod).setIdFormaDoc(idFormaDoc).setIdSituacao(idSituacao)
					.setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel)
					.setCargoSel(cargoSel).setFuncaoSel(funcaoSel).setClassificacaoSel(classificacaoSel)
					.setPessoaObjetoSel(pessoaObjetoSel).setLotacaoObjetoSel(lotacaoObjetoSel)
					.setCargoObjetoSel(cargoObjetoSel).setFuncaoObjetoSel(funcaoObjetoSel)
					.setIdOrgaoObjeto(idOrgaoObjeto).setIdTpLotacao(idTpLotacao);

			config = configuracaoBuilder.construir();
		}
		escreverForm(config);
		if (idTpConfiguracao == null && config != null && config.getCpTipoConfiguracao() != null)
			idTpConfiguracao = config.getCpTipoConfiguracao().getId();
		if (idTpConfiguracao == null)
			throw new RuntimeException("Tipo de configuração deve ser informado");

		ITipoDeConfiguracao tpconf = ExTipoDeConfiguracao.getById(idTpConfiguracao);
		CpConfiguracaoHelper.incluirAtributosDeEdicao(result, tpconf, config);

		result.include("id", id);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("listaNivelAcesso", getListaNivelAcesso());
		result.include("listaPapel", getListaPapel());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("listaTiposMovimentacao", getListaTiposMovimentacao());
		result.include("tiposFormaDoc", getTiposFormaDoc());
		result.include("listaTiposDocumento", getListaTiposDocumento());
		result.include("listaTiposLotacao", getListaTiposLotacao());
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("campoFixo", campoFixo);
	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/excluir")
	public void excluir(Long id, String nmTipoRetorno, Long idMod, Long idFormaDoc) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		if (id != null) {
			try {
				dao().iniciarTransacao();
				ExConfiguracao config = daoCon(id);
				config.setHisDtFim(dao().consultarDataEHoraDoServidor());
				dao().gravarComHistorico(config, getIdentidadeCadastrante());
				dao().commitTransacao();
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		escreveFormRetornoExclusao(nmTipoRetorno, idMod, idFormaDoc);
	}

	@SuppressWarnings("all")
	@Transacional
	@Get("app/configuracao/editar_gravar")
	public void editarGravar(Long id, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idTpFormaDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Integer idSituacao, Integer idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel,
			DpFuncaoConfiancaSelecao funcaoSel, ExClassificacaoSelecao classificacaoSel,
			DpPessoaSelecao pessoaObjeto_pessoaSel, DpLotacaoSelecao lotacaoObjeto_lotacaoSel,
			DpCargoSelecao cargoObjeto_cargoSel, DpFuncaoConfiancaSelecao funcaoObjeto_funcaoSel, Long idOrgaoObjeto,
			Long idTpLotacao, String nmTipoRetorno, boolean campoFixo) throws Exception {

		final ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder().setId(id).setTipoPublicador(null)
				.setIdTpMov(idTpMov).setIdTpDoc(idTpDoc).setIdMod(idMod).setIdFormaDoc(idFormaDoc)
				.setIdTpFormaDoc(idTpFormaDoc).setIdNivelAcesso(idNivelAcesso).setIdPapel(idPapel)
				.setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel)
				.setLotacaoSel(lotacaoSel).setCargoSel(cargoSel).setFuncaoSel(funcaoSel)
				.setClassificacaoSel(classificacaoSel).setIdOrgaoObjeto(idOrgaoObjeto)
				.setPessoaObjetoSel(pessoaObjeto_pessoaSel).setLotacaoObjetoSel(lotacaoObjeto_lotacaoSel)
				.setCargoObjetoSel(cargoObjeto_cargoSel).setFuncaoObjetoSel(funcaoObjeto_funcaoSel)
				.setIdOrgaoUsu(idOrgaoUsu).setIdTpLotacao(idTpLotacao);

		
		ExConfiguracao conf = configuracaoBuilder.construir();
		conf.substituirPorObjetoInicial();
		gravarConfiguracao(idTpConfiguracao, idSituacao, conf);
		escreveFormRetorno(nmTipoRetorno, campoFixo, configuracaoBuilder);
	}

	@Post("app/configuracao/gerenciar_publicacao_boletim_gravar")
	@Transacional
	public void gerenciarPublicacaoBoletimGravar(Integer postback, String gerenciaPublicacao, Long idTpMov,
			Integer idTpConfiguracao, Long idFormaDoc, Long idMod, Integer tipoPublicador, Integer idSituacao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel) throws Exception {

		final ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder().setIdTpMov(idTpMov)
				.setIdMod(idMod).setIdFormaDoc(idFormaDoc).setIdSituacao(idSituacao)
				.setIdTpConfiguracao(idTpConfiguracao).setTipoPublicador(tipoPublicador).setPessoaSel(pessoaSel)
				.setLotacaoSel(lotacaoSel);

		ExConfiguracao exConfiguracao = configuracaoBuilder.construir();
		gravarConfiguracao(idTpConfiguracao, idSituacao, exConfiguracao);
		result.redirectTo(
				MessageFormat.format("/app/configuracao/gerenciar_publicacao_boletim?{0}", getUrlEncodedParameters()));
	}

	@SuppressWarnings("unchecked")
	@Post("app/configuracao/gerenciar_publicacao_boletim")
	@Get("app/configuracao/gerenciar_publicacao_boletim")
	public void gerenciarPublicacaoBoletim(Long idMod, Integer idFormaDoc, Long idTpMov, Long idSituacao,
			DpLotacaoSelecao lotacaoSel, Integer postback, DpPessoaSelecao pessoaSel, String gerenciaPublicacao,
			Long idTpConfiguracao, boolean alterouSel, Integer tipoPublicador) throws Exception {

		List<Object[]> itens = new ArrayList<>();
		this.validarPodeGerenciarBoletim();

		for (ExConfiguracao c : gerarPublicadores()) {
			String nomeMod = gerarNomeModelo(c);
			Object[] entrada = buscarEntradaPorNomeMod(itens, nomeMod);

			if (entrada == null) {
				entrada = new Object[2];
				entrada[0] = nomeMod;
				entrada[1] = new ArrayList<ExConfiguracao>();
				itens.add(entrada);
			}
			((ArrayList<ExConfiguracao>) entrada[1]).add(c);
		}

		result.include("idMod", idMod);
		result.include("idFormaDoc", idFormaDoc);
		result.include("idTpMov", idTpMov);
		result.include("idSituacao", idSituacao);
		result.include("postback", postback);
		result.include("gerenciaPublicacao", gerenciaPublicacao);
		result.include("idTpConfiguracao", idTpConfiguracao);
		result.include("alterouSel", alterouSel);
		result.include("listaFormas", getListaFormas());
		result.include("listaModelosPorForma", getListaModelosPorForma(idFormaDoc));
		result.include("listaTipoPublicador", getListaTipoPublicador());
		result.include("listaSituacaoPodeNaoPode", getListaSituacaoPodeNaoPode());
		result.include("tipoPublicador",
				Optional.fromNullable(tipoPublicador).or(ExConfiguracaoBuilder.ORGAO_INTEGRADO));
		result.include("itens", itens);
		result.include("request", getRequest());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("pessoaSel", new DpPessoaSelecao());

		if (ExConfiguracaoBuilder.isTipoMatricula(tipoPublicador)) {
			result.include("pessoaSel", Optional.fromNullable(pessoaSel).or(new DpPessoaSelecao()));
		} else {
			result.include("lotacaoSel", Optional.fromNullable(lotacaoSel).or(new DpLotacaoSelecao()));
		}
	}

	@SuppressWarnings("static-access")
	private void gravarConfiguracao(Integer idTpConfiguracao, Integer idSituacao, final ExConfiguracao config) {
		assertAcesso(VERIFICADOR_ACESSO);

		if (idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");

		if (idSituacao == null || idSituacao == 0) {
			Enum[] obrigatorios = config.getCpTipoConfiguracao().getObrigatorios();
			if (obrigatorios != null) {
				for (Enum obrigatorio : obrigatorios)
					if (obrigatorio.equals(CpParamCfg.SITUACAO))
						throw new AplicacaoException("Situação de Configuracao não informada");
			}
		}

		try {
			dao().iniciarTransacao();
			config.setHisDtIni(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
	}

	private void escreveFormRetornoExclusao(String nmTipoRetorno, Long idMod, Long idFormaDoc) throws Exception {
		if (MODELO.equals(nmTipoRetorno)) {
			redirectToModelo(idMod);
		} else if (FORMA.equals(nmTipoRetorno)) {
			redirectToForma(idFormaDoc);
		} else {
			result.redirectTo(this).lista(null, null);
		}
	}

	private void redirectToForma(Long idFormaDoc) {
		result.redirectTo("/app/forma/editar?id=" + idFormaDoc);
	}

	private void escreveFormRetorno(String nmTipoRetorno, boolean campoFixo, ExConfiguracaoBuilder builder)
			throws Exception {
		if (AJAX.equals(nmTipoRetorno)) {
			result.redirectTo(this).listaCadastradas(builder.getIdTpConfiguracao(), null, builder.getIdTpMov(),
					builder.getIdFormaDoc(), builder.getIdMod(), nmTipoRetorno, campoFixo);
		} else if (MODELO.equals(nmTipoRetorno)) {
			redirectToModelo(builder.getIdMod());
		} else if (FORMA.equals(nmTipoRetorno)) {
			redirectToForma(builder.getIdFormaDoc());
		} else {
			result.redirectTo(this).lista(builder.getIdTpConfiguracao(), null);
		}
	}

	private void redirectToModelo(Long idMod) throws UnsupportedEncodingException {
		result.redirectTo("/app/modelo/editar?id=" + idMod);
	}

	private Object[] buscarEntradaPorNomeMod(List<Object[]> itens, String nomeMod) {
		for (Object[] obj : itens) {
			if (obj[0].equals(nomeMod))
				return obj;
		}
		return null;
	}

	private String gerarNomeModelo(ExConfiguracao c) {
		if (c.getExModelo() != null) {
			String nomeMod = c.getExModelo().getNmMod();

			if (!c.getExModelo().getExFormaDocumento().getDescrFormaDoc().equals(nomeMod))
				nomeMod = MessageFormat.format("{0} -> {1}", c.getExModelo().getExFormaDocumento().getDescrFormaDoc(),
						nomeMod);

			return nomeMod;
		} else if (c.getExFormaDocumento() != null)
			return c.getExFormaDocumento().getDescrFormaDoc();
		return "[Todos os modelos]";
	}

	private Set<ExConfiguracao> gerarPublicadores() {
		Set<ExConfiguracao> publicadores = new HashSet<ExConfiguracao>();
		TreeSet<CpConfiguracaoCache> listaConfigs = Ex.getInstance().getConf()
					.getListaPorTipo(ExTipoDeConfiguracao.MOVIMENTAR);
		if (listaConfigs == null)
			return new TreeSet<ExConfiguracao>();

		for (CpConfiguracaoCache cfg : listaConfigs) {
			if (cfg instanceof ExConfiguracaoCache) {
				ExConfiguracaoCache config = (ExConfiguracaoCache) cfg;

				if (config.exTipoMovimentacao != 0
						&& config.exTipoMovimentacao == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM
						&& config.podeAdicionarComoPublicador(getTitular(), getLotaTitular())) {
					publicadores.add(dao.consultar(config.idConfiguracao, ExConfiguracao.class, false));
				}
			}
		}
		return publicadores;
	}

	private void validarPodeGerenciarBoletim() {
		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(),
				ExTipoDeConfiguracao.GERENCIAR_PUBLICACAO_BOLETIM))
			throw new AplicacaoException("Operação restrita");
	}

	private Set<ExFormaDocumento> getListaFormas() throws Exception {
		ExBL bl = Ex.getInstance().getBL();
		return bl.obterFormasDocumento(
				bl.obterListaModelos(null, null, false, false, null, null, false, null, null, false), null, null);
	}

	private Set<ExModelo> getListaModelosPorForma(Integer idFormaDoc) throws Exception {
		if (idFormaDoc != null && idFormaDoc != 0) {
			ExFormaDocumento forma = ExDao.getInstance().consultar(idFormaDoc, ExFormaDocumento.class, false);
			return forma.getExModeloSet();
		}
		return getListaModelos();
	}

	private Set<ExModelo> getListaModelos() throws Exception {
		TreeSet<ExModelo> s = new TreeSet<ExModelo>(getExModeloComparator());
		s.addAll(dao().listarExModelos());
		return s;
	}

	private Comparator<ExModelo> getExModeloComparator() {
		return new Comparator<ExModelo>() {
			public int compare(ExModelo o1, ExModelo o2) {
				return o1.getNmMod().compareTo(o2.getNmMod());
			}
		};
	}

	private Map<Integer, String> getListaTipoPublicador() {
		return TipoResponsavelEnum.getListaMatriculaLotacao();
	}

	private Set<CpSituacaoDeConfiguracaoEnum> getListaSituacaoPodeNaoPode() throws Exception {
		HashSet<CpSituacaoDeConfiguracaoEnum> s = new HashSet<>();
		s.add(CpSituacaoDeConfiguracaoEnum.PODE);
		s.add(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
		return s;
	}

	private ExConfiguracao daoCon(long id) {
		return dao().consultar(id, ExConfiguracao.class, false);
	}

	protected void escreverForm(ExConfiguracao c) throws Exception {
		CpConfiguracaoHelper.escreverForm(c, result);

		if (c.getExTipoMovimentacao() != null)
			result.include("idTpMov", c.getExTipoMovimentacao().getIdTpMov());

		if (c.getExTipoDocumento() != null)
			result.include("idTpDoc", c.getExTipoDocumento().getIdTpDoc());

		ExTipoFormaDoc tipoEspecie = c.getExTipoFormaDoc();
		ExFormaDocumento especie = c.getExFormaDocumento();

		if (c.getExModelo() != null && especie == null)
			especie = c.getExModelo().getExFormaDocumento();

		if (especie != null && tipoEspecie == null)
			tipoEspecie = especie.getExTipoFormaDoc();

		if (tipoEspecie != null)
			result.include("idTpFormaDoc", tipoEspecie.getIdTipoFormaDoc());

		if (especie != null)
			result.include("idFormaDoc", especie.getIdFormaDoc());

		if (c.getExModelo() != null)
			result.include("idMod", c.getExModelo().getModeloAtual().getId());

		if (c.getExNivelAcesso() != null)
			result.include("idNivelAcesso", c.getExNivelAcesso().getIdNivelAcesso());

		if (c.getExPapel() != null)
			result.include("idPapel", c.getExPapel().getIdPapel());

		ExClassificacaoSelecao classificacaoSelecao = new ExClassificacaoSelecao();
		if (c.getExClassificacao() != null)
			classificacaoSelecao.buscarPorObjeto(c.getExClassificacao());
		result.include("classificacaoSel", classificacaoSelecao);
	}

	@SuppressWarnings("all")
	private ExTipoDeConfiguracao[] getListaTiposConfiguracao() throws Exception {
		return ExTipoDeConfiguracao.values();
	}

	@SuppressWarnings("all")
	private Set<CpSituacaoDeConfiguracaoEnum> getListaSituacao() throws Exception {
		TreeSet<CpSituacaoDeConfiguracaoEnum> s = new TreeSet<>(new Comparator<CpSituacaoDeConfiguracaoEnum>() {

			public int compare(CpSituacaoDeConfiguracaoEnum o1, CpSituacaoDeConfiguracaoEnum o2) {
				return o1.getDescr().compareTo(o2.getDescr());
			}

		});

		for (CpSituacaoDeConfiguracaoEnum sit : CpSituacaoDeConfiguracaoEnum.values())
			s.add(sit);

		return s;
	}

	@SuppressWarnings("all")
	private Set<ExTipoMovimentacao> getListaTiposMovimentacao() throws Exception {
		TreeSet<ExTipoMovimentacao> s = new TreeSet<ExTipoMovimentacao>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((ExTipoMovimentacao) o1).getDescrTipoMovimentacao()
						.compareTo(((ExTipoMovimentacao) o2).getDescrTipoMovimentacao());
			}

		});

		s.addAll(dao().listarExTiposMovimentacao());

		return s;
	}

	private List<ExTipoFormaDoc> getTiposFormaDoc() throws Exception {
		return dao().listarExTiposFormaDoc();
	}

	private List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		return dao().listarOrdemNivel();
	}

	private List<ExPapel> getListaPapel() throws Exception {
		return dao().listarExPapel();
	}

	private List<ExTipoDocumento> getListaTiposDocumento() throws Exception {
		return dao().listarExTiposDocumento();
	}

	private List<CpTipoLotacao> getListaTiposLotacao() throws Exception {
		return dao().listarTiposLotacao();
	}
}
