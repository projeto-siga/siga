package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
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
@Table(name="SR_LISTA", schema="SIGASR")
public class SrLista extends HistoricoSuporte {
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_LISTA_SEQ", name = "srListaSeq")
	@GeneratedValue(generator = "srListaSeq")
	@Column(name = "ID_LISTA")
	public long idLista;
	
	@Column(name = "NOME_LISTA")
	public String nomeLista;
	
	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;
	
	//public void setHisDtFim(Date hisDtFim);
	
	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	public DpLotacao lotaCadastrante;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "lista", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	public SrLista(){
		
	}
	
	public SrLista(SrLista lista){
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
	
	public Long setSolicOrd(){
		SrLista lista = SrLista.findById(idLista);
		//List<SrMovimentacao> mov = SrMovimentacao.find("lista= " + idLista + " and dtCancelamento is null").fetch();
		Long prioridade = (long) (lista.getMovimentacaoSet(false).size()+1);
		//Long prioridade = (long) (mov.size()+1);
		return prioridade;
	}
	
	public Long getSolicOrd(SrSolicitacao solicitacao){
		SrMovimentacao mov = SrMovimentacao.find("lista= " + idLista + " and dtCancelamento is null and solicitacao = " 
				+ solicitacao.idSolicitacao).first();
		return mov.prioridade;
	}
	
	/*public TreeSet<SrSolicitacao> getSolicitacaoAssociada() {
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao a1, SrSolicitacao a2) {
						return a2.getMovimentacaoSet(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA).iterator().next().prioridade.compareTo(a1.getMovimentacaoSet(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA).iterator().next().prioridade);
					}
				});
		List<SrMovimentacao> mov = SrMovimentacao.find("lista= " + idLista + " and dtCancelamento is null").fetch();
		try {
			if (mov != null) {
				for (SrMovimentacao movim : mov) {
					listaCompleta.add(movim.solicitacao);
				}
			}
		} catch (Exception e) {
				e.printStackTrace();
			}	
		return listaCompleta;
	}*/
	
	public boolean isEmpty() throws Exception {
		if (getSolicSet() != null)
			return false;
		return true;
	}
	
	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return true;
	}
	
	public Long getPriorAssociada() throws Exception {
		
		SrMovimentacao movimentacao  = new SrMovimentacao();
		for (SrMovimentacao movs : getMovimentacaoListaSet(this, (SrTipoMovimentacao) SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA)))
			if (movs.dtCancelamento == null){
				if (movs.prioridade != null){
					
				}
				else
					movimentacao.prioridade = 1L;
			}
		return movimentacao.prioridade;
	}
	
	public TreeSet<SrSolicitacao> getSolicSet() throws Exception {
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao a1, SrSolicitacao a2) {
						return a2.getMovimentacaoSet(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA).iterator().next().prioridade.compareTo(a1.getMovimentacaoSet(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA).iterator().next().prioridade);
					}
				});
		
		for (SrMovimentacao movs : getMovimentacaoSet())
			if (movs.dtCancelamento == null){
				listaCompleta.add(movs.solicitacao);
			}
		return listaCompleta;
	}


	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false);
	}
	
	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados) {
		SrLista lista = SrLista.findById(idLista);
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrMovimentacao movimentacao : lista.meuMovimentacaoSet)
			if (movimentacao.lista.meuMovimentacaoSet != null)
				if (considerarCancelados)
					listaCompleta.addAll(movimentacao.lista.meuMovimentacaoSet);
				else
					for (SrMovimentacao movim : movimentacao.lista.meuMovimentacaoSet)
						if (!movim.isCancelado())
							listaCompleta.add(movim);
		return listaCompleta;
	}

	public Set<SrMovimentacao> getMovimentacaoListaSet(SrLista lista, SrTipoMovimentacao tipomov) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		List<SrMovimentacao> mov = SrMovimentacao.find("lista= " + lista.idLista + " and dtCancelamento is null and tipomov = " + tipomov.idTipoMov).fetch();
		for (SrMovimentacao movim : mov)
		{
			listaCompleta.add((SrMovimentacao) movim);
		}
		return listaCompleta;
	}

	public void recalcularPrioridade(Long idLista) throws Exception {
		
		SrLista lista = new SrLista();
		//Long prioridade = lista.getSolicOrd();
		Long prioridade = lista.setSolicOrd();
		
	}
	
	
}
