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
		Configuration cfg;
		cfg = ExDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
		HibernateUtil.configurarHibernate(cfg);
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
		// HibernateUtil.getSessao();
		//ModeloDao.freeInstance();
		//ExDao.getInstance();
		//Ex.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		//if (!ExDao.getInstance().sessaoEstahAberta())
			//throw new AplicacaoException("Erro: sessão do Hibernate está fechada.");

		ExDao.iniciarTransacao();
		
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		//flt.setSigla("TRF2-MEM-2013/00001-A");
		
		flt.setAnoEmissao(2013L);
		flt.setIdFormaDoc(2);
		flt.setIdTipoMobil(1L);
		flt.setIdOrgaoUsu(3L);
		flt.setNumExpediente(1L);
		
//		ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
		
		ExMobil mob = ExDao.getInstance().consultar(746L, ExMobil.class, false);
		System.out.println(mob);
		
		System.out.println(ExDao.getInstance().listarOrgaosUsuarios());
		
//		ExMobilDaoFiltro flt2 = new ExMobilDaoFiltro();
//		flt.setSigla("TRF2-MEM-2013/00002-A");
//		ExMobil mob2 = ExDao.getInstance().consultarPorSigla(flt2);
//		System.out.println(mob2);
//
//		List<CpConfiguracao> results = (List<CpConfiguracao>) ExDao.getInstance()
//				.consultarConfiguracoesAtivas();
//		
//		System.out.println("Data e hora da ultima atualização - "
//				+ ExDao.getInstance().consultarDataUltimaAtualizacao());
//
//		List<CpConfiguracao> result2s = (List<CpConfiguracao>) ExDao.getInstance()
//				.consultarConfiguracoesAtivas();
//		
//		System.out.println("Data e hora da ultima atualização - "
//				+ ExDao.getInstance().consultarDataUltimaAtualizacao());
		
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

			// Ex.getInstance().getBL().numerarTudo(1);

			String assMov = confereAssinaturasDeMovimentacoes(dao);

			String assDoc = confereAssinaturasDeDocumentos(dao);

			System.out.println("\n\n\n\n\n\n");
			System.out.println(assMov);
			System.out.println();
			System.out.println(assDoc);
			System.out.println();

			System.exit(0);

			InputStream is = ExDaoTest.class.getResourceAsStream("anexo.html");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {

					is.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

			String html = sb.toString();
			// html = new ProcessadorHtml().canonicalizarHtml(html, true, false,
			// false, false, true);
			FOP geradorFop = new FOP();
			byte[] pdf = geradorFop.converter(html, ConversorHtml.PDF);

			DpPessoa pesSigla = new DpPessoa();
			pesSigla.setSesbPessoa("RJ");
			pesSigla.setMatricula(13635L);
			DpPessoa pes = dao.consultarPorSigla(pesSigla);

			System.out.println(pes.getSigla() + " - " + pes.getDescricao());
			System.out.println(pes.getCargo().getDescricao());
			System.out.println(pes.getFuncaoConfianca().getDescricao());
			System.out.println(pes.getLotacao().getSigla() + " - "
					+ pes.getLotacao().getDescricao());

			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla("RJ13635");
			System.out.print("consultarQuantidade: ");
			System.out.println(dao.consultarQuantidade((DaoFiltro) flt));

			Collection<ExMovimentacao> movs = dao
					.consultarMovimentacoesPorLotacaoEntreDatas(pes
							.getLotacao(), new Date(2008, 1, 1), new Date(2008,
							2, 1));
			for (ExMovimentacao mov : movs)
				System.out.println(mov.getExDocumento().getCodigo());

			ExDao.freeInstance();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param dao
	 * @throws Exception
	 */
	private static String confereAssinaturasDeDocumentos(ExDao dao) throws Exception {
		long cDoc = 0;
		long cErr = 0;
		// Rotina para validar todas as assinaturas digitais de documentos
		{
			Query qry = dao
					.getSessao()
					.createQuery(
							"from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov = 11 order by mov.idMov")
					.setMaxResults(100);
			int indice = 0;
			while (true) {
				qry.setFirstResult(indice);
				List<ExMovimentacao> movs = (List<ExMovimentacao>) qry.list();
				for (ExMovimentacao mov : movs) {
					if (mov.isCancelada())
						continue;
					try {
						cDoc++;
						String sNome = Ex
								.getInstance()
								.getBL()
								.verificarAssinatura(
										mov.getExDocumento()
												.getConteudoBlobPdf(),
										mov.getConteudoBlobMov2(),
										mov.getConteudoTpMov(), mov.getData());
						// System.out.println(mov.getExDocumento().getCodigo()
						// + ": " + mov.getDtRegMovDDMMYY() + " - "
						// + mov.getSubscritor().getSiglaCompleta()
						// + " - " + sNome);
						// System.out.println("OK! - " + sNome);
					} catch (final Exception e) {
						cErr++;
						String s = mov.getExDocumento().getCodigo()
								+ ";"
								+ mov.getDtRegMovDDMMYY()
								+ ";"
								+ mov.getSubscritor().getSiglaCompleta()
								+ ";"
								+ mov.getSubscritor().getDescricao()
								+ ";"
								+ clean(e.getMessage())
								+ ";"
								+ clean((e.getCause() != null ? e.getCause()
										.getMessage() : ""))
								+ ";"
								+ clean((e.getCause() != null
										&& e.getCause().getCause() != null ? e
										.getCause().getCause().getMessage()
										: ""));
						System.out.println(s);
					}
				}
				indice += movs.size();
				dao.getSessao().clear();

				if (indice > 100000)
					break;
			}
		}
		return "Erro em assinaturas de documentos: " + cErr + " em " + cDoc;
	}

	private static String confereAssinaturasDeMovimentacoes(ExDao dao) {
		long cMov = 0;
		long cErr = 0;
		// Rotina para validar todas as assinaturas digitais de movimentacoes
		{
			Query qry = dao
					.getSessao()
					.createQuery(
							"from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov = 22 order by mov.idMov")
					.setMaxResults(100);
			int indice = 0;
			while (true) {
				qry.setFirstResult(indice);
				List<ExMovimentacao> movs = (List<ExMovimentacao>) qry.list();
				for (ExMovimentacao mov : movs) {
					if (mov.isCancelada())
						continue;
					try {
						cMov++;
						String sNome = Ex
								.getInstance()
								.getBL()
								.verificarAssinatura(
										mov.getExMovimentacaoRef()
												.getConteudoBlobpdf(),
										mov.getConteudoBlobMov2(),
										mov.getConteudoTpMov(), mov.getData());
						// System.out.println(mov.getExDocumento().getCodigo()
						// + ": " + mov.getDtRegMovDDMMYY() + " - "
						// + mov.getSubscritor().getSiglaCompleta()
						// + " - " + sNome);
						// System.out.println("OK! - " + sNome);
					} catch (final Exception e) {
						cErr++;
						String s = mov.getExMovimentacaoRef().getReferencia()
								+ ";"
								+ mov.getDtRegMovDDMMYY()
								+ ";"
								+ mov.getSubscritor().getSiglaCompleta()
								+ ";"
								+ mov.getSubscritor().getDescricao()
								+ ";"
								+ clean(e.getMessage())
								+ ";"
								+ clean((e.getCause() != null ? e.getCause()
										.getMessage() : ""))
								+ ";"
								+ clean((e.getCause() != null
										&& e.getCause().getCause() != null ? e
										.getCause().getCause().getMessage()
										: ""));
						System.out.println(s);
					}
				}
				indice += movs.size();
				dao.getSessao().clear();

				if (indice > 100000)
					break;
			}
		}
		return "Erro em assinaturas de movimentações: " + cErr + " em " + cMov;
	}

	private static String clean(String s) {
		return s.replace("\n", " ").replace("\r", " ").replace(";", ",").trim();
	}
}
