package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_TIPO_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrTipoAtributo extends HistoricoSuporte {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_TIPO_ATRIBUTO_SEQ", name = "srTipoAtributoSeq")
	@GeneratedValue(generator = "srTipoAtributoSeq")
	@Column(name = "ID_TIPO_ATRIBUTO")
	public Long idTipoAtributo;

	@Column(name = "NOME")
	public String nomeTipoAtributo;

	@Column(name = "DESCRICAO")
	public String descrTipoAtributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrTipoAtributo tipoAtributoInicial;

	@OneToMany(targetEntity = SrTipoAtributo.class, mappedBy = "tipoAtributoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrTipoAtributo> meuTipoAtributoHistoricoSet;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return idTipoAtributo;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		idTipoAtributo = id;
	}

	public static List<SrTipoAtributo> listar() {
		return SrTipoAtributo.find("byHisDtFimIsNull").fetch();
	}

	public List<SrTipoAtributo> getHistoricoTipoAtributo() {
		if (tipoAtributoInicial != null)
			return tipoAtributoInicial.meuTipoAtributoHistoricoSet;
		return null;
	}

	public SrTipoAtributo getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrTipoAtributo> sols = getHistoricoTipoAtributo();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void salvar() throws Exception {
		super.salvar();

		//Edson: comentado o codigo abaixo porque muitos problemas ocorriam. Mas
		//tem de ser corrigido.
		
		//Edson: eh necessario o refresh porque, abaixo, as configuracoes referenciando
		//serao recarregadas do banco, e precisarao reconhecer o novo estado deste tipo de atributo
		//refresh();
		
		// Edson: soh apaga o cache de configuracoes se ja existia antes uma
		// instancia do objeto, caso contrario, nao ha configuracao
		// referenciando
		//if (tipoAtributoInicial != null)
		//	SrConfiguracao.notificarQueMudou(this);
	}

}
