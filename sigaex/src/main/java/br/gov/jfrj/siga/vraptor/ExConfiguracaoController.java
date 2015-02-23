package br.gov.jfrj.siga.vraptor;

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

import com.opensymphony.xwork.Action;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoComparator;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.DpCargoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.vraptor.builder.ExConfiguracaoBuilder;

@Resource
public class ExConfiguracaoController extends ExController {

	private static final int ORGAO_INTEGRADO = 2;
	private static final String VERIFICADOR_ACESSO = "DOC:Módulo de Documentos;FE:Ferramentas;CFG:Configurações";
	
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

		if (c.getDpPessoa() != null) {
			DpPessoaSelecao pessoaSelecao = new DpPessoaSelecao();
			pessoaSelecao.buscarPorObjeto(c.getDpPessoa());
			result.include("pessoaSel", pessoaSelecao);
		}

		if (c.getLotacao() != null) {
			DpLotacaoSelecao lotacaoSelecao = new DpLotacaoSelecao();
			lotacaoSelecao.buscarPorObjeto(c.getLotacao());
			result.include("lotacaoSel", lotacaoSelecao );
		}

		if (c.getCargo() != null) {
			DpCargoSelecao cargoSelecao = new DpCargoSelecao();
			cargoSelecao.buscarPorObjeto(c.getCargo());
			result.include("cargoSel", cargoSelecao);
		}

		if (c.getFuncaoConfianca() != null) {
			DpFuncaoConfiancaSelecao funcaoConfiancaSelecao = new DpFuncaoConfiancaSelecao();
			funcaoConfiancaSelecao.buscarPorObjeto(c.getFuncaoConfianca());
			result.include("funcaoSel", funcaoConfiancaSelecao);
		}

		if (c.getExClassificacao() != null) {
			ExClassificacaoSelecao classificacaoSelecao = new ExClassificacaoSelecao();
			classificacaoSelecao.buscarPorObjeto(c.getExClassificacao());
			result.include("classificacaoSel", classificacaoSelecao);
		}

		if (c.getOrgaoObjeto() != null)
			result.include("idOrgaoObjeto", c.getOrgaoObjeto().getIdOrgaoUsu());
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
	
	@SuppressWarnings("unchecked")
	@Get("app/expediente/configuracao/gerenciar_publicacao_boletim")
	public void gerenciarPublicacaoBoletim() throws Exception {
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

		/*
		 * 
		 * 
		 * setIdFormaDoc(3);
		 * 
		 * setConfigsPorModelo(new HashMap<String, List<ExConfiguracao>>());
		 * 
		 * for (ExConfiguracao c : getPublicadores()){ ExModelo mod =
		 * c.getExModelo(); if (!getConfigsPorModelo().containsKey(mod))
		 * getConfigsPorModelo().put(mod.getNmMod(), new
		 * ArrayList<ExConfiguracao>()); getConfigsPorModelo().get(mod).add(c);
		 * }
		 */
		result.include("listaFormas", getListaFormas());
		result.include("listaModelosPorForma", getListaModelosPorForma(null));
		result.include("listaTipoPublicador", getListaTipoPublicador());
		result.include("listaSituacaoPodeNaoPode", getListaSituacaoPodeNaoPode());
		result.include("tipoPublicador", ORGAO_INTEGRADO);
		result.include("pessoaSel", new DpPessoaSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("itens", itens);
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
				nomeMod = MessageFormat.format("{0} -> {1}", 
					c.getExModelo()
					.getExFormaDocumento()
					.getDescrFormaDoc(), nomeMod);
			
			return nomeMod;
		} else if(c.getExFormaDocumento() != null)
			return c.getExFormaDocumento().getDescrFormaDoc();
		return "[Todos os modelos]";
	}

	private Set<ExConfiguracao> gerarPublicadores() {
		Set<ExConfiguracao> publicadores = new HashSet<ExConfiguracao>();
		TreeSet<CpConfiguracao> listaConfigs = getListaConfiguracao();
		
		for (CpConfiguracao cfg : listaConfigs) {
			if (cfg instanceof ExConfiguracao) {
				ExConfiguracao config = (ExConfiguracao) cfg;
				
				if (config.isAgendamentoPublicacaoBoletim() && config.podeAdicionarComoPublicador(getTitular(), getLotaTitular())) {
					publicadores.add(config);
				}
			}
		}
		return publicadores;
	}

	private TreeSet<CpConfiguracao> getListaConfiguracao() {
		TreeSet<CpConfiguracao> listaConfigs = Ex
				.getInstance()
				.getConf()
				.getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_MOVIMENTAR);
		
		if (listaConfigs == null)
			return new TreeSet<CpConfiguracao>();
		return listaConfigs;
	}

	private void validarPodeGerenciarBoletim() {
		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(),
				getLotaTitular(),
				CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_PUBLICACAO_BOLETIM))
			throw new AplicacaoException("Operação restrita");
	}
	
	private Set<ExFormaDocumento> getListaFormas() throws Exception {
		ExBL bl = Ex.getInstance().getBL();
		return bl.obterFormasDocumento(bl.obterListaModelos(null, false, null, false, null, null, false), null, null);
	}
	
	private Set<ExModelo> getListaModelosPorForma(Long idFormaDoc) throws Exception {
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
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}
	
	private Set<ExSituacaoConfiguracao> getListaSituacaoPodeNaoPode() throws Exception {
		HashSet<ExSituacaoConfiguracao> s = new HashSet<ExSituacaoConfiguracao>();
		s.add(ExDao.getInstance().consultar(1L, ExSituacaoConfiguracao.class, false));
		s.add(ExDao.getInstance().consultar(2L, ExSituacaoConfiguracao.class, false));
		return s;
	}
}