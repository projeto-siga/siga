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
@Table(name = "SR_TIPO_ACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrTipoAcao extends HistoricoSuporte implements SrSelecionavel, Comparable<SrTipoAcao> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8387408543308440033L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ACAO_SEQ", name = "srAcaoSeq")
	@GeneratedValue(generator = "srAcaoSeq")
	@Column(name = "ID_TIPO_ACAO")
	public Long idTipoAcao;

	@Column(name = "SIGLA_TIPO_ACAO")
	public String siglaTipoAcao;

	@Column(name = "DESCR_TIPO_ACAO")
	public String descrTipoAcao;

	@Column(name = "TITULO_TIPO_ACAO")
	public String tituloTipoAcao;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrTipoAcao tipoAcaoInicial;

	@OneToMany(targetEntity = SrTipoAcao.class, mappedBy = "tipoAcaoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrTipoAcao> meuTipoAcaoHistoricoSet;

	@ManyToOne()
	@JoinColumn(name = "ID_PAI")
	public SrTipoAcao pai;

	@OneToMany(targetEntity = SrTipoAcao.class, mappedBy = "pai", fetch = FetchType.LAZY)
	public List<SrTipoAcao> filhoSet;

	public SrTipoAcao() {
		this(null, null);
	}

	public SrTipoAcao(String descricao) {
		this(descricao, null);
	}

	public SrTipoAcao(String sigla, String descricao) {
		this.tituloTipoAcao = descricao;
		this.siglaTipoAcao = sigla;
	}

	@Override
	public Long getId() {
		return this.idTipoAcao;
	}

	public void setId(Long id) {
		idTipoAcao = id;
	}

	@Override
	public String getSigla() {
		return this.siglaTipoAcao;
	}

	@Override
	public String getDescricao() {
		return tituloTipoAcao;
	}

	@Override
	public void setDescricao(String descricao) {
		this.tituloTipoAcao = descricao;
	}

	public List<SrTipoAcao> getHistoricoTipoAcao() {
		if (tipoAcaoInicial != null)
			return tipoAcaoInicial.meuTipoAcaoHistoricoSet;
		return null;
	}

	public SrTipoAcao getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrTipoAcao> sols = getHistoricoTipoAcao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrTipoAcao selecionar(String sigla) throws Exception {
		setSigla(sigla);
		List<SrTipoAcao> itens = buscar();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);
	}

	@Override
	public List<SrTipoAcao> buscar() throws Exception {
		List<SrTipoAcao> lista = new ArrayList<SrTipoAcao>();
		List<SrTipoAcao> listaFinal = new ArrayList<SrTipoAcao>();

		lista = listar(Boolean.FALSE);

		if ((siglaTipoAcao == null || siglaTipoAcao.equals(""))
				&& (tituloTipoAcao == null || tituloTipoAcao.equals("")))
			return lista;

		for (SrTipoAcao acao : lista) {
			if (siglaTipoAcao != null && !siglaTipoAcao.equals("")
					&& !(acao.siglaTipoAcao.contains(getSigla())))
				continue;
			if (tituloTipoAcao != null && !tituloTipoAcao.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloTipoAcao.toLowerCase().split("\\s"))
					if (!acao.tituloTipoAcao.toLowerCase().contains(s))
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
			tituloTipoAcao = "";
			siglaTipoAcao = "";
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
				siglaTipoAcao = s;
			} else
				tituloTipoAcao = sigla;
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

	public SrTipoAcao getPaiPorSigla() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 2 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrTipoAcao.find("byHisDtFimIsNullAndSiglaTipoAcao", sigla).first();
	}

	public boolean isPaiDeOuIgualA(SrTipoAcao outraAcao) {
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

	public boolean isFilhoDeOuIgualA(SrTipoAcao outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public static List<SrTipoAcao> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();
		
		if (!mostrarDesativados)
			sb.append(" hisDtFim is null");
		else {
			sb.append(" idTipoAcao in (");
			sb.append(" SELECT max(idTipoAcao) as idTipoAcao FROM ");
			sb.append(" SrTipoAcao GROUP BY hisIdIni) ");
		}
		
		sb.append(" order by siglaTipoAcao ");
		
		return SrTipoAcao.find(sb.toString()).fetch();
	}

	public void salvar() throws Exception {
		if (getNivel() > 1) {
			pai = getPaiPorSigla();
		}
		super.salvar();
	}

	public List<SrTipoAcao> getAcaoETodasDescendentes() {
		List<SrTipoAcao> lista = new ArrayList<SrTipoAcao>();
		lista.add(this);
		for (SrTipoAcao filho : filhoSet) {
			if (filho.getHisDtFim() == null)
				lista.addAll(filho.getAcaoETodasDescendentes());
		}
		return lista;
	}
	
	@Override
	public String toString() {
		return siglaTipoAcao + " - " + tituloTipoAcao;
	}

	@Override
	public int compareTo(SrTipoAcao arg0) {
		if (arg0.descrTipoAcao == null) {
			return -1;
		} else if (this.descrTipoAcao == null) {
			return 0;
		}
		return this.descrTipoAcao.compareTo(arg0.descrTipoAcao);
	}
	
	/**
	 * Classe que representa um V.O. de {@link SrTipoAcao}.
	 */
	public class SrTipoAcaoVO {
		
		public Long id;
		public String titulo;
		public String sigla;
		public Long hisIdIni;
		public String descricao;
		
		public SrTipoAcaoVO(Long id, String titulo, String sigla, Long hisIdIni) {
			this.id = id;
			this.titulo = titulo;
			this.sigla = sigla;
			this.hisIdIni = hisIdIni;
			this.descricao = titulo;
		}
	}
	
	public SrTipoAcaoVO toVO() {
		return new SrTipoAcaoVO(this.idTipoAcao, this.tituloTipoAcao, this.siglaTipoAcao, this.getHisIdIni());
	}
	
	public String toJson() {
		Gson gson = createGson();
		
		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
		jsonObject.add("nivel", gson.toJsonTree(getNivel()));
		
		return jsonObject.toString();
	}
	
	private Gson createGson() {
		return new GsonBuilder()
			.addSerializationExclusionStrategy(FieldNameExclusionEstrategy.notIn("meuTipoAcaoHistoricoSet", "filhoSet", "tipoAcaoInicial"))
			.create();
	}
}
