package br.gov.jfrj.siga.wf.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.PausableTask;
import com.crivano.jflow.model.TaskDefinitionDetour;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.util.NaoSerializar;
import br.gov.jfrj.siga.wf.util.WfResp;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_desvio")
public class WfDefinicaoDeDesvio extends HistoricoAuditavelSuporte
		implements TaskDefinitionDetour, Sincronizavel, Comparable<Sincronizavel> {
	@Id
	@GeneratedValue
	@Column(name = "DEFD_ID", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long id;

	@Column(name = "DEFD_NM", length = 256)
	private java.lang.String nome;

	@Column(name = "DEFD_TX_CONDICAO", length = 256)
	private java.lang.String condicao;

	@Column(name = "DEFD_NR_ORDEM")
	private int ordem;

	@NaoSerializar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID")
	private WfDefinicaoDeTarefa definicaoDeTarefa;

	@NaoSerializar
	@NaoRecursivo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_SEGUINTE")
	private WfDefinicaoDeTarefa seguinte;

	@Transient
	private String seguinteIde;

	@Column(name = "DEFD_FG_ULTIMO")
	private boolean ultimo;

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}

	@Override
	public String getTitle() {
		return nome;
	}

	@Override
	public String getTaskIdentifier() {
		if (isUltimo())
			return "finish";
		if (seguinte == null)
			return null;
		return seguinte.getIdentifier();
	}

	@Override
	public String getCondition() {
		return condicao;
	}

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.lang.String getCondicao() {
		return condicao;
	}

	public void setCondicao(java.lang.String condicao) {
		this.condicao = condicao;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefa() {
		return definicaoDeTarefa;
	}

	public void setDefinicaoDeTarefa(WfDefinicaoDeTarefa definicaoDeTarefa) {
		this.definicaoDeTarefa = definicaoDeTarefa;
	}

	public WfDefinicaoDeTarefa getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(WfDefinicaoDeTarefa seguinte) {
		this.seguinte = seguinte;
	}

	public boolean isUltimo() {
		return ultimo;
	}

	public void setUltimo(boolean ultimo) {
		this.ultimo = ultimo;
	}

	public String getSeguinteIde() {
		return seguinteIde;
	}

	public void setSeguinteIde(String seguinteIde) {
		this.seguinteIde = seguinteIde;
	}

	@Override
	public String getIdExterna() {
		return this.getDefinicaoDeTarefa().getIdExterna() + "|" + getNome();
	}

	@Override
	public void setIdExterna(String idExterna) {
	}

	@Override
	public void setIdInicial(Long idInicial) {
		this.setHisIdIni(idInicial);
	}

	@Override
	public Date getDataInicio() {
		return getHisDtIni();
	}

	@Override
	public void setDataInicio(Date dataInicio) {
		setHisDtIni(dataInicio);
	}

	@Override
	public Date getDataFim() {
		return getHisDtFim();
	}

	@Override
	public void setDataFim(Date dataFim) {
		setHisDtFim(dataFim);
	}

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	@Override
	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	@Override
	public String getDescricaoExterna() {
		return getNome();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	@PostLoad
	public void postLoad() {
		if (this.getSeguinte() != null)
			this.setSeguinteIde(Long.toString(this.getSeguinte().getIdInicial()));
	}

	public String getIcon() {
		if (ultimo)
			return "stop";
		if (isYes())
			return "accept";
		if (isNo())
			return "cancel";
		return "bullet_go";
	}

	public boolean isYes() {
		return nome.equalsIgnoreCase("sim");
	}

	public boolean isNo() {
		return nome.equalsIgnoreCase("não") || nome.equalsIgnoreCase("nao");
	}

	public static class ProximaTarefa {
		public WfDefinicaoDeTarefa tdProxima;
		public boolean ultimo;
	}

	public static ProximaTarefa localizarProximaTarefaPausavel(WfDefinicaoDeProcedimento pd, WfDefinicaoDeTarefa td,
			WfDefinicaoDeDesvio dd) {
		WfDefinicaoDeTarefa tdProxima = dd != null ? dd.getSeguinte() : td;
		boolean ultimo = dd != null ? dd.isUltimo() : (td != null ? td.isUltimo() : true);

		// Quando é um desvio e ele não tem tarefa seguinte, pula para a próxima tarefa
		// da lista
		if (dd != null && tdProxima == null && !ultimo) {
			tdProxima = obterSeguinte(pd, td);
			if (tdProxima == null)
				ultimo = true;
		}

		Set<WfDefinicaoDeTarefa> set = new HashSet<>();

		while (tdProxima != null) {
			if (set.contains(tdProxima))
				throw new RuntimeException("Caminho circular encontrado ao calcular próxima tarefa pausável");
			else
				set.add(tdProxima);

			if (PausableTask.class.isAssignableFrom(tdProxima.getTipoDeTarefa().getClazz()))
				break;

			WfDefinicaoDeDesvio desvio = null;
			if (tdProxima.getDefinicaoDeDesvio() != null && tdProxima.getDefinicaoDeDesvio().size() == 1)
				desvio = tdProxima.getDefinicaoDeDesvio().get(0);

			// A proxima tarefa está indicada
			if (tdProxima.getSeguinte() != null) {
				tdProxima = tdProxima.getSeguinte();
				continue;
			}

			// Existe um único desvio e ele aponta para uma próxima tarefa
			else if (desvio != null && desvio.getSeguinte() != null) {
				tdProxima = desvio.getSeguinte();
				continue;
			}

			// Existe mais de um desvio e não poderemos inferir a próxima tarefa pausável
			else if (tdProxima.getDefinicaoDeDesvio() != null && tdProxima.getDefinicaoDeDesvio().size() > 1) {
				tdProxima = null;
				break;
			}

			// Depois dessa tarefa vai terminar
			else if (tdProxima.isUltimo()
					|| tdProxima.getDefinicaoDeProcedimento().getDefinicaoDeTarefa().size() == tdProxima.getOrdem() + 1
					|| (desvio != null && desvio.isUltimo())) {
				ultimo = true;
				tdProxima = null;
				break;
			}

			// Ou vai para a tarefa seguinte
			else {
				tdProxima = obterSeguinte(pd, tdProxima);
			}
		}
		ProximaTarefa p = new ProximaTarefa();
		p.tdProxima = tdProxima;
		p.ultimo = ultimo;
		return p;
	}

	private static WfDefinicaoDeTarefa obterSeguinte(WfDefinicaoDeProcedimento pd, WfDefinicaoDeTarefa td) {
		if (td == null)
			return null;
		List<WfDefinicaoDeTarefa> definicoesDeTarefas = td.getDefinicaoDeProcedimento().getDefinicaoDeTarefa();
		int indice = td.getOrdem();
		if (definicoesDeTarefas.size() > indice + 1)
			return definicoesDeTarefas.get(indice + 1);
		return null;
	}

	public String obterProximoResponsavel(WfProcedimento pi) {
		return obterProximoResponsavel(pi, this);
	}

	public static String obterProximoResponsavel(WfProcedimento pi, WfDefinicaoDeDesvio dd) {
		WfDefinicaoDeTarefa td = pi.getCurrentTaskDefinition();
		if (dd == null && td != null) {
			if (td.getSeguinte() != null)
				td = td.getSeguinte();
			else if (!td.isUltimo())
				td = pi.getTaskDefinitionByIndex(pi.getCurrentIndex() + 1);
		}
		ProximaTarefa pt = localizarProximaTarefaPausavel(pi.getDefinicaoDeProcedimento(), td, dd);
		if (pt.ultimo)
			return "FIM";
		else if (pt.tdProxima != null) {
			WfResp r = (WfResp) pi.calcResponsible(pt.tdProxima);
			if (r != null)
				return r.getInitials();
		}
		return null;
	}

}