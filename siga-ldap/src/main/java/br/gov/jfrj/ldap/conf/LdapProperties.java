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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Criptografia;
import br.gov.jfrj.siga.model.prop.ModeloPropriedade;

public class LdapProperties extends ModeloPropriedade {

	private static final String CHAVE_CRIPTO = "MJywdb7udby7&4IKYGD5tg327";
	
	@Override
	public String getPrefixoModulo() {
		return "siga.ldap";
	}

	public String getDnUsuarios()  {
		try {
			return this.obterPropriedade("dnUsuarios");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a propriedade dnUsuarios", 9, e);
		}
	}

	public String getServidorLdap()  {
		try {
			return this.obterPropriedade("servidor");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o servidor LDAP", 9, e);
		}
	}

	public String getPortaLdap()  {
		try {
			return this.obterPropriedade("porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta LDAP", 9, e);
		}
	}

	public String getPortaSSLLdap()  {
		try {
			return this.obterPropriedade("ssl.porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta SSL LDAP", 9, e);
		}
	}

	public String getUsuarioLdap()  {
		try {
			return this.obterPropriedade("usuario");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o usuário LDAP", 9, e);
		}
	}

	public String getSenhaLdap()  {
		try {
			String senhaCriptografada = this.obterPropriedade(
					"senha").trim();
			return descriptografarSenha(senhaCriptografada);
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
			return this.obterPropriedade("keystore");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o keystore", 9, e);
		}
	}
	
	public Boolean isModoEscrita() {
		try {
			return Boolean.valueOf(this.obterPropriedade("modo_escrita")
					.trim());
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a propriedade modo_escrita", 9, e);
		}
	}

}
