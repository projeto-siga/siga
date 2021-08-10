package br.gov.jfrj.siga.ex.sinc;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import org.hibernate.cfg.Configuration;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.OperadorComHistorico;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelComparator;

public class SigaExSinc {
	private static boolean modoLog = false;
	protected static int maxSinc = -1;
	private static String destinatariosExtras = "";
	private static Level logLevel = Level.WARNING;
	private String servidor = "";
	private String url = "";
	private boolean naoExcluir = false;

	private Map<String, ExFormaDocumento> mapEspecies = new HashMap<>();
	private Map<String, ExClassificacao> mapClassificacoes = new HashMap<>();
	private Map<String, ExNivelAcesso> mapNiveisDeAcesso = new HashMap<>();

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

	protected SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);

	private String dataHora;

	private String versao;

	List<Item> list;

	private Logger logger = Logger.getLogger("br.gov.jfrj.log.sinc");

	/*
	 * verificar duplicidades (árvore de Hashtables : nivel 0: orgaoUsuario,
	 * nivel 1: entidade, nivel 2: atributo,valor
	 */
	Hashtable<String, Hashtable<String, Hashtable<String, Hashtable<String, String>>>> unicidades = new Hashtable<String, Hashtable<String, Hashtable<String, Hashtable<String, String>>>>();

	protected Date dt;
	private SincLogHandler logHandler = new SincLogHandler();
	protected String[] args;

	/**
	 * Sincroniza os modelos que estão no banco de dados do Siga-Doc com um ou
	 * mais diretórios de arquivos XML.
	 * 
	 * @param args
	 *            - recebe os parametros da linha de comando. Os parâmetros
	 *            devem ser definidos na ordem abaixo:
	 * 
	 *            ****Banco de dados**** -prod --> aponta para o banco de dados
	 *            de produção -homolo --> aponta para o banco de dados de
	 *            homologação -treina --> aponta para o banco de dados de
	 *            treinamento -desenv --> aponta para o banco de dados de testes
	 * 
	 *            ****Apenas mostrar diferenças***** -modoLog=true --> Apenas
	 *            exibe as diferenças, sem gravar nenhuma alteração
	 * 
	 *            ****Limite de alterações***** -maxSinc=N --> Limita o número
	 *            de alterações. Se o número for maior que N, nenhuma alteração
	 *            será gravada. Ex.: -maxSinc=100
	 * 
	 *            ****Controle de log***** -logLevel=X --> Logar mensagens com
	 *            criticidade maior ou igual a X.
	 * 
	 * 
	 * @throws Exception
	 * @throws Exception
	 * @throws CsisException
	 */
	public static void main(String[] args) throws Exception {
		SigaExSinc sinc = new SigaExSinc(args);
		sinc.run();
	}

	public SigaExSinc(String[] args) {
		this.args = args;
		int result = parseParametros(args);
		if (result != 0)
			System.exit(result);

		logger.addHandler(logHandler);
		logger.setLevel(logLevel);

		if (modoLog) {
			logComDestaque(
					">>>Iniciando em modo LOG!<<<\nUse -modoLog=false para sair do modo LOG e escrever as alterações");
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

	public void run() throws Exception, NamingException, AplicacaoException {

		desativarCacheDeSegundoNivel();

		Configuration cfg;
		if (servidor.equals("prod"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
		else if (servidor.equals("homolo"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		else if (servidor.equals("treina"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.TREINAMENTO);
		else if (servidor.equals("desenv"))
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
		else
			cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);

		// Desabilitado para evitar o erro de compilação depois que foi feita a
		// troca do Hibernate para o JPA
		// HibernateUtil.configurarHibernate(cfg);

		dt = new Date();
		log("--- Processando  " + dt + "--- ");
		log("--- Parametros: servidor= " + servidor + "  e url= " + url);

		sincronizarModelos();

		log(" ---- Fim do Processamento --- ");
		logEnd();
	}

	public void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			}
		}
	}

	public void sincronizarModelos() {
		long inicio = System.currentTimeMillis();

		try {
			log("Importando: Tipos");
			importarListasDeTipos();

			log("Importando: XML");
			List<String> diretorios = ImportarXmlProperties.getDiretorios();
			for (String diretoriobase : diretorios) {
				String diretorio = diretoriobase + "/modelos";
				List<File> files = new ArrayList<>();
				listf(diretorio, files);
				for (File file : files) {
					if (!file.getName().endsWith(".xml"))
						continue;

					try (FileInputStream fis = new FileInputStream(file)) {
						// System.out.println(file.getName()
						// + " Total file size to read (in bytes) : "
						// + fis.available());
						List<ExModelo> mods = importarXml(fis);
						for (ExModelo mod : mods) {
							String nmDiretorio = CalcularDiretorio(diretorio, file.getPath(), mod.getSubdiretorio());
							mod.setNmDiretorio(nmDiretorio);
							setNovo.add(mod);
						}
					} catch (Exception e) {
						throw new Exception("Não foi possível importar o XML de '" + file.getName() + "'.", e);
					}
				}
			}

			log("Importando: BD");
			setAntigo.addAll(importarTabela());

			log("Gravando alterações");
			gravar(dt);
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage());
		}
		long total = (System.currentTimeMillis() - inicio) / 1000;
		log("Tempo total de execução: " + total + " segundos (" + total / 60 + " min)");
	}

	protected static String CalcularDiretorio(String dir, String filepath, String xmlpath) {
		String s = filepath.replace("\\", "/");
		// Remover o nome do arquivo
		//
		s = s.substring(0, s.lastIndexOf("/"));

		// Não considerar os primeiros diretórios
		// decorrentes do path indicado no disco
		if (s.startsWith(dir))
			s = s.substring(dir.length());
		// Não considerar os últimos subdiretórios
		// decorrentes da espécie e dos separadores no nome
		// do modelo
		if (s.contains("/especie-"))
			s = s.substring(0, s.indexOf("/especie-"));
		if ("/".equals(s))
			return null;
		if (s.startsWith("/"))
			s = s.substring(1);
		if (s.endsWith("/"))
			s = s.substring(0, s.length() - 1);
		if ("".equals(s))
			return null;
		return s;
	}

	public List<ExModelo> importarXml(InputStream in) throws Exception {
		XmlPullParser parser = new KXmlParser();

		List<ExModelo> mods = new ArrayList<ExModelo>();

		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			parser.setInput(in, null);

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					if (parser.getName().equals("modelo")) {
						mods.add(importarXmlModelo(parser));
					}
				}
				parser.nextToken();
			}
			return mods;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (XmlPullParserException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	private ExModelo importarXmlModelo(XmlPullParser parser) throws Exception {
		ExModelo modelo = new ExModelo();

		try {
			modelo.setIdExterna(parseStr(parser, "uuid"));
			ExFormaDocumento forma = obterEspeciePelaDescricao(parseStr(parser, "especie"));
			ExClassificacao classificacao = obterClassificacaoPelaSigla(parseStr(parser, "classificacao"));
			ExClassificacao classCriacaoVia = obterClassificacaoPelaSigla(parseStr(parser, "classCriacaoVia"));
			ExNivelAcesso nivel = obterNivelAcessoPelaSigla(parseStr(parser, "nivel"));

			modelo.setNmMod(parseStr(parser, "nome"));
			modelo.setDescMod(parseStr(parser, "descricao"));
			modelo.setExFormaDocumento(forma);
			modelo.setExClassificacao(classificacao);
			modelo.setExClassCriacaoVia(classCriacaoVia);
			modelo.setExNivelAcesso(nivel);
			modelo.setNmArqMod(parseStr(parser, "arquivo"));
			modelo.setConteudoTpBlob(parseStr(parser, "tipo"));
			modelo.setNmDiretorio(parseStr(parser, "diretorio"));

			int token = parser.nextToken();
			while (true) {
				if (parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("modelo")) {
					return modelo;
				}
				if (token == XmlPullParser.CDSECT) {
					String cdata = parser.getText();
					cdata = cdata.replace("\r\n", "\n");
					modelo.setConteudoBlobMod2(cdata.getBytes("UTF-8"));
				}
				token = parser.nextToken();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected ExFormaDocumento obterEspeciePelaDescricao(String s) {
		if (s == null)
			return null;
		ExFormaDocumento especie = mapEspecies.get(s);
		return especie;
	}

	protected ExClassificacao obterClassificacaoPelaSigla(String s) throws Exception {
		if (s == null)
			return null;
		ExClassificacao classificacao = mapClassificacoes.get(s);
		// if (classificacao == null)
		// throw new Exception("Não foi possível localizar a classificação: '"
		// + s + "'");
		return classificacao;
	}

	protected ExNivelAcesso obterNivelAcessoPelaSigla(String s) {
		if (s == null)
			return null;
		ExNivelAcesso nivel = mapNiveisDeAcesso.get(s);
		return nivel;
	}

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

			// for (Sincronizavel item : setNovo) {
			// if (item instanceof ExModelo) {
			// ExModelo p = ((ExModelo) item);
			// if (p.getExFormaDocumento() == null) {
			// log("Modelo sem espécie! " + p.getNmMod());
			// }
			// }
			// }
			list = sinc.getOperacoes(dt, true);
		} catch (Exception e) {
			log("Transação abortada por erro: " + e.getMessage());
			throw new Exception("Erro na gravação", e);
		}
		try {
			ExDao.getInstance().iniciarTransacao();
			OperadorComHistorico o = new OperadorComHistorico() {
				public Sincronizavel gravar(Sincronizavel s , boolean descarregar) {

					Sincronizavel o = ExDao.getInstance().gravar(s);
				
					if (descarregar){
						ExDao.getInstance().getSessao().flush();
					}
					
					return o;
				}

			};

			for (Item opr : list) {
				log(opr.getDescricao());
				sinc.gravar(opr, o, true);
				manterHistoricoSeNecessario(opr);
			}
			if (modoLog) {
				log("");
				log("*********MODO LOG **********");
				log("As alterações não serão efetivadas! Executando rollback...");
				log("");
				log("");
				ExDao.getInstance().rollbackTransacao();
			} else if (maxSinc > 0 && list.size() > maxSinc) {
				log("");
				log("");
				log("***ATENÇÃO***: Limite de operações por sincronismo excedido!");
				log("Operações a serem executadas: " + list.size() + "\nOperações permitidas: " + maxSinc);
				log("Ajuste o parâmetro -maxSinc=<VALOR> para permitir que o sincronismo seja efetivado!");
				log("As alterações não serao efetivadas! Executando rollback...");
				log("");
				log("");

				ExDao.getInstance().rollbackTransacao();
			} else {

				ExDao.getInstance().commitTransacao();
				log("Transação confirmada");
			}
		} catch (Exception e) {
			ExDao.getInstance().rollbackTransacao();
			log("Transação abortada por erro: " + e.getMessage());
			throw new Exception("Erro na gravação", e);
		}

		HibernateUtil.getSessao().flush();
		log("Total de alterações: " + list.size());
	}

	protected static int parseParametros(String[] pars) {

		if (pars.length < 2) {
			System.err.println("Número de Parametros inválidos");
			return 10;
		}
		String servidor = pars[0];
		if (!servidor.toLowerCase().contains("-prod") && !servidor.toLowerCase().contains("-desenv")
				&& !servidor.toLowerCase().contains("-homolo") && !servidor.toLowerCase().contains("-treina")) {
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

	private void desativarCacheDeSegundoNivel() throws Exception {
		Ex.getInstance().getProp().setCacheUseSecondLevelCache(false);
		Ex.getInstance().getProp().setCacheUseQueryCache(false);
	}

	private void logComDestaque(String msg) {
		String espaco = "";
		for (int i = 0; i < 10; i++) {
			espaco += "\n";
		}
		log(espaco + msg + espaco);
	}

	public List<Sincronizavel> importarTabela() {
		List<Sincronizavel> l = new ArrayList<Sincronizavel>();

		for (ExModelo o : ExDao.getInstance().listarTodosModelosOrdenarPorNome(null, null)) {
			l.add(o);
		}
		return l;
	}

	private void importarListasDeTipos() {
		for (ExFormaDocumento especie : ExDao.getInstance().listarTodos(ExFormaDocumento.class, null))
			mapEspecies.put(especie.getDescricao(), especie);

		for (ExClassificacao classificacao : ExDao.getInstance().listarAtivos(ExClassificacao.class, null))
			mapClassificacoes.put(classificacao.getSigla(), classificacao);

		for (ExNivelAcesso nivel : ExDao.getInstance().listarTodos(ExNivelAcesso.class, null))
			mapNiveisDeAcesso.put(nivel.getNmNivelAcesso(), nivel);
	}

	public void log(String s) {
		logger.log(new LogRecord(logLevel, s));
	}

	public void logEnd() throws Exception {
		String sDest = ImportarXmlProperties.getString("lista.destinatario")
				+ (!destinatariosExtras.trim().equals("") ? "," + destinatariosExtras : "");
		if (getDataHora() != null) {
			log("Arquivo XML gerado em " + getDataHora() + "\n");
		}

		if (maxSinc != -1 && list != null && (list.size() > maxSinc)) {
			logHandler.setAssunto(
					"Limite de operações por sincronismo superior a 200. Execute o sincronismo manualmente.");
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
		log("Operações a serem executadas: " + list.size() + "\nOperações permitidas: " + maxSinc);
		log("Ajuste o parâmetro -maxSinc=<VALOR> para permitir que o sincronismo seja efetivado!");
		log("As alterações não serão efetivadas! Executando rollback...");
	}

	/**
	 * Verifica se a entidade que está sendo incluída à uma entidade que já
	 * existe e foi removida indevidamente ocasionando a perda do histórico
	 * 
	 * @param opr
	 * @return
	 */
	private void manterHistoricoSeNecessario(Item opr) {
		// ExModelo
		// if (opr.getNovo() != null && opr.getNovo() instanceof ExModelo) {
		// DpPessoa pesNova = (DpPessoa) opr.getNovo();
		// if (opr.getOperacao().equals(Operacao.incluir)) {
		// List<DpPessoa> historicoPessoa = ExDao.getInstance()
		// .consultarPorMatriculaEOrgao(pesNova.getMatricula(),
		// pesNova.getOrgaoUsuario().getId(), true, true);
		// log("*****************************" + pesNova.getMatricula());
		// if (historicoPessoa.size() > 0) {
		// DpPessoa pesAnterior = historicoPessoa.get(0);
		// if (pesAnterior != null
		// && !pesAnterior.getIdInicial().equals(
		// pesNova.getIdInicial())) {
		// pesNova.setIdInicial(pesAnterior.getIdInicial());
		// log("AVISO (PESSOA): ID_INICIAL reconectada ("
		// + pesNova.getSigla() + ")");
		// }
		//
		// }
		// }
		//
		// }
	}
}
