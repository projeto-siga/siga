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
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoComparator;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExConfiguracaoBuilder;

@Resource
public class ExConfiguracaoController extends ExController {

	private static final String AJAX = "ajax";
	private static final String FORMA = "forma";
	private static final String MODELO = "modelo";
	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";
	
	public ExConfiguracaoController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/expediente/configuracao/listar")
	public void lista() throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());
	}
	
	
	@Get("app/expediente/configuracao/editar")
	public void edita(Long id, boolean campoFixo, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Long idSituacao, Long idTpConfiguracao, DpPessoaSelecao pessoaSel,
			DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel, DpFuncaoConfiancaSelecao funcaoSel,
			ExClassificacaoSelecao classificacaoSel, DpPessoaSelecao pessoaObjetoSel,
			DpLotacaoSelecao lotacaoObjetoSel, DpCargoSelecao cargoObjetoSel, DpFuncaoConfiancaSelecao funcaoObjetoSel, Long idOrgaoObjeto, Long idTpLotacao, String nmTipoRetorno)
			throws Exception {

		ExConfiguracao config = new ExConfiguracao();

		if (id != null) {
			config = daoCon(id);
		} else if (campoFixo) {
			final ExConfiguracaoBuilder configuracaoBuilder = ExConfiguracaoBuilder.novaInstancia()
					.setIdNivelAcesso(idNivelAcesso).setIdPapel(idPapel).setIdTpMov(idTpMov).setIdTpDoc(idTpDoc).setIdMod(idMod)
					.setIdFormaDoc(idFormaDoc).setIdSituacao(idSituacao)
					.setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel)
					.setCargoSel(cargoSel).setFuncaoSel(funcaoSel).setClassificacaoSel(classificacaoSel)
					.setPessoaObjetoSel(pessoaObjetoSel).setLotacaoObjetoSel(lotacaoObjetoSel)
					.setCargoObjetoSel(cargoObjetoSel).setFuncaoObjetoSel(funcaoObjetoSel)
					.setIdOrgaoObjeto(idOrgaoObjeto).setIdTpLotacao(idTpLotacao);

			config = configuracaoBuilder.construir(dao());
		}
		escreverForm(config);

		result.include("id", id);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("listaSituacao", getListaSituacao());
		result.include("listaNivelAcesso", getListaNivelAcesso());
		result.include("listaPapel", getListaPapel());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("listaTiposMovimentacao", getListaTiposMovimentacao());
		result.include("tiposFormaDoc", getTiposFormaDoc());
		result.include("listaTiposDocumento", getListaTiposDocumento());
		result.include("listaTiposLotacao", getListaTiposLotacao());
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("config", config);
		result.include("campoFixo", campoFixo);
		result.include("configuracao", config);
	}

	@SuppressWarnings("all")
	@Get("app/expediente/configuracao/excluir")
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
	@Get("app/expediente/configuracao/editar_gravar")
	public void editarGravar(Long id, Long idOrgaoUsu, Long idTpMov, Long idTpDoc, Long idTpFormaDoc, Long idMod,
			Long idFormaDoc, Long idNivelAcesso, Long idPapel, Long idSituacao, Long idTpConfiguracao, DpPessoaSelecao pessoaSel,
			DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel, DpFuncaoConfiancaSelecao funcaoSel,
			ExClassificacaoSelecao classificacaoSel, DpPessoaSelecao pessoaObjeto_pessoaSel,
			DpLotacaoSelecao lotacaoObjeto_lotacaoSel, DpCargoSelecao cargoObjeto_cargoSel, DpFuncaoConfiancaSelecao funcaoObjeto_funcaoSel, Long idOrgaoObjeto, Long idTpLotacao, String nmTipoRetorno,
			boolean campoFixo) throws Exception {

		final ExConfiguracaoBuilder configuracaoBuilder = ExConfiguracaoBuilder.novaInstancia().setId(id)
				.setTipoPublicador(null).setIdTpMov(idTpMov).setIdTpDoc(idTpDoc).setIdMod(idMod)
				.setIdFormaDoc(idFormaDoc).setIdTpFormaDoc(idTpFormaDoc).setIdNivelAcesso(idNivelAcesso).setIdPapel(idPapel)
				.setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao).setPessoaSel(pessoaSel)
				.setLotacaoSel(lotacaoSel).setCargoSel(cargoSel).setFuncaoSel(funcaoSel)
				.setClassificacaoSel(classificacaoSel).setIdOrgaoObjeto(idOrgaoObjeto).setPessoaObjetoSel(pessoaObjeto_pessoaSel).setLotacaoObjetoSel(lotacaoObjeto_lotacaoSel)
				.setCargoObjetoSel(cargoObjeto_cargoSel).setFuncaoObjetoSel(funcaoObjeto_funcaoSel).setIdOrgaoUsu(idOrgaoUsu)
				.setIdTpLotacao(idTpLotacao);

		gravarConfiguracao(idTpConfiguracao, idSituacao, configuracaoBuilder.construir(dao()));
		escreveFormRetorno(nmTipoRetorno, campoFixo, configuracaoBuilder);
	}

	@Post("app/expediente/configuracao/gerenciar_publicacao_boletim_gravar")
	public void gerenciarPublicacaoBoletimGravar(Integer postback, String gerenciaPublicacao, Long idTpMov,
			Long idTpConfiguracao, Long idFormaDoc, Long idMod, Integer tipoPublicador, Long idSituacao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel) throws Exception {

		final ExConfiguracaoBuilder configuracaoBuilder = ExConfiguracaoBuilder.novaInstancia().setIdTpMov(idTpMov)
				.setIdMod(idMod).setIdFormaDoc(idFormaDoc).setIdSituacao(idSituacao)
				.setIdTpConfiguracao(idTpConfiguracao).setTipoPublicador(tipoPublicador).setPessoaSel(pessoaSel)
				.setLotacaoSel(lotacaoSel);

		ExConfiguracao exConfiguracao = configuracaoBuilder.construir(dao());
		gravarConfiguracao(idTpConfiguracao, idSituacao, exConfiguracao);
		result.redirectTo(MessageFormat.format("/app/expediente/configuracao/gerenciar_publicacao_boletim?{0}",
				getUrlEncodedParameters()));
	}

	@Get("app/expediente/configuracao/listar_cadastradas")
	public void listaCadastradas(Long idTpConfiguracao, Long idOrgaoUsu, Long idTpMov, Long idFormaDoc, Long idMod,
			String nmTipoRetorno, boolean campoFixo) throws Exception {

		assertAcesso(VERIFICADOR_ACESSO);

		ExConfiguracao config = new ExConfiguracao();

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(dao().consultar(idTpConfiguracao, CpTipoConfiguracao.class, false));
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

		result.include("idMod", idMod);
		result.include("nmTipoRetorno", nmTipoRetorno);
		result.include("campoFixo", campoFixo);
		result.include("configuracao", config);
		result.include("idFormaDoc", idFormaDoc);

		this.getRequest().setAttribute("listConfig", listConfig);
		this.getRequest().setAttribute("tpConfiguracao", config.getCpTipoConfiguracao());
	}

	@SuppressWarnings("unchecked")
	@Post("app/expediente/configuracao/gerenciar_publicacao_boletim")
	@Get("app/expediente/configuracao/gerenciar_publicacao_boletim")
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
	private void gravarConfiguracao(Long idTpConfiguracao, Long idSituacao, final ExConfiguracao config) {
		assertAcesso(VERIFICADOR_ACESSO);

		if (idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");

		if (idSituacao == null || idSituacao == 0)
			throw new AplicacaoException("Situação de Configuracao não informada");

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
			result.redirectTo(this).lista();
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
			result.redirectTo(this).lista();
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
		TreeSet<CpConfiguracao> listaConfigs = getListaConfiguracao();

		for (CpConfiguracao cfg : listaConfigs) {
			if (cfg instanceof ExConfiguracao) {
				ExConfiguracao config = (ExConfiguracao) cfg;

				if (config.isAgendamentoPublicacaoBoletim()
						&& config.podeAdicionarComoPublicador(getTitular(), getLotaTitular())) {
					publicadores.add(config);
				}
			}
		}
		return publicadores;
	}

	private TreeSet<CpConfiguracao> getListaConfiguracao() {
		TreeSet<CpConfiguracao> listaConfigs = Ex.getInstance().getConf()
				.getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_MOVIMENTAR);

		if (listaConfigs == null)
			return new TreeSet<CpConfiguracao>();
		return listaConfigs;
	}

	private void validarPodeGerenciarBoletim() {
		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(),
				CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_PUBLICACAO_BOLETIM))
			throw new AplicacaoException("Operação restrita");
	}

	private Set<ExFormaDocumento> getListaFormas() throws Exception {
		ExBL bl = Ex.getInstance().getBL();
		return bl.obterFormasDocumento(bl.obterListaModelos(null, null, false, false, null, null, false, null, null, false), null, null);
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
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, SigaMessages.getMessage("usuario.matricula"));
		map.put(2, "Órgão Integrado");
		return map;
	}

	private Set<ExSituacaoConfiguracao> getListaSituacaoPodeNaoPode() throws Exception {
		HashSet<ExSituacaoConfiguracao> s = new HashSet<ExSituacaoConfiguracao>();
		s.add(ExDao.getInstance().consultar(1L, ExSituacaoConfiguracao.class, false));
		s.add(ExDao.getInstance().consultar(2L, ExSituacaoConfiguracao.class, false));
		return s;
	}

	private ExConfiguracao daoCon(long id) {
		return dao().consultar(id, ExConfiguracao.class, false);
	}

	private void escreverForm(ExConfiguracao c) throws Exception {
		DpPessoaSelecao pessoaSelecao = new DpPessoaSelecao();
		DpLotacaoSelecao lotacaoSelecao = new DpLotacaoSelecao();
		DpFuncaoConfiancaSelecao funcaoConfiancaSelecao = new DpFuncaoConfiancaSelecao();
		DpCargoSelecao cargoSelecao = new DpCargoSelecao();
		ExClassificacaoSelecao classificacaoSelecao = new ExClassificacaoSelecao();
		DpPessoaSelecao pessoaObjetoSelecao = new DpPessoaSelecao();
		DpLotacaoSelecao lotacaoObjetoSelecao = new DpLotacaoSelecao();
		DpFuncaoConfiancaSelecao funcaoConfiancaObjetoSelecao = new DpFuncaoConfiancaSelecao();
		DpCargoSelecao cargoObjetoSelecao = new DpCargoSelecao();

		if (c.getOrgaoUsuario() != null)
			result.include("idOrgaoUsu", c.getOrgaoUsuario().getIdOrgaoUsu());

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
			result.include("idMod", c.getExModelo().getIdMod());

		if (c.getExNivelAcesso() != null)
			result.include("idNivelAcesso", c.getExNivelAcesso().getIdNivelAcesso());

		if (c.getExPapel() != null)
			result.include("idPapel", c.getExPapel().getIdPapel());

		if (c.getCpSituacaoConfiguracao() != null)
			result.include("idSituacao", c.getCpSituacaoConfiguracao().getIdSitConfiguracao());

		if (c.getCpTipoConfiguracao() != null)
			result.include("idTpConfiguracao", c.getCpTipoConfiguracao().getIdTpConfiguracao());

		if (c.getCpTipoLotacao() != null)
			result.include("idTpLotacao", c.getCpTipoLotacao().getIdTpLotacao());

		if (c.getDpPessoa() != null)
			pessoaSelecao.buscarPorObjeto(c.getDpPessoa());

		if (c.getLotacao() != null)
			lotacaoSelecao.buscarPorObjeto(c.getLotacao());

		if (c.getCargo() != null)
			cargoSelecao.buscarPorObjeto(c.getCargo());

		if (c.getFuncaoConfianca() != null)
			funcaoConfiancaSelecao.buscarPorObjeto(c.getFuncaoConfianca());

		if (c.getExClassificacao() != null)
			classificacaoSelecao.buscarPorObjeto(c.getExClassificacao());

		if (c.getPessoaObjeto() != null)
			pessoaObjetoSelecao.buscarPorObjeto(c.getPessoaObjeto());

		if (c.getLotacaoObjeto() != null)
			lotacaoObjetoSelecao.buscarPorObjeto(c.getLotacaoObjeto());

		if (c.getCargoObjeto() != null)
			cargoObjetoSelecao.buscarPorObjeto(c.getCargoObjeto());

		if (c.getFuncaoConfianca() != null)
			funcaoConfiancaObjetoSelecao.buscarPorObjeto(c.getFuncaoConfiancaObjeto());

		if (c.getOrgaoObjeto() != null)
			result.include("idOrgaoObjeto", c.getOrgaoObjeto().getIdOrgaoUsu());

		result.include("pessoaSel", pessoaSelecao);
		result.include("lotacaoSel", lotacaoSelecao);
		result.include("cargoSel", cargoSelecao);
		result.include("funcaoSel", funcaoConfiancaSelecao);
		result.include("classificacaoSel", classificacaoSelecao);

		result.include("pessoaObjeto_pessoaSel", pessoaObjetoSelecao);
		result.include("lotacaoObjeto_lotacaoSel", lotacaoObjetoSelecao);
		result.include("cargoObjeto_cargoSel", cargoObjetoSelecao);
		result.include("funcaoObjeto_funcaoSel", funcaoConfiancaObjetoSelecao);
	}

	@SuppressWarnings("all")
	private Set<CpTipoConfiguracao> getListaTiposConfiguracao() throws Exception {
		TreeSet<CpTipoConfiguracao> s = new TreeSet<CpTipoConfiguracao>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((CpTipoConfiguracao) o1).getDscTpConfiguracao()
						.compareTo(((CpTipoConfiguracao) o2).getDscTpConfiguracao());
			}
		});

		s.addAll(dao().listarTiposConfiguracao());

		return s;
	}

	@SuppressWarnings("all")
	private Set<CpSituacaoConfiguracao> getListaSituacao() throws Exception {
		TreeSet<CpSituacaoConfiguracao> s = new TreeSet<CpSituacaoConfiguracao>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((CpSituacaoConfiguracao) o1).getDscSitConfiguracao()
						.compareTo(((CpSituacaoConfiguracao) o2).getDscSitConfiguracao());
			}

		});

		s.addAll(dao().listarSituacoesConfiguracao());

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
