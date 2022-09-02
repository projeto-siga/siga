package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import java.util.Set;

@AcessoPublico
public class DocumentosIdVerificarAssinaturaGet implements IExApiV1.IDocumentosIdVerificarAssinaturaGet {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();

        if(req.id != null &&! req.id.isEmpty())
            filter.setIdDoc(Long.valueOf(req.id));

        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);
        if (mob == null)
            throw new AplicacaoException(
                    "Não foi possível encontrar o documento " + req.id);

        ExTipoDeMovimentacao exTipoDeAssinatura = req.assinaturaDigital ?
                ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO :
                ExTipoDeMovimentacao.ASSINATURA_COM_SENHA;

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(exTipoDeAssinatura);
        
        resp.idDoc = req.id;
        
        try {
            for (ExMovimentacao mov : movs) {
                if (mov.assertAssinaturaValida() != null && mov.assertAssinaturaValida().contains("OK")) {
                    resp.idMov = mov.getIdMov().toString();
                    resp.status = "Ok";
                    return;
                }
            }    
        } catch (final Exception e) {
            resp.status = "ERRO!";    
        }
        
    }


    @Override
    public String getContext() {
        return "verificar assinatura";
    }
}
