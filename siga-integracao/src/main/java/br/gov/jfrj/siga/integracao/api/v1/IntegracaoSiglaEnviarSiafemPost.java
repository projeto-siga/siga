package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeEnviarSiafem;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.integracao.api.v1.IIntegracaoApiV1.IIntegracaoSiglaEnviarSiafemPost;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class IntegracaoSiglaEnviarSiafemPost implements IIntegracaoSiglaEnviarSiafemPost {

    public void run(Request req, Response resp, IntegracaoApiV1Context ctx) throws Exception {
        String usuarioSiafem = req.usuarioSiafem;
        String senhaSiafem = req.senhaSiafem;

        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = cadastrante;
        DpLotacao lotaTitular = titular.getLotacao();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a ser enviado ao SIAFEM");

        Ex.getInstance().getComp()
                .afirmar(
                        "O documento " + mob.getSigla() + " não pode ser enviado ao SIAFEM por "
                                + titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(),
                        ExPodeEnviarSiafem.class, titular, lotaTitular, mob.doc());

        Ex.getInstance().getBL().gravarSiafem(usuarioSiafem, senhaSiafem, mob.doc(), cadastrante, lotaTitular);

        resp.sigla = mob.doc().getCodigo();

        resp.status = "OK";
    }

    protected ExDao dao() {
        return ExDao.getInstance();
    }

    @Override
    public String getContext() {
        return "Enviar ao SIAFEM";
    }

}
