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
package br.gov.jfrj.importar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.cfg.Configuration;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpPapel;
import br.gov.jfrj.siga.cp.CpTipoPapel;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.cp.bl.CpPropriedadeBL;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.CpTipoPessoa;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Item.Operacao;
import br.gov.jfrj.siga.sinc.lib.OperadorComHistorico;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelComparator;
import br.gov.jfrj.siga.util.ImportarXmlProperties;
import net.sf.ehcache.CacheManager;

public class SigaCpSinc {

	// a rotina do Markenson está fazendo coisas demais
	// a rotina de sync está atribuindo um nível de dependencia estranho para
	// renato (8)

	private static boolean modoLog = false;
	protected static int maxSinc = -1;

	private String servidor = "";

	private String url = "";

	private static String destinatariosExtras = "";
	private static Level logLevel = Level.WARNING;

	public String getServidor() {
		return servidor;
	}

	public String getUrl() {
		return url;
	}

	public Date getDt() {
		return dt;
	}

	private SincronizavelComparator sc = new SincronizavelComparator();

	protected SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);

	protected SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(
			sc);

	public long orgaoUsuario;

	protected CpOrgaoUsuario cpOrgaoUsuario = null;

	private DpPessoa dpPessoaCorrente = null;

	private String dataHora;

	private String versao;

	List<Item> list;

	/*
	 * // para compatibilizar os arquivos novos e antigos
	 */
	/*
	 * private boolean seXmlTemPapel = false; private boolean seXmlTemOrgao =
	 * false;
	 */
	// listas de Tipos
	private List<CpTipoLotacao> tiposLotacao;
	private List<CpTipoPessoa> tiposPessoa;
	private List<CpTipoPapel> tiposPapel;
	/*
	 * Objetos fixos
	 */

	private DpLotacao lotacaoSJRJ;
	private DpLotacao lotacaoDIRFO;
	//
	private Logger logger = Logger.getLogger("br.gov.jfrj.log.sinc");
	/*
	 * verificar duplicidades (árvore de Hashtables : nivel 0: orgaoUsuario,
	 * nivel 1: entidade, nivel 2: atributo,valor
	 */
	Hashtable<String, Hashtable<String, Hashtable<String, Hashtable<String, String>>>> unicidades = new Hashtable<String, Hashtable<String, Hashtable<String, Hashtable<String, String>>>>();

	protected Date dt;
	private SincLogHandler logHandler = new SincLogHandler();
	protected String[] args;

	protected void setOrgaoUsuario(long orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	//
	@SuppressWarnings("static-access")
	public void gravar(Date dt) throws Exception {
		// List<Item> list;
		Sincronizador sinc = new Sincronizador();
		try {
			sinc.religarListaPorIdExterna(setNovo);
			sinc.setSetNovo(setNovo);
			// sinc.religarListaPorIdExterna(setAntigo);
			sinc.setSetAntigo(setAntigo);

			// verifica se as pessoas possuem lotação

			if (modoLog) {
				for (Sincronizavel item : setNovo) {
					if (item instanceof DpPessoa) {
						DpPessoa p = ((DpPessoa) item);
						if (p.getLotacao() == null) {
							log("Pessoa sem lotação! " + p.getSigla());
						}
					}
				}
			}
			list = sinc.getOperacoes(dt);
		} catch (Exception e) {
			log("Transação abortada por erro: " + e.getMessage());
			throw new Exception("Erro na gravação", e);
		}
		try {
			CpDao.getInstance().em().getTransaction().begin();
			OperadorComHistorico o = new OperadorComHistorico() {
				public Sincronizavel gravar(Sincronizavel s) {
					Sincronizavel o = CpDao.getInstance().gravar(s);
					CpDao.getInstance().descarregar();
					return o;
				}
			};

			for (Item opr : list) {
				log(opr.getDescricao());

				if (opr.getNovo() != null && opr.getNovo() instanceof DpLotacao)
					if (opr.getAntigo() != null)
						opr.getAntigo().semelhante(opr.getNovo(), 0);
				sinc.gravar(opr, o, true);
				if (opr.getAntigo() != null) {
					manterNomeExibicao(opr.getAntigo(), opr.getNovo());
				}

				manterHistoricoSeNecessario(opr);

			}
			/*
			 * sqls para inspeção no display :
			 * 
			 * HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.dp_cargo where ID_ORGAO_USU = 9999"
			 * ).list(); HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.dp_funcao_confianca where ID_ORGAO_USU = 9999"
			 * ).list(); HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.cp_orgao where ID_ORGAO_USU = 9999"
			 * ).list(); HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.dp_lotacao where ID_ORGAO_USU = 9999"
			 * ).list(); HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.dp_pessoa where ID_ORGAO_USU = 9999"
			 * ).list(); HibernateUtil.getSessao().createSQLQuery(
			 * "select * from corporativo.cp_papel where ID_ORGAO_USU = 9999"
			 * ).list()
			 */
			if (modoLog) {
				log("");
				log("*********MODO LOG **********");
				log("As alterações não serão efetivadas! Executando rollback...");
				log("");
				log("");
				CpDao.getInstance().rollbackTransacao();
			} else if (maxSinc > 0 && list.size() > maxSinc) {
				log("");
				log("");
				log("***ATENÇÃO***: Limite de operações por sincronismo excedido!");
				log("Operações a serem executadas: " + list.size()
						+ "\nOperações permitidas: " + maxSinc);
				log("Ajuste o parâmetro -maxSinc=<VALOR> para permitir que o sincronismo seja efetivado!");
				log("As alterações não serao efetivadas! Executando rollback...");
				log("");
				log("");

				CpDao.getInstance().em().getTransaction().rollback();
			} else {

				CpDao.getInstance().em().getTransaction().commit();
				log("Transação confirmada");
			}
		} catch (Exception e) {
			CpDao.getInstance().rollbackTransacao();
			log("Transação abortada por erro: " + e.getMessage());
			throw new Exception("Erro na gravação", e);
		}

		CpDao.getInstance().descarregar();
		log("Total de alterações: " + list.size());
		// ((GenericoHibernateDao) dao).getSessao().flush();
	}

	/**
	 * Verifica se a entidade que está sendo incluída à uma entidade que já
	 * existe e foi removida indevidamente ocasionando a perda do histórico
	 * 
	 * @param opr
	 * @return
	 */
	private void manterHistoricoSeNecessario(Item opr) {
		// Pessoa
		if (opr.getNovo() != null && opr.getNovo() instanceof DpPessoa) {
			DpPessoa pesNova = (DpPessoa) opr.getNovo();
			if (opr.getOperacao().equals(Operacao.incluir)) {
				List<DpPessoa> historicoPessoa = CpDao.getInstance()
						.consultarPorMatriculaEOrgao(pesNova.getMatricula(),
								pesNova.getOrgaoUsuario().getId(), true, true);
				log("*****************************" + pesNova.getMatricula());
				if (historicoPessoa.size() > 0) {
					DpPessoa pesAnterior = historicoPessoa.get(0);
					if (pesAnterior != null
							&& !pesAnterior.getIdInicial().equals(
									pesNova.getIdInicial())) {
						pesNova.setIdInicial(pesAnterior.getIdInicial());
						log("AVISO (PESSOA): ID_INICIAL reconectada ("
								+ pesNova.getSigla() + ")");
					}

				}
			}

		}

		// Lotacao
		if (opr.getNovo() != null && opr.getNovo() instanceof DpLotacao) {

			DpLotacao novo = (DpLotacao) opr.getNovo();
			if (opr.getOperacao().equals(Operacao.incluir)) {
				List<DpLotacao> historico = (List<DpLotacao>) CpDao
						.getInstance().consultarFechadosPorIdExterna(
								DpLotacao.class, novo.getIdExterna(),
								novo.getOrgaoUsuario().getId());

				if (historico.size() > 0) {
					DpLotacao anterior = historico.get(0);
					if (anterior != null
							&& (anterior.getIdExterna().equals(novo
									.getIdExterna()))
							&& (!anterior.getIdInicial().equals(
									novo.getIdInicial()))) {
						novo.setIdInicial(anterior.getIdInicial());
						log("AVISO (LOTACAO): ID_INICIAL reconectada ("
								+ novo.getIdInicial() + ")");
					}

				}
			}

		}

		// Cargo
		if (opr.getNovo() != null && opr.getNovo() instanceof DpCargo) {

			DpCargo novo = (DpCargo) opr.getNovo();
			if (opr.getOperacao().equals(Operacao.incluir)) {
				List<DpCargo> historico = (List<DpCargo>) CpDao.getInstance()
						.consultarFechadosPorIdExterna(DpCargo.class,
								novo.getIdExterna(),
								novo.getOrgaoUsuario().getId());

				if (historico.size() > 0) {
					DpCargo anterior = historico.get(0);
					if (anterior != null
							&& (anterior.getIdExterna().equals(novo
									.getIdExterna()))
							&& (!anterior.getIdInicial().equals(
									novo.getIdInicial()))) {
						novo.setIdInicial(anterior.getIdInicial());
						log("AVISO (CARGO): ID_INICIAL reconectada ("
								+ novo.getIdInicial() + ")");
					}

				}
			}

		}

		// Funcao confianca
		if (opr.getNovo() != null && opr.getNovo() instanceof DpFuncaoConfianca) {

			DpFuncaoConfianca novo = (DpFuncaoConfianca) opr.getNovo();
			if (opr.getOperacao().equals(Operacao.incluir)) {
				List<DpFuncaoConfianca> historico = (List<DpFuncaoConfianca>) CpDao
						.getInstance().consultarFechadosPorIdExterna(
								DpFuncaoConfianca.class, novo.getIdExterna(),
								novo.getOrgaoUsuario().getId());

				if (historico.size() > 0) {
					DpFuncaoConfianca anterior = historico.get(0);
					if (anterior != null
							&& (anterior.getIdExterna().equals(novo
									.getIdExterna()))
							&& (!anterior.getIdInicial().equals(
									novo.getIdInicial()))) {
						novo.setIdInicial(anterior.getIdInicial());
						log("AVISO (FUNCAO CONFIANCA): ID_INICIAL reconectada ("
								+ novo.getIdInicial() + ")");
					}

				}
			}

		}

	}

	private void manterNomeExibicao(Sincronizavel antigo, Sincronizavel novo) {
		if (novo instanceof DpPessoa) {
			DpPessoa pAntiga = (DpPessoa) antigo;
			DpPessoa pNova = (DpPessoa) novo;
			@SuppressWarnings("unused")
			CpDao dao = CpDao.getInstance();
			pNova.setNomeExibicao(pAntiga.getNomeExibicao());
		}
	}

	protected static int parseParametros(String[] pars) {

		if (pars.length < 2) {
			System.err.println("Número de Parametros inválidos");
			return 10;
		}
		String servidor = pars[0];
		if (!servidor.toLowerCase().contains("-prod")
				&& !servidor.toLowerCase().contains("-desenv")
				&& !servidor.toLowerCase().contains("-homolo")
				&& !servidor.toLowerCase().contains("-treina")) {
			System.err.println("Servidor não informado");
			return 11;
		}

		for (String param : Arrays.asList(pars)) {
			if (param.equals("-modoLog=true")) {
				modoLog = true;
			}

			if (param.startsWith("-maxSinc=")) {
				maxSinc = Integer.valueOf(param.split("=")[1]);
			}

			if (param.startsWith("-destinatariosExtras")) {
				setDestinatariosExtras(param.split("=")[1]);
			}

			if (param.startsWith("-logLevel")) {
				setLogLevel(param.split("=")[1]);
			}

		}

		return 0;
	}

	private static void setLogLevel(String nomeLevel) {
		logLevel = Level.parse(nomeLevel);
	}

	/**
	 * @param args
	 *            - recebe os parametros da linha de comando. Os parâmetros
	 *            devem ser definidos na ordem abaixo:
	 * 
	 *            ****Banco de dados**** -prod --> aponta para o banco de dados
	 *            de produção -homolo --> aponta para o banco de dados de
	 *            homologação -treina --> aponta para o banco de dados de
	 *            treinamento -desenv --> aponta para o banco de dados de testes
	 * 
	 *            ****Localidade**** -sjrj --> os dados que serão sincronizados
	 *            são da SJRJ -trf2 --> os dados que serão sincronizados são do
	 *            TRF2 -sjes --> os dados que serão sincronizados são da SJES
	 * 
	 *            ****Funcionalidades***** -ldap --> sincroniza o banco de dados
	 *            com o Active Directory
	 * 
	 *            obs: se nenhum parâmetro for definido, sincorniza o XML com o
	 *            banco de dados
	 * 
	 * 
	 * 
	 * @throws Exception
	 * @throws Exception
	 * @throws CsisException
	 */
	public static void main(String[] args) throws Exception {
		SigaCpSinc sinc = new SigaCpSinc(args);
		sinc.run();
	}

	public static void configurarEntityManager(CpAmbienteEnumBL ambiente)
			throws Exception {
		CpPropriedadeBL prop = Cp.getInstance().getProp();
		prop.setPrefixo(ambiente.getSigla());

		Map<String, String> properties = new HashMap<>();
		properties.put( "provider" , "org.hibernate.jpa.HibernatePersistenceProvider");
		properties.put("hibernate.connection.url", prop.urlConexao());
		properties.put("hibernate.connection.username", prop.usuario());
		properties.put("hibernate.connection.password", prop.senha());
 		properties.put("c3p0.min_size", prop.c3poMinSize());
 		properties.put("c3p0.max_size", prop.c3poMaxSize());
 		properties.put("c3p0.timeout", prop.c3poTimeout());
 		properties.put("c3p0.max_statements", prop.c3poMaxStatements());

		// persistenceMap.put("javax.persistence.jdbc.url", "<url>");
		// persistenceMap.put("javax.persistence.jdbc.user", "<username>");
		// persistenceMap.put("javax.persistence.jdbc.password", "<password>");
		// persistenceMap.put("javax.persistence.jdbc.driver", "<driver>");
		// <property name="jboss.entity.manager.jndi.name"
		// value="java:/EntityManager/simpledb"/>

		properties.put("hibernate.jdbc.use_streams_for_binary", "true");
		

		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"siga-cp-sinc", properties);

		EntityManager em = emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);
	}

	public void run() throws Exception, NamingException, AplicacaoException {

		desativarCacheDeSegundoNivel();

		Configuration cfg;
		if (servidor.equals("prod"))
			configurarEntityManager(CpAmbienteEnumBL.PRODUCAO);
		else if (servidor.equals("homolo"))
			configurarEntityManager(CpAmbienteEnumBL.HOMOLOGACAO);
		else if (servidor.equals("treina"))
			configurarEntityManager(CpAmbienteEnumBL.TREINAMENTO);
		else if (servidor.equals("desenv"))
			configurarEntityManager(CpAmbienteEnumBL.DESENVOLVIMENTO);
		else
			configurarEntityManager(CpAmbienteEnumBL.DESENVOLVIMENTO);

		verificarOrigemDeDados();

		dt = new Date();
		log("--- Processando  " + dt + "--- ");
		log("--- Parametros: servidor= " + servidor + "  e url= " + url);

		if (args.length >= 3 && !args[1].equals("-url") && args[2] != null) {
			String method = args[2].trim().replace("-", "").toLowerCase();
			Method m = this.getClass().getMethod(method, null);
			m.invoke(this);
		} else {
			importxml();
		}

		log(" ---- Fim do Processamento --- ");
		logEnd();
		CacheManager.getInstance().shutdown();
	}

	private void desativarCacheDeSegundoNivel() throws Exception {
		Cp.getInstance().getProp().setCacheUseSecondLevelCache(false);
		Cp.getInstance().getProp().setCacheUseQueryCache(false);
	}

	private void verificarOrigemDeDados() throws AplicacaoException {
		boolean orgaoValido = false;
		for (CpOrgaoUsuario orgao : CpDao.getInstance()
				.consultaCpOrgaoUsuario()) {
			if (orgao.getAcronimoOrgaoUsu().equalsIgnoreCase(
					url.replace("-", ""))) {
				orgaoValido = true;
				break;
			}
		}

		if (!orgaoValido) {

			if (url == null || (!url.startsWith("-url") && url.length() < 3)
					|| (url.replace("-url=", "").startsWith("-"))) {
				throw new AplicacaoException(("url não informada"));
			}

		}

	}

	public void importxml() {
		long inicio = System.currentTimeMillis();
		try {
			log("Importando: XML");
			/* -------------------------- */
			// Mensagens.getString("url.origem")
			String urlOrigem = "";
			if (url.startsWith("-url")) {
				urlOrigem = url.substring(5);

			} else {
				// Verificação do acrônimo do orgão usuário
				String acron = url.substring(1);
				CpOrgaoUsuario orgu = obterOrgaoUsuario(acron);
				if (orgu == null) {
					log("Acrônimo '" + acron
							+ "' do órgão Usuário não encontrado no servidor '"
							+ servidor + "'");
					return;
				}
				urlOrigem = ImportarXmlProperties.getString("url."
						.concat(acron));
				if (urlOrigem == null || urlOrigem.trim().equals(""))
					urlOrigem = ImportarXmlProperties.getString("url.".concat(
							acron + ".").concat(servidor));
			}
			URL urlMumps = new URL(urlOrigem);

			URLConnection connMumps = urlMumps.openConnection();
			connMumps.setReadTimeout(0);

			/* */
			try (InputStream st = connMumps.getInputStream()) {
				// String inputStreamString = new
				// Scanner(st,"UTF-8").useDelimiter("\\A").next();
				// System.out.println(inputStreamString);
				importarXml(st);
				log("Importando: BD");
				setAntigo.addAll(importarTabela());
				log("Gravando alterações");
				gravar(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage());
		}
		long total = (System.currentTimeMillis() - inicio) / 1000;
		log("Tempo total de execução: " + total + " segundos (" + total / 60
				+ " min)");
	}

	private void logComDestaque(String msg) {
		String espaco = "";
		for (int i = 0; i < 10; i++) {
			espaco += "\n";
		}
		log(espaco + msg + espaco);
	}

	public SigaCpSinc(String[] args) {
		this.args = args;
		int result = parseParametros(args);
		if (result != 0)
			System.exit(result);

		logger.addHandler(logHandler);
		logger.setLevel(logLevel);

		if (modoLog) {
			logComDestaque(">>>Iniciando em modo LOG!<<<\nUse -modoLog=false para sair do modo LOG e escrever as alterações");
		}
		log("MAX SINC = " + maxSinc);

		String aUrl = "";
		String oServidor = args[0].toLowerCase();
		if (args[1].equalsIgnoreCase("-url"))
			aUrl = args[1].toLowerCase() + "=" + args[2];
		else
			aUrl = args[1].toLowerCase();

		this.servidor = oServidor.substring(oServidor.indexOf("-") + 1);
		this.url = aUrl;
	}

	public List<Sincronizavel> importarTabela() {
		List<Sincronizavel> l = new ArrayList<Sincronizavel>();

		for (DpLotacao o : CpDao.getInstance().listarAtivos(DpLotacao.class,
				"dataFimLotacao", orgaoUsuario)) {
			l.add(o);
		}
		for (DpCargo o : CpDao.getInstance().listarAtivos(DpCargo.class,
				"dataFimCargo", orgaoUsuario)) {
			l.add(o);
		}
		for (DpFuncaoConfianca o : CpDao.getInstance().listarAtivos(
				DpFuncaoConfianca.class, "dataFimFuncao", orgaoUsuario)) {
			l.add(o);
		}
		// Comentado por Edson, pois precisa ser resolvido problema dos órgãos
		// (do CJF)
		// cadastrados manualmente mas que não constam são enviados pelo XML
		/*
		 * if (getVersaoInteira().intValue() >= 2) { for (CpOrgao o :
		 * CpDao.getInstance().listarAtivos(CpOrgao.class, "hisDtFim",
		 * orgaoUsuario)) { l.add(o); } }
		 */
		for (DpPessoa o : CpDao.getInstance().listarAtivos(DpPessoa.class,
				"dataFimPessoa", orgaoUsuario)) {
			l.add(o);
		}
		if (getVersaoInteira().intValue() >= 2) {
			for (CpPapel o : CpDao.getInstance().listarAtivos(CpPapel.class,
					"hisDtFim", orgaoUsuario)) {
				l.add(o);
			}
		}
		return l;
	}

	private void importarListasDeTipos() {
		tiposLotacao = CpDao.getInstance().listarTiposLotacao();
		tiposPessoa = CpDao.getInstance().listarTiposPessoa();
		tiposPapel = CpDao.getInstance().listarTiposPapel();
	}

	private CpTipoPessoa obterTipoPessoaPorDescricao(String dscTpPessoa)
			throws Exception {
		if (dscTpPessoa == null)
			return null;
		if ("".equals(dscTpPessoa))
			return null;
		for (CpTipoPessoa tp : tiposPessoa) {
			if (tp.getDscTpPessoa().equalsIgnoreCase(dscTpPessoa)) {
				return tp;
			}
		}
		throw new Exception("CpTipoPessoa com descrição '" + dscTpPessoa
				+ "' não encontrado(a) !");
	}

	private CpTipoPessoa obterTipoPessoaPorId(Integer idTpPessoa)
			throws Exception {
		if (idTpPessoa == null)
			return null;
		if (idTpPessoa.intValue() < 1)
			return null;
		for (CpTipoPessoa tp : tiposPessoa) {
			if (tp.getIdTpPessoa().equals(idTpPessoa)) {
				return tp;
			}
		}
		throw new Exception("CpTipoPessoa com id '" + idTpPessoa
				+ "' não encontrado(a) !");
	}

	private CpTipoPapel obterTipoPapelPorDescricao(String dscTpPapel)
			throws Exception {
		if (dscTpPapel == null)
			return null;
		if ("".equals(dscTpPapel))
			return null;
		for (CpTipoPapel tp : tiposPapel) {
			if (tp.getDscTpPapel().equalsIgnoreCase(dscTpPapel)) {
				return tp;
			}
		}
		throw new Exception("CpTipoPapel com descrição " + dscTpPapel
				+ " não encontrado !");
	}

	private CpTipoLotacao obterTipoLotacaoPorDescricao(String dscTpLotacao)
			throws Exception {
		if (dscTpLotacao == null)
			return null;
		if ("".equals(dscTpLotacao))
			return null;
		for (CpTipoLotacao tl : tiposLotacao) {
			if (tl.getDscTpLotacao().equalsIgnoreCase(dscTpLotacao)) {
				return tl;
			}
		}
		throw new Exception("CpTipoLotacao com descrição " + dscTpLotacao
				+ " não encontrado !");
	}

	private CpTipoLotacao obterTipoLotacaoPorId(Long idTpLotacao)
			throws Exception {
		if (idTpLotacao == null)
			return null;
		if (idTpLotacao.longValue() < 0)
			return null;
		for (CpTipoLotacao tl : tiposLotacao) {
			if (tl.getIdTpLotacao().equals(idTpLotacao)) {
				return tl;
			}
		}
		throw new Exception("CpTipoPessoa com id " + idTpLotacao
				+ "não encontrado !");
	}

	protected static CpOrgaoUsuario obterOrgaoUsuario(String acronimo) {
		CpOrgaoUsuario o = new CpOrgaoUsuario();
		o.setSiglaOrgaoUsu(acronimo);
		return CpDao.getInstance().consultarPorSigla(o);
	}

	private CpOrgaoUsuario obterOrgaoUsuarioSJRJ() {
		CpOrgaoUsuario o = new CpOrgaoUsuario();
		o.setSigla("RJ");
		return CpDao.getInstance().consultarPorSigla(o);
	}

	private void obterLotacaoSJRJ() {
		CpOrgaoUsuario o = obterOrgaoUsuarioSJRJ();
		DpLotacao lotModeloSJRJ = new DpLotacao();
		lotModeloSJRJ.setSigla("SJRJ");
		lotModeloSJRJ.setOrgaoUsuario(o);
		DpLotacao lotSJRJ = CpDao.getInstance()
				.consultarPorSigla(lotModeloSJRJ);
		setLotacaoSJRJ(lotSJRJ);
	}

	private void obterLotacaoDIRFO() {
		CpOrgaoUsuario o = obterOrgaoUsuarioSJRJ();
		DpLotacao lotModeloDIRFO = new DpLotacao();
		lotModeloDIRFO.setSigla("DIRFO");
		lotModeloDIRFO.setOrgaoUsuario(o);
		DpLotacao lotDIRFO = CpDao.getInstance().consultarPorSigla(
				lotModeloDIRFO);
		setLotacaoDIRFO(lotDIRFO);
	}

	public void importarXml(InputStream in) throws Exception {
		String aditionalEmails = "";

		boolean contemCargo = false;
		boolean contemPessoa = false;
		boolean contemFuncao = false;
		boolean contemLotacao = false;
		boolean contemPapel = false;

		importarListasDeTipos();

		obterLotacaoSJRJ();
		obterLotacaoDIRFO();
		XmlPullParser parser = new KXmlParser();
		boolean fDocumentoCompleto = false;

		try {
			// Mensagens.getString("url.origem")
			/*
			 * Código antigo (passou para )superior.
			 * 
			 * String urlOrigem = ""; if (this.url.startsWith("-url")) urlOrigem
			 * = this.url.substring(5);
			 * 
			 * else urlOrigem = Mensagens.getString("url.".concat(this.url
			 * .substring(1)));
			 * 
			 * URL urlMumps = new URL(urlOrigem);
			 * 
			 * URLConnection connMumps = urlMumps.openConnection();
			 * connMumps.setReadTimeout(0);
			 */
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			// BufferedReader xmlReader = new BufferedReader(new
			// InputStreamReader(connMumps.getInputStream()));
			// parser.setInput(xmlReader);
			parser.setInput(in, null);

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					if (parser.getName().equals("base")) {
						CpOrgaoUsuario o = new CpOrgaoUsuario();
						o.setSiglaOrgaoUsu(parser.getAttributeValue(null,
								"orgaoUsuario"));
						cpOrgaoUsuario = CpDao.getInstance().consultarPorSigla(
								o);
						setDataHora(parseStr(parser, "dataHora"));
						setVersao(parseStr(parser, "versao"));
						orgaoUsuario = cpOrgaoUsuario.getIdOrgaoUsu();
						aditionalEmails = parser.getAttributeValue(null,
								"NotificarPorEmail");

						if (destinatariosExtras != null
								&& aditionalEmails != null) {
							destinatariosExtras = destinatariosExtras
									+ (!aditionalEmails.trim().equals("") ? ","
											+ aditionalEmails : "");
						}

					} else if (parser.getName().equals("cargo")) {
						setNovo.add(importarXmlCargo(parser));
						contemCargo = true;
					} else if (parser.getName().equals("funcao")) {
						setNovo.add(importarXmlFuncao(parser));
						contemFuncao = true;
					} else if (parser.getName().equals("orgao")) {
						if (getVersaoInteira().intValue() < 2)
							throw new Exception(
									"Versão não possui órgão ou atributo 'versao' não especificado. ");
						setNovo.add(importarXmlOrgao(parser));
					} else if (parser.getName().equals("lotacao")) {
						setNovo.add(importarXmlLotacao(parser));
						contemLotacao = true;
					} else if (parser.getName().equals("pessoa")) {
						setNovo.add(importarXmlPessoa(parser));
						contemPessoa = true;
					} else if (parser.getName().equals("papel")) {
						if (getVersaoInteira().intValue() < 2)
							throw new Exception(
									"Versão não possui papel ou atributo 'versao' não especificado. ");
						setNovo.add(importarXmlPapel(parser));
					}
				}
				if (parser.getEventType() == XmlPullParser.END_TAG
						&& parser.getName().equals("base")) {
					fDocumentoCompleto = true;
				}
				parser.nextToken();
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (XmlPullParserException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

		if (!contemCargo) {
			throw new AplicacaoException("XML não contém cargo!");
		}
		if (!contemLotacao) {
			throw new AplicacaoException("XML não contém lotação!");
		}
		if (!contemPessoa) {
			throw new AplicacaoException("XML não contém pessoa!");
		}
		if (!contemFuncao) {
			// throw new
			// AplicacaoException("XML não contém função de confiança!");
		}
		if (!contemPapel) {
			// throw new
			// AplicacaoException("XML não contém função de confiança!");
		}

		if (!fDocumentoCompleto) {
			throw new Exception(
					"XML arquivo não estava completo! Nenhuma alteração foi realizada na base.");
		}

	}

	/**
	 * Cria registro de processamento do XML com o objetivo de verificar se há
	 * duplicidade
	 * 
	 * @param siglaOrgao
	 *            - sigla do órgao (base)
	 * @param nomeEntidade
	 *            - nome da entidade (Se é 'orgao', 'pessoa', 'lotacao' etc.
	 * @param nomeAtributo
	 *            - nome do atributo da entidade
	 * @param valor
	 *            - valor do atributo
	 * @param identificador
	 *            - conteúdo que procura identificar onde ocorreu a duplicação
	 * @throws Exception
	 *             - no caso de haver duplicidade.
	 */
	private void criarUnicidade(String siglaOrgao, String nomeEntidade,
			String nomeAtributo, String valor, String identificador)
			throws Exception {
		if (valor == null || valor.equals("")) {
			throw new Exception("Orgão usuário: " + siglaOrgao + "; Entidade: "
					+ nomeEntidade + "; Atributo: " + nomeAtributo
					+ "; Valor: " + valor
					+ " => valor nulo para o identificador: " + identificador);
		}
		if (unicidades.get(siglaOrgao) == null) {
			unicidades
					.put(siglaOrgao,
							new Hashtable<String, Hashtable<String, Hashtable<String, String>>>());
		}
		if (unicidades.get(siglaOrgao).get(nomeEntidade) == null) {
			unicidades.get(siglaOrgao).put(nomeEntidade,
					new Hashtable<String, Hashtable<String, String>>());
		}
		if (unicidades.get(siglaOrgao).get(nomeEntidade).get(nomeAtributo) == null) {
			unicidades.get(siglaOrgao).get(nomeEntidade)
					.put(nomeAtributo, new Hashtable<String, String>());
		}
		if (unicidades.get(siglaOrgao).get(nomeEntidade).get(nomeAtributo)
				.get(valor) == null) {
			try {
				unicidades.get(siglaOrgao).get(nomeEntidade).get(nomeAtributo)
						.put(valor, identificador);
			} catch (Exception e) {
				throw new Exception("Orgão usuário: " + siglaOrgao.toString()
						+ "; Entidade: " + nomeEntidade.toString()
						+ "; Atributo: " + nomeAtributo.toString()
						+ "; Valor: " + valor.toString()
						+ " Erro na tentativa de inserir a lotação. " + " => "
						+ e.getMessage());
			}
		} else {
			throw new Exception("Orgão usuário: "
					+ siglaOrgao
					+ "; Entidade: "
					+ nomeEntidade
					+ "; Atributo: "
					+ nomeAtributo
					+ "; Valor: "
					+ valor
					+ " => já existe para o identificador: "
					+ unicidades.get(siglaOrgao).get(nomeEntidade)
							.get(nomeAtributo).get(valor)
					+ " na tentativa de inserir o identificador: "
					+ identificador);
		}
	}

	private DpCargo importarXmlCargo(XmlPullParser parser) throws Exception {
		DpCargo cargo = new DpCargo();
		cargo.setIdExterna(parseStr(parser, "id"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "cargo", "idExterna",
				parseStr(parser, "id"), parseStr(parser, "nome"));
		cargo.setNomeCargo(parseStr(parser, "nome"));
		cargo.setSigla(parseStr(parser, "sigla"));
		// leiaute antigo ainda não contém sigla
		if (parseStr(parser, "sigla") != null) {
			criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "cargo", "sigla",
					parseStr(parser, "sigla"), parseStr(parser, "id"));
		}
		cargo.setOrgaoUsuario(cpOrgaoUsuario);
		return cargo;
	}

	private DpFuncaoConfianca importarXmlFuncao(XmlPullParser parser)
			throws Exception {
		DpFuncaoConfianca funcao = new DpFuncaoConfianca();
		funcao.setIdExterna(parseStr(parser, "id"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "funcao",
				"idExterna", parseStr(parser, "id"), parseStr(parser, "nome"));
		funcao.setNomeFuncao(parseStr(parser, "nome"));
		funcao.setSigla(parseStr(parser, "sigla"));
		// leiaute antigo ainda não contém sigla
		if (parseStr(parser, "sigla") != null) {
			criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "funcao",
					"sigla", parseStr(parser, "sigla"), parseStr(parser, "id"));
		}
		funcao.setOrgaoUsuario(cpOrgaoUsuario);
		return funcao;
	}

	private CpOrgao importarXmlOrgao(XmlPullParser parser) throws Exception {

		CpOrgao orgao = new CpOrgao();
		orgao.setIdeOrgao(parseStr(parser, "id"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "orgao", "ideOrgao",
				parseStr(parser, "id"), parseStr(parser, "sigla"));
		orgao.setNmOrgao(parseStr(parser, "nome"));
		orgao.setSigla(parseStr(parser, "sigla"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "orgao", "sigla",
				parseStr(parser, "sigla"), parseStr(parser, "id"));
		orgao.setOrgaoUsuario(cpOrgaoUsuario);
		return orgao;
	}

	private DpLotacao importarXmlLotacao(XmlPullParser parser) throws Exception {

		DpLotacao lotacao = new DpLotacao();
		lotacao.setIdExterna(parseStr(parser, "id"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "lotacao",
				"idExterna", parseStr(parser, "id"), parseStr(parser, "sigla"));
		lotacao.setNomeLotacao(parseStr(parser, "nome"));
		lotacao.setSiglaLotacao(parseStr(parser, "sigla"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "lotacao", "sigla",
				parseStr(parser, "sigla"), parseStr(parser, "id"));
		if (lotacao.getSiglaLotacao() == null) {
			lotacao.setSiglaLotacao(parseStr(parser, "id"));
		}
		if (lotacao.getNomeLotacao() == null) {
			lotacao.setNomeLotacao(parseStr(parser, "id"));
		}
		lotacao.setDataInicioLotacao(new Date());
		lotacao.setOrgaoUsuario(cpOrgaoUsuario);
		if (parseStr(parser, "idPai") != null
				&& !parseStr(parser, "idPai").equals(lotacao.getIdExterna())) {
			DpLotacao o = new DpLotacao();
			o.setIdExterna(parseStr(parser, "idPai"));
			lotacao.setLotacaoPai(o);
		}
		String tipoLotacao = parseStr(parser, "tipoLotacao");
		if (tipoLotacao == null) {
			tipoLotacao = parseStr(parser, "tipo");
		}
		if (tipoLotacao != null && !isNumerico(tipoLotacao)) {
			CpTipoLotacao o = obterTipoLotacaoPorDescricao(tipoLotacao);
			lotacao.setCpTipoLotacao(o);
		} else {
			// inferir tipo de lotacao para a SJRJ se vier nulo no XML
			inferirTipoLotacaoSJRJ(lotacao);
		}
		return lotacao;
	}

	private boolean isNumerico(String tipoLotacao) {
		try {
			new Long(tipoLotacao);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	private DpPessoa importarXmlPessoa(XmlPullParser parser) throws Exception {
		DpPessoa pessoa = new DpPessoa();

		try {
			pessoa.setIdExterna(parseStr(parser, "id"));
			String situacaoFuncPessoa = obterSituacaoPessoaPelaDescricao(
					parseStr(parser, "situacao").trim(), parseStr(parser, "id"));
			// A unicidade da ID somente é verificada para os ATIVOS
			// (situacao=1)
			/*
			 * if ("1".equals(situacaoFuncPessoa) ) {
			 * criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "pessoa",
			 * "idExterna", parseStr(parser, "id"), parseStr(parser, "sigla"));
			 * }
			 */
			try {
				pessoa.setCpfPessoa(parseLong(parser, "cpf"));
			} catch (Exception ex) {
				pessoa.setCpfPessoa(parseLong(parser, "id"));
			}
			// A unicidade do CPF somente é verificada para os ATIVOS
			// (situacao=1)
			/*
			 * if ("1".equals(situacaoFuncPessoa ) ) {
			 * criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "pessoa",
			 * "cpf", pessoa.getCpfPessoa().toString(), parseStr( parser,
			 * "id")); }
			 */
			pessoa.setNomePessoa(parseStr(parser, "nome"));
			Date dtNascimento = parseData(parser, "dtNascimento");
			if (dtNascimento == null)
				pessoa.setDataNascimento(null);
			else
				pessoa.setDataNascimento(dtNascimento);

			pessoa.setEmailPessoa(parseStr(parser, "email"));
			if (pessoa.getEmailPessoaAtual() != null)
				pessoa.setEmailPessoa(pessoa.getEmailPessoaAtual()
						.toLowerCase());
			pessoa.setSesbPessoa(cpOrgaoUsuario.getSiglaOrgaoUsu());
			pessoa.setDataInicioPessoa(new Date());
			pessoa.setSiglaPessoa(parseStr(parser, "sigla"));
			/*
			 * criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "pessoa",
			 * "sigla", pessoa.getSiglaPessoa(), parseStr(parser, "id"));
			 */
			pessoa.setPadraoReferencia(parseStr(parser, "padraoReferencia"));
			pessoa.setMatricula(parseLong(parser, "matricula"));
			criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "pessoa",
					"matricula",
					pessoa.getSesbPessoa() + pessoa.getMatricula(),
					parseStr(parser, "id"));
			pessoa.setOrgaoUsuario(cpOrgaoUsuario);
			// pessoa.setSituacaoFuncionalPessoa(parseStr(parser, "situacao"));
			pessoa.setSituacaoFuncionalPessoa(situacaoFuncPessoa);
			pessoa.setSexo(parseStr(parser, "sexo"));
			pessoa.setGrauInstrucao(parseStr(parser, "grauInstrucao"));
			pessoa.setTipoSanguineo(parseStr(parser, "tipoSanguineo"));
			pessoa.setNacionalidade(parseStr(parser, "nacionalidade"));
			pessoa.setNaturalidade(parseStr(parser, "naturalidade"));
			Date dtPosse = parseData(parser, "dtPosse");
			if (dtPosse != null)
				pessoa.setDataPosse(dtPosse);

			Date dtNomeacao = parseData(parser, "dtNomeacao");
			if (dtNomeacao != null)
				pessoa.setDataNomeacao(dtNomeacao);

			Date dtAtoPublicacao = parseData(parser, "dtAtoPublicacao");
			if (dtAtoPublicacao != null)
				pessoa.setDataPublicacao(dtAtoPublicacao);

			pessoa.setAtoNomeacao(parseStr(parser, "atoNomeacao"));
			pessoa.setEndereco(parseStr(parser, "rua"));
			pessoa.setBairro(parseStr(parser, "bairro"));
			pessoa.setCidade(parseStr(parser, "cidade"));
			pessoa.setCep(parseStr(parser, "cep"));
			pessoa.setTelefone(parseStr(parser, "telefone"));
			pessoa.setIdentidade(parseStr(parser, "rg"));

			pessoa.setOrgaoIdentidade(parseStr(parser, "rgOrgao"));
			pessoa.setUfIdentidade(parseStr(parser, "rgUf"));
			pessoa.setIdEstadoCivil(parseInt(parser, "estCivil"));

			Date rgDtExp = parseData(parser, "rgDtExp");
			if (rgDtExp != null)
				pessoa.setDataExpedicaoIdentidade(rgDtExp);

			Date dtExercicio = parseData(parser, "dtInicioExercicio");
			if (dtExercicio != null)
				pessoa.setDataExercicioPessoa(dtExercicio);

			if (parseStr(parser, "cargo") != null) {
				DpCargo o = new DpCargo();
				o.setIdExterna(parseStr(parser, "cargo"));
				pessoa.setCargo(o);
			}
			if (parseStr(parser, "funcaoConfianca") != null) {
				DpFuncaoConfianca o = new DpFuncaoConfianca();
				o.setIdExterna(parseStr(parser, "funcaoConfianca"));
				pessoa.setFuncaoConfianca(o);
			}
			if (parseStr(parser, "lotacao") != null) {
				DpLotacao o = new DpLotacao();
				o.setIdExterna(parseStr(parser, "lotacao"));
				pessoa.setLotacao(o);
			}
			CpTipoPessoa o;
			// Edson: 1) alguns XML's informam a descrição do tipo
			// ("Servidor", por exemplo) em vez do ID. 2) Alterar o nome do
			// atributo de
			// tipo_rh para tipo e, na hora de importar, ver se o valor é ID ou
			// descrição. Ou então solicitar que os órgãos passem a enviar o ID,
			// não
			// a descrição
			if (parseStr(parser, "tipo") != null) {
				o = obterTipoPessoaPorDescricao(parseStr(parser, "tipo"));
				pessoa.setCpTipoPessoa(o);
				/*
				 * }else if (parseStr(parser, "tipo_rh") != null) { o =
				 * obterTipoPessoaPorId(Integer.valueOf(parseStr(parser,
				 * "tipo_rh"))); pessoa.setCpTipoPessoa(o);
				 */
			} else {
				// inferir tipo de pessoa para a SJRJ se vier nulo no XML
				inferirTipoPessoaSJRJ(pessoa);
			}
		} catch (Exception e) {
			throw e;
		}
		this.dpPessoaCorrente = pessoa;
		return pessoa;
	}

	private CpPapel importarXmlPapel(XmlPullParser parser) throws Exception {

		CpPapel papel = new CpPapel();
		papel.setIdExterna(parseStr(parser, "id"));
		criarUnicidade(cpOrgaoUsuario.getSiglaOrgaoUsu(), "tipo", "idExterna",
				parseStr(parser, "id"), dpPessoaCorrente.getIdExterna());
		// if (parseStr(parser, "tipo") != null) {
		// CpTipoPapel o = obterTipoPapelPorDescricao(parseStr(parser, "tipo"));
		// papel.setCpTipoPapel(o);
		// }
		if (parseStr(parser, "tipo") != null) {
			CpTipoPapel o = obterTipoPapelPorDescricao(parseStr(parser, "tipo"));
			papel.setCpTipoPapel(o);
		}
		// papel.setSigla(parseStr(parser, "sigla"));
		papel.setDpPessoa(dpPessoaCorrente);
		papel.setOrgaoUsuario(cpOrgaoUsuario);
		if (parseStr(parser, "cargo") != null) {
			DpCargo o = new DpCargo();
			o.setIdeCargo(parseStr(parser, "cargo"));
			papel.setDpCargo(o);
		}
		if (parseStr(parser, "lotacao") != null) {
			DpLotacao o = new DpLotacao();
			o.setIdExterna(parseStr(parser, "lotacao"));
			papel.setDpLotacao(o);
		}

		return papel;
	}

	public void log(String s) {
		logger.log(new LogRecord(logLevel, s));
	}

	public void logEnd() throws Exception {
		String sDest = ImportarXmlProperties.getString("lista.destinatario")
				+ (!destinatariosExtras.trim().equals("") ? ","
						+ destinatariosExtras : "");
		if (getDataHora() != null) {
			log("Arquivo XML gerado em " + getDataHora() + "\n");
		}

		if (maxSinc != -1 && list != null && (list.size() > maxSinc)) {
			logHandler
					.setAssunto("Limite de operações por sincronismo superior a 200. Execute o sincronismo manualmente.");
		}

		logHandler.setDestinatariosEmail(sDest.split(","));

	}

	private Date parseData(XmlPullParser parser, String campo) {
		final SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		df.setLenient(false);
		try {
			String sDt = parseStr(parser, campo);
			Date dt = df.parse(sDt);
			String sDtComp = df.format(dt);
			if (sDt.equals(sDtComp))
				return dt;
			else
				throw new Error("Erro no parse de " + sDt);
		} catch (final ParseException e) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}

	}

	private Integer parseInt(XmlPullParser parser, String campo) {
		final String scampo = parseStr(parser, campo);
		try {
			return Integer.valueOf(scampo);
		} catch (final NumberFormatException en) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}

	}

	private Long parseLong(XmlPullParser parser, String campo) {
		final String scampo = parseStr(parser, campo);
		try {
			return Long.valueOf(scampo);
		} catch (final NumberFormatException en) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}

	}

	private String parseStr(XmlPullParser parser, String campo) {
		String s = parser.getAttributeValue(null, campo);
		if (s == null)
			return null;
		s = s.trim();
		if (s.equals(""))
			s = null;
		return s;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getVersao() {
		return versao;
	}

	private Integer getVersaoInteira() {
		Integer intVer = Integer.valueOf(1);
		try {
			intVer = Integer.parseInt(getVersao());
			return intVer;
		} catch (NumberFormatException e) {
			return Integer.valueOf(1);
		}
	}

	/**
	 * Atribui o tipo de lotacao caso ela seja da SJRJ (Provisório)
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	public void inferirTipoLotacaoSJRJ(DpLotacao lot)
			throws NumberFormatException, Exception {
		CpOrgaoUsuario org = lot.getOrgaoUsuario();
		// ver se órgão usuário é SJRJ
		if (org.getIdOrgaoUsu() == null)
			return;
		if (!org.getSigla().equals(obterOrgaoUsuarioSJRJ().getSigla()))
			return;
		DpLotacao lotDIRFO = getLotacaoDIRFO();
		DpLotacao lotPaiAdm = lot;
		while (lotPaiAdm != null) {
			if (lotDIRFO != null
					&& lotPaiAdm.getIdeLotacao()
							.equals(lotDIRFO.getIdLotacao())) {
				// é administrativo
				lot.setCpTipoLotacao(obterTipoLotacaoPorId(Long.valueOf("1")));
				return;
			} else {
				// procura mais acima
				lotPaiAdm = lotPaiAdm.getLotacaoPai();
			}
		}
		// se não encontrou, procura para ver se é jurídico (Filho da SJRJ ou
		// ela)
		// note que a A DIRFO se encaixa no critério, mas já foi tratada acima
		DpLotacao lotSJRJ = getLotacaoSJRJ();
		DpLotacao lotPaiJur = lot;
		while (lotPaiJur != null) {
			if (lotSJRJ != null
					&& lotPaiJur.getIdeLotacao()
							.equals(lotSJRJ.getIdeLotacao())) {
				// é jurídico
				lot.setCpTipoLotacao(obterTipoLotacaoPorId(Long.valueOf("100")));
				return;
			} else {
				// procura mais acima
				lotPaiJur = lotPaiJur.getLotacaoPai();
			}
		}

		// se for turma recursal, trata como judicial
		if (lot.getSiglaLotacao().contains("TR-")) {
			lot.setCpTipoLotacao(obterTipoLotacaoPorId(Long.valueOf("100")));
		} else {
			// Se é exceção, trata como administrativo
			lot.setCpTipoLotacao(obterTipoLotacaoPorId(Long.valueOf("1")));
		}
		return;
	}

	/**
	 * Atribui o tipo de pessoa caso ela seja da SJRJ (Provisório)
	 * 
	 * @throws Exception
	 * 
	 */
	public void inferirTipoPessoaSJRJ(DpPessoa pes) throws Exception {
		CpOrgaoUsuario org = pes.getOrgaoUsuario();
		// SJRJ
		if (org.getSigla().equals(obterOrgaoUsuarioSJRJ().getSigla())) {
			if (pes.getMatricula() != null) {
				long mat = pes.getMatricula().longValue();
				int tipid = -1;
				if (mat >= 10000L && mat <= 14999L) {
					// servidor (2)
					tipid = 2;
				} else if (mat >= 15000L && mat <= 15999L) {
					// servidor requisitado (2)
					tipid = 2;
				} else if (mat >= 16000L && mat <= 17999L) {
					// magistrado (1)
					tipid = 1;
				} else if (mat >= 20000L && mat <= 29999L) {
					// aposentados na época da implantação do SIRH (2)
					tipid = 2;
				} else if (mat >= 30000L && mat <= 69999L) {
					// Estagiários (3)
					tipid = 3;
				} else {
					// TODO: _LAGS - Verificar como identificar tercerizados
					return;
				}
				CpTipoPessoa tpes = obterTipoPessoaPorId(Integer.valueOf(tipid));
				if (tpes != null) {
					pes.setCpTipoPessoa(tpes);
				}
			}
		}
		return;
	}

	/**
	 * Obtém o código da situação funcional da pessoa a partir de ALGUMAS
	 * descrições Somente para os casos onde há erro no envio pelo remetete -
	 * solicitar acerto nesse caso
	 * 
	 * @param situacaoFuncPessoa
	 *            - parseStr(parser, "situacao").trim()
	 * @param idPessoa
	 *            - identificador ou descrição da situaçao fuuncioal da pessoa
	 * @return - identif
	 * @throws Exception
	 */
	public String obterSituacaoPessoaPelaDescricao(String situacaoFuncPessoa,
			String idPessoa) throws Exception {
		// String situacaoFuncPessoa = parseStr(parser, "situacao").trim();
		// Alguns tem enviado a descrição da pessoa baseado no manual ao
		// invés do ID
		if ("ATIVO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "1";
		} else if ("CEDIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "2";
		} else if ("APOSENTADO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "4";
		} else if ("EXONERADO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "5";
		} else if ("DEMITIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "6";
		} else if ("FALECIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "7";
		} else if ("TRANSFERIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "10";
		} else if ("DISPONIBILIDADE".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "16";
		} else if ("REMOVIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "17";
		} else if ("REDISTRIBUIDO".equalsIgnoreCase(situacaoFuncPessoa)) {
			return "20";
		} else {
			try {
				int parseIntSituacao = Integer.parseInt(situacaoFuncPessoa);
				if (parseIntSituacao > 31) {
					throw new Exception(
							"Tag pessoa id "
									+ idPessoa // parseStr(parser, "id")
									+ " tem situacao funcional não tratada no roteiro: '"
									+ situacaoFuncPessoa + "'");
				}
			} catch (Exception e) {
				throw new Exception("Tag pessoa id "
						+ idPessoa // parseStr(parser, "id")
						+ " tem situacao funcional não conhecida: '"
						+ situacaoFuncPessoa + "'");
			}
		}
		return situacaoFuncPessoa;
	}

	public void setLotacaoSJRJ(DpLotacao lotacaoSJRJ) {
		this.lotacaoSJRJ = lotacaoSJRJ;
	}

	public DpLotacao getLotacaoSJRJ() {
		return lotacaoSJRJ;
	}

	public void setLotacaoDIRFO(DpLotacao lotacaoDIRFO) {
		this.lotacaoDIRFO = lotacaoDIRFO;
	}

	public DpLotacao getLotacaoDIRFO() {
		return lotacaoDIRFO;
	}

	protected void setServidor(String servidor) {
		this.servidor = servidor.replace("-", "");
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Acrescenta destinatários a serem notificados da sincronização
	 * 
	 * @param destinatarios
	 *            - lista de e-mails separados por vírgula
	 */
	protected static void setDestinatariosExtras(String destinatarios) {
		destinatariosExtras = destinatarios;
	}

	protected void exibirMensagemMaxSinc(List<Item> list) {
		log("***ATENÇÃO***: Limite de operações por sincronismo excedido!");
		log("Operações a serem executadas: " + list.size()
				+ "\nOperações permitidas: " + maxSinc);
		log("Ajuste o parâmetro -maxSinc=<VALOR> para permitir que o sincronismo seja efetivado!");
		log("As alterações não serão efetivadas! Executando rollback...");
	}
}