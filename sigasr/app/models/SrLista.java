package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_LISTA", schema = "SIGASR")
public class SrLista extends HistoricoSuporte {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_LISTA_SEQ", name = "srListaSeq")
	@GeneratedValue(generator = "srListaSeq")
	@Column(name = "ID_LISTA")
	public Long idLista;

	@Column(name = "NOME_LISTA")
	public String nomeLista;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

	// public void setHisDtFim(Date hisDtFim);

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	public DpLotacao lotaCadastrante;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "lista", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrLista listaInicial;

	@OneToMany(targetEntity = SrLista.class, mappedBy = "listaInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrLista> meuListaHistoricoSet;

	public SrLista() {

	}

	public SrLista(SrLista lista) {
		this.nomeLista = lista.nomeLista;
		this.lotaCadastrante = lista.lotaCadastrante;
	}

	public static List<SrLista> listar() {
		return SrLista.find("dtReg is null order by idLista").fetch();
	}

	public Long getId() {
		return this.idLista;
	}

	public void setId(Long id) {
		idLista = id;
	}

	public DpLotacao getlotaCadastrante() {
		return this.lotaCadastrante;
	}

	public String getNome() {
		return this.nomeLista;
	}

	public void finalizar() {
		this.dtReg = new Date();
		this.save();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrLista> getListaSet() {
		List<SrLista> listas = SrLista.find("dtReg is null").fetch();
		return listas;
	}

	public Long getProximaPosicao() {
		Long prioridade = (long) (getMovimentacaoSet(false,
				SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA).size() + 1);
		return prioridade;
	}

	public boolean isEmpty() throws Exception {
		if (getSolicSet() != null)
			return false;
		return true;
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return true;
	}

	public TreeSet<SrSolicitacao> getSolicSet() throws Exception {
		/*
		 * A comparação não mais se aplica porque a prioridade válida no momento
		 * pode estar tanto na movimentação de inclusão quanto na de alteração
		 * de prioridade.
		 * 
		 * TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
		 * new Comparator<SrSolicitacao>() {
		 * 
		 * @Override public int compare(SrSolicitacao a1, SrSolicitacao a2) {
		 * return a1 .getMovimentacaoSet( false,
		 * SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA)
		 * .iterator().next().prioridade .compareTo(a2 .getMovimentacaoSet(
		 * false, SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA)
		 * .iterator().next().prioridade); } });
		 */

		/*
		 * A exibição das solicitações da lista deve ser por ordem de
		 * prioridade.
		 */
		final SrLista lista = SrLista.findById(idLista);
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao a1, SrSolicitacao a2) {
						try {
							return a1.getMovPrioridade(lista).prioridade
									.compareTo(a2.getMovPrioridade(lista).prioridade);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return 0;
					}
				});
		for (SrMovimentacao mov : getPrioridadeListaSet(false)) {
			SrMovimentacao movIncl = mov.solicitacao
					.getMovimentacaoInclusao(this);
			SrMovimentacao movAltAnt = mov.solicitacao
					.getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA);
			/*
			 * Montar lista com as solicitações com prioridade maior que a
			 * prioridade da solicitação removida da lista.
			 */
			if (movAltAnt != null) {
				listaCompleta.add(mov.solicitacao);
			} else {
				if (movIncl != null) {
					listaCompleta.add(mov.solicitacao);
				}
			}
		}
		return listaCompleta;
	}

	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados) {

		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrLista listas : getHistoricoLista())
			if (listas.meuMovimentacaoSet != null)
				for (SrMovimentacao movimentacao : listas.meuMovimentacaoSet)
					if (!movimentacao.isCancelada() || considerarCancelados)
						listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public List<SrLista> getHistoricoLista() {
		if (listaInicial != null)
			return listaInicial.meuListaHistoricoSet;
		return null;
	}

	public SrLista getListaAtual() {
		List<SrLista> listas = getHistoricoLista();
		if (listas == null)
			return null;
		return listas.get(0);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados,
			Long tipoMov) {
		if (listaInicial == null)
			return null;
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrLista listas : getHistoricoLista())
			if (listas.meuMovimentacaoSet != null)
				for (SrMovimentacao movimentacao : listas.meuMovimentacaoSet)
					if ((!movimentacao.isCancelada() || considerarCancelados)
							&& (tipoMov == null || movimentacao.tipoMov.idTipoMov == tipoMov)
					// && (movimentacao.movReversora == null)
					)
						listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public Set<SrMovimentacao> getPrioridadeListaSet(
			boolean considerarCancelados) {
		if (listaInicial == null)
			return null;
		ArrayList<SrSolicitacao> solic = new ArrayList<SrSolicitacao>();
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrLista listas : getHistoricoLista())
			if (listas.meuMovimentacaoSet != null)
				for (SrMovimentacao movimentacao : listas.meuMovimentacaoSet)
					if (movimentacao.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA) {
						solic.add(movimentacao.solicitacao);
					} else if ((!movimentacao.isCancelada() || considerarCancelados)
							&& (movimentacao.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA || movimentacao.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA)
							&& (!solic.contains(movimentacao.solicitacao)))
						listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoPorTipo(Long idTpMov) {
		for (SrMovimentacao movimentacao : getMovimentacaoSet(false, idTpMov))
			return movimentacao;
		return null;
	}

	public void recalcularPrioridade(Long prioremov) throws Exception {
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao a1, SrSolicitacao a2) {
						return a1.meuMovimentacaoSet.iterator().next().prioridade
								.compareTo(a2.meuMovimentacaoSet.iterator()
										.next().prioridade);
					}
				});
		for (SrMovimentacao mov : getPrioridadeListaSet(false)) {
			SrMovimentacao movIncl = mov.solicitacao
					.getMovimentacaoInclusao(this);
			SrMovimentacao movAltAnt = mov.solicitacao
					.getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA);
			/*
			 * Montar lista com as solicitações com prioridade maior que a
			 * prioridade da solicitação removida da lista.
			 */
			if (movAltAnt != null) {
				if (movAltAnt.prioridade > prioremov) {
					listaCompleta.add(mov.solicitacao);
				} else if (movAltAnt.prioridade == prioremov) {
					;
				}
			} else if (movIncl != null) {
				if (movIncl.prioridade > prioremov)
					listaCompleta.add(mov.solicitacao);
			}
		}

		if (listaCompleta.size() > 0) {
			Iterator itr = listaCompleta.iterator();
			Long i = prioremov;
			while (itr.hasNext()) {
				SrSolicitacao solic = (SrSolicitacao) itr.next();
				solic.getPrioridade(this);
				SrMovimentacao movIncl = solic.getMovimentacaoInclusao(this);
				SrMovimentacao movAltAnt = solic
						.getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA);
				if (movAltAnt != null) {
					movAltAnt.prioridade = i;
					movAltAnt.salvar();
					solic.meuMovimentacaoSet.add(movAltAnt);
				} else if (movIncl != null) {
					movIncl.prioridade = i;
					movIncl.salvar();
					solic.meuMovimentacaoSet.add(movIncl);
				}
				i++;
			}
		} 
	}

	public void priorizar(ArrayList ids, final Long id, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {

		final SrLista lista = SrLista.findById(id);
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao a1, SrSolicitacao a2) {
						try {
							return a1.getMovPrioridade(lista).prioridade
									.compareTo(a2.getMovPrioridade(lista).prioridade);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return 0;
					}
				});
		for (SrMovimentacao mov : getPrioridadeListaSet(false)) {
			listaCompleta.add(mov.solicitacao);
		}
		Iterator itr = listaCompleta.iterator();
		int i = 0;
		while (itr.hasNext()) {
			SrSolicitacao solic = (SrSolicitacao) itr.next();
			Long idsol = Long.valueOf((String) ids.get(i));
			if (!solic.idSolicitacao.equals(idsol)) {
				SrMovimentacao movAlt = new SrMovimentacao();
				movAlt.solicitacao = SrSolicitacao.findById(idsol);
				movAlt.prioridade = (long) i + 1;
				movAlt.lista = SrLista.findById(id);
				movAlt.tipoMov = SrTipoMovimentacao
						.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA);
				movAlt.descrMovimentacao = "Alteração de prioridade em lista";
				movAlt.cadastrante = cadastrante;
				movAlt.lotaCadastrante = lotaCadastrante;
				movAlt.salvar();
			}
			i++;
		}
	}
}
