package br.gov.jfrj.siga.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.model.enm.NivelDaConta;

public class Prop {
	public interface IPropertyProvider {
		String getProp(String nome);

		void addPrivateProperty(String name);

		void addRestrictedProperty(String name);

		void addPublicProperty(String name);

		void addPrivateProperty(String name, String defaultValue);

		void addRestrictedProperty(String name, String defaultValue);

		void addPublicProperty(String name, String defaultValue);
	}

	static public IPropertyProvider provider = null;

	public static void setProvider(IPropertyProvider prov) {
		provider = prov;
	}

	public static String get(String nome) {
	    
	    // Os segredos que assinam os tokens JWT não devem ser iguais
	    //
	    if (nome != null && (nome.contains("jwt.secret") || nome.contains("autenticacao.senha")) 
	            && provider.getProp("/siga.jwt.secret").equals(provider.getProp("/siga.autenticacao.senha")))
	        throw new RuntimeException("Parâmetros siga.jwt.secret e siga.autenticacao.senha não podem ser iguais.");
	    
		return provider.getProp(nome);
	}

	public static Boolean getBool(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Boolean.valueOf(p.trim());
	}

	public static Integer getInt(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Integer.valueOf(p.trim());
	}
	
	public static Long getLong(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Long.valueOf(p.trim());
	}


	public static Double getDouble(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Double.valueOf(p.trim());
	}

	public static List<String> getList(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Arrays.asList(p.split(","));
	}

	public static boolean isGovSP() {
		return "GOVSP".equals(get("/siga.local"));
	}

	public static Date getData(String nome) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

		String s = get(nome);

		try {
			if (s == null)
				return formatter.parse("31/12/2099");
			return formatter.parse(s);
		} catch (Exception nfe) {
			throw new RuntimeException("Erro ao converter propriedade string em data");
		}
	}

	public static void defineGlobalProperties() {
		
		//URL Externa e/ou Interna (para front-end, etc) e
		provider.addPublicProperty("/siga.base.url", "http://localhost:8080");
		provider.addPublicProperty("/sigaex.base.url", get("/siga.base.url"));
		provider.addPublicProperty("/sigagc.base.url", get("/siga.base.url"));
		provider.addPublicProperty("/sigawf.base.url", get("/siga.base.url"));
		provider.addPublicProperty("/sigaarq.base.url", get("/siga.base.url"));
		
		//URL Interna (para back-end). Objetivo não usar Proxy, SSL, Firewall.. entre outras camandas de rede para chamadas internas
		provider.addPublicProperty("/siga.service.base.url", get("/siga.base.url"));		
		final String sigaServiceBaseUrl = get("/siga.service.base.url");

		provider.addPublicProperty("/siga.hibernate.dialect");
		
		provider.addPublicProperty("/siga.gsa.url", null);

		provider.addPublicProperty("/siga.relat.brasao", "brasao.png");
		provider.addPublicProperty("/siga.relat.titulo", "PODER JUDICIÁRIO");
		provider.addPublicProperty("/siga.relat.subtitulo", "JUSTIÇA FEDERAL");

		/* proxy properties */
		provider.addRestrictedProperty("/http.proxyHost", null);
		if (get("/http.proxyHost") != null)
			provider.addRestrictedProperty("/http.proxyPort");
		else
			provider.addRestrictedProperty("/http.proxyPort", null);

		provider.addRestrictedProperty("/https.proxyHost", null);
		if (get("/https.proxyHost") != null)
			provider.addRestrictedProperty("/https.proxyPort");
		else
			provider.addRestrictedProperty("/https.proxyPort", null);

		provider.addRestrictedProperty("/http.nonProxyHosts", "localhost|127.0.0.1|");
		/* END proxy properties */

		/* Parâmetros para ativação de Login por SSO OAuth2/OIDC */
		provider.addPublicProperty("/siga.integracao.sso", null);
		provider.addPublicProperty("/siga.integracao.sso.nome", "SSO");
		provider.addPublicProperty("/siga.integracao.sso.dominio", null);
		provider.addPublicProperty("/siga.integracao.sso.nivelDaConta.dominio", null);
		
		provider.addPublicProperty("/siga.integracao.sso.dominio.logout", get("/siga.integracao.sso.dominio") + "/logout");
		provider.addPublicProperty("/siga.integracao.sso.context", null);
		provider.addPublicProperty("/siga.integracao.sso.iss", "/");
		provider.addPublicProperty("/siga.integracao.sso.jwks.uri", "/jwk");
		provider.addPrivateProperty("/siga.integracao.sso.cliente.id", null);
		provider.addPrivateProperty("/siga.integracao.sso.client.secret", null);
		provider.addPrivateProperty("/siga.integracao.sso.redirect.uri", get("/siga.base.url") + "/siga/callBack");
		provider.addPublicProperty("/siga.integracao.sso.nivelDaContaMinimo",NivelDaConta.PRATA.toString());
		/* Parâmetros para ativação de Login por SSO OAuth2/OIDC */
		
		
		/* PArâmetros abaixo para criação de usuário externo autenticado via gov.br */
		provider.addPublicProperty("/siga.usuario.externo.criar", "false");
		provider.addPublicProperty("/siga.usuario.externo.criar.no.id.orgao",null);
		provider.addPublicProperty("/siga.usuario.externo.criar.no.id.cargo",null);
		provider.addPublicProperty("/siga.usuario.externo.criar.no.id.lotacao",null);
		

		provider.addPublicProperty("/siga.omitir.metodo2", "true");

		provider.addPublicProperty("/siga.cabecalho.logo", "/siga/imagens/logo-trf2-38px.png"); 
		provider.addPublicProperty("/siga.cabecalho.titulo", "Justiça Federal");
		
		provider.addPublicProperty("/siga.email.logo", "/siga/imagens/logo-siga-novo-38px.png"); 
		provider.addPublicProperty("/siga.email.titulo", provider.getProp("/siga.cabecalho.titulo"));
		
		provider.addPublicProperty("/sigawf.ativo", "true");

		provider.addPublicProperty("/siga.ldap.ambiente", null);
		provider.addPublicProperty("/siga.ldap.dn.usuarios", null);
		provider.addPublicProperty("/siga.ldap.keystore", null);
		provider.addPublicProperty("/siga.ldap.modo.escrita", null);
		provider.addPublicProperty("/siga.admin.lotacao", null);		
		provider.addPublicProperty("/siga.admin.orgaos", null);
		provider.addPublicProperty("/siga.ldap.orgaos", null);
		if (get("/siga.ldap.orgaos") != null) {
			for (String s : get("/siga.ldap.orgaos").split(",")) {
				provider.addPublicProperty("/siga.ldap." + s + ".integracao", null);
				provider.addPublicProperty("/siga.ldap." + s + ".modo", null);
				provider.addPublicProperty("/siga.ldap." + s + ".url", null);
			}
		}
		provider.addRestrictedProperty("/siga.ldap.porta", null);
		provider.addPrivateProperty("/siga.ldap.senha", null);
		provider.addRestrictedProperty("/siga.ldap.servidor", null);
		provider.addRestrictedProperty("/siga.ldap.ssl.porta", null);
		provider.addRestrictedProperty("/siga.ldap.usuario", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.autenticacao", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.busca", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.troca.senha", null);
		provider.addPublicProperty("/siga.smtp.starttls.enable", "false");

		provider.addPublicProperty("/siga.recaptcha.key", null);
		provider.addPrivateProperty("/siga.recaptcha.pwd", null);
		provider.addPublicProperty("/siga.smtp", null);
		provider.addPublicProperty("/siga.smtp.auth", "false");
		provider.addPrivateProperty("/siga.smtp.auth.senha", null);
		provider.addPrivateProperty("/siga.smtp.auth.usuario", null);
		provider.addPublicProperty("/siga.smtp.porta", "25");
		provider.addPublicProperty("/siga.smtp.usuario.remetente", "siga@projeto-siga.github.com");
		provider.addPublicProperty("/siga.ambiente", "desenv");
		provider.addPublicProperty("/siga.base.teste", "true");
		provider.addPublicProperty("/siga.devolucao.dias", null);
		provider.addPublicProperty("/siga.jwt.cookie.name", "siga-jwt-auth");
		provider.addPublicProperty("/siga.jwt.cookie.domain", null);
		provider.addPrivateProperty("/siga.jwt.secret");
		provider.addPrivateProperty("/siga.autenticacao.senha", new StringBuffer(provider.getProp("/siga.jwt.secret")).reverse().toString());
		provider.addPublicProperty("/siga.jwt.token.ttl", "3600");
		provider.addPublicProperty("/siga.local", null);
		provider.addPublicProperty("/siga.uf.padrao", null);
		provider.addPublicProperty("/siga.mensagens", null);
		provider.addPublicProperty("/siga.mesa.carrega.lotacao", "true");
		provider.addPublicProperty("/siga.mesa.nao.revisar.temporarios", "false");
		provider.addPublicProperty("/siga.mesa.versao", "2ant");
		provider.addPublicProperty("/siga.municipios", null);
		provider.addPublicProperty("/siga.ws.seguranca.token.jwt", "false");
		provider.addPublicProperty("//sigaex.habilitarExibicaoDataDevolucao", "false"); //TODO

		/* End-points Externos complementares */
		provider.addPublicProperty("/siga.pagina.inicial.url", null);
		provider.addPublicProperty("/sigaex.url", get("/sigaex.base.url") + "/sigaex");
		provider.addPublicProperty("/sigaex.autenticidade.url", get("/sigaex.base.url") + "/sigaex/public/app/autenticar");
		provider.addPublicProperty("/sigagc.url", get("/sigagc.base.url") + "/sigagc");
		provider.addPublicProperty("/sigaarq.url", get("/sigaarq.base.url") + "/sigaarq");
		provider.addPublicProperty("/ckeditor.url", "/ckeditor/ckeditor/ckeditor.js");
		
		/* Indica onde está armazenado o Manual de Operações* */
		provider.addPublicProperty("/sigaex.manual.url", "https://sigadoc.gitbook.io/siga-doc");
		provider.addPublicProperty("/siga.manual.url", "https://sigadoc.gitbook.io/siga-doc");
		
		/* Services
		 * 
		 * Properties para ativação do X-JUS 
		 */
		provider.addPrivateProperty("/xjus.jwt.secret", null);
		provider.addPrivateProperty("/xjus.password", null);
		provider.addPublicProperty("/xjus.permalink.url", null);
        provider.addPublicProperty("/xjus.url", null);
        provider.addPublicProperty("/xjus.reindex", "false");
		
		// Propriedade para desabilitar itens de enums. Informar uma lista, separada por vírgula, na qual cada item é 
		// composto do nome simples do enum, um ponto final e a propriedade .name() a ser desabilitada
		provider.addPublicProperty("/siga.enum.disable", null);

		// Propriedade que controla o acesso aos métodos de exportação para BI da API REST
		provider.addPrivateProperty("/siga.bi.password", null);

		// Propriedade que controla o acesso ao método de conferência de assinaturas de Documentos da API REST
		provider.addPrivateProperty("/sigaex.auditoria.assinaturas.password", null);

		// Propriedade que controla o acesso ao método de númeração genérica da API REST
		provider.addPrivateProperty("/sigaex.numeracao.generica.password", null);

		/* Services
		 * 
		 * Declaração dos serviços e end-points SOAP e RESTful usados pelo back-end nos módulos
		 * 
		 * ENDPOINT, URL, QNAME e NAME
		 * 
		 * SIGA
		 * SIGAEX
		 * SIGAWF
		 * SIGAGC
		 * BLUCSERVICE
		 * VIZSERVICE
		 */

		provider.addPublicProperty("/siga.service.endpoint", sigaServiceBaseUrl + "/siga/servicos/GiService?wsdl");
		provider.addPublicProperty("/siga.service.url", sigaServiceBaseUrl + "/siga/servicos/GiService");
		provider.addPublicProperty("/siga.service.qname", "http://impl.service.gi.siga.jfrj.gov.br/");
		provider.addPublicProperty("/siga.service.name", "GiService");

		provider.addPublicProperty("/sigaex.service.endpoint", sigaServiceBaseUrl + "/sigaex/servicos/ExService?wsdl");
		provider.addPublicProperty("/sigaex.service.url", sigaServiceBaseUrl + "/sigaex/servicos/ExService");
		provider.addPublicProperty("/sigaex.service.qname", "http://impl.service.ex.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigaex.service.name", "ExService");

		provider.addPublicProperty("/sigawf.service.endpoint", sigaServiceBaseUrl + "/sigawf/servicos/WfService?wsdl");
		provider.addPublicProperty("/sigawf.service.url", sigaServiceBaseUrl + "/sigawf/servicos/WfService");
		provider.addPublicProperty("/sigawf.service.qname", "http://impl.service.wf.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigawf.service.name", "WfService");

		provider.addPublicProperty("/sigagc.service.endpoint", sigaServiceBaseUrl + "/sigagc/servicos/GcService?wsdl");
		provider.addPublicProperty("/sigagc.service.url", sigaServiceBaseUrl + "/sigagc/servicos/GcService");
		provider.addPublicProperty("/sigagc.service.qname", "http://impl.service.gc.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigagc.service.name", "GcService");

		provider.addPublicProperty("/blucservice.url", sigaServiceBaseUrl + "/blucservice/api/v1");
		provider.addPublicProperty("/vizservice.url", sigaServiceBaseUrl + "/vizservice");

		provider.addPublicProperty("/siga.sgp.bnf.url", "/siga-beneficios");
		provider.addPublicProperty("/siga.sgp.aq.url", "/sigarhaq");
		provider.addPublicProperty("/siga.sgp.cad.url", "/sigarhcadastro/paginas/pesquisarRecursoHumano.jsf");
		provider.addPublicProperty("/siga.sgp.bdp.url", "/sigarhaq1");
		provider.addPublicProperty("/siga.sgp.dcn.url", "/sigarhdocencia/");
		provider.addPublicProperty("/siga.sgp.cst.url", "/sigarhdadoscadastrais");
		provider.addPublicProperty("/siga.sgp.lot.url", "/sigarhlotacao");
		provider.addPublicProperty("/siga.sgp.trn.url", "/sigptreinamento");
		provider.addPublicProperty("/siga.sgp.terc.url", "/sigarhterceirizados");
		provider.addPublicProperty("/siga.sgp.est.url", "/sigarhest/");
		provider.addPublicProperty("/siga.sgp.rhrelat.url", "/sigarhrelatorios/");
		provider.addPublicProperty("/siga.sgp.rem.url", "/sigarhremocaojuizes/");
		provider.addPublicProperty("/siga.sgp.recad.url", "/sigarhcadastro/");
		provider.addPublicProperty("/siga.sgp.tcu.url", "/sigarhtcu/");		
		

		/* Parâmetros para configuração do armazenamento de documento */
		provider.addPublicProperty("/siga.armazenamento.arquivo.tipo", "TABELA");
		String armaz = get("/siga.armazenamento.arquivo.tipo");
		provider.addPublicProperty("/siga.armazenamento.arquivo.tamanhomax", 
				(Long.toString(10 * 1024 * 1024))); // 10MB
		if ("BLOB".equals(armaz) || "TABELA".equals(armaz)) {
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.usuario", null);
			provider.addPrivateProperty("/siga.armazenamento.arquivo.senha", null);
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.url", null);
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.bucket", null);
			provider.addPublicProperty("/siga.armazenamento.arquivo.formatolivre.tamanhomax", get("/siga.armazenamento.arquivo.tamanhomax"));
		} else {
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.usuario");
			provider.addPrivateProperty("/siga.armazenamento.arquivo.senha");
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.url");
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.bucket", null);
			provider.addPublicProperty("/siga.armazenamento.arquivo.formatolivre.tamanhomax", "10737418240"); //10GB
		}
		provider.addRestrictedProperty("/siga.armazenamento.arquivo.formatolivre.url", get("/siga.armazenamento.arquivo.url"));
		
		/* Lista de unidades que farão o armazenamento no HCP */
		provider.addPublicProperty("/siga.armazenamento.orgaos", "*");
		
		/* Tipos de possíveis responsáveis */
		provider.addPublicProperty("/siga.substituto.tipos", "MATRICULA,LOTACAO");
		
		/* Obriga o preenchimento da descrição da ciência */
		provider.addPublicProperty("/siga.ciencia.preenchimento.obrigatorio", "true");

		/* Cadastro de marcadores: Define um grupo da mesa default onde irá aparecer o marcador
		 * */
		provider.addPublicProperty("/siga.marcadores.grupo.default", "");

		/* Cadastro de marcadores: Lista de finalidades que a lotação poderá cadastrar. Default - todos
		 * Para selecionar apenas alguns, separar os nomes com vírgulas,
		 * por ex: "Etiqueta,Etiqueta Direcionada,Lista"
		 * */
		provider.addPublicProperty("/siga.marcadores.lota.finalidades", "");
		/* Cadastro de marcadores: Se true, exibe campo de data de ativação no cadastro de 
		 * marcadores. Se false não exibe.
		 * */
		provider.addPublicProperty("/siga.marcadores.exibe.dataativacao", "false");
		/* Cadastro de marcadores: Se true, permite data anterior a hoje nas datas de exibição e limite. 
		 * Se false, não permite.
		 * */
		provider.addPublicProperty("/siga.marcadores.permite.data.retroativa", "true");
		/* Cadastro de marcadores: Indica a quantidade de marcadores permitidos para uma unidade. 
		 * - Default: 10 marcadores.
		 * */
		provider.addPublicProperty("/siga.marcadores.qtd.maxima.por.unidade", "10");		
		/* Tela de Pesquisa, combos de espécies e modelos: 
		 * - True: Confere se a espécie tem modelos no combo da tela de pesquisa.  
		 * - False ou inexistente: Não confere, evitando leitura dos modelos.
		 * */
		provider.addPublicProperty("/siga.pesquisa.confere.modelos", "true");
		/* Tela de Pesquisa, só funciona se permissão de acesso SIGA;DOC;PESQ;DTLIMITADA estiver permitida. Será a qtd máxima 
		 * de dias que poderá ser pesquisada na pesquisa avançada.
		 * */
		provider.addPublicProperty("/siga.pesquisa.limite.dias", "31");
		/* Tela de Criar/Editar Documentos: Qtde de minutos para recarregar o combo de modelos que fica 
		 * armazenado em session storage no browser do usuário.
		 * */
		provider.addPublicProperty("/siga.session.modelos.tempo.expiracao", "60");

		/* Permite realizar o upload das extensões declaradas */
		provider.addPublicProperty("/sigagc.todos.extensoes.anexo.permitidas", "jpg,bmp,png,doc,docx,docm,xls,xlsx,ppt,pptx,pdf,txt");
		provider.addPublicProperty("/sigagc.imagem.extensoes.anexo.permitidas", "jpg,bmp,png");
		provider.addPublicProperty("/sigagc.documento.extensoes.anexo.permitidas", "doc,docx,docm,xls,xlsx,ppt,pptx,pdf,txt");
	
		/* Permite inativar lotação com determinadas marcações*/
		/* Trocado de Bool para List String que deve conter as chaves de marcadores permitidas separadas por virgula no padrao do getList() */
		provider.addPublicProperty("/siga.lotacao.inativacao.marcadores.permitidos", null);
		/* Permitir Inativar Lotação com Marcas nos Grupos de Marcadores. 
		 * É ligada caso property siga.lotacao.inativacao.marcadores.permitidos seja diferente de null e soma-se as listas
		 * Default: Documentos não apresentados na Mesa.*/
		provider.addPublicProperty("/siga.lotacao.inativacao.grupo.marcadores.permitidos", "NENHUM");
		
		/* Permite Alterar a lotação da Pessoa com determinadas marcações*/
		provider.addPublicProperty("/siga.alteracao.lotacao.pessoa.marcadores.permitidos", get("/siga.lotacao.inativacao.marcadores.permitidos"));
		provider.addPublicProperty("/siga.alteracao.lotacao.pessoa.grupo.marcadores.permitidos", get("/siga.lotacao.inativacao.grupo.marcadores.permitidos"));

		/* Properties para ativação do Módulo de compra e contratações.*/
		provider.addPrivateProperty("/secc.ui.url", null);
		
		/* Property que é enviada nas chamadas de webservice do freemarker, no header authorization.*/
		provider.addPrivateProperty("/siga.freemarker.webservice.password", null);
		
		/* Configuração do tamanho máximo para exibição paginada de documento completo em pdf
		 * Quantidade de Documentos presentes por pagina */
		provider.addPublicProperty("/siga.exibicao.paginada.pdf.completo.megabytes.tamanhomax", null);
		provider.addPublicProperty("/siga.exibicao.paginada.pdf.completo.quantidade.documentos", null);
		
	}
}
