package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessInstance;
import com.crivano.jflow.model.TaskDefinition;
import com.crivano.jflow.model.enm.ProcessInstanceStatus;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.logic.PodeSim;
import br.gov.jfrj.siga.wf.logic.WfPodePegar;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.SiglaUtils;
import br.gov.jfrj.siga.wf.util.SiglaUtils.SiglaDecodificada;
import br.gov.jfrj.siga.wf.util.WfAcaoVO;
import br.gov.jfrj.siga.wf.util.WfResp;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_procedimento")
public class WfProcedimento extends Objeto
		implements ProcessInstance<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp> {
	public static ActiveRecord<WfProcedimento> AR = new ActiveRecord<>(WfProcedimento.class);

	@Id
	@GeneratedValue
	@Column(name = "PROC_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFP_ID")
	private WfDefinicaoDeProcedimento definicaoDeProcedimento;

	@Column(name = "PROC_CD_PRINCIPAL")
	private String principal;

	@Enumerated(EnumType.STRING)
	@Column(name = "PROC_TP_PRINCIPAL")
	private WfTipoDePrincipal tipoDePrincipal;

	@Column(name = "PROC_NR_CORRENTE")
	private Integer indiceCorrente = null;

	@Transient
	private Map<String, Object> variavelMap = new TreeMap<>();

	@BatchSize(size = 1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimento")
	private List<WfVariavel> variaveis = new ArrayList<>();

	@BatchSize(size = 1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimento")
	private Set<WfMov> movimentacoes = new TreeSet<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "PROC_ST_CORRENTE")
	private ProcessInstanceStatus status = ProcessInstanceStatus.INACTIVE;

	@Column(name = "PROC_TS_EVENTO")
	private Date dtEvento;

	@Column(name = "PROC_NM_EVENTO")
	private String evento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_EVENTO")
	private DpPessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_EVENTO")
	private DpLotacao lotacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "PROC_TP_PRIORIDADE")
	private WfPrioridade prioridade = WfPrioridade.MEDIA;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGU_ID")
	private CpOrgaoUsuario orgaoUsuario;

	@Column(name = "PROC_ANO")
	private Integer ano;

	@Column(name = "PROC_NR")
	private Integer numero;

	public WfProcedimento() {
	}

	public WfProcedimento(WfDefinicaoDeProcedimento definicao, Map<String, Object> variavel) {
		this.definicaoDeProcedimento = definicao;
		if (variavel != null)
			this.variavelMap.putAll(variavel);
	}

	@Override
	public void start() {
		indiceCorrente = -1;
		status = ProcessInstanceStatus.STARTED;
	}

	@Override
	public void pause(String evento, WfResp responsavel) {
		this.evento = evento;
		this.pessoa = ((WfResp) responsavel).getPessoa();
		this.lotacao = ((WfResp) responsavel).getLotacao();
		this.dtEvento = new Date();
		status = ProcessInstanceStatus.PAUSED;
	}

	@Override
	public void resume() {
		this.evento = null;
		this.pessoa = null;
		this.lotacao = null;
		this.dtEvento = null;
		status = ProcessInstanceStatus.RESUMING;
	}

	@Override
	public void end() {
		indiceCorrente = null;
		status = ProcessInstanceStatus.FINISHED;
	}

	@Override
	public WfDefinicaoDeTarefa getCurrentTaskDefinition() {
		if (indiceCorrente == null)
			return null;
		return getTaskDefinitionByIndex(indiceCorrente);
	}

	@Override
	public WfDefinicaoDeTarefa getTaskDefinitionByIndex(int i) {
		if (i < 0 || i >= definicaoDeProcedimento.getTaskDefinition().size())
			return null;
		return definicaoDeProcedimento.getTaskDefinition().get(i);
	}

	// Se for "fim" retorna length + 1
	@Override
	public int getIndexById(String id) {
		if ("finish".equals(id))
			return definicaoDeProcedimento.getTaskDefinition().size();
		int i = 0;
		for (TaskDefinition td : definicaoDeProcedimento.getTaskDefinition()) {
			if (td.getIdentifier().equals(id))
				return i;
			i++;
		}
		return i;
	}

	@PostLoad
	public void onLoad() {
		variavelMap.clear();
		for (WfVariavel v : variaveis) {
			if (v.number != null)
				variavelMap.put(v.getIdentifier(), v.number);
			else if (v.bool != null)
				variavelMap.put(v.getIdentifier(), v.bool);
			else if (v.date != null)
				variavelMap.put(v.getIdentifier(), v.date);
			else
				variavelMap.put(v.getIdentifier(), v.string);
		}
	}

	@PrePersist
	private void onPersist() {
		if (getAno() != null)
			return;
		setAno(WfDao.getInstance().dt().getYear() + 1900);
		Query qry = em().createQuery(
				"select max(numero) from WfProcedimento pi where ano = :ano and orgaoUsuario.idOrgaoUsu = :ouid");
		qry.setParameter("ano", getAno());
		qry.setParameter("ouid", getOrgaoUsuario().getId());
		Integer i = (Integer) qry.getSingleResult();
		setNumero((i == null ? 0 : i) + 1);
	}

	@PreUpdate
	public void onSave() throws Exception {
	}

	private void assertPrincipal() throws RuntimeException {
		if (this.getPrincipal() == null)
			throw new RuntimeException("Não é possível identificar o responsável porque não há \'principal'");
	}

	private WfResp assertPrincipalPessoa(String s) {
		assertPrincipal();
		PessoaLotacaoParser lpp = new PessoaLotacaoParser(s);
		if (lpp.getLotacao() == null)
			return null;
		return new WfResp(lpp.getPessoa(), lpp.getLotacao());
	}

	private WfResp assertPrincipalLotacao(String s) {
		assertPrincipal();
		PessoaLotacaoParser lpp = new PessoaLotacaoParser(s);
		if (lpp.getLotacao() == null)
			return null;
		return new WfResp(null, lpp.getLotacao());
	}

	@Override
	public WfResp calcResponsible(WfDefinicaoDeTarefa tarefa) {
		return localizarResponsavelAtual(tarefa);
	}

	public WfResp localizarResponsavelAtual(WfDefinicaoDeTarefa tarefa) {
		WfResp resp = localizarResponsavelOriginal(tarefa);

		for (WfMov mov : getMovimentacoes()) {
			if (!mov.isAtivo())
				continue;
			if (mov instanceof WfMovDesignacao) {
				WfMovDesignacao m = (WfMovDesignacao) mov;
				if ((m.getPessoaDe() != null && m.getPessoaDe().equivale(resp.getPessoa()))
						|| (m.getPessoaDe() == null && m.getLotaDe().equivale(resp.getLotacao()))) {
					resp = new WfResp(m.getPessoaPara(), m.getLotaPara());
				}
			}
		}

		return resp;
	};

	public WfResp localizarResponsavelOriginal(WfDefinicaoDeTarefa tarefa) {
		ExService service = null;
		if (this.getPrincipal() != null && this.getTipoDePrincipal() == WfTipoDePrincipal.DOC)
			service = Service.getExService();
		try {
			switch (tarefa.getTipoDeResponsavel()) {
			case PRINCIPAL_CADASTRANTE:
				return assertPrincipalPessoa(service.cadastrante(getPrincipal()));
			case PRINCIPAL_LOTA_CADASTRANTE:
				return assertPrincipalLotacao(service.cadastrante(getPrincipal()));
			case PRINCIPAL_TITULAR:
				return assertPrincipalPessoa(service.titular(getPrincipal()));
			case PRINCIPAL_LOTA_TITULAR:
				return assertPrincipalLotacao(service.titular(getPrincipal()));
			case PRINCIPAL_SUBSCRITOR:
				return assertPrincipalPessoa(service.subscritor(getPrincipal()));
			case PRINCIPAL_LOTA_SUBSCRITOR:
				return assertPrincipalLotacao(service.subscritor(getPrincipal()));
			case PRINCIPAL_DESTINATARIO:
				return assertPrincipalPessoa(service.destinatario(getPrincipal()));
			case PRINCIPAL_LOTA_DESTINATARIO:
				return assertPrincipalLotacao(service.destinatario(getPrincipal()));
			case PRINCIPAL_GESTOR:
				return assertPrincipalPessoa(service.gestor(getPrincipal()));
			case PRINCIPAL_LOTA_GESTOR:
				return assertPrincipalLotacao(service.gestor(getPrincipal()));
			case PRINCIPAL_FISCAL_TECNICO:
				return assertPrincipalPessoa(service.fiscalTecnico(getPrincipal()));
			case PRINCIPAL_LOTA_FISCAL_TECNICO:
				return assertPrincipalLotacao(service.fiscalTecnico(getPrincipal()));
			case PRINCIPAL_FISCAL_ADMINISTRATIVO:
				return assertPrincipalPessoa(service.fiscalAdministrativo(getPrincipal()));
			case PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO:
				return assertPrincipalLotacao(service.fiscalAdministrativo(getPrincipal()));
			case PRINCIPAL_INTERESSADO:
				return assertPrincipalPessoa(service.interessado(getPrincipal()));
			case PRINCIPAL_LOTA_INTERESSADO:
				return assertPrincipalLotacao(service.interessado(getPrincipal()));
			case RESPONSAVEL:
				WfResponsavel r = WfDao.getInstance().consultarResponsavelPorOrgaoEDefinicaoDeResponsavel(
						getOrgaoUsuario(), tarefa.getDefinicaoDeResponsavel());
				return new WfResp(r.getPessoa(), r.getLotacao());
			case PESSOA:
			case LOTACAO:
				return new WfResp(tarefa.getPessoa(), tarefa.getLotacao());
			default:
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível calcular o responsável pela tarefa " + tarefa.getNome()
					+ " do procedimento " + this.getSigla(), e);
		}
	};

	@Override
	public WfDefinicaoDeProcedimento getProcessDefinition() {
		return definicaoDeProcedimento;
	}

	@Override
	public Integer getCurrentIndex() {
		return indiceCorrente;
	}

	@Override
	public Map<String, Object> getVariable() {
		return variavelMap;
	}

	@Override
	public ProcessInstanceStatus getStatus() {
		return status;
	}

	@Override
	public Date getDtEvent() {
		return dtEvento;
	}

	@Override
	public String getEvent() {
		return evento;
	}

	@Override
	public WfResp getResponsible() {
		return new WfResp(pessoa, lotacao);
	}

	@Override
	public void setCurrentIndex(int indiceCorrente) {
		this.indiceCorrente = indiceCorrente;
	}

	@Override
	public void setProcessDefinition(WfDefinicaoDeProcedimento definicao) {
		this.definicaoDeProcedimento = definicao;
	}

	@Override
	public void setVariable(Map<String, Object> variavel) {
		this.variavelMap = variavel;
	}

	public DpPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public WfTipoDePrincipal getTipoDePrincipal() {
		return tipoDePrincipal;
	}

	public void setTipoDePrincipal(WfTipoDePrincipal tipoDePrincipal) {
		this.tipoDePrincipal = tipoDePrincipal;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefaCorrente() {
		return getCurrentTaskDefinition();
	}

	public WfDefinicaoDeProcedimento getDefinicaoDeProcedimento() {
		return definicaoDeProcedimento;
	}

	public void setDefinicaoDeProcedimento(WfDefinicaoDeProcedimento definicao) {
		this.definicaoDeProcedimento = definicao;
	}

	public Integer getIndiceCorrente() {
		return indiceCorrente;
	}

	public void setIndiceCorrente(Integer indiceCorrente) {
		this.indiceCorrente = indiceCorrente;
	}

	public Map<String, Object> getVariavelMap() {
		return variavelMap;
	}

	public void setVariavelMap(Map<String, Object> variavelMap) {
		this.variavelMap = variavelMap;
	}

	public List<WfVariavel> getVariaveis() {
		return variaveis;
	}

	public void setVariaveis(List<WfVariavel> listaDeVariaveis) {
		this.variaveis = listaDeVariaveis;
	}

	public Date getDtEvento() {
		return dtEvento;
	}

	public void setDtEvento(Date dtEvento) {
		this.dtEvento = dtEvento;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public void setStatus(ProcessInstanceStatus status) {
		this.status = status;
	}

	public WfPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(WfPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Set<WfMov> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(Set<WfMov> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public String getAtendente() {
		if (getPessoa() != null)
			return getPessoa().getSigla();
		if (getLotacao() != null)
			return getLotacao().getSigla();
		return "";
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getSigla() {
		return orgaoUsuario.getAcronimoOrgaoUsu() + "-WF-" + ano + "/" + Utils.completarComZeros(numero, 5);
	}

	public String getSiglaCompacta() {
		return getSigla().replace("-", "").replace("/", "");
	}

	public void setSigla(String sigla) {
		SiglaDecodificada d = SiglaUtils.parse(sigla, "WF", null);
		this.ano = d.ano;
		this.numero = d.numero;
		this.orgaoUsuario = d.orgaoUsuario;
	}

	public static WfProcedimento findBySigla(String sigla) throws NumberFormatException, Exception {
		return findBySigla(sigla, null);
	}

	public static WfProcedimento findBySigla(String sigla, CpOrgaoUsuario ouDefault)
			throws NumberFormatException, Exception {
		SiglaDecodificada d = SiglaUtils.parse(sigla, "WF", null);

		WfProcedimento info = null;

		if (d.id != null) {
			info = AR.findById(d.id);
		} else if (d.numero != null) {
			info = AR.find("ano = ?1 and numero = ?2 and ou.idOrgaoUsu = ?3", d.ano, d.numero, d.orgaoUsuario.getId())
					.first();
		}

		if (info == null) {
			throw new AplicacaoException("Não foi possível encontrar uma instância de procedimento com o código "
					+ sigla + ". Favor verificá-lo.");
		} else
			return info;
	}

	public List<AcaoVO> getAcoes(DpPessoa titular, DpLotacao lotaTitular) {
		List<AcaoVO> set = new ArrayList<>();

		set.add(WfAcaoVO.builder().nome("_Anotar").icone("note_add").acao("/app/anotar").modal("anotarModal")
				.exp(new PodeSim()).build());

		set.add(WfAcaoVO.builder().nome("_Pegar").icone("add")
				.acao("/app/procedimento/" + getSiglaCompacta() + "/pegar")
				.exp(new WfPodePegar(this, titular, lotaTitular)).post(true).build());

		return set;
	}

	public boolean isFormulario() {
		return status == ProcessInstanceStatus.PAUSED
				&& getCurrentTaskDefinition().getTipoDeTarefa() == WfTipoDeTarefa.FORMULARIO;
	}

	public boolean isDesabilitarFormulario(DpPessoa titular, DpLotacao lotaTitular) {
		return !titular.equivale(getPessoa()) && !lotaTitular.equivale(getLotacao());
	}

	public Object obterValorDeVariavel(WfDefinicaoDeVariavel vd) {
		return getVariavelMap().get(vd.getIdentificador());
	}

	public String getMsgAviso(DpPessoa titular, DpLotacao lotaTitular) throws Exception {
//		WfConhecimento c = WfDao.getInstance().consultarConhecimento(ti.getDefinicaoDeTarefa().getId());
//		if (c != null) {
//			this.setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
//			this.setConhecimento(c.getDescricao());
//		}

		if (!titular.equivale(getPessoa()) && !lotaTitular.equivale(getLotacao())) {
			if (getPessoa() != null && getLotacao() != null)
				return "Esta tarefa será desempenhada por " + titular.getSigla() + " na lotação "
						+ getLotacao().getSigla();
			if (getPessoa() != null)
				return "Esta tarefa será desempenhada por " + getPessoa().getSigla();
			if (getLotacao() != null)
				return "Esta tarefa será desempenhada pela lotação " + getLotacao().getSigla();
		}

		String siglaTitular = titular.getSigla() + "@" + lotaTitular.getSiglaCompleta();
		String respWF = null;
		if (getPessoa() != null)
			respWF = getPessoa().getSigla();
		if (respWF == null && getLotacao() != null)
			respWF = "@" + getLotacao().getSiglaCompleta();

		if (!Utils.empty(getPrincipal()) && getTipoDePrincipal() == WfTipoDePrincipal.DOC) {
			ExService service = Service.getExService();
			String respEX = service.getAtendente(getPrincipal(), siglaTitular);
			DpLotacao lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();
			DpLotacao lotWF = new PessoaLotacaoParser(respWF).getLotacaoOuLotacaoPrincipalDaPessoa();
			boolean podeMovimentar = service.podeMovimentar(getPrincipal(), siglaTitular);
			boolean estaComTarefa = titular.equivale(new PessoaLotacaoParser(respWF).getPessoa());
			respEX = service.getAtendente(getPrincipal(), siglaTitular);
			lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();

			if (podeMovimentar && !estaComTarefa) {
				if (lotWF == null || !lotWF.equivale(lotEX)) {
					return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal()
							+ " for transferido para " + (lotWF == null ? "Nula" : lotWF.getSigla()) + ".";
				}
			}
			if (!podeMovimentar && estaComTarefa) {
				return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal() + ", que está com "
						+ lotEX.getSigla() + ", for devolvido.";
			}
			if (!podeMovimentar && !estaComTarefa) {
				if (lotWF != null && !lotWF.equivale(lotEX)) {
					return "Esta tarefa só poderá prosseguir quando o documento " + getPrincipal() + ", que está com "
							+ lotEX.getSigla() + ", for transferido para " + lotWF.getSigla() + ".";
				}
			}
		}
		return null;
	}

	public List<String> getTags() {
		ArrayList<String> tags = new ArrayList<String>();
		if (getProcessDefinition() != null)
			tags.add("@" + Texto.slugify(getProcessDefinition().getNome(), true, true));
		if (getCurrentTaskDefinition() != null && getCurrentTaskDefinition().getNome() != null)
			tags.add("@" + Texto.slugify(getCurrentTaskDefinition().getNome(), true, true));

		if (getProcessDefinition().getNome() != null && getCurrentTaskDefinition() != null
				&& getCurrentTaskDefinition().getNome() != null)
			tags.add("^wf:" + Texto.slugify(
					getProcessDefinition().getNome() + "-" + getCurrentTaskDefinition().getNome(), true, true));
		return tags;
	}

}
