package br.gov.jfrj.siga.tp.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Data;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "abastecimento", schema = "sigatp")
public class Abastecimento extends TpModel implements Comparable<Abastecimento>, ConvertableEntity {

	public static final ActiveRecord<Abastecimento> AR = new ActiveRecord<>(Abastecimento.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence")
	private Long id;

	@NotNull
	@Data(descricaoCampo="dataHora")
	private Calendar dataHora;

	@ManyToOne
	@NotNull
	private Fornecedor fornecedor;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoDeCombustivel tipoDeCombustivel;

	@NotNull
	@Min(value=1, message="{abastecimento.quantidadeEmLitros.min}")
	private Double quantidadeEmLitros;

	@NotNull
	private Double precoPorLitro;

	@NotNull
	private Double valorTotalDaNotaFiscal;

	@NotNull
	private String numeroDaNotaFiscal;

	@ManyToOne
	@NotNull
	private Veiculo veiculo;

	@ManyToOne
	@NotNull
	private Condutor condutor;

	@Enumerated(EnumType.STRING)
	private NivelDeCombustivel nivelDeCombustivel;

	@NotNull
	private Double odometroEmKm;

	@NotNull
	private Double distanciaPercorridaEmKm;

	@NotNull
	private Double consumoMedioEmKmPorLitro;

 	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITANTE")
	private DpPessoa solicitante;

 	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_TITULAR")
	private DpPessoa titular;

 	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgao;

 	public Abastecimento(){
        this.id = 0L;
        this.tipoDeCombustivel = TipoDeCombustivel.GASOLINA;
        this.nivelDeCombustivel = NivelDeCombustivel.I;
        this.distanciaPercorridaEmKm = getDistanciaPercorridaEmKm();
        this.consumoMedioEmKmPorLitro = getConsumoMedioEmKmPorLitro();
    }

 	@Override
	public Long getId() {
		return id;
	}
 	
 	
	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoDeCombustivel getTipoDeCombustivel() {
		return tipoDeCombustivel;
	}

	public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) {
		this.tipoDeCombustivel = tipoDeCombustivel;
	}

	public Double getQuantidadeEmLitros() {
		return quantidadeEmLitros;
	}

	public void setQuantidadeEmLitros(Double quantidadeEmLitros) {
		this.quantidadeEmLitros = quantidadeEmLitros;
	}

	public Double getPrecoPorLitro() {
		return precoPorLitro;
	}

	public void setPrecoPorLitro(Double precoPorLitro) {
		this.precoPorLitro = precoPorLitro;
	}

	public Double getValorTotalDaNotaFiscal() {
		return valorTotalDaNotaFiscal;
	}

	public void setValorTotalDaNotaFiscal(Double valorTotalDaNotaFiscal) {
		this.valorTotalDaNotaFiscal = valorTotalDaNotaFiscal;
	}

	public String getNumeroDaNotaFiscal() {
		return numeroDaNotaFiscal;
	}

	public void setNumeroDaNotaFiscal(String numeroDaNotaFiscal) {
		this.numeroDaNotaFiscal = numeroDaNotaFiscal;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Condutor getCondutor() {
		return condutor;
	}

	public void setCondutor(Condutor condutor) {
		this.condutor = condutor;
	}

	public NivelDeCombustivel getNivelDeCombustivel() {
		return nivelDeCombustivel;
	}

	public void setNivelDeCombustivel(NivelDeCombustivel nivelDeCombustivel) {
		this.nivelDeCombustivel = nivelDeCombustivel;
	}

	public Double getOdometroEmKm() {
		return odometroEmKm;
	}

	public void setOdometroEmKm(Double odometroEmKm) {
		this.odometroEmKm = odometroEmKm;
	}

	public Double getDistanciaPercorridaEmKm() {
		return null != distanciaPercorridaEmKm ? distanciaPercorridaEmKm : 0.0;
	}

	public void setDistanciaPercorridaEmKm(Double distanciaPercorridaEmKm) {
		this.distanciaPercorridaEmKm = distanciaPercorridaEmKm;
	}

	public Double getConsumoMedioEmKmPorLitro() {
		return null != consumoMedioEmKmPorLitro ? consumoMedioEmKmPorLitro : 0.0;
	}

	public void setConsumoMedioEmKmPorLitro(Double consumoMedioEmKmPorLitro) {
		this.consumoMedioEmKmPorLitro = consumoMedioEmKmPorLitro;
	}

	public DpPessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(DpPessoa solicitante) {
		this.solicitante = solicitante;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public CpOrgaoUsuario getOrgao() {
		return orgao;
	}

	public void setOrgao(CpOrgaoUsuario orgao) {
		this.orgao = orgao;
	}

	@SuppressWarnings("unchecked")
	public static List<Abastecimento> listarTodos() {
		List<Abastecimento> abastecimentos = Abastecimento.AR.findAll();
		Collections.sort(abastecimentos, Collections.reverseOrder());
		return abastecimentos;
	}

	public static List<Abastecimento> buscarTodosPorVeiculo(Veiculo veiculo){
		List<Abastecimento> abastecimentos = Abastecimento.AR.find("veiculo", veiculo).fetch();
		Collections.sort(abastecimentos, Collections.reverseOrder());
		return abastecimentos;
	}

	public static List<Abastecimento> buscarTodosPorCondutor(Condutor condutor){
		List<Abastecimento> abastecimentos = Abastecimento.AR.find("condutor", condutor).fetch();
		Collections.sort(abastecimentos, Collections.reverseOrder());
		return abastecimentos;
	}

	public static List<Abastecimento> buscarTodosPorTipoDeCombustivel(TipoDeCombustivel tipo){
		List<Abastecimento> abastecimentos = Abastecimento.AR.find("tipoDeCombustivel", tipo).fetch();
		Collections.sort(abastecimentos, Collections.reverseOrder());
		return abastecimentos;
	}

	@Override
	public int compareTo(Abastecimento o) {
        return this.dataHora.compareTo(o.dataHora);
	}

	public static List<Abastecimento> listarAbastecimentosDoCondutor(Condutor condutor) {
		List<Abastecimento> abastecimentos = Abastecimento.AR.find("titular.idPessoaIni = ?", condutor.getDpPessoa().getIdInicial()).fetch();
		Collections.sort(abastecimentos, Collections.reverseOrder());
		return abastecimentos;
	}

	@SuppressWarnings("unchecked")
	public static List<Abastecimento> listarParaAdminGabinete(DpPessoa admin) {
		List<Abastecimento> retorno;
		String query = "select a from Abastecimento a "
				+ "where a.titular.idPessoa IS NOT NULL and a.titular.idPessoa in "
				+ "("
				+ "select t.idPessoa from DpPessoa t "
				+ "where (t.idPessoaIni = " + admin.getIdInicial() + " or "
				+ "t.lotacao.idLotacaoIni = " + admin.getLotacao().getIdInicial()
				+ ") and t.dataFimPessoa IS NULL)";

		Query qry = AR.em().createQuery(query);
		retorno = ((List<Abastecimento>) qry.getResultList());

		Collections.sort(retorno, Collections.reverseOrder());
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public static List<Abastecimento> listarParaAgente(DpPessoa agente) {
		List<Abastecimento> retorno;
		String query = "select a from Abastecimento a "
					+ "where orgao.id = " + agente.getOrgaoUsuario().getId() + " "
					+ "and a.titular.idPessoa IS NULL or a.titular.idPessoa in "
					+ "("
					+ "select t.idPessoa from DpPessoa t "
					+ "where (t.idPessoaIni = " + agente.getIdInicial() + " or "
					+ "t.lotacao.idLotacaoIni = " + agente.getLotacao().getIdInicial()
					+ ") and t.dataFimPessoa IS NULL)";

		Query qry = AR.em().createQuery(query);
		retorno = ((List<Abastecimento>) qry.getResultList());

		Collections.sort(retorno, Collections.reverseOrder());
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public static List<Abastecimento> listarTodos(DpPessoa admin) {
		List<Abastecimento> retorno;
		String query = "select a from Abastecimento a "
					+ "where orgao.id = " + admin.getOrgaoUsuario().getId();

		Query qry = AR.em().createQuery(query);
		retorno = ((List<Abastecimento>) qry.getResultList());

		Collections.sort(retorno, Collections.reverseOrder());
		return retorno;
	}

	public String getDadosParaExibicao() {
		SimpleDateFormat dataFormatada = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return dataFormatada.format(this.dataHora.getTime()) + " - " + this.fornecedor.getRazaoSocial();
	}

	public String formataValorExponencialParaDecimal(Double number) {
        return BigDecimal.valueOf((Double) number).toPlainString();
    }

    public String formataMoedaBrasileiraSemSimbolo(Double number) {
        Locale defaultLocale = new Locale("pt", "BR", "BRL");
        NumberFormat nf = NumberFormat.getCurrencyInstance(defaultLocale);
        return nf.format(number).replace("R$", "").trim();
    }

	public static List<Abastecimento> buscarAbastecimentosAvancado(Condutor condutor, Veiculo veiculo, TipoDeCombustivel combustivel, Fornecedor fornecedor) {
		if (condutor == null && veiculo == null && fornecedor == null & combustivel == null) { 
			return new ArrayList<Abastecimento>(); 
		}
		
    	String qrl = (condutor != null) ? "condutor.id = " + condutor.getId() : "";
        qrl += (!qrl.equals("") && veiculo!= null) ? " AND" : "";
	    qrl += (veiculo != null) ? " veiculo.id = " + veiculo.getId() : "";
        qrl += (!qrl.equals("") && fornecedor!= null) ? " AND" : "";
	    qrl += (fornecedor != null) ? " fornecedor.id = " + fornecedor.getId() : "";
        qrl += (!qrl.equals("") && combustivel!= null) ? " AND" : "";
	    qrl += (combustivel != null) ? " tipodecombustivel = '" + combustivel + "'" : "";
        qrl += " order by dataHora desc";
        return Abastecimento.AR.find(qrl).fetch();
	}
}