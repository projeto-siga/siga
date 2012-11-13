package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import notifiers.Correio;

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

	@ManyToOne(cascade = CascadeType.ALL)
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

	@OneToOne
	@JoinColumn(name = "ID_ANDAMENTO_CANCELADOR")
	public SrAndamento andamentoCancelador;

	@OneToOne(mappedBy = "andamentoCancelador")
	public SrAndamento andamentoCancelado;

	public SrAndamento() {
		descrAndamento = "Dando andamento ao chamado...";
	}

	public SrAndamento(SrEstado estado, String descricao, DpPessoa atendente,
			DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) {

		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.atendente = atendente;
		this.lotaAtendente = lotaAtendente;
		this.descrAndamento = descricao;
		this.estado = estado;

	}

	public SrAndamento getAndamentoAnterior() {
		return solicitacao.getAndamentoAnterior(this);
	}

	public boolean isCancelador() {
		return andamentoCancelado != null;
	}

	public boolean isCancelado() {
		return andamentoCancelador != null;
	}

	public boolean isCriacao() {
		return !solicitacao.houveAndamentosSemContarCriacao();

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

	public String getDtRegDDMMYYYYHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtReg);
		}
		return "";
	}

	public String getCadastranteString() {
		return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
	}

	public String getSituacao() {
		return estado.descrEstado + " (" + lotaAtendente.getSigla() + ")";
	}

	// Necessário porque não há binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public boolean temAtendenteOuLota() {
		return (atendente != null || lotaAtendente != null);
	}

	public SrAndamento salvar() throws Exception {

		checarECompletarCampos();

		super.save();

		// O refresh é ecessário para o hibernate incluir o novo andamento na
		// coleção de andamentos da solicitação
		// Plim
		refresh();

		solicitacao.atualizarMarcas();

		if (solicitacao.isPreAtendido())
			notificar();

		return this;

	}

	public void checarECompletarCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		dtReg = new Date();

		if (atendente != null && lotaAtendente == null)
			lotaAtendente = atendente.getLotacao();

		if (solicitacao.houveQualquerAndamento()) {

			SrAndamento anterior = solicitacao.getUltimoAndamento();

			if (!temAtendenteOuLota()) {

				lotaAtendente = anterior.lotaAtendente;

				atendente = anterior.atendente;
			}
			if (estado == null)
				estado = anterior.estado;

		}

		if (atendente == null)
			throw new Exception("Atendente não pode ser nulo");
	}

	public void notificar() {
		Correio.notificarAndamento(this);
	}

}
