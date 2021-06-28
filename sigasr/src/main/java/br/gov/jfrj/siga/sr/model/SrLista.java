package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.SrListaVO;

@Entity
@Table(name = "sr_lista", schema = "sigasr")
public class SrLista extends HistoricoSuporte implements Comparable<SrLista> {

    private static final long serialVersionUID = 1L;

    public static final ActiveRecord<SrLista> AR = new ActiveRecord<>(SrLista.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_LISTA_SEQ", name = "srListaSeq")
    @GeneratedValue(generator = "srListaSeq")
    @Column(name = "ID_LISTA")
    private Long idLista;

	@Column(name = "NOME_LISTA")
    private String nomeLista;

    @Lob
    @Column(name = "DESCR_ABRANGENCIA", length = 8192)
    private String descrAbrangencia;

    @Lob
    @Column(name = "DESCR_JUSTIFICATIVA", length = 8192)
    private String descrJustificativa;

    @Lob
    @Column(name = "DESCR_PRIORIZACAO", length = 8192)
    private String descrPriorizacao;

    @ManyToOne
    @JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
    private DpLotacao lotaCadastrante;

    @OneToMany(targetEntity = SrPrioridadeSolicitacao.class, mappedBy = "lista", fetch = FetchType.LAZY)
    private Set<SrPrioridadeSolicitacao> meuPrioridadeSolicitacaoSet;

    @ManyToOne()
    @JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
    private SrLista listaInicial;

    @OneToMany(targetEntity = SrLista.class, mappedBy = "listaInicial", fetch = FetchType.LAZY)
    @OrderBy("idLista DESC")
    private List<SrLista> meuListaHistoricoSet;

    public static List<SrLista> listar(boolean mostrarDesativado) {
        StringBuilder sb = new StringBuilder();

        if (!mostrarDesativado)
            sb.append(" hisDtFim is null ");
        else {
            sb.append(" idLista in (");
            sb.append(" SELECT max(idLista) as idLista FROM ");
            sb.append(" SrLista GROUP BY hisIdIni) ");
        }

        sb.append(" order by idLista ");

        return SrLista.AR.find(sb.toString()).fetch();
    }

    public static List<SrLista> getCriadasPelaLotacao(DpLotacao lota) {
        return SrLista.AR.find("hisDtFim is null and lotaCadastrante.idLotacaoIni = " + lota.getIdLotacaoIni()).fetch();
    }

    @Override
    public Long getId() {
        return this.getIdLista();
    }

    public void setId(Long id) {
        setIdLista(id);
    }

    @Override
    public boolean semelhante(Assemelhavel obj, int profundidade) {
        return false;
    }

    public List<SrLista> getHistoricoLista() {
        if (getListaInicial() != null)
            return getListaInicial().getMeuListaHistoricoSet();
        return null;
    }

    public SrLista getListaAtual() {
        if (getHisDtFim() == null)
            return this;
        List<SrLista> listas = getHistoricoLista();
        if (listas == null)
            return null;
        return listas.get(0);
    }

    public Set<SrPrioridadeSolicitacao> getPrioridadeSolicitacaoSet() {
        return getPrioridadeSolicitacaoSet(true);
    }

    public Set<SrPrioridadeSolicitacao> getPrioridadeSolicitacaoSetOrdemCrescente() {
        return getPrioridadeSolicitacaoSet(true);
    }

    public Set<SrPrioridadeSolicitacao> getPrioridadeSolicitacaoSet(boolean ordemCrescente) {
        TreeSet<SrPrioridadeSolicitacao> listaCompleta = new TreeSet<SrPrioridadeSolicitacao>(new SrPrioridadeSolicitacaoComparator(ordemCrescente));
        if (getListaInicial() != null)
            for (SrLista lista : getHistoricoLista())
                if (lista.getMeuPrioridadeSolicitacaoSet() != null)
                    for (SrPrioridadeSolicitacao prioridadeSolicitacao : lista.getMeuPrioridadeSolicitacaoSet())
                        listaCompleta.add(prioridadeSolicitacao);
        return listaCompleta;
    }

    public boolean podeEditar(DpLotacao lotaTitular, DpPessoa pess) {
        return (lotaTitular.equivale(getLotaCadastrante())) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.GESTAO);
    }

    public boolean podeIncluir(DpLotacao lotaTitular, DpPessoa pess) {
        return (lotaTitular.equivale(getLotaCadastrante())) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.INCLUSAO);
    }

    public boolean podeConsultar(DpLotacao lotaTitular, DpPessoa pess) {
        return (lotaTitular.equivale(getLotaCadastrante())) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.CONSULTA);
    }

    public boolean podeRemover(DpLotacao lotaTitular, DpPessoa pess) throws Exception {
        return (lotaTitular.equivale(getLotaCadastrante())) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.GESTAO);
    }

    public boolean podePriorizar(DpLotacao lotaTitular, DpPessoa pess) throws Exception {
        return (lotaTitular.equivale(getLotaCadastrante())) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.PRIORIZACAO);
    }

    private boolean possuiPermissao(DpLotacao lotaTitular, DpPessoa pess, Long tipoPermissaoLista) {
        List<SrConfiguracaoCache> permissoesEncontradas = getPermissoesDoCache(lotaTitular, pess);
        for (SrConfiguracaoCache srConfiguracao : permissoesEncontradas) {
            for (SrTipoPermissaoLista permissao : srConfiguracao.tipoPermissaoSet) {
                if (tipoPermissaoLista == permissao.getIdTipoPermissaoLista()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public List<SrConfiguracao> getPermissoes(DpLotacao lotaTitular, DpPessoa pess) {
    	return SrConfiguracao.inflar(getPermissoesDoCache(lotaTitular, pess));
    }
    
    public List<SrConfiguracaoCache> getPermissoesDoCache(DpLotacao lotaTitular, DpPessoa pess) {
        try {
            SrConfiguracao confFiltro = new SrConfiguracao();
            confFiltro.setLotacao(lotaTitular);
            confFiltro.setDpPessoa(pess);
            confFiltro.setListaPrioridade(this);
            confFiltro.setCpTipoConfiguracao(SrTipoDeConfiguracao.PERMISSAO_USO_LISTA);
            return SrConfiguracao.listar(confFiltro);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SrConfiguracao> getPermissoes() {
        return getPermissoes(null, null);
    }

    protected long getPosicaoParaEncaixe(SrPrioridadeSolicitacao prioridadeSolicitacao) throws Exception {
        List<SrPrioridadeSolicitacao> prioridades = new ArrayList<SrPrioridadeSolicitacao>(getPrioridadeSolicitacaoSet());

        if (prioridades.isEmpty()) {
            return 1;
        }

        if (prioridadeSolicitacao.getPrioridade() != null) {

            for (int i = prioridades.size() - 1; i >= 0; i--) {
                SrPrioridadeSolicitacao prioridadeSolic = prioridades.get(i);
                if (prioridadeSolicitacao.getPrioridade().equals(prioridadeSolic.getPrioridade())) {
                    return prioridadeSolic.getNumPosicao() + 1;
                }
            }
        }
        return buscarPosicaoPorPrioridade(prioridades, prioridadeSolicitacao);
    }

    private Long buscarPosicaoPorPrioridade(List<SrPrioridadeSolicitacao> prioridades, SrPrioridadeSolicitacao prioridadeSolicitacao) {
        if (prioridades.get(prioridades.size() - 1).getNumPosicao() == null) {
            return 0L;
        }

        for (int i = 0; i <= prioridades.size() - 1; i++) {
            SrPrioridadeSolicitacao prioridadeSolic = prioridades.get(i);
            if (prioridadeSolicitacao.getPrioridade() != null) {
                if (prioridadeSolic.getPrioridade() == null || prioridadeSolicitacao.getPrioridade().getIdPrioridade() > prioridadeSolic.getPrioridade().getIdPrioridade()) {
                    return prioridadeSolic.getNumPosicao();
                }
            }
        }

        return prioridades.get(prioridades.size() - 1).getNumPosicao() + 1;
    }
    
    protected void recalcularPrioridade(DpPessoa pessoa, DpLotacao lota) throws Exception {
        Long posicao = 0L;
        for (SrPrioridadeSolicitacao prioridadeSolicitacao : getPrioridadeSolicitacaoSet()) {
            posicao++;
            if (!posicao.equals(prioridadeSolicitacao.getNumPosicao())) {
                prioridadeSolicitacao.setNumPosicao(posicao);
                prioridadeSolicitacao.save();
            }
        }
    }

    public String toJson() {
        return this.toVO().toJson();
    }

    public SrListaVO toVO() {
        return new SrListaVO(this);
    }

    public void validarPodeExibirLista(DpLotacao lotacao, DpPessoa titular) throws Exception {
        if (!podeConsultar(lotacao, titular)) {
            throw new Exception("Exibição não permitida");
        }
    }

    public SrPrioridadeSolicitacao getSrPrioridadeSolicitacao(SrSolicitacao solicitacao) {
        for (SrPrioridadeSolicitacao prioridadeSolicitacao : getPrioridadeSolicitacaoSet()) {
            if (solicitacao.getIdInicial().equals(prioridadeSolicitacao.getSolicitacao().getIdInicial())) {
                return prioridadeSolicitacao;
            }
        }
        return null;
    }

    public void incluir(SrSolicitacao solicitacao, SrPrioridade prioridade, boolean naoReposicionarAutomatico) throws Exception {
        SrPrioridadeSolicitacao prioridadeSolicitacao = new SrPrioridadeSolicitacao(this, solicitacao, prioridade, naoReposicionarAutomatico);
        long posicao = getPosicaoParaEncaixe(prioridadeSolicitacao);

        for (SrPrioridadeSolicitacao prioridadeSolic : getPrioridadeSolicitacaoSet()) {
            if (prioridadeSolic.getNumPosicao() >= posicao) {
                prioridadeSolic.incrementarPosicao();
                prioridadeSolic.save();
            }
        }
        prioridadeSolicitacao.setNumPosicao(posicao);
        getMeuPrioridadeSolicitacaoSet().add(prioridadeSolicitacao);
        prioridadeSolicitacao.save();
    }

    /**
     * Retorna um Json de {@link SrLista}.
     */
    public String getSrListaJson() {
        return this.toVO().toJson();
    }

    public void retirar(SrSolicitacao solicitacao, DpPessoa pessoa, DpLotacao lotacao) throws Exception {
        for (SrPrioridadeSolicitacao prioridadeSolicitacao : getPrioridadeSolicitacaoSet()) {
            if (prioridadeSolicitacao.getSolicitacao().getId().equals(solicitacao.getId())) {
                prioridadeSolicitacao.delete();
                excluir(prioridadeSolicitacao);
                break;
            }
        }
        recalcularPrioridade(pessoa, lotacao);
    }

    private void excluir(SrPrioridadeSolicitacao prioridadeSolicitacao) {
        for (SrLista listaHistorico : getHistoricoLista()) {
            boolean removido = listaHistorico.getMeuPrioridadeSolicitacaoSet().remove(prioridadeSolicitacao);

            if (removido) {
                break;
            }
        }
    }
    
    @SuppressWarnings("unused")
    private class SrSolicitacaoListaComparator implements Comparator<SrSolicitacao> {

        private SrLista lista;

        public SrSolicitacaoListaComparator(SrLista lista) {
            this.lista = lista;
        }

        @Override
        public int compare(SrSolicitacao s1, SrSolicitacao s2) {
            try {
                return Long.valueOf(s1.getPrioridadeNaLista(lista)).compareTo(Long.valueOf(s2.getPrioridadeNaLista(lista)));
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

    }

    public Long getIdLista() {
        return idLista;
    }

    public void setIdLista(Long idLista) {
        this.idLista = idLista;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getDescrAbrangencia() {
        return descrAbrangencia;
    }

    public void setDescrAbrangencia(String descrAbrangencia) {
        this.descrAbrangencia = descrAbrangencia;
    }

    public String getDescrJustificativa() {
        return descrJustificativa;
    }

    public void setDescrJustificativa(String descrJustificativa) {
        this.descrJustificativa = descrJustificativa;
    }

    public String getDescrPriorizacao() {
        return descrPriorizacao;
    }

    public void setDescrPriorizacao(String descrPriorizacao) {
        this.descrPriorizacao = descrPriorizacao;
    }

    public DpLotacao getLotaCadastrante() {
        return lotaCadastrante;
    }

    public void setLotaCadastrante(DpLotacao lotaCadastrante) {
        this.lotaCadastrante = lotaCadastrante;
    }

    public Set<SrPrioridadeSolicitacao> getMeuPrioridadeSolicitacaoSet() {
        return meuPrioridadeSolicitacaoSet;
    }

    public void setMeuPrioridadeSolicitacaoSet(Set<SrPrioridadeSolicitacao> meuPrioridadeSolicitacaoSet) {
        this.meuPrioridadeSolicitacaoSet = meuPrioridadeSolicitacaoSet;
    }

    public SrLista getListaInicial() {
        return listaInicial;
    }

    public void setListaInicial(SrLista listaInicial) {
        this.listaInicial = listaInicial;
    }

    public List<SrLista> getMeuListaHistoricoSet() {
        return meuListaHistoricoSet;
    }

    public void setMeuListaHistoricoSet(List<SrLista> meuListaHistoricoSet) {
        this.meuListaHistoricoSet = meuListaHistoricoSet;
    }

	@Override
	public int compareTo(SrLista o) {
		return getNomeLista().compareTo(o.getNomeLista());
	}
}