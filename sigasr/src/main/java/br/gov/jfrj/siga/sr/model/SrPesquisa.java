package br.gov.jfrj.siga.sr.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.util.Util;
import br.gov.jfrj.siga.vraptor.entity.HistoricoSuporteVraptor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.emory.mathcs.backport.java.util.Collections;

@Entity
@Table(name = "SR_PESQUISA", schema = Catalogs.SIGASR)
public class SrPesquisa extends HistoricoSuporteVraptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3775609274483552051L;

	public static final ActiveRecord<SrPesquisa> AR = new ActiveRecord<>(SrPesquisa.class);

	@Id
	@SequenceGenerator(sequenceName = Catalogs.SIGASR +".SR_PESQUISA_SEQ", name = "srPesquisaSeq")
	@GeneratedValue(generator = "srPesquisaSeq")
	@Column(name = "ID_PESQUISA")
	private Long idPesquisa;

	@Column(name = "NOME_PESQUISA")
	private String nomePesquisa;

	@Column(name = "DESCR_PESQUISA")
	private String descrPesquisa;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrPesquisa pesquisaInicial;

	@OneToMany(targetEntity = SrPesquisa.class, mappedBy = "pesquisaInicial", fetch=FetchType.LAZY)
	private List<SrPesquisa> meuPesquisaHistoricoSet;

	@OneToMany(targetEntity = SrPergunta.class, mappedBy = "pesquisa", fetch=FetchType.LAZY)
	private Set<SrPergunta> perguntaSet;


	public SrPesquisa() {
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<SrPesquisa> getHistoricoPesquisa() {
		if (pesquisaInicial != null)
			return pesquisaInicial.meuPesquisaHistoricoSet;
		
		return Collections.emptyList();
	}

	public SrPesquisa getPesquisaAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrPesquisa> pesquisas = getHistoricoPesquisa();
		if (pesquisas == null)
			return null;
		return pesquisas.get(0);
	}

	@SuppressWarnings("unchecked")
	public static List<SrPesquisa> listar(boolean mostrarDesativados) {
		if (!mostrarDesativados) {
			return SrPesquisa.AR.find("byHisDtFimIsNull").fetch();
		} else {
			StringBuilder str = new StringBuilder();
			str.append("SELECT p FROM SrPesquisa p where p.idPesquisa IN (");
			str.append("SELECT MAX(idPesquisa) FROM SrPesquisa GROUP BY hisIdIni)");

			return AR.em().createQuery(str.toString()).getResultList();

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

	@SuppressWarnings("unchecked")
	public Set<SrPergunta> getPerguntaSetAtivas() {
		if (pesquisaInicial == null)
			return Collections.emptySet();

		TreeSet<SrPergunta> listaCompleta = new TreeSet<SrPergunta>(
				new Comparator<SrPergunta>() {
					@Override
					public int compare(SrPergunta a1, SrPergunta a2) {
						return a1.ordemPergunta.compareTo(a2.ordemPergunta);
					}
				});
		for (SrPesquisa pesquisa : getHistoricoPesquisa())
			if (pesquisa.meuPesquisaHistoricoSet != null && pesquisa.perguntaSet != null)
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
		return toJson(false);
	}

	public String toJson(boolean listarAssociacoes) {
		Gson gson = Util.createGson("meuPesquisaHistoricoSet", "perguntaSet", "pesquisaInicial");

		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
		jsonObject.add("perguntasSet", perguntasArray());
		jsonObject.add("associacoesVO", getAssociacoesJson(listarAssociacoes));

		return jsonObject.toString();
	}

	private JsonArray perguntasArray() {
		Gson gson = Util.createGson("pesquisa", "perguntaInicial", "meuPerguntaHistoricoSet");
		JsonArray jsonArray = new JsonArray();

		for (SrPergunta srPergunta : this.perguntaSet) {
			jsonArray.add(gson.toJsonTree(srPergunta));
		}
		return jsonArray;
	}

	public SrPesquisa atualizarTiposPerguntas() throws Exception {
		for (SrPergunta srPergunta : this.perguntaSet) {
			srPergunta.tipoPergunta = SrTipoPergunta.AR.findById(srPergunta.tipoPergunta.getIdTipoPergunta());
		}
		return this;
	}

	private JsonArray getAssociacoesJson(boolean listarAssociacoes) {
		Gson gson = Util.createGson("");
		JsonArray jsonArray = new JsonArray();

		if (listarAssociacoes) {
			List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesPesquisa(this, Boolean.FALSE);

			if (associacoes != null) {
				for (SrConfiguracao conf : associacoes) {
					jsonArray.add(gson.toJsonTree(conf.toVO()));
				}
			}
		}

		return jsonArray;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getDescrPesquisa() {
		return descrPesquisa;
	}

	public void setDescrPesquisa(String descrPesquisa) {
		this.descrPesquisa = descrPesquisa;
	}

	public SrPesquisa getPesquisaInicial() {
		return pesquisaInicial;
	}

	public void setPesquisaInicial(SrPesquisa pesquisaInicial) {
		this.pesquisaInicial = pesquisaInicial;
	}

	public List<SrPesquisa> getMeuPesquisaHistoricoSet() {
		return meuPesquisaHistoricoSet;
	}

	public void setMeuPesquisaHistoricoSet(List<SrPesquisa> meuPesquisaHistoricoSet) {
		this.meuPesquisaHistoricoSet = meuPesquisaHistoricoSet;
	}

	public Set<SrPergunta> getPerguntaSet() {
		return perguntaSet;
	}

	public void setPerguntaSet(Set<SrPergunta> perguntaSet) {
		this.perguntaSet = perguntaSet;
	}

	@Override
	public Long getId() {
		return getIdPesquisa();
	}

	@Override
	public void setId(Long idPesquisa) {
		this.setIdPesquisa(idPesquisa);
	}

	public Long getIdPesquisa() {
		return idPesquisa;
	}

	public void setIdPesquisa(Long idPesquisa) {
		this.idPesquisa = idPesquisa;
	}

}
