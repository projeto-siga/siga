package controllers;

import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrTipoAtributo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	static {
		int a = 0;
		DpLotacao l = null;
	}

	public void doJob() {
		
		if (SrItemConfiguracao.count() == 0) {

			SrItemConfiguracao geDoc;
			SrItemConfiguracao gePat;
			try {
				SrItemConfiguracao softwares = (SrItemConfiguracao) new SrItemConfiguracao(
						"01.00.00.00", "Softwares");
				softwares.salvar();

				geDoc = (SrItemConfiguracao) new SrItemConfiguracao("01.01.00.00",
						"Sistemas de gestão documental");
				geDoc.salvar();

				gePat = (SrItemConfiguracao) new SrItemConfiguracao("01.02.00.00",
						"Sistemas de gestão patrimonial");
				gePat.salvar();

				SrServico soft = (SrServico) new SrServico("01.00",
						"Serviços típicos de software");
				soft.salvar();
				
				SrServico manutSoft = (SrServico) new SrServico("01.01",
				"Manutenção");
				manutSoft.salvar();

				SrServico desenv = (SrServico) new SrServico("02.00",
						"Desenvolvimento");
				desenv.salvar();
				
				SrTipoAtributo attNomeSys = new SrTipoAtributo();
				attNomeSys.nomeTipoAtributo = "Nome do sistema";
				attNomeSys.salvar();
				
				SrTipoAtributo attPrazo = new SrTipoAtributo();
				attPrazo.nomeTipoAtributo = "Prazo";
				attPrazo.salvar();
				
				SrConfiguracao asso = new SrConfiguracao();
				asso.servico = desenv;
				asso.tipoAtributo = attPrazo;
				asso.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
				asso.salvar();
				
				SrConfiguracao asso2 = new SrConfiguracao();
				asso2.itemConfiguracao = geDoc;
				asso2.tipoAtributo = attNomeSys;
				asso2.atributoObrigatorio = true;
				asso2.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
				asso2.salvar();

				SrConfiguracao atenEPre = new SrConfiguracao();
				atenEPre.preAtendente = JPA.em().find(DpLotacao.class, 5046L); //SEGEP
				atenEPre.atendente = JPA.em().find(DpLotacao.class, 4961L); // CSIS
				atenEPre.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
				atenEPre.salvar();
				
				SrConfiguracao pos = new SrConfiguracao();
				pos.posAtendente = JPA.em().find(DpLotacao.class, 4901L); // STI
				pos.servico = manutSoft;
				pos.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
				pos.salvar();
				
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}