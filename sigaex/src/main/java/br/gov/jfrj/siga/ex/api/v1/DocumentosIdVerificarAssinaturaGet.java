package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import com.crivano.swaggerservlet.SwaggerServlet;

import java.util.Set;

@AcessoPublico
public class DocumentosIdVerificarAssinaturaGet implements IExApiV1.IDocumentosIdVerificarAssinaturaGet {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        // Verifica o acesso
        {
            String pwd = Prop.get("/sigaex.auditoria.assinaturas.password");
            if (pwd == null)
                throw new RuntimeException("Serviço deve ser ativado informando a propriedade sigaex.auditoria.assinaturas.password");
            String auth = SwaggerServlet.getHttpServletRequest().getHeader("Authorization");
            if (!pwd.equals(auth))
                throw new RuntimeException(
                        "Propriedade sigaex.auditoria.assinaturas.password não confere com o valor recebido no cabeçalho Authorization");
        }

        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setIdDoc(Long.valueOf(req.id));

        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);
        if (mob == null)
            throw new AplicacaoException(
                    "Não foi possível encontrar o documento " + req.id);
        
        ExTipoDeMovimentacao exTipoDeAssinatura = (req.assinaturaDigital != null && req.assinaturaDigital) ?
                ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO :
                ExTipoDeMovimentacao.ASSINATURA_COM_SENHA;

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(exTipoDeAssinatura);
        if(movs.isEmpty()){
            throw new AplicacaoException(
                    req.id + " não possui movimentação não cancelada de assinatura, " +
                            "então não é possível verificar assinatura");
        }

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
