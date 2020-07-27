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
package br.gov.jfrj.siga.hibernate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.FOP;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.dao.DaoFiltro;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class ExDaoTest extends TestCase {

	private ExDao dao = null;

	public ExDaoTest() throws Exception {
		// HibernateUtil
		// .configurarHibernate("/br/gov/jfrj/siga/hibernate/hibernate.cfg.xml",
		// ExMarca.class, CpMarca.class);
		/*CpAmbienteEnumBL ambiente = CpAmbienteEnumBL.DESENVOLVIMENTO;
		Cp.getInstance().getProp().setPrefixo(ambiente.getSigla());
		AnnotationConfiguration cfg = ExDao.criarHibernateCfg(ambiente);
		HibernateUtil.configurarHibernate(cfg, "");
*/
		
		
//		Configuration cfg;
//		cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
//		HibernateUtil.configurarHibernate(cfg);
	}

	public void testaPattern() {
		Matcher m = Pattern.compile("(https?://)([a-zA-Z_0-9]+)(:[0-9]{1,5})?(/.*)?").matcher("http://localhost:9000/sigasr");
		System.out.println(m.find() + "  --" + m.group(1) + "--" + m.group(2) + "--" + m.group(3) + "--" + m.group(4)+"--");

	}

	public static void main(String[] a) throws Exception {
		ExDaoTest test = new ExDaoTest();
		test.testBugFoundSharedReferencesToACollection();
	}
	
	public void testBugFoundSharedReferencesToACollection() {
		if (true)
			return;
		
		ExDao.iniciarTransacao();
		
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		//flt.setSigla("TRF2-MEM-2013/00001-A");
		
		flt.setAnoEmissao(2013L);
		flt.setIdFormaDoc(2L);
		flt.setIdTipoMobil(1L);
		flt.setIdOrgaoUsu(3L);
		flt.setNumExpediente(1L);
		
//		ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
		
		ExMobil mob = ExDao.getInstance().consultar(746L, ExMobil.class, false);
		System.out.println(mob);
		
		System.out.println(ExDao.getInstance().listarOrgaosUsuarios());
	}
	
	

	/**
	 * @param args
	 */
	public void testDataUltimaAtualizacao() {
		// TODO Auto-generated method stub

		try {
			System.out.println("Data e hora da ultima atualização - "
					+ dao.consultarDataUltimaAtualizacao());

			// ExModelo mod = dao.consultar(512L, ExModelo.class, false);
			// mod.setNmMod(mod.getNmMod() + " - teste.");
			// dao.iniciarTransacao();
			// dao.gravar(mod);
			// dao.commitTransacao();

			List<ExConfiguracao> l = dao.listarExConfiguracoes();
			if (true)
				return;

			DpPessoa pesSigla1 = new DpPessoa();
			pesSigla1.setSesbPessoa("RJ");
			pesSigla1.setMatricula(13635L);
			DpPessoa pes1 = dao.consultarPorSigla(pesSigla1);
			for (Object[] o : (List<Object[]>) dao.consultarPaginaInicial(pes1,
					pes1.getLotacao(), 1)) {
				System.out.println(o[0] + " " + (String) o[1] + " " + o[2]
						+ " " + o[3]);

			}

			if (true)
				return;

			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String clean(String s) {
		return s.replace("\n", " ").replace("\r", " ").replace(";", ",").trim();
	}
}
