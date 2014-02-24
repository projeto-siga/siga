package model;

import static model.TestUtil.criarSoft;
import static model.TestUtil.manterSoft;
import static model.TestUtil.sigadoc;
import static model.TestUtil.sigawf;
import static model.TestUtil.systrab;

import java.util.List;

import models.SrAcao;
import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrItemConfiguracao;
import models.SrSubTipoConfiguracao;
import models.SrTipoAtributo;

import org.hibernate.Session;

import com.google.code.play.selenium.step.AssertEqualsStep;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class TestUtil {

	public static CpOrgaoUsuario rj() {
		CpOrgaoUsuario rj = (CpOrgaoUsuario) CpOrgaoUsuario.findById(1L);
		if (rj == null) {
			rj = new CpOrgaoUsuario();
			rj.setIdOrgaoUsu(1L);
			rj.setNmOrgaoUsu("Seção Judiciária do Rio de Janeiro");
			rj.setSigla("RJ");
			rj.setAcronimoOrgaoUsu("JFRJ");
			rj.save();
		}
		return rj;
	}

	public static CpOrgaoUsuario t2() {
		CpOrgaoUsuario t2 = (CpOrgaoUsuario) CpOrgaoUsuario.findById(3L);
		if (t2 == null) {
			t2 = new CpOrgaoUsuario();
			t2.setIdOrgaoUsu(3L);
			t2.setNmOrgaoUsu("Tribunal Regional Federal da 2ª Região");
			t2.setSigla("T2");
			t2.setAcronimoOrgaoUsu("TRF2");
			t2.save();
		}
		return t2;
	}

	public static CpLocalidade cidadeRio() {
		CpLocalidade rio = (CpLocalidade) CpLocalidade.findById(15L);
		if (rio == null) {
			rio = new CpLocalidade();
			rio.setIdLocalidade(15L);
			rio.setNmLocalidade("Rio de Janeiro");
			rio.setSigla("RJ");
			rio.save();
		}
		return rio;
	}

	public static CpComplexo barroso() {
		CpComplexo barroso = (CpComplexo) CpOrgaoUsuario.findById(3L);
		if (barroso == null) {
			barroso = new CpComplexo();
			barroso.setLocalidade(cidadeRio());
			barroso.setNomeComplexo("Almirante Barroso");
			barroso.setOrgaoUsuario(rj());
			barroso.save();
		}
		return barroso;
	}

	public static DpLotacao csis() throws Exception {
		DpLotacao csis = DpLotacao.find("bySiglaLotacao", "CSIS").first();
		if (csis == null) {
			csis = new DpLotacao();
			csis.setSiglaLotacao("CSIS");
			csis.setOrgaoUsuario(rj());
			csis.setLotacaoPai(csis);
			csis.setNomeLotacao("Coordenadoria de Sistemas");
			csis.salvar();
			csis.refresh();
		}
		return csis;
	}

	public static DpLotacao sesia() throws Exception {
		DpLotacao sesia = DpLotacao.find("bySiglaLotacao", "SESIA").first();
		if (sesia == null) {
			sesia = new DpLotacao();
			sesia.setSiglaLotacao("SESIA");
			sesia.setOrgaoUsuario(rj());
			sesia.setLotacaoPai(csis());
			sesia.setNomeLotacao("Seção de Sistemas Administrativos");
			sesia.salvar();
			sesia.refresh();
		}
		return sesia;
	}

	public static DpLotacao segep() throws Exception {
		DpLotacao segep = DpLotacao.find("bySiglaLotacao", "SEGEP").first();
		if (segep == null) {
			segep = new DpLotacao();
			segep.setSiglaLotacao("SEGEP");
			segep.setOrgaoUsuario(rj());
			segep.setLotacaoPai(csis());
			segep.setNomeLotacao("Seção de Sistemas de Gestão de Pessoas");
			segep.salvar();
			segep.refresh();
		}
		return segep;
	}

	public static DpLotacao sesuti() throws Exception {
		DpLotacao sesuti = DpLotacao.find("bySiglaLotacao", "SESUTI").first();
		if (sesuti == null) {
			sesuti = new DpLotacao();
			sesuti.setSiglaLotacao("SESUTI");
			sesuti.setOrgaoUsuario(t2());
			sesuti.setNomeLotacao("Seção de Suporte do TRF");
			sesuti.salvar();
			sesuti.refresh();
		}
		return sesuti;
	}

	public static DpPessoa eeh() throws Exception {
		DpPessoa eeh = DpPessoa.find("bySiglaPessoa", "EEH").first();
		if (eeh == null) {
			eeh = new DpPessoa();
			eeh.setNomePessoa("Edson");
			eeh.setSiglaPessoa("EEH");
			eeh.setOrgaoUsuario(rj());
			eeh.setLotacao(sesia());
			eeh.salvar();
			eeh.refresh();
		}
		return eeh;
	}

	public static DpPessoa funcionarioTRF() throws Exception {
		DpPessoa fff = DpPessoa.find("bySiglaPessoa", "FFF").first();
		if (fff == null) {
			fff = new DpPessoa();
			fff.setNomePessoa("Pessoa Simulada");
			fff.setSiglaPessoa("FFF");
			fff.setOrgaoUsuario(t2());
			fff.setLotacao(sesuti());
			fff.salvar();
			fff.refresh();
		}
		return fff;
	}

	public static CpTipoConfiguracao tipoConfigDesignacao() throws Exception {
		CpTipoConfiguracao design = CpTipoConfiguracao.findById(300L);
		if (design == null) {
			design = new CpTipoConfiguracao();
			design.setIdTpConfiguracao(300L);
			design.setDscTpConfiguracao("Designação");
			design.salvar();
		}
		return design;
	}

	public static CpTipoConfiguracao tipoConfigAssociacao() throws Exception {
		CpTipoConfiguracao assoc = CpTipoConfiguracao.findById(301L);
		if (assoc == null) {
			assoc = new CpTipoConfiguracao();
			assoc.setIdTpConfiguracao(301L);
			assoc.setDscTpConfiguracao("Associação");
			assoc.salvar();
		}
		return assoc;
	}

	public static SrItemConfiguracao software() throws Exception {
		SrItemConfiguracao soft = SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.00.00").first();
		if (soft == null) {
			soft = new SrItemConfiguracao();
			soft.descrItemConfiguracao = "Software";
			soft.siglaItemConfiguracao = "99.00.00";
			soft.tituloItemConfiguracao = "Software";
			soft.salvar();
			soft.refresh();
		}
		return soft;
	}

	public static SrItemConfiguracao sysdoc() throws Exception {
		software();
		SrItemConfiguracao sysDoc = SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.00").first();
		if (sysDoc == null) {
			sysDoc = new SrItemConfiguracao();
			sysDoc.descrItemConfiguracao = "Sistema de Gestão Documental";
			sysDoc.siglaItemConfiguracao = "99.01.00";
			sysDoc.tituloItemConfiguracao = "Sistema de Gestão Documental";
			sysDoc.salvar();
			sysDoc.refresh();
		}
		return sysDoc;
	}

	public static SrItemConfiguracao sigadoc() throws Exception {
		sysdoc();
		SrItemConfiguracao sysDoc = SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.01").first();
		if (sysDoc == null) {
			sysDoc = new SrItemConfiguracao();
			sysDoc.descrItemConfiguracao = "Siga-Doc";
			sysDoc.siglaItemConfiguracao = "99.01.01";
			sysDoc.tituloItemConfiguracao = "Siga-Doc";
			sysDoc.salvar();
			sysDoc.refresh();
		}
		return sysDoc;
	}

	public static SrItemConfiguracao sigawf() throws Exception {
		sysdoc();
		SrItemConfiguracao sigawf = SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.02").first();
		if (sigawf == null) {
			sigawf = new SrItemConfiguracao();
			sigawf.descrItemConfiguracao = "Siga-Wf";
			sigawf.siglaItemConfiguracao = "99.01.02";
			sigawf.tituloItemConfiguracao = "Siga-Wf";
			sigawf.salvar();
			sigawf.refresh();
		}
		return sigawf;
	}

	public static SrItemConfiguracao systrab() throws Exception {
		software();
		SrItemConfiguracao systrab = SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.02.00").first();
		if (systrab == null) {
			systrab = new SrItemConfiguracao();
			systrab.descrItemConfiguracao = "Sistema de Gestão do Trabalho";
			systrab.siglaItemConfiguracao = "99.02.00";
			systrab.tituloItemConfiguracao = "Sistema de Gestão do Trabalho";
			systrab.salvar();
			systrab.refresh();
		}
		return systrab;
	}

	public static SrAcao acaoSoft() throws Exception {
		SrAcao acaoSoft = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.00")
				.first();
		if (acaoSoft == null) {
			acaoSoft = new SrAcao();
			acaoSoft.descrAcao = "Ação para Software";
			acaoSoft.siglaAcao = "99.00";
			acaoSoft.tituloAcao = "Ação para Software";
			acaoSoft.salvar();
			acaoSoft.refresh();
		}
		return acaoSoft;
	}

	public static SrAcao criarSoft() throws Exception {
		acaoSoft();
		SrAcao criar = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.01")
				.first();
		if (criar == null) {
			criar = new SrAcao();
			criar.descrAcao = "Criar";
			criar.siglaAcao = "99.01";
			criar.tituloAcao = "Criar";
			criar.salvar();
			criar.refresh();
		}
		return criar;
	}

	public static SrAcao manterSoft() throws Exception {
		acaoSoft();
		SrAcao manter = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.02")
				.first();
		if (manter == null) {
			manter = new SrAcao();
			manter.descrAcao = "Manter";
			manter.siglaAcao = "99.02";
			manter.tituloAcao = "Manter";
			manter.salvar();
			manter.refresh();
		}
		return manter;
	}
	
	public static SrTipoAtributo attPrazo() throws Exception{
		SrTipoAtributo prazo = SrTipoAtributo.find("byNomeTipoAtributoAndHisDtFimIsNull", "Prazo")
				.first();
		if (prazo == null) {
			prazo = new SrTipoAtributo();
			prazo.nomeTipoAtributo = "Prazo";
			prazo.descrTipoAtributo = "Prazo para atendimento";
			prazo.salvar();
			prazo.refresh();
		}
		return prazo;
	}
	
	public static SrTipoAtributo attNumDoc() throws Exception{
		SrTipoAtributo numDoc = SrTipoAtributo.find("byNomeTipoAtributoAndHisDtFimIsNull", "Numero do Documento")
				.first();
		if (numDoc == null) {
			numDoc = new SrTipoAtributo();
			numDoc.nomeTipoAtributo = "Numero do Documento";
			numDoc.descrTipoAtributo = "Numero do Documento";
			numDoc.salvar();
			numDoc.refresh();
		}
		return numDoc;
	}

	public static void prepararSessao() throws Exception {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		CpDao.getInstance(playSession);
		HibernateUtil.setSessao(playSession);
		Cp.getInstance().getConf().limparCacheSeNecessario();
	}

	public static void limparBase() throws Exception {
		SrAcao.deleteAll();
		SrItemConfiguracao.deleteAll();
		DpPessoa.deleteAll();
		DpLotacao.deleteAll();
		CpOrgaoUsuario.deleteAll();
	}

	public static void apagaCacheDesignacao() throws Exception {
		SrConfiguracaoBL
				.get()
				.limparCache(
						(CpTipoConfiguracao) CpTipoConfiguracao
								.findById(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
	}

}
