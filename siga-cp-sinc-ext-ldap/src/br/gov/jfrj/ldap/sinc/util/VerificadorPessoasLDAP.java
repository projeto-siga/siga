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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.ldap.AdObjeto;
import br.gov.jfrj.ldap.AdUsuario;
import br.gov.jfrj.ldap.sinc.LdapDaoSinc;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class VerificadorPessoasLDAP {

	private static LdapDaoSinc ldap;
	private static final SincProperties conf = SincProperties.getInstancia();

	private static Logger log = Logger.getLogger(PadronizadorLDAP.class
			.getName());
	private static Level nivelLog = Level.FINE;

	private List<Resultado> encontradosNoBD;
	private List<Resultado> naoEncontradosNoBD;
	private List<String> problemasEncontrados;

	public static void main(String[] args) throws Exception {
		new VerificadorPessoasLDAP().verificar();
	}

	public void verificar() throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(conf
				.getBdStringConexao(), conf.getBdUsuario(), conf.getBdSenha());
		HibernateUtil.configurarHibernate(cfg, "");

		encontradosNoBD = new ArrayList<Resultado>();
		naoEncontradosNoBD = new ArrayList<Resultado>();
		problemasEncontrados = new ArrayList<String>();
		ldap = LdapDaoSinc.getInstance();
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
				.println("\n\n*****EXISTE NO ACTIVE DIRECTORY MAS NÃO CORPORATIVO******");
		System.out
				.println("*****(POSSÍVEIS PRESTADORES DE SERVIÇO OU INEXISTENTES)******");
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
		 * " possui mais de um usuário!"); log.fine("A sigla " + u.getSigla() +
		 * " possui mais de um usuário!"); }
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
