package models;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.JPA;
import util.FieldNameExclusionEstrategy;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "SR_PESQUISA", schema = "SIGASR")
public class SrPesquisa extends HistoricoSuporte {

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;

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

	@OneToMany(targetEntity = SrPesquisa.class, mappedBy = "pesquisaInicial", fetch=FetchType.LAZY)
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
	
	public static List<SrPesquisa> listar(boolean mostrarDesativados) {
		if (!mostrarDesativados) {
			return SrPesquisa.find("byHisDtFimIsNull").fetch();
		} else {
			StringBuilder str = new StringBuilder();
			str.append("SELECT p FROM SrPesquisa p where p.idPesquisa IN (");
			str.append("SELECT MAX(idPesquisa) FROM SrPesquisa GROUP BY hisIdIni)");
			
			return JPA.em()
					.createQuery(str.toString())
					.getResultList();
		}
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
	
	public String toJson() {
		Gson gson = createGson("meuPesquisaHistoricoSet", "perguntaSet", "pesquisaInicial");
		
		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
		jsonObject.add("perguntasSet", perguntasArray());
		
		return jsonObject.toString();
	}
	
	private JsonArray perguntasArray() {
		Gson gson = createGson("pesquisa", "perguntaInicial", "meuPerguntaHistoricoSet");
		JsonArray jsonArray = new JsonArray();

		for (SrPergunta srPergunta : this.perguntaSet) {
			jsonArray.add(gson.toJsonTree(srPergunta));
		}
		return jsonArray;
	}
	
	// TODO: colocar esse metodo na classe base
	private Gson createGson(String... exclusions) {
		return new GsonBuilder()
			.addSerializationExclusionStrategy(FieldNameExclusionEstrategy.notIn(exclusions))
			.create();
	}
	
	public SrPesquisa atualizarTiposPerguntas() {
		for (SrPergunta srPergunta : this.perguntaSet) {
			srPergunta.tipoPergunta = SrTipoPergunta.findById(srPergunta.tipoPergunta.idTipoPergunta);
		}
		return this;
	}
}
