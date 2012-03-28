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
package br.gov.jfrj.ldap.sinc;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import br.gov.jfrj.ldap.conf.LdapProperties;
import br.gov.jfrj.siga.base.AplicacaoException;

public class SincProperties extends LdapProperties{


	private Logger log = Logger.getLogger(SincProperties.class.getName());

	private static SincProperties instancia;
	
	private SincProperties() throws IOException, AplicacaoException {

	}

	public static SincProperties getInstancia(String prefixo) {
		if (instancia == null) {
			try {
				instancia = new SincProperties();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AplicacaoException e) {
				e.printStackTrace();
			}
		}
		if (prefixo!=null){
			instancia.setPrefixo(prefixo);
		}
		return instancia;
	}
	
	
	public static SincProperties getInstancia() {
		return getInstancia("");
	}
	
	
	@Override
	protected String getPrefixoModulo() {
		return "siga.cp.sinc.ldap";
	}

	public Boolean isModoExclusaoUsuarioAtivo() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.modo_exclusao_usuario_ativo").trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Boolean isModoExclusaoGrupoAtivo() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.modo_exclusao_grupo_ativo").trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

//	public Boolean isModoEscrita() {
//		try {
//			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.modo_escrita")
//					.trim());
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			return null;
//		}
//	}

	public Boolean getSincronizarGruposSeguranca() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.sincronizar_grupos_seguranca")
					.trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDominio() {
		try {
			return this.obterPropriedade("sinc.ldap.dominio").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	
	public String getBdStringConexao() {
		try {
			return this.obterPropriedade("sinc.ldap.bd.stringConexao").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getBdUsuario() {
		try {
			return this.obterPropriedade("sinc.ldap.bd.usuario").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getBdSenha() {
		try {
			return this.obterPropriedade("sinc.ldap.bd.senha").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	public String getDnDominio() {
		try {
			return this.obterPropriedade("sinc.ldap.dominioDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	public String getDnGestaoIdentidade() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.gestaoIdentidadeDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposDistribuicao() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.gruposDistribuicaoDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposDistribuicaoInativos() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.gruposDistribuicaoInativosDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposSeguranca() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.gruposSegurancaDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposSegurancaInativos() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.gruposSegurancaInativosDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnUsuarios() {
		try {
			return this.obterPropriedade("sinc.ldap.container.usuariosDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnUsuariosInativos() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.container.usuariosInativosDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getDnContatos() {
		try {
			return this.obterPropriedade("sinc.ldap.container.contatosDN").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

//	public String getLocalidade() {
//		try {
//			return this.obterPropriedade("sinc.localidade").trim();
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			return null;
//		}
//	}

	public String getIdLocalidade() {
		try {
			return this.obterPropriedade("sinc.ldap.localidade.id")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemGabinete() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.servico.filesystem.gabinete.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemSecretaria() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.servico.filesystem.secretaria.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemJuiz() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.servico.filesystem.juiz.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemPub() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.servico.filesystem.pub.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemRaiz() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.servico.filesystem.raiz.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSituacaoFuncionalAtivo() {
		try {
			return Long.valueOf(this.obterPropriedade(
					"sinc.ldap.situacaofuncional.ativo.id").trim());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public String getPrefixoMatricula() {
		try {
			return this.obterPropriedade("sinc.ldap.localidade.prefixoMatricula").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxObjExcluido() {
		try {
			return this.obterPropriedade("sinc.ldap.prefixo.objeto.excluido")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxObjExcluido() {
		try {
			return this.obterPropriedade("sinc.ldap.sufixo.objeto.excluido")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpDistrManualEmail() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.distr.manual.email").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpDistrManualEmail() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.distr.manual.email").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpDistrAuto() {
		try {
			return this.obterPropriedade("sinc.ldap.prefixo.grupo.distr.auto")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpDistrAuto() {
		try {
			return  this.obterPropriedade("sinc.ldap.sufixo.grupo.distr.auto")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAuto() {
		try {
			return this.obterPropriedade("sinc.ldap.prefixo.grupo.seg.auto")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegAuto() {
		try {
			return this.obterPropriedade("sinc.ldap.sufixo.grupo.seg.auto").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAutoSec() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.auto.secretaria").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegAutoSec() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.auto.secretaria").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAutoGab() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.auto.gabinete").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegAutoGab() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.auto.gabinete").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAutoJuiz() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.auto.juiz").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public String getPfxGrpSegAutoPub() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.auto.pub").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAutoRaiz() {
		return getPfxGrpSegAuto();
	}

	public String getSfxGrpSegAutoJuiz() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.auto.juiz").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegAutoPub() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.auto.pub").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public String getSfxGrpSegAutoRaiz() {
		return getSfxGrpSegAuto();
	}


	public String getPfxGrpSegManualPerfil() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.manual.perfil").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegManualPerfil() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.manual.perfil").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegManualPerfilJEE() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.prefixo.grupo.seg.manual.perfiljee").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegManualPerfilJEE() {
		try {
			return this.obterPropriedade(
					"sinc.ldap.sufixo.grupo.seg.manual.perfiljee").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getHomeMDB() {
		try {
			return this.obterPropriedade("sinc.ldap.home_mdb").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getHomeMTA() {
		try {
			return this.obterPropriedade("sinc.ldap.home_mta").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getLegacyExchangeDN() {
		try {
			return this.obterPropriedade("sinc.ldap.legacy_exchange_dn").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getExchHomeServerName() {
		try {
			return this.obterPropriedade("sinc.ldap.exch_home_server_name")
			.trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getNomeContainerGI(){
		return getDnGestaoIdentidade().substring(3,getDnGestaoIdentidade().indexOf(","));
	}
	
	public String getNomeContainerGrpDistr(){
		return getDnGruposDistribuicao().substring(3,getDnGruposDistribuicao().indexOf(","));
	}
	
	public String getNomeContainerGrpSeg(){
		return getDnGruposSeguranca().substring(3,getDnGruposSeguranca().indexOf(","));
	}
	
	public String getNomeContainerUsuarios(){
		return getDnUsuarios().substring(3,getDnUsuarios().indexOf(","));
	}
	
	public String getNomeContainerUsuariosInativos(){
		return getDnUsuariosInativos().substring(3,getDnUsuariosInativos().indexOf(","));
	}
	
	public String getNomeContainerGrpDistrInativos(){
		return getDnGruposDistribuicaoInativos().substring(3,getDnGruposDistribuicaoInativos().indexOf(","));
	}
	
	public String getNomeContainerGrpSegInativos(){
		return getDnGruposSegurancaInativos().substring(3,getDnGruposSegurancaInativos().indexOf(","));
	}
	
	public String getNomeContainerContatos(){
		return getDnContatos().substring(3,getDnContatos().indexOf(","));
	}
	
	public String getNtfsServidor() {
		try {
			return this.obterPropriedade("sinc.ldap.ntfs.servidor").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getNtfsUsuario() {
		try {
			return this.obterPropriedade("sinc.ldap.ntfs.usuario").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getNtfsSenha() {
		try {
			String senhaCriptografada = this.obterPropriedade("sinc.ldap.ntfs.senha").trim();
			return descriptografarSenha(senhaCriptografada);

		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public String getNtfsPathRaiz() {
		try {
			return this.obterPropriedade("sinc.ldap.ntfs.pathRaiz").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public String getContatosOcultosDasListas() {
		try {
			return this.obterPropriedade("sinc.ldap.exchange.contatosOcultosDasListas").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaHomeMdb() {
		try {
			return this.obterPropriedadeLista("sinc.ldap.exchange.homeMDB");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaTemplateLink() {
		try {
			return this.obterPropriedadeLista("sinc.ldap.exchange.templateLink");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaRegrasNovaCaixa() {
		try {
			return this.obterPropriedadeLista("sinc.ldap.exchange.regraNovaCaixa");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean isSSLAtivo(){
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.ssl").trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaDominioEmail() {
		try {
			return this.obterPropriedadeLista("sinc.ldap.dominio_email");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getExchPoliciesExcluded() {
		try {
			return this.obterPropriedade("sinc.ldap.msExchPoliciesExcluded").trim();
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Boolean getNtfsExecutarPsexecNaSaida() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.ntfs.executarPsexecNaSaida").trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Boolean getNtfsExcluirArquivoNaSaida() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.ntfs.excluirArquivoNaSaida").trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean getPadronizadorIgnoraUsuariosConvertidos(){
		try {
			return Boolean.valueOf(this.obterPropriedade("sinc.ldap.padronizador.ignoraUsuariosConvertidos").trim());
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}

	}

}
