package br.gov.jfrj.siga.gc.util;

public class GcGraficoEvolucaoItem implements Comparable<GcGraficoEvolucaoItem> {

	public long mes;
	public long ano;

	public long novos;
	public long visitados;
	public long outros;

	public GcGraficoEvolucaoItem(long mes, long ano, long novos,
			long visitados, long outros) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.novos = novos;
		this.visitados = visitados;
		this.outros = outros;
	}

	@Override
	public int compareTo(GcGraficoEvolucaoItem o) {
		int i = 0;
		i = Long.valueOf(ano).compareTo(o.ano);
		if (i != 0)
			return i;
		i = Long.valueOf(mes).compareTo(o.mes);
		if (i != 0)
			return i;
		return 0;
	}

	public void somar(GcGraficoEvolucaoItem o) {
		this.novos += o.novos;
		this.visitados += o.visitados;
		this.outros += o.outros;
	}
}
