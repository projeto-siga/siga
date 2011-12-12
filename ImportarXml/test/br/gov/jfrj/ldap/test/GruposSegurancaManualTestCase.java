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
package br.gov.jfrj.ldap.test;

import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.importar.AdGrupo;
import br.gov.jfrj.importar.AdGrupoDeSeguranca;
import br.gov.jfrj.importar.AdUsuario;
import br.gov.jfrj.importar.SigaCpSinc;
import br.gov.jfrj.ldap.sinc.LdapDaoSinc;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class GruposSegurancaManualTestCase extends TestCase {

	private boolean sincronizarLDAP = true;
	private static CpDao dao;
	private static Long idConfGravada;

	public GruposSegurancaManualTestCase() throws Exception {
		if (dao == null) {
			AnnotationConfiguration cfg = CpDao.criarHibernateCfg(
					"jdbc:oracle:thin:@mclaren:1521:mcl", "corporativo",
					"corporativo");
			HibernateUtil.configurarHibernate(cfg, "");

			dao = CpDao.getInstance();
		}

	}

	private void exibePermissoesConsole() throws Exception {
		Set<CpConfiguracao> configs = Cp
				.getInstance()
				.getConf()
				.getListaPorTipo(
						CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
		for (CpConfiguracao c : configs) {
			if (c.getHisAtivo() == 1) {
				System.out
						.printf(
								"%s, %s, %s, %s, %s\n",
								c.getDpPessoa().getNomePessoa(),
								c.getCpSituacaoConfiguracao() == null ? "Não foi definido se pode/não pode"
										: c.getCpSituacaoConfiguracao()
												.getDscSitConfiguracao(), c
										.getCpTipoConfiguracao()
										.getDscTpConfiguracao(), c.getLotacao()
										.getSigla(), c.getCpServico()
										.getSigla());
			}
		}
	}

	public void testInserirConfiguracaoGSManual() throws Exception {
		HibernateUtil.iniciarTransacao();
		CpConfiguracao conf = new CpConfiguracao();

		DpPessoa pes = dao.consultarPorCpfMatricula(0L, 13286);
		CpSituacaoConfiguracao sitConf = dao.consultar(
				CpSituacaoConfiguracao.SITUACAO_PODE,
				CpSituacaoConfiguracao.class, false);
		CpTipoConfiguracao tpConf = dao.consultar(
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO,
				CpTipoConfiguracao.class, false);
		DpLotacao lot = dao.consultar(20643L, DpLotacao.class, false);
		CpServico svc = dao.consultar(8L, CpServico.class, false);

		conf.setDpPessoa(pes);
		// conf.setCpSituacaoConfiguracao(sitConf);
		conf.setCpTipoConfiguracao(tpConf);
		conf.setLotacao(lot);
		conf.setCpServico(svc);

		dao.gravar(conf);

		HibernateUtil.commitTransacao();
		assertNotNull(conf.getId());

		idConfGravada = conf.getId();
		System.out.println("Configuracao gravada! ID: " + idConfGravada);

	}

	public void testVerificarConfPessoasPorLotacao() {
		DpLotacao lot1 = dao.consultar(20643L, DpLotacao.class, false); // 01vf
		DpLotacao lot2 = dao.consultar(20917L, DpLotacao.class, false); // sesie

		assertTrue(Cp.getInstance().getConf().getPessoasGrupoSegManual(lot1)
				.size() > 0);
		assertTrue(Cp.getInstance().getConf().getPessoasGrupoSegManual(lot2)
				.size() == 0);
	}

	public void testVerificarConfiguracaoPode() throws Exception {
		exibePermissoesConsole();

		DpPessoa pes = dao.consultarPorCpfMatricula(0L, 13286);
		DpLotacao lot = dao.consultar(20643L, DpLotacao.class, false);
		CpServico svc = dao.consultar(8L, CpServico.class, false);

		assertTrue(Cp.getInstance().getConf().podePorConfiguracao(pes, lot,
				svc,
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO));

	}

	public void testSincronizarLDAP() throws Exception {
		if (!sincronizarLDAP) {
			fail("Sincronização com LDAP desativada!");
		}
		SigaCpSinc sinc = new SigaCpSinc();
		String[] params = { "-desenv", "-sjrj", "-ldap" };
		sinc.main(params);

		AdUsuario u = (AdUsuario) LdapDaoSinc
				.getInstance()
				.pesquisarObjeto(
						"CN=RJ13286,OU=Usuarios,OU=Gestao de Identidade,DC=csis,DC=local",
						"OU=Grupos de Seguranca,OU=Gestao de Identidade,DC=csis,DC=local")
				.get(0);
		AdGrupoDeSeguranca gs = null;
		for (AdGrupo g : u.getGruposPertencentes()) {
			if ((g instanceof AdGrupoDeSeguranca)
					&& (g.getNome().equalsIgnoreCase("01vf_juiz_gs"))) {
				gs = (AdGrupoDeSeguranca) g;
			}
		}

		assertNotNull(gs);
	}

	public void testVerificarConfiguracaoNaoPode_LotacaoDiferente()
			throws Exception {

		DpPessoa pes = dao.consultarPorCpfMatricula(0L, 13286);
		DpLotacao lot = dao.consultar(20917L, DpLotacao.class, false);
		CpServico svc = dao.consultar(8L, CpServico.class, false);

		assertFalse(Cp.getInstance().getConf().podePorConfiguracao(pes, lot,
				svc,
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO));

	}

	public void testVerificarConfiguracaoNaoPode_ConfDesativada()
			throws Exception {
		HibernateUtil.iniciarTransacao();

		CpConfiguracao conf = dao.consultar(idConfGravada,
				CpConfiguracao.class, false);
		conf.setHisAtivo(0);
		conf.setHisDtFim(new Date());

		HibernateUtil.commitTransacao();

		DpPessoa pes = dao.consultarPorCpfMatricula(0L, 13286);
		DpLotacao lot = dao.consultar(20643L, DpLotacao.class, false);
		CpServico svc = dao.consultar(8L, CpServico.class, false);

		assertFalse(Cp.getInstance().getConf().podePorConfiguracao(pes, lot,
				svc,
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO));

	}

	public void testExcluirConfiguracaoGSManual() throws Exception {
		HibernateUtil.iniciarTransacao();
		CpConfiguracao conf = dao.consultar(idConfGravada,
				CpConfiguracao.class, false);
		dao.excluir(conf);
		HibernateUtil.commitTransacao();
		System.out.println("Configuracao excluída! ID: " + idConfGravada);
	}

	public void testVerificarConfiguracaoNaoPode_ConfExcluida()
			throws Exception {
		DpPessoa pes = dao.consultarPorCpfMatricula(0L, 13286);
		DpLotacao lot = dao.consultar(20643L, DpLotacao.class, false);
		CpServico svc = dao.consultar(8L, CpServico.class, false);

		assertFalse(Cp.getInstance().getConf().podePorConfiguracao(pes, lot,
				svc,
				CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO));
	}

}
