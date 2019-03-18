package br.gov.jfrj.siga.gc.util;

import java.util.TreeSet;

import org.joda.time.LocalDate;

public class GcGraficoEvolucao extends TreeSet<GcGraficoEvolucaoItem> {

	@Override
	public boolean add(GcGraficoEvolucaoItem e) {
		if (this.contains(e)) {
			this.floor(e).somar(e);
			return false;
		}
		return super.add(e);
	}
	
	public String criarGrafico() {
		LocalDate ld = new LocalDate();
		ld = new LocalDate(ld.getYear(), ld.getMonthOfYear(), 1);

		// Header
		StringBuilder sb = new StringBuilder();
		sb.append("['Mês','Visitas','Novos'],");
		// Values
		for (int i = -6; i <= 0; i++) {
			LocalDate ldl = ld.plusMonths(i);
			sb.append("['");
			sb.append(ldl.toString("MMM/yy"));
			sb.append("',");
			long novos = 0;
			long visitados = 0;
			GcGraficoEvolucaoItem o = new GcGraficoEvolucaoItem(
					ldl.getMonthOfYear(), ldl.getYear(), 0, 0, 0);
			if (this.contains(o)) {
				o = this.floor(o);
				novos = o.novos;
				visitados = o.visitados;
			}
			sb.append(visitados);
			sb.append(",");
			sb.append(novos);
			sb.append(",");
			sb.append("],");
		}
		return sb.toString();
	}
}
