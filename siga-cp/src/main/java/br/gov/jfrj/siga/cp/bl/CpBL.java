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

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.CPFUtils;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.AbstractCpAcesso.CpTipoAcessoEnum;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoIdentidade;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.auth.ValidadorDeSenha;
import br.gov.jfrj.siga.cp.auth.ValidadorDeSenhaFabrica;
import br.gov.jfrj.siga.cp.logic.CpExibirEmCampoDePesquisa;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.Excel;
import br.gov.jfrj.siga.cp.util.MatriculaUtils;
import br.gov.jfrj.siga.cp.util.SigaUtil;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpCargoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpFuncaoConfiancaDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapViaWebService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class CpBL {
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

	/**
	 * Marca uma {@link CpConfiguracao} como Fechada. Para isso preenche a
	 * {@link CpConfiguracao#setHisDtFim(Date) data de fim}. <br/>
	 * <b>NOTA DE IMPLMENTAÇÃO:</b> Originalmente simplesmente chamava-se
	 * {@link CpDao#gravarComHistorico(br.gov.jfrj.siga.cp.model.HistoricoAuditavel, CpIdentidade)}
	 * com <code>confoOld</code> que é carregado da base de dados. Entretanto na
	 * migração para o EAP 7.2 e o JPA (ao invés do Hibernate) ao se executar
	 * <code>gravarComHistorico</code> era disparada uma exceção com a mensagem de
	 * erro <cite>detached entity passed to persist</cite>. A solução encontrada foi
	 * carregar uma outra {@link CpConfiguracao} da base de dados com o ID de
	 * <code>confoOld</code>, preecher a {@link CpConfiguracao#setHisDtFim(Date)
	 * data de fim} deste e passá-lo para <code>gravarComHistorico</code>.
	 * 
	 * @param confOld               Configuração a ser fechada.
	 * @param identidadeCadastrante Usuário responsável pela atualização
	 * @param dt                    Data de fechamenbto.
	 * @see CpDao#gravarComHistorico(br.gov.jfrj.siga.cp.model.HistoricoAuditavel,
	 *      CpIdentidade)
	 */
	private void fecharAntigaConfiguracao(CpConfiguracao confOld, CpIdentidade identidadeCadastrante, Date dt) {
		if (confOld != null) {
			Long idConfiguracaoOld = confOld.getIdConfiguracao();
			CpConfiguracao confOldToSave = dao().consultar(idConfiguracaoOld, CpConfiguracao.class, false);
			confOldToSave.setHisDtFim(dt);
			dao().gravarComHistorico(confOldToSave, identidadeCadastrante);
		}
	}

	public CpIdentidade alterarIdentidade(CpIdentidade ident, Date dtExpiracao, CpIdentidade identidadeCadastrante)
			throws AplicacaoException {
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpIdentidade idNova = new CpIdentidade();
			try {
				PropertyUtils.copyProperties(idNova, ident);
			} catch (Exception e) {
				throw new AplicacaoException("Erro ao copiar as propriedades da identidade anterior.");
			}
			idNova.setIdIdentidade(null);
			idNova.setDtExpiracaoIdentidade(dtExpiracao);

			dao().iniciarTransacao();
			CpIdentidade id = (CpIdentidade) dao().gravarComHistorico(idNova, ident, dt, identidadeCadastrante);
			dao().commitTransacao();
			return id;
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível cancelar a identidade.", 9, e);
		}
	}

	public void cancelarIdentidade(CpIdentidade ident, CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpIdentidade idNova = new CpIdentidade();
			try {
				PropertyUtils.copyProperties(idNova, ident);
			} catch (Exception e) {
				throw new AplicacaoException("Erro ao copiar as propriedades da identidade anterior.");
			}
			idNova.setIdIdentidade(null);
			idNova.setDtCancelamentoIdentidade(dt);
			idNova.setHisDtFim(dt);
			dao().iniciarTransacao();
			dao().gravarComHistorico(idNova, ident, dt, identidadeCadastrante);
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível cancelar a identidade.", 9, e);
		}
	}

	public void bloquearIdentidade(CpIdentidade ident, CpIdentidade identidadeCadastrante, boolean fBloquear)
			throws Exception {
		if (ident != null) {
			CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.FAZER_LOGIN;
			Date dt = dao().consultarDataEHoraDoServidor();
	
			CpConfiguracao confOld = null;
			try {
				CpConfiguracao confFiltro = new CpConfiguracao();
				confFiltro.setCpIdentidade(ident);
				confFiltro.setCpTipoConfiguracao(tpConf);
				CpConfiguracaoCache confCacheOld = comp.getConfiguracaoBL().buscaConfiguracao(confFiltro, new int[] { 0 }, null);
				if (confCacheOld != null) 
					confOld = dao().consultar(confCacheOld.idConfiguracao, CpConfiguracao.class, false);
				if (confOld == null || confOld.getCpIdentidade() == null)
					confOld = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			CpConfiguracao conf = new CpConfiguracao();
			conf.setCpIdentidade(ident.getIdentidadeInicial());
			conf.setCpSituacaoConfiguracao(fBloquear ? CpSituacaoDeConfiguracaoEnum.NAO_PODE
							: CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR);
			conf.setCpTipoConfiguracao(tpConf);
			conf.setHisDtIni(dt);
	
			dao().iniciarTransacao();
			fecharAntigaConfiguracao(confOld, identidadeCadastrante, dt);
			dao().gravarComHistorico(conf, identidadeCadastrante);
			dao().commitTransacao();
			comp.getConfiguracaoBL().limparCacheSeNecessario();
		} else {
			throw new AplicacaoException("Não é possível efetuar o Bloqueio. Identidade não localizada.");
		}
	}

	public void bloquearPessoa(DpPessoa pes, CpIdentidade identidadeCadastrante, boolean fBloquear) throws Exception {
		if (pes != null) {
			try {
				dao().iniciarTransacao();
	
				CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.FAZER_LOGIN;
				Date dt = dao().consultarDataEHoraDoServidor();
	
				CpConfiguracao confOld = null;
				try {
					CpConfiguracao confFiltro = new CpConfiguracao();
					confFiltro.setDpPessoa(pes);
					confFiltro.setCpTipoConfiguracao(tpConf);
					CpConfiguracaoCache confCacheOld = comp.getConfiguracaoBL().buscaConfiguracao(confFiltro, new int[] { 0 }, null);
					if (confCacheOld != null) 
						confOld = dao().consultar(confCacheOld.idConfiguracao, CpConfiguracao.class, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				CpConfiguracao conf = new CpConfiguracao();
				conf.setDpPessoa(pes.getPessoaInicial());
				conf.setCpSituacaoConfiguracao(fBloquear ? CpSituacaoDeConfiguracaoEnum.NAO_PODE
								: CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR);
				conf.setCpTipoConfiguracao(tpConf);
				conf.setHisDtIni(dt);
	
				fecharAntigaConfiguracao(confOld, identidadeCadastrante, dt);
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
		} else {
			throw new AplicacaoException("Não é possível efetuar o Bloqueio. Pessoa não localizada.");
		}
	}

	public CpConfiguracao configurarAcesso(CpPerfil perfil, CpOrgaoUsuario orgao, DpLotacao lotacao, DpPessoa pes,
			CpServico servico, CpSituacaoDeConfiguracaoEnum situacao, ITipoDeConfiguracao tpConf,
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
			CpConfiguracaoCache confCacheOld = comp.getConfiguracaoBL().buscaConfiguracao(confFiltro, new int[] { 0 }, null);
			if (confCacheOld != null) 
				confOld = dao().consultar(confCacheOld.idConfiguracao, CpConfiguracao.class, false);
			if (confOld != null && !confOld.isEspecifica(confFiltro))
				confOld = null;
			if (confOld != null) 
				confOld = dao().consultar(confOld.getIdConfiguracao(), CpConfiguracao.class, false);
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

		fecharAntigaConfiguracao(confOld, identidadeCadastrante, dt);
		dao().gravarComHistorico(conf, identidadeCadastrante);

		comp.getConfiguracaoBL().limparCacheSeNecessario();

		return conf;
	}

	public String alterarSenha(String cpf, String email, String matricula) throws AplicacaoException {

		String resultado = "";
		try {
			if (Pattern.matches("\\d+", cpf) && cpf.length() == 11) {
				List<CpIdentidade> lista = null;

				if (matricula != null) {
					final long longmatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);
					DpPessoa pessoa = dao().consultarPorCpfMatricula(Long.parseLong(cpf), longmatricula);
					lista = dao().consultaIdentidades(pessoa);
				}

				if (email != null) {
					lista = dao().consultaIdentidadesPorCpfEmail(cpf, email);
				}

				if (!lista.isEmpty()) {
					lista = dao().consultaIdentidadesPorCpf(cpf);
				}
				if (!lista.isEmpty()) {
					String[] senhaGerada = new String[1];
					senhaGerada[0] = GeraMessageDigest.geraSenha();
					for (CpIdentidade cpIdentidade : lista) {
						Cp.getInstance().getBL().alterarSenhaDeIdentidade(cpIdentidade.getNmLoginIdentidade(),
								StringUtils.leftPad(cpIdentidade.getDpPessoa().getPessoaAtual().getCpfPessoa().toString(), 11, "0"),
								null, senhaGerada);
					}
					resultado = "OK";
				} else {
					resultado = "Usuário não localizado.";
				}
			} else {
				resultado = "Usuário não localizado.";
			}

		} catch (AplicacaoException e) {
			resultado = e.getMessage();
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
	 * @param senhaGerada   - Usado para retornar a senha gerada. É um array para
	 *                      que o valor seja passado como referência e o método que
	 *                      o chama tenha a oportunidade de conhecer a senha)
	 * @return
	 * @throws AplicacaoException
	 */
	public CpIdentidade alterarSenhaDeIdentidade(String matricula, String cpf, CpIdentidade idCadastrante,
			String[] senhaGerada) throws AplicacaoException {

		Long longCpf = CPFUtils.getLongValueValidaSimples(cpf);
		final List<DpPessoa> listaPessoas = dao().listarPorCpf(longCpf);
		if (listaPessoas.isEmpty()) {
			throw new AplicacaoException("O CPF informado está incorreto, tente novamente!");
		}

		final long longmatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);
		final DpPessoa pessoa = dao().consultarPorCpfMatricula(Long.parseLong(cpf), longmatricula);

		if (pessoa != null && pessoa.getSigla().equals(matricula) && pessoa.getEmailPessoaAtual() != null) {

			// final Usuario usuario =
			// dao().consultaUsuarioCadastrante(matricula);

			CpIdentidade id = dao().consultaIdentidadeCadastrante(matricula, true);
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

					final String hashNova = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(novaSenha);
					idNova.setDscSenhaIdentidade(hashNova);

					idNova.setDscSenhaIdentidadeCripto(null);
					idNova.setDscSenhaIdentidadeCriptoSinc(null);

					dao().iniciarTransacao();
					dao().gravarComHistorico(idNova, id, dt, idCadastrante);
					dao().commitTransacao();

					if (SigaMessages.isSigaSP()) {
						String[] destinanarios = { pessoa.getEmailPessoaAtual() };
						Correio.enviar(null, destinanarios,
								"Esqueci Minha Senha", "",
								"<table>" + "<tbody>" + "<tr>"
										+ "<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>"
										+ "<img style='padding: 10px 0px; text-align: center;' src='" + Prop.get("/siga.base.url") + "/" + Prop.get("/siga.email.logo")+ "' "
										+ "alt='SP Sem Papel' width='108' height='50' /></td>" + "</tr>" + "<tr>"
										+ "<td style='background-color: #bbb; padding: 0 20px;'>"
										+ "<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>"
										+ "</td>" + "</tr>" + "<tr style='height: 310px;'>"
										+ "<td style='height: 310px; padding: 10px 20px;'>" + "<div>"
										+ "<h4><span style='color: #808080;'>Prezado Servidor(a) " + "<strong>"
										+ idNova.getDpPessoa().getNomePessoa() + "</strong>" + " do(a) " + "<strong>"
										+ idNova.getDpPessoa().getOrgaoUsuario().getDescricao() + "</strong>"
										+ ",</span></h4>"
										+ "<p><span style='color: #808080;'>Voc&ecirc; est&aacute; recebendo sua nova senha para acesso "
										+ "ao Portal SP Sem Papel.</span></p>"
										+ "<p><span style='color: #808080;'><strong>"
										+ "<p><span style='color: #808080;'>Sua matr&iacute;cula &eacute;:&nbsp;&nbsp;<strong>"
										+ idNova.getDpPessoa().getSigla() + "</strong></span></p>"
										+ "<p><span style='color: #808080;'>Sua senha &eacute;:&nbsp;&nbsp;<strong>"
										+ novaSenha + "</strong></span></p>" + "</div>" + "</td>" + "</tr>" + "<tr>"
										+ "<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>"
										+ "<p><span style='color: #aaa;'><strong>Aten&ccedil;&atilde;o:</strong> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span></p>"
										+ "</td>" + "</tr>" + "</tbody>" + "</table>");
					} else {
						Correio.enviar(pessoa.getEmailPessoaAtual(), "Alteração de senha ",
								"\n" + idNova.getDpPessoa().getNomePessoa() + "\nMatricula: "
										+ idNova.getDpPessoa().getSigla() + "\n" + "\nSua senha foi alterada para: "
										+ novaSenha + "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda. ");
					}

					return idNova;
				} catch (final Exception e) {
					dao().rollbackTransacao();
					throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
				}
			} else {
				throw new AplicacaoException("Este usuário não está cadastrado no sistema");
			}

		} else {
			if (pessoa == null) {
				throw new AplicacaoException(
						SigaMessages.getMessage("usuario.erro.cpfmatriculanaocadastrado"));
			} else if (pessoa.getEmailPessoaAtual() == null) {
				throw new AplicacaoException("Este usuário não possui e-mail cadastrado");
			} else {
				throw new AplicacaoException("Dados Incorretos!");
			}
		}
	}

	public CpIdentidade criarIdentidade(String matricula, String cpf, CpIdentidade idCadastrante,
			final String senhaDefinida, String[] senhaGerada, boolean marcarParaSinc) throws AplicacaoException {

		Long longCpf = CPFUtils.getLongValueValidaSimples(cpf);
		final List<DpPessoa> listaPessoas = dao().listarPorCpf(longCpf);
		if (listaPessoas.isEmpty()) {
			throw new AplicacaoException("O CPF informado está incorreto, tente novamente!");
		}

		final long longMatricula = MatriculaUtils.getParteNumericaDaMatricula(matricula);

		final DpPessoa pessoa = dao().consultarPorCpfMatricula(longCpf, longMatricula);

		String siglaOrgaoMatricula = MatriculaUtils.getSiglaDoOrgaoDaMatricula(matricula);
		boolean autenticaPeloBanco = buscarModoAutenticacao(siglaOrgaoMatricula)
				.equals(GiService._MODO_AUTENTICACAO_BANCO);

		if (pessoa != null && matricula.equals(pessoa.getSigla())) {
			CpIdentidade id;
			try {
				id = dao().consultaIdentidadeCadastrante(matricula, true);
			} catch (Exception e1) {
				id = null;
			}
			if (id == null) {
				if (pessoa.getEmailPessoaAtual() != null) {
					String novaSenha = "";
					if (autenticaPeloBanco) {
						if (senhaDefinida != null && senhaDefinida.length() > 0) {
							novaSenha = senhaDefinida;
						} else {
							novaSenha = GeraMessageDigest.geraSenha();
						}

						if (senhaGerada[0] != null) {
							novaSenha = senhaGerada[0];
						}
					}
					try {
						CpIdentidade idNova = new CpIdentidade();
						String hashNova = "";
						if (autenticaPeloBanco) {
							hashNova = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(novaSenha);
						}
						idNova.setDscSenhaIdentidade(hashNova);
						idNova.setNmLoginIdentidade(matricula);
						idNova.setDpPessoa(pessoa);
						idNova.setDtCriacaoIdentidade(dao().consultarDataEHoraDoServidor());
						idNova.setCpOrgaoUsuario(pessoa.getOrgaoUsuario());
						idNova.setCpTipoIdentidade(dao().consultar(1, CpTipoIdentidade.class, false));
						idNova.setHisDtIni(idNova.getDtCriacaoIdentidade());

						dao().iniciarTransacao();
						dao().gravarComHistorico(idNova, idCadastrante);
						dao().commitTransacao();

						if (SigaMessages.isSigaSP()) {
							String[] destinanarios = { pessoa.getEmailPessoaAtual() };
							String conteudoHTML = pessoaIsUsuarioExterno(pessoa)
									? textoEmailNovoUsuarioExternoSP(idNova, matricula, novaSenha)
									: textoEmailNovoUsuarioSP(idNova, matricula, novaSenha, autenticaPeloBanco);
							
							Correio.enviar(null,
									destinanarios, "Novo Usuário", "", conteudoHTML);
						} else {
							Correio.enviar(pessoa.getEmailPessoaAtual(), "Novo Usuário",
									textoEmailNovoUsuario(matricula, novaSenha, autenticaPeloBanco));
						}
						dao().commitTransacao();
						return idNova;
					} catch (final Exception e) {
						dao().rollbackTransacao();
						throw new AplicacaoException(
								"Ocorreu um erro durante a gravação no banco de dados ou no envio do email", 0, e);
					}
				} else {
					throw new AplicacaoException("Este usuário não possui e-mail cadastrado");
				}

			} else {
				throw new AplicacaoException("Usuário já está cadastrado no sistema");
			}

		} else {
			if (pessoa == null) {
				throw new AplicacaoException(
						SigaMessages.getMessage("usuario.erro.cpfmatriculanaocadastrado"));
			} else {
				throw new AplicacaoException("Dados Incorretos!");
			}
		}

	}

	/**
	 * Verifica se a {@link DpPessoa Pessoa} é um usuário externo. <br>
	 * <b>NOTA DE IMPLEMENTAÇÃO:</b> Originalmente dentro de
	 * {@link #criarIdentidade(String, String, CpIdentidade, String, String[], boolean)}
	 * era chamado {@link DpPessoa#isUsuarioExterno()}, que faz uso de
	 * {@link DpPessoa#getLotacao() lotação}. Entretanto durante a migração para o
	 * EAP 7.2, verificou-se que quando o método <code>criarIdentidade</code> era
	 * chamado a partir de
	 * {@link #criarUsuario(String, Long, Long, Long, Long, Long, String, String, String, String)},
	 * ao se chamar {@link DpPessoa#getLotacao()} dentro de
	 * {@link DpPessoa#isUsuarioExterno()} acontecia um
	 * {@link NullPointerException}, mesmo que essa pessoa tivesse acabado de ter
	 * sido carregada pelo Hibernate. Entretanto, se for chamado
	 * <code>pessoa.getLotacao().getId()</code> não houve problema. <br/>
	 * A solução foi emular a implmentação de {@link DpPessoa#isUsuarioExterno()},
	 * usar essa <code>pessoa.getLotacao().getId()</code>, para consultar a Lotação
	 * na base de dados através de
	 * {@link ModeloDao#consultar(java.io.Serializable, Class, boolean)} e chamar
	 * {@link DpLotacao#getIsExternaLotacao()}.
	 * 
	 * @param pessoa Pessoa a ser avaliada.
	 * @return se a Pessoa é um usuário externo.
	 */
	private boolean pessoaIsUsuarioExterno(DpPessoa pessoa) {
		if (pessoa.getOrgaoUsuario() == null && pessoa.getLotacao() == null) {
			return false;
		}		
		
		CpOrgaoUsuario orgaoUsuario = pessoa.getOrgaoUsuario();
		Integer isExternoOrgaoUsu = orgaoUsuario.getIsExternoOrgaoUsu();
		if ((isExternoOrgaoUsu != null) && (isExternoOrgaoUsu == 1)) {
			return true;
		}

		Long idLotacao = pessoa.getLotacao().getId();
		if (idLotacao == null) {
			return false;
		}

		DpLotacao lotacao = dao().consultar(idLotacao, DpLotacao.class, false);
		return (lotacao != null) && (lotacao.getIsExternaLotacao() == 1);
	}

	private String textoEmailNovoUsuario(String matricula, String novaSenha, boolean autenticaPeloBanco) {
		StringBuffer retorno = new StringBuffer();

		retorno.append("Seu login é: ");
		retorno.append(matricula);
		retorno.append("\n e sua senha é ");
		if (autenticaPeloBanco) {
			retorno.append(novaSenha);
		} else {
			retorno.append("a mesma usada para logon na rede (Windows).");
		}
		retorno.append("\n\n Atenção: esta é uma ");
		retorno.append("mensagem automática. Por favor não responda ");

		return retorno.toString();
	}

	private String textoEmailNovoUsuarioSP(CpIdentidade identidade, String matricula, String novaSenha,
			boolean autenticaPeloBanco) {
		StringBuffer retorno = new StringBuffer();

		retorno.append("<table>");
		retorno.append("<tbody>");
		retorno.append("<tr>");
		retorno.append("<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>");
		retorno.append(
				"<img style='padding: 10px 0px; text-align: center;' src='" + Prop.get("/siga.base.url") + "/" + Prop.get("/siga.email.logo")+ "' ");
		retorno.append("alt='SP Sem Papel' width='108' height='50' /></td>");
		retorno.append("</tr>");
		retorno.append("<tr>");
		retorno.append("<td style='background-color: #bbb; padding: 0 20px;'>");
		retorno.append("<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("<tr style='height: 310px;'>");
		retorno.append("<td style='height: 310px; padding: 10px 20px;'>");
		retorno.append("<div>");
		retorno.append("<p><span style='color: #808080;'>Prezado Servidor(a) ");
		retorno.append("<strong>" + identidade.getDpPessoa().getNomePessoa() + "</strong>");
		retorno.append(" do(a) ");
		retorno.append("<strong>" + identidade.getDpPessoa().getOrgaoUsuario().getDescricao() + "</strong>");
		retorno.append(",</span></h4>");
		retorno.append(
				"<p><span style='color: #808080;'>Voc&ecirc; est&aacute; recebendo sua matr&iacute;cula e senha para acesso ");
		retorno.append("ao Portal SP Sem Papel, para acesso ao servi&ccedil;o Documentos Digitais.</span></p>");
		retorno.append(
				"<p><span style='color: #808080;'>Ao usar o portal para cria&ccedil;&atilde;o de documentos, voc&ecirc; est&aacute; ");
		retorno.append("produzindo documento nato-digital, confirme seus dados cadastrais, nome, cargo e unidade ");
		retorno.append("antes de iniciar o uso e assinar documentos.</span></p>");
		retorno.append(
				"<p><span style='color: #808080;'>Realize sua capacita&ccedil;&atilde;o no AVA e utilize o ambiente ");
		retorno.append("de capacita&ccedil;&atilde;o para testes e treinamento.</span></p>");
		retorno.append("<p><span style='color: #808080;'>Sua matr&iacute;cula &eacute;:&nbsp;&nbsp;<strong>");
		retorno.append(matricula);
		retorno.append("</strong></span></p>");
		if (autenticaPeloBanco) {
			retorno.append("<p><span style='color: #808080;'>Sua senha &eacute;:&nbsp;&nbsp;<strong>");
			retorno.append(novaSenha);
			retorno.append("</strong></span></p>");
		} else {
			retorno.append("<p><span style='color: #808080;'>");
			retorno.append("Sua senha &eacute; a mesma usada para logon na rede (Windows).");
			retorno.append("</span></p>");
		}
		retorno.append("</div>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("<tr>");
		retorno.append("<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>");
		retorno.append(
				"<p><span style='color: #aaa;'><strong>Aten&ccedil;&atilde;o:</strong> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span></p>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("</tbody>");
		retorno.append("</table>");
		return retorno.toString();
	}
	
	private String textoEmailAltOrgaoUsuarioSP(CpIdentidade identidade, String matricula) {
		StringBuffer retorno = new StringBuffer();

		retorno.append("<table>");
		retorno.append("<tbody>");
		retorno.append("<tr>");
		retorno.append("<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>");
		retorno.append(
				"<img style='padding: 10px 0px; text-align: center;' src='" + Prop.get( "/siga.base.url") + "/" + Prop.get("/siga.email.logo")+ "' ");
		retorno.append("alt='SP Sem Papel' width='108' height='50' /></td>");
		retorno.append("</tr>");
		retorno.append("<tr>");
		retorno.append("<td style='background-color: #bbb; padding: 0 20px;'>");
		retorno.append("<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("<tr style='height: 310px;'>");
		retorno.append("<td style='height: 310px; padding: 10px 20px;'>");
		retorno.append("<div>");
		retorno.append("<p><span style='color: #808080;'>Prezado Servidor(a) ");
		retorno.append("<strong>" + identidade.getDpPessoa().getNomePessoa() + "</strong>");
		retorno.append(" do(a) ");
		retorno.append("<strong>" + identidade.getDpPessoa().getOrgaoUsuario().getDescricao() + "</strong>");
		retorno.append(".</span></h4>");
		retorno.append(
				"<p><span style='color: #808080;'>Voc&ecirc; est&aacute; recebendo sua Nova matr&iacute;cula para acesso ");
		retorno.append("ao Portal SP Sem Papel, a senha permanece a mesma.</span></p>");
		retorno.append(
				"<p><span style='color: #808080;'>Ao usar o portal para cria&ccedil;&atilde;o de documentos, voc&ecirc; est&aacute; ");
		retorno.append("produzindo documento nato-digital, confirme seus dados cadastrais, nome, cargo e unidade ");
		retorno.append("antes de iniciar o uso e assinar documentos.</span></p>");
		
		retorno.append("<p><span style='color: #808080;'>Sua matr&iacute;cula &eacute;:&nbsp;&nbsp;<strong>");
		retorno.append(matricula);
		retorno.append("</strong></span></p>");
		
		retorno.append("</div>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("<tr>");
		retorno.append("<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>");
		retorno.append(
				"<p><span style='color: #aaa;'><strong>Aten&ccedil;&atilde;o:</strong> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span></p>");
		retorno.append("</td>");
		retorno.append("</tr>");
		retorno.append("</tbody>");
		retorno.append("</table>");
		return retorno.toString();
	}
	
	private String textoEmailNovoUsuarioExternoSP(CpIdentidade identidade, String matricula, String novaSenha) {		
		String conteudo = "";
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/email/novo-usuario-externo.html")))) {			
			String str;
			
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			
		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário externo " + identidade.getDpPessoa().getNomePessoa());
		}
		
		
		conteudo = conteudo.replace("${nomeUsuario}", identidade.getDpPessoa().getNomePessoa())
			.replace("${cpfUsuario}", matricula)
			.replace("${url}", Prop.get("/sigaex.url").replace("/sigaex/app", ""))
			.replace("${senhaUsuario}", novaSenha);
		
		return conteudo;
	}

	public String buscarModoAutenticacao(String orgao) {
		String retorno = GiService._MODO_AUTENTICACAO_DEFAULT;
		if (Prop.get("/siga.ldap.orgaos") == null) {
			return retorno;
		}
		else {
			for (String sesbPessoa: Prop.get("/siga.ldap.orgaos").split(",")) {
				if (sesbPessoa.equalsIgnoreCase(orgao)) {
					String modo = Prop.get("/siga.ldap." + orgao.toLowerCase() + ".modo");
					if (modo != null) {
						retorno = modo;
						break;
					}
				}
			}
			
		}

		return retorno;
	}

	public CpIdentidade trocarSenhaDeIdentidade(String senhaAtual, String senhaNova, String senhaConfirma,
			String nomeUsuario, CpIdentidade idCadastrante) throws NoSuchAlgorithmException, AplicacaoException {

		// usuario existe?
		final CpIdentidade id = dao().consultaIdentidadeCadastrante(nomeUsuario, true);
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		boolean autenticaPeloBanco = buscarModoAutenticacao(id.getCpOrgaoUsuario().getSiglaOrgaoUsu())
				.equals(GiService._MODO_AUTENTICACAO_BANCO);
		if (!autenticaPeloBanco)
			throw new AplicacaoException("O usuário deve modificar sua senha usando a interface do Windows "
					+ "(acionando as teclas Ctrl, Alt e Del / Delete, opção 'Alterar uma senha')"
					+ ", ou entrando em contato com a Central de Atendimento.");

		// preencheu senha atual?
		if (senhaAtual == null || senhaAtual.trim().length() == 0) {
			throw new AplicacaoException("Senha atual não confere");
		}

		boolean podeTrocar = false;
		boolean podeTrocarSenhaAdm = false;

		podeTrocar = ValidadorDeSenhaFabrica.getInstance().validarSenha(id, senhaAtual);

		if (!podeTrocar) {
			// tenta o modo administrador... Podendo autenticar o ADM pelo LDAP
			String servico = "SIGA: Sistema Integrado de Gestão Administrativa;GI: Módulo de Gestão de Identidade;DEF_SENHA: Definir Senha";
			try {
				boolean admTrocaSenha = Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(
						idCadastrante.getDpPessoa(), idCadastrante.getDpPessoa().getPessoaAtual().getLotacao(), servico);
				if (admTrocaSenha) {
					if (buscarModoAutenticacao(idCadastrante.getCpOrgaoUsuario().getSiglaOrgaoUsu())
							.equals(GiService._MODO_AUTENTICACAO_BANCO)) {
						podeTrocarSenhaAdm = ValidadorDeSenhaFabrica.getInstance().validarSenha(idCadastrante, senhaAtual);
					} else {
						podeTrocarSenhaAdm = autenticarViaLdap(senhaAtual, idCadastrante);
					}

					if (!podeTrocarSenhaAdm)
						throw new AplicacaoException("Senha atual não confere");

					try {
						Correio.enviar(id.getDpPessoa().getEmailPessoaAtual(), "Troca de Senha",
								"O Administrador do sistema alterou a senha do seguinte usuário, para efetuar "
										+ "uma manutenção no sistema: " + "\n" + "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa() + "\n - Matricula: "
										+ id.getDpPessoa().getSigla() + "\n - Senha: " + senhaNova
										+ "\n\n Antes de utiliza-lo novamente, altere a sua senha "
										+ "ou solicite uma nova através da opção 'esqueci minha senha'"
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");
					} catch (Exception e) {
						System.out.println(
								"Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
										+ "\n" + "\n - Nome: " + id.getDpPessoa().getNomePessoa() + "\n - Matricula: "
										+ id.getDpPessoa().getSigla());
					}
				}
			} catch (Exception e1) {

			}
		}

		if ((podeTrocar || podeTrocarSenhaAdm) && senhaNova.equals(senhaConfirma)) {
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				CpIdentidade idNova = new CpIdentidade();
				PropertyUtils.copyProperties(idNova, id);
				idNova.setIdIdentidade(null);
				idNova.setDtCriacaoIdentidade(dt);
				final String hashNova = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(senhaNova);
				idNova.setDscSenhaIdentidade(hashNova);

				// BASE64Encoder encoderBase64 = new BASE64Encoder();
				// String chave = encoderBase64.encode(id.getDpPessoa()
				// .getIdInicial().toString().getBytes());
				// String senhaCripto = encoderBase64.encode(Criptografia
				// .criptografar(senhaNova, chave));
				idNova.setDscSenhaIdentidadeCripto(null);
				idNova.setDscSenhaIdentidadeCriptoSinc(null);

				dao().iniciarTransacao();
				dao().gravarComHistorico(idNova, id, dt, idCadastrante);
				dao().commitTransacao();
				return idNova;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException("Senha Atual não confere e/ou Senha nova diferente de confirmação");
		}
	}

	public CpIdentidade trocarSenhaDeIdentidadeGovSp(String senhaAtual, String senhaNova, String senhaConfirma,
			String nomeUsuario, CpIdentidade idCadastrante, List<CpIdentidade> listaIdentidades)
			throws NoSuchAlgorithmException, AplicacaoException {
		if (senhaAtual == null || senhaAtual.trim().length() == 0) {
			throw new AplicacaoException("Senha atual não confere");
		}
		final String hashAtual = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(senhaAtual);

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(nomeUsuario, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		boolean podeTrocar = Boolean.FALSE;

		for (CpIdentidade cpIdentidade : listaIdentidades) {
			if (cpIdentidade.getDscSenhaIdentidade().equals(hashAtual)) {
				podeTrocar = Boolean.TRUE;
				break;
			}
		}

		if (!podeTrocar) {
			// tenta o modo administrador...
			String servico = "SIGA: Sistema Integrado de Gestão Administrativa;GI: Módulo de Gestão de Identidade;DEF_SENHA: Definir Senha";
			try {
				if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(idCadastrante.getDpPessoa(),
						idCadastrante.getDpPessoa().getLotacao(), servico)) {

					if (hashAtual.equals(idCadastrante.getDscSenhaIdentidade())) {
						podeTrocar = true;
					} else {
						throw new AplicacaoException("Senha atual não confere");
					}

					try {
						Correio.enviar(id.getDpPessoa().getEmailPessoaAtual(), "Troca de Senha",
								"O Administrador do sistema alterou a senha do seguinte usuário, para efetuar "
										+ "uma manutenção no sistema: " + "\n" + "\n - Nome: "
										+ id.getDpPessoa().getNomePessoa() + "\n - Matricula: "
										+ id.getDpPessoa().getSigla() + "\n - Senha: " + senhaNova
										+ "\n\n Antes de utiliza-lo novamente, altere a sua senha "
										+ "ou solicite uma nova através da opção 'esqueci minha senha'"
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");
					} catch (Exception e) {
						System.out.println(
								"Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
										+ "\n" + "\n - Nome: " + id.getDpPessoa().getNomePessoa() + "\n - Matricula: "
										+ id.getDpPessoa().getSigla());
					}
				}
			} catch (Exception e1) {

			}
		}

		if (podeTrocar && senhaNova.equals(senhaConfirma)) {
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				final String hashNova = GeraMessageDigest.executaHash(senhaNova.getBytes(), "MD5");

				dao().iniciarTransacao();
				CpIdentidade i = null;
				for (CpIdentidade cpIdentidade : listaIdentidades) {
					i = new CpIdentidade();
					PropertyUtils.copyProperties(i, cpIdentidade);
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
				throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException("Senha Atual não confere e/ou Senha nova diferente de confirmação");
		}
	}
	
	
	public CpIdentidade redefinirSenha(String token, String senhaNova, String senhaConfirma,
			String cpfUsuario, List<CpIdentidade> listaIdentidades)
			throws NoSuchAlgorithmException, AplicacaoException {


		boolean podeTrocar = Boolean.FALSE;

		if (listaIdentidades.isEmpty()) {
			throw new RuntimeException("Usuário não possui um acesso ativo para que seja redefinida a Senha.");
		}
		
		for (CpIdentidade cpIdentidade : listaIdentidades) {
			podeTrocar = Boolean.TRUE;
			break;
		}


		if (podeTrocar && senhaNova.equals(senhaConfirma)) {
			consisteFormatoSenha(senhaNova);
			try {
				Date dt = dao().consultarDataEHoraDoServidor();
				final String hashNova = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(senhaNova);

				dao().iniciarTransacao();
				CpIdentidade i = null;
				for (CpIdentidade cpIdentidade : listaIdentidades) {
					i = new CpIdentidade();
					PropertyUtils.copyProperties(i, cpIdentidade);
					i.setIdIdentidade(null);
					i.setDtCriacaoIdentidade(dt);
					i.setDscSenhaIdentidade(hashNova);
					i.setDscSenhaIdentidadeCripto(null);
					i.setDscSenhaIdentidadeCriptoSinc(null);
					dao().gravarComHistorico(i, cpIdentidade, dt, null);
				}

				dao().commitTransacao();
				return null;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException("Senha Atual não confere e/ou Senha nova diferente de confirmação");
		}
	}

	private boolean autenticarViaLdap(String login, String senhaAtual) {
		boolean podeTrocar;
		try {
			podeTrocar = IntegracaoLdapViaWebService.getInstancia().autenticarUsuario(login, senhaAtual);
		} catch (Exception e) {
			podeTrocar = false;
		}
		return podeTrocar;
	}

	private boolean autenticarViaLdap(String senhaAtual, final CpIdentidade id) {
		return autenticarViaLdap(id.getNmLoginIdentidade(), senhaAtual);
	}

	public boolean podeAlterarSenha(String auxiliar1, String cpf1, String senha1, String auxiliar2, String cpf2,
			String senha2, String matricula, String cpf, String novaSenha) throws AplicacaoException {
		try {
			final long matAux1 = Long.parseLong(auxiliar1.substring(2));
			final DpPessoa pesAux1 = dao().consultarPorCpfMatricula(Long.parseLong(cpf1), matAux1);
			if (pesAux1 == null) {
				throw new AplicacaoException("Auxiliar 1 inválido!");
			}

			final long matAux2 = Long.parseLong(auxiliar2.substring(2));
			final DpPessoa pesAux2 = dao().consultarPorCpfMatricula(Long.parseLong(cpf2), matAux2);
			if (pesAux2 == null) {
				throw new AplicacaoException("Auxiliar 2 inválido!");
			}

			final long longmatricula = Long.parseLong(matricula.substring(2));
			final DpPessoa pessoa = dao().consultarPorCpfMatricula(Long.parseLong(cpf), longmatricula);
			if (pessoa == null) {
				throw new AplicacaoException("A pessoa que terá a senha definida inválida!");
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
				throw new AplicacaoException("Problema ao localizar a identidade dos auxiliares!");
			}

			if (pesAux1.getIdInicial().equals(pesAux2.getIdInicial())
					|| pesAux1.getIdInicial().equals(pessoa.getIdInicial())
					|| pesAux2.getIdInicial().equals(pessoa.getIdInicial())) {
				throw new AplicacaoException("As pessoas devem ser diferentes!");
			}
			;
			if (!ValidadorDeSenhaFabrica.getInstance().validarSenha(cpIdAux1, senha1) || !ValidadorDeSenhaFabrica.getInstance().validarSenha(cpIdAux2, senha2) ) {
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

			return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(aux1, aux1.getLotacao(), servico)
					&& Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(aux2, aux2.getLotacao(), servico);

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
	private boolean pessoasMesmaLotacaoOuSuperior(DpPessoa alvo, List<DpPessoa> listaPessoas) {

		for (DpPessoa p : listaPessoas) {
			if ((alvo.getLotacao().getIdInicial().equals(p.getLotacao().getIdInicial())
					|| (alvo.getLotacao().getLotacaoPai().getIdInicial().equals(p.getLotacao().getIdInicial())))) {
				continue;
			}
			return false;
		}
		return true;
	}

	public CpIdentidade definirSenhaDeIdentidade(String senhaNova, String senhaConfirma, String nomeUsuario,
			String auxiliar1, String auxiliar2, CpIdentidade idCadastrante)
			throws NoSuchAlgorithmException, AplicacaoException {

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(nomeUsuario, true);
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
				final String hashNova = ValidadorDeSenhaFabrica.getInstance().gerarMessageDigest(senhaNova);
				idNova.setDscSenhaIdentidade(hashNova);

				idNova.setDscSenhaIdentidadeCripto(null);
				idNova.setDscSenhaIdentidadeCriptoSinc(null);

				dao().iniciarTransacao();
				dao().gravarComHistorico(idNova, id, dt, idCadastrante);
				dao().commitTransacao();
				Correio.enviar(id.getDpPessoa().getEmailPessoaAtual(), "Alteração de senha ",
						"\n" + idNova.getDpPessoa().getNomePessoa() + "\nMatricula: " + idNova.getDpPessoa().getSigla()
								+ "\n" + "\nSua senha foi alterada para: " + senhaNova
								+ ".\n\n As seguintes pessoas participaram da alteração da senha: " + auxiliar1 + " e "
								+ auxiliar2 + ".\n\n Atenção: esta é uma "
								+ "mensagem automática. Por favor, não responda. ");
				return idNova;
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
			}
		} else {
			throw new AplicacaoException("Senha nova diferente de confirmação");
		}
	}

	public CpModelo alterarCpModelo(CpModelo mod, String conteudo, CpIdentidade identidadeCadastrante)
			throws AplicacaoException { 
		try {
			Date dt = dao().consultarDataEHoraDoServidor();
			CpModelo modNew = new CpModelo();
			try {
				PropertyUtils.copyProperties(modNew, mod);
			} catch (Exception e) {
				throw new AplicacaoException("Erro ao copiar as propriedades do modelo anterior.");
			}
			modNew.setIdMod(null);
			modNew.setConteudoBlobString(conteudo);

			dao().iniciarTransacao();
			CpModelo modSaved = (CpModelo) dao().gravarComHistorico(modNew, mod, dt, identidadeCadastrante);
			dao().commitTransacao();
			return modSaved;
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível alterar o modelo.", 9, e);
		}
	}

	public void appException(String message) throws AplicacaoException {
		appException(message, null);
	}

	private void appException(String message, Throwable cause) throws AplicacaoException {
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

	public InputStream uploadLotacao(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade cadastrante) {			
		Excel excel = new Excel();		
		return excel.uploadLotacao(file, orgaoUsuario, extensao, cadastrante);
	}

	public InputStream uploadFuncao(File file, CpOrgaoUsuario orgaoUsuario, String extensao, final CpIdentidade identidadeCadastrante) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadFuncao(file, orgaoUsuario, extensao, identidadeCadastrante);
		} catch (Exception e) {

		}
		return inputStream;
	}

	public InputStream uploadCargo(File file, CpOrgaoUsuario orgaoUsuario, String extensao,final CpIdentidade identidadeCadastrante) {
		InputStream inputStream = null;
		try {
			Excel excel = new Excel();
			inputStream = excel.uploadCargo(file, orgaoUsuario, extensao,identidadeCadastrante);
		} catch (Exception e) {

		}
		return inputStream;
	}

	public InputStream uploadPessoa(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade i) {
		Excel excel = new Excel();				
		return excel.uploadPessoa(file, orgaoUsuario, extensao, i);
	}

	public DpPessoa criarUsuario(final Long id, final CpIdentidade identidadeCadastrante, final Long idOrgaoUsu, final Long idCargo, final Long idFuncao,
			final Long idLotacao, final String nmPessoa, final String dtNascimento, final String cpf,
			final String email, final String identidade, final String orgaoIdentidade, final String ufIdentidade,
			final String dataExpedicaoIdentidade, final String nomeExibicao, final String enviarEmail) {
		if (idOrgaoUsu == null || idOrgaoUsu == 0)
			throw new AplicacaoException("Órgão não informado");
		if (idCargo == null || idCargo == 0)
			throw new AplicacaoException("Cargo não informado");

		if (idLotacao == null || idLotacao == 0)
			throw new AplicacaoException("Lotação não informado");

		if (nmPessoa == null || nmPessoa.trim() == "")
			throw new AplicacaoException("Nome não informado");

		if (cpf == null || cpf.trim() == "")
			throw new AplicacaoException("CPF não informado");

		if (email == null || email.trim() == "")
			throw new AplicacaoException("E-mail não informado");
		
		if (!Utils.isEmailValido(email)) {			
			throw new AplicacaoException("E-mail inválido");
		}

		if (nmPessoa != null && !nmPessoa.matches(Texto.DpPessoa.NOME_REGEX_CARACTERES_PERMITIDOS))
			throw new AplicacaoException("Nome com caracteres não permitidos");

		Boolean podeAlterarOrgaoPessoa = false;
		
		if (identidadeCadastrante != null)
		        podeAlterarOrgaoPessoa = Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(identidadeCadastrante.getPessoaAtual(),
				identidadeCadastrante.getPessoaAtual().getLotacao(), 
				"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;CAD_PESSOA:Cadastrar Pessoa;ALT:Alterar Órgão Cadastro Pessoa");
		
		DpPessoa pessoa = new DpPessoa();
		DpPessoa pessoaAnt = new DpPessoa();
		List<CpIdentidade> lista = new ArrayList<CpIdentidade>();
		
		if(id != null) {
			pessoaAnt = CpDao.getInstance().consultar(id, DpPessoa.class, false).getPessoaAtual();
			
			if(pessoaAnt != null) {
				
				//Dentro do Mesmo Órgão
				if (!idLotacao.equals(pessoaAnt.getLotacao().getId()) && pessoaAnt.getOrgaoUsuario().getId().equals(idOrgaoUsu)) {
					if (!podeAlterarLotacaoPessoaDentroMesmoOrgao(pessoaAnt)) {
						throw new AplicacaoException("A "+ SigaMessages.getMessage("usuario.lotacao").toLowerCase()  +" da pessoa não pode ser alterada, pois existem documentos pendentes", 0);
					}
				}		
				
				//Para outro órgão
				if(!pessoaAnt.getOrgaoUsuario().getId().equals(idOrgaoUsu)) {
					if (podeAlterarOrgaoPessoa) {
						List<Long> marcadores = new ArrayList<Long>();
						marcadores.add(CpMarcadorEnum.CAIXA_DE_ENTRADA.getId());
						marcadores.add(CpMarcadorEnum.EM_ELABORACAO.getId());
						
						Long qtdeCaixaEntradaTMPPessoa = CpDao.getInstance().quantidadeMarcasPorPessoaMarcadores(pessoaAnt, marcadores, true);
						Long qtdeCaixaEntradaTMPLotacao = CpDao.getInstance().quantidadeMarcasPorLotacaoMarcadores(pessoaAnt.getLotacao(), marcadores, true);
						Long qtdePessoaLotacao = CpDao.getInstance().qtdePessoaLotacao(pessoaAnt.getLotacao().getLotacaoAtual(), Boolean.TRUE);
						
						if(qtdeCaixaEntradaTMPPessoa > 0 || (qtdeCaixaEntradaTMPLotacao > 0 && qtdePessoaLotacao.equals(Long.valueOf(1L)))) {
							throw new AplicacaoException(
									"O Órgão da Pessoa não pode ser alterado, pois existem documentos pendentes na Caixa de Entrada/TMP do Usuário/" + SigaMessages.getMessage("usuario.lotacao"));
						}
					} else {
						throw new AplicacaoException("Usuário não possui permissão para Alterar o Órgão da Pessoa.");
					}
				}
				
				pessoa.setIdInicial(pessoaAnt.getIdInicial());
				pessoa.setMatricula(pessoaAnt.getMatricula());
			}
		}
		
		int i = CpDao.getInstance().consultarQtdePorEmailIgualCpfDiferente(Texto.removerEspacosExtra(email).trim().replace(" ", ""),
				Long.valueOf(cpf.replace("-", "").replace(".", "").trim()), pessoaAnt.getIdPessoaIni() != null ? pessoaAnt.getIdPessoaIni() : 0);
		if (i > 0) {
			throw new AplicacaoException("E-mail informado está cadastrado para outro CPF");
		}

		
		Date data = new Date(System.currentTimeMillis());
		pessoa.setDataInicio(data);
		pessoa.setMatricula(0L);
		pessoa.setSituacaoFuncionalPessoa(SituacaoFuncionalEnum.APENAS_ATIVOS.getValor()[0]);
		pessoa.setNomeExibicao(nomeExibicao);
		
		if (dtNascimento != null && !"".equals(dtNascimento)) {
			Date dtNasc = new Date();
			dtNasc = SigaCalendar.converteStringEmData(dtNascimento);

			Calendar hj = Calendar.getInstance();
			Calendar dtNasci = new GregorianCalendar();
			dtNasci.setTime(dtNasc);

			if (hj.before(dtNasci)) {
				throw new AplicacaoException("Data de nascimento inválida");
			}
			pessoa.setDataNascimento(dtNasc);
		} else {
			pessoa.setDataNascimento(null);
		}
		
		if (dataExpedicaoIdentidade != null && !"".equals(dataExpedicaoIdentidade)) {
			Date dtExp = new Date();
			dtExp = SigaCalendar.converteStringEmData(dataExpedicaoIdentidade);

			Calendar hj = Calendar.getInstance();
			Calendar dtExpId = new GregorianCalendar();
			dtExpId.setTime(dtExp);

			if (hj.before(dtExpId)) {
				throw new AplicacaoException("Data de expedição inválida");
			}
			pessoa.setDataExpedicaoIdentidade(dtExp);
		} else {
			pessoa.setDataExpedicaoIdentidade(null);
		}
		
		
		pessoa.setIdentidade(identidade);
		pessoa.setOrgaoIdentidade(orgaoIdentidade);
		pessoa.setUfIdentidade(ufIdentidade);		
		

		pessoa.setNomePessoa(Texto.removerEspacosExtra(nmPessoa).trim());
		pessoa.setCpfPessoa(Long.valueOf(cpf.replace("-", "").replace(".", "")));
		pessoa.setEmailPessoa(Texto.removerEspacosExtra(email).trim().replace(" ", "").toLowerCase());
		
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		DpCargo cargo = new DpCargo();
		DpFuncaoConfianca funcao = new DpFuncaoConfianca();
		DpLotacao lotacao = new DpLotacao();

		ou.setIdOrgaoUsu(idOrgaoUsu);
		ou = CpDao.getInstance().consultarPorId(ou);
		
		if (identidadeCadastrante != null) {
			if (!"ZZ".equals(identidadeCadastrante.getCpOrgaoUsuario().getSigla())){
				if (!ou.getIdOrgaoUsu().equals(identidadeCadastrante.getCpOrgaoUsuario().getIdOrgaoUsu()) && !podeAlterarOrgaoPessoa) {
					throw new AplicacaoException("Usuário não pode cadastrar nesse órgão.");
				}
			}
		}
				
		cargo = CpDao.getInstance().consultar(idCargo, DpCargo.class, false);
		if (!ou.getIdOrgaoUsu().equals(cargo.getOrgaoUsuario().getIdOrgaoUsu())) {
			throw new AplicacaoException("Cargo informado não pertence ao órgão informado.");
		}
		
		lotacao = CpDao.getInstance().consultar(idLotacao, DpLotacao.class, false);
		if (!ou.getIdOrgaoUsu().equals(lotacao.getOrgaoUsuario().getIdOrgaoUsu())) {
			throw new AplicacaoException("Lotação informada não pertence ao órgão informado.");
		}
		
		if (idFuncao != null && idFuncao != 0) {
			funcao  = CpDao.getInstance().consultar(idFuncao, DpFuncaoConfianca.class, false);
			
			if (!ou.getIdOrgaoUsu().equals(funcao.getOrgaoUsuario().getIdOrgaoUsu())) {
				throw new AplicacaoException("Função de Confiança informada não pertence ao órgão informado.");
			}
		}

		pessoa.setOrgaoUsuario(ou);
		pessoa.setCargo(cargo);
		pessoa.setLotacao(lotacao);
		if (idFuncao != null && idFuncao != 0) {
			pessoa.setFuncaoConfianca(funcao);
		} else {
			pessoa.setFuncaoConfianca(null);
		}
		pessoa.setSesbPessoa(ou.getSigla());
		
		// ÓRGÃO / CARGO / FUNÇÃO DE CONFIANÇA / LOTAÇÃO e CPF iguais.
		DpPessoaDaoFiltro dpPessoa = new DpPessoaDaoFiltro();
		dpPessoa.setIdOrgaoUsu(pessoa.getOrgaoUsuario().getId());
		dpPessoa.setCargo(pessoa.getCargo());
		dpPessoa.setFuncaoConfianca(pessoa.getFuncaoConfianca());
		dpPessoa.setLotacao(pessoa.getLotacao());
		dpPessoa.setCpf(pessoa.getCpfPessoa());
		dpPessoa.setNome("");
		dpPessoa.setId(id);

		dpPessoa.setBuscarFechadas(Boolean.FALSE);
		Integer tamanho = CpDao.getInstance().consultarQuantidade(dpPessoa);

		if (tamanho > 0) {
			throw new AplicacaoException("Usuário já cadastrado com estes dados: Órgão, Cargo, Função, Unidade e CPF");
		}
		
		List<DpPessoa> listaPessoasMesmoCPF = new ArrayList<DpPessoa>();
		DpPessoa pessoa2 = new DpPessoa();
	
		listaPessoasMesmoCPF.addAll(CpDao.getInstance().listarCpfAtivoInativo(pessoa.getCpfPessoa()));
		
		try {
	//		dao().em().getTransaction().begin();

			if(pessoaAnt != null && pessoaAnt.getId() != null) {
				pessoa.setMatricula(pessoaAnt.getMatricula());
				pessoa.setIdePessoa(pessoaAnt.getIdePessoa());
				if(pessoaAnt.getDataFimPessoa() != null) {
					pessoa.setDataFimPessoa(pessoaAnt.getDataFimPessoa());
				}
				CpIdentidade ident = null;
				
				if(!pessoa.getOrgaoUsuario().equivale(pessoaAnt.getOrgaoUsuario())) {
					ident = new CpIdentidade();
					CpIdentidade identAnt = new CpIdentidade();
					identAnt = CpDao.getInstance().consultaIdentidadeCadastrante(pessoaAnt.getSesbPessoa() + pessoaAnt.getMatricula(), true);
					PropertyUtils.copyProperties(ident, identAnt);
					ident.setCpOrgaoUsuario(pessoa.getOrgaoUsuario());
					ident.setNmLoginIdentidade(pessoa.getSesbPessoa() + pessoa.getMatricula());
					ident.setDtCriacaoIdentidade(data);
					ident.setId(null);
					CpDao.getInstance().gravarComHistorico(ident, identAnt, data , identidadeCadastrante);
				}
				CpDao.getInstance().gravarComHistorico(pessoa, pessoaAnt, data , identidadeCadastrante);
				if(ident != null) {
					ident.setDpPessoa(pessoa);
					CpDao.getInstance().gravar(ident);
				}
				
				if (ident != null && !pessoa.getOrgaoUsuario().equivale(pessoaAnt.getOrgaoUsuario())) {
					this.desativarConfiguracoesPessoa(identidadeCadastrante, pessoaAnt.getPessoaInicial() != null ? pessoaAnt.getPessoaInicial() : pessoaAnt, data);
					
					//enviar e-mail informado alteracao de orgao
					if (SigaMessages.isSigaSP()) {
						String[] destinanarios = { pessoa.getEmailPessoaAtual() };
						Correio.enviar(null, destinanarios, "Alteração Usuário", "", textoEmailAltOrgaoUsuarioSP(ident, ident.getNmLoginIdentidade()));
					}
				}
			} else {
				pessoa.setHisIdcIni(identidadeCadastrante);
				CpDao.getInstance().gravar(pessoa);
				
				pessoa.setIdPessoaIni(pessoa.getId());
				pessoa.setIdePessoa(pessoa.getId().toString());
				pessoa.setMatricula(10000 + pessoa.getId());
				pessoa.setIdePessoa(pessoa.getMatricula().toString());
				CpDao.getInstance().gravar(pessoa);

				lista = CpDao.getInstance()
						.consultaIdentidadesPorCpf(cpf.replace(".", "").replace("-", ""));
				CpIdentidade usu = null;
				if (lista.size() > 0) {
					CpIdentidade usuarioExiste = lista.get(0);
					usu = new CpIdentidade();
					usu.setCpTipoIdentidade(CpDao.getInstance().consultar(1, CpTipoIdentidade.class, false));
					usu.setDscSenhaIdentidade(usuarioExiste.getDscSenhaIdentidade());
					usu.setPinIdentidade(usuarioExiste.getPinIdentidade());
					usu.setDtCriacaoIdentidade(data);
					usu.setCpOrgaoUsuario(ou);
					usu.setHisDtIni(usu.getDtCriacaoIdentidade());
					usu.setHisAtivo(1);
				}

				if (usu != null) {
					usu.setNmLoginIdentidade(pessoa.getSesbPessoa() + pessoa.getMatricula());
					usu.setDpPessoa(pessoa);
					CpDao.getInstance().gravarComHistorico(usu, identidadeCadastrante);
				}
			}
			
			for (DpPessoa dpPessoaAnt2 : listaPessoasMesmoCPF) {
				if (dpPessoaAnt2.getNomePessoa().equalsIgnoreCase(pessoa.getNomePessoa())) continue;
				if(!dpPessoaAnt2.getId().equals(id)) {
					pessoa2 = new DpPessoa();
					pessoa2.setNomePessoa(pessoa.getNomePessoa());
					pessoa2.setOrgaoUsuario(dpPessoaAnt2.getOrgaoUsuario());
					pessoa2.setLotacao(dpPessoaAnt2.getLotacao());
					pessoa2.setFuncaoConfianca(dpPessoaAnt2.getFuncaoConfianca());
					pessoa2.setMatricula(dpPessoaAnt2.getMatricula());
					pessoa2.setCpfPessoa(dpPessoaAnt2.getCpfPessoa());
					pessoa2.setCargo(dpPessoaAnt2.getCargo());
					pessoa2.setIdePessoa(dpPessoaAnt2.getIdePessoa());
					pessoa2.setEmailPessoa(dpPessoaAnt2.getEmailPessoa());
					pessoa2.setDataFimPessoa(data);
					pessoa2.setSituacaoFuncionalPessoa(dpPessoaAnt2.getSituacaoFuncionalPessoa());
					pessoa2.setSesbPessoa(dpPessoaAnt2.getSesbPessoa());
					pessoa2.setDataFimPessoa(dpPessoaAnt2.getDataFimPessoa());
					pessoa2.setDataNascimento(dpPessoaAnt2.getDataNascimento());
					pessoa2.setIdPessoaIni(dpPessoaAnt2.getIdPessoaIni());
					CpDao.getInstance().gravarComHistorico(pessoa2, dpPessoaAnt2, data, identidadeCadastrante);
				}
			}
			
			if(enviarEmail != null && idOrgaoUsu != null && cpf != null && lista != null && lista.size() == 0) {
				Cp.getInstance().getBL().criarIdentidade(pessoa.getSesbPessoa() + pessoa.getMatricula(),
						pessoa.getCpfFormatado(), identidadeCadastrante, null, new String[1], Boolean.FALSE);
			}
		
			return pessoa;
		} catch (final Exception e) {
			if (e.getCause() instanceof ConstraintViolationException 
					&& ((ConstraintViolationException) e.getCause()).getConstraintName().toUpperCase().contains("DP_PESSOA_UNIQUE_PESSOA_ATIVA")) {
				throw new AplicacaoException("Ocorreu um problema no cadastro da pessoa.");
			} else if (e.getCause() instanceof ConstraintViolationException 
					&& ((ConstraintViolationException) e.getCause()).getConstraintName().toUpperCase().contains("PESSOA_ATIVA_CHAVES_UNICA")) {
				throw new AplicacaoException(
						"Pessoa já cadastrada e ativa com estes dados: Órgão, Cargo, Função, Unidade e CPF");
			} else {
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		}
	}
	
	public void gravarOrgaoUsuario(CpOrgaoUsuario orgaoNovo, CpOrgaoUsuario orgaoAntigo, CpIdentidade identidadeCadastrante) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		CpDao.getInstance().gravarComHistorico(orgaoNovo, orgaoAntigo, null, identidadeCadastrante);
		
		if(orgaoAntigo != null) {
			DpCargoDaoFiltro dpCargo = new DpCargoDaoFiltro();
			dpCargo.setIdOrgaoUsu(orgaoAntigo.getId());
			dpCargo.setBuscarInativos(true);
			List<DpCargo> listaCargo = CpDao.getInstance().consultarPorFiltro(dpCargo, 0, 0);
			
			DpFuncaoConfiancaDaoFiltro dpFuncao = new DpFuncaoConfiancaDaoFiltro();
			dpFuncao.setIdOrgaoUsu(orgaoAntigo.getId());
			dpFuncao.setBuscarInativas(true);
			List<DpFuncaoConfianca> listaFuncao = CpDao.getInstance().consultarPorFiltro(dpFuncao, 0, 0);
			
			DpLotacaoDaoFiltro dpLotacao = new DpLotacaoDaoFiltro();
			dpLotacao.setIdOrgaoUsu(orgaoAntigo.getId());
			dpLotacao.setBuscarFechadas(true);
			List<DpLotacao> listaLotacao = CpDao.getInstance().consultarPorFiltro(dpLotacao, 0, 0);
			
			DpPessoaDaoFiltro dpPessoa = new DpPessoaDaoFiltro();
			dpPessoa.setIdOrgaoUsu(orgaoAntigo.getId());
			dpPessoa.setBuscarFechadas(true);
			dpPessoa.setNome("");
			List<DpPessoa> listaPessoa = CpDao.getInstance().consultarPorFiltro(dpPessoa, 0, 0);
			
			HashMap<DpPessoa, DpPessoa> hashPessoa = new HashMap<DpPessoa, DpPessoa>();
			
			if(listaPessoa != null) {
				for (DpPessoa pessoa : listaPessoa) {
					DpPessoa pessoaNovo = new DpPessoa();
					copiarPessoa(pessoa,pessoaNovo);
					pessoaNovo.setOrgaoUsuario(orgaoNovo);
					hashPessoa.put(pessoa, pessoaNovo);
				}
			}

			
			for (DpCargo cargo : listaCargo) {
				DpCargo cargoNovo = new DpCargo();
				PropertyUtils.copyProperties(cargoNovo, cargo); 
				cargoNovo.setId(null);
				cargoNovo.setOrgaoUsuario(orgaoNovo);
				cargoNovo.setDataFim(cargo.getDataFim() != null ? CpDao.getInstance().consultarDataEHoraDoServidor() : null);
				cargoNovo.setDataInicio(CpDao.getInstance().consultarDataEHoraDoServidor());
				if(cargo.getDataFim() == null) {
					CpDao.getInstance().gravarComHistorico(cargoNovo, cargo, null, identidadeCadastrante);
				} else {
					CpDao.getInstance().gravarComHistorico(cargoNovo, identidadeCadastrante);
				}
				
				
				hashPessoa.forEach((key, value) -> {
					
					if(key.getCargo().getIdCargoIni().equals(cargo.getIdCargoIni())) {	
						DpPessoa p = new DpPessoa();
						copiarPessoa(value,p);
						p.setCargo(cargo.getCargoAtual());
						hashPessoa.put(key, p);
					}
				});
				
			}
			
			for (DpFuncaoConfianca funcao : listaFuncao) {
				DpFuncaoConfianca funcaoNovo = new DpFuncaoConfianca();
				PropertyUtils.copyProperties(funcaoNovo, funcao); 
				funcaoNovo.setOrgaoUsuario(orgaoNovo);
				funcaoNovo.setId(null);
				funcaoNovo.setDataFim(funcao.getDataFim() != null ? CpDao.getInstance().consultarDataEHoraDoServidor() : null);
				funcaoNovo.setDataInicio(CpDao.getInstance().consultarDataEHoraDoServidor());
				if(funcao.getDataFim() == null) {
					CpDao.getInstance().gravarComHistorico(funcaoNovo, funcao, null, identidadeCadastrante);
				} else {
					CpDao.getInstance().gravarComHistorico(funcaoNovo, identidadeCadastrante);
				}
				
				hashPessoa.forEach((key, value) -> {
					DpPessoa p = new DpPessoa();
					copiarPessoa(value,p);
					if(p.getFuncaoConfianca() != null && p.getFuncaoConfianca().getIdFuncaoIni().equals(funcao.getIdFuncaoIni())) {
						
						p.setFuncaoConfianca(funcao.getFuncaoConfiancaAtual());
						hashPessoa.put(key, p);
					}
				});
				
			}
			
			for (DpLotacao lotacao : listaLotacao) {
				DpLotacao lotacaoNovo = new DpLotacao();
				PropertyUtils.copyProperties(lotacaoNovo, lotacao); 
				lotacaoNovo.setOrgaoUsuario(orgaoNovo);
				lotacaoNovo.setId(null);
				lotacaoNovo.setDataFim(lotacao.getDataFim() != null ? CpDao.getInstance().consultarDataEHoraDoServidor() : null);
				lotacaoNovo.setDataInicio(CpDao.getInstance().consultarDataEHoraDoServidor());
				lotacaoNovo.setDpLotacaoSubordinadosSet(null);
				lotacaoNovo.setDpPessoaLotadosSet(null);
				lotacaoNovo.setLotacoesPosteriores(null);
				
				if(lotacao.getDataFim() == null) {
					CpDao.getInstance().gravarComHistorico(lotacaoNovo, lotacao, null, identidadeCadastrante);
				} else {
					CpDao.getInstance().gravarComHistorico(lotacaoNovo, identidadeCadastrante);
				}
				
				hashPessoa.forEach((key, value) -> {
					if(key.getLotacao().getIdLotacaoIni().equals(lotacao.getIdLotacaoIni())) {
						DpPessoa p = new DpPessoa();
						copiarPessoa(value,p);
						p.setLotacao(lotacao.getLotacaoAtual());
						p.setHisDtFim(key.getHisDtFim() != null ? CpDao.getInstance().consultarDataEHoraDoServidor() : null);
						p.setHisDtIni(CpDao.getInstance().consultarDataEHoraDoServidor());
						p.setHisIdcFim(key.getHisIdcFim());
						p.setOrgaoUsuario(orgaoNovo);
						if(orgaoAntigo != null && !orgaoNovo.getSigla().equals(orgaoAntigo.getSigla())) {
							p.setSesbPessoa(orgaoNovo.getSigla());
						}
						hashPessoa.put(key, p);
					}
				});
				
			}
						
			hashPessoa.forEach((key, value) -> {
				if(orgaoAntigo != null && !orgaoNovo.getSigla().equals(orgaoAntigo.getSigla())) {
					CpIdentidade ident = new CpIdentidade();
					CpIdentidade identAnt = new CpIdentidade();
					identAnt = CpDao.getInstance().consultarIdentidadeAtual(key.getSesbPessoa() + key.getMatricula());
					
					if(identAnt != null) {
						CpDao.getInstance().gravarComHistorico(value, key, null, identidadeCadastrante);
						
						try {
							PropertyUtils.copyProperties(ident, identAnt);
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ident.setCpOrgaoUsuario(orgaoNovo);
						ident.setNmLoginIdentidade(value.getSesbPessoa() + value.getMatricula());
						ident.setId(null);
						ident.setHisDtFim(identAnt.getHisDtFim() != null ? CpDao.getInstance().consultarDataEHoraDoServidor() : null);
						ident.setHisDtIni(CpDao.getInstance().consultarDataEHoraDoServidor());
						if(identAnt.getHisDtFim() == null) {
							CpDao.getInstance().gravarComHistorico(ident, identAnt, null , identidadeCadastrante);
						} else {
							CpDao.getInstance().gravarComHistorico(ident, identidadeCadastrante);
						}
						
					}
				} else {
					CpDao.getInstance().gravarComHistorico(value, key, null, identidadeCadastrante);
				}
			});
		}
	}
	
	public void desativarConfiguracoesPessoa(CpIdentidade identidadeCadastrante, DpPessoa pessoa, Date dataAtual) {
		List<CpConfiguracao> listConfig = CpDao.getInstance().consultarCpConfiguracoesPorPessoa(pessoa.getId());
			
		for (CpConfiguracao cpConfiguracao : listConfig) {
			cpConfiguracao.setDtFimVigConfiguracao(dataAtual);
			cpConfiguracao.setHisDtFim(dataAtual);
			CpDao.getInstance().gravarComHistorico(cpConfiguracao, identidadeCadastrante);	
		}
	}
	
	public void ativarInativarOrgaoUsuario(CpOrgaoUsuario orgaoUsuario, String marco, String dataAlteracao, CpIdentidade identCadastrante) {
		
		
		if(!Data.validaDDMMYYYY(dataAlteracao)) {
			throw new AplicacaoException("Data inválida!");
		}
		
		
		if(orgaoUsuario.getDtFim() == null) {
			//inativar
			DpPessoaDaoFiltro dpPessoa = new DpPessoaDaoFiltro();
			dpPessoa.setIdOrgaoUsu(orgaoUsuario.getId());
			dpPessoa.setBuscarFechadas(false);
			dpPessoa.setNome("");
			
			DpLotacaoDaoFiltro dpLotacao = new DpLotacaoDaoFiltro();
			dpLotacao.setIdOrgaoUsu(orgaoUsuario.getId());
			dpLotacao.setBuscarFechadas(false);
			
			if(dao().consultarQuantidade(dpPessoa) > 0 || dao().consultarQuantidade(dpLotacao) > 0) {
				throw new AplicacaoException("Órgão: "+ orgaoUsuario.getDescricao() +" <br>Possui " + SigaMessages.getMessage("usuario.lotacao") + " e/ou Usuário ativo. A ação solicitada não será realizada");
			}
			
			orgaoUsuario.setDtFim(CpDao.getInstance().consultarDataEHoraDoServidor());
			orgaoUsuario.setMarcoRegulatorio(marco);
			orgaoUsuario.setDataAlteracao(Data.parse(dataAlteracao));
			CpDao.getInstance().gravarComHistorico(orgaoUsuario, identCadastrante);
			
		} else {
			//ativar
			CpOrgaoUsuario orgaoNovo = new CpOrgaoUsuario();
			try {
				PropertyUtils.copyProperties(orgaoNovo, orgaoUsuario);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			orgaoNovo.setDtFim(null);
			orgaoNovo.setHisIdcFim(null);
			orgaoNovo.setHisAtivo(1);
			orgaoNovo.setHisDtIni(CpDao.getInstance().consultarDataEHoraDoServidor());
			orgaoNovo.setHisIdcIni(identCadastrante);
			orgaoNovo.setId(null);
			orgaoNovo.setMarcoRegulatorio(marco);
			orgaoNovo.setDataAlteracao(Data.parse(dataAlteracao));
			CpDao.getInstance().gravarComHistorico(orgaoNovo, identCadastrante);
		}
	}
		
	public String inativarUsuario(final Long idUsuario) {
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		DpPessoa pessoa = dao().consultar(idUsuario, DpPessoa.class, false);
		ou.setIdOrgaoUsu(pessoa.getOrgaoUsuario().getId());
		ou = CpDao.getInstance().consultarPorId(ou);

		pessoa = dao().consultar(idUsuario, DpPessoa.class, false);
		// inativar
		if (pessoa.getDataFimPessoa() == null || "".equals(pessoa.getDataFimPessoa())) {
			Calendar calendar = new GregorianCalendar();
			Date date = new Date();
			calendar.setTime(date);
			pessoa.setDataFimPessoa(calendar.getTime());

		}

		try {
			dao().iniciarTransacao();
			dao().gravar(pessoa);
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}

		return "Usuário inativado com sucesso: " + pessoa.getSesbPessoa() + pessoa.getMatricula();
	}
	
	
	public CpToken gerarUrlPermanente(Long idRef) {
		try {
			CpToken sigaUrlPermanente = new CpToken();
			sigaUrlPermanente = dao().obterCpTokenPorTipoIdRef(CpToken.TOKEN_URLPERMANENTE,idRef); //Se tem token não expirado, devolve token
			if (sigaUrlPermanente == null ) {
				sigaUrlPermanente = new CpToken();
				//Seta tipo 1 - Token para URL Permamente
				sigaUrlPermanente.setIdTpToken(CpToken.TOKEN_URLPERMANENTE);
				
				sigaUrlPermanente.setToken(SigaUtil.randomAlfanumerico(128));
				sigaUrlPermanente.setIdRef(idRef);
				
				Date dt = dao().consultarDataEHoraDoServidor();
				
				sigaUrlPermanente.setDtIat(dt);

				try {
					dao().gravar(sigaUrlPermanente);
				} catch (final Exception e) {
					throw new AplicacaoException("Erro na gravação", 0, e);
				}
			} 
			return sigaUrlPermanente;

			
		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao gerar o Token.", 0, e);
		}
	}
		
	
	public String obterURLPermanente(String tipoLink, String token) {
		String urlPermanente = Prop.get("/sigaex.autenticidade.url").replace("/sigaex/public/app/autenticar", "");
		
		urlPermanente +=  "/siga/public/app/sigalink/"+tipoLink+"/"+token;
		
		return urlPermanente;
	}

	public void gravarMarcador(final Long id, final DpPessoa cadastrante, final DpLotacao lotacao, final CpIdentidade identidade, 
			final String descricao, final String descrDetalhada, final CpMarcadorCorEnum idCor, final CpMarcadorIconeEnum idIcone, final CpMarcadorGrupoEnum grupoId, 
			final CpMarcadorFinalidadeEnum idFinalidade, final String dataAtivacao) throws Exception {
		if (idFinalidade == CpMarcadorFinalidadeEnum.SISTEMA)
			throw new AplicacaoException ("Não é permitido o cadastro de marcadores de sistema.");
			
		if (descricao == null)
			throw new AplicacaoException ("Preencha o nome do marcador.");
			
		if (descricao.length() > 40) 
			throw new AplicacaoException ("Nome do marcador tem mais de 40 bytes.");
		
		String msgLotacao = SigaMessages.getMessage("usuario.lotacao");
		List<CpMarcador> listaMarcadoresLotacaoEGerais = dao().listarCpMarcadoresPorLotacaoEGeral(lotacao, true);
		
		int c = 0, cpp = 0;
		for (CpMarcador m : listaMarcadoresLotacaoEGerais) {
			if (id != null && id.equals(m.getId()))
				continue;
			if (m.getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO) 
				c++;
			if (m.getIdFinalidade() == CpMarcadorFinalidadeEnum.PASTA_PADRAO) 
				cpp++;
		}
		Integer qtdMaxPorUnidade = Prop.getInt("/siga.marcadores.qtd.maxima.por.unidade");
		if (idFinalidade.getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO && id == null 
				&& c > qtdMaxPorUnidade) 
			throw new AplicacaoException ("Atingiu o limite de " + qtdMaxPorUnidade.toString() 
				+ " marcadores possíveis para " + msgLotacao);
		
		if (idFinalidade == CpMarcadorFinalidadeEnum.PASTA_PADRAO && id == null && cpp > 0) 
			throw new AplicacaoException ("Só é permitido criar uma pasta padrão");
		
		Date dtAtivacao = null;
		if (dataAtivacao != null)
			dtAtivacao = Data.parse(dataAtivacao);

		if ((listaMarcadoresLotacaoEGerais.stream()
				.filter(mar -> mar.getDescrMarcador()
						.equals(descricao)).count() > 0) && id == null) 
			throw new AplicacaoException ("Já existe um marcador Geral ou da " + msgLotacao 
					+ " com este nome: " + descricao);

		if (id != null) {
			CpMarcador marcadorAnt = new CpMarcador();
			CpMarcador marcador = new CpMarcador();

			marcadorAnt = dao().consultar(id, CpMarcador.class, false);
			if (marcadorAnt != null) {
				if (marcadorAnt.getDpLotacaoIni() != null &&
						!(marcadorAnt.getDpLotacaoIni().getIdInicial().equals(cadastrante.getLotacao().getIdInicial())))
					throw new AplicacaoException ("Não é permitida a alteração de marcador de outra " + msgLotacao);

				marcador.setHisIdIni(marcadorAnt.getHisIdIni());
				marcador.setIdFinalidade(idFinalidade);
				marcador.setOrdem(marcadorAnt.getOrdem());
				marcador.setDpLotacaoIni(idFinalidade.getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO ? lotacao.getLotacaoInicial() : null);
				marcador.setDescrMarcador(descricao);
				marcador.setDescrDetalhada(descrDetalhada);
				marcador.setIdGrupo(grupoId);
				marcador.setIdCor(idCor);
				marcador.setIdIcone(idIcone);
				marcador.setListavelPesquisaDefault(true);
				dao().gravarComHistorico(marcador, marcadorAnt, dtAtivacao, identidade);
			} else {
				throw new AplicacaoException ("Marcador não existente para esta " + msgLotacao 
						+ " (" + id.toString() + ").");
			}
		} else {
			Integer ordem = c + 1; 
			CpMarcador marcador = new CpMarcador();
			marcador.setIdFinalidade(idFinalidade);
			marcador.setDescrMarcador(descricao);
			marcador.setDescrDetalhada(descrDetalhada);
			marcador.setIdGrupo(grupoId);
			marcador.setIdCor(idCor);
			marcador.setIdIcone(idIcone);
			marcador.setDpLotacaoIni(idFinalidade.getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO ? lotacao.getLotacaoInicial() : null);
			marcador.setOrdem(ordem);
			marcador.setListavelPesquisaDefault(true);
			dao().gravarComHistorico(marcador, null, dtAtivacao, identidade);
		}
	}
	
	public void definirPinIdentidade( List<CpIdentidade> listaIdentidades, String pin, CpIdentidade idCadastrante)
			throws NoSuchAlgorithmException, AplicacaoException {

		try {
			boolean podeTrocar = Cp.getInstance().getComp().podeSegundoFatorPin(idCadastrante.getPessoaAtual(), idCadastrante.getPessoaAtual().getLotacao());
			
			if (podeTrocar) {
				Date dt = dao().consultarDataEHoraDoServidor();
				final String pinHash = GeraMessageDigest.calcSha256(pin);

				CpIdentidade i = null;
				for (CpIdentidade cpIdentidade : listaIdentidades) {
					i = new CpIdentidade();
					PropertyUtils.copyProperties(i, cpIdentidade);
					i.setIdIdentidade(null);
					i.setDtCriacaoIdentidade(dt);
					i.setPinIdentidade(pinHash);
					i.setPinContadorTentativa(INTEGER_ZERO);
					dao().gravarComHistorico(i, cpIdentidade, dt, idCadastrante);
				}
			} else {
				throw new AplicacaoException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
			}
		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
		}
	}
	
	public Boolean consisteFormatoPin(String pin) throws RegraNegocioException {
		boolean formatoPinIsValido = false;
		
		if (pin.isEmpty()) {
			throw new RegraNegocioException("PIN não informado.");
		}
		
		if (!SigaUtil.isNumeric(pin)) {
			throw new RegraNegocioException("PIN deve conter apenas dígitos númericos (0-9).");
		}
		
		if (pin.length() != CpIdentidade.PIN_LENGTH) {
			throw new RegraNegocioException("PIN deve ter "+String.valueOf(CpIdentidade.PIN_LENGTH)+" dígitos numéricos.");
		}	
				
		formatoPinIsValido = true;
		
		return formatoPinIsValido;
	}
	
	public Boolean consisteFormatoSenha(String senhaNova) throws RegraNegocioException {
		boolean formatoSenhaValido = false;
		final int SIZE_MIN_SENHA = 6;
		
		if (senhaNova.isEmpty()) {
			throw new RegraNegocioException("Senha não informada.");
		}

		if (!SigaUtil.validacaoFormatoSenha(senhaNova)) {
			throw new RegraNegocioException("Senha inválida. Deve conter pelo menos 6 caracteres sendo eles maiúsculos, minúsculos e números para aumentar a força da senha.");
		}	
				
		formatoSenhaValido = true;
		
		return formatoSenhaValido;
	}
	
	public Boolean validaPinIdentidade(String pin,CpIdentidade identidadeCadastrante) throws Exception {
		
		boolean pinValido = false;
		
		if (!Cp.getInstance().getComp().podeSegundoFatorPin(identidadeCadastrante.getDpPessoa(), identidadeCadastrante.getDpPessoa().getLotacao())) {
			throw new RegraNegocioException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
		}
				
		if (identidadeCadastrante.getPinIdentidade() == null) {
			throw new RegraNegocioException("Não é possível validar PIN: Não existe chave cadastrada.");
		}
		
		if (hasBloqueioPinPorTentativa(identidadeCadastrante)) {
			throw new RegraNegocioException("Seu PIN está <b>bloqueado</b> por tentativas malsucedidas. "
					+ "Efetue a recuperação do PIN em <b><a href='/siga/app/pin/reset'>Esqueci meu PIN</a></b>.");
		}
		
		consisteFormatoPin(pin);
		pinValido = validaHashPin(pin,identidadeCadastrante);

		if (!pinValido) {
			incrementarContagemTentativasMalsucedidasPin(identidadeCadastrante);
			
			if (hasBloqueioPinPorTentativa(identidadeCadastrante)) { 
				throw new RegraNegocioException("Seu PIN foi <b>bloqueado</b> por tentativas malsucedidas. "
						+ "Efetue a recuperação do PIN em <b><a href='/siga/app/pin/reset'>Esqueci meu PIN</a></b>.");
			} else {
				throw new RegraNegocioException(
						String.format("PIN informado não coincide com o cadastrado.<br />"
							+ "Você tem mais <b>%s tentativa%s</b> antes que seu PIN seja bloqueado por segurança.",
								quantidadeTentativasRestantes(identidadeCadastrante),
								quantidadeTentativasRestantes(identidadeCadastrante) < 2 ? "" : "s")
						);
			}

		} else if (identidadeCadastrante.getPinContadorTentativa().intValue() != INTEGER_ZERO) {
			resetContagemTentativasMalsucedidasPin(identidadeCadastrante);
		}
	
		return pinValido;
	}
	

	private Boolean validaHashPin(String pin, CpIdentidade identidade) throws RegraNegocioException, NoSuchAlgorithmException {
		String hashPinAValidar = null;
		boolean hashPinIsValido = false;
		
		if (identidade == null) {
			throw new RegraNegocioException("Não é possível validar PIN: Identidade não informada.");
		}

		hashPinAValidar = GeraMessageDigest.calcSha256(pin);	
		hashPinIsValido = hashPinAValidar.equals(identidade.getPinIdentidade());
		
		return hashPinIsValido;
	}
	
	
	public CpToken gerarTokenResetPin(Long cpf) {
		try {
			
			/* Invalidar se existir token ativo */
			invalidarTokenAtivo(CpToken.TOKEN_PIN,cpf);

			CpToken tokenResetPin = new CpToken();
			
			//Seta tipo 2 - Token para Reset PIN
			tokenResetPin.setIdTpToken(CpToken.TOKEN_PIN);
			tokenResetPin.setToken(SigaUtil.randomAlfanumericoSeletivo(8));
			tokenResetPin.setIdRef(cpf);
			
			/* HORA ATUAL */
			GregorianCalendar gc = new GregorianCalendar();
			Date dt = dao().consultarDataEHoraDoServidor();
			gc.setTime(dt);

			
			/* EXP - Expiração do Token */
			gc.add(Calendar.HOUR, 1);
			tokenResetPin.setDtExp(gc.getTime());	

			try {
				dao().gravar(tokenResetPin);
			} catch (final Exception e) {

				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		
			return tokenResetPin;

			
		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao gerar o Token.", 0, e);
		}
	}
	
	public CpToken gerarTokenResetSenha(Long cpf) {
		try {
			
			/* Invalidar se existir token ativo */
			invalidarTokenAtivo(CpToken.TOKEN_SENHA,cpf);

			CpToken tokenResetSenha = new CpToken();
			
			tokenResetSenha.setIdTpToken(CpToken.TOKEN_SENHA);
			tokenResetSenha.setToken(SigaUtil.randomAlfanumericoSeletivo(8));
			tokenResetSenha.setIdRef(cpf);
			
			/* HORA ATUAL */
			GregorianCalendar gc = new GregorianCalendar();
			Date dt = dao().consultarDataEHoraDoServidor();
			gc.setTime(dt);

			
			/* EXP - Expiração do Token */
			gc.add(Calendar.HOUR, 1);
			tokenResetSenha.setDtExp(gc.getTime());	

			try {
				dao().gravar(tokenResetSenha);
			} catch (final Exception e) {

				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		
			return tokenResetSenha;

			
		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao gerar o Token.", 0, e);
		}
	}

	public CpToken gravarNovoToken(Long idTpToken, Long idRef, int tokenExpCalendarField,
								   int tokenExpCalendarFieldAmount, String token) {
		
		invalidarTokenAtivo(idTpToken, idRef);

		CpToken cpToken = new CpToken();

		cpToken.setIdTpToken(idTpToken);
		cpToken.setToken(token);
		cpToken.setIdRef(idRef);

		/* HORA ATUAL */
		GregorianCalendar gc = new GregorianCalendar();
		Date dt = dao().consultarDataEHoraDoServidor();
		gc.setTime(dt);
		cpToken.setDtIat(dt);

		/* EXP - Expiração do Token */
		gc.add(tokenExpCalendarField, tokenExpCalendarFieldAmount);
		cpToken.setDtExp(gc.getTime());

		try {
			dao().gravar(cpToken);
		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação do token", 0, e);
		}

		return cpToken;

	}
	
	/**** Controle de Validade Token ****/
	public Boolean isTokenValido(Long tipoToken, Long cpf, String token) {
		Boolean isValido = false;
		try {
			CpToken tokenResetPin = new CpToken();
			tokenResetPin = dao().obterCpTokenPorTipoToken(tipoToken,token); 
			if (tokenResetPin != null ) {
				if (cpf.equals(tokenResetPin.getIdRef())) {
					Date dt = dao().consultarDataEHoraDoServidor();
					LocalDateTime dtNow = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
					LocalDateTime dtExp = LocalDateTime.ofInstant(tokenResetPin.getDtExp().toInstant(), ZoneId.systemDefault());
					
					isValido = dtNow.isBefore(dtExp);			
				}
			}

			return isValido;
			
		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao validar o Token.", 0, e);
		}
	}
	
	public void invalidarTokenAtivo(Long tipoToken, Long idRef) {
		CpToken cpToken = dao().obterCpTokenPorTipoIdRef(tipoToken,idRef);
		if (cpToken != null) {
			cpToken.setDtExp(dao().consultarDataEHoraDoServidor());
			try {
				dao().gravar(cpToken);
			} catch (final Exception e) {
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		} 
	}

	public void invalidarTokenUtilizado(Long tipoToken, String token) {
		try {
			CpToken cpToken = dao().obterCpTokenPorTipoToken(tipoToken, token);
			if (cpToken != null) {
				cpToken.setDtExp(dao().consultarDataEHoraDoServidor());
				try {
					dao().gravar(cpToken);
				} catch (final Exception e) {
					throw new AplicacaoException("Erro na gravação", 0, e);
				}
			}

		} catch (final Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao validar o Token.", 0, e);
		}
	}
	/****  ****/
	
	private String textoEmailDefinicaoPin(DpPessoa destinatario, String corpo) {		
		String conteudo = "";
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/email/novo-pin-definido.html"),StandardCharsets.UTF_8))) {			
			String str;
			
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeUsuario}", destinatario.getNomePessoa())
					.replace("${corpo}", corpo);
			
			return conteudo;
			
		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}
			
	}
	
	private String textoEmailDefinicaoSenha(DpPessoa destinatario, String corpo) {		
		String conteudo = "";
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/email/nova-senha-definida.html"),StandardCharsets.UTF_8))) {			
			String str;
			
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeUsuario}", destinatario.getNomePessoa())
					.replace("${corpo}", corpo);
			
			return conteudo;
			
		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}
			
	}

	
	public void enviarEmailDefinicaoPIN(DpPessoa destinatario, String assunto, String corpo) {
		String[] destinanarios = { destinatario.getEmailPessoaAtual() };
		String conteudoHTML = textoEmailDefinicaoPin(destinatario,corpo);
		
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}
	
	public void enviarEmailDefinicaoSenha(DpPessoa destinatario, String assunto, String corpo) {
		String[] destinanarios = { destinatario.getEmailPessoaAtual() };
		String conteudoHTML = textoEmailDefinicaoSenha(destinatario,corpo);
		
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}
	
	
	private String textoEmailTokenResetPin(DpPessoa destinatario,  String tokenPin) {		
		String conteudo = "";
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/email/token-pin-reset.html"),StandardCharsets.UTF_8))) {			
			String str;
			
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeUsuario}", destinatario.getNomePessoa())
					.replace("${tokenPin}", tokenPin);
			
			return conteudo;
			
		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}
			
	}
	
	private String textoEmailTokenResetSenha(DpPessoa destinatario,  String token) {		
		String conteudo = "";
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/email/token-senha-reset.html"),StandardCharsets.UTF_8))) {			
			String str;
			
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeUsuario}", destinatario.getNomePessoa())
					.replace("${token}", token);
			
			return conteudo;
			
		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}
			
	}

	
	public void enviarEmailTokenResetPIN(DpPessoa destinatario, String assunto, String tokenPin) {
		String[] destinanarios = { destinatario.getEmailPessoaAtual() };
		String conteudoHTML = textoEmailTokenResetPin(destinatario,tokenPin);
		
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}
	
	public void enviarEmailTokenResetSenha(DpPessoa destinatario, String assunto, String token) {
		String[] destinanarios = { destinatario.getEmailPessoaAtual() };
		String conteudoHTML = textoEmailTokenResetSenha(destinatario,token);
		
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}
	
	public DpLotacao criarLotacao(final CpIdentidade identidadeCadastrante, final DpPessoa titular, final DpLotacao lotaTitular, 
			final Long idLotacao, final String nmLotacao, final Long idOrgaoUsu, final String siglaLotacao,
			final String situacao, final Boolean isExternaLotacao, final Long lotacaoPai, final Long idLocalidade) {
		
		validarCriacaoLotacao(nmLotacao, idOrgaoUsu, siglaLotacao, idLocalidade);
		
		CpOrgaoUsuario cpOrgaoUsuario = new CpOrgaoUsuario();
		cpOrgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
		
		validarSiglaCastradaOutraLotacao(idLotacao, siglaLotacao, cpOrgaoUsuario);
		
		List<DpPessoa> listPessoa = null;
		DpLotacao lotacaoNova = new DpLotacao();
		DpLotacao lotacao = null;	
		
		if(idLotacao != null) {
			lotacao = dao().consultar(idLotacao, DpLotacao.class, false);
			lotacaoNova.setIsSuspensa(lotacao.getIsSuspensa());
		} else {
			lotacaoNova.setIsSuspensa(INTEGER_ZERO);
		}
		
		Date dataSistema = new Date(System.currentTimeMillis());
		
		if(podeGravarLotacao(idLotacao, nmLotacao, siglaLotacao, situacao, isExternaLotacao, lotacaoPai, idLocalidade,
				lotacao)) {
			if (idLotacao != null) {			
				listPessoa = popularInativacaoLotacaoEditada(titular, lotaTitular, idLotacao, nmLotacao, siglaLotacao, situacao,
						lotacao, lotacaoNova, dataSistema);
			} else {
				lotacao = null;
			}
			
			popularLotacaoNova(idLotacao, nmLotacao, siglaLotacao, isExternaLotacao, lotacaoPai, idLocalidade,
					cpOrgaoUsuario, lotacaoNova, lotacao, dataSistema, situacao);
			
			try {
				if (lotacao != null) {
					if (lotacaoNova.getDataFimLotacao() != null) {
						lotacao.setDataFimLotacao(lotacaoNova.getDataFimLotacao());
					} else {
						lotacao.setDataFimLotacao(null);
					}
					dao().gravarComHistorico(lotacaoNova, lotacao, dataSistema, identidadeCadastrante);
				} else {
					dao().gravarComHistorico(lotacaoNova, identidadeCadastrante);
				}
				
				gravarLotacaoPessoaComHistorico(identidadeCadastrante, listPessoa, lotacaoNova, lotacao, dataSistema);
			} catch (final Exception e) {
				throw new AplicacaoException("Erro na gravação", INTEGER_ZERO, e);
			}
		}
		return lotacaoNova;
	}

	private boolean podeGravarLotacao(final Long idLotacao, final String nmLotacao, final String siglaLotacao,
			final String situacao, final Boolean isExternaLotacao, final Long lotacaoPai, final Long idLocalidade,
			DpLotacao lotacao) {
		return idLotacao == null ||(idLotacao != null && lotacao != null && (!nmLotacao.equals(lotacao.getNomeLotacao()) || !siglaLotacao.equalsIgnoreCase(lotacao.getSiglaLotacao())
										|| (lotacao.getDataFim() == null && "false".equals(situacao)) || (lotacao.getDataFim() != null && "true".equals(situacao)) 
										|| (isExternaLotacao != null && ((lotacao.getIsExternaLotacao() == null) || lotacao.getIsExternaLotacao() == 0))
										|| (isExternaLotacao == null && ((lotacao.getIsExternaLotacao() != null) && lotacao.getIsExternaLotacao() == 1))
										|| (isExternaLotacao != null && lotacao.getIsExternaLotacao() != null && !isExternaLotacao.equals(lotacao.getIsExternaLotacao().equals(Integer.valueOf(1)) ? Boolean.TRUE : Boolean.FALSE))
										|| (lotacaoPai != null && lotacao.getLotacaoPai() == null)
										|| (lotacaoPai == null && lotacao.getLotacaoPai() != null)
										|| (lotacaoPai != null && !lotacaoPai.equals(lotacao.getLotacaoPai() != null ? lotacao.getLotacaoPai().getId() : 0L))
										|| !idLocalidade.equals(lotacao.getLocalidade().getId())));
	}

	private void gravarLotacaoPessoaComHistorico(final CpIdentidade identidadeCadastrante, List<DpPessoa> listPessoa,
			DpLotacao lotacaoNova, DpLotacao lotacao, Date dataSistema) {
		if(lotacao != null && lotacao.getId() != null) {					
			DpPessoa pessoaNova = null;
			for (DpPessoa dpPessoa : listPessoa) {
				pessoaNova = new DpPessoa();
				if(dpPessoa.getLotacao().getIdInicial().equals(lotacaoNova.getIdLotacaoIni())) {
					pessoaNova.setLotacao(lotacaoNova);
				} else {
					if(dpPessoa.getLotacao().getLotacaoPai() != null && lotacaoNova.getId().equals(dpPessoa.getLotacao().getLotacaoAtual().getLotacaoPai().getId())) {
						pessoaNova.setLotacao(dpPessoa.getLotacao().getLotacaoAtual());
					} else {
						//grava nova lotacao filho e setar na pessoa
						DpLotacao lotacaoFilhoNova = new DpLotacao();
						DpLotacao lotacaoFilhoAntiga =  dpPessoa.getLotacao().getLotacaoAtual();
						
						lotacaoFilhoNova.setDataInicio(dataSistema);
						copiaLotacao(lotacaoFilhoAntiga, lotacaoFilhoNova);
						
						dao().gravarComHistorico(lotacaoFilhoNova, lotacaoFilhoAntiga, dataSistema, identidadeCadastrante);
						pessoaNova.setLotacao(lotacaoFilhoNova);
					}
				}				
				copiarPessoa(dpPessoa, pessoaNova);
				dao().gravarComHistorico(pessoaNova, dpPessoa, dataSistema, identidadeCadastrante);
			}
		}
	}

	private void popularLotacaoNova(final Long idLotacao, final String nmLotacao, final String siglaLotacao,
									final Boolean isExternaLotacao, final Long lotacaoPai, final Long idLocalidade, 
									CpOrgaoUsuario cpOrgaoUsuario, DpLotacao lotacaoNova, DpLotacao lotacao, 
									Date dataSistema, final String situacao) {
		if(lotacaoPai != null) {
			validarIdLotacaoPai(idLotacao, lotacaoPai);
			lotacaoNova.setLotacaoPai(CpDao.getInstance().consultarLotacaoPorId(lotacaoPai));
		}
		lotacaoNova.setNomeLotacao(Texto.removerEspacosExtra(nmLotacao).trim());
		lotacaoNova.setSiglaLotacao(siglaLotacao.toUpperCase());
		
		CpLocalidade localidade = new CpLocalidade();
		localidade.setIdLocalidade(idLocalidade);
		lotacaoNova.setLocalidade(dao().consultarLocalidade(localidade));
		
		if(lotacaoNova.getOrgaoUsuario() == null && lotacao != null) {
			lotacaoNova.setOrgaoUsuario(lotacao.getOrgaoUsuario());
		} else {
			lotacaoNova.setOrgaoUsuario(cpOrgaoUsuario);
		}
		
		if (isExternaLotacao != null) {
			lotacaoNova.setIsExternaLotacao(INTEGER_ONE);
		} else {
			lotacaoNova.setIsExternaLotacao(INTEGER_ZERO);	
		}
		
		lotacaoNova.setDataInicioLotacao(dataSistema);

		if ("false".equals(situacao)) {
			lotacaoNova.setDataFimLotacao(dataSistema);
		}
	}

	private void validarIdLotacaoPai(final Long idLotacao, final Long lotacaoPai) {
		DpLotacao lotPai = new DpLotacao();
		lotPai = CpDao.getInstance().consultarLotacaoPorId(lotacaoPai);
		if(idLotacao != null) {
			while(lotPai != null) {				
				if(lotPai.getId().equals(idLotacao)) {
					throw new AplicacaoException(SigaMessages.getMessage("usuario.lotacao") + " não pode ser selecionada como pai", 0);
				} else if(lotPai.getLotacaoPai() != null) {
					lotPai = CpDao.getInstance().consultarLotacaoPorId(lotPai.getLotacaoPai().getId());
				} else {
					lotPai = null;
				}
			}
		}
	}

	private void validarSiglaCastradaOutraLotacao(final Long idLotacao, final String siglaLotacao,
			CpOrgaoUsuario cpOrgaoUsuario) {
		DpLotacao lotacaoTemp = consultarLotacaoPorSigla(siglaLotacao, cpOrgaoUsuario);		
		
		if(lotacaoTemp != null && lotacaoTemp.getId() != null && !lotacaoTemp.getId().equals(idLotacao)) {
			throw new AplicacaoException("Sigla já cadastrada para outra lotação");
		}
	}

	private List<DpPessoa> popularInativacaoLotacaoEditada(final DpPessoa titular, final DpLotacao lotaTitular,
			final Long idLotacao, final String nmLotacao, final String siglaLotacao, final String situacao,
			DpLotacao lotacao, DpLotacao lotacaoNova, Date dataSistema) {
		List<DpPessoa> listPessoa = CpDao.getInstance().pessoasPorLotacao(idLotacao, Boolean.TRUE, Boolean.FALSE);
		
		Integer qtdeDocumentoCriadosPosse = quatidadeMarcasOuDocumentosEmPosseDaLotacao(lotacao, null, Boolean.FALSE); // Não restringe Marcadores
		
		if(!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular, lotaTitular,"SIGA;GI;CAD_LOTACAO;ALT") && qtdeDocumentoCriadosPosse > 0 && 
				(!lotacao.getNomeLotacao().equalsIgnoreCase(Texto.removerEspacosExtra(nmLotacao).trim()) || !lotacao.getSiglaLotacao().equalsIgnoreCase(siglaLotacao.toUpperCase().trim()))) {
			throw new AplicacaoException("Não é permitido a alteração do nome e sigla da unidade após criação de documento ou tramitação de documento para unidade.");
		}
						
		if(podeInativarLotacao(lotacao, listPessoa, qtdeDocumentoCriadosPosse, situacao)) {
			lotacaoNova.setDataFimLotacao(dataSistema);
		}
		lotacaoNova.setIdLotacaoIni(lotacao.getIdLotacaoIni());
		return listPessoa;
	}
	
	private boolean podeInativarLotacao(final DpLotacao lotacao, final List<DpPessoa> listPessoa, 
					final Integer qtdeDocumentoCriadosPosse, final String situacao) throws AplicacaoException {
		if(lotacao.getDataFim() == null && "false".equals(situacao)) {
	        if(listPessoa.size() > 0 || qtdeDocumentoCriadosPosse > 0) {	        		 
	        	throw new AplicacaoException("Inativação não permitida. Existem documentos e usuários vinculados nessa " + SigaMessages.getMessage("usuario.lotacao") , 0);
	        } else if(dao().listarLotacoesPorPai(lotacao).size() > 0) {
	        	throw new AplicacaoException("Inativação não permitida. Está " + SigaMessages.getMessage("usuario.lotacao") + " é pai de outra " + SigaMessages.getMessage("usuario.lotacao") , 0);
	        } else {
	        	return Boolean.TRUE;
	        }
		}
		return Boolean.FALSE;
	}

	private DpLotacao consultarLotacaoPorSigla(final String siglaLotacao, CpOrgaoUsuario cpOrgaoUsuario) {
		DpLotacao lotacaoTemp = new DpLotacao();		
		lotacaoTemp.setSiglaLotacao(siglaLotacao);		
		lotacaoTemp.setOrgaoUsuario(cpOrgaoUsuario);
		DpLotacao lotacao = CpDao.getInstance().consultarPorSigla(lotacaoTemp);
		return lotacao;
	}

	private void validarCriacaoLotacao(final String nmLotacao, final Long idOrgaoUsu, final String siglaLotacao,
			final Long idLocalidade) {
		if(nmLotacao == null)
			throw new AplicacaoException("Nome da lotação não informado");
		
		if(idOrgaoUsu == null)
			throw new AplicacaoException("Órgão não informado");
		
		if(siglaLotacao == null)
			throw new AplicacaoException("Sigla de lotação não informado");
		
		if(Long.valueOf(0).equals(idLocalidade))
			throw new AplicacaoException("Localidade da lotação não informado");
		
		if(nmLotacao != null && !nmLotacao.matches("[\'a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+")) 
			throw new AplicacaoException("Nome com caracteres não permitidos");
		
		if(siglaLotacao != null && !siglaLotacao.matches("[a-zA-ZçÇ0-9,/-]+")) 
			throw new AplicacaoException("Sigla com caracteres não permitidos");
	}
	
	public void copiaLotacao(DpLotacao lotAnt, DpLotacao lotNova) {
		lotNova.setNomeLotacao(lotAnt.getNomeLotacao());
		lotNova.setSiglaLotacao(lotAnt.getSiglaLotacao());		
		lotNova.setIsExternaLotacao(lotAnt.getIsExternaLotacao());
		lotNova.setIdInicial(lotAnt.getIdInicial());
		lotNova.setLocalidade(lotAnt.getLocalidade());
		lotNova.setLotacaoPai(lotAnt.getLotacaoPai() != null ? lotAnt.getLotacaoPai().getLotacaoAtual() : null);
		lotNova.setOrgaoUsuario(lotAnt.getOrgaoUsuario());
		lotNova.setIsSuspensa(lotAnt.getIsSuspensa());
	}
	
	public void copiarPessoa(DpPessoa pesAnt, DpPessoa pesNova) {
		pesNova.setNomePessoa(pesAnt.getNomePessoa());
		pesNova.setCpfPessoa(pesAnt.getCpfPessoa());
		pesNova.setCargo(pesAnt.getCargo());
		pesNova.setDataNascimento(pesAnt.getDataNascimento());
		pesNova.setFuncaoConfianca(pesAnt.getFuncaoConfianca());
		pesNova.setOrgaoUsuario(pesAnt.getOrgaoUsuario());
		pesNova.setEmailPessoa(pesAnt.getEmailPessoa());
		pesNova.setIdentidade(pesAnt.getIdentidade());
		pesNova.setDataExpedicaoIdentidade(pesAnt.getDataExpedicaoIdentidade());
		pesNova.setOrgaoIdentidade(pesAnt.getOrgaoIdentidade());
		pesNova.setUfIdentidade(pesAnt.getUfIdentidade());
		pesNova.setUfIdentidade(pesAnt.getUfIdentidade());
		pesNova.setSesbPessoa(pesAnt.getSesbPessoa());
		pesNova.setSituacaoFuncionalPessoa(pesAnt.getSituacaoFuncionalPessoa());
		pesNova.setMatricula(pesAnt.getMatricula());
		pesNova.setIdPessoaIni(pesAnt.getIdPessoaIni());
		pesNova.setIdePessoa(pesAnt.getIdePessoa());
	}
	
	
	public DpCargo gravarCargo(final CpIdentidade identidadeCadastrante, final Long id, final String nmCargo, final Long idOrgaoUsu, final Boolean isAtivo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		if (identidadeCadastrante == null) {
			throw new AplicacaoException("Não é possível cadastrar ou alterar o cargo sem que seja informado o cadastrante.");
		}
		
		DpCargo cargo = null;
		DpCargo cargoNovo = new DpCargo();
		Date dt = dao().consultarDataEHoraDoServidor();
		
		if (id != null && id > 0) {
			cargo = dao().consultar(id, DpCargo.class, false);
			if (cargo == null) {
				throw new AplicacaoException("Cargo informado não localizado.");
			}
			PropertyUtils.copyProperties(cargoNovo, cargo); //Copia cargo existente para novo registro
			cargoNovo.setId(null);
		} else {
			cargoNovo.setDataInicio(dt);
		}
		
		CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
		if(idOrgaoUsu == null && cargoNovo.getOrgaoUsuario() == null)
			throw new AplicacaoException("Órgão não informado.");
		else {
			if (idOrgaoUsu != null && idOrgaoUsu != 0) {
				
				orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);			
				if (orgaoUsuario == null) {
					throw new AplicacaoException("Órgão informado não localizado.");
				} 

				if (cargoNovo.getOrgaoUsuario() != null && !orgaoUsuario.getIdOrgaoUsu().equals(cargoNovo.getOrgaoUsuario().getIdOrgaoUsu())) {
					
					List<DpPessoa> listaPessoasNoCargo = null;
					listaPessoasNoCargo = CpDao.getInstance().consultarPessoasComCargo(id);
					
					if (listaPessoasNoCargo != null && !listaPessoasNoCargo.isEmpty()) {
						throw new AplicacaoException("Não é possível alterar o Órgão do Cargo após o vínculo com Pessoas.");
					}
				}
				cargoNovo.setOrgaoUsuario(orgaoUsuario);
			}
			
		}
		
		
		if(nmCargo == null && cargoNovo.getNomeCargo() == null)
			throw new AplicacaoException("Nome do cargo não informado");
		else {
			if(nmCargo != null) {
				if (!nmCargo.matches("[a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9-/.]+")) 
					throw new AplicacaoException("Nome com caracteres não permitidos");

				cargoNovo.setNomeCargo(Texto.removerEspacosExtra(nmCargo).trim());
				
				DpCargo cargoNomeOrgao = new DpCargo();
				cargoNomeOrgao = CpDao.getInstance().consultarPorNomeOrgao(cargoNovo);
				if(cargoNomeOrgao != null && !cargoNomeOrgao.equals(cargo)) {
					throw new AplicacaoException("Nome do cargo já cadastrado neste órgão!");
				}
				cargoNomeOrgao = null;
				
			}
		}
		
		List<DpPessoa> pessoasAtivasCargo = null;
		if (cargo != null) {
			//Consulta pessoas ativas para o cargo. 
			//Se for Inativação. Não permite inativar com pessoas ativas
			//Se for alteração. Aplica a troca do cargo para as pessoas ativas.
			final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setCargo(cargo);
			flt.setIdOrgaoUsu(cargoNovo.getOrgaoUsuario().getIdOrgaoUsu());
			flt.setNome("");
			pessoasAtivasCargo = CpDao.getInstance().consultarPorFiltro(flt);
		}
		
		if (isAtivo != null) {
			if (isAtivo) {
				cargoNovo.setDataFimCargo(null);
				cargoNovo.setHisIdcFim(null);
			} else {

				if (pessoasAtivasCargo != null && !pessoasAtivasCargo.isEmpty()) {
					throw new AplicacaoException(String.format("Não é possível realizar a operação, pois existe %s vinculado ao cadastro.",SigaMessages.getMessage("usuario.matricula")),0);
				}
				cargoNovo.setDataFimCargo(dt);
			}
		}	
		
		//Persistindo as alterações
		try {
			dao().gravarComHistorico(cargoNovo, cargo, dt, identidadeCadastrante);
			
			//Aplica cargo alterado nas pessoas ativas do cargo anterior.
			if (pessoasAtivasCargo != null && !pessoasAtivasCargo.isEmpty()) {		
				for (DpPessoa pessoaAtual : pessoasAtivasCargo) {
					
					DpPessoa pessoaNovo = new DpPessoa();
					copiarPessoa(pessoaAtual,pessoaNovo); //copyProperty não funciona, pois DpPessoa é um Bean despadronizado

					if (pessoaNovo != null) {
						pessoaNovo.setCargo(cargoNovo);
						pessoaNovo.setLotacao(pessoaAtual.getLotacao());
						dao().gravarComHistorico(pessoaNovo, pessoaAtual, dt, identidadeCadastrante);
					}
					
				}
			}

			
		} catch (final Exception e) {
			throw e;
		}
		
		return cargoNovo;
		
	}
	
	public DpFuncaoConfianca gravarFuncaoConfianca(final CpIdentidade identidadeCadastrante, final Long id, final String nmFuncaoConfianca, final Long idOrgaoUsu, final Boolean isAtivo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		if (identidadeCadastrante == null) {
			throw new AplicacaoException("Não é possível cadastrar ou alterar o cargo sem que seja informado o cadastrante.");
		}
		
		DpFuncaoConfianca funcaoConfianca = null;
		DpFuncaoConfianca funcaoConfiancaNovo = new DpFuncaoConfianca();
		Date dt = dao().consultarDataEHoraDoServidor();
		
		if (id != null && id > 0) {
			funcaoConfianca = dao().consultar(id, DpFuncaoConfianca.class, false);
			if (funcaoConfianca == null) {
				throw new AplicacaoException("Função de Confiança informada não localizada.");
			}
			PropertyUtils.copyProperties(funcaoConfiancaNovo, funcaoConfianca); //Copia Funcao de Confianca existente para novo registro
			funcaoConfiancaNovo.setId(null);
		} else {
			funcaoConfiancaNovo.setDataInicio(dt);
		}
		
		
		CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
		if(idOrgaoUsu == null && funcaoConfiancaNovo.getOrgaoUsuario() == null)
			throw new AplicacaoException("Órgão não informado.");
		else {
			if (idOrgaoUsu != null && idOrgaoUsu != 0) {
				
				orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);			
				if (orgaoUsuario == null) {
					throw new AplicacaoException("Órgão informado não localizado.");
				} 

				if (funcaoConfiancaNovo.getOrgaoUsuario() != null && !orgaoUsuario.getIdOrgaoUsu().equals(funcaoConfiancaNovo.getOrgaoUsuario().getIdOrgaoUsu())) {

					List<DpPessoa> listaPessoasNaFuncaoConfianca = null;
					listaPessoasNaFuncaoConfianca= CpDao.getInstance().consultarPessoasComFuncaoConfianca(id);
					
					if (listaPessoasNaFuncaoConfianca != null && !listaPessoasNaFuncaoConfianca.isEmpty()) {
						throw new AplicacaoException("Não é possível alterar o Órgão da Função de Confiança após o vínculo com Pessoas.");
					}
				}
				funcaoConfiancaNovo.setOrgaoUsuario(orgaoUsuario);
			}
			
		}
		
		if(nmFuncaoConfianca == null && funcaoConfiancaNovo.getNomeFuncao() == null)
			throw new AplicacaoException("Nome da Função de Confiança não informado.");
		else {
			if(nmFuncaoConfianca != null) {
				if (!nmFuncaoConfianca.matches(Texto.FuncaoConfianca.REGEX_CARACTERES_PERMITIDOS)) 
					throw new AplicacaoException("Nome com caracteres não permitidos.");

				funcaoConfiancaNovo.setNomeFuncao(Texto.removerEspacosExtra(nmFuncaoConfianca).trim());
				
				DpFuncaoConfianca funcaoConfiancaNomeOrgao = new DpFuncaoConfianca();
				funcaoConfiancaNomeOrgao = CpDao.getInstance().consultarPorNomeOrgao(funcaoConfiancaNovo);
				if(funcaoConfiancaNomeOrgao != null && !funcaoConfiancaNomeOrgao.equals(funcaoConfianca)) {
					throw new AplicacaoException("Nome do cargo já cadastrado neste órgão!");
				}
				funcaoConfiancaNomeOrgao = null;
				
			}
		}
		
		List<DpPessoa> pessoasAtivasFuncaoConfianca = null;
		
		if (funcaoConfianca != null) {
			//Consulta pessoas ativas para a Função de Confiança. 
			//Se for Inativação. Não permite inativar com pessoas ativas
			//Se for alteração. Aplica a troca da Função de Confiança para as pessoas ativas.
			final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setFuncaoConfianca(funcaoConfianca);
			flt.setIdOrgaoUsu(funcaoConfiancaNovo.getOrgaoUsuario().getIdOrgaoUsu());
			flt.setNome("");
			pessoasAtivasFuncaoConfianca = CpDao.getInstance().consultarPorFiltro(flt);
		}
		
		if (isAtivo != null) {
			if (isAtivo) {
				funcaoConfiancaNovo.setDataFimFuncao(null);
				funcaoConfiancaNovo.setHisIdcFim(null);
			} else {

				if (pessoasAtivasFuncaoConfianca != null && !pessoasAtivasFuncaoConfianca.isEmpty()) {
					throw new AplicacaoException(String.format("Não é possível realizar a operação, pois existe %s vinculado ao cadastro.",SigaMessages.getMessage("usuario.matricula")),0);
				}
				funcaoConfiancaNovo.setDataFimFuncao(dt);
			}
		}	
		
		//Persistindo as alterações
		try {
			dao().gravarComHistorico(funcaoConfiancaNovo, funcaoConfianca, dt, identidadeCadastrante);
			
			//Aplica cargo alterado nas pessoas ativas do cargo anterior.
			if (pessoasAtivasFuncaoConfianca != null && !pessoasAtivasFuncaoConfianca.isEmpty()) {
				for (DpPessoa pessoaAtual : pessoasAtivasFuncaoConfianca) {
				
					DpPessoa pessoaNovo = new DpPessoa();
					copiarPessoa(pessoaAtual,pessoaNovo); //copyProperty não funciona, pois DpPessoa é um Bean despadronizado

					if (pessoaNovo != null) {
						pessoaNovo.setFuncaoConfianca(funcaoConfiancaNovo);
						pessoaNovo.setLotacao(pessoaAtual.getLotacao());
						dao().gravarComHistorico(pessoaNovo, pessoaAtual, dt, identidadeCadastrante);
					}
					
				}
			}

			
		} catch (final Exception e) {
			throw e;
		}
		
		return funcaoConfiancaNovo;
	}
	
	public void excluirCargo(final DpCargo cargo)  {
		try {
			if (cargo == null ) 
				throw new AplicacaoException("Cargo não informado.");
			if (!cargo.getId().equals(cargo.getIdCargoIni())) 
				throw new AplicacaoException("Não é possível excluir um cargo que já foi alterado.");
			dao().excluir(cargo);
		} catch (final Exception e) {
			throw e;
		}

	}
	
	public void excluirFuncaoConfianca(final DpFuncaoConfianca funcaoConfianca)  {
		try {
			if (funcaoConfianca == null ) 
				throw new AplicacaoException("Função de Confiança não informada.");
			if (!funcaoConfianca.getId().equals(funcaoConfianca.getIdFuncaoIni())) 
				throw new AplicacaoException("Não é possível excluir uma função de confiança que já foi alterada.");
			dao().excluir(funcaoConfianca);
		} catch (final Exception e) {
			throw e;
		}

	}  
	
	//Não persistindo no banco esse método remove da coleção que será iterada e renderizada na view 
	//os órgão que não querem ser exibidos por outros órgãos no sistema.
	public List<CpOrgaoUsuario> removeOrgaosQueNaoSeraoExibidos(List<CpOrgaoUsuario> orgaos, DpPessoa pessoa, DpLotacao lotacao) {
		Iterator<CpOrgaoUsuario> i = orgaos.iterator(); 
		while (i.hasNext()) { 
		    CpOrgaoUsuario org = i.next();  
			if (!new CpExibirEmCampoDePesquisa(pessoa, lotacao, org).eval()) 
				i.remove();
				
		}
		return orgaos;
	} 
	
	
    /**
     * Consulta quantidade de documentos criados OU que estão em posse da lotação, conforme parâmetros e propriedades
     *
     * @param lotacao
     * @return
     */
	public Integer quatidadeMarcasOuDocumentosEmPosseDaLotacao(DpLotacao lotacao, List<Long> listaMarcadores, boolean excluirMarcadoresDaContagem) { 

		Integer quantidade;

        if (excluirMarcadoresDaContagem && listaMarcadores != null)
        	quantidade = CpDao.getInstance().quatidadeMarcasEmPosseDaLotacaoMarcadores(lotacao,listaMarcadores);
        else
        	quantidade = CpDao.getInstance().consultarQtdeDocCriadosPossePorDpLotacao(lotacao.getIdInicial());

        return quantidade;
    }
	
	
    /**
     * Consulta quantidade de documentos criados OU que estão em posse da lotação, conforme parâmetros e propriedades
     *
     * @param pessoa
     * @return
     */
	public Integer quatidadeMarcasOuDocumentosEmPosseDaPessoaDaLotacao(DpPessoa pessoa, List<Long> listaMarcadores, boolean excluirMarcadoresDaContagem) { 

		Integer quantidade;		

        if (excluirMarcadoresDaContagem && listaMarcadores != null)
        	quantidade = CpDao.getInstance().quatidadeMarcasEmPosseDaPessoaMarcadores(pessoa,listaMarcadores);
        else
        	quantidade = CpDao.getInstance().quantidadeDocumentos(pessoa);

        return quantidade;
    }
	
	public boolean restringeMarcadoresParaInativacaoLotacao() {
		final String TODOS_MARCADORES = "*";
		final String marcadoresPermitidos = Prop.get("/siga.lotacao.inativacao.marcadores.permitidos");
		
		return !TODOS_MARCADORES.equals(marcadoresPermitidos);
		
	}
	
	public boolean restringeMarcadoresParaAlteracaoLotacaoPessoa() {
		final String TODOS_MARCADORES = "*";
		final String marcadoresPermitidos = Prop.get("/siga.alteracao.lotacao.pessoa.marcadores.permitidos");
		
		return !TODOS_MARCADORES.equals(marcadoresPermitidos);
		
	}
	
	
	public List<Long> getListaMarcadoresPermitidosInativacaoLotacao() {

        List<String> listaMarcadores = null;
        List<String> listaGrupoMarcadores = null;
        List<Long> listaMarcadoresPermitidos = null;
		
		if (restringeMarcadoresParaInativacaoLotacao()) {
			
	        listaMarcadores = Prop.getList("/siga.lotacao.inativacao.marcadores.permitidos");
	        
	        if (listaMarcadores != null) {
		        listaGrupoMarcadores = Prop.getList("/siga.lotacao.inativacao.grupo.marcadores.permitidos");
		        listaMarcadoresPermitidos = CpMarcadorEnum.getListIdByListChaves(listaMarcadores);
		        
				//Soma-se aos Marcadores Permitidos os marcadores relacionados aos grupos definidos
				if (!listaGrupoMarcadores.isEmpty()) {
					for (String grupo : listaGrupoMarcadores) {
						if (!"".equals(grupo)) {
							listaMarcadoresPermitidos.addAll(CpMarcadorEnum.getListIdByGrupo(CpMarcadorGrupoEnum.valueOf(grupo.trim())));
						}			
					}			
				}
	        }
		}
			
		return listaMarcadoresPermitidos;
		
	}
	
	public List<Long> getListaMarcadoresPermitidosAlteracaoLotacaoPessoa() {

        List<String> listaMarcadores = null;
        List<String> listaGrupoMarcadores = null;
        List<Long> listaMarcadoresPermitidos = null;
		
		if (restringeMarcadoresParaAlteracaoLotacaoPessoa()) {
			
	        listaMarcadores = Prop.getList("/siga.alteracao.lotacao.pessoa.marcadores.permitidos");
	        
	        if (listaMarcadores != null) {
		        listaGrupoMarcadores = Prop.getList("/siga.alteracao.lotacao.pessoa.grupo.marcadores.permitidos");
		        listaMarcadoresPermitidos = CpMarcadorEnum.getListIdByListChaves(listaMarcadores);
		        
				//Soma-se aos Marcadores Permitidos os marcadores relacionados aos grupos definidos
				if (!listaGrupoMarcadores.isEmpty()) {
					for (String grupo : listaGrupoMarcadores) {
						if (!"".equals(grupo)) {
							listaMarcadoresPermitidos.addAll(CpMarcadorEnum.getListIdByGrupo(CpMarcadorGrupoEnum.valueOf(grupo.trim())));
						}			
					}			
				}
	        }
		}
			
		return listaMarcadoresPermitidos;
		
	}
	
	
	public boolean podeAtivarLotacao(DpLotacao lotacao, DpPessoa titular, DpLotacao lotacaoTitular) throws Exception {	
		final String servico = "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;CAD_LOTACAO:Cadastrar Lotação";
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular, lotacaoTitular, servico);
		
		if (lotacao.getDataFimLotacao() == null) {
			throw new AplicacaoException("Ativação não permitida. " + SigaMessages.getMessage("usuario.lotacao") + " não encontra-se inativa.", 0);
		}
		
		return true;
		
	}
	
	public boolean podeInativarLotacao(DpLotacao lotacao, DpPessoa titular, DpLotacao lotacaoTitular) throws Exception {		
		final String servico = "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;CAD_LOTACAO:Cadastrar Lotação";
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular, lotacaoTitular, servico);
		
		/* Verifica Lotações Filhas */
		if (dao().listarLotacoesPorPai(lotacao).size() > 0) 
			throw new AplicacaoException("Inativação não permitida. Está " + SigaMessages.getMessage("usuario.lotacao") + " é pai de outra " + SigaMessages.getMessage("usuario.lotacao"),0);
		
		/* Verifica Pessoas Ativas */
		List<DpPessoa> listaPessoasAtivasLotacao = CpDao.getInstance().pessoasPorLotacao(lotacao.getId(), Boolean.TRUE, Boolean.FALSE);
		Integer quantidadePessoasAtivas = listaPessoasAtivasLotacao.size();
		if (quantidadePessoasAtivas > 0) {
			throw new AplicacaoException("Inativação não permitida. Existem usuários ativos vinculados nessa " + SigaMessages.getMessage("usuario.lotacao"), 0);
			
		}
		
		if (restringeMarcadoresParaInativacaoLotacao()) {	
			/* Verifica Quantidade de Marcas para Lotação */
			List<Long> listaMarcadoresPermitidos = getListaMarcadoresPermitidosInativacaoLotacao();
			Integer quantidadeEmPosse = quatidadeMarcasOuDocumentosEmPosseDaLotacao(lotacao, listaMarcadoresPermitidos, Boolean.TRUE); 
			if (quantidadeEmPosse > 0) {
				throw new AplicacaoException("Inativação não permitida. Existem documentos vinculados nessa " + SigaMessages.getMessage("usuario.lotacao"), 0);
				
			}
			
			/* Verifica Quantidade de Marcas para as Pessoas Inativas da Lotação */
			final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setBuscarFechadas(Boolean.TRUE);
			flt.setLotacao(lotacao);	
			flt.setNome("");
			List<DpPessoa> listaPessoasInativasDaLotacao = CpDao.getInstance().consultarPorFiltro(flt);
			
			if (listaPessoasInativasDaLotacao != null && listaPessoasInativasDaLotacao.size() > 0 ) {
				for (DpPessoa pessoa : listaPessoasInativasDaLotacao) {
					quantidadeEmPosse = quatidadeMarcasOuDocumentosEmPosseDaPessoaDaLotacao(pessoa, listaMarcadoresPermitidos, Boolean.TRUE); 
					if (quantidadeEmPosse > 0) {
						throw new AplicacaoException("Inativação não permitida. Existem documentos de usuários inativos vinculados nessa " + SigaMessages.getMessage("usuario.lotacao"), 0);
						
					}	
				}
			}
		}
		
		return true;	
	}
	
	public boolean podeAlterarLotacaoPessoaDentroMesmoOrgao(DpPessoa pessoa) {	
		if (restringeMarcadoresParaAlteracaoLotacaoPessoa()) {	
			Integer quantidadeEmPosse = quatidadeMarcasOuDocumentosEmPosseDaPessoaDaLotacao(pessoa, getListaMarcadoresPermitidosAlteracaoLotacaoPessoa(), Boolean.TRUE);
			if (quantidadeEmPosse > 0) {
				return false; 
			}
		}
		
		return true; 
	}
	
	public boolean podeInativarLotacaoLote(DpPessoa titular, DpLotacao lotacaoTitular) {
		final String servico = "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;CAD_LOTACAO:Cadastrar Lotação;INATIVA_LOTE:Inativar Lotação em Lote";
		return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular, titular.getLotacao(), servico);
		
	}
	
	public void resetContagemTentativasMalsucedidasPin(CpIdentidade identidade) {
		identidade.setPinContadorTentativa(INTEGER_ZERO);
		dao().gravar(identidade);	
		dao().descarregar();
	}
	
	public void incrementarContagemTentativasMalsucedidasPin(CpIdentidade identidade) {
		if (!hasBloqueioPinPorTentativa(identidade)) {
			identidade.setPinContadorTentativa(identidade.getPinContadorTentativa() + INTEGER_ONE);
			dao().gravar(identidade);	
			dao().descarregar();
		}
	}
	
	public boolean hasBloqueioPinPorTentativa(CpIdentidade identidade) {
		return identidade.getPinContadorTentativa().intValue() == CpIdentidade.PIN_NUM_MAX_TENTATIVAS;
	}
	
	public int quantidadeTentativasRestantes(CpIdentidade identidade) {
		return CpIdentidade.PIN_NUM_MAX_TENTATIVAS - identidade.getPinContadorTentativa().intValue();
	}
}