package br.gov.jfrj.siga.vraptor;

import static br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios.CARGOS;
import static br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios.FUNCOES;
import static br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios.ORGAOS;
import static br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios.PESSOAS;
import static br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios.UNIDADES;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargoDTO;
import br.gov.jfrj.siga.dp.DpFuncaoDTO;
import br.gov.jfrj.siga.dp.DpPessoaDTO;
import br.gov.jfrj.siga.dp.DpUnidadeDTO;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoDTO;
import br.gov.jfrj.siga.ex.ExConfiguracaoDestinatarios;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExConfiguracaoBuilder;

@Controller
@Path("app/configuracao2")
public class ExConfiguracao2Controller extends ExController {
	
	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ExConfiguracao2Controller() {
		super();
	}
	
	@Inject
	public ExConfiguracao2Controller(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}
		
	@Get("/nova")
	public void cadastro() throws Exception {	
		assertAcesso(VERIFICADOR_ACESSO);
		
		result.include("tiposConfiguracao", getListaTiposConfiguracao());		
		result.include("destinatarios", ExConfiguracaoDestinatarios.values());		
	}
		
	@Consumes("application/json")
	@Post("/nova")
	public void salvar(ExConfiguracaoDTO configuracao) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		
		try {
		
			if (configuracao != null) {							
				for (Long idModelo : configuracao.getIdModelos()) {										
					if (configuracao.getDestinatarios().equals(ORGAOS)) {
						gravarConfiguracaoOrgaos(configuracao, idModelo);					
					}
					
					if (configuracao.getDestinatarios().equals(UNIDADES)) {
						gravarConfiguracaoUnidades(configuracao, idModelo);													
					}
					
					if (configuracao.getDestinatarios().equals(CARGOS)) {
						gravarConfiguracaoCargos(configuracao, idModelo);
					}
					
					if (configuracao.getDestinatarios().equals(FUNCOES)) {
						gravarConfiguracaoFuncoes(configuracao, idModelo);
					}
					
					if (configuracao.getDestinatarios().equals(PESSOAS)) {
						gravarConfiguracaoPessoas(configuracao, idModelo);
					}											
				}			
			}
			
			result.nothing();
			
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.forwardTo(this).cadastro();
		}		
	}

	@Consumes("application/json")
	@Path("/modelos")
	public void listarModelos() {			
		List<ExModelo> modelos = getModelos(null);		
		result.use(Results.json()).from(modelos).serialize();
	}
	
	@Consumes("application/json")
	@Path("/movimentacoes")
	public void listarMovimentacoes() throws Exception {				
		List<ExTipoMovimentacao> tiposMovimentacao = getListaTiposMovimentacao();		
		result.use(Results.json()).from(tiposMovimentacao).serialize();
	}
	
	@Consumes("application/json")
	@Path("/orgaos")
	public void listarOrgaos() {										
		List<CpOrgaoUsuario> orgaos = dao().listarOrgaosUsuarios();		
		result.use(Results.json()).from(orgaos).serialize();
	}
	
	@Consumes("application/json")
	@Path("/unidades")
	public void listarUnidades(DpUnidadeDTO dados) {										
		List<DpUnidadeDTO> unidades = dao().lotacaoPorOrgaos(dados.getIdOrgaoSelecao());		
		result.use(Results.json()).from(unidades).serialize();
	}
	
	@Consumes("application/json")
	@Path("/cargos")
	public void listarCargos(DpCargoDTO dados) {
		List<DpCargoDTO> cargos = dao().cargoPorOrgaos(dados.getIdOrgaoSelecao());
		result.use(Results.json()).from(cargos).serialize();
	}
	
	@Consumes("application/json")
	@Path("/funcoes")
	public void listarFuncoes(DpFuncaoDTO dados) {
		List<DpFuncaoDTO> funcoes = dao().funcaoPorOrgaos(dados.getIdOrgaoSelecao());
		result.use(Results.json()).from(funcoes).serialize();
	}
	
	@Consumes("application/json")
	@Path("/pessoas")
	public void listarPessoas(DpPessoaDTO dados) {
		List<DpPessoaDTO> pessoas = dao().pessoaPorOrgaos(dados.getIdOrgaoSelecao());
		result.use(Results.json()).from(pessoas).serialize();
	}
	
	@Get("/tipos/dicionario")
	public void dicionario() throws Exception {	
		result.include("tiposConfiguracao", getListaTiposConfiguracao());			
	}
	
	private List<ExModelo> getModelos(final Long idFormaDoc) {
		ExFormaDocumento forma = null;
		if (idFormaDoc != null && idFormaDoc != 0) {
			forma = dao().consultar(idFormaDoc, ExFormaDocumento.class, false);
		}

		return Ex
				.getInstance()
				.getBL()
				.obterListaModelos(null, forma, false, false, null, null, false, getTitular(),
						getLotaTitular(), false);
	}
	
	@SuppressWarnings("all")
	private Set<ITipoDeConfiguracao> getListaTiposConfiguracao() throws Exception {
		TreeSet<ITipoDeConfiguracao> s = new TreeSet<>(new Comparator<ITipoDeConfiguracao>() {

			public int compare(ITipoDeConfiguracao o1, ITipoDeConfiguracao o2) {
				return o1.getDescr()
						.compareTo(o2.getDescr());
			}
		});

		s.addAll(dao().listarTiposConfiguracao());

		return s;
	}
	
	@SuppressWarnings("all")
	private List<ExTipoMovimentacao> getListaTiposMovimentacao() throws Exception {
		TreeSet<ExTipoMovimentacao> s = new TreeSet<ExTipoMovimentacao>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((ExTipoMovimentacao) o1).getDescrTipoMovimentacao()
						.compareTo(((ExTipoMovimentacao) o2).getDescrTipoMovimentacao());
			}

		});

		s.addAll(dao().listarExTiposMovimentacao());

		return new ArrayList<>(s);
	}
	
	private void gravarConfiguracaoOrgaos(ExConfiguracaoDTO configuracao, Long idModelo) {
		ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder();
		for (Long idOrgao : configuracao.getIdOrgaos()) {
			configuracaoBuilder = new ExConfiguracaoBuilder().setId(null)
					.setTipoPublicador(null)
					.setIdTpMov(configuracao.getMovimentacao())
					.setIdTpDoc(null)
					.setIdMod(idModelo)
					.setIdFormaDoc(null)
					.setIdTpFormaDoc(null)
					.setIdNivelAcesso(null)
					.setIdPapel(null)
					.setIdSituacao(configuracao.getIdSituacao())
					.setIdTpConfiguracao(configuracao.getTipoConfiguracao())								
					.setIdOrgaoUsu(idOrgao)
					.setLotacaoSel(null)
					.setCargoSel(null)
					.setFuncaoSel(null)
					.setPessoaSel(null)
					.setClassificacaoSel(null)
					.setIdOrgaoObjeto(null)
					.setPessoaObjetoSel(null)
					.setLotacaoObjetoSel(null)
					.setCargoObjetoSel(null)
					.setFuncaoObjetoSel(null)								
					.setIdTpLotacao(null);
			
			gravarConfiguracao(configuracao.getTipoConfiguracao(), configuracao.getIdSituacao(), configuracaoBuilder.construir());
		}						
	}
	
	private void gravarConfiguracaoUnidades(ExConfiguracaoDTO configuracao, Long idModelo) {
		ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder();		
		for (Long idUnidade : configuracao.getIdUnidades()) {
			configuracaoBuilder = new ExConfiguracaoBuilder().setId(null)
					.setTipoPublicador(null)
					.setIdTpMov(configuracao.getMovimentacao())
					.setIdTpDoc(null)
					.setIdMod(idModelo)
					.setIdFormaDoc(null)
					.setIdTpFormaDoc(null)
					.setIdNivelAcesso(null)
					.setIdPapel(null)
					.setIdSituacao(configuracao.getIdSituacao())
					.setIdTpConfiguracao(configuracao.getTipoConfiguracao())								
					.setIdOrgaoUsu(null)
					.setLotacaoSel(configuracao.getLotacaoSelecao(idUnidade))
					.setCargoSel(null)
					.setFuncaoSel(null)
					.setPessoaSel(null)
					.setClassificacaoSel(null)
					.setIdOrgaoObjeto(null)
					.setPessoaObjetoSel(null)
					.setLotacaoObjetoSel(null)
					.setCargoObjetoSel(null)
					.setFuncaoObjetoSel(null)								
					.setIdTpLotacao(null);	
			
			gravarConfiguracao(configuracao.getTipoConfiguracao(), configuracao.getIdSituacao(), configuracaoBuilder.construir());
		}				
	}
	
	private void gravarConfiguracaoCargos(ExConfiguracaoDTO configuracao, Long idModelo) {
		ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder();				
		for (Long idCargo : configuracao.getIdCargos()) {
			configuracaoBuilder = new ExConfiguracaoBuilder().setId(null)
					.setTipoPublicador(null)
					.setIdTpMov(configuracao.getMovimentacao())
					.setIdTpDoc(null)
					.setIdMod(idModelo)
					.setIdFormaDoc(null)
					.setIdTpFormaDoc(null)
					.setIdNivelAcesso(null)
					.setIdPapel(null)
					.setIdSituacao(configuracao.getIdSituacao())
					.setIdTpConfiguracao(configuracao.getTipoConfiguracao())								
					.setIdOrgaoUsu(null)
					.setLotacaoSel(null)
					.setCargoSel(configuracao.getCargoSelecao(idCargo))
					.setFuncaoSel(null)
					.setPessoaSel(null)
					.setClassificacaoSel(null)
					.setIdOrgaoObjeto(null)
					.setPessoaObjetoSel(null)
					.setLotacaoObjetoSel(null)
					.setCargoObjetoSel(null)
					.setFuncaoObjetoSel(null)								
					.setIdTpLotacao(null);		
			
			gravarConfiguracao(configuracao.getTipoConfiguracao(), configuracao.getIdSituacao(), configuracaoBuilder.construir());
		}			
	}
	
	private void gravarConfiguracaoFuncoes(ExConfiguracaoDTO configuracao, Long idModelo) {
		ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder();				
		for (Long idFuncao : configuracao.getIdFuncoes()) {
			configuracaoBuilder = new ExConfiguracaoBuilder().setId(null)
					.setTipoPublicador(null)
					.setIdTpMov(configuracao.getMovimentacao())
					.setIdTpDoc(null)
					.setIdMod(idModelo)
					.setIdFormaDoc(null)
					.setIdTpFormaDoc(null)
					.setIdNivelAcesso(null)
					.setIdPapel(null)
					.setIdSituacao(configuracao.getIdSituacao())
					.setIdTpConfiguracao(configuracao.getTipoConfiguracao())								
					.setIdOrgaoUsu(null)
					.setLotacaoSel(null)
					.setCargoSel(null)
					.setFuncaoSel(configuracao.getFuncaoSelecao(idFuncao))
					.setPessoaSel(null)
					.setClassificacaoSel(null)
					.setIdOrgaoObjeto(null)
					.setPessoaObjetoSel(null)
					.setLotacaoObjetoSel(null)
					.setCargoObjetoSel(null)
					.setFuncaoObjetoSel(null)								
					.setIdTpLotacao(null);			
			
			gravarConfiguracao(configuracao.getTipoConfiguracao(), configuracao.getIdSituacao(), configuracaoBuilder.construir());
		}				
	}
	
	private void gravarConfiguracaoPessoas(ExConfiguracaoDTO configuracao, Long idModelo) {
		ExConfiguracaoBuilder configuracaoBuilder = new ExConfiguracaoBuilder();				
		for (Long idPessoa : configuracao.getIdPessoas()) {
			configuracaoBuilder = new ExConfiguracaoBuilder().setId(null)
					.setTipoPublicador(null)
					.setIdTpMov(configuracao.getMovimentacao())
					.setIdTpDoc(null)
					.setIdMod(idModelo)
					.setIdFormaDoc(null)
					.setIdTpFormaDoc(null)
					.setIdNivelAcesso(null)
					.setIdPapel(null)
					.setIdSituacao(configuracao.getIdSituacao())
					.setIdTpConfiguracao(configuracao.getTipoConfiguracao())								
					.setIdOrgaoUsu(null)
					.setLotacaoSel(null)
					.setCargoSel(null)
					.setFuncaoSel(null)
					.setPessoaSel(configuracao.getPessoaSel(idPessoa))
					.setClassificacaoSel(null)
					.setIdOrgaoObjeto(null)
					.setPessoaObjetoSel(null)
					.setLotacaoObjetoSel(null)
					.setCargoObjetoSel(null)
					.setFuncaoObjetoSel(null)								
					.setIdTpLotacao(null);	
			
			gravarConfiguracao(configuracao.getTipoConfiguracao(), configuracao.getIdSituacao(), configuracaoBuilder.construir());
		}			
	}
	
	@SuppressWarnings("static-access")
	private void gravarConfiguracao(Integer idTpConfiguracao, Integer idSituacao, final ExConfiguracao config) {

		if (idTpConfiguracao == null || idTpConfiguracao == 0)
			throw new RegraNegocioException("Tipo de configuracao não informado");

		if (idSituacao == null || idSituacao == 0)
			throw new RegraNegocioException("Situação de Configuracao não informada");

		try {
			dao().iniciarTransacao();
			config.setHisDtIni(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().em().getTransaction().rollback();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
	}
}
