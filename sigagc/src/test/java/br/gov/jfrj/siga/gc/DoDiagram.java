package br.gov.jfrj.siga.gc;

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
		// Diagram d = new Diagram();
		// d.setfMergeWithAbstractClass(true);
		// boolean fI = false;
		//
		// d.addClass(DpPessoa.class, fI, false, false);
		// d.addClass(DpLotacao.class, fI, false, false);
		// d.addClass(CpOrgaoUsuario.class, fI, false, false);
		// d.addClass(CpMarca.class, fI, true, false);
		// d.addClass(CpConfiguracao.class, false, false, false);
		//
		// d.addClass(GcArquivo.class, fI, true, false);
		// d.addClass(GcConfiguracao.class, fI, true, false);
		// d.addClass(GcInformacao.class, fI, true, false);
		// d.addClass(GcMarca.class, fI, true, false);
		// d.addClass(GcMovimentacao.class, fI, true, false);
		// d.addClass(GcTipoInformacao.class, fI, true, false);
		// d.addClass(GcTipoMovimentacao.class, fI, true, false);
		// d.addClass(GcTag.class, fI, true, false);
		//
		// d.createGraphML("sigagc.graphml", true, false);
		//
		// GcMarca m = new GcMarca();
		// System.out.println(m.toString());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaGC();
	}

}
