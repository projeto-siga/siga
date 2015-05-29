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
package br.gov.jfrj.siga.sinc.lib;

import junit.framework.TestCase;

public class SincronizavelSuporteTest extends TestCase {
	Lotacao l1, l2, l3, _l3;
	Pessoa p1, _p1, p2, p3, p4, p5, p6, _p6;

	public SincronizavelSuporteTest() {
		l1 = new Lotacao();
		l1.sigla = "CSIS";
		l1.nome = "Coordenadoria de Sistemas de Informação";
		l1.setIdExterna("l1");

		l2 = new Lotacao();
		l2.sigla = "SESIE";
		l2.nome = "Seção de Sistemas Especializados";
		l2.setIdExterna("l2");

		p1 = new Pessoa();
		p1.nome = "Orlando";
		p1.cpf = "123456789-12";
		p1.lotacao = l2;
		p1.desconsiderar = "sim";
		p1.setIdExterna("p1");

		_p1 = new Pessoa();
		_p1.nome = "Orlando";
		_p1.cpf = "123456789-12";
		_p1.lotacao = l2;
		_p1.desconsiderar = "não";
		_p1.setIdExterna("p1");

		p2 = new Pessoa();
		p2.nome = "Orlando";
		p2.cpf = "123456789-12";
		p2.lotacao = l2;
		p2.setIdExterna("p2");

		p3 = new Pessoa();
		p3.nome = "Orlando Gomes";
		p3.cpf = "123456789-12";
		p3.lotacao = l2;
		p3.setIdExterna("p3");

		p4 = new Pessoa();
		p4.nome = "Orlando";
		p4.cpf = "1";
		p4.lotacao = l2;
		p4.setIdExterna("p4");

		p5 = new Pessoa();
		p5.nome = "Orlando";
		p5.cpf = "123456789-12";
		p5.lotacao = l1;
		p5.setIdExterna("p5");

		l3 = new Lotacao();
		l3.sigla = "SESIE";
		l3.nome = "Seção de Sistemas Especializados";
		l3.naoPropagar = "sim";
		l3.setIdExterna("l3");

		_l3 = new Lotacao();
		_l3.sigla = "SESIE";
		_l3.nome = "Seção de Sistemas Especializados";
		_l3.naoPropagar = "não";
		_l3.setIdExterna("l3");

		p6 = new Pessoa();
		p6.nome = "Orlando";
		p6.cpf = "123456789-12";
		p6.lotacao = l3;
		p6.setIdExterna("p6");

		_p6 = new Pessoa();
		_p6.nome = "Orlando";
		_p6.cpf = "123456789-12";
		_p6.lotacao = _l3;
		_p6.setIdExterna("p6");

	}

	public void testSemelhante() {
		assertEquals(p1.semelhante(_p1, 0), true);
		assertEquals(p1.semelhante(p3, 0), false);
		assertEquals(p2.semelhante(p4, 0), false);
		assertEquals(p1.semelhante(p5, 0), false);
	}

	public void testDesconsiderar() {
		assertEquals(p1.semelhante(_p1, 0), true);
	}

	public void testNaoPropagar() {
		assertEquals(l3.semelhante(_l3, 0), false);
		assertEquals(l3.semelhante(_l3, 1), true);
		assertEquals(p6.semelhante(_p6, 0), true);
	}

	public void testNivelDependencia() {
		assertEquals(l1.getNivelDeDependencia(), 0);
		assertEquals(p1.getNivelDeDependencia(), 1);
	}

}
