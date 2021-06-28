package br.gov.jfrj.siga.sr.util;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoServico;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.Sr;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.notifiers.CorreioFake;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;

public class TestUtil {
	
	public static void configurarEntityManager() throws Exception{
		EntityManager em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);
		
		CpDao.freeInstance();
		CpDao.getInstance();
	}
	
	public static void criarDadosBasicos(){
		EntityManager em = ContextoPersistencia.em();
		em.createNativeQuery("CREATE FUNCTION REMOVE_ACENTO(acentuado VARCHAR(50)) RETURNS VARCHAR(50)"
				+ " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA "
				+ " EXTERNAL NAME 'br.gov.jfrj.siga.sr.util.TestUtil.removeAcento'").executeUpdate();

		/*ContextoPersistencia.em().createNativeQuery("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout', '5')").executeUpdate();
		ContextoPersistencia.em().createNativeQuery("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.monitor', 'true')").executeUpdate();
		ContextoPersistencia.em().createNativeQuery("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.deadlockTrace', 'true')").executeUpdate();*/
			
		tiposMov();
		marcadores();
		servicos();
		
		CorreioHolder.set(new CorreioFake(null, null));
		
		System.setProperty("siga.properties.versao", "0.0");
	}
	
	/*public static void tearDown(){
		ContextoPersistencia.em().createNativeQuery("DROP FUNCTION REMOVE_ACENTO").executeUpdate();
	}*/
	
	public static void limparCacheConfiguracoes() throws Exception {
		Sr.getInstance().getConf().limparCacheSeNecessario();
	}

	public static CpOrgaoUsuario rj() {
		CpOrgaoUsuario rj = (CpOrgaoUsuario) CpOrgaoUsuario.AR.findById(1L);
		if (rj == null) {
			rj = new CpOrgaoUsuario();
			rj.setIdOrgaoUsu(1L);
			rj.setNmOrgaoUsu("Órgão de Teste Rio de janeiro");
			rj.setSigla("RJ");
			rj.setAcronimoOrgaoUsu("OTRJ");
			rj.save();
		}
		return rj;
	}

	public static CpOrgaoUsuario sp() {
		CpOrgaoUsuario sp = (CpOrgaoUsuario) CpOrgaoUsuario.AR.findById(2L);
		if (sp == null) {
			sp = new CpOrgaoUsuario();
			sp.setIdOrgaoUsu(2L);
			sp.setNmOrgaoUsu("Órgão de Teste São Paulo");
			sp.setSigla("SP");
			sp.setAcronimoOrgaoUsu("OTSP");
			sp.save();
		}
		return sp;
	}

	/*public static CpLocalidade cidadeRio() {
		CpLocalidade rio = (CpLocalidade) CpLocalidade.AR.findById(15L);
		if (rio == null) {
			rio = new CpLocalidade();
			rio.setIdLocalidade(15L);
			rio.setNmLocalidade("Rio de Janeiro");
			rio.setSigla("RJ");
			rio.save();
		}
		return rio;
	}*/

	/*public static CpComplexo barroso() {
		CpComplexo barroso = (CpComplexo) CpComplexo.AR.find("byNomeComplexo",
				"Almirante Barroso").first();
		if (barroso == null) {
			barroso = new CpComplexo();
			barroso.setIdComplexo(1L);
			barroso.setLocalidade(cidadeRio());
			barroso.setNomeComplexo("Almirante Barroso");
			barroso.setOrgaoUsuario(rj());
			barroso.save();
		}
		return barroso;
	}*/

	public static DpLotacao lotaMaior() throws Exception {
		DpLotacao lota = DpLotacao.AR.find("bySiglaLotacao", "MAIOR").first();
		if (lota == null) {
			lota = new DpLotacao();
			lota.setSiglaLotacao("MAIOR");
			lota.setOrgaoUsuario(rj());
			lota.setLotacaoPai(lota);
			lota.setNomeLotacao("Lotação Maior na Hierarquia");
			lota.save();
			lota.setIdInicial(lota.getId());
			lota.save();
			lota.refresh();
		}
		return lota;
	}

	public static DpLotacao lotaMenor() throws Exception {
		DpLotacao lota = DpLotacao.AR.find("bySiglaLotacao", "MENOR").first();
		if (lota == null) {
			lota = new DpLotacao();
			lota.setSiglaLotacao("MENOR");
			lota.setOrgaoUsuario(rj());
			lota.setLotacaoPai(lotaMaior());
			lota.setNomeLotacao("Lotação Menor na Hierarquia");
			lota.save();
			lota.setIdInicial(lota.getId());
			lota.save();
			lota.refresh();
		}
		return lota;
	}

	public static DpLotacao lotaPrimaDaMenor() throws Exception {
		DpLotacao lota = DpLotacao.AR.find("bySiglaLotacao", "PRIMA-MENOR").first();
		if (lota == null) {
			lota = new DpLotacao();
			lota.setSiglaLotacao("SEGEP");
			lota.setOrgaoUsuario(rj());
			lota.setLotacaoPai(lotaMaior());
			lota.setNomeLotacao("Lotação Prima da Menor na Hierarquia");
			lota.save();
			lota.setIdInicial(lota.getId());
			lota.save();
			lota.refresh();
		}
		return lota;
	}

	public static DpPessoa funcionarioMenor() throws Exception {
		DpPessoa eeh = DpPessoa.AR.find("bySiglaPessoa", "FM1").first();
		if (eeh == null) {
			eeh = new DpPessoa();
			eeh.setNomePessoa("Funcionário nº 1 da Lotação Menor");
			eeh.setSiglaPessoa("FM1");
			eeh.setEmailPessoa("fm1@rj.org");
			eeh.setOrgaoUsuario(rj());
			eeh.setLotacao(lotaMenor());
			eeh.save();
			eeh.setIdInicial(eeh.getId());
			eeh.save();
			eeh.refresh();
		}
		return eeh;
	}
	
	public static DpLotacao lotaSp() throws Exception {
		DpLotacao lota = DpLotacao.AR.find("bySiglaLotacao", "LOTASP").first();
		if (lota == null) {
			lota = new DpLotacao();
			lota.setSiglaLotacao("LOTASP");
			lota.setOrgaoUsuario(sp());
			lota.setLotacaoPai(null);
			lota.setNomeLotacao("Lotação Única de São Paulo");
			lota.save();
			lota.setIdInicial(lota.getId());
			lota.save();
			lota.refresh();
		}
		return lota;
	}
	
	public static DpPessoa funcionarioSp() throws Exception {
		DpPessoa eeh = DpPessoa.AR.find("bySiglaPessoa", "FSP").first();
		if (eeh == null) {
			eeh = new DpPessoa();
			eeh.setNomePessoa("Funcionário único de São Paulo");
			eeh.setSiglaPessoa("FSP");
			eeh.setEmailPessoa("fsp@sp.org");
			eeh.setOrgaoUsuario(sp());
			eeh.setLotacao(lotaSp());
			eeh.save();
			eeh.setIdInicial(eeh.getId());
			eeh.save();
			eeh.refresh();
		}
		return eeh;
	}
		
	public static CpUnidadeMedida hora(){
		CpUnidadeMedida h = CpUnidadeMedida.AR.findById(Long.valueOf(CpUnidadeMedida.HORA));
		if (h == null) {
			h = new CpUnidadeMedida();
			h.setId(Long.valueOf(CpUnidadeMedida.HORA));
			h.save();
		}
		return h;
	}

	public static SrItemConfiguracao software() throws Exception {
		SrItemConfiguracao soft = SrItemConfiguracao.AR.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.00.00").first();
		if (soft == null) {
			soft = new SrItemConfiguracao();
			soft.setDescrItemConfiguracao("Software");
			soft.setSiglaItemConfiguracao("99.00.00");
			soft.setTituloItemConfiguracao("Software");
			soft.salvarComHistorico();
			soft.refresh();
		}
		return soft;
	}

	public static SrItemConfiguracao systems() throws Exception {
		software();
		SrItemConfiguracao sysDoc = SrItemConfiguracao.AR.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.00").first();
		if (sysDoc == null) {
			sysDoc = new SrItemConfiguracao();
			sysDoc.setDescrItemConfiguracao("Sistema de Gestãoo Documental");
			sysDoc.setSiglaItemConfiguracao("99.01.00");
			sysDoc.setTituloItemConfiguracao("Sistema de Gestão Documental");
			sysDoc.salvarComHistorico();
			sysDoc.refresh();
		}
		return sysDoc;
	}

	public static SrItemConfiguracao sigadoc() throws Exception {
		systems();
		SrItemConfiguracao sysDoc = SrItemConfiguracao.AR.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.01").first();
		if (sysDoc == null) {
			sysDoc = new SrItemConfiguracao();
			sysDoc.setDescrItemConfiguracao("Siga-Doc");
			sysDoc.setSiglaItemConfiguracao("99.01.01");
			sysDoc.setTituloItemConfiguracao("Siga-Doc");
			sysDoc.salvarComHistorico();
			sysDoc.refresh();
		}
		return sysDoc;
	}

	public static SrAcao acaoSoft() throws Exception {
		SrAcao acaoSoft = SrAcao.AR.find("bySiglaAcaoAndHisDtFimIsNull", "99.00")
				.first();
		if (acaoSoft == null) {
			acaoSoft = new SrAcao();
			acaoSoft.setDescrAcao("Ação para Software");
			acaoSoft.setSiglaAcao("99.00");
			acaoSoft.setTituloAcao("Ação para Software");
			acaoSoft.salvarComHistorico();
			acaoSoft.refresh();
		}
		return acaoSoft;
	}

	public static SrAcao acaoCriarSoft() throws Exception {
		acaoSoft();
		SrAcao criar = SrAcao.AR.find("bySiglaAcaoAndHisDtFimIsNull", "99.01")
				.first();
		if (criar == null) {
			criar = new SrAcao();
			criar.setDescrAcao("Criar");
			criar.setSiglaAcao("99.01");
			criar.setTituloAcao("Criar");
			criar.salvarComHistorico();
			criar.refresh();
		}
		return criar;
	}
	
	public static SrConfiguracao designacaoBasica() throws Exception{
		SrConfiguracao design = SrConfiguracao.AR.find("byDescrConfiguracao", "1stDesign").first();
		if (design == null) {
			design = new SrConfiguracao();
			design.setAtendente(lotaMenor());
			design.setDescrConfiguracao("1stDesign");
			design.salvarComoDesignacao();
		}
		return design;
	}
	
	public static SrSolicitacao solicitacaoRj() throws Exception{
		SrSolicitacao solRj = new SrSolicitacao();
		solRj.setCadastrante(funcionarioMenor());
		solRj.setItemConfiguracao(TestUtil.sigadoc());
		solRj.setAcao(TestUtil.acaoCriarSoft());
		solRj.setDesignacao(designacaoBasica());
		solRj.salvarComHistorico();
		return solRj;
	}
	
	public static SrSolicitacao solicitacaoSpLastYear() throws Exception{
		//Criar uma solicitação SP do ano passado
		SrSolicitacao solSpLastYear = new SrSolicitacao();
		solSpLastYear.setCadastrante(funcionarioSp());
		solSpLastYear.setItemConfiguracao(TestUtil.sigadoc());
		solSpLastYear.setAcao(TestUtil.acaoCriarSoft());
		solSpLastYear.setDesignacao(designacaoBasica());
		solSpLastYear.salvarComHistorico();
		Calendar lastYear = Calendar.getInstance();
		lastYear.setTime(solSpLastYear.getDtReg());
		lastYear.add(Calendar.YEAR, -1);
		solSpLastYear.setDtReg(lastYear.getTime());
		solSpLastYear.setCodigo(solSpLastYear.getCodigo().replace(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), String.valueOf(lastYear.get(Calendar.YEAR))));
		solSpLastYear.save();
		return solSpLastYear;
	}
	
	public static SrSolicitacao solicitacaoSp() throws Exception{
		//Criar uma solicitação SP
		SrSolicitacao solSp = new SrSolicitacao();
		solSp.setCadastrante(funcionarioSp());
		solSp.setItemConfiguracao(TestUtil.sigadoc());
		solSp.setAcao(TestUtil.acaoCriarSoft());
		solSp.setDesignacao(designacaoBasica());
		solSp.salvarComHistorico();
		return solSp;
	}	
	
	private static void tiposMov() {
		new SrTipoMovimentacao(1L, "Início do Atendimento").save();
		//ContextoPersistencia.em().createQuery("from SrTipoMovimentacao cpcfg").getFirstResult();
		//((Query) HibernateUtil.getSessionFactory().openStatelessSession().createQuery("from SrTipoMovimentacao cpcfg")).list();
		new SrTipoMovimentacao(2L, "Andamento").save();
		new SrTipoMovimentacao(7L, "Fechamento").save();
		new SrTipoMovimentacao(8L, "Cancelamento da Solicitação").save();
		new SrTipoMovimentacao(9L, "Início de Pendência").save();
		new SrTipoMovimentacao(10L, "Reabertura").save();
		new SrTipoMovimentacao(11L, "Fim de Pendência").save();
		new SrTipoMovimentacao(12L, "Anexação de Arquivo").save();
		new SrTipoMovimentacao(14L, "Cancelamento de Movimentação").save();
	}
	
	private static void servicos() {
		
		CpTipoServico ts = new CpTipoServico();
		ts.setIdCpTpServico(2);
		ts.setDscTpServico("Sistema");
		ts.setSituacaoDefault(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
		ts.save();

		CpServico siga = new CpServico();
		siga.setCpTipoServico(ts);
		siga.setDscServico("Sistema Integrado de Gestão Administrativa");
		siga.setSiglaServico("SIGA");
		siga.save();
		
		CpServico sr = new CpServico();
		sr.setCpServicoPai(siga);
		sr.setCpTipoServico(ts);
		sr.setDscServico("Módulo de Serviços");
		sr.setSiglaServico("SIGA-SR");
		sr.save();
		
		CpServico mailAtend = new CpServico();
		mailAtend.setCpServicoPai(sr);
		mailAtend.setCpTipoServico(ts);
		mailAtend.setDscServico("Receber Notificação Atendente");
		mailAtend.setSiglaServico("SIGA-SR-EMAILATEND");
		mailAtend.save();

	}

	private static void marcadores() {

		CpTipoMarca t = new CpTipoMarca();
		t.setIdTpMarca(2L);
		t.setDescrTipoMarca("Siga-SR");
		t.save();

		CpTipoMarca cptm = new CpTipoMarca();
		cptm.setIdTpMarca(1L);
		cptm.setDescrTipoMarca("Sistema");
		cptm.save();

		CpTipoMarca m41 = new CpTipoMarca();
		m41.setIdTpMarca(41L);
		m41.setDescrTipoMarca("A Receber");
		m41.save();

		CpMarcador m42 = new CpMarcador();
		m42.setIdMarcador(42L);
		m42.setDescrMarcador("Em Andamento");
		m42.save();

		CpMarcador m43 = new CpMarcador();
		m43.setIdMarcador(43L);
		m43.setDescrMarcador("Fechado");
		m43.save();

		CpMarcador m44 = new CpMarcador();
		m44.setIdMarcador(44L);
		m44.setDescrMarcador("Pendente");
		m44.save();

		CpMarcador m45 = new CpMarcador();
		m45.setIdMarcador(45L);
		m45.setDescrMarcador("Cancelado");
		m45.save();

		CpMarcador m48 = new CpMarcador();
		m48.setIdMarcador(48L);
		m48.setDescrMarcador("Como Cadastrante");
		m48.save();

		CpMarcador m49 = new CpMarcador();
		m49.setIdMarcador(49L);
		m49.setDescrMarcador("Como Solicitante");
		m49.save();
		
		CpMarcador m53 = new CpMarcador();
		m53.setIdMarcador(53L);
		m53.setDescrMarcador("A Fechar");
		m53.save();

		CpMarcador m61 = new CpMarcador();
		m61.setIdMarcador(61L);
		m61.setDescrMarcador("Em Elaboração");
		m61.save();
		
		CpMarcador m65 = new CpMarcador();
		m65.setIdMarcador(65L);
		m65.setDescrMarcador("Fora do Prazo");
		m65.save();
		
		CpMarcador m66 = new CpMarcador();
		m66.setIdMarcador(66L);
		m66.setDescrMarcador("Ativo");
		m66.save();
		
		CpMarcador m69 = new CpMarcador();
		m69.setIdMarcador(69L);
		m69.setDescrMarcador("Necessita Providência");
		m69.save();
		
	}
	
	/*public static SrAcordo OLA() throws Exception{
		SrAcordo a = SrAcordo.AR.find("byNomeAcordoAndHisDtFimIsNull", "OLA")
				.first();
		if (a == null) {
			a = new SrAcordo();
			a.setNomeAcordo("OLA");

			SrParametroAcordo p = new SrParametroAcordo();
			p.setAcordo(a);
			p.setOperador(SrOperador.MENOR_OU_IGUAL);
			p.setValor(1L);
			p.setUnidadeMedida(hora());

			a.salvarComHistorico();

			SrConfiguracao abrang = new SrConfiguracao();
			abrang.setAcordo(a);
			abrang.salvarComoAbrangenciaAcordo();
			limparCacheConfiguracoes();
		}
		return a;
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
		CpComplexo.deleteAll();
		CpLocalidade.deleteAll();
		CpOrgaoUsuario.deleteAll();
		SrTipoMovimentacao.deleteAll();
		CpMarcador.deleteAll();
		CpTipoMarcador.deleteAll();
		CpTipoMarca.deleteAll();
	}

	public static void apagaCacheAssociacao() throws Exception {
		SrConfiguracaoBL
				.get()
				.limparCache(
						(CpTipoConfiguracao) CpTipoConfiguracao
								.findById(CpTipoDeConfiguracao.SR_ASSOCIACAO_TIPO_ATRIBUTO));
	}*/
	
	public static String removeAcento(String acentuado){
		return acentuado;
	}
	
	public static void beginTransaction(){
		ContextoPersistencia.em().getTransaction().begin();
	}

	public static void commit(){
		ContextoPersistencia.em().getTransaction().commit();
	}
	
	public static void reiniciarCacheConfiguracoes(){
		Cp.getInstance().getConf().reiniciarCache();
	}
}
