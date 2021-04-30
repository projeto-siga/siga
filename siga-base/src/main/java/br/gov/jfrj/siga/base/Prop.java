package br.gov.jfrj.siga.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
				return (Date) formatter.parse("31/12/2099");
			return (Date) formatter.parse(s);
		} catch (Exception nfe) {
			throw new RuntimeException("Erro ao converter propriedade string em data");
		}
	}

	public static void defineGlobalProperties() {
		provider.addPublicProperty("/siga.base.url", "http://localhost:8080");
		String base = get("/siga.base.url");

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
		provider.addPublicProperty("/siga.integracao.sso.dominio", null);
		provider.addPrivateProperty("/siga.integracao.sso.cliente.id", null);
		provider.addPrivateProperty("/siga.integracao.sso.client.secret", null);
		provider.addPrivateProperty("/siga.integracao.sso.redirect.uri", base + "/siga/callBack");
		provider.addPublicProperty("/siga.integracao.sso.btn.txt", "Entrar com o SSO");
		/* Parâmetros para ativação de Login por SSO OAuth2/OIDC */

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
		provider.addPublicProperty("/siga.jwt.cookie.domain", null);
		provider.addPrivateProperty("/siga.jwt.secret");
		provider.addPrivateProperty("/siga.autenticacao.senha", provider.getProp("/siga.jwt.secret"));
		provider.addPublicProperty("/siga.jwt.token.ttl", "3600");
		provider.addPublicProperty("/siga.local", null);
		provider.addPublicProperty("/siga.uf.padrao", null);
		provider.addPublicProperty("/siga.mensagens", null);
		provider.addPublicProperty("/siga.mesa.carrega.lotacao", "true");
		provider.addPublicProperty("/siga.mesa.nao.revisar.temporarios", "false");
		provider.addPublicProperty("/siga.mesa.versao", "2");
		provider.addPublicProperty("/siga.municipios", null);
		provider.addPublicProperty("/siga.pagina.inicial.url", null);
		provider.addPublicProperty("/siga.versao.teste", "true");
		provider.addPublicProperty("/siga.ws.seguranca.token.jwt", "false");
		provider.addPublicProperty("/sigaex.autenticidade.url", base + "/sigaex/public/app/autenticar");
		provider.addPublicProperty("/sigaex.url", base + "/sigaex");
		provider.addPublicProperty("/sigaex.manual.url", base + "/siga/arquivos/apostila_sigaex.pdf");

		provider.addPrivateProperty("/xjus.jwt.secret", null);
		provider.addPrivateProperty("/xjus.password", null);
		provider.addPublicProperty("/xjus.permalink.url", null);
		provider.addPublicProperty("/xjus.url", null);

		provider.addPublicProperty("/siga.service.endpoint", base + "/siga/servicos/GiService?wsdl");
		provider.addPublicProperty("/siga.service.url", base + "/siga/servicos/GiService");
		provider.addPublicProperty("/siga.service.qname", "http://impl.service.gi.siga.jfrj.gov.br/");
		provider.addPublicProperty("/siga.service.name", "GiService");

		provider.addPublicProperty("/sigaex.service.endpoint", base + "/sigaex/servicos/ExService?wsdl");
		provider.addPublicProperty("/sigaex.service.url", base + "/sigaex/servicos/ExService");
		provider.addPublicProperty("/sigaex.service.qname", "http://impl.service.ex.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigaex.service.name", "ExService");

		provider.addPublicProperty("/sigawf.service.endpoint", base + "/sigawf/servicos/WfService?wsdl");
		provider.addPublicProperty("/sigawf.service.url", base + "/sigawf/servicos/WfService");
		provider.addPublicProperty("/sigawf.service.qname", "http://impl.service.wf.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigawf.service.name", "WfService");

		provider.addPublicProperty("/sigagc.service.endpoint", base + "/sigagc/servicos/GcService?wsdl");
		provider.addPublicProperty("/sigagc.service.url", base + "/sigagc/servicos/GcService");
		provider.addPublicProperty("/sigagc.service.qname", "http://impl.service.gc.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigagc.service.name", "GcService");

		provider.addPublicProperty("/blucservice.url", base + "/blucservice/api/v1");
		provider.addPublicProperty("/vizservice.url", base + "/vizservice");

		provider.addPublicProperty("/siga.sgp.bnf.url", "/siga-beneficios");
		provider.addPublicProperty("/siga.sgp.aq.url", "/sigarhaq");
		provider.addPublicProperty("/siga.sgp.cad.url", "/sigarh");
		provider.addPublicProperty("/siga.sgp.bdp.url", "/sigarhaq1");
		provider.addPublicProperty("/siga.sgp.dcn.url", "/sigarhaq2");
		provider.addPublicProperty("/siga.sgp.cst.url", "/sigarhdadoscadastrais");
		provider.addPublicProperty("/siga.sgp.lot.url", "/sigarhlotacao");
		provider.addPublicProperty("/siga.sgp.trn.url", "/sigptreinamento");
		provider.addPublicProperty("/siga.sgp.terc.url", "/sigarhterceirizados");

		/* Parâmetros para configuração do armazenamento de documento */
		provider.addPublicProperty("/siga.armazenamento.arquivo.tipo", "BLOB");
		String armaz = get("/siga.armazenamento.arquivo.tipo");
		if ("BLOB".equals(armaz) || "TABELA".equals(armaz)) {
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.usuario", null);
			provider.addPrivateProperty("/siga.armazenamento.arquivo.senha", null);
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.url", null);
		} else {
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.usuario");
			provider.addPrivateProperty("/siga.armazenamento.arquivo.senha");
			provider.addRestrictedProperty("/siga.armazenamento.arquivo.url");
		}
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
	}
}
