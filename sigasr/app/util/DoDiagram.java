package util;

import models.SrAcao;
import models.SrArquivo;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrMovimentacao;
import models.SrPergunta;
import models.SrPesquisa;
import models.SrResposta;
import models.SrSolicitacao;
import models.SrTipoMovimentacao;
import models.SrTipoPergunta;
import br.gov.jfrj.siga.model.Diagram;

public class DoDiagram {

	public static void testGeraDiagramaGC() throws Exception {
		Diagram d = new Diagram();
		d.setfMergeWithAbstractClass(true);
		boolean fI = true;

		//d.addClass(DpPessoa.class, true);
		//d.addClass(DpLotacao.class, true);
		//d.addClass(CpOrgaoUsuario.class, true);
		//d.addClass(CpComplexo.class, true);
		//d.addClass(CpMarcador.class, true);

		//d.addClass(SrConfiguracao.class, true);
		d.addClass(SrMovimentacao.class, false);
		d.addClass(SrArquivo.class, false);
		//d.addClass(SrAtributo.class, false);
		//d.addClass(SrFormaAcompanhamento.class, false);
		d.addClass(SrItemConfiguracao.class, false);
		d.addClass(SrAcao.class, false);
		//d.addClass(SrMarca.class, true);
		d.addClass(SrSolicitacao.class, false);
		//d.addClass(SrTipoAtributo.class, false);
		//d.addClass(SrGrauSatisfacao.class, false);
		//d.addClass(SrGravidade.class, false);
		d.addClass(SrLista.class, false);
		d.addClass(SrPergunta.class, false);
		d.addClass(SrPesquisa.class, false);
		d.addClass(SrResposta.class, false);
		//d.addClass(SrTendencia.class, false);
		d.addClass(SrTipoMovimentacao.class, false);
		d.addClass(SrTipoPergunta.class, false);
		//d.addClass(SrUrgencia.class, false);

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
