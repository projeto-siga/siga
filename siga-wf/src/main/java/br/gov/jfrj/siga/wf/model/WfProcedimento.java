package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import com.crivano.jflow.model.ProcessInstance;
import com.crivano.jflow.model.enm.ProcessInstanceStatus;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.DateUtils;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.util.CpProcessadorReferencias;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.logic.PodeSim;
import br.gov.jfrj.siga.wf.logic.WfPodeEditarVariaveis;
import br.gov.jfrj.siga.wf.logic.WfPodePegar;
import br.gov.jfrj.siga.wf.logic.WfPodeRedirecionar;
import br.gov.jfrj.siga.wf.logic.WfPodeTerminar;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTarefaDocCriarParam2;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.model.task.WfTarefaDocCriar;
import br.gov.jfrj.siga.wf.model.task.WfTarefaSubprocedimento;
import br.gov.jfrj.siga.wf.util.SiglaUtils;
import br.gov.jfrj.siga.wf.util.SiglaUtils.SiglaDecodificada;
import br.gov.jfrj.siga.wf.util.WfResp;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_procedimento")
public class WfProcedimento extends Objeto
        implements ProcessInstance<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp>, Selecionavel,
        Comparable<WfProcedimento> {
    public static ActiveRecord<WfProcedimento> AR = new ActiveRecord<>(WfProcedimento.class);

    @Id
    @GeneratedValue
    @Column(name = "PROC_ID", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFP_ID")
    private WfDefinicaoDeProcedimento definicaoDeProcedimento;

    @Column(name = "PROC_CD_PRINCIPAL")
    private String principal;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROC_TP_PRINCIPAL")
    private WfTipoDePrincipal tipoDePrincipal;

    @Column(name = "PROC_NR_CORRENTE")
    private Integer indiceCorrente = null;

    @Transient
    private Map<String, Object> variavelMap = new TreeMap<>();

    @BatchSize(size = 1)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimento")
    private List<WfVariavel> variaveis = new ArrayList<>();

    @BatchSize(size = 1)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimento")
    @OrderBy("HIS_DT_INI, MOVI_ID")
    private SortedSet<WfMov> movimentacoes = new TreeSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "PROC_ST_CORRENTE")
    private ProcessInstanceStatus status = ProcessInstanceStatus.INACTIVE;

    @Column(name = "PROC_TS_EVENTO")
    private Date eventoData;

    @Column(name = "PROC_NM_EVENTO")
    private String eventoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_ID_EVENTO")
    private DpPessoa eventoPessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOTA_ID_EVENTO")
    private DpLotacao eventoLotacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROC_TP_PRIORIDADE")
    private WfPrioridade prioridade = WfPrioridade.MEDIA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGU_ID")
    private CpOrgaoUsuario orgaoUsuario;

    @Column(name = "PROC_ANO")
    private Integer ano;

    @Column(name = "PROC_NR")
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_ID_TITULAR")
    private DpPessoa titular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOTA_ID_TITULAR")
    private DpLotacao lotaTitular;

    // Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
    // entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
    @OneToMany(mappedBy = "procedimento", fetch = FetchType.LAZY)
    @Where(clause = "ID_TP_MARCA=4")
    private Set<WfMarca> marcas = new TreeSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HIS_DT_INI")
    @Desconsiderar
    private Date hisDtIni;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HIS_DT_FIM")
    @Desconsiderar
    private Date hisDtFim;

    @Desconsiderar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HIS_IDC_INI")
    private CpIdentidade hisIdcIni;

    public WfProcedimento() {
    }

    public WfProcedimento(WfDefinicaoDeProcedimento definicao, Map<String, Object> variavel) {
        this.definicaoDeProcedimento = definicao;
        if (variavel != null)
            this.variavelMap.putAll(variavel);
    }

    @Override
    public void start() {
        indiceCorrente = -1;
        status = ProcessInstanceStatus.STARTED;
        this.setHisDtIni(WfDao.getInstance().consultarDataEHoraDoServidor());
    }

    @Override
    public void pause(String eventoNome, WfResp responsavel) {
        DpPessoa pessoaResponsavel = responsavel != null ? responsavel.getPessoa() : null;
        DpLotacao lotacaoResponsavel = responsavel != null ? responsavel.getLotacao() : null;

        if (!Utils.igual(this.eventoNome, eventoNome) 
                || !Utils.equivale(this.eventoPessoa, pessoaResponsavel) 
                || !Utils.equivale(this.eventoLotacao, lotacaoResponsavel))
            this.eventoData = new Date();
        
        this.eventoNome = eventoNome;
        this.eventoPessoa = pessoaResponsavel;
        this.eventoLotacao = lotacaoResponsavel;
        status = ProcessInstanceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.eventoNome = null;
        this.eventoPessoa = null;
        this.eventoLotacao = null;
        this.eventoData = null;
        status = ProcessInstanceStatus.RESUMING;
    }

    @Override
    public void end() {
        this.eventoNome = null;
        this.eventoPessoa = null;
        this.eventoLotacao = null;
        this.eventoData = null;
        indiceCorrente = null;
        status = ProcessInstanceStatus.FINISHED;
        this.setHisDtFim(WfDao.getInstance().consultarDataEHoraDoServidor());
    }

    @Override
    public WfDefinicaoDeTarefa getCurrentTaskDefinition() {
        if (indiceCorrente == null)
            return null;
        return getTaskDefinitionByIndex(indiceCorrente);
    }

    @Override
    public WfDefinicaoDeTarefa getTaskDefinitionByIndex(int i) {
        if (i < 0 || i >= definicaoDeProcedimento.getTaskDefinition().size())
            return null;
        return definicaoDeProcedimento.getTaskDefinition().get(i);
    }

    // Se for "fim" retorna length + 1
    @Override
    public int getIndexById(String id) {
        return definicaoDeProcedimento.getIndexById(id);
    }

    @PostLoad
    public void onLoad() {
        variavelMap.clear();
        for (WfVariavel v : variaveis) {
            if (v.number != null)
                variavelMap.put(v.getIdentifier(), v.number);
            else if (v.bool != null)
                variavelMap.put(v.getIdentifier(), v.bool);
            else if (v.date != null)
                variavelMap.put(v.getIdentifier(), v.date);
            else
                variavelMap.put(v.getIdentifier(), v.string);
        }
    }

    @PrePersist
    private void onPersist() {
        if (getAno() != null)
            return;
        setAno(WfDao.getInstance().dt().getYear() + 1900);
        Query qry = em().createQuery(
                "select max(numero) from WfProcedimento pi where ano = :ano and orgaoUsuario.idOrgaoUsu = :ouid");
        qry.setParameter("ano", getAno());
        qry.setParameter("ouid", getOrgaoUsuario().getId());
        Integer i = (Integer) qry.getSingleResult();
        setNumero((i == null ? 0 : i) + 1);
    }

    @PreUpdate
    public void onSave() throws Exception {
    }

    private void assertPrincipal() throws RuntimeException {
        if (this.getPrincipal() == null)
            throw new RuntimeException("Não é possível identificar o responsável porque não há \'principal'");
    }

    private WfResp assertPrincipalPessoa(String s) {
        assertPrincipal();
        PessoaLotacaoParser lpp = new PessoaLotacaoParser(s);
        if (lpp.getLotacao() == null)
            return null;
        return new WfResp(lpp.getPessoa(), lpp.getLotacao());
    }

    private WfResp assertPrincipalLotacao(String s) {
        assertPrincipal();
        PessoaLotacaoParser lpp = new PessoaLotacaoParser(s);
        if (lpp.getLotacao() == null)
            return null;
        return new WfResp(null, lpp.getLotacao());
    }

    @Override
    public WfResp calcResponsible(WfDefinicaoDeTarefa tarefa) {
        return localizarResponsavelAtual(tarefa);
    }

    public WfResp localizarResponsavelAtual(WfDefinicaoDeTarefa tarefa) {
        WfResp resp = localizarResponsavelOriginal(tarefa);
        if (resp == null)
            return null;

        for (WfMov mov : getMovimentacoes()) {
            if (!mov.isAtivo())
                continue;
            if (mov instanceof WfMovDesignacao) {
                WfMovDesignacao m = (WfMovDesignacao) mov;
                if ((m.getPessoaDe() != null && m.getPessoaDe().equivale(resp.getPessoa()))
                        || (m.getLotaDe() != null && m.getLotaDe().equivale(resp.getLotacao()))) {
                    resp = new WfResp(m.getPessoaPara(), m.getLotaPara());
                }
            }
        }

        return resp;
    };

    public WfResp localizarResponsavelOriginal(WfDefinicaoDeTarefa tarefa) {
        ExService service = null;
        if (this.getPrincipal() != null && this.getTipoDePrincipal() == WfTipoDePrincipal.DOCUMENTO)
            service = Service.getExService();
        try {
            if (tarefa.getTipoDeTarefa() == WfTipoDeTarefa.AGUARDAR_ASSINATURA_PRINCIPAL)
                return assertPrincipalPessoa(service.cadastrante(getPrincipal()));
            if (tarefa.getTipoDeResponsavel() == null)
                return null;
            switch (tarefa.getTipoDeResponsavel()) {
                case PRINCIPAL_CADASTRANTE:
                    return assertPrincipalPessoa(service.cadastrante(getPrincipal()));
                case PRINCIPAL_LOTA_CADASTRANTE:
                    return assertPrincipalLotacao(service.cadastrante(getPrincipal()));
                case PRINCIPAL_TITULAR:
                    return assertPrincipalPessoa(service.titular(getPrincipal()));
                case PRINCIPAL_LOTA_TITULAR:
                    return assertPrincipalLotacao(service.titular(getPrincipal()));
                case PRINCIPAL_SUBSCRITOR:
                    return assertPrincipalPessoa(service.subscritor(getPrincipal()));
                case PRINCIPAL_LOTA_SUBSCRITOR:
                    return assertPrincipalLotacao(service.subscritor(getPrincipal()));
                case PRINCIPAL_DESTINATARIO:
                    return assertPrincipalPessoa(service.destinatario(getPrincipal()));
                case PRINCIPAL_LOTA_DESTINATARIO:
                    return assertPrincipalLotacao(service.destinatario(getPrincipal()));
                case PRINCIPAL_GESTOR:
                    return assertPrincipalPessoa(service.gestor(getPrincipal()));
                case PRINCIPAL_LOTA_GESTOR:
                    return assertPrincipalLotacao(service.gestor(getPrincipal()));
                case PRINCIPAL_FISCAL_TECNICO:
                    return assertPrincipalPessoa(service.fiscalTecnico(getPrincipal()));
                case PRINCIPAL_LOTA_FISCAL_TECNICO:
                    return assertPrincipalLotacao(service.fiscalTecnico(getPrincipal()));
                case PRINCIPAL_FISCAL_ADMINISTRATIVO:
                    return assertPrincipalPessoa(service.fiscalAdministrativo(getPrincipal()));
                case PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO:
                    return assertPrincipalLotacao(service.fiscalAdministrativo(getPrincipal()));
                case PRINCIPAL_INTERESSADO:
                    return assertPrincipalPessoa(service.interessado(getPrincipal()));
                case PRINCIPAL_LOTA_INTERESSADO:
                    return assertPrincipalLotacao(service.interessado(getPrincipal()));
                case RESPONSAVEL:
                    WfResponsavel r = WfDao.getInstance().consultarResponsavelPorOrgaoEDefinicaoDeResponsavel(
                            getOrgaoUsuario(), tarefa.getDefinicaoDeResponsavel());
                    return new WfResp(r.getPessoa(), r.getLotacao());
                case PESSOA:
                case LOTACAO:
                    return new WfResp(tarefa.getPessoa(), tarefa.getLotacao());
                case PROCEDIMENTO_TITULAR:
                    return new WfResp(titular, lotaTitular);
                case PROCEDIMENTO_LOTA_TITULAR:
                    return new WfResp(null, lotaTitular);
                default:
                    return null;
            }
        } catch (Exception e) {
            String sigla = this.getSigla();
            throw new RuntimeException(
                    "Não foi possível calcular o responsável pela tarefa " + (tarefa != null ? tarefa.getNome() : "")
                            + " do procedimento" + (sigla != null ? " " + sigla : ""),
                    e);
        }
    };

    @Override
    public WfDefinicaoDeProcedimento getProcessDefinition() {
        return definicaoDeProcedimento;
    }

    @Override
    public Integer getCurrentIndex() {
        return indiceCorrente;
    }

    @Override
    public Map<String, Object> getVariable() {
        return variavelMap;
    }

    @Override
    public ProcessInstanceStatus getStatus() {
        return status;
    }

    @Override
    public WfResp getResponsible() {
        return new WfResp(eventoPessoa, eventoLotacao);
    }

    @Override
    public void setCurrentIndex(int indiceCorrente) {
        this.indiceCorrente = indiceCorrente;
    }

    @Override
    public void setProcessDefinition(WfDefinicaoDeProcedimento definicao) {
        this.definicaoDeProcedimento = definicao;
    }

    @Override
    public void setVariable(Map<String, Object> variavel) {
        this.variavelMap = variavel;
    }

    public WfTipoDePrincipal getTipoDePrincipal() {
        return tipoDePrincipal;
    }

    public void setTipoDePrincipal(WfTipoDePrincipal tipoDePrincipal) {
        this.tipoDePrincipal = tipoDePrincipal;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WfDefinicaoDeTarefa getDefinicaoDeTarefaCorrente() {
        return getCurrentTaskDefinition();
    }

    public WfDefinicaoDeProcedimento getDefinicaoDeProcedimento() {
        return definicaoDeProcedimento;
    }

    public void setDefinicaoDeProcedimento(WfDefinicaoDeProcedimento definicao) {
        this.definicaoDeProcedimento = definicao;
    }

    public Integer getIndiceCorrente() {
        return indiceCorrente;
    }

    public void setIndiceCorrente(Integer indiceCorrente) {
        this.indiceCorrente = indiceCorrente;
    }

    public Map<String, Object> getVariavelMap() {
        return variavelMap;
    }

    public void setVariavelMap(Map<String, Object> variavelMap) {
        this.variavelMap = variavelMap;
    }

    public List<WfVariavel> getVariaveis() {
        return variaveis;
    }

    public void setVariaveis(List<WfVariavel> listaDeVariaveis) {
        this.variaveis = listaDeVariaveis;
    }

    public List<WfVariavel> getVariaveisOrdenadas() {
        List<WfVariavel> l = new ArrayList<>();
        WfDefinicaoDeTarefa tdSuper = getDefinicaoDeProcedimento().gerarDefinicaoDeTarefaComTodasAsVariaveis();
        Set<String> identificadoresAProcessar = new TreeSet<>();
        getVariable().keySet().stream().forEach(v -> identificadoresAProcessar.add(v));
        for (WfDefinicaoDeVariavel vd : tdSuper.getDefinicaoDeVariavel()) {
            String identificador = vd.getIdentificador();
            if (getVariable().containsKey(identificador)) {
                Optional<WfVariavel> x = variaveis.stream().filter(v -> identificador.equals(v.getIdentifier()))
                        .findFirst();
                if (!x.isPresent())
                    continue;
                l.add(x.get());
                identificadoresAProcessar.remove(identificador);
            }
        }
        for (String identificador : identificadoresAProcessar) {
            Optional<WfVariavel> x = variaveis.stream().filter(v -> identificador.equals(v.getIdentifier()))
                    .findFirst();
            if (!x.isPresent())
                continue;
            l.add(x.get());
        }
        return l;
    }

    public void setStatus(ProcessInstanceStatus status) {
        this.status = status;
    }

    public WfPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(WfPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Set<WfMov> getMovimentacoes() {
//		if (movimentacoes instanceof TreeSet)
//			return movimentacoes;
//		TreeSet<WfMov> set = new TreeSet<>();
//		set.addAll(movimentacoes);
//		movimentacoes = set;
        return movimentacoes;
    }

    public WfMov getUltimaMovimentacao() {
        WfMov last = null;
        for (WfMov e : movimentacoes)
            last = e;
        return last;
    }

    public void setMovimentacoes(SortedSet<WfMov> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public String getAtendente() {
        if (getEventoPessoa() != null)
            return getEventoPessoa().getSigla();
        if (getEventoLotacao() != null)
            return getEventoLotacao().getSigla();
        return "";
    }

    public CpOrgaoUsuario getOrgaoUsuario() {
        return orgaoUsuario;
    }

    public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
        this.orgaoUsuario = orgaoUsuario;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSigla() {
        if (orgaoUsuario != null && ano != null && numero != null)
            return orgaoUsuario.getAcronimoOrgaoUsu() + "-WF-" + ano + "/" + Utils.completarComZeros(numero, 5);
        return null;
    }

    public String getSiglaCompacta() {
        return getSigla().replace("-", "").replace("/", "");
    }

    public void setSigla(String sigla) {
        SiglaDecodificada d = SiglaUtils.parse(sigla, "WF", null);
        this.ano = d.ano;
        this.numero = d.numero;
        this.orgaoUsuario = d.orgaoUsuario;
    }

    public static WfProcedimento findBySigla(String sigla) throws NumberFormatException, Exception {
        return findBySigla(sigla, null);
    }

    public static WfProcedimento findBySigla(String sigla, CpOrgaoUsuario ouDefault)
            throws NumberFormatException, Exception {
        SiglaDecodificada d = SiglaUtils.parse(sigla, "WF", null);

        WfProcedimento info = null;

        if (d.id != null) {
            info = AR.findById(d.id);
        } else if (d.numero != null) {
            info = AR.find("ano = ?1 and numero = ?2 and orgaoUsuario.idOrgaoUsu = ?3", d.ano, d.numero,
                    d.orgaoUsuario.getId()).first();
        }

        if (info == null) {
            throw new AplicacaoException("Não foi possível encontrar uma instância de procedimento com o código "
                    + sigla + ". Favor verificá-lo.");
        } else
            return info;
    }

    public List<AcaoVO> getAcoes(DpPessoa titular, DpLotacao lotaTitular) {
        List<AcaoVO> set = new ArrayList<>();

        set.add(AcaoVO.builder().nome("_Anotar").icone("note_add").modal("anotarModal").exp(new PodeSim()).build());

        set.add(AcaoVO.builder().nome("Editar Variáveis").icone("database_edit")
                .acao("/app/procedimento/" + getSiglaCompacta() + "/editar-variaveis")
                .exp(new WfPodeEditarVariaveis(this, titular, lotaTitular)).build());

        set.add(AcaoVO.builder().nome("_Pegar").icone("add").acao("/app/procedimento/" + getSiglaCompacta() + "/pegar")
                .exp(new WfPodePegar(this, titular, lotaTitular)).post(true).build());

        set.add(AcaoVO.builder().nome("_Priorizar").icone("text_list_numbers").modal("priorizarModal")
                .exp(new PodeSim()).build());

        set.add(AcaoVO.builder().nome("_Redirecionar").icone("arrow_branch").modal("redirecionarModal")
                .exp(new WfPodeRedirecionar(this, titular, lotaTitular)).build());

        set.add(AcaoVO.builder().nome("_Terminar").icone("delete")
                .msgConfirmacao("Esta operação não pode ser revertida. Tem certeza que deseja terminar o procedimento?")
                .exp(new WfPodeTerminar(this, titular, lotaTitular))
                .acao("/app/procedimento/" + getSiglaCompacta() + "/terminar").post(true).build());

        return set;
    }

    public boolean isPausado() {
        return status == ProcessInstanceStatus.PAUSED;
    }

    public boolean isFinalizado() {
        return status == ProcessInstanceStatus.FINISHED || status == ProcessInstanceStatus.INACTIVE;
    }

    public boolean isRetomando() {
        return status == ProcessInstanceStatus.RESUMING;
    }

    public boolean isFormulario() {
        return status == ProcessInstanceStatus.PAUSED
                && getCurrentTaskDefinition().getTipoDeTarefa() == WfTipoDeTarefa.FORMULARIO;
    }

    public boolean isDesabilitarFormulario(DpPessoa titular, DpLotacao lotaTitular) {
        if (getDefinicaoDeTarefaCorrente() == null
                || getDefinicaoDeTarefaCorrente().getTipoDeTarefa() != WfTipoDeTarefa.FORMULARIO)
            return true;
        return !titular.equivale(getEventoPessoa()) && !lotaTitular.equivale(getEventoLotacao());
    }

    public boolean isDesabilitarConhecimento(DpPessoa titular, DpLotacao lotaTitular) {
        return !titular.equivale(getEventoPessoa()) && !lotaTitular.equivale(getEventoLotacao());
    }

    public Object obterValorDeVariavel(WfDefinicaoDeVariavel vd) {
        return getVariavelMap().get(vd.getIdentificador());
    }

    public String getMsgAviso(DpPessoa titular, DpLotacao lotaTitular) throws Exception {
        String s = getMsgAvisoSemReferencias(titular, lotaTitular);
        if (s == null)
            return s;
        return CpProcessadorReferencias.marcarReferenciasParaDocumentos(s, null);
    }

    public String getMsgAvisoSemReferencias(DpPessoa titular, DpLotacao lotaTitular) throws Exception {
//		WfConhecimento c = WfDao.getInstance().consultarConhecimento(ti.getDefinicaoDeTarefa().getId());
//		if (c != null) {
//			this.setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
//			this.setConhecimento(c.getDescricao());
//		}

        if (getStatus() == ProcessInstanceStatus.RESUMING)
            return "Este workflow está aguardando a realização de uma tarefa de sistema para prosseguir. Isto pode ocorrer porque a tarefa é realmente demorada ou porque ocorreu algum erro no processamento. "
                    + "Caso deseje que o sistema faça uma nova tentativa, clique <a href=\"/sigawf/app/procedimento/"
                    + getSiglaCompacta() + "/retomar\">aqui</a>.";

        DpLotacao lotEvento = getEventoLotacao();
        DpPessoa pesEvento = getEventoPessoa();

        if (!titular.equivale(pesEvento) && !lotaTitular.equivale(lotEvento)) {
            if (pesEvento != null && lotEvento != null)
                return "Esta tarefa será desempenhada por " + pesEvento.getSigla() + " na lotação "
                        + lotEvento.getSigla();
            if (pesEvento != null)
                return "Esta tarefa será desempenhada por " + pesEvento.getSigla();
            if (lotEvento != null)
                return "Esta tarefa será desempenhada pela lotação " + lotEvento.getSigla();
        }

        String siglaTitular = titular.getSigla() + "@" + lotaTitular.getSiglaCompleta();
        String respWF = null;
        if (pesEvento != null)
            respWF = pesEvento.getSigla();
        if (respWF == null && lotEvento != null)
            respWF = "@" + lotEvento.getSiglaCompleta();

        if (getDefinicaoDeTarefaCorrente() != null
                && (getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.SUBPROCEDIMENTO)) {
            String siglaDoDocumentoCriado = WfTarefaSubprocedimento.getSiglaDoSubprocedimentoCriado(this);
            return "Este workflow prosseguirá automaticamente quando o subprocedimento <a href=\"/sigawf/app/procedimento/"
                    + siglaDoDocumentoCriado.replace("/", "").replace("-", "") + "\">" + siglaDoDocumentoCriado
                    + "</a> for concluído.";
        }

        if (!Utils.empty(getPrincipal()) && getTipoDePrincipal() == WfTipoDePrincipal.DOCUMENTO) {
            ExService service = Service.getExService();
            String respEX = service.getAtendente(getPrincipal(), siglaTitular);
            boolean podeMovimentar = service.podeMovimentar(getPrincipal(), siglaTitular);
            DpLotacao lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();
            PessoaLotacaoParser plWF = new PessoaLotacaoParser(respWF);
            DpLotacao lotWF = plWF.getLotacaoOuLotacaoPrincipalDaPessoa();
            DpPessoa resp = plWF.getPessoa();
            DpLotacao lotaResp = plWF.getLotacao();
            boolean estaComTarefa = (resp != null && titular.equivale(resp))
                    || (lotaResp != null && lotaTitular.equivale(lotaResp));
            respEX = service.getAtendente(getPrincipal(), siglaTitular);
            lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();

            if (podeMovimentar && !estaComTarefa) {
                if (lotWF == null || !lotWF.equivale(lotEX)) {
                    return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal()
                            + " for transferido para " + (lotWF == null ? "[Ninguém]" : lotWF.getSigla()) + ".";
                }
            }
            if (lotEX == null && lotWF != null)
                return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal()
                        + ", que estiver em andamento com " + lotWF.getSigla() + ".";
            if (!podeMovimentar && estaComTarefa) {
                return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal()
                        + (lotEX == null
                                ? " for tramitado para " + (lotWF == null ? "[Ninguém]" : lotWF.getSigla()) + "."
                                : ", que está com " + lotEX.getSigla() + ", for devolvido.");
            }
            if (!podeMovimentar && !estaComTarefa) {
                if (lotWF != null && !lotWF.equivale(lotEX)) {
                    return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal() + ", que está com "
                            + (lotEX == null ? "[Ninguém]" : lotEX.getSigla()) + ", for tramitado para "
                            + (lotWF == null ? "[Ninguém]" : lotWF.getSigla()) + ".";
                }
            }
        }

        if (getDefinicaoDeTarefaCorrente() != null
                && getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.INCLUIR_DOCUMENTO) {
            return "Este workflow prosseguirá automaticamente quando for juntado ou incluído um documento do modelo '"
                    + getDefinicaoDeTarefaCorrente().getRefSigla() + "' ao documento " + getPrincipal()
                    + ". Clique <a href=\"/sigaex/app/expediente/doc/editar?mobilPaiSel.sigla=" + getPrincipal()
                    + "&criandoAnexo=true&modelo=" + getDefinicaoDeTarefaCorrente().getRefId()
                    + "\">aqui</a> para incluir.";
        }
        if (getDefinicaoDeTarefaCorrente() != null
                && getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.INCLUIR_AUXILIAR) {
            return "Este workflow prosseguirá automaticamente quando for anexado um arquivo auxilar ao documento "
                    + getPrincipal() + ". Clique <a href=\"/sigaex/app/expediente/mov/anexar_arquivo_auxiliar?sigla="
                    + getPrincipal() + "\">aqui</a> para incluir.";
        }
        if (getDefinicaoDeTarefaCorrente() != null
                && getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.AGUARDAR_ASSINATURA_PRINCIPAL) {
            return "Este workflow prosseguirá automaticamente quando o documento " + getPrincipal()
                    + " estiver assinado. Clique <a href=\"/sigaex/app/expediente/mov/assinar?sigla=" + getPrincipal()
                    + "\">aqui</a> para assinar.";
        }
        if (getDefinicaoDeTarefaCorrente() != null
                && (getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.CRIAR_DOCUMENTO
                        || getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.AUTUAR_DOCUMENTO)) {
            String siglaDoDocumentoCriado = WfTarefaDocCriar.getSiglaDoDocumentoCriado(this);
            if (WfTarefaDocCriarParam2.AGUARDAR_ASSINATURA.name().equals(getDefinicaoDeTarefaCorrente().getParam2())) {
                return "Este workflow prosseguirá automaticamente quando o documento " + siglaDoDocumentoCriado
                        + " estiver assinado. Clique <a href=\"/sigaex/app/expediente/mov/assinar?sigla="
                        + siglaDoDocumentoCriado + "\">aqui</a> para assinar.";
            } else if (getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.CRIAR_DOCUMENTO
                    && WfTarefaDocCriarParam2.AGUARDAR_JUNTADA.name()
                            .equals(getDefinicaoDeTarefaCorrente().getParam2())) {
                return "Este workflow prosseguirá automaticamente quando o documento " + siglaDoDocumentoCriado
                        + " for juntado ao documento " + getPrincipal() + ".";
            } else if (getDefinicaoDeTarefaCorrente().getTipoDeTarefa() == WfTipoDeTarefa.AUTUAR_DOCUMENTO
                    && WfTarefaDocCriarParam2.AGUARDAR_JUNTADA.name()
                            .equals(getDefinicaoDeTarefaCorrente().getParam2())) {
                return "Este workflow prosseguirá automaticamente quando o documento " + getPrincipal()
                        + " for juntado ao documento " + siglaDoDocumentoCriado + ".";
            }
        }
        return null;
    }

    public List<String> getTags() {
        if (getCurrentTaskDefinition() != null)
            return getCurrentTaskDefinition().getTags();
        return new ArrayList<String>();
    }

    public String getAncora() {
        if (getCurrentTaskDefinition() != null)
            return getCurrentTaskDefinition().getAncora();
        return null;
    }

    public Date getEventoData() {
        return eventoData;
    }

    public void setEventoData(Date eventoData) {
        this.eventoData = eventoData;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public DpPessoa getEventoPessoa() {
        return eventoPessoa;
    }

    public void setEventoPessoa(DpPessoa eventoPessoa) {
        this.eventoPessoa = eventoPessoa;
    }

    public DpLotacao getEventoLotacao() {
        return eventoLotacao;
    }

    public void setEventoLotacao(DpLotacao eventoLotacao) {
        this.eventoLotacao = eventoLotacao;
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

    public Date getHisDtIni() {
        return hisDtIni;
    }

    public void setHisDtIni(Date hisDtIni) {
        this.hisDtIni = hisDtIni;
    }

    public Date getHisDtFim() {
        return hisDtFim;
    }

    public void setHisDtFim(Date hisDtFim) {
        this.hisDtFim = hisDtFim;
    }

    public CpIdentidade getHisIdcIni() {
        return hisIdcIni;
    }

    public void setHisIdcIni(CpIdentidade hisIdcIni) {
        this.hisIdcIni = hisIdcIni;
    }

    @Override
    public Date getDtEvent() {
        return getEventoData();
    }

    @Override
    public String getIdEvent() {
        return getEventoNome();
    }

    @Override
    public String getDescricao() {
        return getDefinicaoDeProcedimento().getDescricao()
                + (getDefinicaoDeTarefaCorrente() != null ? " - " + getDefinicaoDeTarefaCorrente().getNome() : "");
    }

    public String obterProximoResponsavel() {
        return WfDefinicaoDeDesvio.obterProximoResponsavel(this, null);
    }

    @Override
    public int compareTo(WfProcedimento o) {
        int i = getPrioridade().compareTo(o.getPrioridade());
        if (i != 0)
            return i;
        i = getHisDtIni().compareTo(o.getHisDtIni());
        if (i != 0)
            return i;
        return getId().compareTo(o.getId());
    }

    public Set<WfMarca> getMarcas() {
        return marcas;
    }

    public void setMarcas(Set<WfMarca> marcas) {
        this.marcas = marcas;
    }

    public Date getDtInicioDaTarefa() {
        Date dt = getDtEvent();
        if (dt == null)
            return null;
        for (WfMov mov : getMovimentacoes()) {
            if (!mov.isAtivo())
                continue;
            if (mov instanceof WfMovTransicao && dt.before(mov.getHisDtIni()))
                dt = mov.getHisDtIni();
        }
        return dt;
    }

    public String getDuracaoDaTarefa() {
        if (isFinalizado())
            return null;
        return DateUtils.esperaSimples(getDtInicioDaTarefa());
    }

    public String getDuracaoDoProcedimento() {
        if (!isFinalizado())
            return DateUtils.esperaSimples(getHisDtIni());
        return DateUtils.intervalo(getHisDtFim(), getHisDtIni());
    }

    public String getStatusDescr() {
        switch (getStatus()) {
            case FINISHED:
                return "Concluído";
            case INACTIVE:
                return "Inativo";
            case PAUSED:
                return "Pausado";
            case RESUMING:
                return "Continuando";
            case STARTED:
                return "Iniciado";
        }
        return getStatus().name();
    }

    public WfProcedimento getSubprocedimento() {
        if (getDefinicaoDeTarefaCorrente() == null)
            return null;
        if (getDefinicaoDeTarefaCorrente().getTipoDeTarefa() != WfTipoDeTarefa.SUBPROCEDIMENTO)
            return null;
        String siglaDoSubprocedimento = WfTarefaSubprocedimento.getSiglaDoSubprocedimentoCriado(this);
        WfProcedimento piSub;
        try {
            piSub = findBySigla(siglaDoSubprocedimento);
            return piSub;
        } catch (Exception e) {
            throw new RuntimeException("Erro obtendo subprocedimento " + siglaDoSubprocedimento, e);
        }
    }

    public String getTarefaCorrenteDescr() {
        return getTarefaCorrenteDescr(false);
    }
    
    public String getTarefaCorrenteDescrHtml() {
        return getTarefaCorrenteDescr(true);
    }
    
    public String getTarefaCorrenteDescr(boolean html) {
        if (getDefinicaoDeTarefaCorrente() == null)
            return null;
        String s = getDefinicaoDeTarefaCorrente().getNome();
        WfProcedimento piSub = getSubprocedimento();
        if (piSub != null && piSub.getDefinicaoDeTarefaCorrente() != null) {
            s += " - ";
            if (html)
                s += "<a href=\"/sigawf/app/procedimento/" + piSub.getId() + "\">";
            s += piSub.getDefinicaoDeTarefaCorrente().getNome();
            if (html)
                s += "</a>";
        }
        return s;
    }

    public static class SiglaAtendente {
        private String sigla;
        private String siglaPessoa;
        private String siglaLotacao;

        SiglaAtendente(WfProcedimento pi) {
            siglaPessoa = pi.getEventoPessoa() != null ? pi.getEventoPessoa().getSigla() : null;
            siglaLotacao = pi.getEventoLotacao() != null ? pi.getEventoLotacao().getSigla() : null;
            if (siglaPessoa != null)
                sigla = siglaPessoa;
            else if (siglaLotacao != null)
                sigla = siglaLotacao;
        }

        public String getSigla() {
            return sigla;
        }

        public String getSiglaPessoa() {
            return siglaPessoa;
        }

        public String getSiglaLotacao() {
            return siglaLotacao;
        }
    }

    public SiglaAtendente getAtendenteDescr() {
        WfProcedimento piSub = getSubprocedimento();
        if (piSub != null)
            return new SiglaAtendente(piSub);
        return new SiglaAtendente(this);
    }

}