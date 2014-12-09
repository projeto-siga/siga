package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import util.FieldNameExclusionEstrategy;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Entity
@Table(name = "SR_ACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrAcao extends HistoricoSuporte implements SrSelecionavel, Comparable<SrAcao> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8387408543308440033L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ACAO_SEQ", name = "srAcaoSeq")
	@GeneratedValue(generator = "srAcaoSeq")
	@Column(name = "ID_ACAO")
	public Long idAcao;

	@Column(name = "SIGLA_ACAO")
	public String siglaAcao;

	@Column(name = "DESCR_ACAO")
	public String descrAcao;

	@Column(name = "TITULO_ACAO")
	public String tituloAcao;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAcao acaoInicial;
	
	@Column(name = "TIPO_ACAO")
	@Enumerated()
	public SrTipoAcao tipoAcao;
	
	@Column(name = "TIPO_EXECUCAO")
	@Enumerated()
	public SrTipoExecucaoAcao tipoExecucao;
	
	@Column(name = "FORMA_ATENDIMENTO")
	@Enumerated()
	public SrFormaAtendimentoAcao formaAtendimento;

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "acaoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrAcao> meuAcaoHistoricoSet;

	@ManyToOne()
	@JoinColumn(name = "ID_PAI")
	public SrAcao pai;

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "pai", fetch = FetchType.LAZY)
	public List<SrAcao> filhoSet;

	public SrAcao() {
		this(null, null);
	}

	public SrAcao(String descricao) {
		this(descricao, null);
	}

	public SrAcao(String sigla, String descricao) {
		this.tituloAcao = descricao;
		this.siglaAcao = sigla;
	}

	@Override
	public Long getId() {
		return this.idAcao;
	}

	public void setId(Long id) {
		idAcao = id;
	}

	@Override
	public String getSigla() {
		return this.siglaAcao;
	}

	@Override
	public String getDescricao() {
		return tituloAcao;
	}

	@Override
	public void setDescricao(String descricao) {
		this.tituloAcao = descricao;
	}

	public List<SrAcao> getHistoricoAcao() {
		if (acaoInicial != null)
			return acaoInicial.meuAcaoHistoricoSet;
		return null;
	}

	public SrAcao getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAcao> sols = getHistoricoAcao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrAcao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null);
	}

	public SrAcao selecionar(String sigla, List<SrAcao> listaBase)
			throws Exception {
		setSigla(sigla);
		List<SrAcao> itens = buscar(listaBase);
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrAcao> buscar() throws Exception {
		return buscar(null);
	}

	public List<SrAcao> buscar(List<SrAcao> listaBase) throws Exception {

		List<SrAcao> lista = new ArrayList<SrAcao>();
		List<SrAcao> listaFinal = new ArrayList<SrAcao>();

		if (listaBase == null)
			lista = listar(Boolean.FALSE);
		else
			lista = listaBase;

		if ((siglaAcao == null || siglaAcao.equals(""))
				&& (tituloAcao == null || tituloAcao.equals("")))
			return lista;

		for (SrAcao acao : lista) {
			if (siglaAcao != null && !siglaAcao.equals("")
					&& !(acao.siglaAcao.contains(getSigla())))
				continue;
			if (tituloAcao != null && !tituloAcao.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloAcao.toLowerCase().split("\\s"))
					if (!acao.tituloAcao.toLowerCase().contains(s))
						naoAtende = true;
				if (naoAtende)
					continue;
			}
			listaFinal.add(acao);
		}
		return listaFinal;
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			tituloAcao = "";
			siglaAcao = "";
		} else {
			String padrao = "([0-9][0-9]).?([0-9][0-9])";
			final Pattern p1 = Pattern.compile("^" + padrao);
			final Matcher m1 = p1.matcher(sigla);
			if (m1.find()) {
				String s = "";
				for (int i = 1; i <= m1.groupCount(); i++) {
					s += m1.group(i);
					s += (i < m1.groupCount()) ? "." : "";
				}
				siglaAcao = s;
			} else
				tituloAcao = sigla;
		}
	}

	public int getNivel() {
		int camposVazios = 0;
		int pos = getSigla().indexOf(".00", 0);
		while (pos > -1) {
			camposVazios++;
			pos = getSigla().indexOf(".00", pos + 1);
		}
		return 2 - camposVazios;
	}

	public boolean isEspecifico() {
		return getNivel() == 2;
	}

	public String getSiglaSemZeros() {
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return getSigla().substring(0, posFimComparacao + 1);
	}

	public SrAcao getPaiPorSigla() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 2 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrAcao.find("byHisDtFimIsNullAndSiglaAcao", sigla).first();
	}

	public boolean isPaiDeOuIgualA(SrAcao outraAcao) {
		if (outraAcao == null || outraAcao.getSigla() == null)
			return false;
		if (this.equals(outraAcao))
			return true;
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return outraAcao.getSigla().startsWith(
				getSigla().substring(0, posFimComparacao + 1));
	}

	public boolean isFilhoDeOuIgualA(SrAcao outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public static List<SrAcao> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();
		
		if (!mostrarDesativados)
			sb.append(" hisDtFim is null");
		else {
			sb.append(" idAcao in (");
			sb.append(" SELECT max(idAcao) as idAcao FROM ");
			sb.append(" SrAcao GROUP BY hisIdIni) ");
		}
		
		sb.append(" order by siglaAcao ");
		
		return SrAcao.find(sb.toString()).fetch();
	}

	@SuppressWarnings("unused")
	public String getGcTags() {
		int nivel = this.getNivel();
		String tags = "";
		SrAcao pai = this.pai;
		if (pai != null)
			tags += pai.getGcTags();
		return tags + "&tags=@" + getTituloSlugify();
	}

	public String getTituloSlugify() {
		return Texto.slugify(tituloAcao, true, false);
	}

	public void salvar() throws Exception {
		if (getNivel() > 1) {
			pai = getPaiPorSigla();
		}
		super.salvar();
	}

	public List<SrAcao> getAcaoETodasDescendentes() {
		List<SrAcao> lista = new ArrayList<SrAcao>();
		lista.add(this);
		for (SrAcao filho : filhoSet) {
			if (filho.getHisDtFim() == null)
				lista.addAll(filho.getAcaoETodasDescendentes());
		}
		return lista;
	}
	
	@Override
	public String toString() {
		return siglaAcao + " - " + tituloAcao;
	}

	@Override
	public int compareTo(SrAcao arg0) {
		if (arg0.descrAcao == null) {
			return -1;
		} else if (this.descrAcao == null) {
			return 0;
		}
		return this.descrAcao.compareTo(arg0.descrAcao);
	}
	
	/**
	 * Classe que representa um V.O. de {@link SrAcao}.
	 */
	public class SrAcaoVO {
		
		public Long id;
		public String titulo;
		public String sigla;
		public Long hisIdIni;
		
		public SrAcaoVO(Long id, String titulo, String sigla, Long hisIdIni) {
			this.id = id;
			this.titulo = titulo;
			this.sigla = sigla;
			this.hisIdIni = hisIdIni;
		}
	}
	
	public SrAcaoVO toVO() {
		return new SrAcaoVO(this.idAcao, this.tituloAcao, this.siglaAcao, this.getHisIdIni());
	}
	
	public String toJson() {
		Gson gson = createGson();
		
		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
		
		return jsonObject.toString();
	}
	
	private Gson createGson() {
		return new GsonBuilder()
			.addSerializationExclusionStrategy(FieldNameExclusionEstrategy.notIn("meuAcaoHistoricoSet", "filhoSet", "acaoInicial"))
			.create();
	}
}
