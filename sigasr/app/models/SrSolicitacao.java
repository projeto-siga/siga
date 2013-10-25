package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import notifiers.Correio;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.mvc.Router;
import util.SigaPlayCalendar;
import util.Util;
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

	@ManyToOne
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
	@JoinColumn(name = "ID_SERVICO")
	public SrServico servico;

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

	@OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrSolicitacao> meuSolicitacaoHistoricoSet;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST)
	protected List<SrAtributo> meuAtributoSet;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;

	// Edson: O where abaixo teve de ser explicito porque os id_refs conflitam
	// entre os modulos, e o Hibernate acaba trazendo tambem marcas do Siga-Doc
	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

	public SrSolicitacao() {

	}

	public SrSolicitacao(DpPessoa solicitante, DpLotacao lotaSolicitante,
			DpPessoa cadastrante, DpLotacao lotaCadastrante,
			SrSolicitacao solcitacaoPai,
			SrFormaAcompanhamento formaAcompanhamento,
			SrItemConfiguracao itemConfiguracao, SrServico servico,
			SrGravidade gravidade, SrUrgencia urgencia, SrTendencia tendencia,
			String dscrSolcitacao, CpComplexo local, String telPrincipal,
			String motivoFechamentoAbertura) {
		super();
		this.solicitante = solicitante;
		this.lotaSolicitante = lotaSolicitante;
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.solicitacaoPai = solcitacaoPai;
		this.formaAcompanhamento = formaAcompanhamento;
		this.itemConfiguracao = itemConfiguracao;
		this.servico = servico;
		this.urgencia = urgencia;
		this.tendencia = tendencia;
		this.gravidade = gravidade;
		this.descrSolicitacao = dscrSolcitacao;
		this.local = local;
		this.telPrincipal = telPrincipal;
		this.motivoFechamentoAbertura = motivoFechamentoAbertura;
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
		final Pattern p = Pattern
				.compile("^?([A-Z]{2})?-?(SR{1})?-?([0-9]{4})?/?([0-9]{1,5})?(\\.{1})?([0-9]{1,2})?$");
		final Matcher m = p.matcher(sigla);

		if (m.find()) {

			if (m.group(1) != null) {
				try {
					CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
					orgaoUsuario.setSiglaOrgaoUsu(m.group(1));
					orgaoUsuario = (CpOrgaoUsuario) JPA
							.em()
							.createQuery(
									"from CpOrgaoUsuario where siglaOrgaoUsu = '"
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

			if (m.group(6) != null)
				numSequencia = Long.valueOf(m.group(6));
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
		if (numSequencia != null) {
			query += " and solicitacaoPai.idSolicitacao = (select idSolicitacao from SrSolicitacao where numSolicitacao = "
					+ numSolicitacao + " )";
			query += " and numSequencia = " + numSequencia;
		} else {
			query += " and numSolicitacao = " + numSolicitacao;
		}
		SrSolicitacao sol = (SrSolicitacao) JPA.em().createQuery(query)
				.getSingleResult();

		return sol;
	}

	@Override
	public List<? extends SrSelecionavel> buscar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodigo(Long idSolicitacao) {
		SrSolicitacao solicitacao = new SrSolicitacao();
		solicitacao = SrSolicitacao.findById(idSolicitacao);

		if (solicitacao.solicitacaoPai != null)
			return solicitacao.solicitacaoPai.getCodigo() + "."
					+ solicitacao.getNumSequenciaString();

		String numString = solicitacao.numSolicitacao.toString();

		while (numString.length() < 5)
			numString = "0" + numString;

		return solicitacao.orgaoUsuario.getAcronimoOrgaoUsu() + "-SR-"
				+ solicitacao.getAnoEmissao() + "/" + numString;
	}

	public String getCodigo() {

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo() + "." + getNumSequenciaString();

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
		List<SrSolicitacao> sols = getHistoricoSolicitacao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	public List<SrAtributo> getAtributoSet() {
		if (meuAtributoSet == null)
			return new ArrayList<SrAtributo>();
		return meuAtributoSet;
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

	public SrMovimentacao getMovimentacaoSolLista(SrSolicitacao solicitacao,
			SrLista lista) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		String query = "select mov from SrMovimentacao mov where mov.solicitacao = "
				+ solicitacao.idSolicitacao
				+ " and mov.lista = "
				+ lista.idLista
				+ " and mov.descrMovimentacao = 'Inclusão em lista' and mov.dtIniMov = (select max(dtIniMov) "
				+ "from SrMovimentacao where solicitacao = "
				+ solicitacao.idSolicitacao
				+ " and lista = "
				+ lista.idLista
				+ " and mov.descrMovimentacao = 'Inclusão em lista')";
		SrMovimentacao movIncl = (SrMovimentacao) JPA.em().createQuery(query)
				.getSingleResult();
		return (SrMovimentacao) movIncl;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoComCanceladas() {
		for (SrMovimentacao movimentacao : getMovimentacaoSetComCancelados())
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimoAndamento() {
		return getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
	}

	public SrMovimentacao getUltimaMovimentacaoPorTipo(Long idTpMov) {
		for (SrMovimentacao movimentacao : getMovimentacaoSet(false, idTpMov))
			return movimentacao;
		return null;
	}

	public SrMovimentacao getUltimaMovimentacaoCancelavel() {
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA
					|| mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA)
				continue;
			return mov;
		}
		return null;
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
				itemConfiguracao, servico,
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
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
		if (conf != null)
			return conf.atendente.getLotacaoAtual();
		return null;
	}

	public HashMap<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados()
			throws Exception {
		HashMap<Long, Boolean> map = new HashMap<Long, Boolean>();
		getTiposAtributoAssociados(map);
		return map;
	}

	public List<SrTipoAtributo> getTiposAtributoAssociados() throws Exception {
		return getTiposAtributoAssociados(null);
	}

	private List<SrTipoAtributo> getTiposAtributoAssociados(
			HashMap<Long, Boolean> map) throws Exception {
		List<SrTipoAtributo> listaFinal = new ArrayList<SrTipoAtributo>();

		for (SrConfiguracao conf : SrConfiguracao.getConfiguracoes(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO,
				null)) {
			SrTipoAtributo tipo = conf.tipoAtributo.getAtual();
			if (!listaFinal.contains(tipo)) {
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
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE);
		if (conf != null)
			return conf.posAtendente.getLotacaoAtual();
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

	public Set<SrSolicitacao> getSolicitacaoFilhaSet() {
		return getSolicitacaoFilhaSet(false);
	}

	private Set<SrSolicitacao> getSolicitacaoFilhaSet(
			boolean considerarFinadasHistorico) {
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
				if (considerarFinadasHistorico)
					listaCompleta.addAll(sol.meuSolicitacaoFilhaSet);
				else
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

	public boolean isEditado() {
		return !idSolicitacao.equals(getHisIdIni());
	}

	public boolean isCancelado() {
		return getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) != null;
	}

	public boolean isFechado() {
		if (isCancelado())
			return false;
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return true;
		}
		return false;
	}

	public boolean isPendente() {
		if (isCancelado())
			return false;
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA)
				return true;
		}
		return false;
	}

	public boolean isEmPosAtendimento() {
		if (isCancelado())
			return false;
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO)
				return true;
		}
		return false;
	}

	public boolean isEmPreAtendimento() {
		if (isCancelado())
			return false;
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO
					|| mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				return false;
			else if (mov.tipoMov.idTipoMov == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO)
				return true;
		}
		return false;
	}

	public boolean isEmAtendimento() {
		return !isCancelado() && !isEmPreAtendimento() && !isEmPosAtendimento()
				&& !isFechado() && !isPendente();
	}

	public boolean temPreAtendenteDesignado() throws Exception {
		return (getPreAtendenteDesignado() != null);
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
				&& ((ultMov.atendente != null && ultMov.atendente
						.equivale(pess)) || ultMov.lotaAtendente.equivale(lota));
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
		return getArquivosAnexosNaCriacao().size() > 0
				|| getMovimentacoesAnexacao().size() > 0;
	}

	public Set<SrArquivo> getArquivosAnexosNaCriacao() {
		Set<SrArquivo> listaCompleta = new LinkedHashSet<SrArquivo>();
		if (solicitacaoInicial != null)
			for (SrSolicitacao sol : getHistoricoSolicitacao())
				if (sol.arquivo != null)
					listaCompleta.add(sol.arquivo);
		return listaCompleta;
	}

	public Set<SrMovimentacao> getMovimentacoesAnexacao() {
		return getMovimentacaoSetPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_ARQUIVO);
	}

	public boolean podeCriarFilha(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmAtendimento() && !isFilha();
	}

	public boolean podeDesfazerMovimentacao(DpLotacao lota, DpPessoa pess) {
		return getMovimentacaoSet().size() > 1
				&& getUltimaMovimentacao().lotaCadastrante.equivale(lota);
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return isEmPreAtendimento() && estaCom(lota, pess);
	}

	public boolean podePriorizar(DpLotacao lota, DpPessoa pess) {
		return podeEditar(lota, pess);
	}

	public boolean podeAbrirJaFechando(DpLotacao lota, DpPessoa pess) {
		return false;
	}

	public boolean podeFinalizarPreAtendimento(DpLotacao lota, DpPessoa pess) {
		return isEmPreAtendimento() && estaCom(lota, pess);
	}

	public boolean podeFechar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess)
				&& (isEmPreAtendimento() || isEmAtendimento());
	}

	public boolean podeFinalizarPosAtendimento(DpLotacao lota, DpPessoa pess) {
		return isEmPosAtendimento() && estaCom(lota, pess);
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
		return isFechado() && estaCom(lota, pess);
	}

	public boolean podeAnexarArquivo(DpLotacao lota, DpPessoa pess) {
		return (isEmPreAtendimento() || isEmAtendimento());
	}

	public boolean podeAssociarLista(DpLotacao lota, DpPessoa pess) {
		return true;
	}

	public boolean podeTrocarAtendente(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmAtendimento();
	}

	public SrSolicitacao deduzirLocalERamal() {

		if (solicitante == null)
			return this;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		String queryString = "select sol from SrSolicitacao sol where sol.idSolicitacao = ("
				+ "	select max(idSolicitacao) from SrSolicitacao "
				+ "	where solicitante.idPessoa in ("
				+ "		select idPessoa from DpPessoa "
				+ "		where idPessoaIni = :idInicial" + "	)" + ")";
		TypedQuery<SrSolicitacao> query = JPA.em().createQuery(queryString,
				SrSolicitacao.class);
		query.setParameter("idInicial", solicitante.getIdPessoaIni());
		List<SrSolicitacao> listaProvisoria = query.getResultList();
		SrSolicitacao sol = null;
		if (listaProvisoria != null && listaProvisoria.size() > 0)
			sol = listaProvisoria.get(0);

		if (sol == null && lotaSolicitante != null) {
			queryString = queryString.replace("solicitante.idPessoa",
					"lotaSolicitante.idLotacao");
			queryString = queryString.replace("Pessoa", "Lotacao");
			query = JPA.em().createQuery(queryString, SrSolicitacao.class);
			query.setParameter("idInicial", lotaSolicitante.getIdLotacaoIni());
			listaProvisoria = query.getResultList();
			if (listaProvisoria != null && listaProvisoria.size() > 0)
				sol = listaProvisoria.get(0);
		}

		if (sol != null) {
			telPrincipal = sol.telPrincipal;
			local = sol.local;
		} else {
			telPrincipal = "";
			local = null;
		}

		return this;
	}

	public SortedSet<SrOperacao> operacoes(final DpLotacao lotaTitular,
			final DpPessoa titular, final boolean vendoHistoricoCompleto) {

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

		operacoes
				.add(new SrOperacao("arrow_divide", "Criar Solicitação Filha",
						podeCriarFilha(lotaTitular, titular),
						"Application.criarFilha"));

		operacoes.add(new SrOperacao("text_list_numbers", "Definir Lista",
				podeAssociarLista(lotaTitular, titular),
				"Application.associarLista"));

		operacoes.add(new SrOperacao("lock", "Fechar", podeFechar(lotaTitular,
				titular), "Application.fechar"));

		operacoes.add(new SrOperacao("accept", "Finalizar Pré-Atendimento",
				podeFinalizarPreAtendimento(lotaTitular, titular),
				"Application.finalizarPreAtendimento"));

		operacoes.add(new SrOperacao("lock", "Finalizar Pós_Atendimento",
				podeFinalizarPosAtendimento(lotaTitular, titular),
				"Application.finalizarPosAtendimento"));

		operacoes.add(new SrOperacao("cross", "Cancelar Solicitação",
				podeCancelar(lotaTitular, titular), "Application.cancelar"));

		operacoes.add(new SrOperacao("lock_open", "Reabrir", podeReabrir(
				lotaTitular, titular), "Application.reabrir"));

		operacoes.add(new SrOperacao("clock_pause", "Deixar Pendente",
				podeDeixarPendente(lotaTitular, titular),
				"Application.deixarPendente"));

		operacoes.add(new SrOperacao("clock_play", "Terminar Pendência",
				podeTerminarPendencia(lotaTitular, titular),
				"Application.terminarPendencia"));

		operacoes.add(new SrOperacao("attach", "Anexar Arquivo",
				podeAnexarArquivo(lotaTitular, titular), "anexarArquivo",
				"modal=true"));

		operacoes.add(new SrOperacao("cancel", "Desfazer "
				+ getUltimaMovimentacaoCancelavel().tipoMov.nome,
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

		checarCampos();

		super.salvar();

		if (getMovimentacaoSetComCancelados().size() == 0) {
			SrMovimentacao mov = new SrMovimentacao();
			if (fecharAoAbrir) {
				mov.descrMovimentacao = motivoFechamentoAbertura;
				mov.lotaAtendente = getPosAtendenteDesignado();
				mov.tipoMov = SrTipoMovimentacao
						.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
			} else {
				mov.descrMovimentacao = "Abertura";
				if (temPreAtendenteDesignado()) {
					mov.lotaAtendente = getPreAtendenteDesignado();
					mov.tipoMov = SrTipoMovimentacao
							.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO);
				} else {
					mov.lotaAtendente = getAtendenteDesignado();
					mov.tipoMov = SrTipoMovimentacao
							.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO);
				}
			}
			mov.solicitacao = this;
			mov.cadastrante = cadastrante;
			mov.lotaCadastrante = lotaCadastrante;
			mov.salvar();
			if (!isEditado()
					&& formaAcompanhamento != SrFormaAcompanhamento.NUNCA
					&& formaAcompanhamento != SrFormaAcompanhamento.FECHAMENTO)
				Correio.notificarAbertura(this);
		} else
			atualizarMarcas();
	}

	public void checarCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		dtReg = new Date();

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();

		if (solicitante == null)
			solicitante = cadastrante;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		if (orgaoUsuario == null)
			orgaoUsuario = lotaSolicitante.getOrgaoUsuario();

		if (numSolicitacao == null && solicitacaoPai == null)
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
							+ "configuração e/ou serviço");
	}

	private Long getNumSeqMov() {
		Long numSeqMov = find(
				"select max(numSequencia)+1 from SrMovimentacao where solicitacao.idSolicitacao = "
						+ idSolicitacao).first();
		return (numSeqMov != null) ? numSeqMov : 1;
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		SrMovimentacao movimentacao = getUltimaMovimentacaoCancelavel();
		movimentacao.desfazer(cadastrante, lotaCadastrante);
	}

	public SrSolicitacao criarFilhaSemSalvar() throws Exception {
		SrSolicitacao filha = new SrSolicitacao();
		Util.copiar(filha, this);
		filha.idSolicitacao = null;
		filha.solicitacaoPai = this;
		for (SrSolicitacao s : getSolicitacaoFilhaSet())
			filha.numSequencia = s.numSequencia;
		if (filha.numSequencia == null)
			filha.numSequencia = 1L;
		else filha.numSequencia++;
		return filha;
	}

	public void atualizarMarcasAntigo() throws Exception {

		removerMarcasAntigo();

		Long marcador = 0L, marcadorAnterior = 0L;
		SrMovimentacao movMarca = null, movMarcaAnterior = null;
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			Long t = mov.tipoMov.idTipoMov;
			if (mov.isCancelada())
				continue;
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO) {
				marcadorAnterior = marcador;
				movMarcaAnterior = movMarca;
				marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA) {
				marcador = marcadorAnterior;
				movMarca = movMarcaAnterior;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				marcadorAnterior = marcador;
				movMarcaAnterior = movMarca;
				marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
				marcador = marcadorAnterior;
				movMarca = movMarcaAnterior;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO) {
				movMarca = mov;
			}
		}

		marcar(marcador, movMarca.lotaAtendente, movMarca.atendente);

		if (!isFechado() && !isCancelado()) {
			marcar(CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE,
					lotaCadastrante, cadastrante);
			marcar(CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE,
					lotaSolicitante, solicitante);
		}

	}

	public void removerMarcasAntigo() {
		for (SrMarca marca : getMarcaSet())
			JPA.em().remove(marca);
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

		Long marcador = 0L, marcadorAnterior = 0L;
		SrMovimentacao movMarca = null, movMarcaAnterior = null;
		for (SrMovimentacao mov : getMovimentacaoSetOrdemCrescente()) {
			Long t = mov.tipoMov.idTipoMov;
			if (mov.isCancelada())
				continue;
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PRE_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO) {
				marcadorAnterior = marcador;
				movMarcaAnterior = movMarca;
				marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA) {
				marcador = marcadorAnterior;
				movMarca = movMarcaAnterior;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_SOLICITACAO) {
				marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA) {
				marcadorAnterior = marcador;
				movMarcaAnterior = movMarca;
				marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
				movMarca = mov;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA) {
				marcador = marcadorAnterior;
				movMarca = movMarcaAnterior;
			}
			if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO) {
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

	public void marcar(Long marcador, DpLotacao lotacao, DpPessoa pessoa)
			throws Exception {
		new SrMarca(marcador, pessoa, lotacao, this).save();
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

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmLista() {
		return getListaAssociada().size() > 0;
	}

	public List<SrLista> getListaDisponivel() {
		ArrayList<SrLista> listaCompleta = new ArrayList<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.lista != null)
				listaCompleta.add(mov.lista);
		}
		SrLista lista = new SrLista();
		ArrayList<SrLista> listaTodos = new ArrayList<SrLista>();
		for (SrLista listas : lista.getListaSet()) {
			if (listas != null)
				listaTodos.add(listas);
		}
		ArrayList<SrLista> listaDisponiveis = new ArrayList<SrLista>(listaTodos);
		listaDisponiveis.removeAll(listaCompleta);
		return listaDisponiveis;
	}

	public List<SrLista> getListaAssociada() {
		ArrayList<SrLista> listaCompleta = new ArrayList<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSet(
				false,
				SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA)) {
			listaCompleta.add(mov.lista);
		}
		return listaCompleta;
	}

	public void associarLista(SrSolicitacao solicitacao, SrLista lista)
			throws Exception {
		// SrSolicitacao sol = new SrSolicitacao();
		// sol.meuMovimentacaoSet = solicitacao.getMovimentacaoSet();
		SrMovimentacao mov = new SrMovimentacao();
		if (lista != null) {
			mov.prioridade = lista.setSolicOrd();
		}
		mov.cadastrante = solicitacao.cadastrante;
		mov.lotaCadastrante = solicitacao.lotaCadastrante;
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_LISTA);
		mov.descrMovimentacao = "Inclusão em lista";
		// mov.cadastrante = solicitacao.cadastrante;
		// mov.lotaCadastrante = solicitacao.lotaCadastrante;
		mov.lista = lista;
		// mov.prioridade = getMovimentacaoSolLista(solicitacao,
		// lista).prioridade;
		mov.solicitacao = solicitacao;
		// mov.estado = SrEstado.ANDAMENTO;
		mov.numSequencia = solicitacao.getNumSeqMov();
		mov.salvar();
		solicitacao.meuMovimentacaoSet.add(mov);
	}

	public void desassociarLista(SrSolicitacao solicitacao, SrLista lista)
			throws Exception {
		SrSolicitacao sol = new SrSolicitacao();
		sol.meuMovimentacaoSet = solicitacao.getMovimentacaoSet();
		SrMovimentacao movIncl = (SrMovimentacao) getMovimentacaoSolLista(
				solicitacao, lista);
		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = solicitacao.cadastrante;
		mov.lotaCadastrante = solicitacao.lotaCadastrante;
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA);
		mov.descrMovimentacao = "Cancelamento de Inclusão em Lista";
		mov.lista = null;
		mov.prioridade = getMovimentacaoSolLista(solicitacao, lista).prioridade;
		mov.solicitacao = solicitacao;
		mov.numSequencia = solicitacao.getNumSeqMov();
		// Edson: comentando, pelo motivo do comentário abaixo
		// mov.idMovRef = movIncl;
		mov.salvar();
		// Edson: comentando, pois a ideia seria reverter, não cancelar...
		// movIncl.dtCancelamento = new Date();
		movIncl.prioridade = null;
		movIncl.movCanceladora = mov;
		movIncl.save();
		sol.meuMovimentacaoSet.add(mov);
		sol.meuMovimentacaoSet.add(movIncl);
	}

	public String getGcTags() {
		String s = "tags=@servico";
		if (servico != null)
			s += servico.getGcTags();
		if (itemConfiguracao != null)
			s += itemConfiguracao.getGcTags();
		return s;
	}

}
