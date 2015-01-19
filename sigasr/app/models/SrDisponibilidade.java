package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@Table(name = "SR_DISPONIBILIDADE", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrDisponibilidade extends HistoricoSuporte {

	private static final long serialVersionUID = 7243562288736225097L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_DISPONIBILIDADE_SEQ", name = "srDisponibilidadeSeq")
	@GeneratedValue(generator = "srDisponibilidadeSeq")
	@Column(name = "ID_DISPONIBILIDADE")
	private Long idDisponibilidade;

	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private SrItemConfiguracao itemConfiguracao;

	@JoinColumn(name = "ID_ORGAO_USU NUMBER")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private CpOrgaoUsuario orgao;
	
	@Enumerated
	@Column(name = "TP_DISPONIBILIDADE")
	private SrTipoDisponibilidade tipo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_HR_INI", nullable = false)
	private Date dataHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_HR_FIM")
	private Date dataHoraTermino;

	@Column(name = "MSG", length = 50)
	private String mensagem;

	@Column(name = "DET_TECNICO", length = 50)
	private String detalhamentoTecnico;

	@Override
	public Long getId() {
		return idDisponibilidade;
	}

	@Override
	public void setId(Long id) {
		this.idDisponibilidade = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	public Long getIdDisponibilidade() {
		return idDisponibilidade;
	}

	public void setIdDisponibilidade(Long idDisponibilidade) {
		this.idDisponibilidade = idDisponibilidade;
	}

	public SrItemConfiguracao getItemConfiguracao() {
		return itemConfiguracao;
	}

	public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
		this.itemConfiguracao = itemConfiguracao;
	}

	public CpOrgaoUsuario getOrgao() {
		return orgao;
	}

	public void setOrgao(CpOrgaoUsuario orgao) {
		this.orgao = orgao;
	}

	public SrTipoDisponibilidade getTipo() {
		return tipo;
	}

	public void setTipo(SrTipoDisponibilidade tipo) {
		this.tipo = tipo;
	}

	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public Date getDataHoraTermino() {
		return dataHoraTermino;
	}

	public void setDataHoraTermino(Date dataHoraTermino) {
		this.dataHoraTermino = dataHoraTermino;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDetalhamentoTecnico() {
		return detalhamentoTecnico;
	}

	public void setDetalhamentoTecnico(String detalhamentoTecnico) {
		this.detalhamentoTecnico = detalhamentoTecnico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((idDisponibilidade == null) ? 0 : idDisponibilidade
						.hashCode());
		result = prime
				* result
				+ ((itemConfiguracao == null) ? 0 : itemConfiguracao.hashCode());
		result = prime * result + ((orgao == null) ? 0 : orgao.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SrDisponibilidade other = (SrDisponibilidade) obj;
		if (idDisponibilidade == null) {
			if (other.idDisponibilidade != null)
				return false;
		} else if (!idDisponibilidade.equals(other.idDisponibilidade))
			return false;
		if (itemConfiguracao == null) {
			if (other.itemConfiguracao != null)
				return false;
		} else if (!itemConfiguracao.equals(other.itemConfiguracao))
			return false;
		if (orgao == null) {
			if (other.orgao != null)
				return false;
		} else if (!orgao.equals(other.orgao))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
}
