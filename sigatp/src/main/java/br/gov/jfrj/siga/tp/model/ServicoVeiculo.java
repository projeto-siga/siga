package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.util.Reflexao;
import br.gov.jfrj.siga.tp.validation.annotation.Data;
import br.gov.jfrj.siga.tp.validation.annotation.Sequence;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.uteis.SequenceMethods;
import br.gov.jfrj.siga.uteis.SiglaDocumentoType;

@Entity
@Audited
@Table(name = "servicoveiculo", schema = "sigatp")
public class ServicoVeiculo extends TpModel implements Comparable<ServicoVeiculo>, SequenceMethods, ConvertableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final ActiveRecord<ServicoVeiculo> AR = new ActiveRecord<>(ServicoVeiculo.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence")
	private Long id;

	@Sequence(propertieOrgao="cpOrgaoUsuario",siglaDocumento=SiglaDocumentoType.STP)
	@Column(updatable = false)
	private Long numero;

 	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
 	private CpOrgaoUsuario cpOrgaoUsuario;

 	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_PESSOA")
 	private DpPessoa executor;

 	@NotNull
 	@Data(descricaoCampo = "Data/Hora")
	private Calendar dataHora;

 	//@Data(descricaoCampo = "ultimaAlteracao")
	private Calendar ultimaAlteracao;

 	@ManyToOne
	@NotNull
	@JoinColumn(name = "VEICULO_ID")
	private Veiculo veiculo;

 	@NotNull
 	@Data(descricaoCampo = "Data/Hora In\u00EDcio Previsto")
	private Calendar dataHoraInicioPrevisto;

 	@NotNull
 	@Data(descricaoCampo = "Data/Hora Fim Previsto")
	private Calendar dataHoraFimPrevisto;

 	@UpperCase
 	@NotNull
	private String descricao;

 	@NotNull
	@Enumerated(EnumType.STRING)
	private TiposDeServico tiposDeServico;

 	@NotNull
	@Enumerated(EnumType.STRING)
	private EstadoServico situacaoServico;

	@OneToOne(targetEntity = RequisicaoTransporte.class)
	private RequisicaoTransporte requisicaoTransporte;

	@Data(descricaoCampo = "Data/Hora In\u00EDcio")
	private Calendar dataHoraInicio;

	@Data(descricaoCampo = "Data/Hora Fim")
	private Calendar dataHoraFim;

	private String motivoCancelamento;
	
	@Transient
	private String estadoServicoInicial;

	public ServicoVeiculo() {
		this.id = new Long(0);
		this.numero = new Long(0);
		this.dataHora = Calendar.getInstance();
		this.situacaoServico = EstadoServico.AGENDADO;
		this.tiposDeServico = TiposDeServico.VISTORIA;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

	public DpPessoa getExecutor() {
		return executor;
	}

	public void setExecutor(DpPessoa executor) {
		this.executor = executor;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public Calendar getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(Calendar ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Calendar getDataHoraInicioPrevisto() {
		return dataHoraInicioPrevisto;
	}

	public void setDataHoraInicioPrevisto(Calendar dataHoraInicioPrevisto) {
		this.dataHoraInicioPrevisto = dataHoraInicioPrevisto;
	}

	public Calendar getDataHoraFimPrevisto() {
		return dataHoraFimPrevisto;
	}

	public void setDataHoraFimPrevisto(Calendar dataHoraFimPrevisto) {
		this.dataHoraFimPrevisto = dataHoraFimPrevisto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TiposDeServico getTiposDeServico() {
		return tiposDeServico;
	}

	public void setTiposDeServico(TiposDeServico tiposDeServico) {
		this.tiposDeServico = tiposDeServico;
	}

	public EstadoServico getSituacaoServico() {
		return situacaoServico;
	}

	public void setSituacaoServico(EstadoServico situacaoServico) {
		this.situacaoServico = situacaoServico;
	}

	public RequisicaoTransporte getRequisicaoTransporte() {
		return requisicaoTransporte;
	}

	public void setRequisicaoTransporte(RequisicaoTransporte requisicaoTransporte) {
		this.requisicaoTransporte = requisicaoTransporte;
	}

	public Calendar getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Calendar dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public Calendar getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(Calendar dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public String getDadosParaExibicao() {
		return this.numero.toString();
	}

	public String getEstadoServicoInicial() {
		return this.estadoServicoInicial;
	}
	
	public void setEstadoServicoInicial(String estadoServicoInicial) {
		this.estadoServicoInicial = estadoServicoInicial;
	}

	@Override
	public int compareTo(ServicoVeiculo o) {
		// ordenacao decrescente
		if (this.dataHoraInicioPrevisto.before(o.dataHoraInicioPrevisto)) {
	        return -1 * -1 ;
	    }
	    if (this.dataHoraInicioPrevisto.after(o.dataHoraInicioPrevisto)) {
	        return 1 * -1;
	    }
	    return 0;
	}

	@Override
	public String getSequence() {
		if (this.numero != 0) {
			return cpOrgaoUsuario.getAcronimoOrgaoUsu().replace("-","").toString() +  "-" +
					   Reflexao.recuperaAnotacaoField(this).substring(1) + "-" +
					   String.format("%04d",this.dataHora.get(Calendar.YEAR)) + "/" +
					   String.format("%05d", numero) + "-" +
					   Reflexao.recuperaAnotacaoField(this).substring(0,1);
		} else
		{
			return "";
		}
	}

	public void setSequence(Object cpOrgaoUsuarioObject) {
		CpOrgaoUsuario cpOrgaoUsuario = (CpOrgaoUsuario) cpOrgaoUsuarioObject;
		int year = Calendar.getInstance().get(Calendar.YEAR);

		StringBuilder query = new StringBuilder();
		query.append("SELECT sv FROM ServicoVeiculo sv WHERE sv.id = ");
		query.append("(SELECT MAX(sv.id) FROM ServicoVeiculo sv WHERE sv.numero = ");
		query.append("(SELECT MAX(t.numero) FROM ServicoVeiculo t");
		query.append(" WHERE cpOrgaoUsuario.id = " + cpOrgaoUsuario.getId());
		query.append(" AND YEAR(dataHora) = " + year);
		query.append(" AND sv.cpOrgaoUsuario.id = t.cpOrgaoUsuario.id");
		query.append(" AND YEAR(sv.dataHora) = YEAR(t.dataHora)))");

		Query qry = AR.em().createQuery(query.toString());
		try {
			Object obj = qry.getSingleResult();
			this.numero = ((ServicoVeiculo) obj).numero + 1;
		} catch(NoResultException ex) {
			this.numero = new Long(1);
		}
	}

	public static List<ServicoVeiculo> buscarEmAndamento() {
		return ServicoVeiculo.AR.find(FormatarDataHora.recuperaFuncaoTrunc() +"(dataHoraFim) = " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + CpDao.getInstance().consultarDataEHoraDoServidor() + ")").fetch();
	}

	public static ServicoVeiculo buscar(String sequence) throws Exception {
		String[] partesDoCodigo=null;
		ServicoVeiculo servico = new ServicoVeiculo();

		try {
			 partesDoCodigo = sequence.split("[-/]");

		} catch (Exception e) {
			//throw new Exception(new I18nMessage("sequence", "servicoVeiculo.sequence.exception", sequence).getMessage());
			throw new Exception(MessagesBundle.getMessage("servicoVeiculo.sequence.exception", sequence));
		}

		CpOrgaoUsuario cpOrgaoUsuario = CpOrgaoUsuario.AR.find("acronimoOrgaoUsu",partesDoCodigo[0]).first();
		Integer ano = new Integer(Integer.parseInt(partesDoCodigo[2]));
		Long numero = new Long(Integer.parseInt(partesDoCodigo[3]));

		String siglaDocumento = partesDoCodigo[4] + partesDoCodigo[1];
		if (! Reflexao.recuperaAnotacaoField(servico).equals(siglaDocumento)) {
			//throw new Exception(new I18nMessage("siglaDocumento", "servicoVeiculo.siglaDocumento.exception", sequence).getMessage());
			throw new Exception(MessagesBundle.getMessage("servicoVeiculo.siglaDocumento.exception", sequence));
		}

		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoUsuario",cpOrgaoUsuario);
		parametros.put("numero",numero);
		parametros.put("ano",ano);		
		List<ServicoVeiculo> servicos =  ServicoVeiculo.AR.find("cpOrgaoUsuario = :cpOrgaoUsuario and numero = :numero and YEAR(dataHora) = :ano" ,
										 parametros).fetch();

		if (servicos.size() > 1) { 
			//throw new Exception(new I18nMessage("codigoDuplicado", "servicoVeiculo.codigoDuplicado.exception", sequence).getMessage());
			throw new Exception(MessagesBundle.getMessage("servicoVeiculo.codigoDuplicado.exception", sequence));
		}
		
		if (servicos.size() == 0 ) { 
			//throw new Exception(new I18nMessage("codigoInvalido", "servicoVeiculo.codigoInvalido.exception", sequence).getMessage());
			throw new Exception(MessagesBundle.getMessage("servicoVeiculo.codigoInvalido.exception", sequence));
		}
		
	 	return servicos.get(0);
	}

	public boolean ordemDeDatasPrevistas(Calendar dtInicio, Calendar dtFim){
		return dtInicio.before(dtFim);
	}

	@SuppressWarnings("unchecked")
	public static List<ServicoVeiculo> buscarPorVeiculo(Long idVeiculo, String dataHoraInicio) {
		String filtroVeiculo = "";
		if (idVeiculo != null) {
			filtroVeiculo = "veiculo.id = " + idVeiculo + " AND ";
		}

		//String dataFormatadaOracle = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
		String dataFormatadaOracle = dataHoraInicio;
		List<ServicoVeiculo> servicosVeiculo;

		String qrl = 	"SELECT s FROM ServicoVeiculo s WHERE " + filtroVeiculo +
					    "  situacaoServico NOT IN ('" + EstadoServico.CANCELADO + "','" + EstadoServico.REALIZADO + "')" +
						" AND " + FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraInicio) <= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + ")" +
						" AND (dataHoraFim IS NULL OR " + FormatarDataHora.recuperaFuncaoTrunc() + "(dataHoraFim) >= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + "))";

		Query qry = AR.em().createQuery(qrl);
		try {
			servicosVeiculo = (List<ServicoVeiculo>) qry.getResultList();
		} catch(NoResultException ex) {
			servicosVeiculo = null;
		}
		return servicosVeiculo;
	}
}