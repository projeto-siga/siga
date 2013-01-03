package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	@ManyToOne
	@JoinColumn(name = "ID_CANCELADOR")
	public DpPessoa cancelador;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CANCELADOR")
	public DpLotacao lotaCancelador;

	@Column(name = "DT_CANCELAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtCancelamento;

	public SrAndamento() throws Exception {
		this(null);
	}

	// Edson: O objetivo de ter um construtor com parâmetro é evitar que os
	// valores default setados dentro dele acabem sendo chamados pelo
	// framework a cada request, pois a ideia é esses valores serem
	// definidos só no início da operação
	public SrAndamento(SrSolicitacao sol) throws Exception {
		this.solicitacao = sol;
		if (solicitacao != null && solicitacao.temAndamento())
			estado = solicitacao.getUltimoAndamento().estado;
	}

	public SrAndamento(SrEstado estado, String descricao, DpPessoa atendente,
			DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante, SrSolicitacao sol) {

		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.atendente = atendente;
		this.lotaAtendente = lotaAtendente;
		this.descrAndamento = descricao;
		this.estado = estado;
		this.solicitacao = sol;

	}

	public boolean isCancelado() {
		return dtCancelamento != null;
	}

	public boolean isPrimeiroAndamento() {
		List<SrAndamento> andamentos = solicitacao.getAndamentoSet();
		return (andamentos.indexOf(this) == andamentos.size() - 1);
	}

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

	public String getDtCancelamentoDDMMYYYYHHMM() {
		if (dtCancelamento != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtCancelamento);
		}
		return "";
	}

	public String getCadastranteString() {
		return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
	}

	public String getSituacaoString() {
		return estado.descrEstado + " (" + lotaAtendente.getSigla() + ")";
	}

	// Necessário porque não há binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public boolean temAtendenteOuLota() {
		return (atendente != null || lotaAtendente != null);
	}

	public boolean isProxAtendenteAlteravel() throws Exception {
		return estado == SrEstado.ANDAMENTO
				&& (!solicitacao.isEmPreAtendimento() || !solicitacao
						.temAtendenteDesignado());
	}

	public SrAndamento deduzirProxAtendente() throws Exception {

		this.lotaAtendente = null;
		this.atendente = null;
		if (estado == SrEstado.FECHADO || estado == SrEstado.POS_ATENDIMENTO)
			this.lotaAtendente = solicitacao.getPosAtendenteDesignado();
		if (estado == SrEstado.PRE_ATENDIMENTO)
			this.lotaAtendente = solicitacao.getPreAtendenteDesignado();
		if (estado == SrEstado.ANDAMENTO && solicitacao.isEmPreAtendimento())
			this.lotaAtendente = solicitacao.getAtendenteDesignado();

		if (this.lotaAtendente == null && solicitacao != null) {
			this.lotaAtendente = solicitacao.getLotaAtendente();
			this.atendente = solicitacao.getAtendente();
		}
		return this;
	}

	@Override
	public SrAndamento save() {
		try {
			salvar();
		} catch (Exception e) {
			//
		}
		return this;
	}

	public SrAndamento salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		return salvar();
	}

	public SrAndamento salvar() throws Exception {
		checarCampos();
		super.save();
		return this;
	}

	public void desfazer(DpPessoa pessoa, DpLotacao lota) throws Exception {
		dtCancelamento = new Date();
		lotaCancelador = lota;
		cancelador = pessoa;
		salvar();
	}

	private void checarCampos() throws Exception {

		if (solicitacao == null)
			throw new Exception(
					"Andamento precisa fazer parte de uma solicitação");

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		dtReg = new Date();

		checarCamposConsiderandoSolicitacao();

		if (atendente == null && lotaAtendente == null)
			throw new Exception("Atendente não pode ser nulo");

		if (lotaAtendente == null)
			lotaAtendente = atendente.getLotacao();
	}

	private void checarCamposConsiderandoSolicitacao() throws Exception {

		if (!solicitacao.temAndamento())
			return;

		SrAndamento anterior = solicitacao.getUltimoAndamento();

		if (estado == SrEstado.ANDAMENTO && solicitacao.isEmPreAtendimento()) {
			atendente = null;
			lotaAtendente = solicitacao.getAtendenteDesignado();
		} else if (estado == SrEstado.FECHADO
				&& solicitacao.temPosAtendenteDesignado()
				&& !solicitacao.getLotaAtendente().equivale(
						solicitacao.getPosAtendenteDesignado())) {
			atendente = null;
			lotaAtendente = solicitacao.getPosAtendenteDesignado();
			estado = SrEstado.POS_ATENDIMENTO;
		} else if (estado == SrEstado.PRE_ATENDIMENTO) {
			atendente = null;
			lotaAtendente = solicitacao.getPreAtendenteDesignado();
		}

		if (!temAtendenteOuLota()) {
			lotaAtendente = anterior.lotaAtendente;
			atendente = anterior.atendente;
		}
		if (estado == null)
			estado = anterior.estado;

	}

	public void notificar() {
		if (!isCancelado())
			Correio.notificarAndamento(this);
		else
			Correio.notificarCancelamentoAndamento(this);
	}

}
