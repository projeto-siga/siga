package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.acesso.ConheceUsuario;
import br.gov.jfrj.siga.acesso.UsuarioAutenticado;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.DadosParaCriacaoDeUsuario;

@RequestScoped
public class SigaObjects implements ConheceUsuario {
	private static Log log = LogFactory.getLog(SigaObjects.class);

	public static Log getLog() {
		return SigaObjects.log;
	}

	private HttpServletRequest request;

	private DpPessoa cadastrante;

	private DpPessoa titular;

	private DpLotacao lotaTitular;

	private CpIdentidade identidadeCadastrante;

	private String mensagem;

	public SigaObjects() throws Exception {
		this(null);
	}

	@Inject
	public SigaObjects(HttpServletRequest request) throws Exception {
		super();
		this.request = request;
		carregaPerfil();
	}

	public void assertAcesso(String pathServico) throws AplicacaoException {
		String servico = "SIGA:Sistema Integrado de Gestão Administrativa;"
				+ pathServico;
		if (!Cp.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(getTitular(),
						getLotaTitular(), servico)) {

			String siglaUsuario = getTitular() == null ? "Indefinido"
					: getTitular().getSigla();
			String siglaLotacao = getLotaTitular() == null ? "Indefinida"
					: getLotaTitular().getSiglaCompleta();

			throw new AplicacaoException("Acesso negado. Serviço: '" + servico
					+ "' usuário: " + siglaUsuario + " lotação: "
					+ siglaLotacao);
		}
	}

	protected void carregaPerfil() throws Exception {
		if (ContextoPersistencia.getUserPrincipal() == null)
			return;

		// autenticação por formulário
		String principal = ContextoPersistencia.getUserPrincipal();
		
		CpIdentidade id = null;
		try {
			id = dao().consultaIdentidadeCadastrante(principal, true);
		} catch (Exception ex) {
			// swallow and test if id == null later
		}
		if (id == null) {
			// Criar o usuário externo no banco de dados e depois carregar a identidade
			//
			DadosParaCriacaoDeUsuario dados = ContextoPersistencia.getDadosParaCriacaoDeUsuario();
			// Verificar se temos dados para criação de usuário
			if (dados != null && dados.getNome() != null && dados.getEmail() != null && dados.getCpf() != null) {
				Boolean criarUsuarioExterno = Prop.getBool("/siga.usuario.externo.criar");
				String idOrgaoUsuarioExterno = Prop.get("/siga.usuario.externo.criar.no.id.orgao");
				String idCargoOrgaoExterno = Prop.get("/siga.usuario.externo.criar.no.id.cargo");
				String idLotacaoOrgaoExterno = Prop.get("/siga.usuario.externo.criar.no.id.lotacao");
				// Verificar se temos parâmetros habilitando a criação de usuário
				if (criarUsuarioExterno && idOrgaoUsuarioExterno != null && idCargoOrgaoExterno != null && idLotacaoOrgaoExterno != null) {
					boolean transactional = ContextoPersistencia.isTransactional();
					if (!transactional)
						ContextoPersistencia.upgradeToTransactional();
					new CpBL().criarUsuario(null, null, Long.parseLong(idOrgaoUsuarioExterno),Long.parseLong(idCargoOrgaoExterno), null, Long.parseLong(idLotacaoOrgaoExterno), dados.getNome(), null, dados.getCpf(), dados.getEmail(), null,
							null, null, null, null, "S");
					if (!transactional)
						ContextoPersistencia.flushTransactionAndDowngradeToNonTransactional();
				}
			}
		}
		UsuarioAutenticado.carregarUsuarioAutenticado(principal, this);
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public CpIdentidade getIdentidadeCadastrante() {
		return identidadeCadastrante;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public String getMensagem() {
		return mensagem;
	}

	public List<DpSubstituicao> getMeusTitulares() throws Exception {
		if (getCadastrante() == null)
			return null;
		DpSubstituicao dpSubstituicao = new DpSubstituicao();
		dpSubstituicao.setSubstituto(getCadastrante());
		dpSubstituicao.setLotaSubstituto(getCadastrante().getLotacao());
		List<DpSubstituicao> itens = dao().consultarSubstituicoesPermitidas(dpSubstituicao);

		return itens;
	}
	
	public List<DpVisualizacao> getMeusDelegados() throws Exception {
		if (getCadastrante() == null)
			return null;
		DpVisualizacao dpVisualizacao = new DpVisualizacao();
		dpVisualizacao.setDelegado(getCadastrante());
		List<DpVisualizacao> itens = dao().consultarVisualizacoesPermitidas(dpVisualizacao);

		return itens;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public void setIdentidadeCadastrante(CpIdentidade identidadeCadastrante) {
		this.identidadeCadastrante = identidadeCadastrante;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public DpPessoa daoPes(long id) {
		return dao().consultar(id, DpPessoa.class, false);
	}

	public DpPessoa daoPes(String sigla) {
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setSigla(sigla);
		return (DpPessoa) dao().consultarPorSigla(flt);
	}

	public DpLotacao daoLot(long id) {
		return dao().consultar(id, DpLotacao.class, false);
	}

	public DpLotacao daoLot(String sigla) {
		DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setSigla(sigla);
		// flt.setSiglaCompleta(sigla);
		return (DpLotacao) dao().consultarPorSigla(flt);
	}

	public CpOrgaoUsuario daoOU(String sigla) {
		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(sigla);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) CpDao.getInstance()
				.consultarPorSigla(fltOrgao);
		return orgaoUsuario;
	}

	public CpDao dao() {
		return CpDao.getInstance();
	}

}
