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
package br.gov.jfrj.siga.cd.auth.certs;

import java.security.KeyStore;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.certs.X509CertificateVerifier;
import org.jboss.security.auth.spi.BaseCertLoginModule;

import br.gov.jfrj.siga.cd.CertificadoUtil;
import br.gov.jfrj.siga.cd.X509ChainValidator;
import br.gov.jfrj.siga.cd.ac.FachadaDeCertificadosAC;

/**
 * Classe responsável por administrar o LOGIN por certificado digital e
 * verificar se o mesmo é valido. Esta classe deve ser sempre referenciada no
 * arquivo login-config.xml quando se deseja utilizar o CPF do certificado para
 * fazer a autenticação e obter os papéis (roles) do usuário a partir de uma
 * QUERY SQL.
 * 
 * Esta classe deve constar do login-module Ex: <login-module
 * code="br.gov.jfrj.siga.cd.auth.certs.LoginPorCpfDoCertificadoICPBrasil"
 * flag="required">
 * 
 * @author aym
 * 
 */
public class LoginPorCpfDoCertificadoICPBrasil extends BaseCertLoginModule
		implements X509CertificateVerifier {
	// variáveis correspondentes aos module-option do login-module
	private String cpfToLoginQuery; // Sql para converter cpf em userLogin
	private String rolesQuery; // Quary para se obter os papéis a partir do
	// userLogin
	private String dsJndiName; // JNDI do Banco de dados
	// variáveis calculadas
	private Object identity; // identidade, no caso o DN (ou o CN dependendo da
	// configuração)
	private String userLogin = null; // Login do usuário no banco de dados
	private Group[] roleSets = null; // conjunto de roles (papéis de segurança

	// JAAS)

	/**
	 * inicializa as variáveis dos module-option
	 */
	@SuppressWarnings("unchecked")
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		log.debug("Inicializando LoginPorCpfDoCertificadoICPBrasil...");
		try {
			super.initialize(subject, callbackHandler, sharedState, options);
			cpfToLoginQuery = (String) options.get("cpfToLoginQuery");
			rolesQuery = (String) options.get("rolesQuery");
			dsJndiName = (String) options.get("dsJndiName");
		} catch (Throwable e) {
			log
					.error(
							"Erro na inicialização de LoginPorCpfDoCertificadoICPBrasil: ",
							e);
		}
		log.debug("LoginPorCpfDoCertificadoICPBrasil inicializado.");
	}

	@Override
	protected Principal getIdentity() {
		return (Principal) identity;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Executa as tarefas referentes ao LOGIN 
	 */
	public boolean login() throws LoginException {
		
		if (super.login() == true) {
			Object username = sharedState.get("javax.security.auth.login.name");
			if (username instanceof Principal) {
				identity = (Principal) username;
			} else {
				String name = username.toString();
				try {
					identity = createIdentity(name);
				} catch (Exception e) {
					log.debug("Falha ao criar principal", e);
					throw new LoginException("Falha ao criar principal: "
							+ e.getMessage());
				}
				
			}

			if (identity == null) {
				try {
					identity = createIdentity((String) username);
				} catch (Exception e) {
					log.debug("Falha ao criar o principal", e);
					throw new LoginException("Falha ao criar o principal: "
							+ e.getMessage());
				}
			}
		}
		String username = getUsername();
		if (getUseFirstPass() == true) {
			sharedState.put("javax.security.auth.login.name", username);
		}
		try {
			userLogin = obterUserLoginDB();
		} catch (Exception e) {
			log.debug("Falha ao obter o userLogin", e);
			throw new LoginException("Falha ao obter o userLogin: "
					+ e.getMessage());
		}
		try {
			roleSets = obterRoleSetsDB();
		} catch (Exception e) {
			log.debug("Falha ao obter os roles.", e);
			throw new LoginException("Falha ao obter os roles: "
					+ e.getMessage());
		}
		
		super.loginOk = true;
		super.log.trace("Usuario '" + identity + "' authenticado, loginOk="
				+ loginOk);
		return true;
	}

	public boolean verify(X509Certificate cert, String alias,
			KeyStore keystore, KeyStore truststore) {
		return validateCredential(cert, alias, keystore, truststore);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Valida o certificado 
	 */
	public boolean validateCredential(X509Certificate cert, String alias,
			KeyStore keystore, KeyStore truststore) {

		Collection<X509Certificate> certs = new ArrayList<X509Certificate>();
		certs.add(cert);
		try {
			X509Certificate[] cadeiaTotal = FachadaDeCertificadosAC
					.montarCadeiaOrdenadaECompleta(certs);
			final X509ChainValidator cadeia = new X509ChainValidator(
					cadeiaTotal, /* trustedAnchors */new HashSet(
							FachadaDeCertificadosAC.getTrustAnchors()), null);
			cadeia.checkCRL(false);
			cadeia.validateChain(new Date());

		} catch (Exception e) {
			super.log.trace("Usuario " + cert.getSubjectDN().getName() + " com credencial inválida:"
					+ e.getMessage());
			
			return false;
		}
		return true;
	}

	/**
	 * obtém o CPF a partir do certificado
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getCpf() throws Exception {
		X509Certificate cert = (X509Certificate) getAliasAndCert()[1];
		return CertificadoUtil.recuperarCPF(cert);
	}

	private String getUserLogin() {
		return userLogin;
	}

	/**
	 * Obter o login do usuário no banco de dados a partir do CPF
	 * 
	 * @return
	 */
	protected String obterUserLoginDB() {
		String cpf = null;
		try {
			cpf = getCpf();
		} catch (Exception e1) {
			log.error("Falha ao obter o CPF do certificado.", e1);
		}
		Connection con = null;
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup(dsJndiName);
			con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement(cpfToLoginQuery);
			ps.setString(1, cpf);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String userLogin = rs.getString(1);
				return userLogin;
			} else {
				return null;
			}
		} catch (SQLException e) {
			log.error("Falha na Query de obtenção do Login.", e);
			return null;
		} catch (NamingException e) {
			log.error("Erro inesperado na obtenção do Login.", e);
			return null;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Throwable e) {
				log.error("Erro ao fechar conexão", e);
			}
		}
	}

	@Override
	protected Group[] getRoleSets() {
		return roleSets;
	}

	/**
	 * Obter o array de papéis do usuário no banco de dados a partir do LOGIN do
	 * usuário
	 * 
	 * @return
	 * @throws LoginException
	 */
	protected Group[] obterRoleSetsDB() throws LoginException {
		String userLogin = getUserLogin();
		Group[] groups;
		Connection con = null;
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup(dsJndiName);
			con = ds.getConnection();
			PreparedStatement ps = con.prepareStatement(rolesQuery);
			ps.setString(1, userLogin);
			ResultSet rs = ps.executeQuery();
			// carrega os grupos encontrados
			ArrayList<Group> grpList = new ArrayList<Group>();
			int conta = 0;
			while (rs.next()) {
				String rolename = rs.getString(1);
				Group grupo = new SimpleGroup(rolename);
				// grupo.addMember(principal);
				grpList.add(grupo);
				conta++;
			}
			;
			// transforma em Array
			groups = new Group[conta];
			int contaArr = 0;
			for (Group grupo : grpList) {
				groups[contaArr] = grupo;
				contaArr++;
			}
		} catch (SQLException e) {
			log.error("Falha na Query para obter os Roles.", e);
			return null;
		} catch (NamingException e) {
			log.error("Erro inesperado na obtenção dos Roles.", e);
			return null;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Throwable e) {
				log.error("Erro ao fechar conexão.", e);
			}
		}
		return groups;
	}
	@Override
	public boolean logout() throws LoginException {
		if (!super.logout()) return false; //identity utilizado no super.logout()
		identity = null;
		userLogin = null; 
		roleSets = null;
		return true;
	}
	
}
