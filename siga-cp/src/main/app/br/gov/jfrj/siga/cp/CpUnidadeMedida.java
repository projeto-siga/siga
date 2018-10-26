package br.gov.jfrj.siga.cp;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;

@Entity
@Table(name = "CP_UNIDADE_MEDIDA", schema = "CORPORATIVO")
public class CpUnidadeMedida extends AbstractCpUnidadeMedida {

	final static public int ANO = 1;

	final static public int MES = 2;

	final static public int DIA = 3;

	final static public int HORA = 4;

	final static public int MINUTO = 5;

	final static public int SEGUNDO = 6;

	public static final ActiveRecord<CpUnidadeMedida> AR = new ActiveRecord<>(
			CpUnidadeMedida.class);

	public String getPlural() {
		if (getDescricao().endsWith("s"))
			return getDescricao() + "es";
		return getDescricao() + "s";
	}

	public void setId(Long id) {
		setIdUnidadeMedida(id);
	}

	public Long getId() {
		return getIdUnidadeMedida();
	}

}
