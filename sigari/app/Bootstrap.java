import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() throws Exception {

		if (false && !Play.classes.hasClass("br.gov.jfrj.siga.dp.DpPessoa")) {
			System.out
					.println("\n\n\n*********************** INICIALIZANDO CLASSES *****************************");
			System.out.println(Play.usePrecompiled);
			Play.javaPath.add(VirtualFile.fromRelativePath("../siga-cp/src/"));
			System.out.println(Play.javaPath);

			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.CpOrgaoUsuario");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpCargo");
			Play.classes
					.getApplicationClass("br.gov.jfrj.siga.dp.DpFuncaoConfianca");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpLotacao");
			Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpPessoa");

			System.out.println(Play.classes
					.hasClass("br.gov.jfrj.siga.dp.DpPessoa"));
			System.out.println("classes carregadas...");
		}
	}
}