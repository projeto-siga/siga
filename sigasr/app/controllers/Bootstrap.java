package controllers;

import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrSolicitacao;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {

		//if (!Play.classes.hasClass("br.gov.jfrj.siga.dp.DpPessoa")) {
			System.out
					.println("\n\n\n*********************** INICIALIZANDO CLASSES *****************************");
//			System.out.println(Play.usePrecompiled);
//			System.out.println(Play.javaPath);
			DpPessoa eeh = DpPessoa.findById(46962L);
			SrSolicitacao s = new SrSolicitacao();
			s.findById(123L);
			System.out.println(Play.classes
					.hasClass("br.gov.jfrj.siga.cp.CpServico"));

			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.CpOrgaoUsuario");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpCargo");
			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.DpFuncaoConfianca");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpLotacao");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpPessoa");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.cp.CpServico");

			System.out.println(Play.classes
					.hasClass("br.gov.jfrj.siga.cp.CpServico"));
			System.out.println("classes carregadas...");
		//}

	}
}