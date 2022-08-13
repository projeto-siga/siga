package br.gov.jfrj.siga.ex.calc.diarias;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class DiariasDaJusticaFederal {

//	Art. 10. As diárias nacionais e internacionais, pagas a servidores e
//	magistrados, terão como valor máximo o correspondente à diária respectiva paga a
//	ministro do Supremo Tribunal Federal e serão escalonadas da seguinte forma:
//	I - as diárias pagas aos membros do Conselho da Justiça Federal serão
//	equivalentes a 100% (cem por cento) do valor da diária a que tem direito o ministro do
//	Supremo Tribunal Federal;
//	II - as diárias pagas aos membros dos tribunais regionais federais serão
//	equivalentes a 95% (noventa e cinco por cento) do valor da diária a que tem direito o
//	ministro do Supremo Tribunal Federal;
//	III - as diárias pagas a juiz federal ou a juiz federal substituto serão
//	equivalentes a 95% (noventa e cinco por cento) do valor da diária a que tem direito o
//	membro do tribunal regional federal;
//	IV - as diárias pagas aos servidores ocupantes do cargo de analista
//	judiciário ou de cargos em comissão serão equivalentes a 55% (cinquenta e cinco por
//	cento) do valor da diária a que tem direito o ministro do Supremo Tribunal Federal;
//	V - as diárias pagas aos servidores ocupantes do cargo de técnico
//	judiciário ou no exercício de função comissionada serão equivalentes a 45% (quarenta
//	e cinco por cento) do valor da diária a que tem direito o ministro do Supremo Tribunal
//	Federal.

	public enum FaixaEnum {
		MINISTRO_DO_STF(1), MEMBRO_DO_CJF(1), MEMBRO_DO_TRF(0.95), JUIZ(0.95 * 0.95),
		ANALISTA_JUDICIARIO_E_CARGOS_EM_COMISSAO(0.55), TECNICO_JUDICIARIO_OU_FUNCAO_COMISSIONADA(0.45);

		double coef;

		FaixaEnum(double coef) {
			this.coef = coef;
		}

		public static FaixaEnum find(String s) {
			switch (s) {
			case "MEMBRO DO CONSELHO":
				return MEMBRO_DO_CJF;
			case "DESEMBARGADOR FEDERAL":
				return MEMBRO_DO_TRF;
			case "JUIZ FEDERAL DE 1º GRAU":
			case "JUIZ FEDERAL SUBSTITUTO":
				return JUIZ;
			case "ANALISTA JUDICIÁRIO":
			case "CJ-4":
			case "CJ-3":
			case "CJ-2":
			case "CJ-1":
				return ANALISTA_JUDICIARIO_E_CARGOS_EM_COMISSAO;
			case "FC-6":
			case "FC-5":
			case "FC-4":
			case "FC-3":
			case "FC-2":
			case "FC-1":
			case "TÉCNICO JUDICIÁRIO":
			case "AUXILIAR JUDICIÁRIO":
				return TECNICO_JUDICIARIO_OU_FUNCAO_COMISSIONADA;
			}
			return null;
		}

//		public static double valorUnitarioDaDiaria(String s) {
//			return valorUnitarioDaDiaria(find(s));
//		}
//
//		public static double valorUnitarioDaDiaria(FaixaEnum i, double valorUnitatioDaDiariaDoMinistroDoSTF) {
//			return 
//		}
	}

	public enum DeslocamentoConjuntoEnum {
		EQUIPE_DE_TRABALHO("Equipe de Trabalho"), ASSESSORAMENTO_DE_AUTORIDADE("Assessoramento de Autoridade"),
		ASSISTENCIA_DIRETA_A_AUTORIDADE("Assistência Direta à Autoridade"),
		SEGURANCA_DE_MAGISTRADO("Segurança de Magistrado");

		String descr;

		DeslocamentoConjuntoEnum(String descr) {
			this.descr = descr;
		}

		public static DeslocamentoConjuntoEnum find(String s) {
//			if (valueOf(s) != null)
//				return valueOf(s);

			for (DeslocamentoConjuntoEnum i : values())
				if (i.descr.equals(s))
					return i;
			return null;
		}

	}

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
		public String mensagemDeErro;

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
			final DeslocamentoConjuntoEnum deslocamentoConjunto, final double cotacaoDoDolar,
			final double valorUnitarioDoAuxilioAlimentacao, final double valorUnitarioDoAuxilioTransporte,
			final double limiteDiario, final List<DiariasDaJusticaFederalParametroTrecho> trechos) {
		DiariasDaJusticaFederalResultado r = new DiariasDaJusticaFederalResultado();
		try {
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

			// Faz o cálculo dia a dia
			List<LocalDate> datas = listaDeDatas(trechos.get(0).data, trechos.get(trechos.size() - 1).data);
			boolean semDespesasDeHospedagem = false;

			double diaria = floor(valorUnitarioDaDiaria);
			if (internacional)
				diaria = floor(valorUnitarioDaDiaria * cotacaoDoDolar);

			// § 1º O magistrado ou servidor que se deslocar em equipe de trabalho
			// receberá diária equivalente ao maior valor pago entre os demais membros da
			// equipe.
			if (deslocamentoConjunto == DeslocamentoConjuntoEnum.EQUIPE_DE_TRABALHO)
				diaria = diaria * 1;

			// § 3º O servidor que se afastar da sede do serviço, acompanhando
			// magistrado, na qualidade de assessor ou para prestar-lhe assistência direta,
			// fará jus à diária correspondente a 80% do valor da diária percebida pelo
			// magistrado.
			if (deslocamentoConjunto == DeslocamentoConjuntoEnum.ASSESSORAMENTO_DE_AUTORIDADE
					|| deslocamentoConjunto == DeslocamentoConjuntoEnum.ASSISTENCIA_DIRETA_A_AUTORIDADE)
				diaria = floor(diaria * 0.8);

			// § 5º O técnico judiciário - área administrativa - agente de segurança que
			// se deslocar para fora da sede a fim de fazer a segurança de magistrado fará
			// jus à diária correspondente a 80% (oitenta por cento) do valor da diária
			// percebida pelo magistrado.
			if (deslocamentoConjunto == DeslocamentoConjuntoEnum.SEGURANCA_DE_MAGISTRADO)
				diaria = floor(diaria * 0.8);

			// Art. 12. O servidor que se deslocar para participar de evento de duração
			// superior a 45 (quarenta e cinco) dias perceberá diária correspondente a 60%
			// (sessenta por cento) do valor fixado, observados os critérios constantes no
			// art. 10.
			if (datas.size() > 45)
				diaria = floor(diaria * 0.6);

			for (LocalDate d : datas) {
				DiariasDaJusticaFederalResultadoDiario dia = new DiariasDaJusticaFederalResultadoDiario();
				boolean primeiroDia = d == datas.get(0);
				boolean ultimoDia = d == datas.get(datas.size() - 1);
				dia.data = d;

				// Calcula acréscimo de deslocamento com base nas informações de cada trecho
				DiariasDaJusticaFederalParametroTrecho trecho = mapaDeTrechos.get(d);
				if (trecho != null) {
					dia.trecho = trecho.trecho;
					semDespesasDeHospedagem = trecho.semDespesasDeHospedagem;

//					Art. 17. Será acrescido o adicional de 80% (oitenta por cento) sobre o valor
//					básico da diária de analista judiciário, referida no art. 10 desta resolução, devida a
//					magistrados e servidores, para cobrir despesa de deslocamento até o local de
//					embarque e do desembarque ao local de trabalho ou hospedagem e vice-versa.
//					§ 1º quando a viagem for para mais de um destino, exceto escalas e
//					conexões, o adicional de transporte de que trata este artigo poderá ser concedido mais
//					de uma vez, a critério da administração, observado o disposto no § 2º.
//					§ 2º Não será devido o adicional de transporte de que cuida este artigo
//					para os deslocamentos de embarque e/ou desembarque que tenham sido realizados
//					com utilização de veículo oficial. 
					if (!trecho.carroOficialAteOEmbarque)
						dia.acrescimoDeDeslocamento += diaria * 0.25;
					if (!trecho.carroOficialAteODestino)
						dia.acrescimoDeDeslocamento += diaria * 0.25;
				}

//				Art. 6º As diárias serão concedidas por dia de afastamento da sede do
//				serviço, incluindo-se o de partida e o de chegada, destinadas a indenizar o magistrado
//				ou o servidor pelas despesas extraordinárias com pousada, alimentação e locomoção
//				Este texto não substitui a publicação oficial.
//				urbana, observando-se as disposições dos arts. 11 e 12 desta resolução e os
//				seguintes critérios:
//				I - valor integral, quando o deslocamento importar pernoite fora da
//				localidade de exercício;
//				II - metade do valor:
//				a) quando o deslocamento não exigir pernoite fora da localidade de
//				exercício;
//				b) na data do retorno à sede;
//				c) quando a União custear, por meio diverso, as despesas de hospedagem
//				ou quando fornecido alojamento ou outra forma de hospedagem por órgão ou entidade
//				da administração pública. 
//			§ 1º As diárias internacionais serão concedidas a partir do dia do
//				deslocamento do território nacional e contadas integralmente do dia da partida até o
//				dia do retorno, inclusive. 				
				boolean meiaDiaria = semDespesasDeHospedagem || (!internacional && primeiroDia);
				dia.diaria = meiaDiaria ? floor(diaria / 2) : diaria;

				// Art. 13. As diárias sofrerão desconto correspondente ao auxílioalimentação,
				// ao auxílio-transporte e à indenização de transporte a que fizer jus o
				// magistrado ou servidor, exceto aquelas eventualmente pagas em fins de semana
				// e feriados.
				if (d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY) {
					// Calcula desconto de auxílio alimentação
					dia.descontoDeAuxilioAlimentacao = valorUnitarioDoAuxilioAlimentacao;
					// Calcula desconto de auxílio transporte
					dia.descontoDeAuxilioTransporte = valorUnitarioDoAuxilioTransporte;
				}

				// Calcula o subtotal antes da aplicação da glosa
				dia.subtotalBruto = dia.diaria + dia.acrescimoDeDeslocamento - dia.descontoDeAuxilioAlimentacao
						- dia.descontoDeAuxilioTransporte;

				// Calcula desconto em diarias
				if (!internacional) {
					double limiteDeGlosa = limiteDiario;
					if (meiaDiaria)
						limiteDeGlosa /= 2;
					if (limiteDeGlosa < dia.subtotalBruto) {
						dia.descontoDeTeto = dia.subtotalBruto - limiteDeGlosa;
					}
				}

				// Calcula o subtotalLiquido depois da aplicação da glosa
				dia.subtotalLiquido = dia.subtotalBruto - dia.descontoDeTeto;

				r.dias.add(dia);

				// Calcula totalizadores
				r.totalDoDescontoDeTeto += dia.descontoDeTeto;
				r.total += dia.subtotalLiquido;
			}
		} catch (AplicacaoException ex) {
			r.mensagemDeErro = ex.getMessage();
		}
		return r;
	}

	// Não contemplados:

	// § 4º Conceder-se-á diária nacional quando o retorno à sede se der no dia
	// seguinte ao da chegada ao território nacional.

	// Art. 17. Será acrescido o adicional de 80% (oitenta por cento) sobre o valor
	// básico da diária de analista judiciário, referida no art. 10 desta resolução,
	// devida a magistrados e servidores, para cobrir despesa de deslocamento até o
	// local de embarque e do desembarque ao local de trabalho ou hospedagem e
	// vice-versa.

	private List<LocalDate> listaDeDatas(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> dates = new ArrayList<>();
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1))
			dates.add(d);
		return dates;
	}

	private static double floor(double d) {
		return Math.floor(d * 100) / 100;
	}

//	public boolean isDiaUtil(LocalDate data) {
//		return isDiaUtil(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//	}
//	
//	public boolean isDiaUtil(Date data) {
//		return !isFinalDeSemana(data) && !isFeriado(data);
//	}
//
//	public static List<CpFeriado> getListaFeriados() {
//		List<CpFeriado> listaFeriados = new ArrayList<>();
//		CpDao dao = CpDao.getInstance();
//		listaFeriados = dao.listarFeriados();
//		for (CpFeriado f : listaFeriados)
//			f.getCpOcorrenciaFeriadoSet().size();
//		return listaFeriados;
//	}
//
//	public static boolean isFinalDeSemana(Date data) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(data);
//		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
//	}
//
//	public static boolean isFeriado(Date data) {
//		for (CpFeriado f : getListaFeriados())
//			if (f.abrange(data))
//				return true;
//		return false;
//	}
}
