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

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelComparator;

public class SincronizadorTest extends TestCase {
	Lotacao l1, _l1, l2, _l2;
	Pessoa p1, _p1, p2, p3, _p3, p4, p5, _p5;
	SincronizavelComparator sc = new SincronizavelComparator();

	public SincronizadorTest() {
		l1 = new Lotacao();
		l1.setIdExterna("1");
		l1.sigla = "CSIS";
		l1.nome = "Coordenadoria de Sistemas de Informação";

		_l1 = new Lotacao();
		_l1.setIdExterna("1");
		_l1.sigla = "CSIS";
		_l1.nome = "Coordenadoria de Sistemas de Informação";

		l2 = new Lotacao();
		l2.setIdExterna("2");
		l2.sigla = "SESIE";
		l2.nome = "Seção de Sistemas Especializados";

		_l2 = new Lotacao();
		_l2.setIdExterna("2");
		_l2.sigla = "SESIE";
		_l2.nome = "Seção de Sistemas Especializados";

		p1 = new Pessoa();
		p1.setIdExterna("1");
		p1.nome = "Orlando";
		p1.cpf = "123456789-12";
		p1.lotacao = l2;

		_p1 = new Pessoa();
		_p1.setIdExterna("1");
		_p1.setIdInicial(1L);
		_p1.nome = "Orlando Gomes";
		_p1.cpf = "123456789-12";
		_p1.lotacao = _l2;

		p2 = new Pessoa();
		p2.setIdExterna("2");
		p2.nome = "Orlando";
		p2.cpf = "123456789-12";
		p2.lotacao = l2;

		p3 = new Pessoa();
		p3.setIdExterna("3");
		p3.nome = "Orlando Gomes";
		p3.cpf = "123456789-12";
		p3.lotacao = l2;

		_p3 = new Pessoa();
		_p3.setIdExterna("3");
		_p3.nome = "Orlando Gomes";
		_p3.cpf = "123456789-12";
		_p3.lotacao = _l2;

		p4 = new Pessoa();
		p4.setIdExterna("4");
		p4.nome = "Orlando";
		p4.cpf = "1";
		p4.lotacao = l1;

		p5 = new Pessoa();
		p5.setIdExterna("5");
		p5.nome = "Orlando";
		p5.cpf = "123456789-12";
		p5.lotacao = l1;

		_p5 = new Pessoa();
		_p5.setIdExterna("5");
		_p5.nome = "Orlando";
		_p5.cpf = "123456789-12";
		_p5.lotacao = _l1;

	}

	public void testEncaixe() throws AplicacaoException {
		SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);
		setNovo.add(p1);
		setNovo.add(p4);
		setNovo.add(p5);
		SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);
		setAntigo.add(_p1);
		setAntigo.add(p3);
		setAntigo.add(p5);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getEncaixe();

		assertEquals(list.get(0).getNovo(), p1);
		assertEquals(list.get(0).getAntigo(), _p1);
		assertEquals(list.get(0).getOperacao(), Item.Operacao.alterar);

		assertNull(list.get(1).getNovo());
		assertEquals(list.get(1).getAntigo(), p3);
		assertEquals(list.get(1).getOperacao(), Item.Operacao.excluir);

		assertEquals(list.get(2).getNovo(), p4);
		assertNull(list.get(2).getAntigo());
		assertEquals(list.get(2).getOperacao(), Item.Operacao.incluir);

		assertEquals(list.size(), 3);
	}

	public void testReplicarIdInicial() throws AplicacaoException {
		SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);
		setNovo.add(p1);
		SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);
		setAntigo.add(_p1);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getEncaixe();
		sinc.replicarIdInicial();

		assertEquals(list.get(0).getNovo().getIdInicial().longValue(), 1L);
	}

	public void testAtualizarDataInicialEFinal() throws AplicacaoException {
		SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);
		setNovo.add(p1);
		setNovo.add(p4);
		setNovo.add(p5);
		SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);
		setAntigo.add(_p1);
		setAntigo.add(p3);
		setAntigo.add(p5);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getEncaixe();
		Date dt = new Date();
		sinc.atualizarDataInicialEFinal(dt);

		assertEquals(list.get(0).getNovo().getDataInicio(), dt);
		assertEquals(list.get(0).getAntigo().getDataFim(), dt);
		assertEquals(list.get(0).getOperacao(), Item.Operacao.alterar);

		assertEquals(list.get(1).getAntigo().getDataFim(), dt);
		assertEquals(list.get(1).getOperacao(), Item.Operacao.excluir);

		assertEquals(list.get(2).getNovo().getDataInicio(), dt);
		assertEquals(list.get(2).getOperacao(), Item.Operacao.incluir);
	}

	public void testOrdenarOperacoes() throws AplicacaoException {
		Filho sg = new Filho("SG", "SG", null, null);
		Filho sti = new Filho("STI", "STI", sg, null);
		Filho csis = new Filho("CSIS", "CSIS", sti, null);
		Filho sesie = new Filho("SESIE", "SESIE", csis, null);
		Filho sav = new Filho("SAV", "SAV", csis, null);
		Filho renato = new Filho("13635", "Renato", sesie, null);
		Filho orlando = new Filho("13939", "Orlando", renato, null);
		Filho markenson = new Filho("1", "Markenson", renato, sav);
		SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);
		setAntigo.add(sg);
		setAntigo.add(sti);
		setAntigo.add(csis);
		setAntigo.add(sesie);
		setAntigo.add(sav);
		setAntigo.add(renato);
		setAntigo.add(orlando);
		setAntigo.add(markenson);

		Filho _sg = new Filho("SG", "SG", null, null);
		Filho _sdi = new Filho("SDI", "SDI", _sg, null);
		Filho _sti = new Filho("STI", "STI", _sg, null);
		Filho _csis = new Filho("CSIS", "CSIS", _sti, null);
		Filho _sesie = new Filho("SESIE", "SESIE", _csis, null);
		Filho _renato = new Filho("13635", "Renato", _sdi, null);
		Filho _markenson = new Filho("1", "Markenson", _sesie, null);
		Filho _orlando = new Filho("13939", "Orlando", _markenson, null);
		SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);
		setNovo.add(_sg);
		setNovo.add(_sdi);
		setNovo.add(_sti);
		setNovo.add(_csis);
		setNovo.add(_sesie);
		setNovo.add(_renato);
		setNovo.add(_markenson);
		setNovo.add(_orlando);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getOperacoes(new Date());

		assertEquals(list.get(0).getNovo(), _sdi);
		assertEquals(list.get(0).getAntigo(), null);
		assertEquals(list.get(0).getOperacao(), Item.Operacao.incluir);

		assertEquals(list.get(list.size() - 3).getNovo(), null);
		assertEquals(list.get(list.size() - 3).getAntigo(), sav);
		assertEquals(list.get(list.size() - 3).getOperacao(),
				Item.Operacao.excluir);
	}

	public void testReligar() throws AplicacaoException {
		SortedSet<Sincronizavel> setNovo = new TreeSet<Sincronizavel>(sc);
		setNovo.add(l1);
		setNovo.add(l2);
		setNovo.add(p1);
		setNovo.add(p4);
		setNovo.add(p5);
		SortedSet<Sincronizavel> setAntigo = new TreeSet<Sincronizavel>(sc);
		setAntigo.add(_l1);
		setAntigo.add(_l2);
		setAntigo.add(_p1);
		setAntigo.add(_p3);
		setAntigo.add(_p5);

		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setNovo);
		sinc.setSetAntigo(setAntigo);

		List<Item> list = sinc.getEncaixe();
		sinc.replicarIdInicial();
		sinc.atualizarDataInicialEFinal(new Date());
		sinc.ordenarOperacoes();
		for (Item i : list)
			if (i.getNovo() != null)
				sinc.religar(i.getNovo());

		// p4
		assertEquals(list.get(0).getOperacao(), Item.Operacao.incluir);
		assertEquals(((Pessoa) list.get(0).getNovo()).getLotacao(), _l1);

		// p1
		assertEquals(list.get(1).getOperacao(), Item.Operacao.alterar);
		assertEquals(((Pessoa) list.get(1).getNovo()).getLotacao(), _l2);
		assertEquals(((Pessoa) list.get(1).getAntigo()).getLotacao(), _l2);

		// p3
		assertEquals(list.get(2).getOperacao(), Item.Operacao.excluir);
		assertEquals(((Pessoa) list.get(2).getAntigo()).getLotacao(), _l2);

		assertEquals(list.size(), 3);
	}

}
