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
package br.gov.jfrj.siga.gi.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService(targetNamespace = "http://impl.service.gi.siga.jfrj.gov.br/")
public interface GiService extends Remote {

	public static final String _MODO_AUTENTICACAO_BANCO = "banco";
	public static final String _MODO_AUTENTICACAO_LDAP = "ldap";
	// caso nao exista a propriedade no JBOSS, autenticar via banco
	public static final String _MODO_AUTENTICACAO_DEFAULT = _MODO_AUTENTICACAO_BANCO;
	
    @WebMethod
	public String login(String matricula, String senha);

    @WebMethod
	public String dadosUsuario(String matricula);

    @WebMethod
	public String acessos(String matricula, String lotacao);

    @WebMethod
	public String acesso(String matricula, String lotacao, String servico);
    
    /**
     * WebMethod perfilAcessoPorCpf      
     * @param cpf
     * @param token
     * @return
     */
    @WebMethod
	public String perfilAcessoPorCpf(String cpf, String token);
    
    @WebMethod
	public String esqueciSenha(String cpf, String email);
    
    @WebMethod
    public String criarUsuario(String cadastranteStr, String orgaoUsu, String lotacao, String cargo, String funcao, String nmPessoa, String dtNascimento,String cpf, String email);
    
    @WebMethod
    public String buscarModoAutenticacao(String login);
	
    @WebMethod
	public String inativarUsuario(final Long idUsuario);

    /**
     * WebMethod que gera Token
     * @param matricula
     * @param senha
     * @return String token
     */
    @WebMethod
	public String gerarToken(String matricula, String senha) throws Exception;
        
}