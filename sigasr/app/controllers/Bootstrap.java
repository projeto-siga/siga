package controllers;

import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrServico;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
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
				SrItemConfiguracao softwares = new SrItemConfiguracao(
						"01.00.00.00", "Softwares").save();

				geDoc = new SrItemConfiguracao("01.01.00.00",
						"Sistemas de gestão documental").save();

				gePat = new SrItemConfiguracao("01.02.00.00",
						"Sistemas de gestão patrimonial").save();

				SrServico soft = new SrServico("01.00",
						"Serviços típicos de software").save();

				SrServico hard = new SrServico("02.00",
						"Serviços típicos de hardware").save();

				SrConfiguracao design = new SrConfiguracao();
				design.itemConfiguracao = softwares;
				design.servico = soft;
				design.posAtendente = JPA.em().find(DpLotacao.class, 5053L);
				design.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
				design.save();

				System.out.println(design.getIdConfiguracao());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}