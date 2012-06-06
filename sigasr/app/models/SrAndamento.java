package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import controllers.SrCalendar;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_ANDAMENTO")
public class SrAndamento extends GenericModel {

	@Id
	@GeneratedValue
	@Column(name = "ID_ANDAMENTO")
	public long idAndamento;

	@Column(name = "DESCR_ANDAMENTO")
	public String descrAndamento;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

	@JoinColumn(name = "ID_ESTADO", nullable = false)
	public SrEstado estado;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	public SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	public DpPessoa atendente;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_ATENDENTE", nullable = false)
	public DpLotacao lotaAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE", nullable = false)
	public DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	public DpLotacao lotaCadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO")
	public SrSolicitacao solicitacao;

	public SrAndamento() {
		descrAndamento = "Dando andamento ao chamado...";
	}

	public SrAndamento getAndamentoAnterior() {
		return solicitacao.getAndamentoAnterior(this);
	}

	/*
	 * public boolean isTransferenciaParaPessoa() { return (atendente != null &&
	 * (getAndamentoAnterior() == null || getAndamentoAnterior().atendente ==
	 * null || !getAndamentoAnterior().atendente .equivale(atendente))); }
	 * 
	 * public boolean isTransferenciaParaLotacao() { return (atendente == null
	 * && (getAndamentoAnterior() == null ||
	 * (!getAndamentoAnterior().lotaAtendente .equivale(lotaAtendente)))); }
	 * 
	 * public boolean isRecebimentoPorPessoa() { return getAndamentoAnterior()
	 * != null && getAndamentoAnterior().isTransferenciaParaPessoa() &&
	 * cadastrante.equivale(getAndamentoAnterior().atendente); }
	 * 
	 * public boolean isRecebimentoPorLotacao() { return getAndamentoAnterior()
	 * != null && getAndamentoAnterior().isTransferenciaParaLotacao() &&
	 * lotaCadastrante .equivale(getAndamentoAnterior().lotaAtendente); }
	 * 
	 * public String getTitulo() { String titulo = "";
	 * 
	 * if (isRecebimentoPorPessoa()) titulo += "Recebimento por " +
	 * cadastrante.getDescricaoIniciaisMaiusculas() + ". ";
	 * 
	 * if (isRecebimentoPorLotacao()) titulo += "Recebimento por " +
	 * lotaCadastrante.getSigla() + ". ";
	 * 
	 * if (isTransferenciaParaPessoa()) titulo += "Transferência para " +
	 * atendente.getDescricaoIniciaisMaiusculas() + " (" +
	 * lotaAtendente.getSigla() + ")";
	 * 
	 * if (isTransferenciaParaLotacao()) titulo += "Transferência para " +
	 * lotaAtendente.getSigla();
	 * 
	 * if (titulo.length() == 0) titulo = "Comentário";
	 * 
	 * return titulo; }
	 */
	public String getDtRegString() {
		SrCalendar cal = new SrCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString();
	}

	public String getAtendenteString() {
		if (atendente != null)
			return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
		else
			return lotaAtendente.getSigla();
	}

	public String getCadastranteString() {
		return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
	}

}
