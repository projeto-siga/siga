package br.gov.jfrj.siga.tp.model;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "FinalidadeRequisicao", schema = "SIGATP")
@Unique(message = "{finalidadeRequisicao.descricao.unique}", field = "descricao")
public class FinalidadeRequisicao extends TpModel implements ConvertableEntity, Comparable<FinalidadeRequisicao> {

	private static final long _ID_DA_FINALIDADE_OUTRA = -1;
	public static ActiveRecord<FinalidadeRequisicao> AR = new ActiveRecord<>(FinalidadeRequisicao.class);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence_generator")
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "SIGATP.hibernate_sequence")
	private Long id;

	@NotNull
	@UpperCase
	private String descricao;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_ORI")
	private CpOrgaoUsuario cpOrgaoOrigem;

	public FinalidadeRequisicao() {
		this.id = new Long(0);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public CpOrgaoUsuario getCpOrgaoOrigem() {
		return cpOrgaoOrigem;
	}

	public void setCpOrgaoOrigem(CpOrgaoUsuario cpOrgaoOrigem) {
		this.cpOrgaoOrigem = cpOrgaoOrigem;
	}

	public static FinalidadeRequisicao buscar(String descricaoBuscar) {
		FinalidadeRequisicao retorno = null;
		try {
			retorno = FinalidadeRequisicao.AR.find("descricao = ?", descricaoBuscar).first();
		} catch (Exception e) {
			return null;
		}
		return retorno;
	}

	public static FinalidadeRequisicao buscar(Long idBuscar) {
		FinalidadeRequisicao retorno = null;
		try {
			retorno = FinalidadeRequisicao.AR.find("id = ?", idBuscar).first();
		} catch (Exception e) {
			return null;
		}
		return retorno;
	}
	
	public static FinalidadeRequisicao outra() {
 		return buscar(_ID_DA_FINALIDADE_OUTRA);
	}

	public static List<FinalidadeRequisicao> listarTodos(CpOrgaoUsuario orgaoUsuario) {
		return FinalidadeRequisicao.AR.find("cpOrgaoOrigem = ? and id <> ?", orgaoUsuario, _ID_DA_FINALIDADE_OUTRA).fetch();
	}

	@SuppressWarnings("unchecked")
	public static List<FinalidadeRequisicao> listarTodos() {
		List<FinalidadeRequisicao> finalidades = FinalidadeRequisicao.AR.findAll();
		Collections.sort(finalidades);
		return finalidades;
	}

	@SuppressWarnings("unchecked")
	public static List<FinalidadeRequisicao> listarParaCombo(boolean mostrarOutra) {
	 	List<FinalidadeRequisicao> lista = FinalidadeRequisicao.AR.findAll();
	 	if(!mostrarOutra) {
	 		lista.remove(FinalidadeRequisicao.outra());
	 	}
	 	return lista;
	}

	public void checarProprietario(CpOrgaoUsuario orgaoUsuario) throws Exception {
		if ((!this.cpOrgaoOrigem.equivale(orgaoUsuario)) || (this.id.equals(_ID_DA_FINALIDADE_OUTRA))) {
			try {
				//throw new Exception(new I18nMessage("finalidadeRequisicao", "finalidadeRequisicao.checarProprietario.exception").getMessage());
				throw new Exception(MessagesBundle.getMessage("finalidadeRequisicao.checarProprietario.exception"));
			} catch (Exception e) {
			    throw new Exception(e);
			}
		}
	}

	public boolean ehOutra() {
		if (this.id.equals(_ID_DA_FINALIDADE_OUTRA)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
 	    }
 	
		if (getClass() != obj.getClass()) {
 	        return false;
 	    }
 	
		final FinalidadeRequisicao outro = (FinalidadeRequisicao) obj;
	    return this.id.equals(outro.id);
	}
	
    @Override
    public int compareTo(FinalidadeRequisicao o) {
        return this.getId().compareTo(o.getId());
    }
}