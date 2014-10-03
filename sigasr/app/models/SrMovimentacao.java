package models;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import notifiers.Correio;

import org.joda.time.DateTime;

import play.db.jpa.GenericModel;
import util.SigaPlayCalendar;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

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
	@JoinColumn(name = "ID_CADASTRANTE")
	public DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	public DpLotacao lotaCadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO")
	public SrSolicitacao solicitacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	public SrLista lista;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOV_CANCELADORA")
	public SrMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	public SrTipoMovimentacao tipoMov;

	@Column(name = "NUM_SEQUENCIA")
	public Long numSequencia;

	@Column(name = "ID_PRIORIDADE")
	public Long prioridade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	public SrPesquisa pesquisa;

	@OneToMany(targetEntity = SrResposta.class, mappedBy = "movimentacao", fetch=FetchType.LAZY)
	// @OrderBy("pergunta asc")
	protected List<SrResposta> respostaSet;

	@Column(name = "DT_AGENDAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtAgenda;

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

	public List<SrResposta> getRespostaSet() {
		if (respostaSet == null)
			return new ArrayList<SrResposta>();
		return respostaSet;
	}

	public List<SrResposta> setRespostaMap(HashMap<Long, Object> respostas)
			throws Exception {

		respostaSet = new ArrayList<SrResposta>();
		Iterator<Map.Entry<Long, Object>> entries = respostas.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Entry<Long, Object> entry = entries.next();
			SrResposta resp = new SrResposta();
			Long entrada = entry.getKey();
			resp.pergunta = SrPergunta.findById(entrada);
			if (resp.pergunta.tipoPergunta.idTipoPergunta == 1)
				resp.descrResposta = (String) entry.getValue();
			else
				resp.grauSatisfacao = (SrGrauSatisfacao) entry.getValue();
			respostaSet.add(resp);
		}
		return respostaSet;
	}

	public HashMap<Long, String> getRespostaMap() {
		HashMap<Long, String> map = new HashMap<Long, String>();
		if (respostaSet != null)
			for (SrResposta resp : respostaSet) {
				if (!resp.descrResposta.equals(""))
					map.put(resp.pergunta.idPergunta, resp.descrResposta);
				else
					map.put(resp.pergunta.idPergunta,
							resp.grauSatisfacao.descrGrauSatisfacao);
			}
		return map;
	}

	public boolean isCancelada() {
		return movCanceladora != null;
	}

	public boolean isCanceladoOuCancelador() {
		return isCancelada()
				|| tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
	}

	public SrMovimentacao getAnterior() {
		boolean pronto = false;
		for (SrMovimentacao mov : solicitacao.getMovimentacaoSet()) {
			if (pronto)
				return mov;
			if (mov.idMovimentacao == this.idMovimentacao)
				pronto = true;
		}
		return null;
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

	public String getDtAgendaDDMMYYHHMM() {
		if (dtAgenda != null) {
			DateTime dateTime = new DateTime(dtAgenda);
			return dateTime.toString("dd/MM/yyyy HH:mm");
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

	public String getCadastranteString() {
		return atendente.getSigla() + " (" + lotaAtendente.getSigla() + ")";
	}

	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public SrMovimentacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		return salvar();
	}

	public SrMovimentacao salvar() throws Exception {
		
		//Edson: considerar deixar esse codigo em SrSolicitacao.movimentar(),
		//visto que sao chamadas muitas operacoes daquela classe
		
		checarCampos();
		super.save();
		
		// Edson: atualizando o srMovimentacaoSet...
		if (solicitacao.meuMovimentacaoSet == null)
			solicitacao.meuMovimentacaoSet = new TreeSet<SrMovimentacao>();
		solicitacao.meuMovimentacaoSet.add(this);
		
		solicitacao.atualizarMarcas();
		if (solicitacao.getMovimentacaoSetComCancelados().size() > 1
				&& tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
				&& solicitacao.formaAcompanhamento != SrFormaAcompanhamento.NUNCA
				&& !(solicitacao.formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
						&& tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO && tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO))
			notificar();
		return this;
	}

	public void desfazer(DpPessoa pessoa, DpLotacao lota) throws Exception {
		SrMovimentacao movCanceladora = new SrMovimentacao(this.solicitacao);
		movCanceladora.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
		movCanceladora.descrMovimentacao = "Cancelando "
				+ tipoMov.nome.toLowerCase() + " nº " + numSequencia;

		SrMovimentacao ultimaValida = getAnterior();
		movCanceladora.atendente = ultimaValida.atendente;
		movCanceladora.lotaAtendente = ultimaValida.lotaAtendente;

		movCanceladora.salvar(pessoa, lota);
		this.movCanceladora = movCanceladora;
		this.salvar();
	}

	private void checarCampos() throws Exception {

		if (solicitacao == null)
			throw new Exception(
					"Movimentação precisa fazer parte de uma solicitação");

		solicitacao = solicitacao.solicitacaoInicial != null ? solicitacao.solicitacaoInicial
				: solicitacao;

		if (arquivo != null) {
			double lenght = (double) arquivo.blob.length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) � maior que o m�ximo permitido (2MB)");
		}

		if (dtIniMov == null)
			dtIniMov = new Date();

		if (solicitacao.getMovimentacaoSetComCancelados().size() == 0) {
			numSequencia = 1L;
		} else {
			SrMovimentacao anterior = solicitacao.getUltimaMovimentacao();

			if (atendente == null && lotaAtendente == null) {
				lotaAtendente = anterior.lotaAtendente;
				atendente = anterior.atendente;
			}

			if (numSequencia == null)
				numSequencia = solicitacao
						.getUltimaMovimentacaoMesmoSeCancelada().numSequencia + 1;
		}

		if (tipoMov == null)
			tipoMov = SrTipoMovimentacao
					.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);

		if (atendente == null && lotaAtendente == null)
			throw new Exception("Atendente não pode ser nulo");

		if (lotaAtendente == null)
			lotaAtendente = atendente.getLotacao();
	}

	public void notificar() {
		if (!isCancelada())
			Correio.notificarMovimentacao(this);
		else
			Correio.notificarCancelamentoMovimentacao(this);
	}
}
