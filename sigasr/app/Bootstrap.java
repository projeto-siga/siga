import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import controllers.SrDao;

import br.gov.jfrj.siga.base.AplicacaoException;
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
			try {
				SrItemConfiguracao softwares = new SrItemConfiguracao(
						"01.00.00.00", "Softwares");
				SrDao.getInstance().salvar(softwares, "RJ13285");
				SrItemConfiguracao geDoc = new SrItemConfiguracao(
						"01.01.00.00", "Sistemas de gestão documental");
				SrDao.getInstance().salvar(geDoc, "RJ13285");
				SrItemConfiguracao gePat = new SrItemConfiguracao(
						"01.02.00.00", "Sistemas de gestão patrimonial");
				SrDao.getInstance().salvar(gePat, "RJ13285");
				/*
				 * SrItemConfiguracao geTrab = new
				 * SrItemConfiguracao("01.03.00.00",
				 * "Sistemas de gestão do trabalho");
				 * SrDao.getInstance().salvar(geTrab, "RJ13285");
				 * SrItemConfiguracao geRH = new
				 * SrItemConfiguracao("01.04.00.00",
				 * "Sistemas de recursos humanos");
				 * SrDao.getInstance().salvar(geRH, "RJ13285");
				 * SrItemConfiguracao sigaex = new
				 * SrItemConfiguracao("01.01.01.00",
				 * "SIGA-EX - SIGA Expedientes");
				 * SrDao.getInstance().salvar(sigaex, "RJ13285");
				 * SrItemConfiguracao masotes = new
				 * SrItemConfiguracao("01.01.02.00",
				 * "CM - Sistemas de malotes");
				 * SrDao.getInstance().salvar(masotes, "RJ13285");
				 * SrItemConfiguracao sisapa = new
				 * SrItemConfiguracao("01.01.03.00",
				 * "SISAPA - Sistemas de processos administrativos");
				 * SrDao.getInstance().salvar(sisapa, "RJ13285");
				 * SrItemConfiguracao sigasr = new
				 * SrItemConfiguracao("01.03.01.00", "SIGA-SE - SIGA Serviços");
				 * SrDao.getInstance().salvar(sigasr, "RJ13285");
				 * SrItemConfiguracao carh = new
				 * SrItemConfiguracao("01.04.01.00",
				 * "Sistemas de Cadastro de Recursos Humanos");
				 * SrDao.getInstance().salvar(carh, "RJ13285");
				 * SrItemConfiguracao bie = new
				 * SrItemConfiguracao("01.02.04.00", "Boletim Interno");
				 * SrDao.getInstance().salvar(bie, "RJ13285");
				 * SrItemConfiguracao dou = new
				 * SrItemConfiguracao("01.01.05.00",
				 * "Publicacao no Diário Oficial");
				 * SrDao.getInstance().salvar(dou, "RJ13285");
				 */
				SrServico soft = new SrServico("01.00",
						"Serviços típicos de software");
				SrDao.getInstance().salvar(soft, "RJ13285");
				SrServico hard = new SrServico("02.00",
						"Serviços típicos de hardware");
				SrDao.getInstance().salvar(hard, "RJ13285");
				SrServico buyhard = new SrServico("02.01", "Comprar hardware")
						.save();
				buyhard.descrServico = "Comprar hardware 2";
				//buyhard.save();
				JPA.em().flush();
				
				/*
				 * SrServico proj = new SrServico("01.01",
				 * "Desenvolver projeto de software");
				 * SrDao.getInstance().salvar(proj, "RJ13285"); SrServico tecno
				 * = new SrServico("01.02", "Estudar tecnologia") .save();
				 * SrServico rel = new SrServico("01.03",
				 * "Criar novo relatório ou consulta");
				 * SrDao.getInstance().salvar(rel, "RJ13285"); SrServico ext =
				 * new SrServico("01.04", "Implantar software externo") .save();
				 * SrServico aquis = new SrServico("01.05",
				 * "Especificar software para aquisição");
				 * SrDao.getInstance().salvar(aquis, "RJ13285"); SrServico
				 * corretiv = new SrServico("01.06",
				 * "Manutenir corretivamente software");
				 * SrDao.getInstance().salvar(corretiv, "RJ13285"); SrServico
				 * adaptativ = new SrServico("01.07",
				 * "Manutenir adaptativamente software");
				 * SrDao.getInstance().salvar(adaptativ, "RJ13285"); SrServico
				 * evolutiv = new SrServico("01.08",
				 * "Manutenir evolutivamente software");
				 * SrDao.getInstance().salvar(evolutiv, "RJ13285"); SrServico
				 * strut = new SrServico("01.09",
				 * "Manutenir a estrutura de dados");
				 * SrDao.getInstance().salvar(strut, "RJ13285"); SrServico
				 * integr = new SrServico("01.10",
				 * "Migrar e integrar software");
				 * SrDao.getInstance().salvar(integr, "RJ13285"); SrServico user
				 * = new SrServico("01.11",
				 * "Fornecer suporte a usuários de software externo");
				 * SrDao.getInstance().salvar(user, "RJ13285"); SrServico lent =
				 * new SrServico("01.12", "Corrigir lentidão de software");
				 * SrDao.getInstance().salvar(lent, "RJ13285"); SrServico
				 * indisponib = new SrServico("01.13",
				 * "Corrigir indisponibilidade de software");
				 * SrDao.getInstance().salvar(indisponib, "RJ13285"); SrServico
				 * suporte = new SrServico("01.14",
				 * "Fornecer suporte a usuários de software interno");
				 * SrDao.getInstance().salvar(suporte, "RJ13285"); SrServico
				 * document = new SrServico("01.15", "Documentar software");
				 * SrDao.getInstance().salvar(document, "RJ13285"); SrServico
				 * acesso = new SrServico("01.16",
				 * "Cadastrar acesso de usuários");
				 * SrDao.getInstance().salvar(acesso, "RJ13285"); SrServico
				 * param = new SrServico("01.17",
				 * "Configurar parâmetros funcionais de software");
				 * SrDao.getInstance().salvar(param, "RJ13285"); SrServico
				 * stations = new SrServico("01.18",
				 * "Instalar software em estações de trabalho");
				 * SrDao.getInstance().salvar(stations, "RJ13285"); SrServico
				 * install = new SrServico("01.19",
				 * "Instalar software nos Servidores");
				 * SrDao.getInstance().salvar(install, "RJ13285");
				 */

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
				sol1.itemConfiguracao = gePat;
				sol1.servico = soft;
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
				sol2.itemConfiguracao = geDoc;
				sol2.servico = hard;
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
				
				sol2.descrSolicitacao = "asdf";
				
				JPA.em().persist(sol2);
				JPA.em().flush();
				
			} catch (AplicacaoException ae) {
				int a = 0;
			}
		}
	}

}