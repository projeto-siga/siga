package models;

import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRAZO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.io.File;
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
import java.util.LinkedHashSet;
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
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import notifiers.Correio;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.db.jpa.JPA;
import play.mvc.Router;
import util.SigaPlayCalendar;
import util.Util;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
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
	@JoinColumn(name = "ID_ORGAO_USU")
	public CpOrgaoUsuario orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	public SrSolicitacao solicitacaoPai;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_JUNTADA")
	public SrSolicitacao solicitacaoJuntada;

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

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	protected List<SrAtributo> meuAtributoSet;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", fetch = FetchType.LAZY)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@OneToMany(mappedBy = "solicitacaoPai", fetch = FetchType.LAZY)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;

	@OneToMany(mappedBy = "solicitacaoJuntada")
	protected Set<SrSolicitacao> meuSolicitacaoJuntadasSet;

	// Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
	// entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
	@OneToMany(mappedBy = "solicitacao", fetch = FetchType.LAZY)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

	@Column(name = "FG_RASCUNHO")
	@Type(type = "yes_no")
	public Boolean rascunho;

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

	public Boolean isRascunho() {
		return rascunho != null && rascunho;
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
						+ ")?-?(SR{1})-?(2{1}[0-9]{3})?/?([0-9]{1,5})?([.]{1})?([0-9]{1,2})?$");
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
		if (descrSolicitacao.length() > 40)
			return descrSolicitacao.substring(0, 39) + "...";
		return descrSolicitacao;
	}

	public String getDescrItem() {
		return itemConfiguracao != null ? itemConfiguracao.tituloItemConfiguracao
				: "Item não informado";
	}

	public String getDescrAcao() {
		return acao != null ? acao.tituloAcao : "Ação não informada";
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
			codigo = "TMPSR-" + (solicitacaoInicial != null ? solicitacaoInicial.idSolicitacao
					: idSolicitacao);
			return;
		}

		if (solicitacaoPai != null) {
			codigo = solicitacaoPai.getCodigo() + "." + getNumSequenciaString();
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

	public String getDtRegString() {
		SigaPlayCalendar cal = new SigaPlayCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString(false);
	}

	public String getAtributosString() {
		String s = "";
		for (SrAtributo att : getAtributoSet()) {
			if (att.valorAtributo != null)
				s += att.tipoAtributo.nomeTipoAtributo + ": "
						+ att.valorAtributo + ". ";
		}
		return s;
	}

	// Edson: NecessÃ¡rio porque nÃ£o hÃ¡ binder para arquivo
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
		return (numSequencia < 10 ? "0" : "") + numSequencia.toString();
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

	public List<SrAtributo> getAtributoSet() {
		return meuAtributoSet != null ? meuAtributoSet
				: new ArrayList<SrAtributo>();
	}

	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false, null);
	}

	public Set<SrMovimentacao> getMovimentacaoSetPorTipo(Long tipoMov) {
		return getMovimentacaoSet(false, tipoMov);
	}

	public Set<SrMovimentacao> getMovimentacaoSetComCancelados() {
		return getMovimentacaoSet(true, null);
	}

	public Set<SrMovimentacao> getMovimentacaoSetOrdemCrescente() {
		return getMovimentacaoSet(false, null, true);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados,
			Long tipoMov) {
		return getMovimentacaoSet(considerarCancelados, tipoMov, false);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados,
			Long tipoMov, boolean ordemCrescente) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new SrMovimentacaoComparator(ordemCrescente));
		if (solicitacaoInicial != null)
			for (SrSolicitacao sol : getHistoricoSolicitacao())
				if (sol.meuMovimentacaoSet != null)
					for (SrMovimentacao movimentacao : sol.meuMovimentacaoSet)
						if ((!movimentacao.isCanceladoOuCancelador() || considerarCancelados)
								&& (tipoMov == null || movimentacao.tipoMov.idTipoMov == tipoMov))
							listaCompleta.add(movimentacao);
		return listaCompleta;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoMesmoSeCancelada() {
		for (SrMovimentacao movimentacao : getMovimentacaoSetComCancelados())
			return movimentacao;
		return null;
	}

	public boolean jaFoiDesignada() {
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;
		}
		return false;
	}

	public SrMovimentacao getUltimoAndamento() {
		return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_ANDAMENTO);
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
		for (SrMovimentacao movimentacao : getMovimentacaoSet(false, idTpMov))
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoCancelavel() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.numSequencia > 1
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_AVALIACAO
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE)
				return mov;
		}
		return null;
	}

	public Set<SrMovimentacao> getMovimentacoesPrincipais() {
		Set<SrMovimentacao> set = new LinkedHashSet<SrMovimentacao>();
		for (SrMovimentacao m : getMovimentacaoSet()) {
			if (m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ANDAMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL 
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_AVALIACAO)
				set.add(m);
		}
		return set;
	}

	public DpLotacao getLotaAtendente() {
		return getUltimaMovimentacao().lotaAtendente;
	}

	public DpPessoa getAtendente() {
		return getUltimaMovimentacao().atendente;
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
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE;
		
		SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
		if (conf != null)
			return conf.preAtendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		
		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE;

		SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
		if (conf != null)
			return conf.atendente.getLotacaoAtual();
		return null;
	}

	public SrPesquisa getPesquisaDesignada() throws Exception {
		if (solicitante == null)
			return null;
		
		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_PESQUISA_SATISFACAO;
		
		SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
		if (conf != null)
			return conf.pesquisaSatisfacao;
		return null;
	}

	public Set<SrMovimentacao> getPendencias() {
		Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		Set<SrMovimentacao> setPendentes = new HashSet<SrMovimentacao>();

		for (SrMovimentacao ini : setIni) {
			if ((ini.movFinalizadora == null || ini.movFinalizadora.isCancelada()) && 
					(ini.dtAgenda == null || ini.dtAgenda.after(new Date())))
				setPendentes.add(ini);
		}

		return setPendentes;
	}

	// Edson: ver comentario abaixo, em getTiposAtributoAssociados()
	public HashMap<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados() throws Exception {
		HashMap<Long, Boolean> map = new HashMap<Long, Boolean>();
		getTiposAtributoAssociados(map);
		return map;
	}

	public List<SrTipoAtributo> getTiposAtributoAssociados() throws Exception {
		return getTiposAtributoAssociados(null);
	}

	// Edson: isso esta esquisito. A funcao esta praticamente com dois retornos.
	// Talvez ficasse melhor se o SrAtributo ja tivesse a informacao sobre
	// a obrigatoriedade dele
	private List<SrTipoAtributo> getTiposAtributoAssociados(HashMap<Long, Boolean> map) throws Exception {
		List<SrTipoAtributo> listaFinal = new ArrayList<SrTipoAtributo>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		
		for (SrTipoAtributo t : SrTipoAtributo.listar()){
			confFiltro.tipoAtributo = t;
			SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
			if (conf != null){
				listaFinal.add(t);
				if (map != null)
					map.put(t.idTipoAtributo, conf.atributoObrigatorio);
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
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE;
		
		SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
		if (conf != null)
			return conf.posAtendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getEquipeQualidadeDesignada() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.acaoFiltro = acao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_EQUIPE_QUALIDADE;
		
		SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
		if (conf != null)
			return conf.equipeQualidade.getLotacaoAtual();
		return null;
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
	public void setAtributoMap(HashMap<Long, String> atributos) {
		meuAtributoSet = new ArrayList<SrAtributo>();
		for (Long idTipoAtt : atributos.keySet()) {
			SrTipoAtributo tipoAtt = SrTipoAtributo.findById(idTipoAtt);
			SrAtributo att = new SrAtributo(tipoAtt, atributos.get(idTipoAtt),
					this);
			meuAtributoSet.add(att);
		}
	}

	public HashMap<Long, String> getAtributoMap() {
		HashMap<Long, String> map = new HashMap<Long, String>();
		if (meuAtributoSet != null)
			for (SrAtributo att : meuAtributoSet) {
				map.put(att.tipoAtributo.idTipoAtributo, att.valorAtributo);
			}
		return map;
	}

	private Set<SrSolicitacao> getSolicitacaoFilhaSet() {
		if (getHisIdIni() == null)
			return null;
		TreeSet<SrSolicitacao> listaCompleta = new TreeSet<SrSolicitacao>(
				new Comparator<SrSolicitacao>() {
					@Override
					public int compare(SrSolicitacao s1, SrSolicitacao s2) {
						return s1.numSequencia.compareTo(s2.numSequencia);
					}
				});
		for (SrSolicitacao sol : getHistoricoSolicitacao()) {
			if (sol.meuSolicitacaoFilhaSet != null)
				for (SrSolicitacao filha : sol.meuSolicitacaoFilhaSet)
					if (filha.getHisDtFim() == null)
						listaCompleta.add(filha);
		}
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

	private Long getTempoCadastramento() {
		if (dtIniEdicao == null)
			return 0L;

		Duration duration = new Duration(dtIniEdicao.getTime(), getHisDtIni()
				.getTime());
		return duration.getMillis();
	}

	@SuppressWarnings("unchecked")
	public Long getTempoCadastramentoTotal() {
		String stringQuery = "from SrSolicitacao where solicitacaoInicial.idSolicitacao = "
				+ getIdInicial();
		List<SrSolicitacao> list = JPA.em().createQuery(stringQuery)
				.getResultList();
		Duration duration = new Duration(0L);
		for (SrSolicitacao sol : list) {
			duration = duration.plus(sol.getTempoCadastramento());
		}
		if (duration == null)
			return 0L;

		return duration.getMillis();
	}

	public boolean isJuntada() {
		return sofreuMov(TIPO_MOVIMENTACAO_JUNTADA);
	}

	public boolean isEditado() {
		return !idSolicitacao.equals(getHisIdIni());
	}

	public boolean isCancelado() {
		return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) != null;
	}

	public boolean isFechadoParcialmente() {
		return sofreuMov(TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO, TIPO_MOVIMENTACAO_AVALIACAO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEmControleQualidade() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE,
				TIPO_MOVIMENTACAO_FECHAMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isFechado() {
		return sofreuMov(TIPO_MOVIMENTACAO_FECHAMENTO,
				TIPO_MOVIMENTACAO_REABERTURA) && !isCancelado();
	}

	public boolean isPendente() {
		Set<SrMovimentacao> setIni = getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		for (SrMovimentacao ini : setIni) {
			if ((ini.movFinalizadora == null || ini.movFinalizadora.isCancelada()) 
					&& (ini.dtAgenda == null || ini.dtAgenda.after(new Date())))
				return true;
		}
		return false;
	}

	public boolean isEmPosAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEmPreAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
	}

	public boolean isEmAtendimento() {
		long idsTpsMovs[] = { TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_REABERTURA };
		return sofreuMov(idsTpsMovs, TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isJuntada();
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

	public boolean temPreAtendenteDesignado() throws Exception {
		return (getPreAtendenteDesignado() != null);
	}

	public boolean temPesquisaSatisfacao() throws Exception {
		return getPesquisaDesignada() != null;
	}

	public boolean temEquipeQualidadeDesignada() throws Exception {
		return (getEquipeQualidadeDesignada() != null);
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
		return (ultMov.atendente != null && pess != null && ultMov.atendente.equivale(pess)) 
					|| (ultMov.lotaAtendente != null && ultMov.lotaAtendente.equivale(lota))
					|| (ultMovDoPai != null && ((ultMovDoPai.atendente != null && ultMovDoPai.atendente.equivale(pess))
												|| (ultMovDoPai.lotaAtendente != null && ultMovDoPai.lotaAtendente.equivale(lota))));
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

	public boolean podeVincular(DpLotacao lotaTitular, DpPessoa titular) {
		return !isRascunho();
	}

	public boolean podeDesfazerMovimentacao(DpLotacao lota, DpPessoa pess) {
		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		if (ultCancelavel == null || ultCancelavel.cadastrante == null)
			return false;
		return ultCancelavel.lotaCadastrante.equivale(lota);
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return (estaCom(lota, pess) || isEmListaPertencenteA(lota))
				&& (isEmPreAtendimento() || isEmAtendimento() || isRascunho());
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
						|| isEmPosAtendimento() || isEmControleQualidade());
	}

	public boolean podeExcluir(DpLotacao lota, DpPessoa pess) {
		return foiCadastradaPor(lota, pess) && isRascunho();
	}

	public boolean podeRetornarAoAtendimento(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmControleQualidade();
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
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	public SrSolicitacao deduzirLocalRamalEMeioContato() {

		if (solicitante == null)
			return this;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		// Tenta buscar a ï¿½ltima aberta pelo solicitante
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

		// Tenta buscar a ï¿½ltima aberta pela lotaï¿½ï¿½o dele
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
		List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.setCpTipoConfiguracao(JPA.em().find(
				CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE;

		for (SrItemConfiguracao i : SrItemConfiguracao.listar(false)) {
			if (!i.isEspecifico())
				continue;
			confFiltro.itemConfiguracaoFiltro = i;
			if (SrConfiguracao.buscar(confFiltro,
					new int[] { SrConfiguracaoBL.ACAO }) != null){
				listaFinal.add(i);
				SrItemConfiguracao itemPai = i.pai;
				while (itemPai != null) {
					if (!listaFinal.contains(itemPai))
						listaFinal.add(itemPai);
					itemPai = itemPai.pai;
				}
			}
		}

		Collections.sort(listaFinal, new SrItemConfiguracaoComparator());

		return listaFinal;
	}

	public List<SrAcao> getAcoesDisponiveis() throws Exception {
		return new ArrayList<SrAcao>(getAcoesDisponiveisComAtendente().keySet());
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendente()
			throws Exception {
		Map<SrAcao, DpLotacao> listaFinal = new HashMap<SrAcao, DpLotacao>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setDpPessoa(solicitante);
		confFiltro.setComplexo(local);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;
		confFiltro.setCpTipoConfiguracao(JPA.em().find(
				CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		confFiltro.subTipoConfig = SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE;

		for (SrAcao a : SrAcao.listar(false)) {
			if (!a.isEspecifico())
				continue;
			confFiltro.acaoFiltro = a;
			SrConfiguracao conf = SrConfiguracao.buscar(confFiltro);
			if (conf != null)
				listaFinal.put(a, conf.atendente);
		}

		return listaFinal;
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendenteOrdemSigla()
			throws Exception {
		Map<SrAcao, DpLotacao> m = new TreeMap<SrAcao, DpLotacao>(
				new Comparator<SrAcao>() {
					@Override
					public int compare(SrAcao o1, SrAcao o2) {
						if (o1 != null && o2 != null && o1.idAcao == o2.idAcao)
							return 0;
						return o1.siglaAcao.compareTo(o2.siglaAcao);
					}
				});

		m.putAll(getAcoesDisponiveisComAtendente());
		return m;
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendenteOrdemTitulo()
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

		m.putAll(getAcoesDisponiveisComAtendente());
		return m;
	}

	@SuppressWarnings("serial")
	public SortedSet<SrOperacao> operacoes(final DpLotacao lotaTitular,
			final DpPessoa titular, final boolean vendoHistoricoCompleto)
					throws Exception {

		SortedSet<SrOperacao> operacoes = new TreeSet<SrOperacao>() {
			@Override
			public boolean add(SrOperacao e) {
				// Edson: serÃ¡ que essas coisas poderiam estar dentro do
				// SrOperacao?
				if (e.params == null)
					e.params = new HashMap<String, Object>();
				e.params.put("id", idSolicitacao);
				if (vendoHistoricoCompleto && e.params.get("completo") == null)
					e.params.put("completo", true);
				if (!e.isModal())
					e.url = Router.reverse(e.url, e.params).url;
				if (!e.pode)
					return false;
				return super.add(e);
			}
		};

		operacoes.add(new SrOperacao("pencil", "Editar", podeEditar(
				lotaTitular, titular), "Application.editar"));

		/*operacoes.add(new SrOperacao("table_relationship", "Vincular",
				podeVincular(lotaTitular, titular),
				"Application.vincularSolicitacoes", "popup=true"));*/

		operacoes.add(new SrOperacao("arrow_divide",
				"Escalonar", podeEscalonar(lotaTitular,
						titular), "Application.escalonar"));

		/*operacoes.add(new SrOperacao("arrow_join", "Juntar", podeJuntar(
				lotaTitular, titular), "Application.juntarSolicitacoes",
				"popup=true"));*/

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

		/*operacoes.add(new SrOperacao("clock_go", "Alterar Prazo",
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
				+ (ultCancelavel != null ? ultCancelavel.tipoMov.nome : ""),
				podeDesfazerMovimentacao(lotaTitular, titular),
				"Application.desfazerUltimaMovimentacao"));

		operacoes
		.add(new SrOperacao("eye", "Ver Todas as Movimentações",
				!vendoHistoricoCompleto, "Application.exibir",
				"completo=true"));

		operacoes
		.add(new SrOperacao("eye", "Ver Apenas Andamentos",
				vendoHistoricoCompleto, "Application.exibir",
				"completo=false"));

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
		//Edson: Ver por que isto está sendo necessário. Sem isso, após o salvar(),
		//ocorre LazyIniException ao tentar acessar esses meuMovimentacaoSet's
		if (solicitacaoInicial != null)
			for (SrSolicitacao s : solicitacaoInicial.meuSolicitacaoHistoricoSet){
				for (SrMovimentacao m : s.meuMovimentacaoSet){}
			}

		super.salvar();
		
		//Edson: melhorar isto, pra nao precisar salvar novamente
		if (isRascunho()) {
			atualizarCodigo();
			save();
		}

		if (!isRascunho() && !jaFoiDesignada()) {
			if (fecharAoAbrir)
				fechar(lotaCadastrante, cadastrante, motivoFechamentoAbertura);
			else if (temPreAtendenteDesignado())
				iniciarPreAtendimento(lotaCadastrante, cadastrante);
			else
				iniciarAtendimento(lotaCadastrante, cadastrante);

			for (SrLista lista : getListasParaInclusaoAutomatica(lotaCadastrante)) {
				incluirEmLista(lista, cadastrante, lotaCadastrante);
			}

			if (!isEditado()
					&& formaAcompanhamento != SrFormaAcompanhamento.NUNCA
					&& formaAcompanhamento != SrFormaAcompanhamento.FECHAMENTO)
				Correio.notificarAbertura(this);
		} else
			atualizarMarcas();
	}
	
	public void excluir() throws Exception{
		finalizar();
		for (SrMarca e : getMarcaSet()) {
			e.solicitacao.meuMarcaSet.remove(e);
			e.delete();
		}
	}

	private void checarEPreencherCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante nÃ£o pode ser nulo");

		if (dtReg == null)
			dtReg = new Date();

		if (arquivo != null) {
			double lenght = (double) arquivo.blob.length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) ï¿½ maior que o mï¿½ximo permitido (2MB)");
		}

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();

		if (solicitante == null)
			solicitante = cadastrante;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		if (solicitante.equivale(cadastrante)){
			dtOrigem = null;
			meioComunicacao = null;
		}

		if (orgaoUsuario == null)
			orgaoUsuario = lotaSolicitante.getOrgaoUsuario();

		if (numSolicitacao == null)
			if (!isRascunho())
				numSolicitacao = getProximoNumero();
				atualizarCodigo();

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
					"NÃ£o foi encontrado nenhum atendente designado "
							+ "para esta solicitação. SugestÃ£o: alterar item de "
							+ "configuração e/ou ação");
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		if (!podeDesfazerMovimentacao(lotaCadastrante, cadastrante))
			throw new Exception("Operação nÃ£o permitida");

		SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();

		// tratamento pois pode ter retorno nulo do mÃ©todo
		// getUltimaMovimentacaoCancelave()
		if (movimentacao != null) {

			if (movimentacao.tipoMov != null) {
				// caso seja movimentacao cancelada ou fechada, reinsere nas
				// listas de prioridade
				if (movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO
						|| movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO)
					reInserirListasDePrioridade(lotaCadastrante, cadastrante);

				if (movimentacao.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_JUNTADA) {
					Query query = JPA
							.em()
							.createQuery(
									"UPDATE SrSolicitacao sol SET sol.solicitacaoJuntada = :solRecebeJuntada WHERE sol.idSolicitacao = :idSol");
					query.setParameter("solRecebeJuntada", null);
					query.setParameter("idSol", this.idSolicitacao);
					query.executeUpdate();
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
		for (SrSolicitacao s : getSolicitacaoFilhaSet())
			filha.numSequencia = s.numSequencia;
		if (filha.numSequencia == null)
			filha.numSequencia = 1L;
		else
			filha.numSequencia++;
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
		encaixar(setA, setB, marcasAIncluir, marcasAExcluir);

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
	}

	private SortedSet<SrMarca> calcularMarcadores() {
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
				Long t = mov.tipoMov.idTipoMov;
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
				if (t == TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO_PARCIAL;
					movMarca = mov;
				}
				if (t == TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE) {
					marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_CONTROLE_QUALIDADE;
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
				if (t == TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
					pendencias++;
					if (mov.dtAgenda != null && (dtFimPendenciaMaisLonge == null || mov.dtAgenda.after(dtFimPendenciaMaisLonge)))
						dtFimPendenciaMaisLonge = mov.dtAgenda;
					movPendencia = mov;
				}
				if (t == TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
					pendencias--;
				}
				if (t == TIPO_MOVIMENTACAO_ANDAMENTO) {
					movMarca = mov;
				}
			}

			if (marcador != 0L) 
				acrescentarMarca(set, marcador, movMarca.dtIniMov, null,
						movMarca.atendente, movMarca.lotaAtendente);

			if (pendencias > 0){
				if (isRascunho())
					acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, movPendencia.dtIniMov, dtFimPendenciaMaisLonge,
							cadastrante, lotaCadastrante);
				else acrescentarMarca(set, CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, movPendencia.dtIniMov, dtFimPendenciaMaisLonge,
						movPendencia.atendente, movPendencia.lotaAtendente);
			}

			if (!isFechado() && !isCancelado()) {
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null,
						null, cadastrante, lotaCadastrante);
				acrescentarMarca(set,
						CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE, null,
						null, solicitante, lotaSolicitante);
			}
		}

		return set;
	}

	private void encaixar(SortedSet<SrMarca> setA, SortedSet<SrMarca> setB,
			Set<SrMarca> incluir, Set<SrMarca> excluir) {
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

				// O registro existe nos dois - atualizar.add(new Par(b, a));
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
 * 		Edson:DB1, acertar conforme o item 24
 * 
		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(
				solicitante, local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE, new int[] {});
		for (SrConfiguracao conf : confs) {
			for (SrLista lista : conf.getListaConfiguracaoSet()) {
				SrLista listaAtual = lista.getListaAtual();
				if (!listaFinal.contains(listaAtual))
					listaFinal.add(listaAtual);
			}
		}
*/
		return new ArrayList<SrLista>(listaFinal);
	}

	public List<SrLista> getListasDisponiveisParaInclusao(
			DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setLotacao(lotaTitular);
		confFiltro.setDpPessoa(cadastrante);
		confFiltro.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class, 
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
		
		for (SrLista l : SrLista.listar(false)){
			confFiltro.listaPrioridade = l;
			if (SrConfiguracao.buscar(confFiltro) != null)
				listaFinal.add(l);
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

	public boolean temSolicitacaoVinculada() {
		return getMovimentacaoSet(Boolean.TRUE, TIPO_MOVIMENTACAO_VINCULACAO)
				.size() > 0;
	}

	public Set<SrSolicitacao> getSolicitacaoVinculadaSet() {
		Set<SrSolicitacao> vinculadas = new HashSet<SrSolicitacao>();
		for (SrMovimentacao mov : getMovimentacaoSet(Boolean.TRUE,
				TIPO_MOVIMENTACAO_VINCULACAO))
			if (mov.vinculo.solicitacaoA.solicitacaoInicial.idSolicitacao == this.solicitacaoInicial.idSolicitacao)
				vinculadas.add(mov.vinculo.solicitacaoB);
			else if (mov.vinculo.solicitacaoB.solicitacaoInicial.idSolicitacao == this.solicitacaoInicial.idSolicitacao)
				vinculadas.add(mov.vinculo.solicitacaoA);

		return vinculadas;
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
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.lista != null && mov.lista.equivale(lista))
				return mov.prioridade != null ? mov.prioridade : -1;
		}
		return -1;
	}

	public void incluirEmLista(SrLista lista, DpPessoa pess, DpLotacao lota)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		if (isEmLista(lista))
			throw new IllegalArgumentException("Lista " + lista.nomeLista
					+ " já contém a solicitação " + getCodigo());

		SrMovimentacao mov = new SrMovimentacao();
		mov.prioridade = (long) lista.getProximaPosicao();
		mov.cadastrante = pess;
		mov.lotaCadastrante = lota;
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INCLUSAO_LISTA);
		mov.descrMovimentacao = "InclusÃ£o na lista " + lista.nomeLista
				+ " com a prioridade " + mov.prioridade;
		mov.lista = lista;
		mov.solicitacao = this;
		mov.salvar();
		lista.refresh();
	}

	public void retirarDeLista(SrLista lista, DpPessoa pess, DpLotacao lota)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = pess;
		mov.lotaCadastrante = lota;
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA);
		mov.descrMovimentacao = "Cancelamento de InclusÃ£o em Lista";
		mov.solicitacao = this;
		mov.lista = lista;
		mov.salvar();
		lista.refresh();

		lista.recalcularPrioridade(pess, lota);
	}

	// Edson: este método é protected porque não pode ser chamado pelo
	// usuário,
	// mas sim pela SrLista, passando a posição correta a ser colocada a
	// solicitação
	protected void priorizar(SrLista lista, long prioridade, DpPessoa pess,
			DpLotacao lota) throws Exception {
		SrMovimentacao mov = new SrMovimentacao();
		mov.prioridade = prioridade;
		mov.cadastrante = pess;
		mov.lotaCadastrante = lota;
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA);
		mov.descrMovimentacao = "Alteração de prioridade na lista "
				+ lista.nomeLista + ": " + mov.prioridade;
		mov.lista = lista;
		mov.solicitacao = this;
		mov.salvar();
		lista.refresh();
	}

	private void iniciarPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.lotaAtendente = getPreAtendenteDesignado();
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO);
		mov.salvar(pess, lota);
	}

	public void finalizarPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeFinalizarPreAtendimento(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
		iniciarAtendimento(lota, pess);
	}

	public void retornarAoPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeRetornarAoPreAtendimento(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
		iniciarPreAtendimento(lota, pess);
	}

	private void iniciarAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO);
		mov.lotaAtendente = mov.solicitacao.getAtendenteDesignado();
		mov.salvar(pess, lota);
	}

	private void iniciarPosAtendimento(DpLotacao lota, DpPessoa pess,
			String motivo) throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO);
		mov.lotaAtendente = mov.solicitacao.getPosAtendenteDesignado();
		mov.descrMovimentacao = motivo;
		mov.salvar(pess, lota);
	}

	public void fechar(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {

		if ((pess != null) && !podeFechar(lota, pess))
			throw new Exception("Operação nÃ£o permitida");

		long estadoAtual = 0L;
		// Edson: A variÃ¡vel estadoAtual e o bloco abaixo, que a define, nÃ£o
		// seriam necessÃ¡rios se tivÃ©ssemos a classe SrEstado.
		if (isEmPreAtendimento())
			estadoAtual = TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO;
		if (isEmAtendimento())
			estadoAtual = TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO;
		else if (isEmPosAtendimento())
			estadoAtual = TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO;
		else if (isFechadoParcialmente())
			estadoAtual = TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL;
		else if (isEmControleQualidade())
			estadoAtual = TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE;

		if (estadoAtual == TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO
				|| estadoAtual == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
			if (temPosAtendenteDesignado()) {
				iniciarPosAtendimento(lota, pess, motivo);
				return;
			} else
				estadoAtual = TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO;

		if (estadoAtual == TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO) {
			if (temPesquisaSatisfacao()) {
				//fecharParcialmente(lota, pess, motivo);
				enviarPesquisa();
			} 
			estadoAtual = TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL;
		}

		if (estadoAtual == TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL
				|| estadoAtual == TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE)
			fecharTotalmente(lota, pess, motivo);
	}

	public void enviarPesquisa() throws Exception {
		// Implementar
		Correio.pesquisaSatisfacao(this);
	}

	private void fecharParcialmente(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL);
		mov.descrMovimentacao = motivo;
		mov.salvar(pess, lota);
	}

	public void responderPesquisa(DpLotacao lota, DpPessoa pess,
			Map<Long, String> respostaMap) throws Exception {
		if (!podeResponderPesquisa(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.pesquisa = this.getPesquisaDesignada();
		movimentacao.descrMovimentacao = "Avaliação realizada.";
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_AVALIACAO);
		movimentacao.setRespostaMap(respostaMap);
		movimentacao.salvar(pess, lota);
		
		// if (avaliacao.isSuficiente)...
		// fecharTotalmente()
		// else
		//if (getEquipeQualidadeDesignada() != null)
		//	iniciarControleQualidade(lota, pess);
		//else
		//	fecharTotalmente(null, null, "Fechado.");
	}

	private void iniciarControleQualidade(DpLotacao lota, DpPessoa pess)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE);
		mov.lotaAtendente = mov.solicitacao.getEquipeQualidadeDesignada();
		mov.salvar(pess, lota);
	}

	public void retornarAoAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeRetornarAoAtendimento(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
		iniciarAtendimento(lota, pess);
	}

	private void fecharTotalmente(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_FECHAMENTO);
		mov.descrMovimentacao = motivo;
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
			throw new Exception("Operação nÃ£o permitida");
		reInserirListasDePrioridade(lota, pess);

		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA);
		mov.lotaAtendente = getUltimoAtendenteEtapaAtendimento();
		mov.salvar(pess, lota);
	}

	private void reInserirListasDePrioridade(DpLotacao lota, DpPessoa pess)
			throws Exception {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO
					|| mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO)
				break;
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_LISTA)
				incluirEmLista(mov.lista, pess, lota);
		}
	}

	public void deixarPendente(DpLotacao lota, DpPessoa pess,
			SrTipoMotivoPendencia motivo, String calendario, String horario,
			String detalheMotivo) throws Exception {
		if (!podeDeixarPendente(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
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
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		movimentacao.descrMovimentacao = detalheMotivo;
		movimentacao.motivoPendencia = motivo;
		movimentacao.salvar(pess, lota);
	}

	public void alterarPrazo(DpLotacao lota, DpPessoa pess, String motivo,
			String calendario, String horario) throws Exception {
		if (!podeAlterarPrazo(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
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
		movimentacao.descrMovimentacao = motivo;
		movimentacao.salvar(pess, lota);
	}

	public void terminarPendencia(DpLotacao lota, DpPessoa pess, String descricao, Long idMovimentacao)
			throws Exception {
		if (!podeTerminarPendencia(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA);
		movimentacao.descrMovimentacao = descricao;
		movimentacao = movimentacao.salvar(pess, lota);
		
		SrMovimentacao movFinalizada = SrMovimentacao.findById(idMovimentacao);
		movFinalizada.movFinalizadora = movimentacao;
		movFinalizada.save();
	}

	public void cancelar(DpLotacao lota, DpPessoa pess) throws Exception {
		if (!podeCancelar(lota, pess))
			throw new Exception("Operação nÃ£o permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO);
		movimentacao.salvar(pess, lota);

		// remove das listas apos finalizar, para que seja possivel reabrir
		// depois
		removerDasListasDePrioridade(lota, pess);
	}

	public void juntar(DpLotacao lota, DpPessoa pess,
			SrSolicitacao solRecebeJuntada, String justificativa)
			throws Exception {
		if ((pess != null) && !podeJuntar(lota, pess))
			throw new Exception("Operação nÃ£o permitida");

		this.solicitacaoJuntada = solRecebeJuntada;
		Query query = JPA
				.em()
				.createQuery(
						"UPDATE SrSolicitacao sol SET sol.solicitacaoJuntada = :solRecebeJuntada WHERE sol.idSolicitacao = :idSol");
		query.setParameter("solRecebeJuntada", solRecebeJuntada);
		query.setParameter("idSol", this.idSolicitacao);
		query.executeUpdate();

		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_JUNTADA);
		movimentacao.descrMovimentacao = justificativa;
		movimentacao.salvar(pess, lota);

		movimentacao = new SrMovimentacao(solRecebeJuntada);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBE_JUNCAO_SOLICITACAO);
		movimentacao.descrMovimentacao = justificativa;
		movimentacao.salvar(pess, lota);

	}

	@SuppressWarnings("unchecked")
	public void vincular(DpLotacao lota, DpPessoa pess,
			SrSolicitacao solRecebeVinculo, String justificativa)
			throws Exception {
		if ((pess != null) && !podeVincular(lota, pess))
			throw new Exception("Operação nÃ£o permitida");

		String query = "from SrSolicitacaoVinculo where ( solicitacaoA.idSolicitacao = "
				+ this.idSolicitacao
				+ " and solicitacaoB.idSolicitacao = "
				+ solRecebeVinculo.idSolicitacao
				+ " ) "
				+ " or ( solicitacaoA.idSolicitacao = "
				+ solRecebeVinculo.idSolicitacao
				+ " and solicitacaoB.idSolicitacao = "
				+ this.idSolicitacao
				+ " ) ";

		List<SrSolicitacaoVinculo> list = (List<SrSolicitacaoVinculo>) JPA.em()
				.createQuery(query).getResultList();

		if (list.size() > 0)
			throw new Exception("Vinculo jÃ¡ existente.");

		SrSolicitacaoVinculo vinculo = new SrSolicitacaoVinculo(this,
				solRecebeVinculo);
		vinculo.salvar();

		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_VINCULACAO);
		movimentacao.vinculo = vinculo;
		movimentacao.descrMovimentacao = justificativa;
		movimentacao.salvar(pess, lota);

		SrMovimentacao mov = new SrMovimentacao(solRecebeVinculo);
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_VINCULACAO);
		mov.vinculo = vinculo;
		mov.descrMovimentacao = justificativa;
		mov.salvar(pess, lota);
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
		if (dtOrigem != null){
			SigaPlayCalendar cal = new SigaPlayCalendar();
			cal.setTime(dtOrigem);
			return cal.getTempoTranscorridoString(false);
		}
		return "";
	}

	public void setDtOrigemString(String stringDtMeioContato) {
		DateTimeFormatter formatter = forPattern("dd/MM/yyyy HH:mm");
		if (stringDtMeioContato != null 
				&& !stringDtMeioContato.isEmpty()
				&& stringDtMeioContato.contains("/") 
				&& stringDtMeioContato.contains(":"))
			this.dtOrigem = new DateTime (formatter.parseDateTime(stringDtMeioContato)).toDate();
	}
}
