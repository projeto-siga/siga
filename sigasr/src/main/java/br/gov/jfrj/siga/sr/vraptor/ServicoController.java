package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/servico")
public class ServicoController extends SrController {
    private Correio correio;
    public ServicoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator, Correio correio) {
        super(request, result, dao, so, em, srValidator);
        this.correio = correio;
    }
    
    
    @Path("/cadastrar")
    public void cadastrar() {
     
        
        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
        result.include("solicitacao",new SrSolicitacao());
    }

}
