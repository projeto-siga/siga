package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;
import java.util.Map;

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
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
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
    public void teste(boolean banco) {
        SrSolicitacao solicitacao = new SrSolicitacao();
        if (banco) {
            solicitacao = (SrSolicitacao) SrSolicitacao.AR.all().fetch().get(0);
        }

        result.include("meiosComunicadaoList", SrMeioComunicacao.values());
        result.include("locaisDisponiveis", solicitacao.getLocaisDisponiveis());
        result.include("solicitacao", solicitacao);
        result.include("itemConfiguracao",solicitacao.getItemConfiguracao());
    }

    @Path("/testeErro")
    public void testeErro() {
        validator.add(new ValidationMessage("Chamas", "nome.teste"));
        validator.add(new ValidationMessage("Eternas", "nome.teste"));
        validator.add(new ValidationMessage("do", "nome.teste"));
        validator.add(new ValidationMessage("Aconchego", "nome.teste"));

        validator.onErrorUsePageOf(this).teste(false);

    }
    
    @Path("/exibirAcao")
    public void exibirAcao(SrSolicitacao solicitacao) throws Exception {
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        result.include("solicitacao",solicitacao);
        result.include("acoesEAtendentes",acoesEAtendentes);

    }

}

