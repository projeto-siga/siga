package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeGerarLinkPublicoDoProcesso;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.vraptor.Transacional;

import java.util.Set;

@Transacional
public class ProcessosSiglaGerarLinkPublicoPost implements IExApiV1.IProcessosSiglaGerarLinkPublicoPost {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = ctx.getTitular();
        DpLotacao lotaTitular = ctx.getLotaTitular();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Processo a gerar link público");

        ctx.assertAcesso(mob, titular, lotaTitular);

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO);
        if (!movs.isEmpty())
            throw new AplicacaoException("Link público do Processo já foi gerado anteriormente.");

        Ex.getInstance().getComp().afirmar("Não é possível gerar o link para o processo especificado", ExPodeGerarLinkPublicoDoProcesso.class, cadastrante, lotaTitular, mob);

        resp.link = Ex.getInstance().getBL().gerarLinkPublicoProcesso(cadastrante, titular, lotaTitular, mob);

    }

    @Override
    public String getContext() {
        return "gerar link público do Processo";
    }


}
