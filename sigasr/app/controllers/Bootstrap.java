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
				SrItemConfiguracao softwares = (SrItemConfiguracao) new SrItemConfiguracao(
						"01.00.00.00", "Softwares").salvar();

				geDoc = (SrItemConfiguracao) new SrItemConfiguracao("01.01.00.00",
						"Sistemas de gestão documental").salvar();

				gePat = (SrItemConfiguracao) new SrItemConfiguracao("01.02.00.00",
						"Sistemas de gestão pcabo yatrimonial").salvar();

				SrServico soft = (SrServico) new SrServico("01.00",
						"Serviços típicos de software").salvar();
				
				SrServico manutSoft = (SrServico) new SrServico("01.01",
				"Manutenção de software").salvar();

				SrServico hard = (SrServico) new SrServico("02.00",
						"Serviços típicos de hardware").salvar();

				SrConfiguracao atenEPos = new SrConfiguracao();
				atenEPos.itemConfiguracao = geDoc;
				atenEPos.servico = manutSoft;
				atenEPos.atendente = JPA.em().find(DpLotacao.class, 4961L); // CSIS
				atenEPos.posAtendente = JPA.em().find(DpLotacao.class, 4901L); // STI
				atenEPos.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
				atenEPos.save();
				
				SrConfiguracao pre = new SrConfiguracao();
				pre.itemConfiguracao = softwares;
				pre.servico = soft;
				pre.preAtendente = JPA.em().find(DpLotacao.class, 5046L); // SEGEP
				pre.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class,
						CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
				pre.save();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}