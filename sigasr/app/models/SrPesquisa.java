package models;

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

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_PESQUISA", schema = "SIGASR")
public class SrPesquisa extends HistoricoSuporte {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_PESQUISA_SEQ", name = "srPesquisaSeq")
	@GeneratedValue(generator = "srPesquisaSeq")
	@Column(name = "ID_PESQUISA")
	public Long idPesquisa;

	@Column(name = "NOME_PESQUISA")
	public String nomePesquisa;

	@Column(name = "DESCR_PESQUISA")
	public String descrPesquisa;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrPesquisa pesquisaInicial;

	@OneToMany(targetEntity = SrPesquisa.class, mappedBy = "pesquisaInicial", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrPesquisa> meuPesquisaHistoricoSet;

	@OneToMany(targetEntity = SrPergunta.class, mappedBy = "pesquisa", fetch=FetchType.LAZY)
	@OrderBy("ordemPergunta")
	public Set<SrPergunta> perguntaSet;

	public SrPesquisa() {

	}

	public Long getId() {
		return this.idPesquisa;
	}

	public void setId(Long id) {
		idPesquisa = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrPesquisa> getHistoricoPesquisa() {
		if (pesquisaInicial != null)
			return pesquisaInicial.meuPesquisaHistoricoSet;
		return null;
	}

	public SrPesquisa getPesquisaAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrPesquisa> pesquisas = getHistoricoPesquisa();
		if (pesquisas == null)
			return null;
		return pesquisas.get(0);
	}

	public static List<SrPesquisa> listar() {
		return SrPesquisa.find("byHisDtFimIsNull").fetch();
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void salvar() throws Exception {
		super.salvar();
		if (perguntaSet != null)
			for (SrPergunta pergunta : perguntaSet) {
				pergunta.pesquisa = this;
				pergunta.salvar();
			}
	}

	public Set<SrPergunta> getPerguntaSetAtivas() {
		if (pesquisaInicial == null)
			return null;
		TreeSet<SrPergunta> listaCompleta = new TreeSet<SrPergunta>(
				new Comparator<SrPergunta>() {
					@Override
					public int compare(SrPergunta a1, SrPergunta a2) {
						return a1.ordemPergunta.compareTo(a2.ordemPergunta);
					}
				});
		for (SrPesquisa pesquisa : getHistoricoPesquisa())
			if (pesquisa.meuPesquisaHistoricoSet != null)
				if (pesquisa.perguntaSet != null)
					for (SrPergunta perg : pesquisa.perguntaSet)
						if (perg.getHisDtFim() == null)
							listaCompleta.add(perg);
		return listaCompleta;
	}

	public String getPergunta(Long idPergunta) {
		SrPergunta pergunta = SrPergunta.findById(idPergunta);
		return pergunta.descrPergunta;
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void finalizar() throws Exception {
		super.finalizar();
		if (perguntaSet != null)
			for (SrPergunta pergunta : perguntaSet) {
				pergunta.finalizar();
			}
	}
}
