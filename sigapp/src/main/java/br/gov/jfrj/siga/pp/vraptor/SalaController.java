package br.gov.jfrj.siga.pp.vraptor;

import java.util.ArrayList;
import java.util.Iterator;
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
import br.gov.jfrj.siga.pp.models.Locais;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/sala")
public class SalaController extends PpController {

    public SalaController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }

    @Path("/incluir")
    public void incluir() {
		// pega usuario do sistema
		String matriculaSessao = getUsuarioMatricula();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao);
		if (null == objUsuario) {
			redirecionaPaginaErro("Usuario sem permissao" , null);
		}
	}

    @Path("/insert")
    public void insert(Locais formLocal, int cod_forum) {
    	Foruns objForum = new Foruns(cod_forum, " ", " ");
    	formLocal.setForumFk(objForum);
    	String varCodLocal = formLocal.getCod_local();
    	String resposta = "";
    	try {
    	    ContextoPersistencia.em().detach(formLocal);
    		System.out.println(ContextoPersistencia.em().createQuery("from Locais where cod_local ='" + varCodLocal+ "'").getSingleResult());
    		resposta = "Sala ja existe. Confira o codigo da sala. ";

    	} catch (Exception e) {
    		try {
    			System.out.println(ContextoPersistencia.em().createQuery("from Foruns where cod_forum=" + cod_forum));
    			formLocal.save();
    			ContextoPersistencia.em().flush();
    			resposta = "Ok.";
    		} catch (Exception e2) {
    			resposta = "Forum da sala nao cadastrado. Confira o codigo do forum.";
    		}
    	}
    	result.include("resposta", resposta);
    }

    @Path("/listar")
    public void listar(String cod_local, String sala,
			String desc_forum) {
		// pega usuario do sistema
		String matriculaSessao = getUsuarioMatricula();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao);
		if (objUsuario != null) {
			List<Locais> listLocais = new ArrayList<Locais>();
			if (desc_forum == null) {
				desc_forum = "";
			}
			if (cod_local == null) {
				cod_local = "";
			}
			if (sala == null) {
				sala = "";
			}
			if (desc_forum.isEmpty() && cod_local.isEmpty() && sala.isEmpty()) {
				listLocais = Locais.AR.findAll();
				result.include("listLocais", listLocais);
			} else if (!cod_local.isEmpty()) {
				try {
			        Locais auxLocal = Locais.AR.findById(cod_local);
					if (auxLocal != null)
						listLocais.add((Locais) auxLocal);
				} finally {
				    result.include("listLocais", listLocais);
				}
 			} else if (!sala.isEmpty()) {
				try {
					listLocais = ContextoPersistencia.em().createQuery(	"from Locais where local like '" + sala	+ "%'").getResultList();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				    result.include("listLocais", listLocais);
				}
			} else if (!desc_forum.isEmpty()) {
				try {
					List<Foruns> listForuns = new ArrayList<Foruns>();
					Foruns objForum = new Foruns(0, "", "");
					List<Locais> listLocaisAux = new ArrayList<Locais>();
					listForuns = ContextoPersistencia
							.em()
							.createQuery(
									"from Foruns where descricao_forum like'"
											+ desc_forum + "%'")
							.getResultList();
					for (Iterator iterator01 = listForuns.iterator(); iterator01
							.hasNext();) {
						objForum = (Foruns) iterator01.next();
						listLocaisAux = ContextoPersistencia
								.em()
								.createQuery(
										"from Locais where forumFk ="
												+ objForum.getCod_forum())
								.getResultList();
						for (Iterator iterator02 = listLocaisAux.iterator(); iterator02
								.hasNext();) {
							listLocais.add((Locais) iterator02.next());
							System.out.println("- Detalhe -");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					result.include("listLocais", listLocais);
				}
			}
		} else {
		    redirecionaPaginaErro("Usuario sem permissao", null);
		}
	}

    @Path("/delete")
    public void delete(String cod_sala) {
		String resposta = " ";
		if (!cod_sala.isEmpty()) {
			try {
				resposta = "Ok.";
				Locais.AR.delete("from Locais where cod_local='" + cod_sala + "'", null);
			} catch (Exception e) {
				e.printStackTrace();
				resposta = "Esta sala possui agendamentos. Delete primeiro os agendamentos referenciados.";
			}
		} else {
			resposta = "Não Ok.";
		}
		result.include("resposta", resposta);
	}


}
