import models.SrAndamento;
import models.SrArquivo;
import models.SrAtributo;
import models.SrConfiguracao;
import models.SrFormaAcompanhamento;
import models.SrItemConfiguracao;
import models.SrMarca;
import models.SrServico;
import models.SrSolicitacao;
import models.SrTipoAtributo;
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

		d.addClass(SrConfiguracao.class, false);
		d.addClass(SrAndamento.class, false);
		d.addClass(SrArquivo.class, false);
		d.addClass(SrAtributo.class, false);
		d.addClass(SrFormaAcompanhamento.class, false);
		d.addClass(SrItemConfiguracao.class, false);
		d.addClass(SrServico.class, false);
		d.addClass(SrMarca.class, false);
		d.addClass(SrSolicitacao.class, false);
		d.addClass(SrTipoAtributo.class, false);

		d.createGraphML("sigasr.graphml", true, false);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaGC();
	}

}
