package br.gov.jfrj.siga.base.diarias;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AplicacaoException;

public class DiariasDaJusticaFederal {
	public static class DiariasDaJusticaFederalParametroTrecho {
		static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

		public LocalDate data;
		public String trecho;
		public boolean carroOficialAteOEmbarque;
		public boolean carroOficialAteODestino;
		public boolean semDespesasDeHospedagem;

		public String getData() {
			return data.format(formatter);
		}

		public String getTrecho() {
			return trecho;
		}

		public boolean isCarroOficialAteODestino() {
			return carroOficialAteODestino;
		}

		public boolean isCarroOficialAteOEmbarque() {
			return carroOficialAteOEmbarque;
		}

		public boolean isSemDespesasDeHospedagem() {
			return semDespesasDeHospedagem;
		}
	}

	public static class DiariasDaJusticaFederalResultado {
		public ArrayList<DiariasDaJusticaFederalResultadoDiario> dias = new ArrayList<>();
		public double totalDoDescontoDeTeto;
		public double total;

		public ArrayList<DiariasDaJusticaFederalResultadoDiario> getDias() {
			return dias;
		}

		public double getTotal() {
			return total;
		}

		public double getTotalDoDescontoDeTeto() {
			return totalDoDescontoDeTeto;
		}
	}

	public static class DiariasDaJusticaFederalResultadoDiario {
		public LocalDate data;
		public String trecho;
		public double diaria;
		public double acrescimoDeDeslocamento;
		public double descontoDeAuxilioAlimentacao;
		public double descontoDeAuxilioTransporte;
		public double subtotalBruto;
		public double descontoDeTeto;
		public double subtotalLiquido;

		public double getAcrescimoDeDeslocamento() {
			return acrescimoDeDeslocamento;
		}

		public LocalDate getData() {
			return data;
		}

		public double getDescontoDeAuxilioAlimentacao() {
			return descontoDeAuxilioAlimentacao;
		}

		public double getDescontoDeAuxilioTransporte() {
			return descontoDeAuxilioTransporte;
		}

		public double getDescontoDeTeto() {
			return descontoDeTeto;
		}

		public double getDiaria() {
			return diaria;
		}

		public double getSubtotalBruto() {
			return subtotalBruto;
		}

		public double getSubtotalLiquido() {
			return subtotalLiquido;
		}

		public String getTrecho() {
			return trecho;
		}
	}

	public DiariasDaJusticaFederalResultado calcular(final double valorUnitarioDaDiaria, final boolean internacional,
			final double cotacaoDoDolar, final double valorUnitarioDoAuxilioAlimentacao, final double limiteDiario,
			final List<DiariasDaJusticaFederalParametroTrecho> trechos) {
		DiariasDaJusticaFederalResultado r = new DiariasDaJusticaFederalResultado();

		if (trechos == null)
			throw new AplicacaoException("Trechos não pode ser nulo");
		if (trechos.isEmpty())
			throw new AplicacaoException("Trechos não pode ser uma lista vazia");

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
			dia.data = d;

			// Calcula acréscimo de deslocamento com base nas informações de cada trecho
			DiariasDaJusticaFederalParametroTrecho trecho = mapaDeTrechos.get(d);
			if (trecho != null) {
				dia.trecho = trecho.trecho;
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

			r.dias.add(dia);

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
