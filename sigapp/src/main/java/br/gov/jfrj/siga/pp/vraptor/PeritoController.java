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
        String matriculaSessao = getUsuarioMatricula();
        
        UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu = '"+matriculaSessao+"'").first();
        
        if(objUsuario == null)
            redirecionaPaginaErro("Usuario sem permissao", null);
    }
    
    public void insert(String cpf_perito, String nome_perito){
        String resposta="";
        try {
            Peritos objPerito = new Peritos(cpf_perito, nome_perito);
            objPerito.save();
            ContextoPersistencia.em().flush();
            resposta="ok";
        } catch(PersistenceException e) {
            e.printStackTrace();
            resposta="Nao ok.";
            if (e.getMessage().substring(23, 53).equals(".ConstraintViolationException:")) 
                resposta = "Verifique se o CPF do perito esta correto, ou, se o perito ja esta cadastrado. ";
        } finally {
            result.include("resposta", resposta);
        }
    }

}
