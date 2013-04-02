import models.GcArquivo;
import models.GcConfiguracao;
import models.GcInformacao;
import models.GcMarca;
import models.GcMovimentacao;
import models.GcTag;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
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
		boolean fI = true;

		d.addClass(DpPessoa.class, fI, false, false);
		d.addClass(DpLotacao.class, fI, false, false);
		d.addClass(CpOrgaoUsuario.class, fI, false, false);
		d.addClass(CpMarca.class, fI, true, false);
		d.addClass(CpConfiguracao.class, false, false, false);

		d.addClass(GcArquivo.class, false);
		d.addClass(GcConfiguracao.class, false);
		d.addClass(GcInformacao.class, false);
		d.addClass(GcMarca.class, false);
		d.addClass(GcMovimentacao.class, false);
		d.addClass(GcTipoInformacao.class, false);
		d.addClass(GcTipoMovimentacao.class, false);
		d.addClass(GcTag.class, false);

		d.createGraphML("sigagc.graphml", true, false);

		GcMarca m = new GcMarca();
		System.out.println(m.toString());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaGC();
	}

}
