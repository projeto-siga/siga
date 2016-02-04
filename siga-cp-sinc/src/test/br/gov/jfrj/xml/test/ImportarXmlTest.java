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
package br.gov.jfrj.xml.test;

import java.io.InputStream;
import java.util.Date;

import org.hibernate.cfg.AnnotationConfiguration;

import junit.framework.TestCase;
import br.gov.jfrj.importar.SigaCpSinc;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class ImportarXmlTest extends TestCase {
	public static void testImportarXml() throws Exception {
		// TODO: executar em homologação e produção : alter table
		// corporativo.DP_PESSOA modify (SIGLA_PESSOA varchar2(20))
		// IMPLANTACAO
		// testImportarXml("implanta.xml");
		// Teste SJRJ
		testImportarXml("exp.xml");

		// Teste com ORGAO e PAPEL
		/*
		 * testImportarXml("SigaCpSinc1.xml");
		 * testImportarXml("SigaCpSinc2.xml");
		 * testImportarXml("SigaCpSinc3.xml");
		 * testImportarXml("SigaCpSinc4.xml");
		 */

		// Testes de duplicidade
		// testImportarXml("SigaCpSincDup.xml");

		// Teste ANS
		// testImportarXml("siga_ans.xml");

		// Teste TJ-PA
		// testImportarXml("siga_tjpa.xml");

		// Teste do exemplo do Roteiro
		// testImportarXml("exemplo-roteiro.xml");
		//
		assertTrue(true);
	}

	public static void testImportarXml(String nomeArquivo) throws Exception {
		if (nomeArquivo == null)
			return;
		// SigaCpSinc.processarTesteXML();
		AnnotationConfiguration cfg;
		// desenvolvimento
		/*
		 * cfg = CpDao.criarHibernateCfg("jdbc:oracle:thin:@servidor:1521:instancia",
		 * "usuario", "senha");
		 */
		cfg = CpDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
		// cfg = CpDao.criarHibernateCfg(CpAmbienteEnumBL.HOMOLOGACAO);
		HibernateUtil.configurarHibernate(cfg, "");

		SigaCpSinc geraXml = new SigaCpSinc();

		Date dt = new Date();
		geraXml.log("--- Processando Teste " + dt + "--- ");
		geraXml.log("--- Executando: CpDaoTest>>processarTesteXML()");
		try {
			geraXml.log("Importando: XML");
			InputStream in = Class.forName(
					"br.gov.jfrj.xml.test.ImportarXmlTest")
					.getResourceAsStream(nomeArquivo);
			geraXml.importarXml(in);
			geraXml.log("Importando: BD");
			geraXml.importarTabela();
			geraXml.log("Gravando alterações");
			geraXml.gravar(dt);

		} catch (Exception e) {
			e.printStackTrace();
			geraXml.log(e.getMessage());
		}
		geraXml.log(" ---- Fim do Processamento --- ");
		geraXml.logEnd();

	}
}
