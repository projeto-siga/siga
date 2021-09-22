package br.gov.jfrj.siga.tp.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.util.Situacao;
import br.gov.jfrj.siga.tp.validation.annotation.Chassi;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.Renavam;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

@Entity
@Audited
@Table(name = "veiculo", schema = "sigatp")
@Unique(message = "{veiculo.placa.unique}", field = "placa")
public class Veiculo extends TpModel implements ConvertableEntity, Comparable<Veiculo> {

	private static final long serialVersionUID = -3602265045747814797L;
	public static final ActiveRecord<Veiculo> AR = new ActiveRecord<>(Veiculo.class);

	@Id
    @GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@NotNull
	@Size(max = 8, message = "{veiculo.placa.maxSize}")
	@UpperCase
	private String placa;

	@NotNull
	@ManyToOne
	private Grupo grupo;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario cpOrgaoUsuario;

	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	@NotNull
	@UpperCase
	@Size(max = 11, message = "{veiculo.patrimonio.maxSize}")
	private String patrimonio;

	@OneToMany(orphanRemoval = true, mappedBy = "veiculo")
	// TODO: Verificar erro de Cast no token da Linha abaixo
	// @OrderBy("dataHoraInicio DESC")
	private List<LotacaoVeiculo> lotacoes;

	@Transient
	private Double odometroEmKmAtual;

	@Enumerated(EnumType.STRING)
	private PerguntaSimNao usoComum;

	@Max(value = 9999, message = "{veiculo.anoFabricacao.maxSize}")
	@Min(value = 1000, message = "{veiculo.anoFabricacao.minSize}")
	private int anoFabricacao;

	@Max(value = 9999, message = "{veiculo.anoModelo.maxSize}")
	@Min(value = 1000, message = "{veiculo.anoModelo.minSize}")
	private int anoModelo;

	@NotNull
	@UpperCase
	private String marca;

	@NotNull
	@UpperCase
	private String modelo;

	@Enumerated(EnumType.STRING)
	private TipoDeCombustivel tipoDeCombustivel;

	@ManyToOne
	private Cor cor;

	@UpperCase
	private String motor;

	@UpperCase
	private String potencia;

	@UpperCase
	private String direcao;

	@UpperCase
	private String transmissao;

	@UpperCase
	private String tipoDeBlindagem;

	private String tanque;

	private String pneuMedida;

	private String pneuPressaoDianteira;

	private String pneuPressaoTraseira;

	@NotNull
	@Renavam
	private String renavam;

	@NotNull
	@Chassi
	@UpperCase
	private String chassi;

	@Enumerated(EnumType.STRING)
	private PerguntaSimNao licenciamentoAnual;

	@Enumerated(EnumType.STRING)
	private PerguntaSimNao dpvat;

	@Enumerated(EnumType.STRING)
	private CategoriaCNH categoriaCNH;

	private boolean temAIRBAG;

	private boolean temGPS;

	private boolean temPILOTOAUTOMATICO;

	private boolean temCONTROLEDETRACAO;

	private boolean temSENSORDEMARCHARE;

	private boolean temABS;

	private boolean temCDPLAYER;

	private boolean temBANCOSEMCOURO;

	private boolean temRODADELIGALEVE;

	private boolean temCAMERADEMARCHARE;

	private boolean temEBD;

	private boolean temDVDPLAYER;

	private boolean temTELALCDPAPOIOCABECA;

	private boolean temFREIOADISCONASQUATRORODAS;

	private boolean temARCONDICIONADO;

	private boolean temOUTROS;

	@UpperCase
	private String outros;

	@Data(descricaoCampo = "Data de Aquisicao")
	private Calendar dataAquisicao;

	private Double valorAquisicao;

	@Data(descricaoCampo = "Data de Garantia")
	private Calendar dataGarantia;

	@ManyToOne
	private Fornecedor fornecedor;

	private String numeroCartaoAbastecimento;

	@Data(descricaoCampo = "Validade do Cartao de Abastecimento")
	private Calendar validadeCartaoAbastecimento;

	private String numeroCartaoSeguro;

	@Data(intervalo = 10, descricaoCampo = "Validade do Cartao de Seguro")
	private Calendar validadeCartaoSeguro;

	@Data(descricaoCampo = "Data de Alienacao")
	private Calendar dataAlienacao;

	@UpperCase
	private String termoAlienacao;

	@UpperCase
	private String processoAlienacao;

	@OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
	private List<AutoDeInfracao> autosDeInfracao;

	@OneToMany(mappedBy = "veiculo", orphanRemoval = true)
	private List<Avaria> avarias;

	@OneToMany(mappedBy = "veiculo", orphanRemoval = true)
	private List<Abastecimento> abastecimentos;

	@OneToMany(mappedBy = "veiculo", orphanRemoval = true)
	private List<RelatorioDiario> relatoriosdiarios;

	public Veiculo() {
		this.id = new Long(0);
		this.grupo = null;
		this.placa = null;
		this.situacao = Situacao.Ativo;
		this.patrimonio = "";
		this.lotacoes = null;
		this.usoComum = PerguntaSimNao.NAO;
		this.anoFabricacao = 2013;
		this.anoModelo = 2013;
		this.marca = "";
		this.modelo = "";
		this.tipoDeCombustivel = TipoDeCombustivel.GASOLINA;
		this.cor = null;
		this.motor = "";
		this.potencia = "";
		this.direcao = "";
		this.transmissao = "";
		this.tipoDeBlindagem = "";
		this.tanque = "";
		this.pneuMedida = "";
		this.pneuPressaoDianteira = "";
		this.pneuPressaoTraseira = "";
		this.renavam = "";
		this.chassi = "";
		this.licenciamentoAnual = PerguntaSimNao.NAO;
		this.dpvat = PerguntaSimNao.NAO;
		this.categoriaCNH = CategoriaCNH.D;
		this.temAIRBAG = false;
		this.temGPS = false;
		this.temPILOTOAUTOMATICO = false;
		this.temCONTROLEDETRACAO = false;
		this.temSENSORDEMARCHARE = false;
		this.temABS = false;
		this.temCDPLAYER = false;
		this.temBANCOSEMCOURO = false;
		this.temRODADELIGALEVE = false;
		this.temCAMERADEMARCHARE = false;
		this.temEBD = false;
		this.temDVDPLAYER = false;
		this.temTELALCDPAPOIOCABECA = false;
		this.temFREIOADISCONASQUATRORODAS = false;
		this.temARCONDICIONADO = false;
		this.temOUTROS = false;
		this.outros = "";
		this.dataAquisicao = null;
		this.valorAquisicao = 0.00;
		this.dataGarantia = null;
		this.numeroCartaoAbastecimento = "";
		this.validadeCartaoAbastecimento = null;
		this.numeroCartaoSeguro = "";
		this.validadeCartaoSeguro = null;
		this.dataAlienacao = null;
		this.termoAlienacao = "";
		this.processoAlienacao = "";
	}

	public String getDadosParaExibicao() {
		if (ehNovo()) {
			return MessagesBundle.getMessage("veiculo.cadastro");
		}
		return this.marca + " " + this.modelo + " - " + this.placa;
	}

	@Override
	public int compareTo(Veiculo o) {
		return (this.situacao + this.marca + this.modelo).compareTo(o.situacao + o.marca + o.modelo);
	}

	public Double getUltimoOdometroDeLotacao() {
		Double retorno = (double) 0;

		if (lotacoes != null && !lotacoes.isEmpty() && lotacoes.get(0).getOdometroEmKm() != null) {
			Collections.sort(lotacoes, LotacaoVeiculo.comparator());
			retorno = lotacoes.get(0).getOdometroEmKm();
		}

		return retorno;
	}

	public DpLotacao getDpLotacaoVigente() {
		DpLotacao retorno = null;

		if (lotacoes != null && !lotacoes.isEmpty()) {
			Collections.sort(lotacoes, LotacaoVeiculo.comparator());
			retorno = lotacoes.get(0).getLotacao();
		}
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public static List<Veiculo> listarDisponiveis(String dataSaida, Long idMissao, Long idOrgao) {
		List<Veiculo> veiculos;
		String dataFormatadaOracle = FormatarDataHora.retorna_DataeHora(dataSaida);
		String qrl = "SELECT v FROM Veiculo v where " + " v.situacao = '" + Situacao.Ativo.toString() + "' " + " AND v.cpOrgaoUsuario.id in  " + "(SELECT cp.id FROM CpOrgaoUsuario cp"
				+ " WHERE  cp.id = " + idOrgao + ")" + " AND v.id not in " + "(SELECT s.veiculo.id FROM ServicoVeiculo s" + " WHERE  s.veiculo.id = v.id" + " AND   s.dataHoraInicio <= "
				+ dataFormatadaOracle + " AND    (s.dataHoraFim = NULL " + " OR    s.dataHoraFim >= " + dataFormatadaOracle + "))" + " AND   v.id not in" + "(SELECT m.veiculo.id FROM Missao m"
				+ " WHERE  m.veiculo.id = v.id" + " AND    m.id != " + idMissao + " AND    m.estadoMissao != '" + EstadoMissao.CANCELADA + "'" + " AND    m.estadoMissao != '"
				+ EstadoMissao.PROGRAMADA + "'" + " AND    m.estadoMissao != '" + EstadoMissao.FINALIZADA + "'" + " AND   ((m.dataHoraSaida <=  " + dataFormatadaOracle
				+ " AND    m.dataHoraRetorno = NULL)" + " OR    (m.dataHoraSaida <=  " + dataFormatadaOracle + " AND    m.dataHoraRetorno >= " + dataFormatadaOracle + ")))" + " AND v.id not in "
				+ "(SELECT a.veiculo.id FROM Avaria a" + " WHERE a.podeCircular = '" + PerguntaSimNao.NAO + "'" + " AND a.dataDeRegistro <= " + dataFormatadaOracle + " AND (a.dataDeSolucao = NULL "
				+ " OR a.dataDeSolucao >= " + dataFormatadaOracle + "))" + " ORDER BY v.marca, v.modelo";

		Query qry = AR.em().createQuery(qrl);
		try {
			veiculos = (List<Veiculo>) qry.getResultList();
		} catch (NoResultException ex) {
			veiculos = null;
		}

		return veiculos;
	}

	public static Boolean estaDisponivel(Missao m) throws Exception {
		SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String dataHoraSaidaStr = formatar.format(m.getDataHoraSaida().getTime());
		List<Veiculo> veiculos = listarDisponiveis(dataHoraSaidaStr, m.getId(), m.getCpOrgaoUsuario().getId());
		for (Veiculo veiculo : veiculos) {
			if (veiculo.id.equals(m.getVeiculo().id)) {
				return true;
			}
		}
		return false;
	}

	public static List<Veiculo> listarTodos(CpOrgaoUsuario orgaoUsuario) {
		List<Veiculo> veiculos = Veiculo.AR.find("cpOrgaoUsuario", orgaoUsuario).fetch();
		Collections.sort(veiculos);
		return veiculos;
	}

	@SuppressWarnings("unchecked")
	public static List<Veiculo> listarFiltradoPor(CpOrgaoUsuario orgaoUsuario, DpLotacao lotacao) throws Exception {

		List<Veiculo> veiculos;

		String qrl = "SELECT v FROM Veiculo v WHERE " + "  v.cpOrgaoUsuario.id = " + orgaoUsuario.getId() + " AND v.id in (SELECT L.veiculo.id FROM LotacaoVeiculo L " + " where L.veiculo.id = v.id "
				+ " AND L.lotacao.idLotacaoIni = " + lotacao.getIdLotacaoIni() + " AND L.dataHoraFim IS NULL)" + " ORDER BY v.marca, v.modelo";

		Query qry = AR.em().createQuery(qrl);
		try {
			veiculos = (List<Veiculo>) qry.getResultList();
		} catch (NoResultException ex) {
			veiculos = null;
		}
		return veiculos;
	}

	public void configurarOdometroParaMudancaDeLotacao() {
		this.odometroEmKmAtual = this.getUltimoOdometroDeLotacao();
	}

	public DpLotacao getUltimaLotacao() {
		return this.getDpLotacaoVigente();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(String patrimonio) {
		this.patrimonio = patrimonio;
	}

	public List<LotacaoVeiculo> getLotacoes() {
		if (lotacoes != null)
			Collections.sort(lotacoes, LotacaoVeiculo.comparator());
		return lotacoes;
	}

	public void setLotacoes(List<LotacaoVeiculo> lotacoes) {
		this.lotacoes = lotacoes;
	}

	public Double getOdometroEmKmAtual() {
		return odometroEmKmAtual;
	}

	public void setOdometroEmKmAtual(Double odometroEmKmAtual) {
		this.odometroEmKmAtual = odometroEmKmAtual;
	}

	public PerguntaSimNao getUsoComum() {
		return usoComum;
	}

	public void setUsoComum(PerguntaSimNao usoComum) {
		this.usoComum = usoComum;
	}

	public int getAnoFabricacao() {
		return anoFabricacao;
	}

	public void setAnoFabricacao(int anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}

	public int getAnoModelo() {
		return anoModelo;
	}

	public void setAnoModelo(int anoModelo) {
		this.anoModelo = anoModelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public TipoDeCombustivel getTipoDeCombustivel() {
		return tipoDeCombustivel;
	}

	public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) {
		this.tipoDeCombustivel = tipoDeCombustivel;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public String getPotencia() {
		return potencia;
	}

	public void setPotencia(String potencia) {
		this.potencia = potencia;
	}

	public String getDirecao() {
		return direcao;
	}

	public void setDirecao(String direcao) {
		this.direcao = direcao;
	}

	public String getTransmissao() {
		return transmissao;
	}

	public void setTransmissao(String transmissao) {
		this.transmissao = transmissao;
	}

	public String getTipoDeBlindagem() {
		return tipoDeBlindagem;
	}

	public void setTipoDeBlindagem(String tipoDeBlindagem) {
		this.tipoDeBlindagem = tipoDeBlindagem;
	}

	public String getTanque() {
		return tanque;
	}

	public void setTanque(String tanque) {
		this.tanque = tanque;
	}

	public String getPneuMedida() {
		return pneuMedida;
	}

	public void setPneuMedida(String pneuMedida) {
		this.pneuMedida = pneuMedida;
	}

	public String getPneuPressaoDianteira() {
		return pneuPressaoDianteira;
	}

	public void setPneuPressaoDianteira(String pneuPressaoDianteira) {
		this.pneuPressaoDianteira = pneuPressaoDianteira;
	}

	public String getPneuPressaoTraseira() {
		return pneuPressaoTraseira;
	}

	public void setPneuPressaoTraseira(String pneuPressaoTraseira) {
		this.pneuPressaoTraseira = pneuPressaoTraseira;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public PerguntaSimNao getLicenciamentoAnual() {
		return licenciamentoAnual;
	}

	public void setLicenciamentoAnual(PerguntaSimNao licenciamentoAnual) {
		this.licenciamentoAnual = licenciamentoAnual;
	}

	public PerguntaSimNao getDpvat() {
		return dpvat;
	}

	public void setDpvat(PerguntaSimNao dpvat) {
		this.dpvat = dpvat;
	}

	public CategoriaCNH getCategoriaCNH() {
		return categoriaCNH;
	}

	public void setCategoriaCNH(CategoriaCNH categoriaCNH) {
		this.categoriaCNH = categoriaCNH;
	}

	public boolean isTemAIRBAG() {
		return temAIRBAG;
	}

	public void setTemAIRBAG(boolean temAIRBAG) {
		this.temAIRBAG = temAIRBAG;
	}

	public boolean isTemGPS() {
		return temGPS;
	}

	public void setTemGPS(boolean temGPS) {
		this.temGPS = temGPS;
	}

	public boolean isTemPILOTOAUTOMATICO() {
		return temPILOTOAUTOMATICO;
	}

	public void setTemPILOTOAUTOMATICO(boolean temPILOTOAUTOMATICO) {
		this.temPILOTOAUTOMATICO = temPILOTOAUTOMATICO;
	}

	public boolean isTemCONTROLEDETRACAO() {
		return temCONTROLEDETRACAO;
	}

	public void setTemCONTROLEDETRACAO(boolean temCONTROLEDETRACAO) {
		this.temCONTROLEDETRACAO = temCONTROLEDETRACAO;
	}

	public boolean isTemSENSORDEMARCHARE() {
		return temSENSORDEMARCHARE;
	}

	public void setTemSENSORDEMARCHARE(boolean temSENSORDEMARCHARE) {
		this.temSENSORDEMARCHARE = temSENSORDEMARCHARE;
	}

	public boolean isTemABS() {
		return temABS;
	}

	public void setTemABS(boolean temABS) {
		this.temABS = temABS;
	}

	public boolean isTemCDPLAYER() {
		return temCDPLAYER;
	}

	public void setTemCDPLAYER(boolean temCDPLAYER) {
		this.temCDPLAYER = temCDPLAYER;
	}

	public boolean isTemBANCOSEMCOURO() {
		return temBANCOSEMCOURO;
	}

	public void setTemBANCOSEMCOURO(boolean temBANCOSEMCOURO) {
		this.temBANCOSEMCOURO = temBANCOSEMCOURO;
	}

	public boolean isTemRODADELIGALEVE() {
		return temRODADELIGALEVE;
	}

	public void setTemRODADELIGALEVE(boolean temRODADELIGALEVE) {
		this.temRODADELIGALEVE = temRODADELIGALEVE;
	}

	public boolean isTemCAMERADEMARCHARE() {
		return temCAMERADEMARCHARE;
	}

	public void setTemCAMERADEMARCHARE(boolean temCAMERADEMARCHARE) {
		this.temCAMERADEMARCHARE = temCAMERADEMARCHARE;
	}

	public boolean isTemEBD() {
		return temEBD;
	}

	public void setTemEBD(boolean temEBD) {
		this.temEBD = temEBD;
	}

	public boolean isTemDVDPLAYER() {
		return temDVDPLAYER;
	}

	public void setTemDVDPLAYER(boolean temDVDPLAYER) {
		this.temDVDPLAYER = temDVDPLAYER;
	}

	public boolean isTemTELALCDPAPOIOCABECA() {
		return temTELALCDPAPOIOCABECA;
	}

	public void setTemTELALCDPAPOIOCABECA(boolean temTELALCDPAPOIOCABECA) {
		this.temTELALCDPAPOIOCABECA = temTELALCDPAPOIOCABECA;
	}

	public boolean isTemFREIOADISCONASQUATRORODAS() {
		return temFREIOADISCONASQUATRORODAS;
	}

	public void setTemFREIOADISCONASQUATRORODAS(boolean temFREIOADISCONASQUATRORODAS) {
		this.temFREIOADISCONASQUATRORODAS = temFREIOADISCONASQUATRORODAS;
	}

	public boolean isTemARCONDICIONADO() {
		return temARCONDICIONADO;
	}

	public void setTemARCONDICIONADO(boolean temARCONDICIONADO) {
		this.temARCONDICIONADO = temARCONDICIONADO;
	}

	public boolean isTemOUTROS() {
		return temOUTROS;
	}

	public void setTemOUTROS(boolean temOUTROS) {
		this.temOUTROS = temOUTROS;
	}

	public String getOutros() {
		return outros;
	}

	public void setOutros(String outros) {
		this.outros = outros;
	}

	public Calendar getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(Calendar dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public Double getValorAquisicao() {
		return valorAquisicao;
	}

	public void setValorAquisicao(Double valorAquisicao) {
		this.valorAquisicao = valorAquisicao;
	}

	public Calendar getDataGarantia() {
		return dataGarantia;
	}

	public void setDataGarantia(Calendar dataGarantia) {
		this.dataGarantia = dataGarantia;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getNumeroCartaoAbastecimento() {
		return numeroCartaoAbastecimento;
	}

	public void setNumeroCartaoAbastecimento(String numeroCartaoAbastecimento) {
		this.numeroCartaoAbastecimento = numeroCartaoAbastecimento;
	}

	public Calendar getValidadeCartaoAbastecimento() {
		return validadeCartaoAbastecimento;
	}

	public void setValidadeCartaoAbastecimento(Calendar validadeCartaoAbastecimento) {
		this.validadeCartaoAbastecimento = validadeCartaoAbastecimento;
	}

	public String getNumeroCartaoSeguro() {
		return numeroCartaoSeguro;
	}

	public void setNumeroCartaoSeguro(String numeroCartaoSeguro) {
		this.numeroCartaoSeguro = numeroCartaoSeguro;
	}

	public Calendar getValidadeCartaoSeguro() {
		return validadeCartaoSeguro;
	}

	public void setValidadeCartaoSeguro(Calendar validadeCartaoSeguro) {
		this.validadeCartaoSeguro = validadeCartaoSeguro;
	}

	public Calendar getDataAlienacao() {
		return dataAlienacao;
	}

	public void setDataAlienacao(Calendar dataAlienacao) {
		this.dataAlienacao = dataAlienacao;
	}

	public String getTermoAlienacao() {
		return termoAlienacao;
	}

	public void setTermoAlienacao(String termoAlienacao) {
		this.termoAlienacao = termoAlienacao;
	}

	public String getProcessoAlienacao() {
		return processoAlienacao;
	}

	public void setProcessoAlienacao(String processoAlienacao) {
		this.processoAlienacao = processoAlienacao;
	}

	public List<AutoDeInfracao> getAutosDeInfracao() {
		return autosDeInfracao;
	}

	public void setAutosDeInfracao(List<AutoDeInfracao> autosDeInfracao) {
		this.autosDeInfracao = autosDeInfracao;
	}

	public List<Avaria> getAvarias() {
		return avarias;
	}

	public void setAvarias(List<Avaria> avarias) {
		this.avarias = avarias;
	}

	public List<Abastecimento> getAbastecimentos() {
		return abastecimentos;
	}

	public void setAbastecimentos(List<Abastecimento> abastecimentos) {
		this.abastecimentos = abastecimentos;
	}

	public List<RelatorioDiario> getRelatoriosdiarios() {
		return relatoriosdiarios;
	}

	public void setRelatoriosdiarios(List<RelatorioDiario> relatoriosdiarios) {
		this.relatoriosdiarios = relatoriosdiarios;
	}

	public DpLotacaoSelecao getLotacaoAtualSel() {
		DpLotacaoSelecao selecao = new DpLotacaoSelecao();
		DpLotacao dpLotacaoVigente = getDpLotacaoVigente();

		if (dpLotacaoVigente != null) {
			selecao.setId(dpLotacaoVigente.getId());
			selecao.setSigla(dpLotacaoVigente.getSigla());
			selecao.setDescricao(dpLotacaoVigente.getDescricao());
		}
		return selecao;
	}

	@SuppressWarnings("unchecked")
	public static List<Veiculo> listarParaConsumoMedio(CpOrgaoUsuario orgaoUsuario) throws Exception {
		List<Veiculo> veiculos;
		String qrl = "SELECT v FROM Veiculo v WHERE cpOrgaoUsuario.id = " + orgaoUsuario.getId() + " and " +	
					 "(select count(a) from Abastecimento a where a.veiculo = v) >= 2";
			
		Query qry = AR.em().createQuery(qrl);
		try {
			veiculos = (List<Veiculo>) qry.getResultList();
		} catch (NoResultException ex) {
			veiculos = null;
		}

		Collections.sort(veiculos);
		return veiculos;
	}
}