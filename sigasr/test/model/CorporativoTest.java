package model;

import static org.junit.Assert.*;

import java.util.List;

import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrAcao;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Historico;

import play.test.UnitTest;

public class CorporativoTest extends UnitTest {

	@Test
	public void criarOrgaoUsuario() throws Exception {
		CpOrgaoUsuario orgaoUsu = new CpOrgaoUsuario();
		orgaoUsu.setIdOrgaoUsu(1L);
		orgaoUsu.setNmOrgaoUsu("Seção Judiciária do Rio de Janeiro");
		orgaoUsu.setSigla("RJ");
		orgaoUsu.setAcronimoOrgaoUsu("JFRJ");
		orgaoUsu.save();

		CpOrgaoUsuario orgaoUsu3 = new CpOrgaoUsuario();
		orgaoUsu3.setIdOrgaoUsu(3L);
		orgaoUsu3.setNmOrgaoUsu("Tribunal Regional Federal da 2ª Região");
		orgaoUsu3.setSigla("T2");
		orgaoUsu3.setAcronimoOrgaoUsu("TRF2");
		orgaoUsu3.save();
		assertTrue(CpOrgaoUsuario.findAll().size() == 2);
	}

	@Test
	public void criarlotacoes() throws Exception {

		CpOrgaoUsuario rj = CpOrgaoUsuario.findById(1L);
		CpOrgaoUsuario t2 = CpOrgaoUsuario.findById(3L);

		DpLotacao sesuti = new DpLotacao();
		sesuti.setSiglaLotacao("SESUTI");
		sesuti.setOrgaoUsuario(t2);
		sesuti.setNomeLotacao("Seção de Suporte Técnico");
		sesuti.salvar();

		DpLotacao seusu = new DpLotacao();
		seusu.setSiglaLotacao("SEUSU");
		seusu.setOrgaoUsuario(rj);
		seusu.setNomeLotacao("Seção do Usuário");
		seusu.salvar();

		DpLotacao csis = new DpLotacao();
		csis.setSiglaLotacao("CSIS");
		csis.setOrgaoUsuario(rj);
		csis.setNomeLotacao("Cordenadoria de Sistemas");
		csis.salvar();

		DpLotacao sesia = new DpLotacao();
		sesia.setSiglaLotacao("SESIA");
		sesia.setOrgaoUsuario(rj);
		sesia.setLotacaoPai(csis);
		sesia.setNomeLotacao("Seção de Sistemas Administrativos");
		sesia.salvar();

		DpLotacao segep = new DpLotacao();
		segep.setSiglaLotacao("SEGEP");
		segep.setOrgaoUsuario(rj);
		segep.setLotacaoPai(csis);
		segep.setNomeLotacao("Seção de Sistemas de Gestão de Pessoas");
		segep.salvar();

		assertTrue(DpLotacao.findAll().size() == 5);
	}

	@Test
	public void criarPessoa() throws Exception {
		DpPessoa ed = new DpPessoa();
		System.out.println(ed.getIdPessoa());
		ed.setNomePessoa("Edson");
		ed.setSiglaPessoa("EEH");
		ed.setOrgaoUsuario((CpOrgaoUsuario) CpOrgaoUsuario.findById(1L));
		ed.setLotacao((DpLotacao) DpLotacao.find("bySiglaLotacao", "SESIA")
				.first());
		ed.salvar();
		assertTrue(DpPessoa.findAll().size() == 1);

	}

	@Test
	public void criarTipoConfiguracao() throws Exception {
		CpTipoConfiguracao design = new CpTipoConfiguracao();
		design.setIdTpConfiguracao(300L);
		design.setDscTpConfiguracao("Designação");
		design.salvar();

		CpTipoConfiguracao assoc = new CpTipoConfiguracao();
		assoc.setIdTpConfiguracao(301L);
		assoc.setDscTpConfiguracao("Associação");
		assoc.salvar();

		assertTrue(CpTipoConfiguracao.all().fetch().size() == 2);
	}

}
