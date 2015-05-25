package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/solicitacao")
public class SolicitacaoController extends SrController {
    private Correio correio;
    private Validator validator;

    public SolicitacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator, Correio correio, Validator validator) {
        super(request, result, dao, so, em, srValidator);
        this.correio = correio;
        this.validator = validator;
    }

    @Path("/teste")
    public void teste(boolean banco) throws Exception {
        SrSolicitacao value = new SrSolicitacao();
        if (banco) {
            value = (SrSolicitacao) SrSolicitacao.AR.all().fetch().get(0);
        }

        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
        result.include("locaisDisponiveis", value.getLocaisDisponiveis());
        result.include("solicitacao", value);
        
        result.include("acao", SrAcao.AR.findById(407L));
    }

    @Path("/testeErro")
    public void testeErro() throws Exception {
        validator.add(new ValidationMessage("Chamas", "nome.teste"));
        validator.add(new ValidationMessage("Eternas", "nome.teste"));
        validator.add(new ValidationMessage("do", "nome.teste"));
        validator.add(new ValidationMessage("Aconchego", "nome.teste"));

        validator.onErrorUsePageOf(this).teste(false);

    }

}
