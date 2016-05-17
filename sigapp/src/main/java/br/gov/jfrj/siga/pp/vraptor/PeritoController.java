package br.gov.jfrj.siga.pp.vraptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.pp.models.Peritos;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/perito")
public class PeritoController extends PpController{

    public PeritoController(HttpServletRequest request, Result result,
            CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }
    
    @Path("/incluir")
    public void incluir() {
        String matriculaSessao = getCadastrante().getMatricula().toString();
        String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao , sesb_pessoaSessao);
        if(objUsuario == null)
            redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o", null);
    }
    
    public void insert(String cpf_perito, String nome_perito){
        String resposta="";
        try {
            Peritos objPerito = new Peritos(cpf_perito, nome_perito);
            objPerito.save();
            ContextoPersistencia.em().flush();
            resposta="Ok.";
        } catch(PersistenceException e) {
            e.printStackTrace();
            resposta="N&atilde;o ok.";
            if (e.getMessage().substring(23, 53).equals(".ConstraintViolationException:")) 
                resposta = "Verifique se o CPF do perito est&aacute; correto, ou, se o perito j&aacute; est&aacute; cadastrado. ";
        } finally {
            result.include("resposta", resposta);
        }
    }
}
