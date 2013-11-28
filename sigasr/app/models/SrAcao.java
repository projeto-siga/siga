package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ACAO", schema = "SIGASR")
public class SrAcao extends HistoricoSuporte implements SrSelecionavel {

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

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "acaoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrAcao> meuAcaoHistoricoSet;

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

	public String getDescricaoCompleta() {
		String sigla = this.siglaAcao;
		int nivel = this.getNivel();
		String desc_nivel = null;
		if (nivel == 1) {
			desc_nivel = this.tituloAcao;
		}
		if (nivel == 2) {
			String sigla_raiz = this.getSigla().substring(0, 2) + ".00";
			SrAcao configuracao = SrAcao.find(
					"bySiglaAcaoAndHisDtFimIsNull", sigla_raiz).first();
			desc_nivel = configuracao.tituloAcao + " : "
					+ this.tituloAcao;
		}
		return desc_nivel;
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
		List<SrAcao> sols = getHistoricoAcao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public int getHisAtivo() {
		return getHisDtFim() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrAcao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null, null, null);
	}

	public SrAcao selecionar(String sigla, DpPessoa pess, CpComplexo local,
			SrItemConfiguracao item) throws Exception {
		setSigla(sigla);
		List<SrAcao> itens = buscar(pess, local, item);
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrAcao> buscar() throws Exception {
		return buscar(null, null, null);
	}

	public List<SrAcao> buscar(DpPessoa pess, CpComplexo local,
			SrItemConfiguracao item) throws Exception {

		List<SrAcao> lista = new ArrayList<SrAcao>();
		List<SrAcao> listaFinal = new ArrayList<SrAcao>();

		if (pess == null)
			lista = listar();
		else
			lista.addAll(listarComAtendentePorPessoaLocalEItem(pess, local,
					item).keySet());

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

	public List<SrAcao> listarAcaoETodasDescendentes() {
		return SrAcao.find("byHisDtFimIsNullAndSiglaAcaoLike",
				getSiglaSemZeros() + "%").fetch();
	}

	public static List<SrAcao> listar() {
		return SrAcao.find("hisDtFim is null order by siglaAcao").fetch();
	}

	public static Map<SrAcao, DpLotacao> listarComAtendentePorPessoaLocalEItem(
			DpPessoa pess, CpComplexo local, SrItemConfiguracao item)
			throws Exception {
		Map<SrAcao, DpLotacao> listaFinal = new HashMap<SrAcao, DpLotacao>();
		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(pess,
				local, item, null,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE,
				new int[] { SrConfiguracaoBL.ACAO });
		for (SrConfiguracao conf : confs) {
			// Edson: o && !containsKey, abaixo, é necessário pra que atendentes
			// de configurações mais genéricas não substituam os das mais
			// específicas, que vêm antes
			if (conf.acao == null) {
				for (SrAcao acao : listar())
					if (acao.isEspecifico() && !listaFinal.containsKey(acao))
						listaFinal.put(acao, conf.atendente);
				break;
			} else
				for (SrAcao acao : conf.acao.getAtual()
						.listarAcaoETodasDescendentes())
					if (acao.isEspecifico() && !listaFinal.containsKey(acao))
						listaFinal.put(acao, conf.atendente);
		}

		return listaFinal;
	}

	public static Map<SrAcao, DpLotacao> listarComAtendentePorPessoaLocalEItemOrdemSigla(
			DpPessoa pess, CpComplexo local, SrItemConfiguracao item)
			throws Exception {
		Map<SrAcao, DpLotacao> m = new TreeMap<SrAcao, DpLotacao>(
				new Comparator<SrAcao>() {
					@Override
					public int compare(SrAcao o1, SrAcao o2) {
						if (o1 != null && o2 != null
								&& o1.idAcao == o2.idAcao)
							return 0;
						return o1.siglaAcao.compareTo(o2.siglaAcao);
					}
				});

		m.putAll(listarComAtendentePorPessoaLocalEItem(pess, local, item));
		return m;
	}

	public static Map<SrAcao, DpLotacao> listarComAtendentePorPessoaLocalEItemOrdemTitulo(
			DpPessoa pess, CpComplexo local, SrItemConfiguracao item)
			throws Exception {
		Map<SrAcao, DpLotacao> m = new TreeMap<SrAcao, DpLotacao>(
				new Comparator<SrAcao>() {
					@Override
					public int compare(SrAcao o1, SrAcao o2) {
						int i = o1.tituloAcao.compareTo(o2.tituloAcao);
						if (i != 0)
							return i;
						return o1.idAcao.compareTo(o2.idAcao);
					}
				});

		m.putAll(listarComAtendentePorPessoaLocalEItem(pess, local, item));
		return m;
	}

	public String getGcTags() {
		String sigla = this.siglaAcao;

		int nivel = this.getNivel();
		String tags = "";
		if (nivel == 1) {
			tags = "&tags=@" + Texto.slugify(this.tituloAcao, true, false);
		}
		if (nivel == 2) {
			String sigla_raiz = this.getSigla().substring(0, 2) + ".00";
			SrAcao configuracao = SrAcao.find("bySiglaAcao",
					sigla_raiz).first();
			tags = "&tags=@"
					+ Texto.slugify(configuracao.tituloAcao, true, false)
					+ "&tags=@"
					+ Texto.slugify(this.tituloAcao, true, false);
		}
		return tags;
	}
}
