package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaTramitarEmParaleloPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeTransferir;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaTramitarEmParaleloPost implements IDocumentosSiglaTramitarEmParaleloPost {

    private void validarPreenchimentoDestino(Request req, Response resp) throws AplicacaoException {
        if (StringUtils.isEmpty(req.lotacao) && StringUtils.isEmpty(req.matricula))
            throw new AplicacaoException("Você deve fornecer ou matricula ou lotacao *com* a matricula");
    }

    private void validarAcesso(ExApiV1Context ctx, Request req, DpPessoa titular, DpLotacao lotaTitular, ExMobil mob)
            throws Exception, PresentableUnloggedException {
        ctx.assertAcesso(mob, titular, lotaTitular);
        Ex.getInstance().getComp().afirmar(
                titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta()
                        + " não pode " + getContext() + " o documento " + req.sigla,
                ExPodeTransferir.class, titular, lotaTitular, mob);
    }

    private DpPessoa getResponsavel(Request req) {
        DpPessoa pes = null;
        if (StringUtils.isNotEmpty(req.matricula)) {
            pes = new DpPessoa();
            pes.setSigla(req.matricula);
            pes = ExDao.getInstance().consultarPorSigla(pes);
        }
        return pes;
    }

    private DpLotacao getLotacao(Request req) {
        DpLotacao lot = null;
        if (StringUtils.isNotEmpty(req.lotacao)) {
            lot = new DpLotacao();
            lot.setSigla(req.lotacao);
            lot = ExDao.getInstance().consultarPorSigla(lot);
        }
        return lot;
    }

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        validarPreenchimentoDestino(req, resp);

        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = ctx.getTitular();
        DpLotacao lotaTitular = ctx.getLotaTitular();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a " + getContext());

        validarAcesso(ctx, req, titular, lotaTitular, mob);

        DpLotacao lot = getLotacao(req);
        DpPessoa pes = getResponsavel(req);
        Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

        Ex.getInstance().getBL().transferir(//
                null, // CpOrgao orgaoExterno
                null, // String obsOrgao
                cadastrante, // DpPessoa cadastrante
                lotaTitular, // DpLotacao lotaCadastrante
                mob, // ExMobil mob
                dt, // final Date dtMov
                dt, // Date dtMovIni
                null, // Date dtFimMov
                lot, // DpLotacao lotaResponsavel
                pes, // final DpPessoa responsavel
                null, // Ainda falta implementar a notificação para grupo de email
                null, // DpLotacao lotaDestinoFinal
                null, // DpPessoa destinoFinal
                null, // DpPessoa subscritor
                titular, // DpPessoa titular
                null, // ExTipoDespacho tpDespacho.
                true, // final boolean fInterno
                null, // String descrMov
                null, // String conteudo
                null, // String nmFuncaoSubscritor
                false, // boolean forcarTransferencia
                false, // boolean automatico,
                getTipoMov());

        resp.status = "OK";
    }

    private ExTipoDeMovimentacao getTipoMov() {
        return ExTipoDeMovimentacao.TRAMITE_PARALELO;
    }

    @Override
    public String getContext() {
        return "tramitar em paralelo";
    }

}
