package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "autodeinfracao", schema = "sigatp")
public class AutoDeInfracao extends TpModel implements ConvertableEntity, Comparable<AutoDeInfracao> {

	public static final ActiveRecord<AutoDeInfracao> AR = new ActiveRecord<>(AutoDeInfracao.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@NotNull
	@Data(descricaoCampo = "Data e Hora")
	private Calendar dataHora;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "VEICULO_ID")
	private Veiculo veiculo;

	//  Incluido depois da OSI17 - Joao Luis

	@ManyToOne
	@NotNull
	@JoinColumn(name = "PENALIDADE_ID")
	private Penalidade penalidade;

	//Incluido apos a OSI22 - Fernando
	@Enumerated(EnumType.STRING)
	private TipoDeNotificacao tipoDeNotificacao;
	
	/* Comentado apos OSI17 - Joao Luis 
	@NotNull
	private String codigoDaAutuacao;

	@NotNull
	private String codigoDaPenalidade;

	@NotNull
	@UpperCase
	private String descricao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Gravidade gravidade;

	@NotNull
	@UpperCase
	private String enquadramento; */

	@NotNull
	@NotEmpty
	@UpperCase
	private String local;

	@NotNull
	@Enumerated(EnumType.STRING)
	private PerguntaSimNao foiRecebido;

	@NotNull
	private Double valor;

	private Double valorComDesconto;
	
	//Comentado apos a OSI22 - Fernando
	//@NotNull
	//private Integer quantidadeDePontos;

	@NotNull
	@Data(descricaoCampo = "Data de Vencimento")
	private Calendar dataDeVencimento;

	private Calendar dataDePagamento;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "CONDUTOR_ID")
	private Condutor condutor;

	@Data(descricaoCampo = "Data Limite")
	private Calendar dataLimiteApresentacao;

	@UpperCase
	private String memorando;

	@Data(descricaoCampo = "Data Processo")
	private Calendar dataDoProcesso;

	@UpperCase
	private String numeroDoProcesso;

	@Transient
	public PerguntaSimNao foiPago() {
		return dataDePagamento != null ? PerguntaSimNao.SIM : PerguntaSimNao.NAO;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	//  Incluido depois da OSI17 - Joao Luis

	public Penalidade getPenalidade() {
		return penalidade;
	}

	public void setPenalidade(Penalidade penalidade) {
		this.penalidade = penalidade;
	}
	
	public TipoDeNotificacao getTipoDeNotificacao() {
		return tipoDeNotificacao;
	}
	
	public void setTipoDeNotificacao(TipoDeNotificacao tipoDeNotificacao) {
		this.tipoDeNotificacao = tipoDeNotificacao;
	}

	/*  Comentado depois da OSI17 - Joao Luis
	public String getCodigoDaAutuacao() {
		return codigoDaAutuacao;
	}

	public void setCodigoDaAutuacao(String codigoDaAutuacao) {
		this.codigoDaAutuacao = codigoDaAutuacao;
	}

	public String getCodigoDaPenalidade() {
		return codigoDaPenalidade;
	}

	public void setCodigoDaPenalidade(String codigoDaPenalidade) {
		this.codigoDaPenalidade = codigoDaPenalidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Gravidade getGravidade() {
		return gravidade;
	}

	public void setGravidade(Gravidade gravidade) {
		this.gravidade = gravidade;
	}

	public String getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(String enquadramento) {
		this.enquadramento = enquadramento;
	} */

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public PerguntaSimNao getFoiRecebido() {
		return foiRecebido;
	}

	public void setFoiRecebido(PerguntaSimNao foiRecebido) {
		this.foiRecebido = foiRecebido;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorComDesconto() {
		return valorComDesconto;
	}

	public void setValorComDesconto(Double valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

/*	public Integer getQuantidadeDePontos() {
		return quantidadeDePontos;
	}

	public void setQuantidadeDePontos(Integer quantidadeDePontos) {
		this.quantidadeDePontos = quantidadeDePontos;
	}*/

	public Calendar getDataDeVencimento() {
		return dataDeVencimento;
	}

	public void setDataDeVencimento(Calendar dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public Calendar getDataDePagamento() {
		return dataDePagamento;
	}

	public void setDataDePagamento(Calendar dataDePagamento) {
		this.dataDePagamento = dataDePagamento;
	}

	public Condutor getCondutor() {
		return condutor;
	}

	public void setCondutor(Condutor condutor) {
		this.condutor = condutor;
	}

	public Calendar getDataLimiteApresentacao() {
		return dataLimiteApresentacao;
	}

	public void setDataLimiteApresentacao(Calendar dataLimiteApresentacao) {
		this.dataLimiteApresentacao = dataLimiteApresentacao;
	}

	public String getMemorando() {
		return memorando;
	}

	public void setMemorando(String memorando) {
		this.memorando = memorando;
	}

	public Calendar getDataDoProcesso() {
		return dataDoProcesso;
	}

	public void setDataDoProcesso(Calendar dataDoProcesso) {
		this.dataDoProcesso = dataDoProcesso;
	}

	public String getNumeroDoProcesso() {
		return numeroDoProcesso;
	}

	public void setNumeroDoProcesso(String numeroDoProcesso) {
		this.numeroDoProcesso = numeroDoProcesso;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public AutoDeInfracao() {
		this.id = 0L;
		this.foiRecebido = PerguntaSimNao.NAO;
//		this.gravidade = Gravidade.LEVE;
	}

	public AutoDeInfracao(Long id, Calendar dataHora, Veiculo veiculo, String codigoDaAutuacao, String codigoDaPenalidade, String descricao, Gravidade gravidade, String enquadramento, String local,
			PerguntaSimNao foiRecebido, Double valor, int quantidadeDePontos, Double valorComDesconto, Calendar dataDeVencimento, Calendar dataDePagamento, Condutor condutor,
			Calendar dataLimiteApresentacao, String memorando, Calendar dataDoProcesso, String numeroDoProcesso) {
		super();
		this.id = id;
		this.dataHora = dataHora;
		this.veiculo = veiculo;
//		this.codigoDaAutuacao = codigoDaAutuacao;
//		this.codigoDaPenalidade = codigoDaPenalidade;
//		this.descricao = descricao;
//		this.gravidade = gravidade;
//		this.enquadramento = enquadramento;
		this.local = local;
		this.foiRecebido = foiRecebido;
		this.valor = valor;
//		this.quantidadeDePontos = quantidadeDePontos;
		this.valorComDesconto = valorComDesconto;
		this.dataDeVencimento = dataDeVencimento;
		this.dataDePagamento = dataDePagamento;
		this.condutor = condutor;
		this.dataLimiteApresentacao = dataLimiteApresentacao;
		this.memorando = memorando;
		this.dataDoProcesso = dataDoProcesso;
		this.numeroDoProcesso = numeroDoProcesso;
	}

	public static List<AutoDeInfracao> buscarAutosDeInfracaoPorVeiculo(Veiculo veiculo) {
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao.AR.find("veiculo", veiculo).fetch();
		Collections.sort(autosDeInfracao, Collections.reverseOrder());
		return autosDeInfracao;
	}

	public static List<AutoDeInfracao> buscarAutosDeInfracaoPorCondutor(Condutor condutor) {
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao.AR.find("condutor", condutor).fetch();
		Collections.sort(autosDeInfracao, Collections.reverseOrder());
		return autosDeInfracao;
	}

	@Override
	public int compareTo(AutoDeInfracao o) {
		return this.dataHora.compareTo(o.dataHora);
	}

	public boolean dataPosteriorDataCorrente(Calendar dataDePagamento) {
		return dataDePagamento.after(Calendar.getInstance());
	}

	@SuppressWarnings("unchecked")
	public static List<AutoDeInfracao> listarOrdenado() {
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao.AR.findAll();
		Collections.sort(autosDeInfracao, Collections.reverseOrder());
		return autosDeInfracao;
	}
}