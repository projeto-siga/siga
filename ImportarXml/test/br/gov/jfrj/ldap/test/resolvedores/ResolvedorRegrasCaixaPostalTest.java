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
package br.gov.jfrj.ldap.test.resolvedores;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;

import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.ldap.sinc.resolvedores.RegraCaixaPostal;
import br.gov.jfrj.ldap.sinc.resolvedores.ResolvedorRegrasCaixaPostal;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class ResolvedorRegrasCaixaPostalTest extends TestCase {

	private static CpDao dao;
	private static SincProperties conf = SincProperties.getInstancia();
	private static ResolvedorRegrasCaixaPostal resolvedor;
	private DpPessoa p;
	private static RegraCaixaPostal regra;


	public void setUp(){
		try{
			if (dao == null){
				AnnotationConfiguration cfg;
				/*
				cfg = CpDao.criarHibernateCfg(
						"jdbc:oracle:thin:@mclaren:1521:mcl", "corporativo",
						"corporativo");
						*/
				cfg = CpDao.criarHibernateCfg(CpAmbienteEnumBL.DESENVOLVIMENTO);
				CpDao.configurarHibernateParaDebug(cfg);
				HibernateUtil.configurarHibernate(cfg, "");
				dao = CpDao.getInstance();
				if (resolvedor == null){
					resolvedor = ResolvedorRegrasCaixaPostal.getInstancia(conf);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testRegraJuiz() throws AplicacaoException {
		Long idPessoa = 23832L;
		String homeMDB = "CN=Juizes,CN=JuizesOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Juízes,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //JUIZ
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}

	@Test
	public void testRegraDiretor() throws AplicacaoException {
		Long idPessoa = 24981L;
		String homeMDB = "CN=DirSub,CN=DirSubOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //DIRETOR
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}

	@Test
	public void testRegraCoordenador() throws AplicacaoException {
		Long idPessoa = 24139L;
		String homeMDB = "CN=Coord,CN=CoordOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //COORDENADOR
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}

	@Test
	public void testRegraEstagiario() throws AplicacaoException {
		Long idPessoa = 130582L;
		String homeMDB = "CN=Estagiario,CN=EstagiarioOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Estag.,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //ESTAGIÁRIO
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}

	@Test
	public void testRegraSupervChefeAssOfJust() throws AplicacaoException {
		Long idPessoa1 = 24688L;
		String homeMDB = "CN=SupervChefeAssOfJust,CN=SupervChefeAssOfJustOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa1);//(ASSISTENTE IV)
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa1);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);

		Long idPessoa2 = 26646L;
		p = dao.consultarPorIdInicial(idPessoa2); //OFICIAL DE JUSTIÇA
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa2);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
}
	
	@Test
	public void testRegraServidorS_Z() throws AplicacaoException {
		Long idPessoa = 26023L;
		String homeMDB = "CN=ServidorSaZ,CN=ServidorSaZOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local" ;
		
		p = dao.consultarPorIdInicial(idPessoa); //SERVIDOR LETRA S-Z
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);

		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}
	
	@Test
	public void testRegraServidorM_R() throws AplicacaoException {
		Long idPessoa = 26575L;
		String homeMDB = "CN=ServidorMaR,CN=ServidorMaROriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		p = dao.consultarPorIdInicial(idPessoa); //SERVIDOR LETRA M-R
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}
	
	@Test
	public void testRegraServidorG_L() throws AplicacaoException {
		Long idPessoa = 85722L;
		String homeMDB = "CN=ServidorGaL,CN=ServidorGaLOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //SERVIDOR LETRA G-L
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}
	
	@Test
	public void testRegraServidorA_F() throws AplicacaoException {
		Long idPessoa = 68298L;
		String homeMDB = "CN=ServidorAaF,CN=ServidorAaFOriginal,CN=InformationStore,CN=CSISEXC,CN=Servers,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";
		String templateLink = "CN=Política Servidores,CN=ELC Mailbox Policies,CN=First Organization,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=csis,DC=local";

		p = dao.consultarPorIdInicial(idPessoa); //SERVIDOR LETRA A-F
		regra = resolvedor.getRegra(p);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
		
		regra = resolvedor.getRegraPorIdInicial(idPessoa);
		assertEquals(regra.getHomeMDB(),homeMDB);
		assertEquals(regra.getTemplateLink(),templateLink);
	}


}
