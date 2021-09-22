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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.sinc.lib.Item.Operacao;

public class Sincronizador {

	private static final int MAX_ATERACOES = 50; // número máximo de alterações
	// em um único passo
	private SincronizavelComparator sc = new SincronizavelComparator();
	private ItemComparator ic = new ItemComparator();

	private SortedSet<Sincronizavel> setNovo;
	private SortedSet<Sincronizavel> setAntigo;
	private OperacaoList list;

	private Map<String, Sincronizavel> map = new HashMap<String, Sincronizavel>();

	public SortedSet<Sincronizavel> getSetNovo() {
		return setNovo;
	}

	public void setSetNovo(SortedSet<Sincronizavel> setNovo2) {
		this.setNovo = setNovo2;
	}

	public SortedSet<Sincronizavel> getSetAntigo() {
		return setAntigo;
	}

	public String getSicronizavelKey(Sincronizavel s) {
		return s.getClass().getSimpleName() + ": " + s.getIdExterna();
	}

	public void setSetAntigo(SortedSet<Sincronizavel> setAntigo) {
		this.setAntigo = setAntigo;
		this.map.clear();
		for (Sincronizavel s : this.setAntigo)
			this.map.put(getSicronizavelKey(s), s);
	}
	
	private void encaixar() throws AplicacaoException {
        encaixar(false);
	}

	// Executa algoritmo de comparação entre as listas xml e tabela e
	// preenche
	// as listas: inserir, excluir e atualizar.
	//
	private void encaixar(boolean naoExcluir) throws AplicacaoException {
		Iterator<Sincronizavel> iNovo = setNovo.iterator();
		Iterator<Sincronizavel> iAntigo = setAntigo.iterator();

		Sincronizavel oAntigo = null;
		Sincronizavel oNovo = null;

		if (iAntigo.hasNext())
			oAntigo = iAntigo.next();
		if (iNovo.hasNext())
			oNovo = iNovo.next();
		while (oAntigo != null || oNovo != null) {
			if ((oAntigo == null)
					|| (oNovo != null && sc.compare(oAntigo, oNovo) > 0)) {
				// O novo não existe entre os antigos, portanto deve ser
				// incluido
				list.add(new Item(Item.Operacao.incluir, oNovo, null));
				if (iNovo.hasNext())

					oNovo = iNovo.next();
				else
					oNovo = null;
			} else if (oNovo == null
					|| (oAntigo != null && sc.compare(oNovo, oAntigo) > 0)) {
				// O corp não existe no xml
				if (!naoExcluir)
			        list.add(new Item(Item.Operacao.excluir, null, oAntigo));
				// excluir.add(oTabela);
				if (iAntigo.hasNext())
					oAntigo = iAntigo.next();
				else
					oAntigo = null;
			} else {

				if (oAntigo == null) {
					int i = 0;
				}
				// O registro existe no corp e no xml
				// atualizar.put(oXml.getIdExterna(), new Par(oXml,
				// oTabela));
				if (!oNovo.semelhante(oAntigo, 0))
					list.add(new Item(Item.Operacao.alterar, oNovo, oAntigo));
				// atualizar.add(new Par(oXml, oTabela));
				if (iNovo.hasNext())
					oNovo = iNovo.next();
				else
					oNovo = null;
				if (iAntigo.hasNext())
					oAntigo = iAntigo.next();
				else
					oAntigo = null;
			}

			if (list.size() > MAX_ATERACOES) {
				// throw new AplicacaoException(
				// "Número máximo de alterações em um único passo ultrapassado! ("
				// + MAX_ATERACOES + ")");
			}
		}
		iNovo = null;
		iAntigo = null;
	}
	
	public List<Item> getOperacoes(Date dt) throws AplicacaoException {
        return getOperacoes(dt, false);
	}

	public List<Item> getOperacoes(Date dt, boolean naoExcluir) throws AplicacaoException {
		list = new OperacaoList();
		encaixar(naoExcluir);
		replicarIdInicial();
		atualizarDataInicialEFinal(dt);
		ordenarOperacoes();
		return list;
	}

	public void replicarIdInicial() {
		for (Item i : list) {
			if (i.getOperacao() != Operacao.alterar)
				continue;
			i.getNovo().setIdInicial(i.getAntigo().getIdInicial());
		}
	}

	public void atualizarDataInicialEFinal(Date dt) {
		for (Item i : list) {
			if (i.getNovo() != null)
				i.getNovo().setDataInicio(dt);
			if (i.getAntigo() != null)
				i.getAntigo().setDataFim(dt);
		}
	}

	public void gravar(Item i, OperadorComHistorico opr, boolean religar) {
		
		if (i.getOperacao() == Operacao.incluir) {
		
			if (religar)
				religar(i.getNovo());
			
			i.setNovo(opr.gravar(i.getNovo(), false));
			
			i.getNovo().setIdInicial(i.getNovo().getId());
			this.map.put(getSicronizavelKey(i.getNovo()), i.getNovo());

		} else 	if (i.getOperacao() == Operacao.excluir) {
			i.setAntigo(opr.gravar(i.getAntigo(), false));
			
			this.map.remove(getSicronizavelKey(i.getAntigo()));
		
		} else {   //Operacao.alterar

			if (religar)
				religar(i.getNovo());

			i.setAntigo(opr.gravar(i.getAntigo(), true));
			
			i.setNovo(opr.gravar(i.getNovo(), false));
			
			this.map.put(getSicronizavelKey(i.getNovo()), i.getNovo());
		}
	}

	/*
	 * gravarComHistorico sem a identidade cadastrante
	 */

	public void gravar(Item i, OperadorSemHistorico opr, boolean religar) {
		if (i.getOperacao() == Operacao.incluir) {
			if (religar)
				religar(i.getNovo());
			i.setNovo(opr.incluir(i.getNovo()));
			i.getNovo().setIdInicial(i.getNovo().getId());
			this.map.put(getSicronizavelKey(i.getNovo()), i.getNovo());
		}
		if (i.getOperacao() == Operacao.excluir) {
			i.setAntigo(opr.excluir(i.getAntigo()));
			this.map.remove(getSicronizavelKey(i.getAntigo()));
		}
		if (i.getOperacao() == Operacao.alterar) {
			if (religar)
				religar(i.getNovo());
			// if (i.getNovo().getClass().getName().contains("DpLotacao"))
			// System.out.println("*********** GRAVANDO ************"
			// + i.getNovo().getDescricaoExterna());
			i.setNovo(opr.alterar(i.getAntigo(), i.getNovo()));
			// i.setAntigo(opr.alterarAntigo(i.getAntigo()));
			// i.setNovo(opr.alterarNovo(i.getNovo()));
			this.map.put(getSicronizavelKey(i.getNovo()), i.getNovo());
		}
	}

	public List<Item> getEncaixe() throws AplicacaoException {
		list = new OperacaoList();
		encaixar();
		return list;
	}

	public void ordenarOperacoes() {
		Collections.sort(list, ic);
	}

	public void religar(Sincronizavel s) {
		Class cls = s.getClass();
		do {
			try {
				Field fieldlist[] = cls.getDeclaredFields();
				for (int i = 0; i < fieldlist.length; i++) {
					Field fld = fieldlist[i];
					if (((fld.getModifiers() & Modifier.STATIC) != 0))
						continue;

					fld.setAccessible(true);
					Object o1 = fld.get(s);
					if (o1 != null && o1 instanceof Sincronizavel) {
						Sincronizavel ss = map
								.get(getSicronizavelKey((Sincronizavel) o1));
						fld.set(s, ss);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			cls = cls.getSuperclass();
		} while (!cls.equals(Object.class));
	}

	// Varrer todos os objetos, encontrar seus membros sincronizaveis e
	// substitui-los pela versao importada do xml
	//
	public void religarListaPorIdExterna(SortedSet<Sincronizavel> setNovo2) {
		Map<String, Sincronizavel> mapAux = new HashMap<String, Sincronizavel>();

		for (Sincronizavel s : setNovo2)
			mapAux.put(getSicronizavelKey(s), s);

		for (Sincronizavel s : setNovo2) {
			Class cls = s.getClass();
			do {
				try {
					Field fieldlist[] = cls.getDeclaredFields();
					for (int i = 0; i < fieldlist.length; i++) {
						Field fld = fieldlist[i];

						fld.setAccessible(true);
						Object o1 = fld.get(s);
						if (o1 != null && o1 instanceof Sincronizavel) {
							Sincronizavel ss = mapAux
									.get(getSicronizavelKey((Sincronizavel) o1));
							fld.set(s, ss);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				cls = cls.getSuperclass();
			} while (!cls.equals(Object.class));
		}
	}
}
