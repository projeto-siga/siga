package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodePublicarPortalDaTransparencia;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class ProcessosSiglaGerarLinkPost implements IExApiV1.IProcessosSiglaGerarLinkPost {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = ctx.getTitular();
        DpLotacao lotaTitular = ctx.getLotaTitular();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);
        ExDocumento doc = mob.getDoc();

        ctx.assertAcesso(mob, titular, lotaTitular);

        Ex.getInstance().getComp().afirmar("Não é possível gerar o link para o processo especificado", ExPodePublicarPortalDaTransparencia.class, cadastrante, lotaTitular, mob);

        ExNivelAcesso exTipoSig = ExDao.getInstance().consultar(ExNivelAcesso.ID_PUBLICO, ExNivelAcesso.class, false);

        Ex.getInstance().getBL().redefinirNivelAcesso(cadastrante, lotaTitular, doc, null, lotaTitular, cadastrante, cadastrante, cadastrante, null, exTipoSig);

        CpToken cpToken;
        try {
            cpToken = Cp.getInstance().getBL().gerarUrlPermanente(doc.getIdDoc());
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao gerar Token.", e);
        }
        CpToken cpToken1 = CpDao.getInstance().obterCpTokenPorTipoToken(cpToken.getIdTpToken(), cpToken.getToken());

        String urlPermanente;
        if (cpToken != null) {
            urlPermanente = Cp.getInstance().getBL().obterURLPermanente(cpToken.getIdTpToken().toString(), cpToken.getToken());
        } else {
            urlPermanente = "Endereço público não disponível.";
        }

        resp.link = urlPermanente;
    }

    @Override
    public String getContext() {
        return "obter link do processo";
    }


}
