package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import util.Util;

import models.siga.PlayHistoricoSuporte;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO", schema = "SIGASR")
public class SrItemConfiguracao extends HistoricoSuporte implements
		SrSelecionavel {

	private static Comparator<SrItemConfiguracao> comparator = new Comparator<SrItemConfiguracao>() {
		@Override
		public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
			if (o1 != null && o2 != null
					&& o1.idItemConfiguracao == o2.idItemConfiguracao)
				return 0;
			return o1.siglaItemConfiguracao.compareTo(o2.siglaItemConfiguracao);
		}
	};

	private static String MASCARA_JAVA = "([0-9][0-9]).?([0-9][0-9]).?([0-9][0-9]).?([0-9][0-9])";

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ITEM_CONFIGURACAO_SEQ", name = "srItemSeq")
	@GeneratedValue(generator = "srItemSeq")
	@Column(name = "ID_ITEM_CONFIGURACAO")
	public Long idItemConfiguracao;

	@Column(name = "SIGLA_ITEM_CONFIGURACAO")
	public String siglaItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	public String descrItemConfiguracao;

	@Column(name = "TITULO_ITEM_CONFIGURACAO")
	public String tituloItemConfiguracao;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrItemConfiguracao itemInicial;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "itemInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrItemConfiguracao> meuItemHistoricoSet;

	public SrItemConfiguracao() {
		this(null, null);
	}

	public SrItemConfiguracao(String descricao) {
		this(descricao, null);
	}

	public SrItemConfiguracao(String sigla, String descricao) {
		this.tituloItemConfiguracao = descricao;
		this.siglaItemConfiguracao = sigla;
	}

	@Override
	public Long getId() {
		return idItemConfiguracao;
	}

	public String getSigla() {
		return siglaItemConfiguracao;
	}

	public String getDescricao() {
		return tituloItemConfiguracao;
	}

	public String getDescricaoCompleta() {
		String sigla = this.siglaItemConfiguracao;
		int nivel = this.getNivel();
		String desc_nivel = null;
		if (nivel == 1) {
			desc_nivel = this.tituloItemConfiguracao;
		}
		if (nivel == 2) {
			String sigla_nivelpai = this.getSigla().substring(0, 2) + ".00"
					+ ".00" + ".00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			desc_nivel = configuracao.tituloItemConfiguracao + " - "
					+ this.tituloItemConfiguracao;
		}
		if (nivel == 3) {
			String sigla_nivelpai = this.getSigla().substring(0, 2) + "."
					+ this.getSigla().substring(3, 5) + ".00" + ".00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			String sigla_nivel_anterior = this.getSigla().substring(0, 2)
					+ ".00" + ".00" + ".00";
			SrItemConfiguracao configuracao_anterior = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivel_anterior).first();
			desc_nivel = configuracao_anterior.tituloItemConfiguracao + " - "
					+ configuracao.tituloItemConfiguracao + " - "
					+ this.tituloItemConfiguracao;
		}
		if (nivel == 4) {
			String sigla_nivel_spai = this.getSigla().substring(0, 2) + "."
					+ this.getSigla().substring(3, 5)
					+ this.getSigla().substring(6, 8) + "." + ".00";
			SrItemConfiguracao configuracao_spai = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivel_spai).first();
			String sigla_nivelpai = this.getSigla().substring(0, 2) + "."
					+ this.getSigla().substring(3, 5) + ".00" + ".00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			String sigla_nivel_anterior = this.getSigla().substring(0, 2)
					+ ".00" + ".00" + ".00";
			SrItemConfiguracao configuracao_anterior = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivel_anterior).first();
			desc_nivel = configuracao_spai.tituloItemConfiguracao + " - "
					+ configuracao_anterior.tituloItemConfiguracao + " - "
					+ configuracao.tituloItemConfiguracao + " - "
					+ this.tituloItemConfiguracao;
		}
		return desc_nivel;
	}

	@Override
	public void setId(Long id) {
		this.idItemConfiguracao = id;
	}

	public void setDescricao(String descricao) {
		this.tituloItemConfiguracao = descricao;
	}

	public List<SrItemConfiguracao> getHistoricoItemConfiguracao() {
		if (itemInicial != null)
			return itemInicial.meuItemHistoricoSet;
		return null;
	}

	public SrItemConfiguracao getAtual() {
		List<SrItemConfiguracao> sols = getHistoricoItemConfiguracao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public SrItemConfiguracao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null);
	}

	public SrItemConfiguracao selecionar(String sigla, DpPessoa pess)
			throws Exception {
		setSigla(sigla);
		List<SrItemConfiguracao> itens = buscar(pess);
		if (itens.size() == 0 || itens.size() > 1
				|| !itens.get(0).isEspecifico())
			return null;
		return itens.get(0);
	}

	public List<SrItemConfiguracao> buscarOld() {
		String query = "from SrItemConfiguracao where 1=1";
		if (tituloItemConfiguracao != null
				&& !tituloItemConfiguracao.equals("")) {
			for (String s : tituloItemConfiguracao.toLowerCase().split("\\s"))
				query += " and lower(tituloItemConfiguracao) like '%" + s
						+ "%'";
		}
		if (siglaItemConfiguracao != null && !siglaItemConfiguracao.equals(""))
			query += " and siglaItemConfiguracao like '%" + getSigla() + "%' ";
		query += " and hisDtFim is null order by siglaItemConfiguracao";

		return SrItemConfiguracao.find(query).fetch();
	}

	@Override
	public List<SrItemConfiguracao> buscar() throws Exception {
		return buscar(null);
	}

	public List<SrItemConfiguracao> buscar(DpPessoa pess) throws Exception {

		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();

		if (pess == null)
			lista = listar();
		else
			lista = listarPorPessoa(pess);

		if ((siglaItemConfiguracao == null || siglaItemConfiguracao.equals(""))
				&& (tituloItemConfiguracao == null || tituloItemConfiguracao
						.equals("")))
			return lista;

		for (SrItemConfiguracao item : lista) {
			if (siglaItemConfiguracao != null
					&& !siglaItemConfiguracao.equals("")
					&& !(item.siglaItemConfiguracao.toLowerCase()
							.contains(getSigla())))
				continue;
			if (tituloItemConfiguracao != null
					&& !tituloItemConfiguracao.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloItemConfiguracao.toLowerCase().split(
						"\\s"))
					if (!item.tituloItemConfiguracao.toLowerCase().contains(s))
						naoAtende = true;
				if (naoAtende)
					continue;
			}
			do {
				if (!listaFinal.contains(item))
					listaFinal.add(item);
				item = item.getPai();
			} while (item != null);
		}
		Collections.sort(listaFinal, comparator);
		return listaFinal;
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			siglaItemConfiguracao = "";
			tituloItemConfiguracao = "";
		} else {
			final Pattern p1 = Pattern.compile("^" + MASCARA_JAVA);
			final Matcher m1 = p1.matcher(sigla);
			if (m1.find()) {
				String s = "";
				for (int i = 1; i <= m1.groupCount(); i++) {
					s += m1.group(i);
					s += (i < m1.groupCount()) ? "." : "";
				}
				siglaItemConfiguracao = s;
			} else
				tituloItemConfiguracao = sigla;
		}
	}

	public int getNivel() {
		int camposVazios = 0;
		int pos = getSigla().indexOf(".00", 0);
		while (pos > -1) {
			camposVazios++;
			pos = getSigla().indexOf(".00", pos + 1);
		}
		return 4 - camposVazios;
	}

	public boolean isEspecifico() {
		return getNivel() == 4;
	}

	public String getSiglaSemZeros() {
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return getSigla().substring(0, posFimComparacao + 1);
	}

	public SrItemConfiguracao getPai() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 4 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrItemConfiguracao.find(
				"byHisDtFimIsNullAndSiglaItemConfiguracao", sigla).first();
	}

	public boolean isPaiDeOuIgualA(SrItemConfiguracao outroItem) {
		if (outroItem == null || outroItem.getSigla() == null)
			return false;
		if (this.equals(outroItem))
			return true;

		return outroItem.getSigla().startsWith(getSiglaSemZeros());
	}

	public boolean isFilhoDeOuIgualA(SrItemConfiguracao outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public List<SrItemConfiguracao> listarItemETodosDescendentes() {
		return SrItemConfiguracao.find(
				"byHisDtFimIsNullAndSiglaItemConfiguracaoLike",
				getSiglaSemZeros() + "%").fetch();
	}

	public static List<SrItemConfiguracao> listar() {
		return SrItemConfiguracao.find(
				"hisDtFim is null order by siglaItemConfiguracao").fetch();
	}

	public static List<SrItemConfiguracao> listarPorPessoa(DpPessoa pess)
			throws Exception {
		Set<SrItemConfiguracao> listaFinal = new TreeSet<SrItemConfiguracao>(
				comparator);

		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(pess,
				null, null, CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE, new int[] {
						SrConfiguracaoBL.ITEM_CONFIGURACAO,
						SrConfiguracaoBL.SERVICO });
		for (SrConfiguracao conf : confs) {
			if (conf.itemConfiguracao == null)
				listaFinal.addAll(listar());
			else {
				listaFinal.addAll(conf.itemConfiguracao.getAtual()
						.listarItemETodosDescendentes());
				SrItemConfiguracao itemPai = conf.itemConfiguracao.getAtual()
						.getPai();
				while (itemPai != null) {
					if (!listaFinal.contains(itemPai))
						listaFinal.add(itemPai);
					itemPai = itemPai.getPai();
				}
			}
		}
		return new ArrayList(listaFinal);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}
}
