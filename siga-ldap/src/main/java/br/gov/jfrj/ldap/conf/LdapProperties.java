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
package br.gov.jfrj.ldap.conf;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Criptografia;
import br.gov.jfrj.siga.base.Prop;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LdapProperties {

	private static final String CHAVE_CRIPTO = "MJywdb7udby7&4IKYGD5tg327";
	
	public String getDnUsuarios()  {
		try {
			return Prop.get("/siga.ldap.dn.usuarios");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a propriedade dnUsuarios", 9, e);
		}
	}

	public String getServidorLdap()  {
		try {
			return Prop.get("/siga.ldap.servidor");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o servidor LDAP", 9, e);
		}
	}

	public String getPortaLdap()  {
		try {
			return Prop.get("/siga.ldap.porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta LDAP", 9, e);
		}
	}

	public String getPortaSSLLdap()  {
		try {
			return Prop.get("/siga.ldap.ssl.porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta SSL LDAP", 9, e);
		}
	}

	public String getUsuarioLdap()  {
		try {
			return Prop.get("/siga.ldap.usuario");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o usuário LDAP", 9, e);
		}
	}

	public String getSenhaLdap()  {
		try {
			String senhaCriptografada = Prop.get("/siga.ldap.senha");
			if(senhaCriptografada != null){
				senhaCriptografada= senhaCriptografada.trim();
				return descriptografarSenha(senhaCriptografada);
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a senha LDAP", 9, e);
		}
	}

	protected String descriptografarSenha(String senhaCriptografada)
			 {
		BASE64Encoder enc = new BASE64Encoder();
		BASE64Decoder dec = new BASE64Decoder();
		try {
			return new String(Criptografia.desCriptografar(dec
					.decodeBuffer(senhaCriptografada), enc.encode(CHAVE_CRIPTO
					.getBytes())));
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao descriptografar a senha LDAP", 9, e);
		} 
	}

	public String getKeyStore()  {
		try {
			return Prop.get("/siga.ldap.keystore");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o keystore", 9, e);
		}
	}
	
	public Boolean isModoEscrita() {
		try {
			return Prop.getBool("/siga.ldap.modo.escrita");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a propriedade modo_escrita", 9, e);
		}
	}

}
