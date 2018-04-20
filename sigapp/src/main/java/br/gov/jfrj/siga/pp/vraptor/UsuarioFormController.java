package br.gov.jfrj.siga.pp.vraptor;

import java.util.ArrayList;
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
    	/** Item de menu "configuraÃ§Ã£o"
    	    Usado para trocar para agenda de outro forum
    	*  */
        String mensagem = "";
        String matriculaSessao = getCadastrante().getMatricula().toString();
        String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        String nomeSessao = getCadastrante().getNomeAbreviado();
        String lotacaoSessao = getLotaTitular().getSiglaCompleta();   // O final da substring -SG Ã© de SÃ£o Goncalo . Tribunal tem o seguinte formato: T2COSIGP
        lotacaoSessao = lotacaoSessao.substring(lotacaoSessao.length()-3, lotacaoSessao.length());
        //java.lang.System.out.println("- >>>> LOTACAO: "+lotacaoSessao.substring(lotacaoSessao.length()-3, lotacaoSessao.length()));
        UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
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
                objUsuario.setSesb_pessoa(sesb_pessoaSessao);
                objUsuario.setNome_usu(nomeSessao);
                try {
                    objUsuario.save();
                    ContextoPersistencia.em().flush();
                    mensagem = "Ok.";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensagem = "N&atilde;o Ok.";
                }
            } else {
                paramCodForum = Integer.toString(objUsuario.getForumFk().getCod_forum());
                Foruns objForum = Foruns.AR.find(
                        "cod_forum = " + Integer.parseInt(paramCodForum))
                        .first();
                descricaoForum = objForum.getDescricao_forum();
                ContextoPersistencia.em().flush();
            }
            ArrayList<Foruns> outrosForuns = (ArrayList) Foruns.AR.find("cod_forum <> " + paramCodForum).fetch();
            if(!lotacaoSessao.equals("-NI")){ // entra no if caso não seja lotado em Niterói
             for (byte i = 0;i<outrosForuns.size();i++) { // varre o arraylist
				if (outrosForuns.get(i).getCod_forum()==2){ // dois é o código de niterói.
					outrosForuns.remove(i); // remove niteroi da lista
					// System.out.println("================> Retirou o código dois da lista.");

				}
			 }
            }
            result.include("objUsuario", objUsuario);
            result.include("paramCodForum", paramCodForum);
            result.include("descricaoForum", descricaoForum);
            result.include("mensagem", mensagem);
            result.include("outrosForuns", outrosForuns);
        } else {
            redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null);
        }
    }
}
