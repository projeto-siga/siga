package models;

import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public class SrValor implements Comparable<SrValor> {
	
	private static Map<Integer, Long> unidadesEmSegundos;
	
	private Map<Integer, Long> getUnidadesEmSegundos(){
		if (unidadesEmSegundos == null){
			unidadesEmSegundos = new HashMap<Integer, Long>();
			unidadesEmSegundos.put(CpUnidadeMedida.ANO, 946080000L);
			unidadesEmSegundos.put(CpUnidadeMedida.MES, 2592000L);
			unidadesEmSegundos.put(CpUnidadeMedida.DIA, 86400L);
			unidadesEmSegundos.put(CpUnidadeMedida.HORA, 3600L);
			unidadesEmSegundos.put(CpUnidadeMedida.MINUTO, 60L);
			unidadesEmSegundos.put(CpUnidadeMedida.SEGUNDO, 1L);
		}
		return unidadesEmSegundos;
	}

	private Long valor;
	
	private Integer unidadeMedida;
	
	public SrValor(Long valor, Integer unidadeMedida) {
		this.valor = valor;
		this.unidadeMedida = unidadeMedida;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public long getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(Integer unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	public Long getValorEmSegundos(){
		return getUnidadesEmSegundos().get(unidadeMedida) * valor;
	}

	@Override
	public int compareTo(SrValor o) {
		return this.getValorEmSegundos().compareTo(o.getValorEmSegundos());
	}
}
