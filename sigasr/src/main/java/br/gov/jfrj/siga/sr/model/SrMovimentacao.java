package br.gov.jfrj.siga.sr.model;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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

import org.joda.time.DateTime;

import play.db.jpa.GenericModel;
import util.SigaPlayCalendar;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.notifiers.Correio;

@Entity
@Table(name = "SR_MOVIMENTACAO", schema = "SIGASR")
public class SrMovimentacao extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	@JoinColumn(name = "ID_LOTA_ATENDENTE")
	public DpLotacao lotaAtendente;
	
	@ManyToOne
	@JoinColumn(name = "ID_DESIGNACAO")
	public SrConfiguracao designacao;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	public DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	public DpLotacao lotaCadastrante;
	
	@ManyToOne
	@JoinColumn(name = "ID_TITULAR")
	public DpPessoa titular;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_TITULAR")
	public DpLotacao lotaTitular;

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

	@OneToMany(targetEntity = SrResposta.class, mappedBy = "movimentacao", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	// @OrderBy("pergunta asc")
	protected List<SrResposta> respostaSet;
	
	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "movFinalizadora", fetch=FetchType.LAZY)
	protected List<SrMovimentacao> movFinalizadaSet;

	@Column(name = "DT_AGENDAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtAgenda;

	@Enumerated
	public SrTipoMotivoPendencia motivoPendencia;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOV_FINALIZADORA")
	public SrMovimentacao movFinalizadora;
	
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_REFERENCIA")
	public SrSolicitacao solicitacaoReferencia;
	
	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne
	@JoinColumn(name = "ID_ACAO")
	public SrAcao acao;
	
	@Enumerated
	public SrTipoMotivoEscalonamento motivoEscalonamento;

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

	public void setRespostaMap(Map<Long, String> respostaMap)
			throws Exception {
		respostaSet = new ArrayList<SrResposta>();
		for (Long idPergunta : respostaMap.keySet()){
			SrResposta resp = new SrResposta();
			resp.movimentacao = this;
			resp.pergunta = SrPergunta.findById(idPergunta);
			if (resp.pergunta.tipoPergunta.getIdTipoPergunta() == SrTipoPergunta.TIPO_PERGUNTA_TEXTO_LIVRE)
				resp.descrResposta = respostaMap.get(idPergunta);
			else
				resp.grauSatisfacao = SrGrauSatisfacao.valueOf(respostaMap.get(idPergunta));
			respostaSet.add(resp);
		}
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

	public boolean isFinalizada() {
		return movFinalizadora != null;
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
	
	public SrMovimentacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante, 
			DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		return salvar();
	}

	public SrMovimentacao salvar() throws Exception {

		//Edson: considerar deixar esse codigo em SrSolicitacao.movimentar(),
		//visto que sao chamadas muitas operacoes daquela classe

		checarCampos();
		super.save();
		solicitacao.refresh();

		solicitacao.atualizarMarcas();
		if (solicitacao.getMovimentacaoSetComCancelados().size() > 1
				&& tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
				&& solicitacao.formaAcompanhamento != SrFormaAcompanhamento.ABERTURA
				&& !(solicitacao.formaAcompanhamento == SrFormaAcompanhamento.ABERTURA_FECHAMENTO
				&& tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO && tipoMov.idTipoMov != SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO))
			notificar();
		
		//Necessaria condicao a parte, pois o solicitante pode escolher nunca receber notificacao (SrFormaAcompanhamento.NUNCA)
		if (solicitacao.isFilha() &&
				tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
			Correio.notificarAtendente(this); //notifica o atendente da solicitacao pai, caso a filha seja fechada
		return this;
	}

	public void desfazer(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		SrMovimentacao movCanceladora = new SrMovimentacao(this.solicitacao);
		movCanceladora.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
		movCanceladora.descrMovimentacao = "Cancelando "
				+ tipoMov.nome.toLowerCase();

		SrMovimentacao ultimaValida = getAnterior();
		movCanceladora.atendente = ultimaValida.atendente;
		movCanceladora.lotaAtendente = ultimaValida.lotaAtendente;
		movCanceladora.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
		
		this.movCanceladora = movCanceladora;
		
		if (movFinalizadaSet != null)
			for (SrMovimentacao movFinalizada : movFinalizadaSet){
				movFinalizada.movFinalizadora = null;
				movFinalizada.save();
			}
		movFinalizadaSet = new ArrayList<SrMovimentacao>();
		
		this.salvar();
		
	}

	private void checarCampos() throws Exception {

		if (solicitacao == null)
			throw new Exception(
					"Movimenta��o precisa fazer parte de uma solicita��o");

		if (arquivo != null) {
			double lenght = (double) arquivo.blob.length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) � maior que o m�ximo permitido (2MB)");
		}

		if (dtIniMov == null)
			dtIniMov = new Date();

		SrMovimentacao ultimaMovDoContexto = solicitacao
				.getUltimaMovimentacaoMesmoSeCanceladaTodoOContexto();
		
		if (ultimaMovDoContexto == null) {
			numSequencia = 1L;
		} else {
			SrMovimentacao anterior = solicitacao.getUltimaMovimentacao();

			if (lotaAtendente == null) {
				lotaAtendente = anterior.lotaAtendente;
			}

			if (numSequencia == null)
				numSequencia = ultimaMovDoContexto.numSequencia + 1;
		}

		if (tipoMov == null)
			tipoMov = SrTipoMovimentacao
			.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);

		if (!solicitacao.isRascunho()) {
			if (atendente == null && lotaAtendente == null)
				throw new Exception("Atendente n�o pode ser nulo");

			if (lotaAtendente == null)
				lotaAtendente = atendente.getLotacao();
		}
	}

	public void notificar() {
		if (tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO)
			Correio.notificarReplanejamentoMovimentacao(this);
		else if (!isCancelada())
			Correio.notificarMovimentacao(this);
		else
			Correio.notificarCancelamentoMovimentacao(this);
	}
	
	public String getMotivoPendenciaString() {
		return this.motivoPendencia != null ? this.motivoPendencia.descrTipoMotivoPendencia : "";
	}
	
	public SrMovimentacao getMovFinalizada(){
		if (movFinalizadaSet == null || movFinalizadaSet.size() == 0)
			return null;
		return movFinalizadaSet.get(0);
	}
	
	public Date getDtFimMov(){
		return movFinalizadora != null ? movFinalizadora.dtIniMov
				: dtAgenda;
	}

	public SrItemConfiguracao getItemConfiguracao() {
		return itemConfiguracao;
	}

	public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
		this.itemConfiguracao = itemConfiguracao;
	}

	public SrAcao getAcao() {
		return acao;
	}

	public void setAcao(SrAcao acao) {
		this.acao = acao;
	}
	
	public String getMotivoEscalonamentoString() {
		return this.motivoEscalonamento.descrTipoMotivoEscalonamento;
	}
}
