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
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.gc.model.GcAcesso;
import br.gov.jfrj.siga.gc.model.GcArquivo;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoInformacao;
import br.gov.jfrj.siga.gc.model.GcTipoTag;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class GcServiceImplTest extends TestCase {
	GcServiceImpl service;
	GcTag t1, t2, t3;
	List<GcTag> list;
	GcInformacao i1;
	Long tagSequential = 0L;

	private EntityTransaction transaction;
	private EntityManager em;
	private GcTipoTag tt1;
	private GcArquivo a1;
	private DpPessoa p1;
	private GcAcesso acc1;
	private CpIdentidade id1;
	private DpLotacao l1;
	private CpOrgaoUsuario ou1;
	private GcTipoInformacao ti1;

	protected void setUp() throws Exception {
		super.setUp();

		// System.out.println("teste");
		em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		em.getTransaction().begin();

		tt1 = new GcTipoTag(1, "@");
		tt1.save();

		t1 = buildTag("@sr-item-1-123:sistemas");
		t1.save();
		t2 = buildTag("@sr-item-2-231:gestao-do-trabalho");
		t2.save();
		t3 = buildTag("@sr-item-3-312:siga-doc");
		t3.save();

		list = new ArrayList<>();
		list.add(t1);
		list.add(t2);
		list.add(t3);

		acc1 = new GcAcesso(GcAcesso.ACESSO_PUBLICO, "Público");
		acc1.save();

		ou1 = new CpOrgaoUsuario();
		ou1.setIdOrgaoUsu(1L);
		ou1.setNmOrgaoUsu("Orgao 1");
		ou1.setAcronimoOrgaoUsu("OU1");
		ou1.save();

		l1 = new DpLotacao();
		l1.setNomeLotacao("L1");
		l1.setOrgaoUsuario(ou1);
		l1.save();

		p1 = new DpPessoa();
		p1.setOrgaoUsuario(ou1);
		p1.save();

		id1 = new CpIdentidade();
		id1.setDpPessoa(p1);
		id1.save();

		ti1 = new GcTipoInformacao(
				GcTipoInformacao.TIPO_INFORMACAO_REGISTRO_DE_CONHECIMENTO,
				"Registro de Conhecimento");
		ti1.save();

		a1 = new GcArquivo();
		a1.save();

		i1 = new GcInformacao();
		i1.tipo = ti1;
		i1.arq = a1;
		i1.autor = p1;
		i1.lotacao = l1;
		i1.edicao = acc1;
		i1.visualizacao = acc1;
		i1.tags = new TreeSet<GcTag>();
		i1.tags.addAll(list);
		i1.ou = ou1;
		i1.hisIdcIni = id1;
		i1.save();

		em.getTransaction().commit();

		service = new GcServiceImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
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
		tag.tipo = tt1;
		tag.setIde(ide);
		tag.setIndice(indice);
		tag.setTitulo(titulo);
		// tag.setId(++tagSequential);

		return tag;
	}

	public void testAlteracaoSimplesUltimoNivel() throws Exception {
		service.atualizarTag("@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-documentos");
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-documentos]",
				i1.getTags().toString());
	}

	public void testAlteracaoSimplesNivelIntermediario() throws Exception {
		service.atualizarTag("@sr-item-2-231:gt");
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gt, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
	}

	public void testAlteracaoSimplesPrimeiroNivel() throws Exception {
		service.atualizarTag("@sr-item-1-123:sistemas-informatizados");
		assertEquals(
				"[@sr-item-1-123:sistemas-informatizados, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
	}

	public void testAlteracaoDeNivelDeUltimoParaPenultimo() throws Exception {
		service.atualizarTag("@sr-item-1-123:sistemas, @sr-item-2-312:gestao-documental");
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-312:gestao-documental]",
				i1.getTags().toString());
	}

}
