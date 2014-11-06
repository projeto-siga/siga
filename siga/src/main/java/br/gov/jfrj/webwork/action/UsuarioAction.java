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

/*
 * Criado em  05/01/2006
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.webwork.action;

import java.util.List;

import org.apache.log4j.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class UsuarioAction extends SigaActionSupport {
	
	private static final Logger log = Logger.getLogger( UsuarioAction.class );

	private String cpf;

	private String matricula;

	private String nomeUsuario;

	private String senhaAtual;

	private String senhaConfirma;

	private String senhaNova;

	private String titulo;

	private String tit;
	
	private Integer metodo;
	private String auxiliar1;
	private String cpf1;
	private String senha1;
	
	private String auxiliar2;
	private String cpf2;
	private String senha2;
	
	private Long idOrgao;
	
	private String trocarSenhaRede;
	
	private String ajaxMsgErro;
	
	
	public String getCpf1() {
		return cpf1;
	}

	public void setCpf1(String cpf1) {
		this.cpf1 = cpf1;
	}

	public String getCpf2() {
		return cpf2;
	}

	public void setCpf2(String cpf2) {
		this.cpf2 = cpf2;
	}

	public String getTit() {
		return tit;
	}

	public void setTit(String tit) {
		this.tit = tit;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCpf() {
		return cpf;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @return Retorna o atributo senhaAtual.
	 */
	public String getSenhaAtual() {
		return senhaAtual;
	}

	/**
	 * @return Retorna o atributo senhaConfirma.
	 */
	public String getSenhaConfirma() {
		return senhaConfirma;
	}

	/**
	 * @return Retorna o atributo senhaNova.
	 */
	public String getSenhaNova() {
		return senhaNova;
	}

	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	public void setMatricula(final String matricula) {
		this.matricula = matricula;
	}

	public void setNomeUsuario(final String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	/**
	 * @param senhaAtual
	 *            Atribui a senhaAtual o valor.
	 */
	public void setSenhaAtual(final String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	/**
	 * @param senhaConfirma
	 *            Atribui a senhaConfirma o valor.
	 */
	public void setSenhaConfirma(final String senhaConfirma) {
		this.senhaConfirma = senhaConfirma;
	}

	/**
	 * @param senhaNova
	 *            Atribui a senhaNova o valor.
	 */
	public void setSenhaNova(final String senhaNova) {
		this.senhaNova = senhaNova;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.
	 * action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	// @Override
	// public ActionErrors validate(final ActionMapping mapping,
	// final HttpServletRequest request) {
	// ActionErrors erros = super.validate(mapping, request);
	// final boolean validar = page == 1;
	// if (erros == null)
	// erros = new ActionErrors();
	// if (validar && !senhaNova.equals(senhaConfirma)) {
	// erros.add("senha", new ActionMessage("errors.senha.confirma",
	// Mensagens.getString("trocarSenha.senhaNova"), Mensagens
	// .getString("trocarSenha.senhaConfirma")));
	// }
	// return erros;
	// }
	public String aEsqueciSenha() throws Exception {
		return Action.SUCCESS;
	}

	public String aEsqueciSenhaGravar() throws Exception {
		String msgAD = "";
		getRequest().setAttribute("volta", "esqueci");
		getRequest().setAttribute("titulo", "Esqueci Minha Senha");
		boolean senhaTrocadaAD = false;
		switch (metodo) {
		case 1:
			
//			verificarMetodoIntegracaoAD(matricula);
			
			String[] senhaGerada = new String[1];
			CpIdentidade idNovaAlterada = Cp.getInstance().getBL().alterarSenhaDeIdentidade(
					matricula, cpf, getIdentidadeCadastrante(),senhaGerada);
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(auxiliar1,cpf1,senha1,auxiliar2,cpf2,senha2,matricula,cpf,senhaNova)){
				setMensagem(
						"Não foi possível alterar a senha!<br/>" +
						"1) As pessoas informadas não podem ser as mesmas;<br/>" +
						"2) Verifique se as matrículas e senhas foram informadas corretamente;<br/>" +
						"3) Verifique se as pessoas são da mesma lotação ou da lotação imediatamente superior em relação à matrícula que terá a senha alterada;<br/>");
				return Action.SUCCESS;
			}else{
				CpIdentidade idAux1 = dao().consultaIdentidadeCadastrante(auxiliar1, true);
				CpIdentidade idNovaDefinida = Cp.getInstance().getBL().definirSenhaDeIdentidade(senhaNova, senhaConfirma, matricula, auxiliar1, auxiliar2, idAux1);
//				senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNovaDefinida,senhaNova);
			}
			break;
		default:
			setMensagem("Método inválido!");
			return Action.SUCCESS;
		}

		if (isIntegradoAD(matricula) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail também foi alterada.";
		}
		
		if (isIntegradoAD(matricula) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATENÇÃO: A senha de rede e e-mail NÃO foi alterada embora o seu órgão esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		setMensagem("A Senha foi alterada com sucesso e foi enviada para seu email" + msgAD);

		return Action.SUCCESS;
	}

	public String aIncluirUsuario() throws Exception {
		return Action.SUCCESS;
	}

	public String aIncluirUsuarioGravar() throws Exception {
		String msgComplemento = "";
		getRequest().setAttribute("volta", "incluir");
		getRequest().setAttribute("titulo", "Novo Usuário");
		String[] senhaGerada = new String[1];
		boolean senhaTrocadaAD = false;
		boolean isIntegradoAoAD = isIntegradoAD(matricula);
		CpIdentidade idNova = null;
		switch (metodo) {
		case 1:
			
			idNova = Cp.getInstance().getBL().criarIdentidade(matricula,
					cpf, getIdentidadeCadastrante(),senhaNova,senhaGerada,isIntegradoAoAD);
			if (isIntegradoAoAD){
				senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova,senhaNova);
			}
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(auxiliar1,cpf1,senha1,auxiliar2,cpf2,senha2,matricula,cpf,senhaNova)){
				setMensagem(
						"Não foi possível alterar a senha!<br/>" +
						"1) As pessoas informadas não podem ser as mesmas;<br/>" +
						"2) Verifique se as matrículas e senhas foram informadas corretamente;<br/>" +
						"3) Verifique se as pessoas são da mesma lotação ou da lotação imediatamente superior em relação à matrícula que terá a senha alterada;<br/>");
				return Action.SUCCESS;
			}else{
				idNova = Cp.getInstance().getBL().criarIdentidade(matricula,cpf, getIdentidadeCadastrante(),senhaNova,senhaGerada,
						isIntegradoAoAD);
			}
			break;
		default:
			setMensagem("Método inválido!");
			return Action.SUCCESS;
		}
		
		if (isIntegradoAoAD){
			
			if (senhaTrocadaAD){
				msgComplemento = "<br/> Atenção: Sua senha de rede e e-mail foi definida com sucesso.";
			}else{
				msgComplemento = "<br/> ATENÇÃO: A senha de rede e e-mail NÃO foi definida embora o seu órgão esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
			}

			
		}else{
			msgComplemento = "<br/> O seu login e senha foram enviados para seu email.";
		}

		setMensagem("Usuário cadastrado com sucesso." + msgComplemento);

		return Action.SUCCESS;

	}

	private void verificarMetodoIntegracaoAD(String matricula) throws AplicacaoException {
		if (isIntegradoAD(matricula)){
			throw new AplicacaoException("Por favor, utilize o MÉTODO 2! O MÉTODO 1 só está disponível para órgãos sem integração de senhas entre o SIGA, e-mail e rede.");
		}
	}
	
	private boolean isIntegradoAD(String matricula) throws AplicacaoException {

		boolean result = false;
		
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if ( matricula == null || matricula.length() < 2 ) {
			log.warn( "A matrícula informada é nula ou inválida" );
			throw new AplicacaoException( "A matrícula informada é nula ou inválida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao().consultarPorSigla(orgaoFlt);

		if ( orgaoUsu != null ) {
			result = IntegracaoLdap.getInstancia().integrarComLdap(orgaoUsu);
		}
		
		return result;
	}
	

	public String aTrocarSenha() throws Exception {
		return Action.SUCCESS;
	}
	
	public String aTrocarSenhaGravar() throws Exception {
		String msgAD = "";
		getRequest().setAttribute("volta", "troca");
		getRequest().setAttribute("titulo", "Troca de Senha");
		String senhaAtual, senhaNova, senhaConfirma, nomeUsuario;
		senhaAtual = getSenhaAtual();
		senhaNova = getSenhaNova();
		senhaConfirma = getSenhaConfirma();
		nomeUsuario = getNomeUsuario().toUpperCase();
		
		CpIdentidade idNova = Cp.getInstance().getBL().trocarSenhaDeIdentidade(
				senhaAtual, senhaNova, senhaConfirma, nomeUsuario,
				getIdentidadeCadastrante());
		boolean senhaTrocadaAD = false;
		
		if (trocarSenhaRede!=null && trocarSenhaRede.equals("on")){
			senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova,senhaNova);	
		}

		if (isIntegradoAD(nomeUsuario) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail também foi alterada.";
		}
		
		if (isIntegradoAD(nomeUsuario) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATENÇÃO: A senha de rede e e-mail NÃO foi alterada embora o seu órgão esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		setMensagem("A senha foi alterada com sucesso" + msgAD);
		
		return Action.SUCCESS;

	}

	public Integer getMetodo() {
		return metodo;
	}

	public void setMetodo(Integer metodo) {
		this.metodo = metodo;
	}

	public String getAuxiliar1() {
		return auxiliar1;
	}

	public void setAuxiliar1(String auxiliar1) {
		this.auxiliar1 = auxiliar1;
	}

	public String getSenha1() {
		return senha1;
	}

	public void setSenha1(String senha1) {
		this.senha1 = senha1;
	}

	public String getAuxiliar2() {
		return auxiliar2;
	}

	public void setAuxiliar2(String auxiliar2) {
		this.auxiliar2 = auxiliar2;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

	public String isIntegradoLdap() throws AplicacaoException {
		return isIntegradoAD(getMatricula())==true?"ajax_retorno":"ajax_vazio";
	}
	
	public String isEmailValido() throws AplicacaoException {
		try{
			return isEmailValido(getMatricula())==true?"ajax_vazio":"ajax_retorno";	
		}catch(Exception e){
			setAjaxMsgErro(e.getMessage());
			return Action.ERROR;
		}
		
	}

	private boolean isEmailValido(String matricula) {
		
		DpPessoa pessoaFlt = new DpPessoa();
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if ( matricula == null || matricula.length() < 2 ) {
			log.warn( "A matrícula informada é nula ou inválida" );
			throw new AplicacaoException( "A matrícula informada é nula ou inválida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao().consultarPorSigla(orgaoFlt);
		
		if (orgaoUsu == null){
			throw new AplicacaoException("O órgão informado é nulo ou inválido." );
		}

		List<DpPessoa> lstPessoa = null;
		try{
			lstPessoa = dao().consultarPorMatriculaEOrgao(Long.valueOf(matricula.substring(2)), orgaoUsu.getId(), false, false);
		}catch(Exception e){
			throw new AplicacaoException("Formato de matrícula inválida." );
		}

		if (lstPessoa.size() == 0){
			throw new AplicacaoException("O usuário não está cadastrado no banco de dados." );
		}
		
		if (lstPessoa != null && lstPessoa.size() == 1) {
			DpPessoa p = lstPessoa.get(0);
			if (p.getEmailPessoaAtual() != null && p.getEmailPessoaAtual().trim().length() > 0){
				return true;
			}else{
				throw new AplicacaoException("Você ainda não possui um e-mail válido. Tente mais tarde." );
			}
		}
		
		return false;

	}

	public void setAjaxMsgErro(String ajaxMsgErro) {
		this.ajaxMsgErro = ajaxMsgErro;
	}
	
	public String getAjaxMsgErro() {
		return ajaxMsgErro;
	}

	public Long getIdOrgao() {
		return idOrgao;
	}
	
	public void setIdOrgao(Long idOrgao) {
		this.idOrgao = idOrgao;
	}
	
	public boolean isBaseTeste() {
		return  Boolean.valueOf(System.getProperty("isBaseTest").trim());
	}

	public void setTrocarSenhaRede(String trocarSenhaRede) {
		this.trocarSenhaRede = trocarSenhaRede;
	}

	public String isTrocarSenhaRede() {
		return trocarSenhaRede;
	}
}

// falta: salvar o grupo; manter o histórico; eliminar "simulandoUsuario" e
// substituir pela DpPessoa ou CpIdentidade