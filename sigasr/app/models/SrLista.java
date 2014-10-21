package models;

import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_LISTA", schema = "SIGASR")
public class SrLista extends HistoricoSuporte {

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

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	public DpLotacao lotaCadastrante;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "lista", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrLista listaInicial;

	@OneToMany(targetEntity = SrLista.class, mappedBy = "listaInicial", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrLista> meuListaHistoricoSet;

	public static List<SrLista> listar() {
		return SrLista.find("hisDtFim is null order by idLista").fetch();
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
		SrLista ini = listaInicial != null ? listaInicial
				: this;
		if (ini.meuMovimentacaoSet != null)
			for (SrMovimentacao movimentacao : ini.meuMovimentacaoSet)
				if ((!movimentacao.isCanceladoOuCancelador()))
					listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return (lota.equals(lotaCadastrante));
	}

	public boolean podePriorizar(DpLotacao lotaTitular, DpPessoa pess)
			throws Exception {
		return (lotaTitular.equals(lotaCadastrante));
	}

	public boolean podeRemover(DpLotacao lotaTitular, DpPessoa pess)
			throws Exception {
		if ((lotaTitular.equals(lotaCadastrante)))
			return true;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(lotaTitular, pess,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA, this);
		return conf != null;
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
					"O número de elementos passados ("
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

	@Override
	public void salvar() throws Exception {
		super.salvar();
		
		//Edson: comentado o codigo abaixo porque muitos problemas ocorriam. Mas
		//tem de ser corrigido.
		
		//Edson: eh necessario o refresh porque, abaixo, as configuracoes referenciando
		//serao recarregadas do banco, e precisarao reconhecer o novo estado desta lista
		//refresh();

		// Edson: soh apaga o cache de configuracoes se ja existia antes uma
		// instancia do objeto, caso contrario, nao ha configuracao
		// referenciando
		//if (listaInicial != null)
		//	SrConfiguracao.notificarQueMudou(this);
	}

}
