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
package br.gov.jfrj.siga.gc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.junit.Before;

import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoTag;

public class GcServiceImplTest extends TestCase {
	GcServiceImpl service;
	GcTag t1, t2, t3;
	List<GcTag> list;
	GcInformacao i1;
	List<GcInformacao> infs;
	GcTipoTag tipo = new GcTipoTag(1, "@");
	Long tagSequential = 0L;

	private EntityTransaction transaction;
	private EntityManager em;

	@Before
	public void initializeDependencies(){
		System.out.println("teste");
	}
	
	public GcServiceImplTest() throws Exception {
		em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		transaction = em.getTransaction();

		t1 = buildTag("@sr-item-1-123:sistemas");
		t2 = buildTag("@sr-item-2-231:gestao-do-trabalho");
		t3 = buildTag("@sr-item-3-312:siga-doc");

		list = new ArrayList<>();
		list.add(t1);
		list.add(t2);
		list.add(t3);

		i1 = new GcInformacao();
		i1.tags = new TreeSet<GcTag>();
		i1.tags.addAll(list);

		infs = new ArrayList<>();
		infs.add(i1);
		service = new GcServiceImpl();
	}

	private GcTag buildTag(String s) throws Exception {
		Matcher matcher = GcTag.tagPattern.matcher(s);
		if (!matcher.find()) {
			throw new Exception("Tag inválido. Deve seguir o padrão: "
					+ GcTag.tagPattern.toString());
		}
		String grupo = matcher.group(1);
		Integer indice = (matcher.group(2) != null) ? Integer.parseInt(matcher
				.group(2)) : null;
		String ide = matcher.group(3);
		String titulo = matcher.group(4);

		GcTag tag = new GcTag();
		tag.setCategoria(grupo + "-" + indice + "-" + ide);
		tag.tipo = tipo;
		tag.setIde(ide);
		tag.setIndice(indice);
		tag.setTitulo(titulo);
		tag.setId(++tagSequential);

		return tag;
	}

	public void testAlteracaoSimplesUltimoNivel() throws Exception {
		service.atualizarTagAlg(
				"@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-documentos",
				infs);
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-documentos]",
				i1.getTags().toString());
	}

	public void testAlteracaoSimplesNivelIntermediario() throws Exception {
		service.atualizarTagAlg("@sr-item-2-231:gt", infs);
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gt, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
	}

	public void testAlteracaoSimplesPrimeiroNivel() throws Exception {
		service.atualizarTagAlg("@sr-item-1-123:sistemas-informatizados", infs);
		assertEquals(
				"[@sr-item-1-123:sistemas-informatizados, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
	}

	public void testAlteracaoDeNivelDeUltimoParaPenultimo() throws Exception {
		service.atualizarTagAlg("@sr-item-2-312:gestao-documental", infs);
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-312:gestao-documental]",
				i1.getTags().toString());
	}

}
