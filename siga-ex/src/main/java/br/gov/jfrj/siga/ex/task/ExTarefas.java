package br.gov.jfrj.siga.ex.task;

import br.gov.jfrj.siga.base.Prop;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ExTarefas {
    private static final Logger log = Logger.getLogger("ExTarefas");
    private static final Timer timer = new Timer(true);

    public static void agendarTarefaGarbageCollector() {
        timer.schedule(getTask(() -> {
            if (Prop.getBool("garbage.tarefa")) {
                log.info("[GarbageCollectorTask] [Periodicidade - "+ Prop.get("garbage.tarefa.periodo") +" minuto(s)] ");
                timer.scheduleAtFixedRate(getTask(() -> System.gc()), 0, Duration.ofMinutes(Prop.getInt("garbage.tarefa.periodo")).toMillis());
            }
        }), Duration.ofMinutes(1).toMillis());
    }

    private static TimerTask getTask(Runnable r) {
        return new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        };
    }
}