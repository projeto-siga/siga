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
package br.gov.jfrj.siga.gc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import junit.framework.TestCase;

public class GcCacheTest extends TestCase {
	private EntityManager em;
	private DpPessoa p1;
	private CpIdentidade id1;
	private DpLotacao l1;
	private CpOrgaoUsuario ou1;

	protected void setUp() throws Exception {
		super.setUp();

		em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);
		CpDao.freeInstance();
		CpDao.getInstance();
		// HibernateUtil.configurarHibernate((Session)em.getDelegate());

		em.getTransaction().begin();

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

		try (BufferedReader br = new BufferedReader(new FileReader(new File(
				"src/test/java/br/gov/jfrj/siga/gc/GcCacheTest.sql")))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("--") || line.trim().length() == 0)
					continue;
				if (line.endsWith(";"))
					line = line.substring(0, line.length() - 1);
				em.createNativeQuery(line).executeUpdate();
			}
		}

		em.getTransaction().commit();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCarregarCache() throws Exception {
		Cp.getInstance().getConf().limparCacheSeNecessario();

		boolean pode = Cp.getInstance().getConf()
				.podeUtilizarServicoPorConfiguracao(p1, l1, "TESTE");
		assertFalse(pode);
	}
}
