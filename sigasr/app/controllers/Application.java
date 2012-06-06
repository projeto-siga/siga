package controllers;

import play.*;
import play.data.binding.As;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.persistence.Query;

import org.apache.commons.io.IOUtils;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import models.*;

public class Application extends Controller {

	// @Before(only = { "buscarSrServico" })
	// static void parseParams() {
	// String[] items = params.getAll("f");
	// params.remove("items");
	// for (int i = 0; i < items.length; i++) {
	// Item item = Item.findById(Long.parseLong(items[i]));
	// params.put("items[" + i + "].id", items[i]);
	// params.put("items[" + i + "].propertyA", item.propertyA);
	// params.put("items[" + i + "].propertyB", item.propertyB);
	// }
	// }

	@Before
	static void addDefaults() {
		try {
			renderArgs.put("_base", "http://10.34.5.90:8080");
			URLConnection conn = new URL("http://localhost:8080")
					.openConnection();
			CookieManager manager = new CookieManager();
			CookieHandler.setDefault(new CookieManager());
			List l = manager.getCookieStore().getCookies();
			int a = 0;

		} catch (IOException ioe) {

		}
	}

	public static void index() {
		cadastrar();
	}

	public static void cadastrar() {
		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.all()
				.fetch();
		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		SrSolicitacao solicitacao = new SrSolicitacao();

		// Enquanto não tem login...
		DpPessoa pessoaSolicitanteSel = JPA.em().find(DpPessoa.class, 10374L);

		render(itensConfiguracao, formasAcompanhamento, gravidades, urgencias,
				tendencias, solicitacao, pessoaSolicitanteSel);
	}

	public static void gravar(SrSolicitacao solicitacao, File arquivo,
			@As(binder = DpPessoaBinder.class) DpPessoa pessoaSolicitante,
			SrItemConfiguracao SrItemConfiguracaoItemSel,
			SrServico SrServicoServicoSel) throws Exception {

		if (arquivo != null) {
			try {
				solicitacao.arquivo = new SrArquivo();
				solicitacao.arquivo.nomeArquivo = arquivo.getName();
				solicitacao.arquivo.blob = IOUtils
						.toByteArray(new FileInputStream(arquivo));
				solicitacao.arquivo.mime = new javax.activation.MimetypesFileTypeMap()
						.getContentType(arquivo);
			} catch (IOException ioe) {

			}
		}

		// Enquanto não tem login...
		DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);

		solicitacao.cadastrante = eeh;
		solicitacao.lotaCadastrante = eeh.getLotacao();
		solicitacao.solicitante = pessoaSolicitante;
		solicitacao.itemConfiguracao = SrItemConfiguracaoItemSel;
		solicitacao.servico = SrServicoServicoSel;
		solicitacao.dtReg = new Date();
		solicitacao.criar();

		listar(null, null, null, null, null, null, null, null, null);

	}

	public static void listar(
			SrSolicitacaoFiltro filtro,
			@As(binder = DpPessoaBinder.class) DpPessoa pessoaSolicitante,
			@As(binder = DpPessoaBinder.class) DpPessoa pessoaCadastrante,
			@As(binder = DpPessoaBinder.class) DpPessoa pessoaAtendente,
			@As(binder = DpLotacaoBinder.class) DpLotacao lotacaoLotaSolicitante,
			@As(binder = DpLotacaoBinder.class) DpLotacao lotacaoLotaCadastrante,
			@As(binder = DpLotacaoBinder.class) DpLotacao lotacaoLotaAtendente,
			SrItemConfiguracao SrItemConfiguracaoItemSel,
			SrServico SrServicoServicoSel) {

		filtro.cadastrante = pessoaCadastrante;
		filtro.atendente = pessoaAtendente;
		filtro.solicitante = pessoaSolicitante;

		filtro.lotaAtendente = lotacaoLotaAtendente;
		filtro.lotaCadastrante = lotacaoLotaCadastrante;
		filtro.lotaSolicitante = lotacaoLotaSolicitante;

		filtro.itemConfiguracao = SrItemConfiguracaoItemSel;
		filtro.servico = SrServicoServicoSel;

		List<SrSolicitacao> listaSolicitacao = filtro.buscar();

		// Montando o filtro...
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		String[] tipos = new String[] { "Pessoa", "LotaÃ§Ã£o" };
		List<CpMarcador> marcadores = JPA
				.em()
				.createQuery(
						"select distinct cpMarcador.descrMarcador from SrMarca")
				.getResultList();

		if (filtro == null)
			filtro = new SrSolicitacaoFiltro();

		renderArgs.put("pessoaAtendenteSel", pessoaAtendente);
		renderArgs.put("lotacaoLotaAtendenteSel", lotacaoLotaAtendente);
		renderArgs.put("pessoaCadastranteSel", pessoaCadastrante);
		renderArgs.put("lotacaoLotaCadastranteSel", lotacaoLotaCadastrante);
		renderArgs.put("pessoaSolicitanteSel", pessoaSolicitante);
		renderArgs.put("lotacaoLotaSolicitanteSel", lotacaoLotaSolicitante);
		render(listaSolicitacao, urgencias, tendencias, gravidades, tipos,
				marcadores, filtro, SrItemConfiguracaoItemSel,
				SrServicoServicoSel);
	}

	public static void exibir(Long id) {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		SrAndamento andamento = new SrAndamento();
		andamento.estado = solicitacao.getUltimoAndamento().estado;
		DpLotacao lotacaoLotaAtendenteSel = solicitacao.getUltimoAndamento().lotaAtendente;
		DpPessoa pessoaAtendenteSel = solicitacao.getUltimoAndamento().atendente;

		// Enquanto não tem login...
		DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);
		boolean exibirAtendente = solicitacao.podeAlterarAtendente(
				eeh.getLotacao(), eeh);

		SrEstado[] estados = SrEstado.values();

		render(solicitacao, andamento, exibirAtendente, pessoaAtendenteSel,
				lotacaoLotaAtendenteSel, estados);
	}

	public static void baixarAnexo(Long idArquivo) {
		SrArquivo arq = SrArquivo.findById(idArquivo);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
	}

	public static void andamento(Long id, SrAndamento andamento,
			@As(binder = DpPessoaBinder.class) DpPessoa pessoaAtendente,
			@As(binder = DpLotacaoBinder.class) DpLotacao lotacaoLotaAtendente,
			File arquivo) {

		SrSolicitacao sol = SrSolicitacao.findById(id);
		andamento.atendente = pessoaAtendente;
		andamento.lotaAtendente = lotacaoLotaAtendente;

		if (arquivo != null) {
			try {
				andamento.arquivo = new SrArquivo();
				andamento.arquivo.nomeArquivo = arquivo.getName();
				andamento.arquivo.blob = IOUtils
						.toByteArray(new FileInputStream(arquivo));
				andamento.arquivo.mime = new javax.activation.MimetypesFileTypeMap()
						.getContentType(arquivo);
			} catch (IOException ioe) {

			}
		}

		sol.darAndamento(andamento);

		exibir(id);

	}

	public static void proxy(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		for (Http.Header h : request.headers.values())
			conn.setRequestProperty(h.name, h.value());

		// BufferedReader in = new BufferedReader(new InputStreamReader(u
		// .openStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		renderHtml(sb.toString());

	}

	public static void selecionar(String sigla, String tipo) {
		SrSelecionavel sel = null;
		try {
			sel = (SrSelecionavel) Class.forName("models." + tipo)
					.newInstance();
			sel = sel.selecionar(sigla);
		} catch (IllegalAccessException e) {

		} catch (ClassNotFoundException cnfe) {

		} catch (InstantiationException ie) {
			int a = 0;
		}
		render(sel);
	}

	public static void buscar(String sigla, String tipo, String nome,
			SrServico filtro) {
		render();
	}

	public static void buscarSrServico(String sigla, String tipo, String nome,
			SrServico filtro, SrServico categoria) {
		List<SrSelecionavel> itens;

		try {
			if (filtro == null)
				filtro = new SrServico();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			else if (categoria != null && categoria.siglaServico != null
					&& !categoria.siglaServico.trim().equals(""))
				filtro.siglaServico = categoria.siglaServico;

			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrSelecionavel>();
		}

		categoria = (SrServico) filtro.obterPorHierarquia(1);
		if (categoria == null)
			categoria = new SrServico();
		List<SrCodigoHierarquicoSuporte> opcoesNivel1 = filtro
				.obterOpcoesPorHierarquia(1);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria, opcoesNivel1);
	}

	public static void buscarSrItemConfiguracao(String sigla, String tipo,
			String nome, SrItemConfiguracao filtro,
			SrItemConfiguracao categoria1, SrItemConfiguracao categoria2,
			SrItemConfiguracao categoria3, String alterou) {
		List<SrSelecionavel> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			else {
				String nivelAlterado = params.get("alterou");
				if (nivelAlterado != null)
					if (nivelAlterado.equals("3"))
						filtro.siglaItemConfiguracao = categoria3.siglaItemConfiguracao;
					else if (nivelAlterado.equals("2"))
						filtro.siglaItemConfiguracao = categoria2.siglaItemConfiguracao;
					else if (nivelAlterado.equals("1"))
						filtro.siglaItemConfiguracao = categoria1.siglaItemConfiguracao;
			}
			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrSelecionavel>();
		}

		categoria1 = (SrItemConfiguracao) filtro.obterPorHierarquia(1);
		if (categoria1 == null)
			categoria1 = new SrItemConfiguracao();
		categoria2 = (SrItemConfiguracao) filtro.obterPorHierarquia(2);
		if (categoria2 == null)
			categoria2 = new SrItemConfiguracao();
		categoria3 = (SrItemConfiguracao) filtro.obterPorHierarquia(3);
		if (categoria3 == null)
			categoria3 = new SrItemConfiguracao();
		List<SrCodigoHierarquicoSuporte> opcoesNivel1 = filtro
				.obterOpcoesPorHierarquia(1);
		List<SrCodigoHierarquicoSuporte> opcoesNivel2 = filtro
				.obterOpcoesPorHierarquia(2);
		List<SrCodigoHierarquicoSuporte> opcoesNivel3 = filtro
				.obterOpcoesPorHierarquia(3);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria1, opcoesNivel1, categoria2, opcoesNivel2,
				categoria3, opcoesNivel3);
	}

}