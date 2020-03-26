package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.util.SiglaUtils;
import br.gov.jfrj.siga.wf.util.WfResp;
import br.gov.jfrj.siga.wf.util.SiglaUtils.SiglaDecodificada;

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

	@Override
	public WfResp calcResponsible(WfDefinicaoDeTarefa tarefa) {
		switch (tarefa.getTipoDeResponsavel()) {
		case PRINCIPAL_CADASTRANTE:
		case PRINCIPAL_LOTA_CADASTRANTE:
		case PRINCIPAL_TITULAR:
		case PRINCIPAL_LOTA_TITULAR:
		case PRINCIPAL_SUBSCRITOR:
		case PRINCIPAL_LOTA_SUBSCRITOR:
		case PRINCIPAL_DESTINATARIO:
		case PRINCIPAL_LOTA_DESTINATARIO:
		case PRINCIPAL_GESTOR:
		case PRINCIPAL_LOTA_GESTOR:
		case PRINCIPAL_FISCAL_TECNICO:
		case PRINCIPAL_LOTA_FISCAL_TECNICO:
		case PRINCIPAL_FISCAL_ADMINISTRATIVO:
		case PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO:
		case PRINCIPAL_INTERESSADO:
		case PRINCIPAL_LOTA_INTERESSADO:
		case RESPONSAVEL:
			WfResponsavel r = WfDao.getInstance().consultarResponsavelPorOrgaoEDefinicaoDeResponsavel(getOrgaoUsuario(),
					tarefa.getDefinicaoDeResponsavel());
			return new WfResp(r.getPessoa(), r.getLotacao());
		case PESSOA:
		case LOTACAO:
			return new WfResp(tarefa.getPessoa(), tarefa.getLotacao());
		default:
			return null;
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

}
