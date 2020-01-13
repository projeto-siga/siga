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
package br.gov.jfrj.siga.cp;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
// Ver um lugar melhor para queries assim ficarem quando não se estiver usando
// XML
@NamedQueries({
		@NamedQuery(name = "consultarIdentidadeCadastrante", query = "select u from CpIdentidade u, DpPessoa pes "
				+ "     where u.nmLoginIdentidade = :nmUsuario"
				+ "      and u.dpPessoa.cpfPessoa = pes.cpfPessoa"
				+ "      and pes.sesbPessoa = :sesbPessoa"
				+ "      and pes.dataFimPessoa is null"),
		@NamedQuery(name = "consultarIdentidades", query = "select u from CpIdentidade u , DpPessoa pes "
				+ "     where pes.idPessoaIni = :idPessoaIni"
				+ "      and u.dpPessoa = pes" + "      and u.hisDtFim is null"),
		@NamedQuery(name = "consultarIdentidadeCadastranteAtiva", query = "select u from CpIdentidade u , DpPessoa pes "
				+ "where ((u.nmLoginIdentidade = :nmUsuario and pes.sesbPessoa = :sesbPessoa and pes.sesbPessoa is not null) or "
				+ " (pes.cpfPessoa is not null and pes.cpfPessoa <> :cpfZero and pes.cpfPessoa = :cpf)) "
				+ "and u.dpPessoa.idPessoaIni = pes.idPessoaIni "
				+ "and u.hisDtFim is null "
				+ "and u.dtCancelamentoIdentidade is null "
				+ "and (u.dtExpiracaoIdentidade is null or u.dtExpiracaoIdentidade > current_date()) "
				+ "and pes.dataFimPessoa is null "
				+ "and (pes.situacaoFuncionalPessoa = :sfp1 "
				+ "or pes.situacaoFuncionalPessoa = :sfp2 "
				+ "or pes.situacaoFuncionalPessoa = :sfp12 "
				+ "or pes.situacaoFuncionalPessoa = :sfp22 "
				+ "or pes.situacaoFuncionalPessoa = :sfp31) "),
        @NamedQuery(name = "consultarIdentidadeAtualPelaInicial", query = "from CpIdentidade u "
				+ "		where u.hisDtIni = "
				+ "		(select max(p.hisDtIni) from CpIdentidade p where p.hisIdIni = :idIni)"
				+ "		 and u.hisIdIni = :idIni"),				
		@NamedQuery(name = "consultarIdentidadeCpfEmail", query = "select u from CpIdentidade u , DpPessoa pes "
				+ "where (pes.cpfPessoa is not null and pes.cpfPessoa <> :cpfZero and pes.cpfPessoa = :cpf)"
				+ "and pes.emailPessoa = :email "
				+ "and u.dpPessoa.idPessoaIni = pes.idPessoaIni "
				+ "and u.hisDtFim is null "
				+ "and u.dtCancelamentoIdentidade is null "
				+ "and (u.dtExpiracaoIdentidade is null or u.dtExpiracaoIdentidade > current_date()) "
				+ "and pes.dataFimPessoa is null "
				+ "and (pes.situacaoFuncionalPessoa = :sfp1 "
				+ "or pes.situacaoFuncionalPessoa = :sfp2 "
				+ "or pes.situacaoFuncionalPessoa = :sfp12 "
				+ "or pes.situacaoFuncionalPessoa = :sfp22 "
				+ "or pes.situacaoFuncionalPessoa = :sfp31)")})

public abstract class AbstractCpIdentidade extends HistoricoAuditavelSuporte {
	@SequenceGenerator(name = "generator", sequenceName = "CORPORATIVO.CP_IDENTIDADE_SEQ")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID_IDENTIDADE", unique = true, nullable = false)
	@Desconsiderar
	private Long idIdentidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU", nullable = false)
	private CpOrgaoUsuario cpOrgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_IDENTIDADE")
	private CpTipoIdentidade cpTipoIdentidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	private DpPessoa dpPessoa;

	@Column(name = "SENHA_IDENTIDADE", length = 40)
	private String dscSenhaIdentidade;

	@Column(name = "SENHA_IDENTIDADE_CRIPTO")
	private String dscSenhaIdentidadeCripto;

	@Column(name = "SENHA_IDENTIDADE_CRIPTO_SINC")
	@Desconsiderar
	private String dscSenhaIdentidadeCriptoSinc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CANCELAMENTO_IDENTIDADE", length = 19)
	private Date dtCancelamentoIdentidade;

	@Column(name = "DATA_CRIACAO_IDENTIDADE", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacaoIdentidade;

	@Column(name = "DATA_EXPIRACAO_IDENTIDADE", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExpiracaoIdentidade;

	@Column(name = "LOGIN_IDENTIDADE", length = 20)
	private String nmLoginIdentidade;

	/* recuperacao */
	
	@Column(name = "TELEFONE_RECUPERACAO", length = 15)
	private String telefoneRecuperacao;
	
	@Column(name = "EMAIL_RECUPERACAO", length = 100)
	private String emailRecuperacao;
	
	@Column(name = "TOKEN_RECUPERACAO", length = 255)
	private String tokenRecuperacao;
	
	@Column(name = "DATA_CONFIRMA_EMAIL_RECUPERA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtConfirmacaoEmailRecuperacao;
	
	@Column(name = "DATA_EXPIRA_TOKEN_RECUPERA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExpiracaoTokenRecuperacao;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractCpIdentidade)) {
			return false;
		}
		AbstractCpIdentidade other = (AbstractCpIdentidade) obj;
		if (dscSenhaIdentidade == null) {
			if (other.dscSenhaIdentidade != null) {
				return false;
			}
		} else if (!dscSenhaIdentidade.equals(other.dscSenhaIdentidade)) {
			return false;
		}
		if (dtCancelamentoIdentidade == null) {
			if (other.dtCancelamentoIdentidade != null) {
				return false;
			}
		} else if (!dtCancelamentoIdentidade
				.equals(other.dtCancelamentoIdentidade)) {
			return false;
		}
		if (dtCriacaoIdentidade == null) {
			if (other.dtCriacaoIdentidade != null) {
				return false;
			}
		} else if (!dtCriacaoIdentidade.equals(other.dtCriacaoIdentidade)) {
			return false;
		}
		if (dtExpiracaoIdentidade == null) {
			if (other.dtExpiracaoIdentidade != null) {
				return false;
			}
		} else if (!dtExpiracaoIdentidade.equals(other.dtExpiracaoIdentidade)) {
			return false;
		}
		if (getHisDtFim() == null) {
			if (other.getHisDtFim() != null) {
				return false;
			}
		} else if (!getHisDtFim().equals(other.getHisDtFim())) {
			return false;
		}
		if (getHisDtIni() == null) {
			if (other.getHisDtIni() != null) {
				return false;
			}
		} else if (!getHisDtIni().equals(other.getHisDtIni())) {
			return false;
		}
		if (idIdentidade == null) {
			if (other.idIdentidade != null) {
				return false;
			}
		} else if (!idIdentidade.equals(other.idIdentidade)) {
			return false;
		}
		if (nmLoginIdentidade == null) {
			if (other.nmLoginIdentidade != null) {
				return false;
			}
		} else if (!nmLoginIdentidade.equals(other.nmLoginIdentidade)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the cpOrgaoUsuario
	 */
	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	/**
	 * @return the cpTipoIdentidade
	 */
	public CpTipoIdentidade getCpTipoIdentidade() {
		return cpTipoIdentidade;
	}

	/**
	 * @return the dpPessoa
	 */
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	/**
	 * @return the dscSenhaIdentidade
	 */
	public String getDscSenhaIdentidade() {
		return dscSenhaIdentidade;
	}

	public String getDscSenhaIdentidadeCripto() {
		return dscSenhaIdentidadeCripto;
	}

	public String getDscSenhaIdentidadeCriptoSinc() {
		return dscSenhaIdentidadeCriptoSinc;
	}

	/**
	 * @return the dtCancelamentoIdentidade
	 */
	public Date getDtCancelamentoIdentidade() {
		return dtCancelamentoIdentidade;
	}

	/**
	 * @return the dtCriacaoIdentidade
	 */
	public Date getDtCriacaoIdentidade() {
		return dtCriacaoIdentidade;
	}

	/**
	 * @return the dtExpiracaoIdentidade
	 */
	public Date getDtExpiracaoIdentidade() {
		return dtExpiracaoIdentidade;
	}

	/**
	 * @return the idCpIdentidade
	 */
	public Long getIdIdentidade() {
		return idIdentidade;
	}

	/**
	 * @return the nmLoginIdentidade
	 */
	public String getNmLoginIdentidade() {
		return nmLoginIdentidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dscSenhaIdentidade == null) ? 0 : dscSenhaIdentidade
						.hashCode());
		result = prime
				* result
				+ ((dtCancelamentoIdentidade == null) ? 0
						: dtCancelamentoIdentidade.hashCode());
		result = prime
				* result
				+ ((dtCriacaoIdentidade == null) ? 0 : dtCriacaoIdentidade
						.hashCode());
		result = prime
				* result
				+ ((dtExpiracaoIdentidade == null) ? 0 : dtExpiracaoIdentidade
						.hashCode());
		result = prime * result
				+ ((getHisDtFim() == null) ? 0 : getHisDtFim().hashCode());
		result = prime * result
				+ ((getHisDtIni() == null) ? 0 : getHisDtIni().hashCode());
		result = prime * result
				+ ((idIdentidade == null) ? 0 : idIdentidade.hashCode());
		result = prime
				* result
				+ ((nmLoginIdentidade == null) ? 0 : nmLoginIdentidade
						.hashCode());
		return result;
	}

	/**
	 * @param cpOrgaoUsuario
	 *            the cpOrgaoUsuario to set
	 */
	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

	/**
	 * @param cpTipoIdentidade
	 *            the cpTipoIdentidade to set
	 */
	public void setCpTipoIdentidade(CpTipoIdentidade cpTipoIdentidade) {
		this.cpTipoIdentidade = cpTipoIdentidade;
	}

	/**
	 * @param dpPessoa
	 *            the dpPessoa to set
	 */
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	/**
	 * @param dscSenhaIdentidade
	 *            the dscSenhaIdentidade to set
	 */
	public void setDscSenhaIdentidade(String dscSenhaIdentidade) {
		this.dscSenhaIdentidade = dscSenhaIdentidade;
	}

	public void setDscSenhaIdentidadeCripto(String senha) {
		this.dscSenhaIdentidadeCripto = senha;
	}

	public void setDscSenhaIdentidadeCriptoSinc(String senha) {
		this.dscSenhaIdentidadeCriptoSinc = senha;
	}

	/**
	 * @param dtCancelamentoIdentidade
	 *            the dtCancelamentoIdentidade to set
	 */
	public void setDtCancelamentoIdentidade(Date dtCancelamentoIdentidade) {
		this.dtCancelamentoIdentidade = dtCancelamentoIdentidade;
	}

	/**
	 * @param dtCriacaoIdentidade
	 *            the dtCriacaoIdentidade to set
	 */
	public void setDtCriacaoIdentidade(Date dtCriacaoIdentidade) {
		this.dtCriacaoIdentidade = dtCriacaoIdentidade;
	}

	/**
	 * @param dtExpiracaoIdentidade
	 *            the dtExpiracaoIdentidade to set
	 */
	public void setDtExpiracaoIdentidade(Date dtExpiracaoIdentidade) {
		this.dtExpiracaoIdentidade = dtExpiracaoIdentidade;
	}

	/**
	 * @param idCpIdentidade
	 *            the idCpIdentidade to set
	 */
	public void setIdIdentidade(Long idCpIdentidade) {
		this.idIdentidade = idCpIdentidade;
	}

	/**
	 * @param nmLoginIdentidade
	 *            the nmLoginIdentidade to set
	 */
	public void setNmLoginIdentidade(String nmLoginIdentidade) {
		this.nmLoginIdentidade = nmLoginIdentidade;
	}

	// RECUPERACAO

	/**
	 * @return the telefoneRecuperacao
	 */
	public String getTelefoneRecuperacao() {
		return telefoneRecuperacao;
	}

	/**
	 * @param telefoneRecuperacao the telefoneRecuperacao to set
	 */
	public void setTelefoneRecuperacao(String telefoneRecuperacao) {
		this.telefoneRecuperacao = telefoneRecuperacao;
	}

	/**
	 * @return the emailRecuperacao
	 */
	public String getEmailRecuperacao() {
		return emailRecuperacao;
	}
	
	/**
	 * Salva / substitui o e-mail pessoal a ser usado para recuperacao automatica de senha, 
	 * e CASO O NOVO E-MAIL DE RECUPERACAO NAO SEJA NULO, 
	 * prepara o registro para a validacao deste e-mail e retorna o token de validacao a ser 
	 * enviado para a confirmacao do endereco.
	 * CASO O NOVO E-MAIL DE RECUPERACAO SEJA NULO, 
	 * retorna nulo. 
	 * @param emailRecuperacao O endereco de e-mail a usar para Recuperacao automatica da senha
	 * @return O token de validacao do e-mail salvo, a ser enviado para esse mesmo endereco.
	 */
	public String trocarEmailRecuperacao(String emailRecuperacao) {
		if("".equals(emailRecuperacao))
			emailRecuperacao = null;
		this.emailRecuperacao = emailRecuperacao;
		
		if(emailRecuperacao != null) {
			return this.generateTokenRecuperacao(null);
		} else {
			this.resetDtExpiracaoTokenRecuperacao(null);
			return null;
		}
	}

	/**
	 * @return the tokenRecuperacao
	 */
	public String generateTokenRecuperacao(Date dataEHoraDoServidor) {
		this.setTokenRecuperacao(UUID.randomUUID().toString());
		this.resetDtExpiracaoTokenRecuperacao(dataEHoraDoServidor);
		return tokenRecuperacao;
	}

	/**
	 * Compara um token com o token salvo para confirmacao do e-mail. 
	 * @param token o token a validar
	 * @return true if given token is identical to 
	 */
	public boolean validarEmailRecuperacao(String token) {
		if(this.getEmailRecuperacao() == null || this.getEmailRecuperacao().equals("")) return false;
		return tokenRecuperacao.equals(token);
	}

	/**
	 * @param tokenRecuperacao the tokenRecuperacao to evaluate
	 * @return true if given token is identical to 
	 */
	public boolean validarTokenRecuperacao(String token, Date data) {
		boolean tokenIgual = this.tokenRecuperacao.equals(token);
		
		boolean dataValida = this.dtExpiracaoTokenRecuperacao.compareTo(data) >= 0;
		
		return tokenIgual && dataValida;
	}

	/**
	 * @return the dtExpiracaoRecuperacao
	 */
	public Date getDtExpiracaoTokenRecuperacao() {
		return dtExpiracaoTokenRecuperacao;
	}

	/**
	 * @param dataEHoraDoServidor data e hora atuais OU nulo para apagar
	 * @param horas Quantidade de horas para expirar token, ignorado caso dataEHoraDoServidor seja nulo
	 * @return data e hora de expiracao do token OU nulo
	 */
	public Date resetDtExpiracaoTokenRecuperacao(Date dataEHoraDoServidor, int horas) {
		if(dataEHoraDoServidor != null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(dataEHoraDoServidor);
		    calendar.add(Calendar.HOUR_OF_DAY, horas);
			
			this.dtExpiracaoTokenRecuperacao = calendar.getTime();
		} else {
			this.dtExpiracaoTokenRecuperacao = null;
		}
		
		return this.dtExpiracaoTokenRecuperacao;
	}

	/**
	 * Configura a expiracao do token em 1 hora 
	 * Caso o parametro seja nulo, apaga o valor do campo
	 * @param dataEHoraDoServidor data e hora atuais OU nulo para apagar
	 * @return data e hora de expiracao do token OU null
	 */
	public Date resetDtExpiracaoTokenRecuperacao(Date dataEHoraDoServidor) {
		return resetDtExpiracaoTokenRecuperacao(dataEHoraDoServidor, 1);
	}

	/**
	 * @return se o e-mail de recuperacao foi confirmado
	 */
	public boolean emailRecuperacaoFoiConfirmado() {
		return dtConfirmacaoEmailRecuperacao != null;
	}

	/**
	 * @param dtConfirmacaoEmailRecuperacao the dtConfirmacaoEmailRecuperacao to set
	 */
	public void setDtConfirmacaoEmailRecuperacao(Date dtConfirmacaoEmailRecuperacao) {
		this.dtConfirmacaoEmailRecuperacao = dtConfirmacaoEmailRecuperacao;
	}

	public Date getDtConfirmacaoEmailRecuperacao() {
		return this.dtConfirmacaoEmailRecuperacao;
	}

	/**
	 * ATENCAO - evitar usar essa funcao. Use validateTokenRecuperacao 
	 * @return the tokenRecuperacao
	 */
	public String getTokenRecuperacao() {
		return tokenRecuperacao;
	}

	/**
	 * ATENCAO - nao use essa funcao! Use generateTokenRecuperacao
	 * @param tokenRecuperacao the tokenRecuperacao to set
	 */
	public void setTokenRecuperacao(String tokenRecuperacao) {
		this.tokenRecuperacao = tokenRecuperacao;
	}

	/**
	 * @param emailRecuperacao the emailRecuperacao to set
	 */
	public void setEmailRecuperacao(String emailRecuperacao) {
		this.emailRecuperacao = emailRecuperacao;
	}

	/**
	 * @param dtExpiracaoTokenRecuperacao the dtExpiracaoTokenRecuperacao to set
	 */
	public void setDtExpiracaoTokenRecuperacao(Date dtExpiracaoTokenRecuperacao) {
		this.dtExpiracaoTokenRecuperacao = dtExpiracaoTokenRecuperacao;
	}

}
