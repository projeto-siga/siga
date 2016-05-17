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
    public void exclui(String matricula_proibida, String sesb_proibida){
		String mensagem = "";
		// pega usuário do sistema
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao , sesb_pessoaSessao);
		if ((objUsuario !=null) && ( (lotacaoSessao.trim().equals("T2SEADDA")||lotacaoSessao.trim().equals("T2SESIA") || lotacaoSessao.trim().equals("T2SESGET")) )){ //pode excluir a permissão
			List<UsuarioForum> listPermitidos = new ArrayList<UsuarioForum>();
			if((matricula_proibida!=null) && (!matricula_proibida.isEmpty()) && (sesb_proibida!=null) && (!sesb_proibida.isEmpty()) ){ // deleta permissão
				try{
					UsuarioForum.AR.delete("from UsuarioForum where matricula_usu='" + matricula_proibida + "' and sesb_pessoa = '" +  sesb_proibida + "'", null);
					mensagem = "Ok.";
				}catch(Exception e){
					e.printStackTrace();
					mensagem = "N&atilde;o Ok.";
				}finally{
				    result.include("mensagem", mensagem);
				}
			 } else{ // lista permitidos
				try{
					 listPermitidos = (List) UsuarioForum.AR.all().fetch(); // isso não dá erro no caso de retorno vazio.
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
				    result.include("listPermitidos", listPermitidos);
				}
			}
		}else{redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null );}
    }

    @Path("/inclui")
    public void inclui(String matricula_permitida, String sesb_permitida, String nome_permitido, String forum_permitido ) throws Exception {
    	// ALTERAR A PÁGINA DE CADASTRO DE USUÁRIOS PARA INCLUIR SESB_PESSOA_PERMITIDA.
    	// POR ENQUANTO VAMOS ATRIBUIR SESB_PESSOA_PERMITIDA CONFORME A LINHA ABAIXO. MAS, DEVERÁ VIR COMO PARÂMETRO.
    	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		String mensagem = "";
		// pega usuário do sistema
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
		if (objUsuario == null) {
			redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null);
		}
		if (((lotacaoSessao.trim().equals("T2SEADDA") || lotacaoSessao.trim().equals("T2SESIA") || lotacaoSessao.trim().equals("T2SESGET")))){
			// Pode incluir permissão de usuário. Estando lotado na CSIS OU SESIA 
			if((matricula_permitida!=null) && (sesb_permitida!=null) && (nome_permitido!=null) && (forum_permitido!=null) && (!matricula_permitida.isEmpty()) && (!sesb_permitida.isEmpty()) && (!nome_permitido.isEmpty()) && (!forum_permitido.isEmpty())){
				// Estando os parâmetros presentes. Prossegue.
				Foruns atribForum = Foruns.findByCodigo(forum_permitido);
				//busca na base de dados o forum que veio pelo parâmetro.
				UsuarioForum usuarioPermitido = new UsuarioForum(matricula_permitida, sesb_permitida, nome_permitido, atribForum);
				try {
				    if(objUsuario.equals(usuarioPermitido)) throw new Exception("org.hibernate.exception.ConstraintViolationException");
					ContextoPersistencia.em().persist(usuarioPermitido);
					ContextoPersistencia.em().flush();
					mensagem = "Ok.";
				} catch (Exception e) {
				    e.printStackTrace();
				    if (e.getMessage().contains("a different object with the same identifier value was already associated with the session") || e.getMessage().contains("Could not execute JDBC batch update")
				            || e.getMessage().contains("org.hibernate.exception.ConstraintViolationException")){
						mensagem="Usuario j&aacute; tinha permiss&atilde;o.";
					}else{
						mensagem = "N&atilde;o Ok.";
					}
				}finally{
				    result.include("mensagem", mensagem);
				}
			}else{
				mensagem="";
				result.include("mensagem", mensagem);
			}
		}else{
		    redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null );
		}
    }
}
