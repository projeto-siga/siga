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
package br.gov.jfrj.siga.cp.bl;

import java.io.File;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.util.CPFUtils;
import br.gov.jfrj.siga.cp.AbstractCpAcesso.CpTipoAcessoEnum;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoIdentidade;
import br.gov.jfrj.siga.cp.util.Excel;
import br.gov.jfrj.siga.cp.util.MatriculaUtils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;

public class CpBL {

	private static ResourceBundle bundle;

	CpCompetenciaBL comp;

	public CpCompetenciaBL getComp() {
		return comp;
	}

	public void setComp(CpCompetenciaBL comp) {
		this.comp = comp;
	}

	private CpDao dao() {
		return comp.getConfiguracaoBL().dao();
	}

	public CpIdentidade alterarIdentidade(CpIdentidade ident, Date dtExpiracao,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpIdentidade idNova = new CpIdentidade();
			try {
				PropertyUtils.copyProperties(idNova, ident);
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao copiar as propriedades da identidade anterior.");
			}
			idNova.setIdIdentidade(null);
			idNova.setDtExpiracaoIdentidade(dtExpiracao);

			dao().iniciarTransacao();
			CpIdentidade id = (CpIdentidade) dao().gravarComHistorico(idNova,
					ident, dt, identidadeCadastrante);
			dao().commitTransacao();
			return id;
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException(
					"Não foi possível cancelar a identidade.", 9, e);
		}
	}

	public void cancelarIdentidade(CpIdentidade ident,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpIdentidade idNova = new CpIdentidade();
			try {
				PropertyUtils.copyProperties(idNova, ident);
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao copiar as propriedades da identidade anterior.");
			}
			idNova.setIdIdentidade(null);
			idNova.setDtCancelamentoIdentidade(dt);
			idNova.setHisDtFim(dt);
			dao().iniciarTransacao();
			dao().gravarComHistorico(idNova, ident, dt, identidadeCadastrante);
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException(
					"Não foi possível cancelar a identidade.", 9, e);
		}
	}

	public void bloquearIdentidade(CpIdentidade ident,
			CpIdentidade identidadeCadastrante, boolean fBloquear)
			throws Exception {
		CpTipoConfiguracao tpConf = dao().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_FAZER_LOGIN,
				CpTipoConfiguracao.class, false);
		Date dt = dao().consultarDataEHoraDoServidor();

		CpConfiguracao confOld = null;
		try {
			CpConfiguracao confFiltro = new CpConfiguracao();
			confFiltro.setCpIdentidade(ident);
			confFiltro.setCpTipoConfiguracao(tpConf);
			confOld = comp.getConfiguracaoBL().buscaConfiguracao(confFiltro,
					new int[] { 0 }, null);
			if (confOld.getCpIdentidade() == null)
				confOld = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		CpConfiguracao conf = new CpConfiguracao();
		conf.setCpIdentidade(ident);
		conf.setCpSituacaoConfiguracao(dao()
				.consultar(
						fBloquear ? CpSituacaoConfiguracao.SITUACAO_NAO_PODE
								: CpSituacaoConfiguracao.SITUACAO_IGNORAR_CONFIGURACAO_ANTERIOR,
						CpSituacaoConfiguracao.class, false));
		conf.setCpTipoConfiguracao(tpConf);
		conf.setHisDtIni(dt);

		dao().iniciarTransacao();
		if (confOld != null) {
			confOld.setHisDtFim(dt);
			dao().gravarComHistorico(confOld, identidadeCadastrante);
		}
		dao().gravarComHistorico(conf, identidadeCadastrante);
		dao().commitTransacao();
		comp.getConfiguracaoBL().limparCacheSeNecessario();
	}

	public void bloquearPessoa(DpPessoa pes,
			CpIdentidade identidadeCadastrante, boolean fBloquear)
			throws Exception {

		try {
			dao().iniciarTransacao();

			CpTipoConfiguracao tpConf = dao().consultar(
					CpTipoConfiguracao.TIPO_CONFIG_FAZER_LOGIN,
					CpTipoConfiguracao.class, false);
			Date dt = dao().consultarDataEHoraDoServidor();

			CpConfiguracao confOld = null;
			try {
				CpConfiguracao confFiltro = new CpConfiguracao();
				confFiltro.setDpPessoa(pes);
				confFiltro.setCpTipoConfiguracao(tpConf);
				confOld = comp.getConfiguracaoBL().buscaConfiguracao(
						confFiltro, new int[] { 0 }, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			CpConfiguracao conf = new CpConfiguracao();
			conf.setDpPessoa(pes);
			conf.setCpSituacaoConfiguracao(dao()
					.consultar(
							fBloquear ? CpSituacaoConfiguracao.SITUACAO_NAO_PODE
									: CpSituacaoConfiguracao.SITUACAO_IGNORAR_CONFIGURACAO_ANTERIOR,
							CpSituacaoConfiguracao.class, false));
			conf.setCpTipoConfiguracao(tpConf);
			conf.setHisDtIni(dt);

			if (confOld != null) {
				confOld.setHisDtFim(dt);
				dao().gravarComHistorico(confOld, identidadeCadastrante);
			}
			dao().gravarComHistorico(conf, identidadeCadastrante);

			for (CpIdentidade ident : dao().consultaIdentidades(pes)) {
				if (fBloquear && ident.isBloqueada() == false)
					bloquearIdentidade(ident, identidadeCadastrante, true);
			}
			comp.getConfiguracaoBL().limparCacheSeNecessario();
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw e;
		}
	}

	public CpConfiguracao configurarAcesso(CpPerfil perfil, CpOrgaoUsuario orgao,
			DpLotacao lotacao, DpPessoa pes, CpServico servico,
			CpSituacaoConfiguracao situacao, CpTipoConfiguracao tpConf,
			CpIdentidade identidadeCadastrante) throws Exception {
		Date dt = dao().consultarDataEHoraDoServidor();

		CpConfiguracao confOld = null;
		try {
			CpConfiguracao confFiltro = new CpConfiguracao();
			confFiltro.setCpGrupo(perfil);
			confFiltro.setDpPessoa(pes);
			confFiltro.setLotacao(lotacao);
			confFiltro.setOrgaoUsuario(orgao);
			confFiltro.setCpServico(servico);
			confFiltro.setCpTipoConfiguracao(tpConf);
			confOld = comp.getConfiguracaoBL().buscaConfiguracao(confFiltro,
					new int[] { 0 }, null);
			if (confOld != null && !confOld.isEspecifica(confFiltro))
				confOld = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		CpConfiguracao conf = new CpConfiguracao();
		conf.setCpGrupo(perfil);
		conf.setDpPessoa(pes);
		conf.setLotacao(lotacao);
		conf.setOrgaoUsuario(orgao);
		conf.setCpServico(servico);
		conf.setCpSituacaoConfiguracao(situacao);
		conf.setCpTipoConfiguracao(tpConf);
		conf.setHisDtIni(dt);

		if (confOld != null) {
			confOld.setHisDtFim(dt);
			dao().gravarComHistorico(confOld, identidadeCadastrante);
		}
		dao().gravarComHistorico(conf, identidadeCadastrante);
		
		comp.getConfiguracaoBL().limparCacheSeNecessario();

		return conf;
	}
	
	public String alterarSenha(String cpf, String email, String matricula)
			throws AplicacaoException {
		
		String resultado = "";
		try {
			if( Pattern.matches( "\\d+", cpf) && cpf.length() == 11) {
				List<CpIdentidade> lista = null;
				
				if(matricula != null) {
					final long longmatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);
					DpPessoa pessoa = dao().consultarPorCpfMatricula(
							Long.parseLong(cpf), longmatricula);
					lista = dao().consultaIdentidades(pessoa);
				}
				
				if(email != null) {
					lista = dao().consultaIdentidadesPorCpfEmail(cpf, email);
				}
				
				if(!lista.isEmpty()) {
					lista = dao().consultaIdentidadesPorCpf(cpf);
				}
				if(!lista.isEmpty()) {
					String[] senhaGerada = new String[1];
					senhaGerada[0] = GeraMessageDigest.geraSenha();
					for (CpIdentidade cpIdentidade : lista) {
						Cp.getInstance().getBL().alterarSenhaDeIdentidade(cpIdentidade.getNmLoginIdentidade(),
								StringUtils.leftPad(cpIdentidade.getDpPessoa().getCpfPessoa().toString(), 11, "0"), null,senhaGerada);
					}
					resultado = "OK";
				} else {
					resultado = "Usuário não localizado.";
				}
			} else {
				resultado = "Usuário não localizado.";
			}

		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * Altera a senha da identidade.
	 * 
	 * @param matricula
	 * @param cpf
	 * @param idCadastrante
	 * @param senhaGerada
	 *            - Usado para retornar a senha gerada. É um array para que o
	 *            valor seja passado como referência e o método que o chama
	 *            tenha a oportunidade de conhecer a senha)
	 * @return
	 * @throws AplicacaoException
	 */
	public CpIdentidade alterarSenhaDeIdentidade(String matricula, String cpf,
			CpIdentidade idCadastrante, String[] senhaGerada)
			throws AplicacaoException {
		
		Long longCpf = CPFUtils.getLongValueValidaSimples(cpf);
		final List<DpPessoa> listaPessoas = dao().listarPorCpf(longCpf);
		if(listaPessoas.isEmpty()) {
			throw new AplicacaoException("O CPF informado está incorreto, tente novamente!");
		}
		
		final long longmatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);
		final DpPessoa pessoa = dao().consultarPorCpfMatricula(
				Long.parseLong(cpf), longmatricula);

		if (pessoa != null && pessoa.getSigla().equals(matricula)
				&& pessoa.getEmailPessoaAtual() != null) {

			// final Usuario usuario =
			// dao().consultaUsuarioCadastrante(matricula);

			CpIdentidade id = dao().consultaIdentidadeCadastrante(matricula,
					true);
			if (id != null) {
				String novaSenha = GeraMessageDigest.geraSenha();
				if (senhaGerada[0] != null) {
					novaSenha = senhaGerada[0];
				}
				try {
					Date dt = dao().consultarDataEHoraDoServidor();
					CpIdentidade idNova = new CpIdentidade();
					PropertyUtils.copyProperties(idNova, id);
					idNova.setIdIdentidade(null);
					idNova.setDtCancelamentoIdentidade(null);
					idNova.setDtCriacaoIdentidade(dt);

					final String hashNova = GeraMessageDigest.executaHash(
							novaSenha.getBytes(), "MD5");
					idNova.setDscSenhaIdentidade(hashNova);

					idNova.setDscSenhaIdentidadeCripto(null);
					idNova.setDscSenhaIdentidadeCriptoSinc(null);

					dao().iniciarTransacao();
					dao().gravarComHistorico(idNova, id, dt, idCadastrante);
					dao().commitTransacao();

					if (SigaMessages.isSigaSP()) {
						String[] destinanarios = { pessoa.getEmailPessoaAtual()};
						Correio.enviar(
							SigaBaseProperties.getString("servidor.smtp.usuario.remetente"),
							destinanarios,
							"Esqueci Minha Senha",
							"",
							"<table>"
							+ "<tbody>"
							+ "<tr>"
							+ "<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>"
							+ "<img style='padding: 10px 0px; text-align: center;' src='http://www.documentos.spsempapel.sp.gov.br/siga/imagens/logo-sem-papel-cor.png' "
							+ "alt='SP Sem Papel' width='108' height='50' /></td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td style='background-color: #bbb; padding: 0 20px;'>"
							+ "<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>"
							+ "</td>"
							+ "</tr>"
							+ "<tr style='height: 310px;'>"
							+ "<td style='height: 310px; padding: 10px 20px;'>"
							+ "<div>"
							+ "<h4><span style='color: #808080;'>Prezado Servidor(a) "
							+ "<strong>" + idNova.getDpPessoa().getNomePessoa() + "</strong>"
							+ " do(a) "
							+ "<strong>" + idNova.getDpPessoa().getOrgaoUsuario().getDescricao() + "</strong>" 
							+",</span></h4>"
							+ "<p><span style='color: #808080;'>Voc&ecirc; est&aacute; recebendo sua nova senha para acesso "
							+ "ao Portal SP Sem Papel.</span></p>"
							+ "<p><span style='color: #808080;'><strong>"
							+ "<p><span style='color: #808080;'>Sua matr&iacute;cula &eacute;:&nbsp;&nbsp;<strong>"
							+ idNova.getDpPessoa().getSigla()
							+ "</strong></span></p>"
							+ "<p><span style='color: #808080;'>Sua senha &eacute;:&nbsp;&nbsp;<strong>"
							+ novaSenha
							+ "</strong></span></p>"
							+ "</div>"
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>"
							+ "<p><span style='color: #aaa;'><strong>Aten&ccedil;&atilde;o:</strong> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span></p>"
							+ "</td>"
							+ "</tr>"
							+ "</tbody>"
							+ "</table>"
							);
					} else {
						Correio.enviar(
								pessoa.getEmailPessoaAtual(),
								"Alteração de senha ",
								"\n"
										+ idNova.getDpPessoa().getNomePessoa()
										+ "\nMatricula: "
										+ idNova.getDpPessoa().getSigla()
										+ "\n"
										+ "\nSua senha foi alterada para: "
										+ novaSenha
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda. ");
					}

					return idNova;
				} catch (final Exception e) {
					dao().rollbackTransacao();
					throw new AplicacaoException(
							"Ocorreu um erro durante a gravação", 0, e);
				}
			} else {
				throw new AplicacaoException(
						"Este usuário não está cadastrado no sistema");
			}

		} else {
			if (pessoa == null) {
				throw new AplicacaoException(
						getBundle().getString("usuario.erro.cpfmatriculanaocadastrado"));
			} else if (pessoa.getEmailPessoaAtual() == null) {
				throw new AplicacaoException(
						"Este usuário não possui e-mail cadastrado");
			} else {
				throw new AplicacaoException("Dados Incorretos!");
			}
		}
	}

	public CpIdentidade criarIdentidade(String matricula, String cpf,
			CpIdentidade idCadastrante, final String senhaDefinida,
			String[] senhaGerada, boolean marcarParaSinc)
			throws AplicacaoException {

		Long longCpf = CPFUtils.getLongValueValidaSimples(cpf);
		final List<DpPessoa> listaPessoas = dao().listarPorCpf(longCpf);
		if(listaPessoas.isEmpty()) {
			throw new AplicacaoException("O CPF informado está incorreto, tente novamente!");
		}
		
		final long longMatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);

		final DpPessoa pessoa = dao().consultarPorCpfMatricula(longCpf,
				longMatricula);

		if (pessoa != null && matricula.equals(pessoa.getSigla())) {
			CpIdentidade id;
			try {
				id = dao().consultaIdentidadeCadastrante(matricula, true);
			} catch (Exception e1) {
				id = null;
			}
			if (id == null) {
				if (pessoa.getEmailPessoaAtual() != null) {
					String novaSenha = null;
					if (senhaDefinida != null && senhaDefinida.length() > 0) {
						novaSenha = senhaDefinida;
					} else {
						novaSenha = GeraMessageDigest.geraSenha();
					}

					if (senhaGerada[0] != null) {
						novaSenha = senhaGerada[0];
					}
					try {
						CpIdentidade idNova = new CpIdentidade();
						final String hashNova = GeraMessageDigest.executaHash(
								novaSenha.getBytes(), "MD5");
						idNova.setDscSenhaIdentidade(hashNova);
						idNova.setNmLoginIdentidade(matricula);
						idNova.setDpPessoa(pessoa);
						idNova.setDtCriacaoIdentidade(dao()
								.consultarDataEHoraDoServidor());
						idNova.setCpOrgaoUsuario(pessoa.getOrgaoUsuario());
						idNova.setCpTipoIdentidade(dao().consultar(1,
								CpTipoIdentidade.class, false));
						idNova.setHisDtIni(idNova.getDtCriacaoIdentidade());

						dao().iniciarTransacao();
						dao().gravarComHistorico(idNova, idCadastrante);
						dao().commitTransacao();
						
						if (SigaMessages.isSigaSP()) {
							String[] destinanarios = { pessoa.getEmailPessoaAtual()};
							
							Correio.enviar(
								SigaBaseProperties.getString("servidor.smtp.usuario.remetente"),
								destinanarios,
								"Novo Usuário",
								"",
								"<table>"
								+ "<tbody>"
								+ "<tr>"
								+ "<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>"
								+ "<img style='padding: 10px 0px; text-align: center;' src='http://www.documentos.spsempapel.sp.gov.br/siga/imagens/logo-sem-papel-cor.png' "
								+ "alt='SP Sem Papel' width='108' height='50' /></td>"
								+ "</tr>"
								+ "<tr>"
								+ "<td style='background-color: #bbb; padding: 0 20px;'>"
								+ "<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>"
								+ "</td>"
								+ "</tr>"
								+ "<tr style='height: 310px;'>"
								+ "<td style='height: 310px; padding: 10px 20px;'>"
								+ "<div>"
								+ "<p><span style='color: #808080;'>Prezado Servidor(a) "
								+ "<strong>" + idNova.getDpPessoa().getNomePessoa() + "</strong>"
								+ " do(a) "
								+ "<strong>" + idNova.getDpPessoa().getOrgaoUsuario().getDescricao() + "</strong>" 
								+",</span></h4>"
								+ "<p><span style='color: #808080;'>Voc&ecirc; est&aacute; recebendo sua matr&iacute;cula e senha para acesso "
								+ "ao Portal SP Sem Papel, para acesso ao servi&ccedil;o Documentos Digitais.</span></p>"
								+ "<p><span style='color: #808080;'>Ao usar o portal para cria&ccedil;&atilde;o de documentos, voc&ecirc; est&aacute; "
								+ "produzindo documento nato-digital, confirme seus dados cadastrais, nome, cargo e unidade "
								+ "antes de iniciar o uso e assinar documentos.</span></p>"
								+ "<p><span style='color: #808080;'>Realize sua capacita&ccedil;&atilde;o no AVA e utilize o ambiente "
								+ "de capacita&ccedil;&atilde;o para testes e treinamento.</span></p>"
								+ "<p><span style='color: #808080;'>Sua matr&iacute;cula &eacute;:&nbsp;&nbsp;<strong>"
								+ matricula
								+ "</strong></span></p>"
								+ "<p><span style='color: #808080;'>Sua senha &eacute;:&nbsp;&nbsp;<strong>"
								+ novaSenha
								+ "</strong></span></p>"
								+ "</div>"
								+ "</td>"
								+ "</tr>"
								+ "<tr>"
								+ "<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>"
								+ "<p><span style='color: #aaa;'><strong>Aten&ccedil;&atilde;o:</strong> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span></p>"
								+ "</td>"
								+ "</tr>"
								+ "</tbody>"
								+ "</table>"
								);
						} else {
							Correio.enviar(
								pessoa.getEmailPessoaAtual(),
								"Novo Usuário",
								"Seu login é: "
								+ matricula
								+ "\n e sua senha é "
								+ novaSenha
								+ "\n\n Atenção: esta é uma "
								+ "mensagem automática. Por favor não responda ");
						}
						dao().commitTransacao();
						return idNova;
					} catch (final Exception e) {
						dao().rollbackTransacao();
						throw new AplicacaoException(
								"Ocorreu um erro durante a gravação no banco de dados ou no envio do email",
								0, e);
					}
				} else {
					throw new AplicacaoException(
							"Este usuário não possui e-mail cadastrado");
				}

			} else {
				throw new AplicacaoException(
						"Usuário já está cadastrado no sistema");
			}

		} else {
			if (pessoa == null) {
				throw new AplicacaoException(getBundle().getString("usuario.erro.cpfmatriculanaocadastrado"));
			} else {
				throw new AplicacaoException("Dados Incorretos!");
			}
		}

	}

	public CpIdentidade trocarSenhaDeIdentidade(String senhaAtual,
			String senhaNova, String senhaConfirma, String nomeUsuario,
			CpIdentidade idCadastrante) throws NoSuchAlgorithmException,
			AplicacaoException {
		if (senhaAtual == null || senhaAtual.trim().length() == 0) {
			throw new AplicacaoException("Senha atual não confere");
		}
		final String hashAtual = GeraMessageDigest.executaHash(
				senhaAtual.getBytes(), "MD5");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(
				nomeUsuario, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		boolean podeTrocar = id.getDscSenhaIdentidade().equals(hashAtual);

		if (!podeTrocar) {
			// tenta o modo administrador...
			String servico = "SIGA: Sistema Integrado de Gestão Administrativa;GI: Módulo de Gestão de Identidade;DEF_SENHA: Definir Senha";
			try {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(
								idCadastrante.getDpPessoa(),
								idCadastrante.getDpPessoa().getLotacao(),
								servico)) {

					if (hashAtual.equals(idCadastrante.getDscSenhaIdentidade())) {
						podeTrocar = true;
					} else {
						throw new AplicacaoException("Senha atual não confere");
					}

					try {
						Correio.enviar(
								id.getDpPessoa().getEmailPessoaAtual(),
								"Troca de Senha",
								"O Administrador do sistema alterou a senha do seguinte usuário, para efetuar "
										+ "uma manutenção no sistema: "
										+ "\n"
										+ "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa()
										+ "\n - Matricula: "
										+ id.getDpPessoa().getSigla()
										+ "\n - Senha: "
										+ senhaNova
										+ "\n\n Antes de utiliza-lo novamente, altere a sua senha "
										+ "ou solicite uma nova através da opção 'esqueci minha senha'"
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");
					} catch (Exception e) {
						System.out
								.println("Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
										+ "\n"
										+ "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa()
										+ "\n - Matricula: "
										+ id.getDpPessoa().getSigla());
					}
				}
			} catch (Exception e1) {

			}
		}

		if (podeTrocar && senhaNova.equals(senhaConfirma)) {
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				CpIdentidade idNova = new CpIdentidade();
				PropertyUtils.copyProperties(idNova, id);
				idNova.setIdIdentidade(null);
				idNova.setDtCriacaoIdentidade(dt);
				final String hashNova = GeraMessageDigest.executaHash(
						senhaNova.getBytes(), "MD5");
				idNova.setDscSenhaIdentidade(hashNova);

//				BASE64Encoder encoderBase64 = new BASE64Encoder();
//				String chave = encoderBase64.encode(id.getDpPessoa()
//						.getIdInicial().toString().getBytes());
//				String senhaCripto = encoderBase64.encode(Criptografia
//						.criptografar(senhaNova, chave));
				idNova.setDscSenhaIdentidadeCripto(null);
				idNova.setDscSenhaIdentidadeCriptoSinc(null);

				dao().iniciarTransacao();
				dao().gravarComHistorico(idNova, id, dt, idCadastrante);
				dao().commitTransacao();
				return idNova;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException(
						"Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException(
					"Senha Atual não confere e/ou Senha nova diferente de confirmação");
		}
	}
	
	public CpIdentidade trocarSenhaDeIdentidadeGovSp(String senhaAtual,
			String senhaNova, String senhaConfirma, String nomeUsuario,
			CpIdentidade idCadastrante, List<CpIdentidade> listaIdentidades) throws NoSuchAlgorithmException,
			AplicacaoException {
		if (senhaAtual == null || senhaAtual.trim().length() == 0) {
			throw new AplicacaoException("Senha atual não confere");
		}
		final String hashAtual = GeraMessageDigest.executaHash(
				senhaAtual.getBytes(), "MD5");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(
				nomeUsuario, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		boolean podeTrocar = Boolean.FALSE;
		
		for (CpIdentidade cpIdentidade : listaIdentidades) {
			if(cpIdentidade.getDscSenhaIdentidade().equals(hashAtual)) {
				podeTrocar = Boolean.TRUE;
				break;
			}
		}

		if (!podeTrocar) {
			// tenta o modo administrador...
			String servico = "SIGA: Sistema Integrado de Gestão Administrativa;GI: Módulo de Gestão de Identidade;DEF_SENHA: Definir Senha";
			try {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(
								idCadastrante.getDpPessoa(),
								idCadastrante.getDpPessoa().getLotacao(),
								servico)) {

					if (hashAtual.equals(idCadastrante.getDscSenhaIdentidade())) {
						podeTrocar = true;
					} else {
						throw new AplicacaoException("Senha atual não confere");
					}

					try {
						Correio.enviar(
								id.getDpPessoa().getEmailPessoaAtual(),
								"Troca de Senha",
								"O Administrador do sistema alterou a senha do seguinte usuário, para efetuar "
										+ "uma manutenção no sistema: "
										+ "\n"
										+ "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa()
										+ "\n - Matricula: "
										+ id.getDpPessoa().getSigla()
										+ "\n - Senha: "
										+ senhaNova
										+ "\n\n Antes de utiliza-lo novamente, altere a sua senha "
										+ "ou solicite uma nova através da opção 'esqueci minha senha'"
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");
					} catch (Exception e) {
						System.out
								.println("Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
										+ "\n"
										+ "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa()
										+ "\n - Matricula: "
										+ id.getDpPessoa().getSigla());
					}
				}
			} catch (Exception e1) {

			}
		}

		if (podeTrocar && senhaNova.equals(senhaConfirma)) {
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				final String hashNova = GeraMessageDigest.executaHash(
						senhaNova.getBytes(), "MD5");
				
				dao().iniciarTransacao();
				CpIdentidade i = null;
				for (CpIdentidade cpIdentidade : listaIdentidades) {
					i = new CpIdentidade();
					PropertyUtils.copyProperties(i,cpIdentidade);
					i.setIdIdentidade(null);
					i.setDtCriacaoIdentidade(dt);
					i.setDscSenhaIdentidade(hashNova);
					i.setDscSenhaIdentidadeCripto(null);
					i.setDscSenhaIdentidadeCriptoSinc(null);
					dao().gravarComHistorico(i, cpIdentidade, dt, idCadastrante);
				}
				
				dao().commitTransacao();
				return null;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException(
						"Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException(
					"Senha Atual não confere e/ou Senha nova diferente de confirmação");
		}
	}

	public boolean podeAlterarSenha(String auxiliar1, String cpf1,
			String senha1, String auxiliar2, String cpf2, String senha2,
			String matricula, String cpf, String novaSenha)
			throws AplicacaoException {
		try {
			final long matAux1 = Long.parseLong(auxiliar1.substring(2));
			final DpPessoa pesAux1 = dao().consultarPorCpfMatricula(
					Long.parseLong(cpf1), matAux1);
			if (pesAux1 == null) {
				throw new AplicacaoException("Auxiliar 1 inválido!");
			}

			final long matAux2 = Long.parseLong(auxiliar2.substring(2));
			final DpPessoa pesAux2 = dao().consultarPorCpfMatricula(
					Long.parseLong(cpf2), matAux2);
			if (pesAux2 == null) {
				throw new AplicacaoException("Auxiliar 2 inválido!");
			}

			final long longmatricula = Long.parseLong(matricula.substring(2));
			final DpPessoa pessoa = dao().consultarPorCpfMatricula(
					Long.parseLong(cpf), longmatricula);
			if (pessoa == null) {
				throw new AplicacaoException(
						"A pessoa que terá a senha definida inválida!");
			}

			CpIdentidade cpIdAux1 = null;
			CpIdentidade cpIdAux2 = null;
			for (CpIdentidade cpId : dao().consultaIdentidades(pesAux1)) {
				if (cpId.getCpTipoIdentidade().getIdCpTpIdentidade().equals(1)) {
					cpIdAux1 = cpId;
				}
			}

			for (CpIdentidade cpId : dao().consultaIdentidades(pesAux2)) {
				if (cpId.getCpTipoIdentidade().getIdCpTpIdentidade().equals(1)) {
					cpIdAux2 = cpId;
				}
			}

			if (cpIdAux1 == null || cpIdAux2 == null) {
				throw new AplicacaoException(
						"Problema ao localizar a identidade dos auxiliares!");
			}

			String hashSenha1 = null;
			String hashSenha2 = null;
			hashSenha1 = GeraMessageDigest
					.executaHash(senha1.getBytes(), "MD5");
			hashSenha2 = GeraMessageDigest
					.executaHash(senha2.getBytes(), "MD5");

			if (pesAux1.getIdInicial().equals(pesAux2.getIdInicial())
					|| pesAux1.getIdInicial().equals(pessoa.getIdInicial())
					|| pesAux2.getIdInicial().equals(pessoa.getIdInicial())) {
				throw new AplicacaoException("As pessoas devem ser diferentes!");
			}
			;
			if (!cpIdAux1.getDscSenhaIdentidade().equals(hashSenha1)
					|| !cpIdAux2.getDscSenhaIdentidade().equals(hashSenha2)) {
				throw new AplicacaoException("As senhas não conferem!");
			}

			List<DpPessoa> auxiliares = new ArrayList<DpPessoa>();
			auxiliares.add(pesAux1);
			auxiliares.add(pesAux2);

			if (isAuxAdministradores(pesAux1, pesAux2)) {
				return true;
			}

			if (!pessoasMesmaLotacaoOuSuperior(pessoa, auxiliares)) {
				throw new AplicacaoException(
						"Os auxiliares devem ser da mesma lotação do usuário que terá a senha trocada!\n Também é permitido que pessoas da lotação imediatamente superior na hiearquia sejam auxiliares.");
			}

		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return true;

	}

	private boolean isAuxAdministradores(DpPessoa aux1, DpPessoa aux2) {

		String servico = "SIGA: Sistema Integrado de Gestão Administrativa;GI: Módulo de Gestão de Identidade;DEF_SENHA: Definir Senha";
		try {

			return Cp
					.getInstance()
					.getConf()
					.podeUtilizarServicoPorConfiguracao(aux1,
							aux1.getLotacao(), servico)
					&& Cp.getInstance()
							.getConf()
							.podeUtilizarServicoPorConfiguracao(aux2,
									aux2.getLotacao(), servico);

		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Verifica se as pessoas são da mesma lotação ou da lotação imadiatamente
	 * superior da pessoa-alvo
	 * 
	 * @return
	 */
	private boolean pessoasMesmaLotacaoOuSuperior(DpPessoa alvo,
			List<DpPessoa> listaPessoas) {

		for (DpPessoa p : listaPessoas) {
			if ((alvo.getLotacao().getIdInicial()
					.equals(p.getLotacao().getIdInicial()) || (alvo
					.getLotacao().getLotacaoPai().getIdInicial().equals(p
					.getLotacao().getIdInicial())))) {
				continue;
			}
			return false;
		}
		return true;
	}

	public CpIdentidade definirSenhaDeIdentidade(String senhaNova,
			String senhaConfirma, String nomeUsuario, String auxiliar1,
			String auxiliar2, CpIdentidade idCadastrante)
			throws NoSuchAlgorithmException, AplicacaoException {

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(
				nomeUsuario, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		if (senhaNova.equals(senhaConfirma)) {
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				CpIdentidade idNova = new CpIdentidade();
				PropertyUtils.copyProperties(idNova, id);
				idNova.setIdIdentidade(null);
				idNova.setDtCriacaoIdentidade(dt);
				final String hashNova = GeraMessageDigest.executaHash(
						senhaNova.getBytes(), "MD5");
				idNova.setDscSenhaIdentidade(hashNova);

				idNova.setDscSenhaIdentidadeCripto(null);
				idNova.setDscSenhaIdentidadeCriptoSinc(null);

				dao().iniciarTransacao();
				dao().gravarComHistorico(idNova, id, dt, idCadastrante);
				dao().commitTransacao();
				Correio.enviar(
						id.getDpPessoa().getEmailPessoaAtual(),
						"Alteração de senha ",
						"\n"
								+ idNova.getDpPessoa().getNomePessoa()
								+ "\nMatricula: "
								+ idNova.getDpPessoa().getSigla()
								+ "\n"
								+ "\nSua senha foi alterada para: "
								+ senhaNova
								+ ".\n\n As seguintes pessoas participaram da alteração da senha: "
								+ auxiliar1
								+ " e "
								+ auxiliar2
								+ ".\n\n Atenção: esta é uma "
								+ "mensagem automática. Por favor, não responda. ");
				return idNova;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException(
						"Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException("Senha nova diferente de confirmação");
		}
	}

	public CpModelo alterarCpModelo(CpModelo mod, String conteudo,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpModelo modNew = new CpModelo();
			try {
				PropertyUtils.copyProperties(modNew, mod);
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao copiar as propriedades do modelo anterior.");
			}
			modNew.setIdMod(null);
			modNew.setConteudoBlobString(conteudo);

			dao().iniciarTransacao();
			CpModelo modSaved = (CpModelo) dao().gravarComHistorico(modNew,
					mod, dt, identidadeCadastrante);
			dao().commitTransacao();
			return modSaved;
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível alterar o modelo.",
					9, e);
		}
	}

	public void appException(String message) throws AplicacaoException {
		appException(message, null);
	}

	private void appException(String message, Throwable cause)
			throws AplicacaoException {
		throw new AplicacaoException(message, 0, cause);
	}

	public void logAcesso(CpTipoAcessoEnum tipo, String principal, int iat, int exp, String auditIP) {
		CpIdentidade identidade = dao().consultaIdentidadeCadastrante(principal, true);
		CpAcesso acesso = new CpAcesso();
		acesso.setCpIdentidade(identidade);
		acesso.setDtInicio(new Date(iat * 1000L));
		acesso.setDtTermino(new Date(exp * 1000L));
		acesso.setTipo(tipo);
		acesso.setAuditIP(auditIP);
		dao().gravar(acesso);
	}
	
	public InputStream uploadLotacao(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadLotacao(file, orgaoUsuario, extensao);
		} catch (Exception e) {
			
		}
		return inputStream;
	}
	
	public InputStream uploadFuncao(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadFuncao(file, orgaoUsuario, extensao);
		} catch (Exception e) {
			
		}
		return inputStream;
	}

	public InputStream uploadCargo(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadCargo(file, orgaoUsuario, extensao);
		} catch (Exception e) {
			
		}
		return inputStream;
	}
	
	public InputStream uploadPessoa(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade i) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadPessoa(file, orgaoUsuario, extensao, i);
		} catch (Exception e) {
			
		}
		return inputStream;
	}

	private static ResourceBundle getBundle() {
    	if (SigaBaseProperties.getString("siga.local") == null) {
    		bundle = ResourceBundle.getBundle("messages_TRF2");
    	} else {
    		bundle = ResourceBundle.getBundle("messages_" + SigaBaseProperties.getString("siga.local"));
    	}
        return bundle;
    }
	
	
	public String criarUsuario(final Long id, final Long idOrgaoUsu, final Long idCargo, final Long idFuncao, final Long idLotacao, final String nmPessoa, final String dtNascimento, 
			final String cpf, final String email) throws Exception{
		
		if(idOrgaoUsu == null || idOrgaoUsu == 0)
			throw new AplicacaoException("Órgão não Localizado");
		
		if(idCargo == null || idCargo == 0)
			throw new AplicacaoException("Cargo não Localizado");
		
		if(idLotacao == null || idLotacao == 0)
			throw new AplicacaoException("Lotação não Localizado");
		
		if(nmPessoa == null || nmPessoa.trim() == "")
			throw new AplicacaoException("Nome não informado");
		
		if(cpf == null || cpf.trim() == "") 
			throw new AplicacaoException("CPF não informado");
		
		if(email == null || email.trim() == "") 
			throw new AplicacaoException("E-mail não informado");
		
		if(nmPessoa != null && !nmPessoa.matches("[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ'' ]+")) 
			throw new AplicacaoException("Nome com caracteres não permitidos");
		
		
		DpPessoa pessoa = new DpPessoa();
		
		if (id == null) {
			Date data = new Date(System.currentTimeMillis());
			pessoa.setDataInicio(data);
			pessoa.setMatricula(0L);
			pessoa.setSituacaoFuncionalPessoa(SituacaoFuncionalEnum.APENAS_ATIVOS.getValor()[0]);
		} else {
			pessoa = dao().consultar(id, DpPessoa.class, false);
		}
		
		if(dtNascimento != null && !"".equals(dtNascimento)) {
			Date dtNasc = new Date();
			dtNasc = SigaCalendar.converteStringEmData(dtNascimento);
			
			Calendar hj = Calendar.getInstance();
			Calendar dtNasci = new GregorianCalendar();
			dtNasci.setTime(dtNasc);
			
			if(hj.before(dtNasci)) {
				throw new AplicacaoException("Data de nascimento inválida");
			}
			pessoa.setDataNascimento(dtNasc);
		} else {
			pessoa.setDataNascimento(null);
		}
		
		pessoa.setNomePessoa(Texto.removerEspacosExtra(nmPessoa).trim());
		pessoa.setCpfPessoa(Long.valueOf(cpf.replace("-", "").replace(".", "")));
		pessoa.setEmailPessoa(Texto.removerEspacosExtra(email).trim().replace(" ","").toLowerCase());
		
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		DpCargo cargo = new DpCargo();
		DpFuncaoConfianca funcao = new DpFuncaoConfianca();
		DpLotacao lotacao = new DpLotacao();
		
		ou.setIdOrgaoUsu(idOrgaoUsu);
		ou = CpDao.getInstance().consultarPorId(ou);
		cargo.setId(idCargo);
		lotacao.setId(idLotacao);
		funcao.setIdFuncao(idFuncao);
		
		pessoa.setOrgaoUsuario(ou);
		pessoa.setCargo(cargo);
		pessoa.setLotacao(lotacao);
		
		if(idFuncao != null && idFuncao != 0) {
			pessoa.setFuncaoConfianca(funcao);
		} else {
			pessoa.setFuncaoConfianca(null);
		}
		pessoa.setSesbPessoa(ou.getSigla());
		
		//ÓRGÃO / CARGO / FUNÇÃO DE CONFIANÇA / LOTAÇÃO e CPF iguais.
		DpPessoaDaoFiltro dpPessoa = new DpPessoaDaoFiltro();
		dpPessoa.setIdOrgaoUsu(pessoa.getOrgaoUsuario().getId());
		dpPessoa.setCargo(pessoa.getCargo());
		dpPessoa.setFuncaoConfianca(pessoa.getFuncaoConfianca());
		dpPessoa.setLotacao(pessoa.getLotacao());
		dpPessoa.setCpf(pessoa.getCpfPessoa());
		dpPessoa.setNome("");
		dpPessoa.setId(id);
		
		dpPessoa.setBuscarFechadas(Boolean.FALSE);
		Integer tamanho = dao().consultarQuantidade(dpPessoa);
		
		if(tamanho > 0) {
			throw new AplicacaoException("Usuário já cadastrado com estes dados: Órgão, Cargo, Função, Unidade e CPF");
		}
		
		try {
			dao().iniciarTransacao();
			dao().gravar(pessoa);
			if(pessoa.getIdPessoaIni() == null && pessoa.getId() != null) {
				pessoa.setIdPessoaIni(pessoa.getId());
				pessoa.setIdePessoa(pessoa.getId().toString());
				pessoa.setMatricula(10000 + pessoa.getId());
				pessoa.setIdePessoa(pessoa.getMatricula().toString());
				dao().gravar(pessoa);
				
				List<CpIdentidade> lista = CpDao.getInstance().consultaIdentidadesPorCpf(cpf.replace(".", "").replace("-", ""));
				CpIdentidade usu = null;
				if(lista.size() > 0) {
					CpIdentidade usuarioExiste = lista.get(0);
					usu = new CpIdentidade();
					usu.setCpTipoIdentidade(dao().consultar(1,
										CpTipoIdentidade.class, false));
					usu.setDscSenhaIdentidade(usuarioExiste.getDscSenhaIdentidade());
					usu.setDtCriacaoIdentidade(dao()
							.consultarDataEHoraDoServidor());
					usu.setCpOrgaoUsuario(ou);
					usu.setHisDtIni(usu.getDtCriacaoIdentidade());
					usu.setHisAtivo(1);
				}
				
				if(usu != null) {
					usu.setNmLoginIdentidade(pessoa.getSesbPessoa() + pessoa.getMatricula());
					usu.setDpPessoa(pessoa);
					//dao().gravarComHistorico(usu, getIdentidadeCadastrante());
				}
			}
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		
		
		
		return "Usuário cadastrado com sucesso: " + pessoa.getSesbPessoa() + pessoa.getMatricula();
		
		
	}
	

}
