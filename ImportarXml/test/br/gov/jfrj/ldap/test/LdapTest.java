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
package br.gov.jfrj.ldap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import br.gov.jfrj.importar.AdContato;
import br.gov.jfrj.importar.AdGrupoDeDistribuicao;
import br.gov.jfrj.importar.AdObjeto;
import br.gov.jfrj.importar.AdUnidadeOrganizacional;
import br.gov.jfrj.ldap.sinc.LdapDaoSinc;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.ldap.util.LdapUtils;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.TipoConfiguracaoGrupoEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class LdapTest {

	private static LdapDaoSinc ldap;
	private static SincProperties conf;

	public LdapTest() {
		conf = SincProperties.getInstancia("sjrj.desenv");
		ldap = LdapDaoSinc.getInstance(conf);
	}

	@Test
	public void testObjetoExiste() {
		fail("Not yet implemented");
	}

	@Test
	public void testPesquisarObjeto() {
		fail("Not yet implemented");
	}

	@Test
	public void testAlterar() {
		fail("Not yet implemented");
	}

	@Test
	public void testExcluir() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncluir() {
		fail("Not yet implemented");
	}

	@Test
	public void incluirContato() throws NamingException, AplicacaoException {

		AdContato novoContato = new AdContato("Markenson",
				"markenson@jfrj.jus.br", conf.getDnDominio());
		AdUnidadeOrganizacional gi = new AdUnidadeOrganizacional(
				"Gestao de Identidade", "Gestao de Identidade", conf
						.getDnDominio());
		AdUnidadeOrganizacional uoContatos = (AdUnidadeOrganizacional) ldap
				.pesquisarObjeto(conf.getDnContatos()).get(0);
		uoContatos.setGrupoPai(gi);
		novoContato.setGrupoPai(uoContatos);

		ldap.incluirAD(novoContato);

		AdContato c = (AdContato) (ldap.pesquisarObjeto("CN=Markenson,"
				+ conf.getDnContatos()).get(0));
		assertTrue(c != null);

	}

	@Test
	public void alterarContato() throws NamingException, AplicacaoException {
		AdContato contatoAntigo = (AdContato) (ldap
				.pesquisarObjeto("CN=Markenson," + conf.getDnContatos()).get(0));
		AdUnidadeOrganizacional gi = new AdUnidadeOrganizacional(
				"Gestao de Identidade", "Gestao de Identidade", conf
						.getDnDominio());
		AdUnidadeOrganizacional uoContatos = (AdUnidadeOrganizacional) ldap
				.pesquisarObjeto(conf.getDnContatos()).get(0);
		uoContatos.setGrupoPai(gi);
		contatoAntigo.setGrupoPai(uoContatos);

		AdContato contatoNovo = contatoAntigo;
		contatoNovo.setIdExterna("markensonfranca@gmail.com");
		ldap.alterarAD(contatoAntigo, contatoNovo);

		AdContato c = (AdContato) (ldap.pesquisarObjeto("CN=Markenson,"
				+ conf.getDnContatos()).get(0));
		assertTrue(c.getIdExterna().equals("markensonfranca@gmail.com"));
	}

	@Test
	public void excluirContato() throws NamingException, AplicacaoException {
		AdContato c = (AdContato) (ldap.pesquisarObjeto("CN=Markenson,"
				+ conf.getDnContatos()).get(0));
		ldap.excluirAD(c);
		assertTrue(ldap.pesquisarObjeto("CN=Markenson," + conf.getDnContatos())
				.size() == 0);
	}

	@Test
	public void testEscapeDN() {
		assertEquals("No special characters to escape", "Helloé", LdapUtils
				.escapeDN("Helloé"));
		assertEquals("leading #", "\\# Helloé", LdapUtils.escapeDN("# Helloé"));
		assertEquals("leading space", "\\ Helloé", LdapUtils
				.escapeDN(" Helloé"));
		assertEquals("trailing space", "Helloé\\ ", LdapUtils
				.escapeDN("Helloé "));
		assertEquals("only 3 spaces", "\\  \\ ", LdapUtils.escapeDN("   "));
		assertEquals("Christmas Tree DN",
				"\\ Hello\\\\ \\+ \\, \\\"World\\\" \\;\\ ", LdapUtils
						.escapeDN(" Hello\\ + , \"World\" ; "));
	}

	@Test
	public void testEscapeLDAPSearchFilter() {
		assertEquals("No special characters to escape",
				"Hi This is a test #çà", LdapUtils
						.escapeLDAPSearchFilter("Hi This is a test #çà"));
		assertEquals(
				"LDAP Christams Tree",
				"Hi \\28This\\29 = is \\2a a \\5c test # ç à ô",
				LdapUtils
						.escapeLDAPSearchFilter("Hi (This) = is * a \\ test # ç à ô"));
	}

	@Test
	public void testMoverObjeto() throws NamingException, AplicacaoException {
		ldap.moverObjeto(conf.getDnUsuarios(), "OU=Usuarios,"
				+ conf.getDnGruposSeguranca());

		assertTrue(ldap.objetoExiste("OU=Usuarios,"
				+ conf.getDnGruposSeguranca()));
		assertTrue(!ldap.objetoExiste(conf.getDnUsuarios()));

		ldap.moverObjeto("OU=Usuarios," + conf.getDnGruposSeguranca(), conf
				.getDnUsuarios());

		assertTrue(ldap.objetoExiste(conf.getDnUsuarios()));
		assertTrue(!ldap.objetoExiste("OU=Usuarios,"
				+ conf.getDnGruposSeguranca()));
	}

	/**
	 * Testa a troca de senha no AD. Esse teste não tem ASSERT. Se não der
	 * exceção significa que funcionou.
	 * 
	 * @throws NamingException
	 *             - 1) Verifique se a complexidade da senha é compatível com a
	 *             do servidor 2) Verifique se a senha antiga está correta.
	 * @throws IOException
	 * @throws AplicacaoException 
	 */
	@Test
	public void testAlterarSenha() throws NamingException, IOException, AplicacaoException {
		String dnUsuario = "CN=RJ13286," + conf.getDnUsuarios();
		String senhaAntiga = "12345";
		String senhaNova = "";

//		ldap.alterarSenha(dnUsuario, senhaAntiga, senhaNova);

		ldap.pesquisarObjeto(dnUsuario);

	}

	/**
	 * Testa a definição de senha no AD, sem a necessidade de senha antiga. Esse
	 * teste não tem ASSERT. Se não der exceção significa que funcionou. 1)
	 * Verifique se a complexidade da senha é compatível com a do servidor
	 * 
	 * @throws NamingException
	 * @throws IOException
	 * @throws AplicacaoException 
	 */
	@Test
	public void testDefinirSenha() throws NamingException, IOException, AplicacaoException {
		String dnUsuario = "CN=RJ13286," + conf.getDnUsuarios();
		String senhaNova = "Password2";

//		ldap.definirSenhaAD(dnUsuario, senhaNova);

		ldap.pesquisarObjeto(dnUsuario);
	}

	@Test
	public void testIncluirGruposEmail() throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(
				"jdbc:oracle:thin:@mclaren:1521:mcl", "corporativo",
				"corporativo");
		CpDao.configurarHibernateParaDebug(cfg);
		HibernateUtil.configurarHibernate(cfg, "");
		CpDao cpDao = CpDao.getInstance();

		AdUnidadeOrganizacional gi = new AdUnidadeOrganizacional(
				"Gestao de Identidade", "Gestao de Identidade", conf
						.getDnDominio());
		AdUnidadeOrganizacional distr = new AdUnidadeOrganizacional(
				"Grupos de Distribuicao", "Grupos de Distribuicao", gi, conf
						.getDnDominio());

		CpGrupoDeEmail cpGrpEmail = new CpGrupoDeEmail();
		cpGrpEmail.setHisAtivo(1);
		List<CpGrupoDeEmail> gruposEmail = cpDao.consultar(cpGrpEmail, null);

		for (CpGrupoDeEmail gEmail : gruposEmail) {
			List<ConfiguracaoGrupo> listaCfgGrupo = Cp.getInstance().getConf()
					.obterCfgGrupo(gEmail);
			AdGrupoDeDistribuicao adGrupoEmail = new AdGrupoDeDistribuicao(
					gEmail.getSigla(), gEmail.getSigla(), conf.getDnDominio());
			AdObjeto adMembro = null;
			for (ConfiguracaoGrupo cfgGrupo : listaCfgGrupo) {

				if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.CARGO)) {
					List<DpPessoa> listaPessoas = cpDao
							.consultarPessoasComCargo(Long.valueOf(cfgGrupo
									.getConteudoConfiguracao()));

					for (DpPessoa p : listaPessoas) {
						List<AdObjeto> listaAD = ldap.pesquisarObjeto("CN="
								+ conf.getPrefixoMatricula() + p.getMatricula()
								+ "," + conf.getDnUsuarios());
						if (listaAD.size() == 1) {
							adMembro = listaAD.get(0);
						}

						if (adMembro != null) {
							adGrupoEmail.acrescentarMembro(adMembro);
							adMembro = null;
						}
					}
					adMembro = null;
				}
				if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.EMAIL)) {

				}
				if (cfgGrupo.getTipo()
						.equals(TipoConfiguracaoGrupoEnum.FORMULA)) {

				}
				if (cfgGrupo.getTipo().equals(
						TipoConfiguracaoGrupoEnum.FUNCAOCONFIANCA)) {
					List<DpPessoa> listaPessoas = cpDao
							.consultarPessoasComFuncaoConfianca(Long
									.valueOf(cfgGrupo.getConteudoConfiguracao()));

					for (DpPessoa p : listaPessoas) {
						List<AdObjeto> listaAD = ldap.pesquisarObjeto("CN="
								+ conf.getPrefixoMatricula() + p.getMatricula()
								+ "," + conf.getDnUsuarios());
						if (listaAD.size() == 1) {
							adMembro = listaAD.get(0);
						}

						if (adMembro != null) {
							adGrupoEmail.acrescentarMembro(adMembro);
							adMembro = null;
						}
					}
					adMembro = null;
				}
				if (cfgGrupo.getTipo()
						.equals(TipoConfiguracaoGrupoEnum.LOTACAO)) {
					DpLotacao l = cpDao
							.consultar(Long.valueOf(cfgGrupo
									.getConteudoConfiguracao()),
									DpLotacao.class, false);
					List<AdObjeto> listaAD = ldap.pesquisarObjeto("CN="
							+ conf.getPfxGrpDistrAuto() + l.getSigla()
							+ conf.getSfxGrpDistrAuto() + ","
							+ conf.getDnGruposDistribuicao());

					if (listaAD.size() == 1) {
						adMembro = listaAD.get(0);
					}

				}
				if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.PESSOA)) {
					DpPessoa p = cpDao.consultar(Long.valueOf(cfgGrupo
							.getConteudoConfiguracao()), DpPessoa.class, false);
					List<AdObjeto> listaAD = ldap.pesquisarObjeto("CN="
							+ conf.getPrefixoMatricula() + p.getMatricula()
							+ "," + conf.getDnUsuarios());

					if (listaAD.size() == 1) {
						adMembro = listaAD.get(0);
					}
				}

				if (adMembro != null) {
					adGrupoEmail.acrescentarMembro(adMembro);
					adMembro = null;
				}

			}
		}
	}

	@Test
	public void testConectarLdap() {
//		assertTrue(LdapDaoSinc.verificarConexao("siga-gi", "Password1"));
	}

}
