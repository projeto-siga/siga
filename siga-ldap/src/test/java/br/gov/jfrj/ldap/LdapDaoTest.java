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
package br.gov.jfrj.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import junit.framework.TestCase;

import org.junit.Test;

import br.gov.jfrj.siga.base.AplicacaoException;

public class LdapDaoTest extends TestCase{

	private static final String PASSWORD2 = "Password2";
	private static final String PASSWORD1 = "Password1";
	private static final String DN_GESTAO_IDENTIDADE_USUARIOS = "OU=Usuarios,OU=Gestao de Identidade,DC=csis,DC=local";
	private static final String DN_GESTAO_IDENTIDADE = "OU=Gestao de Identidade,DC=csis,DC=local";
	private static final String CN_USUARIO = "CN=NOME_USUARIO";
	private static final String DN_USUARIO = CN_USUARIO + ","
			+ DN_GESTAO_IDENTIDADE_USUARIOS;
	private static final String SERVIDOR = "127.0.0.1";
	private static final String PORTA_COMUM = String
			.valueOf(ILdapDao.PORTA_COMUM);
	private static final String PORTA_SSL = String.valueOf(ILdapDao.PORTA_SSL);
	private static final String USUARIO_CONEXAO = "siga-gi@csis.local";
	private static final String USUARIO_CONEXAO_SENHA = PASSWORD1;
	private static final String KEYSTORE = "C:\\Desenvolvimento\\jdk1.6.0\\jre\\lib\\security\\cacerts";

	private static ILdapDao ldap = new LdapDaoImpl(false);

	@Test
	public void testVerificarConexao() {
		if (true)
			return;
		assertTrue(ldap.verificarConexao("siga-gi", "csis.local", PASSWORD1,
				SERVIDOR, PORTA_COMUM));
	}

	@Test
	public void testConexaoSemSSL() throws AplicacaoException {
		if (true)
			return;
		ldap.conectarSemSSL(SERVIDOR, PORTA_COMUM, USUARIO_CONEXAO,
				USUARIO_CONEXAO_SENHA);
	}

	@Test
	public void testConexaoComSSL() throws AplicacaoException {
		if (true)
			return;
		ldap.conectarComSSL(SERVIDOR, PORTA_SSL, USUARIO_CONEXAO,
				USUARIO_CONEXAO_SENHA, KEYSTORE);
	}

	@Test
	public void testIncluirUsuario() throws AplicacaoException, NamingException {
		if (true)
			return;
		ldap.criarUsuario("SIGLA_USUARIO", "NOME_USUARIO",
				DN_GESTAO_IDENTIDADE_USUARIOS);

		assertEquals(ldap.pesquisar(DN_USUARIO).get("sAMAccountName").get()
				.toString(), "SIGLA_USUARIO");
	}

	@Test
	public void testObjetoExiste() {
		if (true)
			return;
		ldap.existe(DN_USUARIO);
	}

	@Test
	public void testPesquisar() throws AplicacaoException, NamingException {
		if (true)
			return;
		Attributes atributos = ldap
				.pesquisar("CN=NOME_USUARIO,OU=Usuarios,OU=Gestao de Identidade,DC=csis,DC=local");
		assertEquals(atributos.get("sAMAccountName").get().toString(),
				"SIGLA_USUARIO");
	}

	@Test
	public void testAlterarUsuario() throws AplicacaoException, NamingException {
		if (true)
			return;
		Attributes atributos = new BasicAttributes(true);
		atributos.put("objectClass", "user");
		atributos.put("samAccountName", "SIGLA_USUARIO");
		atributos.put("cn", "NOME_USUARIO");
		atributos.put("distinguishedName", DN_USUARIO);
		atributos.put("scriptPath", "kix33.exe");

		ldap.alterar(DN_USUARIO, atributos);

		assertEquals(ldap.pesquisar(DN_USUARIO).get("scriptPath").get()
				.toString(), "kix33.exe");

	}

	@Test
	public void testDesativarUsuario() throws AplicacaoException {
		if (true)
			return;
		ldap.desativarUsuario(DN_USUARIO);
	}

	@Test
	public void testAtivarUsuario() throws AplicacaoException {
		if (true)
			return;
		ldap.ativarUsuario(DN_USUARIO);
	}

	@Test
	public void testDefinirSenha() throws AplicacaoException {
		if (true)
			return;
		ldap.definirSenha(DN_USUARIO, PASSWORD2);
		String siglaUsuario = dnToSamAccountName(DN_USUARIO);
		assertTrue(ldap.verificarConexao(siglaUsuario, "csis.local",
				PASSWORD2, SERVIDOR, PORTA_COMUM));
		ldap.definirSenha(DN_USUARIO, PASSWORD1);
		assertTrue(ldap.verificarConexao(siglaUsuario, "csis.local",
				PASSWORD1, SERVIDOR, PORTA_COMUM));
	}

	@Test
	public void testAlterarSenha() throws AplicacaoException {
		if (true)
			return;
		ldap.alterarSenha(DN_USUARIO, PASSWORD1, PASSWORD2);
		String siglaUsuario = dnToSamAccountName(DN_USUARIO);
		assertTrue(ldap.verificarConexao(siglaUsuario, "csis.local",
				PASSWORD2, SERVIDOR, PORTA_COMUM));
		ldap.alterarSenha(DN_USUARIO, PASSWORD2, PASSWORD1);
		assertTrue(ldap.verificarConexao(siglaUsuario, "csis.local",
				PASSWORD1, SERVIDOR, PORTA_COMUM));
		// A alteração de senha seguinte deveria falhar. Porém o ldap armazena a
		// senha em cache e ainda
		// não foi descoberta uma maneira de desativar o cache
		ldap.alterarSenha(DN_USUARIO, PASSWORD2, PASSWORD1);
	}

	@Test
	public void testTiposObjetos() throws AplicacaoException {
		if (true)
			return;
		String dnGrupoSeg = "CN=sesie_gs,OU=Grupos de Seguranca,OU=Gestao de Identidade,DC=csis,DC=local";
		String dnGrupoDistr = "CN=tssesie_gd,OU=Grupos de Distribuicao,OU=Gestao de Identidade,DC=csis,DC=local";
		String dnUsuario = DN_USUARIO;

		assertTrue(ldap.isUsuario(dnUsuario));
		assertFalse(ldap.isGrupo(dnUsuario));

		assertTrue(ldap.isGrupo(dnGrupoSeg));
		assertTrue(ldap.isGrupoSeguranca(dnGrupoSeg));
		assertTrue(ldap.isGrupoGlobal(dnGrupoSeg));

		assertFalse(ldap.isGrupoDomainLocal(dnGrupoSeg));
		assertFalse(ldap.isGrupoSistema(dnGrupoSeg));
		assertFalse(ldap.isGrupoUniversal(dnGrupoSeg));

		assertTrue(ldap.isGrupo(dnGrupoDistr));
		assertTrue(ldap.isGrupoDistribuicao(dnGrupoDistr));
		assertTrue(ldap.isGrupoGlobal(dnGrupoDistr));

		assertFalse(ldap.isGrupoDomainLocal(dnGrupoDistr));
		assertFalse(ldap.isGrupoSistema(dnGrupoDistr));
		assertFalse(ldap.isGrupoUniversal(dnGrupoDistr));

	}

	@Test
	public void testMover() throws AplicacaoException {
		if (true)
			return;
		ldap.mover(DN_USUARIO, CN_USUARIO + "," + DN_GESTAO_IDENTIDADE);
		assertTrue(CN_USUARIO != null);

		ldap.mover(CN_USUARIO + "," + DN_GESTAO_IDENTIDADE, DN_USUARIO);
		assertTrue(ldap.pesquisar(DN_USUARIO) != null);

	}

	@Test
	public void testExcluirUsuario() throws AplicacaoException {
		if (true)
			return;
		ldap.excluir(DN_USUARIO);
	}

	@Test
	public void testCriarUsuario() throws AplicacaoException {
		if (true)
			return;
		String dn = "CN=" + "usuario1," + DN_GESTAO_IDENTIDADE;

		ldap.criarUsuario("usuario1_login", "usuario1", DN_GESTAO_IDENTIDADE);
		assertNotNull(ldap.pesquisar(dn));
		ldap.excluir(dn);
	}

	@Test
	public void testCriarContato() throws AplicacaoException {
		if (true)
			return;
		String dn = "CN=" + "contato1," + DN_GESTAO_IDENTIDADE;
		ldap.criarContato("contato1", DN_GESTAO_IDENTIDADE);
		assertNotNull(ldap.pesquisar(dn));
		ldap.excluir(dn);
	}

	@Test
	public void testCriarGrupoDistribuicao() throws AplicacaoException {
		if (true)
			return;
		String dn = "CN=" + "gd1," + DN_GESTAO_IDENTIDADE;
		ldap.criarGrupoDistribuicao("gd1", DN_GESTAO_IDENTIDADE);
		assertNotNull(ldap.pesquisar(dn));
		ldap.excluir(dn);
	}

	@Test
	public void testCriarGrupoSeguranca() throws AplicacaoException {
		if (true)
			return;
		String dn = "CN=" + "gs1," + DN_GESTAO_IDENTIDADE;
		ldap.criarGrupoSeguranca("gs1", DN_GESTAO_IDENTIDADE);
		assertNotNull(ldap.pesquisar(dn));
		ldap.excluir(dn);
	}

	@Test
	public void testCriarUnidadeOrganizacional() throws AplicacaoException {
		if (true)
			return;
		String dn = "OU=" + "ou1," + DN_GESTAO_IDENTIDADE;
		ldap.criarUnidadeOrganizacional("ou1", DN_GESTAO_IDENTIDADE);
		assertNotNull(ldap.pesquisar(dn));
		ldap.excluir(dn);
	}

	private String dnToSamAccountName(String dnUsuario)
			throws AplicacaoException {
		Attributes attrs = ldap.pesquisar(dnUsuario);

		String siglaUsuario = null;
		try {
			siglaUsuario = attrs.get("samaccountname").get().toString();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return siglaUsuario;
	}

}
