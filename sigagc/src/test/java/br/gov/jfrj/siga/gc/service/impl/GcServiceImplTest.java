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
	GcTag t1, t2, t3, t4, t5, t6;
	List<GcTag> list;
	GcInformacao i1, i2, i3;
	Long tagSequential = 0L;

	private EntityTransaction transaction;
	private EntityManager em;
	private GcTipoTag tt1, tt2, tt3;
	private GcArquivo a1;
	private DpPessoa p1;
	private GcAcesso acc1;
	private CpIdentidade id1;
	private DpLotacao l1;
	private CpOrgaoUsuario ou1;
	private GcTipoInformacao ti1;

	protected void setUp() throws Exception {
		super.setUp();

		em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		em.getTransaction().begin();

		tt1 = new GcTipoTag(1, "@");
		tt1.save();

		tt2 = new GcTipoTag(2, "#");
		tt2.save();

		tt3 = new GcTipoTag(3, "^");
		tt3.save();

		t1 = GcTag.getInstance("@sr-item-1-123:sistemas", null, true, true);
		t1.save();
		t2 = GcTag.getInstance("@sr-item-2-231:gestao-do-trabalho", null, true,
				true);
		t2.save();
		t3 = GcTag.getInstance("@sr-item-3-312:siga-doc", null, true, true);
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
		i1.setTipo(ti1);
		i1.setArq(a1);
		i1.setAutor(p1);
		i1.setLotacao(l1);
		i1.setEdicao(acc1);
		i1.setVisualizacao(acc1);
		i1.setTags(new TreeSet<GcTag>());
		i1.getTags().addAll(list);
		i1.setOu(ou1);
		i1.setHisIdcIni(id1);
		i1.save();

		t4 = GcTag.getInstance("^sr-item-1-123:sistemas", null, true, true);
		t4.save();

		t5 = GcTag
				.getInstance("^sr-item-1-123:sistemas", null, true, true);
		t5.save();

		t6 = GcTag
				.getInstance("^sr-acao-2-789:corrigir", null, true, true);
		t6.save();

		i2 = new GcInformacao();
		i2.setTipo(ti1);
		i2.setArq(a1);
		i2.setAutor(p1);
		i2.setLotacao(l1);
		i2.setEdicao(acc1);
		i2.setVisualizacao(acc1);
		i2.setTags(new TreeSet<GcTag>());
		i2.getTags().add(t4);
		i2.setOu(ou1);
		i2.setHisIdcIni(id1);
		i2.save();

		i3 = new GcInformacao();
		i3.setTipo(ti1);
		i3.setArq(a1);
		i3.setAutor(p1);
		i3.setLotacao(l1);
		i3.setEdicao(acc1);
		i3.setVisualizacao(acc1);
		i3.setTags(new TreeSet<GcTag>());
		i3.getTags().add(t5);
		i3.getTags().add(t6);
		i3.setOu(ou1);
		i3.setHisIdcIni(id1);
		i3.save();

		em.getTransaction().commit();

		service = new GcServiceImpl();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInplaceAlteracaoSimples() throws Exception {
		service.atualizarTag("^sr-item-1-123:sistemas-informatizados");
		assertEquals("[^sr-item-1-123:sistemas-informatizados]", i2.getTags()
				.toString());
	}

	public void testInplaceAlteracaoSimplesPrimeiroNivel() throws Exception {
		service.atualizarTag("^sr-item-1-123:sistemas-informatizados");
		assertEquals("[^sr-acao-2-789:corrigir, ^sr-item-1-123:sistemas-informatizados]", i3.getTags()
				.toString());
		
		// Não pode alterar os tags de classificacao
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
	}

	public void testInplaceAlteracaoSimplesUltimoNivel() throws Exception {
		service.atualizarTag("^sr-acao-2-789:corrigir-bugs");
		assertEquals("[^sr-acao-2-789:corrigir-bugs, ^sr-item-1-123:sistemas]", i3.getTags()
				.toString());
		
		// Não pode alterar os tags de classificacao
		assertEquals(
				"[@sr-item-1-123:sistemas, @sr-item-2-231:gestao-do-trabalho, @sr-item-3-312:siga-doc]",
				i1.getTags().toString());
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

	public void testAlteracaoDeNivelDeSegundoParaPrimeiro() throws Exception {
		service.atualizarTag("@sr-item-1-231:gestao-do-trabalho");
		assertEquals(
				"[@sr-item-1-231:gestao-do-trabalho, @sr-item-2-312:siga-doc]",
				i1.getTags().toString());
	}

	// Tags adicionais não devem ser afetados

	// Atualizar quando chamado com tag não hierárquico deve dar erro?

	// Tem sentido e hierarquia sem id?

	// Tem sentido em id sem hierarquia?

	// Testar o webservice pelo soapui

}
