package models.siga;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

/**
 * A class that represents a row in the CP_CLASSIFICACAO table. You can
 * customize the behavior of this class by editing the class, {@link
 * CpClassificacao()}.
 */
@Entity
@Table(name = "CP_CONFIGURACAO", schema = "CORPORATIVO")
@Inheritance(strategy = InheritanceType.JOINED)
public class PlayConfiguracao extends PlayHistoricoSuporte
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4514355304185987860L;

	@Id
	//Ver comentário em sigasr.controllers.Util.nextVal() 
	//@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	//@SequenceGenerator(name = "generator", sequenceName = "CP_CONFIGURACAO_SEQ")
	@Column(name = "ID_CONFIGURACAO", nullable = false)
	@Desconsiderar
	private Long idConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@NaoRecursivo
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	@NaoRecursivo
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CARGO")
	@NaoRecursivo
	private DpCargo cargo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FUNCAO_CONFIANCA")
	@NaoRecursivo
	private DpFuncaoConfianca funcaoConfianca;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	@NaoRecursivo
	private DpPessoa dpPessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SIT_CONFIGURACAO")
	@NaoRecursivo
	private CpSituacaoConfiguracao cpSituacaoConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_CONFIGURACAO")
	@NaoRecursivo
	private CpTipoConfiguracao cpTipoConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO")
	@NaoRecursivo
	private CpServico cpServico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_IDENTIDADE")
	@NaoRecursivo
	private CpIdentidade cpIdentidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO")
	@NaoRecursivo
	private CpGrupo cpGrupo;

	@Column(name = "NM_EMAIL")
	@NaoRecursivo
	private String nmEmail;

	@Column(name = "DESC_FORMULA")
	@NaoRecursivo
	private String dscFormula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_LOTACAO")
	@NaoRecursivo
	private CpTipoLotacao cpTipoLotacao;

	@Column(name = "DT_INI_VIG_CONFIGURACAO")
	@Temporal(TemporalType.DATE)
	private Date dtIniVigConfiguracao;

	@Column(name = "DT_FIM_VIG_CONFIGURACAO")
	@Temporal(TemporalType.DATE)
	private Date dtFimVigConfiguracao;

	public CpIdentidade getCpIdentidade() {
		return cpIdentidade;
	}

	public void setCpIdentidade(CpIdentidade cpIdentidade) {
		this.cpIdentidade = cpIdentidade;
	}

	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof CpConfiguracao))
			return false;
		final CpConfiguracao that = (CpConfiguracao) rhs;

		if ((this.getIdConfiguracao() == null ? that.getIdConfiguracao() == null
				: this.getIdConfiguracao().equals(that.getIdConfiguracao())))
			return true;
		return false;

	}

	public Long getIdConfiguracao() {
		return idConfiguracao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		final int idValue = this.getIdConfiguracao() == null ? 0 : this
				.getIdConfiguracao().hashCode();
		result = result * 37 + idValue;

		return result;

	}

	public void setIdConfiguracao(final Long idConfiguracao) {
		this.idConfiguracao = idConfiguracao;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public DpCargo getCargo() {
		return cargo;
	}

	public void setCargo(DpCargo cargo) {
		this.cargo = cargo;
	}

	public DpFuncaoConfianca getFuncaoConfianca() {
		return funcaoConfianca;
	}

	public void setFuncaoConfianca(DpFuncaoConfianca funcaoConfianca) {
		this.funcaoConfianca = funcaoConfianca;
	}

	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	public CpSituacaoConfiguracao getCpSituacaoConfiguracao() {
		return cpSituacaoConfiguracao;
	}

	public void setCpSituacaoConfiguracao(
			CpSituacaoConfiguracao cpSituacaoConfiguracao) {
		this.cpSituacaoConfiguracao = cpSituacaoConfiguracao;
	}

	public CpTipoConfiguracao getCpTipoConfiguracao() {
		return cpTipoConfiguracao;
	}

	public void setCpTipoConfiguracao(CpTipoConfiguracao cpTipoConfiguracao) {
		this.cpTipoConfiguracao = cpTipoConfiguracao;
	}

	public CpServico getCpServico() {
		return cpServico;
	}

	public void setCpServico(CpServico cpServico) {
		this.cpServico = cpServico;
	}

	/**
	 * @return the cpGrupo
	 */
	public CpGrupo getCpGrupo() {
		return cpGrupo;
	}

	/**
	 * @param cpGrupo
	 *            the cpGrupo to set
	 */
	public void setCpGrupo(CpGrupo cpGrupo) {
		this.cpGrupo = cpGrupo;
	}

	/**
	 * @return the nmEmail
	 */
	public String getNmEmail() {
		return nmEmail;
	}

	/**
	 * @param nmEmail
	 *            the nmEmail to set
	 */
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	/**
	 * @return the dscFormula
	 */
	public String getDscFormula() {
		return dscFormula;
	}

	/**
	 * @param dscFormula
	 *            the dscFormula to set
	 */
	public void setDscFormula(String dscFormula) {
		this.dscFormula = dscFormula;
	}

	/**
	 * @return the cpTipoLotacao
	 */
	public CpTipoLotacao getCpTipoLotacao() {
		return cpTipoLotacao;
	}

	public Date getDtIniVigConfiguracao() {
		return dtIniVigConfiguracao;
	}

	public void setDtIniVigConfiguracao(Date dtIniVigConfiguracao) {
		this.dtIniVigConfiguracao = dtIniVigConfiguracao;
	}

	public Date getDtFimVigConfiguracao() {
		return dtFimVigConfiguracao;
	}

	public void setDtFimVigConfiguracao(Date dtFimVigConfiguracao) {
		this.dtFimVigConfiguracao = dtFimVigConfiguracao;
	}

	/**
	 * @param cpTipoLotacao
	 *            the cpTipoLotacao to set
	 */
	public void setCpTipoLotacao(CpTipoLotacao cpTipoLotacao) {
		this.cpTipoLotacao = cpTipoLotacao;
	}
	
	public boolean isEspecifica(CpConfiguracao filtro) {
		if (filtro.getDpPessoa() != null)
			return getDpPessoa() != null;
		if (filtro.getLotacao() != null)
			return getLotacao() != null;
		if (filtro.getOrgaoUsuario() != null)
			return getOrgaoUsuario() != null;
		if (filtro.getCpGrupo() != null)
			return getCpGrupo() != null
					&& getCpGrupo().getId().equals(filtro.getCpGrupo().getId());
		return false;
	}

	public Long getId() {
		return getIdConfiguracao();
	}

	public void setId(Long id) {
		setIdConfiguracao(id);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	/**
	 * 
	 * @return retorna o objeto que é a origem da configuração
	 */
	public Object getOrigem() {
		if (getDpPessoa() != null) {
			return getDpPessoa();
		} else if (getLotacao() != null) {
			return getLotacao();
		} else if (getCpGrupo() != null) {
			return getCpGrupo();
		} else if (getOrgaoUsuario() != null) {
			return getOrgaoUsuario();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return retorna uma string representativa da origem para exibições curtas
	 */
	public String printOrigemCurta() {
		Object ori = getOrigem();
		if (ori instanceof DpPessoa) {
			DpPessoa pes = (DpPessoa) ori;
			return (pes.getSesbPessoa() + pes.getMatricula());
		} else if (ori instanceof DpLotacao) {
			return ((DpLotacao) ori).getSiglaLotacao();
		} else if (ori instanceof CpGrupo) {
			return ((CpGrupo) ori).getSiglaGrupo();
		} else if (ori instanceof CpOrgaoUsuario) {
			return ((CpOrgaoUsuario) ori).getSiglaOrgaoUsu();
		} else {
			return new String();
		}
	}

	/**
	 * 
	 * @return retorna uma String representativa da origem
	 */
	public String printOrigem() {
		Object ori = getOrigem();
		if (ori instanceof DpPessoa) {
			return ((DpPessoa) ori).getNomePessoa();
		} else if (ori instanceof DpLotacao) {
			return ((DpLotacao) ori).getNomeLotacao();
		} else if (ori instanceof CpGrupo) {
			return ((CpGrupo) ori).getDescricao();
		} else if (ori instanceof CpOrgaoUsuario) {
			return ((CpOrgaoUsuario) ori).getNmOrgaoUsu();
		} else {
			return new String();
		}
	}

	public boolean ativaNaData(Date dt) {
		return super.ativoNaData(dt);
	}

	/**
	 * Retorna a data de fim de vigência no formato dd/mm/aa HH:MM:SS, por
	 * exemplo, 01/02/10 17:52:23.
	 */
	public String getHisDtFimDDMMYY_HHMMSS() {
		if (getHisDtFim() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getHisDtFim());
		}
		return "";
	}

	public String getHisDtIniDDMMYY() {
		if (getHisDtIni() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getHisDtIni());
		}
		return "";
	}

}