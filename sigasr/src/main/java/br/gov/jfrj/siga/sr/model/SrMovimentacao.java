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

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;
import br.gov.jfrj.siga.uteis.SigaPlayCalendar;
import br.gov.jfrj.siga.vraptor.entity.ObjetoVraptor;

@Entity
@Table(name = "SR_MOVIMENTACAO", schema = Catalogs.SIGASR)
public class SrMovimentacao extends ObjetoVraptor {
    private static final long serialVersionUID = 1L;
    public static final ActiveRecord<SrMovimentacao> AR = new ActiveRecord<>(SrMovimentacao.class);

    @Id
    @SequenceGenerator(sequenceName = Catalogs.SIGASR + ".SR_MOVIMENTACAO_SEQ", name = "srMovimentacaoSeq")
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
    @JoinColumn(name = "ID_DESIGNACAO")
    private SrConfiguracao designacao;

    @ManyToOne
    @JoinColumn(name = "ID_CADASTRANTE")
    private DpPessoa cadastrante;

    @ManyToOne
    @JoinColumn(name = "ID_LOTA_CADASTRANTE")
    private DpLotacao lotaCadastrante;

    @ManyToOne
    @JoinColumn(name = "ID_TITULAR")
    private DpPessoa titular;

    @ManyToOne
    @JoinColumn(name = "ID_LOTA_TITULAR")
    private DpLotacao lotaTitular;

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

    @OneToMany(targetEntity = SrResposta.class, mappedBy = "movimentacao", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    // @OrderBy("pergunta asc")
    private List<SrResposta> respostaSet;

    @OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "movFinalizadora", fetch = FetchType.LAZY)
    private List<SrMovimentacao> movFinalizadaSet;

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

    public SrMovimentacao() throws Exception {
        this(null);
    }

    public SrMovimentacao(SrSolicitacao sol) throws Exception {
        this.setSolicitacao(sol);
        if (this.getSolicitacao() != null) {
            SrMovimentacao ultMov = getSolicitacao().getUltimaMovimentacao();
            if (ultMov != null) {
                this.setLotaAtendente(ultMov.getLotaAtendente());
                this.setAtendente(ultMov.getAtendente());
            }
        }
    }

    public List<SrResposta> getRespostaSet() {
        if (respostaSet == null)
            return new ArrayList<SrResposta>();
        return respostaSet;
    }

    public void setRespostaMap(Map<Long, String> respostaMap) throws Exception {
        setRespostaSet(new ArrayList<SrResposta>());
        for (Long idPergunta : respostaMap.keySet()) {
            SrResposta resp = new SrResposta();
            resp.setMovimentacao(this);
            resp.setPergunta(SrPergunta.AR.findById(idPergunta));
            if (resp.getPergunta().getTipoPergunta().getIdTipoPergunta() == SrTipoPergunta.TIPO_PERGUNTA_TEXTO_LIVRE)
                resp.setDescrResposta(respostaMap.get(idPergunta));
            else
                resp.setGrauSatisfacao(SrGrauSatisfacao.valueOf(respostaMap.get(idPergunta)));
            getRespostaSet().add(resp);
        }
    }

    public HashMap<Long, String> getRespostaMap() {
        HashMap<Long, String> map = new HashMap<Long, String>();
        if (getRespostaSet() != null)
            for (SrResposta resp : getRespostaSet()) {
                if (!resp.getDescrResposta().equals(""))
                    map.put(resp.getPergunta().getIdPergunta(), resp.getDescrResposta());
                else
                    map.put(resp.getPergunta().getIdPergunta(), resp.getGrauSatisfacao().getDescrGrauSatisfacao());
            }
        return map;
    }

    public boolean isCancelada() {
        return getMovCanceladora() != null;
    }

    public boolean isFinalizada() {
        return getMovFinalizadora() != null;
    }

    public boolean isCanceladoOuCancelador() {
        return isCancelada() || getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
    }

    public SrMovimentacao getAnterior() {
        boolean pronto = false;
        for (SrMovimentacao mov : getSolicitacao().getMovimentacaoSet()) {
            if (pronto)
                return mov;
            if (mov.getIdMovimentacao() == this.getIdMovimentacao())
                pronto = true;
        }
        return null;
    }

    public String getDtIniString() {
        SigaPlayCalendar cal = new SigaPlayCalendar();
        cal.setTime(getDtIniMov());
        return cal.getTempoTranscorridoString(false);
    }

    public String getDtIniMovDDMMYYHHMM() {
        if (getDtIniMov() != null) {
            final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
            return df.format(getDtIniMov());
        }
        return "";
    }

    public String getDtAgendaDDMMYYHHMM() {
        if (getDtAgenda() != null) {
            DateTime dateTime = new DateTime(getDtAgenda());
            return dateTime.toString("dd/MM/yyyy HH:mm");
        }
        return "";
    }

    public String getAtendenteString() {
        if (getAtendente() != null)
            return getAtendente().getSigla() + " (" + getLotaAtendente().getSigla() + ")";
        else
            return getLotaAtendente().getSigla();
    }

    public String getDtIniMovDDMMYYYYHHMM() {
        if (getDtIniMov() != null) {
            final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return df.format(getDtIniMov());
        }
        return "";

    }

    public String getCadastranteString() {
        return getAtendente().getSigla() + " (" + getLotaAtendente().getSigla() + ")";
    }

    public void setArquivo(File file) {
        this.arquivo = SrArquivo.newInstance(file);
    }

    public SrMovimentacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        this.setCadastrante(cadastrante);
        this.setLotaCadastrante(lotaCadastrante);
        this.setTitular(titular);
        this.setLotaTitular(lotaTitular);
        salvarAtualizandoSolicitacao();
        return this;
    }

    public void salvarAtualizandoSolicitacao() throws Exception {

        // Edson: considerar deixar esse codigo em SrSolicitacao.movimentar(),
        // visto que sao chamadas muitas operacoes daquela classe

        checarCampos();
        super.save();

        getSolicitacao().atualizarMarcas();
        if (getSolicitacao().getMovimentacaoSetComCancelados().size() > 1
                && getTipoMov().getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
                && getSolicitacao().getFormaAcompanhamento() != SrFormaAcompanhamento.ABERTURA
                && !(getSolicitacao().getFormaAcompanhamento() == SrFormaAcompanhamento.ABERTURA_FECHAMENTO && getTipoMov().getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO && getTipoMov()
                        .getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO))
            notificar();

        // Necessaria condicao a parte, pois o solicitante pode escolher nunca receber notificacao (SrFormaAcompanhamento.NUNCA)
        if (getSolicitacao().isFilha() && getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
            CorreioHolder.get().notificarAtendente(this); // notifica o atendente da solicitacao pai, caso a filha seja fechada

    }

    public void desfazer(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        SrMovimentacao movCanceladora = new SrMovimentacao(this.getSolicitacao());
        movCanceladora.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO));
        movCanceladora.setDescrMovimentacao("Cancelando " + getTipoMov().getNome().toLowerCase());

        SrMovimentacao ultimaValida = getAnterior();
        movCanceladora.setAtendente(ultimaValida.getAtendente());
        movCanceladora.setLotaAtendente(ultimaValida.getLotaAtendente());
        movCanceladora.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

        this.setMovCanceladora(movCanceladora);

        if (getMovFinalizadaSet() != null)
            for (SrMovimentacao movFinalizada : getMovFinalizadaSet()) {
                movFinalizada.setMovFinalizadora(null);
                movFinalizada.save();
            }
        setMovFinalizadaSet(new ArrayList<SrMovimentacao>());

        this.salvarAtualizandoSolicitacao();

    }

    private void checarCampos() throws Exception {

        if (getSolicitacao() == null)
            throw new Exception("Movimentação precisa fazer parte de uma solicitação");

        if (getArquivo() != null) {
            double lenght = (double) getArquivo().getBlob().length / 1024 / 1024;
            if (lenght > 2)
                throw new IllegalArgumentException("O tamanho do arquivo (" + new DecimalFormat("#.00").format(lenght) + "MB) é maior que o máximo permitido (2MB)");
        }

        if (getDtIniMov() == null)
            setDtIniMov(new Date());

        SrMovimentacao ultimaMovDoContexto = getSolicitacao().getUltimaMovimentacaoMesmoSeCanceladaTodoOContexto();

        if (ultimaMovDoContexto == null) {
            setNumSequencia(1L);
        } else {
            SrMovimentacao anterior = getSolicitacao().getUltimaMovimentacao();

            if (getLotaAtendente() == null) {
                setLotaAtendente(anterior.getLotaAtendente());
            }

            if (getNumSequencia() == null)
                setNumSequencia(ultimaMovDoContexto.getNumSequencia() + 1);
        }

        if (getTipoMov() == null)
            setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));

        if (!getSolicitacao().isRascunho()) {
            if (getAtendente() == null && getLotaAtendente() == null)
                throw new Exception("Atendente não pode ser nulo");

            if (getLotaAtendente() == null)
                setLotaAtendente(getAtendente().getLotacao());
        }
    }

	public void notificar() throws Exception {
        if (getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO)
            CorreioHolder
            	.get()
            	.notificarReplanejamentoMovimentacao(this);
        else if (!isCancelada())
        	CorreioHolder
        		.get()
        		.notificarMovimentacao(this);
        else
        	CorreioHolder
        		.get()
        		.notificarCancelamentoMovimentacao(this);
    }

    public String getMotivoPendenciaString() {
        return this.getMotivoPendencia() != null ? this.getMotivoPendencia().getDescrTipoMotivoPendencia() : "";
    }

    public SrMovimentacao getMovFinalizada() {
        if (getMovFinalizadaSet() == null || getMovFinalizadaSet().size() == 0)
            return null;
        return getMovFinalizadaSet().get(0);
    }

    public Date getDtFimMov() {
        return getMovFinalizadora() != null ? getMovFinalizadora().getDtIniMov() : getDtAgenda();
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
        return this.getMotivoEscalonamento().getDescrTipoMotivoEscalonamento();
    }

    @Override
    protected Long getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public String getDescrMovimentacao() {
        return descrMovimentacao;
    }

    public void setDescrMovimentacao(String descrMovimentacao) {
        this.descrMovimentacao = descrMovimentacao;
    }

    public Date getDtIniMov() {
        return dtIniMov;
    }

    public void setDtIniMov(Date dtIniMov) {
        this.dtIniMov = dtIniMov;
    }

    public SrArquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(SrArquivo arquivo) {
        this.arquivo = arquivo;
    }

    public DpPessoa getAtendente() {
        return atendente;
    }

    public void setAtendente(DpPessoa atendente) {
        this.atendente = atendente;
    }

    public DpLotacao getLotaAtendente() {
        return lotaAtendente;
    }

    public void setLotaAtendente(DpLotacao lotaAtendente) {
        this.lotaAtendente = lotaAtendente;
    }

    public SrConfiguracao getDesignacao() {
        return designacao;
    }

    public void setDesignacao(SrConfiguracao designacao) {
        this.designacao = designacao;
    }

    public DpPessoa getCadastrante() {
        return cadastrante;
    }

    public void setCadastrante(DpPessoa cadastrante) {
        this.cadastrante = cadastrante;
    }

    public DpLotacao getLotaCadastrante() {
        return lotaCadastrante;
    }

    public void setLotaCadastrante(DpLotacao lotaCadastrante) {
        this.lotaCadastrante = lotaCadastrante;
    }

    public DpPessoa getTitular() {
        return titular;
    }

    public void setTitular(DpPessoa titular) {
        this.titular = titular;
    }

    public DpLotacao getLotaTitular() {
        return lotaTitular;
    }

    public void setLotaTitular(DpLotacao lotaTitular) {
        this.lotaTitular = lotaTitular;
    }

    public SrSolicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SrSolicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public SrLista getLista() {
        return lista;
    }

    public void setLista(SrLista lista) {
        this.lista = lista;
    }

    public SrMovimentacao getMovCanceladora() {
        return movCanceladora;
    }

    public void setMovCanceladora(SrMovimentacao movCanceladora) {
        this.movCanceladora = movCanceladora;
    }

    public SrTipoMovimentacao getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(SrTipoMovimentacao tipoMov) {
        this.tipoMov = tipoMov;
    }

    public Long getNumSequencia() {
        return numSequencia;
    }

    public void setNumSequencia(Long numSequencia) {
        this.numSequencia = numSequencia;
    }

    public Long getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Long prioridade) {
        this.prioridade = prioridade;
    }

    public SrPesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(SrPesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public void setRespostaSet(List<SrResposta> respostaSet) {
        this.respostaSet = respostaSet;
    }

    public List<SrMovimentacao> getMovFinalizadaSet() {
        return movFinalizadaSet;
    }

    public void setMovFinalizadaSet(List<SrMovimentacao> movFinalizadaSet) {
        this.movFinalizadaSet = movFinalizadaSet;
    }

    public Date getDtAgenda() {
        return dtAgenda;
    }

    public void setDtAgenda(Date dtAgenda) {
        this.dtAgenda = dtAgenda;
    }

    public SrTipoMotivoPendencia getMotivoPendencia() {
        return motivoPendencia;
    }

    public void setMotivoPendencia(SrTipoMotivoPendencia motivoPendencia) {
        this.motivoPendencia = motivoPendencia;
    }

    public SrMovimentacao getMovFinalizadora() {
        return movFinalizadora;
    }

    public void setMovFinalizadora(SrMovimentacao movFinalizadora) {
        this.movFinalizadora = movFinalizadora;
    }

    public SrSolicitacao getSolicitacaoReferencia() {
        return solicitacaoReferencia;
    }

    public void setSolicitacaoReferencia(SrSolicitacao solicitacaoReferencia) {
        this.solicitacaoReferencia = solicitacaoReferencia;
    }

    public SrTipoMotivoEscalonamento getMotivoEscalonamento() {
        return motivoEscalonamento;
    }

    public void setMotivoEscalonamento(SrTipoMotivoEscalonamento motivoEscalonamento) {
        this.motivoEscalonamento = motivoEscalonamento;
    }
    
	public List<String> getEmailsNotificacaoReplanejamento() {
		SrSolicitacao solicitacao = getSolicitacao().getSolicitacaoAtual();
		List<String> recipients = new ArrayList<String>();
		for (SrGestorItem gestor : solicitacao.getItemConfiguracao().getGestorSet()) {
			DpPessoa pessoaGestorAtual = gestor.getDpPessoa().getPessoaAtual();
			if (pessoaGestorAtual != null && pessoaGestorAtual.getDataFim() == null)
				if (pessoaGestorAtual.getEmailPessoa() != null)
					recipients.add(pessoaGestorAtual.getEmailPessoa());

			if (gestor.getDpLotacao() != null)
				for (DpPessoa gestorPessoa : gestor.getDpLotacao().getDpPessoaLotadosSet())
					if (gestorPessoa.getPessoaAtual().getDataFim() == null)
						if (gestorPessoa.getPessoaAtual().getEmailPessoa() != null)
							recipients.add(gestorPessoa.getPessoaAtual().getEmailPessoa());
		}
		recipients.add(solicitacao.getSolicitante().getEmailPessoa());
		return recipients;
	}

	public List<String> getEmailsNotificacaoAtendende() {
		List<String> recipients = new ArrayList<String>();
		String email = null;

		DpPessoa atendenteSolPai = solicitacao.getSolicitacaoPai().getAtendente();
		if (atendenteSolPai != null) {
			email = atendenteSolPai.getPessoaAtual().getEmailPessoa();
			if (email != null)
				recipients.add(email);
		} else {
			DpLotacao lotaAtendenteSolPai = solicitacao.getSolicitacaoPai().getLotaAtendente();
			if (lotaAtendenteSolPai != null)
				for (DpPessoa pessoaDaLotacao : lotaAtendenteSolPai.getDpPessoaLotadosSet())
					if (pessoaDaLotacao.getDataFim() == null) {
						email = pessoaDaLotacao.getPessoaAtual().getEmailPessoa();
						if (email != null)
							recipients.add(email);
					}
		}
		return recipients;
	}
}
