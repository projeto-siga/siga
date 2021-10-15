package br.gov.jfrj.siga.sr.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;
import br.gov.jfrj.siga.sr.util.SrViewUtil;

@Entity
@Table(name = "sr_movimentacao", schema = "sigasr")
public class SrMovimentacao extends Objeto {
    private static final long serialVersionUID = 1L;
    public static final ActiveRecord<SrMovimentacao> AR = new ActiveRecord<>(SrMovimentacao.class);
    private final static Logger log = Logger.getLogger(SrMovimentacao.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_MOVIMENTACAO_SEQ", name = "srMovimentacaoSeq")
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

    @Enumerated
    private SrPrioridade prioridade;

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
    
    @Enumerated
    private SrTipoMotivoFechamento motivoFechamento;

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
    
    @Column(name = "CONHECIMENTO")
    private String conhecimento;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sr_movimentacao_acordo", schema = "sigasr", joinColumns = { @JoinColumn(name = "ID_MOVIMENTACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ACORDO") })
    private List<SrAcordo> acordos;
    
    @Column(name = "DNM_TEMPO_DECORRIDO_ATENDMTO")
    private Long dnmTempoDecorridoAtendimento;

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
    
    public boolean isEntreAsPrincipais(){
    	if (SrTipoMovimentacao.TIPOS_MOV_PRINCIPAIS.contains(getTipoMov().getIdTipoMov()))
    		return true;
    	if (getTipoMov().getId().equals(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) 
    			&& getMotivoPendencia() != null 
    			&& !getMotivoPendencia().equals(SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA) 
    			&& !isFinalizadaOuExpirada())
    		return true;
    	return false;
    }
    
    public boolean isInicioAtendimento() {
    	return (SrTipoMovimentacao.TIPOS_MOV_INI_ATENDIMENTO.contains(getTipoMov().getIdTipoMov()));
    }
    
    public boolean isFimAtendimento() {
    	return (SrTipoMovimentacao.TIPOS_MOV_FIM_ATENDIMENTO.contains(getTipoMov().getIdTipoMov()));
    }

    public boolean isCancelada() {
        return getMovCanceladora() != null;
    }
    
    public Date getDtFimMov() {
        return getMovFinalizadora() != null ? getMovFinalizadora().getDtIniMov() : getDtAgenda();
    }
    
    public boolean isFinalizadaOuExpirada() {
        return getDtFimMov() != null && getDtFimMov().before(new Date());
    }

    public boolean isCanceladoOuCancelador() {
        return isCancelada() || getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
    }
    
    public boolean isDescricaoAtomica(){
    	if (getDescrMovimentacao() != null)
    		switch (getTipoMov().getId().intValue()) {
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA:
				return !getDescrMovimentacao().contains("Juntando a ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO:
				return !getDescrMovimentacao().contains("Vinculando a ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA:
				return !getDescrMovimentacao().contains("Fim previsto: ")
						&& !getDescrMovimentacao().contains(" | ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE:
				return !getDescrMovimentacao().contains("Prioridade Tecnica: ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA:
				return !getDescrMovimentacao().contains(" pendencia iniciada em ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO:
				return !getDescrMovimentacao().contains("Atendente: ");
			case (int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO:
				return !getDescrMovimentacao().contains("Atribuindo a ") 
						&& !getDescrMovimentacao().contains("Retirando atribuição");
			case ((int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO):
				return !getDescrMovimentacao().contains("Motivo: ") 
						&& !getDescrMovimentacao().contains("Item: ");
			case ((int)SrTipoMovimentacao.TIPO_MOVIMENTACAO_RECLASSIFICACAO):
				return !getDescrMovimentacao().contains("Item: ");
			}
    	return true;
    }
    
    public String getDescricao(){
    	return getDescrMovimentacao();
    }
    
    public SrMovimentacao getAnterior() {
        return getAnteriorPorTipo(null);
    }
    
    public SrMovimentacao getAnteriorPorTipo(Long idTpMov) {
        boolean pronto = false;
        for (SrMovimentacao mov : getSolicitacao().getMovimentacaoSet()) {
            if (pronto && (idTpMov == null || mov.getTipoMov().getIdTipoMov().equals(idTpMov)))
                return mov;
            if (mov.getIdMovimentacao() == this.getIdMovimentacao())
                pronto = true;
        }
        return null;
    }
    
    public boolean isTrocaDePessoaAtendente(){
    	if (!getTipoMov().getId().equals(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO))
    		return false;
    	SrMovimentacao anterior = getAnterior();
    	if (anterior == null)
    		return false;
    	if (getAtendente() == null)
    		return anterior.getAtendente() != null;
    	else return (anterior.getAtendente() == null || !anterior.getAtendente().equivale(this.getAtendente()));
    }

    public String getDtIniString() {
        return SrViewUtil.toStr(getDtIniMov());
    }

    public String getDtIniMovDDMMYYYYHHMM() {
        return SrViewUtil.toDDMMYYYYHHMM(getDtIniMov());
    }

    public String getDtAgendaDDMMYYYYHHMM() {
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

    public String getCadastranteString() {
    	return cadastrante.getSigla() + " (" + lotaCadastrante.getSigla() + ")";
    }

    public SrMovimentacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        this.setCadastrante(cadastrante);
        this.setLotaCadastrante(lotaCadastrante);
        this.setTitular(titular);
        this.setLotaTitular(lotaTitular);
        this.carregarSolicitacao();
        salvarAtualizandoSolicitacao();
        return this;
    }

    public void salvarAtualizandoSolicitacao() throws Exception {

        // Edson: considerar deixar esse codigo em SrSolicitacao.movimentar(),
        // visto que sao chamadas muitas operacoes daquela classe

        checarCampos();
        super.save();
        ContextoPersistencia.em().flush();

        getSolicitacao().refresh();

        getSolicitacao().atualizarMarcas();
        
        //notificação usuário
        if (getAnteriorPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) != null
                && getTipoMov().getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
                && getSolicitacao().getFormaAcompanhamento() != SrFormaAcompanhamento.ABERTURA
                && !(getSolicitacao().getFormaAcompanhamento() == SrFormaAcompanhamento.ABERTURA_FECHAMENTO && getTipoMov().getIdTipoMov() != SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO))
            notificar();

        //notificação atendente
        notificarAtendente();
        notificarAtendenteSolicitacaoFilha();
    }

    public void desfazer(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        SrMovimentacao movCanceladora = new SrMovimentacao(this.getSolicitacao());
        movCanceladora.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO));
        movCanceladora.setDescrMovimentacao("Cancelando " + getTipoMov().getNome().toLowerCase());

        SrMovimentacao ultimaValida = getAnterior();
        movCanceladora.setAtendente(ultimaValida.getAtendente());
        movCanceladora.setLotaAtendente(ultimaValida.getLotaAtendente());
        movCanceladora.setItemConfiguracao(ultimaValida.getItemConfiguracao());
        movCanceladora.setAcao(ultimaValida.getAcao());
        movCanceladora.setPrioridade(ultimaValida.getPrioridade());
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

    	if (cadastrante == null)
            throw new Exception("Cadastrante não pode ser nulo");
    	if (lotaCadastrante == null)
            lotaCadastrante = cadastrante.getLotacao();
    	if (titular == null)
            titular = cadastrante;
    	if (lotaTitular == null)
            lotaTitular = titular.getLotacao();
    	
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
            setItemConfiguracao(solicitacao.getItemConfiguracao());
            setAcao(solicitacao.getAcao());
            setPrioridade(solicitacao.getPrioridade());
        } else {
        	SrMovimentacao anterior = getSolicitacao().getUltimaMovimentacao();
        	
            if (getAtendente() != null && getAtendente().getId() == null)  setAtendente(null);

            if (getLotaAtendente() == null) {
            	if (anterior != null && anterior.getLotaAtendente() != null)
            		setLotaAtendente(anterior.getLotaAtendente());
            } 
            
            if (getItemConfiguracao() == null) {
            	if (anterior != null && anterior.getItemConfiguracao() != null)
            		setItemConfiguracao(anterior.getItemConfiguracao());
            	else setItemConfiguracao(solicitacao.getItemAtual());
            }
            
            if (getAcao() == null) {
            	if (anterior != null && anterior.getAcao() != null)
            		setAcao(anterior.getAcao());
            	else setAcao(solicitacao.getAcaoAtual());
            }
            
            if (getPrioridade() == null){
            	if (anterior != null && anterior.getPrioridade() != null)
            		setPrioridade(anterior.getPrioridade());
            	else setPrioridade(solicitacao.getPrioridadeTecnica());
            }

            if (getNumSequencia() == null)
                setNumSequencia(ultimaMovDoContexto.getNumSequencia() + 1);
        }

        if (getTipoMov() == null)
            setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));
        else if (SrTipoMovimentacao.TIPOS_MOV_INI_ATENDIMENTO.contains(getTipoMov().getId())
				|| SrTipoMovimentacao.TIPOS_MOV_ATUALIZACAO_ATENDIMENTO.contains(getTipoMov().getId()))
        	atualizarAcordos();
              
        SrEtapaSolicitacao ultimoAtendimento = solicitacao.getUltimoAtendimento();
        if (ultimoAtendimento != null)
        	setDnmTempoDecorridoAtendimento(ultimoAtendimento.getDecorridoEmSegundos());
    }
    
    public void atualizarAcordos() throws Exception {
        setAcordos(new ArrayList<SrAcordo>());

        SrConfiguracao c = new SrConfiguracao();
        c.setDpPessoa(solicitacao.getSolicitante());
        c.setLotacao(solicitacao.getLotaSolicitante());
        c.setComplexo(solicitacao.getLocal());
        c.setBuscarPorPerfis(true);
        c.setItemConfiguracaoFiltro(getItemConfiguracao());
        c.setAcaoFiltro(getAcao());
        c.setPrioridade(getPrioridade());
        c.setAtendente(getLotaAtendente());
        c.setCpTipoConfiguracao(SrTipoDeConfiguracao.ABRANGENCIA_ACORDO);
        
        List<SrConfiguracaoCache> confs = SrConfiguracao.listar(c);
        for (SrConfiguracaoCache conf : confs) {
        	if (conf.acordo != 0) {
        		SrAcordo acordoAtual = SrAcordo.AR.findById(conf.acordo).getAcordoAtual();
        		if (acordoAtual != null && acordoAtual.getHisDtFim() == null && !getAcordos().contains(acordoAtual) 
        			&& acordoAtual.contemParametro(SrParametro.ATENDIMENTO, SrParametro.ATENDIMENTO_GERAL))
        			getAcordos().add(acordoAtual);
        	}
        }
    }

    public List<SrParametroAcordo> getParametrosAcordoOrdenados(SrParametro p){
    	List<SrParametroAcordo> l = new ArrayList<SrParametroAcordo>();
    	for (SrParametroAcordo par : getParametrosAcordoOrdenados())
    		if (par.getParametro().equals(p))
    			l.add(par);
    	return l;
    }
    
    public Set<SrParametroAcordo> getParametrosAcordoOrdenados(){
    	Set<SrParametroAcordo> set = new TreeSet<SrParametroAcordo>(
			new SrParametroAcordoComparator());
    	for (SrAcordo a : getAcordos())
    		set.addAll(a.getParametroAcordoSet());
    	return set;
    }

	public void notificar() throws Exception {
		try{
			if (getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE)
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
		} catch(Exception e){
			log.error("Erro ao notificar", e);
		}
    }
	
	public void notificarAtendente() throws Exception {
		DpLotacao lotaAtendente = null;
		try {
			if ((isFimAtendimento() && getSolicitacao().isFilha()))
				lotaAtendente = getSolicitacao().getSolicitacaoPai()
						.getLotaAtendente();
			else if (isInicioAtendimento()
					|| !isLotaAtendenteResponsavelPelaMovimentacao())
				lotaAtendente = getLotaAtendente();
			
			if (podeReceberNotificacaoAtendente(getTitular(), lotaAtendente))
				CorreioHolder.get().notificarAtendente(this, getSolicitacao());
		} catch (Exception e) {
			log.error("Erro ao notificar", e);
		}
	}

	private void notificarAtendenteSolicitacaoFilha() {
		if (getSolicitacao().isPai()
				&& !getSolicitacao().getSolicitacaoFilhaSet().isEmpty()
				&& getTipoMov().getId() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO)
			for (SrSolicitacao filha : getSolicitacao()
					.getSolicitacaoFilhaSet()) {
				if (filha.isAtivo()
						&& podeReceberNotificacaoAtendente(getTitular(),
								filha.getLotaAtendente()))
					CorreioHolder.get().notificarAtendente(this, filha);
			}
	}

	private boolean isLotaAtendenteResponsavelPelaMovimentacao() {
		return getLotaAtendente() != null && getLotaTitular() != null && getLotaTitular()
				.equivale(getLotaAtendente());
	}
	
	private boolean podeReceberNotificacaoAtendente(DpPessoa pessoa, DpLotacao lotaAtendente) {
		return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa,
				lotaAtendente, "SIGA;SR;EMAILATEND:Receber Notificação Atendente");
	}

    public String getMotivoPendenciaString() {
        return this.getMotivoPendencia() != null ? this.getMotivoPendencia().getDescrTipoMotivoPendencia() : "";
    }

    public SrMovimentacao getMovFinalizada() {
        if (getMovFinalizadaSet() == null || getMovFinalizadaSet().size() == 0)
            return null;
        return getMovFinalizadaSet().get(0);
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

    protected Long getId() {
        return idMovimentacao;
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
    
    public void setArquivo(UploadedFile file) {
        this.arquivo = SrArquivo.newInstance(file);
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
    
    public SrPrioridade getPrioridade() {
        return prioridade;
    }
    
    public String getPrioridadeString(){
        return prioridade == null ? "" : prioridade.getDescPrioridade();
    }

    public void setPrioridade(SrPrioridade prioridade) {
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
    
	public SrTipoMotivoFechamento getMotivoFechamento() {
		return motivoFechamento;
	}

	public void setMotivoFechamento(SrTipoMotivoFechamento motivoFechamento) {
		this.motivoFechamento = motivoFechamento;
	}

    public List<SrAcordo> getAcordos() {
        return acordos;
    }
    
    public boolean possuiAcordos(){
    	return getAcordos() != null && getAcordos().size() > 0;
    }

    public void setAcordos(List<SrAcordo> acordos) {
        this.acordos = acordos;
    }
    
	public List<String> getEmailsNotificacaoReplanejamento() {
		SrSolicitacao solicitacao = getSolicitacao().getSolicitacaoAtual();
		List<String> recipients = new ArrayList<String>();
		for (SrGestorItem gestor : solicitacao.getItemAtual().getGestorSet()) {
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
		DpPessoa atendente = null;
		
		atendente = solicitacao.getAtendente();
		if (atendente != null){
			email = atendente.getPessoaAtual().getEmailPessoa();
			if (email != null)
				recipients.add(email);
		} else {
			List<DpPessoa> listaPessoasAtendentes = solicitacao.getPessoasAtendentesDisponiveis();
			List<DpSubstituicao> listaSubstitutos = solicitacao.getSubstitutos();
			if (listaPessoasAtendentes.size() > 0)
			        for (DpPessoa pessoaDaLotacao : listaPessoasAtendentes) {
			                atendente = pessoaDaLotacao.getPessoaAtual();
			                if (atendente.getDataFim() == null) {
			                        email = atendente.getEmailPessoa();
			                        if (email != null)
			                                recipients.add(email);
			                }
			        }
			if (listaSubstitutos.size() > 0)
			        for (DpSubstituicao pessoaSubstitutaDaLotacao : listaSubstitutos) {
			                atendente = pessoaSubstitutaDaLotacao.getSubstituto().getPessoaAtual();
			                if (atendente.getDataFim() == null) {
			                        email = atendente.getEmailPessoa();
			                        if (email != null)
			                                recipients.add(email);
			                }
			        }
			        
		}
		return recipients;
	}
	
	public Long getDnmTempoDecorridoAtendimento() {
		return dnmTempoDecorridoAtendimento;
	}

	public void setDnmTempoDecorridoAtendimento(Long dnmTempoDecorridoAtendimento) {
		this.dnmTempoDecorridoAtendimento = dnmTempoDecorridoAtendimento;
	}

	public void carregarSolicitacao() {
	    if (this.getSolicitacao() != null && this.getSolicitacao().getId() != null) {
	        this.setSolicitacao(SrSolicitacao.AR.findById(this.getSolicitacao().getId()));
	    }
	}

	public String getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(String conhecimento) {
		this.conhecimento = conhecimento;
	}
}
