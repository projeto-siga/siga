package br.gov.jfrj.siga.tp.vraptor.job;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.tasks.helpers.TriggerBuilder;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import br.com.caelum.vraptor.tasks.scheduler.TaskScheduler;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.TpDao;

@ApplicationScoped
@Scheduled(fixedRate = 1)
public class CustomScheduler {

	private static final String CRON_AGENDAR_TAREFAS = "cron.agendarTarefas";
	private static final String CRON_EMAIL = "cron.inicio";
	private static final String CRON_WORKFLOW = "cron.iniciow";

	public CustomScheduler(TaskScheduler scheduler) {
		try {
			criaEntityManager();
			
			boolean executa = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(CRON_AGENDAR_TAREFAS));
			if(executa) {
				String cronWorkFlow = Parametro.buscarConfigSistemaEmVigor(CRON_WORKFLOW);
				String cronEmail = Parametro.buscarConfigSistemaEmVigor(CRON_EMAIL);
				
				scheduler.schedule(WorkFlowNotificacao.class, new TriggerBuilder().cron(cronWorkFlow), "WorkFlowNotificacao");
				scheduler.schedule(EmailNotificacao.class, new TriggerBuilder().cron(cronEmail), "EmailNotificacao");
			} else {
				Logger.getLogger("custom.schedule").info("Agendador desligado. As tarefas nao serao executadas. Caso isso seja um problema, favor verificar a existencia e o valor do parametro \"" + CRON_AGENDAR_TAREFAS + "\".");
			}
		} catch (Exception e) {
			Logger.getLogger("custom.schedule").info("Erro no Agendador de tarefas: " + e.getMessage());
		} finally {
			fecharEntityManager();
		}
	}

	public static void criaEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
		EntityManager em = factory.createEntityManager();
		Session session = (Session) em.getDelegate();

		TpDao.freeInstance();
		TpDao.getInstance(session, session.getSessionFactory().openStatelessSession());

		ContextoPersistencia.setEntityManager(em);
		HibernateUtil.setSessao(session);
	}

	public static void fecharEntityManager() {
		EntityManager em = ContextoPersistencia.em();
		if (em != null && em.isOpen()) {
			em.close();
		}
		TpDao.freeInstance();
	}
}