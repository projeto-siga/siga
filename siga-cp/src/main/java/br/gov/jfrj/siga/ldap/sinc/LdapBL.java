/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ldap.sinc;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import javax.naming.ldap.Control;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import br.gov.jfrj.ldap.ILdapDao;
import br.gov.jfrj.ldap.LdapDaoImpl;
import br.gov.jfrj.ldap.util.LdapUtils;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Criptografia;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ldap.AdContato;
import br.gov.jfrj.siga.ldap.AdGrupo;
import br.gov.jfrj.siga.ldap.AdGrupoDeDistribuicao;
import br.gov.jfrj.siga.ldap.AdGrupoDeSeguranca;
import br.gov.jfrj.siga.ldap.AdObjeto;
import br.gov.jfrj.siga.ldap.AdReferencia;
import br.gov.jfrj.siga.ldap.AdUnidadeOrganizacional;
import br.gov.jfrj.siga.ldap.AdUsuario;
import br.gov.jfrj.siga.ldap.sinc.resolvedores.RegraCaixaPostal;
import br.gov.jfrj.siga.ldap.sinc.resolvedores.ResolvedorNomeEmail;
import br.gov.jfrj.siga.ldap.sinc.resolvedores.ResolvedorRegrasCaixaPostal;
import br.gov.jfrj.siga.sinc.lib.OperadorSemHistorico;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import sun.misc.BASE64Decoder;

/**
 * Classe que provê uma interface java para a árvore LDAP.
 * 
 * Considerações gerais: Nas pesquisas, o LDAP considera palavas com acento como
 * se não tivessem o acento (no caso do cedilha também). Exemplo: FRANÇA =
 * FRANCA ou ÁRVORE = ARVORE = ÀRVORE
 * 
 * @author kpf
 * 
 */
public class LdapBL implements OperadorSemHistorico {

	private static final String EXCH_DISPLAY_TYPE_GRUPO = "1";

	private static final String EXCH_DISPLAY_TYPE_CONTATO = "6";

	private static final String EXCH_DISPLAY_TYPE_USUARIO = "1073741824";

	/* TODO bjn parametrizar */
	private static String EXCH_VERSION;

	final static String ATTRIBUTE_FOR_USER = "sAMAccountName";

//	private static LdapBL instance = null;
	private SincProperties conf;

	private ILdapDao ldap;

	private final ResolvedorNomeEmail rNomeEmail = new ResolvedorNomeEmail();

	private final Map<String, Set<String>> cacheGetEmailPorCn = new HashMap<String, Set<String>>();
	private final Map<String, Set<String>> cacheGetDnPorEmail = new HashMap<String, Set<String>>();
	private final Map<String, Set<String>> cacheGetEmailPorDn = new HashMap<String, Set<String>>();

	private List<AdReferencia> referencias = new ArrayList<AdReferencia>();

	private static Logger log = Logger.getLogger("br.gov.jfrj.log.sinc.ldap.bl");

//	/**
//	 * Retorna uma instância única (singleton)
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public static LdapBL getInstance(SincProperties conf) {
//		if (instance == null)
//			try {
//				instance = new LdapBL(conf);
//				EXCH_VERSION = conf.getVersaoExchange();
//				if (EXCH_VERSION == null) {
//					throw new Exception(
//							"Favor configurar parametro siga.cp.sinc.ldap.jfrj.prod.versao_exchange no arquivo properties.");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (AplicacaoException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		return instance;
//	}

	private LdapBL() throws Exception {
	}

	/**
	 * Construtor
	 * 
	 * @param conf
	 * @throws Exception
	 */
	public LdapBL(SincProperties conf) throws Exception {
		this.conf = conf;
//		if (conf.isModoEscrita())
//			throw new Exception("Não queremos escrever no AD por enquanto.");
		ldap = new LdapDaoImpl(!conf.isModoEscrita()).getProxy();
		if (this.conf.isSSLAtivo()) {
			ldap.conectarComSSL(conf.getServidorLdap(), conf.getPortaSSLLdap(), conf.getUsuarioLdap(),
					conf.getSenhaLdap(), conf.getKeyStore());
		} else {
			ldap.conectarSemSSL(conf.getServidorLdap(), conf.getPortaLdap(), conf.getUsuarioLdap(),
					conf.getSenhaLdap());
		}
		EXCH_VERSION = conf.getVersaoExchange();
		if (EXCH_VERSION == null) {
			throw new Exception(
					"Favor configurar parametro siga.cp.sinc.ldap.jfrj.prod.versao_exchange no arquivo properties.");
		}
	}

	private void adicionarMembros(AdGrupo adGrupo) throws NamingException, AplicacaoException {
		try {
			removerMembrosAntigos(adGrupo);
			incluirNovosMembros(adGrupo);

		} catch (AttributeInUseException e) {
			// ignora porque já existe
		} catch (NameNotFoundException e) {
			log.fine("GRUPO NÃO ENCONTRADO PARA INCLUSÃO DE MEMBROS >>>>> " + adGrupo.getNomeCompleto());
		} catch (AplicacaoException e) {
			log.warning(e.getMessage());
		}
	}

	private void incluirNovosMembros(AdGrupo adGrupo) throws NamingException, AplicacaoException {
		for (AdObjeto o : adGrupo.getMembros()) {
			ldap.inserirValorAtributoMultivalorado(adGrupo.getNomeCompleto(), "member", o.getNomeCompleto());
			log.info("Incluindo membro...: " + o.getNomeCompleto());
//			}
		}
	}

	private void removerMembrosAntigos(AdGrupo adGrupo) throws NamingException, AplicacaoException {
		Attributes attrs = ldap.pesquisar(adGrupo.getNomeCompleto());
		if (attrs != null && attrs.get("member") != null) {
			NamingEnumeration membros = (NamingEnumeration<NameClassPair>) attrs.get("member").getAll();
			while (membros.hasMoreElements()) {
				String membro = membros.next().toString();
				ldap.removerValorAtributoMultivalorado(adGrupo.getNomeCompleto(), "member", membro);
			}
		}
	}

	/**
	 * Método necessário para efeito de sincronismo. Incluir um adObjeto que na
	 * árvore LDAP.
	 */
	@Override
	public Sincronizavel incluir(Sincronizavel novo) {
		log.fine("Incluíndo no LDAP:" + ((AdObjeto) novo).getNomeCompleto());
		AdObjeto objIncluido = null;
		try {
			objIncluido = incluirAD((AdObjeto) novo);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
		return objIncluido;
	}

	/**
	 * Método necessário para efeito de sincronismo. Exclui um adObjeto que está
	 * gravado na árvore LDAP.
	 */
	@Override
	public Sincronizavel excluir(Sincronizavel antigo) {
		log.fine("Excluíndo do LDAP:" + ((AdObjeto) antigo).getNomeCompleto());
		AdObjeto objIncluido = null;
		try {
			objIncluido = excluirAD((AdObjeto) antigo);
		} catch (NamingException e) {
			log.info(e.getMessage());
		} catch (AplicacaoException e) {
			throw new RuntimeException(e);
		}
		return objIncluido;
	}

	/**
	 * Método necessário para efeito de sincronismo. Altera um adObjeto que está
	 * gravado na árvore LDAP.
	 */
	@Override
	public Sincronizavel alterar(Sincronizavel antigo, Sincronizavel novo) {
		log.fine("Alterando no LDAP:" + ((AdObjeto) antigo).getNomeCompleto() + " --> "
				+ ((AdObjeto) antigo).getNomeCompleto());

		AdObjeto objAlterado = null;
		try {
			objAlterado = alterarAD((AdObjeto) antigo, (AdObjeto) novo);
		} catch (NamingException e) {
			log.info(e.getMessage());
		} catch (AplicacaoException e) {
			throw new RuntimeException(e);
		}

		return objAlterado;
	}

	/**
	 * Insere um AdObjeto na árvore LDAP
	 * 
	 * @param objeto
	 * @return
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public AdObjeto incluirAD(AdObjeto objeto) throws NamingException, AplicacaoException {
		if (objeto instanceof AdUnidadeOrganizacional) {
			AdUnidadeOrganizacional uo = (AdUnidadeOrganizacional) objeto;
			return this.criarUnidadeOrganizacional(uo);
		}

		if (objeto instanceof AdGrupoDeDistribuicao) {
			AdGrupoDeDistribuicao g = (AdGrupoDeDistribuicao) objeto;
			return this.criarGrupoDistribuicao(g, true);
		}

		if (objeto instanceof AdGrupoDeSeguranca) {
			AdGrupoDeSeguranca g = (AdGrupoDeSeguranca) objeto;
			return this.criarGrupoSeguranca(g, true);
		}

		if (objeto instanceof AdGrupo) {
			AdGrupo g = (AdGrupo) objeto;
			return this.criarGrupo(g, true);
		}

		if (objeto instanceof AdUsuario) {
			AdUsuario u = (AdUsuario) objeto;
			return this.criarUsuario(u);
		}

		if (objeto instanceof AdContato) {
			AdContato c = (AdContato) objeto;
			return this.criarContato(c);
		}

		if (objeto instanceof AdReferencia) {
			AdReferencia r = (AdReferencia) objeto;
			return this.criarReferencia(r);
		}

		return null;

	}

	private AdObjeto criarReferencia(AdReferencia r) {
		for (AdGrupo g : r.getGruposPertencentes()) {
			try {
				ldap.inserirValorAtributoMultivalorado(g.getNomeCompleto(), "member", r.getReferencia());
			} catch (Exception e) {
				log.warning(
						"Objeto [" + r.getReferencia() + "] não pode ser inserido em [" + g.getNomeCompleto() + "]");
			}
		}

		return r;
	}

	/**
	 * Remove um objeto da árvore LDAP
	 * 
	 * @throws AplicacaoException
	 */
	public AdObjeto excluirAD(AdObjeto objeto) throws NamingException, AplicacaoException {

		if (objeto instanceof AdGrupoDeDistribuicao) {
			AdGrupoDeDistribuicao g = (AdGrupoDeDistribuicao) objeto;
			if (g.getGrupoPai() != null) {
				incluir(g.getGrupoPai());
			}
			this.excluirGrupoDistribuicao(g);
			return g;
		}

		if (objeto instanceof AdGrupoDeSeguranca) {
			AdGrupoDeSeguranca g = (AdGrupoDeSeguranca) objeto;
			if (g.getGrupoPai() != null) {
				incluir(g.getGrupoPai());
			}
			this.excluirGrupoSeguranca(g);
			return g;
		}

		if (objeto instanceof AdUsuario) {
			AdUsuario u = (AdUsuario) objeto;
			if (u.getGrupoPai() != null) {
				incluir(u.getGrupoPai());
			}

			this.excluirUsuario(u);
			return u;
		}

		if (objeto instanceof AdContato) {
			AdContato c = (AdContato) objeto;
			if (c.getGrupoPai() != null) {
				incluir(c.getGrupoPai());
			}

			this.excluirContato(c);
			return c;
		}

		if (objeto instanceof AdReferencia) {
			AdReferencia r = (AdReferencia) objeto;
			if (r.getGrupoPai() != null) {
				incluir(r.getGrupoPai());
			}

			this.excluirReferencia(r);
			return r;
		}

		return null;

	}

	private void excluirReferencia(AdReferencia r) {
		for (AdGrupo g : r.getGruposPertencentes()) {
			removerItemCampoMultivalorado(g.getNomeCompleto(), "member", r.getReferencia(), null);
		}
	}

	/**
	 * Altera um adObjeto que está gravado na árvore LDAP.
	 * 
	 * @param antigo - objeto a ser alterado
	 * @param novo   - objeto com as novas informações
	 * @return - objeto que foi gravado. Caso seja um adGrupo, pode ter midificações
	 *         em seus membros por causa da alteração.
	 * @throws NamingException    - disparado caso o objeto novo não seja do mesmo
	 *                            tipo que o antigo
	 * @throws AplicacaoException
	 */
	public AdObjeto alterarAD(AdObjeto antigo, AdObjeto novo) throws NamingException, AplicacaoException {

		if (!antigo.getClass().equals(novo.getClass())) {
			throw new NamingException("Os objetos são de tipos diferentes!");
		}

		if (antigo instanceof AdReferencia) {
			return antigo;
		}

		substituirAtributos(antigo, novo);

		if (novo instanceof AdUsuario) {
			definirSenhaUsuario((AdUsuario) novo);
			ldap.ativarUsuario(novo.getNomeCompleto());

		}

		if (novo instanceof AdGrupo) {
			adicionarMembros((AdGrupo) novo);
		}

		return novo;

	}

	private void substituirAtributos(AdObjeto antigo, AdObjeto novo) throws NamingException, AplicacaoException {
		Attributes attrs = montarAtributos(novo);

		log.fine("Alterando atributos do usuário : " + antigo.getNomeCompleto());

		// atributos que podem danificar as configurações já existentes
		String[] ignorarAttrs = { "showInAddressBook", "displayName", "proxyAddresses", "mail", "mailNickname",
				"legacyExchangeDN", "samAccountName", "msExchHideFromAddressLists", "homeMTA", "mDBUseDefaults",
				"msExchHomeServerName", "msExchMailboxTemplateLink", "msExchPoliciesExcluded",
				"msExchRecipientDisplayType", "msExchRecipientTypeDetails", "msExchUserAccountControl",
				"msExchVersion" };

		substituirAtributos(antigo.getNomeCompleto(), attrs, ignorarAttrs);
	}

	public void substituirAtributos(String dnOrigem, Attributes attrs, String[] attrsIgnorados) throws NamingException {
		if (attrs == null) {
			return;
		}
		NamingEnumeration<String> iAttrs = attrs.getIDs();
		while (iAttrs.hasMoreElements()) {
			Attribute attr = attrs.get(iAttrs.next());
			if (attr.getID().equals("cn") || attr.getID().equals("distinguishedName")
					|| attr.getID().equals("groupType") || attr.getID().equals("objectClass")
					|| attr.getID().equals("msExchMailboxGuid") || attr.getID().equals("homeMDB")
					|| (attr.getID().equals("displayName") && attr.get() == null)) {
				continue;
			}

			boolean ignorar = false;
			if (attrsIgnorados != null) {
				for (String attrIgn : attrsIgnorados) {
					if (attr.getID().equals(attrIgn)) {
						ignorar = true;
						break;
					}
				}
			}

			if (ignorar) {
				ignorar = false;
				continue;
			}

			ModificationItem member[] = new ModificationItem[1];
			member[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
			try {
				if (ldap.isSomenteLeitura()) {
					log.info(member[0].toString());
				} else {
					log.fine("Substituindo attr: " + member[0].toString());
					ldap.getContexto().modifyAttributes(dnOrigem, member);
				}
			} catch (AttributeInUseException e) {
				// ignora porque já existe
			}
		}
	}

	/**
	 * Pesquisa um objeto na árvore LDAP
	 * 
	 * @param dn - Nome distinto do objeto
	 * @return
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public List<AdObjeto> pesquisarObjeto(String... multiplosDN) throws NamingException, AplicacaoException {

		List<String> ignorar = montarListaDNIgnoradosNaPesquisa();

		return pesquisarObjeto(ignorar, multiplosDN);
	}

	/**
	 * Pesquisa um objeto na árvore LDAP, sem ignorar nenhum sub-DN. As
	 * configurações do arquivo siga.properties que ignoram usuarios inativos ou
	 * grupos inativos não surtem efeito nesse método.
	 * 
	 * @param multiplosDN
	 * @return
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public List<AdObjeto> pesquisarObjetoSemIgnorarNada(String... multiplosDN)
			throws NamingException, AplicacaoException {

		return pesquisarObjeto(new ArrayList<String>(), multiplosDN);
	}

	/**
	 * Pesquisa um objeto LDAP na árvore. Se o objeto contiver membros, estes serão
	 * adicionados à resposta.
	 * 
	 * @param ignorarDN
	 * @param multiplosDN
	 * @return
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	private List<AdObjeto> pesquisarObjeto(List<String> ignorarDN, String... multiplosDN)
			throws NamingException, AplicacaoException {
		List<AdObjeto> l = new ArrayList<AdObjeto>();
		for (String dn : multiplosDN) {
			pesquisarObjeto(dn, null, l, ignorarDN);
		}

		Map<String, AdObjeto> membrosNaoIgnorados = new TreeMap<String, AdObjeto>();

		// coloca os objetos encontrados dentro de um map.
		for (AdObjeto o : l) {
			membrosNaoIgnorados.put(o.getNomeCompleto(), o);
		}

		// coloca os objetos dentro de seus respectivos grupos
		List<AdObjeto> referenciasAdicionadas = new ArrayList<AdObjeto>();
		for (AdObjeto o : l) {
			if (o instanceof AdGrupo) {
				AdGrupo g = (AdGrupo) o;
				// Não devemos tentar extrair membros de grupos que estão no container "Extras", pois isso dá erro.
				if (!g.getNomeCompleto().contains("OU=Extras,")) 
					extrairMembros(g, membrosNaoIgnorados, referenciasAdicionadas);
			}
		}
		l.addAll(referenciasAdicionadas);
		return l;
	}

	private List<String> montarListaDNIgnoradosNaPesquisa() {
		List<String> ignorar = new ArrayList<String>();

		// exclui extras
		ignorar.add(conf.getDnExtras());

		// excluir usuários?
		if (!conf.isModoExclusaoUsuarioAtivo()) {
			ignorar.add(conf.getDnUsuariosInativos());
		}

		// excluir grupos?
		if (!conf.isModoExclusaoGrupoAtivo()) {
			ignorar.add(conf.getDnGruposDistribuicaoInativos());
			if (conf.getSincronizarGruposSeguranca()) {
				ignorar.add(conf.getDnGruposSegurancaInativos());
			}
		}

		// sincronizar grupos de segurança?
		if (!conf.getSincronizarGruposSeguranca()) {
			ignorar.add(conf.getDnGruposSeguranca());
			if (!ignorar.contains(conf.getDnGruposSegurancaInativos())) {
				ignorar.add(conf.getDnGruposSegurancaInativos());
			}
		}
		return ignorar;
	}

	/**
	 * Muda a localização de um objeto na árvore LDAP.
	 * 
	 * @param dnOrigem
	 * @param dnDestino
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public void moverObjeto(String dnOrigem, String dnDestino) throws NamingException, AplicacaoException {
		ldap.mover(dnOrigem, dnDestino);
	}

	public AdGrupo criarGrupo(AdGrupo adGrupo, boolean comMembros) throws NamingException, AplicacaoException {

		// se for grupo manual, substitui o automatico correspondente
		Attributes attrsAntigos = null;
		if (isGrupoDistribuicaoManualEmail(adGrupo.getNome())) {

			String dnGrpAutoCorr = getGrpAutoCorrespondente((AdGrupoDeDistribuicao) adGrupo);
            try {
                attrsAntigos = ldap.pesquisar(dnGrpAutoCorr);
            } catch (NameNotFoundException e) {
                log.warning("Não foi possível localizar o grupo automático '" + dnGrpAutoCorr + "' correspondente ao grupo manual '" + adGrupo.getNome() + "'");
                // Não é necessário lançar a exceção porque o tratamento do retorno nulo já é feito abaixo.
            }
			if (attrsAntigos != null) {
				String cn = attrsAntigos.get("cn").get().toString();
				String dn = attrsAntigos.get("distinguishedName").get().toString();
				renomearGrupo(cn, dn);
				excluirDaGlobalAddrList(dn);
				moverObjeto(dn, resolverDnInativo(cn));
			}

		} else if (isGrupoDistribuicaoAuto(adGrupo.getNome())) {
			String dnGrpManualCorr = getGrpManualCorrespondente((AdGrupoDeDistribuicao) adGrupo);
			try {
			    attrsAntigos = ldap.pesquisar(dnGrpManualCorr);
			} catch (NameNotFoundException e) {
			    log.warning("Não foi possível localizar o grupo manual '" + dnGrpManualCorr + "' correspondente ao grupo automático '" + adGrupo.getNome() + "'");
			    // Não é necessário lançar a exceção porque o tratamento do retorno nulo já é feito abaixo.
			}
			if (attrsAntigos != null) {
				String cn = attrsAntigos.get("cn").get().toString();
				String dn = attrsAntigos.get("distinguishedName").get().toString();
				renomearGrupo(cn, dn);
				excluirDaGlobalAddrList(dn);
				moverObjeto(dn, resolverDnInativo(cn));
			}

		}

		Attributes attrs = montarAtributos(adGrupo);

		if (!ldap.existe(adGrupo.getNomeCompleto())) {
			ldap.incluir(adGrupo.getNomeCompleto(), attrs);

			if (comMembros) {
				adicionarMembros(adGrupo);
			}
		}

		// aplica atributos do grupo automatico anterior anterior
		if (isGrupoDistribuicaoManualEmail(adGrupo.getNome()) || isGrupoDistribuicaoAuto(adGrupo.getNome())) {
			String[] ignorarAttrs = { "dSCorePropagationData", "member", "memberOf", "nTSecurityDescriptor",
					"objectGUID", "objectSid", "sAMAccountName", "sAMAccountType", "uSNChanged", "uSNCreated",
					"whenChanged", "whenCreated", "objectCategory", "instanceType", "name" };

			substituirAtributos(adGrupo.getNomeCompleto(), attrsAntigos, ignorarAttrs);
		}

		return adGrupo;

	}

	private AdGrupoDeDistribuicao criarGrupoDistribuicao(AdGrupoDeDistribuicao adGrupo, boolean comMembros)
			throws NamingException, AplicacaoException {
		return (AdGrupoDeDistribuicao) criarGrupo(adGrupo, comMembros);
	}

	private AdGrupoDeSeguranca criarGrupoSeguranca(AdGrupoDeSeguranca adGrupo, boolean comMembros)
			throws NamingException, AplicacaoException {
		return (AdGrupoDeSeguranca) criarGrupo(adGrupo, comMembros);
	}

	/**
	 * Cria uma unidade organizacional (OU) na árvore LDAP.
	 * 
	 * @param uo - objeto que contém as informações a serem gravadas na árvore LDAP.
	 * @return
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public AdUnidadeOrganizacional criarUnidadeOrganizacional(AdUnidadeOrganizacional uo)
			throws NamingException, AplicacaoException {
		Attributes attrs = montarAtributos(uo);

		if (!ldap.existe(uo.getNomeCompleto())) {
			ldap.incluir(uo.getNomeCompleto(), attrs);
		}

		return uo;

	}

	/**
	 * Extrai e grava as informacoes de um AdUsuario na árvore LDAP.
	 * 
	 * Para manipular as propriedades veja:http://support.microsoft.com/kb/305144
	 * 
	 * @param adUsuario
	 * @throws NamingException
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	public AdUsuario criarUsuario(AdUsuario adUsuario) throws NamingException, AplicacaoException {
		adUsuario.setNomeExibicao(adUsuario.getNomeResolucaoEmail());
		Attributes attrs = montarAtributos(adUsuario);

		BASE64Decoder dec = new BASE64Decoder();

		if (!ldap.existe(adUsuario.getNomeCompleto())) {

			try {

				if (conf.isModoExclusaoUsuarioAtivo()) {
					ldap.incluir(adUsuario.getNomeCompleto(), attrs);
					log.fine(adUsuario.getNomeCompleto() + " incluído com sucesso!");

				} else {
					String dnInativo = "CN=" + adUsuario.getNome() + "," + conf.getDnUsuariosInativos();
					if (pesquisarObjeto(dnInativo).size() > 0) {
						moverObjeto(dnInativo, adUsuario.getNomeCompleto());
						ldap.alterarAtributo(adUsuario.getNomeCompleto(), "samAccountName", adUsuario.getSigla());

					} else {
						try {
							ldap.incluir(adUsuario.getNomeCompleto(), attrs);
						} catch (Exception e) {
							log.warning("Não foi possivel incluir o usuario [" + adUsuario.getNomeCompleto()
									+ "]. Verifique se a sigla esta duplicada!");
						}

						log.fine(adUsuario.getNomeCompleto() + " incluído com sucesso!");
					}

				}
			} catch (NameAlreadyBoundException e) {
				log.warning(
						"ERRO AO INSERIR NO LDAP (POSSIVELMENTE A PESSOA MUDOU DE NOME OU SÃO INCOMPATÍVEIS ENTRE O ORACLE E O ACTIVE DIRECTORY).\n SE FOR UM USUARIO USUARIO, VERIFIQUE CONFLITO DE SIGLAS!!! >>>>>>"
								+ adUsuario.getNomeCompleto());
			}
			definirSenhaUsuario(adUsuario);
			try {
				ldap.ativarUsuario(adUsuario.getNomeCompleto());
			} catch (Exception e) {
				log.warning("Não foi possivel ativar o usuario [" + adUsuario.getNomeCompleto() + "].");
			}

		} else {

			Attributes ats = ldap.pesquisar(adUsuario.getNomeCompleto());
			log.warning("USUÁRIO JÁ EXISTE >>>>> " + ats.get("cn"));

		}

		if (adUsuario.getGrupoPai() != null && !(adUsuario.getGrupoPai() instanceof AdUnidadeOrganizacional)) {
		    try {
		        definirGrupoPaiUsuario(adUsuario);
		    } catch (Exception e) {
                log.warning("Não foi possivel definir grupo pai do usuario [" + adUsuario.getNomeCompleto() + "].");
            }
		}

		return adUsuario;
	}

	private void definirGrupoPaiUsuario(AdUsuario adUsuario) throws NamingException, AplicacaoException {
		String nomeGrupoPai = "CN=" + adUsuario.getGrupoPai().getNome() + "," + conf.getDnGruposDistribuicao();
		ldap.inserirValorAtributoMultivalorado(nomeGrupoPai, "member", adUsuario.getNomeCompleto());
	}

	public AdContato criarContato(AdContato adContato) throws NamingException, AplicacaoException {

		Set<String> dnObjeto = cacheGetDnPorEmail.get(adContato.getNome());
		if (dnObjeto != null && dnObjeto.size() == 0) {
			Attributes attrs = montarAtributos(adContato);

			if (!ldap.existe(adContato.getNomeCompleto())) {
				ldap.incluir(adContato.getNomeCompleto(), attrs);
				log.fine(adContato.getNomeCompleto() + " incluído com sucesso!");
			}
		} else {
			log.warning("Contato não foi criado por existir duplicidade! " + adContato.getNome() + " --> " + dnObjeto);
		}

		return adContato;
	}

	private void definirSenhaUsuario(AdUsuario adUsuario) throws AplicacaoException {
		BASE64Decoder dec = new BASE64Decoder();
		String senhaNova = null;
		if (adUsuario.getSenhaCripto() != null) {
			try {
				senhaNova = new String((Criptografia.desCriptografar(dec.decodeBuffer(adUsuario.getSenhaCripto()),
						adUsuario.getChaveCripto())));
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ldap.definirSenha(adUsuario.getNomeCompleto(), senhaNova);
			// definirSenhaAD(adUsuario.getNomeCompleto(), senhaNova);
		}
	}

	private void excluirContato(AdContato c) throws AplicacaoException {
		// this.contexto.destroySubcontext("CN=" + c.getNome() + ","
		// + conf.getDnContatos());
		ldap.excluir("CN=" + c.getNome() + "," + conf.getDnContatos());
	}

	private void excluirGrupoDistribuicao(AdGrupoDeDistribuicao g) throws NamingException, AplicacaoException {

		if (conf.isModoExclusaoGrupoAtivo()) {
			ldap.excluir("CN=" + g.getNome() + "," + conf.getDnGruposDistribuicao());
		} else {

			if (pesquisarObjeto(g.getNomeCompleto()).size() <= 0) {
				return;
			}

			renomearGrupo(g.getNome(), g.getNomeCompleto());
			excluirDaGlobalAddrList(g.getNomeCompleto());

			if (pesquisarObjeto(resolverDnInativo(g.getNome())).size() <= 0) {
				log.warning(
						"Modo de exclusão desativado! Apenas moverá para o container de grupos de segurança inativos!");
				this.moverObjeto(g.getNomeCompleto(), resolverDnInativo(g.getNome()));
			} else {
				log.warning("Grupo de Seguranca já está inativo: " + g.getNome());
			}
		}
	}

	private void excluirDaGlobalAddrList(String dnGrupoDistribuicao) {
		try {
			ldap.alterarAtributo(dnGrupoDistribuicao, "msExchHideFromAddressLists", "TRUE");
			ldap.excluirAtributo(dnGrupoDistribuicao, "showInAddressBook");
		} catch (AplicacaoException e) {
		} catch (NoSuchAttributeException e) {
        }
	}

	private String resolverDnInativo(String cn) {
		return "CN=" + getDataAtual() + conf.getPfxObjExcluido() + cn + conf.getSfxObjExcluido() + ","
				+ conf.getDnGruposDistribuicaoInativos();
	}

	private String getGrpManualCorrespondente(AdGrupoDeDistribuicao grpManual) {
		return "CN=" + grpManual.getNome().replace(conf.getSfxGrpDistrAuto(), conf.getSfxGrpDistrManualEmail()) + ","
				+ conf.getDnGruposDistribuicao();
	}

	private String getGrpAutoCorrespondente(AdGrupoDeDistribuicao grpAuto) {
		return "CN=" + grpAuto.getNome().replace(conf.getSfxGrpDistrManualEmail(), conf.getSfxGrpDistrAuto()) + ","
				+ conf.getDnGruposDistribuicao();
	}

	private void renomearGrupo(String cn, String dn) throws AplicacaoException, NamingException {

		String dataAtual = getDataAtual();
		ldap.alterarAtributo(dn, "sAMAccountName",
				dataAtual + conf.getPfxObjExcluido() + cn + conf.getSfxObjExcluido());
		ldap.alterarAtributo(dn, "legacyExchangeDN",
				conf.getLegacyExchangeDN() + dataAtual + conf.getPfxObjExcluido() + cn + conf.getSfxObjExcluido());

		NamingEnumeration emails = ldap.getAttributes(dn).get("proxyAddresses").getAll();
		Attributes attrs = new BasicAttributes(true);
		while (emails.hasMore()) {
			String[] entradaProxy = emails.nextElement().toString().split(":");
			String emailRenomeado = entradaProxy[0] + ":" + dataAtual + entradaProxy[1];
			if (attrs.get("proxyAddresses") == null) {
				attrs.put("proxyAddresses", emailRenomeado);
			} else {
				attrs.get("proxyAddresses").add(emailRenomeado);
			}

		}
		ldap.alterar(dn, attrs);
	}

	private String getDataAtual() {
		Calendar cal = Calendar.getInstance();
		int ano = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1;
		int dia = cal.get(Calendar.DAY_OF_MONTH);

		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int minuto = cal.get(Calendar.MINUTE);
		int segundo = cal.get(Calendar.SECOND);

		String strAno = String.valueOf(cal.get(Calendar.YEAR));
		String strMes = String.valueOf(mes < 10 ? "0" + mes : mes);
		String strDia = String.valueOf(dia < 10 ? "0" + dia : dia);

		String strHora = String.valueOf(hora < 10 ? "0" + hora : hora);
		String strMinuto = String.valueOf(minuto < 10 ? "0" + minuto : minuto);
		String strSegundo = String.valueOf(segundo < 10 ? "0" + segundo : segundo);

		return strAno + strMes + strDia + "_" + strHora + strMinuto + strMinuto + "_";

	}

	private void excluirGrupoSeguranca(AdGrupoDeSeguranca g) throws NamingException, AplicacaoException {
		if (conf.isModoExclusaoGrupoAtivo()) {
			// this.contexto.destroySubcontext("CN=" + g.getNome() + ","
			// + conf.getDnGruposSeguranca());
			ldap.excluir("CN=" + g.getNome() + "," + conf.getDnGruposSeguranca());
		} else {
			// alterarAtributo(g, "sAMAccountName", getDataAtual()
			// + conf.getPfxObjExcluido() + g.getNome()
			// + conf.getSfxObjExcluido());
			ldap.alterarAtributo(g.getNomeCompleto(), "sAMAccountName",
					getDataAtual() + conf.getPfxObjExcluido() + g.getNome() + conf.getSfxObjExcluido());
			String dnInativo = "CN=" + getDataAtual() + conf.getPfxObjExcluido() + g.getNome()
					+ conf.getSfxObjExcluido() + "," + conf.getDnGruposSegurancaInativos();
			if (pesquisarObjeto(dnInativo).size() <= 0) {
				log.warning(
						"Modo de exclusão desativado! Apenas moverá para o container de grupos de segurança inativos!");
				this.moverObjeto(g.getNomeCompleto(), dnInativo);
			} else {
				log.warning("Grupo de Seguranca já está inativo: " + g.getNome());
			}
		}

	}

	// private void alterarAtributo(AdObjeto o, String nomeAtributo, String
	// valor) {
	//
	// try {
	// // Attributes ats = this.contexto.getAttributes(o.getNomeCompleto());
	// Attributes ats = ldap.pesquisar(o.getNomeCompleto());
	// ModificationItem member[] = new ModificationItem[1];
	// Attribute at = ats.get(nomeAtributo);
	// at.clear();
	// at.add(valor);
	// member[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, at);
	// this.contexto.modifyAttributes(o.getNomeCompleto(), member);
	// } catch (AttributeInUseException e) {
	// // ignora porque já existe
	// } catch (NamingException e) {
	// e.printStackTrace();
	// }
	// }

	private void excluirUsuario(AdUsuario u) throws NamingException, AplicacaoException {
		if (conf.isModoExclusaoUsuarioAtivo()) {
			ldap.excluir("CN=" + u.getNome() + "," + conf.getDnUsuarios());
			// this.contexto.destroySubcontext("CN=" + u.getNome() + ","
			// + conf.getDnUsuarios());
		} else {
			String dnInativo = "CN=" + u.getNome() + "," + conf.getDnUsuariosInativos();
			List<AdObjeto> usuarioInativo = null;
			try {
				usuarioInativo = pesquisarObjeto(dnInativo);
			} catch (Exception e) {
				// ignora, pois apenas está verificando se usuário existe
			}
			if (usuarioInativo == null || usuarioInativo.size() <= 0) {
				log.warning("Modo de exclusão desativado! Apenas moverá para o container de usuários inativos!");
				// ativarUsuario(u, false);s
				ldap.desativarUsuario(u.getNomeCompleto());
				// this.alterarAtributo(u, "samAccountName", conf
				// .getPfxObjExcluido()
				// + u.getSigla() + conf.getSfxObjExcluido());
				ldap.alterarAtributo(u.getNomeCompleto(), "samAccountName",
						conf.getPfxObjExcluido() + u.getSigla() + conf.getSfxObjExcluido());
				this.moverObjeto(u.getNomeCompleto(), dnInativo);
			} else {
				log.warning("Usuário já está inativo: " + u.getNome());
			}
		}

	}

	private List<String> extrairFilhos(String dn) throws NamingException, AplicacaoException {
		// Attributes pai = this.contexto.getAttributes(dn);
//		Attributes pai = ldap.pesquisar(dn);

		List<String> filhos = new ArrayList<String>();

		// if (pai.get("objectClass").contains("organizationalUnit")) {

		// NamingEnumeration<NameClassPair> subordinados =
		// this.contexto.list(dn);
		try {

			String searchFilter = "objectCategory=*";
			SearchControls searchCtls = new SearchControls();
			searchCtls.setCountLimit(0);

			// Especifica o escopo
			searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

			// Ativa o resultado por página
			int pageSize = 1000;
			byte[] cookie = null;
			ldap.getContexto()
					.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			int total;

			do {
				/* executa a pesquisa */
				NamingEnumeration<SearchResult> subordinados = ldap.getContexto().search(dn, searchFilter, searchCtls);

				/* para cada subordinado inclua-o na lista de filhos */
				while (subordinados != null && subordinados.hasMore()) {
					SearchResult s = (SearchResult) subordinados.next();

					filhos.add(s.getNameInNamespace());

				}

				// Examina o controle de resposta dos resultados paginados
				Control[] controls = ldap.getContexto().getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							total = prrc.getResultSize();
							cookie = prrc.getCookie();
						}
					}
				} else {
					log.info("Nenhum controle enviado pelo servidor!");
				}
				// Reativa os resultados paginados
				ldap.getContexto().setRequestControls(
						new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
			} while (cookie != null);
		} catch (NamingException e) {
			System.err.println("A pesquisa paginada falhou!");
			e.printStackTrace();
		} catch (IOException ie) {
			System.err.println("A pesquisa paginada falhou!");
			ie.printStackTrace();
		}

		return filhos;
	}

	/**
	 * Monta um objeto AdGrupo a partir dos atributos LDAP. O atributo
	 * adminDescription foi escolhido para guardar a IdExterna já que esse atributo
	 * existe em todos os objetos do LDAP (objeto TOP do schema Active Diretory).
	 * 
	 * Segundo o site
	 * http://msdn.microsoft.com/en-us/library/ms675213(v=VS.85).aspx:
	 * 
	 * Admin-Description: The description displayed on admin screens.
	 * 
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	private AdGrupo extrairGrupo(Attributes attrs) throws NamingException {

		AdObjeto grPai = null;
		AdGrupo g = null;

		String commonName = attrs.get("cn").get().toString();
		String distinguishedName = attrs.get("distinguishedName").get().toString();

		if (isGrupoDistribuicaoManualEmail(commonName) || isGrupoDistribuicaoAuto(commonName)
				|| ldap.isGrupoDistribuicao(distinguishedName)) {
			g = new AdGrupoDeDistribuicao(commonName, commonName, conf.getDnDominio());

			return g;
		}

		if (isGrupoSegurancaAuto(commonName) || isGrupoSegurancaManualPerfil(commonName)
				|| isGrupoSegurancaManualPerfilJEE(commonName) || ldap.isGrupoSeguranca(distinguishedName)) {
			g = new AdGrupoDeSeguranca(commonName, commonName, conf.getDnDominio());

			return g;
		}

		return null;
	}

	private boolean isGrupoSegurancaAuto(String cnGrupo) {
		return (conf.getPfxGrpSegAuto().length() == 0 || cnGrupo.startsWith(conf.getPfxGrpSegAuto()))
				&& (conf.getSfxGrpSegAuto().length() == 0 || cnGrupo.endsWith(conf.getSfxGrpSegAuto()));
	}

	private boolean isGrupoSegurancaManualPerfil(String cnGrupo) {
		return (conf.getPfxGrpSegManualPerfil().length() == 0 || cnGrupo.startsWith(conf.getPfxGrpSegManualPerfil()))
				&& (conf.getSfxGrpSegManualPerfil().length() == 0 || cnGrupo.endsWith(conf.getSfxGrpSegManualPerfil()));
	}

	private boolean isGrupoSegurancaManualPerfilJEE(String cnGrupo) {
		return (conf.getPfxGrpSegManualPerfilJEE().length() == 0
				|| cnGrupo.startsWith(conf.getPfxGrpSegManualPerfilJEE()))
				&& (conf.getSfxGrpSegManualPerfilJEE().length() == 0
						|| cnGrupo.endsWith(conf.getSfxGrpSegManualPerfilJEE()));
	}

	private boolean isGrupoDistribuicaoAuto(String cnGrupo) {
		return (conf.getPfxGrpDistrAuto().length() == 0 || cnGrupo.startsWith(conf.getPfxGrpDistrAuto()))
				&& (conf.getSfxGrpDistrAuto().length() == 0 || cnGrupo.endsWith(conf.getSfxGrpDistrAuto()));
	}

	private List<AdObjeto> extrairMembros(AdGrupo g, Map<String, AdObjeto> membrosNaoIgnorados, List<AdObjeto> l) {
		AdObjeto resultado = null;
		
		String searchFilter = "(objectCategory=*)";
		SearchControls searchCtls = new SearchControls();

		// Especifica o escopo
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answer;

		try {
			answer = ldap.getContexto().search(g.getNomeCompleto(), searchFilter, searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();

				if (attrs != null) {
					List<AdObjeto> listaMembros = new ArrayList<AdObjeto>();
					if (attrs.get("objectClass").contains("group") && attrs.get("member") != null) {
						NamingEnumeration membros = attrs.get("member").getAll();
						while (membros.hasMoreElements()) {
							String dnMembro = membros.next().toString();
							log.fine("incluindo membro: " + dnMembro);
							// se alguém for ignorado, este não deve entrar no
							// grupo
							if (membrosNaoIgnorados.get(dnMembro) != null) {
								g.acrescentarMembro(membrosNaoIgnorados.get(dnMembro));
							} else {
								log.fine("Membro ignorado na leitura do AD: " + dnMembro + " (" + g.getNome() + ")");
								List<AdReferencia> ref = extrairReferencia(dnMembro);
//								g.acrescentarMembro(ref);
								referencias.addAll(ref);
//								l.add(ref);
							}
						}
						resultado = extrairGrupo(attrs);
						for (AdObjeto adObjeto : listaMembros) {
							((AdGrupo) resultado).acrescentarMembro(adObjeto);
						}
					}

					if (attrs.get("objectClass").contains("user")) {
						resultado = extrairUsuario(attrs);
					}

				}
			}
		} catch (NamingException e) {
			log.log(Level.SEVERE, "Erro NamingException: " + g.getNomeCompleto() + " -> " + e.getMessage());
		}

		return null;
	}

	/**
	 * Monta um objeto AdUnidadeOrganizacional a partir dos atributos LDAP. O
	 * atributo adminDescription foi escolhido para guardar a IdExterna já que esse
	 * atributo existe em todos os objetos do LDAP (objeto TOP do schema Active
	 * Diretory).
	 * 
	 * Segundo o site
	 * http://msdn.microsoft.com/en-us/library/ms675213(v=VS.85).aspx:
	 * 
	 * Admin-Description: The description displayed on admin screens.
	 * 
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	private AdUnidadeOrganizacional extrairUnidadeOrganizacional(Attributes attrs) throws NamingException {

		AdObjeto pai = null;
		AdUnidadeOrganizacional uo = null;

		uo = new AdUnidadeOrganizacional(attrs.get("ou").get().toString(), attrs.get("ou").get().toString(),
				conf.getDnDominio());

		return uo;
	}

	/**
	 * Monta um objeto AdUsuario a partir dos atributos LDAP. O atributo
	 * adminDescription foi escolhido para guardar a IdExterna já que esse atributo
	 * existe em todos os objetos do LDAP (objeto TOP do schema Active Diretory).
	 * 
	 * Segundo o site
	 * http://msdn.microsoft.com/en-us/library/ms675213(v=VS.85).aspx:
	 * 
	 * Admin-Description: The description displayed on admin screens.
	 * 
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	private AdUsuario extrairUsuario(Attributes attrs) throws NamingException {
		AdObjeto grPai = null;
		AdUsuario u = null;

		u = new AdUsuario(attrs.get("cn").get().toString(), attrs.get("cn").get().toString(), conf.getDnDominio());

		u.setSigla(attrs.get("sAMAccountName").get().toString());

		String emailUsuario = null;

		if (attrs.get("proxyAddresses") != null) {
			for (int i = 0; i < attrs.get("proxyAddresses").size(); i++) {
				if (attrs.get("proxyAddresses").get(i).toString().startsWith("smtp:")) {
					emailUsuario = attrs.get("proxyAddresses").get(i).toString().replace("smtp:", "");
					u.addEmail(emailUsuario);
				}
			}
		}

		u.setHomeMDB(attrs.get("homeMDB") != null ? attrs.get("homeMDB").get().toString() : "");
		u.setTemplateLink(
				attrs.get("msExchMailboxTemplateLink") != null ? attrs.get("msExchMailboxTemplateLink").get().toString()
						: "");

		u.setTipoPessoa(
				attrs.get("extensionAttribute1") != null ? attrs.get("extensionAttribute1").get().toString() : "");
		return u;
	}

	/**
	 * Monta uma lista de objetos AdReferencia a partir dos atributos LDAP.
	 * 
	 * @param attrs - atributos lidos do LDAP
	 * @return
	 * @throws NamingException
	 */
	private List<AdReferencia> extrairReferencia(String dnReferencia) throws NamingException {
		Set<String> listaEmails = cacheGetEmailPorDn.get(dnReferencia);
		List<AdReferencia> referencias = new ArrayList<AdReferencia>();
		if (listaEmails != null) {
			for (String nome : listaEmails) {
				if (nome == null) {
					nome = dnReferencia;
				}

				AdReferencia ext = new AdReferencia(nome, nome, conf.getDnDominio());

				ext.setReferencia(dnReferencia);
				referencias.add(ext);
			}

		}

		return referencias;
	}

	private String extrairCN(String dn) {
		String cn = null;
		try {
			cn = dn.split(",")[0].replaceAll("CN=", "");
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível extrair o CN do DN " + dn);
		}

		return cn;

	}

	/**
	 * Converte um objeto adObjeto em um objeto Attributes.
	 * 
	 * @param objeto - objeto a ser convertido
	 * @return um objto Attributes contendo as informações de adObjeto
	 * @throws AplicacaoException
	 * @throws NamingException
	 */
	private Attributes montarAtributos(AdObjeto objeto) throws AplicacaoException, NamingException {
		Attributes attrs = new BasicAttributes(true);

		if (objeto instanceof AdUnidadeOrganizacional) {
			AdUnidadeOrganizacional adUnidade = (AdUnidadeOrganizacional) objeto;

			attrs.put("objectClass", "organizationalUnit");
			attrs.put("cn", adUnidade.getNome());
			attrs.put("distinguishedName", adUnidade.getNomeCompleto());

			return attrs;
		}

		if (objeto instanceof AdGrupoDeDistribuicao) {
			AdGrupoDeDistribuicao adGrupo = (AdGrupoDeDistribuicao) objeto;

			attrs.put("objectClass", "group");
			attrs.put("cn", adGrupo.getNome());
			attrs.put("groupType", Integer.toString(ldap.ADS_GROUP_TYPE_UNIVERSAL_GROUP));
			attrs.put("distinguishedName", adGrupo.getNomeCompleto());
			attrs.put("samAccountName", adGrupo.getNome());

			montarAtributosMailboxGrupo(attrs, adGrupo);

			return attrs;
		}

		if (objeto instanceof AdGrupoDeSeguranca) {
			AdGrupoDeSeguranca adGrupo = (AdGrupoDeSeguranca) objeto;

			attrs.put("objectClass", "group");
			attrs.put("cn", adGrupo.getNome());
			attrs.put("samAccountName", adGrupo.getNome());
			attrs.put("groupType",
					Integer.toString(ldap.ADS_GROUP_TYPE_GLOBAL_GROUP | ldap.ADS_GROUP_TYPE_SECURITY_ENABLED));
			attrs.put("distinguishedName", adGrupo.getNomeCompleto());

			attrs.put("displayName", adGrupo.getNome());

			return attrs;
		}

		if (objeto instanceof AdGrupo) {
			AdGrupo adGrupo = (AdGrupo) objeto;

			attrs.put("objectClass", "group");
			attrs.put("cn", adGrupo.getNome());
			attrs.put("samAccountName", adGrupo.getNome());
			attrs.put("groupType", Integer.toString(ldap.ADS_GROUP_TYPE_LOCAL_GROUP));
			attrs.put("distinguishedName", adGrupo.getNomeCompleto());

			return attrs;
		}

		if (objeto instanceof AdUsuario) {
			AdUsuario adUsuario = (AdUsuario) objeto;

			attrs.put("objectClass", "user");
			attrs.put("samAccountName", adUsuario.getSigla());
			attrs.put("cn", adUsuario.getNome());
			attrs.put("distinguishedName", adUsuario.getNomeCompleto());

			attrs.put("userPrincipalName", adUsuario.getSigla() + conf.getUserPrincipalNameDomain());
			attrs.put("displayName", Texto.inciaisMaiuscula(adUsuario.getNomeResolucaoEmail()));
			attrs.put("extensionAttribute1", adUsuario.getTipoPessoa());

			// script de inicialização do usuário
			attrs.put("scriptPath", "kix32.exe");

			montarAtributosMailboxUsuario(attrs, adUsuario);

			log.fine("-----Atributos do usuário: " + adUsuario.getNomeCompleto());
			NamingEnumeration<String> ids = attrs.getIDs();
			while (ids.hasMoreElements()) {
				String id = (String) ids.nextElement();
				log.fine(id + ":" + attrs.get(id));
			}
			log.fine("-----Fim dos atributos do usuário: " + adUsuario.getNomeCompleto());

			return attrs;
		}

		if (objeto instanceof AdContato) {
			AdContato adContato = (AdContato) objeto;

			attrs.put("objectClass", "contact");
			attrs.put("cn", adContato.getNome());
			attrs.put("distinguishedName", adContato.getNomeCompleto());

			montarAtributosMailboxContato(attrs, adContato);

			return attrs;
		}

		return attrs;

	}

	private void montarAtributosMailboxGrupo(Attributes attrs, AdGrupo adGrupo) {
		// para cada e-mail: smtp:<e-mail> (não primário)
		// for (int i = 1; i < conf.getListaDominioEmail().size(); i++) {
		// attrs.put("proxyAddresses", "smtp:" + adUsuario.getSigla() +
		// conf.getListaDominioEmail().get(i));
		// }

		String dominioPrimario = conf.getListaDominioEmail().get(0);
		attrs.put("msExchPoliciesExcluded", conf.getExchPoliciesExcluded());
		String email = "";
		try {
			if (isGrupoDistribuicaoManualEmail(adGrupo.getNome())) {

				for (int i = 0; i < conf.getListaDominioEmail().size(); i++) {
					if (i == 0) {
						attrs.put("proxyAddresses", "SMTP:" + removerSufixoGrupo(adGrupo) + dominioPrimario);
						continue;
					}

					email = removerSufixoGrupo(adGrupo) + conf.getListaDominioEmail().get(i);

					verificarSeEmailDisponivelGrpManual(adGrupo, email);

					attrs.get("proxyAddresses").add("smtp:" + email);
					attrs.get("proxyAddresses").add("smtp:" + conf.getPfxGrpDistrManualEmail()
							+ removerSufixoGrupo(adGrupo) + conf.getListaDominioEmail().get(i));

				}
				// se for grupo automático
			} else {
				// inicia em 1 para ignorar o SMTP primário
				for (int i = 0; i < conf.getListaDominioEmail().size(); i++) {
					if (i == 0) {
						attrs.put("proxyAddresses", "SMTP:" + removerSufixoGrupo(adGrupo) + dominioPrimario);
						continue;
					}
					email = removerSufixoGrupo(adGrupo) + conf.getListaDominioEmail().get(i);

					verificarSeEmailDisponivelGrpAuto(adGrupo, email);

					attrs.get("proxyAddresses").add("smtp:" + email);
				}
			}
		} catch (NamingException e) {
			throw new RuntimeException(e.getCause());
		}
		attrs.put("mail", removerSufixoGrupo(adGrupo) + dominioPrimario);

		attrs.put("displayName", removerSufixoGrupo(adGrupo));

		attrs.put("mailNickname", removerSufixoGrupo(adGrupo));

		attrs.put("legacyExchangeDN", conf.getLegacyExchangeDN() + removerSufixoGrupo(adGrupo));
		attrs.put("msExchUserAccountControl", "0");

		attrs.put("msExchVersion", String.valueOf(Long.parseLong(EXCH_VERSION, 16)).getBytes());

		attrs.put("msExchRecipientDisplayType", EXCH_DISPLAY_TYPE_GRUPO);

		for (int i = 0; i < conf.getListaShowInAddressBookGrupo().size(); i++) {
			if (i == 0) {
				attrs.put("showInAddressBook", conf.getListaShowInAddressBookGrupo().get(i));
			} else {
				attrs.get("showInAddressBook").add(conf.getListaShowInAddressBookGrupo().get(i));
			}
		}

		attrs.put("msExchHideFromAddressLists", "TRUE");
		attrs.put("reportToOriginator", "TRUE");

	}

	private String verificarSeEmailDisponivelGrpAuto(AdGrupo adGrupo, String email) throws NamingException {
		String utilizadorDoEmail = emailEmUso(email);
		if (utilizadorDoEmail != null && !utilizadorDoEmail.equals(adGrupo.getNomeCompleto())) {
			String utilizadorManual = utilizadorDoEmail.split(",")[0]
					.replace(conf.getSfxGrpDistrManualEmail(), conf.getSfxGrpDistrAuto()).replace("CN=", "");
			if (!adGrupo.getNome().equals(utilizadorManual)) {
				throw new RuntimeException("O e-mail " + email + " já está em uso pelo objeto " + utilizadorDoEmail);
			}

		}
		return utilizadorDoEmail;
	}

	private String verificarSeEmailDisponivelGrpManual(AdGrupo adGrupo, String email) throws NamingException {
		String utilizadorDoEmail = emailEmUso(email);
		if (utilizadorDoEmail != null && !utilizadorDoEmail.equals(adGrupo.getNomeCompleto())) {
			String utilizadorAuto = adGrupo.getNome().replace(conf.getSfxGrpDistrManualEmail(),
					conf.getSfxGrpDistrAuto());

			if (!utilizadorDoEmail.startsWith("CN=" + utilizadorAuto + ",")) {
				throw new RuntimeException("O e-mail " + email + " já está em uso pelo objeto " + utilizadorDoEmail);
			}

		}
		return utilizadorDoEmail;
	}

	private String removerSufixoGrupo(AdGrupo adGrupo) {
//		if(isGrupoDistribuicaoManualEmail(adGrupo.getNome())){
//			return adGrupo.getNome().replace(
//					conf.getSfxGrpDistrManualEmail(), "");
//		}else{
//			return adGrupo.getNome().replace(
//					conf.getSfxGrpDistrAuto(), "");
//		}
		return adGrupo.getNome();
	}

	public boolean isGrupoDistribuicaoManualEmail(String cnGrupo) {
		return (conf.getPfxGrpDistrManualEmail().length() == 0 || cnGrupo.startsWith(conf.getPfxGrpDistrManualEmail()))
				&& (conf.getSfxGrpDistrManualEmail().length() == 0
						|| cnGrupo.endsWith(conf.getSfxGrpDistrManualEmail()));
	}

	/**
	 * Define os atributos referentes à caixa de e-mail do Microsoft Exchange. Os
	 * atributos podem ser consultados em
	 * http://support.microsoft.com/kb/296479/en-us Exemplo do objeto CN=Markenson
	 * Paulo França
	 * 
	 * legacyExchangeDN /o=JFRJ/ou=Exchange Administrative Group
	 * (FYDIBOHF23SPDLT)/cn=Recipients/cn=kpf
	 * 
	 * proxyAddresses smtp:markenson@jfrj.gov.br SMTP:markenson@jfrj.jus.br
	 * smtp:kpf@corp.jfrj.gov.br smtp:kpf@jfrj.gov.br smtp:kpf@jfrj.jus.br
	 * 
	 * textEncodedORAddress não definido
	 * 
	 * mail markenson@jfrj.jus.br
	 * 
	 * mailNickname kpf
	 * 
	 * displayName Markenson Paulo França
	 * 
	 * msExchHomeServerName /o=JFRJ/ou=Exchange Administrative Group
	 * (FYDIBOHF23SPDLT)/cn=Configuration/cn=Servers/cn=MASTER
	 * 
	 * homeMDB CN=SupervChefeAssOfJust,CN=SupervChefeAssOfJustOriginal,CN=
	 * InformationStore,CN=MASTER,CN=Servers,CN=Exchange Administrative Group
	 * (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=JFRJ,CN=Microsoft
	 * Exchange,CN=Services,CN=Configuration,DC=corp,DC=jfrj,DC=gov,DC=br
	 * 
	 * homeMTA CN=Microsoft MTA,CN=MASTER,CN=Servers,CN=Exchange Administrative
	 * Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=JFRJ,CN=Microsoft
	 * Exchange,CN=Services,CN=Configuration,DC=corp,DC=jfrj,DC=gov,DC=br
	 * 
	 * msExchUserAccountControl 0
	 * 
	 * msExchMasterAccountSid não definido
	 * 
	 * msExchMailboxGuid {42B2B13E-80D2-4B19-A803-70D3DB834F6D}
	 * 
	 * 
	 * @param attrs     - conjunto de atributos que receberão as informações
	 *                  específicas do exchange
	 * @param adUsuario - objeto que contém as informações que serão lidas para o AD
	 * @throws AplicacaoException
	 * @throws NamingException
	 */
	private void montarAtributosMailboxUsuario(Attributes attrs, AdUsuario adUsuario)
			throws AplicacaoException, NamingException {
		if (!adUsuario.getCriarEmail()) {
			return;
		}

		attrs.put("msExchPoliciesExcluded", conf.getExchPoliciesExcluded());

		attrs.put("mDBUseDefaults", conf.getMDBUseDefaults());
		// e-mail primario
		String dominioPrimario = conf.getListaDominioEmail().get(0);

		// determina os emails do usuário
		definirEmailsDoUsuario(attrs, adUsuario, dominioPrimario);

		attrs.put("legacyExchangeDN", conf.getLegacyExchangeDN() + adUsuario.getSigla());

		attrs.put("msExchHomeServerName", conf.getExchHomeServerName());

		// a string depende do cargo/funcao
		String homeMDB = "";
		String templateLink = "";
		try {
			long matricula = Long.valueOf(adUsuario.getNome().substring(2));
			RegraCaixaPostal regra = ResolvedorRegrasCaixaPostal.getInstancia(conf).getRegraPorMatricula(matricula);

			if (regra != null) {
				homeMDB = regra.getHomeMDB();
				templateLink = regra.getTemplateLink();
			}

		} catch (AplicacaoException e) {
			log.warning("Regras de caixas postais indefinidas! O Sincronismo dará erro!");
		}

		attrs.put("homeMDB", homeMDB);
		
		// Atributo abaixo foi comentado após equipe de infra apontar problema na tentativa de inclusão de usuario pelo GI
		// de DEZ/2023.
		// javax.naming.InvalidNameException: CN=RJ63462,OU=Usuarios,OU=Gestao de Identidade,DC=corp,DC=jfrj,DC=gov,DC=br: 
		// [LDAP: error code 34 - 00000057: LdapErr: DSID-0C090EF7, comment: Error in attribute conversion operation, data 0, v23f0
//		attrs.put("msExchMailboxTemplateLink", templateLink);

		attrs.put("homeMTA", conf.getHomeMTA());
		attrs.put("msExchUserAccountControl", "0");

		attrs.put("msExchRecipientTypeDetails", EXCH_DISPLAY_TYPE_GRUPO);

		attrs.put("msExchVersion", String.valueOf(Long.parseLong(EXCH_VERSION, 16)).getBytes());

		// mailbox do markenson (deve ser preenchido com o valor da mailbox
		// criada pelo exchange
		// TAMANHO:16 bytes
		// attrs.put("msExchMailboxGuid", "1234567890123456".getBytes());

		// sempre cria um GUID (somente vai pro AD se for uma inclusão)
		attrs.put("msExchMailboxGuid", criarNovoGUID());

		// no site da M$ diz que são obrigatórios mas não existem no nosso AD
		// attrs.put("textEncodedORAddress", "?" );
		// attrs.put("msExchMasterAccountSid", "?");

		attrs.put("msExchRecipientDisplayType", EXCH_DISPLAY_TYPE_USUARIO);

		for (int i = 0; i < conf.getListaShowInAddressBookUsuario().size(); i++) {
			if (i == 0) {
				attrs.put("showInAddressBook", conf.getListaShowInAddressBookUsuario().get(i));
			} else {
				attrs.get("showInAddressBook").add(conf.getListaShowInAddressBookUsuario().get(i));
			}
		}

	}

	private byte[] criarNovoGUID() {
		return hexStringToByteArray(UUID.randomUUID().toString().replace("-", ""));
	}

	private void definirEmailsDoUsuario(Attributes attrs, AdUsuario adUsuario, String dominioPrimario)
			throws AplicacaoException, NamingException {

		Attribute proxyAddressesUsuario = null;
		// proxyAddressesUsuario = this.contexto.getAttributes(
		// adUsuario.getNomeCompleto()).get("proxyAddresses");
		Attributes attrsUsuario = null;
		try {
			attrsUsuario = ldap.pesquisar(adUsuario.getNomeCompleto());
		} catch (Exception e) {
			// ignora se não encontrar
		}
		proxyAddressesUsuario = attrsUsuario == null ? null : attrsUsuario.get("proxyAddresses");

		if (proxyAddressesUsuario != null) {
			attrs.put(proxyAddressesUsuario);
		} else {
			if (adUsuario.getNomeResolucaoEmail() != null) {
				rNomeEmail.setNome(adUsuario.getNomeResolucaoEmail());
				rNomeEmail.setMatricula(adUsuario.getNome());
				String[] emailsPossiveis = rNomeEmail.getNomesResolvidos();
				String emailEscolhido = null;
				try {
					for (int i = 0; i < emailsPossiveis.length; i++) {
						if (isEmailEmUsoPeloUsuario(emailsPossiveis[i], adUsuario.getNome())
								|| emailEmUso(emailsPossiveis[i]) == null) {
							emailEscolhido = emailsPossiveis[i];
							break;
						}
					}
					if (emailEscolhido == null) {
						log.warning("Não foi possível definir um e-mail através das regras existentes! ("
								+ adUsuario.getNome() + ")");
						// System.out.println("Não foi possível definir um e-mail através das regras
						// existentes! ("
						// + adUsuario.getNome() + ")");
//						throw new AplicacaoException(
//								"Não foi possível definir um e-mail através das regras existentes! ("
//										+ adUsuario.getNome() + ")");
					}
				} catch (NamingException e) {
					throw new AplicacaoException("Não foi possível verificar se o e-mail está em uso!");
				}

				// para cada e-mail: smtp:<e-mail> (não primário)
				for (int i = 0; i < conf.getListaDominioEmail().size(); i++) {
					String mail = emailEscolhido + conf.getListaDominioEmail().get(i);
					if (i == 0) {
						attrs.put("proxyAddresses", "SMTP:" + mail);
					} else {
						attrs.get("proxyAddresses").add("smtp:" + mail);
					}
				}

				for (int i = 0; i < conf.getListaDominioEmail().size(); i++) {
					String mail = adUsuario.getSigla() + conf.getListaDominioEmail().get(i);
					attrs.get("proxyAddresses").add("smtp:" + mail);
				}

			}

		}

		attrs.put("mail", adUsuario.getSigla() + dominioPrimario);
		attrs.put("mailNickname", adUsuario.getSigla());
	}

	private boolean isEmailEmUsoPeloUsuario(String email, String nome) throws NamingException {
		if (cacheGetEmailPorCn != null && cacheGetEmailPorCn.size() > 0) {
			if (cacheGetEmailPorCn.get(email) != null && cacheGetEmailPorCn.get(email).size() > 0) {
				return true;
			}
		}
		String searchFilter = "(&(|(objectClass=user)(objectClass=group))(&((proxyAddresses=*:" + email + "@*)(cn="
				+ nome + "))))";
		SearchControls searchCtls = new SearchControls();
		searchCtls.setCountLimit(0);

		// Especifica o escopo
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> resultado = ldap.getContexto().search(conf.getDnGestaoIdentidade(),
				searchFilter, searchCtls);
		if (resultado.hasMore()) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica se o e-mail já está em uso na árvore
	 * 
	 * @param email
	 * @return - null, se o e-mail não existe Dn do objeto que usa o e-mail, se o
	 *         e-mail já estiver em uso.
	 * @throws NamingException
	 */
	public String emailEmUso(String email) throws NamingException {
		String searchFilter = "(&(|(objectClass=user)(objectClass=group))((proxyAddresses=*:" + email + "*)))";
		SearchControls searchCtls = new SearchControls();
		searchCtls.setCountLimit(0);

		// Especifica o escopo
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		List<String> pontosDePesquisa = conf.getListaPontosPesquisaEmail();

		for (String p : pontosDePesquisa) {
			NamingEnumeration<SearchResult> resultado = ldap.getContexto().search(p, searchFilter, searchCtls);

			if (resultado.hasMore()) {
				return ((SearchResult) resultado.nextElement()).getAttributes().get("distinguishedName").get()
						.toString();
			}
		}

		return null;
	}

	/**
	 * Define os atributos necessários para que o Exchange reconheça o contato como
	 * uma objeto válido.
	 * 
	 * Ref: http://support.microsoft.com/kb/296479/en-us
	 * http://support.microsoft.com/kb/318072/en-us
	 * 
	 * @param attrs
	 * @param adContato
	 */
	private void montarAtributosMailboxContato(Attributes attrs, AdContato adContato) {
		attrs.put("msExchPoliciesExcluded", conf.getExchPoliciesExcluded());

		// para cada e-mail: SMTP:<e-mail>
		attrs.put("proxyAddresses", "SMTP:" + adContato.getIdExterna());
		attrs.put("targetAddress", "SMTP:" + adContato.getIdExterna());

		attrs.put("mail", adContato.getIdExterna());
		attrs.put("displayName", adContato.getIdExterna());

		attrs.put("mailNickname", adContato.getIdExterna().substring(0, adContato.getIdExterna().indexOf("@")));

		attrs.put("legacyExchangeDN", conf.getLegacyExchangeDN() + adContato.getIdExterna());

		attrs.put("msExchRecipientDisplayType", EXCH_DISPLAY_TYPE_CONTATO);

		attrs.put("msExchHideFromAddressLists", conf.getContatosOcultosDasListas());
	}

	// Converting a string of hex character to bytes
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	// Converting a bytes array to string of hex character
	public static String byteArrayToHexString(byte[] b) {
		int len = b.length;
		String data = new String();

		for (int i = 0; i < len; i++) {
			data += Integer.toHexString((b[i] >> 4) & 0xf);
			data += Integer.toHexString(b[i] & 0xf);
		}
		return data;
	}

	/**
	 * Pesquisa um objeto na árvore LDAP
	 * 
	 * @param dn
	 * @param oPai
	 * @param l
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	private void pesquisarObjeto(String dn, AdObjeto oPai, List<AdObjeto> l)
			throws NamingException, AplicacaoException {
		pesquisarObjeto(dn, oPai, l, null);
	}

	/**
	 * Pesquisa um objeto na árvore LDAP
	 * 
	 * @param dn
	 * @param oPai
	 * @param l
	 * @param ignorar - lista de objetos a serem ignorados
	 * @throws NamingException
	 * @throws AplicacaoException
	 */
	private void pesquisarObjeto(String dn, AdObjeto oPai, List<AdObjeto> l, List<String> ignorar)
			throws NamingException, AplicacaoException {
		dn = LdapUtils.escapeDN(dn);
		
		AdObjeto objetoAd = null;

		Attributes attrs = null;
		try {
			attrs = ldap.pesquisar(dn);
		} catch (Exception e) {
			// ignora
		}

		if (attrs == null) {
			return;
		}
		// attrs = this.contexto.getAttributes(dn);

		// tratarGUID(attrs);

		if (attrs.get("objectClass").contains("organizationalUnit")) {
			objetoAd = extrairUnidadeOrganizacional(attrs);
		}

		if (attrs.get("objectClass").contains("group")) {
			objetoAd = extrairGrupo(attrs);
		}

		if (attrs.get("objectClass").contains("user")) {
			objetoAd = extrairUsuario(attrs);
		}

		if (attrs.get("objectClass").contains("contact")) {
			objetoAd = extrairContato(attrs);
		}

		if (objetoAd != null) {
			objetoAd.setGrupoPai((AdUnidadeOrganizacional) oPai);
			l.add(objetoAd);

			gravarCacheEmail(attrs);

			List<String> filhos = extrairFilhos(dn);

			Boolean filhoIgnorado = false;
			for (String f : filhos) {
				if (ignorar != null && ignorar.size() > 0) {

					for (String s : ignorar) {
						if (s.equalsIgnoreCase(f)) {
							filhoIgnorado = true;
							break;
						}
					}
				}
				if (!filhoIgnorado) {
					pesquisarObjeto(f, objetoAd, l);
				}
				filhoIgnorado = false;
				// ((AdGrupo) objetoAd).acrescentarMembro(filho);
			}
		}
	}

	private void gravarCacheEmail(Attributes atributos) {
		if (atributos.get("proxyAddresses") != null) {

			try {
				NamingEnumeration emails = atributos.get("proxyAddresses").getAll();
				Attributes attrs = new BasicAttributes(true);
				String cn = atributos.get("cn").get().toString();
				String dn = atributos.get("distinguishedName").get().toString();

				Set<String> listaCacheGetEmailPorCn = cacheGetEmailPorCn.get(cn);
				if (listaCacheGetEmailPorCn == null) {
					listaCacheGetEmailPorCn = new HashSet<String>();
					cacheGetEmailPorCn.put(cn, listaCacheGetEmailPorCn);
				}

				Set<String> listaCacheGetEmailPorDn = cacheGetEmailPorDn.get(dn);
				if (listaCacheGetEmailPorDn == null) {
					listaCacheGetEmailPorDn = new HashSet<String>();
					cacheGetEmailPorDn.put(dn, listaCacheGetEmailPorDn);
				}

				while (emails.hasMore()) {
					String email = emails.nextElement().toString();
					if (email.startsWith("SMTP:") || email.startsWith("smtp:")) {
						String[] entradaProxy = email.split(":");

						listaCacheGetEmailPorCn.add(entradaProxy[1]);
						listaCacheGetEmailPorDn.add(entradaProxy[1]);

						Set<String> listaDN = cacheGetDnPorEmail.get(entradaProxy[1]);
						if (listaDN == null) {
							listaDN = new HashSet<String>();
							cacheGetDnPorEmail.put(entradaProxy[1], listaDN);
						}

						listaDN.add(dn);

					}
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public boolean objetoExiste(String cn) {
		return ldap.existe(cn);
	}

	private AdObjeto extrairContato(Attributes attrs) throws NamingException {
		AdContato c = null;

		c = new AdContato(attrs.get("cn").get().toString(), attrs.get("cn").get().toString(), conf.getDnDominio());

		if (attrs.get("mail") != null) {
			c.setIdExterna(attrs.get("mail").get().toString());
		}

		return c;
	}

	class LDAPListener implements NamespaceChangeListener, ObjectChangeListener {

		Logger log = Logger.getLogger(LDAPListener.class.getName());

		@Override
		public void namingExceptionThrown(NamingExceptionEvent evt) {
			// log.warning("erro:" + evt.getException().getMessage());
		}

		@Override
		public void objectAdded(NamingEvent evt) {
			log.fine("antigo:" + evt.getOldBinding().getName());
			log.info("novo:" + evt.getNewBinding().getName());
		}

		@Override
		public void objectChanged(NamingEvent evt) {
			log.fine("antigo:" + evt.getOldBinding().getName());
			log.fine("novo:" + evt.getNewBinding().getName());
		}

		@Override
		public void objectRemoved(NamingEvent evt) {
			log.fine("antigo:" + evt.getOldBinding().getName());
			log.fine("novo:" + evt.getNewBinding().getName());
		}

		@Override
		public void objectRenamed(NamingEvent evt) {
			log.fine("antigo:" + evt.getOldBinding().getName());
			log.fine("novo:" + evt.getNewBinding().getName());
		}

	}

	public List<String> verAtributos(AdObjeto o) {
		List<String> resultado = new ArrayList<String>();

		try {
			Attributes attrs = montarAtributos(o);

			NamingEnumeration<String> iAttrs = attrs.getIDs();
			while (iAttrs.hasMoreElements()) {
				Attribute attr = attrs.get(iAttrs.next());
				if (attr.getID().equals("msExchMailboxGuid")) {
					resultado.add("msExchMailboxGuid:{" + UUID.nameUUIDFromBytes((byte[]) (attr.get())) + "}");
				} else {
					resultado.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr).toString()
							.replace("Replace attribute: ", ""));
				}

			}
		} catch (AplicacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (o instanceof AdGrupo) {
			StringBuilder membros = new StringBuilder("Membros: [");
			for (AdObjeto m : ((AdGrupo) o).getMembros()) {
				membros.append(m.getNome());
				membros.append(",");
			}
			membros.deleteCharAt(membros.length() - 1);
			membros.append("]");
			resultado.add(membros.toString());
		}

		return resultado;

	}

	@SuppressWarnings("static-access")
	public void limparSenhaSinc(Sincronizavel novo) {
		if (conf.isModoEscrita()) {
			try {
				if (novo instanceof AdUsuario) {

					CpDao dao = CpDao.getInstance();
					dao.iniciarTransacao();
					AdUsuario u = ((AdUsuario) novo);
					DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
					flt.setNome(u.getNome());
					List<DpPessoa> listaPessoa = dao.consultarPorFiltro(flt);

					for (DpPessoa p : listaPessoa) {
						if (u.getSenhaCripto() != null) {
							for (CpIdentidade cpId : dao.consultaIdentidades(p)) {
								if (cpId.getCpTipoIdentidade().getIdCpTpIdentidade().equals(1)) {

//									log(p.getSigla() + ": Definindo SENHA_IDENTIDADE_CRIPTO_SINC como NULL NO banco de dados...");
									cpId.setDscSenhaIdentidadeCriptoSinc(null);

								}
							}
						}

						if (u.getNomeExibicao() != null) {
//							log(p.getSigla() + ": Definindo NOME_EXIBICAO como NULL no banco de dados...");
							p.setNomeExibicao(null);
						}

					}

					dao.commitTransacao();

				}

			} catch (AplicacaoException e) {
				System.out.println(e);
			}
		}
	}

	public void ativarUsuario(String dnUsuario) throws AplicacaoException {
		ldap.ativarUsuario(dnUsuario);
	}

	public void alterarAtributo(String dnOrigem, String atributo, Object valor) throws AplicacaoException {
		ldap.alterarAtributo(dnOrigem, atributo, valor);
	}

	public void removerItemCampoMultivalorado(String dn, String atributoMV, String contendoValor, String... exceto) {
		boolean atualizarAtributo = false;
		Attributes attrsOriginal = ldap.getAttributes(dn);
		Attributes attrsNovo = new BasicAttributes(true);
		NamingEnumeration<String> attrsIDs = attrsOriginal.getIDs();
		try {
			while (attrsIDs.hasMore()) {
				String attrID = attrsIDs.next();
				if (attrID.equals(atributoMV)) {
//					Attribute attrOriginal = attrsOriginal.get(attrID);

					if (attrsOriginal.get(atributoMV) != null) {
						NamingEnumeration itensOriginais = attrsOriginal.get(atributoMV).getAll();

						while (itensOriginais.hasMore()) {
							String item = itensOriginais.next().toString();
							if (item.contains(contendoValor) && !estaListaExcecao(item, exceto)) {
								atualizarAtributo = true;
								// ignora valor
								log.fine("Removendo [" + item + "] de [" + atributoMV + "] para [" + dn + "]");
							} else {
								if (attrsNovo.get(atributoMV) == null) {
									attrsNovo.put(atributoMV, item);
								} else {
									attrsNovo.get(atributoMV).add(item);
								}
							}
						}

					}

					break;
				}
			}

			if (atualizarAtributo) {
				ldap.alterar(dn, attrsNovo);
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AplicacaoException e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAtributo(String dn, String nomeAtributo) throws AplicacaoException {
		try {
			return ldap.getAttributes(dn).get(nomeAtributo).get().toString();
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível obter o atributo " + nomeAtributo);
		}

	}

	private boolean estaListaExcecao(String item, String[] exceto) {
		if (exceto == null) {
			return false;
		}

		for (String ex : exceto) {
			if (item.equalsIgnoreCase(ex)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, Set<String>> getCacheDnPorEmail() {
		return cacheGetDnPorEmail;
	}

	public List<AdReferencia> getReferencias() {
		return referencias;
	}

}
