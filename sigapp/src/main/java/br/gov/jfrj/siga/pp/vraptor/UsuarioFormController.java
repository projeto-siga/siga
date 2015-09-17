package br.gov.jfrj.siga.pp.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.pp.models.Foruns;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/usuarioForm")
public class UsuarioFormController extends PpController {

    public UsuarioFormController(HttpServletRequest request, Result result,
            CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }
    
    @Path("/atualiza")
    public void atualiza(String paramCodForum) throws Exception {
        String mensagem = "";
        String matriculaSessao = getUsuarioMatricula();
        String nomeSessao = getCadastrante().getNomeAbreviado();
        UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu =" + matriculaSessao).first();
        if (objUsuario != null) {
            String descricaoForum = "";
            if (paramCodForum != null && !paramCodForum.isEmpty()) {
                Foruns objForum = Foruns.AR.findById(Integer
                        .parseInt(paramCodForum));
                descricaoForum = objForum.getDescricao_forum();
                objUsuario.delete();
                ContextoPersistencia.em().flush();
                objUsuario.setForumFk(objForum);
                objUsuario.setMatricula_usu(matriculaSessao);
                objUsuario.setNome_usu(nomeSessao);
                try {
                    objUsuario.save();
                    ContextoPersistencia.em().flush();
                    mensagem = "Ok.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensagem = "Não Ok.";
                }
            } else {
                paramCodForum = Integer.toString(objUsuario.getForumFk().getCod_forum());
                Foruns objForum = Foruns.AR.find(
                        "cod_forum = " + Integer.parseInt(paramCodForum))
                        .first();
                descricaoForum = objForum.getDescricao_forum();
                ContextoPersistencia.em().flush();
            }
            List<Foruns> outrosForuns = Foruns.AR.find("cod_forum <> " + paramCodForum).fetch();
            result.include("objUsuario", objUsuario);
            result.include("paramCodForum", paramCodForum);
            result.include("descricaoForum", descricaoForum);
            result.include("mensagem", mensagem);
            result.include("outrosForuns", outrosForuns);
        } else {
            redirecionaPaginaErro("Usuario sem permissao." , null);
        }
    }
    

}
