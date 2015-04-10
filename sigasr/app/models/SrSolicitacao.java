package models;

import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import models.vo.ListaInclusaoAutomatica;
import notifiers.Correio;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.db.jpa.JPA;
import play.mvc.Router;
import util.Cronometro;
import util.SigaPlayCalendar;
import util.Util;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_SOLICITACAO", schema = "SIGASR")
public class SrSolicitacao extends HistoricoSuporte implements SrSelecionavel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SOLICITACAO_SEQ", name = "srSolicitacaoSeq")
	@GeneratedValue(generator = "srSolicitacaoSeq")
	@Column(name = "ID_SOLICITACAO")
	public Long idSolicitacao;

	@ManyToOne()
	@JoinColumn(name = "ID_SOLICITANTE")
	public DpPessoa solicitante;

	@ManyToOne
	@JoinColumn(name = "ID_INTERLOCUTOR")
	public DpPessoa interlocutor;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_SOLICITANTE")
	public DpLotacao lotaSolicitante;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	public DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	public DpLotacao lotaCadastrante;
	
	@ManyToOne
	@JoinColumn(name = "ID_TITULAR")
	public DpPessoa titular;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_TITULAR")
	public DpLotacao lotaTitular;
	
	@ManyToOne
	@JoinColumn(name = "ID_DESIGNACAO")
	public SrConfiguracao designacao;

	@Transient
	public DpLotacao atendenteNaoDesignado;

	@Transient
	private Cronometro cron;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	public CpOrgaoUsuario orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	public SrSolicitacao solicitacaoPai;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SR_SOLICITACAO_ACORDO", schema = "SIGASR", joinColumns = { @JoinColumn(name = "ID_SOLICITACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ACORDO") })
	public List<SrAcordo> acordos;

	@Enumerated
	public SrFormaAcompanhamento formaAcompanhamento;

	@Enumerated
	public SrMeioComunicacao meioComunicacao;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	public SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_ACAO")
	public SrAcao acao;

	@Lob
	@Column(name = "DESCR_SOLICITACAO", length = 8192)
	public String descrSolicitacao;

	@Enumerated
	public SrGravidade gravidade;

	@Enumerated
	public SrTendencia tendencia;

	@Enumerated
	public SrUrgencia urgencia;

	@Enumerated
	public SrPrioridade prioridade;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

	@Column(name = "DT_EDICAO_INI")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtIniEdicao;

	@Column(name = "DT_ORIGEM")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_COMPLEXO")
	public CpComplexo local;

	@Column(name = "TEL_PRINCIPAL")
	public String telPrincipal;

	@Transient
	public boolean fecharAoAbrir;

	@Transient
	public String motivoFechamentoAbertura;

	@Column(name = "NUM_SOLICITACAO")
	public Long numSolicitacao;

	@Column(name = "NUM_SEQUENCIA")
	public Long numSequencia;

	@Column(name = "DESCR_CODIGO")
	public String codigo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrSolicitacao solicitacaoInicial;

	@OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrSolicitacao> meuSolicitacaoHistoricoSet;

	@OneToMany(targetEntity = SrAtributoSolicitacao.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	protected List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@OneToMany(mappedBy = "solicitacaoPai", fetch = FetchType.LAZY)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacaoReferencia", fetch = FetchType.LAZY)
	private Set<SrMovimentacao> meuMovimentacaoReferenciaSet;

	// Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
	// entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
	@OneToMany(mappedBy = "solicitacao", fetch = FetchType.LAZY)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

	@Column(name = "FG_RASCUNHO")
	@Type(type = "yes_no")
	public Boolean rascunho;

	@Column(name = "FECHADO_AUTOMATICAMENTE")
	@Type(type = "yes_no")
	public Boolean fechadoAutomaticamente;

	public SrSolicitacao() {

	}

	public class SrTarefa {
		public SrAcao acao;
		public SrConfiguracao conf;
		
		public SrTarefa() {
			
		}
		public SrAcao getAcao() {
			return acao;
		}
		public void setAcao(SrAcao acao) {
			this.acao = acao;
		}
		public SrConfiguracao getConf() {
			return conf;
		}
		public void setConf(SrConfiguracao conf) {
			this.conf = conf;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((acao == null) ? 0 : acao.hashCode());
			result = prime * result + ((conf == null) ? 0 : conf.atendente.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SrTarefa other = (SrTarefa) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (acao == null) {
				if (other.acao != null)
					return false;
			} else if (!acao.equals(other.acao))
				return false;
			if (conf == null) {
				if (other.conf != null)
					return false;
			} else if (!conf.atendente.equals(other.conf.atendente))
				return false;
			return true;
		}
		private SrSolicitacao getOuterType() {
			return SrSolicitacao.this;
		}	
	}
	
	@Override
	public Long getId() {
		return idSolicitacao;
	}

	@Override
	public void setId(Long id) {
		this.idSolicitacao = id;
	}

	@Override
	public String getSigla() {
		return getCodigo();
	}

	@Override
	public void setSigla(String sigla) {
		sigla = sigla.trim().toUpperCase();
		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (Object ou : CpOrgaoUsuario.all().fetch()) {
			CpOrgaoUsuario cpOu = (CpOrgaoUsuario) ou;
			mapAcronimo.put(cpOu.getAcronimoOrgaoUsu(), cpOu);
		}
		String acronimos = "";
		for (String s : mapAcronimo.keySet()) {
			acronimos += "|" + s;
		}
		final Pattern p = Pattern
				.compile("^([A-Za-z0-9]{2}"
						+ acronimos
						+ ")?-?SR{1}-?(?:([0-9]{4})/?)??([0-9]{1,5})(?:[.]{1}([0-9]{1,3}))?$");
		final Matcher m = p.matcher(sigla);

		if (m.find()) {

			if (m.group(1) != null) {
				try {
					CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
					orgaoUsuario.setSiglaOrgaoUsu(m.group(1));
					orgaoUsuario = (CpOrgaoUsuario) JPA
							.em()
							.createQuery(
									"from CpOrgaoUsuario where acronimoOrgaoUsu = '"
											+ m.group(1) + "'")
							.getSingleResult();
					this.orgaoUsuario = orgaoUsuario;
				} catch (final Exception ce) {

				}
			}

			if (m.group(2) != null) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, Integer.valueOf(m.group(2)));
				c1.set(Calendar.DAY_OF_YEAR, 1);
				this.dtReg = c1.getTime();
			} else
				this.dtReg = new Date();

			if (m.group(3) != null)
				numSolicitacao = Long.valueOf(m.group(3));

			if (m.group(4) != null)
				numSequencia = Long.valueOf(m.group(4));

		}

	}

	@Override
	public String getDescricao() {
		if (descrSolicitacao == null || descrSolicitacao.length() == 0) {
			if (isFilha())
				return solicitacaoPai.getDescricao();
			else
				return "Descrição não informada";
		} else
			return descrSolicitacao;
	}

	public List<SrAtributoSolicitacao> getMeuAtributoSolicitacaoSet() {
		if (meuAtributoSolicitacaoSet == null
				|| meuAtributoSolicitacaoSet.size() == 0) {
			if (isFilha())
				return solicitacaoPai.getMeuAtributoSolicitacaoSet();
		}

		return meuAtributoSolicitacaoSet;

	}

	public void setMeuAtributoSolicitacaoSet(
			List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet) {
		this.meuAtributoSolicitacaoSet = meuAtributoSolicitacaoSet;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descrSolicitacao = descricao;
	}

	public Boolean isFechadoAutomaticamente() {
		return fechadoAutomaticamente != null ? fechadoAutomaticamente : false;
	}

	public void setFechadoAutomaticamente(Boolean fechadoAutomaticamente) {
		this.fechadoAutomaticamente = fechadoAutomaticamente;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) throws Exception {
		setSigla(sigla);
		if (orgaoUsuario == null && lotaTitular != null)
			orgaoUsuario = lotaTitular.getOrgaoUsuario();

		String query = "from SrSolicitacao where hisDtFim is null ";

		if (orgaoUsuario != null) {
			query += " and orgaoUsuario.idOrgaoUsu = "
					+ orgaoUsuario.getIdOrgaoUsu();
		}
		if (dtReg != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(dtReg);
			int year = c1.get(Calendar.YEAR);
			query += " and dtReg between to_date('01/01/" + year
					+ " 00:01', 'dd/mm/yyyy HH24:mi') and to_date('31/12/"
					+ year + " 23:59','dd/mm/yyyy HH24:mi')";
		}
		if (numSolicitacao != null)
			query += " and numSolicitacao = " + numSolicitacao;
		if (numSequencia == null)
			query += " and numSequencia is null";
		else
			query += " and numSequencia = " + numSequencia;

		SrSolicitacao sol = (SrSolicitacao) JPA.em().createQuery(query)
				.getSingleResult();

		return sol;
	}

	@Override
	public List<? extends SrSelecionavel> buscar() throws Exception {
		return null;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void atualizarCodigo() {
		if (isRascunho() || numSolicitacao == null) {
			codigo = "TMPSR-"
					+ (solicitacaoInicial != null ? solicitacaoInicial.idSolicitacao
							: idSolicitacao);
			return;
		}

		if (solicitacaoPai != null) {
			codigo = solicitacaoPai.getCodigo() + getNumSequenciaString();
			return;
		}

		if (numSolicitacao == null) {
			codigo = "";
			return;
		}

		String numString = numSolicitacao.toString();

		while (numString.length() < 5)
			numString = "0" + numString;

		codigo = orgaoUsuario.getAcronimoOrgaoUsu() + "-SR-" + getAnoEmissao()
				+ "/" + numString;
	}

	public List<SrAtributoSolicitacao> getAtributoSolicitacaoSet() {
		if (meuAtributoSolicitacaoSet == null || meuAtributoSolicitacaoSet.isEmpty()) {
			if (isFilha())
				return solicitacaoPai.getAtributoSolicitacaoSet();
			else
				return new ArrayList<SrAtributoSolicitacao>();
		} else
			return meuAtributoSolicitacaoSet;
	}

	public String getDtRegString() {
		SigaPlayCalendar cal = new SigaPlayCalendar();
		cal.setTime(dtReg);
		return "<!--" + dtReg.getTime() + "-->" + cal.getTempoTranscorridoString(false);
	}

	public String getAtributosString() {
		String s = "";
		for (SrAtributoSolicitacao att : getAtributoSolicitacaoSet()) {
			if (att.valorAtributoSolicitacao != null)
				s += att.atributo.nomeAtributo + ": "
						+ att.valorAtributoSolicitacao + ". ";
		}
		return s;
	}

	// Edson: Necess�rio porque nao h� binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public int getGUT() {
		return gravidade.nivelGravidade * urgencia.nivelUrgencia
				* tendencia.nivelTendencia;
	}

	public String getGUTString() {
		return gravidade.descrGravidade + " " + urgencia.descrUrgencia + " "
				+ tendencia.descrTendencia;
	}

	public String getGUTPercent() {
		return (int) ((getGUT() / 125.0) * 100) + "%";
	}

	public void associarPrioridadePeloGUT() {
		int valorGUT = getGUT();
		if (Util.isbetween(1, 24, valorGUT))
			prioridade = SrPrioridade.PLANEJADO;
		else if (Util.isbetween(25, 49, valorGUT))
			prioridade = SrPrioridade.BAIXO;
		else if (Util.isbetween(50, 74, valorGUT))
			prioridade = SrPrioridade.MEDIO;
		else if (Util.isbetween(75, 99, valorGUT))
			prioridade = SrPrioridade.ALTO;
		else if (Util.isbetween(100, 125, valorGUT))
			prioridade = SrPrioridade.IMEDIATO;
	}

	public String getDtRegDDMMYYYYHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtReg);
		}
		return "";
	}

	public String getDtRegDDMMYYYY() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(dtReg);
		}
		return "";
	}

	public String getDtRegHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(dtReg);
		}
		return "";
	}

	public Long getProximoNumero() {
		if (orgaoUsuario == null)
			return 0L;
		Long num = find(
				"select max(numSolicitacao)+1 from SrSolicitacao where orgaoUsuario.idOrgaoUsu = "
						+ orgaoUsuario.getIdOrgaoUsu()).first();
		return (num != null) ? num : 1;
	}

	public String getAnoEmissao() {
		if (dtReg == null)
			return null;
		return new SimpleDateFormat("yyyy").format(dtReg);
	}

	public String getNumSequenciaString() {
		if (numSequencia == null)
			return null;
		return "." + (numSequencia < 10 ? "0" : "") + numSequencia.toString();
	}

	public List<SrSolicitacao> getHistoricoSolicitacao() {
		if (solicitacaoInicial != null)
			return solicitacaoInicial.meuSolicitacaoHistoricoSet;
		return null;
	}

	public SrSolicitacao getSolicitacaoAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrSolicitacao> sols = getHistoricoSolicitacao();
		if (sols == null)
			return null;
		return sols.get(0);

	}

	public SrMovimentacao getUltimoAndamento() {
		return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_ANDAMENTO);
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoMesmoSeCanceladaTodoOContexto() {
		for (SrMovimentacao movimentacao : getMovimentacaoSetComCanceladosTodoOContexto())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoQuePossuaDescricao() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.descrMovimentacao != null
					&& mov.descrMovimentacao.length() > 0)
				return mov;
		}
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoPorTipo(Long idTpMov) {
		for (SrMovimentacao movimentacao : getMovimentacaoSetPorTipo(idTpMov))
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoCancelavel() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.numSequencia > 1
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_AVALIACAO)
				return mov;
		}
		return null;
	}

	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false, null, false, false, false, false);
	}

	public Set<SrMovimentacao> getMovimentacaoSetPorTipo(Long tipoMov) {
		return getMovimentacaoSet(false, tipoMov, false, false, false, false);
	}

	public Set<SrMovimentacao> getMovimentacaoReferenciaSetPorTipo(Long tipoMov) {
		return getMovimentacaoSet(false, tipoMov, false, false, false, true);
	}

	public Set<SrMovimentacao> getMovimentacaoSetComCancelados() {
		return getMovimentacaoSet(true, null, false, false, false, false);
	}

	public Set<SrMovimentacao> getMovimentacaoSetComCanceladosTodoOContexto() {
		return getMovimentacaoSet(true, null, false, true, false, false);
	}

	public Set<SrMovimentacao> getMovimentacaoSetOrdemCrescente() {
		return getMovimentacaoSet(false, null, true, false, false, false);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCanceladas,
			Long tipoMov, boolean ordemCrescente, boolean todoOContexto,
			boolean apenasPrincipais, boolean inversoJPA) {

		List<Long> tiposPrincipais = Arrays.asList(TIPO_MOVIMENTACAO_ANDAMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO, TIPO_MOVIMENTACAO_AVALIACAO, TIPO_MOVIMENTACAO_ESCALONAMENTO);

		return getMovimentacaoSet(considerarCanceladas, tipoMov,
				ordemCrescente, todoOContexto, apenasPrincipais, inversoJPA,
				tiposPrincipais);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCanceladas,
			Long tipoMov, boolean ordemCrescente, boolean todoOContexto,
			boolean apenasPrincipais, boolean inversoJPA,
			List<Long> tiposPrincipais) {

		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new SrMovimentacaoComparator(ordemCrescente));

		Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
		if (todoOContexto) {
			solsAConsiderar.addAll(getPaiDaArvore()
					.getSolicitacaoFilhaSetRecursivo());
		} else
			solsAConsiderar.add(this);

		for (SrSolicitacao sol : solsAConsiderar) {
			if (sol.solicitacaoInicial != null)
				for (SrSolicitacao instancia : sol.getHistoricoSolicitacao()) {
					Set<SrMovimentacao> movSet = inversoJPA ? instancia.meuMovimentacaoReferenciaSet
							: instancia.meuMovimentacaoSet;
					if (movSet != null)
						for (SrMovimentacao movimentacao : movSet) {
							if (!considerarCanceladas
									&& movimentacao.isCanceladoOuCancelador())
								continue;
							if (tipoMov != null
									&& movimentacao.tipoMov.idTipoMov != tipoMov)
								continue;
							if (apenasPrincipais
									&& !tiposPrincipais
											.contains(movimentacao.tipoMov.idTipoMov))
								continue;

							listaCompleta.add(movimentacao);
						}
				}
		}
		return listaCompleta;
	}

	public boolean jaFoiDesignada() {
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;
		}
		return false;
	}

	public DpLotacao getLotaAtendente() {
		if (getUltimaMovimentacao() != null)
			return getUltimaMovimentacao().lotaAtendente;
		else
			return null;
	}

	public DpPessoa getAtendente() {
		return getUltimaMovimentacao().atendente;
	}

	public boolean isFilha() {
		return (this.solicitacaoPai != null);
	}

	public Set<SrMovimentacao> getPendenciasEmAberto() {
		Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		Set<SrMovimentacao> setPendentes = new HashSet<SrMovimentacao>();

		for (SrMovimentacao ini : setIni) {
			if ((!ini.isFinalizada())
					&& (ini.dtAgenda == null || ini.dtAgenda.after(new Date())))
				setPendentes.add(ini);
		}

		return setPendentes;
	}

	// Edson: ver comentario abaixo, em getTiposAtributoAssociados()
	public HashMap<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados()
			throws Exception {
		HashMap<Long, Boolean> map = new HashMap<Long, Boolean>();
		getAtributoAssociados(map);
		return map;
	}

	public List<SrAtributo> getAtributoAssociados() throws Exception {
		return getAtributoAssociados(null);
	}

	// Edson: isso esta esquisito. A funcao esta praticamente com dois retornos.
	// Talvez ficasse melhor se o SrAtributo ja tivesse a informacao sobre
	// a obrigatoriedade dele
	private List<SrAtributo> getAtributoAssociados(HashMap<Long, Boolean> map)
			throws Exception {
		List<SrAtributo> listaFinal = new ArrayList<SrAtributo>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;

		for (SrAtributo t : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
			confFiltro.atributo = t;
			SrConfiguracao conf = SrConfiguracao.buscarAssociacao(confFiltro);
			if (conf != null) {
				listaFinal.add(t);
				if (map != null)
					map.put(t.idAtributo, conf.atributoObrigatorio);
			}
		}

		return listaFinal;
	}

	public DpLotacao getUltimoAtendenteEtapaAtendimento() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ANDAMENTO)
				return mov.lotaAtendente;
		}
		return null;
	}

	// Edson: poderia também guardar num HashMap transiente e, ao salvar(),
	// mandar criar os atributos, caso se quisesse permitir um
	// solicitacao.getAtributoSet().put...
	public void setAtributoSolicitacaoMap(
			HashMap<Long, String> atributosSolicitacao) {
		if (atributosSolicitacao != null) {
			meuAtributoSolicitacaoSet = new ArrayList<SrAtributoSolicitacao>();
			for (Long idAtt : atributosSolicitacao.keySet()) {
				SrAtributo att = SrAtributo.findById(idAtt);
				SrAtributoSolicitacao attSolicitacao = new SrAtributoSolicitacao(
						att, atributosSolicitacao.get(idAtt), this);
				meuAtributoSolicitacaoSet.add(attSolicitacao);
			}
		}
	}

	public HashMap<Long, String> getAtributoSolicitacaoMap() {
		HashMap<Long, String> map = new LinkedHashMap<Long, String>(); // Para
																		// manter
																		// a
																		// ordem
																		// de
																		// insercao
		if (meuAtributoSolicitacaoSet != null)
			for (SrAtributoSolicitacao att : meuAtributoSolicitacaoSet) {
				map.put(att.atributo.idAtributo, att.valorAtributoSolicitacao);
			}
		return map;
	}

	private Set<SrSolicitacao> getSolicitacaoFilhaSet() {
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao s1, SrSolicitacao s2) {
						return s1.numSequencia.compareTo(s2.numSequencia);
					}
				});

		if (solicitacaoInicial != null) {
			for (SrSolicitacao sol : getHistoricoSolicitacao()) {
				if (sol.meuSolicitacaoFilhaSet != null)
					for (SrSolicitacao filha : sol.meuSolicitacaoFilhaSet)
						if (filha.getHisDtFim() == null)
							listaCompleta.add(filha);
			}
		}
		return listaCompleta;
	}

	public Set<SrSolicitacao> getSolicitacaoFilhaSetRecursivo() {
		Set<SrSolicitacao> listaCompleta = new LinkedHashSet<SrSolicitacao>();
		listaCompleta.add(this);
		for (SrSolicitacao filha : getSolicitacaoFilhaSet())
			listaCompleta.addAll(filha.getSolicitacaoFilhaSetRecursivo());
		return listaCompleta;
	}

	public boolean isPai() {
		return getSolicitacaoFilhaSet() != null
				&& getSolicitacaoFilhaSet().size() > 0;
	}

	public Set<SrMarca> getMarcaSet() {
		TreeSet<SrMarca> listaCompleta = new TreeSet<SrMarca>();
		if (solicitacaoInicial != null)
			for (SrSolicitacao sol : getHistoricoSolicitacao())
				if (sol.meuMarcaSet != null)
					listaCompleta.addAll(sol.meuMarcaSet);
		return listaCompleta;
	}

	public Set<SrMarca> getMarcaSetAtivas() {
		Set<SrMarca> set = new TreeSet<SrMarca>();
		Date agora = new Date();
		for (SrMarca m : getMarcaSet()) {
			if ((m.getDtIniMarca() == null || m.getDtIniMarca().before(agora))
					&& (m.getDtFimMarca() == null || m.getDtFimMarca().after(
							agora)))
				set.add(m);
		}
		return set;
	}

	public boolean isJuntada() {
		return sofreuMov(TIPO_MOVIMENTACAO_JUNTADA,
				TIPO_MOVIMENTACAO_DESENTRANHAMENTO);
	}

	public boolean isEditado() {
		return !idSolicitacao.equals(getHisIdIni());
	}

	public boolean isCancelado() {
		return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) != null;
	}

	public Boolean isRascunho() {
		return rascunho != null ? rascunho : false;
	}

	public boolean isFechado() {
		return sofreuMov(TIPO_MOVIMENTACAO_FECHAMENTO,
				TIPO_MOVIMENTACAO_REABERTURA) && !isCancelado();
	}

	public boolean isPendente() {
		Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		for (SrMovimentacao ini : setIni) {
			if ((!ini.isFinalizada())
					&& (ini.dtAgenda == null || ini.dtAgenda.after(new Date())))

				return true;
		}
		return false;
	}

	public boolean isEmAtendimento() {
		long idsTpsMovs[] = { TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_REABERTURA };
		return sofreuMov(idsTpsMovs, TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEscalonada() {
		return sofreuMov(
				SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO,
				SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
	}

	public boolean isAFechar() {
		Set<SrSolicitacao> filhas = getSolicitacaoFilhaSet();
		if (filhas.size() == 0)
			return false;
		for (SrSolicitacao filha : filhas) {
			if (!filha.isFechado() && !filha.isCancelado())
				return false;
		}
		return true;
	}

	public boolean sofreuMov(long idTpMov, long... idsTpsReversores) {
		return sofreuMov(new long[] { idTpMov }, idsTpsReversores);
	}

	public boolean sofreuMov(long[] idsTpsMovs, long... idsTpsReversores) {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			for (long idTpMov : idsTpsMovs)
				if (mov.tipoMov.idTipoMov == idTpMov)
					return true;
				else
					for (long idTpReversor : idsTpsReversores)
						if (mov.tipoMov.idTipoMov == idTpReversor)
							return false;
		}
		return false;
	}

	public SrSolicitacao getSolicitacaoPrincipal() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO)
				return null;
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				return mov.solicitacaoReferencia;
		}
		return null;
	}

	public boolean estaCom(DpPessoa pess, DpLotacao lota) {
		SrMovimentacao ultMov = getUltimaMovimentacao();
		SrMovimentacao ultMovDoPai = null;
		if (isFilha())
			ultMovDoPai = this.solicitacaoPai.getUltimaMovimentacao();
		if (isRascunho())
			return foiCadastradaPor(pess, lota) || foiSolicitadaPor(pess, lota);
		return (ultMov != null && ((ultMov.atendente != null && pess != null && ultMov.atendente.equivale(pess)) 
					|| (ultMov.lotaAtendente != null && ultMov.lotaAtendente.equivale(lota))))

				|| (ultMovDoPai != null && ((ultMovDoPai.atendente != null && ultMovDoPai.atendente
						.equivale(pess)) || (ultMovDoPai.lotaAtendente != null && ultMovDoPai.lotaAtendente
						.equivale(lota))));

	}

	public boolean foiSolicitadaPor(DpPessoa pess, DpLotacao lota) {
		return (solicitante.equivale(pess) || lotaSolicitante.equivale(lota));
	}

	public boolean foiCadastradaPor(DpPessoa pess, DpLotacao lota) {
		return (cadastrante.equivale(pess) || (lotaTitular != null && lotaTitular.equivale(lota)));
	}

	public boolean isParteDeArvore() {
		return solicitacaoPai != null
				|| (getSolicitacaoFilhaSet() != null && !getSolicitacaoFilhaSet()
						.isEmpty());
	}

	public SrSolicitacao getPaiDaArvore() {
		SrSolicitacao pai = this;
		while (pai.solicitacaoPai != null) {
			pai = pai.solicitacaoPai;
		}
		return pai;
	}

	public boolean temArquivosAnexos() {
		return getArquivoAnexoNaCriacao() != null
				|| getMovimentacoesAnexacao().size() > 0;
	}

	public SrArquivo getArquivoAnexoNaCriacao() {
		if (solicitacaoInicial != null)
			for (SrSolicitacao sol : getHistoricoSolicitacao())
				if (sol.arquivo != null)

					return sol.arquivo;
		return null;
	}

	public Set<SrMovimentacao> getMovimentacoesAnexacao() {
		return getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO);
	}

	public boolean podeEscalonar(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota) && isEmAtendimento();
	}

	public boolean podeJuntar(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota) && (isEmAtendimento() || isPendente())
				&& !isJuntada();
	}

	public boolean podeDesentranhar(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota) && isJuntada();
	}

	public boolean podeVincular(DpPessoa titular, DpLotacao lotaTitular) {
		return !isRascunho();
	}

	public boolean podeDesfazerMovimentacao(DpPessoa pess, DpLotacao lota) {
		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		if (ultCancelavel == null || ultCancelavel.cadastrante == null)
			return false;
		return ultCancelavel.lotaCadastrante.equivale(lota);
	}

	public boolean podeEditar(DpPessoa pess, DpLotacao lota) {
		return (estaCom(pess, lota) || isEmListaPertencenteA(lota))
				&& isRascunho()
				&& (!jaFoiDesignada());
	}

	public boolean podePriorizar(DpPessoa pess, DpLotacao lota) {
		return podeEditar(pess, lota);
	}

	public boolean podeAbrirJaFechando(DpPessoa pess, DpLotacao lota) {
		return false;
	}

	public boolean podeFechar(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota)
				&& (isEmAtendimento());
	}

	public boolean podeExcluir(DpPessoa pess, DpLotacao lota) {
		return foiCadastradaPor(pess, lota) && isRascunho();
	}

	public boolean podeCancelar(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota)
				&& isEmAtendimento();
	}

	public boolean podeDeixarPendente(DpPessoa pess, DpLotacao lota) {
		return isRascunho()
				|| ((isEmAtendimento() || isPendente()) && estaCom(pess, lota));
	}

	public boolean podeAlterarPrazo(DpPessoa pess, DpLotacao lota) {
		return !isRascunho() && !isFechado() && estaCom(pess, lota);
	}

	public boolean podeTerminarPendencia(DpPessoa pess, DpLotacao lota) {
		return isPendente() && estaCom(pess, lota);
	}

	public boolean podeReabrir(DpPessoa pess, DpLotacao lota) {
		return isFechado()
				&& (estaCom(pess, lota) || foiCadastradaPor(pess, lota) || foiSolicitadaPor(
					pess, lota));
	}

	public boolean podeAnexarArquivo(DpPessoa pess, DpLotacao lota) {
		return (isEmAtendimento() || isPendente() || isRascunho());
	}

	public boolean podeImprimirTermoAtendimento(DpPessoa pess, DpLotacao lota) {
		return isEmAtendimento() && estaCom(pess, lota);
	}

	public boolean podeIncluirEmLista(DpPessoa pess, DpLotacao lota) {
		return isEmAtendimento();
	}

	public boolean podeTrocarAtendente(DpPessoa pess, DpLotacao lota) {
		return estaCom(pess, lota) && isEmAtendimento();
	}

	public boolean podeResponderPesquisa(DpPessoa pess, DpLotacao lota)
			throws Exception {

		if (!isFechado() || !foiSolicitadaPor(pess, lota)
				/*|| !temPesquisaSatisfacao()*/)
			return false;

		for (SrMovimentacao mov : getMovimentacaoSet())
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;

		return false;

	}

	public boolean podeFecharPaiAutomatico() {
		return isFilha()
				&& solicitacaoPai.getSolicitacaoAtual().isFechadoAutomaticamente()
				&& solicitacaoPai.isAFechar();
	}

	@SuppressWarnings("unchecked")
	public SrSolicitacao deduzirLocalRamalEMeioContato() {

		if (solicitante == null)
			return this;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		// Tenta buscar a ultima aberta pelo solicitante
		String queryString = "from SrSolicitacao sol where sol.idSolicitacao = ("
				+ "	select max(idSolicitacao) from SrSolicitacao "
				+ "	where solicitante.idPessoa in ("
				+ "		select idPessoa from DpPessoa "
				+ "		where idPessoaIni = "
				+ solicitante.getIdPessoaIni() + "))";
		List<SrSolicitacao> listaProvisoria = JPA.em().createQuery(queryString)
				.getResultList();
		SrSolicitacao ultima = null;
		if (listaProvisoria != null && listaProvisoria.size() > 0)
			ultima = listaProvisoria.get(0);

		// Tenta buscar a ultima aberta pela lotacao dele
		if (ultima == null && lotaSolicitante != null) {
			queryString = "from SrSolicitacao sol where sol.idSolicitacao = ("
					+ "	select max(idSolicitacao) from SrSolicitacao "
					+ "	where lotaSolicitante.idLotacao in ("
					+ "		select idLotacao from DpLotacao "
					+ "		where idLotacaoIni = "
					+ lotaSolicitante.getIdLotacaoIni() + "))";
			listaProvisoria = JPA.em().createQuery(queryString).getResultList();
			if (listaProvisoria != null && listaProvisoria.size() > 0)
				ultima = listaProvisoria.get(0);
		}

		if (ultima != null) {
			telPrincipal = ultima.telPrincipal;
			local = ultima.local;
			meioComunicacao = ultima.meioComunicacao;
		} else {
			telPrincipal = "";
			local = null;
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public List<CpComplexo> getLocaisDisponiveis() {
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		if (solicitante != null)
			locais = JPA
					.em()
					.createQuery(
							"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
									+ solicitante.getOrgaoUsuario()
											.getIdOrgaoUsu()).getResultList();
		return locais;
	}

	public List<SrItemConfiguracao> getItensDisponiveis() throws Exception {
		if (solicitante == null)
			return null;

		List<SrItemConfiguracao> listaTodosItens = new ArrayList<SrItemConfiguracao>();
		List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();
		
		List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
		listaTodosItens = SrItemConfiguracao.listar(false);

		for (SrItemConfiguracao i : listaTodosItens) {
			if (!i.isEspecifico())
				continue;
			for (SrConfiguracao c : listaPessoasAConsiderar) 
				if (!listaFinal.contains(i)) {
					
					c.itemConfiguracaoFiltro = i;
					
					if (SrConfiguracao.buscarDesignacao(c,
							new int[] { SrConfiguracaoBL.ACAO}) != null){
						listaFinal.add(i);
						SrItemConfiguracao itemPai = i.pai;
						while (itemPai != null) {
							if (!listaFinal.contains(itemPai))
								listaFinal.add(itemPai);
							itemPai = itemPai.pai;
						}
					}
				}
		}

		Collections.sort(listaFinal, new SrItemConfiguracaoComparator());

		return listaFinal;
	}

	public List<SrAcao> getAcoesDisponiveis() throws Exception {
		//Map<SrAcao, SrConfiguracao> acoesEAtendentes = getAcoesDisponiveisComAtendente();
		//return acoesEAtendentes != null ? new ArrayList<SrAcao>(acoesEAtendentes.keySet()) : null;
		List<SrTarefa> acoesEAtendentes = getAcoesDisponiveisComAtendente();
		List<SrAcao> acoes = new ArrayList<SrAcao>();
		
		if(acoesEAtendentes == null)
			return null;
		for (SrTarefa t: acoesEAtendentes) 
			acoes.add(t.acao);
		return acoes;
	}

	public List<SrTarefa> getAcoesDisponiveisComAtendente()
			throws Exception {

		if (solicitante == null || itemConfiguracao == null)
			return null;

		List<SrTarefa> listaFinal = new ArrayList<SrTarefa>();	
		Set<SrTarefa> setTerafa = new HashSet<SrTarefa>();
		List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarDesignacoes();
		SrTarefa tarefa = null;

		for (SrAcao a : SrAcao.listar(false)) {
			if (!a.isEspecifico())
				continue;
			for (SrConfiguracao c : listaPessoasAConsiderar) {
				c.itemConfiguracaoFiltro = itemConfiguracao;
				c.acaoFiltro = a;
				c.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));

				List<SrConfiguracao> confs = SrConfiguracao.listar(c, new int[] { SrConfiguracaoBL.ATENDENTE});
				if (confs.size() > 0) 
					for (SrConfiguracao conf : confs) {
						tarefa = this.new SrTarefa();
						tarefa.acao = a;
						tarefa.conf = conf;
						setTerafa.add(tarefa);
					}
			}
		}
		listaFinal.addAll(setTerafa);
		return listaFinal;
	}
	
	public Map<SrAcao, List<SrTarefa>> getAcoesEAtendentes() throws Exception {
		Map<SrAcao, List<SrTarefa>> acoesEAtendentesFinal = new TreeMap<SrAcao, List<SrTarefa>>(new Comparator<SrAcao>() {
	        @Override
	        public int compare(SrAcao  a1, SrAcao a2) {
				int i = a1.tituloAcao.compareTo(a2.tituloAcao);
				if (i != 0)
					return i;
				return a1.idAcao.compareTo(a2.idAcao);
	        }
	        });

		List<SrTarefa> acoesEAtendentes = getAcoesDisponiveisComAtendente();
		if (acoesEAtendentes != null && this.itemConfiguracao != null){
			
			if (acoesEAtendentes.size() == 1)
				this.acao = acoesEAtendentes.get(0).acao;
			else this.acao = null;
			
			for (SrTarefa t : acoesEAtendentes){
				List<SrTarefa> tarefas = acoesEAtendentesFinal.get(t.getAcao().pai);
				if (tarefas == null)
					tarefas = new ArrayList<SrTarefa>();
				tarefas.add(t);
				acoesEAtendentesFinal.put(t.getAcao().pai, tarefas);
			}
			
			//Edson: melhor se fosse um SortedSet
			for (List<SrTarefa> tarefas : acoesEAtendentesFinal.values()){
				Collections.sort(tarefas , new Comparator<SrTarefa>() {
			        @Override
			        public int compare(SrTarefa  o1, SrTarefa o2) {
						int i = o1.acao.tituloAcao.compareTo(o2.acao.tituloAcao);
						if (i != 0)
							return i;
						return o1.acao.idAcao.compareTo(o2.acao.idAcao);
			        }
			    });
			}
						
		}
		
		return acoesEAtendentesFinal;
	}
	
	@SuppressWarnings("serial")
	public SortedSet<SrOperacao> operacoes(final DpPessoa pess, final DpLotacao lota)
					throws Exception {
		SortedSet<SrOperacao> operacoes = new TreeSet<SrOperacao>() {
			@Override
			public boolean add(SrOperacao e) {
				// Edson: ser� que essas coisas poderiam estar dentro do
				// SrOperacao?
				if (e.params == null)
					e.params = new HashMap<String, Object>();
				e.params.put("id", idSolicitacao);

				if (!e.isModal())
					e.url = Router.reverse(e.url, e.params).url;
				if (!e.pode)
					return false;
				return super.add(e);
			}
		};

		operacoes.add(new SrOperacao("pencil", "Editar", podeEditar(
				pess, lota), "Application.editar"));

		operacoes.add(new SrOperacao("table_relationship", "Vincular",
				podeVincular(pess, lota),
				"vincular", "modal=true"));

		operacoes.add(new SrOperacao("arrow_divide",
				"Escalonar", podeEscalonar(pess, lota), 
				"escalonar", "modal=true")); 

		operacoes.add(new SrOperacao("arrow_join", "Juntar",
				podeJuntar(pess, lota),
				"juntar", "modal=true"));
		
		operacoes.add(new SrOperacao("arrow_out", "Desentranhar",
				podeDesentranhar(pess, lota), "desentranhar", "modal=true"));
		
		operacoes.add(new SrOperacao("text_list_numbers", "Incluir em Lista",
				podeIncluirEmLista(pess, lota), "incluirEmLista",
				"modal=true"));
		
		operacoes.add(new SrOperacao("lock", "Fechar", podeFechar(pess, lota
				), "fechar", "modal=true"));

		operacoes.add(new SrOperacao("script_edit", "Responder Pesquisa",
				podeResponderPesquisa(pess, lota),
				"responderPesquisa", "modal=true"));

		operacoes.add(new SrOperacao("cross", "Cancelar Solicitação",
				podeCancelar(pess, lota), "Application.cancelar"));

		operacoes.add(new SrOperacao("lock_open", "Reabrir", podeReabrir(
				pess, lota), "Application.reabrir"));

		operacoes.add(new SrOperacao("clock_pause", "Incluir Pendência",
				podeDeixarPendente(pess, lota), "pendencia",
				"modal=true"));

		/*
		 * operacoes.add(new SrOperacao("clock_edit", "Alterar Prazo",
		 * podeAlterarPrazo(lotaTitular, titular), "alterarPrazo",
		 * "modal=true"));
		 */
		operacoes.add(new SrOperacao("cross", "Excluir", "Application.excluir",
				podeExcluir(pess, lota),
				"Deseja realmente excluir esta solicitação?", null, "", ""));

		operacoes.add(new SrOperacao("attach", "Anexar Arquivo",
				podeAnexarArquivo(pess, lota), "anexarArquivo",
				"modal=true"));

		operacoes.add(new SrOperacao("printer", "Termo de Atendimento",
				podeImprimirTermoAtendimento(pess, lota),
				"Application.termoAtendimento", "popup=true"));

		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		operacoes.add(new SrOperacao("cancel", "Desfazer "
				+ (ultCancelavel != null ? ultCancelavel.tipoMov.nome : ""),
				podeDesfazerMovimentacao(pess, lota),
				"Application.desfazerUltimaMovimentacao"));

		return operacoes;
	}

	public void salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante, 
			DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		salvar();
	}

	@SuppressWarnings("unused")
	public void salvar() throws Exception {

		checarEPreencherCampos();
		//Edson: Ver por que isto est� sendo necess�rio. Sem isso, ap�s o salvar(),
		//ocorre LazyIniException ao tentar acessar esses meuMovimentacaoSet's
		if (solicitacaoInicial != null)
			for (SrSolicitacao s : solicitacaoInicial.meuSolicitacaoHistoricoSet) {
				for (SrMovimentacao m : s.meuMovimentacaoSet) {
				}
			}

		super.salvar();

		// Edson: melhorar isto, pra nao precisar salvar novamente

		if (isRascunho()) {
			atualizarCodigo();
			save();
		}

		if (!isRascunho() && !jaFoiDesignada()) {

			if (fecharAoAbrir)
				fechar(cadastrante, lotaCadastrante, titular, lotaTitular, motivoFechamentoAbertura);
			else
				iniciarAtendimento(cadastrante, lotaCadastrante, titular, lotaTitular);

			incluirEmListasAutomaticas();

			if (!isEditado()
					&& formaAcompanhamento != SrFormaAcompanhamento.ABERTURA_FECHAMENTO)
				Correio.notificarAbertura(this);
		} else
			atualizarMarcas();
	}

	private void incluirEmListasAutomaticas() throws Exception {
		for (ListaInclusaoAutomatica dadosInclusao : getListasParaInclusaoAutomatica(lotaCadastrante)) {
			incluirEmLista(dadosInclusao.getLista(), cadastrante, lotaCadastrante, dadosInclusao.getPrioridadeNaLista(), Boolean.FALSE);
		}
	}

	public void excluir() throws Exception {
		finalizar();
		for (SrMarca e : getMarcaSet()) {
			e.solicitacao.meuMarcaSet.remove(e);
			e.delete();
		}
	}

	public void atualizarAcordos() throws Exception {
		acordos = new ArrayList<SrAcordo>();
				
		List<SrConfiguracao> filtrosConf = new ArrayList<SrConfiguracao>();
		
		SrConfiguracao confSolicitante = new SrConfiguracao();
		confSolicitante.setDpPessoa(solicitante);
		confSolicitante.setLotacao(lotaSolicitante);
		confSolicitante.setComplexo(local);
		confSolicitante.setBuscarPorPerfis(true);
		filtrosConf.add(confSolicitante);
				
		if (titular != null){		
			SrConfiguracao confTitular = new SrConfiguracao();
			confTitular.setDpPessoa(titular);
			confTitular.setLotacao(lotaTitular);
			confTitular.setComplexo(local);
			confTitular.setBuscarPorPerfis(true);
			filtrosConf.add(confTitular);
		}

		for (SrConfiguracao c : filtrosConf) {
			
			c.itemConfiguracaoFiltro = itemConfiguracao;
			c.acaoFiltro = acao;
			c.prioridade = prioridade;
			if (designacao != null)
				c.atendente = designacao.atendente;
			c.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
					CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
			
			List<SrConfiguracao> confs = SrConfiguracao.listar(c);
			for (SrConfiguracao conf : confs) {
				if (conf.acordo != null) {
					SrAcordo acordoAtual = ((SrAcordo) SrAcordo
							.findById(conf.acordo.idAcordo)).getAcordoAtual();
					if (acordoAtual != null && !acordos.contains(acordoAtual))
						acordos.add(acordoAtual);
				}
			}
		}
	}

	private void checarEPreencherCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		if (dtReg == null)
			dtReg = new Date();

		if (arquivo != null) {
			double lenght = (double) arquivo.blob.length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) � maior que o m�ximo permitido (2MB)");
		}

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();
		
		if (titular == null)
			titular = cadastrante;
		
		if (lotaTitular == null)
			lotaTitular = titular.getLotacao();

		if (solicitante == null)
			solicitante = cadastrante;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		if (solicitante.equivale(cadastrante)) {
			dtOrigem = null;
			meioComunicacao = null;
		}

		if (orgaoUsuario == null)
			orgaoUsuario = lotaSolicitante.getOrgaoUsuario();

		if (numSolicitacao == null)
			// DB1: Verifica se é uma Solicitação Filha, pois caso seja não
			// deve atualizar o número da solicitação, caso contrário não
			// funcionará o filtro por código para essa filha
			if (!isRascunho() && !isFilha()) {
				numSolicitacao = getProximoNumero();
				atualizarCodigo();
			}

		if (gravidade == null)
			gravidade = SrGravidade.NORMAL;

		if (urgencia == null)
			urgencia = SrUrgencia.NORMAL;

		if (tendencia == null)
			tendencia = SrTendencia.PIORA_MEDIO_PRAZO;

		// só valida o atendente caso não seja rascunho
		if (!isRascunho() && designacao == null)
			throw new Exception(
					"Não foi encontrado nenhum atendente designado "
							+ "para esta solicitação. Sugestão: alterar item de "
							+ "configuração e/ou ação");

		if (isFilha()) {
			if (descrSolicitacao.equals(solicitacaoPai.descrSolicitacao)
					|| descrSolicitacao.trim().isEmpty())
				descrSolicitacao = null;

			if (this.meuAtributoSolicitacaoSet != null)
				this.meuAtributoSolicitacaoSet = null;
		}

		atualizarAcordos();
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!podeDesfazerMovimentacao(cadastrante, lotaTitular))
			throw new Exception("Operação não permitida");

		SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();

		// tratamento pois pode ter retorno nulo do método
		// getUltimaMovimentacaoCancelave()
		if (movimentacao != null) {

			if (movimentacao.tipoMov != null) {
				// caso seja movimentacao cancelada ou fechada, reinsere nas
				// listas de prioridade
				if (movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO
						|| movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO)
					reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);

				if (movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_JUNTADA) {
					this.save();
				}
			}

			movimentacao.desfazer(cadastrante, lotaCadastrante, titular, lotaTitular);
		}
	}

	public SrSolicitacao criarFilhaSemSalvar() throws Exception {
		SrSolicitacao filha = new SrSolicitacao();
		Util.copiar(filha, this);
		filha.idSolicitacao = null;
		filha.solicitacaoPai = this;
		filha.numSolicitacao = this.numSolicitacao;
		filha.rascunho = null;
		filha.solicitacaoInicial = null;
		filha.meuMovimentacaoSet = null;
		filha.dtIniEdicao = new Date();
		filha.meuMovimentacaoReferenciaSet = null;
		for (SrSolicitacao s : getSolicitacaoFilhaSet())
			filha.numSequencia = s.numSequencia;
		if (filha.numSequencia == null)
			filha.numSequencia = 1L;
		else
			filha.numSequencia++;
		filha.atualizarCodigo();
		return filha;
	}

	public void atualizarMarcas() {
		SortedSet<SrMarca> setA = new TreeSet<SrMarca>();

		// Edson: Obtido do sigagc - Excluir marcas duplicadas (???)
		for (SrMarca m : getMarcaSet()) {
			if (setA.contains(m))
				m.delete();
			else
				setA.add(m);
		}
		SortedSet<SrMarca> setB = calcularMarcadores();
		Set<SrMarca> marcasAIncluir = new TreeSet<SrMarca>();
		Set<SrMarca> marcasAExcluir = new TreeSet<SrMarca>();
		Set<Par<SrMarca, SrMarca>> atualizar = new TreeSet<Par<SrMarca, SrMarca>>();
		encaixar(setA, setB, marcasAIncluir, marcasAExcluir, atualizar);

		if (meuMarcaSet == null)
			meuMarcaSet = new TreeSet<SrMarca>();
		for (SrMarca i : marcasAIncluir) {
			i.save();
			meuMarcaSet.add(i);
		}
		for (SrMarca e : marcasAExcluir) {
			e.solicitacao.meuMarcaSet.remove(e);
			e.delete();
		}
		for (Entry<SrMarca, SrMarca> pair : atualizar) {
			SrMarca a = pair.getKey();
			SrMarca b = pair.getValue();
			a.setDpLotacaoIni(b.getDpLotacaoIni());
			a.setDpPessoaIni(b.getDpPessoaIni());
			a.setDtFimMarca(b.getDtFimMarca());
			a.setDtIniMarca(b.getDtIniMarca());
			a.save();
		}
	}

	private SortedSet<SrMarca> calcularMarcadores() {
		SortedSet<SrMarca> set = new TreeSet<SrMarca>();

		if (isRascunho())
			acrescentarMarca(set,
					CpMarcador.MARCADOR_SOLICITACAO_EM_ELABORACAO, null, null,
					cadastrante, lotaTitular);

		Set<SrMovimentacao> movs = getMovimentacaoSetOrdemCrescente();

		if (movs != null && movs.size() > 0) {
			Long marcador = 0L;
			SrMovimentacao movMarca = null;

			List<SrMovimentacao> pendencias = new ArrayList<SrMovimentacao>();

			for (SrMovimentacao mov : movs) {
				Long t = mov.tipoMov.idTipoMov;
				if (mov.isCancelada())
					continue;
				if (t == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_FECHAMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_REABERTURA) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_JUNTADA) {
					marcador = CpMarcador.MARCADOR_JUNTADO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_DESENTRANHAMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
					if (mov.getDtFimMov() == null || mov.getDtFimMov().after(new Date()))
						pendencias.add(mov);
				}

				if (t == TIPO_MOVIMENTACAO_ANDAMENTO || t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO) {
					movMarca = mov;
				}

			}

			if (marcador != 0L)
				acrescentarMarca(set, marcador, movMarca.dtIniMov, null,
						movMarca.atendente, movMarca.lotaAtendente);

			if (marcador == CpMarcador.MARCADOR_SOLICITACAO_FECHADO
					&& isFilha())
				solicitacaoPai.atualizarMarcas();

			if (!isFechado() && isAFechar())
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_FECHADO_PARCIAL, movMarca.dtIniMov,
						null, movMarca.atendente, movMarca.lotaAtendente);
			
			if (marcador != 0L)
				acrescentarMarca(set, marcador, movMarca.dtIniMov, null,
						movMarca.atendente, movMarca.lotaAtendente);

			
			if (pendencias.size() > 0) {
				SrMovimentacao ultimaPendencia = pendencias.get(pendencias.size()-1);
				Date dtFimPendenciaMaisLonge = null;
				for (SrMovimentacao pendencia : pendencias){
					if (pendencia.dtAgenda != null && (dtFimPendenciaMaisLonge == null || pendencia.dtAgenda.after(dtFimPendenciaMaisLonge)))
						dtFimPendenciaMaisLonge = pendencia.dtAgenda;
				}
				if (isRascunho())
					acrescentarMarca(set,
							CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
							ultimaPendencia.dtIniMov, dtFimPendenciaMaisLonge,
							cadastrante, lotaCadastrante);
				else
					acrescentarMarca(set,
							CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
							ultimaPendencia.dtIniMov, dtFimPendenciaMaisLonge,
							ultimaPendencia.atendente, ultimaPendencia.lotaAtendente);
			}

			
			if (!isFechado() && !isCancelado()) {
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null,
						null, cadastrante, lotaCadastrante);
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE, null,
						null, solicitante, lotaSolicitante);

				Date prazo = getDtPrazoAtendimentoAcordado();
				if (prazo != null)
					acrescentarMarca(set,
							CpMarcador.MARCADOR_SOLICITACAO_FORA_DO_PRAZO,
							prazo, null, movMarca.atendente,
							movMarca.lotaAtendente);
			}
		}

		return set;
	}

	private void encaixar(SortedSet<SrMarca> setA, SortedSet<SrMarca> setB,
			Set<SrMarca> incluir, Set<SrMarca> excluir,
			Set<Par<SrMarca, SrMarca>> atualizar) {
		Iterator<SrMarca> ia = setA.iterator();
		Iterator<SrMarca> ib = setB.iterator();

		SrMarca a = null;
		SrMarca b = null;

		if (ia.hasNext())
			a = ia.next();
		if (ib.hasNext())
			b = ib.next();
		while (a != null || b != null) {
			if ((a == null) || (b != null && a.compareTo(b) > 0)) {
				// Existe em setB, mas nao existe em setA
				incluir.add(b);
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;

			} else if (b == null || (a != null && b.compareTo(a) > 0)) {
				// Existe em setA, mas nao existe em setB
				excluir.add(a);
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			} else {

				// O registro existe nos dois
				atualizar.add(new Par<SrMarca, SrMarca>(a, b));
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			}
		}
		ib = null;
		ia = null;
	}

	private void acrescentarMarca(SortedSet<SrMarca> set, Long idMarcador,
			Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota) {
		SrMarca mar = new SrMarca();
		// Edson: nao eh necessario ser this.solicitacaoInicial em vez de this
		// porque este metodo soh eh chamado por atualizarMarcas(), que ja se
		// certifica de chamar este metodo apenas para a solicitacao inicial
		mar.solicitacao = this;
		mar.setCpMarcador((CpMarcador) CpMarcador.findById(idMarcador));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	public String getMarcadoresEmHtml() {
		return getMarcadoresEmHtml(null, null);
	}

	public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();
		List<Long> marcadoresDesconsiderar = Arrays.asList(new Long[] {
				CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE,
				CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE });

		Set<SrMarca> marcas = getMarcaSetAtivas();

		for (SrMarca mar : marcas) {
			if (marcadoresDesconsiderar.contains(mar.getCpMarcador()
					.getIdMarcador()))
				continue;
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(mar.getCpMarcador().getDescrMarcador());
			sb.append(" (");
			if (mar.getDpPessoaIni() != null) {
				String nome = mar.getDpPessoaIni()
						.getDescricaoIniciaisMaiusculas();
				sb.append(nome.substring(0, nome.indexOf(" ")));
				sb.append(", ");
			}
			if (mar.getDpLotacaoIni() != null) {
				DpLotacao atual = mar.getDpLotacaoIni().getLotacaoAtual();
				if (atual == null)
					atual = mar.getDpLotacaoIni();
				sb.append(atual.getSigla());
			}
			sb.append(")");
		}

		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	public boolean isMarcada(long marcador) {
		return isMarcada(marcador, null, null);
	}

	public boolean isMarcada(long marcador, DpLotacao lota) {
		return isMarcada(marcador, lota, null);
	}

	public boolean isMarcada(long marcador, DpLotacao lota, DpPessoa pess) {
		for (SrMarca m : getMarcaSet())
			if (m.getCpMarcador().getIdMarcador().equals(marcador)
					&& (lota == null || m.getDpLotacaoIni().equivale(lota))
					&& (pess == null || m.getDpPessoaIni().equivale(pess)))
				return true;
		return false;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<ListaInclusaoAutomatica> getListasParaInclusaoAutomatica(DpLotacao lotaTitular) throws Exception {
		SrConfiguracao filtro = new SrConfiguracao();
		filtro.setDpPessoa(solicitante);
		filtro.setOrgaoUsuario(orgaoUsuario);
		filtro.setLotacao(lotaTitular);
		filtro.prioridade = prioridade;
		filtro.itemConfiguracaoFiltro = itemConfiguracao;
		filtro.acaoFiltro = acao;
		filtro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA));
		
		List<ListaInclusaoAutomatica> listaFinal = new ArrayList<ListaInclusaoAutomatica>();
		for (SrConfiguracao conf : SrConfiguracao.listar(filtro, new int[] { SrConfiguracaoBL.ATENDENTE, SrConfiguracaoBL.LISTA_PRIORIDADE })) {
			if (conf.listaPrioridade != null) {
				ListaInclusaoAutomatica listaInclusaoAutomatica = new ListaInclusaoAutomatica(conf.listaPrioridade.getListaAtual(), conf.prioridadeNaLista);
				
				if (!listaFinal.contains(listaInclusaoAutomatica))
					listaFinal.add(listaInclusaoAutomatica);
			}
		}
		return listaFinal;
	}
	
	public List<DpPessoa> getPessoasAtendentesDisponiveis(){
		List<DpPessoa> listaFinal = new ArrayList<DpPessoa>();
		if (getLotaAtendente() != null){
			DpLotacao atendente = getLotaAtendente().getLotacaoAtual();
			if (atendente == null)
				atendente = getLotaAtendente();
			for (DpPessoa p : atendente.getDpPessoaLotadosSet()){
				if (p.getHisDtFim() == null)
					listaFinal.add(p);
			}
			Collections.sort(listaFinal, new Comparator<DpPessoa>() {
		        @Override
		        public int compare(DpPessoa  o1, DpPessoa o2) {
					if (o1 != null && o2 != null && o1.getId().equals(o2.getId()))
						return 0;
					return o1.getNomePessoa().compareTo(o2.getNomePessoa());
		        }
		    });
		}
		return listaFinal;
	}

	public List<SrLista> getListasDisponiveisParaInclusao(
			DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);
		
		for (SrLista l : SrLista.listar(false)) {
			SrLista atual = l.getListaAtual();
			if (atual.podeIncluir(lotaTitular, cadastrante))
				if(!listaFinal.contains(atual))
					listaFinal.add(atual);
		}
		listaFinal.removeAll(getListasAssociadas());
		Collections.sort(listaFinal, new Comparator<SrLista>() {
			@Override
			public int compare(SrLista l1, SrLista l2) {
				return l1.nomeLista.compareTo(l2.nomeLista);
			}
		});
		return listaFinal;
	}

	public Set<SrLista> getListasAssociadas() {
		Set<SrLista> associadas = new HashSet<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
					&& mov.lista.isAtivo())
				associadas.add(mov.lista);
			else if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
				associadas.remove(mov.lista);
		return associadas;
	}

	public Set<SrSolicitacao> getSolicitacoesVinculadas() {
		Set<SrSolicitacao> solVinculadas = new HashSet<SrSolicitacao>();
		
		// vincula��es partindo desta solicita��o
		for (SrMovimentacao mov : getMovimentacaoSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_VINCULACAO)
				if (mov.solicitacaoReferencia != null)
					solVinculadas.add(mov.solicitacaoReferencia);

		// vincula��es partindo de outra solicita��o referenciando esta
		for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
			if (this.equals(mov.solicitacaoReferencia))
				solVinculadas.add(mov.solicitacao);
		return solVinculadas;
	}

	public Set<SrSolicitacao> getSolicitacoesJuntadas() {
		Set<SrSolicitacao> solJuntadas = new HashSet<SrSolicitacao>();

		for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA))
			if (!mov.isFinalizada() && this.equals(mov.solicitacaoReferencia))
				solJuntadas.add(mov.solicitacao);
		return solJuntadas;
	}

	public boolean isEmLista() {
		return getListasAssociadas().size() > 0;
	}

	public boolean isEmListaPertencenteA(DpLotacao lota) {
		for (SrLista l : getListasAssociadas()) {
			if (l.lotaCadastrante.equivale(lota))
				return true;
		}
		return false;
	}

	public boolean isEmLista(SrLista lista) {
		for (SrLista l : getListasAssociadas())
			if (l.equivale(lista))
				return true;
		return false;
	}

	public long getPrioridadeNaLista(SrLista lista) throws Exception {
		SrPrioridadeSolicitacao prioridadeSolicitacao = lista.getSrPrioridadeSolicitacao(this);
		return prioridadeSolicitacao != null ? prioridadeSolicitacao.numPosicao : -1;
	}

	public void incluirEmLista(SrLista lista, DpPessoa pess, DpLotacao lota, SrPrioridade prioridade, boolean naoReposicionarAutomatico) throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		if (isEmLista(lista))
			throw new IllegalArgumentException("Lista " + lista.nomeLista + " já contém a solicitação " + getCodigo());

		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = pess;
		mov.lotaCadastrante = lota;
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_INCLUSAO_LISTA);
		mov.descrMovimentacao = "Inclusão na lista " + lista.nomeLista;
		mov.lista = lista;
		mov.solicitacao = this;
		mov.salvar();
		
		lista.incluir(this, prioridade, naoReposicionarAutomatico);
	}

	public void retirarDeLista(SrLista lista, DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = cadastrante;
		mov.lotaCadastrante = lotaCadastrante;
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA);
		mov.descrMovimentacao = "Cancelamento de InclusÃ£o em Lista";
		mov.solicitacao = this;
		mov.lista = lista;
		mov.salvar();
		lista.retirar(this, cadastrante, lotaCadastrante);
	}

	private void iniciarAtendimento(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO);
		if (atendenteNaoDesignado == null)
			mov.lotaAtendente = designacao.atendente;
		else
			mov.lotaAtendente = atendenteNaoDesignado;
		mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public void fechar(DpPessoa cadastrante, DpLotacao lotaCadastrante, 
			DpPessoa titular, DpLotacao lotaTitular, String motivo)
			throws Exception {
		
		if(isPai() && !isAFechar())
			throw new Exception("Operação não permitida. Necessário fechar toda solicitação " + 
									"filha criada partir dessa que deseja fechar.");
			
		if ((cadastrante != null) && !podeFechar(cadastrante, lotaTitular))
			throw new Exception("Operação não permitida");

		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_FECHAMENTO);
		mov.descrMovimentacao = motivo;
		mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

		removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
		
		if (podeFecharPaiAutomatico())
			solicitacaoPai.fechar(cadastrante, lotaCadastrante, titular, lotaTitular,
							"Solicitação fechada automaticamente");
	
		/*if (temPesquisaSatisfacao())
			enviarPesquisa();*/
	}

	public void enviarPesquisa() throws Exception {
		// Implementar
		Correio.pesquisaSatisfacao(this);
	}

	public void responderPesquisa(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular,
			Map<Long, String> respostaMap) throws Exception {
		if (!podeResponderPesquisa(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		//movimentacao.pesquisa = this.getPesquisaDesignada();
		movimentacao.descrMovimentacao = "Avaliação realizada.";
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_AVALIACAO);
		movimentacao.setRespostaMap(respostaMap);
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	private void removerDasListasDePrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		for (SrLista lista : this.getListasAssociadas()) {
			this.retirarDeLista(lista, cadastrante, lotaCadastrante, titular, lotaTitular);
		}
	}

	public void reabrir(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!podeReabrir(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		reInserirListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);

		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA);
		mov.lotaAtendente = getUltimoAtendenteEtapaAtendimento();
		mov.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	private void reInserirListasDePrioridade(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO
					|| mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO)
				break;

			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
				incluirEmLista(mov.lista, cadastrante, lotaCadastrante, prioridade, false);

		}
	}

	public void deixarPendente(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular,
			SrTipoMotivoPendencia motivo, String calendario, String horario,
			String detalheMotivo) throws Exception {
		if (!podeDeixarPendente(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		
		if (calendario != null && !calendario.equals("")){
			DateTime dateTime = null;
			if (horario != null && !horario.equals("")){
				DateTimeFormatter formatter = DateTimeFormat
						.forPattern("dd/MM/yyyy HH:mm");
				dateTime = new DateTime(formatter.parseDateTime(calendario + " "
						+ horario));
			} else {
				DateTimeFormatter formatter = DateTimeFormat
						.forPattern("dd/MM/yyyy");
				dateTime = new DateTime(formatter.parseDateTime(calendario));
			}
			if (dateTime != null)
				movimentacao.dtAgenda = dateTime.toDate();
		}
		
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		movimentacao.motivoPendencia = motivo;
		movimentacao.descrMovimentacao = motivo.descrTipoMotivoPendencia;
		if (detalheMotivo != null && !detalheMotivo.trim().equals(""))
			movimentacao.descrMovimentacao += " | " + detalheMotivo;
		if (movimentacao.dtAgenda != null)
			movimentacao.descrMovimentacao += " | Fim previsto: " + movimentacao.getDtAgendaDDMMYYHHMM();
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public void alterarPrazo(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String motivo,
			String calendario, String horario) throws Exception {
		if (!podeAlterarPrazo(titular, lotaTitular))
			throw new Exception("Opera��o n�o permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		DateTime datetime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("dd/MM/yyyy HH:mm");
		if (!calendario.equals("")) {
			datetime = new DateTime(formatter.parseDateTime(calendario + " "
					+ horario));
			movimentacao.dtAgenda = datetime.toDate();
		}

		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO);
		movimentacao.descrMovimentacao = "Prazo alterado para " + calendario + " " 
				+ horario + " - " + motivo;
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public void terminarPendencia(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String descricao, Long idMovimentacao)
			throws Exception {
		if (!podeTerminarPendencia(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA);
		
		// Edson: eh necessario setar a finalizadora na finalizada antes de 
		// salvar() a finalizadora, pq se n�o, ao atualizarMarcas(), vai 
		// parecer que a pendencia nao foi finalizada, atrapalhando calculos 
		// de prazo
		SrMovimentacao movFinalizada = SrMovimentacao.findById(idMovimentacao);
		movFinalizada.movFinalizadora = movimentacao;
		
		movimentacao.descrMovimentacao = descricao;
		movimentacao.descrMovimentacao += " | Terminando pendencia iniciada em " + movFinalizada.getDtIniMovDDMMYYHHMM();
		movimentacao = movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
		movFinalizada.save();
	}

	public void cancelar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!podeCancelar(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO);
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

		// remove das listas apos finalizar, para que seja possivel reabrir
		// depois
		removerDasListasDePrioridade(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public void juntar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular,
			SrSolicitacao solRecebeJuntada, String justificativa)
			throws Exception {
		if ((cadastrante != null) && !podeJuntar(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		if (solRecebeJuntada.equivale(this))
			throw new Exception(
					"Não e possivel juntar uma solicitação a si mesma.");
		if (solRecebeJuntada.isJuntada()
				&& solRecebeJuntada.getSolicitacaoPrincipal().equivale(this))
			throw new Exception("Não e possivel realizar juntada circular.");
		if (solRecebeJuntada.isFilha()
				&& solRecebeJuntada.solicitacaoPai.equivale(this))
			throw new Exception(
					"Não e possivel juntar uma solicitação a uma das suas filhas. Favor realizar o processo inverso.");

		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_JUNTADA);
		movimentacao.solicitacaoReferencia = solRecebeJuntada;
		movimentacao.descrMovimentacao = justificativa + " | Juntando a " + solRecebeJuntada.codigo;
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public void desentranhar(DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular, DpLotacao lotaTitular, String justificativa)
			throws Exception {
		if (!podeDesentranhar(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO);
		movimentacao.descrMovimentacao = justificativa;
		movimentacao = movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);

		SrMovimentacao juntada = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);
		juntada.movFinalizadora = movimentacao;
		juntada.save();

	}

	public void vincular(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular,
			SrSolicitacao solRecebeVinculo, String justificativa)
			throws Exception {
		if ((cadastrante != null) && !podeVincular(titular, lotaTitular))
			throw new Exception("Operação não permitida");
		if (solRecebeVinculo.equivale(this))
	        throw new Exception("Não e possivel vincular uma solicita�ao a si mesma.");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_VINCULACAO);
		movimentacao.solicitacaoReferencia = solRecebeVinculo;
		movimentacao.descrMovimentacao = justificativa + " | Vinculando a " + solRecebeVinculo.codigo;
		movimentacao.salvar(cadastrante, lotaCadastrante, titular, lotaTitular);
	}

	public String getGcTags() {
		String s = "tags=@servico";
		if (acao != null)
			s += acao.getGcTags();
		if (itemConfiguracao != null)
			s += itemConfiguracao.getGcTags();
		return s;
	}

	public String getGcTagAbertura() {
		String s = "^sr:";
		if (acao != null)
			s += Texto.slugify(acao.tituloAcao, true, false);
		if (itemConfiguracao != null)
			s += "-"
					+ Texto.slugify(itemConfiguracao.tituloItemConfiguracao,
							true, false);
		return s;
	}

	public String getGcTituloAbertura() {
		String s = "";
		if (acao != null)
			s += acao.tituloAcao;
		if (itemConfiguracao != null)
			s += " - " + itemConfiguracao.tituloItemConfiguracao;
		return s;
	}

	public String getDtOrigemDDMMYYYYHHMM() {
		if (dtOrigem != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtOrigem);
		}
		return "";
	}

	public String getDtOrigemHHMM() {
		if (dtOrigem != null) {
			final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(dtOrigem);
		}
		return "";
	}

	public String getDtOrigemDDMMYYYY() {
		if (dtOrigem != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(dtOrigem);
		}
		return "";
	}

	public String getDtOrigemString() {
		if (dtOrigem != null) {
			SigaPlayCalendar cal = new SigaPlayCalendar();
			cal.setTime(dtOrigem);
			return cal.getTempoTranscorridoString(false);
		}
		return "";
	}

	public void setDtOrigemString(String stringDtMeioContato) {
		DateTimeFormatter formatter = forPattern("dd/MM/yyyy HH:mm");
		if (stringDtMeioContato != null && !stringDtMeioContato.isEmpty()
				&& stringDtMeioContato.contains("/")
				&& stringDtMeioContato.contains(":"))
			this.dtOrigem = new DateTime(
					formatter.parseDateTime(stringDtMeioContato)).toDate();
	}

	public String getDtIniEdicaoDDMMYYYYHHMMSS() {
		if (dtIniEdicao != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			return df.format(dtIniEdicao);
		}
		return "";
	}

	public void setDtIniEdicaoDDMMYYYYHHMMSS(String string) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			this.dtIniEdicao = df.parse(string);
		} catch (Exception e) {

		}
	}

	public Date getDtInicioPrimeiraEdicao() {
		if (solicitacaoInicial != null)
			return solicitacaoInicial.dtIniEdicao;
		else
			return this.dtIniEdicao;
	}

	public Date getDtInicioAtendimento() {
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO
					|| mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
				return mov.dtIniMov;
		return null;
	}

	public Date getDtEfetivoFechamento() {
		SrMovimentacao fechamento = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
		if (fechamento == null)
			return null;
		return fechamento.dtIniMov;
	}
	
	public Date getDtCancelamento() {
		SrMovimentacao cancelamento = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO);
		if (cancelamento == null)
			return null;
		return cancelamento.dtIniMov;
	}
	
	// Edson: retorna os periodos de pendencia de forma linear, ou seja, 
	// colapsando as sobreposicoes, para facilitar os calculos:
	// Transforma: -------
	//                -------  
	//
	// em: ----------
	private Map<Date, Date> getTrechosPendentes() {
		Map<Date, Date> pendencias = new LinkedHashMap<Date, Date>();
		Date dtIniEmAberto = null, dtFimEmAberto = null;

		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {

			if (dtIniEmAberto != null && dtFimEmAberto != null
					&& mov.dtIniMov.after(dtFimEmAberto)) {
				dtIniEmAberto = null;
				dtFimEmAberto = null;
			}

			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				if (dtIniEmAberto == null)
					dtIniEmAberto = mov.dtIniMov;
				if (dtFimEmAberto == null
						|| (mov.getDtFimMov() != null && mov.getDtFimMov()
								.after(dtFimEmAberto)))
					dtFimEmAberto = mov.getDtFimMov();
				pendencias.put(dtIniEmAberto, dtFimEmAberto);
				if (dtFimEmAberto == null)
					return pendencias;
			}
		}
		return pendencias;
	}
	
	public Date getDataAPartirDe(Date dtBase, Long segundosAdiante){
		Map<Date, Date> pendencias = getTrechosPendentes();
		for (Date dtIniPendencia : pendencias.keySet()) {
			if (dtIniPendencia.before(dtBase))
				continue;
			Date dtFimPendencia = pendencias.get(dtIniPendencia);
			if (dtFimPendencia == null)
				return null;
			int delta = (int) (dtIniPendencia.getTime() - dtBase.getTime()) / 1000;
			if (delta <= segundosAdiante) {
				segundosAdiante -= delta;
				dtBase = dtFimPendencia;
			} else
				break;
		}
		return new Date(dtBase.getTime() + segundosAdiante * 1000);
	}

	private Date getDtPrazoCadastramentoAcordado() {
		if (acordos == null || acordos.size() == 0 || isCancelado())
			return null;
		Long menorTempoAcordado = null;
		for (SrAcordo a : acordos){
			Long acordado = a.getAcordoAtual().getAtributoEmSegundos("tempoDecorridoCadastramento");
			if (menorTempoAcordado == null || (acordado != null && acordado < menorTempoAcordado))
				menorTempoAcordado = acordado;
		}
		if (menorTempoAcordado == null)
			return null;
		return getDataAPartirDe(getDtInicioPrimeiraEdicao(), menorTempoAcordado);
	}

	private Date getDtPrazoAtendimentoAcordado() {
		if (acordos == null || acordos.size() == 0 || isCancelado())
			return null;
		Long menorTempoAcordado = null;
		for (SrAcordo a : acordos){
			Long acordado = a.getAcordoAtual().getAtributoEmSegundos("tempoDecorridoAtendimento");
			if (menorTempoAcordado == null || (acordado != null && acordado < menorTempoAcordado))
				menorTempoAcordado = acordado;
		}
		if (menorTempoAcordado == null)
			return null;
		return getDataAPartirDe(getDtInicioAtendimento(), menorTempoAcordado);
	}

	public Cronometro getCronometro() throws Exception {
		if (cron == null) {
			cron = new Cronometro();
			boolean fechado = isFechado(), cancelado = isCancelado(), 
					pendente = isPendente();
			if (jaFoiDesignada()) {
				cron.setDescricao("Atendimento");
				cron.setInicio(getDtInicioAtendimento());
				cron.setFim(fechado ? getDtEfetivoFechamento()
						: cancelado ? getDtCancelamento()
								: getDtPrazoAtendimentoAcordado());
				if (cron.getFim() == null || fechado || cancelado)
					cron.setDecorrido(getTempoDecorridoAtendimento().getValor()*1000);
				else
					cron.setRestante(cron.getFim().getTime() - new Date()
							.getTime());
			} else {
				cron.setDescricao("Cadastro");
				cron.setInicio(getDtInicioPrimeiraEdicao());
				cron.setFim(getDtPrazoCadastramentoAcordado());
				if (cron.getFim() == null || fechado || cancelado)
					cron.setDecorrido(getTempoDecorridoCadastramento()
							.getValor()*1000);
				else
					cron.setRestante(cron.getFim().getTime() - new Date()
							.getTime());
			}
			cron.setLigado(!fechado && !cancelado
					&& (cron.getFim() != null || !pendente));
		}

		return cron;
	}

	// Edson: retorna o tempo decorrido entre duas datas, descontando
	// os per�odos de pend�ncia (blocos).
	// PPP, abaixo, � o bloco pendente. I � dtIni e F � dtFim
	public SrValor getTempoDecorrido(Date dtIni, Date dtFim) {
		Map<Date, Date> pendencias = getTrechosPendentes();
		Long decorrido = 0L;
		for (Date dtIniBlocoPendencia : pendencias.keySet()) {
			Date dtFimBlocoPendencia = pendencias.get(dtIniBlocoPendencia);

			// ----------I----------F---PPPP---
			if (dtIniBlocoPendencia.after(dtFim))
				break;

			// ---PPPP---I----------F----------
			if (dtFimBlocoPendencia != null && dtFimBlocoPendencia.before(dtIni))
				continue;

			// ----------I---PPPP---F----------
			if (dtIniBlocoPendencia.after(dtIni))
				decorrido += (int) ((dtIniBlocoPendencia.getTime() - dtIni
						.getTime()) / 1000);

			dtIni = dtFimBlocoPendencia;

			// ----------I---------PPFP--------- ou 
			// ----------I---------PPFPPPPPPPPPP...
			if (dtFimBlocoPendencia == null || dtFimBlocoPendencia.after(dtFim))
				return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
		}
		decorrido += (int) ((dtFim.getTime() - dtIni.getTime()) / 1000);
		return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
	}
	
	public SrValor getTempoDecorridoCadastramento(){
		Date dtFimCadastro = isFechado() ? getDtEfetivoFechamento()
				: isCancelado() ? getDtCancelamento() : getDtInicioAtendimento();
		if (dtFimCadastro == null)
			dtFimCadastro = new Date();
		return getTempoDecorrido(getDtInicioPrimeiraEdicao(), dtFimCadastro);
	}
	
	public SrValor getTempoDecorridoAtendimento() {
		Date dtFechamento = isFechado() ? getDtEfetivoFechamento()
				: isCancelado() ? getDtCancelamento() : new Date();
		return getTempoDecorrido(getDtInicioAtendimento(), dtFechamento);
	}
	
	//Edson: implementar no futuro
	public Long getResultadoPesquisaSatisfacao(){
		return 0L;
	}

	public boolean isAcordosSatisfeitos() {
		if (acordos == null || acordos.size() == 0)
			return true;
		for (SrAcordo a : acordos) {
			if (!isAcordoSatisfeito(a))
				return false;
		}
		return true;
	}

	public boolean isAcordoSatisfeito(SrAcordo acordo) {
		if (acordo == null)
			return true;
		acordo = acordo.getAcordoAtual();
		if (acordo.atributoAcordoSet == null)
			return true;
		for (SrAtributoAcordo pa : acordo.atributoAcordoSet) {
			if (!isAtributoAcordoSatisfeito(pa))
				return false;
		}
		return true;
	}

	public boolean isAtributoAcordoSatisfeito(SrAtributoAcordo atributoAcordo) {
		try {
			SrValor valor = (SrValor) SrSolicitacao.class.getMethod(
					atributoAcordo.atributo.asGetter()).invoke(this);
			if (valor == null)
				return true;
			return atributoAcordo.isNaFaixa(valor);
		} catch (NoSuchMethodException nsme) {
			return false;
		} catch (InvocationTargetException ite) {
			return false;
		} catch (IllegalAccessException iae) {
			return false;
		}
	}

	@Override
	public boolean equivale(Object other) {
		try {
			SrSolicitacao outra = (SrSolicitacao) other;
			return outra.getHisIdIni().equals(this.getHisIdIni());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * No caso de solicitacoes filhas, deve ser considerado o solicitante e o
	 * cadastrante para fins de exibicao de itens de configuracao e acoes
	 * disponiveis, alem do atendente designado da solicitacao.
	 */
	private List<SrConfiguracao> getFiltrosParaConsultarDesignacoes() {
		
		List<SrConfiguracao> pessoasAConsiderar = new ArrayList<SrConfiguracao>();
		
		if (titular == null)
			return pessoasAConsiderar;
		
		SrConfiguracao confSolicitante = new SrConfiguracao();
		confSolicitante.setDpPessoa(solicitante);
		confSolicitante.setLotacao(lotaSolicitante);
		confSolicitante.setComplexo(local);
		confSolicitante.setBuscarPorPerfis(true);
		pessoasAConsiderar.add(confSolicitante);
		
		if (jaFoiDesignada()){
			SrConfiguracao confTitular = new SrConfiguracao();
			confTitular.setDpPessoa(titular);
			confTitular.setLotacao(lotaTitular);
			confTitular.setComplexo(local);
			confTitular.setBuscarPorPerfis(true);
			pessoasAConsiderar.add(confTitular);
		}
		
		return pessoasAConsiderar;

	}
	
	public List<SrItemConfiguracao> getHistoricoItem() {
		List<SrItemConfiguracao> historicoItens = listarHistoricoItemInicial();
		SrItemConfiguracao anterior = itemConfiguracao;

		for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescentePorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO)) {
			if (movimentacao.getItemConfiguracao() != null && anterior != null && !movimentacao.getItemConfiguracao().equivale(anterior)) {
				historicoItens.add(movimentacao.getItemConfiguracao());
				anterior = movimentacao.getItemConfiguracao();
			}
		}
		return historicoItens;
	}

	public List<SrAcao> getHistoricoAcao() {
		List<SrAcao> historicoAcoes = listaHistoricoAcaoInicial();
		SrAcao acaoAnterior = acao;
		for (SrMovimentacao movimentacao : getMovimentacaoSetOrdemCrescentePorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO)) {
			if (movimentacao.getAcao() != null && acaoAnterior != null && !movimentacao.getAcao().equivale(acaoAnterior)) {
				historicoAcoes.add(movimentacao.getAcao());
				acaoAnterior = movimentacao.getAcao();
			}
		}
		return historicoAcoes;
	}

	private Set<SrMovimentacao> getMovimentacaoSetOrdemCrescentePorTipo(
			Long idTipoMovimentacao) {
		return getMovimentacaoSet(false, null, true, false, false, false,
				Arrays.asList(idTipoMovimentacao));
	}

	private List<SrAcao> listaHistoricoAcaoInicial() {
		List<SrAcao> acoes = new ArrayList<SrAcao>();
		if (acao != null) {
			acoes.add(acao);
		}
		return acoes;
	}

	private List<SrItemConfiguracao> listarHistoricoItemInicial() {
		List<SrItemConfiguracao> itensConfiguracao = new ArrayList<SrItemConfiguracao>();
		if (itemConfiguracao != null) {
			itensConfiguracao.add(itemConfiguracao);
		}
		return itensConfiguracao;
	}

	public SrItemConfiguracao getItemAtual() {
		List<SrItemConfiguracao> historicoItem = getHistoricoItem();
		if (historicoItem.isEmpty()) {
			return null;
		}
		int size = historicoItem.size();
		return historicoItem.get(size - 1);
	}

	public SrAcao getAcaoAtual() {
		List<SrAcao> historicoAcao = getHistoricoAcao();
		if (historicoAcao.isEmpty()) {
			return null;
		}
		int size = historicoAcao.size();
		return historicoAcao.get(size - 1);
	}
}
