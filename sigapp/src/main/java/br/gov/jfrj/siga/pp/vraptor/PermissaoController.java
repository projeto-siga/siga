package br.gov.jfrj.siga.pp.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.util.Contracts;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.models.Foruns;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/permissao")
public class PermissaoController extends PpController {

    public PermissaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
    }

    @Path("/exclui")
    public void exclui(String matricula_proibida){
		String mensagem = "";
		// pega usuário do sistema
		String matriculaSessao = getUsuarioMatricula();
		String lotacaoSessao = getUsuarioLotacao();
		UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null) && ( (lotacaoSessao.trim().equals("CSIS")||lotacaoSessao.trim().equals("SESIA")) )){ //pode excluir a permissão
			List<UsuarioForum> listPermitidos = new ArrayList<UsuarioForum>();
			if((matricula_proibida!=null) && (!matricula_proibida.isEmpty())){ // deleta permissao
				try{
					UsuarioForum objUsuarioProibido = UsuarioForum.AR.find("matricula_usu='"+matricula_proibida+"'").first();
					objUsuarioProibido.delete();
					ContextoPersistencia.em().flush();
					mensagem = "Ok";
				}catch(Exception e){
					e.printStackTrace();
					mensagem = "Nao Ok";
				}finally{
				    result.include("mensagem", mensagem);
				}
			 } else{ // lista permitidos
				try{
					 listPermitidos = (List) UsuarioForum.AR.find(" order by nome_usu ").fetch(); // isso não dá erro no caso de retorno vazio.
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
				    result.include("listPermitidos", listPermitidos);
				}
			}
		}
    }

    @Path("/inclui")
    public void inclui(String matricula_permitida, String nome_permitido, String forum_permitido ) throws Exception {
		String mensagem = "";
		// pega usuário do sistema
		String matriculaSessao = getUsuarioMatricula();
		// String nomeSessao = cadastrante().getNomeAbreviado();
		String lotacaoSessao = getUsuarioLotacao();
		UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu = '"+matriculaSessao+"'").first();
		
		if ((objUsuario !=null) && ((lotacaoSessao.trim().equals("CSIS") || lotacaoSessao.trim().equals("SESIA")))){
			if((matricula_permitida!=null) && (nome_permitido!=null) && (forum_permitido!=null) && (!matricula_permitida.isEmpty()) && (!nome_permitido.isEmpty()) && (!forum_permitido.isEmpty())){
				Foruns atribForum = (Foruns) Foruns.AR.find("cod_forum='"+forum_permitido+"'").first();
				UsuarioForum usuarioPermitido = new UsuarioForum(matricula_permitida, nome_permitido, atribForum);
				try {
				    if(objUsuario.equals(usuarioPermitido))
				        throw new Exception("org.hibernate.exception.ConstraintViolationException");
				    
					ContextoPersistencia.em().persist(usuarioPermitido);
					ContextoPersistencia.em().flush();
					mensagem = "Ok.";
				} catch (Exception e) {
				    e.printStackTrace();
				    if (e.getMessage().contains("a different object with the same identifier value was already associated with the session") || e.getMessage().contains("Could not execute JDBC batch update")
				            || e.getMessage().contains("org.hibernate.exception.ConstraintViolationException")){
						mensagem="Usuario ja tinha permissao.";
					}else{
						mensagem = "Nao Ok.";
					}
				}finally{
				    result.include("mensagem", mensagem);
				}
			}else{
				mensagem="";
				result.include("mensagem", mensagem);
			}
		}else{
		    redirecionaPaginaErro("Usuario sem permissao." , null );
		}
    }
}
