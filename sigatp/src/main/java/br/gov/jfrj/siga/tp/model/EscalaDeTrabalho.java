package br.gov.jfrj.siga.tp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;

@Entity
@Audited
@Table(name = "escaladetrabalho", schema = "sigatp")
public class EscalaDeTrabalho extends TpModel implements ConvertableEntity  {

	private static final long serialVersionUID = 1L;
	public static final ActiveRecord<EscalaDeTrabalho> AR = new ActiveRecord<>(EscalaDeTrabalho.class);

	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	private Long id;

	@NotNull
	private Calendar dataVigenciaInicio;

	private Calendar dataVigenciaFim;

	@ManyToOne
	@NotNull
	private Condutor condutor;

	@NotNull(message = "Inclua ao menos um dia de trabalho na escala")
	@OneToMany(mappedBy = "escalaDeTrabalho")
	private List<DiaDeTrabalho> diasDeTrabalho;

	public EscalaDeTrabalho() {
		this.id = 0L;
		this.diasDeTrabalho = new ArrayList<DiaDeTrabalho>();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDataVigenciaInicio() {
		return dataVigenciaInicio;
	}

	public void setDataVigenciaInicio(Calendar dataVigenciaInicio) {
		this.dataVigenciaInicio = dataVigenciaInicio;
	}

	public Calendar getDataVigenciaFim() {
		return dataVigenciaFim;
	}

	public void setDataVigenciaFim(Calendar dataVigenciaFim) {
		this.dataVigenciaFim = dataVigenciaFim;
	}

	public Condutor getCondutor() {
		return condutor;
	}

	public void setCondutor(Condutor condutor) {
		this.condutor = condutor;
	}

	public List<DiaDeTrabalho> getDiasDeTrabalho() {
		return diasDeTrabalho;
	}

	public void setDiasDeTrabalho(List<DiaDeTrabalho> diasDeTrabalho) {
		this.diasDeTrabalho = diasDeTrabalho;
	}

	public void iniciarVigencia() {
		this.dataVigenciaInicio = Calendar.getInstance();
	}

	public void encerrarVigencia() {
		this.dataVigenciaFim = Calendar.getInstance();
	}

	public static List<EscalaDeTrabalho> buscarTodosPorCondutor(Condutor condutor) {
		HashMap<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("condutor", condutor);
		return EscalaDeTrabalho.AR.find("condutor = :condutor ORDER BY dataVigenciaInicio DESC, dataVigenciaFim DESC", parametros).fetch();		
	}

	public static List<EscalaDeTrabalho> buscarTodasVigentes() {
		StringBuffer hqlVigentes = new StringBuffer();
		hqlVigentes.append("dataVigenciaInicio < current_date ");
		hqlVigentes.append("and ((dataVigenciaFim is null) or (dataVigenciaFim > current_date)) ");
		hqlVigentes.append("order by dataVigenciaInicio ");
		return EscalaDeTrabalho.AR.find(hqlVigentes.toString()).<EscalaDeTrabalho> fetch();
	}

	public String getEscalaParaExibicao() {
		StringBuffer saida = new StringBuffer();
		boolean primeiro = true;

		if (diasDeTrabalho != null) {
			Collections.sort(diasDeTrabalho);
			for (Iterator<DiaDeTrabalho> iterator = diasDeTrabalho.iterator(); iterator.hasNext();) {
				if (primeiro) {
					primeiro = false;
				} else {
					saida.append("; ");
				}
				DiaDeTrabalho dia = iterator.next();
				saida.append(dia.toString());
			}
		}

		return saida.toString();
	}

	public Boolean estaEscaladoNesteDia(String data) throws ParseException {
		for (DiaDeTrabalho diaDeTrabalho : diasDeTrabalho) {
			Calendar horaSaidaMissao = Calendar.getInstance();

			SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			horaSaidaMissao.setTime(formatar.parse(data));
			SimpleDateFormat formatarHora = new SimpleDateFormat("HHmm");
			String strHoraSaidaMissao = formatarHora.format(horaSaidaMissao.getTime());

			String dataSaidaMissao = String.valueOf(DiaDaSemana.getDiaDaSemana(data).getOrdem()) + strHoraSaidaMissao;

			if (Integer.parseInt(dataSaidaMissao) >= Integer.parseInt(diaDeTrabalho.getDiaEntrada().getOrdem() + diaDeTrabalho.getHoraEntradaFormatada("HHmm"))
					&& Integer.parseInt(dataSaidaMissao) <= Integer.parseInt(diaDeTrabalho.getDiaSaida().getOrdem() + diaDeTrabalho.getHoraSaidaFormatada("HHmm"))) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<EscalaDeTrabalho> buscarPorCondutores(Long idCondutor, String dataHoraInicio) {

		String filtroCondutor = "";
		if (idCondutor != null) {
			filtroCondutor = "condutor.id = " + idCondutor + " AND ";
		}

//		String dataFormatadaOracle = "to_date('" + dataHoraInicio + "', 'DD/MM/YYYY')";
		String dataFormatadaOracle = dataHoraInicio;
		List<EscalaDeTrabalho> escalas;

		String qrl = "SELECT e FROM EscalaDeTrabalho e WHERE " + filtroCondutor + FormatarDataHora.recuperaFuncaoTrunc() + "(dataVigenciaInicio) <= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + ")"
				+ " AND (dataVigenciaFim IS NULL OR " + FormatarDataHora.recuperaFuncaoTrunc() + "(dataVigenciaFim) >= " + FormatarDataHora.recuperaFuncaoTrunc() + "(" + dataFormatadaOracle + "))";

		Query qry = AR.em().createQuery(qrl);
		try {
			escalas = (List<EscalaDeTrabalho>) qry.getResultList();
		} catch (NoResultException ex) {
			escalas = null;
		}
		return escalas;
	}
	
					
	
	
	
	
	
}
