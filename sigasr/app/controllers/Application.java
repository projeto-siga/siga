package controllers;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import models.SrAndamento;
import models.SrArquivo;
import models.SrConfiguracao;
import models.SrEstado;
import models.SrFormaAcompanhamento;
import models.SrGravidade;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSolicitacao;
import models.SrTendencia;
import models.SrUrgencia;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class Application extends Controller {

	static void prepararCpDao() {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		CpDao.getInstance(playSession);
	}

	@Before
	static void addDefaults() {

		if (request.url.contains("proxy"))
			return;

		try {
			renderArgs.put("_base", "http://10.34.5.90:8080");

			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			String[] IDs = ConexaoHTTP.get(
					"http://localhost:8080/siga/usuario_autenticado.action",
					atributos).split(";");

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

			prepararCpDao();

		} catch (Exception e) {
			redirect("http://localhost:8080/siga");
		}

	}

	static DpPessoa getCadastrante() {
		return (DpPessoa) renderArgs.get("cadastrante");
	}

	static DpLotacao getLotaTitular() {
		return (DpLotacao) renderArgs.get("lotaTitular");
	}

	public static void index() {
		editar(null);
	}

	public static void gadget() {
		Query query = JPA.em().createNamedQuery("contarSrMarcas");
		query.setParameter("idPessoaIni", getCadastrante().getIdInicial());
		query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
		List contagens = query.getResultList();
		render(contagens);
	}

	public static void editar(Long id) {
		SrSolicitacao solicitacao;
		if (id == null)
			solicitacao = new SrSolicitacao();
		else
			solicitacao = SrSolicitacao.findById(id);

		solicitacao.solicitante = getCadastrante();
		formEditar(solicitacao);
	}

	private static void formEditar(SrSolicitacao solicitacao) {

		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.all()
				.fetch();
		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		boolean abrirFechando = solicitacao.podeAbrirJaFechando(
				getLotaTitular(), getCadastrante());
		boolean priorizar = solicitacao.podePriorizar(getLotaTitular(),
				getCadastrante());

		render("@editar", solicitacao, itensConfiguracao, formasAcompanhamento,
				gravidades, urgencias, tendencias, solicitacao, priorizar,
				abrirFechando);
	}

	private static void validarFormEditar(SrSolicitacao solicitacao) {

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

		if (validation.hasErrors()) {
			formEditar(solicitacao);
		}
	}

	public static void gravar(SrSolicitacao solicitacao) throws Exception {

		validarFormEditar(solicitacao);

		solicitacao.salvar(getCadastrante(), getLotaTitular());

		listar(null);

	}

	public static void listar(SrSolicitacaoFiltro filtro) {

		List<SrSolicitacao> listaSolicitacao = filtro.buscar();

		// Montando o filtro...
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();

		if (filtro == null)
			filtro = new SrSolicitacaoFiltro();

		render(listaSolicitacao, urgencias, tendencias, gravidades, tipos,
				marcadores, filtro);
	}

	public static void exibir(Long id) {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		SrAndamento andamento = new SrAndamento();
		andamento.estado = solicitacao.getUltimoAndamento().estado;

		boolean trocarAtendente = solicitacao.podeTrocarAtendente(
				getLotaTitular(), getCadastrante());
		boolean criarFilha = solicitacao.podeCriarFilha(getLotaTitular(),
				getCadastrante());
		boolean desfazerAndamento = solicitacao.podeDesfazerAndamento(
				getLotaTitular(), getCadastrante());
		boolean editar = solicitacao.podeEditar(getLotaTitular(),
				getCadastrante());
		boolean trocarSituacao = solicitacao.podeTrocarSituacao(
				getLotaTitular(), getCadastrante());

		List<SrEstado> estados = solicitacao.getEstadosSelecionaveis();

		render(solicitacao, editar, desfazerAndamento, trocarSituacao,
				andamento, trocarAtendente, criarFilha, estados);

	}

	public static void baixar(Long idArquivo) {
		SrArquivo arq = SrArquivo.findById(idArquivo);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
	}

	public static void andamento(SrAndamento andamento) throws Exception {

		andamento.salvar(getCadastrante(), getLotaTitular());

		exibir(andamento.solicitacao.idSolicitacao);

	}

	public static void desfazerUltimoAndamento(Long id) {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		exibir(id);
	}

	public static void criarFilha(Long id) {

		SrSolicitacao pai = SrSolicitacao.findById(id);
		SrSolicitacao solicitacao = new SrSolicitacao();
		// SrDao.getInstance().copiar(solicitacao, pai);
		solicitacao.idSolicitacao = null;
		solicitacao.solicitacaoPai = pai;
		solicitacao.numSequencia = pai.getNumeroProximaFilha();

		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.all()
				.fetch();
		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();

		render("Application/editar.html", solicitacao, itensConfiguracao,
				formasAcompanhamento, gravidades, urgencias, tendencias,
				solicitacao);

	}

	public static void listarDesignacao() {
		List<SrConfiguracao> designacoes = JPA
				.em()
				.createQuery(
						"from SrConfiguracao where cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO
								+ " and hisDtFim is null", SrConfiguracao.class)
				.getResultList();

		render(designacoes);
	}

	public static void editarDesignacao(Long id) {

		SrConfiguracao designacao;

		if (id != null)
			designacao = JPA.em().find(SrConfiguracao.class, id);
		else
			designacao = new SrConfiguracao();

		render(designacao);
	}

	public static void gravarDesignacao(SrConfiguracao designacao) {

		// Plim
		// try {
		designacao.setCpTipoConfiguracao(JPA.em().find(
				CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));

		// dao().salvar(designacao, "RJ13285");
		// } catch (AplicacaoException ae) {
		int a = 0;
		// }

		listarDesignacao();
	}

	public static void listarItem() {
		List<SrItemConfiguracao> itens = SrItemConfiguracao.find(
				"byHisDtFimIsNull").fetch();
		render(itens);
	}

	public static void editarItem(Long id) {
		SrItemConfiguracao item;
		if (id != null)
			item = SrItemConfiguracao.findById(id);
		else
			item = new SrItemConfiguracao();

		render(item);
	}

	public static void gravarItem(SrItemConfiguracao item) {
		// try {
		item.save();
		// dao().salvar(item, "RJ13285");
		// } catch (AplicacaoException ae) {
		// int a = 0;
		// }
		listarItem();
	}

	public static void desativarItem(Long id) {
		// Plim
		// try {
		SrItemConfiguracao item = SrItemConfiguracao.findById(id);
		// dao().finalizar(item);
		// } catch (AplicacaoException ae) {
		int a = 0;
		// }
		listarItem();
	}

	public static void selecionarItem(String sigla) {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla);
		render("Application/selecionar.html", sel);
	}

	public static void buscarItem(String sigla, String nome,
			SrItemConfiguracao filtro) {
		List<SrItemConfiguracao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrItemConfiguracao>();
		}

		render(itens, filtro, nome);
	}

	public static void listarServico() {
		List<SrServico> servicos = SrServico.find("byHisDtFimIsNull").fetch();
		render(servicos);
	}

	public static void editarServico(Long id) {
		SrServico servico;
		if (id != null)
			servico = SrServico.findById(id);
		else
			servico = new SrServico();

		render(servico);
	}

	public static void gravarServico(SrServico servico) {
		// Plim
		// try {
		// dao().salvar(servico, "RJ13285");
		// } catch (AplicacaoException ae) {
		int a = 0;
		// }
		listarServico();
	}

	public static void desativarServico(Long id) {
		SrServico servico = SrServico.findById(id);
		servico.delete();

		listarServico();
	}

	public static void selecionarServico(String sigla) {
		SrServico sel = new SrServico().selecionar(sigla);
		render("Application/selecionar.html", sel);
	}

	public static void buscarServico(String sigla, String nome, SrServico filtro) {
		List<SrServico> itens = null;

		try {
			if (filtro == null)
				filtro = new SrServico();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrServico>();
		}

		render(itens, filtro, nome);
	}

	public static void proxy(String url) throws Exception {
		HashMap<String, String> atributos = new HashMap<String, String>();
		for (Http.Header h : request.headers.values())
			atributos.put(h.name, h.value());

		renderHtml(ConexaoHTTP.get(url, atributos));
	}

	public static void selecionarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("http://localhost:8080/siga/" + tipo + "/selecionar.action?"
				+ "propriedade=" + tipo + nome + "&sigla="
				+ URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSiga(String sigla, String tipo, String nome)
			throws Exception {
		proxy("http://localhost:8080/siga/" + tipo + "/buscar.action?"
				+ "propriedade=" + tipo + nome + "&sigla="
				+ URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSigaFromPopup(String tipo) throws Exception {
		String paramString = "?";
		for (String s : request.params.all().keySet())
			if (!s.equals("body"))
				paramString += s + "="
						+ URLEncoder.encode(request.params.get(s), "UTF-8")
						+ "&";
		proxy("http://localhost:8080/siga/" + tipo + "/buscar.action"
				+ paramString);
	}

}