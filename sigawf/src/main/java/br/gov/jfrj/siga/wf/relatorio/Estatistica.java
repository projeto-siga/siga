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
package br.gov.jfrj.siga.wf.relatorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.base.AplicacaoException;

/**
 * Classe com utilitários estatísticos. Os cálculos são realizados sobre os dados do array
 * fornecido no método setArray().
 * 
 * @author kpf
 * 
 */
public class Estatistica {

	private double array[];

	/**
	 * Calcula o coeficiente de Variação de Pearson.
	 */
	public double getPearson() {

		return (getDesvioPadrao() / getMediaAritmetica()) * 100;

	}

	/**
	 * Calcula a média aritmética.
	 * 
	 * @return
	 */
	public double getMediaAritmetica() {

		double total = 0;

		for (int counter = 0; counter < array.length; counter++)

			total += array[counter];

		return total / array.length;

	}
	
	/**
	 * Calcula a média aritmética desconsiderando um percentual dos maiores e menores elementos
	 * 
	 * @return
	 */
	public double getMediaAritmeticaTruncada(double percentual) {

		if (array==null || array.length<=0){
			return 0.0;
		}
		ordenar();
		int descartar = (int) ((percentual/100)*array.length);
		int de= descartar;
		int ate= array.length - descartar;
		
		double[] elementosConsiderados=null;
		if (de<=ate){
			elementosConsiderados = Arrays.copyOfRange(array, de, ate);	
		}else{
			throw new AplicacaoException("Não é possível calcular a média truncada! Verifique se o percentual a ser descartado remove todos os elementos.");
		}
		
		
		return getMediaAritmetica(elementosConsiderados);

	}


	/**
	 * Calcula a soma dos elementos.
	 * 
	 * @return
	 */
	public double getSomaDosElementos() {

		double total = 0;

		for (int counter = 0; counter < array.length; counter++)

			total += array[counter];

		return total;

	}

	/**
	 * Calcula a soma dos elementos ao quadrado. Utilizado para calcular a
	 * variância.
	 * 
	 * @return
	 */
	public double getSomaDosElementosAoQuadrado() {

		double total = 0;

		for (int counter = 0; counter < array.length; counter++)

			total += Math.pow(array[counter], 2);

		return total;

	}

	/**
	 * Calcula a média aritmética.
	 * 
	 * @param array
	 * @return
	 */
	public double getMediaAritmetica(double array[]) {

		double total = 0;

		for (int counter = 0; counter < array.length; counter++)

			total += array[counter];

		return total / array.length;

	}

	/**
	 * Calcula a soma dos elementos.
	 * 
	 * @param array
	 * @return
	 */
	public double getSomaDosElementos(double array[]) {

		double total = 0;

		for (int counter = 0; counter < array.length; counter++)

			total += array[counter];

		return total;

	}

	/**
	 * Ordena o array especificado em ordem ascendente numérica.
	 */
	public void ordenar() {

		Arrays.sort(array);

	}

	/**
	 * Imprime os elementos do array.
	 */
	public void imprimeArray() {

		System.out.print("\nElementos do Array: ");

		for (int count = 0; count < array.length; count++)

			System.out.print(array[count] + " ");

	}

	/**
	 * Pesquisa um elemento dentro do array. O Array não pode conter valores
	 * duplicados.
	 */
	public int buscaPor(int value) {

		return Arrays.binarySearch(array, value);

	}

	/**
	 * Calcula a variância Amostral
	 */
	public double getVariancia() {

		double p1 = 1 / Double.valueOf(array.length - 1);

		double p2 = getSomaDosElementosAoQuadrado()

		- (Math.pow(getSomaDosElementos(), 2) / Double

		.valueOf(array.length));

		return p1 * p2;

	}

	/**
	 * Calcula o desvio padrão amostral.
	 * 
	 * @return
	 */
	public double getDesvioPadrao() {

		return Math.sqrt(getVariancia());

	}

	/**
	 * Calcula a Mediana.
	 * 
	 * @return
	 */
	public double getMediana() {

		this.ordenar();

		int tipo = array.length % 2;

		if (tipo == 1) {

			return array[((array.length + 1) / 2) - 1];

		} else {

			int m = array.length / 2;

			return (array[m - 1] + array[m]) / 2;

		}

	}

	/**
	 * Calcula a moda.
	 * 
	 * @return
	 */
	public double getModa() {

		HashMap map = new HashMap();

		Integer i;

		Double moda = 0.0;

		Integer numAtual, numMaior = 0;

		for (int count = 0; count < array.length; count++) {

			i = (Integer) map.get(new Double(array[count]));

			if (i == null) {

				map.put(new Double(array[count]), new Integer(1));

			} else {

				map
						.put(new Double(array[count]), new Integer(
								i.intValue() + 1));

				numAtual = i.intValue() + 1;

				if (numAtual > numMaior) {

					numMaior = numAtual;

					moda = new Double(array[count]);

				}

			}

		}

		// System.out.print("\n Eis o mapa: "+map.toString());

		return moda;

	}

	/**
	 * Calcula o coeficiente de assimetria.
	 * 
	 * @return
	 */
	public double getCoefAssimetria() {

		return (getMediaAritmetica() - getModa()) / getDesvioPadrao();

	}

	/**
	 * Retorna o array
	 * 
	 * @return
	 */
	public double[] getArray() {
		return array;

	}

	/**
	 * Informa o array com os dados a serem utilizados no cálculo.
	 * @param array
	 */
	public void setArray(double[] array) {

		this.array = array;

	}

	/**
	 * Informa o mapa com os dados a serem utilizados no cálculo. 
	 * @param mapa
	 */
	public void setArray(Map<String, Long> mapa) {
		double[] array = new double[mapa.size()];
		int i = 0;
		for (String k : mapa.keySet()) {
			array[i] = mapa.get(k);
			i++;
		}

		this.array = array;

	}

	/**
	 * Informa o ArrayList com os dados a serem utilizados no cálculo.
	 * @param lista
	 */
	public void setArray(ArrayList<Long> lista) {
		double[] array = new double[lista.size()];
		int i = 0;
		for (Long item : lista) {
			array[i] = item;
			i++;
		}

		this.array = array;

	}

}
