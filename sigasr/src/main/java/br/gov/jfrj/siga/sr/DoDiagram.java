package br.gov.jfrj.siga.sr;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.CpAplicacaoFeriado;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoAcao;

public class DoDiagram {

	public static void testGeraDiagramaSR() throws Exception {
		 Diagram d = new Diagram();
		 d.setfMergeWithAbstractClass(true);
		 boolean fI = false;
		
		 d.addClass(CpComplexo.class, fI, false, false);
		 d.addClass(CpConfiguracao.class, fI, false, false);
		 d.addClass(CpAplicacaoFeriado.class, fI, false, false);
		 d.addClass(CpFeriado.class, fI, false, false);
		 d.addClass(CpLocalidade.class, fI, false, false);
		 d.addClass(CpMarca.class, fI, false, false);
		 d.addClass(CpMarcador.class, fI, false, false);
		 d.addClass(CpOcorrenciaFeriado.class, fI, false, false);
		 d.addClass(CpOrgaoUsuario.class, fI, false, false);
		 d.addClass(CpTipoLotacao.class, fI, false, false);
		 d.addClass(CpUF.class, fI, false, false);
		 d.addClass(DpCargo.class, fI, false, false);
		 d.addClass(DpFuncaoConfianca.class, fI, false, false);
		 d.addClass(DpLotacao.class, fI, false, false);
		 d.addClass(DpPessoa.class, fI, false, false);

		 d.addClass(SrAcao.class, fI, true, false);
		 d.addClass(SrAcordo.class, fI, true, false);
		 d.addClass(SrConfiguracao.class, fI, true, false);
		 d.addClass(SrItemConfiguracao.class, fI, true, false);
		 d.addClass(SrMovimentacao.class, fI, true, false);
		 d.addClass(SrSolicitacao.class, fI, true, false);
		 d.addClass(SrTipoAcao.class, fI, true, false);
		
		 d.createGraphML("sigagc.graphml", true, false);

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		testGeraDiagramaSR();
	}

}
