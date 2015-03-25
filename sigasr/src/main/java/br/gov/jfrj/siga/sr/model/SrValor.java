package br.gov.jfrj.siga.sr.model;

import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public class SrValor implements Comparable<SrValor> {
	
	private static Map<Integer, Integer> unidadesEmSegundos;
	
	private Map<Integer, Integer> getUnidadesEmSegundos(){
		if (unidadesEmSegundos == null){
			unidadesEmSegundos = new HashMap<Integer, Integer>();
			unidadesEmSegundos.put(CpUnidadeMedida.ANO, 946080000);
			unidadesEmSegundos.put(CpUnidadeMedida.MES, 2592000);
			unidadesEmSegundos.put(CpUnidadeMedida.DIA, 86400);
			unidadesEmSegundos.put(CpUnidadeMedida.HORA, 3600);
			unidadesEmSegundos.put(CpUnidadeMedida.MINUTO, 60);
			unidadesEmSegundos.put(CpUnidadeMedida.SEGUNDO, 1);
		}
		return unidadesEmSegundos;
	}

	private Integer valor;
	
	private Integer unidadeMedida;
	
	public static void setUnidadesEmSegundos(
			Map<Integer, Integer> unidadesEmSegundos) {
		SrValor.unidadesEmSegundos = unidadesEmSegundos;
	}

	public SrValor(Integer valor, Integer unidadeMedida) {
		this.valor = valor;
		this.unidadeMedida = unidadeMedida;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public long getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(Integer unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	public Integer getValorEmSegundos(){
		return getUnidadesEmSegundos().get(unidadeMedida) * valor;
	}

	@Override
	public int compareTo(SrValor o) {
		return this.getValorEmSegundos().compareTo(o.getValorEmSegundos());
	}
}
