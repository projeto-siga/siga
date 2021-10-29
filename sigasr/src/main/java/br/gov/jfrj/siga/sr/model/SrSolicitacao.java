package br.gov.jfrj.siga.sr.model;

import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.ListaInclusaoAutomatica;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;
import br.gov.jfrj.siga.sr.notifiers.Destinatario;
import br.gov.jfrj.siga.sr.util.SrViewUtil;
import br.gov.jfrj.siga.sr.util.Util;
import br.gov.jfrj.siga.uteis.SigaPlayCalendar;

@Entity
@Table(name = "sr_solicitacao", schema = "sigasr")
public class SrSolicitacao extends HistoricoSuporte implements SrSelecionavel {
    private static final String MODAL_TRUE = "modal=true";
    private static final String OPERACAO_NAO_PERMITIDA = "Opera\u00E7\u00E3o n\u00E3o permitida";
    private static final Long SEM_NUMERACAO = 0L;
    private static final Long NUMERACAO_INICIAL = 1L;
    
    private static final long serialVersionUID = 1L;

    public static final ActiveRecord<SrSolicitacao> AR = new ActiveRecord<>(SrSolicitacao.class);
    private final static Logger log = Logger.getLogger(SrSolicitacao.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_SOLICITACAO_SEQ", name = "srSolicitacaoSeq")
    @GeneratedValue(generator = "srSolicitacaoSeq")
    @Column(name = "ID_SOLICITACAO")
    private Long idSolicitacao;

    @ManyToOne()
    @JoinColumn(name = "ID_SOLICITANTE")
    private DpPessoa solicitante;

    @ManyToOne
    @JoinColumn(name = "ID_INTERLOCUTOR")
    private DpPessoa interlocutor;

    @ManyToOne
    @JoinColumn(name = "ID_LOTA_SOLICITANTE")
    private DpLotacao lotaSolicitante;

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
    @JoinColumn(name = "ID_DESIGNACAO")
    private SrConfiguracao designacao;

    @Transient
    private DpLotacao atendenteNaoDesignado;

    @ManyToOne
    @JoinColumn(name = "ID_ORGAO_USU")
    private CpOrgaoUsuario orgaoUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_SOLICITACAO_PAI")
    private SrSolicitacao solicitacaoPai;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sr_solicitacao_acordo", schema = "sigasr", joinColumns = { @JoinColumn(name = "ID_SOLICITACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ACORDO") })
    private List<SrAcordo> acordos;

    @Enumerated
    private SrFormaAcompanhamento formaAcompanhamento;

    @Enumerated
    private SrMeioComunicacao meioComunicacao;

    @ManyToOne
    @JoinColumn(name = "ID_ITEM_CONFIGURACAO")
    private SrItemConfiguracao itemConfiguracao;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_ARQUIVO")
    private SrArquivo arquivo;

    @ManyToOne
    @JoinColumn(name = "ID_ACAO")
    private SrAcao acao;
    
    @Lob
    @Column(name = "DESCR_SOLICITACAO", length = 8192)
    private String descrSolicitacao;

    @Enumerated
    private SrGravidade gravidade;

    @Enumerated
    private SrPrioridade prioridade;

    @Column(name = "DT_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtReg;

    @Column(name = "DT_EDICAO_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtIniEdicao;

    @Column(name = "DT_ORIGEM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtOrigem;

    @ManyToOne
    @JoinColumn(name = "ID_COMPLEXO")
    private CpComplexo local;

    @Column(name = "TEL_PRINCIPAL")
    private String telPrincipal;
    
    @Column(name = "ENDERECO")
    private String endereco;

    @Transient
    private boolean fecharAoAbrir;

    @Transient
    private String motivoFechamentoAbertura;

    @Column(name = "NUM_SOLICITACAO")
    private Long numSolicitacao;

    @Column(name = "NUM_SEQUENCIA")
    private Long numSequencia;

    @Column(name = "DESCR_CODIGO")
    private String codigo;

    @ManyToOne()
    @JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
    private SrSolicitacao solicitacaoInicial;

    @OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", fetch = FetchType.LAZY)
    // @OrderBy("hisDtIni desc")
    private List<SrSolicitacao> meuSolicitacaoHistoricoSet;

    @OneToMany(targetEntity = SrAtributoSolicitacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
    protected List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet;

    @OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
    // @OrderBy("dtIniMov DESC")
    private Set<SrMovimentacao> meuMovimentacaoSet;
    
    @OneToMany(targetEntity = SrPrioridadeSolicitacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
    private Set<SrPrioridadeSolicitacao> meuPrioridadeSolicitacaoSet;

    @OneToMany(mappedBy = "solicitacaoPai", fetch = FetchType.LAZY)
    // @OrderBy("numSequencia asc")
    private Set<SrSolicitacao> meuSolicitacaoFilhaSet;

    @OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacaoReferencia", fetch = FetchType.LAZY)
    private Set<SrMovimentacao> meuMovimentacaoReferenciaSet;

    // Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
    // entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
    @OneToMany(mappedBy = "solicitacao", fetch = FetchType.LAZY)
    @Where(clause = "ID_TP_MARCA=2")
    private Set<SrMarca> meuMarcaSet;

    @Column(name = "FG_RASCUNHO")
    @Type(type = "yes_no")
    private Boolean rascunho;

    @Column(name = "FECHADO_AUTOMATICAMENTE")
    @Type(type = "yes_no")
    private Boolean fechadoAutomaticamente;
    
    @Column(name="DNM_TEMPO_DECORRIDO_CADASTRO")
    private Long dnmTempoDecorridoCadastro;
    
    @Transient
    private Map<Long, SrAtributoSolicitacaoMap> atributoSolicitacaoMap;

    public SrSolicitacao() {

    }

    public class SrTarefa {
        private SrAcao acao;
        private SrConfiguracaoCache conf;

        public SrTarefa() {

        }

        public SrAcao getAcao() {
            return acao;
        }

        public void setAcao(SrAcao acao) {
            this.acao = acao;
        }

        public SrConfiguracaoCache getConf() {
            return conf;
        }

        public void setConf(SrConfiguracaoCache conf) {
            this.conf = conf;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((acao == null) ? 0 : acao.hashCode());
            result = prime * result + ((conf == null || conf.atendente == 0) ? 0 : Long.valueOf(conf.atendente).hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SrTarefa other = (SrTarefa) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (acao == null) {
                if (other.acao != null)
                    return false;
            } else if (!acao.equals(other.acao))
                return false;
            if (conf == null) {
                if (other.conf != null)
                    return false;
            } else if (conf.atendente != 0 && conf.atendente != other.conf.atendente)
                return false;
            return true;
        }

        private SrSolicitacao getOuterType() {
            return SrSolicitacao.this;
        }
    }

    @Override
    public Long getId() {
        return getIdSolicitacao();
    }

    @Override
    public void setId(Long id) {
        this.setIdSolicitacao(id);
    }

    @Override
    public String getSigla() {
        return getCodigo();
    }
    
    public String getSiglaCompacta() {
    	return getSigla() != null ? getSigla().replace("-", "").replace("/", "") : "";
	}

    @Override
    public void setSigla(String sigla) {
        String siglaUpper = sigla.trim().toUpperCase();
        final Matcher mTMP = Pattern.compile("^TMPSR-?([0-9]+)$").matcher(siglaUpper);
        if (mTMP.find())
        	setId(Long.valueOf(mTMP.group(1)));
        else {
        	Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
        	for (Object ou : CpOrgaoUsuario.AR.all().fetch()) {
        		CpOrgaoUsuario cpOu = (CpOrgaoUsuario) ou;
        		mapAcronimo.put(cpOu.getAcronimoOrgaoUsu(), cpOu);
        		mapAcronimo.put(cpOu.getSiglaOrgaoUsu(), cpOu);
        	}
        	StringBuilder acronimos = new StringBuilder();
        	for (String s : mapAcronimo.keySet()) {
        		if (acronimos.length() > 0)
        			acronimos.append("|");
        		acronimos.append(s);
        	}
        	final Pattern p = Pattern.compile("^(" + acronimos.toString() + ")?-?SR{1}-?(?:([0-9]{4})/?)??([0-9]{1,5})(?:[.]?([0-9]{1,3}))?$");
        	final Matcher m = p.matcher(siglaUpper);
        	if (m.find()) {
        		if (m.group(1) != null) {
        			try {
        				CpOrgaoUsuario orgao = new CpOrgaoUsuario();
        				orgao.setSiglaOrgaoUsu(m.group(1));
        				orgao = CpOrgaoUsuario.AR.find("acronimoOrgaoUsu = ?1 or siglaOrgaoUsu = ?2", m.group(1), m.group(1)).first();
        				this.setOrgaoUsuario(orgao);
        			} catch (final Exception ce) {

        			}
        		}
        		if (m.group(2) != null) {
        			Calendar c1 = Calendar.getInstance();
        			c1.set(Calendar.YEAR, Integer.valueOf(m.group(2)));
        			c1.set(Calendar.DAY_OF_YEAR, 1);
        			this.setDtReg(c1.getTime());
        		} else
        			this.setDtReg(new Date());
        		if (m.group(3) != null)
        			setNumSolicitacao(Long.valueOf(m.group(3)));
        		if (m.group(4) != null)
        			setNumSequencia(Long.valueOf(m.group(4)));
        	}
        }
    }
    
    public String getDescricaoMax70(){
    	return Texto.maximoCaracteres(getDescricao(), 70, 100);
    }

    @Override
    public String getDescricao() {
        if (!isDescrSolicitacaoPreenchida()) {
            if (isFilha())
                return getSolicitacaoPai().getDescricao();
            else
                return "Descri\u00E7\u00E3o n\u00E3o informada";
        } else
            return getDescrSolicitacao();
    }
      
    public String getDescricaoSemQuebraDeLinha() {
    	return Texto.semQuebraDeLinha(getDescricao());
    }
    
    public Set<SrAtributoSolicitacao> getAtributoSolicitacaoSetAtual(boolean todoOContexto) {
    	boolean atributosComHistorico = false;
    	return getAtributoSolicitacaoSet(todoOContexto, atributosComHistorico);
    }
    
    public Set<SrAtributoSolicitacao> getAtributoSolicitacaoSet(boolean todoOContexto, boolean atributosComHistorico) {
    	Set<SrSolicitacao> solsAConsiderar = getSolicitacoesDependendoDoContexto(todoOContexto);
		Set<SrAtributoSolicitacao> atributos = new LinkedHashSet<SrAtributoSolicitacao>();
		for (SrSolicitacao solicitacao : solsAConsiderar) {
	    	if (solicitacao.getMeuAtributoSolicitacaoSet() != null) {
	    		if (atributosComHistorico)
	    			atributos.addAll(solicitacao.getMeuAtributoSolicitacaoSet());
	    		else
	    			atributos.addAll(solicitacao.buscarAtributosAtuais());
	    	}
		}
    	return atributos;
    }
    
    private Set<SrAtributoSolicitacao> buscarAtributosAtuais() {
    	Set<SrAtributoSolicitacao> atributosAtuais = new LinkedHashSet<SrAtributoSolicitacao>();
		for (SrAtributoSolicitacao atributo : getMeuAtributoSolicitacaoSet())
			if (atributo != null && atributo.isAtivo())
				atributosAtuais.add(atributo);
		return atributosAtuais;
    }

    @Override
    public void setDescricao(String descricao) {
        this.setDescrSolicitacao(descricao);
    }

    public Boolean isFechadoAutomaticamente() {
        return fechadoAutomaticamente != null ? fechadoAutomaticamente : false;
    }

    public void setFechadoAutomaticamente(Boolean fechadoAutomaticamente) {
        this.fechadoAutomaticamente = fechadoAutomaticamente;
    }

    @Override
    public SrSelecionavel selecionar(String sigla) throws Exception {
        setSigla(sigla);
        String query = "";
        if (getId() != null)
        	query = "from SrSolicitacao where hisDtFim is null and hisIdIni = " + getId();
        else {
        	if (getOrgaoUsuario() == null && getLotaTitular() != null)
        		setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());
        	
        	if (getDtReg() == null && getNumSequencia() == null) {
        		throw new AplicacaoException("Não é possível selecionar sem que seja informado o número de sequência ou a data do registro.");
        	}

        	query = "from SrSolicitacao where hisDtFim is null ";

        	if (getOrgaoUsuario() != null) {
        		query += " and orgaoUsuario.idOrgaoUsu = " + getOrgaoUsuario().getIdOrgaoUsu();
        	}
        	
        	if (getDtReg() != null) {
        		Calendar c1 = Calendar.getInstance();
        		c1.setTime(getDtReg());
        		int year = c1.get(Calendar.YEAR);
        		query += " and year(" + (getNumSequencia() != null ? "solicitacaoPai.dtReg" : "dtReg") + ") = " + year;
        	}
        	if (getNumSolicitacao() != null)
        		query += " and numSolicitacao = " + getNumSolicitacao();
        	if (getNumSequencia() == null)
        		query += " and (numSequencia is null or numSequencia = 0)";
        	else
        		query += " and numSequencia = " + getNumSequencia();
        }
        try{
        	return (SrSolicitacao) AR.em().createQuery(query).getSingleResult();
        } catch(NoResultException nre){
        	return null;
       // 	throw new AplicacaoException("Não foi encontrada uma solicitação com o código " + sigla);
        }
    }

    @Override
    public List<? extends SrSelecionavel> buscar() throws Exception {
        return null;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void atualizarCodigo() {
        if (isRascunho() || getNumSolicitacao() == null) {
            codigo = "TMPSR-" + (getSolicitacaoInicial() != null ? getSolicitacaoInicial().getIdSolicitacao() : getIdSolicitacao());
            return;
        }

        if (getSolicitacaoPai() != null) {
            codigo = getSolicitacaoPai().getCodigo() + getNumSequenciaString();
            return;
        }

        if (getNumSolicitacao() == null) {
            codigo = "";
            return;
        }

        StringBuilder numString = new StringBuilder();
        numString.append(getNumSolicitacao().toString());
        while (numString.length() < 5)
            numString.insert(0, "0");

        codigo = getOrgaoUsuario().getAcronimoOrgaoUsu() + "-SR-" + getAnoEmissao() + "/" + numString.toString();
    }

    public String getDtRegString() {
        SigaPlayCalendar cal = new SigaPlayCalendar();
        cal.setTime(getDtReg());
        return cal.getTempoTranscorridoString(false);
    }
    
    public SrPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(SrPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getPrioridadeString(){
        return prioridade == null ? "" : prioridade.getDescPrioridade();
    }

    public SrPrioridade getPrioridadeTecnica(){
    	SrMovimentacao ultMov = getUltimaMovimentacao();
		return ultMov != null ? ultMov.getPrioridade() : getPrioridade();
    }

    public String getAtributosString() {
        StringBuilder s = new StringBuilder();
        for (SrAtributoSolicitacao att : getMeuAtributoSolicitacaoSet()) {
            if (att.getValorAtributoSolicitacao() != null)
                s.append(att.getAtributo().getNomeAtributo() + ": " + att.getValorAtributoSolicitacao() + ". ");
        }
        return s.toString();
    }

    // Edson: NecessÃ¡rio porque nao hÃ¯Â¿Â½ binder para arquivo
    public void setArquivo(UploadedFile file) {
    	this.arquivo = SrArquivo.newInstance(file);
    }
    
    public Map<SrGravidade, SrPrioridade> getGravidadesDisponiveisEPrioridades(){
    	Map<SrGravidade, SrPrioridade> m = new LinkedHashMap<SrGravidade, SrPrioridade>();
    	m.put(SrGravidade.SEM_GRAVIDADE, SrPrioridade.BAIXO);
    	m.put(SrGravidade.NORMAL, SrPrioridade.MEDIO);
    	m.put(SrGravidade.GRAVE, SrPrioridade.ALTO);
    	m.put(SrGravidade.MUITO_GRAVE, SrPrioridade.IMEDIATO);
    	return m;
    }

    public String getDtRegDDMMYYYYHHMM() {
        return SrViewUtil.toDDMMYYYYHHMM(getDtReg());
    }

    public String getDtRegDDMMYYYY() {
        return SrViewUtil.toDDMMYYYY(getDtReg());
    }

    public String getDtRegHHMM() {
        return SrViewUtil.toHHMM(getDtReg());
    }
    
	public String getHisDtIniDDMMYYYYHHMM() {
		return SrViewUtil.toDDMMYYYYHHMM(getHisDtIni());
	}

    public Long getProximoNumero() {
    	if (getOrgaoUsuario() == null || getAnoEmissao() == null)
            return SEM_NUMERACAO;
        
        TypedQuery<Long> query = em().createQuery("select max(numSolicitacao) + 1 from SrSolicitacao "
        		+ "where orgaoUsuario.idOrgaoUsu = :idOrgaoUsu and year(dtReg) = :ano and numSequencia is null and numSolicitacao < 90000", Long.class);
        query.setParameter("idOrgaoUsu", getOrgaoUsuario().getIdOrgaoUsu());
        query.setParameter("ano", Integer.valueOf(getAnoEmissao()));
        Long resultado = query.getSingleResult();
        if (resultado == null)
        	return NUMERACAO_INICIAL;
        return resultado;
    }

    public String getAnoEmissao() {
        return SrViewUtil.toYYYY(getDtReg());
    }

    public String getNumSequenciaString() {
        if (getNumSequencia() == null)
            return null;
        return "." + (getNumSequencia() < 10 ? "0" : "") + getNumSequencia().toString();
    }

    public List<SrSolicitacao> getHistoricoSolicitacao() {
    	//por causa do detach no ObjetoObjectInstantiator:
    	if (getSolicitacaoInicial() != null) {
    		setSolicitacaoInicial(SrSolicitacao.AR.findById(getSolicitacaoInicial().getId()));
            return getSolicitacaoInicial().getMeuSolicitacaoHistoricoSet();
        }
        return null;
    }

    public SrSolicitacao getSolicitacaoAtual() {
        if (getHisDtFim() == null)
            return this;
        List<SrSolicitacao> sols = getHistoricoSolicitacao();
        if (sols == null)
            return null;
        return sols.get(0);

    }

    public SrMovimentacao getUltimoAndamento() {
        return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_ANDAMENTO);
    }

    public SrMovimentacao getUltimaMovimentacao() {
        for (SrMovimentacao movimentacao : getMovimentacaoSet())
            return movimentacao;
        return null;
    }

    public SrMovimentacao getUltimaMovimentacaoMesmoSeCanceladaTodoOContexto() {
        for (SrMovimentacao movimentacao : getMovimentacaoSetComCanceladosTodoOContexto())
            return movimentacao;
        return null;
    }

    public SrMovimentacao getUltimaMovimentacaoQuePossuaDescricao() {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getDescricao() != null && mov.getDescricao().length() > 0)
                return mov;
        }
        return null;
    }

    public SrMovimentacao getUltimaMovimentacaoPorTipo(Long idTpMov) {
        for (SrMovimentacao movimentacao : getMovimentacaoSetPorTipo(idTpMov))
            return movimentacao;
        return null;
    }

    public SrMovimentacao getUltimaMovimentacaoCancelavel() {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getNumSequencia() > 1 && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
                    && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_AVALIACAO)
                return mov;
        }
        return null;
    }

    public Set<SrMovimentacao> getMovimentacaoSet() {
        return getMovimentacaoSet(false, null, false, false, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSetPorTipo(Long tipoMov) {
        return getMovimentacaoSet(false, tipoMov, false, false, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoReferenciaSetPorTipo(Long tipoMov) {
        return getMovimentacaoSet(false, tipoMov, false, false, false, true);
    }

    public Set<SrMovimentacao> getMovimentacaoSetComCancelados() {
        return getMovimentacaoSet(true, null, false, false, false, false);
    }
    
    public Set<SrMovimentacao> getMovimentacaoSetPorTipoTodoOContexto(Long tipoMov) {
        return getMovimentacaoSet(false, tipoMov, false, true, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSetComCanceladosTodoOContexto() {
        return getMovimentacaoSet(true, null, false, true, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSetOrdemCrescente() {
        return getMovimentacaoSet(false, null, true, false, false, false);
    }
    
    private Set<SrMovimentacao> getMovimentacaoSetOrdemCrescentePorTipo(Long idTipoMovimentacao) {
        return getMovimentacaoSet(false, idTipoMovimentacao, true, false, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCanceladas, Long tipoMov, boolean ordemCrescente, boolean todoOContexto, boolean apenasPrincipais, boolean inversoJPA) {

        Set<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(new SrMovimentacaoComparator(ordemCrescente));

        Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
        if (todoOContexto) {
            solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
        } else
            solsAConsiderar.add(this);

        for (SrSolicitacao sol : solsAConsiderar) {
            if (sol.getSolicitacaoInicial() != null)
                for (SrSolicitacao instancia : sol.getHistoricoSolicitacao()) {
                    Set<SrMovimentacao> movSet = inversoJPA ? instancia.getMeuMovimentacaoReferenciaSet() : instancia.getMeuMovimentacaoSet();
                    if (movSet != null)
                        for (SrMovimentacao movimentacao : movSet) {
                            if (!considerarCanceladas && movimentacao.isCanceladoOuCancelador())
                                continue;
                            if (tipoMov != null && movimentacao.getTipoMov().getIdTipoMov() != tipoMov)
                                continue;
                            if (apenasPrincipais && !movimentacao.isEntreAsPrincipais())
                                continue;

                            listaCompleta.add(movimentacao);
                        }
                }
        }
        return listaCompleta;
    }

    public boolean jaFoiDesignada() {
        for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
            if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO || mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_FECHAMENTO)
                return true;
        }
        return false;
    }

    public DpLotacao getLotaAtendente() {
        if (getUltimaMovimentacao() != null)
            return getUltimaMovimentacao().getLotaAtendente();
        else
            return null;
    }

    public DpPessoa getAtendente() {
    	if (getUltimaMovimentacao() != null)
    		return getUltimaMovimentacao().getAtendente();
    	else 
    		return null;
    }

    public boolean isFilha() {
        return this.getSolicitacaoPai() != null;
    }
    
	public List<SrPendencia> getPendencias(boolean incluirTerminadas, boolean todoOContexto) {
		Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
    	if (todoOContexto) {
    		solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
    	} else
    		solsAConsiderar.add(this);
		List<SrPendencia> pendentes = new ArrayList<SrPendencia>();
		for (SrSolicitacao s : solsAConsiderar)
		for (SrMovimentacao mov : s.getMovimentacaoSetOrdemCrescentePorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA)) {
			if (incluirTerminadas || !mov.isFinalizadaOuExpirada())
				pendentes.add(new SrPendencia(mov.getDtIniMov(), mov
						.getDtFimMov(), mov.getDescricao(),
						mov.getId(), mov.getMotivoPendencia(), s));
		}
		return pendentes;
	}
    
    public List<SrPendencia> getPendenciasIncluindoTerminadas(){
    	return getPendencias(true, false);
    }
    
    private List<SrIntervaloEmAtendimento> getTrechosNaoPendentes(SrTipoMotivoPendencia... pendenciasADesconsiderar){
    	List<SrIntervaloEmAtendimento> is = new ArrayList<SrIntervaloEmAtendimento>();
    	List<SrTipoMotivoPendencia> listaPendenciasADesconsiderar = 
    			pendenciasADesconsiderar != null ? Arrays.asList(pendenciasADesconsiderar) : new ArrayList<SrTipoMotivoPendencia>();
    	
    	Date iniEmAtendmto = getDtIniEdicao();
    	for (SrPendencia p : getPendenciasIncluindoTerminadas()) {
    		if (listaPendenciasADesconsiderar.contains(p.getMotivo()))
    			continue;
    		if (p.comecouDepoisDe(iniEmAtendmto))
    			is.add(new SrIntervaloEmAtendimento(iniEmAtendmto, p.getInicio(), "Período de atendimento"));
    		if (p.isInfinito())
    			return is;
    		if (p.getFim().after(iniEmAtendmto))
    			iniEmAtendmto = p.getFim();
    	}
    	is.add(new SrIntervaloEmAtendimento(iniEmAtendmto, null, "Período de atendimento"));
    	return is;
    }
    
    private List<SrIntervaloEmAtendimento> getTrechosNaoPendentesPorEtapa(SrEtapaSolicitacao e, SrTipoMotivoPendencia... pendenciasADesconsiderar){
    	Date dtIni = e.getInicio(), dtFim = e.getFim();
    	List<SrIntervaloEmAtendimento> is = new ArrayList<SrIntervaloEmAtendimento>();
    	for (SrIntervaloEmAtendimento i : getTrechosNaoPendentes(pendenciasADesconsiderar)) {
    		if (i.terminouAntesDe(dtIni))
    			continue;
    		if (i.comecouDepoisDe(dtFim))
    			break;
    		SrIntervaloEmAtendimento newI = new SrIntervaloEmAtendimento(i.getInicio(), i.getFim(), i.getDescricao());
    		newI.setHorario(e.getLotaResponsavel());
    		if (i.comecouAntesDe(dtIni))
    			newI.setInicio(dtIni);
    		if (i.isInfinito() || i.terminouDepoisDe(dtFim))
    			newI.setFim(dtFim);
    		is.add(newI);
    	}
    	return is;
    }
    
    public List<SrPendencia> getPendenciasEmAberto(){
    	return getPendenciasEmAberto(false);
    }
    
    public List<SrPendencia> getPendenciasEmAberto(boolean todoOContexto) {
        return getPendencias(false, todoOContexto);
    }
    
    // Edson: ver comentario abaixo, em getTiposAtributoAssociados()
    public Map<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados() throws Exception {
        Map<Long, Boolean> map = new HashMap<Long, Boolean>();
        getAtributoAssociados(map);
        return map;
    }

    public List<SrAtributo> getAtributoAssociados() throws Exception {
        return getAtributoAssociados(null);
    }

    // Edson: isso esta esquisito. A funcao esta praticamente com dois retornos.
    // Talvez ficasse melhor se o SrAtributo ja tivesse a informacao sobre
    // a obrigatoriedade dele
    private List<SrAtributo> getAtributoAssociados(Map<Long, Boolean> map) throws Exception {
        List<SrAtributo> listaFinal = new ArrayList<SrAtributo>();

        SrConfiguracao confFiltro = new SrConfiguracao();
        confFiltro.setDpPessoa(getSolicitante());
        confFiltro.setComplexo(getLocal());
        confFiltro.setItemConfiguracaoFiltro(getItemConfiguracao());
        confFiltro.setAcaoFiltro(getAcao());

        for (SrAtributo t : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
            confFiltro.setAtributo(t);
            SrConfiguracaoCache conf = SrConfiguracao.buscarAssociacao(confFiltro);
            if (conf != null) {
                listaFinal.add(t);
                if (map != null)
                    map.put(t.getIdAtributo(), conf.atributoObrigatorio);
            }
        }

		Collections.sort(listaFinal, new Comparator<SrAtributo>() {
			@Override
			public int compare(SrAtributo o1, SrAtributo o2) {
				return o1.getIdAtributo().compareTo(o2.getIdAtributo());
			}
		});
        return listaFinal;
    }

    public DpLotacao getUltimoAtendenteEtapaAtendimento() {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_ANDAMENTO)
                return mov.getLotaAtendente();
        }
        return null;
    }

    public void setAtributoSolicitacaoList(List<SrAtributoSolicitacaoMap> atributoSolicitacaoList) throws Exception {
        if (atributoSolicitacaoMap == null) 
        	atributoSolicitacaoMap = new LinkedHashMap<Long, SrAtributoSolicitacaoMap>();
        for (SrAtributoSolicitacaoMap atribSolicitacao : atributoSolicitacaoList)
            atributoSolicitacaoMap.put(atribSolicitacao.getIdAtributo(), atribSolicitacao);    
    }
        
    public void setAtributoSolicitacaoMap(Map<Long, SrAtributoSolicitacaoMap> atributoSolicitacaoMap) {
		this.atributoSolicitacaoMap = atributoSolicitacaoMap;
	}

	public Map<Long, SrAtributoSolicitacaoMap> getAtributoSolicitacaoMap() {
		boolean todoOContexto = false;
    	if (atributoSolicitacaoMap != null)
    		return atributoSolicitacaoMap;
		if (isAtributoDaEntidadeCarregado("meuAtributoSolicitacaoSet")) {
        	atributoSolicitacaoMap = new LinkedHashMap<Long, SrAtributoSolicitacaoMap>();
			for (SrAtributoSolicitacao att : getAtributoSolicitacaoSetAtual(todoOContexto)) 
				if(att.getAtributo() != null) {
					atributoSolicitacaoMap.put(att.getAtributo().getAtual().getIdAtributo(), 
							new SrAtributoSolicitacaoMap(att.getId(), att.getAtributo().getAtual().getIdAtributo(), att.getValorAtributoSolicitacao()));
				}
			return atributoSolicitacaoMap;
		}
		return new LinkedHashMap<Long, SrAtributoSolicitacaoMap>();
    }

    public Set<SrSolicitacao> getSolicitacaoFilhaSet() {
        Set<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(new Comparator<SrSolicitacao>() {
            @Override
            public int compare(SrSolicitacao s1, SrSolicitacao s2) {
                return s1.getNumSequencia().compareTo(s2.getNumSequencia());
            }
        });

        if (getSolicitacaoInicial() != null) {
            for (SrSolicitacao sol : getHistoricoSolicitacao()) {
                if (sol.getMeuSolicitacaoFilhaSet() != null && sol.getMeuSolicitacaoFilhaSet().size() > 0)
                    for (SrSolicitacao filha : sol.getMeuSolicitacaoFilhaSet())
                        if (filha.getHisDtFim() == null)
                            listaCompleta.add(filha);
            }
        }
        return listaCompleta;
    }
    
    public Set<SrPrioridadeSolicitacao> getPrioridadeSolicitacaoSet() {
        Set<SrPrioridadeSolicitacao> listaCompleta = new HashSet<SrPrioridadeSolicitacao>();
        if (getSolicitacaoInicial() != null) {
            for (SrSolicitacao sol : getHistoricoSolicitacao()) {
                if (sol.getMeuPrioridadeSolicitacaoSet() != null)
                	listaCompleta.addAll(sol.getMeuPrioridadeSolicitacaoSet());
            }
        }
        return listaCompleta;
    }

    public Set<SrSolicitacao> getSolicitacaoFilhaSetRecursivo() {
        Set<SrSolicitacao> listaCompleta = new LinkedHashSet<SrSolicitacao>();
        listaCompleta.add(this);
        for (SrSolicitacao filha : getSolicitacaoFilhaSet())
            listaCompleta.addAll(filha.getSolicitacaoFilhaSetRecursivo());
        return listaCompleta;
    }

	public SrSolicitacao  getUltimaSolicitacaoFilhaNaoAtiva() {
		for (SrMovimentacao mov: getMovimentacaoSetComCanceladosTodoOContexto()) {
			if (mov.getSolicitacao().isFilha() && !mov.getSolicitacao().isAtivo())
				return mov.getSolicitacao();
		}
		return null;
	}
	
    public boolean isPai() {
        return getSolicitacaoFilhaSet() != null && !getSolicitacaoFilhaSet().isEmpty();
    }

    public Set<SrMarca> getMarcaSet() {
        Set<SrMarca> listaCompleta = new TreeSet<SrMarca>();
        if (getSolicitacaoInicial() != null)
            for (SrSolicitacao sol : getHistoricoSolicitacao())
                if (sol.getMeuMarcaSet() != null)
                    listaCompleta.addAll(sol.getMeuMarcaSet());
        return listaCompleta;
    }

    public Set<SrMarca> getMarcaSetAtivas() {
        Set<SrMarca> set = new TreeSet<SrMarca>();
        Date agora = new Date();
        for (SrMarca m : getMarcaSet()) {
            if ((m.getDtIniMarca() == null || m.getDtIniMarca().before(agora)) && (m.getDtFimMarca() == null || m.getDtFimMarca().after(agora)))
                set.add(m);
        }
        return set;
    }

    public boolean isJuntada() {
        return sofreuMov(TIPO_MOVIMENTACAO_JUNTADA, TIPO_MOVIMENTACAO_DESENTRANHAMENTO);
    }

    public boolean isEditado() {
        return !getIdSolicitacao().equals(getHisIdIni());
    }

    public boolean isCancelado() {
        return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) != null;
    }

    public Boolean isRascunho() {
        return getRascunho() != null ? getRascunho() : false;
    }

    public boolean isFechado() {
        return sofreuMov(TIPO_MOVIMENTACAO_FECHAMENTO, TIPO_MOVIMENTACAO_REABERTURA) && !isCancelado();
    }

    public boolean isPendente() {
        return getPendenciasEmAberto().size() > 0;
    }
    
    public boolean isTemporaria(){
    	if (getSigla() != null)
    			return getSigla().substring(0, 3).equals("TMP") ? true : false;
    	else 
    		return false;
    }
    
    public boolean isPendenteSemPrevisao(){
    	for (SrPendencia p : getPendenciasEmAberto())
    		if (p.isInfinito())
    			return true;
    	return false;
    }
    
    public boolean possuiPendencia(SrTipoMotivoPendencia tipo){
        for (SrPendencia p : getPendenciasEmAberto()){
                if (p.getMotivo().equals(tipo))
                        return true;
        }
        return false;
    }

    public boolean isAtivo() {
        long[] idsTpsMovs = { TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO, TIPO_MOVIMENTACAO_REABERTURA };
        return sofreuMov(idsTpsMovs, TIPO_MOVIMENTACAO_FECHAMENTO) && !isCancelado() && !isJuntada();
    }
    
    public boolean isEmAndamento() {
        return isAtivo() && !isPendente() && !isJuntada();
    }

    public boolean isAbertaComTodasFilhasFechadas() {
    	if (!isAtivo())
    		return false;
        Set<SrSolicitacao> filhas = getSolicitacaoFilhaSet();
        if (filhas.isEmpty())
            return false;
        for (SrSolicitacao filha : filhas) {
            if (!filha.isFechado() && !filha.isCancelado())
                return false;
        }
        return true;
    }
    
    public SrTipoMotivoFechamento getMotivoFechamentoUltimaFilha(){
    	for (SrMovimentacao fechmto : getMovimentacaoSetPorTipoTodoOContexto(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO))
        	if (!fechmto.getSolicitacao().equivale(this))
        		return fechmto.getMotivoFechamento();        
    	return null;
    }

    public boolean sofreuMov(long idTpMov, long... idsTpsReversores) {
        return sofreuMov(new long[] { idTpMov }, idsTpsReversores);
    }

    public boolean sofreuMov(long[] idsTpsMovs, long... idsTpsReversores) {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            for (long idTpMov : idsTpsMovs)
                if (mov.getTipoMov().getIdTipoMov() == idTpMov)
                    return true;
                else
                    for (long idTpReversor : idsTpsReversores)
                        if (mov.getTipoMov().getIdTipoMov() == idTpReversor)
                            return false;
        }
        return false;
    }

    public SrSolicitacao getSolicitacaoPrincipalJuntada() {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO)
                return null;
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
                return mov.getSolicitacaoReferencia();
        }
        return null;
    }

	public boolean estaCom(DpPessoa pess, DpLotacao lota) {
		return estaSendoAtendidaPor(pess, lota)
				|| ((isRascunho()) && (foiCadastradaPor(pess, lota) || foiSolicitadaPor(
						pess, lota)));
	}
    
    public boolean estaSendoAtendidaPor(DpPessoa pess, DpLotacao lota){
    	if (isFilha() && solicitacaoPai.estaCom(pess, lota))
			return true;
    	SrMovimentacao ultMov = getUltimaMovimentacao();
		return (ultMov != null && ultMov.getLotaAtendente() != null
				&& ultMov.getLotaAtendente().equivale(lota));
    }
  
    public boolean foiSolicitadaPor(DpPessoa pess, DpLotacao lota) {
        return getSolicitante().equivale(pess) || getLotaSolicitante().equivale(lota);
    }

    public boolean foiCadastradaPor(DpPessoa pess, DpLotacao lota) {
        return getCadastrante().equivale(pess) || (getLotaTitular() != null && getLotaTitular().equivale(lota));
    }
    
    public boolean jaFoiAtendidaPor(DpPessoa pess, DpLotacao lota, boolean todoOContexto){
    	Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
    	if (todoOContexto) {
    		solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
    	} else
    		solsAConsiderar.add(this);
    	for (SrSolicitacao sol : solsAConsiderar)
    		for (SrMovimentacao mov : sol.getMovimentacaoSet())
    			if (mov.getLotaAtendente() != null && mov.getLotaAtendente().equivale(lota))
    				return true;
    	return false;
    }

    public boolean isParteDeArvore() {
        return getSolicitacaoPai() != null || (getSolicitacaoFilhaSet() != null && !getSolicitacaoFilhaSet().isEmpty());
    }

    public SrSolicitacao getPaiDaArvore() {
        SrSolicitacao pai = this;
        while (pai.getSolicitacaoPai() != null) {
            pai = pai.getSolicitacaoPai();
        }
        return pai;
    }
    
    private boolean temOutrasFilhasAbertas(SrSolicitacao filha) {
        for (SrSolicitacao outraFilha : getSolicitacaoFilhaSet()){
            if (!outraFilha.equivale(filha) && outraFilha.isAtivo())
            	return true;
        }
        return false;    	
    }

    public Set<SrArquivo> getArquivosAnexos(boolean todoOContexto) {
    	Set<SrSolicitacao> solsAConsiderar = getSolicitacoesDependendoDoContexto(todoOContexto);
    	Set<SrArquivo> arqs = new TreeSet<SrArquivo>();
    	for (SrSolicitacao s : solsAConsiderar){
    		String numSequencia = s.isFilha() ? " (" + s.getNumSequenciaString() + ")" : "";
    		if (s.getArquivoAnexoNaCriacao() != null)
    			arqs.add(s.getArquivoAnexoNaCriacao().setDescricaoComplementar(numSequencia));
    		for (SrMovimentacao mov : s.getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO)) {
    			if (mov.getArquivo() != null)
    				arqs.add(mov.getArquivo().setDescricaoComplementar((mov.getDescricao() != null ? mov.getDescricao(): "") + numSequencia));
    		}
    	}
    	return arqs;
    }

    public boolean podeEscalonar(DpPessoa pess, DpLotacao lota) {
        return estaCom(pess, lota) && isAtivo();
    }

    public boolean podeJuntar(DpPessoa pess, DpLotacao lota) {
    	return estaCom(pess, lota) && isEmAndamento();
    }

    public boolean podeDesentranhar(DpPessoa pess, DpLotacao lota) {
        return estaCom(pess, lota) && isJuntada();
    }

    public boolean podeVincular(DpPessoa titular, DpLotacao lotaTitular) {
        return !isRascunho();
    }

    public boolean podeDesfazerMovimentacao(DpPessoa pess, DpLotacao lota) {
        SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
        if (ultCancelavel == null || ultCancelavel.getTitular() == null || ultCancelavel.getLotaTitular() == null)
            return false;
        return ultCancelavel.getLotaTitular().equivale(lota);
    }

    public boolean podeEditar(DpPessoa pess, DpLotacao lota) {
        return (estaCom(pess, lota) || isEmListaPertencenteA(lota)) && isRascunho() && (!jaFoiDesignada());
    }

    public boolean podePriorizar(DpPessoa pess, DpLotacao lota) {
        return podeEditar(pess, lota);
    }

    public boolean podeAbrirJaFechando(DpPessoa pess, DpLotacao lota) {
        return false;
    }

    public boolean podeFechar(DpPessoa pess, DpLotacao lota) {
        return estaCom(pess, lota) && (isEmAndamento());
    }
    
    public boolean podeReclassificar(DpPessoa pess, DpLotacao lota) {
        return estaCom(pess, lota) && !isRascunho();
    }

    public boolean podeExcluir(DpPessoa pess, DpLotacao lota) {
        return foiCadastradaPor(pess, lota) && isRascunho();
    }

    public boolean podeCancelar(DpPessoa pess, DpLotacao lota) {
        return estaCom(pess, lota) && isEmAndamento();
    }

    public boolean podeDeixarPendente(DpPessoa pess, DpLotacao lota) {
        return isRascunho() || ((isAtivo() && !isJuntada()) && estaCom(pess, lota));
    }

    public boolean podeAlterarPrioridade(DpPessoa pess, DpLotacao lota) {
        return isAtivo() && estaCom(pess, lota);
    }

    public boolean podeTerminarPendencia(DpPessoa pess, DpLotacao lota) {
        return isPendente() && estaCom(pess, lota);
    }

    public boolean podeReabrir(DpPessoa pess, DpLotacao lota) {
        return isFechado() && (estaCom(pess, lota) || foiCadastradaPor(pess, lota) || foiSolicitadaPor(pess, lota));
    }

    public boolean podeAnexarArquivo(DpPessoa pess, DpLotacao lota) {
    	return (isAtivo() && !isJuntada() || isPendente() || isRascunho());
    }

    public boolean podeImprimirTermoAtendimento(DpPessoa pess, DpLotacao lota) {
    	return isAtivo();
    }

    public boolean podeIncluirEmLista(DpPessoa pess, DpLotacao lota) {
    	return isAtivo();
    }

    public boolean podeTrocarAtendente(DpPessoa pess, DpLotacao lota) {
    	return estaCom(pess, lota) && isAtivo();
    }

    public boolean podeResponderPesquisa(DpPessoa pess, DpLotacao lota) throws Exception {

        if (!isFechado() || !foiSolicitadaPor(pess, lota)
        /* || !temPesquisaSatisfacao() */)
            return false;

        for (SrMovimentacao mov : getMovimentacaoSet())
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO)
                return false;
            else if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
                return true;

        return false;

    }

    public boolean podeFecharAutomatico() {
        return isFechadoAutomaticamente() && isEmAndamento() && isAbertaComTodasFilhasFechadas();
    }
    
    public boolean podeEditarAtributo(DpPessoa pess, DpLotacao lota) {
    	return estaCom(pess, lota) && isAtivo();
    }

    @SuppressWarnings("unchecked")
    public SrSolicitacao deduzirLocalRamalEMeioContato() {

        if (getSolicitante() == null)
            return this;

        if (getLotaSolicitante() == null)
            setLotaSolicitante(getSolicitante().getLotacao());

        // Tenta buscar a ultima aberta pelo solicitante
        String queryString = "from SrSolicitacao sol where sol.idSolicitacao = (" + "	select max(idSolicitacao) from SrSolicitacao " + "	where solicitante.idPessoa in ("
                + "		select idPessoa from DpPessoa " + "		where idPessoaIni = " + getSolicitante().getIdPessoaIni() + "))";
        List<SrSolicitacao> listaProvisoria = AR.em().createQuery(queryString).getResultList();
        SrSolicitacao ultima = null;
        if (listaProvisoria != null && !listaProvisoria.isEmpty())
            ultima = listaProvisoria.get(0);

        // Tenta buscar a ultima aberta pela lotacao dele
        if (ultima == null && getLotaSolicitante() != null) {
            queryString = "from SrSolicitacao sol where sol.idSolicitacao = (" + "	select max(idSolicitacao) from SrSolicitacao " + "	where lotaSolicitante.idLotacao in ("
                    + "		select idLotacao from DpLotacao " + "		where idLotacaoIni = " + getLotaSolicitante().getIdLotacaoIni() + "))";
            listaProvisoria = AR.em().createQuery(queryString).getResultList();
            if (listaProvisoria != null && !listaProvisoria.isEmpty())
                ultima = listaProvisoria.get(0);
        }

        if (ultima != null) {
            setTelPrincipal(ultima.getTelPrincipal());
            setLocal(ultima.getLocal());
            setMeioComunicacao(ultima.getMeioComunicacao());
            setEndereco(ultima.getEndereco());
        } else {
            setTelPrincipal("");
            setLocal(null);
            setEndereco("");
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public Map<CpOrgaoUsuario, List<CpComplexo>> getLocaisDisponiveis() {
        List<CpComplexo> locais = new ArrayList<CpComplexo>();
        Map<CpOrgaoUsuario, List<CpComplexo>> hashFinal = new HashMap<CpOrgaoUsuario, List<CpComplexo>>();
        if (getSolicitante() != null && getSolicitante().getId() != null)
            locais = AR.em().createQuery("from CpComplexo order by nomeComplexo")
                        .getResultList();
        for (CpComplexo c : locais){
        	if (hashFinal.get(c.getOrgaoUsuario()) == null)
                hashFinal.put(c.getOrgaoUsuario(), new ArrayList<CpComplexo>());
        	hashFinal.get(c.getOrgaoUsuario()).add(c);
        }
        return hashFinal;
    }

    @SuppressWarnings("unchecked")
    public Map<CpOrgaoUsuario, List<CpComplexo>> getLocaisParaBusca() {
        List<CpComplexo> locais = new ArrayList<CpComplexo>();
        Map<CpOrgaoUsuario, List<CpComplexo>> hashFinal = new HashMap<CpOrgaoUsuario, List<CpComplexo>>();
        locais = AR.em().createQuery("from CpComplexo order by nomeComplexo").getResultList();
        for (CpComplexo c : locais){
        	if (hashFinal.get(c.getOrgaoUsuario()) == null)
                hashFinal.put(c.getOrgaoUsuario(), new ArrayList<CpComplexo>());
        	hashFinal.get(c.getOrgaoUsuario()).add(c);
        }
        return hashFinal;
    }

    public List<SrItemConfiguracao> getItensDisponiveis() throws Exception {
    	List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();
    	if (getSolicitante() == null)
            return listaFinal;

        List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
        List<SrItemConfiguracao> listaTodosItens = SrItemConfiguracao.listar(false);

        for (SrItemConfiguracao i : listaTodosItens) {
            if (!i.isEspecifico())
                continue;
            for (SrConfiguracao c : listaPessoasAConsiderar)
                if (!listaFinal.contains(i)) {

                    c.setItemConfiguracaoFiltro(i);

                    if (SrConfiguracao.buscarDesignacao(c, new int[] { SrConfiguracaoBL.ACAO }) != null) {
                        listaFinal.add(i);
                        SrItemConfiguracao itemPai = i.getPai();
                        while (itemPai != null) {
                            if (!listaFinal.contains(itemPai))
                                listaFinal.add(itemPai);
                            itemPai = itemPai.getPai();
                        }
                    }
                }
        }

        Collections.sort(listaFinal, new SrItemConfiguracaoComparator());

        return listaFinal;
    }

    public List<SrAcao> getAcoesDisponiveis() throws Exception {
        List<SrTarefa> acoesEAtendentes = getAcoesDisponiveisComAtendente();
        List<SrAcao> acoes = new ArrayList<SrAcao>();

        if (acoesEAtendentes == null)
            return null;
        for (SrTarefa t : acoesEAtendentes)
            acoes.add(t.acao);
        return acoes;
    }

    public List<SrTarefa> getAcoesDisponiveisComAtendente() throws Exception {

    	 List<SrTarefa> listaFinal = new ArrayList<SrTarefa>();
        if (getSolicitante() == null)
            return listaFinal;

        Set<SrTarefa> setTerafa = new HashSet<SrTarefa>();
        List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
        SrTarefa tarefa = null;

        for (SrAcao a : SrAcao.listar(false)) {
            if (!a.isEspecifico())
                continue;
            for (SrConfiguracao c : listaPessoasAConsiderar) {
                c.setItemConfiguracaoFiltro(getItemConfiguracao());
                c.setAcaoFiltro(a);
                c.setCpTipoConfiguracao(SrTipoDeConfiguracao.DESIGNACAO);

                List<SrConfiguracaoCache> confs = SrConfiguracao.listar(c, new int[] { SrConfiguracaoBL.ATENDENTE });
                if (!confs.isEmpty())
                    for (SrConfiguracaoCache conf : confs) {
                        tarefa = this.new SrTarefa();
                        tarefa.acao = a;
                        tarefa.conf = conf;
                        setTerafa.add(tarefa);
                    }
            }
        }
        listaFinal.addAll(setTerafa);
        return listaFinal;
    }

    public Map<SrAcao, List<SrTarefa>> getAcoesEAtendentes() throws Exception {
        Map<SrAcao, List<SrTarefa>> acoesEAtendentesFinal = new TreeMap<SrAcao, List<SrTarefa>>(new Comparator<SrAcao>() {
            @Override
            public int compare(SrAcao a1, SrAcao a2) {
                int i = a1.getTituloAcao().compareTo(a2.getTituloAcao());
                if (i != 0)
                    return i;
                return a1.getIdAcao().compareTo(a2.getIdAcao());
            }
        });

        List<SrTarefa> acoesEAtendentes = getAcoesDisponiveisComAtendente();
        if (acoesEAtendentes != null) {
        	
        	if (this.getAcao() == null) {
        		   if (acoesEAtendentes.size() == 1)
        		           this.setAcao(acoesEAtendentes.get(0).acao);
        		    else
        		        this.setAcao(null);
        	}

			for (SrTarefa t : acoesEAtendentes) {
				 // TODO:[refatoracao] - Estava inserindo null na lista.
				if (t.getAcao().getPai() != null) {
					List<SrTarefa> tarefas = acoesEAtendentesFinal.get(t.getAcao().getPai());
					if (tarefas == null)
						tarefas = new ArrayList<SrTarefa>();
					tarefas.add(t);
					acoesEAtendentesFinal.put(t.getAcao().getPai(), tarefas);
				}
			}

            // Edson: melhor se fosse um SortedSet
            for (List<SrTarefa> tarefas : acoesEAtendentesFinal.values()) {
                Collections.sort(tarefas, new Comparator<SrTarefa>() {
                    @Override
                    public int compare(SrTarefa o1, SrTarefa o2) {
                        int i = o1.acao.getTituloAcao().compareTo(o2.acao.getTituloAcao());
                        if (i != 0)
                            return i;
                        return o1.acao.getIdAcao().compareTo(o2.acao.getIdAcao());
                    }
                });
            }

        }

        return acoesEAtendentesFinal;
    }

    @SuppressWarnings("serial")
    public SortedSet<SrOperacao> operacoes(final DpPessoa pess, final DpLotacao lota) throws Exception {
    	SortedSet<SrOperacao> operacoes = new TreeSet<SrOperacao>() {
    		@Override
			public boolean add(SrOperacao e) {
    			if (!e.isModal())
    				e.par("sigla", getSiglaCompacta());
				if (!e.isPode())
					return false;
				return super.add(e);
			}
        };

        operacoes.add(new SrOperacao("pencil", "Editar", podeEditar(pess, lota), "editar"));

        operacoes.add(new SrOperacao("table_relationship", "Vincular", podeVincular(pess, lota), "vincular", MODAL_TRUE));

        operacoes.add(new SrOperacao("arrow_divide", "Escalonar", podeEscalonar(pess, lota), "escalonar", MODAL_TRUE));

        operacoes.add(new SrOperacao("arrow_join", "Juntar", podeJuntar(pess, lota), "juntar", MODAL_TRUE));

        operacoes.add(new SrOperacao("arrow_out", "Desentranhar", podeDesentranhar(pess, lota), "desentranhar", MODAL_TRUE));

        operacoes.add(new SrOperacao("text_list_numbers", "Incluir em Lista", podeIncluirEmLista(pess, lota), "incluirEmLista", MODAL_TRUE));

        operacoes.add(new SrOperacao("lock", "Fechar", podeFechar(pess, lota), "fechar", MODAL_TRUE));
        
        operacoes.add(new SrOperacao("tag_blue_edit", "Reclassificar", podeReclassificar(pess, lota), "reclassificar", MODAL_TRUE));

        /*
         * operacoes.add(new SrOperacao("script_edit", "Responder Pesquisa", podeResponderPesquisa(pess, lota), "responderPesquisa", MODAL_TRUE));
         */

        operacoes.add(new SrOperacao("cross", "Cancelar Solicita\u00E7\u00E3o", podeCancelar(pess, lota), "cancelar"));

        operacoes.add(new SrOperacao("lock_open", "Reabrir", podeReabrir(pess, lota), "reabrir"));

        operacoes.add(new SrOperacao("clock_pause", "Incluir Pend\u00EAncia", podeDeixarPendente(pess, lota), "deixarPendente", MODAL_TRUE));

        operacoes.add(new SrOperacao("clock_edit", "Alterar Prioridade",
                podeAlterarPrioridade(pess, lota), "alterarPrioridade",
                "modal=true"));
        
        operacoes.add(new SrOperacao("cross", "Excluir", "excluir", podeExcluir(pess, lota), "Deseja realmente excluir esta solicita\u00E7\u00E3o?", null, "", ""));

        operacoes.add(new SrOperacao("attach", "Anexar Arquivo", podeAnexarArquivo(pess, lota), "anexarArquivo", MODAL_TRUE + "&solicitacao.id=" + getId()));

        operacoes.add(new SrOperacao("printer", "Termo de Atendimento", podeImprimirTermoAtendimento(pess, lota), "termoAtendimento", "popup=true"));

        SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
        operacoes.add(new SrOperacao("cancel", "Desfazer " + (ultCancelavel != null ? ultCancelavel.getTipoMov().getNome() : ""), podeDesfazerMovimentacao(pess, lota),
                "desfazerUltimaMovimentacao"));

        return operacoes;
    }

    public void salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        this.setCadastrante(cadastrante);
        this.setLotaCadastrante(lotaCadastrante);
        this.setTitular(titular);
        this.setLotaTitular(lotaTitular);
        salvarComHistorico();
    }

    @SuppressWarnings("unused")
    @Override
    public void salvarComHistorico() throws Exception {

       	completarPreenchimento();
        
       	if (getArquivo() != null) {
            double lenght = (double) getArquivo().getBlob().length / 1024 / 1024;
            if (lenght > 2)
                throw new AplicacaoException("O tamanho do arquivo (" + new DecimalFormat("#.00").format(lenght) + "MB) \u00E9 maior que o m\u00E1ximo permitido (2MB)");
        }
        
        if (!isRascunho() && getDesignacao() == null)
            throw new AplicacaoException("Não foi encontrado nenhum atendente designado " + "para esta solicitação. Sugestão: alterar item de " + "configuração e/ou ação");
        
        if (isFilha()) {
            if (getDescrSolicitacao() != null && (getDescrSolicitacao().equals(getSolicitacaoPai().getDescrSolicitacao()) || getDescrSolicitacao().trim().isEmpty()))
                setDescrSolicitacao(null);

            if (this.meuAtributoSolicitacaoSet != null)
                this.meuAtributoSolicitacaoSet = null;
        }
        
        setDtReg(new Date());
                         
        // Edson: Ver por que isto está sendo necessário. Sem isso, após o salvar(),
        // ocorre LazyIniException ao tentar acessar esses meuMovimentacaoSet's
        if (getSolicitacaoInicial() != null){
        	for (SrSolicitacao s : getSolicitacaoInicial().getMeuSolicitacaoHistoricoSet()) {
        		for (SrMovimentacao m : s.getMeuMovimentacaoSet()) {
        		}
        	}
        }

    	// DB1: Verifica se é uma Solicitação Filha, pois caso seja não
    	// deve atualizar o número da solicitação, caso contrário não
    	// funcionará o filtro por código para essa filha
        if (getNumSolicitacao() == null && !isRascunho() && !isFilha()) {	
            setNumSolicitacao(getProximoNumero());
            atualizarCodigo();
            setTempoDecorridoCadastro(getCadastro().getDecorridoEmSegundos());
        }
        
        try {
        	super.salvarComHistorico();
        	ContextoPersistencia.em().flush();
        } catch (PersistenceException pe) {
        	Throwable t = pe.getCause();
        	if (t instanceof ConstraintViolationException) 
        		throw new AplicacaoException("Não foi possível gravar sua solicitação. Tente novamente."); 
        } 
        salvarAtributosComHistorico(getCadastrante(), getLotaCadastrante());
        
    	refresh();
        getSolicitacaoInicial().refresh();

        // Edson: melhorar isto, pra nao precisar salvar novamente
        if (isRascunho()) {
            atualizarCodigo();
            save();
        }

        if (!isRascunho() && !jaFoiDesignada()) {

            if (isFecharAoAbrir())
                fechar(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular(), 
                		getMotivoFechamentoAbertura(), SrTipoMotivoFechamento.ATENDIMENTO_CONCLUÍDO);
            else
                iniciarAtendimento(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular());

            incluirEmListasAutomaticas();

            try{
             if (!isEditado() && getFormaAcompanhamento() != SrFormaAcompanhamento.ABERTURA_FECHAMENTO)
            	 CorreioHolder
            	 	.get()
            	 	.notificarAbertura(this);
            } catch(Exception e){
            	log.error("Erro ao notificar", e);
            }
            
        } else
            atualizarMarcas();
        
        if (isFilha())
        	getSolicitacaoPai().deixarPendenteAguardandoFilha(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular(), this);
    }

    private void salvarAtributosComHistorico(DpPessoa cadastrante, DpLotacao lotaCadastrante) throws Exception {
    	if (getAtributoSolicitacaoMap() != null) {
    		for (Map.Entry<Long, SrAtributoSolicitacaoMap> atributo : getAtributoSolicitacaoMap().entrySet()) {
    			if (atributo.getValue() == null || !atributo.getValue().isValorPreenchido())
    				continue;
    			SrAtributoSolicitacao atributoSolicitacao = criarAtributoDaSolicitacao(atributo.getKey(),atributo.getValue(), cadastrante, lotaCadastrante);
    			if (!atributo.getValue().isAtributoSolicitacaoInicial())
    				atributoSolicitacao.setId(atributo.getValue().getIdAtributoSolicitacao());
    			atributoSolicitacao.salvarComHistorico();
    		}
    	}
	}
    
    private void definirAtributoSolicitacaoMapComoInicial() {
    	if (getAtributoSolicitacaoMap() != null) {
    		for (Map.Entry<Long, SrAtributoSolicitacaoMap> atributo : getAtributoSolicitacaoMap().entrySet()) {
    			if (atributo.getValue() != null)
    				atributo.getValue().definirAtributoSolicitacaoComoInicial();
    		}
    	}    	
    }
    
    private SrAtributoSolicitacao criarAtributoDaSolicitacao(Long idTipoAtributo, SrAtributoSolicitacaoMap atributo, DpPessoa cadastrante, DpLotacao lotaCadastrante) {
		SrAtributo tipoAtributo = SrAtributo.AR.findById(idTipoAtributo);
		return new SrAtributoSolicitacao(atributo.getValorAtributo(), tipoAtributo, this, cadastrante, lotaCadastrante);
    }
           
    private void incluirEmListasAutomaticas() throws Exception {
        for (ListaInclusaoAutomatica dadosInclusao : getListasParaInclusaoAutomatica(getLotaCadastrante())) {
            incluirEmLista(dadosInclusao.getLista(), getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular(), dadosInclusao.getPrioridadeNaLista(), Boolean.FALSE);
        }
    }

    public void excluir() throws Exception {
        finalizar();
        for (SrMarca e : getMarcaSet()) {
            e.getSolicitacao().getMeuMarcaSet().remove(e);
            e.delete();
        }
    }

    public void atualizarAcordos() throws Exception {
        setAcordos(new ArrayList<SrAcordo>());

        SrConfiguracao c = new SrConfiguracao();
        c.setDpPessoa(getSolicitante());
        c.setLotacao(getLotaSolicitante());
        c.setComplexo(getLocal());
        c.setBuscarPorPerfis(true);
        c.setItemConfiguracaoFiltro(getItemConfiguracao());
        c.setAcaoFiltro(getAcao());
        c.setPrioridade(getPrioridade());
        c.setAtendente(getLotaTitular());
        c.setComplexo(getLocal());
        c.setBuscarPorPerfis(true);
        c.setCpTipoConfiguracao(SrTipoDeConfiguracao.ABRANGENCIA_ACORDO);
        
        List<SrConfiguracaoCache> confs = SrConfiguracao.listar(c);
        for (SrConfiguracaoCache conf : confs) {
        	if (conf.acordo != 0) {
        		SrAcordo acordoAtual = SrAcordo.AR.findById(conf.acordo).getAcordoAtual();
        		if (acordoAtual != null && acordoAtual.getHisDtFim() == null && !getAcordos().contains(acordoAtual)
        			&& acordoAtual.contemParametro(SrParametro.CADASTRO))
        			getAcordos().add(acordoAtual);
        	}
        }
    }

    public void completarPreenchimento() throws Exception {

        if (getCadastrante() == null)
            throw new AplicacaoException("Cadastrante n\u00E3o pode ser nulo");
        
        if (getDtIniEdicao() == null)
            setDtIniEdicao(new Date());

        if (getLotaCadastrante() == null || getLotaCadastrante().getId() == null)
            setLotaCadastrante(getCadastrante().getLotacao());

        if (getTitular() == null || getTitular().getId() == null)
            setTitular(getCadastrante());

        if (getLotaTitular() == null || getLotaTitular().getId() == null)
            setLotaTitular(getTitular().getLotacao());

        if (getSolicitante() == null || getSolicitante().getId() == null)
            setSolicitante(getTitular());

        if (getLotaSolicitante() == null || getLotaSolicitante().getId() == null)
            setLotaSolicitante(getSolicitante().getLotacao());

        if (getSolicitante().equivale(getCadastrante())) 
            setMeioComunicacao(null);
        else if (getMeioComunicacao() == null)
    		setMeioComunicacao(SrMeioComunicacao.TELEFONE);
        
        if (getDtOrigem() == null)
            setDtOrigem(new Date());

        if(getLotaSolicitante() != null) {
        	 if (getOrgaoUsuario() == null || getOrgaoUsuario().getId() == null)
                 setOrgaoUsuario(getLotaSolicitante().getOrgaoUsuario());
        }
       
        if (getFormaAcompanhamento() == null)
        	setFormaAcompanhamento(SrFormaAcompanhamento.ABERTURA_ANDAMENTO);

        if (getGravidade() == null)
            setGravidade(SrGravidade.SEM_GRAVIDADE);

        setPrioridade(getGravidadesDisponiveisEPrioridades().get(getGravidade()));
        
        atualizarAcordos();
    }

    public void desfazerUltimaMovimentacao(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (!podeDesfazerMovimentacao(cadastrante, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
       
        boolean podeDeixarPaiPendente = false;
        SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();

        if (movimentacao != null) {
            if (movimentacao.getTipoMov() != null) {
            	
                if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO 
                		|| movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_FECHAMENTO) {    
                	if(isFilha()) {
	                    if (getSolicitacaoPai().isAtivo()) 
	                    	podeDeixarPaiPendente = true;
	                	else
	                		throw new AplicacaoException("Operação não permitida: A solicitação principal " +
	                			this.getSolicitacaoPai().getCodigo() + " não está ativa.");
                	}
                    reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
                }
                if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_JUNTADA) {
                    this.save();
                }
                
            }

            movimentacao.desfazer(cadastrante, lotaCadastrante, titular, lotaTitular);
            if (podeDeixarPaiPendente) 
            	getSolicitacaoPai().deixarPendenteAguardandoFilha(getCadastrante(), getLotaCadastrante(), 
            			getTitular(), getSolicitacaoPai().getLotaAtendente(), this);       
        }
    }

    public SrSolicitacao criarFilhaSemSalvar() throws Exception {
        SrSolicitacao filha = new SrSolicitacao();
        Util.copiar(filha, this);
        filha.setIdSolicitacao(null);
        filha.setSolicitacaoPai(this);
        filha.setNumSolicitacao(this.getNumSolicitacao());
        filha.setRascunho(null);
        filha.setSolicitacaoInicial(null);
        filha.setMeuMovimentacaoSet(null);
        filha.setAcordos(null);
        filha.setDtIniEdicao(new Date());
        filha.setMeuMovimentacaoReferenciaSet(null);
        for (SrSolicitacao s : getSolicitacaoFilhaSet())
            filha.setNumSequencia(s.getNumSequencia());
        if (filha.getNumSequencia() == null)
            filha.setNumSequencia(1L);
        else
            filha.setNumSequencia(filha.getNumSequencia() + 1);
        filha.atualizarCodigo();
        return filha;
    }

    public void atualizarMarcas() throws Exception {
        SortedSet<SrMarca> setA = new TreeSet<SrMarca>();

        // Edson: Obtido do sigagc - Excluir marcas duplicadas (???)
        for (SrMarca m : getMarcaSet()) {
            if (setA.contains(m))
                m.delete();
            else
                setA.add(m);
        }
        SortedSet<SrMarca> setB = calcularMarcadores();
        Set<SrMarca> marcasAIncluir = new TreeSet<SrMarca>();
        Set<SrMarca> marcasAExcluir = new TreeSet<SrMarca>();
        Set<Par<SrMarca, SrMarca>> atualizar = new TreeSet<Par<SrMarca, SrMarca>>();
        encaixar(setA, setB, marcasAIncluir, marcasAExcluir, atualizar);

        if (getMeuMarcaSet() == null)
            setMeuMarcaSet(new TreeSet<SrMarca>());
        for (SrMarca i : marcasAIncluir) {
            i.save();
            getMeuMarcaSet().add(i);
        }
        for (SrMarca e : marcasAExcluir) {
            e.getSolicitacao().getMeuMarcaSet().remove(e);
            e.delete();
        }
        for (Entry<SrMarca, SrMarca> pair : atualizar) {
            SrMarca a = pair.getKey();
            SrMarca b = pair.getValue();
            a.setDpLotacaoIni(b.getDpLotacaoIni());
            a.setDpPessoaIni(b.getDpPessoaIni());
            a.setDtFimMarca(b.getDtFimMarca());
            a.setDtIniMarca(b.getDtIniMarca());
            a.setSolicitacao(b.getSolicitacao());
            a.save();
        }
    }

    private SortedSet<SrMarca> calcularMarcadores() throws Exception {
    	SortedSet<SrMarca> set = new TreeSet<SrMarca>();

    	//Edson: depois, eliminar a necessidade de dar tratamento diferenciado ao
    	//estado Em Elaboracao. Mesma coisa mais abaixo, ao incluir marca
    	//em Elaboracao agendada em razao de pendencia
    	if (isRascunho())
    		acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_EM_ELABORACAO.getId(), null, null, getCadastrante(), getLotaTitular());

    	Set<SrMovimentacao> movs = getMovimentacaoSetOrdemCrescente();

    	if (movs != null && !movs.isEmpty()) {
    		Long marcador = 0L;
    		SrMovimentacao movMarca = null;
    		Long marcadorAndamento = 0L;

    		List<SrMovimentacao> pendencias = new ArrayList<SrMovimentacao>();

    		for (SrMovimentacao mov : movs) {
    			Long t = mov.getTipoMov().getIdTipoMov();
    			if (mov.isCancelada())
    				continue;
    			if (t == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) {
    				marcador = CpMarcadorEnum.SOLICITACAO_ATIVO.getId();
    				movMarca = mov;
    				marcadorAndamento = CpMarcadorEnum.SOLICITACAO_EM_ANDAMENTO.getId();
    			}
    			if (t == TIPO_MOVIMENTACAO_FECHAMENTO) {
    				marcador = CpMarcadorEnum.SOLICITACAO_FECHADO.getId();
    				movMarca = mov;
    				marcadorAndamento = null;
    			}
    			if (t == TIPO_MOVIMENTACAO_REABERTURA) {
    				marcador = CpMarcadorEnum.SOLICITACAO_ATIVO.getId();
    				movMarca = mov;
    				marcadorAndamento = CpMarcadorEnum.SOLICITACAO_EM_ANDAMENTO.getId();
    			}
    			if (t == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) {
    				marcador = CpMarcadorEnum.SOLICITACAO_CANCELADO.getId();
    				movMarca = mov;
    				marcadorAndamento = null;
    			}
    			if (t == TIPO_MOVIMENTACAO_JUNTADA) {
    				marcadorAndamento = CpMarcadorEnum.JUNTADO.getId();
    			}
    			if (t == TIPO_MOVIMENTACAO_DESENTRANHAMENTO) {
    				marcadorAndamento = CpMarcadorEnum.SOLICITACAO_EM_ANDAMENTO.getId();
    			}
    			if (t == TIPO_MOVIMENTACAO_INICIO_PENDENCIA && (mov.getDtFimMov() == null || mov.getDtFimMov().after(new Date()))) {
    				pendencias.add(mov);
    			}

    			if (t == TIPO_MOVIMENTACAO_ANDAMENTO || t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO) {
    				movMarca = mov;
    			}

    		}

    		if (marcador != 0L)
    			acrescentarMarca(set, marcador, movMarca.getDtIniMov(), null, movMarca.getAtendente(), movMarca.getLotaAtendente());

    		if (!pendencias.isEmpty()) {
    			SrMovimentacao ultimaPendencia = pendencias.get(pendencias.size() - 1);
    			Date dtFimPendenciaMaisLonge = null;
    			for (SrMovimentacao pendencia : pendencias) {
    				if (pendencia.getDtAgenda() != null && (dtFimPendenciaMaisLonge == null || pendencia.getDtAgenda().after(dtFimPendenciaMaisLonge)))
    					dtFimPendenciaMaisLonge = pendencia.getDtAgenda();
    			}
    			if (isRascunho()){
    				acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_PENDENTE.getId(), ultimaPendencia.getDtIniMov(), dtFimPendenciaMaisLonge, getCadastrante(), getLotaCadastrante());
    				//Edson: se pendencia eh agendada, agenda a marca Em Elaboracao
    				if (dtFimPendenciaMaisLonge != null)
    					acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_EM_ELABORACAO.getId(), dtFimPendenciaMaisLonge, null,
    							cadastrante, lotaTitular);
    			} else{ 
    				acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_PENDENTE.getId(), ultimaPendencia.getDtIniMov(), dtFimPendenciaMaisLonge, ultimaPendencia.getAtendente(),
    						ultimaPendencia.getLotaAtendente());
    				//Edson: se pendencia eh agendada, agenda a marca Em Andamento
    				//A clausula '||marcador==ATIVO' serve para suportar solicitacoes da epoca
    				//em que era permitido fechar estando pendente
    				if (dtFimPendenciaMaisLonge != null && marcador == CpMarcadorEnum.SOLICITACAO_ATIVO.getId())
    					acrescentarMarca(set, marcadorAndamento, dtFimPendenciaMaisLonge, null,
    							movMarca.getAtendente(), movMarca.getLotaAtendente());
    			}
    		} else {
    			if (marcador == CpMarcadorEnum.SOLICITACAO_ATIVO.getId())
    				acrescentarMarca(set, marcadorAndamento, movMarca.getDtIniMov(), null,
    						movMarca.getAtendente(), movMarca.getLotaAtendente());
    		}

    		if (!isFechado() && !isCancelado()) {
    			if (marcador == CpMarcadorEnum.SOLICITACAO_ATIVO.getId()) {
    				acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_COMO_CADASTRANTE.getId(), null, null, getCadastrante(), getLotaCadastrante());
    				acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_COMO_SOLICITANTE.getId(), null, null, getSolicitante(), getLotaSolicitante());

    				Date prazo = null;
    				SrEtapaSolicitacao atendimentoGeral = getAtendimentoGeral();
    				prazo = atendimentoGeral.getFimPrevisto();
    				if (prazo == null){
    					SrEtapaSolicitacao atendimentoAtual = getUltimoAtendimento();
    					if (atendimentoAtual != null)
    						prazo = atendimentoAtual.getFimPrevisto();
    				}
    				if (prazo != null){
    					acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_FORA_DO_PRAZO.getId(),
    							prazo, null, movMarca.getAtendente(), movMarca.getLotaAtendente());
    					if (isFilha() && !movMarca.getLotaAtendente().equivale(getSolicitacaoPai().getLotaAtendente()))
    						acrescentarMarca(set, CpMarcadorEnum.SOLICITACAO_FORA_DO_PRAZO.getId(),
    								prazo, null, getSolicitacaoPai().getAtendente(), getSolicitacaoPai().getLotaAtendente());
    				}

    			}

    			if (marcador == CpMarcadorEnum.SOLICITACAO_FECHADO.getId() && isFilha())
    				solicitacaoPai.atualizarMarcas();

    			if (isAbertaComTodasFilhasFechadas()){
    				Long marcadorAFechar = CpMarcadorEnum.SOLICITACAO_NECESSITA_PROVIDENCIA.getId();
    				SrTipoMotivoFechamento motivoFilha = getMotivoFechamentoUltimaFilha();
    				//Edson: a aceitação do motivo null é para compatibilizar com fechamentos anteriores à funcionalidade do tipo de fechamento
    				if (motivoFilha == null || motivoFilha.equals(SrTipoMotivoFechamento.ATENDIMENTO_CONCLUÍDO) || motivoFilha.equals(SrTipoMotivoFechamento.TAREFA_CONCLUÍDA))
    					marcadorAFechar = CpMarcadorEnum.SOLICITACAO_FECHADO_PARCIAL.getId();
    				acrescentarMarca(set, marcadorAFechar, movMarca.getDtIniMov(),
    						null, movMarca.getAtendente(), movMarca.getLotaAtendente());
    			} 

    		}
    	}
    	return set;
    }

    private void encaixar(SortedSet<SrMarca> setA, SortedSet<SrMarca> setB, Set<SrMarca> incluir, Set<SrMarca> excluir, Set<Par<SrMarca, SrMarca>> atualizar) {
        Iterator<SrMarca> ia = setA.iterator();
        Iterator<SrMarca> ib = setB.iterator();

        SrMarca a = null;
        SrMarca b = null;

        if (ia.hasNext())
            a = ia.next();
        if (ib.hasNext())
            b = ib.next();
        while (a != null || b != null) {
            if ((a == null) || (b != null && a.compareTo(b) > 0)) {
                // Existe em setB, mas nao existe em setA
                incluir.add(b);
                if (ib.hasNext())
                    b = ib.next();
                else
                    b = null;

            } else if (b == null || (a != null && b.compareTo(a) > 0)) {
                // Existe em setA, mas nao existe em setB
                excluir.add(a);
                if (ia.hasNext())
                    a = ia.next();
                else
                    a = null;
            } else {

                // O registro existe nos dois
                atualizar.add(new Par<SrMarca, SrMarca>(a, b));
                if (ib.hasNext())
                    b = ib.next();
                else
                    b = null;
                if (ia.hasNext())
                    a = ia.next();
                else
                    a = null;
            }
        }
        ib = null;
        ia = null;
    }

    private void acrescentarMarca(SortedSet<SrMarca> set, Long idMarcador, Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota) throws Exception {
        SrMarca mar = new SrMarca();
        // Edson: nao eh necessario ser this.solicitacaoInicial em vez de this
        // porque este metodo soh eh chamado por atualizarMarcas(), que ja se
        // certifica de chamar este metodo apenas para a solicitacao inicial
        mar.setSolicitacao(this);
        mar.setCpMarcador((CpMarcador) CpMarcador.AR.findById(idMarcador));
        if (pess != null)
            mar.setDpPessoaIni(pess.getPessoaInicial());
        if (lota != null)
            mar.setDpLotacaoIni(lota.getLotacaoInicial());
        mar.setDtIniMarca(dtIni);
        mar.setDtFimMarca(dtFim);
        set.add(mar);
    }

    public String getMarcadoresEmHtml() {
        return getMarcadoresEmHtml(null, null);
    }

    public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
        StringBuilder sb = new StringBuilder();
        List<Long> marcadoresDesconsiderar = Arrays.asList(new Long[] { CpMarcadorEnum.SOLICITACAO_COMO_CADASTRANTE.getId(), CpMarcadorEnum.SOLICITACAO_COMO_SOLICITANTE.getId(), CpMarcadorEnum.SOLICITACAO_ATIVO.getId() });

        Set<SrMarca> marcas = getMarcaSetAtivas();

        for (SrMarca mar : marcas) {
            if (marcadoresDesconsiderar.contains(mar.getCpMarcador().getIdMarcador()))
                continue;
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(mar.getCpMarcador().getDescrMarcador());
            sb.append(" (");
            if (mar.getDpPessoaIni() != null) {
                String nome = mar.getDpPessoaIni().getPessoaAtual().getDescricaoIniciaisMaiusculas();
                sb.append(nome.substring(0, nome.indexOf(" ")));
                sb.append(", ");
            }
            if (mar.getDpLotacaoIni() != null) {
                DpLotacao atual = mar.getDpLotacaoIni().getLotacaoAtual();
                if (atual == null)
                    atual = mar.getDpLotacaoIni();
                sb.append(atual.getSigla());
            }
            sb.append(")");
        }

        if (sb.length() == 0)
            return null;
        return sb.toString();
    }

    public boolean isMarcada(long marcador) {
        return isMarcada(marcador, null, null);
    }

    public boolean isMarcada(long marcador, DpLotacao lota) {
        return isMarcada(marcador, lota, null);
    }

    public boolean isMarcada(long marcador, DpLotacao lota, DpPessoa pess) {
        for (SrMarca m : getMarcaSet())
            if (m.getCpMarcador().getIdMarcador().equals(marcador) && (lota == null || m.getDpLotacaoIni().equivale(lota)) && (pess == null || m.getDpPessoaIni().equivale(pess)))
                return true;
        return false;
    }

    @Override
    public boolean semelhante(Assemelhavel obj, int profundidade) {
        return false;
    }

    public List<ListaInclusaoAutomatica> getListasParaInclusaoAutomatica(DpLotacao lotaTitular) throws Exception {
        SrConfiguracao filtro = new SrConfiguracao();
        filtro.setDpPessoa(getSolicitante());
        filtro.setOrgaoUsuario(getOrgaoUsuario());
        filtro.setLotacao(lotaTitular);
        filtro.setPrioridade(getPrioridade());
        filtro.setItemConfiguracaoFiltro(getItemConfiguracao());
        filtro.setAcaoFiltro(getAcaoAtual());
        filtro.setCpTipoConfiguracao(SrTipoDeConfiguracao.DEFINICAO_INCLUSAO_AUTOMATICA);

        List<ListaInclusaoAutomatica> listaFinal = new ArrayList<ListaInclusaoAutomatica>();
        for (SrConfiguracaoCache conf : SrConfiguracao.listar(filtro, new int[] { SrConfiguracaoBL.ATENDENTE, SrConfiguracaoBL.LISTA_PRIORIDADE })) {
            SrLista listaAtual = SrLista.AR.findById(conf.listaPrioridade).getListaAtual();
			if (conf.listaPrioridade != 0 && listaAtual.isAtivo()) {
            	SrLista listaConf = listaAtual;
            	ListaInclusaoAutomatica listaInclusaoAutomatica = new ListaInclusaoAutomatica(listaConf.getListaAtual(), conf.prioridadeNaLista);

                if (!listaFinal.contains(listaInclusaoAutomatica))
                    listaFinal.add(listaInclusaoAutomatica);
            }
        }
        return listaFinal;
    }

    public List<DpPessoa> getPessoasAtendentesDisponiveis() {
        List<DpPessoa> listaFinal = new ArrayList<DpPessoa>();
        if (getLotaAtendente() != null) {
            DpLotacao atendente = DpLotacao.AR.findById(getLotaAtendente().getIdLotacao());
            atendente = atendente.getLotacaoAtual();
            if (atendente == null)
                atendente = getLotaAtendente();
            for (DpPessoa p : atendente.getDpPessoaLotadosSet()) {
                if (p.getHisDtFim() == null)
                    listaFinal.add(p);
            }
            Collections.sort(listaFinal, new Comparator<DpPessoa>() {
                @Override
                public int compare(DpPessoa o1, DpPessoa o2) {
                    if (o1 != null && o2 != null && o1.getId().equals(o2.getId()))
                        return 0;
                    return o1.getNomePessoa().compareTo(o2.getNomePessoa());
                }
            });
        }
        return listaFinal;
    }
    
    @SuppressWarnings("unchecked")
    public List<DpSubstituicao> getSubstitutos(){
            List<DpSubstituicao> listaSubstitutos = new ArrayList<DpSubstituicao>();
            if (getLotaAtendente() != null && getLotaAtendente().getLotacaoAtual() != null){
                    DpLotacao lotaAtendente = getLotaAtendente().getLotacaoAtual();
                    
                    //Edson: por causa do detach no ObjetoObjectInstantiator
                    lotaAtendente = DpLotacao.AR.findById(lotaAtendente.getIdLotacao());

                    listaSubstitutos = ContextoPersistencia.em().createQuery("from DpSubstituicao dps "
                    						+ "where dps.titular = null and dps.lotaTitular.idLotacao in "
                                            + "(select lot.idLotacao from DpLotacao lot where lot.idLotacaoIni = :idLotacaoIni) and "
                                            + "(dtFimSubst = null or dtFimSubst > 	:dbDatetime) and dps.substituto is not null "
                                            + "and dtFimRegistro = null")
                    						.setParameter("dbDatetime", CpDao.getInstance().consultarDataEHoraDoServidor())
                                            .setParameter("idLotacaoIni", lotaAtendente.getIdInicial()).getResultList();
                    
                    // BJN - remover terminados
                    List<DpSubstituicao> fechados = new ArrayList<DpSubstituicao>();
                    for (Iterator<DpSubstituicao> subs = listaSubstitutos.iterator(); subs.hasNext();) {
						DpSubstituicao substituicao = (DpSubstituicao) subs.next();
						if(substituicao.getSubstituto().getPessoaAtual().isFechada())
							fechados.add(substituicao);
					}
                    for (Iterator<DpSubstituicao> png = fechados.iterator(); png.hasNext();) {
						DpSubstituicao remover = (DpSubstituicao) png.next();
						listaSubstitutos.remove(remover);
					}
                    
                    Collections.sort(listaSubstitutos, new Comparator<DpSubstituicao>() {
                    @Override
                    public int compare(DpSubstituicao  o1, DpSubstituicao o2) {
                                    if (o1 != null && o2 != null && o1.getIdSubstituicao().equals(o2.getIdSubstituicao()))
                                            return 0;
                                    return o1.getSubstituto().getNomePessoa().
                                                            compareTo(o2.getSubstituto().getNomePessoa());
                    }
                });
            }
            return listaSubstitutos;
    }

    public List<SrLista> getListasDisponiveisParaInclusao(DpLotacao lotaTitular, DpPessoa titular) throws Exception {
        List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);

        for (SrLista l : SrLista.listar(false)) {
            SrLista atual = l.getListaAtual();
            if (atual.podeIncluir(lotaTitular, titular) && !listaFinal.contains(atual))
                listaFinal.add(atual);
        }
        listaFinal.removeAll(getListasAssociadas());
        Collections.sort(listaFinal, new Comparator<SrLista>() {
            @Override
            public int compare(SrLista l1, SrLista l2) {
                return (l1.getNomeLista() == null) ? -1 : l1.getNomeLista().compareTo(l2.getNomeLista());
            }
        });
        return listaFinal;
    }
    
    public Set<SrLista> getListasAssociadas() {
    	return getListasAssociadas(false);
    }

    public Set<SrLista> getListasAssociadas(boolean todoOContexto) {
    	Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
    	if (todoOContexto) {
    		solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
    	} else
    		solsAConsiderar.add(this);
    	Set<SrLista> listas = new HashSet<SrLista>();
    	for (SrSolicitacao s : solsAConsiderar){
    		for (SrPrioridadeSolicitacao prior : s.getPrioridadeSolicitacaoSet()){
    			listas.add(prior.getLista().getListaAtual());
    		}
    	}
    	return listas;
    }

    public Set<SrSolicitacao> getSolicitacoesVinculadas(boolean todoOContexto) {
    	Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
    	if (todoOContexto) {
    		solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
    	} else
    		solsAConsiderar.add(this);

    	Set<SrSolicitacao> solVinculadas = new HashSet<SrSolicitacao>();

    	for (SrSolicitacao s : solsAConsiderar){
    		for (SrMovimentacao mov : s.getMovimentacaoSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
    			solVinculadas.add(mov.getSolicitacaoReferencia());
    		for (SrMovimentacao mov : s.getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
    			solVinculadas.add(mov.getSolicitacao());
    	}

        return solVinculadas;
    }

    public Set<SrSolicitacao> getSolicitacoesJuntadas(boolean todoOContexto) {
    	 Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
         if (todoOContexto) {
             solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
         } else
             solsAConsiderar.add(this);
         
        Set<SrSolicitacao> solJuntadas = new HashSet<SrSolicitacao>();

        for (SrSolicitacao s : solsAConsiderar)
        for (SrMovimentacao mov : s.getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA))
            if (!mov.isFinalizadaOuExpirada())
                solJuntadas.add(mov.getSolicitacao());
        return solJuntadas;
    }

    public boolean isEmLista() {
        return !getListasAssociadas().isEmpty();
    }

    public boolean isEmListaPertencenteA(DpLotacao lota) {
        for (SrLista l : getListasAssociadas()) {
            if (l.getLotaCadastrante().equivale(lota))
                return true;
        }
        return false;
    }

    public boolean isEmLista(SrLista lista) {
        for (SrLista l : getListasAssociadas())
            if (l.equivale(lista))
                return true;
        return false;
    }

    public long getPrioridadeNaLista(SrLista lista) throws Exception {
        SrPrioridadeSolicitacao prioridadeSolicitacao = lista.getSrPrioridadeSolicitacao(this);
        return prioridadeSolicitacao != null ? prioridadeSolicitacao.getNumPosicao() : -1;
    }

    public void incluirEmLista(SrLista lista, DpPessoa pess, DpLotacao lota, DpPessoa pessTitular, DpLotacao lotaTitular, SrPrioridade prioridade, boolean naoReposicionarAutomatico) throws Exception {
        if (lista == null)
            throw new AplicacaoException("Lista n\u00E3o informada");

        if (isEmLista(lista))
            throw new AplicacaoException("Lista " + lista.getNomeLista() + " j\u00E1 cont\u00E9m a solicita\u00E7\u00E3o " + getCodigo());

        lista.incluir(this, prioridade, naoReposicionarAutomatico);
        ContextoPersistencia.em().flush();
    }

    public void retirarDeLista(SrLista lista, DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (lista == null)
            throw new AplicacaoException("Lista n\u00E3o informada");

        lista.retirar(this, cadastrante, lotaCadastrante);
    }

    private void iniciarAtendimento(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO));
        if (getAtendenteNaoDesignado() == null)
            mov.setLotaAtendente(getDesignacao().getAtendente());
        else
            mov.setLotaAtendente(getAtendenteNaoDesignado());
        
        if (mov.getLotaAtendente().equivale(getLotaTitular()))
        	mov.setAtendente(getTitular());
        
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }
    
    private void fechar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String motivo, SrTipoMotivoFechamento tpMotivo) throws Exception {
    	fechar(cadastrante, lotaCadastrante, titular, lotaTitular, getItemAtual(), getAcaoAtual(), motivo, tpMotivo, null, null);
    }

    public void fechar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, 
    		SrItemConfiguracao itemConfiguracao, SrAcao acao, String motivo, SrTipoMotivoFechamento tpMotivo, 
    		String conhecimento, Map<Long, SrAtributoSolicitacaoMap> atributos) throws Exception {
    	//Impede que seja lançado erro na tela de fechamento de solicitação filha quando ocorre tentativa de fechadamento automático 
    	//da solicitação principal com ação "Atendimento de primeiro nível". 
    	if (isPai() && acao.getTituloAcao().toLowerCase().contains("1º nível"))
    		return;
    	if (isPai() && !isAbertaComTodasFilhasFechadas())
            throw new AplicacaoException("Operação não permitida. Necessário fechar toda solicitação " + 
            			"filha criada a partir dessa que deseja fechar.");

        if ((cadastrante != null) && !podeFechar(cadastrante, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        
        if (itemConfiguracao == null || itemConfiguracao.getId() == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new AplicacaoException("Operação não permitida. Necessário informar um item de configuração e uma ação.");

        if (acao.getTituloAcao().toLowerCase().contains("1º nível")  && !isPai()) 
        	throw new AplicacaoException("Operação não permitida. Necessário reclassificar a solicitação. Selecione uma 'ação' " +
        				"relacionada ao atendimento realizado.");
        
        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_FECHAMENTO));
        mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao.getId()));
        mov.setMotivoFechamento(tpMotivo);
        mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
        mov.setConhecimento(conhecimento);

        mov.setDescrMovimentacao(motivo);
        
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

        removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
        
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        this.setAtributoSolicitacaoMap(atributos);
        salvarAtributosComHistorico(cadastrante, lotaCadastrante);

        if (isFilha()) {
        	DpLotacao lotaAtendente = getSolicitacaoPai().getLotaAtendente(); 
        	getSolicitacaoPai().terminarPendenciaAguardandoFilha(getCadastrante(), getLotaCadastrante(), getTitular(), lotaAtendente);
        	if (getSolicitacaoPai().podeFecharAutomatico())
        		getSolicitacaoPai().fechar(getCadastrante(), getLotaCadastrante(), getTitular(), lotaAtendente, 
        				"Solicitação fechada automaticamente", tpMotivo);
        }      
        /*
         * if (temPesquisaSatisfacao()) enviarPesquisa();
         */
    }
    
    public SrSolicitacao escalonarCriandoFilha(DpPessoa cadastrante, DpLotacao lotacao, DpPessoa titular, DpLotacao lotaTitular,
    		SrItemConfiguracao itemConfiguracao, SrAcao acao, SrConfiguracao designacao, DpLotacao atendenteNaoDesignado,
    		Boolean fechadoAuto, String descricao, Map<Long, SrAtributoSolicitacaoMap> atributos) throws Exception{

    	if (itemConfiguracao == null || itemConfiguracao.getId() == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
    		throw new AplicacaoException("Operação não permitida. Necessário informar um item de configuração e uma ação.");

    	if (fechadoAuto != null) {
    		setFechadoAutomaticamente(fechadoAuto);
    		save();
    	}
    	SrSolicitacao filha = null;
    	if (isFilha())
    		filha = getSolicitacaoPai().criarFilhaSemSalvar();
    	else
    		filha = criarFilhaSemSalvar();
    	filha.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao.getId()));
    	filha.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
    	filha.setDesignacao(designacao);
    	filha.setDescrSolicitacao(descricao);
    	if (atendenteNaoDesignado != null && atendenteNaoDesignado.getIdeLotacao() != null)
    		filha.setAtendenteNaoDesignado(atendenteNaoDesignado);
    	filha.setAtributoSolicitacaoMap(atributos);
    	filha.definirAtributoSolicitacaoMapComoInicial();
    	filha.salvar(cadastrante, lotacao, titular, lotaTitular);
    	return filha;
    }
    
    public void escalonarPorMovimentacao(DpPessoa cadastrante, DpLotacao lotacao, DpPessoa titular, DpLotacao lotaTitular,
    		SrItemConfiguracao itemConfiguracao, SrAcao acao, SrConfiguracao designacao, DpLotacao atendenteNaoDesignado,
    		SrTipoMotivoEscalonamento motivo, String descricao, DpLotacao atendente, Map<Long, SrAtributoSolicitacaoMap> atributos) throws Exception{
    	
    	if (itemConfiguracao == null || itemConfiguracao.getId() == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new AplicacaoException("Operação não permitida. Necessário informar um item de configuração e uma ação.");
    	
    	if (designacao == null)
            throw new AplicacaoException("Não foi encontrado nenhum atendente designado. Sugestão: alterar item de " + "configuração e/ou ação");
    	
    	if (!this.isEmAndamento())
    		throw new AplicacaoException("Operação não permitida. A solicitação não está 'Em Andamento'");
    	SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO));
        mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao.getId()));
        mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
        mov.setLotaAtendente(atendenteNaoDesignado != null && atendenteNaoDesignado.getIdLotacao() != null ? atendenteNaoDesignado : atendente);
        // Edson: isso abaixo talvez pudesse valer pra todas as movimentacoes e ficar la no
        // mov.checarCampos()
        if (getAtendente() != null && !mov.getLotaAtendente().equivale(getLotaAtendente()))
            mov.setAtendente(null);
        mov.setMotivoEscalonamento(motivo);
        mov.setDesignacao(designacao);
        //por causa do detach no ObjetoObjectInstantiator:
      	if (mov.getLotaAtendente().getLotacaoInicial() != null)
      		mov.getLotaAtendente().setLotacaoInicial(DpLotacao.AR.findById(mov.getLotaAtendente().getLotacaoInicial().getId()));
        mov.setDescrMovimentacao(descricao);
        mov.salvar(cadastrante, lotacao, titular, lotaTitular);
        save();
        this.setAtributoSolicitacaoMap(atributos);
        this.salvarAtributosComHistorico(cadastrante, lotacao);
    }
        
    public void reclassificar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrSolicitacao dadosDoForm) throws Exception {
        if (!podeReclassificar(cadastrante, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        
        SrItemConfiguracao item = dadosDoForm.getItemConfiguracao();
        SrAcao acao = dadosDoForm.getAcao();
        
        if (item == null || item.getId() == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new AplicacaoException("Operação não permitida. Necessario informar um item de configuração e uma ação.");

        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_RECLASSIFICACAO));
        mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(item.getId()));
        mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
        
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        this.setAtributoSolicitacaoMap(dadosDoForm.getAtributoSolicitacaoMap());
        salvarAtributosComHistorico(cadastrante, lotaCadastrante);
    }


    public void enviarPesquisa() throws Exception {
    	CorreioHolder
    		.get()
    		.pesquisaSatisfacao(this);
    }

    public void responderPesquisa(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, Map<Long, String> respostaMap) throws Exception {
        if (!podeResponderPesquisa(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_AVALIACAO));
        movimentacao.setRespostaMap(respostaMap);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    private void removerDasListasDePrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        for (SrLista lista : this.getListasAssociadas()) {
            this.retirarDeLista(lista, cadastrante, lotaCadastrante, titular, lotaTitular);
        }
    }

    public void reabrir(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
    	if (!podeReabrir(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        
        boolean podeDeixarPaiPendente = false;
        DpLotacao lotacaoAtendente = getLotaAtendente();
        
        if (lotacaoAtendente.isFechada())
        	throw new AplicacaoException("Operação não permitida: A Lotação atendente (" + lotacaoAtendente.getSiglaCompleta() +
                ") foi extinta. Necessário abrir nova solicitação. Crie um vínculo dessa solicitação com a nova, através do recurso Vincular");
        
    	if (isFilha()) {
            if (getSolicitacaoPai().isAtivo()) 
            	podeDeixarPaiPendente = true;
        	else
        		throw new AplicacaoException("Operação não permitida: A solicitação principal " +
        			this.getSolicitacaoPai().getCodigo() + " não está ativa.");
    	}    		
        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA));
        mov.setLotaAtendente(lotacaoAtendente);
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);    	
        
        reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
        if (podeDeixarPaiPendente) 
        	getSolicitacaoPai().deixarPendenteAguardandoFilha(getCadastrante(), getLotaCadastrante(), 
        			getTitular(), getSolicitacaoPai().getLotaAtendente(), this);       
    }

    private void reInserirListasDePrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        
    	/*for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO
                    || mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO)
                break;

            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
                incluirEmLista(mov.getLista(), cadastrante, lotaCadastrante, titular, lotaTitular, getPrioridade(), false);

        }*/
    }

    public void deixarPendente(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrTipoMotivoPendencia motivo, String detalheMotivo, Date dtAgenda) throws Exception {
        if (!podeDeixarPendente(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setDtAgenda(dtAgenda);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA));
        movimentacao.setMotivoPendencia(motivo);
        movimentacao.setDescrMovimentacao(detalheMotivo);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void alterarPrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrPrioridade prioridade) throws Exception{
    	if (!podeAlterarPrioridade(titular, lotaTitular))
            throw new AplicacaoException("Operação não permitida");
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE));
        movimentacao.setPrioridade(prioridade);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        save();
    }

    public void terminarPendencia(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String descricao, Long idMovimentacao) throws Exception {
        if (!podeTerminarPendencia(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA));

        // Edson: eh necessario setar a finalizadora na finalizada antes de
        // salvar() a finalizadora, pq se nÃ£o, ao atualizarMarcas(), vai
        // parecer que a pendencia nao foi finalizada, atrapalhando calculos
        // de prazo
        SrMovimentacao movFinalizada = SrMovimentacao.AR.findById(idMovimentacao);
        movFinalizada.setMovFinalizadora(movimentacao);

        movimentacao.setDescrMovimentacao(descricao);
        movimentacao = movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        movFinalizada.save();
    }

    public void cancelar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (!podeCancelar(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        if (isPai() && !isAbertaComTodasFilhasFechadas())
            throw new AplicacaoException("Operação não permitida. Necessário fechar ou cancelar toda solicitação " + 
            			"filha criada a partir dessa que deseja cancelar.");
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO));
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
        if (isFilha()) {
        	DpLotacao lotaAtendente = getSolicitacaoPai().getLotaAtendente(); 
        	getSolicitacaoPai().terminarPendenciaAguardandoFilha(getCadastrante(), getLotaCadastrante(), getTitular(), lotaAtendente);
        	if (getSolicitacaoPai().podeFecharAutomatico())
        		getSolicitacaoPai().fechar(getCadastrante(), getLotaCadastrante(), getTitular(), lotaAtendente, 
        				"Solicitação fechada automaticamente", SrTipoMotivoFechamento.ATENDIMENTO_NEGADO);
        }   
    }

    public void juntar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrSolicitacao solRecebeJuntada, String justificativa) throws Exception {
        if ((cadastrante != null) && !podeJuntar(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        if (solRecebeJuntada.equivale(this))
            throw new AplicacaoException("N\u00E3o \u00E9 possivel juntar uma solicita\u00E7\u00E3o a si mesma.");
        // Edson: comentei porque, como o ObjetoObjectInstantiator executa o detach na solRecebeJuntada, dava lazy exception abaixo
        // if (solRecebeJuntada.isJuntada() && solRecebeJuntada.getSolicitacaoPrincipal().equivale(this))
        //    throw new AplicacaoException("N\u00E3o e possivel realizar juntada circular.");
        if (solRecebeJuntada.isFilha() && solRecebeJuntada.getSolicitacaoPai().equivale(this))
            throw new AplicacaoException("N\u00E3o e possivel juntar uma solicita\u00E7\u00E3o a uma das suas filhas. Favor realizar o processo inverso.");

        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_JUNTADA));
        movimentacao.setSolicitacaoReferencia(solRecebeJuntada);
        movimentacao.setDescrMovimentacao(justificativa);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void desentranhar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String justificativa) throws Exception {
        if (!podeDesentranhar(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO));
        movimentacao.setDescrMovimentacao(justificativa);
        movimentacao = movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

        SrMovimentacao juntada = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);
        juntada.setMovFinalizadora(movimentacao);
        juntada.save();

    }

    public void vincular(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrSolicitacao solRecebeVinculo, String justificativa) throws Exception {
        if ((cadastrante != null) && !podeVincular(titular, lotaTitular))
            throw new AplicacaoException(OPERACAO_NAO_PERMITIDA);
        if (solRecebeVinculo.equivale(this))
            throw new AplicacaoException("N\u00E3o e poss\u00EDvel vincular uma solicita\u00E7\u00E3o a si mesma.");
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_VINCULACAO));
        movimentacao.setSolicitacaoReferencia(solRecebeVinculo);
        movimentacao.setDescrMovimentacao(justificativa);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public String getGcTags() {
        String s = "tags=@servico";
        if (getAcaoAtual() != null)
            s += getAcaoAtual().getGcTags();
        if (getItemAtual() != null)
            s += getItemAtual().getGcTags();
        return s;
    }

    public String getGcTagAbertura() {
        String s = "";
		if (getItemAtual() != null)
			s += getItemAtual().getGcTagAncora();
        if (getAcaoAtual() != null)
            s += getAcaoAtual().getGcTagAncora();
        return s;
    }

    public String getGcTituloAbertura() {
        String s = "";
        if (getAcaoAtual() != null)
            s += getAcaoAtual().getTituloAcao();
        if (getItemConfiguracao() != null)
            s += " - " + getItemConfiguracao().getTituloItemConfiguracao();
        return s;
    }
    
    public String getDtOrigemDDMMYYYYHHMM() {
        return SrViewUtil.toDDMMYYYYHHMM(getDtOrigem());
    }

    public String getDtOrigemHHMM() {
        return SrViewUtil.toHHMM(getDtOrigem());
    }

    public String getDtOrigemDDMMYYYY() {
        return SrViewUtil.toDDMMYYYY(getDtOrigem());
    }

    public String getDtOrigemString() {
       return SrViewUtil.toStr(getDtOrigem());
    }

    public void setDtOrigemString(String stringDtMeioContato) {
        setDtOrigem(SrViewUtil.fromDDMMYYYYHHMM(stringDtMeioContato));
    }

    public String getDtIniEdicaoDDMMYYYYHHMMSS() {
        return SrViewUtil.toDDMMYYYYHHMMSS(getDtIniEdicao());
    }

    public void setDtIniEdicaoDDMMYYYYHHMMSS(String string) {
        setDtIniEdicao(SrViewUtil.fromDDMMYYYYHHMMSS(string));
    }

    public Date getDtInicioPrimeiraEdicao() {
        if (getSolicitacaoInicial() != null)
            return getSolicitacaoInicial().getDtIniEdicao();
        else
            return this.getDtIniEdicao();
    }

    public Date getDtInicioAtendimento() {
        for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
                return mov.getDtIniMov();
        return null;
    }

    public Date getDtEfetivoFechamento() {
        SrMovimentacao fechamento = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
        if (fechamento == null)
            return null;
        return fechamento.getDtIniMov();
    }

    public Date getDtCancelamento() {
        SrMovimentacao cancelamento = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO);
        if (cancelamento == null)
            return null;
        return cancelamento.getDtIniMov();
    }
    
    public SrEtapaSolicitacao getCadastro(){
    	SrEtapaSolicitacao c = new SrEtapaSolicitacao(SrParametro.CADASTRO);
    	c.setInicio(getDtInicioPrimeiraEdicao());
    	if (jaFoiDesignada())
			c.setFim(getDtInicioAtendimento());
		c.setLotaResponsavel(getLotaTitular());
		c.setParamsAcordo(getParametrosAcordoOrdenados(c.getParametro()));
    	c.setIntervalosCorrentes(getTrechosNaoPendentesPorEtapa(c));
    	c.setPrioridade(getPrioridade());
    	c.setItem(getItemConfiguracao());
    	c.setAcao(getAcao());
    	c.setSolicitacao(this);
    	return c;
    }
    
    public SrEtapaSolicitacao getAtendimentoGeral(){
    	SrEtapaSolicitacao g = new SrEtapaSolicitacao(SrParametro.ATENDIMENTO_GERAL);
    	g.setInicio(getDtInicioAtendimento());
    	if (isFechado())
			g.setFim(getDtEfetivoFechamento());
		else if (isCancelado())
			g.setFim(getDtCancelamento());
    	for (SrMovimentacao mov : getMovimentacaoSet())
            if (SrTipoMovimentacao.TIPOS_MOV_INI_ATENDIMENTO.contains(mov.getTipoMov().getId()) 
            		|| SrTipoMovimentacao.TIPOS_MOV_ATUALIZACAO_ATENDIMENTO.contains(mov.getTipoMov().getId()))
            	g.setParamsAcordo(mov.getParametrosAcordoOrdenados(g.getParametro()));
    	g.setIntervalosCorrentes(getAtendimentos(SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA));
    	g.setSolicitacao(this);
    	return g;
    }
    
    public SrEtapaSolicitacao getAtendimento(SrMovimentacao movIni, SrMovimentacao movMeio, SrMovimentacao movFim, SrTipoMotivoPendencia... pendenciasADesconsiderar){
    	if (movMeio == null)
    		movMeio = movIni;
    	SrEtapaSolicitacao a = new SrEtapaSolicitacao(SrParametro.ATENDIMENTO);
    	a.setInicio(movIni.getDtIniMov());
    	a.setFim(movFim != null ? movFim.getDtIniMov() : null);
    	a.setLotaResponsavel(movIni.getLotaAtendente());
    	a.setPessoaResponsavel(movFim != null ? movFim.getTitular() : null);
    	a.setParamsAcordo(movMeio.getParametrosAcordoOrdenados(a.getParametro()));
    	//DefinicaoHorario  h = getDefinicioarHorarioPorPessoalELota()
    	a.setIntervalosCorrentes(getTrechosNaoPendentesPorEtapa(a, pendenciasADesconsiderar));
    	a.setPrioridade(movMeio.getPrioridade());
    	a.setItem(movMeio.getItemConfiguracao());
    	a.setAcao(movMeio.getAcao());
    	a.setSolicitacao(this);
    	a.setTipoMov(movFim != null ? movFim.getTipoMov() : null);
    	a.setFaixa(movIni.getLotaAtendente().getOrgaoUsuario());
    	return a;
    }
  
    public Set<SrEtapaSolicitacao> getEtapas(){
    	return getEtapas(null, false);
    }
    
    public Set<SrEtapaSolicitacao> getEtapas(boolean todoOContexto){
    	return getEtapas(null, todoOContexto);
    }
  
    /*
     * Edson: as regras para gerar as etapas são as seguintes:
     * 
     * 1. Exibição do histórico de atendimentos (lota == null):
     * 1a) Cadastro: só na solicitação principal
     * 1b) Atendimento (Total): só na solicitação principal, só se houver acordo
     * 1c) Atendimento: sempre
     * 
     * 2. Exibição do cronômetro (lota != null)
     * 2a) Cadastro: só na solicitação principal, *se for a etapa ativa e o responsável pela etapa for a lota*
     * 2b) Atendimento (Total): só na solicitação principal, só se houver acordo
     * 2c) Atendimento: *só se for a etapa ativa e o responsável pela etapa for a lota*
     * 
     */
    @SuppressWarnings("unchecked")
	public Set<SrEtapaSolicitacao> getEtapas(DpLotacao lota, boolean todoOContexto){
    	Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
    	if (todoOContexto) {
    		solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
    	} else
    		solsAConsiderar.add(this);
    	Set<SrEtapaSolicitacao> etapasContexto = new TreeSet<SrEtapaSolicitacao>();
    	for (SrSolicitacao s : solsAConsiderar){
    		Set<SrEtapaSolicitacao> etapas = new TreeSet<SrEtapaSolicitacao>();
    		if (jaFoiDesignada()){
    			SrEtapaSolicitacao geral = s.getAtendimentoGeral();
    			List<SrEtapaSolicitacao> atendmtos = s.getAtendimentos();
    			if (lota == null)
    				etapas.addAll(atendmtos);
    			else {
    				for (SrEtapaSolicitacao a : atendmtos)
    					if (a.isInfinito() && a.getLotaResponsavel().equivale(lota)){
    						etapas.add(a);
    						break;
    					}
    			}
    			if (!s.isFilha() && geral.getParamAcordo() != null)
    				etapas.add(geral);
    		}
    		if (!s.isFilha()){
    			SrEtapaSolicitacao cadastro = s.getCadastro();
    			if (lota == null || (cadastro.isInfinito() && cadastro.getLotaResponsavel().equivale(lota)))
    				etapas.add(cadastro);
    		}
    		etapasContexto.addAll(etapas);
    	}
    	return etapasContexto;
    }
    
	public List<SrEtapaSolicitacao> getAtendimentos(SrTipoMotivoPendencia... pendenciasADesconsiderar) {
		List<SrEtapaSolicitacao> atendimentos = new ArrayList<SrEtapaSolicitacao>();
		SrMovimentacao movIni = null, movAtualizacao = null;
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			boolean isInicio = SrTipoMovimentacao.TIPOS_MOV_INI_ATENDIMENTO.contains(mov.getTipoMov().getId())
					&& (movIni == null || !mov.getLotaAtendente().equivale(movIni.getLotaAtendente()));
			boolean isFim = SrTipoMovimentacao.TIPOS_MOV_FIM_ATENDIMENTO.contains(mov.getTipoMov().getId());
			boolean isAtualizacao = SrTipoMovimentacao.TIPOS_MOV_ATUALIZACAO_ATENDIMENTO.contains(mov.getTipoMov().getId())
					&& !isInicio;
			if (!isInicio && !isFim && !isAtualizacao)
				continue;
			if (isAtualizacao)
				movAtualizacao = mov;
			if (movIni != null && (isInicio || isFim)) {
				atendimentos.add(getAtendimento(movIni, movAtualizacao, mov, pendenciasADesconsiderar));
				movIni = null;
				movAtualizacao = null;
			}
			if (isInicio)
				movIni = mov;
		}
		if (movIni != null)
			atendimentos.add(getAtendimento(movIni, movAtualizacao, null, pendenciasADesconsiderar));
		return atendimentos;
	}
	
	public SrEtapaSolicitacao getAtendimentoAtivo(){
		for (SrEtapaSolicitacao a : getAtendimentos()){
			if (a.isAtivo())
				return a;
		}
		return null;
	}
	
	public SrEtapaSolicitacao getUltimoAtendimento(){
		SrEtapaSolicitacao atual = null;
		for (SrEtapaSolicitacao a : getAtendimentos())
			atual = a;
		return atual;
	}

    public boolean isAcordosSatisfeitos() {
        for (SrEtapaSolicitacao etapa : getEtapas()) {
            if (etapa.isAcordoSatisfeito())
                return false;
        }
        return true;
    }
    
    public boolean isAcordoSatisfeito(SrAcordo acordo) {
    	for (SrEtapaSolicitacao etapa : getEtapas()) {
            if (etapa.isAcordoSatisfeito())
                return false;
        }
    	return true;
    }
    
    public Set<SrParametroAcordo> getParametrosAcordoOrdenados(){
    	Set<SrParametroAcordo> set = new TreeSet<SrParametroAcordo>(
			new SrParametroAcordoComparator());
    	if (getAcordos() != null)
    		for (SrAcordo a : getAcordos())
    			set.addAll(a.getParametroAcordoSet());
    	return set;
    }
    
    public List<SrParametroAcordo> getParametrosAcordoOrdenados(SrParametro p){
    	List<SrParametroAcordo> l = new ArrayList<SrParametroAcordo>();
    	for (SrParametroAcordo par : getParametrosAcordoOrdenados())
    		if (par.getParametro().equals(p))
    			l.add(par);
    	return l;
    }
    
    @Override
    public boolean equivale(Object other) {
        try {
            SrSolicitacao outra = (SrSolicitacao) other;
            return outra.getHisIdIni().equals(this.getHisIdIni());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * No caso de solicitacoes filhas, deve ser considerado o solicitante e o cadastrante para fins de exibicao de itens de configuracao e acoes disponiveis, alem do atendente designado da
     * solicitacao.
     */
    private List<SrConfiguracao> getFiltrosParaConsultarDesignacoes() {

        List<SrConfiguracao> pessoasAConsiderar = new ArrayList<SrConfiguracao>();

        if (getTitular() == null)
            return pessoasAConsiderar;

        SrConfiguracao confSolicitante = new SrConfiguracao();
        confSolicitante.setDpPessoa(getSolicitante());
        confSolicitante.setLotacao(getLotaSolicitante());
        confSolicitante.setOrgaoUsuario(getOrgaoUsuario());
        confSolicitante.setComplexo(getLocal());
        confSolicitante.setBuscarPorPerfis(true);
        pessoasAConsiderar.add(confSolicitante);

        if (jaFoiDesignada()) {
            SrConfiguracao confTitular = new SrConfiguracao();
            confTitular.setDpPessoa(getTitular());
            confTitular.setLotacao(getLotaTitular());
            confTitular.setComplexo(getLocal());
            confTitular.setOrgaoUsuario(getOrgaoUsuario());
            confTitular.setBuscarPorPerfis(true);
            pessoasAConsiderar.add(confTitular);
        }

        return pessoasAConsiderar;

    }

    public List<SrItemConfiguracao> getHistoricoItem() {
        List<SrItemConfiguracao> historicoItens = listarHistoricoItemInicial();
        SrItemConfiguracao anterior = getItemConfiguracao();

        for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescente()) {
            if (movimentacao.getItemConfiguracao() != null && anterior != null && !movimentacao.getItemConfiguracao().equivale(anterior)) {
                historicoItens.add(movimentacao.getItemConfiguracao());
                anterior = movimentacao.getItemConfiguracao();
            }
        }
        return historicoItens;
    }

    public List<SrAcao> getHistoricoAcao() {
        List<SrAcao> historicoAcoes = listaHistoricoAcaoInicial();
        SrAcao acaoAnterior = getAcao();
        for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescente()) {
            if (movimentacao.getAcao() != null && acaoAnterior != null && !movimentacao.getAcao().equivale(acaoAnterior)) {
                historicoAcoes.add(movimentacao.getAcao());
                acaoAnterior = movimentacao.getAcao();
            }
        }
        return historicoAcoes;
    }

    private List<SrAcao> listaHistoricoAcaoInicial() {
        List<SrAcao> acoes = new ArrayList<SrAcao>();
        if (getAcao() != null) {
            acoes.add(getAcao());
        }
        return acoes;
    }

    private List<SrItemConfiguracao> listarHistoricoItemInicial() {
        List<SrItemConfiguracao> itensConfiguracao = new ArrayList<SrItemConfiguracao>();
        if (getItemConfiguracao() != null) {
            itensConfiguracao.add(getItemConfiguracao());
        }
        return itensConfiguracao;
    }

    private void deixarPendenteAguardandoFilha(DpPessoa cadastrante, DpLotacao lotaCadastrante, 
    		DpPessoa titular, DpLotacao lotaTitular, SrSolicitacao filha) throws Exception {
        if (!temOutrasFilhasAbertas(filha)){ 
        	deixarPendente(cadastrante, lotaCadastrante, titular, lotaTitular,
                    SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA, "Movimentação realizada automaticamente", null);
        }
    }
    
    private void terminarPendenciaAguardandoFilha(DpPessoa cadastrante, DpLotacao lotaCadastrante, 
    		DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (isAbertaComTodasFilhasFechadas()) 
        	terminarPendenciaPorMotivo(cadastrante, lotaCadastrante, titular, 
        			lotaTitular, SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA);
    }
    
    private void terminarPendenciaPorMotivo(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, 
    		DpLotacao lotaTitular, SrTipoMotivoPendencia motivoDaPendencia) throws Exception {
        for (SrPendencia iniP : getPendenciasEmAberto()) {
            if (iniP.getMotivo().equals(motivoDaPendencia))
                    terminarPendencia(cadastrante, lotaCadastrante, titular, lotaTitular, "", iniP.getId());
        }
    }
	
	public SrArquivo getArquivoAnexoNaCriacao() {
		if (getSolicitacaoInicial() != null)
			for (SrSolicitacao sol : getHistoricoSolicitacao())
				if (sol.getArquivo() != null)
					return sol.getArquivo();
		return null;
	}
	
	private Set<SrSolicitacao> getSolicitacoesDependendoDoContexto(boolean todoOContexto) {
        Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
        if (todoOContexto) 
            solsAConsiderar.addAll(getPaiDaArvore().getSolicitacaoFilhaSetRecursivo());
        else
            solsAConsiderar.add(this);
       return solsAConsiderar;
	}
    
	public SrItemConfiguracao getItemAtual() {
		SrMovimentacao ultMov = getUltimaMovimentacao();
		return ultMov != null ? ultMov.getItemConfiguracao() : getItemConfiguracao();
	}

	public SrAcao getAcaoAtual() {
		SrMovimentacao ultMov = getUltimaMovimentacao();
		return ultMov != null ? ultMov.getAcao() : getAcao();
	}
	
	public String getDescrItemAtual(){
		return getItemAtual() != null ? getItemAtual().getTituloItemConfiguracao() : "Item Não Informado";
	}
	
	public String getDescrAcaoAtual(){
		return getAcaoAtual() != null ? getAcaoAtual().getDescricao() : "Ação Não Informada";
	}
	
	public String getDescrItemAtualCompleta(){
		return getItemAtual() != null ? getItemAtual().getDescricaoCompleta() : "Item Não Informado";
	}
	
	public String getDescrAcaoAtualCompleta(){
		return getAcaoAtual() != null ? getAcaoAtual().getDescricaoCompleta() : "Ação Não Informada";
	}
	
    public Long getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(Long idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public DpPessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(DpPessoa solicitante) {
        this.solicitante = solicitante;
    }

    public DpPessoa getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(DpPessoa interlocutor) {
        this.interlocutor = interlocutor;
    }

    public DpLotacao getLotaSolicitante() {
        return lotaSolicitante;
    }

    public void setLotaSolicitante(DpLotacao lotaSolicitante) {
        this.lotaSolicitante = lotaSolicitante;
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

    public SrSolicitacao setLotaTitular(DpLotacao lotaTitular) {
        this.lotaTitular = lotaTitular;
        return this;
    }

    public SrConfiguracao getDesignacao() {
        return designacao;
    }

    public void setDesignacao(SrConfiguracao designacao) {
        this.designacao = designacao;
    }

    public DpLotacao getAtendenteNaoDesignado() {
        return atendenteNaoDesignado;
    }

    public void setAtendenteNaoDesignado(DpLotacao atendenteNaoDesignado) {
        this.atendenteNaoDesignado = atendenteNaoDesignado;
    }

    public CpOrgaoUsuario getOrgaoUsuario() {
        return orgaoUsuario;
    }

    public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
        this.orgaoUsuario = orgaoUsuario;
    }

    public SrSolicitacao getSolicitacaoPai() {
        return solicitacaoPai;
    }

    public void setSolicitacaoPai(SrSolicitacao solicitacaoPai) {
        this.solicitacaoPai = solicitacaoPai;
    }

    public List<SrAcordo> getAcordos() {
        return acordos;
    }

    public void setAcordos(List<SrAcordo> acordos) {
        this.acordos = acordos;
    }

    public SrFormaAcompanhamento getFormaAcompanhamento() {
        return formaAcompanhamento;
    }

    public void setFormaAcompanhamento(SrFormaAcompanhamento formaAcompanhamento) {
        this.formaAcompanhamento = formaAcompanhamento;
    }

    public SrMeioComunicacao getMeioComunicacao() {
        return meioComunicacao;
    }

    public void setMeioComunicacao(SrMeioComunicacao meioComunicacao) {
        this.meioComunicacao = meioComunicacao;
    }

    public SrItemConfiguracao getItemConfiguracao() {
        return itemConfiguracao;
    }

    public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
        this.itemConfiguracao = itemConfiguracao;
    }

    public SrArquivo getArquivo() {
        return arquivo;
    }

    public SrAcao getAcao() {
        return acao;
    }

    public void setAcao(SrAcao acao) {
        this.acao = acao;
    }

    public String getDescrSolicitacao() {
        return descrSolicitacao;
    }

    public void setDescrSolicitacao(String descrSolicitacao) {
        this.descrSolicitacao = descrSolicitacao;
    }

    public SrGravidade getGravidade() {
        return gravidade;
    }

    public void setGravidade(SrGravidade gravidade) {
        this.gravidade = gravidade;
    }

    public Date getDtReg() {
        return dtReg;
    }

    public void setDtReg(Date dtReg) {
        this.dtReg = dtReg;
    }

    public Date getDtIniEdicao() {
        return dtIniEdicao;
    }

    public void setDtIniEdicao(Date dtIniEdicao) {
        this.dtIniEdicao = dtIniEdicao;
    }

    public Date getDtOrigem() {
        return dtOrigem;
    }

    public void setDtOrigem(Date dtOrigem) {
        this.dtOrigem = dtOrigem;
    }

    public CpComplexo getLocal() {
        return local;
    }

    public void setLocal(CpComplexo local) {
        this.local = local;
    }

    public String getTelPrincipal() {
        return telPrincipal;
    }

    public void setTelPrincipal(String telPrincipal) {
        this.telPrincipal = telPrincipal;
    }
    
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isFecharAoAbrir() {
        return fecharAoAbrir;
    }
    
    public boolean isDescrSolicitacaoPreenchida() {
    	return (getDescrSolicitacao() != null && getDescrSolicitacao().trim().length() > 0);
    }
    
    private boolean isAtributoDaEntidadeCarregado(String nomeAtributo) {
    	return em().getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(this, nomeAtributo);
    }

    public void setFecharAoAbrir(boolean fecharAoAbrir) {
        this.fecharAoAbrir = fecharAoAbrir;
    }

    public String getMotivoFechamentoAbertura() {
        return motivoFechamentoAbertura;
    }

    public void setMotivoFechamentoAbertura(String motivoFechamentoAbertura) {
        this.motivoFechamentoAbertura = motivoFechamentoAbertura;
    }

    public Long getNumSolicitacao() {
        return numSolicitacao;
    }

    public void setNumSolicitacao(Long numSolicitacao) {
        this.numSolicitacao = numSolicitacao;
    }

	public Long getNumSequencia() {
		return numSequencia;
    }

    public void setNumSequencia(Long numSequencia) {
        this.numSequencia = numSequencia;
    }

    public Long getTempoDecorridoCadastro() {
		return dnmTempoDecorridoCadastro;
	}

	public void setTempoDecorridoCadastro(Long tempoDecorridoCadastro) {
		this.dnmTempoDecorridoCadastro = tempoDecorridoCadastro;
	}

	public SrSolicitacao getSolicitacaoInicial() {
        return solicitacaoInicial;
    }

    public void setSolicitacaoInicial(SrSolicitacao solicitacaoInicial) {
        this.solicitacaoInicial = solicitacaoInicial;
    }

    public List<SrSolicitacao> getMeuSolicitacaoHistoricoSet() {
        return meuSolicitacaoHistoricoSet;
    }

    public void setMeuSolicitacaoHistoricoSet(List<SrSolicitacao> meuSolicitacaoHistoricoSet) {
        this.meuSolicitacaoHistoricoSet = meuSolicitacaoHistoricoSet;
    }

    public Set<SrPrioridadeSolicitacao> getMeuPrioridadeSolicitacaoSet() {
		return meuPrioridadeSolicitacaoSet;
	}

	public void setMeuPrioridadeSolicitacaoSet(
			Set<SrPrioridadeSolicitacao> meuPrioridadeSolicitacaoSet) {
		this.meuPrioridadeSolicitacaoSet = meuPrioridadeSolicitacaoSet;
	}

	public Set<SrMovimentacao> getMeuMovimentacaoSet() {
        return meuMovimentacaoSet;
    }

    public void setMeuMovimentacaoSet(Set<SrMovimentacao> meuMovimentacaoSet) {
        this.meuMovimentacaoSet = meuMovimentacaoSet;
    }

    public Set<SrSolicitacao> getMeuSolicitacaoFilhaSet() {
        return meuSolicitacaoFilhaSet;
    }

    public void setMeuSolicitacaoFilhaSet(Set<SrSolicitacao> meuSolicitacaoFilhaSet) {
        this.meuSolicitacaoFilhaSet = meuSolicitacaoFilhaSet;
    }

    public Set<SrMovimentacao> getMeuMovimentacaoReferenciaSet() {
        return meuMovimentacaoReferenciaSet;
    }

    public void setMeuMovimentacaoReferenciaSet(Set<SrMovimentacao> meuMovimentacaoReferenciaSet) {
        this.meuMovimentacaoReferenciaSet = meuMovimentacaoReferenciaSet;
    }

    public Set<SrMarca> getMeuMarcaSet() {
        return meuMarcaSet;
    }

    public void setMeuMarcaSet(Set<SrMarca> meuMarcaSet) {
        this.meuMarcaSet = meuMarcaSet;
    }
    
    public List<SrAtributoSolicitacao> getMeuAtributoSolicitacaoSet() {
    	return meuAtributoSolicitacaoSet;
    }

    public Boolean getRascunho() {
        return rascunho;
    }

    public void setRascunho(Boolean rascunho) {
        this.rascunho = rascunho;
    }

	public Destinatario getDestinatarioEmailNotificacao() {
		return new Destinatario(getSolicitacaoAtual().getSolicitante().getPessoaAtual());
	}

	public void refresh() {
	    ContextoPersistencia.em().refresh(this);
	}
			
	@Override
	public String toString() {
		return getCodigo();
	}
}
