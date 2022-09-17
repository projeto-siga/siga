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
package br.gov.jfrj.ldap.sinc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.ldap.AdModelo;
import br.gov.jfrj.ldap.AdObjeto;
import br.gov.jfrj.ldap.AdUsuario;
import br.gov.jfrj.ldap.sinc.LdapBL;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class VerificadorPessoasLDAP {

	private static LdapBL ldap;
	private static final SincProperties conf = SincProperties.getInstancia();

	private static Logger log = Logger.getLogger(PadronizadorLDAP.class
			.getName());
	private static Level nivelLog = Level.FINE;

	private List<Resultado> encontradosNoBD;
	private List<Resultado> naoEncontradosNoBD;
	private List<String> problemasEncontrados;

	public static void main(String[] args) throws Exception {
		// new VerificadorPessoasLDAP().verificar();
		// new VerificadorPessoasLDAP().verificarSiglas();
		// new VerificadorPessoasLDAP().redefinirUserPrincipalName();
		// new VerificadorPessoasLDAP().reativarContasEMoverParaExtras();
//		new VerificadorPessoasLDAP().inserirCxsPostais();

	}

	private void inserirCxsPostais() {
		String[] matriculas = new String[] { "RJ41639", "RJ44318",
				"RJ44406", "RJ44408", "RJ44518", "RJ44495", "RJ44439",
				"RJ44423", "RJ44509", "RJ44593", "RJ44193", "RJ44118",
				"RJ30261", "RJ44592", "RJ44403", "RJ44568", "RJ42759",
				"RJ44375", "RJ44506", "RJ43483", "RJ44570", "RJ44562",
				"RJ44525", "RJ44417", "RJ43664", "RJ50669",
				"RJ44473", "RJ44342", "RJ44422", "RJ44358", "RJ44395",
				"RJ44369", "RJ44397", "RJ44366", "RJ44526", "RJ44394",
				"RJ44356", "RJ44575", "RJ44477", "RJ30244", "RJ43675",
				"RJ44478", "RJ44576", "RJ43207", "RJ44501", "RJ44390",
				"RJ44407", "RJ44391", "RJ44571", "RJ44392", "RJ50632",
				"RJ44337", "RJ44385", "RJ44499", "RJ44496", "RJ44524",
				"RJ44532", "RJ44465", "RJ44354", "RJ44464", "RJ43482",
				"RJ44355", "RJ44442", "RJ44401", "RJ44580", "RJ44536",
				"RJ44540", "RJ50663", "RJ50665", "RJ44541", "RJ44100",
				"RJ44096", "RJ50662", "RJ44370", "RJ44533" };

		List<String> paraInserirCx = new ArrayList<String>();
		paraInserirCx.addAll(Arrays.asList(matriculas));
		
		Configuration cfg;
		try {
			cfg = CpDao.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
			HibernateUtil.configurarHibernate(cfg);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		SincProperties conf = SincProperties
				.getInstancia("siga.cp.sinc.ldap.sjrj.prod");
		ldap = LdapBL.getInstance(conf);
		AdModelo ge = AdModelo.getInstance(conf);
		try {
			List<AdObjeto> lBD = ge.gerarModelo(consultarPessoas(),null,false);
			
			for (AdObjeto m : lBD) {
				if (m instanceof AdUsuario){
					if (paraInserirCx.contains(((AdUsuario) m).getNome())){
						ldap.alterarAD(m, m);
						System.out.println(((AdUsuario) m).getNome());
					}
				}

				
			}
			
		} catch (AplicacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public List<DpPessoa> consultarPessoas() {
		CpDao dao = CpDao.getInstance();

		Set<DpPessoa> pessoasConsultadas = new HashSet<DpPessoa>();
		
		 DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		 flt.setNome("");
		 flt.setSigla("");
		
		 flt.setSituacaoFuncionalPessoa(conf.getIdSituacaoFuncionalAtivo().toString());
		 flt.setBuscarFechadas(false);
		 flt.setIdOrgaoUsu(Long.valueOf(conf.getIdLocalidade()));

		List<DpPessoa> pessoas = dao.consultarPorFiltro(flt);
		pessoasConsultadas.addAll(pessoas);
		
		
		return new ArrayList<DpPessoa>(pessoasConsultadas);

	}

	
	private void reativarContasEMoverParaExtras() throws NamingException,
			AplicacaoException {
		String[] matriculas = new String[] { "RJ10254", "RJ10281", "RJ10292",
				"RJ10309", "RJ10310", "RJ10360", "RJ10362", "RJ10367",
				"RJ10399", "RJ10415", "RJ10418", "RJ10508", "RJ10519",
				"RJ10534", "RJ10541", "RJ10545", "RJ10555", "RJ10582",
				"RJ10598", "RJ10599", "RJ10665", "RJ10682", "RJ10686",
				"RJ10726", "RJ10733", "RJ10738", "RJ10748", "RJ10763",
				"RJ10764", "RJ10788", "RJ10824", "RJ10839", "RJ10849",
				"RJ10850", "RJ10963", "RJ10982", "RJ10984", "RJ10992",
				"RJ11003", "RJ11005", "RJ11049", "RJ11064", "RJ11073",
				"RJ11096", "RJ11161", "RJ11166", "RJ11202", "RJ11203",
				"RJ11223", "RJ11361", "RJ11380", "RJ11389", "RJ11407",
				"RJ11417", "RJ11430", "RJ11480", "RJ11534", "RJ11565",
				"RJ11581", "RJ11583", "RJ11691", "RJ11954", "RJ11962",
				"RJ12003", "RJ12100", "RJ12137", "RJ12157", "RJ12278",
				"RJ12312", "RJ12318", "RJ12326", "RJ12362", "RJ12390",
				"RJ12410", "RJ12416", "RJ12419", "RJ12467", "RJ12512",
				"RJ12564", "RJ12591", "RJ12597", "RJ12599", "RJ12610",
				"RJ12680", "RJ12717", "RJ12792", "RJ12802", "RJ12849",
				"RJ12857", "RJ12858", "RJ12919", "RJ12947", "RJ12989",
				"RJ13074", "RJ13087", "RJ13135", "RJ13136", "RJ13144",
				"RJ13151", "RJ13175", "RJ13254", "RJ13296", "RJ13330",
				"RJ13376", "RJ13385", "RJ13420", "RJ13476", "RJ13500",
				"RJ13505", "RJ13512", "RJ13519", "RJ13551", "RJ13568",
				"RJ13600", "RJ13615", "RJ13649", "RJ13674", "RJ13693",
				"RJ13696", "RJ13709", "RJ13736", "RJ13739", "RJ13741",
				"RJ13764", "RJ13779", "RJ13790", "RJ13845", "RJ13851",
				"RJ13876", "RJ13883", "RJ13889", "RJ13902", "RJ13931",
				"RJ13958", "RJ13967", "RJ13968", "RJ13977", "RJ14010",
				"RJ14045", "RJ14072", "RJ14090", "RJ14115", "RJ14131",
				"RJ14165", "RJ14229", "RJ14230", "RJ14242", "RJ14270",
				"RJ14293", "RJ14408", "RJ14417", "RJ14535", "RJ14540",
				"RJ14542", "RJ14576", "RJ16008", "RJ16009", "RJ16012",
				"RJ16017", "RJ16019", "RJ16021", "RJ16022", "RJ16023",
				"RJ17053", "RJ17054", "RJ17152", "RJ17162" };
		List<String> paraReativar = new ArrayList<String>();
		paraReativar.addAll(Arrays.asList(matriculas));

		SincProperties conf = SincProperties
				.getInstancia("siga.cp.sinc.ldap.sjrj.prod");
		ldap = LdapBL.getInstance(conf);
		List<AdObjeto> listaAD = ldap.pesquisarObjeto(conf
				.getDnUsuariosInativos());
		for (AdObjeto adObjeto : listaAD) {
			if (adObjeto instanceof AdUsuario) {
				AdUsuario u = (AdUsuario) adObjeto;

				if (paraReativar.contains(u.getNome())) {

					// remove o prefixo _delete
					Attributes attrs = new BasicAttributes(true);
					attrs.put("samAccountName",
							u.getSigla().replace("delete_", "").trim());
					try {
						String dnUsuario = "CN="
								+ u.getNome()
								+ ",OU=Usuarios Inativos,OU=Gestao de Identidade,DC=corp,DC=jfrj,DC=gov,DC=br";
						ldap.substituirAtributos(dnUsuario, attrs, null);

						ldap.ativarUsuario(dnUsuario);

						ldap.moverObjeto(
								dnUsuario,
								"CN="
										+ u.getNome()
										+ ",OU=TRF,OU=Extras,OU=Gestao de Identidade,DC=corp,DC=jfrj,DC=gov,DC=br");
						System.out.println("Ativado e movido:" + u.getNome());
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	private void redefinirUserPrincipalName() throws NamingException,
			AplicacaoException {
		SincProperties conf = SincProperties
				.getInstancia("siga.cp.sinc.ldap.sjrj.prod");
		ldap = LdapBL.getInstance(conf);
		List<AdObjeto> listaAD = ldap.pesquisarObjeto(conf
				.getDnGestaoIdentidade());
		for (AdObjeto adObjeto : listaAD) {
			if (adObjeto instanceof AdUsuario) {
				AdUsuario u = (AdUsuario) adObjeto;
				Attributes attrs = new BasicAttributes(true);
				attrs.put("userPrincipalName",
						u.getSigla() + conf.getUserPrincipalNameDomain());
				ldap.substituirAtributos(u.getNomeCompleto(), attrs, null);
			}
		}
	}

	private void verificarSiglas() {
		try {

			SincProperties conf = SincProperties
					.getInstancia("siga.cp.sinc.ldap.sjrj.prod");
			ldap = LdapBL.getInstance(conf);

			Configuration cfg = CpDao
					.criarHibernateCfg(CpAmbienteEnumBL.PRODUCAO);
			HibernateUtil.configurarHibernate(cfg);

			List<DpPessoa> listaBD = CpDao.getInstance().listarAtivos(
					DpPessoa.class, "dataFimPessoa", 1L);
			List<AdObjeto> listaAD = ldap.pesquisarObjeto(conf
					.getDnGestaoIdentidade());

			HashMap<String, String> mapaAD = new HashMap<String, String>();
			for (AdObjeto adObjeto : listaAD) {
				if (adObjeto instanceof AdUsuario) {
					AdUsuario adU = (AdUsuario) adObjeto;
					mapaAD.put(adU.getSigla(), adU.getNome().toLowerCase());
				}
			}

			for (DpPessoa dpPessoa : listaBD) {
				if (mapaAD.get(dpPessoa.getSiglaPessoa().toLowerCase()) == null) {
					continue;
				}
				if (!mapaAD.get(dpPessoa.getSiglaPessoa().toLowerCase())
						.equalsIgnoreCase(dpPessoa.getSigla())) {
					System.out
							.printf("\nMATRICULA: %s BD:%s AD:%s", dpPessoa
									.getSigla(), dpPessoa.getSiglaPessoa(),
									mapaAD.get(dpPessoa.getSiglaPessoa()
											.toLowerCase()));
				}

			}

		} catch (Exception e) {

		}

	}

	public void verificar() throws Exception {
		Configuration cfg = CpDao.criarHibernateCfg(
				conf.getBdStringConexao(), conf.getBdUsuario(),
				conf.getBdSenha());
		HibernateUtil.configurarHibernate(cfg);

		encontradosNoBD = new ArrayList<Resultado>();
		naoEncontradosNoBD = new ArrayList<Resultado>();
		problemasEncontrados = new ArrayList<String>();
		ldap = LdapBL.getInstance();
		System.out.println("Lendo Active Directory...");

		List<AdObjeto> lista = ldap.pesquisarObjeto(conf
				.getDnGestaoIdentidade());

		System.out.println("Comparando o AD com o Corporativo...");
		Session session = HibernateUtil.getSessao();
		List<Object> listaSiglas = session.createSQLQuery(
				"SELECT DISTINCT SIGLA_PESSOA FROM DP_PESSOA WHERE ID_ORGAO_USU = "
						+ conf.getIdLocalidade()).list();

		boolean encontrado;
		AdUsuario u = null;
		for (AdObjeto o : lista) {
			encontrado = false;
			if (o instanceof AdUsuario) {
				for (Object s : listaSiglas) {
					String sigla = (String) s;

					u = (AdUsuario) o;
					if (sigla.toUpperCase().equals(u.getSigla().toUpperCase())) {
						encontrado = true;
						break;
					}
				}

				if (!encontrado) {
					Resultado r = new Resultado();
					r.setNome(u.getNome());
					r.setSigla(u.getSigla());
					r.setCargo("");

					naoEncontradosNoBD.add(r);
				}
			}

		}
		exibirResultados();

	}

	private void exibirResultados() {
		System.out.println("\n\n*****RESULTADOS******");
		System.out
				.println("\n\n*****ENCONTRADOS NO CORPORATIVO E NO ACTIVE DIRECTORY******");
		for (Resultado enc : encontradosNoBD) {
			System.out.printf("%s\t%s\t%s\n", enc.getSigla(), enc.getNome(),
					enc.getCargo());
		}

		System.out
				.println("\n\n*****EXISTE NO ACTIVE DIRECTORY MAS Nï¿½O CORPORATIVO******");
		System.out
				.println("*****(POSSï¿½VEIS PRESTADORES DE SERVIï¿½O OU INEXISTENTES)******");
		for (Resultado nEnc : naoEncontradosNoBD) {
			System.out.printf("%s\t%s\n", nEnc.getSigla(), nEnc.getNome());
		}

		System.out.println("\n\n*****PROBLEMAS ENCONTRADOS******");
		for (String p : problemasEncontrados) {
			System.out.printf("%s\n", p);
		}

	}

	private void iniciarVerificacao(AdObjeto o, List<DpPessoa> listaPessoa) {

		// List<DpPessoa> listaPessoa = s.createQuery(
		// "from DpPessoa p where data_fim_pessoa is null and ID_ORGAO_USU = "
		// + conf.getIdLocalidade() + " AND sigla_pessoa like '"
		// + u.getSigla().toUpperCase() + "'").list();

		if (listaPessoa.size() == 0) {

		}
		/*
		 * else {
		 * 
		 * if (listaPessoa.size() == 1) { DpPessoa pessoa = listaPessoa.get(0);
		 * 
		 * Resultado r = new Resultado(); r.setNome(pessoa.getNomePessoa());
		 * r.setSigla(pessoa.getSigla()); if (pessoa.getCargo()!=null){
		 * r.setCargo(pessoa.getCargo().getNomeCargo()); }else{
		 * r.setCargo("INDEFINIDO"); }
		 * 
		 * 
		 * encontradosNoBD.add(r);
		 * 
		 * } else { problemasEncontrados.add("A sigla " + u.getSigla() +
		 * " possui mais de um usuï¿½rio!"); log.info("A sigla " + u.getSigla() +
		 * " possui mais de um usuï¿½rio!"); }
		 * 
		 * }
		 */
	}

	class Resultado {
		private String nome;
		private String sigla;
		private String cargo;

		private String getNome() {
			return nome;
		}

		private void setNome(String nome) {
			this.nome = nome;
		}

		private String getSigla() {
			return sigla;
		}

		private void setSigla(String sigla) {
			this.sigla = sigla;
		}

		private String getCargo() {
			return cargo;
		}

		private void setCargo(String cargo) {
			this.cargo = cargo;
		}

	}
}
