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
package br.gov.jfrj.siga.ldap.sinc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.gov.jfrj.ldap.conf.LdapProperties;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Criptografia;
import br.gov.jfrj.siga.base.Prop;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SincProperties  {

	private Logger log = Logger.getLogger(SincProperties.class.getName());

	private String prefixo;

//	private static SincProperties instancia;

	private SincProperties() throws IOException, AplicacaoException {

	}

	public SincProperties(String prefixo) {
		super();
		setPrefixo(getPrefixoModulo() + "." + prefixo);
	}

//	public static SincProperties getInstancia(String prefixo) {
//		if (instancia == null) {
//			try {
//				instancia = new SincProperties();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (AplicacaoException e) {
//				e.printStackTrace();
//			}
//		}
//		if (prefixo != null) {
//			instancia.setPrefixo(prefixo);
//		}
//		return instancia;
//	}
//
//	public static SincProperties getInstancia() {
//		return getInstancia("");
//	}

	public String getPrefixoModulo() {
		return "siga.cp.sinc.ldap";
	}

	public Boolean isModoExclusaoUsuarioAtivo() {
		try {
			return Boolean.valueOf(this.obterPropriedade("modo_exclusao_usuario_ativo").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Boolean isModoExclusaoGrupoAtivo() {
		try {
			return Boolean.valueOf(this.obterPropriedade("modo_exclusao_grupo_ativo").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

//	public Boolean isModoEscrita() {
//		try {
//			return Boolean.valueOf(this.obterPropriedade("modo_escrita")
//					.trim());
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			return null;
//		}
//	}

	public Boolean getSincronizarGruposSeguranca() {
		try {
			return Boolean.valueOf(this.obterPropriedade("sincronizar_grupos_seguranca").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDominio() {
		try {
			return this.obterPropriedade("dominio").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getVersaoExchange() {
		try {
			return this.obterPropriedade("versao_exchange").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getBdStringConexao() {
		try {
			return this.obterPropriedade("bd.stringConexao").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getBdUsuario() {
		try {
			return this.obterPropriedade("bd.usuario").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getBdSenha() {
		try {
			return this.obterPropriedade("bd.senha").trim();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getDnDominio() {
		try {
			return this.obterPropriedade("dominioDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getDnGestaoIdentidade() {
		try {
			return this.obterPropriedade("container.gestaoIdentidadeDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposDistribuicao() {
		try {
			return this.obterPropriedade("container.gruposDistribuicaoDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposDistribuicaoInativos() {
		try {
			return this.obterPropriedade("container.gruposDistribuicaoInativosDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposSeguranca() {
		try {
			return this.obterPropriedade("container.gruposSegurancaDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnGruposSegurancaInativos() {
		try {
			return this.obterPropriedade("container.gruposSegurancaInativosDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnUsuarios() {
		try {
			return this.obterPropriedade("container.usuariosDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnUsuariosInativos() {
		try {
			return this.obterPropriedade("container.usuariosInativosDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getDnContatos() {
		try {
			return this.obterPropriedade("container.contatosDN").trim();
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
			return this.obterPropriedade("localidade.id").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Long getIdSvcFilesystemGabinete() {
		try {
			return Long.valueOf(this.obterPropriedade("servico.filesystem.gabinete.id").trim());
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
			return Long.valueOf(this.obterPropriedade("servico.filesystem.secretaria.id").trim());
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
			return Long.valueOf(this.obterPropriedade("servico.filesystem.juiz.id").trim());
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
			return Long.valueOf(this.obterPropriedade("servico.filesystem.pub.id").trim());
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
			return Long.valueOf(this.obterPropriedade("servico.filesystem.raiz.id").trim());
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
			return Long.valueOf(this.obterPropriedade("situacaofuncional.ativo.id").trim());
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
			return this.obterPropriedade("localidade.prefixoMatricula").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxObjExcluido() {
		try {
			return this.obterPropriedade("prefixo.objeto.excluido").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxObjExcluido() {
		try {
			return this.obterPropriedade("sufixo.objeto.excluido").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpDistrManualEmail() {
		try {
			return this.obterPropriedade("prefixo.grupo.distr.manual.email").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpDistrManualEmail() {
		try {
			return this.obterPropriedade("sufixo.grupo.distr.manual.email").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpDistrAuto() {
		try {
			return this.obterPropriedade("prefixo.grupo.distr.auto").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpDistrAuto() {
		try {
			return this.obterPropriedade("sufixo.grupo.distr.auto").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegAuto() {
		try {
			return this.obterPropriedade("prefixo.grupo.seg.auto").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegAuto() {
		try {
			return this.obterPropriedade("sufixo.grupo.seg.auto").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegManualPerfil() {
		try {
			return this.obterPropriedade("prefixo.grupo.seg.manual.perfil").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegManualPerfil() {
		try {
			return this.obterPropriedade("sufixo.grupo.seg.manual.perfil").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getPfxGrpSegManualPerfilJEE() {
		try {
			return this.obterPropriedade("prefixo.grupo.seg.manual.perfiljee").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getSfxGrpSegManualPerfilJEE() {
		try {
			return this.obterPropriedade("sufixo.grupo.seg.manual.perfiljee").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getHomeMDB() {
		try {
			return this.obterPropriedade("home_mdb").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getHomeMTA() {
		try {
			return this.obterPropriedade("home_mta").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getLegacyExchangeDN() {
		try {
			return this.obterPropriedade("legacy_exchange_dn").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getExchHomeServerName() {
		try {
			return this.obterPropriedade("exch_home_server_name").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getNomeContainerGI() {
		return getDnGestaoIdentidade().substring(3, getDnGestaoIdentidade().indexOf(","));
	}

	public String getNomeContainerGrpDistr() {
		return getDnGruposDistribuicao().substring(3, getDnGruposDistribuicao().indexOf(","));
	}

	public String getNomeContainerGrpSeg() {
		return getDnGruposSeguranca().substring(3, getDnGruposSeguranca().indexOf(","));
	}

	public String getNomeContainerUsuarios() {
		return getDnUsuarios().substring(3, getDnUsuarios().indexOf(","));
	}

	public String getNomeContainerUsuariosInativos() {
		return getDnUsuariosInativos().substring(3, getDnUsuariosInativos().indexOf(","));
	}

	public String getNomeContainerGrpDistrInativos() {
		return getDnGruposDistribuicaoInativos().substring(3, getDnGruposDistribuicaoInativos().indexOf(","));
	}

	public String getNomeContainerGrpSegInativos() {
		return getDnGruposSegurancaInativos().substring(3, getDnGruposSegurancaInativos().indexOf(","));
	}

	public String getNomeContainerContatos() {
		return getDnContatos().substring(3, getDnContatos().indexOf(","));
	}

	public String getNtfsServidor() {
		try {
			return this.obterPropriedade("ntfs.servidor").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getNtfsUsuario() {
		try {
			return this.obterPropriedade("ntfs.usuario").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

//	public String getNtfsSenha() {
//		try {
//			String senhaCriptografada = this.obterPropriedade("ntfs.senha").trim();
//			return descriptografarSenha(senhaCriptografada);
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			return null;
//		}
//	}

	public String getNtfsPathRaiz() {
		try {
			return this.obterPropriedade("ntfs.pathRaiz").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getContatosOcultosDasListas() {
		try {
			return this.obterPropriedade("exchange.contatosOcultosDasListas").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaHomeMdb() {
		try {
			return this.obterPropriedadeLista("exchange.homeMDB");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaTemplateLink() {
		try {
			return this.obterPropriedadeLista("exchange.templateLink");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaRegrasNovaCaixa() {
		try {
			return this.obterPropriedadeLista("exchange.regraNovaCaixa");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Boolean isSSLAtivo() {
		try {
			return Boolean.valueOf(this.obterPropriedade("ssl").trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaDominioEmail() {
		try {
			return this.obterPropriedadeLista("dominio_email");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getExchPoliciesExcluded() {
		try {
			return this.obterPropriedade("msExchPoliciesExcluded").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getUserPrincipalNameDomain() {
		try {
			return this.obterPropriedade("userPrincipalNameDomain").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public String getMDBUseDefaults() {
		try {
			return this.obterPropriedade("mDBUseDefaults").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Boolean getNtfsExecutarPsexecNaSaida() {
		try {
			return Boolean.valueOf(this.obterPropriedade("ntfs.executarPsexecNaSaida").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Boolean getNtfsExcluirArquivoNaSaida() {
		try {
			return Boolean.valueOf(this.obterPropriedade("ntfs.excluirArquivoNaSaida").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Boolean getPadronizadorIgnoraUsuariosConvertidos() {
		try {
			return Boolean.valueOf(this.obterPropriedade("padronizador.ignoraUsuariosConvertidos").trim());
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	public String getDnExtras() {
		try {
			return this.obterPropriedade("container.extrasDN").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	public List<String> getListaPontosPesquisaEmail() {
		try {
			return this.obterPropriedadeLista("container.pontospesquisa.email");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long getTempoLimiteSincSenha() {
		try {
			return Long.valueOf(this.obterPropriedade("tempoLimiteSincSenha").trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getListaShowInAddressBookGrupo() {
		try {
			return this.obterPropriedadeLista("showInAddressBookGrupo");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public List<String> getListaShowInAddressBookUsuario() {
		try {
			return this.obterPropriedadeLista("showInAddressBookUsuario");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public List<String> getListaServicosSincronizaveis() {
		try {
			return this.obterPropriedadeLista("servicosSincronizaveis");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public String getExpressaoUsuariosAtivos() {
		try {
			return this.obterPropriedade("expressao.usuarios.ativos").trim();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retorna a expressão a ser avaliada para saber se o usuário deve ou não ter a
	 * caixa postal criada
	 * 
	 * @return
	 */
	public String getExpressaoUsuariosCriarEmail() {
		try {
			return this.obterPropriedade("expressao.usuarios.criar.email").trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String obterPropriedade(String string) {
		return System.getProperty(getPrefixo() + "." + string);
	}

	/**
	 * obtém uma lista de propriedades que começam com um nome (parâmetro) que é
	 * separado por um ponto e seguido de um numero sequencial. Ex: parâmetro nome
	 * -> uf no arquivo : uf.0=AC uf.1=AL uf.2=AM ... uf.26=TO
	 * 
	 * Obs: Note que os parâmetros em lista também têm herança, da mesma forma que
	 * os parâmetros simples
	 * 
	 * @param nome nome do parâmetro a obter. Se atribuído o prefixo, o mesmo não
	 *             deve estar contido.
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> obterPropriedadeLista(String nome) throws Exception {
		ArrayList<String> lista = new ArrayList<String>();
		for (int i = 0; true; i++) {
			String prp = obterPropriedade(nome + "." + String.valueOf(i));
			if (prp == null)
				break;
			lista.add(prp.trim());
		}
		return lista;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	
	
	
	
	
	
	public String getServidorLdap()  {
		try {
			return obterPropriedade("servidor");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o servidor LDAP", 9, e);
		}
	}

	public String getPortaLdap()  {
		try {
			return obterPropriedade("porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta LDAP", 9, e);
		}
	}

	public String getPortaSSLLdap()  {
		try {
			return obterPropriedade("ssl.porta");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a porta SSL LDAP", 9, e);
		}
	}

	public String getUsuarioLdap()  {
		try {
			return obterPropriedade("usuario");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o usuário LDAP", 9, e);
		}
	}

	public String getSenhaLdap()  {
		try {
			String senhaCriptografada = obterPropriedade("senha");
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
	
	protected String getChaveCriptoParaSenha() {
		try {
			return obterPropriedade("chave.cripto.para.senha");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a chave cripto para a senha", 9, e);
		}
	}

	protected String descriptografarSenha(String senhaCriptografada)
			 {
		BASE64Encoder enc = new BASE64Encoder();
		BASE64Decoder dec = new BASE64Decoder();
		try {
			return new String(Criptografia.desCriptografar(dec
					.decodeBuffer(senhaCriptografada), enc.encode(getChaveCriptoParaSenha()
					.getBytes())));
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao descriptografar a senha LDAP", 9, e);
		} 
	}

	public String getKeyStore()  {
		try {
			return obterPropriedade("keystore");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o keystore", 9, e);
		}
	}
	
	public Boolean isModoEscrita() {
		try {
			String p = obterPropriedade("modo.escrita");
			if (p == null)
				return null;
			return Boolean.valueOf(p.trim());
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter a propriedade modo.escrita", 9, e);
		}
	}
}
