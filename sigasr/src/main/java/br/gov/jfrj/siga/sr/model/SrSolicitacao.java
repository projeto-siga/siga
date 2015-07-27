package br.gov.jfrj.siga.sr.model;

import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.vo.ListaInclusaoAutomatica;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;
import br.gov.jfrj.siga.sr.notifiers.Destinatario;
import br.gov.jfrj.siga.sr.util.Cronometro;
import br.gov.jfrj.siga.sr.util.Util;
import br.gov.jfrj.siga.uteis.SigaPlayCalendar;

@Entity
@Table(name = "SR_SOLICITACAO", schema = "SIGASR")
public class SrSolicitacao extends HistoricoSuporte implements SrSelecionavel {
    private static final String MODAL_TRUE = "modal=true";
    private static final String HH_MM = "HH:mm";
    private static final String DD_MM_YYYY = "dd/MM/yyyy";
    private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    private static final String OPERACAO_NAO_PERMITIDA = "Opera\u00E7\u00E3o n\u00E3o permitida";

    private static final long serialVersionUID = 1L;

    public static final ActiveRecord<SrSolicitacao> AR = new ActiveRecord<>(SrSolicitacao.class);

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

    @Transient
    private Cronometro cron;

    @ManyToOne
    @JoinColumn(name = "ID_ORGAO_USU")
    private CpOrgaoUsuario orgaoUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_SOLICITACAO_PAI")
    private SrSolicitacao solicitacaoPai;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SR_SOLICITACAO_ACORDO", schema = "SIGASR", joinColumns = { @JoinColumn(name = "ID_SOLICITACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ACORDO") })
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
    private SrTendencia tendencia;

    @Enumerated
    private SrUrgencia urgencia;

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

    @OneToMany(targetEntity = SrAtributoSolicitacao.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    protected List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet;

    @OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
    // @OrderBy("dtIniMov DESC")
    private Set<SrMovimentacao> meuMovimentacaoSet;

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

    public SrSolicitacao() {

    }

    public class SrTarefa {
        private SrAcao acao;
        private SrConfiguracao conf;

        public SrTarefa() {

        }

        public SrAcao getAcao() {
            return acao;
        }

        public void setAcao(SrAcao acao) {
            this.acao = acao;
        }

        public SrConfiguracao getConf() {
            return conf;
        }

        public void setConf(SrConfiguracao conf) {
            this.conf = conf;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((acao == null) ? 0 : acao.hashCode());
            result = prime * result + ((conf == null || conf.getAtendente() == null) ? 0 : conf.getAtendente().hashCode());
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
            } else if (conf.getAtendente() != null && !conf.getAtendente().equals(other.conf.getAtendente()))
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

    @Override
    public void setSigla(String sigla) {
        String siglaUpper = sigla.trim().toUpperCase();
        Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
        for (Object ou : CpOrgaoUsuario.AR.all().fetch()) {
            CpOrgaoUsuario cpOu = (CpOrgaoUsuario) ou;
            mapAcronimo.put(cpOu.getAcronimoOrgaoUsu(), cpOu);
        }
        StringBuilder acronimos = new StringBuilder();
        for (String s : mapAcronimo.keySet()) {
            acronimos.append("|" + s);
        }
        final Pattern p = Pattern.compile("^([A-Za-z0-9]{2}" + acronimos.toString() + ")?-?SR{1}-?(?:([0-9]{4})/?)??([0-9]{1,5})(?:[.]{1}([0-9]{1,3}))?$");
        final Matcher m = p.matcher(siglaUpper);

        if (m.find()) {

            if (m.group(1) != null) {
                try {
                    CpOrgaoUsuario orgao = new CpOrgaoUsuario();
                    orgao.setSiglaOrgaoUsu(m.group(1));
                    orgao = (CpOrgaoUsuario) AR.em().createQuery("from CpOrgaoUsuario where acronimoOrgaoUsu = '" + m.group(1) + "'").getSingleResult();
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

    @Override
    public String getDescricao() {
        if (getDescrSolicitacao() == null || getDescrSolicitacao().length() == 0) {
            if (isFilha())
                return getSolicitacaoPai().getDescricao();
            else
                return "Descri\u00E7\u00E3o n\u00E3o informada";
        } else
            return getDescrSolicitacao();
    }

    public List<SrAtributoSolicitacao> getMeuAtributoSolicitacaoSet() {
        if ((meuAtributoSolicitacaoSet == null || meuAtributoSolicitacaoSet.isEmpty()) && isFilha()) {
            return getSolicitacaoPai().getMeuAtributoSolicitacaoSet();
        }
        return meuAtributoSolicitacaoSet;
    }

    public void setMeuAtributoSolicitacaoSet(List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet) {
        this.meuAtributoSolicitacaoSet = meuAtributoSolicitacaoSet;
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
        if (getOrgaoUsuario() == null && getLotaTitular() != null)
            setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());

        String query = "from SrSolicitacao where hisDtFim is null ";

        if (getOrgaoUsuario() != null) {
            query += " and orgaoUsuario.idOrgaoUsu = " + getOrgaoUsuario().getIdOrgaoUsu();
        }
        if (getDtReg() != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(getDtReg());
            int year = c1.get(Calendar.YEAR);
            query += " and dtReg between to_date('01/01/" + year + " 00:01', 'dd/mm/yyyy HH24:mi') and to_date('31/12/" + year + " 23:59','dd/mm/yyyy HH24:mi')";
        }
        if (getNumSolicitacao() != null)
            query += " and numSolicitacao = " + getNumSolicitacao();
        if (getNumSequencia() == null)
            query += " and numSequencia is null";
        else
            query += " and numSequencia = " + getNumSequencia();

        return (SrSolicitacao) AR.em().createQuery(query).getSingleResult();
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

    public List<SrAtributoSolicitacao> getAtributoSolicitacaoSet() {
        if (meuAtributoSolicitacaoSet == null || meuAtributoSolicitacaoSet.isEmpty()) {
            if (isFilha())
                return getSolicitacaoPai().getAtributoSolicitacaoSet();
            else
                return new ArrayList<SrAtributoSolicitacao>();
        } else
            return meuAtributoSolicitacaoSet;
    }

    public String getDtRegString() {
        SigaPlayCalendar cal = new SigaPlayCalendar();
        cal.setTime(getDtReg());
        return "<span style=\"display: none\">" + new SimpleDateFormat("yyyyMMdd").format(dtReg)
                + "</span>" + cal.getTempoTranscorridoString(false);
    }
    
    public SrPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(SrPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getPrioridadeString(){
        return prioridade == null ? "" : "<span style=\"display: none\">" + prioridade.getIdPrioridade()
                        + "</span>" + prioridade.getDescPrioridade();
    }

    public SrPrioridade getPrioridadeTecnica(){
        SrMovimentacao ultimaPriorizacao = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE);
        return ultimaPriorizacao != null ? ultimaPriorizacao.getPrioridade() : this.getPrioridade();
    }

    public String getPrioridadeTecnicaString(){
        return getPrioridadeTecnica() == null ? "" : "<span style=\"display: none\">" + getPrioridadeTecnica().getIdPrioridade()
                        + "</span>" + getPrioridadeTecnica().getDescPrioridade();
    }

    public String getAtributosString() {
        StringBuilder s = new StringBuilder();
        for (SrAtributoSolicitacao att : getAtributoSolicitacaoSet()) {
            if (att.getValorAtributoSolicitacao() != null)
                s.append(att.getAtributo().getNomeAtributo() + ": " + att.getValorAtributoSolicitacao() + ". ");
        }
        return s.toString();
    }

    // Edson: NecessÃ¡rio porque nao hÃ¯Â¿Â½ binder para arquivo
    public void setArquivo(UploadedFile file) {
    	this.arquivo = SrArquivo.newInstance(file);
    }

    public int getGUT() {
        return getGravidade().getNivelGravidade() * getUrgencia().getNivelUrgencia() * getTendencia().getNivelTendencia();
    }

    public String getGUTString() {
        return getGravidade().getDescrGravidade() + " " + getUrgencia().getDescrUrgencia() + " " + getTendencia().getDescrTendencia();
    }

    public String getGUTPercent() {
        return (int) ((getGUT() / 125.0) * 100) + "%";
    }

    public void associarPrioridadePeloGUT() {
        /*int valorGUT = getGUT();
        if (Util.isbetween(1, 24, valorGUT))
            setPrioridade(SrPrioridade.PLANEJADO);
        else if (Util.isbetween(25, 49, valorGUT))
            setPrioridade(SrPrioridade.BAIXO);
        else if (Util.isbetween(50, 74, valorGUT))
            setPrioridade(SrPrioridade.MEDIO);
        else if (Util.isbetween(75, 99, valorGUT))
            setPrioridade(SrPrioridade.ALTO);
        else if (Util.isbetween(100, 125, valorGUT))
            setPrioridade(SrPrioridade.IMEDIATO);*/
    	if (gravidade == SrGravidade.SEM_GRAVIDADE)
            prioridade = SrPrioridade.BAIXO;
    	else if (gravidade == SrGravidade.NORMAL)
            prioridade = SrPrioridade.MEDIO;
    	else if (gravidade == SrGravidade.GRAVE)
            prioridade = SrPrioridade.ALTO;
    	else if (gravidade == SrGravidade.MUITO_GRAVE)
    		prioridade = SrPrioridade.IMEDIATO;
    	
    }

    public String getDtRegDDMMYYYYHHMM() {
        if (getDtReg() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
            return df.format(getDtReg());
        }
        return "";
    }

    public String getDtRegDDMMYYYY() {
        if (getDtReg() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.format(getDtReg());
        }
        return "";
    }

    public String getDtRegHHMM() {
        if (getDtReg() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(HH_MM);
            return df.format(getDtReg());
        }
        return "";
    }

    public Long getProximoNumero() {
        if (getOrgaoUsuario() == null)
            return 0L;
        Long num = AR.find("select max(numSolicitacao)+1 from SrSolicitacao where orgaoUsuario.idOrgaoUsu = " + getOrgaoUsuario().getIdOrgaoUsu()).first();
        return (num != null) ? num : 1;
    }

    public String getAnoEmissao() {
        if (getDtReg() == null)
            return null;
        return new SimpleDateFormat("yyyy").format(getDtReg());
    }

    public String getNumSequenciaString() {
        if (getNumSequencia() == null)
            return null;
        return "." + (getNumSequencia() < 10 ? "0" : "") + getNumSequencia().toString();
    }

    public List<SrSolicitacao> getHistoricoSolicitacao() {
        if (getSolicitacaoInicial() != null)
            return getSolicitacaoInicial().getMeuSolicitacaoHistoricoSet();
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
            if (mov.getDescrMovimentacao() != null && mov.getDescrMovimentacao().length() > 0)
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
            if (mov.getNumSequencia() > 1 && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_INCLUSAO_LISTA
                    && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA && mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_AVALIACAO)
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

    public Set<SrMovimentacao> getMovimentacaoSetComCanceladosTodoOContexto() {
        return getMovimentacaoSet(true, null, false, true, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSetOrdemCrescente() {
        return getMovimentacaoSet(false, null, true, false, false, false);
    }

    public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCanceladas, Long tipoMov, boolean ordemCrescente, boolean todoOContexto, boolean apenasPrincipais, boolean inversoJPA) {

        List<Long> tiposPrincipais = Arrays.asList(TIPO_MOVIMENTACAO_ANDAMENTO, TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO, TIPO_MOVIMENTACAO_FECHAMENTO, TIPO_MOVIMENTACAO_AVALIACAO,
                TIPO_MOVIMENTACAO_ESCALONAMENTO);

        return getMovimentacaoSet(considerarCanceladas, tipoMov, ordemCrescente, todoOContexto, apenasPrincipais, inversoJPA, tiposPrincipais);
    }

    public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCanceladas, Long tipoMov, boolean ordemCrescente, boolean todoOContexto, boolean apenasPrincipais, boolean inversoJPA,
            List<Long> tiposPrincipais) {

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
                            if (apenasPrincipais && !tiposPrincipais.contains(movimentacao.getTipoMov().getIdTipoMov()))
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
        return getUltimaMovimentacao().getAtendente();
    }

    public boolean isFilha() {
        return this.getSolicitacaoPai() != null;
    }

    public Set<SrMovimentacao> getPendenciasEmAberto() {
        Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
        Set<SrMovimentacao> setPendentes = new HashSet<SrMovimentacao>();

        for (SrMovimentacao ini : setIni) {
            if ((!ini.isFinalizada()) && (ini.getDtAgenda() == null || ini.getDtAgenda().after(new Date())))
                setPendentes.add(ini);
        }

        return setPendentes;
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
            SrConfiguracao conf = SrConfiguracao.buscarAssociacao(confFiltro);
            if (conf != null) {
                listaFinal.add(t);
                if (map != null)
                    map.put(t.getIdAtributo(), conf.isAtributoObrigatorio());
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

    // Edson: poderia tambÃÂ©m guardar num HashMap transiente e, ao salvar(),
    // mandar criar os atributos, caso se quisesse permitir um
    // solicitacao.getAtributoSet().put...
    public void setAtributoSolicitacaoMap(List<SrAtributoSolicitacaoMap> atributoSolicitacaoMap) throws Exception {

        if (atributoSolicitacaoMap != null) {
            meuAtributoSolicitacaoSet = new ArrayList<SrAtributoSolicitacao>();

            for (SrAtributoSolicitacaoMap atribSolicitacao : atributoSolicitacaoMap) {
            	SrAtributo att = SrAtributo.AR.findById(atribSolicitacao.getIdAtributo());
	            SrAtributoSolicitacao attSolicitacao = new SrAtributoSolicitacao(att, atribSolicitacao.getValorAtributo(), this);
	            meuAtributoSolicitacaoSet.add(attSolicitacao);
	        }
        }

//        if (atributosSolicitacao != null) {
//            meuAtributoSolicitacaoSet = new ArrayList<SrAtributoSolicitacao>();
//            for (Long idAtt : atributosSolicitacao.keySet()) {
//                SrAtributo att = SrAtributo.AR.findById(idAtt);
//                SrAtributoSolicitacao attSolicitacao = new SrAtributoSolicitacao(att, atributosSolicitacao.get(idAtt), this);
//                meuAtributoSolicitacaoSet.add(attSolicitacao);
//            }
//        }
    }

    public List<SrAtributoSolicitacaoMap> getAtributoSolicitacaoMap() {
    	List<SrAtributoSolicitacaoMap> list = new ArrayList<>();
    	if(meuAtributoSolicitacaoSet != null){
    		for (SrAtributoSolicitacao att : meuAtributoSolicitacaoSet) {
    			if(att.getAtributo() != null)
    				list.add(new SrAtributoSolicitacaoMap(att.getAtributo().getIdAtributo(), att.getValorAtributoSolicitacao()));
    		}
    	}
    	Collections.sort(list, new Comparator<SrAtributoSolicitacaoMap>() {
			@Override
			public int compare(SrAtributoSolicitacaoMap o1, SrAtributoSolicitacaoMap o2) {
				return o1.getIdAtributo().compareTo(o2.getIdAtributo());
			}
		});
    	return list;
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
                if (sol.getMeuSolicitacaoFilhaSet() != null)
                    for (SrSolicitacao filha : sol.getMeuSolicitacaoFilhaSet())
                        if (filha.getHisDtFim() == null)
                            listaCompleta.add(filha);
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
        Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
        for (SrMovimentacao ini : setIni) {
            if ((!ini.isFinalizada()) && (ini.getDtAgenda() == null || ini.getDtAgenda().after(new Date())))

                return true;
        }
        return false;
    }
    
    public boolean possuiPendencia(SrTipoMotivoPendencia tipo){
        for (SrMovimentacao p : getPendenciasEmAberto()){
                if (p.getMotivoPendencia().equals(tipo))
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

    public boolean isEscalonada() {
        return sofreuMov(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO, SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
    }

    public boolean isAFechar() {
        Set<SrSolicitacao> filhas = getSolicitacaoFilhaSet();
        if (filhas.isEmpty())
            return false;
        for (SrSolicitacao filha : filhas) {
            if (!filha.isFechado() && !filha.isCancelado())
                return false;
        }
        return true;
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

    public SrSolicitacao getSolicitacaoPrincipal() {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO)
                return null;
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
                return mov.getSolicitacaoReferencia();
        }
        return null;
    }

    public boolean estaCom(DpPessoa pess, DpLotacao lota) {
    	if (isFilha() && solicitacaoPai.estaCom(pess, lota))
			return true;
		if (isRascunho())
			return foiCadastradaPor(pess, lota) || foiSolicitadaPor(pess, lota);
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

    public boolean temArquivosAnexos() {
        return getArquivoAnexoNaCriacao() != null || !getMovimentacoesAnexacao().isEmpty();
    }

    public SrArquivo getArquivoAnexoNaCriacao() {
        if (getSolicitacaoInicial() != null)
            for (SrSolicitacao sol : getHistoricoSolicitacao())
                if (sol.getArquivo() != null)
                    return sol.getArquivo();
        return null;
    }

    public Set<SrMovimentacao> getMovimentacoesAnexacao() {
        return getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO);
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
        return !isRascunho() && !isFechado() && estaCom(pess, lota);
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
    	return isAtivo() && estaCom(pess, lota);
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

    public boolean podeFecharPaiAutomatico() {
        return isFilha() && getSolicitacaoPai().getSolicitacaoAtual().isFechadoAutomaticamente() && getSolicitacaoPai().isAFechar();
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
        } else {
            setTelPrincipal("");
            setLocal(null);
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

    public List<SrItemConfiguracao> getItensDisponiveis() throws Exception {
        if (getSolicitante() == null)
            return null;

        List<SrItemConfiguracao> listaTodosItens = new ArrayList<SrItemConfiguracao>();
        List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();

        List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
        listaTodosItens = SrItemConfiguracao.listar(false);

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

        if (getSolicitante() == null || getItemConfiguracao() == null)
            return null;

        List<SrTarefa> listaFinal = new ArrayList<SrTarefa>();
        Set<SrTarefa> setTerafa = new HashSet<SrTarefa>();
        List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
        SrTarefa tarefa = null;

        for (SrAcao a : SrAcao.listar(false)) {
            if (!a.isEspecifico())
                continue;
            for (SrConfiguracao c : listaPessoasAConsiderar) {
                c.setItemConfiguracaoFiltro(getItemConfiguracao());
                c.setAcaoFiltro(a);
                c.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class, CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));

                List<SrConfiguracao> confs = SrConfiguracao.listar(c, new int[] { SrConfiguracaoBL.ATENDENTE });
                if (!confs.isEmpty())
                    for (SrConfiguracao conf : confs) {
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
        if (acoesEAtendentes != null && this.getItemConfiguracao() != null) {

        	// DB1: Adicionado verificaÃÂ§ÃÂ£o pois ao editar uma solicitaÃÂ§ÃÂ£o, estava resetando a aÃÂ§ÃÂ£o
        	// jÃÂ¡ selecionada pelo usuÃÂ¡rio mesmo que a ediÃÂ§ÃÂ£o fosse cancelada.
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
				// Edson: serÃ¯Â¿Â½ que essas coisas poderiam estar dentro do
				// SrOperacao?
				if (!e.isModal())
					e.setUrl(e.getUrl() + "?id="+idSolicitacao + e.getParamsFormatted());
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

        checarEPreencherCampos();
        // Edson: Ver por que isto estÃ¯Â¿Â½ sendo necessÃ¯Â¿Â½rio. Sem isso, apÃ¯Â¿Â½s o salvar(),
        // ocorre LazyIniException ao tentar acessar esses meuMovimentacaoSet's
        if (getSolicitacaoInicial() != null)
            for (SrSolicitacao s : getSolicitacaoInicial().getMeuSolicitacaoHistoricoSet()) {
                for (SrMovimentacao m : s.getMeuMovimentacaoSet()) {
                }
            }

        super.salvarComHistorico();

        // Edson: melhorar isto, pra nao precisar salvar novamente

        if (isRascunho()) {
            atualizarCodigo();
            save();
        }

        if (!isRascunho() && !jaFoiDesignada()) {

            if (isFecharAoAbrir())
                fechar(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular(), getMotivoFechamentoAbertura());
            else
                iniciarAtendimento(getCadastrante(), getLotaCadastrante(), getTitular(), getLotaTitular());

            incluirEmListasAutomaticas();

             if (!isEditado() && getFormaAcompanhamento() != SrFormaAcompanhamento.ABERTURA_FECHAMENTO)
            	 CorreioHolder
            	 	.get()
            	 	.notificarAbertura(this);
        } else
            atualizarMarcas();
        
      //Edson: melhorar este codigo, tirando o loop daqui
        if (isFilha()){
                boolean temOutrasFilhasAbertas = false;
                for (SrSolicitacao filha : solicitacaoPai.getSolicitacaoFilhaSet()){
                        if (!filha.equivale(this) && filha.isAtivo())
                                temOutrasFilhasAbertas = true;
                }
                if (!temOutrasFilhasAbertas)
                        solicitacaoPai.deixarPendente(cadastrante, lotaCadastrante,
                                        titular, lotaTitular,
                                        SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA, null, null, "");

        }
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

        List<SrConfiguracao> filtrosConf = new ArrayList<SrConfiguracao>();

        SrConfiguracao confSolicitante = new SrConfiguracao();
        confSolicitante.setDpPessoa(getSolicitante());
        confSolicitante.setLotacao(getLotaSolicitante());
        confSolicitante.setComplexo(getLocal());
        confSolicitante.setBuscarPorPerfis(true);
        filtrosConf.add(confSolicitante);

        if (getTitular() != null && getTitular().getId() != null) {
            SrConfiguracao confTitular = new SrConfiguracao();
            confTitular.setDpPessoa(getTitular());
            confTitular.setLotacao(getLotaTitular());
            confTitular.setComplexo(getLocal());
            confTitular.setBuscarPorPerfis(true);
            filtrosConf.add(confTitular);
        }

        for (SrConfiguracao c : filtrosConf) {

            c.setItemConfiguracaoFiltro(getItemConfiguracao());
            c.setAcaoFiltro(getAcao());
            c.setPrioridade(getPrioridade());
            if (getDesignacao() != null && getDesignacao().getId() != null)
                c.setAtendente(getDesignacao().getAtendente());
            c.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class, CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));

            List<SrConfiguracao> confs = SrConfiguracao.listar(c);
            for (SrConfiguracao conf : confs) {
                if (conf.getAcordo() != null && conf.getAcordo().getId() != null) {
                    adicionarAcordoAtual(conf);
                }
            }
        }
    }

    private void adicionarAcordoAtual(SrConfiguracao conf) throws Exception {
        SrAcordo acordoAtual = SrAcordo.AR.findById(conf.getAcordo().getIdAcordo()).getAcordoAtual();
        if (acordoAtual != null && !getAcordos().contains(acordoAtual))
            getAcordos().add(acordoAtual);
    }

    private void checarEPreencherCampos() throws Exception {

        if (getCadastrante() == null)
            throw new Exception("Cadastrante n\u00E3o pode ser nulo");

        if (getDtReg() == null)
            setDtReg(new Date());

        if (getArquivo() != null) {
            double lenght = (double) getArquivo().getBlob().length / 1024 / 1024;
            if (lenght > 2)
                throw new IllegalArgumentException("O tamanho do arquivo (" + new DecimalFormat("#.00").format(lenght) + "MB) \u00E9 maior que o m\u00E1ximo permitido (2MB)");
        }

        if (getLotaCadastrante() == null || getLotaCadastrante().getId() == null)
            setLotaCadastrante(getCadastrante().getLotacao());

        if (getTitular() == null || getTitular().getId() == null)
            setTitular(getCadastrante());

        if (getLotaTitular() == null || getLotaTitular().getId() == null)
            setLotaTitular(getTitular().getLotacao());

        if (getSolicitante() == null || getSolicitante().getId() == null)
            setSolicitante(getCadastrante());

        if (getLotaSolicitante() == null || getLotaSolicitante().getId() == null)
            setLotaSolicitante(getSolicitante().getLotacao());

        if (getSolicitante().equivale(getCadastrante())) {
            setDtOrigem(null);
            setMeioComunicacao(null);
        }

        if (getOrgaoUsuario() == null || getOrgaoUsuario().getId() == null)
            setOrgaoUsuario(getLotaSolicitante().getOrgaoUsuario());

        if (getNumSolicitacao() == null && !isRascunho() && !isFilha()) {
            // DB1: Verifica se Ã¯Â¿Â½ uma Solicita\u00E7Ã£o Filha, pois caso seja nÃ£o
            // deve atualizar o nÃ¯Â¿Â½mero da solicitaÃ§Ã£o, caso contrÃ¯Â¿Â½rio nÃ£o
            // funcionarÃ¯Â¿Â½ o filtro por cÃ¯Â¿Â½digo para essa filha
            setNumSolicitacao(getProximoNumero());
            atualizarCodigo();
        }

        if (getGravidade() == null)
            setGravidade(SrGravidade.NORMAL);

        if (getUrgencia() == null)
            setUrgencia(SrUrgencia.NORMAL);

        if (getTendencia() == null)
            setTendencia(SrTendencia.PIORA_MEDIO_PRAZO);

        // sÃÂ³ valida o atendente caso nÃÂ£o seja rascunho
        if (!isRascunho() && getDesignacao() == null)
            throw new Exception("N\u00E3o foi encontrado nenhum atendente designado " + "para esta solicita\u00E7\u00E3o. Sugest\u00E3o: alterar item de " + "configura\u00E7\u00E3o e/ou a\u00E7\u00E3o");

        if (isFilha()) {
            if (getDescrSolicitacao() != null && (getDescrSolicitacao().equals(getSolicitacaoPai().getDescrSolicitacao()) || getDescrSolicitacao().trim().isEmpty()))
                setDescrSolicitacao(null);

            if (this.meuAtributoSolicitacaoSet != null)
                this.meuAtributoSolicitacaoSet = null;
        }

        atualizarAcordos();
    }

    public void desfazerUltimaMovimentacao(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (!podeDesfazerMovimentacao(cadastrante, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);

        SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();

        // tratamento pois pode ter retorno nulo do mÃ¯Â¿Â½todo
        // getUltimaMovimentacaoCancelave()
        if (movimentacao != null) {

            if (movimentacao.getTipoMov() != null) {
                // caso seja movimentacao cancelada ou fechada, reinsere nas
                // listas de prioridade
                if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO || movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_FECHAMENTO)
                    reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);

                if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_JUNTADA) {
                    this.save();
                }
            }

            movimentacao.desfazer(cadastrante, lotaCadastrante, titular, lotaTitular);
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
            a.save();
        }
    }

    private SortedSet<SrMarca> calcularMarcadores() throws Exception {
        SortedSet<SrMarca> set = new TreeSet<SrMarca>();

        //Edson: depois, eliminar a necessidade de dar tratamento diferenciado ao
        //estado Em Elaboracao. Mesma coisa mais abaixo, ao incluir marca
        //em Elaboracao agendada em razao de pendencia
        if (isRascunho())
            acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_EM_ELABORACAO, null, null, getCadastrante(), getLotaTitular());

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
                	marcador = CpMarcador.MARCADOR_SOLICITACAO_ATIVO;
                    movMarca = mov;
                    marcadorAndamento = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
                }
                if (t == TIPO_MOVIMENTACAO_FECHAMENTO) {
                    marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
                    movMarca = mov;
                    marcadorAndamento = null;
                }
                if (t == TIPO_MOVIMENTACAO_REABERTURA) {
                	marcador = CpMarcador.MARCADOR_SOLICITACAO_ATIVO;
                    movMarca = mov;
                    marcadorAndamento = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
                }
                if (t == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) {
                    marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
                    movMarca = mov;
                    marcadorAndamento = null;
                }
                if (t == TIPO_MOVIMENTACAO_JUNTADA) {
                	marcadorAndamento = CpMarcador.MARCADOR_JUNTADO;
                }
                if (t == TIPO_MOVIMENTACAO_DESENTRANHAMENTO) {
                	marcadorAndamento = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
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
                    acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, ultimaPendencia.getDtIniMov(), dtFimPendenciaMaisLonge, getCadastrante(), getLotaCadastrante());
                  //Edson: se pendencia eh agendada, agenda a marca Em Elaboracao
                    if (dtFimPendenciaMaisLonge != null)
                            acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_EM_ELABORACAO, dtFimPendenciaMaisLonge, null,
                                            cadastrante, lotaTitular);
                } else{ 
                    acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, ultimaPendencia.getDtIniMov(), dtFimPendenciaMaisLonge, ultimaPendencia.getAtendente(),
                            ultimaPendencia.getLotaAtendente());
                  //Edson: se pendencia eh agendada, agenda a marca Em Andamento
                  //A clausula '||marcador==ATIVO' serve para suportar solicitacoes da epoca
                  //em que era permitido fechar estando pendente
                  if (dtFimPendenciaMaisLonge != null && marcador == CpMarcador.MARCADOR_SOLICITACAO_ATIVO)
                          acrescentarMarca(set, marcadorAndamento, dtFimPendenciaMaisLonge, null,
                                  movMarca.getAtendente(), movMarca.getLotaAtendente());
                }
            } else {
                if (marcador == CpMarcador.MARCADOR_SOLICITACAO_ATIVO)
                        acrescentarMarca(set, marcadorAndamento, movMarca.getDtIniMov(), null,
                                        movMarca.getAtendente(), movMarca.getLotaAtendente());
            }

            if (!isFechado() && !isCancelado()) {
            	if (marcador == CpMarcador.MARCADOR_SOLICITACAO_ATIVO) {
                acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null, null, getCadastrante(), getLotaCadastrante());
                acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE, null, null, getSolicitante(), getLotaSolicitante());

                Date prazo = getDtPrazoAtendimentoAcordado();
                if (prazo != null)
                    acrescentarMarca(set, 
                    		CpMarcador.MARCADOR_SOLICITACAO_FORA_DO_PRAZO,
                    		prazo, null, movMarca.getAtendente(),
                    		movMarca.getLotaAtendente());
            }
           
            if (marcador == CpMarcador.MARCADOR_SOLICITACAO_FECHADO
                        && isFilha())
                solicitacaoPai.atualizarMarcas();

            if (!isFechado() && isAFechar())
                acrescentarMarca(set,
                                CpMarcador.MARCADOR_SOLICITACAO_FECHADO_PARCIAL, movMarca.getDtIniMov(),
                                null, movMarca.getAtendente(), movMarca.getLotaAtendente());

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
        List<Long> marcadoresDesconsiderar = Arrays.asList(new Long[] { CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE, CpMarcador.MARCADOR_SOLICITACAO_ATIVO });

        Set<SrMarca> marcas = getMarcaSetAtivas();

        for (SrMarca mar : marcas) {
            if (marcadoresDesconsiderar.contains(mar.getCpMarcador().getIdMarcador()))
                continue;
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(mar.getCpMarcador().getDescrMarcador());
            sb.append(" (");
            if (mar.getDpPessoaIni() != null) {
                String nome = mar.getDpPessoaIni().getDescricaoIniciaisMaiusculas();
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
        filtro.setAcaoFiltro(getAcao());
        filtro.setCpTipoConfiguracao(ContextoPersistencia.em().find(CpTipoConfiguracao.class, CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA));

        List<ListaInclusaoAutomatica> listaFinal = new ArrayList<ListaInclusaoAutomatica>();
        for (SrConfiguracao conf : SrConfiguracao.listar(filtro, new int[] { SrConfiguracaoBL.ATENDENTE, SrConfiguracaoBL.LISTA_PRIORIDADE })) {
            if (conf.getListaPrioridade() != null && conf.getListaPrioridade().getListaAtual().isAtivo()) {
            	SrLista listaConf = SrLista.AR.findById(conf.getListaPrioridade().getIdLista());
            	ListaInclusaoAutomatica listaInclusaoAutomatica = new ListaInclusaoAutomatica(listaConf.getListaAtual(), conf.getPrioridadeNaLista());

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

                    listaSubstitutos = ContextoPersistencia.em().createQuery("from DpSubstituicao dps where dps.titular = null and dps.lotaTitular.idLotacao in "
                                            + "     (select lot.idLotacao from DpLotacao lot where lot.idLotacaoIni = :idLotacaoIni) and "
                                            + "(dtFimSubst = null or dtFimSubst > sysdate) and dps.substituto is not null and dtFimRegistro = null")
                                            .setParameter("idLotacaoIni", lotaAtendente.getIdInicial()).getResultList();

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

    public List<SrLista> getListasDisponiveisParaInclusao(DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
        List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);

        for (SrLista l : SrLista.listar(false)) {
            SrLista atual = l.getListaAtual();
            if (atual.podeIncluir(lotaTitular, cadastrante) && !listaFinal.contains(atual))
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
        Set<SrLista> associadas = new HashSet<SrLista>();
        for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
            if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_INCLUSAO_LISTA || mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA && mov.getLista().isAtivo())
                associadas.add(mov.getLista());
            else if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
                associadas.remove(mov.getLista());
        return associadas;
    }

    public Set<SrSolicitacao> getSolicitacoesVinculadas() {
        Set<SrSolicitacao> solVinculadas = new HashSet<SrSolicitacao>();

        // vinculacoes partindo desta solicitacao
        for (SrMovimentacao mov : getMovimentacaoSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
            if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_VINCULACAO && mov.getSolicitacaoReferencia() != null)
                solVinculadas.add(mov.getSolicitacaoReferencia());

        // vinculacoes partindo de outra solicitacao referenciando esta
        for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
            if (this.equals(mov.getSolicitacaoReferencia()))
                solVinculadas.add(mov.getSolicitacao());

        return solVinculadas;
    }

    public Set<SrSolicitacao> getSolicitacoesJuntadas() {
        Set<SrSolicitacao> solJuntadas = new HashSet<SrSolicitacao>();

        for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA))
            if (!mov.isFinalizada() && this.equals(mov.getSolicitacaoReferencia()))
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
            throw new IllegalArgumentException("Lista n\u00E3o informada");

        if (isEmLista(lista))
            throw new IllegalArgumentException("Lista " + lista.getNomeLista() + " j\u00E1 cont\u00E9m a solicita\u00E7\u00E3o " + getCodigo());

        SrMovimentacao mov = new SrMovimentacao();
        mov.setCadastrante(pess);
        mov.setLotaCadastrante(lota);
        mov.setTitular(pessTitular);
        mov.setLotaTitular(lota);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_INCLUSAO_LISTA));
        mov.setDescrMovimentacao("Inclus\u00E3o na lista " + lista.getNomeLista());
        mov.setLista(lista);
        mov.setSolicitacao(this);
        mov.salvarAtualizandoSolicitacao();

        lista.incluir(this, prioridade, naoReposicionarAutomatico);
        ContextoPersistencia.em().flush();
    }

    public void retirarDeLista(SrLista lista, DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (lista == null)
            throw new IllegalArgumentException("Lista n\u00E3o informada");

        SrMovimentacao mov = new SrMovimentacao();
        mov.setCadastrante(cadastrante);
        mov.setLotaCadastrante(lotaCadastrante);
        mov.setTitular(titular);
        mov.setLotaTitular(lotaTitular);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA));
        mov.setDescrMovimentacao("Cancelamento de Inclus\u00E3o em Lista");
        mov.setSolicitacao(this);
        mov.setLista(lista);
        mov.salvarAtualizandoSolicitacao();
        lista.retirar(this, cadastrante, lotaCadastrante);
    }

    private void iniciarAtendimento(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO));
        if (getAtendenteNaoDesignado() == null)
            mov.setLotaAtendente(getDesignacao().getAtendente());
        else
            mov.setLotaAtendente(getAtendenteNaoDesignado());
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void fechar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String motivo) throws Exception {

        if (isPai() && !isAFechar())
            throw new Exception("Opera\u00E7\u00E3o n\u00E3o permitida. Necess\u00E1rio fechar toda solicita\u00E7\u00E3o " + "filha criada partir dessa que deseja fechar.");

        if ((cadastrante != null) && !podeFechar(cadastrante, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);

        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_FECHAMENTO));
        mov.setDescrMovimentacao(motivo);
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

        removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);

        if (isFilha() && solicitacaoPai.isAFechar()){
            for (SrMovimentacao iniP : solicitacaoPai.getPendenciasEmAberto()){
                    if (iniP.getMotivoPendencia().equals(SrTipoMotivoPendencia.ATENDIMENTO_NA_FILHA))
                            solicitacaoPai.terminarPendencia(cadastrante, lotaCadastrante, titular, lotaTitular, "", iniP.getIdMovimentacao());
            }
        }
        
        if (podeFecharPaiAutomatico())
            getSolicitacaoPai().fechar(cadastrante, lotaCadastrante, titular, lotaTitular, "Solicita\u00E7\u00E3o fechada automaticamente");

        /*
         * if (temPesquisaSatisfacao()) enviarPesquisa();
         */
    }

    public void enviarPesquisa() throws Exception {
    	CorreioHolder
    		.get()
    		.pesquisaSatisfacao(this);
    }

    public void responderPesquisa(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, Map<Long, String> respostaMap) throws Exception {
        if (!podeResponderPesquisa(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setDescrMovimentacao("Avalia\u00E7\u00E3o realizada.");
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
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        DpLotacao lotacaoAtendente = getLotaAtendente();

        if (lotacaoAtendente.isFechada())
                throw new Exception("Operaï¿½ï¿½o nï¿½o permitida: A Lotaï¿½ï¿½o atendente (" + lotacaoAtendente.getSiglaCompleta() +
                                ") foi extinta. Necessï¿½rio abrir nova solicitaï¿½ï¿½o. Crie um vinculo dessa solicitaï¿½ï¿½o com a nova, atravï¿½s do recurso Vincular");
        
        reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);

        SrMovimentacao mov = new SrMovimentacao(this);
        mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA));
        //mov.setLotaAtendente(getUltimoAtendenteEtapaAtendimento());
        mov.setLotaAtendente(lotacaoAtendente);
        mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    private void reInserirListasDePrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        for (SrMovimentacao mov : getMovimentacaoSet()) {
            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO
                    || mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO)
                break;

            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
                incluirEmLista(mov.getLista(), cadastrante, lotaCadastrante, titular, lotaTitular, getPrioridade(), false);

        }
    }

    public void deixarPendente(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrTipoMotivoPendencia motivo, String calendario, String horario,
            String detalheMotivo) throws Exception {
        if (!podeDeixarPendente(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);

        if (calendario != null && !"".equals(calendario)) {
            DateTime dateTime = null;
            if (horario != null && !"".equals(horario)) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(DD_MM_YYYY_HH_MM);
                dateTime = new DateTime(formatter.parseDateTime(calendario + " " + horario));
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(DD_MM_YYYY);
                dateTime = new DateTime(formatter.parseDateTime(calendario));
            }
            if (dateTime != null)
                movimentacao.setDtAgenda(dateTime.toDate());
        }

        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA));
        movimentacao.setMotivoPendencia(motivo);
        movimentacao.setDescrMovimentacao(motivo.getDescrTipoMotivoPendencia());
        if (detalheMotivo != null && !"".equals(detalheMotivo.trim()))
            movimentacao.setDescrMovimentacao(movimentacao.getDescrMovimentacao() + " | " + detalheMotivo);
        if (movimentacao.getDtAgenda() != null)
            movimentacao.setDescrMovimentacao(movimentacao.getDescrMovimentacao() + " | Fim previsto: " + movimentacao.getDtAgendaDDMMYYHHMM());
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void alterarPrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrPrioridade prioridade) throws Exception{
    	if (!podeAlterarPrioridade(titular, lotaTitular))
            throw new Exception("Operaï¿½ï¿½o nï¿½o permitida");
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE));
        movimentacao.setDescrMovimentacao("Prioridade tecnica: " + prioridade.getDescPrioridade());
        movimentacao.setPrioridade(prioridade);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void terminarPendencia(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String descricao, Long idMovimentacao) throws Exception {
        if (!podeTerminarPendencia(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA));

        // Edson: eh necessario setar a finalizadora na finalizada antes de
        // salvar() a finalizadora, pq se nÃ£o, ao atualizarMarcas(), vai
        // parecer que a pendencia nao foi finalizada, atrapalhando calculos
        // de prazo
        SrMovimentacao movFinalizada = SrMovimentacao.AR.findById(idMovimentacao);
        movFinalizada.setMovFinalizadora(movimentacao);

        movimentacao.setDescrMovimentacao(descricao);
        movimentacao.setDescrMovimentacao(movimentacao.getDescrMovimentacao() + " | Terminando pendencia iniciada em " + movFinalizada.getDtIniMovDDMMYYHHMM());
        movimentacao = movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
        movFinalizada.save();
    }

    public void cancelar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        if (!podeCancelar(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO));
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

        // remove das listas apos finalizar, para que seja possivel reabrir
        // depois
        removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void juntar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, SrSolicitacao solRecebeJuntada, String justificativa) throws Exception {
        if ((cadastrante != null) && !podeJuntar(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        if (solRecebeJuntada.equivale(this))
            throw new Exception("N\u00E3o \u00E9 possivel juntar uma solicita\u00E7\u00E3o a si mesma.");
        // Edson: comentei porque, como o ObjetoObjectInstantiator executa o detach na solRecebeJuntada, dava lazy exception abaixo
        // if (solRecebeJuntada.isJuntada() && solRecebeJuntada.getSolicitacaoPrincipal().equivale(this))
        //    throw new Exception("N\u00E3o e possivel realizar juntada circular.");
        if (solRecebeJuntada.isFilha() && solRecebeJuntada.getSolicitacaoPai().equivale(this))
            throw new Exception("N\u00E3o e possivel juntar uma solicita\u00E7\u00E3o a uma das suas filhas. Favor realizar o processo inverso.");

        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_JUNTADA));
        movimentacao.setSolicitacaoReferencia(solRecebeJuntada);
        movimentacao.setDescrMovimentacao(justificativa + " | Juntando a " + solRecebeJuntada.codigo);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public void desentranhar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String justificativa) throws Exception {
        if (!podeDesentranhar(titular, lotaTitular))
            throw new Exception(OPERACAO_NAO_PERMITIDA);
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
            throw new Exception(OPERACAO_NAO_PERMITIDA);
        if (solRecebeVinculo.equivale(this))
            throw new Exception("N\u00E3o e poss\u00EDvel vincular uma solicita\u00E7\u00E3o a si mesma.");
        SrMovimentacao movimentacao = new SrMovimentacao(this);
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_VINCULACAO));
        movimentacao.setSolicitacaoReferencia(solRecebeVinculo);
        movimentacao.setDescrMovimentacao(justificativa + " | Vinculando a " + solRecebeVinculo.codigo);
        movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
    }

    public String getGcTags() {
        String s = "tags=@servico";
        if (getAcao() != null)
            s += getAcao().getGcTags();
        if (getItemConfiguracao() != null)
            s += getItemConfiguracao().getGcTags();
        return s;
    }

    public String getGcTagAbertura() {
        String s = "^sr:";
        if (getAcao() != null)
            s += Texto.slugify(getAcao().getTituloAcao(), true, false);
        if (getItemConfiguracao() != null)
            s += "-" + Texto.slugify(getItemConfiguracao().getTituloItemConfiguracao(), true, false);
        return s;
    }

    public String getGcTituloAbertura() {
        String s = "";
        if (getAcao() != null)
            s += getAcao().getTituloAcao();
        if (getItemConfiguracao() != null)
            s += " - " + getItemConfiguracao().getTituloItemConfiguracao();
        return s;
    }

    public String getDtOrigemDDMMYYYYHHMM() {
        if (getDtOrigem() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
            return df.format(getDtOrigem());
        }
        return "";
    }

    public String getDtOrigemHHMM() {
        if (getDtOrigem() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(HH_MM);
            return df.format(getDtOrigem());
        }
        return "";
    }

    public String getDtOrigemDDMMYYYY() {
        if (getDtOrigem() != null) {
            final SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.format(getDtOrigem());
        }
        return "";
    }

    public String getDtOrigemString() {
        if (getDtOrigem() != null) {
            SigaPlayCalendar cal = new SigaPlayCalendar();
            cal.setTime(getDtOrigem());
            return cal.getTempoTranscorridoString(false);
        }
        return "";
    }

    public void setDtOrigemString(String stringDtMeioContato) {
        DateTimeFormatter formatter = forPattern(DD_MM_YYYY_HH_MM);
        if (stringDtMeioContato != null && !stringDtMeioContato.isEmpty() && stringDtMeioContato.contains("/") && stringDtMeioContato.contains(":"))
            this.setDtOrigem(new DateTime(formatter.parseDateTime(stringDtMeioContato)).toDate());
    }

    public String getDtIniEdicaoDDMMYYYYHHMMSS() {
        if (getDtIniEdicao() != null) {
            final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return df.format(getDtIniEdicao());
        }
        return "";
    }

    public void setDtIniEdicaoDDMMYYYYHHMMSS(String string) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            this.setDtIniEdicao(df.parse(string));
        } catch (Exception e) {

        }
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

    // Edson: retorna os periodos de pendencia de forma linear, ou seja,
    // colapsando as sobreposicoes, para facilitar os calculos:
    // Transforma: -------
    // -------
    //
    // em: ----------
    private Map<Date, Date> getTrechosPendentes() {
        Map<Date, Date> pendencias = new LinkedHashMap<Date, Date>();
        Date dtIniEmAberto = null, dtFimEmAberto = null;

        for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {

            if (dtIniEmAberto != null && dtFimEmAberto != null && mov.getDtIniMov().after(dtFimEmAberto)) {
                dtIniEmAberto = null;
                dtFimEmAberto = null;
            }

            if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
                if (dtIniEmAberto == null)
                    dtIniEmAberto = mov.getDtIniMov();
                if (dtFimEmAberto == null || (mov.getDtFimMov() != null && mov.getDtFimMov().after(dtFimEmAberto)))
                    dtFimEmAberto = mov.getDtFimMov();
                pendencias.put(dtIniEmAberto, dtFimEmAberto);
                if (dtFimEmAberto == null)
                    return pendencias;
            }
        }
        return pendencias;
    }

    public Date getDataAPartirDe(Date dtBase, Long segundosAdiante) {
        Map<Date, Date> pendencias = getTrechosPendentes();
        for (Date dtIniPendencia : pendencias.keySet()) {
            if (dtIniPendencia.before(dtBase))
                continue;
            Date dtFimPendencia = pendencias.get(dtIniPendencia);
            if (dtFimPendencia == null)
                return null;
            int delta = (int) (dtIniPendencia.getTime() - dtBase.getTime()) / 1000;
            if (delta <= segundosAdiante) {
                segundosAdiante -= delta;
                dtBase = dtFimPendencia;
            } else
                break;
        }
        return new Date(dtBase.getTime() + segundosAdiante * 1000);
    }

    public Date getDtPrazoCadastramentoAcordado() {
        if (getAcordos() == null || getAcordos().isEmpty() || isCancelado())
            return null;
        Long menorTempoAcordado = null;
        for (SrAcordo a : getAcordos()) {
            Long acordado = a.getAtributoEmSegundos("tempoDecorridoCadastramento");
            if (menorTempoAcordado == null || (acordado != null && acordado < menorTempoAcordado))
                menorTempoAcordado = acordado;
        }
        if (menorTempoAcordado == null)
            return null;
        return getDataAPartirDe(getDtInicioPrimeiraEdicao(), menorTempoAcordado);
    }

    public Date getDtPrazoAtendimentoAcordado() {
        if (getAcordos() == null || getAcordos().isEmpty() || isCancelado())
            return null;
        Long menorTempoAcordado = null;
        for (SrAcordo a : getAcordos()) {
            Long acordado = a.getAtributoEmSegundos("tempoDecorridoAtendimento");
            if (menorTempoAcordado == null || (acordado != null && acordado < menorTempoAcordado))
                menorTempoAcordado = acordado;
        }
        if (menorTempoAcordado == null)
            return null;
        return getDataAPartirDe(getDtInicioAtendimento(), menorTempoAcordado);
    }
    
    public String getDtPrazoCadastramentoAcordadoDDMMYYYYHHMM() {
        Date dt = getDtPrazoCadastramentoAcordado();
        if (dt != null) {
                final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return "<span style=\"display: none\">" + new SimpleDateFormat("yyyyMMdd").format(dt)
                                + "</span>" + df.format(dt);
        }
        return "";
    }
    
    public String getDtPrazoAtendimentoAcordadoDDMMYYYYHHMM() {
        Date dt = getDtPrazoAtendimentoAcordado();
        if (dt != null) {
                final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return "<span style=\"display: none\">" + new SimpleDateFormat("yyyyMMdd").format(dt)
                                + "</span>" + df.format(dt);
        }
        return "";
    }
    
    public String getDtPrazoDDMMYYYYHHMM(){
    	if (jaFoiDesignada())
    		return getDtPrazoAtendimentoAcordadoDDMMYYYYHHMM();
    	else return getDtPrazoCadastramentoAcordadoDDMMYYYYHHMM();
    }

    public Cronometro getCronometro() throws Exception {
        if (getCron() == null) {
            setCron(new Cronometro());
            boolean fechado = isFechado(), cancelado = isCancelado(), pendente = isPendente();
            if (jaFoiDesignada()) {
                getCron().setDescricao("Atendimento");
                getCron().setInicio(getDtInicioAtendimento());
                getCron().setFim(fechado ? getDtEfetivoFechamento() : cancelado ? getDtCancelamento() : getDtPrazoAtendimentoAcordado());
                if (getCron().getFim() == null || fechado || cancelado)
                    getCron().setDecorrido(getTempoDecorridoAtendimento().getValor() * 1000);
                else
                    getCron().setRestante(getCron().getFim().getTime() - new Date().getTime());
            } else {
                getCron().setDescricao("Cadastro");
                getCron().setInicio(getDtInicioPrimeiraEdicao());
                getCron().setFim(getDtPrazoCadastramentoAcordado());
                if (getCron().getFim() == null || fechado || cancelado)
                    getCron().setDecorrido(getTempoDecorridoCadastramento().getValor() * 1000);
                else
                    getCron().setRestante(getCron().getFim().getTime() - new Date().getTime());
            }
            getCron().setLigado(!fechado && !cancelado && (getCron().getFim() != null || !pendente));
        }

        return getCron();
    }

    // Edson: retorna o tempo decorrido entre duas datas, descontando
    // os perÃ£odos de pendÃ¯Â¿Â½ncia (blocos).
    // PPP, abaixo, Ã¯Â¿Â½ o bloco pendente. I Ã¯Â¿Â½ dtIni e F Ã¯Â¿Â½ dtFim
    public SrValor getTempoDecorrido(Date dtIni, Date dtFim) {
        Map<Date, Date> pendencias = getTrechosPendentes();
        Long decorrido = 0L;
        for (Date dtIniBlocoPendencia : pendencias.keySet()) {
            Date dtFimBlocoPendencia = pendencias.get(dtIniBlocoPendencia);

            // ----------I----------F---PPPP---
            if (dtIniBlocoPendencia.after(dtFim))
                break;

            // ---PPPP---I----------F----------
            if (dtFimBlocoPendencia != null && dtFimBlocoPendencia.before(dtIni))
                continue;

            // ----------I---PPPP---F----------
            if (dtIniBlocoPendencia.after(dtIni))
                decorrido += (int) ((dtIniBlocoPendencia.getTime() - dtIni.getTime()) / 1000);

            dtIni = dtFimBlocoPendencia;

            // ----------I---------PPFP--------- ou
            // ----------I---------PPFPPPPPPPPPP...
            if (dtFimBlocoPendencia == null || dtFimBlocoPendencia.after(dtFim))
                return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
        }
        decorrido += (int) ((dtFim.getTime() - dtIni.getTime()) / 1000);
        return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
    }

    public SrValor getTempoDecorridoCadastramento() {
        Date dtFimCadastro = isFechado() ? getDtEfetivoFechamento() : isCancelado() ? getDtCancelamento() : getDtInicioAtendimento();
        if (dtFimCadastro == null)
            dtFimCadastro = new Date();
        return getTempoDecorrido(getDtInicioPrimeiraEdicao(), dtFimCadastro);
    }

    public SrValor getTempoDecorridoAtendimento() {
        Date dtFechamento = isFechado() ? getDtEfetivoFechamento() : isCancelado() ? getDtCancelamento() : new Date();
        return getTempoDecorrido(getDtInicioAtendimento(), dtFechamento);
    }

    // Edson: implementar no futuro
    public Long getResultadoPesquisaSatisfacao() {
        return 0L;
    }

    public boolean isAcordosSatisfeitos() {
        if (getAcordos() == null || getAcordos().isEmpty())
            return true;
        for (SrAcordo a : getAcordos()) {
            if (!isAcordoSatisfeito(a))
                return false;
        }
        return true;
    }

    public boolean isAcordoSatisfeito(SrAcordo acordo) {
        if (acordo == null)
            return true;
        if (acordo.getAtributoAcordoSet() == null)
            return true;
        for (SrAtributoAcordo pa : acordo.getAtributoAcordoSet()) {
            if (!isAtributoAcordoSatisfeito(pa))
                return false;
        }
        return true;
    }

    public boolean isAtributoAcordoSatisfeito(SrAtributoAcordo atributoAcordo) {
        try {
            SrValor valor = (SrValor) SrSolicitacao.class.getMethod(atributoAcordo.getAtributo().asGetter()).invoke(this);
            if (valor == null)
                return true;
            return atributoAcordo.isNaFaixa(valor);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
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
        confSolicitante.setComplexo(getLocal());
        confSolicitante.setBuscarPorPerfis(true);
        pessoasAConsiderar.add(confSolicitante);

        if (jaFoiDesignada()) {
            SrConfiguracao confTitular = new SrConfiguracao();
            confTitular.setDpPessoa(getTitular());
            confTitular.setLotacao(getLotaTitular());
            confTitular.setComplexo(getLocal());
            confTitular.setBuscarPorPerfis(true);
            pessoasAConsiderar.add(confTitular);
        }

        return pessoasAConsiderar;

    }

    public List<SrItemConfiguracao> getHistoricoItem() {
        List<SrItemConfiguracao> historicoItens = listarHistoricoItemInicial();
        SrItemConfiguracao anterior = getItemConfiguracao();

        for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescentePorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO)) {
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
        for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescentePorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO)) {
            if (movimentacao.getAcao() != null && acaoAnterior != null && !movimentacao.getAcao().equivale(acaoAnterior)) {
                historicoAcoes.add(movimentacao.getAcao());
                acaoAnterior = movimentacao.getAcao();
            }
        }
        return historicoAcoes;
    }

    private Set<SrMovimentacao> getMovimentacaoSetOrdemCrescentePorTipo(Long idTipoMovimentacao) {
        return getMovimentacaoSet(false, null, true, false, false, false, Arrays.asList(idTipoMovimentacao));
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

    public SrItemConfiguracao getItemAtual() {
        List<SrItemConfiguracao> historicoItem = getHistoricoItem();
        if (historicoItem.isEmpty()) {
            return null;
        }
        int size = historicoItem.size();
        return historicoItem.get(size - 1);
    }

    public SrAcao getAcaoAtual() {
        List<SrAcao> historicoAcao = getHistoricoAcao();
        if (historicoAcao.isEmpty()) {
            return null;
        }
        int size = historicoAcao.size();
        return historicoAcao.get(size - 1);
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

    public void setLotaTitular(DpLotacao lotaTitular) {
        this.lotaTitular = lotaTitular;
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

    public Cronometro getCron() {
        return cron;
    }

    public void setCron(Cronometro cron) {
        this.cron = cron;
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

    public SrTendencia getTendencia() {
        return tendencia;
    }

    public void setTendencia(SrTendencia tendencia) {
        this.tendencia = tendencia;
    }

    public SrGravidade getGravidade() {
        return gravidade;
    }

    public void setGravidade(SrGravidade gravidade) {
        this.gravidade = gravidade;
    }

    public SrUrgencia getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(SrUrgencia urgencia) {
        this.urgencia = urgencia;
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

    public boolean isFecharAoAbrir() {
        return fecharAoAbrir;
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
		if (numSequencia == null) {
			return 0L;
		}
		return numSequencia;
    }

    public void setNumSequencia(Long numSequencia) {
        this.numSequencia = numSequencia;
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
}
