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
package br.gov.jfrj.siga.libs.webwork;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.acesso.ConheceUsuario;
import br.gov.jfrj.siga.acesso.UsuarioAutenticado;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.CertificadoUtil;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;

import com.opensymphony.webwork.interceptor.PrincipalAware;
import com.opensymphony.webwork.interceptor.PrincipalProxy;

public class SigaActionSupport extends SigaAnonimoActionSupport implements
PrincipalAware, ConheceUsuario {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4186578355554697523L;

	private static Log log = LogFactory.getLog(SigaActionSupport.class);

	private DpPessoa cadastrante;

	private DpPessoa titular;

	private DpLotacao lotaTitular;

	private CpIdentidade identidadeCadastrante;

	public static Log getLog() {
		return SigaActionSupport.log;
	}

	private PrincipalProxy principalProxy;

	private String mensagem;

	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Verifica se foi autenticado por um certificado de cliente
	 * 
	 * @return
	 */
	public boolean isClientCertAuth() {
		return CertificadoUtil.isClientCertAuth(getRequest());
	}

	@Override
	protected void carregaPerfil() throws Exception {
		// Nato: precisamos carregar o perfil apenas quando a ultima dependencia
		// for injetada.
		if (getContext() == null || getPrincipalProxy() == null)
			return;

		// if (SigaActionSupport.getLog().isDebugEnabled()) {
		// SigaActionSupport.getLog().debug("Executando o carrega Perfil");
		// }
		// if (getCadastrante() != null && (getCadastrante().getId() != null))
		// return;
		// if (principalProxy.getUserPrincipal() == null)
		// throw new AplicacaoException("Nenhum usuário autenticado.");
		try {
			if (UsuarioAutenticado.isClientCertAuth(getRequest())) {
				// autenticacao por certificado de cliente
				UsuarioAutenticado.carregarUsuarioAutenticadoRequest(
						getRequest(), this);
			} else {
				// autenticação por formulário
				String principal = principalProxy.getUserPrincipal().getName();
				UsuarioAutenticado.carregarUsuarioAutenticado(principal, this);
			}
		} catch (Exception e) {
			setCadastrante(null);
			if (sePaginaNecessitaPrincipal()) {
				throw new AplicacaoException(
						"Não foi possível carregar o usuário :"
								+ e.getMessage());
			}
			// if (SigaActionSupport.getLog().isErrorEnabled())
			// SigaActionSupport.getLog().error(
			// "Não foi possível carregar o perfil do usuário"
			// + e.toString());
			// throw new AplicacaoException(
			// "Não é possível carregar o usuário autenticado.", 0, e);
		}
	}

	/**
	 * verifica se a página necessita principal (userPrincipal). Atualmente não
	 * há obrigatoriedade de verificação, assim, nenhuma necessita de
	 * verificação. No entanto, no futuro seria interessante verificar quais as
	 * páginas reamente não necessitam como
	 * "br.gov.jfrj.webwork.action.UsuarioAction" e
	 * "br.gov.jfrj.siga.libs.webwork.LogoffAction" , colcocando o default como 
	 * true e estas subclasses como false.
	 * 
	 * @return
	 */
	protected boolean sePaginaNecessitaPrincipal() {
		return false;
	}

	public void setPrincipalProxy(final PrincipalProxy principalProxy) {
		this.principalProxy = principalProxy;
		try {
			carregaPerfil();
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public PrincipalProxy getPrincipalProxy() {
		return principalProxy;
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}
	
	public DpLotacao getLotaCadastrante(){
		return this.cadastrante.getLotacao();
	}

	public List<DpSubstituicao> getMeusTitulares() throws Exception {
		if (getCadastrante() == null)
			return null;
		DpSubstituicao dpSubstituicao = new DpSubstituicao();
		dpSubstituicao.setSubstituto(getCadastrante());
		dpSubstituicao.setLotaSubstituto(getCadastrante().getLotacao());
		List<DpSubstituicao> itens = dao().consultarSubstituicoesPermitidas(dpSubstituicao);
		return itens;
	}

	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	protected void invalidar() {
		setCadastrante(null);
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public void assertAcesso(String pathServico) throws AplicacaoException,
	Exception {
		String servico = "SIGA:Sistema Integrado de Gestão Administrativa;"
				+ pathServico;
		if (!Cp.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(getTitular(),
						getLotaTitular(), servico))
			throw new AplicacaoException("Acesso negado. Serviço: '" + servico
					+ "' usuário: " + getTitular().getSigla() + " lotação: "
					+ getLotaTitular().getSiglaCompleta());
	}

	public CpIdentidade getIdentidadeCadastrante() {
		return identidadeCadastrante;
	}

	public void setIdentidadeCadastrante(CpIdentidade identidadeCadastrante) {
		this.identidadeCadastrante = identidadeCadastrante;
	}

}
