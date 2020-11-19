package br.gov.jfrj.siga.gc.model;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.util.WikiParser;
import br.gov.jfrj.siga.gc.vraptor.AppController;
import br.gov.jfrj.siga.gc.vraptor.GcInterceptor;
import br.gov.jfrj.siga.gc.vraptor.SigaLogicResult;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_INFORMACAO", schema = "SIGAGC")
@NamedQueries({
		@NamedQuery(name = "buscarConhecimento", query = "select i.id, i.arq.titulo, (select j.arq.conteudo from GcInformacao j where j = i), count(*), (select count(*) from GcMarca m where m.inf=i and m.cpMarcador.idMarcador = 28 and (m.dpLotacaoIni.idLotacao = :lotacaoIni or m.dpPessoaIni.idPessoa = :pessoaIni) and m.dtFimMarca is null) as interessado, (select count(*) from GcMarca m where m.inf=i and m.cpMarcador.idMarcador = 70 and (m.dpLotacaoIni.idLotacao = :lotacaoIni or m.dpPessoaIni.idPessoa = :pessoaIni) and m.dtFimMarca is null) as executor from GcInformacao i inner join i.tags t where t in (:tags) and i.hisDtFim is null group by i.id, i.arq.titulo, i.hisDtIni  order by interessado desc, executor desc, count(*) desc, i.hisDtIni desc"),
		@NamedQuery(name = "buscarConhecimentoTudoIgual", query = "select i.id, i.arq.titulo, (select j.arq.conteudo from GcInformacao j where j = i), count(*) from GcInformacao i inner join i.tags t where (select count(*) from GcTag t2 where t2 in t and t2 not in (:tags) and t2.tipo in (select tipo from GcTag where id in (:tags))) = 0 and i.hisDtFim is null group by i.id, i.arq.titulo, i.hisDtIni order by count(*) desc, i.hisDtIni desc"),
		@NamedQuery(name = "buscarConhecimentoAlgumIgualNenhumDiferente", query = "select i.id, i.arq.titulo, (select j.arq.conteudo from GcInformacao j where j = i), count(*), (select count(*) from GcMarca m where m.inf=i and m.cpMarcador.idMarcador = 28 and (m.dpLotacaoIni.idLotacao = :lotacaoIni or m.dpPessoaIni.idPessoa = :pessoaIni) and m.dtFimMarca is null) as interessado, (select count(*) from GcMarca m where m.inf=i and m.cpMarcador.idMarcador = 70 and (m.dpLotacaoIni.idLotacao = :lotacaoIni or m.dpPessoaIni.idPessoa = :pessoaIni) and m.dtFimMarca is null) as executor from GcInformacao i inner join i.tags t where t in (:tags) and i not in (select i2 from GcInformacao i2 inner join i2.tags t2 where t2 not in (:tags) and t2.tipo in (select tipo from GcTag where id in (:tags))) and i.hisDtFim is null group by i.id, i.arq.titulo, i.hisDtIni order by interessado desc, executor desc, count(*) desc, i.hisDtIni desc"),
		@NamedQuery(name = "buscarConhecimentoExatoOuNada", query = "select i.id, i.arq.titulo, (select j.arq.conteudo from GcInformacao j where j = i), count(*) from GcInformacao i inner join i.tags t where (select count(*) from GcTag t2 where t2 in t and t2 not in (:tags) and t2.tipo in (select tipo from GcTag where id in (:tags))) = 0 and i.hisDtFim is null group by i.id, i.arq.titulo, i.hisDtIni having count(*) = :numeroDeTags order by count(*) desc, i.hisDtIni desc"),
		@NamedQuery(name = "maisRecentes", query = "from GcInformacao i where i.hisDtFim is null and i.elaboracaoFim is not null order by i.hisDtIni desc"),
		// @NamedQuery(name = "maisRecentesLotacao", query =
		// "from GcInformacao i where i.hisDtFim is null and i.elaboracaoFim is not null and i.lotacao.idLotacao = :idLotacao order by i.hisDtIni desc"),
		@NamedQuery(name = "maisRecentesLotacao", query = "from GcInformacao i where i.hisDtFim is null and i.elaboracaoFim is not null and i.lotacao.idLotacaoIni = :idlotacaoInicial order by i.hisDtIni desc"),
		@NamedQuery(name = "maisVisitados", query = "select (select j from GcInformacao j where j = i) from GcInformacao i inner join i.movs m where m.tipo.id = 11 and i.hisDtFim is null and i.elaboracaoFim is not null group by i order by count(*) desc"),
		@NamedQuery(name = "maisVisitadosLotacao", query = "select (select j from GcInformacao j where j = i) from GcInformacao i inner join i.movs m where m.tipo.id = 11 and i.hisDtFim is null and i.elaboracaoFim is not null and i.lotacao.idLotacaoIni = :idlotacaoInicial group by i order by count(*) desc"),
		// @NamedQuery(name = "principaisAutores", query =
		// "select (select p from DpPessoa p where p = i.autor) from GcInformacao i where i.hisDtFim is null group by i.autor order by count(*) desc"),
		@NamedQuery(name = "principaisAutores", query = "select p.nomePessoa, p.idPessoaIni, i.lotacao.siglaLotacao, i.lotacao.idLotacaoIni, count(*) from GcInformacao i inner join i.autor p where i.hisDtFim is null and i.elaboracaoFim is not null group by p.nomePessoa, p.idPessoaIni, i.lotacao.siglaLotacao, i.lotacao.idLotacaoIni order by count(*) desc"),
		@NamedQuery(name = "principaisAutoresLotacao", query = "select p.nomePessoa, p.idPessoaIni, i.lotacao.siglaLotacao, i.lotacao.idLotacaoIni, count(*) from GcInformacao i inner join i.autor p where i.hisDtFim is null and i.elaboracaoFim is not null and i.lotacao.idLotacaoIni = :idlotacaoInicial group by p.nomePessoa, p.idPessoaIni, i.lotacao.siglaLotacao, i.lotacao.idLotacaoIni order by count(*) desc"),
		// @NamedQuery(name = "principaisLotacoes", query =
		// "select (select l from DpLotacao l where l = i.lotacao) from GcInformacao i where i.hisDtFim is null group by i.lotacao order by count(*) desc"),
		@NamedQuery(name = "principaisLotacoes", query = "select l.nomeLotacao, l.idLotacaoIni, l.siglaLotacao, count(*) from GcInformacao i inner join i.lotacao l where i.hisDtFim is null and i.elaboracaoFim is not null group by l.nomeLotacao, l.idLotacaoIni, l.siglaLotacao order by count(*) desc"),
		// @NamedQuery(name = "principaisTags", query =
		// "select (select tt from GcTag tt where tt = t) from GcInformacao i inner join i.tags t where i.hisDtFim is null and t.tipo.id in (1,2) group by t order by count(*) desc"),
		@NamedQuery(name = "principaisTags", query = "select (select distinct tt.titulo from GcTag tt where tt.titulo = t.titulo), count(*) from GcInformacao i inner join i.tags t where i.hisDtFim is null and i.elaboracaoFim is not null and t.tipo.id in (1,2) group by t.titulo order by count(*) desc"),
		@NamedQuery(name = "principaisTagsLotacao", query = "select (select distinct tt.titulo from GcTag tt where tt.titulo = t.titulo), count(*) from GcInformacao i inner join i.tags t where i.hisDtFim is null and i.elaboracaoFim is not null and i.lotacao.idLotacaoIni = :idlotacaoInicial and t.tipo.id in (1,2) group by t.titulo order by count(*) desc"),
		@NamedQuery(name = "evolucaoNovos", query = "select month(inf.elaboracaoFim) as mes, year(inf.elaboracaoFim) as ano, count(*) as novas from GcInformacao inf where inf.elaboracaoFim is not null group by month(inf.elaboracaoFim), year(inf.elaboracaoFim)"),
		@NamedQuery(name = "evolucaoNovosLotacao", query = "select month(inf.elaboracaoFim) as mes, year(inf.elaboracaoFim) as ano, count(*) as novas from GcInformacao inf where inf.elaboracaoFim is not null and inf.lotacao.idLotacaoIni = :idlotacaoInicial group by month(inf.elaboracaoFim), year(inf.elaboracaoFim)"),
		// @NamedQuery(name = "evolucaoVisitados", query =
		// "select month(mov.hisDtIni) as mes, year(mov.hisDtIni) as ano, count(distinct inf.id) as visitadas from GcInformacao inf join inf.movs mov where mov.tipo = 11 and inf.elaboracaoFim is not null and (year(inf.elaboracaoFim) * 12 + month(inf.elaboracaoFim) < year(mov.hisDtIni) * 12 + month(mov.hisDtIni)) group by month(mov.hisDtIni), year(mov.hisDtIni)"),
		@NamedQuery(name = "evolucaoVisitados", query = "select month(mov.hisDtIni) as mes, year(mov.hisDtIni) as ano, count(distinct inf.id) as visitadas from GcInformacao inf join inf.movs mov where mov.tipo = 11 and inf.elaboracaoFim is not null and inf.elaboracaoFim < mov.hisDtIni group by month(mov.hisDtIni), year(mov.hisDtIni)"),
		// @NamedQuery(name = "evolucaoVisitadosLotacao", query =
		// "select month(mov.hisDtIni) as mes, year(mov.hisDtIni) as ano, count(distinct inf.id) as visitadas from GcInformacao inf join inf.movs mov where mov.tipo = 11 and inf.elaboracaoFim is not null and inf.lotacao.idLotacaoIni = :idlotacaoInicial and (year(inf.elaboracaoFim) * 12 + month(inf.elaboracaoFim) < year(mov.hisDtIni) * 12 + month(mov.hisDtIni)) group by month(mov.hisDtIni), year(mov.hisDtIni)"),
		@NamedQuery(name = "evolucaoVisitadosLotacao", query = "select month(mov.hisDtIni) as mes, year(mov.hisDtIni) as ano, count(distinct inf.id) as visitadas from GcInformacao inf join inf.movs mov where mov.tipo = 11 and inf.elaboracaoFim is not null and inf.elaboracaoFim < mov.hisDtIni and inf.lotacao.idLotacaoIni = :idlotacaoInicial group by month(mov.hisDtIni), year(mov.hisDtIni)"),
		@NamedQuery(name = "dadosParaRecuperacaoDeInformacao", query = "select inf, arq,  mov.hisDtIni, mov.id, (mov.tipo.id) as ativo from GcInformacao as inf join inf.arq as arq join inf.movs mov where ((mov.tipo in (1, 10) and mov.arq = inf.arq) or (mov.tipo = 3)) and inf.elaboracaoFim is not null and ((mov.hisDtIni > :dt) or (mov.hisDtIni = :dt and mov.id > :desempate)) order by mov.hisDtIni, mov.id"),
		@NamedQuery(name = "pontosDeEntrada", query = "select inf, arq from GcInformacao as inf join inf.arq as arq where inf.tipo.id = 4 and inf.elaboracaoFim is not null and inf.hisDtFim is null and arq.titulo like :texto order by arq.titulo") })
// select GcInformacao as i, i.arq as a, mov.hisDtIni as dt from GcInformacao
// inf join inf.movs mov where ((mov.tipo in {1, 10} and mov.arq = inf.arq) or
// (mov.tipo = 3)) and inf.elaboracaoFim is not null
// select inf.id, inf.arq.titulo, inf.arq.conteudo from GcInformacao inf join
// inf.tags tag where tag in (:tags)
public class GcInformacao extends Objeto {
	public static ActiveRecord<GcInformacao> AR = new ActiveRecord<>(
			GcInformacao.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcInformacaoSeq")
	@GeneratedValue(generator = "gcInformacaoSeq")
	@Column(name = "ID_INFORMACAO")
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_INFORMACAO")
	private GcTipoInformacao tipo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_INFORMACAO_PAI")
	private GcArquivo informacaoPai;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ORGAO_USUARIO")
	private CpOrgaoUsuario ou;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ACESSO")
	private GcAcesso visualizacao;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ACESSO_EDICAO")
	private GcAcesso edicao;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PESSOA_TITULAR")
	private DpPessoa autor;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_LOTACAO_TITULAR")
	private DpLotacao lotacao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_GRUPO")
	private CpPerfil grupo;

	@Sort(type = SortType.NATURAL)
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "GC_TAG_X_INFORMACAO", schema = "SIGAGC", joinColumns = @JoinColumn(name = "id_informacao"), inverseJoinColumns = @JoinColumn(name = "id_tag"))
	private SortedSet<GcTag> tags;

	@Column(name = "DT_ELABORACAO_FIM")
	private Date elaboracaoFim;

	@Column(name = "ANO")
	private Integer ano;

	@Column(name = "NUMERO")
	private Integer numero;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ARQUIVO")
	private GcArquivo arq;

	// @Sort(type = SortType.NATURAL)
	// @OneToMany(mappedBy = "inf")
	// @OneToMany
	// @Where(clause = "id_tp_marca = 3")
	// public java.util.SortedSet<GcMarca> marcas;
	@Transient
	private java.util.List<GcMarca> marcas;
	// public java.util.Set<GcMarca> marcas;

	@Sort(type = SortType.NATURAL)
	@OneToMany(mappedBy = "inf")
	private SortedSet<GcMovimentacao> movs;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	private Date hisDtFim;

	public void setId(long id) {
		this.id = id;
	}

	public void setTipo(GcTipoInformacao tipo) {
		this.tipo = tipo;
	}

	public void setInformacaoPai(GcArquivo informacaoPai) {
		this.informacaoPai = informacaoPai;
	}

	public void setOu(CpOrgaoUsuario ou) {
		this.ou = ou;
	}

	public void setVisualizacao(GcAcesso visualizacao) {
		this.visualizacao = visualizacao;
	}

	public void setEdicao(GcAcesso edicao) {
		this.edicao = edicao;
	}

	public void setAutor(DpPessoa autor) {
		this.autor = autor;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public void setTags(SortedSet<GcTag> tags) {
		this.tags = tags;
	}

	public void setElaboracaoFim(Date elaboracaoFim) {
		this.elaboracaoFim = elaboracaoFim;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public void setArq(GcArquivo arq) {
		this.arq = arq;
	}

	public void setMarcas(java.util.List<GcMarca> marcas) {
		this.marcas = marcas;
	}

	public void setMovs(SortedSet<GcMovimentacao> movs) {
		this.movs = movs;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@PostLoad
	private void onLoad() {
		marcas = GcMarca.AR
				.find("inf.id = ?1 and (dtFimMarca is null or dtFimMarca > CURRENT_TIMESTAMP) order by dtIniMarca, cpMarcador.descrMarcador",
						this.id).fetch();
		// marcas = GcMarca.find("id_tp_marca = 3 and inf.id = ?",
		// this.id).fetch();
	}

	public String getDtIniString() {
		SigaCalendar cal = new SigaCalendar();
		cal.setTime(hisDtIni);
		return cal.getTempoTranscorridoString(true);
	}

	public String getDtElaboracaoFim() {
		SigaCalendar cal = new SigaCalendar();
		cal.setTime(elaboracaoFim);
		return cal.getTempoTranscorridoString(true);
	}

	public String getDescricaoCompletaEMarcadoresEmHtml(DpPessoa pess,
			DpLotacao lota) {
		String m = getMarcadoresEmHtml(pess, lota);
		if (m == null)
			return getSigla();
		return getSigla() + " - " + getMarcadoresEmHtml(pess, lota);
	}

	public String getSigla() {
		if (this.elaboracaoFim != null) {
			return ou.getAcronimoOrgaoUsu() + "-GC-" + ano + "/"
					+ Utils.completarComZeros(numero, 5);
		}
		return "TMPGC-" + id;
	}

	public String getSiglaCompacta() {
		return getSigla().replace("-", "").replace("/", "");
	}

	public boolean isFinalizado() {
		return this.elaboracaoFim != null;
	}

	public boolean isCancelado() {
		return this.hisDtFim != null;
	}

	public boolean podeDesfazer(DpPessoa titular, DpLotacao lotaTitular, GcMovimentacao mov) {
		if (isCancelado())
			return false;
		if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL || mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO 
				|| mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR){
				if (mov.getMovCanceladora() != null)
					return false;
				if ((mov.getPessoaAtendente() == null && lotaTitular.equivale(mov.getLotacaoAtendente())) || titular.equivale(mov.getPessoaAtendente()))
					return true;
				if ((mov.getPessoaTitular() == null && lotaTitular.equivale(mov.getLotacaoTitular())) || titular.equivale(mov.getPessoaTitular()))
					return true;
		}
		return false;
	}
	
	public boolean podeVincularPapel(DpPessoa titular, DpLotacao lotaTitular) {
		return !isCancelado() && isFinalizado()
			&& acessoPermitido(titular, lotaTitular, getEdicao().getId());
	}

	public GcMovimentacao podeTomarCiencia(DpPessoa titular,
			DpLotacao lotaTitular) throws Exception {
		if (titular == null && lotaTitular == null)
			return null;
		if (isCancelado())
			return null;
		for (GcMovimentacao mov : movs) {
			if (mov.isCancelada() || mov.getTipo().getId() != GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR)
				continue;
			if (titular != null){
				if (titular.equivale(mov.getPessoaAtendente())) 
					return mov;
				if (mov.getPessoaAtendente() == null && lotaTitular.equivale(mov.getLotacaoAtendente())) 
					return mov;
			} else if (lotaTitular.equivale(mov.getLotacaoAtendente()))
					return mov;
		}
		return null;
	}

	public GcMovimentacao podeRevisar(DpPessoa titular, DpLotacao lotaTitular) {
		if (titular == null && lotaTitular == null)
			return null;
		if (isCancelado())
			return null;
		for (GcMovimentacao mov : movs) {
			if (mov.isCancelada() || mov.getTipo().getId() != GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO)
				continue;
			if (titular != null){
				if (titular.equivale(mov.getPessoaAtendente())) 
					return mov;
				if (mov.getPessoaAtendente() == null && lotaTitular.equivale(mov.getLotacaoAtendente())) 
					return mov;
			} else if (lotaTitular.equivale(mov.getLotacaoAtendente()))
					return mov;
		}
		return null;
	}
		
	public Map<GcPapel, List<Object>> getPapeisVinculados() {
		Map<GcPapel, List<Object>> mapa = new HashMap<GcPapel, List<Object>>();
		for (GcMovimentacao mov : movs)
			if (!mov.isCancelada() && mov.getTipo() .getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL) {
				if (mapa.get(mov.getPapel()) == null)
					mapa.put(mov.getPapel(), new ArrayList<Object>());
				if (mov.getPessoaAtendente() != null)
					mapa.get(mov.getPapel()).add(mov.getPessoaAtendente());
				else if (mov.getLotacaoAtendente() != null)
					mapa.get(mov.getPapel()).add(mov.getLotacaoAtendente());
				else if (mov.getGrupo() != null)
					mapa.get(mov.getPapel()).add(mov.getGrupo());
			}
		return mapa;
	}

	public boolean podeMarcarComoInteressado(DpPessoa titular) {
		if (isCancelado())
			return false;
		for (GcMovimentacao mov : movs) {
			if (!mov.isCancelada() && mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL
					&& titular.equivale(mov.getPessoaAtendente())) {
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
			if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO) {
				return false;
			}
		}
		return this.autor.equivale(titular)
				|| this.lotacao.equivale(lotaTitular);
	}

	public boolean podeEditar(DpPessoa titular, DpLotacao lotaTitular) {
		return (!isCancelado() && (podeRevisar(titular, lotaTitular) != null || acessoPermitido(
				titular, lotaTitular, edicao.getId())));
	}

	public boolean podeExcluir(DpPessoa titular, DpLotacao lotaTitular) {
		return (!isFinalizado() && acessoPermitido(titular, lotaTitular,
				edicao.getId()));
	}

	public boolean podeNotificar(DpPessoa titular, DpLotacao lotaTitular) {
		return (!isCancelado() && acessoPermitido(titular, lotaTitular, visualizacao.getId()));
	}

	public boolean podeFinalizar(DpPessoa titular, DpLotacao lotaTitular) {
		return !isCancelado() && !isFinalizado()
				&& acessoPermitido(titular, lotaTitular, edicao.getId());
	}

	public boolean podeSolicitarRevisao(DpPessoa titular, DpLotacao lotaTitular) {
		return !isCancelado()
				&& acessoPermitido(titular, lotaTitular, edicao.getId());
	}

	public boolean podeAnexar(DpPessoa titular, DpLotacao lotaTitular) {
		return (!isCancelado() && (podeRevisar(titular, lotaTitular) != null || acessoPermitido(
				titular, lotaTitular, edicao.getId())));
	}

	public boolean podeDuplicar() {
		// return !isCancelado();
		return true;
	}
	
	public SortedSet<GcAcaoVO> acoes(CpIdentidade idc, DpPessoa titular,
			DpLotacao lotaTitular) throws Exception {
		SortedSet<GcAcaoVO> acoes = new TreeSet<GcAcaoVO>();

		StringBuilder sb = new StringBuilder();
		SigaLogicResult router = GcInterceptor.result().use(
				SigaLogicResult.class);

		boolean podeExibirLinkSemAutenticacao = 
				this.isFinalizado() && this.acessoExternoPublicoPermitido();
		
		router.getRedirectURL(sb, AppController.class).exibirPublicoExterno(
				this.getSiglaCompacta());
		addAcao(acoes, "eye", "Link externo", null, sb.toString(),
				podeExibirLinkSemAutenticacao);

		router.getRedirectURL(sb, AppController.class).editar(
				this.getSiglaCompacta(), null, null, null, null, null);
		addAcao(acoes, "pencil", "Editar", null, sb.toString(),
				podeEditar(titular, lotaTitular));

		router.getRedirectURL(sb, AppController.class).remover(
				this.getSiglaCompacta());
		addAcao(acoes, "delete", "Excluir", null, sb.toString(),
				podeExcluir(titular, lotaTitular),
				"Confirma a exclusão deste conhecimento?", null, null);

		router.getRedirectURL(sb, AppController.class).historico(
				this.getSiglaCompacta());
		addAcao(acoes, "eye", "Exibir Histórico de Alterações", null,
				sb.toString(), true);

		router.getRedirectURL(sb, AppController.class).movimentacoes(
				this.getSiglaCompacta());
		addAcao(acoes, "eye", "Exibir Movimentações", null, sb.toString(), true);

		// addAcao(acoes, "eye", "Exibir Informações Completas", null,
		// "Application.movimentacoes", "completo=true", true);
		router.getRedirectURL(sb, AppController.class)
				.desmarcarComoInteressado(this.getSiglaCompacta());
		addAcao(acoes, "heart_delete", "Desmarcar Interesse", null,
				sb.toString(), podeDesmarcarComoInteressado(titular));

		router.getRedirectURL(sb, AppController.class).marcarComoInteressado(
				this.getSiglaCompacta());
		addAcao(acoes, "heart_add", "Marcar Interesse", null, sb.toString(),
				podeMarcarComoInteressado(titular));

		router.getRedirectURL(sb, AppController.class).notificar(
				this.getSiglaCompacta());
		addAcao(acoes, "bell", "Notificar", null, sb.toString(),
				podeNotificar(titular, lotaTitular));
		
		router.getRedirectURL(sb, AppController.class).vincularPapel(
				this.getSiglaCompacta());
		addAcao(acoes, "folder_user", "Definir Perfil", null, sb.toString(),
				podeVincularPapel(titular, lotaTitular));

		router.getRedirectURL(sb, AppController.class).fechar(
				this.getSiglaCompacta());
		addAcao(acoes, "lock", "Finalizar Elaboração", null, sb.toString(),
				podeFinalizar(titular, lotaTitular),
				"Confirma a finalização da elaboração deste conhecimento?",
				null, null);

		/*
		 * addAcao(acoes, "folder_user", "Revisado", null,
		 * "Application.revisado", null, podeRevisar(titular, lotaTitular));
		 */

		router.getRedirectURL(sb, AppController.class).revisado(
				this.getSiglaCompacta());
		addAcao(acoes, "folder_user", "Revisado", null, sb.toString(),
				podeRevisar(titular, lotaTitular) != null,
				"Confirma a revisão deste conhecimento?", null, null);

		router.getRedirectURL(sb, AppController.class).solicitarRevisao(
				this.getSiglaCompacta());
		addAcao(acoes, "folder_user", "Solicitar Revisão", null, sb.toString(),
				podeSolicitarRevisao(titular, lotaTitular));

		router.getRedirectURL(sb, AppController.class).cancelar(
				this.getSiglaCompacta());
		addAcao(acoes, "cancel", "Cancelar", null, sb.toString(),
				podeCancelar(titular, lotaTitular),
				"Confirma o cancelamento deste conhecimento?", null, null);

		router.getRedirectURL(sb, AppController.class).anexar(
				this.getSiglaCompacta());
		addAcao(acoes, "attach", "Anexar Arquivo", null, sb.toString(),
				podeAnexar(titular, lotaTitular));

		router.getRedirectURL(sb, AppController.class).duplicar(
				this.getSiglaCompacta());
		addAcao(acoes, "arrow_divide", "Duplicar", null, sb.toString(),
				podeDuplicar(),
				"Esta operação criará um conhecimento com os mesmos dados do atual. "
						+ "Prosseguir?", null, null);

		// addAcao("printer.png","Visualizar Impressão",null,"", null, true,
		// null);

		return acoes;
	}

	protected void addAcao(SortedSet<GcAcaoVO> acoes, String icone,
			String nome, String nameSpace, String url, boolean pode) {
		addAcao(acoes, icone, nome, nameSpace, url, pode, null, null, null);
	}

	protected void addAcao(SortedSet<GcAcaoVO> acoes, String icone,
			String nome, String nameSpace, String url, boolean pode,
			String msgConfirmacao, String pre, String pos) {
		Map<String, Object> map = new HashMap<String, Object>();

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
		
		//Edson: usa um critério para impedir que marcas com um número muito grande de ocorrências
		//polua a tela
		Map<CpMarcador, GcMarca> marcasUmaPorMarcador = new HashMap<CpMarcador, GcMarca>();
		for (GcMarca m : marcas){
			//Se já tem uma marca elencada para exibição que se refira à pessoa ou lotação que
			//está visualizando, não mostra as demais que tenham o mesmo marcador
			GcMarca marcaJaEscolhida = marcasUmaPorMarcador.get(m.getCpMarcador());
			if (marcaJaEscolhida != null && 
					((marcaJaEscolhida.getDpPessoaIni() != null && marcaJaEscolhida.getDpPessoaIni().equivale(pess))
					|| (marcaJaEscolhida.getDpLotacaoIni() != null && marcaJaEscolhida.getDpLotacaoIni().equivale(lota)))){
				continue;
			}
			marcasUmaPorMarcador.put(m.getCpMarcador(), m);
		}
		
		StringBuilder sb = new StringBuilder();
        
        for (GcMarca mar : marcasUmaPorMarcador.values()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(mar.getCpMarcador().getDescrMarcador());
            if (mar.getDpPessoaIni() != null || mar.getDpLotacaoIni() != null){
            	sb.append(" (");
            	StringBuilder subSb = new StringBuilder();
            	if (mar.getDpPessoaIni() != null) {
            		String nome = mar.getDpPessoaIni().getDescricaoIniciaisMaiusculas();
            		subSb.append(nome.substring(0, nome.indexOf(" ")));
            	}
            	if (mar.getDpLotacaoIni() != null) {
            		if (subSb.length() > 0)
            			subSb.append(", ");
            		DpLotacao atual = mar.getDpLotacaoIni().getLotacaoAtual();
            		if (atual == null)
            			atual = mar.getDpLotacaoIni();
            		subSb.append(atual.getSigla());
            	}
            	sb.append(subSb);
            	sb.append(")");
            }
        }

        if (sb.length() == 0)
            return null;
        return sb.toString();
	}

	public String getConteudoHTML() throws Exception {
		if (this.arq == null || this.arq.getConteudo() == null)
			return null;

		String s = this.arq.getConteudoTXT();

		if (s.startsWith("<")) {
			return s;
		}

		s = s.replace(" # ", "\n# ").replace(" ## ", "\n## ")
				.replace(" ### ", "\n### ").replace(" #### ", "\n#### ")
				.replace(" * ", "\n* ").replace(" ** ", "\n** ")
				.replace(" *** ", "\n*** ").replace(" **** ", "\n**** ")
				.replace(" ==", "\n==");
		String fragment = WikiParser.renderXHTML(s);
		fragment = fragment.replace("&lt;&lt;&lt;Internal image(?): ",
				"<img src=\"/sigagc/app/baixar?id=").replace("&gt;&gt;&gt;",
				"\"/>");
		return fragment;
	}
	
	public boolean acessoExternoPublicoPermitido() {
		if(this.visualizacao.getId() == (int) GcAcesso.ACESSO_EXTERNO_PUBLICO)
			return true;
		return false;
	}

	public boolean acessoPermitido(DpPessoa titular, DpLotacao lotaTitular,
			long id) {
		switch ((int) id) {
		case (int) GcAcesso.ACESSO_EXTERNO_PUBLICO:
		case (int) GcAcesso.ACESSO_PUBLICO:
			return true;
		case (int) GcAcesso.ACESSO_ORGAO_USU:
			return ou.getIdOrgaoUsu().equals(
					titular.getOrgaoUsuario().getIdOrgaoUsu())
					|| ou.getIdOrgaoUsu().equals(
							lotaTitular.getOrgaoUsuario().getIdOrgaoUsu());
		case (int) GcAcesso.ACESSO_LOTACAO_E_SUPERIORES:
			for (DpLotacao lot = lotacao; lot != null; lot = lot
					.getLotacaoPai())
				if (lotaTitular.equivale(lot))
					return true;
			return false;
		case (int) GcAcesso.ACESSO_LOTACAO_E_INFERIORES:
			for (DpLotacao lot = lotaTitular; lot != null; lot = lot
					.getLotacaoPai())
				if (lotacao.equivale(lot))
					return true;
			return false;
		case (int) GcAcesso.ACESSO_LOTACAO:
			return lotacao.equivale(lotaTitular);
		case (int) GcAcesso.ACESSO_PESSOAL:
			return autor.equivale(titular);
		case (int) GcAcesso.ACESSO_LOTACAO_E_GRUPO:
			if (lotacao.equivale(lotaTitular))
				return true;
			try {
				for (CpPerfil perfil : Cp
						.getInstance()
						.getConf()
						.consultarPerfisPorPessoaELotacao(titular, lotaTitular,
								new Date())) {
					if (perfil.equivale(getGrupo()))
						return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}

	public boolean isContemArquivos() {
		if (movs == null)
			return false;
		for (GcMovimentacao m : movs)
			if (m.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO
					&& m.getMovCanceladora() == null)
				return true;
		return false;
	}

	public boolean fts(String[] palavras) {
		String s = arq.getTitulo() + " " + arq.getClassificacao() + " "
				+ new String(arq.getConteudo(), Charset.forName("utf-8"));
		s = s.toLowerCase();

		for (String palavra : palavras) {
			if (s.trim().length() == 0)
				continue;
			if (!s.contains(palavra))
				return false;
		}
		return true;
	}

	public String getGcTags() throws Exception {
		StringBuilder s = new StringBuilder();
		for (GcTag tag : getTags()) {
			s.append("&tags=" + URLEncoder.encode(tag.toString(), "UTF-8"));
		}
		return s.toString();
	}
	
	public static GcInformacao findBySigla(String sigla)
			throws NumberFormatException, Exception {
		return findBySigla(sigla, null);
	}

	/**
	 * Identifica uma informação através do seu código (JFRJ-GC-2013/00002 ou
	 * TMPGC-23)
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 **/
	public static GcInformacao findBySigla(String sigla, CpOrgaoUsuario ouDefault)
			throws NumberFormatException, Exception {
		sigla = sigla.trim().toUpperCase();

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
			mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
		}
		StringBuilder acronimos = new StringBuilder();
		for (String s : mapAcronimo.keySet()) {
			if (acronimos.length() > 0)
				acronimos.append("|");
			acronimos.append(s);
		}

		final Pattern p2 = Pattern.compile("^TMPGC-?([0-9]+)");
		final Pattern p1 = Pattern.compile("^(" + acronimos.toString()
				+ ")?-?(GC)?-?(?:(20[0-9]{2})/?)??([0-9]{1,5})$");
		final Matcher m2 = p2.matcher(sigla);
		final Matcher m1 = p1.matcher(sigla);

		GcInformacao info = null;

		if (m2.find()) {
			info = GcInformacao.AR.findById(Long.parseLong(m2.group(1)));
		} else if (m1.find()) {
			String ano = m1.group(3);
			String numero = m1.group(4);
			String orgao = m1.group(1);
			if (orgao == null && ouDefault != null)
				orgao = ouDefault.getAcronimoOrgaoUsu();
			if (ano == null)
				ano = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			
			info = GcInformacao.AR.find(
					"ano = ?1 and numero = ?2 and (ou.acronimoOrgaoUsu = ?3 or ou.siglaOrgaoUsu = ?4)",
					(Integer) Integer.parseInt(ano),
					(Integer) Integer.parseInt(numero), orgao, orgao)
					.first();
		} else throw new NumberFormatException("Formato de código inválido");

		if (info == null) {
			throw new AplicacaoException(
					"Não foi possível encontrar um conhecimento com o código "
							+ sigla + ". Favor verificá-lo.");
		} else
			return info;
	}
	
	public static GcInformacao findByTitulo(String titulo) throws Exception {
		try{
			String[] palavras = titulo.toUpperCase().split(" ");
			StringBuilder query = new StringBuilder("hisDtFim is null and visualizacao = " + GcAcesso.ACESSO_PUBLICO);
			for (String palavra : palavras){
				query.append(" and upper(arq.titulo) like '%");
				query.append(palavra);
				query.append("%' ");
			}
			List<GcInformacao> results = GcInformacao.AR.find(query.toString()).fetch();
			return results.size() == 1 ? results.get(0) : null;
		} catch(Exception e){
			return null;
		}
	}

	public Set<GcTag> getTags() {
		return this.tags;
	}

	public long getId() {
		return id;
	}

	public GcTipoInformacao getTipo() {
		return tipo;
	}

	public GcArquivo getInformacaoPai() {
		return informacaoPai;
	}

	public CpOrgaoUsuario getOu() {
		return ou;
	}

	public GcAcesso getVisualizacao() {
		return visualizacao;
	}

	public GcAcesso getEdicao() {
		return edicao;
	}

	public DpPessoa getAutor() {
		return autor;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public Date getElaboracaoFim() {
		return elaboracaoFim;
	}

	public Integer getAno() {
		return ano;
	}

	public Integer getNumero() {
		return numero;
	}

	public GcArquivo getArq() {
		return arq;
	}

	public java.util.List<GcMarca> getMarcas() {
		return marcas;
	}

	public SortedSet<GcMovimentacao> getMovs() {
		return movs;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public Date getHisDtFim() {
		return hisDtFim;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public CpPerfil getGrupo() {
		return grupo;
	}

	public void setGrupo(CpPerfil grupo) {
		this.grupo = grupo;
	}

	public void corrigirClassificacao() {
		if (arq == null)
			return;

		if (arq == null) {
			if (arq.getClassificacao() != null)
				arq.setClassificacao(null);
			return;
		}

		StringBuilder s = new StringBuilder();
		for (GcTag tag : getTags()) {
			if (s.length() > 0)
				s.append(", ");
			s.append(tag.toString());
		}
		arq.setClassificacao(s.toString());
	}
}
