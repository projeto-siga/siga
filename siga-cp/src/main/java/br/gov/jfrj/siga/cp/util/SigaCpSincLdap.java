package br.gov.jfrj.siga.cp.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.mvel2.MVEL;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ldap.AdContato;
import br.gov.jfrj.siga.ldap.AdGrupo;
import br.gov.jfrj.siga.ldap.AdGrupoDeDistribuicao;
import br.gov.jfrj.siga.ldap.AdGrupoDeSeguranca;
import br.gov.jfrj.siga.ldap.AdModelo;
import br.gov.jfrj.siga.ldap.AdObjeto;
import br.gov.jfrj.siga.ldap.AdReferencia;
import br.gov.jfrj.siga.ldap.AdUsuario;
import br.gov.jfrj.siga.ldap.sinc.LdapBL;
import br.gov.jfrj.siga.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.OperadorSemHistorico;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelComparator;

public class SigaCpSincLdap extends SigaCpSinc {

	private Logger log = Logger.getLogger("br.gov.jfrj.log.sinc.ldap");
	private boolean modoLog = true;
	private boolean sincSenhas = false;
	private boolean exibirAlteracoesCargo = false;
	private boolean sairSemSincronizar = false;
	private static List<AdGrupo> jaProcessado = new ArrayList<AdGrupo>();
	private AdModelo leitorBD;
	private LdapBL leitorLDAP;
	private String carregarArquivoBD = null;


	private Map<String, String[]> restricao = new HashMap<String, String[]>();

	private SincronizavelComparator sc = new SincronizavelComparator();

	protected SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);

	protected SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(
			sc);

	protected Date dt;
	private String salvarArquivoBD = null;
	private String salvarLeiturasDir = null;
	
	private final int ERRO_ZERO = 0;
	private final int ERRO_ALGUM = 1;
	
	private int statusErro = ERRO_ZERO;

	private static SincProperties conf;

	private void gravarLDAP(Date dt, SincProperties conf) throws Exception {
		Sincronizador sinc = new Sincronizador();
		
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getOperacoes(dt);
		 
		if (modoLog) {
			System.out.println("\n\n>>>MODO LOG! Exibindo operações que seriam executadas...\n\n");
		}	

		if (maxSinc>0 && list.size()>maxSinc){
			exibirMensagemMaxSinc(list);

			return;
		}

		executarSincronizacao(sinc, list);	
		
		
		log("Total de alterações: " + list.size());
		// ((GenericoHibernateDao) dao).getSessao().flush();
	}

	private void executarSincronizacao(Sincronizador sinc, List<Item> list)
			throws Exception {
		try {
			OperadorSemHistorico o = new OperadorSemHistorico() {
				// @Override
				// public Sincronizavel alterarAntigo(Sincronizavel antigo) {
				// return antigo;
				// }
				//
				// @Override
				// public Sincronizavel alterarNovo(Sincronizavel novo) {
				// }

				@Override
				public Sincronizavel excluir(Sincronizavel antigo) {
					if (modoLog){
						simularExclusao(antigo);
						return antigo;							
					}
					
					Sincronizavel o = LdapBL.getInstance().excluir(
							(AdObjeto) antigo);
					return o;
				}

				@Override
				public Sincronizavel incluir(Sincronizavel novo) {
					if (modoLog){
							simularInclusao(novo);						
							return novo;							

					}
					
					Sincronizavel o = LdapBL.getInstance().incluir(
							(AdObjeto) novo);

					LdapBL.getInstance().limparSenhaSinc(novo);

					return o;
				}

				private void simularInclusao(Sincronizavel novo) {
					log("Atributos de: " + ((AdObjeto)novo).getNome());
					for (String s: LdapBL.getInstance().verAtributos((AdObjeto)novo)) {
						log("\t" + s);
					}
				}
				
				private void simularExclusao(Sincronizavel novo) {
					log("Atributos de: " + ((AdObjeto)novo).getNome());
					for (String s: LdapBL.getInstance().verAtributos((AdObjeto)novo)) {
						log("\t" + s);
					}
				}

				private List<Field> getAllFields(Sincronizavel obj, List<Field> campos) {
					Class<? extends AdObjeto> clazz = (Class<? extends AdObjeto>) obj.getClass();
					if (campos == null){
						return new ArrayList<Field>();
					}
					if (clazz != AdObjeto.class){
						campos.addAll(getAllFields(obj, campos));
					}else{
						campos.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
					}
					return campos;
				}

				@Override
				public Sincronizavel alterar(Sincronizavel antigo,
						Sincronizavel novo) {
					if (modoLog){
						simularAlteracao(antigo, novo);
						return antigo;							
					}
					
					Sincronizavel o = LdapBL.getInstance().alterar(
								(AdObjeto) antigo, (AdObjeto) novo);
	
					LdapBL.getInstance().limparSenhaSinc(novo);
					verificarAlteracaoCargo(antigo, novo);

					return o;
				}

				private void simularAlteracao(Sincronizavel antigo,
						Sincronizavel novo) {
					log("Atributos antigos de: " + ((AdObjeto)antigo).getNome());
					for (String s: LdapBL.getInstance().verAtributos((AdObjeto)antigo)) {
						if (s.contains("msExchMailboxGuid") || s.contains("msExchVersion")){
							continue;
						}
						log("\t" + s);
					}
					log("Atributos novos de: " + ((AdObjeto)novo).getNome());
					for (String s: LdapBL.getInstance().verAtributos((AdObjeto)novo)) {
						if (s.contains("msExchMailboxGuid") || s.contains("msExchVersion")){
							continue;
						}
						log("\t" + s);
					}
					if (novo instanceof AdUsuario && ((AdUsuario) novo).getSenhaCripto() != null){
						log("\t" + "Senha: SERÁ SINCRONIZADA!");
					}
					
					verificarAlteracaoCargo(antigo, novo);
				}

				private void verificarAlteracaoCargo(Sincronizavel antigo,
						Sincronizavel novo) {
					if (exibirAlteracoesCargo){
						if(novo instanceof AdUsuario && ((AdUsuario) novo).getCriarEmail()){
							if (!((AdUsuario) antigo).getHomeMDB().equals(
											((AdUsuario) novo).getHomeMDB())) {
								SigaCpSincLdap.this.log("\nCargo Alterado: "
										+ (((AdUsuario) antigo).getNome() + "\n\tDe:\t"
												+ ((AdUsuario) antigo).getHomeMDB().split(",")[0]
												+ "\n\tPara:\t" + ((AdUsuario) novo)
												.getHomeMDB().split(",")[0] + "\n"));
							}
						}
					}
				}

				
			};

			for (Item opr : list) {
				log(opr.getDescricao());
				sinc.gravar(opr, o, true);
			}

		} catch (Exception e) {
			throw new Exception("Erro na atualização do Active Directory.", e);
		}
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
		SigaCpSinc sinc = new SigaCpSincLdap(args);
		sinc.run();
	}

	public void ldap() throws NamingException, AplicacaoException, Exception {
		
		long inicio = System.currentTimeMillis();
		try {
			carregaConfiguracoes();
			validaRestricoes();

			List<AdObjeto> dadosBD = lerDadosDoBD();
			logarArquivoJson(dadosBD,salvarLeiturasDir!=null,salvarLeiturasDir + "\\leitura-bd.json");
			
			List<AdObjeto> dadosLDAP = lerDadosDoLDAP();
			logarArquivoJson(dadosLDAP,salvarLeiturasDir!=null,salvarLeiturasDir + "\\leitura-ldap.json");
			
			if (restricao.size() > 0) {
				// lBD = restringirGrupos(lBD);
				dadosBD = eliminarLotacoesAcimaDaRestricao(dadosBD);
				dadosLDAP = eliminarObjetosRestritos(dadosLDAP, dadosBD);
			}

			substituirContatosPorReferencias(dadosBD,leitorLDAP.getReferencias(),
					leitorLDAP.getCacheDnPorEmail());

			logarArquivoJson(dadosBD,salvarLeiturasDir!=null,salvarLeiturasDir + "\\bd-apos-contatos-por-referencia.json");

			if(sairSemSincronizar){
				log("\nParametro sairSemSincronizar=true. Encerrando sem realizar sincroniza��o.");
				System.exit(0);	
			}
			
			setNovo.addAll(dadosBD);
			setAntigo.addAll(dadosLDAP);

			log("\nActive Directory importado com sucesso!");

			gravarLDAP(dt, conf);
		} catch (Exception e) {
			registraErroNoLog(e);
			statusErro = ERRO_ZERO;
		}

		long total = (System.currentTimeMillis() - inicio) / 1000;
		log("Tempo total de execução: " + total + " segundos (" + total / 60
				+ " min)");

		System.exit(statusErro);
	}

	private void logarArquivoJson(List<AdObjeto> objetosLidos, boolean salvarArquivo, String nomeArquivo) {
		
		if (!salvarArquivo){ return; }
		
		JsonArray resultado = new JsonArray();
	
		for (AdObjeto obj : objetosLidos) {
			JsonObject jsonObjLido = new JsonObject();
						
			jsonObjLido.addProperty("idExterna",obj.getIdExterna());
			jsonObjLido.addProperty("nome",obj.getNome());
			jsonObjLido.addProperty("nomeCompleto",obj.getNomeCompleto());
			
			if(obj instanceof AdContato){
				jsonObjLido.addProperty("tipo", "Contato");
			}
			
			if(obj instanceof AdUsuario){
				jsonObjLido.addProperty("tipo", "Usuario");
				AdUsuario u = ((AdUsuario)obj);
				jsonObjLido.addProperty("sigla", u.getSigla());
			}
			
			if(obj instanceof AdReferencia){
				jsonObjLido.addProperty("tipo", "Referencia");
				AdReferencia r = ((AdReferencia)obj);
				jsonObjLido.addProperty("referencia", r.getReferencia());
			}
			
			if(obj instanceof AdGrupo){
				
				AdGrupo g = null;
				
				if(obj instanceof AdGrupoDeSeguranca){
					AdGrupoDeSeguranca gs = ((AdGrupoDeSeguranca)obj);
					g=gs;
					jsonObjLido.addProperty("tipo", "Grupo de Seguran�a");
				}

				if(obj instanceof AdGrupoDeDistribuicao){
					AdGrupoDeDistribuicao gd = ((AdGrupoDeDistribuicao)obj);
					g=gd;
					jsonObjLido.addProperty("tipo", "Grupo de Distribui��o");
				}
				
				
				JsonArray membrosGrupo = new JsonArray();
				
				for (AdObjeto m : g.getMembros()) {
					JsonObject jsonGrupo = new JsonObject();
					jsonGrupo.addProperty("nome", m.getNome());
					jsonGrupo.addProperty("nomeCompleto", m.getNomeCompleto());
					membrosGrupo.add(jsonGrupo);
				}
				
				jsonObjLido.add("membros", membrosGrupo);
				
			}
			
			JsonArray pertenceA = new JsonArray();
			
			for (AdObjeto grp : obj.getGruposPertencentes()) {
				JsonObject jsonGrupo = new JsonObject();
				jsonGrupo.addProperty("nome", grp.getNome());
				pertenceA.add(jsonGrupo);
			}
			
			jsonObjLido.add("pertence", pertenceA);

			resultado.add(jsonObjLido);
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy.HH.mm.ss");
		Date date = new Date();

		
		salvarParaArquivo(new GsonBuilder().create().toJson(resultado), nomeArquivo + "-" + dateFormat.format(date));
		
	}

	private void registraErroNoLog(Exception e) {
		log("\n\n\n*********  ERRO!  **********");
		log("*********  ERRO!  **********");
		log("*********  ERRO!  **********\n\n\n");

		log(e.getMessage() + "\n");
		if (e.getCause() != null) {
			log(e.getCause().getMessage() + "\n");
		}

		for (StackTraceElement s : e.getStackTrace()) {
			log(s.toString());
		}
		log("\n");
	}

	private List<AdObjeto> lerDadosDoLDAP() throws NamingException {
		log("Importando: Active Directory");
		System.out.print("Testando conexão com LDAP...");
		
		leitorLDAP = LdapBL.getInstance(conf);

		if (leitorLDAP == null) {
			System.out.println("FALHOU!");
			throw new AplicacaoException(
					"Não foi possível fazer a conexão com o LDAP!");
		}
		System.out.println("OK!");

		carregarEmailsEmCache(leitorLDAP);
		
		List<AdObjeto> dadosLDAP = leitorLDAP.pesquisarObjeto(conf
				.getDnGestaoIdentidade());
		
		return dadosLDAP;
	}

	private List<AdObjeto> lerDadosDoBD() {
		log("Importando: BD");
		if (sincSenhas){
			log("****  As senhas SERÃO sincronizadas!  ***");
		}else{
			log("****  As senhas NÃO SERÃO sincronizadas!  ***");
		}

		leitorBD = AdModelo.getInstance(conf);
		List<AdObjeto> dadosBD = null;
		if (carregarArquivoBD !=null){
			try{
				dadosBD = carregarDoArquivo(carregarArquivoBD);
			}catch (Exception e) {
				dadosBD = lerDoBancoDeDados();
			}
		}else{
			dadosBD = lerDoBancoDeDados();
		}
		
		return dadosBD;
	}

	private List<AdObjeto> lerDoBancoDeDados() {
		List<AdObjeto> dadosBD;
		dadosBD = leitorBD.gerarModelo(consultarPessoas(),consultarLotacoes(),sincSenhas);
		if (salvarArquivoBD != null){
			salvarParaArquivo(dadosBD,salvarArquivoBD);
		}
		return dadosBD;
	}

	private void salvarParaArquivo(List<AdObjeto> lista, String arq) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(arq);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(lista);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void salvarParaArquivo(String json, String arq) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(arq);
			fos.write(json.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private List<AdObjeto> carregarDoArquivo(String arq) {
		List<AdObjeto> lista = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(arq);
			ois = new ObjectInputStream(fis);
			lista = (List<AdObjeto>) ois.readObject();
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao carregar arquivo " + arq);
		}finally{
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lista;
	}

	private void validaRestricoes() {
		if (restricao.size() >0){
			if (restricao.get("lotacao") ==null && restricao.get("pessoa") ==null){
				throw new AplicacaoException("Erro na sintaxe da restrição! Por exemplo, use: -restringir=lotacao:LOTACAO1,LOTACAO2;pessoa=RJ12345,RJ54321");
			}
			log("***  Sincronismo restrito: ***");
			if (restricao.get("lotacao") !=null && restricao.get("lotacao").length > 0){
				log("\tLotações:" + Arrays.deepToString(restricao.get("lotacao")));
			}
			
			if (restricao.get("pessoa")!=null && restricao.get("pessoa").length > 0){
				log("\tPessoas:" + Arrays.deepToString(restricao.get("pessoa")));
			}
		}else{
//				throw new AplicacaoException("Defina uma restrição!\n\n\n\nPor exemplo, use: -restringir=lotacao:LOTACAO1,LOTACAO2;pessoa=RJ12345,RJ54321");
		}
	}

	private void carregaConfiguracoes() {
		conf = SincProperties.getInstancia(SincProperties.getInstancia()
				.getPrefixoModulo()
				+ "."
				+ getUrl().replace("-", "")
				+ "."
				+ getServidor().replace("-", ""));
	}

	/**
	 * Altera os objetos AdContato por AdReferencias 
	 * de modo que o dn de um membro do grupo aponte para outro objeto em do contato
	 * @param lBD
	 * @param referencias
	 * @param map
	 */
	private void substituirContatosPorReferencias(List<AdObjeto> lBD,List<AdReferencia> referencias,
			Map<String, Set<String>> map) {
		
		List<AdContato> contatos = new ArrayList<AdContato>();
		for (AdObjeto objBD : lBD) {
			if (objBD instanceof AdContato){
				contatos.add((AdContato)objBD);
			}
		}
		
		for (AdContato contato : contatos) {
			Set<String> listaDn = map.get(contato.getNome());
			
			if(listaDn!=null){
				for (String dn : listaDn) {
					if (dn != null && !dn.endsWith(conf.getDnContatos())) {
						AdReferencia novaRef = new AdReferencia(contato.getNome(), contato.getNome(), conf.getDnDominio());
						novaRef.setReferencia(dn);
						for (AdGrupo g: contato.getGruposPertencentes()){
							g.acrescentarMembro(novaRef);
							g.removerMembro(contato);
						}
						lBD.add(novaRef);
						lBD.remove(contato);
					}
				}
			}
		}
	}

	/**
	 * Faz a leitura da árvore de extras para carregar os e-mails no cache.
	 * Isso é útil para verificar se os contatos devem ser criados ou substituídos
	 * por outros objetos do AD
	 * 
	 * @param ldap
	 * @throws NamingException
	 */
	private void carregarEmailsEmCache(LdapBL ldap) throws NamingException {
		ldap.pesquisarObjeto(conf.getDnExtras());
	}

	private List<DpLotacao> consultarLotacoes() {

		CpDao dao = CpDao.getInstance();
		
		if (restricao.size() > 0) {
			Set<DpLotacao> lotacoesPaiRestricao = new HashSet<DpLotacao>();
			CpOrgaoUsuario orgaoUsuario = dao.consultar(
					Long.valueOf(conf.getIdLocalidade()), CpOrgaoUsuario.class,
					false);

			try {
				String[] lotacoes = restricao.get("lotacao");
				if (lotacoes != null) {
					
					for (String sigla : lotacoes) {
						DpLotacao lotacaoFiltro = new DpLotacao();
						lotacaoFiltro.setSigla(sigla);
						lotacaoFiltro.setOrgaoUsuario(orgaoUsuario);
						DpLotacao l = dao.consultarPorSigla(lotacaoFiltro);
						if (l!=null){
							lotacoesPaiRestricao.add(l);
						}
					}
				}
			} catch (Exception e) {
				// ignora
			}
			return new ArrayList<DpLotacao>(getSetoresSubordinados(lotacoesPaiRestricao));
		}else{
			List<DpLotacao> lotacoes = null;
			lotacoes = dao.listarAtivos(DpLotacao.class, "dataFimLotacao" , Long.valueOf(conf.getIdLocalidade()));
			
			return lotacoes;
		}
		
		
	}

	private Set<DpLotacao> getSetoresSubordinados(Set<DpLotacao> lista) {
		Set<DpLotacao> todosSubordinados = new HashSet<DpLotacao>();

		for (DpLotacao pai : lista) {
			if (pai.getDpLotacaoSubordinadosSet().size() <= 0) {
				todosSubordinados.add(pai);
				continue;
			} else {
				todosSubordinados.add(pai);
				todosSubordinados.addAll(getSetoresSubordinados(pai
						.getDpLotacaoSubordinadosSet()));
			}
		}

		return todosSubordinados;
	}
	
	private List<AdObjeto> eliminarLotacoesAcimaDaRestricao(List<AdObjeto> lista) {
		//encontra o grupo de distribuição raiz
		Set<AdObjeto> paraRemover = new HashSet<AdObjeto>();
		for (AdObjeto o : lista) {
			if ((o instanceof AdUsuario)){
				AdUsuario u = (AdUsuario)o;
				if (restricao.get("pessoa") != null){
					for (String pes : restricao.get("pessoa")) {
						if (pes.equalsIgnoreCase(u.getNome())){
							paraRemover.addAll(getAcima(u.getGruposPertencentes(), null));
						}
					}
				}
			}

			if ((o instanceof AdGrupoDeDistribuicao)){
				AdGrupoDeDistribuicao gd = (AdGrupoDeDistribuicao)o;
				if (restricao.get("lotacao") != null){
					String prefixo = null;
					String sufixo = null;

					if (LdapBL.getInstance(conf).isGrupoDistribuicaoManualEmail(o.getNome())){
						prefixo = conf.getPfxGrpDistrAuto();
						sufixo = conf.getSfxGrpDistrManualEmail();
					}else{
						prefixo = conf.getPfxGrpDistrAuto();
						sufixo = conf.getSfxGrpDistrAuto();
					}
					for (String lot : restricao.get("lotacao")) {
						if (gd.getNome().equalsIgnoreCase(prefixo + lot + sufixo)){
							Set<AdGrupo> gruposAcima = getAcima(gd.getGruposPertencentes(),null); 
							if (gruposAcima !=null){
								paraRemover.addAll(gruposAcima);
								for (AdGrupo adGrupo : gruposAcima) {
									
									//manter o grupo se for manual
									if (adGrupo.getNome().endsWith(conf.getSfxGrpDistrManualEmail())){
										List<AdObjeto> membrosMantidos = new ArrayList<AdObjeto>();
										for (AdObjeto membro: adGrupo.getMembros()){
											if (gd.contemMembro(membro)){
												membrosMantidos.add(membro);
											}
										}
										
										if (adGrupo.getMembros().contains(gd)){
											membrosMantidos.add(gd);
										}
								
										//deixa apenas membros mantidos
										adGrupo.getMembros().retainAll(membrosMantidos);
										
										paraRemover.remove(adGrupo);
										continue;
									}
									
									
								}
							}
						}
					} 
				}
			}
			
			if ((o instanceof AdGrupoDeSeguranca)){
				AdGrupoDeSeguranca gs = (AdGrupoDeSeguranca)o;
				if (restricao.get("lotacao") != null){
					String prefixo = conf.getPfxGrpSegAuto();
					String sufixo = conf.getSfxGrpSegAuto();
					for (String lot : restricao.get("lotacao")) {
						if (gs.getNome().equalsIgnoreCase(prefixo + lot + sufixo)){
							Set<AdGrupo> gruposAcima = getAcima(gs.getGruposPertencentes(),null);
							if (gruposAcima !=null){
								paraRemover.addAll(gruposAcima);	
								for (AdGrupo adGrupo : gruposAcima) {
									
									//manter o grupo se for manual
									if (adGrupo.getNome().endsWith(conf.getSfxGrpSegManualPerfil()) || 
											adGrupo.getNome().endsWith(conf.getSfxGrpSegManualPerfilJEE())){
										List<AdObjeto> membrosMantidos = new ArrayList<AdObjeto>();
										for (AdObjeto membro: adGrupo.getMembros()){
											if (gs.contemMembro(membro)){
												membrosMantidos.add(membro);
											}
										}
										
										if (adGrupo.getMembros().contains(gs)){
											membrosMantidos.add(gs);
										}
								
										//deixa apenas membros mantidos
										adGrupo.getMembros().retainAll(membrosMantidos);
										
										paraRemover.remove(adGrupo);
										continue;
										
									}
								}
							}
						}
					} 
				}
			}
		}
		
		//se possui gs correspondente (Como os gs não possuem as lotações como membro, deve-se usar o análogo do _gd)
		List<AdObjeto> listaCorrespondentes = new ArrayList<AdObjeto>();
		for (AdObjeto del : paraRemover) {
			if (del instanceof AdGrupoDeDistribuicao){
				String gsCorrespondente = del.getNome().replace(conf.getPfxGrpDistrAuto(), "");
				gsCorrespondente = gsCorrespondente.replace(conf.getSfxGrpDistrAuto(), conf.getSfxGrpSegAuto());
				for (AdObjeto adObjeto : lista) {
					if (adObjeto.getNome().equalsIgnoreCase(gsCorrespondente)){
						listaCorrespondentes.add(adObjeto);
						break;
					}
				}
			}
		}
		
		lista.removeAll(paraRemover);
		lista.removeAll(listaCorrespondentes);
		return lista;
		//encontra o grupo de segurança raiz
		

	}

	
	private Set<AdGrupo> getAcima(Set<AdGrupo> grupos,Set<AdGrupo> resultado) {
			for (AdGrupo adGrupo : grupos) {
				if (jaProcessado.contains(adGrupo)){
					return resultado;
				}else{
					jaProcessado.add(adGrupo);
				}
				Set<AdGrupo> gruposPertencentes = adGrupo.getGruposPertencentes();
				if (gruposPertencentes !=null && gruposPertencentes.size() > 0){
					if (resultado == null){
						resultado = new HashSet<AdGrupo>();
					}
					resultado.add(adGrupo);
					resultado.addAll(getAcima(gruposPertencentes,resultado));
					
				}else{
					break;
				}
			}
		return resultado;
	}

	private List<AdObjeto> restringirGrupos(List<AdObjeto> lista) {
		List<AdObjeto> apenasComMembros = new ArrayList<AdObjeto>();
		apenasComMembros.addAll(lista);

		for (AdObjeto o : lista) {
			if ((o instanceof AdGrupoDeDistribuicao) && ((AdGrupo)o).getMembros().size()==0){
				for (String lot : restricao.get("lotacao")) {
					if (LdapBL.getInstance(conf).isGrupoDistribuicaoManualEmail(o.getNome())){
						if (!o.getNome().equalsIgnoreCase(conf.getPfxGrpDistrAuto() + lot + conf.getSfxGrpDistrManualEmail())){
							apenasComMembros.remove(o);
						}
					}else{
						if (!o.getNome().equalsIgnoreCase(conf.getPfxGrpDistrAuto() + lot + conf.getSfxGrpDistrAuto())){
							apenasComMembros.remove(o);
						}
					}
				}
			}
			
			if ((o instanceof AdGrupoDeSeguranca) && ((AdGrupo)o).getMembros().size()==0){
				for (String lot : restricao.get("lotacao")) {
					if (!o.getNome().equalsIgnoreCase(conf.getPfxGrpSegAuto() + lot + conf.getSfxGrpSegAuto())){
						apenasComMembros.remove(o);
					}
				}
			}
		}
		return apenasComMembros;
	}

	/**
	 * Para operar apenas nos elementos restritos,
	 * 
	 * @param restricao
	 *            - lista de objetos que devem ser utilizados no sincronismo
	 * @param 
	 * aRestringir
	 *            - lista de objetos que sofrerá a restrição
	 */
	private List<AdObjeto> eliminarObjetosRestritos(List<AdObjeto> ldap,
			List<AdObjeto> bd) {
		List<AdObjeto> itensRetidos = new ArrayList<AdObjeto>();

		for (AdObjeto l : ldap) {
			// elimina os objetos restritos diretamente 
			for (AdObjeto b : bd) {
				if ((l.getGruposPertencentes().contains(b) || l.getClass().equals(b.getClass())
						&& l.getIdExterna().equals(b.getIdExterna())) || (l instanceof AdGrupo && ((AdGrupo)l).getMembros().contains(b))) {
					itensRetidos.add(l);
					break;
				}
			}
		}


		if (restricao.get("pessoa") != null){
			for (String p : restricao.get("pessoa")) {
				for (AdObjeto l : ldap) {
					if (l.getNome().equals(p)){
						itensRetidos.add(l);
					}
				}
			}
		}
		
		if (restricao.get("lotacao") != null){
			for (String p : restricao.get("lotacao")) {
				for (AdObjeto l : ldap) {
					if (l.getNome().equals(p)){
						itensRetidos.add(l);
					}
				}
			}
		}

		return itensRetidos;

	}

	public SigaCpSincLdap(String[] args) {
		super(args);
		int result = parseParametros(args);
		if (result != 0)
			System.exit(result);

		String aUrl = "";
		String oServidor = args[0].toLowerCase();
		if (args[1].equalsIgnoreCase("-url"))
			aUrl = args[1].toLowerCase() + "=" + args[2];
		else
			aUrl = args[1].toLowerCase();

		setServidor(oServidor);
		setUrl(aUrl);

		for (String param : Arrays.asList(args)) {
			if (param.equals("-modoLog=false")) {
				modoLog = false;
			}

			if (param.startsWith("-restringir=")) {
				String definicaoRestricao = param
						.substring(param.indexOf("=") + 1);
				String[] elementosRestringidos = definicaoRestricao.split(";");
				for (String e : elementosRestringidos) {
					String tipo = e.split(":")[0];
					String[] nomes = e.split(":")[1].split(",");
					restricao.put(tipo, nomes);
				}
			}
			
			if (param.equalsIgnoreCase("-sincSenhas=true")){
				sincSenhas = true;
			}
			
			if (param.equalsIgnoreCase("-exibirAlteracoesCargo=true")){
				exibirAlteracoesCargo = true;
			}
			
			if (param.startsWith("-carregarArquivoBD=")) {
				carregarArquivoBD = param.split("=")[1];
			}
			
			if (param.startsWith("-salvarArquivoBD=")) {
				salvarArquivoBD = param.split("=")[1];
			}

			if (param.startsWith("-salvarLeiturasDir=")) {
				salvarLeiturasDir = param.split("=")[1];
			}

			if (param.equalsIgnoreCase("-sairSemSincronizar=true")){
				sairSemSincronizar = true;
			}

		}
	}

	public List<DpPessoa> consultarPessoas() {
		CpDao dao = CpDao.getInstance();

		Set<DpPessoa> pessoasConsultadas = new HashSet<DpPessoa>();
		if (restricao.size() > 0) {
			CpOrgaoUsuario orgaoUsuario = dao.consultar(
					Long.valueOf(conf.getIdLocalidade()), CpOrgaoUsuario.class,
					false);

			String[] pessoas = restricao.get("pessoa");
			if (pessoas != null) {
				Map<String,DpPessoa> params = new HashMap<String, DpPessoa>();
				for (String sigla : pessoas) {
					DpPessoa pessoaFiltro = new DpPessoa();
					pessoaFiltro.setSigla(sigla);
					pessoaFiltro.setOrgaoUsuario(orgaoUsuario);

					DpPessoa p = dao.consultarPorSigla(pessoaFiltro);

					if (p != null) {
						params.put("pessoa",p);
						if((Boolean)MVEL.eval(conf.getExpressaoUsuariosAtivos(),params)){
							pessoasConsultadas.add(p);
						}
					}
				}
			}

			try {
				String[] lotacoes = restricao.get("lotacao");
				if (lotacoes != null) {
					for (String sigla : lotacoes) {
						DpLotacao lotacaoFiltro = new DpLotacao();
						lotacaoFiltro.setSigla(sigla);
						lotacaoFiltro.setOrgaoUsuario(orgaoUsuario);
						DpLotacao l = dao.consultarPorSigla(lotacaoFiltro);
						if (l!=null){
							List<DpPessoa> pessoasNaLotacao = dao.pessoasPorLotacao(l.getId(), true,false);
								if (pessoasNaLotacao != null) {
									pessoasConsultadas.addAll(pessoasNaLotacao);
								}	
						}
					}
				}
			} catch (Exception e) {
				// ignora
			}

		}else{
			 DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			 flt.setNome("");
			 flt.setSigla("");
			
			 flt.setBuscarFechadas(false);
			 flt.setIdOrgaoUsu(Long.valueOf(conf.getIdLocalidade()));

			List<DpPessoa> pessoas = dao.consultarPorFiltro(flt);

			Iterator<DpPessoa> it = pessoas.iterator();
			Map<String,DpPessoa> params = new HashMap<String, DpPessoa>();
			while(it.hasNext()){
				DpPessoa p = it.next();
				params.put("pessoa",p);

				if(!(Boolean)MVEL.eval(conf.getExpressaoUsuariosAtivos(),params)){
					it.remove();
				}
			}
			pessoasConsultadas.addAll(pessoas);

			
		}
		
		return new ArrayList<DpPessoa>(pessoasConsultadas);

	}

}