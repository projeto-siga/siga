package br.gov.jfrj.siga.sr.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Session;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.CpTipoMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.sr.model.Sr;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoBL;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrOperador;
import br.gov.jfrj.siga.sr.model.SrParametroAcordo;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;

public class TestUtil {
	
	public static void setup(EntityManager em) throws Exception{
		em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);
		
		em.getTransaction().begin();
		em.createNativeQuery("CREATE FUNCTION REMOVE_ACENTO(acentuado VARCHAR(50)) RETURNS VARCHAR(50)"
				+ " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA "
				+ " EXTERNAL NAME 'br.gov.jfrj.siga.sr.util.TestUtil.removeAcento'").executeUpdate();
				
		CpDao.freeInstance();
		CpDao.getInstance((Session) em.getDelegate(), ((Session) em
				.getDelegate()).getSessionFactory().openStatelessSession());
		
		//Edson: não sei por que o HibernateUtil precisa de uma sessao. Os Dao's
				//que chamam essa classe já têm o objeto sessão 
		HibernateUtil.configurarHibernate((Session)em.getDelegate());
		
		criarDadosBasicos();
	}
	
	public static void tearDown(EntityManager em){
		em.getTransaction().commit();
	}
	
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
	
	public static void criarDadosBasicos() throws Exception {
		tiposMov();
		tiposConfig();
		marcadores();
	}

	private static void tiposMov() {
		new SrTipoMovimentacao(1L, "Início do Atendimento").save();
		new SrTipoMovimentacao(2L, "Andamento").save();
		new SrTipoMovimentacao(7L, "Fechamento").save();
		new SrTipoMovimentacao(8L, "Cancelamento da Solicitação").save();
		new SrTipoMovimentacao(9L, "Início de Pendência").save();
		new SrTipoMovimentacao(10L, "Reabertura").save();
		new SrTipoMovimentacao(11L, "Fim de Pendência").save();
		new SrTipoMovimentacao(12L, "Anexação de Arquivo").save();
		new SrTipoMovimentacao(14L, "Cancelamento de Movimentação").save();
	}
	
	private static void tiposConfig(){
		
		CpTipoConfiguracao design = new CpTipoConfiguracao();
		design.setIdTpConfiguracao(300L);
		design.setDscTpConfiguracao("Designação");
		design.save();
		
		CpTipoConfiguracao assoc = new CpTipoConfiguracao();
		assoc.setIdTpConfiguracao(304L);
		assoc.setDscTpConfiguracao("Abrangência de Acordo");
		assoc.save();
	}

	private static void marcadores() {

		CpTipoMarca t = new CpTipoMarca();
		t.setIdTpMarca(2L);
		t.setDescrTipoMarca("Siga-SR");
		t.save();

		CpTipoMarcador cptm = new CpTipoMarcador();
		cptm.setIdTpMarcador(1L);
		cptm.setDescrTipoMarcador("Sistema");
		cptm.save();

		CpMarcador m41 = new CpMarcador();
		m41.setIdMarcador(41L);
		m41.setDescrMarcador("A Receber");
		m41.setCpTipoMarcador(cptm);
		m41.save();

		CpMarcador m42 = new CpMarcador();
		m42.setIdMarcador(42L);
		m42.setDescrMarcador("Em Andamento");
		m42.setCpTipoMarcador(cptm);
		m42.save();

		CpMarcador m43 = new CpMarcador();
		m43.setIdMarcador(43L);
		m43.setDescrMarcador("Fechado");
		m43.setCpTipoMarcador(cptm);
		m43.save();

		CpMarcador m44 = new CpMarcador();
		m44.setIdMarcador(44L);
		m44.setDescrMarcador("Pendente");
		m44.setCpTipoMarcador(cptm);
		m44.save();

		CpMarcador m45 = new CpMarcador();
		m45.setIdMarcador(45L);
		m45.setDescrMarcador("Cancelado");
		m45.setCpTipoMarcador(cptm);
		m45.save();

		CpMarcador m48 = new CpMarcador();
		m48.setIdMarcador(48L);
		m48.setDescrMarcador("Como Cadastrante");
		m48.setCpTipoMarcador(cptm);
		m48.save();

		CpMarcador m49 = new CpMarcador();
		m49.setIdMarcador(49L);
		m49.setDescrMarcador("Como Solicitante");
		m49.setCpTipoMarcador(cptm);
		m49.save();

	}
	
	public static SrConfiguracao design() throws Exception {
		SrConfiguracao design = SrConfiguracao.AR.find("byDescrConfiguracaoAndHisDtFimIsNull", "Designação Básica")
				.first();
		if (design == null) {
			design = new SrConfiguracao();
			design.setAtendente(lotaMenor());
			design.setDescrConfiguracao("Designação Básica");
			design.salvarComoDesignacao();
			design.refresh();
			limparCacheConfiguracoes();
			
		}
		return design;
	}
	
	public static SrAcordo SLA() throws Exception{
		SrAcordo a = SrAcordo.AR.find("byNomeAcordoAndHisDtFimIsNull", "SLA")
				.first();
		if (a == null) {
			a = new SrAcordo();
			a.setNomeAcordo("SLA");

			SrParametroAcordo p = new SrParametroAcordo();
			p.setAcordo(a);
			p.setOperador(SrOperador.MENOR_OU_IGUAL);
			p.setValor(2L);
			p.setUnidadeMedida(hora());

			a.salvarComHistorico();

			SrConfiguracao abrang = new SrConfiguracao();
			abrang.setAcordo(a);
			abrang.salvarComoAbrangenciaAcordo();
			abrang.refresh();
			limparCacheConfiguracoes();
		}

		return a;
	}
	
	public static SrAcordo OLA() throws Exception{
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

	/*public static void prepararSessao() throws Exception {
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
								.findById(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
	}*/
	
	public static String removeAcento(String acentuado){
		return acentuado;
	}

}
