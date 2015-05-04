package test.model;

//import br.gov.jfrf.siga.sr.models.SrAcao;
//import br.gov.jfrf.siga.sr.models.SrAtributo;
//import br.gov.jfrf.siga.sr.models.SrConfiguracaoBL;
//import br.gov.jfrf.siga.sr.models.SrItemConfiguracao;
//import br.gov.jfrf.siga.sr.models.SrTipoMovimentacao;
//import br.gov.jfrf.siga.sr.models.SrTipoPergunta;
//
//import org.hibernate.Session;
//
//import play.db.jpa.JPA;
//import br.gov.jfrj.siga.cp.CpComplexo;
//import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
//import br.gov.jfrj.siga.cp.bl.Cp;
//import br.gov.jfrj.siga.dp.CpLocalidade;
//import br.gov.jfrj.siga.dp.CpMarcador;
//import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
//import br.gov.jfrj.siga.dp.CpTipoMarca;
//import br.gov.jfrj.siga.dp.CpTipoMarcador;
//import br.gov.jfrj.siga.dp.DpLotacao;
//import br.gov.jfrj.siga.dp.DpPessoa;
//import br.gov.jfrj.siga.dp.dao.CpDao;
//import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class TestUtil {

//	public static CpOrgaoUsuario rj() {
//		CpOrgaoUsuario rj = (CpOrgaoUsuario) CpOrgaoUsuario.findById(1L);
//		if (rj == null) {
//			rj = new CpOrgaoUsuario();
//			rj.setIdOrgaoUsu(1L);
//			rj.setNmOrgaoUsu("Se��o Judici�ria do Rio de Janeiro");
//			rj.setSigla("RJ");
//			rj.setAcronimoOrgaoUsu("JFRJ");
//			rj.save();
//		}
//		return rj;
//	}
//
//	public static CpOrgaoUsuario t2() {
//		CpOrgaoUsuario t2 = (CpOrgaoUsuario) CpOrgaoUsuario.findById(3L);
//		if (t2 == null) {
//			t2 = new CpOrgaoUsuario();
//			t2.setIdOrgaoUsu(3L);
//			t2.setNmOrgaoUsu("Tribunal Regional Federal da 2� Regi�o");
//			t2.setSigla("T2");
//			t2.setAcronimoOrgaoUsu("TRF2");
//			t2.save();
//		}
//		return t2;
//	}
//
//	public static CpLocalidade cidadeRio() {
//		CpLocalidade rio = (CpLocalidade) CpLocalidade.findById(15L);
//		if (rio == null) {
//			rio = new CpLocalidade();
//			rio.setIdLocalidade(15L);
//			rio.setNmLocalidade("Rio de Janeiro");
//			rio.setSigla("RJ");
//			rio.save();
//		}
//		return rio;
//	}
//
//	public static CpComplexo barroso() {
//		CpComplexo barroso = (CpComplexo) CpComplexo.find("byNomeComplexo",
//				"Almirante Barroso").first();
//		if (barroso == null) {
//			barroso = new CpComplexo();
//			barroso.setIdComplexo(1L);
//			barroso.setLocalidade(cidadeRio());
//			barroso.setNomeComplexo("Almirante Barroso");
//			barroso.setOrgaoUsuario(rj());
//			barroso.save();
//		}
//		return barroso;
//	}
//
//	public static DpLotacao csis() throws Exception {
//		DpLotacao csis = DpLotacao.find("bySiglaLotacao", "CSIS").first();
//		if (csis == null) {
//			csis = new DpLotacao();
//			csis.setSiglaLotacao("CSIS");
//			csis.setOrgaoUsuario(rj());
//			csis.setLotacaoPai(csis);
//			csis.setNomeLotacao("Coordenadoria de Sistemas");
//			csis.salvar();
//			csis.refresh();
//		}
//		return csis;
//	}
//
//	public static DpLotacao sesia() throws Exception {
//		DpLotacao sesia = DpLotacao.find("bySiglaLotacao", "SESIA").first();
//		if (sesia == null) {
//			sesia = new DpLotacao();
//			sesia.setSiglaLotacao("SESIA");
//			sesia.setOrgaoUsuario(rj());
//			sesia.setLotacaoPai(csis());
//			sesia.setNomeLotacao("Se��o de Sistemas Administrativos");
//			sesia.salvar();
//			sesia.refresh();
//		}
//		return sesia;
//	}
//
//	public static DpLotacao segep() throws Exception {
//		DpLotacao segep = DpLotacao.find("bySiglaLotacao", "SEGEP").first();
//		if (segep == null) {
//			segep = new DpLotacao();
//			segep.setSiglaLotacao("SEGEP");
//			segep.setOrgaoUsuario(rj());
//			segep.setLotacaoPai(csis());
//			segep.setNomeLotacao("Se��o de Sistemas de Gest�o de Pessoas");
//			segep.salvar();
//			segep.refresh();
//		}
//		return segep;
//	}
//
//	public static DpLotacao sesuti() throws Exception {
//		DpLotacao sesuti = DpLotacao.find("bySiglaLotacao", "SESUTI").first();
//		if (sesuti == null) {
//			sesuti = new DpLotacao();
//			sesuti.setSiglaLotacao("SESUTI");
//			sesuti.setOrgaoUsuario(t2());
//			sesuti.setNomeLotacao("Se��o de Suporte do TRF");
//			sesuti.salvar();
//			sesuti.refresh();
//		}
//		return sesuti;
//	}
//
//	public static DpPessoa eeh() throws Exception {
//		DpPessoa eeh = DpPessoa.find("bySiglaPessoa", "EEH").first();
//		if (eeh == null) {
//			eeh = new DpPessoa();
//			eeh.setNomePessoa("Edson");
//			eeh.setSiglaPessoa("EEH");
//			eeh.setEmailPessoa("eeh@abc.com");
//			eeh.setOrgaoUsuario(rj());
//			eeh.setLotacao(sesia());
//			eeh.salvar();
//			eeh.refresh();
//		}
//		return eeh;
//	}
//
//	public static DpPessoa funcionarioTRF() throws Exception {
//		DpPessoa fff = DpPessoa.find("bySiglaPessoa", "FFF").first();
//		if (fff == null) {
//			fff = new DpPessoa();
//			fff.setNomePessoa("Pessoa Simulada");
//			fff.setEmailPessoa("usuTrf@abc.com");
//			fff.setSiglaPessoa("FFF");
//			fff.setOrgaoUsuario(t2());
//			fff.setLotacao(sesuti());
//			fff.salvar();
//			fff.refresh();
//		}
//		return fff;
//	}
//
//	public static CpTipoConfiguracao tipoConfigDesignacao() throws Exception {
//		CpTipoConfiguracao design = CpTipoConfiguracao.findById(300L);
//		if (design == null) {
//			design = new CpTipoConfiguracao();
//			design.setIdTpConfiguracao(300L);
//			design.setDscTpConfiguracao("Designa��o");
//			design.salvar();
//		}
//		return design;
//	}
//
//	public static CpTipoConfiguracao tipoConfigAssociacao() throws Exception {
//		CpTipoConfiguracao assoc = CpTipoConfiguracao.findById(301L);
//		if (assoc == null) {
//			assoc = new CpTipoConfiguracao();
//			assoc.setIdTpConfiguracao(301L);
//			assoc.setDscTpConfiguracao("Associa��o");
//			assoc.salvar();
//		}
//		return assoc;
//	}
//	
//	public static CpTipoConfiguracao tipoConfigPermissaoUsoLista() throws Exception {
//		CpTipoConfiguracao design = CpTipoConfiguracao.findById(302L);
//		if (design == null) {
//			design = new CpTipoConfiguracao();
//			design.setIdTpConfiguracao(302L);
//			design.setDscTpConfiguracao("Permiss�o para Uso de Lista");
//			design.salvar();
//		}
//		return design;
//	}
//
//	public static SrItemConfiguracao software() throws Exception {
//		SrItemConfiguracao soft = SrItemConfiguracao.find(
//				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.00.00").first();
//		if (soft == null) {
//			soft = new SrItemConfiguracao();
//			soft.descrItemConfiguracao = "Software";
//			soft.siglaItemConfiguracao = "99.00.00";
//			soft.tituloItemConfiguracao = "Software";
//			soft.salvar();
//			soft.refresh();
//		}
//		return soft;
//	}
//
//	public static SrItemConfiguracao sysdoc() throws Exception {
//		software();
//		SrItemConfiguracao sysDoc = SrItemConfiguracao.find(
//				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.00").first();
//		if (sysDoc == null) {
//			sysDoc = new SrItemConfiguracao();
//			sysDoc.descrItemConfiguracao = "Sistema de Gest�o Documental";
//			sysDoc.siglaItemConfiguracao = "99.01.00";
//			sysDoc.tituloItemConfiguracao = "Sistema de Gest�o Documental";
//			sysDoc.salvar();
//			sysDoc.refresh();
//		}
//		return sysDoc;
//	}
//
//	public static SrItemConfiguracao sigadoc() throws Exception {
//		sysdoc();
//		SrItemConfiguracao sysDoc = SrItemConfiguracao.find(
//				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.01").first();
//		if (sysDoc == null) {
//			sysDoc = new SrItemConfiguracao();
//			sysDoc.descrItemConfiguracao = "Siga-Doc";
//			sysDoc.siglaItemConfiguracao = "99.01.01";
//			sysDoc.tituloItemConfiguracao = "Siga-Doc";
//			sysDoc.salvar();
//			sysDoc.refresh();
//		}
//		return sysDoc;
//	}
//
//	public static SrItemConfiguracao sigawf() throws Exception {
//		sysdoc();
//		SrItemConfiguracao sigawf = SrItemConfiguracao.find(
//				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.02").first();
//		if (sigawf == null) {
//			sigawf = new SrItemConfiguracao();
//			sigawf.descrItemConfiguracao = "Siga-Wf";
//			sigawf.siglaItemConfiguracao = "99.01.02";
//			sigawf.tituloItemConfiguracao = "Siga-Wf";
//			sigawf.salvar();
//			sigawf.refresh();
//		}
//		return sigawf;
//	}
//
//	public static SrItemConfiguracao systrab() throws Exception {
//		software();
//		SrItemConfiguracao systrab = SrItemConfiguracao.find(
//				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.02.00").first();
//		if (systrab == null) {
//			systrab = new SrItemConfiguracao();
//			systrab.descrItemConfiguracao = "Sistema de Gest�o do Trabalho";
//			systrab.siglaItemConfiguracao = "99.02.00";
//			systrab.tituloItemConfiguracao = "Sistema de Gest�o do Trabalho";
//			systrab.salvar();
//			systrab.refresh();
//		}
//		return systrab;
//	}
//
//	public static SrAcao acaoSoft() throws Exception {
//		SrAcao acaoSoft = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.00")
//				.first();
//		if (acaoSoft == null) {
//			acaoSoft = new SrAcao();
//			acaoSoft.descrAcao = "A��o para Software";
//			acaoSoft.siglaAcao = "99.00";
//			acaoSoft.tituloAcao = "A��o para Software";
//			acaoSoft.salvar();
//			acaoSoft.refresh();
//		}
//		return acaoSoft;
//	}
//
//	public static SrAcao criarSoft() throws Exception {
//		acaoSoft();
//		SrAcao criar = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.01")
//				.first();
//		if (criar == null) {
//			criar = new SrAcao();
//			criar.descrAcao = "Criar";
//			criar.siglaAcao = "99.01";
//			criar.tituloAcao = "Criar";
//			criar.salvar();
//			criar.refresh();
//		}
//		return criar;
//	}
//
//	public static SrAcao manterSoft() throws Exception {
//		acaoSoft();
//		SrAcao manter = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull", "99.02")
//				.first();
//		if (manter == null) {
//			manter = new SrAcao();
//			manter.descrAcao = "Manter";
//			manter.siglaAcao = "99.02";
//			manter.tituloAcao = "Manter";
//			manter.salvar();
//			manter.refresh();
//		}
//		return manter;
//	}
//
//	public static SrAtributo attPrazo() throws Exception {
//		SrAtributo prazo = SrAtributo.find(
//				"byNomeTipoAtributoAndHisDtFimIsNull", "Prazo").first();
//		if (prazo == null) {
//			prazo = new SrAtributo();
//			prazo.nomeAtributo = "Prazo";
//			prazo.descrAtributo = "Prazo para atendimento";
//			prazo.salvar();
//			prazo.refresh();
//		}
//		return prazo;
//	}
//
//	public static SrAtributo attNumDoc() throws Exception {
//		SrAtributo numDoc = SrAtributo.find(
//				"byNomeTipoAtributoAndHisDtFimIsNull", "Numero do Documento")
//				.first();
//		if (numDoc == null) {
//			numDoc = new SrAtributo();
//			numDoc.nomeAtributo = "Numero do Documento";
//			numDoc.descrAtributo = "Numero do Documento";
//			numDoc.salvar();
//			numDoc.refresh();
//		}
//		return numDoc;
//	}
//
//	public static SrTipoPergunta tipoPerguntaTexto() throws Exception {
//		SrTipoPergunta texto = SrTipoPergunta.findById(1L);
//		if (texto == null) {
//			texto = new SrTipoPergunta();
//			texto.idTipoPergunta = 1L;
//			texto.nomeTipoPergunta = "Texto Livre";
//			texto.save();
//			texto.refresh();
//		}
//		return texto;
//	}
//	
//	public static SrTipoPergunta tipoPerguntaNota1A5() throws Exception {
//		SrTipoPergunta nota = SrTipoPergunta.findById(2L);
//		if (nota == null) {
//			nota = new SrTipoPergunta();
//			nota.idTipoPergunta = 2L;
//			nota.nomeTipoPergunta = "Nota de 1 a 5";
//			nota.save();
//			nota.refresh();
//		}
//		return nota;
//	}
//
//	public static void tiposMov() {
//		new SrTipoMovimentacao(1L, "In�cio do Atendimento").save();
//		new SrTipoMovimentacao(2L, "Andamento").save();
//		new SrTipoMovimentacao(3L, "Inclusao em Lista").save();
//		new SrTipoMovimentacao(4L, "In�cio do Pr�-Atendimento").save();
//		new SrTipoMovimentacao(5L, "In�cio do P�s-Atendimento").save();
//		new SrTipoMovimentacao(6L, "Cancelamento de Inclus�o em Lista").save();
//		new SrTipoMovimentacao(7L, "Fechamento").save();
//		new SrTipoMovimentacao(8L, "Cancelamento da Solicita��o").save();
//		new SrTipoMovimentacao(9L, "In�cio de Pend�ncia").save();
//		new SrTipoMovimentacao(10L, "Reabertura").save();
//		new SrTipoMovimentacao(11L, "Fim de Pend�ncia").save();
//		new SrTipoMovimentacao(12L, "Anexa��o de Arquivo").save();
//		new SrTipoMovimentacao(13L, "Altera��o de Prioridade em Lista").save();
//		new SrTipoMovimentacao(14L, "Cancelamento de Movimenta��o").save();
//		new SrTipoMovimentacao(15L, "Fechamento Parcial").save();
//		new SrTipoMovimentacao(16L, "Avalia��o").save();
//		new SrTipoMovimentacao(17L, "In�cio do Controle de Qualidade").save();
//	}
//
//	public static void marcadores() {
//
//		CpTipoMarca t = new CpTipoMarca();
//		t.setIdTpMarca(2L);
//		t.setDescrTipoMarca("Siga-SR");
//		t.save();
//
//		CpTipoMarcador cptm = new CpTipoMarcador();
//		cptm.setIdTpMarcador(1L);
//		cptm.setDescrTipoMarcador("Sistema");
//		cptm.save();
//
//		CpMarcador m41 = new CpMarcador();
//		m41.setIdMarcador(41L);
//		m41.setDescrMarcador("A Receber");
//		m41.setCpTipoMarcador(cptm);
//		m41.save();
//
//		CpMarcador m42 = new CpMarcador();
//		m42.setIdMarcador(42L);
//		m42.setDescrMarcador("Em Andamento");
//		m42.setCpTipoMarcador(cptm);
//		m42.save();
//
//		CpMarcador m43 = new CpMarcador();
//		m43.setIdMarcador(43L);
//		m43.setDescrMarcador("Fechado");
//		m43.setCpTipoMarcador(cptm);
//		m43.save();
//
//		CpMarcador m44 = new CpMarcador();
//		m44.setIdMarcador(44L);
//		m44.setDescrMarcador("Pendente");
//		m44.setCpTipoMarcador(cptm);
//		m44.save();
//
//		CpMarcador m45 = new CpMarcador();
//		m45.setIdMarcador(45L);
//		m45.setDescrMarcador("Cancelado");
//		m45.setCpTipoMarcador(cptm);
//		m45.save();
//
//		CpMarcador m46 = new CpMarcador();
//		m46.setIdMarcador(46L);
//		m46.setDescrMarcador("Pr�-Atendimento");
//		m46.setCpTipoMarcador(cptm);
//		m46.save();
//
//		CpMarcador m47 = new CpMarcador();
//		m47.setIdMarcador(47L);
//		m47.setDescrMarcador("P�s-Atendimento");
//		m47.setCpTipoMarcador(cptm);
//		m47.save();
//
//		CpMarcador m48 = new CpMarcador();
//		m48.setIdMarcador(48L);
//		m48.setDescrMarcador("Como Cadastrante");
//		m48.setCpTipoMarcador(cptm);
//		m48.save();
//
//		CpMarcador m49 = new CpMarcador();
//		m49.setIdMarcador(49L);
//		m49.setDescrMarcador("Como Solicitante");
//		m49.setCpTipoMarcador(cptm);
//		m49.save();
//
//		CpMarcador m53 = new CpMarcador();
//		m53.setIdMarcador(53L);
//		m53.setDescrMarcador("Fechado Parcial");
//		m53.setCpTipoMarcador(cptm);
//		m53.save();
//
//		CpMarcador m54 = new CpMarcador();
//		m54.setIdMarcador(54L);
//		m54.setDescrMarcador("Em Controle de Qualidade");
//		m54.setCpTipoMarcador(cptm);
//		m54.save();
//	}
//
//	public static void criarDadosBasicos() throws Exception {
//		sigadoc();
//		sigawf();
//		systrab();
//		
//		manterSoft();
//		criarSoft();
//		
//		attNumDoc();
//		attPrazo();
//		
//		tipoPerguntaTexto();
//		tipoPerguntaNota1A5();
//		
//		tiposMov();
//		
//		marcadores();
//		
//		tipoConfigDesignacao();
//		tipoConfigAssociacao();
//		tipoConfigPermissaoUsoLista();
//	}
//
//	public static void prepararSessao() throws Exception {
//		Session playSession = (Session) JPA.em().getDelegate();
//		CpDao.freeInstance();
//		CpDao.getInstance(playSession);
//		HibernateUtil.setSessao(playSession);
//		Cp.getInstance().getConf().limparCacheSeNecessario();
//	}
//
//	public static void limparBase() throws Exception {
//		SrAcao.deleteAll();
//		SrItemConfiguracao.deleteAll();
//		DpPessoa.deleteAll();
//		DpLotacao.deleteAll();
//		CpComplexo.deleteAll();
//		CpLocalidade.deleteAll();
//		CpOrgaoUsuario.deleteAll();
//		SrTipoMovimentacao.deleteAll();
//		CpMarcador.deleteAll();
//		CpTipoMarcador.deleteAll();
//		CpTipoMarca.deleteAll();
//	}
//
//	public static void apagaCacheDesignacao() throws Exception {
//		SrConfiguracaoBL
//				.get()
//				.limparCache(
//						(CpTipoConfiguracao) CpTipoConfiguracao
//								.findById(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
//	}
//
//	public static void apagaCacheAssociacao() throws Exception {
//		SrConfiguracaoBL
//				.get()
//				.limparCache(
//						(CpTipoConfiguracao) CpTipoConfiguracao
//								.findById(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
//	}

}
