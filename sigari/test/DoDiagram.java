import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class DoDiagram {
	static {
		// ClassPool pool = ClassPool.getDefault();
		// CtClass cc;
		// try {
		// cc = pool.get("br.gov.jfrj.siga.model.Objeto");
		// cc.setSuperclass(pool.get("play.db.jpa.GenericModel"));
		// cc.writeFile();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		System.out.println("*** Classe alterada.");
	}

	public static void testGeraDiagramaGC() throws Exception {
		Diagram d = new Diagram();
		d.setfMergeWithAbstractClass(true);
		boolean fI = false;

		d.addClass(DpPessoa.class, fI, false, false);
		d.addClass(DpLotacao.class, fI, false, false);
		d.addClass(CpOrgaoUsuario.class, fI, false, false);
		d.addClass(CpMarca.class, fI, true, false);
		d.addClass(CpConfiguracao.class, false, false, false);

		d.createGraphML("sigari.graphml", true, false);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaGC();
	}

}
