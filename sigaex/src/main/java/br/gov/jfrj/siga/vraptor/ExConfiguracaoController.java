package br.gov.jfrj.siga.vraptor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoComparator;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.DpCargoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.vraptor.builder.ExConfiguracaoBuilder;

@Resource
public class ExConfiguracaoController extends ExController {

	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";
	
	public ExConfiguracaoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@Get("app/expediente/configuracao/listar")
	public void lista() throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaosUsu", getOrgaosUsu());

	}

	@Get("app/expediente/configuracao/editar")
	public void edita(Long id, boolean campoFixo, 
			Long idOrgaoUsu, Long idTpMov, Long idTpDoc,
			Long idMod, Long idFormaDoc,
			Long idNivelAcesso, Long idSituacao, Long idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel, 
			DpFuncaoConfiancaSelecao funcaoSel, ExClassificacaoSelecao classificacaoSel,
			Long idOrgaoObjeto) throws Exception {

		ExConfiguracao config = new ExConfiguracao();

		if (id != null) {
			config = daoCon(id);
		} else if(campoFixo) {

			final ExConfiguracaoBuilder configuracaoBuilder = ExConfiguracaoBuilder.novaInstancia()
					.setIdNivelAcesso(idNivelAcesso).setIdTpMov(idTpMov).setIdTpDoc(idTpDoc)
					.setIdMod(idMod).setIdFormaDoc(idFormaDoc).setIdNivelAcesso(idNivelAcesso)
					.setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao)
					.setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel).setCargoSel(cargoSel)
					.setFuncaoSel(funcaoSel).setClassificacaoSel(classificacaoSel).setIdOrgaoObjeto(idOrgaoObjeto);

			config = configuracaoBuilder.construir(dao());
		}
		escreverForm(config);

		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("listaSituacao", getListaSituacao());
		result.include("listaNivelAcesso", getListaNivelAcesso());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("listaTiposMovimentacao", getListaTiposMovimentacao());
		result.include("tiposFormaDoc", getTiposFormaDoc());
		result.include("listaTiposDocumento", getListaTiposDocumento());

	}

	@SuppressWarnings("all")
	@Get("app/expediente/configuracao/editar_gravar")
	public void editarGravar(Long id,
			Long idOrgaoUsu, Long idTpMov, Long idTpDoc,
			Long idMod, Long idFormaDoc,
			Long idNivelAcesso, Long idSituacao, Long idTpConfiguracao,
			DpPessoaSelecao pessoaSel, DpLotacaoSelecao lotacaoSel, DpCargoSelecao cargoSel, 
			DpFuncaoConfiancaSelecao funcaoSel, ExClassificacaoSelecao classificacaoSel,
			Long idOrgaoObjeto, String nmTipoRetorno) throws Exception {

		assertAcesso(VERIFICADOR_ACESSO);

		if(idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");

		if(idSituacao == null || idSituacao == 0)
			throw new AplicacaoException("Situação de Configuracao não informada");

		ExConfiguracao config;
		if (id == null)
			config = new ExConfiguracao();
		else
			config = daoCon(id);

		final ExConfiguracaoBuilder configuracaoBuilder = ExConfiguracaoBuilder.novaInstancia()
				.setIdNivelAcesso(idNivelAcesso).setIdTpMov(idTpMov).setIdTpDoc(idTpDoc)
				.setIdMod(idMod).setIdFormaDoc(idFormaDoc).setIdNivelAcesso(idNivelAcesso)
				.setIdSituacao(idSituacao).setIdTpConfiguracao(idTpConfiguracao)
				.setPessoaSel(pessoaSel).setLotacaoSel(lotacaoSel).setCargoSel(cargoSel)
				.setFuncaoSel(funcaoSel).setClassificacaoSel(classificacaoSel).setIdOrgaoObjeto(idOrgaoObjeto);

		config = configuracaoBuilder.construir(dao());

		try {
			dao().iniciarTransacao();
			config.setHisDtIni(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		escreveFormRetorno(nmTipoRetorno, configuracaoBuilder);

	}

	private void escreveFormRetorno(String nmTipoRetorno, ExConfiguracaoBuilder builder) throws Exception {

		if("ajax".equals(nmTipoRetorno)) {
			Integer idFormaDoc = builder.getIdFormaDoc() != null ? builder.getIdFormaDoc().intValue() : null;

			result.redirectTo(this).listaCadastradas(builder.getIdTpConfiguracao(), null, builder.getIdTpMov(), idFormaDoc, builder.getIdMod());
		}
		else if("modelo".equals(nmTipoRetorno)) {
			result.redirectTo(ExModeloController.class).edita(builder.getIdMod(), 1);;
			
		}else if("forma".equals(nmTipoRetorno)) {
			Integer idFormaDoc = builder.getIdFormaDoc() != null ? builder.getIdFormaDoc().intValue() : null;
			result.redirectTo(ExFormaDocumentoController.class).editarForma(idFormaDoc);
		} else {
			result.redirectTo(this).lista();
		}
	}

	@Get("app/expediente/configuracao/listar_cadastradas")
	public void listaCadastradas(Long idTpConfiguracao, Long idOrgaoUsu,
			Long idTpMov, Integer idFormaDoc, Long idMod) throws Exception {

		assertAcesso(VERIFICADOR_ACESSO);

		ExConfiguracao config = new ExConfiguracao();

		if (idTpConfiguracao != null && idTpConfiguracao != 0) {
			config.setCpTipoConfiguracao(dao().consultar(idTpConfiguracao,
					CpTipoConfiguracao.class, false));
		} else {
			result.include("err", "Tipo de configuração não informado");
			result.use(Results.page()).forwardTo("/paginas/erro.jsp");
			return;
		}

		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			config.setOrgaoUsuario(dao().consultar(idOrgaoUsu,
					CpOrgaoUsuario.class, false));
		} else 
			config.setOrgaoUsuario(null);

		if (idTpMov != null && idTpMov != 0) {
			config.setExTipoMovimentacao(dao().consultar(idTpMov,
					ExTipoMovimentacao.class, false));
		} else
			config.setExTipoMovimentacao(null);

		if (idFormaDoc != null && idFormaDoc != 0) {
			config.setExFormaDocumento(dao().consultar(idFormaDoc,
					ExFormaDocumento.class, false));
		} else
			config.setExFormaDocumento(null);

		if (idMod != null && idMod != 0) {
			config.setExModelo(dao().consultar(idMod, ExModelo.class, false));
		} else 
			config.setExModelo(null);

		List<ExConfiguracao> listConfig = Ex.getInstance().getConf()
				.buscarConfiguracoesVigentes(config);

		Collections.sort(listConfig, new ExConfiguracaoComparator());

		this.getRequest().setAttribute("listConfig", listConfig);
		this.getRequest().setAttribute("tpConfiguracao", config.getCpTipoConfiguracao());
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

		if (c.getOrgaoUsuario() != null)
			result.include("idOrgaoUsu", c.getOrgaoUsuario().getIdOrgaoUsu());

		if (c.getExTipoMovimentacao() != null)
			result.include("idTpMov", c.getExTipoMovimentacao().getIdTpMov());

		if (c.getExTipoDocumento() != null)
			result.include("idTpDoc", c.getExTipoDocumento().getIdTpDoc());

		if (c.getExTipoFormaDoc() != null)
			result.include("idTpFormaDoc", c.getExTipoFormaDoc().getIdTipoFormaDoc());

		if (c.getExFormaDocumento() != null)
			result.include("idFormaDoc", c.getExFormaDocumento().getIdFormaDoc());

		if (c.getExModelo() != null)
			result.include("idMod", c.getExModelo().getIdMod());

		if (c.getExNivelAcesso() != null)
			result.include("idNivelAcesso", c.getExNivelAcesso().getIdNivelAcesso());

		if (c.getCpSituacaoConfiguracao() != null)
			result.include("idSituacao", c.getCpSituacaoConfiguracao().getIdSitConfiguracao());

		if (c.getCpTipoConfiguracao() != null)
			result.include("idTpConfiguracao", c.getCpTipoConfiguracao().getIdTpConfiguracao());

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

		if (c.getOrgaoObjeto() != null)
			result.include("idOrgaoObjeto", c.getOrgaoObjeto().getIdOrgaoUsu());

		result.include("pessoaSel", pessoaSelecao);
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("cargoSel", cargoSelecao);
		result.include("funcaoSel", funcaoConfiancaSelecao);
		result.include("classificacaoSel", classificacaoSelecao);

	}

	@SuppressWarnings("all")
	private Set<CpTipoConfiguracao> getListaTiposConfiguracao() throws Exception {
		TreeSet<CpTipoConfiguracao> s = new TreeSet<CpTipoConfiguracao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((CpTipoConfiguracao) o1).getDscTpConfiguracao()
								.compareTo(
										((CpTipoConfiguracao) o2)
												.getDscTpConfiguracao());
					}
				});

		s.addAll(dao().listarTiposConfiguracao());

		return s;
	}

	@SuppressWarnings("all")
	private Set<CpSituacaoConfiguracao> getListaSituacao() throws Exception {
		TreeSet<CpSituacaoConfiguracao> s = new TreeSet<CpSituacaoConfiguracao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((CpSituacaoConfiguracao) o1)
								.getDscSitConfiguracao().compareTo(
										((CpSituacaoConfiguracao) o2)
												.getDscSitConfiguracao());
					}

				});

		s.addAll(dao().listarSituacoesConfiguracao());

		return s;
	}
	
	@SuppressWarnings("all")
	private Set<ExTipoMovimentacao> getListaTiposMovimentacao() throws Exception {
		TreeSet<ExTipoMovimentacao> s = new TreeSet<ExTipoMovimentacao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((ExTipoMovimentacao) o1)
								.getDescrTipoMovimentacao().compareTo(
										((ExTipoMovimentacao) o2)
												.getDescrTipoMovimentacao());
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
	
	private List<ExTipoDocumento> getListaTiposDocumento() throws Exception {
		return dao().listarExTiposDocumento();
	}
}
