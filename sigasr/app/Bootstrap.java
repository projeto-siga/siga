import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if (SrItemConfiguracao.count() == 0) {
			SrItemConfiguracao softwares = new SrItemConfiguracao("Softwares").save();
			SrItemConfiguracao geDoc = new SrItemConfiguracao(
					"Sistemas de gestao documental", softwares).save();
			SrItemConfiguracao gePat = new SrItemConfiguracao(
					"Sistemas de gestao patrimonial", softwares).save();
			SrItemConfiguracao geTrab = new SrItemConfiguracao(
					"Sistemas de gestao do trabalho", softwares).save();
			SrItemConfiguracao geRH = new SrItemConfiguracao(
					"Sistemas de recursos humanos", softwares).save();
			SrItemConfiguracao sigaex = new SrItemConfiguracao(
					"SIGA-EX - SIGA Expedientes", geDoc).save();
			SrItemConfiguracao malotes = new SrItemConfiguracao(
					"CM - Sistemas de malotes", geDoc).save();
			SrItemConfiguracao sisapa = new SrItemConfiguracao(
					"SISAPA - Sistemas de processos administrativos", geDoc)
					.save();
			SrItemConfiguracao sigasr = new SrItemConfiguracao(
					"SIGA-SE - SIGA Servicos", geTrab).save();
			SrItemConfiguracao carh = new SrItemConfiguracao(
					"Sistemas de Cadastro de Recursos Humanos", geRH).save();
			SrItemConfiguracao bie = new SrItemConfiguracao("Boletim Interno",
					geDoc).save();
			SrItemConfiguracao dou = new SrItemConfiguracao(
					"Publicacao no Diario Oficial", geDoc).save();
			
			SrFormaAcompanhamento email = new SrFormaAcompanhamento("E-mail").save();
			SrFormaAcompanhamento presencial = new SrFormaAcompanhamento("Cobrança presencial").save();
			
			/*
			 * if (SrItemConfiguracao.count() == 0) {
			 * Fixtures.loadModels("initial-data.yml"); }
			 */
		}
	}

}