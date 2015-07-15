package br.gov.jfrj.siga.uteis;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;

public class PessoaLotaFuncCargoSelecaoHelper {

    public static void adicionarCamposSelecao(Result result) {
        result.include("dpPessoaSel", new DpPessoaSelecao());
        result.include("lotacaoSel", new DpLotacaoSelecao());
        result.include("funcaoConfiancaSel", new DpFuncaoConfiancaSelecao());
        result.include("cargoSel", new DpCargoSelecao());
        result.include("cpGrupoSel", new CpPerfilSelecao());
    }
}
