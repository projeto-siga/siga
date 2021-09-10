package br.gov.jfrj.siga.tp.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

@Entity
@Audited
@Table(name = "penalidade", schema = "sigatp")
@Unique(message="{penalidade.codigoInfracao.unique}", field="codigoInfracao")
public class Penalidade extends TpModel implements ConvertableEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final long _ID_DA_PENALIDADE_OUTRA = -1;

	public static final ActiveRecord<Penalidade> AR = new ActiveRecord<>(Penalidade.class);

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@UpperCase
	private String codigoInfracao;

	@NotNull
	@UpperCase
	private String descricaoInfracao;

	@NotNull
	@UpperCase
	private String artigoCTB;

	@NotNull
	private Double valor;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Infrator infrator;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Gravidade classificacao;


	public Penalidade() {
		this.setId(new Long(0));
		this.setInfrator(Infrator.CONDUTOR);
		this.setClassificacao(Gravidade.LEVE);
	}

	@SuppressWarnings("unchecked")
	public static List<Penalidade> listarTodos() {
		return Penalidade.AR.findAll();
	}

	public static List<Penalidade> listarTodos(CpOrgaoUsuario orgaoUsuario) {
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoOrigem", orgaoUsuario);
		parametros.put("id", _ID_DA_PENALIDADE_OUTRA);		
		return Penalidade.AR.find("cpOrgaoOrigem = :cpOrgaoOrigem and id <> :id", orgaoUsuario, _ID_DA_PENALIDADE_OUTRA).fetch();
	}

	public static Penalidade buscar(Long idBuscar) {
		Penalidade retorno = null;
		try {
			retorno = Penalidade.AR.find("id = :id", Collections.singletonMap("id", idBuscar)).first();
		} catch (Exception e) {
			return null;
		}
		return retorno;
	}

	@Override
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoInfracao() {
		return codigoInfracao;
	}

	public void setCodigoInfracao(String codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}

	public String getDescricaoInfracao() {
		return descricaoInfracao;
	}

	public void setDescricaoInfracao(String descricaoInfracao) {
		this.descricaoInfracao = descricaoInfracao;
	}

	public String getArtigoCTB() {
		return artigoCTB;
	}

	public void setArtigoCTB(String artigoCTB) {
		this.artigoCTB = artigoCTB;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Infrator getInfrator() {
		return infrator;
	}

	public void setInfrator(Infrator infrator) {
		this.infrator = infrator;
	}

	public Gravidade getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Gravidade classificacao) {
		this.classificacao = classificacao;
	}

	public String getDadosParaExibicao() {
		return this.codigoInfracao + " - " + this.descricaoInfracao;
	}
}
