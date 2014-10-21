package models;

import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA;
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
import static models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.emory.mathcs.backport.java.util.Collections;
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

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SOLICITACAO_SEQ", name = "srSolicitacaoSeq")
	@GeneratedValue(generator = "srSolicitacaoSeq")
	@Column(name = "ID_SOLICITACAO")
	public Long idSolicitacao;

	@ManyToOne()
	@JoinColumn(name = "ID_SOLICITANTE")
	public DpPessoa solicitante;

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

	@Enumerated
	public SrFormaAcompanhamento formaAcompanhamento;

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

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

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

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrSolicitacao solicitacaoInicial;

	@OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrSolicitacao> meuSolicitacaoHistoricoSet;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	protected List<SrAtributo> meuAtributoSet;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;

	// Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
	// entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

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
		if (descrSolicitacao.length() > 40)
			return descrSolicitacao.substring(0, 39) + "...";
		return descrSolicitacao;
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodigo() {

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo() + "." + getNumSequenciaString();

		if (numSolicitacao == null)
			return "";

		String numString = numSolicitacao.toString();

		while (numString.length() < 5)
			numString = "0" + numString;

		return orgaoUsuario.getAcronimoOrgaoUsu() + "-SR-" + getAnoEmissao()
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

	// Edson: Necessário porque não há binder para arquivo
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

	public String getDtRegDDMMYYYYHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
		return SrSolicitacao
				.find("from SrSolicitacao where idSolicitacao = (select max(idSolicitacao) from SrSolicitacao where hisIdIni = "
						+ getHisIdIni() + ")").first();
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
		SrSolicitacao sol = solicitacaoInicial != null ? solicitacaoInicial
				: this;
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

	public SrMovimentacao getUltimoAndamento() {
		return getUltimaMovimentacaoPorTipo(TIPO_MOVIMENTACAO_ANDAMENTO);
	}

	public SrMovimentacao getUltimaMovimentacaoPorTipo(Long idTpMov) {
		for (SrMovimentacao movimentacao : getMovimentacaoSet(false, idTpMov))
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoCancelavel() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.numSequencia > 1
					&& mov.tipoMov.idTipoMov != TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA
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
			if ((m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ANDAMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL || m.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_FECHAMENTO)
					&& m.descrMovimentacao != null
					&& !m.descrMovimentacao.trim().equals(""))
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
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE);
		if (conf != null)
			return conf.preAtendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
		if (conf != null)
			return conf.atendente.getLotacaoAtual();
		return null;
	}

	public SrPesquisa getPesquisaDesignada() throws Exception {
		if (solicitante == null)
			return null;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_PESQUISA_SATISFACAO);
		if (conf != null)
			return conf.pesquisaSatisfacao;
		return null;
	}

	// Edson: ver comentario abaixo, em getTiposAtributoAssociados()
	public HashMap<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados()
			throws Exception {
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
	private List<SrTipoAtributo> getTiposAtributoAssociados(
			HashMap<Long, Boolean> map) throws Exception {
		List<SrTipoAtributo> listaFinal = new ArrayList<SrTipoAtributo>();

		for (SrConfiguracao conf : SrConfiguracao.getConfiguracoes(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO, null,
				new int[] { SrConfiguracaoBL.TIPO_ATRIBUTO })) {
			SrTipoAtributo tipo = conf.tipoAtributo.getAtual();
			if (tipo != null && !listaFinal.contains(tipo)) {
				listaFinal.add(tipo);
				if (map != null)
					map.put(tipo.idTipoAtributo, conf.atributoObrigatorio);
			}
		}
		return listaFinal;
	}

	public DpLotacao getPosAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE);
		if (conf != null)
			return conf.posAtendente.getLotacaoAtual();
		return null;
	}

	public SrPesquisa getPesquisaSatisfacaoDesignada() throws Exception {
		if (solicitante == null)
			return null;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_PESQUISA_SATISFACAO);
		if (conf != null)
			return conf.pesquisaSatisfacao;
		return null;
	}

	public DpLotacao getEquipeQualidadeDesignada() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				local, itemConfiguracao, acao,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_EQUIPE_QUALIDADE);
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
		SrSolicitacao sol = solicitacaoInicial != null ? solicitacaoInicial
				: this;
		if (sol.meuSolicitacaoFilhaSet != null)
			for (SrSolicitacao filha : sol.meuSolicitacaoFilhaSet)
				if (filha.getHisDtFim() == null)
					listaCompleta.add(filha);
		return listaCompleta;
	}

	public boolean isPai() {
		return getSolicitacaoFilhaSet() != null
				&& getSolicitacaoFilhaSet().size() > 0;
	}

	public Set<SrMarca> getMarcaSet() {
		SrSolicitacao sol = solicitacaoInicial != null ? solicitacaoInicial
				: this;
		return sol.meuMarcaSet != null ? sol.meuMarcaSet
				: new TreeSet<SrMarca>();
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
				&& !isCancelado();
	}

	public boolean isEmControleQualidade() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_CONTROLE_QUALIDADE,
				TIPO_MOVIMENTACAO_FECHAMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO)
				&& !isCancelado();
	}

	public boolean isFechado() {
		return sofreuMov(TIPO_MOVIMENTACAO_FECHAMENTO,
				TIPO_MOVIMENTACAO_REABERTURA) && !isCancelado();
	}

	public boolean isPendente() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_PENDENCIA,
				TIPO_MOVIMENTACAO_FIM_PENDENCIA) && !isCancelado();
	}

	public boolean isEmPosAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado();
	}

	public boolean isEmPreAtendimento() {
		return sofreuMov(TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado();
	}

	public boolean isEmAtendimento() {
		long idsTpsMovs[] = { TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO,
				TIPO_MOVIMENTACAO_REABERTURA };
		return sofreuMov(idsTpsMovs, TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO,
				TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO,
				TIPO_MOVIMENTACAO_FECHAMENTO_PARCIAL,
				TIPO_MOVIMENTACAO_FECHAMENTO)
				&& !isCancelado() && !isPendente();
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
		if (getPesquisaSatisfacaoDesignada() != null)
			return true;
		return false;
	}

	public boolean temPesquisaRespondida() {
		return false;
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
		return ultMov != null
				&& ((ultMov.atendente != null && pess != null && ultMov.atendente
						.equivale(pess)) || ultMov.lotaAtendente.equivale(lota));
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
		SrSolicitacao sol = solicitacaoInicial != null ? solicitacaoInicial
				: this;
		return sol.arquivo;
	}

	public Set<SrMovimentacao> getMovimentacoesAnexacao() {
		return getMovimentacaoSetPorTipo(TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO);
	}

	public boolean podeCriarFilha(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && (isEmAtendimento() || isPendente())
				&& !isFilha();
	}

	public boolean podeDesfazerMovimentacao(DpLotacao lota, DpPessoa pess) {
		SrMovimentacao ultCancelavel = getUltimaMovimentacaoCancelavel();
		if (ultCancelavel == null || ultCancelavel.cadastrante == null)
			return false;
		return ultCancelavel.lotaCadastrante.equivale(lota);
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return (estaCom(lota, pess) || isEmListaPertencenteA(lota))
				&& (isEmPreAtendimento() || isEmAtendimento());
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

	public boolean podeRetornarAoAtendimento(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmControleQualidade();
	}

	public boolean podeCancelar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess)
				&& (isEmPreAtendimento() || isEmAtendimento());
	}

	public boolean podeDeixarPendente(DpLotacao lota, DpPessoa pess) {
		return isEmAtendimento() && !isPendente() && estaCom(lota, pess);
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
		return (isEmPreAtendimento() || isEmAtendimento() || isPendente());
	}

	public boolean podeImprimirTermoAtendimento(DpLotacao lota, DpPessoa pess) {
		return isEmAtendimento() && estaCom(lota, pess);
	}

	public boolean podeAssociarLista(DpLotacao lota, DpPessoa pess) {
		return !isFechado();
	}

	public boolean podeTrocarAtendente(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmAtendimento();
	}

	public boolean podeResponderPesquisa(DpLotacao lotaTitular, DpPessoa titular) {
		return (isFechadoParcialmente() && foiSolicitadaPor(lotaTitular,
				titular));
	}

	public SrSolicitacao deduzirLocalERamal() {

		if (solicitante == null)
			return this;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		// Tenta buscar a �ltima aberta pelo solicitante
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

		// Tenta buscar a �ltima aberta pela lota��o dele
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
		Set<SrItemConfiguracao> listaFinal = new TreeSet<SrItemConfiguracao>(
				new SrItemConfiguracaoComparator());

		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(null,
				solicitante, local, null, null, null, null,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE, new int[] {
						SrConfiguracaoBL.ITEM_CONFIGURACAO,
						SrConfiguracaoBL.ACAO });

		for (SrConfiguracao conf : confs) {
			if (conf.itemConfiguracao == null) {
				listaFinal.addAll(SrItemConfiguracao.listar());
				break;
			} else {
				SrItemConfiguracao atual = conf.itemConfiguracao.getAtual();
				if (atual != null)
					listaFinal.addAll(atual.getItemETodosDescendentes());

				// Edson: a adi��o dos pais � necess�ria para formar a
				// hierarquia
				SrItemConfiguracao itemPai = atual.pai;
				while (itemPai != null) {
					if (!listaFinal.contains(itemPai))
						listaFinal.add(itemPai);
					itemPai = itemPai.pai;
				}
			}
		}
		return new ArrayList(listaFinal);
	}

	public List<SrAcao> getAcoesDisponiveis() throws Exception {
		return new ArrayList(getAcoesDisponiveisComAtendente().keySet());
	}

	public Map<SrAcao, DpLotacao> getAcoesDisponiveisComAtendente()
			throws Exception {
		Map<SrAcao, DpLotacao> listaFinal = new HashMap<SrAcao, DpLotacao>();
		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(null,
				this.solicitante, this.local, this.itemConfiguracao, null,
				null, null, CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE,
				new int[] { SrConfiguracaoBL.ACAO });
		for (SrConfiguracao conf : confs) {
			// Edson: o && !containsKey, abaixo, Ã© necessÃ¡rio pra que
			// atendentes
			// de configuraÃ§Ãµes mais genÃ©ricas nÃ£o substituam os das
			// mais
			// especÃ­ficas, que vÃªm antes
			if (conf.acao == null) {
				for (SrAcao acao : SrAcao.listar())
					if (acao.isEspecifico() && !listaFinal.containsKey(acao))
						listaFinal.put(acao, conf.atendente);
				break;
			} else {
				SrAcao atual = conf.acao.getAtual();
				if (atual != null)
					for (SrAcao acao : atual.getAcaoETodasDescendentes())
						if (acao.isEspecifico()
								&& !listaFinal.containsKey(acao))
							listaFinal.put(acao, conf.atendente);
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

	public SortedSet<SrOperacao> operacoes(final DpLotacao lotaTitular,
			final DpPessoa titular, final boolean vendoHistoricoCompleto)
			throws Exception {

		SortedSet<SrOperacao> operacoes = new TreeSet<SrOperacao>() {
			@Override
			public boolean add(SrOperacao e) {
				// Edson: será que essas coisas poderiam estar dentro do
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

		operacoes.add(new SrOperacao("arrow_divide",
				"Criar Solicitação Filha", podeCriarFilha(lotaTitular,
						titular), "Application.criarFilha"));

		operacoes.add(new SrOperacao("text_list_numbers", "Definir Lista",
				podeAssociarLista(lotaTitular, titular), "associarLista",
				"modal=true"));

		operacoes.add(new SrOperacao("lock", "Fechar", podeFechar(lotaTitular,
				titular), "fechar", "modal=true"));

		operacoes.add(new SrOperacao("lock", "Responder Pesquisa",
				podeResponderPesquisa(lotaTitular, titular),
				"Application.responderPesquisa", "popup=true"));

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

		// operacoes.add(new SrOperacao("clock_pause", "Deixar Pendente",
		// podeDeixarPendente(lotaTitular,
		// titular),"Application.deixarPendente"));

		operacoes.add(new SrOperacao("clock_pause", "Deixar Pendente",
				podeDeixarPendente(lotaTitular, titular), "deixarPendente",
				"modal=true"));

		operacoes.add(new SrOperacao("clock_play", "Terminar Pendência",
				podeTerminarPendencia(lotaTitular, titular),
				"Application.terminarPendencia"));

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

		super.salvar();

		if (getMovimentacaoSetComCancelados().size() == 0) {
			if (fecharAoAbrir)
				fechar(lotaCadastrante, cadastrante, motivoFechamentoAbertura);
			else if (temPreAtendenteDesignado())
				iniciarPreAtendimento(lotaCadastrante, cadastrante);
			else
				iniciarAtendimento(lotaCadastrante, cadastrante);
			if (!isEditado()
					&& formaAcompanhamento != SrFormaAcompanhamento.NUNCA
					&& formaAcompanhamento != SrFormaAcompanhamento.FECHAMENTO)
				Correio.notificarAbertura(this);
		} else
			atualizarMarcas();
	}

	private void checarEPreencherCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		if (arquivo != null) {
			double lenght = (double) arquivo.blob.length / 1024 / 1024;
			if (lenght > 2)
				throw new IllegalArgumentException("O tamanho do arquivo ("
						+ new DecimalFormat("#.00").format(lenght)
						+ "MB) � maior que o m�ximo permitido (2MB)");
		}

		dtReg = new Date();

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();

		if (solicitante == null)
			solicitante = cadastrante;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		if (orgaoUsuario == null)
			orgaoUsuario = lotaSolicitante.getOrgaoUsuario();

		if (numSolicitacao == null)
			numSolicitacao = getProximoNumero();

		if (gravidade == null)
			gravidade = SrGravidade.NORMAL;

		if (urgencia == null)
			urgencia = SrUrgencia.NORMAL;

		if (tendencia == null)
			tendencia = SrTendencia.PIORA_MEDIO_PRAZO;

		if (!temAtendenteDesignado() && !temPreAtendenteDesignado())
			throw new Exception(
					"Não foi encontrado nenhum atendente designado "
							+ "para esta solicitação. Sugestão: alterar item de "
							+ "configuração e/ou ação");
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		if (!podeDesfazerMovimentacao(lotaCadastrante, cadastrante))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();
		movimentacao.desfazer(cadastrante, lotaCadastrante);
	}

	public SrSolicitacao criarFilhaSemSalvar() throws Exception {
		SrSolicitacao filha = new SrSolicitacao();
		Util.copiar(filha, this);
		filha.idSolicitacao = null;
		filha.solicitacaoPai = this.solicitacaoInicial;
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
		SrSolicitacao sol = solicitacaoInicial != null ? solicitacaoInicial
				: this;
		SortedSet<SrMarca> setA = new TreeSet<SrMarca>();
		if (sol.meuMarcaSet == null)
			sol.meuMarcaSet = new TreeSet<SrMarca>();
		// Edson: Obtido do sigagc - Excluir marcas duplicadas (???)
		for (SrMarca m : sol.meuMarcaSet) {
			if (setA.contains(m))
				m.delete();
			else
				setA.add(m);
		}
		SortedSet<SrMarca> setB = sol.calcularMarcadores();
		Set<SrMarca> marcasAIncluir = new TreeSet<SrMarca>();
		Set<SrMarca> marcasAExcluir = new TreeSet<SrMarca>();
		encaixar(setA, setB, marcasAIncluir, marcasAExcluir);
		for (SrMarca i : marcasAIncluir) {
			i.save();
			sol.meuMarcaSet.add(i);
		}
		for (SrMarca e : marcasAExcluir) {
			sol.meuMarcaSet.remove(e);
			e.delete();
		}
	}

	private SortedSet<SrMarca> calcularMarcadores() {
		SortedSet<SrMarca> set = new TreeSet<SrMarca>();

		Long marcador = 0L, marcadorAnterior = 0L;
		SrMovimentacao movMarca = null, movMarcaAnterior = null;
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
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
			if (t == TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				marcadorAnterior = marcador;
				movMarcaAnterior = movMarca;
				marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
				if (mov.dtAgenda != null)
					marcador = CpMarcador.MARCADOR_SOLICITACAO_AGENDADO;
				movMarca = mov;
			}
			if (t == TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
				marcador = marcadorAnterior;
				movMarca = movMarcaAnterior;
			}
			if (t == TIPO_MOVIMENTACAO_ANDAMENTO) {
				movMarca = mov;
			}
		}

		acrescentarMarca(set, marcador, movMarca.dtIniMov, null,
				movMarca.atendente, movMarca.lotaAtendente);

		if (!isFechado() && !isCancelado()) {
			acrescentarMarca(set,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null,
					null, cadastrante, lotaCadastrante);
			acrescentarMarca(set,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE, null,
					null, solicitante, lotaSolicitante);
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

				if (a == null) {
					int i = 0;
				}
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

		if (pess != null && lota != null) {
			// Marcacoes para a propria lotacao e para a propria pessoa ou sem
			// informacao de pessoa
			for (SrMarca mar : getMarcaSet()) {
				if (marcadoresDesconsiderar.contains(mar.getCpMarcador()
						.getIdMarcador()))
					continue;
				if (((mar.getDpLotacaoIni() != null && lota.getIdInicial()
						.equals(mar.getDpLotacaoIni().getIdInicial())) || mar
						.getDpLotacaoIni() == null)
						&& (mar.getDpPessoaIni() == null || pess.getIdInicial()
								.equals(mar.getDpPessoaIni().getIdInicial()))) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(mar.getCpMarcador().getDescrMarcador());
				}
			}
			// Marcacoes para a propria lotacao e para outra pessoa
			if (sb.length() == 0) {
				for (SrMarca mar : getMarcaSet()) {
					if (marcadoresDesconsiderar.contains(mar.getCpMarcador()
							.getIdMarcador()))
						continue;
					if (sb.length() > 0)
						sb.append(", ");
					if ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
							.equals(mar.getDpLotacaoIni().getIdInicial()))
							&& (mar.getDpPessoaIni() != null && !pess
									.getIdInicial()
									.equals(mar.getDpPessoaIni().getIdInicial()))) {
						sb.append(mar.getCpMarcador().getDescrMarcador());
						sb.append(" [");
						sb.append(mar.getDpPessoaIni().getSigla());
						sb.append("]");
					}
				}
			}
		}

		// Marcacoes para qualquer outra pessoa ou lotacao
		if (sb.length() == 0) {
			for (SrMarca mar : getMarcaSet()) {
				if (marcadoresDesconsiderar.contains(mar.getCpMarcador()
						.getIdMarcador()))
					continue;
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
		// TODO Auto-generated method stub
		return false;
	}

	public List<SrLista> getListasDisponiveisParaInclusao(
			DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrLista> listaFinal = SrLista.getCriadasPelaLotacao(lotaTitular);

		List<SrConfiguracao> confs = SrConfiguracao.getConfiguracoes(
				lotaTitular, cadastrante,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA,
				new int[] { SrConfiguracaoBL.LISTA_PRIORIDADE });

		for (SrConfiguracao conf : confs) {
			SrLista listaAtual = conf.listaPrioridade.getListaAtual();
			if (!listaFinal.contains(listaAtual))
				listaFinal.add(listaAtual);
		}

		listaFinal.removeAll(getListasAssociadas());
		Collections.sort(listaFinal, new Comparator<SrLista>() {
			@Override
			public int compare(SrLista l1, SrLista l2) {
				return l1.nomeLista.compareTo(l2.nomeLista);
			}
		});
		return new ArrayList<SrLista>(listaFinal);
	}

	public Set<SrLista> getListasAssociadas() {
		Set<SrLista> associadas = new HashSet<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente())
			if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_INCLUSAO_LISTA
					|| mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA
					&& mov.lista.isAtivo())
				associadas.add(mov.lista);
			else if (mov.tipoMov.idTipoMov == TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA)
				associadas.remove(mov.lista);
		return associadas;
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

	public void associarLista(SrLista lista, DpPessoa pess, DpLotacao lota)
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
		mov.descrMovimentacao = "Inclusão na lista " + lista.nomeLista
				+ " com a prioridade " + mov.prioridade;
		mov.lista = lista.listaInicial != null ? lista.listaInicial
				: lista;
		mov.solicitacao = this;
		mov.salvar();
		lista.refresh();
	}

	public void desassociarLista(SrLista lista, DpPessoa pess, DpLotacao lota)
			throws Exception {
		if (lista == null)
			throw new IllegalArgumentException("Lista não informada");

		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = pess;
		mov.lotaCadastrante = lota;
		mov.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA);
		mov.descrMovimentacao = "Cancelamento de Inclusão em Lista";
		mov.solicitacao = this;
		mov.lista = lista.listaInicial != null ? lista.listaInicial
				: lista;
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
		mov.lista = lista.listaInicial != null ? lista.listaInicial
				: lista;
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
			throw new Exception("Operação não permitida");
		iniciarAtendimento(lota, pess);
	}

	public void retornarAoPreAtendimento(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeRetornarAoPreAtendimento(lota, pess))
			throw new Exception("Operação não permitida");
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
			throw new Exception("Operação não permitida");

		long estadoAtual = 0L;
		// Edson: A variável estadoAtual e o bloco abaixo, que a define, não
		// seriam necessários se tivéssemos a classe SrEstado.
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
				fecharParcialmente(lota, pess, motivo);
				enviarPesquisa();
			} else
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
			List<SrResposta> respostas) throws Exception {
		if (!podeResponderPesquisa(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.pesquisa = this.getPesquisaDesignada();
		movimentacao.descrMovimentacao = "Avaliação realizada.";
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(TIPO_MOVIMENTACAO_AVALIACAO);
		movimentacao.salvar(pess, lota);
		for (SrPergunta pergunta : this.getPesquisaDesignada().perguntaSet) {
			for (SrResposta resp : respostas) {
				if (pergunta.idPergunta.equals(resp.pergunta.idPergunta)) {
					SrResposta resposta = new SrResposta();
					resposta.pergunta = pergunta;
					resposta.descrResposta = resp.descrResposta;
					resposta.movimentacao = movimentacao;
					resposta.save();
				}
			}
		}
		// if (avaliacao.isSuficiente)...
		// fecharTotalmente()
		// else
		if (getEquipeQualidadeDesignada() != null)
			iniciarControleQualidade(lota, pess);
		else
			fecharTotalmente(null, null, "Fechado.");
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
			throw new Exception("Operação não permitida");
		iniciarAtendimento(lota, pess);
	}

	private void fecharTotalmente(DpLotacao lota, DpPessoa pess, String motivo)
			throws Exception {
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao.findById(TIPO_MOVIMENTACAO_FECHAMENTO);
		mov.descrMovimentacao = motivo;
		mov.salvar(pess, lota);
	}

	public void reabrir(DpLotacao lota, DpPessoa pess) throws Exception {
		if (!podeReabrir(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao mov = new SrMovimentacao(this);
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA);
		mov.lotaAtendente = getUltimoAtendenteEtapaAtendimento();
		mov.salvar(pess, lota);
	}

	public void deixarPendente(DpLotacao lota, DpPessoa pess, String motivo,
			String calendario, String horario) throws Exception {
		if (!podeDeixarPendente(lota, pess))
			throw new Exception("Operação não permitida");
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
		movimentacao.descrMovimentacao = motivo;
		movimentacao.salvar(pess, lota);
	}

	public void terminarPendencia(DpLotacao lota, DpPessoa pess)
			throws Exception {
		if (!podeTerminarPendencia(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA);
		movimentacao.salvar(pess, lota);
	}

	public void cancelar(DpLotacao lota, DpPessoa pess) throws Exception {
		if (!podeCancelar(lota, pess))
			throw new Exception("Operação não permitida");
		SrMovimentacao movimentacao = new SrMovimentacao(this);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO);
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

}
