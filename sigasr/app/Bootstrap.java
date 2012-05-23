import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if (SrItemConfiguracao.count() == 0) {
			SrItemConfiguracao softwares = new SrItemConfiguracao(
					"01.00.00.00", "Softwares").save();
			SrItemConfiguracao geDoc = new SrItemConfiguracao("01.01.00.00",
					"Sistemas de gestão documental").save();
			SrItemConfiguracao gePat = new SrItemConfiguracao("01.02.00.00",
					"Sistemas de gestão patrimonial").save();
			SrItemConfiguracao geTrab = new SrItemConfiguracao("01.03.00.00",
					"Sistemas de gestão do trabalho").save();
			SrItemConfiguracao geRH = new SrItemConfiguracao("01.04.00.00",
					"Sistemas de recursos humanos").save();
			SrItemConfiguracao sigaex = new SrItemConfiguracao("01.01.01.00.",
					"SIGA-EX - SIGA Expedientes").save();
			SrItemConfiguracao malotes = new SrItemConfiguracao("01.01.02.00.",
					"CM - Sistemas de malotes").save();
			SrItemConfiguracao sisapa = new SrItemConfiguracao("01.01.03.00.",
					"SISAPA - Sistemas de processos administrativos").save();
			SrItemConfiguracao sigasr = new SrItemConfiguracao("01.03.01.00.",
					"SIGA-SE - SIGA Serviços").save();
			SrItemConfiguracao carh = new SrItemConfiguracao("01.04.01.00.",
					"Sistemas de Cadastro de Recursos Humanos").save();
			SrItemConfiguracao bie = new SrItemConfiguracao("01.02.04.00.",
					"Boletim Interno").save();
			SrItemConfiguracao dou = new SrItemConfiguracao("01.01.05.00.",
					"Publicacao no Diário Oficial").save();
			SrServico soft = new SrServico("01.00",
					"Serviços típicos de software").save();
			SrServico hard = new SrServico("02.00",
			"Serviços típicos de hardware").save();
			SrServico buyhard = new SrServico("02.01",
			"Comprar hardware").save();
			SrServico proj = new SrServico("01.01",
					"Desenvolver projeto de software").save();
			SrServico tecno = new SrServico("01.02", "Estudar tecnologia")
					.save();
			SrServico rel = new SrServico("01.03",
					"Criar novo relatório ou consulta").save();
			SrServico ext = new SrServico("01.04",
					"Implantar software externo").save();
			SrServico aquis = new SrServico("01.05",
					"Especificar software para aquisição").save();
			SrServico corretiv = new SrServico("01.06",
					"Manutenir corretivamente software").save();
			SrServico adaptativ = new SrServico("01.07",
					"Manutenir adaptativamente software").save();
			SrServico evolutiv = new SrServico("01.08",
					"Manutenir evolutivamente software").save();
			SrServico strut = new SrServico("01.09",
					"Manutenir a estrutura de dados").save();
			SrServico integr = new SrServico("01.10",
					"Migrar e integrar software").save();
			SrServico user = new SrServico("01.11",
					"Fornecer suporte a usuários de software externo").save();
			SrServico lent = new SrServico("01.12",
					"Corrigir lentidão de software").save();
			SrServico indisponib = new SrServico("01.13",
					"Corrigir indisponibilidade de software").save();
			SrServico suporte = new SrServico("01.14",
					"Fornecer suporte a usuários de software interno").save();
			SrServico document = new SrServico("01.15", "Documentar software")
					.save();
			SrServico acesso = new SrServico("01.16",
					"Cadastrar acesso de usuários").save();
			SrServico param = new SrServico("01.17",
					"Configurar parâmetros funcionais de software").save();
			SrServico stations = new SrServico("01.18",
					"Instalar software em estações de trabalho").save();
			SrServico install = new SrServico("01.19",
					"Instalar software nos Servidores").save();

			/*
			 * if (SrItemConfiguracao.count() == 0) {
			 * Fixtures.loadModels("initial-data.yml"); }
			 */
		}
	}

}