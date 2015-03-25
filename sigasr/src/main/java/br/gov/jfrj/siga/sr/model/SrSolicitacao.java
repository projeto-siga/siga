
package br.gov.jfrj.siga.sr.model;

import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA;
import static br.gov.jfrj.siga.sr.model.SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO;
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.SrCorreio;
import br.gov.jfrj.siga.sr.util.Cronometro;
import br.gov.jfrj.siga.sr.util.Util;

@Entity
@Table(name = "SR_SOLICITACAO", schema = "SIGASR")
public class SrSolicitacao extends HistoricoSuporte implements SrSelecionavel {

	public static ActiveRecord<SrSolicitacao> AR = new ActiveRecord<>(SrSolicitacao.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SOLICITACAO_SEQ", name = "srSolicitacaoSeq")
	@GeneratedValue(generator = "srSolicitacaoSeq")
	@Column(name = "ID_SOLICITACAO")
	protected Long idSolicitacao;

	@ManyToOne()
	@JoinColumn(name = "ID_SOLICITANTE")
	protected DpPessoa solicitante;

	@ManyToOne
	@JoinColumn(name = "ID_INTERLOCUTOR")
	protected DpPessoa interlocutor;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_SOLICITANTE")
	protected DpLotacao lotaSolicitante;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	protected DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	protected DpLotacao lotaCadastrante;

	@Transient
	protected DpLotacao atendenteNaoDesignado;
	
	@Transient
	private Cronometro cron;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	protected CpOrgaoUsuario orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	protected SrSolicitacao solicitacaoPai;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_SOLICITACAO_ACORDO", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_SOLICITACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ACORDO")})
	protected List<SrAcordo> acordos;

	@Enumerated
	protected SrFormaAcompanhamento formaAcompanhamento;

	@Enumerated
	protected SrMeioComunicacao meioComunicacao;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	protected SrItemConfiguracao itemConfiguracao;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	protected SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_ACAO")
	protected SrAcao acao;

	@Lob
	@Column(name = "DESCR_SOLICITACAO", length = 8192)
	protected String descrSolicitacao;

	@Enumerated
	protected SrGravidade gravidade;

	@Enumerated
	protected SrTendencia tendencia;

	@Enumerated
	protected SrUrgencia urgencia;

	@Enumerated
	protected SrPrioridade prioridade;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dtReg;

	@Column(name = "DT_EDICAO_INI")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dtIniEdicao;

	@Column(name = "DT_ORIGEM")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dtOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_COMPLEXO")
	protected CpComplexo local;

	@Column(name = "TEL_PRINCIPAL")
	protected String telPrincipal;

	@Transient
	protected boolean fecharAoAbrir;

	@Transient
	protected String motivoFechamentoAbertura;

	@Column(name = "NUM_SOLICITACAO")
	protected Long numSolicitacao;

	@Column(name = "NUM_SEQUENCIA")
	protected Long numSequencia;

	@Column(name = "DESCR_CODIGO")
	protected String codigo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	protected SrSolicitacao solicitacaoInicial;

	@OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	protected List<SrSolicitacao> meuSolicitacaoHistoricoSet;

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
	protected Boolean rascunho;
	
	@Column(name = "FECHADO_AUTOMATICAMENTE")
	@Type(type = "yes_no")
	protected Boolean fechadoAutomaticamente;
	
	
	public Long getIdSolicitacao() {
		return idSolicitacao;
	}

	public void setIdSolicitacao(Long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}

	public DpPessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(DpPessoa solicitante) {
		this.solicitante = solicitante;
	}

	public DpPessoa getInterlocutor() {
		return interlocutor;
	}

	public void setInterlocutor(DpPessoa interlocutor) {
		this.interlocutor = interlocutor;
	}

	public DpLotacao getLotaSolicitante() {
		return lotaSolicitante;
	}

	public void setLotaSolicitante(DpLotacao lotaSolicitante) {
		this.lotaSolicitante = lotaSolicitante;
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public void setCadastrante(DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	public void setLotaCadastrante(DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	public DpLotacao getAtendenteNaoDesignado() {
		return atendenteNaoDesignado;
	}

	public void setAtendenteNaoDesignado(DpLotacao atendenteNaoDesignado) {
		this.atendenteNaoDesignado = atendenteNaoDesignado;
	}

	public Cronometro getCron() {
		return cron;
	}

	public void setCron(Cronometro cron) {
		this.cron = cron;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public SrSolicitacao getSolicitacaoPai() {
		return solicitacaoPai;
	}

	public void setSolicitacaoPai(SrSolicitacao solicitacaoPai) {
		this.solicitacaoPai = solicitacaoPai;
	}

	public List<SrAcordo> getAcordos() {
		return acordos;
	}

	public void setAcordos(List<SrAcordo> acordos) {
		this.acordos = acordos;
	}

	public SrFormaAcompanhamento getFormaAcompanhamento() {
		return formaAcompanhamento;
	}

	public void setFormaAcompanhamento(SrFormaAcompanhamento formaAcompanhamento) {
		this.formaAcompanhamento = formaAcompanhamento;
	}

	public SrMeioComunicacao getMeioComunicacao() {
		return meioComunicacao;
	}

	public void setMeioComunicacao(SrMeioComunicacao meioComunicacao) {
		this.meioComunicacao = meioComunicacao;
	}

	public SrItemConfiguracao getItemConfiguracao() {
		return itemConfiguracao;
	}

	public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
		this.itemConfiguracao = itemConfiguracao;
	}

	public SrArquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(SrArquivo arquivo) {
		this.arquivo = arquivo;
	}

	public SrAcao getAcao() {
		return acao;
	}

	public void setAcao(SrAcao acao) {
		this.acao = acao;
	}

	public String getDescrSolicitacao() {
		return descrSolicitacao;
	}

	public void setDescrSolicitacao(String descrSolicitacao) {
		this.descrSolicitacao = descrSolicitacao;
	}

	public SrGravidade getGravidade() {
		return gravidade;
	}

	public void setGravidade(SrGravidade gravidade) {
		this.gravidade = gravidade;
	}

	public SrTendencia getTendencia() {
		return tendencia;
	}

	public void setTendencia(SrTendencia tendencia) {
		this.tendencia = tendencia;
	}

	public SrUrgencia getUrgencia() {
		return urgencia;
	}

	public void setUrgencia(SrUrgencia urgencia) {
		this.urgencia = urgencia;
	}

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Date getDtReg() {
		return dtReg;
	}

	public void setDtReg(Date dtReg) {
		this.dtReg = dtReg;
	}

	public Date getDtIniEdicao() {
		return dtIniEdicao;
	}

	public void setDtIniEdicao(Date dtIniEdicao) {
		this.dtIniEdicao = dtIniEdicao;
	}

	public Date getDtOrigem() {
		return dtOrigem;
	}

	public void setDtOrigem(Date dtOrigem) {
		this.dtOrigem = dtOrigem;
	}

	public CpComplexo getLocal() {
		return local;
	}

	public void setLocal(CpComplexo local) {
		this.local = local;
	}

	public String getTelPrincipal() {
		return telPrincipal;
	}

	public void setTelPrincipal(String telPrincipal) {
		this.telPrincipal = telPrincipal;
	}

	public boolean isFecharAoAbrir() {
		return fecharAoAbrir;
	}

	public void setFecharAoAbrir(boolean fecharAoAbrir) {
		this.fecharAoAbrir = fecharAoAbrir;
	}

	public String getMotivoFechamentoAbertura() {
		return motivoFechamentoAbertura;
	}

	public void setMotivoFechamentoAbertura(String motivoFechamentoAbertura) {
		this.motivoFechamentoAbertura = motivoFechamentoAbertura;
	}

	public Long getNumSolicitacao() {
		return numSolicitacao;
	}

	public void setNumSolicitacao(Long numSolicitacao) {
		this.numSolicitacao = numSolicitacao;
	}

	public Long getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(Long numSequencia) {
		this.numSequencia = numSequencia;
	}

	public SrSolicitacao getSolicitacaoInicial() {
		return solicitacaoInicial;
	}

	public void setSolicitacaoInicial(SrSolicitacao solicitacaoInicial) {
		this.solicitacaoInicial = solicitacaoInicial;
	}

	public List<SrSolicitacao> getMeuSolicitacaoHistoricoSet() {
		return meuSolicitacaoHistoricoSet;
	}

	public void setMeuSolicitacaoHistoricoSet(
			List<SrSolicitacao> meuSolicitacaoHistoricoSet) {
		this.meuSolicitacaoHistoricoSet = meuSolicitacaoHistoricoSet;
	}

	public List<SrAtributoSolicitacao> getMeuAtributoSolicitacaoSet() {
		return meuAtributoSolicitacaoSet;
	}

	public void setMeuAtributoSolicitacaoSet(
			List<SrAtributoSolicitacao> meuAtributoSolicitacaoSet) {
		this.meuAtributoSolicitacaoSet = meuAtributoSolicitacaoSet;
	}

	public Set<SrMovimentacao> getMeuMovimentacaoSet() {
		return meuMovimentacaoSet;
	}

	public void setMeuMovimentacaoSet(Set<SrMovimentacao> meuMovimentacaoSet) {
		this.meuMovimentacaoSet = meuMovimentacaoSet;
	}

	public Set<SrSolicitacao> getMeuSolicitacaoFilhaSet() {
		return meuSolicitacaoFilhaSet;
	}

	public void setMeuSolicitacaoFilhaSet(Set<SrSolicitacao> meuSolicitacaoFilhaSet) {
		this.meuSolicitacaoFilhaSet = meuSolicitacaoFilhaSet;
	}

	public Set<SrMovimentacao> getMeuMovimentacaoReferenciaSet() {
		return meuMovimentacaoReferenciaSet;
	}

	public void setMeuMovimentacaoReferenciaSet(
			Set<SrMovimentacao> meuMovimentacaoReferenciaSet) {
		this.meuMovimentacaoReferenciaSet = meuMovimentacaoReferenciaSet;
	}

	public Set<SrMarca> getMeuMarcaSet() {
		return meuMarcaSet;
	}

	public void setMeuMarcaSet(Set<SrMarca> meuMarcaSet) {
		this.meuMarcaSet = meuMarcaSet;
	}

	public Boolean getRascunho() {
		return rascunho;
	}

	public void setRascunho(Boolean rascunho) {
		this.rascunho = rascunho;
	}

	public SrSolicitacao() {

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
		for (Object ou : CpOrgaoUsuario.AR.all().fetch()) {
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
						+ ")?-?(SR{1})-?(2{1}[0-9]{3})?/?([0-9]{1,5})?([.]{1})?([0-9]{1,2})?$");
		final Matcher m = p.matcher(sigla);

		if (m.find()) {

			if (m.group(1) != null) {
				try {
					CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
					orgaoUsuario.setSiglaOrgaoUsu(m.group(1));
					orgaoUsuario = (CpOrgaoUsuario) 
							em()
							.createQuery(
									"from CpOrgaoUsuario where acronimoOrgaoUsu = '"
											+ m.group(1) + "'")
							.getSingleResult();
					this.orgaoUsuario = orgaoUsuario;
				} catch (final Exception ce) {

				}
			}

			if (m.group(3) != null) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, Integer.valueOf(m.group(3)));
				c1.set(Calendar.DAY_OF_YEAR, 1);
				this.dtReg = c1.getTime();
			} else
				this.dtReg = new Date();

			if (m.group(4) != null)
				numSolicitacao = Long.valueOf(m.group(4));

			if (m.group(5) != null)
				numSequencia = Long.valueOf(m.group(5));
		}

	}

	@Override
	public String getDescricao() {
		if (descrSolicitacao == null || descrSolicitacao.length() == 0)
			return "Descrição não informada";
		return descrSolicitacao;
	}

	public String getDescrItem() {
		return itemConfiguracao != null ? itemConfiguracao.getTituloItemConfiguracao()
				: "Item não informado";
	}

	public String getDescrAcao() {
		return acao != null ? acao.getTituloAcao() : "Ação não informada";
	}

	public String getSiglaEDescrItem() {
		return itemConfiguracao != null ? itemConfiguracao.toString()
				: "Item não informado";
	}

	public String getSiglaEDescrAcao() {
		return acao != null ? acao.toString() : "Ação não informada";
	}

	@Override
	public void setDescricao(String descricao) {
		this.descrSolicitacao = descricao;
	}
	
	public Boolean getFechadoAutomaticamente() {
		return fechadoAutomaticamente;
	}

	public void setFechadoAutomaticamente(Boolean fechadoAutomaticamente) {
		this.fechadoAutomaticamente = fechadoAutomaticamente;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) throws Exception {
		setSigla(sigla);
		if (orgaoUsuario == null && cadastrante != null)
			orgaoUsuario = cadastrante.getOrgaoUsuario();

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

		SrSolicitacao sol = (SrSolicitacao) em().createQuery(query)
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
			codigo = "TMPSR-" + (solicitacaoInicial != null ? solicitacaoInicial.idSolicitacao
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
		return meuAtributoSolicitacaoSet != null ? meuAtributoSolicitacaoSet : new ArrayList<SrAtributoSolicitacao>();
	}

	public String getDtRegString() {
		SigaCalendar cal = new SigaCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString(false);
	}

	public String getAtributosString() {
		String s = "";
		for (SrAtributoSolicitacao att : getAtributoSolicitacaoSet()) {
			if (att.getValorAtributoSolicitacao() != null)
				s += att.getAtributo().getNomeAtributo() + ": "
						+ att.getValorAtributoSolicitacao() + ". ";
		}
		return s;
	}

	// Edson: Necess�rio porque nao h� binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public int getGUT() {
		return gravidade.getNivelGravidade() * urgencia.getNivelUrgencia()
				* tendencia.getNivelTendencia();
	}

	public String getGUTString() {
		return gravidade.getDescrGravidade() + " " + urgencia.getDescrUrgencia() + " "
				+ tendencia.getDescrTendencia();
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
		Long num = AR.find(
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
			if (mov.getDescrMovimentacao() != null
					&& mov.getDescrMovimentacao().length() > 0)
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
			if (mov.getNumSequencia() > 1
					&& mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA
					&& mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					&& mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
					&& mov.getTipoMov().getIdTipoMov() != TIPO_MOVIMENTACAO_AVALIACAO)
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

	public Set<SrMovimentacao> getMovimentacaoSet(
			boolean considerarCanceladas, Long tipoMov, boolean ordemCrescente,
			boolean todoOContexto, boolean apenasPrincipais, boolean inverso) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new SrMovimentacaoComparator(ordemCrescente));

		List<Long> tiposPrincipais = Arrays.asList(TIPO_MOVIMENTACAO_ANDAMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO, TIPO_MOVIMENTACAO_AVALIACAO, TIPO_MOVIMENTACAO_ESCALONAMENTO);

		Set<SrSolicitacao> solsAConsiderar = new LinkedHashSet<SrSolicitacao>();
		if (todoOContexto) {
			solsAConsiderar.addAll(getPaiDaArvore()
					.getSolicitacaoFilhaSetRecursivo());
		} else
			solsAConsiderar.add(this);

		for (SrSolicitacao sol : solsAConsiderar) {
			if (sol.solicitacaoInicial != null)
				for (SrSolicitacao instancia : sol.getHistoricoSolicitacao()) {
					Set<SrMovimentacao> movSet = inverso ? instancia.meuMovimentacaoReferenciaSet
							: instancia.meuMovimentacaoSet;
					if (movSet != null)
						for (SrMovimentacao movimentacao : movSet) {
							if (!considerarCanceladas
									&& movimentacao.isCanceladoOuCancelador())
								continue;
							if (tipoMov != null
									&& movimentacao.getTipoMov().getIdTipoMov() != tipoMov)
								continue;
							if (apenasPrincipais
									&& !tiposPrincipais
											.contains(movimentacao.getTipoMov().getIdTipoMov()))
								continue;

							listaCompleta.add(movimentacao);
						}
				}
		}
		return listaCompleta;
	}

	public boolean jaFoiDesignada() {
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO
					|| mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;
		}
		return false;
	}

	public DpLotacao getLotaAtendente() {
		return getUltimaMovimentacao().getLotaAtendente();
	}

	public DpPessoa getAtendente() {
		return getUltimaMovimentacao().getAtendente();
	}

	public boolean isFilha() {
		return (this.solicitacaoPai != null);
	}

	public DpLotacao getPreAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.setItemConfiguracaoFiltro(itemConfiguracao);
		confFiltro.setAcaoFiltro(acao);
		confFiltro.setBuscarPorPerfis(true);
		confFiltro.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE);
		
		SrConfiguracao conf = SrConfiguracao.buscarDesignacao(confFiltro);
		if (conf != null)
			return conf.getPreAtendente().getLotacaoAtual();
		return null;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;

		for (SrConfiguracao c : getFiltrosParaConsultarConfiguracoes()){
			c.setItemConfiguracaoFiltro(itemConfiguracao);
			c.setAcaoFiltro(acao);
			c.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
			SrConfiguracao conf = SrConfiguracao.buscarDesignacao(c);
			if (conf != null)
				return conf.getAtendente().getLotacaoAtual();
		}
		
		return null;
	}

	public SrPesquisa getPesquisaDesignada() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.setItemConfiguracaoFiltro(itemConfiguracao);
		confFiltro.setAcaoFiltro(acao);
		confFiltro.setBuscarPorPerfis(true);
		confFiltro.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_PESQUISA_SATISFACAO);
		
		SrConfiguracao conf = SrConfiguracao.buscarDesignacao(confFiltro);
		if (conf != null)
			return conf.getPesquisaSatisfacao();
		return null;
	}

	public Set<SrMovimentacao> getPendenciasEmAberto() {
		Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		Set<SrMovimentacao> setPendentes = new HashSet<SrMovimentacao>();

		for (SrMovimentacao ini : setIni) {
			if ((!ini.isFinalizada()) && 
					(ini.getDtAgenda() == null || ini.getDtAgenda().after(new Date())))
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
	private List<SrAtributo> getAtributoAssociados(HashMap<Long, Boolean> map) throws Exception {
		List<SrAtributo> listaFinal = new ArrayList<SrAtributo>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.setItemConfiguracaoFiltro(itemConfiguracao);
		confFiltro.setAcaoFiltro(acao);

		for (SrAtributo t : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
			confFiltro.setAtributo(t);
			SrConfiguracao conf = SrConfiguracao.buscarAssociacao(confFiltro);
			if (conf != null) {
				listaFinal.add(t);
				if (map != null)
					map.put(t.getIdAtributo(), conf.isAtributoObrigatorio());
			}
		}

		return listaFinal;
	}

	public DpLotacao getPosAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.setItemConfiguracaoFiltro(itemConfiguracao);
		confFiltro.setAcaoFiltro(acao);
		confFiltro.setBuscarPorPerfis(true);
		confFiltro.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE);
		
		SrConfiguracao conf = SrConfiguracao.buscarDesignacao(confFiltro);
		if (conf != null)
			return conf.getPosAtendente().getLotacaoAtual();
		return null;
	}

	public DpLotacao getUltimoAtendenteEtapaAtendimento() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_ANDAMENTO)
				return mov.getLotaAtendente();
		}
		return null;
	}

	// Edson: poderia também guardar num HashMap transiente e, ao salvar(),
	// mandar criar os atributos, caso se quisesse permitir um
	// solicitacao.getAtributoSet().put...
	public void setAtributoSolicitacaoMap(HashMap<Long, String> atributosSolicitacao) throws Exception {
		if (atributosSolicitacao != null) {
			meuAtributoSolicitacaoSet = new ArrayList<SrAtributoSolicitacao>();
			for (Long idAtt : atributosSolicitacao.keySet()) {
				SrAtributo att = SrAtributo.AR.findById(idAtt);
				SrAtributoSolicitacao attSolicitacao = new SrAtributoSolicitacao(att, atributosSolicitacao.get(idAtt), this);
				meuAtributoSolicitacaoSet.add(attSolicitacao);
			}
		}
	}

	public HashMap<Long, String> getAtributoSolicitacaoMap() {
		HashMap<Long, String> map = new LinkedHashMap<Long, String>(); // Para manter a ordem de insercao
		if (meuAtributoSolicitacaoSet != null)
			for (SrAtributoSolicitacao att : meuAtributoSolicitacaoSet) {
				map.put(att.getAtributo().getIdAtributo(), att.getValorAtributoSolicitacao());
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
		
		if (solicitacaoInicial != null){
			for (SrSolicitacao sol : getHistoricoSolicitacao()) {
				if (sol.meuSolicitacaoFilhaSet != null)
					for (SrSolicitacao filha : sol.meuSolicitacaoFilhaSet)
						if (filha.getHisDtFim() == null)
							listaCompleta.add(filha);
			}
		}
		return listaCompleta;
	}
	
	public Set<SrSolicitacao> getSolicitacaoFilhaSetRecursivo(){
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
		for (SrMarca m : getMarcaSet()){
			if ((m.getDtIniMarca() == null  || m.getDtIniMarca().before(agora)) 
					&& (m.getDtFimMarca() == null || m.getDtFimMarca().after(agora)))
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
					&& (ini.getDtAgenda() == null || ini.getDtAgenda().after(new Date())))

				return true;
		}
		return false;
	}

	public boolean isEmPosAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEmPreAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEmAtendimento() {
		long idsTpsMovs[] = { TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_REABERTURA };
		return sofreuMov(idsTpsMovs, TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}
	
	public boolean isEscalonada() {
		return sofreuMov(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO, 
					SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
	}
	
	public boolean isAFechar(){
		Set<SrSolicitacao> filhas = getSolicitacaoFilhaSet();
		if (filhas.size() == 0)
			return false;
		for (SrSolicitacao filha : filhas){
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
				if (mov.getTipoMov().getIdTipoMov() == idTpMov)
					return true;
				else
					for (long idTpReversor : idsTpsReversores)
						if (mov.getTipoMov().getIdTipoMov() == idTpReversor)
							return false;
		}
		return false;
	}

	public SrSolicitacao getSolicitacaoPrincipal() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO)
				return null;
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				return mov.getSolicitacaoReferencia();
		}
		return null;
	}

	public boolean temPreAtendenteDesignado() throws Exception {
		return (getPreAtendenteDesignado() != null);
	}

	public boolean temPesquisaSatisfacao() throws Exception {
		return getPesquisaDesignada() != null;
	}

	public boolean temPosAtendenteDesignado() throws Exception {
		return (getPosAtendenteDesignado() != null);
	}

	public boolean temAtendenteDesignado() throws Exception {
		return (getAtendenteDesignado() != null);
	}

	public boolean estaCom(DpLotacao lota, DpPessoa pess) {
		SrMovimentacao ultMov = getUltimaMovimentacao();
		SrMovimentacao ultMovDoPai = null;
		if (isFilha())
			 ultMovDoPai = this.solicitacaoPai.getUltimaMovimentacao();
		if (isRascunho())
			return foiCadastradaPor(lota, pess) || foiSolicitadaPor(lota, pess);
		return (ultMov != null && ((ultMov.getAtendente() != null && pess != null && ultMov.getAtendente().equivale(pess)) 
					|| (ultMov.getLotaAtendente() != null && ultMov.getLotaAtendente().equivale(lota))))

					|| (ultMovDoPai != null && ((ultMovDoPai.getAtendente() != null && ultMovDoPai.getAtendente().equivale(pess))
												|| (ultMovDoPai.getLotaAtendente() != null && ultMovDoPai.getLotaAtendente().equivale(lota))));

	}

	public boolean estaForaAtendenteDesignado() throws Exception {
		return !estaCom(getAtendenteDesignado(), null) && isEmAtendimento();
	}

	public boolean foiSolicitadaPor(DpLotacao lota, DpPessoa pess) {
		return (solicitante.equivale(pess) || lotaSolicitante.equivale(lota));
	}

	public boolean foiCadastradaPor(DpLotacao lota, DpPessoa pess) {
		return (cadastrante.equivale(pess) || lotaCadastrante.equivale(lota));
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

	public boolean podeEscalonar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && (isEmAtendimento() || isEmPreAtendimento());
	}

	public boolean podeJuntar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && (isEmAtendimento() || isPendente())
				&& !isJuntada();
	}

	public boolean podeDesentranhar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isJuntada();
	}

	public boolean podeVincular(DpLotacao lotaTitular, DpPessoa titular) {
		return !isRascunho();
	}

	public boolean podeDesfazerMovimentacao(DpLotacao lota, DpPessoa pess) {
		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		if (ultCancelavel == null || ultCancelavel.getCadastrante() == null)
			return false;
		return ultCancelavel.getLotaCadastrante().equivale(lota);
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return (estaCom(lota, pess) || isEmListaPertencenteA(lota))
				&& (isEmPreAtendimento() || isEmAtendimento() || isRascunho())
				&& (!jaFoiDesignada());
	}

	public boolean podePriorizar(DpLotacao lota, DpPessoa pess) {
		return podeEditar(lota, pess);
	}

	public boolean podeAbrirJaFechando(DpLotacao lota, DpPessoa pess) {
		return false;
	}

	public boolean podeRetornarAoPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		return isEmAtendimento() && estaCom(lota, pess)
				&& (getPreAtendenteDesignado() != null);
	}

	public boolean podeFinalizarPreAtendimento(DpLotacao lota, DpPessoa pess) {
		return isEmPreAtendimento() && estaCom(lota, pess);
	}

	public boolean podeFechar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess)
				&& (isEmPreAtendimento() || isEmAtendimento()
						|| isEmPosAtendimento());
	}

	public boolean podeExcluir(DpLotacao lota, DpPessoa pess) {
		return foiCadastradaPor(lota, pess) && isRascunho();
	}

	public boolean podeRetornarAoAtendimento(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmPosAtendimento();
	}

	public boolean podeCancelar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess)
				&& (isEmPreAtendimento() || isEmAtendimento());
	}

	public boolean podeDeixarPendente(DpLotacao lota, DpPessoa pess) {
		return isRascunho()
				|| ((isEmAtendimento() || isPendente()) && estaCom(lota, pess));
	}

	public boolean podeAlterarPrazo(DpLotacao lota, DpPessoa pess) {
		return !isRascunho() && !isFechado() && estaCom(lota, pess);
	}

	public boolean podeTerminarPendencia(DpLotacao lota, DpPessoa pess) {
		return isPendente() && estaCom(lota, pess);
	}

	public boolean podeReabrir(DpLotacao lota, DpPessoa pess) {
		return isFechado()
				&& (estaCom(lota, pess) || foiCadastradaPor(lota, pess) || foiSolicitadaPor(
					lota, pess));
	}

	public boolean podeAnexarArquivo(DpLotacao lota, DpPessoa pess) {
		return (isEmPreAtendimento() || isEmAtendimento() || isPendente() || isRascunho());
	}

	public boolean podeImprimirTermoAtendimento(DpLotacao lota, DpPessoa pess) {
		return isEmAtendimento() && estaCom(lota, pess);
	}

	public boolean podeIncluirEmLista(DpLotacao lota, DpPessoa pess) {
		return isEmAtendimento();
	}

	public boolean podeTrocarAtendente(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmAtendimento();
	}

	public boolean podeResponderPesquisa(DpLotacao lotaTitular, DpPessoa titular)
			throws Exception {

		if (!isFechado() || !foiSolicitadaPor(lotaTitular, titular)
				|| !temPesquisaSatisfacao())
			return false;

		for (SrMovimentacao mov : getMovimentacaoSet())
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO)
				return false;
			else if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;

		return false;

	}
	
	public boolean podeFecharPaiAutomatico() {
		return isFilha() && solicitacaoPai.getSolicitacaoAtual().fechadoAutomaticamente 
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
		List<SrSolicitacao> listaProvisoria = em().createQuery(queryString)
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
			listaProvisoria = em().createQuery(queryString).getResultList();
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
			locais = 
			em()
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
		
		List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarConfiguracoes();
		
		listaTodosItens = SrItemConfiguracao.listar(false);
		
		for (SrItemConfiguracao i : listaTodosItens) {
			if (!i.isEspecifico())
				continue;
			for (SrConfiguracao c : listaPessoasAConsiderar) 
				if (!listaFinal.contains(i)) {
					
					c.setItemConfiguracaoFiltro(i);
					c.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
					
					if (SrConfiguracao.buscarDesignacao(c,
							new int[] { SrConfiguracaoBL.ACAO}) != null){
						listaFinal.add(i);
						SrItemConfiguracao itemPai = i.getPai();
						while (itemPai != null) {
							if (!listaFinal.contains(itemPai))
								listaFinal.add(itemPai);
							itemPai = itemPai.getPai();
						}
					}
				}
		}

		Collections.sort(listaFinal, new SrItemConfiguracaoComparator());

		return listaFinal;
	}

	public List<SrAcao> getAcoesDisponiveis() throws Exception {
		Map<SrAcao, DpLotacao> acoesEAtendentes = getAcoesDisponiveisComAtendente();
		return acoesEAtendentes != null ? new ArrayList<SrAcao>(acoesEAtendentes.keySet()) : null;
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendente()
			throws Exception {

		if (solicitante == null || itemConfiguracao == null)
			return null;

		Map<SrAcao, DpLotacao> listaFinal = new HashMap<SrAcao, DpLotacao>();
		
		List<SrConfiguracao> listaPessoasAConsiderar = getFiltrosParaConsultarConfiguracoes();

		for (SrAcao a : SrAcao.listar(false)) {
			if (!a.isEspecifico())
				continue;
			for (SrConfiguracao c : listaPessoasAConsiderar) 
				if (!listaFinal.containsKey(a)) {
					
					c.setItemConfiguracaoFiltro(itemConfiguracao);
					c.setSubTipoConfig(SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
					c.setAcaoFiltro(a);
					
					SrConfiguracao conf = SrConfiguracao.buscarDesignacao(c);
					if (conf != null)
						listaFinal.put(a, conf.getAtendente());
				}
		}

		return listaFinal;
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendenteOrdemSigla()
			throws Exception {
		Map<SrAcao, DpLotacao> m = new TreeMap<SrAcao, DpLotacao>(
				new Comparator<SrAcao>() {
					@Override
					public int compare(SrAcao o1, SrAcao o2) {
						if (o1 != null && o2 != null && o1.getIdAcao() == o2.getIdAcao())
							return 0;
						return o1.getSiglaAcao().compareTo(o2.getSiglaAcao());
					}
				});

		m.putAll(getAcoesDisponiveisComAtendente());
		return m;
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendenteOrdemTitulo()
			throws Exception {
		Map acoesEAtendentes = getAcoesDisponiveisComAtendente();

		if (acoesEAtendentes != null){
		Map<SrAcao, DpLotacao> m = new TreeMap<SrAcao, DpLotacao>(
				new Comparator<SrAcao>() {
					@Override
					public int compare(SrAcao o1, SrAcao o2) {
						int i = o1.getTituloAcao().compareTo(o2.getTituloAcao());
						if (i != 0)
							return i;
						return o1.getIdAcao().compareTo(o2.getIdAcao());
					}
				});

			m.putAll(acoesEAtendentes);
			return m;
		}
		return null;
	}
	
	public Map<SrAcao, DpLotacao> getAcoesEAtendentes() throws Exception {
		Map<SrAcao, DpLotacao> acoesEAtendentesFinal = this
				.getAcoesDisponiveisComAtendenteOrdemTitulo();
		if (acoesEAtendentesFinal != null && this.itemConfiguracao != null){
			if (this.acao == null
					|| !acoesEAtendentesFinal.containsKey(this.acao)) {
				if (acoesEAtendentesFinal.size() > 0)
					this.acao = acoesEAtendentesFinal.keySet().iterator().next();
				else
					this.acao = null;
			}
		}
		return acoesEAtendentesFinal;
	}

	@SuppressWarnings("serial")
	public SortedSet<SrOperacao> operacoes(final DpLotacao lotaTitular,
			final DpPessoa titular)
					throws Exception {
		SortedSet<SrOperacao> operacoes = new TreeSet<SrOperacao>() {
			@Override
			public boolean add(SrOperacao e) {
				// Edson: ser� que essas coisas poderiam estar dentro do
				// SrOperacao?
				if (e.params == null)
					e.params = new HashMap<String, Object>();
				e.params.put("id", idSolicitacao);

				//if (!e.isModal())
				//	e.url = Router.reverse(e.url, e.params).url;
				if (!e.pode)
					return false;
				return super.add(e);
			}
		};

		operacoes.add(new SrOperacao("pencil", "Editar", podeEditar(
				lotaTitular, titular), "Application.editar"));

		operacoes.add(new SrOperacao("table_relationship", "Vincular",
				podeVincular(lotaTitular, titular),
				"vincular", "modal=true"));

		operacoes.add(new SrOperacao("arrow_divide",
				"Escalonar", podeEscalonar(lotaTitular, titular), 
				"escalonar", "modal=true")); 

/*		operacoes.add(new SrOperacao("arrow_divide",
				"Escalonar", podeEscalonar(lotaTitular, titular), 
				"Application.escalonar"));*/

		operacoes.add(new SrOperacao("arrow_join", "Juntar",
				podeJuntar(lotaTitular, titular),
				"juntar", "modal=true"));
		
		operacoes.add(new SrOperacao("arrow_out", "Desentranhar",
				podeDesentranhar(lotaTitular, titular), "desentranhar", "modal=true"));
		
		operacoes.add(new SrOperacao("text_list_numbers", "Incluir em Lista",
				podeIncluirEmLista(lotaTitular, titular), "incluirEmLista",
				"modal=true"));
		
		operacoes.add(new SrOperacao("lock", "Fechar", podeFechar(lotaTitular,
				titular), "fechar", "modal=true"));

		operacoes.add(new SrOperacao("script_edit", "Responder Pesquisa",
				podeResponderPesquisa(lotaTitular, titular),
				"responderPesquisa", "modal=true"));

		operacoes.add(new SrOperacao("arrow_rotate_anticlockwise",
				"Retornar ao Pré-Atendimento", podeRetornarAoPreAtendimento(
						lotaTitular, titular),
				"Application.retornarAoPreAtendimento"));

		operacoes.add(new SrOperacao("accept", "Finalizar Pré-Atendimento",
				podeFinalizarPreAtendimento(lotaTitular, titular),
				"Application.finalizarPreAtendimento"));

		operacoes.add(new SrOperacao("accept", "Retornar Ao Atendimento",
				podeRetornarAoAtendimento(lotaTitular, titular),
				"Application.retornarAoAtendimento"));

		operacoes.add(new SrOperacao("cross", "Cancelar Solicitação",
				podeCancelar(lotaTitular, titular), "Application.cancelar"));

		operacoes.add(new SrOperacao("lock_open", "Reabrir", podeReabrir(
				lotaTitular, titular), "Application.reabrir"));

		operacoes.add(new SrOperacao("clock_pause", "Incluir Pendência",
				podeDeixarPendente(lotaTitular, titular), "pendencia",
				"modal=true"));

		/*operacoes.add(new SrOperacao("clock_edit", "Alterar Prazo",
				podeAlterarPrazo(lotaTitular, titular), "alterarPrazo",
				"modal=true"));*/

		operacoes.add(new SrOperacao("cross", "Excluir", "Application.excluir",
				podeExcluir(lotaTitular, titular),
				"Deseja realmente excluir esta solicitação?", null, "", ""));

		operacoes.add(new SrOperacao("attach", "Anexar Arquivo",
				podeAnexarArquivo(lotaTitular, titular), "anexarArquivo",
				"modal=true"));

		operacoes.add(new SrOperacao("printer", "Termo de Atendimento",
				podeImprimirTermoAtendimento(lotaTitular, titular),
				"Application.termoAtendimento", "popup=true"));

		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		operacoes.add(new SrOperacao("cancel", "Desfazer "
				+ (ultCancelavel != null ? ultCancelavel.getTipoMov().getNome() : ""),
				podeDesfazerMovimentacao(lotaTitular, titular),
				"Application.desfazerUltimaMovimentacao"));

		return operacoes;
	}

	public void salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		salvar();
	}

	public void salvar() throws Exception {

		checarEPreencherCampos();
		//Edson: Ver por que isto est� sendo necess�rio. Sem isso, ap�s o salvar(),
		//ocorre LazyIniException ao tentar acessar esses meuMovimentacaoSet's
		if (solicitacaoInicial != null)
			for (SrSolicitacao s : solicitacaoInicial.meuSolicitacaoHistoricoSet){
				for (SrMovimentacao m : s.meuMovimentacaoSet){}
			}

		super.salvarComHistorico();

		//Edson: melhorar isto, pra nao precisar salvar novamente

		if (isRascunho()) {
			atualizarCodigo();
			save();
		}

		if (!isRascunho() && !jaFoiDesignada()) {

			if (fecharAoAbrir)
				fechar(lotaCadastrante, cadastrante, motivoFechamentoAbertura);
			else if (temPreAtendenteDesignado() && atendenteNaoDesignado == null)
				iniciarPreAtendimento(lotaCadastrante, cadastrante);
			else
				iniciarAtendimento(lotaCadastrante, cadastrante, atendenteNaoDesignado);

			for (SrLista lista : getListasParaInclusaoAutomatica(lotaCadastrante)) {
				incluirEmLista(lista, cadastrante, lotaCadastrante);
			}
            if (!isEditado())
            	SrCorreio.notificarAbertura(this);
		} else
			atualizarMarcas();
	}

	public void excluir() throws Exception{
		finalizar();
		for (SrMarca e : getMarcaSet()) {
			e.getSolicitacao().meuMarcaSet.remove(e);
			e.delete();
		}
	}
	
	public void atualizarAcordos() throws Exception{
		acordos = new ArrayList<SrAcordo>();
				
		for (SrConfiguracao c : getFiltrosParaConsultarConfiguracoes()) {
			
			c.setItemConfiguracaoFiltro(itemConfiguracao);
			c.setAcaoFiltro(acao);
			c.setPrioridade(prioridade);
			c.setAtendente(getAtendenteDesignado());
			c.setCpTipoConfiguracao(em().find(CpTipoConfiguracao.class,
					CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
			
			List<SrConfiguracao> confs = SrConfiguracao.listar(c);
			for (SrConfiguracao conf : confs) {
				SrAcordo acordoAtual = ((SrAcordo) SrAcordo
						.AR.findById(conf.getAcordo().getIdAcordo())).getAcordoAtual();
				if (acordoAtual != null && !acordos.contains(acordoAtual))
					acordos.add(acordoAtual);
			}
		}
	}

	private void checarEPreencherCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		if (dtReg == null)
			dtReg = new Date();

		if (arquivo != null) {
			double lenght = (double) arquivo.getBlob().length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) � maior que o m�ximo permitido (2MB)");
		}

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();

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
			if (!isRascunho()){
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
		if (!isRascunho() && !temAtendenteDesignado()
				&& !temPreAtendenteDesignado())
			throw new Exception(
					"Não foi encontrado nenhum atendente designado "
							+ "para esta solicitação. Sugestão: alterar item de "
							+ "configuração e/ou ação");
		
		atualizarAcordos();
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		if (!podeDesfazerMovimentacao(lotaCadastrante, cadastrante))
			throw new Exception("Operação não permitida");

		SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();

		// tratamento pois pode ter retorno nulo do método
		// getUltimaMovimentacaoCancelave()
		if (movimentacao != null) {

			if (movimentacao.getTipoMov() != null) {
				// caso seja movimentacao cancelada ou fechada, reinsere nas
				// listas de prioridade
				if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO
						|| movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_FECHAMENTO)
					reInserirListasDePrioridade(lotaCadastrante, cadastrante);

				if (movimentacao.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_JUNTADA) {
					this.save();
				}
			}

			movimentacao.desfazer(cadastrante, lotaCadastrante);
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
	
	public void atualizarMarcas() throws Exception{
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
			e.getSolicitacao().meuMarcaSet.remove(e);
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

	private SortedSet<SrMarca> calcularMarcadores() throws Exception{
		SortedSet<SrMarca> set = new TreeSet<SrMarca>();

		if (isRascunho())
			acrescentarMarca(set,
					CpMarcador.MARCADOR_SOLICITACAO_EM_ELABORACAO, null, null,
					cadastrante, lotaCadastrante);

		Set<SrMovimentacao> movs = getMovimentacaoSetOrdemCrescente();

		if (movs != null && movs.size() > 0) {
			Long marcador = 0L;
			SrMovimentacao movMarca = null;

			int pendencias = 0;
			Date dtFimPendenciaMaisLonge = null;
			SrMovimentacao movPendencia = null;

			for (SrMovimentacao mov : movs) {
				Long t = mov.getTipoMov().getIdTipoMov();
				if (mov.isCancelada())
					continue;
				if (t == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO;
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
					pendencias++;
					if (mov.getDtAgenda() != null && (dtFimPendenciaMaisLonge == null || mov.getDtAgenda().after(dtFimPendenciaMaisLonge)))
						dtFimPendenciaMaisLonge = mov.getDtAgenda();
					movPendencia = mov;
				}
				if (t == TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
					pendencias--;
				}
				if (t == TIPO_MOVIMENTACAO_ANDAMENTO || t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO) {
					movMarca = mov;
				}
				
			}

			if (marcador != 0L) 
				acrescentarMarca(set, marcador, movMarca.getDtIniMov(), null,
						movMarca.getAtendente(), movMarca.getLotaAtendente());

			if (marcador == CpMarcador.MARCADOR_SOLICITACAO_FECHADO && isFilha())
				solicitacaoPai.atualizarMarcas();
			
			if (!isFechado() && isAFechar())
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_FECHADO_PARCIAL, movMarca.getDtIniMov(),
						null, movMarca.getAtendente(), movMarca.getLotaAtendente());
			
			if (pendencias > 0){
				if (isRascunho())
					acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, movPendencia.getDtIniMov(), dtFimPendenciaMaisLonge,
							cadastrante, lotaCadastrante);
				else acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, movPendencia.getDtIniMov(), dtFimPendenciaMaisLonge,
						movPendencia.getAtendente(), movPendencia.getLotaAtendente());
			}

			if (marcador != 0L)
				acrescentarMarca(set, marcador, movMarca.getDtIniMov(), null,
						movMarca.getAtendente(), movMarca.getLotaAtendente());

			if (pendencias > 0) {
				if (isRascunho())
					acrescentarMarca(set,
							CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
							movPendencia.getDtIniMov(), dtFimPendenciaMaisLonge,
							cadastrante, lotaCadastrante);
				else
					acrescentarMarca(set,
							CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
							movPendencia.getDtIniMov(), dtFimPendenciaMaisLonge,
							movPendencia.getAtendente(), movPendencia.getLotaAtendente());
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
							CpMarcador.MARCADOR_SOLICITACAO_FORA_DO_PRAZO, prazo, null,
							movMarca.getAtendente(), movMarca.getLotaAtendente());
			}
		}

		return set;
	}

	private void encaixar(SortedSet<SrMarca> setA, SortedSet<SrMarca> setB,
			Set<SrMarca> incluir, Set<SrMarca> excluir, Set<Par<SrMarca, SrMarca>> atualizar) {
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
			Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota) throws Exception{
		SrMarca mar = new SrMarca();
		// Edson: nao eh necessario ser this.solicitacaoInicial em vez de this
		// porque este metodo soh eh chamado por atualizarMarcas(), que ja se
		// certifica de chamar este metodo apenas para a solicitacao inicial
		mar.setSolicitacao(this);
		mar.setCpMarcador((CpMarcador) CpMarcador.AR.findById(idMarcador));
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
				String nome = mar.getDpPessoaIni().getDescricaoIniciaisMaiusculas();
				sb.append(nome.substring(0, nome.indexOf(" ")));
				sb.append(", ");
			}
			if (mar.getDpLotacaoIni() != null) {
				sb.append(mar.getDpLotacaoIni().getSigla());
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

	public List<SrLista> getListasParaInclusaoAutomatica(DpLotacao lotaTitular)
			throws Exception {
		List<SrLista> listaFinal = new ArrayList<SrLista>();
		/*
		 * Edson:DB1, acertar conforme o item 24
		 * 
		 * List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(
		 * solicitante, local, itemConfiguracao, acao,
		 * CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
		 * SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE, new int[] {}); for
		 * (SrConfiguracao conf : confs) { for (SrLista lista :
		 * conf.getListaConfiguracaoSet()) { SrLista listaAtual =
		 * lista.getListaAtual(); if (!listaFinal.contains(listaAtual))
		 * listaFinal.add(listaAtual); } }
		 */
		return new ArrayList<SrLista>(listaFinal);
	}

	public List<SrLista> getListasDisponiveisParaInclusao(
			DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);

		for (SrLista l : SrLista.listar(false)) {
			if (l.podeIncluir(lotaTitular, cadastrante))
				listaFinal.add(l);
		}

		listaFinal.removeAll(getListasAssociadas());
		Collections.sort(listaFinal, new Comparator<SrLista>() {
			@Override
			public int compare(SrLista l1, SrLista l2) {
				return l1.getNomeLista().compareTo(l2.getNomeLista());
			}
		});
		return listaFinal;
	}

	public Set<SrLista> getListasAssociadas() {
		Set<SrLista> associadas = new HashSet<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
			if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_INCLUSAO_LISTA
			|| mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
			&& mov.getLista().isAtivo())
				associadas.add(mov.getLista());
			else if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
				associadas.remove(mov.getLista());
		return associadas;
	}

	public Set<SrSolicitacao> getSolicitacoesVinculadas() {
		Set<SrSolicitacao> solVinculadas = new HashSet<SrSolicitacao>();
		
		//vincula��es partindo desta solicita��o
		for (SrMovimentacao mov : getMovimentacaoSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
			if (mov.getTipoMov().getIdTipoMov() == TIPO_MOVIMENTACAO_VINCULACAO)
				if(mov.getSolicitacaoReferencia() != null)
					solVinculadas.add(mov.getSolicitacaoReferencia());

		//vincula��es partindo de outra solicita��o referenciando esta
		for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO))
			if (this.equals(mov.getSolicitacaoReferencia()))
				solVinculadas.add(mov.getSolicitacao());
		return solVinculadas;
	}
	
	public Set<SrSolicitacao> getSolicitacoesJuntadas() {
		Set<SrSolicitacao> solJuntadas = new HashSet<SrSolicitacao>();

		for (SrMovimentacao mov : getMovimentacaoReferenciaSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA))
			if (!mov.isFinalizada() && this.equals(mov.getSolicitacaoReferencia()))
				solJuntadas.add(mov.getSolicitacao());
		return solJuntadas;
	}

	public boolean isEmLista() {
		return getListasAssociadas().size() > 0;
	}

	public boolean isEmListaPertencenteA(DpLotacao lota) {
		for (SrLista l : getListasAssociadas()) {
			if (l.getLotaCadastrante().equivale(lota))
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
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.getLista() != null && mov.getLista().equivale(lista))
				return mov.getPrioridade() != null ? mov.getPrioridade() : -1;
		}
		return -1;
	}

	public void incluirEmLista(SrLista lista, DpPessoa pess, DpLotacao lota)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		if (isEmLista(lista))
			throw new IllegalArgumentException("Lista " + lista.getNomeLista()
					+ " já contém a solicitação " + getCodigo());

		SrMovimentacao mov = new SrMovimentacao();
		mov.setPrioridade((long) lista.getProximaPosicao());
		mov.setCadastrante(pess);
		mov.setLotaCadastrante(lota);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_INCLUSAO_LISTA));
		mov.setDescrMovimentacao("Inclus�o na lista " + lista.getNomeLista()
				+ " com a prioridade " + mov.getPrioridade());
		mov.setLista(lista);
		mov.setSolicitacao(this);
		mov.salvar();
		lista.refresh();
	}

	public void retirarDeLista(SrLista lista, DpPessoa pess, DpLotacao lota)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		SrMovimentacao mov = new SrMovimentacao();
		mov.setCadastrante(pess);
		mov.setLotaCadastrante(lota);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA));
		mov.setDescrMovimentacao("Cancelamento de Inclus�o em Lista");
		mov.setSolicitacao(this);
		mov.setLista(lista);
		mov.salvar();
		lista.refresh();

		lista.recalcularPrioridade(pess, lota);
	}

	// Edson: este método é protected porque nao pode ser chamado pelo
	// usu�rio,
	// mas sim pela SrLista, passando a posicao correta a ser colocada a
	// solicitacao
	protected void priorizar(SrLista lista, long prioridade, DpPessoa pess,
			DpLotacao lota) throws Exception {
		SrMovimentacao mov = new SrMovimentacao();
		mov.setPrioridade(prioridade);
		mov.setCadastrante(pess);
		mov.setLotaCadastrante(lota);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA));
		mov.setDescrMovimentacao("Alteração de prioridade na lista "
				+ lista.getNomeLista() + ": " + mov.getPrioridade());
		mov.setLista(lista);
		mov.setSolicitacao(this);
		mov.salvar();
		lista.refresh();
	}

	private void iniciarPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.setLotaAtendente( getPreAtendenteDesignado());
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO));
		mov.salvar(pess, lota);
	}

	public void finalizarPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeFinalizarPreAtendimento(lota, pess))
			throw new Exception("Operação não permitida");
		iniciarAtendimento(lota, pess, null);
	}

	public void retornarAoPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeRetornarAoPreAtendimento(lota, pess))
			throw new Exception("Operação não permitida");
		iniciarPreAtendimento(lota, pess);
	}

	private void iniciarAtendimento(DpLotacao lota, DpPessoa pess, DpLotacao lotaAtend)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO));
		if (lotaAtend == null)
			mov.setLotaAtendente( mov.getSolicitacao().getAtendenteDesignado());
		else
			mov.setLotaAtendente( lotaAtend);
		mov.salvar(pess, lota);
	}

	private void iniciarPosAtendimento(DpLotacao lota, DpPessoa pess,
			String motivo) throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO));
		mov.setLotaAtendente( mov.getSolicitacao().getPosAtendenteDesignado());
		mov.setDescrMovimentacao(motivo);
		mov.salvar(pess, lota);
	}

	public void fechar(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {
		if(isPai() && !isAFechar())
			throw new Exception("Operação não permitida. Necessário fechar toda solicitação " + 
									"filha criada partir dessa que deseja fechar.");
			
		if ((pess != null) && !podeFechar(lota, pess))
			throw new Exception("Operação não permitida");

		if ((isEmPreAtendimento() || isEmAtendimento())
				&& temPosAtendenteDesignado()) {
			iniciarPosAtendimento(lota, pess, motivo);
		} else {
			fecharTotalmente(lota, pess, motivo);
			if (podeFecharPaiAutomatico())
				solicitacaoPai.fechar(solicitacaoPai.getLotaAtendente(), pess, 
								"Solicitação fechada automaticamente");
	
			if (temPesquisaSatisfacao())
				enviarPesquisa();
		}
		
	}

	public void enviarPesquisa() throws Exception {
		// Implementar
		SrCorreio.pesquisaSatisfacao(this);
	}

	public void responderPesquisa(DpLotacao lota, DpPessoa pess,
			Map<Long, String> respostaMap) throws Exception {
		if (!podeResponderPesquisa(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setPesquisa(this.getPesquisaDesignada());
		movimentacao.setDescrMovimentacao("Avaliação realizada.");
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_AVALIACAO));
		movimentacao.setRespostaMap(respostaMap);
		movimentacao.salvar(pess, lota);
	}

	public void retornarAoAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeRetornarAoAtendimento(lota, pess))
			throw new Exception("Operação não permitida");
		iniciarAtendimento(lota, pess, getLotaAtendente());
	}

	private void fecharTotalmente(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.setTipoMov(SrTipoMovimentacao.AR.findById(TIPO_MOVIMENTACAO_FECHAMENTO));
		mov.setDescrMovimentacao(motivo);
		mov.salvar(pess, lota);

		// remove das listas apos finalizar, para que seja possivel reabrir
		// depois
		removerDasListasDePrioridade(lota, pess);
	}

	private void removerDasListasDePrioridade(DpLotacao lota, DpPessoa pess)
			throws Exception {
		for (SrLista lista : this.getListasAssociadas()) {
			this.retirarDeLista(lista, pess, lota);
		}
	}

	public void reabrir(DpLotacao lota, DpPessoa pess) throws Exception {
		if (!podeReabrir(lota, pess))
			throw new Exception("Operação não permitida");
		reInserirListasDePrioridade(lota, pess);

		SrMovimentacao mov = new SrMovimentacao(this);
		mov.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA));
		mov.setLotaAtendente(getUltimoAtendenteEtapaAtendimento());
		mov.salvar(pess, lota);
	}

	private void reInserirListasDePrioridade(DpLotacao lota, DpPessoa pess)
			throws Exception {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO
					|| mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO)
				break;

			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
				incluirEmLista(mov.getLista(), pess, lota);

		}
	}

	public void deixarPendente(DpLotacao lota, DpPessoa pess,
			SrTipoMotivoPendencia motivo, String calendario, String horario,
			String detalheMotivo) throws Exception {
		if (!podeDeixarPendente(lota, pess))
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
				movimentacao.setDtAgenda(dateTime.toDate());
		}
		
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA));
		movimentacao.setDescrMovimentacao(detalheMotivo);
		movimentacao.setMotivoPendencia(motivo);
		movimentacao.salvar(pess, lota);
	}

	public void alterarPrazo(DpLotacao lota, DpPessoa pess, String motivo,
			String calendario, String horario) throws Exception {
		if (!podeAlterarPrazo(lota, pess))
			throw new Exception("Opera��o n�o permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		DateTime datetime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("dd/MM/yyyy HH:mm");
		if (!calendario.equals("")) {
			datetime = new DateTime(formatter.parseDateTime(calendario + " "
					+ horario));
			movimentacao.setDtAgenda(datetime.toDate());
		}

		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO));
		movimentacao.setDescrMovimentacao("Prazo alterado para " + calendario + " " 
				+ horario + " - " + motivo);
		movimentacao.salvar(pess, lota);
	}

	public void terminarPendencia(DpLotacao lota, DpPessoa pess, String descricao, Long idMovimentacao)
			throws Exception {
		if (!podeTerminarPendencia(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA));
		movimentacao.setDescrMovimentacao(descricao);
		
		// Edson: eh necessario setar a finalizadora na finalizada antes de 
		// salvar() a finalizadora, pq se n�o, ao atualizarMarcas(), vai 
		// parecer que a pendencia nao foi finalizada, atrapalhando calculos 
		// de prazo
		SrMovimentacao movFinalizada = SrMovimentacao.AR.findById(idMovimentacao);
		movFinalizada.setMovFinalizadora(movimentacao);
		
		movimentacao = movimentacao.salvar(pess, lota);
		movFinalizada.save();
	}

	public void cancelar(DpLotacao lota, DpPessoa pess) throws Exception {
		if (!podeCancelar(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO));
		movimentacao.salvar(pess, lota);

		// remove das listas apos finalizar, para que seja possivel reabrir
		// depois
		removerDasListasDePrioridade(lota, pess);
	}

	public void juntar(DpLotacao lota, DpPessoa pess,
			SrSolicitacao solRecebeJuntada, String justificativa)
			throws Exception {
		if ((pess != null) && !podeJuntar(lota, pess))
			throw new Exception("Operação não permitida");
		if (solRecebeJuntada.equivale(this))
	        throw new Exception("Não e possivel juntar uma solicitação a si mesma.");
		if (solRecebeJuntada.isJuntada() && solRecebeJuntada.getSolicitacaoPrincipal().equivale(this))
	        throw new Exception("Não e possivel realizar juntada circular.");
		if (solRecebeJuntada.isFilha() && solRecebeJuntada.solicitacaoPai.equivale(this))
	        throw new Exception("Não e possivel juntar uma solicitação a uma das suas filhas. Favor realizar o processo inverso.");

		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_JUNTADA));
		movimentacao.setSolicitacaoReferencia(solRecebeJuntada);
		movimentacao.setDescrMovimentacao(justificativa + " | Juntando a " + solRecebeJuntada.codigo);
		movimentacao.salvar(pess, lota);

	}

	public void desentranhar(DpLotacao lota, DpPessoa pess, String justificativa)
			throws Exception {
		if (!podeDesentranhar(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_DESENTRANHAMENTO));
		movimentacao.setDescrMovimentacao(justificativa);
		movimentacao = movimentacao.salvar(pess, lota);

		SrMovimentacao juntada = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);
		juntada.setMovFinalizadora(movimentacao);
		juntada.save();
		
	}

	@SuppressWarnings("unchecked")
	public void vincular(DpLotacao lota, DpPessoa pess,
			SrSolicitacao solRecebeVinculo, String justificativa)
			throws Exception {
		if ((pess != null) && !podeVincular(lota, pess))
			throw new Exception("Operação não permitida");
		if (solRecebeVinculo.equivale(this))
	        throw new Exception("Não e possivel vincular uma solicita�ao a si mesma.");
		
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(TIPO_MOVIMENTACAO_VINCULACAO));
		movimentacao.setSolicitacaoReferencia(solRecebeVinculo);
		movimentacao.setDescrMovimentacao(justificativa + " | Vinculando a " + solRecebeVinculo.codigo);
		movimentacao.salvar(pess, lota);
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
			s += Texto.slugify(acao.getTituloAcao(), true, false);
		if (itemConfiguracao != null)
			s += "-"
					+ Texto.slugify(itemConfiguracao.getTituloItemConfiguracao(),
							true, false);
		return s;
	}

	public String getGcTituloAbertura() {
		String s = "";
		if (acao != null)
			s += acao.getTituloAcao();
		if (itemConfiguracao != null)
			s += " - " + itemConfiguracao.getTituloItemConfiguracao();
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
		if (dtOrigem != null){
			SigaCalendar cal = new SigaCalendar();
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
	
	public String getDtIniEdicaoDDMMYYYYHHMMSS(){
		if (dtIniEdicao != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return df.format(dtIniEdicao);
		}
		return "";
	}
	
	public void setDtIniEdicaoDDMMYYYYHHMMSS(String string) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try{
			this.dtIniEdicao = df.parse(string);
		} catch(Exception e){

		}
	}

	public Date getDtInicioPrimeiraEdicao() {
		if (solicitacaoInicial != null)
			return solicitacaoInicial.dtIniEdicao;
		else
			return this.dtIniEdicao;
	}
	
	public Date getDtInicioAtendimento(){
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO
			|| mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
				return mov.getDtIniMov();
		return null;
	}
	
	public Date getDtEfetivoFechamento() {
		SrMovimentacao fechamento = getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
		if (fechamento == null)
			return null;
		return fechamento.getDtIniMov();
	}
	
	// Edson: retorna os periodos de pendencia de forma linear, ou seja, 
	// colapsando as sobreposicoes, para facilitar os calculos:
	// Transforma: -------
	//                -------  
	//
	// em:         ----------  
	private Map<Date, Date> getTrechosPendentes() {
		Map<Date, Date> pendencias = new LinkedHashMap<Date, Date>();
		Date dtIniEmAberto = null, dtFimEmAberto = null;

		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			
			if (dtIniEmAberto != null
					&& dtFimEmAberto != null && mov.getDtIniMov().after(dtFimEmAberto)){
				dtIniEmAberto = null;
				dtFimEmAberto = null;
			}

			if (mov.getTipoMov().getIdTipoMov() == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				if (dtIniEmAberto == null)
					dtIniEmAberto = mov.getDtIniMov();
				if (dtFimEmAberto == null || (mov.getDtFimMov() != null && mov.getDtFimMov().after(dtFimEmAberto)))
					dtFimEmAberto = mov.getDtFimMov();
				pendencias.put(dtIniEmAberto, dtFimEmAberto);
				if (dtFimEmAberto == null)
					return pendencias;
			}	
		}
		return pendencias;
	}
	
	public Date getDataAPartirDe(Date dtBase, Integer segundosAdiante){
		Map<Date, Date> pendencias = getTrechosPendentes();
		for (Date dtIniPendencia : pendencias.keySet()){
			if (dtIniPendencia.before(dtBase))
				continue;
			Date dtFimPendencia = pendencias.get(dtIniPendencia);
			if (dtFimPendencia == null)
				return null;
			int delta = (int)(dtIniPendencia.getTime() - dtBase.getTime())/1000;
			if (delta <= segundosAdiante){
				segundosAdiante -= delta;
				dtBase = dtFimPendencia;
			}
			else break;
		}
		return new Date(dtBase.getTime() + segundosAdiante * 1000);
	}

	private Date getDtPrazoCadastramentoAcordado() {
		if(acordos == null || acordos.size() == 0 || isCancelado())
			return null;
		Integer menorTempoAcordado = null;
		for (SrAcordo a : acordos){
			Integer acordado = a.getAcordoAtual().getAtributoEmSegundos("tempoDecorridoCadastramento");
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
		Integer menorTempoAcordado = null;
		for (SrAcordo a : acordos){
			Integer acordado = a.getAcordoAtual().getAtributoEmSegundos("tempoDecorridoAtendimento");
			if (menorTempoAcordado == null || (acordado != null && acordado < menorTempoAcordado))
				menorTempoAcordado = acordado;
		}
		if (menorTempoAcordado == null)
			return null;
		return getDataAPartirDe(getDtInicioAtendimento(), menorTempoAcordado);
	}
	
	public Cronometro getCronometro() throws Exception {
		if (cron == null) {
			if (jaFoiDesignada()){
				cron = new Cronometro().setDescricao("Atendimento")
						.setInicio(getDtInicioAtendimento());
				if (isFechado()){
					cron.setFim(getDtEfetivoFechamento()).setLigado(false);
				} else {
					cron.setFim(getDtPrazoAtendimentoAcordado()).setLigado(true);
				}
			} else {
				cron = new Cronometro().setDescricao("Cadastro")
						.setInicio(getDtInicioPrimeiraEdicao());
					cron.setFim(getDtPrazoCadastramentoAcordado()).setLigado(true);
			}
		}
		return cron;
	}
	
	// Edson: retorna o tempo decorrido entre duas datas, descontando
	// os per�odos de pend�ncia (blocos).
	// PPP, abaixo, � o bloco pendente. I � dtIni e F � dtFim
	public SrValor getTempoDecorrido(Date dtIni, Date dtFim) {
		Map<Date, Date> pendencias = getTrechosPendentes();
		Integer decorrido = 0;
		for (Date dtIniBlocoPendencia : pendencias.keySet()) {
			Date dtFimBlocoPendencia = pendencias.get(dtIniBlocoPendencia);

			// ----------I----------F---PPPP---
			if (dtIniBlocoPendencia.after(dtFim))
				break;

			// ---PPPP---I----------F----------
			if (dtFimBlocoPendencia.before(dtIni))
				continue;

			// ---------PIPP--------F----------
			if (dtIniBlocoPendencia.after(dtIni))
				decorrido += (int) (dtIniBlocoPendencia.getTime() - dtIni
						.getTime()) / 1000;

			dtIni = dtFimBlocoPendencia;

			// ----------I---------PPFP---------
			if (dtFimBlocoPendencia.after(dtFim))
				return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
		}
		decorrido += (int) (dtFim.getTime() - dtIni.getTime()) / 1000;
		return new SrValor(decorrido, CpUnidadeMedida.SEGUNDO);
	}
	
	public SrValor getTempoDecorridoCadastramento(){
		Date dtInicioAtendimento = getDtInicioAtendimento();
		if (dtInicioAtendimento == null)
			dtInicioAtendimento = new Date();
		return getTempoDecorrido(getDtInicioPrimeiraEdicao(), dtInicioAtendimento);
	}
	
	public SrValor getTempoDecorridoAtendimento(){
		Date dtFechamento = isFechado() ? getDtEfetivoFechamento() : new Date();
		return getTempoDecorrido(getDtInicioAtendimento(), dtFechamento);
	}
	
	public Integer getResultadoPesquisaSatisfacao(){
		return 0;
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
		if (acordo.getAtributoAcordoSet() == null)
			return true;
		for (SrAtributoAcordo pa : acordo.getAtributoAcordoSet()) {
			if (!isAtributoAcordoSatisfeito(pa))
				return false;
		}
		return true;
	}
	
	public boolean isAtributoAcordoSatisfeito(SrAtributoAcordo atributoAcordo) {
		try {
			SrValor valor = (SrValor) SrSolicitacao.class.getMethod(
					atributoAcordo.getAtributo().asGetter()).invoke(this);
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
	 * No caso de solicitacoes filhas, deve ser considerado o solicitante e o cadastrante para fins de exibicao 
	 * de itens de configuracao e acoes disponiveis, alem do atendente designado da solicitacao.
	 */
	private List<SrConfiguracao> getFiltrosParaConsultarConfiguracoes() {
		
		List<SrConfiguracao> pessoasAConsiderar = new ArrayList<SrConfiguracao>();
		
		if (cadastrante == null)
			return pessoasAConsiderar;
		
		SrConfiguracao confCadastrante = new SrConfiguracao();
		confCadastrante.setDpPessoa(cadastrante);
		confCadastrante.setLotacao(lotaCadastrante);
		pessoasAConsiderar.add(confCadastrante);
		
		if (solicitante != null && !cadastrante.equivale(solicitante)){
			SrConfiguracao confSolicitante = new SrConfiguracao();
			confSolicitante.setDpPessoa(solicitante);
			confSolicitante.setLotacao(lotaSolicitante);
			pessoasAConsiderar.add(confSolicitante);
		}
		
		for (SrConfiguracao conf : pessoasAConsiderar){
			conf.setComplexo(local);
			conf.setBuscarPorPerfis(true);
		}
		
		return pessoasAConsiderar;

	}
	
	public SrItemConfiguracao getItemAposEscalonar() {
		SrMovimentacao movEscalonar = getUltimaMovimentacaoPorTipo(
										SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO);		
		if(movEscalonar != null)
			return movEscalonar.getItemConfiguracao();
		return null;
	}

	public SrAcao getAcaoAposEscalonar() {
		SrMovimentacao movEscalonar = getUltimaMovimentacaoPorTipo(
										SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO);		
		if(movEscalonar != null)
			return movEscalonar.getAcao();
		return null;
	}
}
