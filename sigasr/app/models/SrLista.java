package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Historico;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="SR_LISTA", schema="SIGASR")
public class SrLista extends GenericModel {
	
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

	public List<SrLista> getListaSet() {
		List<SrLista> listas =  SrLista.findAll();
		return listas;
	}
	
	public HashSet<SrSolicitacao> getSolicitacaoAssociada() {
		HashSet<SrSolicitacao> listaCompleta = new HashSet<SrSolicitacao>();
		List<SrMovimentacao> mov = SrMovimentacao.find("lista= " + idLista + " and dtCancelamento is null").fetch();
		for (SrMovimentacao movim : mov)
		{
			listaCompleta.add(movim.solicitacao);
		}
		return listaCompleta;
	}
	
	public boolean isEmpty() {
		if (getSolicitacaoAssociada() != null) 
			return false;
		return true;
	}
	
	public Long getPriorAssociada(SrLista lista) throws Exception {
		
		SrMovimentacao movimentacao  = new SrMovimentacao();
		for (SrMovimentacao movs : getMovimentacaoListaSet(lista, (SrTipoMovimentacao) SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA)))
			if (movs.dtCancelamento == null){
				if (movs.prioridade != null){
					
				}
				else
					movimentacao.prioridade = 1L;
			}
			
		return movimentacao.prioridade;
			
		
	}

	public Set<SrMovimentacao> getMovimentacaoListaSet(SrLista lista) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		List<SrMovimentacao> mov = SrMovimentacao.find("lista= " + lista.idLista + " and dtCancelamento is null").fetch();
		for (SrMovimentacao movim : mov)
		{
			listaCompleta.add((SrMovimentacao) movim);
		}
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
	
	
}
