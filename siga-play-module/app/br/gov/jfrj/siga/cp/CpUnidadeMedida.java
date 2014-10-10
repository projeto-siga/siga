package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CP_UNIDADE_MEDIDA", schema = "CORPORATIVO")
public class CpUnidadeMedida extends AbstractCpUnidadeMedida {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6079358181141108760L;

	final static public int ANO = 1;

	final static public int MES = 2;

	final static public int DIA = 3;
	
	final static public int HORA = 4;
	
	/**
	 * Recupera a lista de unidades de medida contendo Dia e Hora.
	 */
	public static List<CpUnidadeMedida> diaHoraLista() {
		List<CpUnidadeMedida> unidadesMedida = CpUnidadeMedida.all().fetch();
		List<CpUnidadeMedida> lista = new ArrayList<CpUnidadeMedida>();
		
		for (CpUnidadeMedida unidadeMedida : unidadesMedida) {
			if (unidadeMedida.getIdUnidadeMedida().equals(Long.valueOf(DIA)) || unidadeMedida.getIdUnidadeMedida().equals(Long.valueOf(HORA)))
				lista.add(unidadeMedida);
		}
		
		return lista;
	}
}
