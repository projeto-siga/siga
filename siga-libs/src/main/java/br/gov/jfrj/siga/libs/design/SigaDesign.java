package br.gov.jfrj.siga.libs.design;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import br.gov.jfrj.siga.libs.util.SigaLibsEL;

public class SigaDesign {

	static CompiledTemplate tCabecalho = template("cabecalho.html");
	static CompiledTemplate tRodape = template("rodape.html");
	static SigaLibsEL sigalibsel = new SigaLibsEL();

	private static CompiledTemplate template(String file) {
		String template;
		try {
			InputStream stream = SigaDesign.class
					.getClassLoader()
					.getResourceAsStream("br/gov/jfrj/siga/libs/design/" + file);
			template = IOUtils.toString(stream, "UTF-8");
			CompiledTemplate compiled = TemplateCompiler
					.compileTemplate(template);
			return compiled;
		} catch (IOException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// <c:set var="ambiente">
		// <c:if
		// test="${f:resource('isVersionTest') or f:resource('isBaseTest')}">
		// <c:if test="${f:resource('isVersionTest')}">SISTEMA</c:if>
		// <c:if
		// test="${f:resource('isVersionTest') and f:resource('isBaseTest')}"> E
		// </c:if>
		// <c:if test="${f:resource('isBaseTest')}">BASE</c:if> DE TESTES
		// </c:if>
		// </c:set>
		// <c:if test="${not empty ambiente}">
		// <c:set var="env" scope="request">${ambiente}</c:set>
		// </c:if>

		Map<String, Boolean> permissoes = new HashMap<>();
		List<Menu> menu = new ArrayList<>();
		List<Substituicao> substituicoes = new ArrayList<>();

		cabecalho("titulo", null, null, null, null, false, false, false,
				"RENATO DO AMARAL CRIVANO MACHADO", "RJSESIA",
				"RENATO DO AMARAL CRIVANO MACHADO", "RJSESIA", permissoes,
				menu, substituicoes, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String cabecalho(String titulo, String ambiente, String meta,
			String js, String onLoad, boolean popup, boolean desabilitarBusca,
			boolean desabilitarMenu, String nomeCadastrante,
			String siglaLotaCadastrante, String nomeTitular,
			String siglaLotaTitular, Map<String, Boolean> permissoes,
			List<Menu> menu, List<Substituicao> substituicoes,
			String complementoHead) {

		if (nomeTitular != null && nomeTitular.equals(nomeCadastrante))
			nomeTitular = null;
		if (siglaLotaTitular != null
				&& siglaLotaTitular.equals(siglaLotaCadastrante))
			siglaLotaTitular = null;

		List<Menu> menus = menuPrincipal();
		if (menu != null)
			menus.addAll(menu);

		Map vars = new HashMap();
		vars.put("titulo_pagina", titulo);
		vars.put("meta", meta);
		vars.put("incluir_js", js);
		vars.put("on_load", onLoad);

		vars.put("popup", popup);
		vars.put("desabilitar_busca", desabilitarBusca);
		vars.put("desabilitar_menu", desabilitarMenu);

		vars.put("cadastrante_nome", primeiroNomeEIniciais(nomeCadastrante));
		vars.put("cadastrante_lotacao_sigla", siglaLotaCadastrante);
		vars.put("titular_nome", primeiroNomeEIniciais(nomeTitular));
		vars.put("titular_lotacao_sigla", siglaLotaTitular);
		vars.put("substituicoes", substituicoes);

		vars.put("pagina_de_erro", false);
		vars.put("menu_principal", menus);
		vars.put("head_complemento", complementoHead);

		tCabecalho = reler("cabecalho.html");
		String output = (String) TemplateRuntime.execute(tCabecalho, vars);
		//System.out.println(output);
		return output;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String rodape(boolean popup, boolean paginaDeErro) {

		Map vars = new HashMap();
		vars.put("popup", popup);
		vars.put("pagina_de_erro", paginaDeErro);
		vars.put("f", sigalibsel);

		String output = (String) TemplateRuntime.execute(tRodape, vars);
		// System.out.println(output);

		return output;
	}

	public static List<Menu> menuPrincipal() {
		return new Menu.MenuBuilder()
				.menu("sitemap", "Módulos", null, true)

				.item("cube", "Documentos",
						"/sigaex/app/expediente/doc/listar?primeiraVez=sim",
						"SIGA;DOC:Módulo de Documentos")
				.item("cube", "Workflow", "/sigawf/app/resumo",
						"SIGA;WF:Módulo de Workflow")
				.item("cube", "Serviços", "/sigasr/", "SIGA;SR")
				.item("cube", "Gestão de Conhecimento",
						"/sigagc/app/estatisticaGeral",
						"SIGA;GC:Módulo de Gestão de Conhecimento")

				.item("plus-square", "Pessoas", null, true)

				.subitem("cube", "AQ", "/sigaaq/", // SigaLibsEL.getURLSistema("siga.sgp.aq"),
						"SIGA;AQ: Módulo de Adicional de Qualificação")

				.subitem("cube", "Benefícios", "/sigabnf/", // SigaLibsEL.getURLSistema("siga.sgp.bnf"),
						"SIGA;BNF: Módulo de Benefícios")

				.subitem("cube", "Cadastro", "/sigacad/", // SigaLibsEL.getURLSistema("siga.sgp.cad"),
						"SIGA;CAD: Módulo de Cadastro")

				.subitem("cube", "Consultas", "/sigacst/", // SigaLibsEL.getURLSistema("siga.sgp.cst"),
						"SIGA;CST: Módulo de Consultas")

				.subitem("cube", "Lotação", "/sigalot/", // SigaLibsEL.getURLSistema("siga.sgp.lot"),
						"SIGA;LOT: Módulo de Lotação")

				.subitem("cube", "Treinamento", "/sigatrn/", // SigaLibsEL.getURLSistema("siga.sgp.trn"),
						"SIGA;TRN: Módulo de Treinamento")

				.item("cube", "Agenda de Perícias Médicas", "/sigapp/",
						"SIGA;PP:Agendamento de perícias do INSS")

				.item("cube", "Transportes", "/sigatp/",
						"SIGA;TP:Módulo de Transportes")

				.menu("user",
						"Gestão de Identidade",
						null,
						"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade")

				.item("cube", "Identidade",
						"/siga/gi/identidade/listar.action",
						"SIGA;GI;ID:Gerenciar identidades")
				.item("cube", "Configurar Permissões",
						"/siga/gi/acesso/listar.action",
						"SIGA;GI;PERMISSAO:Gerenciar permissões")
				.item("cube", "Perfil de Acesso",
						"/siga/gi/perfil/listar.action",
						"SIGA;GI;PERFIL:Gerenciar perfis de acesso")
				.item("cube", "Perfil de Acesso do JEE",
						"/siga/gi/perfiljee/listar.action",
						"SIGA;GI;PERFILJEE:Gerenciar perfis do JEE")
				.item("cube", "Grupo de Distribuição",
						"/siga/gi/email/listar.action",
						"SIGA;GI;GDISTR:Gerenciar grupos de distribuição")
				.item("cube", "Acesso a Serviços",
						"/siga/gi/servico/acesso.action",
						"SIGA;GI;SELFSERVICE:Gerenciar serviços da própria lotação")

				.item("dashboard", "Relatórios", null,
						"SIGA;GI;REL:Gerar relatórios")
				.subitem("cube", "Acesso aos Serviços",
						"/siga/gi/relatorio/selecionar_acesso_servico.action",
						true)
				.subitem(
						"cube",
						"Permissões de Usuário",
						"/siga/gi/relatorio/selecionar_permissao_usuario.action",
						true)
				.subitem(
						"cube",
						"Alteração de Direitos",
						"/siga/gi/relatorio/selecionar_alteracao_direitos.action",
						true)
				.subitem(
						"cube",
						"Histórico de Usuário",
						"/siga/gi/relatorio/selecionar_historico_usuario.action",
						true)

				.menu("cog", "Ferramentas", null,
						"SIGA:Sistema Integrado de Gestão Administrativa;FE:Ferramentas")
				.item("cube", "Cadastro de modelos",
						"/siga/modelo/listar.action",
						"SIGA;FE;MODVER:Visualizar modelos")
				.item("cube", "Cadastro de Órgãos Externos",
						"/siga/orgao/listar.action",
						"SIGA;FE;CAD_ORGAO:Cadastrar Órgãos")
				.item("cube", "Administrar SIGA WF",
						"/sigawf/administrar.action",
						"SIGA;FE;WF_ADMIN:Administrar SIGAWF")
				.item("cube", "Cadastro de Feriados",
						"/siga/feriado/listar.action",
						"SIGA;FE;CAD_FERIADO:Cadastrar Feriados")

				.build();
	}

	public static String primeiroNomeEIniciais(String nome) {
		if (nome == null)
			return "";
		String a[] = nome.split(" ");

		String nomeAbreviado = "";
		for (String n : a) {
			if (nomeAbreviado.length() == 0)
				nomeAbreviado = n.substring(0, 1).toUpperCase()
						+ n.substring(1).toLowerCase();
			else if (!"|DA|DE|DO|DAS|DOS|E|".contains("|" + n + "|"))
				nomeAbreviado += " " + n.substring(0, 1) + ".";
		}
		return nomeAbreviado;
	}

	// Ativar apenas para fins de debug, pois os templates são empacotados no
	// JAR e não são relidos a menos que o JAR seja recompilado. Durante as
	// alterações, convém chamar essa função antes de processar o template. É
	// necessário ajustar o path para o diretório onde os templates estão sendo
	// editados na máquina do desenvolvedor.
	protected static CompiledTemplate reler(String template) {
		byte[] encoded;
		try {
			encoded = Files
					.readAllBytes(Paths
							.get("/Users/nato/Repositories/projeto-siga/siga/siga-libs/src/main/java/br/gov/jfrj/siga/libs/design/"
									+ template));
			return TemplateCompiler
					.compileTemplate(new String(encoded, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
