package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarTextoHtml;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.tp.vraptor.ParametroController;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "andamento", schema = "sigatp")
public class Andamento extends TpModel implements Comparable<Andamento> {

	public static final ActiveRecord<Andamento> AR = new ActiveRecord<>(Andamento.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator") @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence")
	private Long id;

	@UpperCase
	private String descricao;

	@NotNull
	private Calendar dataAndamento;

	private Calendar dataNotificacaoWorkFlow;

 	@NotNull
	@Enumerated(EnumType.STRING)
 	private EstadoRequisicao estadoRequisicao;

	@NotNull
	@ManyToOne
	private RequisicaoTransporte requisicaoTransporte;

	@ManyToOne
	private Missao missao;

	@NotNull
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	private DpPessoa responsavel;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		StringBuffer saida = new StringBuffer();
		boolean temDescricao = (this.descricao != null);
		boolean temMissaoNaDescricao = temDescricao && !(FormatarTextoHtml.removerAcentuacao(this.descricao).toUpperCase().contains("MISSAO"));
		boolean temMissao = (this.missao != null);
		
		if (temDescricao) {
			saida.append(this.descricao);
		}
		
		if(temDescricao && temMissaoNaDescricao && temMissao) {
			saida.append(" - MISSAO NO. " + this.missao.getSequence());
		}
		
		return (saida != null ? saida.toString() : "");
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Calendar getDataAndamento() {
		return dataAndamento;
	}

	public void setDataAndamento(Calendar dataAndamento) {
		this.dataAndamento = dataAndamento;
	}

	public Calendar getDataNotificacaoWorkFlow() {
		return dataNotificacaoWorkFlow;
	}

	public void setDataNotificacaoWorkFlow(Calendar dataNotificacaoWorkFlow) {
		this.dataNotificacaoWorkFlow = dataNotificacaoWorkFlow;
	}

	public EstadoRequisicao getEstadoRequisicao() {
		return estadoRequisicao;
	}

	public void setEstadoRequisicao(EstadoRequisicao estadoRequisicao) {
		this.estadoRequisicao = estadoRequisicao;
	}

	public RequisicaoTransporte getRequisicaoTransporte() {
		return requisicaoTransporte;
	}

	public void setRequisicaoTransporte(RequisicaoTransporte requisicaoTransporte) {
		this.requisicaoTransporte = requisicaoTransporte;
	}

	public Missao getMissao() {
		return missao;
	}

	public void setMissao(Missao missao) {
		this.missao = missao;
	}

	public DpPessoa getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(DpPessoa responsavel) {
		this.responsavel = responsavel;
	}

	@Override
	public int compareTo(Andamento o) {
		int retorno = this.dataAndamento.compareTo(o.dataAndamento);

		return retorno;
	}

	public static List<Andamento> listarPorDataNotificacaoWorkFlow() throws Exception {
		String dataFormatadaOracle = ParametroController.formatarDataParametroParaOracle("cron.dataInicioPesquisaw") ;
		//Object[] parametros = {dataFormatadaOracle, EstadoRequisicao.ABERTA, EstadoRequisicao.AUTORIZADA,EstadoRequisicao.PROGRAMADA,EstadoRequisicao.REJEITADA};
		return Andamento.AR.find("SELECT a FROM Andamento a " +
				 "WHERE a.dataNotificacaoWorkFlow is null " +
				 "AND   a.dataAndamento >= " + dataFormatadaOracle + " " +
				 "AND   a.estadoRequisicao IN ('" + EstadoRequisicao.ABERTA + "'," +
				 							   "'" + EstadoRequisicao.REJEITADA + "'," +
				 							   "'" + EstadoRequisicao.AUTORIZADA + "'," +
				 							   "'" + EstadoRequisicao.PROGRAMADA + "')").fetch();
	}

	public static void gravarDataNotificacaoWorkFlow(Long id) throws Exception {
		Andamento andamento = Andamento.AR.findById(id);
		andamento.dataNotificacaoWorkFlow = Calendar.getInstance();
		andamento.save();
	}
}
