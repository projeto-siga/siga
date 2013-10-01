package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import notifiers.Correio;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import util.SigaPlayCalendar;

@Entity
@Table(name = "SR_MOVIMENTACAO", schema = "SIGASR")
public class SrMovimentacao extends GenericModel {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_MOVIMENTACAO_SEQ", name = "srMovimentacaoSeq")
	@GeneratedValue(generator = "srMovimentacaoSeq")
	@Column(name = "ID_MOVIMENTACAO")
	public long idMovimentacao;

	@Column(name = "DESCR_MOVIMENTACAO")
	public String descrMovimentacao;

	@Column(name = "DT_INI_MOV")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtIniMov;

	@Column(name = "DT_FIM_MOV")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtFimMov;

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
	@JoinColumn(name = "ID_LISTA")
	public SrLista lista;

	@ManyToOne
	@JoinColumn(name = "ID_CANCELADOR")
	public DpPessoa cancelador;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CANCELADOR")
	public DpLotacao lotaCancelador;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOV_CANCELADORA")
	public SrMovimentacao idmovCanceladora;

	@Column(name = "DT_MOV_CANCELADORA")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtCancelamento;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	public SrTipoMovimentacao tipoMov;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_REF")
	public SrMovimentacao idMovRef;

	@Column(name = "NUM_SEQUENCIA")
	public Long numSequencia;

	@Column(name = "ID_PRIORIDADE")
	public Long prioridade;

	public SrMovimentacao() throws Exception {
		this(null);
	}

	public SrMovimentacao(SrSolicitacao sol) throws Exception {
		this.solicitacao = sol;
		if (this.solicitacao != null) {
			SrMovimentacao ultMov = solicitacao.getUltimaMovimentacao();
			if (ultMov != null) {
				this.lotaAtendente = ultMov.lotaAtendente;
				this.atendente = ultMov.atendente;
			}
		}
	}

	public boolean isCancelado() {
		return dtCancelamento != null;
	}

	public boolean isPrimeiraMovimentacao() {
		SrMovimentacao primeiro = null;
		for (SrMovimentacao movimentacao : solicitacao.getMovimentacaoSet())
			primeiro = movimentacao;
		return (primeiro == null || primeiro.equals(this));
	}

	public Long getnumSequencia() {
		numSequencia = find(
				"select max(mov.numSequencia)+1 from SrMovimentacao mov, SrSolicitacao sol where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.solicitacao = " + solicitacao.getId())
				.first();
		return (numSequencia != null) ? numSequencia : 1;
	}

	public Long getProximaMovimentacao() {
		Long num = find(
				"select max(mov.numSequencia)+1 from SrMovimentacao mov, SrSolicitacao sol where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.solicitacao = " + solicitacao.getId())
				.first();
		return (num != null) ? num : 1;
	}

	public Long getMovimentacaoAtual() {
		Long num = find(
				"select max(mov.numSequencia) from SrMovimentacao mov, SrSolicitacao sol where sol.idSolicitacao = mov.solicitacao "
						+ "and mov.solicitacao = " + solicitacao.getId())
				.first();
		return (num != null) ? num : 1;
	}

	public String getDtIniString() {
		SigaPlayCalendar cal = new SigaPlayCalendar();
		cal.setTime(dtIniMov);
		return cal.getTempoTranscorridoString(false);
	}

	public String getDtIniMovDDMMYYHHMM() {
		if (dtIniMov != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
			return df.format(dtIniMov);
		}
		return "";
	}

	public String getAtendenteString() {
		if (atendente != null)
			return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
		else
			return lotaAtendente.getSigla();
	}

	public String getDtIniMovDDMMYYYYHHMM() {
		if (dtIniMov != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtIniMov);
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

	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public boolean temAtendenteOuLota() {
		return (atendente != null || lotaAtendente != null);
	}

	@Override
	public SrMovimentacao save() {
		try {
			salvar();
		} catch (Exception e) {
			//
		}
		return this;
	}

	public SrMovimentacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		return salvar();
	}

	public SrMovimentacao salvar() throws Exception {
		checarCampos();
		super.save();
		// Edson: atualizando o srMovimentacaoSet...
		solicitacao.refresh();
		solicitacao.atualizarMarcas(solicitacao);
		if (isPrimeiraMovimentacao()
				&& solicitacao.formaAcompanhamento != SrFormaAcompanhamento.NUNCA
				&& !(solicitacao.formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
						&& tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO && tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO))
			notificar();
		return this;
	}

	public void desfazer(DpPessoa pessoa, DpLotacao lota) throws Exception {
		dtCancelamento = new Date();
		lotaCancelador = lota;
		cancelador = pessoa;
		tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
		salvar();
	}

	private void checarCampos() throws Exception {

		if (solicitacao == null)
			throw new Exception(
					"Movimentação precisa fazer parte de uma solicitação");

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		dtIniMov = new Date();

		checarCamposConsiderandoSolicitacao();

		if (tipoMov == null)
			tipoMov = SrTipoMovimentacao
					.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);

		if (atendente == null && lotaAtendente == null)
			throw new Exception("Atendente não pode ser nulo");

		if (lotaAtendente == null)
			lotaAtendente = atendente.getLotacao();
	}

	private void checarCamposConsiderandoSolicitacao() throws Exception {

		if (!solicitacao.temMovimentacao())
			return;

		SrMovimentacao anterior = solicitacao.getUltimaMovimentacao();

		if (!temAtendenteOuLota()) {
			lotaAtendente = anterior.lotaAtendente;
			atendente = anterior.atendente;
		}

		if (isCancelado()) {
			numSequencia = getMovimentacaoAtual();
		} else
			numSequencia = getProximaMovimentacao();

	}

	public void notificar() {
		if (!isCancelado())
			Correio.notificarMovimentacao(this);
		else
			Correio.notificarCancelamentoMovimentacao(this);
	}

}
