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
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_SERVICO", schema = "SIGASR")
public class SrServico extends HistoricoSuporte implements SrSelecionavel {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SERVICO_SEQ", name = "srServicoSeq")
	@GeneratedValue(generator = "srServicoSeq")
	@Column(name = "ID_SERVICO")
	public Long idServico;

	@Column(name = "SIGLA_SERVICO")
	public String siglaServico;

	@Column(name = "DESCR_SERVICO")
	public String descrServico;

	@Column(name = "TITULO_SERVICO")
	public String tituloServico;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrServico servicoInicial;

	@OneToMany(targetEntity = SrServico.class, mappedBy = "servicoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrServico> meuServicoHistoricoSet;

	public SrServico() {
		this(null, null);
	}

	public SrServico(String descricao) {
		this(descricao, null);
	}

	public SrServico(String sigla, String descricao) {
		this.tituloServico = descricao;
		this.siglaServico = sigla;
	}

	@Override
	public Long getId() {
		return this.idServico;
	}

	public void setId(Long id) {
		idServico = id;
	}

	@Override
	public String getSigla() {
		return this.siglaServico;
	}

	@Override
	public String getDescricao() {
		return tituloServico;
	}

	public String getDescricaoCompleta() {
		String sigla = this.siglaServico;
		int nivel = this.getNivel();
		String desc_nivel = null;
		if (nivel == 1) {
			desc_nivel = this.tituloServico;
		}
		if (nivel == 2) {
			String sigla_raiz = this.getSigla().substring(0, 2) + ".00";
			SrServico configuracao = SrServico.find(
					"bySiglaServicoAndHisDtFimIsNull", sigla_raiz).first();
			desc_nivel = configuracao.tituloServico + " : "
					+ this.tituloServico;
		}
		return desc_nivel;
	}

	@Override
	public void setDescricao(String descricao) {
		this.tituloServico = descricao;
	}

	public List<SrServico> getHistoricoServico() {
		if (servicoInicial != null)
			return servicoInicial.meuServicoHistoricoSet;
		return null;
	}

	public SrServico getAtual() {
		List<SrServico> sols = getHistoricoServico();
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
	public SrServico selecionar(String sigla) throws Exception {
		return selecionar(sigla, null, null);
	}

	public SrServico selecionar(String sigla, DpPessoa pess,
			SrItemConfiguracao item) throws Exception {
		setSigla(sigla);
		List<SrServico> itens = buscar(pess, item);
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	public List<SrServico> buscarOld() {
		String query = "from SrServico where 1=1";
		if (tituloServico != null && !tituloServico.equals("")) {
			for (String s : tituloServico.toLowerCase().split("\\s"))
				query += " and lower(tituloServico) like '%" + s + "%'";
		}
		if (siglaServico != null && !siglaServico.equals(""))
			query += " and siglaServico like '%" + siglaServico + "%' ";
		query += " and hisDtFim is null";
		return SrServico.find(query).fetch();
	}

	@Override
	public List<SrServico> buscar() throws Exception {
		return buscar(null, null);
	}

	public List<SrServico> buscar(DpPessoa pess, SrItemConfiguracao item)
			throws Exception {

		List<SrServico> lista = new ArrayList<SrServico>();
		List<SrServico> listaFinal = new ArrayList<SrServico>();

		if (pess == null)
			lista = listar();
		else
			lista.addAll(listarComAtendentePorPessoaEItem(pess, item).keySet());

		if ((siglaServico == null || siglaServico.equals(""))
				&& (tituloServico == null || tituloServico.equals("")))
			return lista;

		for (SrServico servico : lista) {
			if (siglaServico != null && !siglaServico.equals("")
					&& !(servico.siglaServico.contains(getSigla())))
				continue;
			if (tituloServico != null && !tituloServico.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloServico.toLowerCase().split("\\s"))
					if (!servico.tituloServico.toLowerCase().contains(s))
						naoAtende = true;
				if (naoAtende)
					continue;
			}
			listaFinal.add(servico);
		}
		return listaFinal;
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			tituloServico = "";
			siglaServico = "";
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
				siglaServico = s;
			} else
				tituloServico = sigla;
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

	public boolean isPaiDeOuIgualA(SrServico outroServico) {
		if (outroServico == null || outroServico.getSigla() == null)
			return false;
		if (this.equals(outroServico))
			return true;
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return outroServico.getSigla().startsWith(
				getSigla().substring(0, posFimComparacao + 1));
	}

	public boolean isFilhoDeOuIgualA(SrServico outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public List<SrServico> listarServicoETodosDescendentes() {
		return SrServico.find("byHisDtFimIsNullAndSiglaServicoLike",
				getSiglaSemZeros() + "%").fetch();
	}

	public static List<SrServico> listar() {
		return SrServico.find("hisDtFim is null order by siglaServico").fetch();
	}

	public static Map<SrServico, DpLotacao> listarComAtendentePorPessoaEItem(
			DpPessoa pess, SrItemConfiguracao item) throws Exception {
		Map<SrServico, DpLotacao> listaFinal = new HashMap<SrServico, DpLotacao>();
		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(pess,
				item, null, CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE,
				new int[] { SrConfiguracaoBL.SERVICO });
		for (SrConfiguracao conf : confs) {
			// Edson: o && !containsKey, abaixo, é necessário pra que atendentes
			// de configurações mais genéricas não substituam os das mais
			// específicas, que vêm antes
			if (conf.servico == null) {
				for (SrServico serv : listar())
					if (serv.isEspecifico() && !listaFinal.containsKey(serv))
						listaFinal.put(serv, conf.atendente);
				break;
			} else
				for (SrServico serv : conf.servico.getAtual()
						.listarServicoETodosDescendentes())
					if (serv.isEspecifico() && !listaFinal.containsKey(serv))
						listaFinal.put(serv, conf.atendente);
		}

		return listaFinal;
	}

	public static Map<SrServico, DpLotacao> listarComAtendentePorPessoaEItemOrdemSigla(
			DpPessoa pess, SrItemConfiguracao item) throws Exception {
		Map<SrServico, DpLotacao> m = new TreeMap<SrServico, DpLotacao>(
				new Comparator<SrServico>() {
					@Override
					public int compare(SrServico o1, SrServico o2) {
						if (o1 != null && o2 != null
								&& o1.idServico == o2.idServico)
							return 0;
						return o1.siglaServico.compareTo(o2.siglaServico);
					}
				});

		m.putAll(listarComAtendentePorPessoaEItem(pess, item));
		return m;
	}

	public static Map<SrServico, DpLotacao> listarComAtendentePorPessoaEItemOrdemTitulo(
			DpPessoa pess, SrItemConfiguracao item) throws Exception {
		Map<SrServico, DpLotacao> m = new TreeMap<SrServico, DpLotacao>(
				new Comparator<SrServico>() {
					@Override
					public int compare(SrServico o1, SrServico o2) {
						int i = o1.tituloServico.compareTo(o2.tituloServico);
						if (i != 0)
							return i;
						return o1.idServico.compareTo(o2.idServico);
					}
				});

		m.putAll(listarComAtendentePorPessoaEItem(pess, item));
		return m;
	}

	public String getGcTags() {
		String sigla = this.siglaServico;

		int nivel = this.getNivel();
		String tags = "";
		if (nivel == 1) {
			tags = "&tags=@" + Texto.slugify(this.tituloServico, true, false);
		}
		if (nivel == 2) {
			String sigla_raiz = this.getSigla().substring(0, 2) + ".00";
			SrServico configuracao = SrServico.find("bySiglaServico",
					sigla_raiz).first();
			tags = "&tags=@"
					+ Texto.slugify(configuracao.tituloServico, true, false)
					+ "&tags=@"
					+ Texto.slugify(this.tituloServico, true, false);
		}
		return tags;
	}
}
