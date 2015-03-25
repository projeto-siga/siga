package br.gov.jfrj.siga.sr.model;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sr.SrCorreio;

@Entity
@Table(name = "SR_MOVIMENTACAO", schema = "SIGASR")
public class SrMovimentacao extends Objeto {

	public static ActiveRecord<SrMovimentacao> AR = new ActiveRecord<>(SrMovimentacao.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_MOVIMENTACAO_SEQ", name = "srMovimentacaoSeq")
	@GeneratedValue(generator = "srMovimentacaoSeq")
	@Column(name = "ID_MOVIMENTACAO")
	private long idMovimentacao;

	@Column(name = "DESCR_MOVIMENTACAO")
	private String descrMovimentacao;

	@Column(name = "DT_INI_MOV")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniMov;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	private SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	private DpPessoa atendente;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_ATENDENTE")
	private DpLotacao lotaAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	private DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	private DpLotacao lotaCadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO")
	private SrSolicitacao solicitacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	private SrLista lista;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOV_CANCELADORA")
	private SrMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	private SrTipoMovimentacao tipoMov;

	@Column(name = "NUM_SEQUENCIA")
	private Long numSequencia;

	@Column(name = "ID_PRIORIDADE")
	private Long prioridade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	private SrPesquisa pesquisa;

	@OneToMany(targetEntity = SrResposta.class, mappedBy = "movimentacao", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	// @OrderBy("pergunta asc")
	protected List<SrResposta> respostaSet;
	
	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "movFinalizadora", fetch=FetchType.LAZY)
	protected List<SrMovimentacao> movFinalizadaSet;

	@Column(name = "DT_AGENDAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAgenda;

	@Enumerated
	private SrTipoMotivoPendencia motivoPendencia;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOV_FINALIZADORA")
	private SrMovimentacao movFinalizadora;
	
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_REFERENCIA")
	private SrSolicitacao solicitacaoReferencia;
	
	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	private SrItemConfiguracao itemConfiguracao;

	@ManyToOne
	@JoinColumn(name = "ID_ACAO")
	private SrAcao acao;
	
	@Enumerated
	private SrTipoMotivoEscalonamento motivoEscalonamento;

	/**
	 * @return the idMovimentacao
	 */
	public long getIdMovimentacao() {
		return idMovimentacao;
	}

	/**
	 * @param idMovimentacao the idMovimentacao to set
	 */
	public void setIdMovimentacao(long idMovimentacao) {
		this.idMovimentacao = idMovimentacao;
	}

	/**
	 * @return the descrMovimentacao
	 */
	public String getDescrMovimentacao() {
		return descrMovimentacao;
	}

	/**
	 * @param descrMovimentacao the descrMovimentacao to set
	 */
	public void setDescrMovimentacao(String descrMovimentacao) {
		this.descrMovimentacao = descrMovimentacao;
	}

	/**
	 * @return the dtIniMov
	 */
	public Date getDtIniMov() {
		return dtIniMov;
	}

	/**
	 * @param dtIniMov the dtIniMov to set
	 */
	public void setDtIniMov(Date dtIniMov) {
		this.dtIniMov = dtIniMov;
	}

	/**
	 * @return the arquivo
	 */
	public SrArquivo getArquivo() {
		return arquivo;
	}

	/**
	 * @param arquivo the arquivo to set
	 */
	public void setArquivo(SrArquivo arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * @return the atendente
	 */
	public DpPessoa getAtendente() {
		return atendente;
	}

	/**
	 * @param atendente the atendente to set
	 */
	public void setAtendente(DpPessoa atendente) {
		this.atendente = atendente;
	}

	/**
	 * @return the lotaAtendente
	 */
	public DpLotacao getLotaAtendente() {
		return lotaAtendente;
	}

	/**
	 * @param lotaAtendente the lotaAtendente to set
	 */
	public void setLotaAtendente(DpLotacao lotaAtendente) {
		this.lotaAtendente = lotaAtendente;
	}

	/**
	 * @return the cadastrante
	 */
	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	/**
	 * @param cadastrante the cadastrante to set
	 */
	public void setCadastrante(DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	/**
	 * @return the lotaCadastrante
	 */
	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	/**
	 * @param lotaCadastrante the lotaCadastrante to set
	 */
	public void setLotaCadastrante(DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	/**
	 * @return the solicitacao
	 */
	public SrSolicitacao getSolicitacao() {
		return solicitacao;
	}

	/**
	 * @param solicitacao the solicitacao to set
	 */
	public void setSolicitacao(SrSolicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	/**
	 * @return the lista
	 */
	public SrLista getLista() {
		return lista;
	}

	/**
	 * @param lista the lista to set
	 */
	public void setLista(SrLista lista) {
		this.lista = lista;
	}

	/**
	 * @return the movCanceladora
	 */
	public SrMovimentacao getMovCanceladora() {
		return movCanceladora;
	}

	/**
	 * @param movCanceladora the movCanceladora to set
	 */
	public void setMovCanceladora(SrMovimentacao movCanceladora) {
		this.movCanceladora = movCanceladora;
	}

	/**
	 * @return the tipoMov
	 */
	public SrTipoMovimentacao getTipoMov() {
		return tipoMov;
	}

	/**
	 * @param tipoMov the tipoMov to set
	 */
	public void setTipoMov(SrTipoMovimentacao tipoMov) {
		this.tipoMov = tipoMov;
	}

	/**
	 * @return the numSequencia
	 */
	public Long getNumSequencia() {
		return numSequencia;
	}

	/**
	 * @param numSequencia the numSequencia to set
	 */
	public void setNumSequencia(Long numSequencia) {
		this.numSequencia = numSequencia;
	}

	/**
	 * @return the prioridade
	 */
	public Long getPrioridade() {
		return prioridade;
	}

	/**
	 * @param prioridade the prioridade to set
	 */
	public void setPrioridade(Long prioridade) {
		this.prioridade = prioridade;
	}

	/**
	 * @return the pesquisa
	 */
	public SrPesquisa getPesquisa() {
		return pesquisa;
	}

	/**
	 * @param pesquisa the pesquisa to set
	 */
	public void setPesquisa(SrPesquisa pesquisa) {
		this.pesquisa = pesquisa;
	}

	/**
	 * @return the movFinalizadaSet
	 */
	public List<SrMovimentacao> getMovFinalizadaSet() {
		return movFinalizadaSet;
	}

	/**
	 * @param movFinalizadaSet the movFinalizadaSet to set
	 */
	public void setMovFinalizadaSet(List<SrMovimentacao> movFinalizadaSet) {
		this.movFinalizadaSet = movFinalizadaSet;
	}

	/**
	 * @return the dtAgenda
	 */
	public Date getDtAgenda() {
		return dtAgenda;
	}

	/**
	 * @param dtAgenda the dtAgenda to set
	 */
	public void setDtAgenda(Date dtAgenda) {
		this.dtAgenda = dtAgenda;
	}

	/**
	 * @return the motivoPendencia
	 */
	public SrTipoMotivoPendencia getMotivoPendencia() {
		return motivoPendencia;
	}

	/**
	 * @param motivoPendencia the motivoPendencia to set
	 */
	public void setMotivoPendencia(SrTipoMotivoPendencia motivoPendencia) {
		this.motivoPendencia = motivoPendencia;
	}

	/**
	 * @return the movFinalizadora
	 */
	public SrMovimentacao getMovFinalizadora() {
		return movFinalizadora;
	}

	/**
	 * @param movFinalizadora the movFinalizadora to set
	 */
	public void setMovFinalizadora(SrMovimentacao movFinalizadora) {
		this.movFinalizadora = movFinalizadora;
	}

	/**
	 * @return the solicitacaoReferencia
	 */
	public SrSolicitacao getSolicitacaoReferencia() {
		return solicitacaoReferencia;
	}

	/**
	 * @param solicitacaoReferencia the solicitacaoReferencia to set
	 */
	public void setSolicitacaoReferencia(SrSolicitacao solicitacaoReferencia) {
		this.solicitacaoReferencia = solicitacaoReferencia;
	}

	/**
	 * @return the motivoEscalonamento
	 */
	public SrTipoMotivoEscalonamento getMotivoEscalonamento() {
		return motivoEscalonamento;
	}

	/**
	 * @param motivoEscalonamento the motivoEscalonamento to set
	 */
	public void setMotivoEscalonamento(SrTipoMotivoEscalonamento motivoEscalonamento) {
		this.motivoEscalonamento = motivoEscalonamento;
	}

	/**
	 * @param respostaSet the respostaSet to set
	 */
	public void setRespostaSet(List<SrResposta> respostaSet) {
		this.respostaSet = respostaSet;
	}

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
			resp.setMovimentacao(this);
			resp.setPergunta(SrPergunta.AR.findById(idPergunta));
			if (resp.getPergunta().getTipoPergunta().getIdTipoPergunta() == SrTipoPergunta.TIPO_PERGUNTA_TEXTO_LIVRE)
				resp.setDescrResposta(respostaMap.get(idPergunta));
			else
				resp.setGrauSatisfacao(SrGrauSatisfacao.valueOf(respostaMap.get(idPergunta)));
			respostaSet.add(resp);
		}
	}

	public HashMap<Long, String> getRespostaMap() {
		HashMap<Long, String> map = new HashMap<Long, String>();
		if (respostaSet != null)
			for (SrResposta resp : respostaSet) {
				if (!resp.getDescrResposta().equals(""))
					map.put(resp.getPergunta().getIdPergunta(), resp.getDescrResposta());
				else
					map.put(resp.getPergunta().getIdPergunta(),
							resp.getGrauSatisfacao());
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
				|| tipoMov.getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
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
		SigaCalendar cal = new SigaCalendar();
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
		solicitacao.refresh();

		solicitacao.atualizarMarcas();
		if (solicitacao.getMovimentacaoSetComCancelados().size() > 1
				&& tipoMov.getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
				&& solicitacao.formaAcompanhamento != SrFormaAcompanhamento.ABERTURA
				&& !(solicitacao.formaAcompanhamento == SrFormaAcompanhamento.ABERTURA_FECHAMENTO
				&& tipoMov.getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO && tipoMov.getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO))
			notificar();
		
		//Necessaria condicao a parte, pois o solicitante pode escolher nunca receber notificacao (SrFormaAcompanhamento.NUNCA)
		if (solicitacao.isFilha() &&
				tipoMov.getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
			SrCorreio.notificarAtendente(this); //notifica o atendente da solicitacao pai, caso a filha seja fechada
		return this;
	}

	public void desfazer(DpPessoa pessoa, DpLotacao lota) throws Exception {
		SrMovimentacao movCanceladora = new SrMovimentacao(this.solicitacao);
		movCanceladora.tipoMov = SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
		movCanceladora.descrMovimentacao = "Cancelando "
				+ tipoMov.getNome().toLowerCase();

		SrMovimentacao ultimaValida = getAnterior();
		movCanceladora.atendente = ultimaValida.atendente;
		movCanceladora.lotaAtendente = ultimaValida.lotaAtendente;
		movCanceladora.salvar(pessoa, lota);
		
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
			double lenght = (double) arquivo.getBlob().length / 1024 / 1024;
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

			if (atendente == null && lotaAtendente == null) {
				lotaAtendente = anterior.lotaAtendente;
				atendente = anterior.atendente;
			}

			if (numSequencia == null)
				numSequencia = ultimaMovDoContexto.numSequencia + 1;
		}

		if (tipoMov == null)
			tipoMov = SrTipoMovimentacao
			.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);

		if (!solicitacao.isRascunho()) {
			if (atendente == null && lotaAtendente == null)
				throw new Exception("Atendente n�o pode ser nulo");

			if (lotaAtendente == null)
				lotaAtendente = atendente.getLotacao();
		}
	}

	public void notificar() throws Exception{
		if (tipoMov.getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO)
			SrCorreio.notificarReplanejamentoMovimentacao(this);
		else if (!isCancelada())
			SrCorreio.notificarMovimentacao(this);
		else
			SrCorreio.notificarCancelamentoMovimentacao(this);
	}
	
	public String getMotivoPendenciaString() {
		return this.motivoPendencia.getDescrTipoMotivoPendencia();
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
		return this.motivoEscalonamento.getDescrTipoMotivoEscalonamento();
	}
}
