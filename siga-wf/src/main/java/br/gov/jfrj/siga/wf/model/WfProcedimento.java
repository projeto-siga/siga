package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessInstance;
import com.crivano.jflow.model.TaskDefinition;
import com.crivano.jflow.model.enm.ProcessInstanceStatus;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.util.WfResp;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_PROCEDIMENTO", catalog = "WF")
public abstract class WfProcedimento
		implements ProcessInstance<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp> {
	@Id
	@Column(name = "PROC_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFP_ID")
	private WfDefinicaoDeProcedimento definicaoDeProcedimento;

	@Column(name = "PROC_CD_PRINCIPAL")
	private String principal;

	@Column(name = "PROC_TP_PRINCIPAL")
	private WfTipoDePrincipal tipoDePrincipal;

	@Column(name = "PROC_NR_CORRENTE")
	private Integer indiceCorrente = null;

	@Transient
	private Map<String, Object> variavelMap = new TreeMap<>();

	@BatchSize(size = 1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimento")
	private List<WfVariavel> variaveis = new ArrayList<>();

	@Column(name = "PROC_ST_CORRENTE")
	private ProcessInstanceStatus status = ProcessInstanceStatus.INACTIVE;

	@Column(name = "PROC_TS_EVENTO")
	private Date dtEvento;

	@Column(name = "PROC_NM_EVENTO")
	private String evento;

	@Column(name = "PESS_ID_EVENTO")
	private DpPessoa pessoa;

	@Column(name = "LOTA_ID_EVENTO")
	private DpLotacao lotacao;

	@Column(name = "PROC_TP_PRIORIDADE")
	private WfPrioridade prioridade;

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

	public void onSave() throws Exception {
		variaveis.clear();
		for (String k : variavelMap.keySet()) {
			WfVariavel v = new WfVariavel();
			v.setIdentifier(k);
			Object o = variavelMap.get(k);
			if (o instanceof Boolean)
				v.bool = (Boolean) o;
			else if (o instanceof Double)
				v.number = (Double) o;
			else if (o instanceof Date)
				v.date = (Date) o;
			else
				v.string = o.toString();
			variaveis.add(v);
		}
	}

	@Override
	public abstract WfResp calcResponsible(WfDefinicaoDeTarefa tarefa);

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
}
