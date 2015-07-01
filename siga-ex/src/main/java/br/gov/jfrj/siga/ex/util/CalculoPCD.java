/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Classe que efetua o cálculo dos valores a serem pagos no pedido de concessão
 * de diárias.
 * 
 * @author kpf
 * 
 */
public class CalculoPCD {

	public static final int PERNOITE_ANTERIOR = 1;
	public static final int PERNOITE_POSTERIOR = 2;
	private SigaCalendar dataInicio;
	private SigaCalendar dataFim;
	private DpPessoa beneficiario;
	private int pernoite;
	private String cargo;
	private String funcao;
	private HashMap<String, Float> tabelaValores;

	private float descontoSalario = 0F; // informação do WEmul
	private float valorConcedido = 0F;// informação do WEmul

	/**
	 * Em valores de hoje o auxilio alimentação é de R$630,00
	 */
	private static final float AUXILIO_ALIMENTACAO = 630F;
	/**
	 * Se o beneficiário utilizar carro oficial durante o período de
	 * afastamento, este atributo é true. Este valor influencia no cálculo da
	 * concessão do adicional de deslocamento.
	 */
	private boolean carroOficial;

	/**
	 * Se o beneficiário solicitar auxílio transporte durante o período de
	 * afastamento, este atributo é true. Este valor influencia no cálculo da
	 * concessão do adicional de deslocamento.
	 */
	private boolean solicitaAuxTransporte;

	/**
	 * TRUE Se o beneficiário for EXECUTOR DE MANDADOS.
	 */
	private boolean executorMandado;

	public CalculoPCD() {
		inicializarTabelaValores();
	}

	/**
	 * Inicia e determina a tabela de valores da diária de acordo com o cargo ou
	 * função.
	 */
	private void inicializarTabelaValores() {
		tabelaValores = new HashMap<String, Float>();

		tabelaValores.put("JUIZ FEDERAL TITULAR", 554F);
		tabelaValores.put("JUIZ FEDERAL SUBSTITUTO", 526F);
		tabelaValores.put("ASSISTENTE I", 214F);
		tabelaValores.put("ASSISTENTE II", 214F);
		tabelaValores.put("ASSISTENTE III", 214F);
		tabelaValores.put("ASSISTENTE IV", 214F);
		tabelaValores.put("SUPERVISOR", 214F);
		tabelaValores.put("COORDENADOR", 264F);
		tabelaValores.put("DIRETOR DE SUBSECRETARIA", 314F);
		tabelaValores.put("DIRETOR DE SECRETARIA", 342F);
		tabelaValores.put("ANALISTA JUDICIÁRIO", 214F);
		tabelaValores.put("TÉCNICO JUDICIÁRIO", 186F);
		tabelaValores.put("CJ01", 264F);
		tabelaValores.put("CJ04", 368F);

	}

	/**
	 * Feriado/Sábado/Domingo são excluídos do desconto de alimentação e
	 * transporte;
	 * 
	 * @return
	 */
	public int getNumeroDiasInuteis() {
		int diasInuteis = 0;
		Calendar ini = new SigaCalendar(dataInicio.getTimeInMillis());
		while (ini.compareTo(dataFim) <= 0) {
			if (Feriados.verificarPorData(ini.getTime()) != null
					|| ini.get(SigaCalendar.DAY_OF_WEEK) == SigaCalendar.SATURDAY
					|| ini.get(SigaCalendar.DAY_OF_WEEK) == SigaCalendar.SUNDAY) {
				diasInuteis++;
				// System.out.println(inicio.getTime());
			}
			ini.add(SigaCalendar.DAY_OF_MONTH, 1);
		}

		return diasInuteis;
	}

	/**
	 * O número de diárias é calculado pelo período entre as datas inicial e
	 * final, inclusive a ida e metade do dia da volta ex: 02/10/2009 até
	 * 03/10/2009 = 1,5 diárias (uma do dia 02 e 0,5 do dia 03);
	 * 
	 * @return
	 */
	public float getNumeroDiarias() {
		// AA (CÁLCULO DO Nº DE DIÁRIAS)
		//
		// dias = dataFinal - dataInicial + 0,5
		// pernoiteOuDia30 = 0
		// SE (Q7=30)
		// entao pernoiteOuDia30 = 1
		// SENÃO
		// SE (Y7="x" ou Z7="x")
		// entao pernoiteOuDia30=1
		//
		// retorna dias + pernoiteOuDia30

		float numDiarias = (dataInicio.dias360(dataFim)) + .5F;
		int pernoiteOuDia30 = 0;
		if (dataInicio.get(SigaCalendar.DAY_OF_MONTH) == 30
				|| pernoite == PERNOITE_ANTERIOR
				|| pernoite == PERNOITE_POSTERIOR
				|| pernoite == (PERNOITE_ANTERIOR + PERNOITE_POSTERIOR)) {
			pernoiteOuDia30 = 1;
		}
		return numDiarias + pernoiteOuDia30;
	}

	/**
	 * O valor da diária é baseado no cargo/função do beneficiário.
	 * 
	 * @param
	 * @return
	 */
	public float getValorDiaria() {

		if (cargo != null) {
			if (cargo.equals("JUIZ FEDERAL TITULAR")
					|| cargo.equals("JUIZ FEDERAL SUBSTITUTO")) {
				return tabelaValores.get(cargo);
			}
		}

		if (funcao != null) {
			if (funcao.equals("ASSISTENTE I") || funcao.equals("ASSISTENTE II")
					|| funcao.equals("ASSISTENTE III")
					|| funcao.equals("ASSISTENTE IV")
					|| funcao.equals("SUPERVISOR")
					|| funcao.equals("COORDENADOR") || funcao.equals("CJ01")
					|| funcao.equals("CJ04")
					|| funcao.equals("DIRETOR DE SUBSECRETARIA")
					|| funcao.equals("DIRETOR DE SECRETARIA")) {
				return tabelaValores.get(funcao);
			}
		}

		if (cargo != null) {
			if (cargo.equals("ANALISTA JUDICIÁRIO")
					|| cargo.equals("TÉCNICO JUDICIÁRIO")) {
				return tabelaValores.get(cargo);
			}
		}
		return 0F; // retorna null para dar erro no cálculo caso não tenha

	}

	/**
	 * Nº de diárias * valor da diária + ajuste diária (arrendondado para baixo)
	 * 
	 * @return
	 */
	public float getCalculoDiarias() {
		return (float) Math.floor((getNumeroDiarias() * getValorDiaria()));
	}

	/**
	 * Seja qual for o cargo ocupado pelo servidor ou mesmo o magistrado, o
	 * adicional de deslocamento corresponde a 25% da diária de um analista.
	 * 
	 * @return
	 */
	public float getValorDeslocamento() {
		return (float) Math.floor((tabelaValores.get("ANALISTA JUDICIÁRIO") * 0.25));
	}

	/**
	 * O número de deslocamentos depende se o beneficiário utilizará carro
	 * oficial e/ou solicitou o auxílio transporte para po período de
	 * afastamento. A tabela é a seguinte:
	 * 
	 * carroOficial(false) e solicitaAuxTransporte (true) = 2 carroOficial(true)
	 * e solicitaAuxTransporte (false) = 0 carroOficial(true) e
	 * solicitaAuxTransporte (true) = 1 carroOficial(false) e
	 * solicitaAuxTransporte (false) = 0
	 * 
	 * @return
	 */
	public int getNumeroDeslocamentos() {
		if (!carroOficial && solicitaAuxTransporte) {
			return 2;
		}

		if ((carroOficial && !solicitaAuxTransporte)
				|| (!carroOficial && !solicitaAuxTransporte)) {
			return 0;
		}

		if (carroOficial && solicitaAuxTransporte) {
			return 1;
		}

		return 0;

	}

	/**
	 * valor do deslocamento * nº de deslocamentos
	 * 
	 * @return
	 */
	public float getCalculoDeslocamento() {
		return getValorDeslocamento() * getNumeroDeslocamentos();
	}

	/**
	 * Retorna o valor diário do auxílio alimentação.
	 * 
	 * @return
	 */
	public float getValorAuxilioAlimentacaoDiario() {

		if (this.executorMandado) {
			return 0F;
		}

		return (float) Math.floor(AUXILIO_ALIMENTACAO/22);
	}

	/**
	 * O auxilio alimentação diário sempre é descontado em valor inteiro.
	 * Exemplo: 02/10/2009 até 03/10/2009 = 1,5 diárias (uma do dia 02 e 0,5 do
	 * dia 03). Embora seja condida 1,5 diárias o desconto de alimentação será
	 * de 2 um referente ao dia 02 e um referente ao dia 03. Os magistrados
	 * recebem subsídio único, desta forma não recebem e não descontam auxilo
	 * alimentação e transporte.
	 * 
	 * Cálculo: Se for juiz federal (titular ou sibstituto) = 0 Senão d1 =
	 * dataInicial - dataFinal + 1 se (dia(dataInicial)=30) d1 + 1; retorna d1 -
	 * DiasInuteis;
	 * 
	 * 
	 * @return
	 */
	public float getNumeroDiariasDesconto() {
		if (cargo.equals("JUIZ FEDERAL TITULAR")
				|| cargo.equals("JUIZ FEDERAL SUBSTITUTO")) {
			return 0F;
		}

		float diariasDesconto = dataInicio.dias360(dataFim) + 1;
		if (dataInicio.get(SigaCalendar.DAY_OF_MONTH) == 30) {
			diariasDesconto += 1;
		}
		return (diariasDesconto - getNumeroDiasInuteis());
	}

	/**
	 * Retorna o total do desconto referente ao auxílio alimentação.
	 * 
	 * @return
	 */
	public float getCalculoDescontoAlimentacao() {
		return getValorAuxilioAlimentacaoDiario() * getNumeroDiariasDesconto();
	}

	/**
	 * Retorna o total do desconto referente ao auxílio transporte.
	 * 
	 * @return
	 */
	public float getCalculoDescontoTransporte() {
		return (float) Math.floor(getValorAuxilioTransporteDiario()
				* getNumeroDiariasDesconto());
	}

	/**
	 * Para o desconto de transporte é necessário ter acesso via wemul ao
	 * sistema de vale transporte (seção de benefícios na SGP) é efetuado
	 * tomando por base o valor diário total R$ 34,8 menos a divisão do valor
	 * dos 6% V.BAs. ex (tabela em anexo): R$ 311,76/30 (um mês) = R$ 10,39 que
	 * é igual a R$ R$ 24,41 x o valor inteiro do delocamento (vide item 7) que
	 * é 2 totalizando R$ 48,00 (desprezndo-se os centavos, arrendondado para
	 * baixo).
	 * 
	 * VD = valor concedido / 30; difDSVD = DS - VD; difDSVD *
	 * getNumeroDiariasDesconto()
	 * 
	 * @return
	 */
	public float getValorAuxilioTransporteDiario() {
		if (descontoSalario > 0 && valorConcedido > 0) {
			float vd = valorConcedido / 22;
			float difDSVD = descontoSalario - vd;
			return difDSVD;
		}

		return 0F;

	}

	/**
	 * Retorna o total de todos os descontos. getCalculoDescontoAlimentacao() +
	 * getCalculoDescontoTransporte()
	 * 
	 * @return
	 */
	public float getCalculoTotalDescontos() {
		return getCalculoDescontoAlimentacao() + getCalculoDescontoTransporte();
	}

	/**
	 * getCalculoDiarias() + getCalculoDeslocamento() + ajusteTotalPCD
	 * 
	 * @return
	 */
	public float getCalculoValorBruto() {
		return getCalculoDiarias() + getCalculoDeslocamento();
	}

	/**
	 * getCalculoValorBruto() - getCalculoTotalDescontos();
	 * 
	 * @return
	 */
	public float getCalculoValorLiquido() {
		return getCalculoValorBruto() - getCalculoTotalDescontos();
	}

	/**
	 * Retorna a data de início do cálculo
	 * 
	 * @return
	 */
	public String getDataInicio() {
		return new SimpleDateFormat("dd/mm/yyyy").getDateInstance().format(
				dataInicio.getTime());
	}

	/**
	 * Retorna a data final do cálculo
	 * 
	 * @return
	 */
	public String getDataFim() {
		return new SimpleDateFormat("dd/mm/yyyy").getDateInstance().format(
				dataFim.getTime());
	}

	/**
	 * Retorna se o cálculo considera o uso de carro oficial.
	 * 
	 * @return
	 */
	public boolean isCarroOficial() {
		return carroOficial;
	}

	/**
	 * Informa se o cálculo considera o uso de carro oficial.
	 * 
	 * @param carroOficial -
	 *            TRUE se o cálculo deve considerar o uso de carro oficial
	 */
	public void setCarroOficial(boolean carroOficial) {
		this.carroOficial = carroOficial;
	}

	/**
	 * Retorna se o cálculo considera a solicitação de auxílio transporte.
	 * 
	 * @return
	 */
	public boolean isSolicitaAuxTransporte() {
		return solicitaAuxTransporte;
	}

	/**
	 * Informa se o cálculo considera a solicitação de auxílio transporte.
	 * 
	 * @param solicitaAuxTransporte -
	 *            TRUE se considera a solicitação de auxílio transporte.
	 */
	public void setSolicitaAuxTransporte(boolean solicitaAuxTransporte) {
		this.solicitaAuxTransporte = solicitaAuxTransporte;
	}

	/**
	 * Retorna o beneficiário do cálculo.
	 * 
	 * @return
	 */
	public DpPessoa getBeneficiario() {
		return beneficiario;
	}

	/**
	 * Configura o beneficiário do cálculo.
	 * 
	 * @param beneficiario
	 */
	public void setBeneficiario(DpPessoa beneficiario) {
		this.beneficiario = beneficiario;
	}

	/**
	 * Retorna se o cálculo considera pernoite.
	 * 
	 * @return 1 - pernoite no dia anterior 2 - pernoite no dia posterior
	 */
	public int getPernoite() {
		return pernoite;
	}

	/**
	 * Informa se o cálculo considera pernoite
	 * 
	 * @param pernoite
	 */
	public void setPernoite(int pernoite) {
		this.pernoite = pernoite;
	}

	/**
	 * Retorna o valor que é descontado do salário do beneficiário devido ao
	 * pagamento do auxílio transporte. A SOF realiza a consulta desse valor no
	 * WEmul.
	 * 
	 * @return
	 */
	public float getDescontoSalario() {
		return descontoSalario;
	}

	/**
	 * Informa o valor que é descontado do salário do beneficiário devido ao
	 * pagamento do auxílio transporte. A SOF realiza a consulta desse valor no
	 * WEmul.
	 * 
	 * @param ds
	 */
	public void setDescontoSalario(float ds) {
		this.descontoSalario = ds;
	}

	/**
	 * Retorna o valor que é concedido ao beneficiário pelo auxílio transporte.
	 * A SOF realiza a consulta desse valor no WEmul.
	 * 
	 * @return
	 */
	public float getValorConcedido() {
		return valorConcedido;
	}

	/**
	 * Informa o valor que é concedido ao beneficiário pelo auxílio transporte.
	 * A SOF realiza a consulta desse valor no WEmul.
	 * 
	 * @param valorConcedido
	 */
	public void setValorConcedido(float valorConcedido) {
		this.valorConcedido = valorConcedido;
	}

	/**
	 * Informa a data de início do cálculo.
	 * 
	 * @param data
	 */
	public void setDataInicio(String data) {
		if (data != null && data.length() > 0) {
			Integer dia = new Integer(data.substring(0, 2));
			Integer mes = new Integer(data.substring(3, 5));
			Integer ano = new Integer(data.substring(6));
			this.dataInicio = new SigaCalendar(ano, mes - 1, dia);
		}
	}

	/**
	 * Informa a data final do cálculo.
	 * 
	 * @param data
	 */
	public void setDataFim(String data) {
		if (data != null && data.length() > 0) {
			Integer dia = new Integer(data.substring(0, 2));
			Integer mes = new Integer(data.substring(3, 5));
			Integer ano = new Integer(data.substring(6));
			this.dataFim = new SigaCalendar(ano, mes - 1, dia);
		}
	}

	/**
	 * Retorna se o beneficário é executor de mandados. Os oficiais de justiça
	 * posuem cáculo diferenciado do adicional de deslocamento.
	 * 
	 * @return
	 */
	public boolean isExecutorMandado() {
		return executorMandado;
	}

	/**
	 * Informa se o beneficário é executor de mandados. Os oficiais de justiça
	 * posuem cáculo diferenciado do adicional de deslocamento.
	 * 
	 * @param executorMandado
	 */
	public void setExecutorMandado(boolean executorMandado) {
		this.executorMandado = executorMandado;
	}

	/**
	 * Retorna o cargo a ser considerado para o cálculo. As diárias possuem
	 * diferentes valores dependendo do cargo do beneficiário.
	 * 
	 * @return
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * Informa o cargo a ser considerado para o cálculo. As diárias possuem
	 * diferentes valores dependendo do cargo do beneficiário.
	 * 
	 * @param cargo
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo.trim();
	}

	/**
	 * Retorna a função comissionada a ser considerada para o cálculo. As
	 * diárias possuem diferentes valores dependendo da função.
	 * 
	 * @return
	 */
	public String getFuncao() {
		return funcao;
	}

	/**
	 * Informa a função comissionada a ser considerada para o cálculo. As
	 * diárias possuem diferentes valores dependendo da função.
	 * 
	 * @param funcao
	 */
	public void setFuncao(String funcao) {
		this.funcao = funcao.trim();
	}

}
