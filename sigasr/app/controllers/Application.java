package controllers;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import models.SrAndamento;
import models.SrArquivo;
import models.SrAtributo;
import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrEstado;
import models.SrFormaAcompanhamento;
import models.SrGravidade;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSolicitacao;
import models.SrTendencia;
import models.SrTipoAtributo;
import models.SrUrgencia;

import org.hibernate.Session;

import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http;
import util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class Application extends Controller {

	@Before
	static void addDefaultsSempre() throws Exception {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		CpDao.getInstance(playSession);
		SrConfiguracaoBL.get().limparCache(null);
	}

	@Before(unless = { "exibirAtendente", "exibirAtributos",
			"exibirLocalERamal", "exibirItemConfiguracao", "exibirServico" })
	static void addDefaults() throws Exception {
		try {
			
			Logger.info("Siga-SR info: " + getBaseSiga());

			// Obter cabeçalho e rodapé do Siga
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			String popup = params.get("popup");
			if (popup == null
					|| (!popup.equals("true") && !popup.equals("false")))
				popup = "false";
			String paginaVazia = ConexaoHTTP.get(getBaseSiga()
					+ "/pagina_vazia.action?popup=" + popup, atributos);
			String[] pageText = paginaVazia.split("<!-- insert body -->");
			String[] cabecalho = pageText[0].split("<!-- insert menu -->");
			renderArgs.put("_cabecalho", cabecalho);
			renderArgs.put("_rodape", pageText[1]);

			// Obter usuário logado
			String[] IDs = ConexaoHTTP.get(
					getBaseSiga() + "/usuario_autenticado.action", atributos)
					.split(";");

			Logger.info("Siga-SR info: " + IDs.toString());
			
			renderArgs.put("cadastrante",
					JPA.em().find(DpPessoa.class, Long.parseLong(IDs[0])));

			if (IDs[1] != null && !IDs[1].equals(""))
				renderArgs.put("lotaCadastrante",
						JPA.em().find(DpLotacao.class, Long.parseLong(IDs[1])));

			if (IDs[2] != null && !IDs[2].equals(""))
				renderArgs.put("titular",
						JPA.em().find(DpPessoa.class, Long.parseLong(IDs[2])));

			if (IDs[3] != null && !IDs[3].equals(""))
				renderArgs.put("lotaTitular",
						JPA.em().find(DpLotacao.class, Long.parseLong(IDs[3])));

			assertAcesso("");

		} catch (ArrayIndexOutOfBoundsException aioob) {
			// Edson: Quando as informações não puderam ser obtidas do Siga,
			// manda para a página de login
			redirect("/siga");
		} catch (Exception e) {
			// Edson: Este bloco não deveria ser necessário, mas o Play,
			// pelo visto, não joga automaticamente para o
			// método @Catch as exceções geradas no @Before
			catchExceptions(e);
		}

		try {
			assertAcesso("ADM:Administrar");
			renderArgs.put("exibirMenuAdministrar", true);
		} catch (Exception e) {
			renderArgs.put("exibirMenuAdministrar", false);
		}

	}

	@Catch
	public static void catchExceptions(Exception e) {
		// MailUtils.sendErrorMail(e);
		if (getCadastrante() != null)
			Logger.error("Erro Siga-SR; Pessoa: " + getCadastrante().getSigla()
					+ "; Lotação: " + getLotaTitular().getSigla(), e);
		e.printStackTrace();
		error(e.getMessage());
	}

	// Edson: o objetivo deste método é apenas impedir que os links nas páginas
	// montadas pelo Siga sejam repassados ao Play com caminhos
	// relativos, o que causa problemas quando a aplicação está rodando
	// em porta diferente (9000)

	static DpPessoa getCadastrante() {
		return (DpPessoa) renderArgs.get("cadastrante");
	}

	static DpLotacao getLotaTitular() {
		return (DpLotacao) renderArgs.get("lotaTitular");
	}

	static String getBaseSiga() {
		return "http://"
				+ SigaBaseProperties.getString(SigaBaseProperties
						.getString("ambiente") + ".servidor") + ":8080/siga";
	}

	private static void assertAcesso(String pathServico) throws Exception {
		String servico = "SIGA:Sistema Integrado de Gestão Administrativa;SR:Módulo de Serviços;"
				+ pathServico;
		if (!Cp.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(getCadastrante(),
						getLotaTitular(), servico))
			throw new Exception("Acesso negado. Serviço: '" + servico
					+ "' usuário: " + getCadastrante().getSigla()
					+ " lotação: " + getLotaTitular().getSiglaCompleta());
	}

	public static void index() throws Exception {
		editar(null);
	}

	public static void gadget() {
		Query query = JPA.em().createNamedQuery("contarSrMarcas");
		query.setParameter("idPessoaIni", getCadastrante().getIdInicial());
		query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
		List contagens = query.getResultList();
		render(contagens);
	}

	public static void editar(Long id) throws Exception {
		SrSolicitacao solicitacao;
		if (id == null) {
			solicitacao = new SrSolicitacao();
			solicitacao.solicitante = getCadastrante();
		} else
			solicitacao = SrSolicitacao.findById(id);

		formEditar(solicitacao.deduzirLocalERamal());
	}

	public static void exibirLocalERamal(SrSolicitacao solicitacao)
			throws Exception {
		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();
		render(solicitacao.deduzirLocalERamal(), locais);
	}

	public static void exibirAtributos(SrSolicitacao solicitacao)
			throws Exception {
		render(solicitacao);
	}

	public static void exibirItemConfiguracao(SrSolicitacao solicitacao)
			throws Exception {
		if (!SrItemConfiguracao.listarPorPessoa(solicitacao.solicitante)
				.contains(solicitacao.itemConfiguracao))
			solicitacao.itemConfiguracao = null;

		solicitacao.servico = null;

		render(solicitacao);
	}

	public static void exibirServico(SrSolicitacao solicitacao)
			throws Exception {
		List<SrServico> servicos = SrServico.listarPorPessoaEItem(
				solicitacao.solicitante, solicitacao.itemConfiguracao);
		if (solicitacao.servico == null
				|| !servicos.contains(solicitacao.servico)) {
			if (servicos.size() > 0)
				solicitacao.servico = servicos.get(0);
			else
				solicitacao.servico = null;
		}

		render(solicitacao, servicos);
	}

	private static void formEditar(SrSolicitacao solicitacao) throws Exception {

		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();
		boolean abrirFechando = solicitacao.podeAbrirJaFechando(
				getLotaTitular(), getCadastrante());
		boolean priorizar = solicitacao.podePriorizar(getLotaTitular(),
				getCadastrante());

		List<SrServico> servicos = SrServico.listarPorPessoaEItem(
				solicitacao.solicitante, solicitacao.itemConfiguracao);
		if (solicitacao.servico == null
				|| !servicos.contains(solicitacao.servico)) {
			if (servicos.size() > 0)
				solicitacao.servico = servicos.get(0);
			else
				solicitacao.servico = null;
		}

		render("@editar", solicitacao, formasAcompanhamento, gravidades,
				urgencias, tendencias, priorizar, abrirFechando, locais,
				servicos);
	}

	private static void validarFormEditar(SrSolicitacao solicitacao)
			throws Exception {

		if (solicitacao.itemConfiguracao == null) {
			validation.addError("solicitacao.itemConfiguracao",
					"Item não informado");
		}

		if (solicitacao.servico == null) {
			validation.addError("solicitacao.servico", "Serviço não informado");
		}

		if (solicitacao.descrSolicitacao == null
				|| solicitacao.descrSolicitacao.trim().equals("")) {
			validation.addError("solicitacao.descrSolicitacao",
					"Descrição não informada");
		}

		HashMap<Long, Boolean> obrigatorio = solicitacao
				.getObrigatoriedadeTiposAtributoAssociados();
		for (SrAtributo att : solicitacao.getAtributoSet()) {
			if (att.valorAtributo.trim().equals("")
					&& obrigatorio.get(att.tipoAtributo.idTipoAtributo))
				validation.addError("solicitacao.atributoMap["
						+ att.tipoAtributo.idTipoAtributo + "]",
						att.tipoAtributo.nomeTipoAtributo + " não informado");
		}

		if (validation.hasErrors()) {
			formEditar(solicitacao);
		}
	}

	public static void gravar(SrSolicitacao solicitacao) throws Exception {
		validarFormEditar(solicitacao);
		solicitacao.salvar(getCadastrante(), getLotaTitular());
		Long id = solicitacao.idSolicitacao;
		exibir(id);
	}

	public static void listar(SrSolicitacaoFiltro filtro) throws Exception {

		List<SrSolicitacao> listaSolicitacao;

		if (filtro.pesquisar)
			listaSolicitacao = filtro.buscar();
		else
			listaSolicitacao = new ArrayList<SrSolicitacao>();

		// Montando o filtro...
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();

		render(listaSolicitacao, urgencias, tendencias, gravidades, tipos,
				marcadores, filtro);
	}

	public static void exibir(Long id) throws Exception {
		// antes: 8 queries
		SrSolicitacao solicitacao = SrSolicitacao.findById(id); // 3 queries
		SrAndamento andamento = new SrAndamento(solicitacao); // 1query
		andamento.deduzirProxAtendente(); // 318 queries

		boolean criarFilha = solicitacao.podeCriarFilha(getLotaTitular(),
				getCadastrante());
		boolean desfazerAndamento = solicitacao.podeDesfazerAndamento(
				getLotaTitular(), getCadastrante());
		boolean editar = solicitacao.podeEditar(getLotaTitular(),
				getCadastrante());
		boolean movimentarPlenamente = solicitacao.estaCom(getLotaTitular(),
				getCadastrante());

		List<SrEstado> estados = solicitacao.getEstadosSelecionaveis();

		render(solicitacao, editar, desfazerAndamento, movimentarPlenamente,
				andamento, criarFilha, estados);
	}

	public static void selecionar(String sigla) throws Exception {
		SrSolicitacao sel = new SrSolicitacao();
		sel.cadastrante = getCadastrante();
		sel = (SrSolicitacao) sel.selecionar(sigla);
		render("@selecionar", sel);
	}

	public static void exibirAtendente(SrAndamento andamento) throws Exception {
		render(andamento.deduzirProxAtendente());
	}

	public static void baixar(Long idArquivo) {
		SrArquivo arq = SrArquivo.findById(idArquivo);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
	}

	public static void andamento(SrAndamento andamento) throws Exception {
		andamento.solicitacao.darAndamento(andamento, getCadastrante(),
				getLotaTitular());
		Long id = andamento.solicitacao.idSolicitacao;
		exibir(id);
	}

	public static void desfazerUltimoAndamento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.desfazerUltimoAndamento(getCadastrante(), getLotaTitular());
		exibir(id);
	}

	public static void criarFilha(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		SrSolicitacao filha = sol.criarFilhaSemSalvar();
		formEditar(filha);
	}

	public static void listarDesignacao() throws Exception {
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes();
		render(designacoes);
	}

	public static void editarDesignacao(Long id) {
		List<CpComplexo> orgaos = JPA.em().createQuery("from CpOrgaoUsuario")
				.getResultList();
		SrConfiguracao designacao = new SrConfiguracao();
		if (id != null)
			designacao = JPA.em().find(SrConfiguracao.class, id);
		render(designacao, orgaos);
	}

	public static void gravarDesignacao(SrConfiguracao designacao)
			throws Exception {
		designacao.salvarComoDesignacao();
		listarDesignacao();
	}

	public static void desativarDesignacao(Long id) throws Exception {
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.finalizar();
		listarDesignacao();
	}

	public static void listarAssociacao() {
		List<List<SrConfiguracao>> listasAssociacoes = SrConfiguracao
				.listarAssociacoesTipoAtributo();
		render(listasAssociacoes);
	}

	public static void editarAssociacao(Long id) {
		SrConfiguracao associacao = new SrConfiguracao();
		if (id != null)
			associacao = (SrConfiguracao) JPA.em()
					.find(SrConfiguracao.class, id).getConfiguracaoAtual();
		List<SrTipoAtributo> tiposAtributo = SrTipoAtributo.listar();
		render(associacao, tiposAtributo);
	}

	public static void gravarAssociacao(SrConfiguracao associacao)
			throws Exception {
		associacao.salvarComoAssociacaoTipoAtributo();
		listarAssociacao();
	}

	public static void desativarAssociacao(Long id) throws Exception {
		SrConfiguracao associacao = JPA.em().find(SrConfiguracao.class, id);
		associacao.finalizar();
		listarAssociacao();
	}

	public static void listarItem() {
		List<SrItemConfiguracao> itens = SrItemConfiguracao.listar();
		render(itens);
	}

	public static void editarItem(Long id) {
		SrItemConfiguracao item = new SrItemConfiguracao();
		if (id != null)
			item = SrItemConfiguracao.findById(id);
		render(item);
	}

	public static void gravarItem(SrItemConfiguracao item) throws Exception {
		item.salvar();
		listarItem();
	}

	public static void desativarItem(Long id) throws Exception {
		SrItemConfiguracao item = SrItemConfiguracao.findById(id);
		item.finalizar();
		listarItem();
	}

	public static void selecionarItem(String sigla, Long pessoa)
			throws Exception {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla,
				pessoa != null ? JPA.em().find(DpPessoa.class, pessoa) : null);
		render("@selecionar", sel);
	}

	public static void buscarItem(String sigla, String nome,
			SrItemConfiguracao filtro, Long pessoa) {

		List<SrItemConfiguracao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(pessoa != null ? JPA.em().find(
					DpPessoa.class, pessoa) : null);
		} catch (Exception e) {
			itens = new ArrayList<SrItemConfiguracao>();
		}

		render(itens, filtro, nome, pessoa);
	}

	public static void listarTipoAtributo() {
		List<SrTipoAtributo> atts = SrTipoAtributo.listar();
		render(atts);
	}

	public static void editarTipoAtributo(Long id) {
		SrTipoAtributo att = new SrTipoAtributo();
		if (id != null)
			att = SrTipoAtributo.findById(id);
		render(att);
	}

	public static void gravarTipoAtributo(SrTipoAtributo att) throws Exception {
		att.salvar();
		listarTipoAtributo();
	}

	public static void desativarTipoAtributo(Long id) throws Exception {
		SrTipoAtributo item = SrTipoAtributo.findById(id);
		item.finalizar();
		listarTipoAtributo();
	}

	public static void listarServico() {
		List<SrServico> servicos = SrServico.listar();
		render(servicos);
	}

	public static void editarServico(Long id) {
		SrServico servico = new SrServico();
		if (id != null)
			servico = SrServico.findById(id);
		render(servico);
	}

	public static void gravarServico(SrServico servico) throws Exception {
		servico.salvar();
		listarServico();
	}

	public static void desativarServico(Long id) throws Exception {
		SrServico servico = SrServico.findById(id);
		servico.finalizar();
		listarServico();
	}

	public static void selecionarServico(String sigla, Long pessoa, Long item)
			throws Exception {
		SrServico sel = new SrServico().selecionar(
				sigla,
				pessoa != null ? JPA.em().find(DpPessoa.class, pessoa) : null,
				item != null ? (SrItemConfiguracao) SrItemConfiguracao
						.findById(item) : null);
		render("@selecionar", sel);
	}

	public static void buscarServico(String sigla, String nome,
			SrServico filtro, Long pessoa, Long item) {
		List<SrServico> itens = null;
		try {
			if (filtro == null)
				filtro = new SrServico();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(
					pessoa != null ? JPA.em().find(DpPessoa.class, pessoa)
							: null,
					item != null ? (SrItemConfiguracao) SrItemConfiguracao
							.findById(item) : null);
		} catch (Exception e) {
			itens = new ArrayList<SrServico>();
		}

		render(itens, filtro, nome, pessoa, item);
	}

	public static void selecionarSiga(String sigla, String tipo, String nome)
			throws Exception {
		redirect("/siga/" + tipo + "/selecionar.action?" + "propriedade="
				+ tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSiga(String sigla, String tipo, String nome)
			throws Exception {
		redirect("/siga/" + tipo + "/buscar.action?" + "propriedade=" + tipo
				+ nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

}
