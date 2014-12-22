package models;

import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;

import java.util.Collection;
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
import javax.persistence.Transient;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_LISTA", schema = "SIGASR")
public class SrLista extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class SrSolicitacaoListaComparator implements
	Comparator<SrSolicitacao> {

		private SrLista lista;

		public SrSolicitacaoListaComparator(SrLista lista) {
			this.lista = lista;
		}

		@Override
		public int compare(SrSolicitacao s1, SrSolicitacao s2) {
			try {
				return Long.valueOf(s1.getPrioridadeNaLista(lista)).compareTo(
						Long.valueOf(s2.getPrioridadeNaLista(lista)));
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}

	}

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_LISTA_SEQ", name = "srListaSeq")
	@GeneratedValue(generator = "srListaSeq")
	@Column(name = "ID_LISTA")
	public Long idLista;

	@Column(name = "NOME_LISTA")
	public String nomeLista;

	@Lob
	@Column(name = "DESCR_ABRANGENCIA", length = 8192)
	public String descrAbrangencia;

	@Lob
	@Column(name = "DESCR_JUSTIFICATIVA", length = 8192)
	public String descrJustificativa;

	@Lob
	@Column(name = "DESCR_PRIORIZACAO", length = 8192)
	public String descrPriorizacao;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	public DpLotacao lotaCadastrante;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "lista", fetch = FetchType.LAZY)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrLista listaInicial;

	@OneToMany(targetEntity = SrLista.class, mappedBy = "listaInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrLista> meuListaHistoricoSet;

	public static List<SrLista> listar(boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer();

		if (!mostrarDesativado)
			sb.append(" hisDtFim is null ");
		else {
			sb.append(" idLista in (");
			sb.append(" SELECT max(idLista) as idLista FROM ");
			sb.append(" SrLista GROUP BY hisIdIni) ");
		}

		sb.append(" order by idLista ");

		return SrLista.find(sb.toString()).fetch();
	}

	public static List<SrLista> getCriadasPelaLotacao(DpLotacao lota) {
		return SrLista.find(
				"hisDtFim is null and lotaCadastrante.idLotacaoIni = "
						+ lota.getIdLotacaoIni()).fetch();
	}

	public Long getId() {
		return this.idLista;
	}

	public void setId(Long id) {
		idLista = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrLista> getHistoricoLista() {
		if (listaInicial != null)
			return listaInicial.meuListaHistoricoSet;
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

	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false);
	}

	public Set<SrMovimentacao> getMovimentacaoSetOrdemCrescente() {
		return getMovimentacaoSet(true);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean ordemCrescente) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new SrMovimentacaoComparator(ordemCrescente));
		if (listaInicial != null)
			for (SrLista lista : getHistoricoLista())
				if (lista.meuMovimentacaoSet != null)
					for (SrMovimentacao movimentacao : lista.meuMovimentacaoSet)
						if ((!movimentacao.isCanceladoOuCancelador()))
							listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}
	
	public boolean podeEditar(DpLotacao lotaTitular, DpPessoa pess) {
		return (lotaTitular.equals(lotaCadastrante)) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.GESTAO);
	}
	
	public boolean podeIncluir(DpLotacao lotaTitular, DpPessoa pess) {
		return (lotaTitular.equals(lotaCadastrante)) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.INCLUSAO);
	}

	public boolean podeConsultar(DpLotacao lotaTitular, DpPessoa pess) {
		return (lotaTitular.equals(lotaCadastrante)) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.CONSULTA);
	}

	public boolean podeRemover(DpLotacao lotaTitular, DpPessoa pess) throws Exception {
		return (lotaTitular.equals(lotaCadastrante)) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.GESTAO);
	}
	
	public boolean podePriorizar(DpLotacao lotaTitular, DpPessoa pess) throws Exception {
		return (lotaTitular.equals(lotaCadastrante)) || possuiPermissao(lotaTitular, pess, SrTipoPermissaoLista.PRIORIZACAO);
	}

	private boolean possuiPermissao(DpLotacao lotaTitular, DpPessoa pess, Long tipoPermissaoLista) {
		List<SrConfiguracao> permissoesEncontradas = getPermissoes(lotaTitular, pess);
		for (SrConfiguracao srConfiguracao : permissoesEncontradas) {
			for (SrTipoPermissaoLista permissao: srConfiguracao.tipoPermissaoSet) {
				if (tipoPermissaoLista == permissao.getIdTipoPermissaoLista()) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public List<SrConfiguracao> getPermissoes(DpLotacao lotaTitular, DpPessoa pess) {
		try {
			SrConfiguracao confFiltro = new SrConfiguracao();
			confFiltro.setLotacao(lotaTitular);
			confFiltro.setDpPessoa(pess);
			confFiltro.setCpTipoConfiguracao(JPA.em().find(
					CpTipoConfiguracao.class,
					CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
			return SrConfiguracao.listar(confFiltro, new int[] { SrConfiguracaoBL.LISTA_PRIORIDADE });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Set<SrSolicitacao> getSolicitacaoSet() throws Exception {
		Set<SrSolicitacao> sols = new TreeSet<SrSolicitacao>(
				new SrSolicitacaoListaComparator(this));
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA)
				sols.add(mov.solicitacao);
			else
				sols.remove(mov.solicitacao);
		}
		return sols;
	}

	public boolean isEmpty() throws Exception {
		return getSolicitacaoSet().size() > 0;
	}

	protected int getProximaPosicao() throws Exception {
		return getSolicitacaoSet().size() + 1;
	}

	public void priorizar(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			List<SrSolicitacao> sols) throws Exception {

		if (sols.size() != getSolicitacaoSet().size())
			throw new IllegalArgumentException(
					"O nímero de elementos passados ("
							+ sols.size()
							+ ") é diferente do número de solicitações existentes na lista ("
							+ getSolicitacaoSet().size() + ")");

		for (SrSolicitacao sol : sols) {
			if (!sol.isEmLista(this))
				throw new IllegalArgumentException("A solicitação "
						+ sol.getCodigo() + " não faz parte da lista");
		}

		this.recalcularPrioridade(cadastrante, lotaCadastrante, sols);
		this.refresh();
	}

	protected void recalcularPrioridade(DpPessoa pessoa, DpLotacao lota)
			throws Exception {
		recalcularPrioridade(pessoa, lota, this.getSolicitacaoSet());
	}

	private void recalcularPrioridade(DpPessoa pessoa, DpLotacao lota,
			Collection<SrSolicitacao> sols) throws Exception {
		long i = 0;
		for (SrSolicitacao s : sols) {
			i++;
			if (s.getPrioridadeNaLista(this) != i)
				s.priorizar(this, i, pessoa, lota);
		}
	}
	
	/**
	 * Classe que representa um V.O. de {@link SrLista}.
	 */
	public class SrListaVO {

		public Long idLista;
		public String nomeLista;

		public SrListaVO(Long idLista, String nomeLista) {
			this.idLista = idLista;
			this.nomeLista = nomeLista;
		}
	}

	public SrListaVO toVO() {
		return new SrListaVO(this.idLista, this.nomeLista);
	}

	public void validarPodeExibirLista(DpLotacao lotacao, DpPessoa cadastrante) throws Exception {
		if (!podeConsultar(lotacao, cadastrante)) {
			throw new Exception("Exibição não permitida");
		}
	}
}
