package br.gov.jfrj.siga.base.diarias;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AplicacaoException;

public class DiariasDaJusticaFederal {
	public static class DiariasDaJusticaFederalParametroTrecho {
		LocalDate data;
		String trecho;
		boolean carroOficialAteOEmbarque;
		boolean carroOficialAteODestino;
		boolean semDespesasDeHospedagem;
	}

	public static class DiariasDaJusticaFederalResultadoDiario {
		LocalDate data;
		String trecho;
		double diaria;
		double acrescimoDeDeslocamento;
		double descontoDeAuxilioAlimentacao;
		double descontoDeAuxilioTransporte;
		double subtotalBruto;
		double descontoDeTeto;
		double subtotalLiquido;
	}

	public static class DiariasDaJusticaFederalResultado {
		ArrayList<DiariasDaJusticaFederalResultadoDiario> dias;
		double totalDoDescontoDeTeto;
		double total;
	}

//	public static DiariasDaJusticaFederalResultado calcular(final double valorUnitarioDaDiaria,
//			final boolean internacional, final double cotacaoDoDolar, final double valorUnitarioDoAuxilioAlimentacao,
//			final double limiteDiario, final HashMap<String, Object> trechos) {
//		List<DiariasDaJusticaFederalParametroTrecho> l = new ArrayList<>();
//		
//		DiariasDaJusticaFederalParametroTrecho t = new DiariasDaJusticaFederalParametroTrecho();
//		
//		l.add(t);
//		return calcular(valorUnitarioDaDiaria, internacional, cotacaoDoDolar, valorUnitarioDoAuxilioAlimentacao,
//				limiteDiario, l);
//	}

	public DiariasDaJusticaFederalResultado calcular(final double valorUnitarioDaDiaria, final boolean internacional,
			final double cotacaoDoDolar, final double valorUnitarioDoAuxilioAlimentacao, final double limiteDiario,
			final List<DiariasDaJusticaFederalParametroTrecho> trechos) {
		DiariasDaJusticaFederalResultado r = new DiariasDaJusticaFederalResultado();

		// Cria um mapa de todos os trechos por data
		SortedMap<LocalDate, DiariasDaJusticaFederalParametroTrecho> mapaDeTrechos = new TreeMap<>();
		LocalDate ultimaData = null;
		for (DiariasDaJusticaFederalParametroTrecho trecho : trechos) {
			if (trecho.data == null)
				throw new AplicacaoException("Trecho não pode ter data nula");
			if (ultimaData != null && !ultimaData.isBefore(trecho.data))
				throw new AplicacaoException(
						"Trecho não pode ter data igual ou menor do que a data do trecho anterior");
			mapaDeTrechos.put(trecho.data, trecho);
			ultimaData = trecho.data;
		}

		double diaria = valorUnitarioDaDiaria;
		if (internacional)
			diaria = valorUnitarioDaDiaria * cotacaoDoDolar;

		// Faz o cálculo dia a dia
		List<LocalDate> datas = listaDeDatas(trechos.get(0).data, trechos.get(trechos.size() - 1).data);
		boolean semDespesasDeHospedagem = false;
		for (LocalDate d : datas) {
			DiariasDaJusticaFederalResultadoDiario dia = new DiariasDaJusticaFederalResultadoDiario();

			// Calcula acréscimo de deslocamento com base nas informações de cada trecho
			DiariasDaJusticaFederalParametroTrecho trecho = mapaDeTrechos.get(d);
			if (trecho != null) {
				semDespesasDeHospedagem = trecho.semDespesasDeHospedagem;

				// Art. 17. Será acrescido o adicional de 80% (oitenta por cento) sobre o valor
				// básico da diária de analista judiciário, referida no art. 10 desta resolução,
				// devida a magistrados e servidores, para cobrir despesa de deslocamento até o
				// local de embarque e do desembarque ao local de trabalho ou hospedagem e
				// vice-versa.
				if (trecho.carroOficialAteOEmbarque)
					dia.acrescimoDeDeslocamento += diaria * 0.25;
				if (trecho.carroOficialAteODestino)
					dia.acrescimoDeDeslocamento += diaria * 0.25;
			}

			// Art. 13. As diárias sofrerão desconto correspondente ao auxílioalimentação,
			// ao auxílio-transporte e à indenização de transporte a que fizer jus o
			// magistrado ou servidor, exceto aquelas eventualmente pagas em fins de semana
			// e feriados.
			if (isDiaUtil(d)) {
				// Calcula desconto de auxílio alimentação
				dia.descontoDeAuxilioAlimentacao = valorUnitarioDoAuxilioAlimentacao;
				// Calcula desconto de auxílio transporte
				dia.descontoDeAuxilioTransporte = valorUnitarioDoAuxilioAlimentacao;
			}

			// Calcula o subtotal antes da aplicação da glosa
			dia.subtotalBruto = dia.diaria + dia.acrescimoDeDeslocamento - dia.descontoDeAuxilioAlimentacao
					- dia.descontoDeAuxilioTransporte;

			// Calcula desconto em diarias
			boolean ultimoDia = d == datas.get(datas.size() - 1);
			boolean meiaDiaria = !internacional && ultimoDia;
			double limiteDeGlosa = limiteDiario;
			if (meiaDiaria)
				limiteDeGlosa /= 2;
			if (limiteDeGlosa < dia.subtotalBruto) {
				dia.descontoDeTeto = 0;
			}

			// Calcula o subtotalLiquido depois da aplicação da glosa
			dia.subtotalLiquido = dia.subtotalBruto - dia.descontoDeTeto;

			// Calcula totalizadores
			r.totalDoDescontoDeTeto += dia.descontoDeTeto;
			r.total += dia.subtotalLiquido;
		}

		return r;
	}

	// § 4º Conceder-se-á diária nacional quando o retorno à sede se der no dia
	// seguinte ao da chegada ao território nacional.

	// Art. 12. O servidor que se deslocar para participar de evento de duração
	// superior a 45 (quarenta e cinco) dias perceberá diária correspondente a 60%
	// (sessenta por cento) do valor fixado, observados os critérios constantes no
	// art. 10.

	private boolean isDiaUtil(LocalDate d) {
		return true;
	}

	private List<LocalDate> listaDeDatas(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> dates = new ArrayList<>();
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1))
			dates.add(d);
		return dates;
	}
}
