package br.gov.jfrj.siga.wf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessInstance;
import com.crivano.jflow.model.TaskDefinition;
import com.crivano.jflow.model.enm.ProcessInstanceStatus;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_PROCEDIMENTO", catalog = "WF")
public abstract class WfProcedimento
		implements ProcessInstance<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResponsavel> {
	@Id
	@Column(name = "PROC_ID", unique = true, nullable = false)
	private Long id;

	private WfDefinicaoDeProcedimento definicao;

	private String principal;

	private WfTipoDePrincipal tipoDePrincipal;

	private Integer indiceCorrente = null;

	private Map<String, Object> variavel = new TreeMap<>();

	private List<WfVariavel> listaDeVariaveis = new ArrayList<>();

	private ProcessInstanceStatus status = ProcessInstanceStatus.INACTIVE;

	private Date dtEvento;

	private String evento;

	private DpPessoa pessoa;

	private DpLotacao lotacao;

	private WfPrioridade prioridade;

	public WfProcedimento(WfDefinicaoDeProcedimento definicao, Map<String, Object> variavel) {
		this.definicao = definicao;
		if (variavel != null)
			this.variavel.putAll(variavel);
	}

	@Override
	public void start() {
		indiceCorrente = -1;
		status = ProcessInstanceStatus.STARTED;
	}

	@Override
	public void pause(String evento, WfResponsavel responsavel) {
		this.evento = evento;
		this.pessoa = ((WfResponsavel) responsavel).getPessoa();
		this.lotacao = ((WfResponsavel) responsavel).getLotacao();
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
		if (i < 0 || i >= definicao.getTaskDefinition().size())
			return null;
		return definicao.getTaskDefinition().get(i);
	}

	// Se for "fim" retorna length + 1
	@Override
	public int getIndexById(String id) {
		int i = 0;
		for (TaskDefinition td : definicao.getTaskDefinition()) {
			if (td.getIdentifier().equals(id))
				return i;
			i++;
		}
		return i;
	}

	public void onLoad() {
		variavel.clear();
		for (WfVariavel v : listaDeVariaveis) {
			if (v.number != null)
				variavel.put(v.getIdentifier(), v.number);
			else if (v.bool != null)
				variavel.put(v.getIdentifier(), v.bool);
			else if (v.date != null)
				variavel.put(v.getIdentifier(), v.date);
			else
				variavel.put(v.getIdentifier(), v.string);
		}
	}

	public void onSave() throws Exception {
		listaDeVariaveis.clear();
		for (String k : variavel.keySet()) {
			WfVariavel v = new WfVariavel();
			v.setIdentifier(k);
			Object o = variavel.get(k);
			if (o instanceof Boolean)
				v.bool = (Boolean) o;
			else if (o instanceof Double)
				v.number = (Double) o;
			else if (o instanceof Date)
				v.date = (Date) o;
			else
				v.string = o.toString();
			listaDeVariaveis.add(v);
		}
	}

	@Override
	public abstract WfResponsavel calcResponsible(WfDefinicaoDeTarefa tarefa);

	@Override
	public WfDefinicaoDeProcedimento getProcessDefinition() {
		return definicao;
	}

	@Override
	public Integer getCurrentIndex() {
		return indiceCorrente;
	}

	@Override
	public Map<String, Object> getVariable() {
		return variavel;
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
	public WfResponsavel getResponsible() {
		return new WfResponsavel(pessoa, lotacao);
	}

	@Override
	public void setCurrentIndex(int indiceCorrente) {
		this.indiceCorrente = indiceCorrente;
	}

	@Override
	public void setProcessDefinition(WfDefinicaoDeProcedimento definicao) {
		this.definicao = definicao;
	}

	@Override
	public void setVariable(Map<String, Object> variavel) {
		this.variavel = variavel;
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

	public WfDefinicaoDeProcedimento getDefinicao() {
		return definicao;
	}

	public void setDefinicao(WfDefinicaoDeProcedimento definicao) {
		this.definicao = definicao;
	}

	public Integer getIndiceCorrente() {
		return indiceCorrente;
	}

	public void setIndiceCorrente(Integer indiceCorrente) {
		this.indiceCorrente = indiceCorrente;
	}

	public Map<String, Object> getVariavel() {
		return variavel;
	}

	public void setVariavel(Map<String, Object> variavel) {
		this.variavel = variavel;
	}

	public List<WfVariavel> getListaDeVariaveis() {
		return listaDeVariaveis;
	}

	public void setListaDeVariaveis(List<WfVariavel> listaDeVariaveis) {
		this.listaDeVariaveis = listaDeVariaveis;
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
