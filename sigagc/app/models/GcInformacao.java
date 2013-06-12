package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import play.db.jpa.GenericModel;
import play.mvc.Router;
import util.SigaPlayCalendar;
import ys.wikiparser.WikiParser;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "GC_INFORMACAO")
@NamedQueries({
		@NamedQuery(name = "buscarConhecimento", query = "select i.id, i.arq.titulo, (select j.arq.conteudo from GcInformacao j where j = i), count(*) from GcInformacao i inner join i.tags t where t in (:tags) and i.hisDtFim is null group by i.id, i.arq.titulo, i.hisDtIni  order by count(*) desc, i.hisDtIni desc"),
		@NamedQuery(name = "maisRecentes", query = "from GcInformacao i where i.hisDtFim is null order by i.hisDtIni desc"),
		@NamedQuery(name = "maisVisitados", query = "select (select j from GcInformacao j where j = i) from GcInformacao i inner join i.movs m where m.tipo.id = 11 and i.hisDtFim is null group by i order by count(*) desc"),
		@NamedQuery(name = "principaisAutores", query = "select (select p from DpPessoa p where p = i.autor) from GcInformacao i where i.hisDtFim is null group by i.autor order by count(*) desc"),
		@NamedQuery(name = "principaisLotacoes", query = "select (select l from DpLotacao l where l = i.lotacao) from GcInformacao i where i.hisDtFim is null group by i.lotacao order by count(*) desc"),
		@NamedQuery(name = "principaisTags", query = "select (select tt from GcTag tt where tt = t) from GcInformacao i inner join i.tags t where i.hisDtFim is null and t.tipo.id in (1,2) group by t order by count(*) desc"),
		@NamedQuery(name = "evolucaoNovos", query = "select month(inf.elaboracaoFim) as mes, year(inf.elaboracaoFim) as ano, count(*) as novas from GcInformacao inf where inf.elaboracaoFim is not null group by month(inf.elaboracaoFim), year(inf.elaboracaoFim)"),
		@NamedQuery(name = "evolucaoVisitados", query = "select month(mov.hisDtIni) as mes, year(mov.hisDtIni) as ano, count(distinct inf.id) as visitadas from GcInformacao inf join inf.movs mov where mov.tipo = 11 and inf.elaboracaoFim is not null and (year(inf.elaboracaoFim) * 12 + month(inf.elaboracaoFim) < year(mov.hisDtIni) * 12 + month(mov.hisDtIni)) group by month(mov.hisDtIni), year(mov.hisDtIni)"), })
// select inf.id, inf.arq.titulo, inf.arq.conteudo from GcInformacao inf join
// inf.tags tag where tag in (:tags)
public class GcInformacao extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_INFORMACAO")
	public long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_INFORMACAO")
	public GcTipoInformacao tipo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_INFORMACAO_PAI")
	public GcArquivo informacaoPai;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ORGAO_USUARIO")
	public CpOrgaoUsuario ou;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PESSOA")
	public DpPessoa autor;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_LOTACAO")
	public DpLotacao lotacao;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "GC_TAG_X_INFORMACAO", joinColumns = @JoinColumn(name = "id_informacao"), inverseJoinColumns = @JoinColumn(name = "id_tag"))
	public Set<GcTag> tags;

	@Column(name = "DT_ELABORACAO_FIM")
	public Date elaboracaoFim;

	@Column(name = "ANO")
	public Integer ano;

	@Column(name = "NUMERO")
	public Integer numero;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ARQUIVO")
	public GcArquivo arq;

	// @Sort(type = SortType.NATURAL)
	// @OneToMany(mappedBy = "inf")
	// @OneToMany
	// @Where(clause = "id_tp_marca = 3")
	// public java.util.SortedSet<GcMarca> marcas;
	@Transient
	public java.util.List<GcMarca> marcas;
	// public java.util.Set<GcMarca> marcas;

	@Sort(type = SortType.NATURAL)
	@OneToMany(mappedBy = "inf")
	public SortedSet<GcMovimentacao> movs;

	@Column(name = "HIS_DT_INI")
	public Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	public Date hisDtFim;

	@ManyToOne(optional = false)
	@JoinColumn(name = "HIS_IDC_INI")
	public CpIdentidade hisIdcIni;

	@PostLoad
	private void onLoad() {
		marcas = GcMarca.find("inf.id = ?", this.id).fetch();
		// marcas = GcMarca.find("id_tp_marca = 3 and inf.id = ?",
		// this.id).fetch();
	}

	public String getDtIniString() {
		SigaPlayCalendar cal = new SigaPlayCalendar();
		cal.setTime(hisDtIni);
		return cal.getTempoTranscorridoString(true);
	}

	public String getDescricaoCompletaEMarcadoresEmHtml(DpPessoa pess,
			DpLotacao lota) {
		String m = getMarcadoresEmHtml(pess, lota);
		if (m == null)
			return getSigla();
		return getSigla() + " - " + getMarcadoresEmHtml(pess, lota);
	}

	private String completarComZeros(int valor, int casas) {
		String s = String.valueOf(valor);
		while (s.length() < casas)
			s = "0" + s;
		return s;
	}

	private String getSigla() {
		if (this.elaboracaoFim != null) {
			return ou.getAcronimoOrgaoUsu() + "-GC-" + ano + "/"
					+ completarComZeros(numero, 5);
		}
		return "TMPGC-" + id;
	}

	public boolean isFinalizado() {
		return this.elaboracaoFim != null;
	}

	public boolean isCancelado() {
		return this.hisDtFim != null;
	}

	public boolean podeRevisar(DpPessoa titular) {
		if (isCancelado())
			return false;
		for (GcMovimentacao mov : movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO
					&& titular.equivale(mov.pessoa)) {
				return true;
			}
		}
		return false;
	}

	public boolean podeMarcarComoInteressado(DpPessoa titular) {
		if (isCancelado())
			return false;
		for (GcMovimentacao mov : movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO
					&& titular.equivale(mov.pessoa)) {
				return false;
			}
		}
		return true;
	}

	public boolean podeDesmarcarComoInteressado(DpPessoa titular) {
		if (isCancelado())
			return false;
		return !podeMarcarComoInteressado(titular);
	}

	public boolean podeCancelar(DpPessoa titular, DpLotacao lotaTitular) {
		if (isCancelado() || !isFinalizado())
			return false;
		for (GcMovimentacao mov : movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO) {
				return false;
			}
		}
		return this.autor.equivale(titular)
				|| this.lotacao.equivale(lotaTitular);
	}

	public boolean podeEditar() {
		return !isCancelado();
	}

	public boolean podeExcluir() {
		return !isFinalizado();
	}

	public boolean podeNotificar() {
		return !isCancelado();
	}

	public boolean podeFinalizar() {
		return !isCancelado() && !isFinalizado();
	}

	public boolean podeSolicitarRevisao() {
		return !isCancelado();
	}

	public SortedSet<GcAcaoVO> acoes(CpIdentidade idc, DpPessoa titular,
			DpLotacao lotaTitular) {
		SortedSet<GcAcaoVO> acoes = new TreeSet<GcAcaoVO>();

		addAcao(acoes, "pencil", "Editar", null, "Application.editar", null,
				podeEditar());
		addAcao(acoes, "delete", "Excluir", null, "Application.remover", null,
				podeExcluir(), "Confirma a exclusão de tópico de informação?",
				null, null);
		addAcao(acoes, "eye", "Exibir Informações Completas", null,
				"Application.exibir", "completo=true", true);
		addAcao(acoes, "eye", "Exibir Histórico de Alterações", null,
				"Application.historico", "historico=true", true);
		addAcao(acoes, "heart_delete", "Desmarcar Interesse", null,
				"Application.desmarcarComoInteressado", null,
				podeDesmarcarComoInteressado(titular));
		addAcao(acoes, "heart_add", "Marcar Interesse", null,
				"Application.marcarComoInteressado", null,
				podeMarcarComoInteressado(titular));
		addAcao(acoes, "bell", "Notificar", null, "Application.notificar",
				null, podeNotificar());
		addAcao(acoes,
				"lock",
				"Finalizar Elaboração",
				null,
				"Application.fechar",
				null,
				podeFinalizar(),
				"Confirma a finalização da elaboração deste tópico de informação?",
				null, null);
		addAcao(acoes, "folder_user", "Revisado", null, "Application.revisado",
				null, podeRevisar(titular));
		addAcao(acoes, "folder_user", "Solicitar Revisão", null,
				"Application.solicitarRevisao", null, podeSolicitarRevisao());
		addAcao(acoes, "cancel", "Cancelar", null, "Application.cancelar",
				null, podeCancelar(titular, lotaTitular),
				"Confirma o cancelamento deste tópico de informação?", null,
				null);

		// addAcao("printer.png","Visualizar Impressão",null,"", null, true,
		// null);

		return acoes;
	}

	protected void addAcao(SortedSet<GcAcaoVO> acoes, String icone,
			String nome, String nameSpace, String action, String parametros,
			boolean pode) {
		addAcao(acoes, icone, nome, nameSpace, action, parametros, pode, null,
				null, null);
	}

	protected void addAcao(SortedSet<GcAcaoVO> acoes, String icone,
			String nome, String nameSpace, String action, String parametros,
			boolean pode, String msgConfirmacao, String pre, String pos) {
		TreeMap<String, String> params = new TreeMap<String, String>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", String.valueOf(this.id));
		if (parametros != null)
			mapFromUrlEncodedForm(map, parametros);

		String url = Router.reverse(action, map).url;

		if (pode) {
			GcAcaoVO acao = new GcAcaoVO(icone, nome, url, pode,
					msgConfirmacao, map, pre, pos);
			acoes.add(acao);
		}
	}

	public static void mapFromUrlEncodedForm(Map map, String parametros) {
		if (parametros != null) {
			final String as[] = new String(parametros).split("&");
			for (final String s : as) {
				final String parametro[] = s.split("=");
				// try {
				if (parametro.length == 2) {
					map.put(parametro[0], parametro[1]);
					// URLDecoder.decode(param[1], "iso-8859-1"));
				}
				// } catch (final UnsupportedEncodingException e) {
				// }
			}
		}
	}

	public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();

		// Marcacoes para a propria lotacao e para a propria pessoa ou sem
		// informacao de pessoa
		//
		for (GcMarca mar : marcas) {
			if (((mar.getDpLotacaoIni() != null && lota.getIdInicial().equals(
					mar.getDpLotacaoIni().getIdInicial())) || mar
					.getDpLotacaoIni() == null)
					&& (mar.getDpPessoaIni() == null || pess.getIdInicial()
							.equals(mar.getDpPessoaIni().getIdInicial()))) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(mar.getCpMarcador().getDescrMarcador());
			}
		}

		// Marcacoes para a propria lotacao e para outra pessoa
		//
		if (sb.length() == 0) {
			for (GcMarca mar : marcas) {
				if (sb.length() > 0)
					sb.append(", ");
				if ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
						.equals(mar.getDpLotacaoIni().getIdInicial()))
						&& (mar.getDpPessoaIni() != null && !pess
								.getIdInicial().equals(
										mar.getDpPessoaIni().getIdInicial()))) {
					sb.append(mar.getCpMarcador().getDescrMarcador());
					sb.append(" [");
					sb.append(mar.getDpPessoaIni().getSigla());
					sb.append("]");
				}
			}
		}

		// Marcacoes para qualquer outra pessoa ou lotacao
		//
		if (sb.length() == 0) {
			for (GcMarca mar : marcas) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(mar.getCpMarcador().getDescrMarcador());
				if (mar.getDpLotacaoIni() != null
						|| mar.getDpPessoaIni() != null) {
					sb.append(" [");
					if (mar.getDpLotacaoIni() != null) {
						sb.append(mar.getDpLotacaoIni().getSigla());
					}
					if (mar.getDpPessoaIni() != null) {
						if (mar.getDpLotacaoIni() != null) {
							sb.append(", ");
						}
						sb.append(mar.getDpPessoaIni().getSigla());
					}
					sb.append("]");
				}
			}
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	public String getConteudoHTML() throws Exception {
		if (this.arq == null || this.arq.conteudo == null)
			return null;
		String s = this.arq.getConteudoTXT().replace(" # ", "\n# ")
				.replace(" ## ", "\n## ").replace(" ### ", "\n### ")
				.replace(" #### ", "\n#### ").replace(" * ", "\n* ")
				.replace(" ** ", "\n** ").replace(" *** ", "\n*** ")
				.replace(" **** ", "\n**** ").replace(" ==", "\n==");
		String fragment = WikiParser.renderXHTML(s);
		return fragment;
	}
}
