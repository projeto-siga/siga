import models.CpClassificacao;
import models.CpTipoClassificacao;
import models.GcConfiguracao;
import models.GcInformacao;
import models.GcMarca;
import models.GcMovimentacao;
import models.GcTipoInformacao;
import models.GcTipoMovimentacao;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class DoDiagram {

	public static void testGeraDiagramaGC() throws Exception {
		Diagram d = new Diagram();
		d.setfMergeWithAbstractClass(true);
		boolean fI = true;

		d.addClass(DpPessoa.class, fI, false, false);
		d.addClass(DpLotacao.class, fI, false, false);
		d.addClass(CpOrgaoUsuario.class, fI, false, false);
		d.addClass(CpMarca.class, fI, false, false);
		d.addClass(CpConfiguracao.class, false, false, false);

		d.addClass(GcInformacao.class, false);
		d.addClass(GcTipoInformacao.class, false);
		d.addClass(CpClassificacao.class, false);
		d.addClass(CpTipoClassificacao.class, false);
		d.addClass(GcMovimentacao.class, false);
		d.addClass(GcTipoMovimentacao.class, false);
		d.addClass(GcMarca.class, false);
		d.addClass(GcConfiguracao.class, false);

		d.createGraphML("sigagc.graphml", true, false);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaGC();
	}

}
