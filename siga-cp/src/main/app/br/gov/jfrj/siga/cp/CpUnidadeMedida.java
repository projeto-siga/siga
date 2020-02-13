package br.gov.jfrj.siga.cp;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;

@Entity
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "corporativo.cp_unidade_medida")
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
