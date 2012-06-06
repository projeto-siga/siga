import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.*;
import play.db.jpa.JPA;
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
			SrItemConfiguracao sigaex = new SrItemConfiguracao("01.01.01.00",
					"SIGA-EX - SIGA Expedientes").save();
			SrItemConfiguracao malotes = new SrItemConfiguracao("01.01.02.00",
					"CM - Sistemas de malotes").save();
			SrItemConfiguracao sisapa = new SrItemConfiguracao("01.01.03.00",
					"SISAPA - Sistemas de processos administrativos").save();
			SrItemConfiguracao sigasr = new SrItemConfiguracao("01.03.01.00",
					"SIGA-SE - SIGA Serviços").save();
			SrItemConfiguracao carh = new SrItemConfiguracao("01.04.01.00",
					"Sistemas de Cadastro de Recursos Humanos").save();
			SrItemConfiguracao bie = new SrItemConfiguracao("01.02.04.00",
					"Boletim Interno").save();
			SrItemConfiguracao dou = new SrItemConfiguracao("01.01.05.00",
					"Publicacao no Diário Oficial").save();
			SrServico soft = new SrServico("01.00",
					"Serviços típicos de software").save();
			SrServico hard = new SrServico("02.00",
					"Serviços típicos de hardware").save();
			SrServico buyhard = new SrServico("02.01", "Comprar hardware")
					.save();
			SrServico proj = new SrServico("01.01",
					"Desenvolver projeto de software").save();
			SrServico tecno = new SrServico("01.02", "Estudar tecnologia")
					.save();
			SrServico rel = new SrServico("01.03",
					"Criar novo relatório ou consulta").save();
			SrServico ext = new SrServico("01.04", "Implantar software externo")
					.save();
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

			DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);
			DpPessoa gmb = JPA.em().find(DpPessoa.class, 3558L);
			DpPessoa simone = JPA.em().find(DpPessoa.class, 3729L);
			DpPessoa lys = JPA.em().find(DpPessoa.class, 3853L);
			DpPessoa kpf = JPA.em().find(DpPessoa.class, 10331L);

			SrSolicitacao sol1 = new SrSolicitacao();
			sol1.cadastrante = eeh;
			sol1.descrSolicitacao = "Solicito testar bastante, para que depois não se dê a desculpa de não ter havido oportunidades suficientes para detecção de erros.";
			sol1.formaAcompanhamento = SrFormaAcompanhamento.ANDAMENTO;
			sol1.gravidade = SrGravidade.MUITO_GRAVE;
			sol1.itemConfiguracao = sigasr;
			sol1.servico = corretiv;
			sol1.local = "Almirante Barroso";
			sol1.lotaCadastrante = eeh.getLotacao();
			sol1.orgaoUsuario = eeh.getOrgaoUsuario();
			sol1.solicitante = gmb;
			sol1.lotaSolicitante = gmb.getLotacao();
			sol1.tendencia = SrTendencia.PIORA_CURTO_PRAZO;
			sol1.urgencia = SrUrgencia.ALGUMA_URGENCIA;
			sol1.dtReg = new Date();
			sol1.criar();

			SrSolicitacao sol2 = new SrSolicitacao();
			sol2.cadastrante = lys;
			sol2.descrSolicitacao = "Também solicito a execução de muitos testes, visto que é limitada a nossa paciência e capacidade de assimilação de alto nível de stress acarretado por reclamações enfurecidas.";
			sol2.formaAcompanhamento = SrFormaAcompanhamento.FECHAMENTO;
			sol2.gravidade = SrGravidade.EXTREMAMENTE_GRAVE;
			sol2.itemConfiguracao = sigasr;
			sol2.servico = install;
			sol2.local = "Rio Branco";
			sol2.lotaCadastrante = lys.getLotacao();
			sol2.orgaoUsuario = lys.getOrgaoUsuario();
			sol2.solicitante = simone;
			sol2.lotaSolicitante = simone.getLotacao();
			sol2.tendencia = SrTendencia.PIORA_IMEDIATA;
			sol2.urgencia = SrUrgencia.AGIR_IMEDIATO;
			try {
				sol2.dtReg = new SimpleDateFormat("dd/MM/yyyy HH:mm")
						.parse("21/05/2012 21:00");
			} catch (ParseException pe) {
				//
			}
			sol2.criar();
			// JPA.em().getTransaction().commit();
		}
	}

}