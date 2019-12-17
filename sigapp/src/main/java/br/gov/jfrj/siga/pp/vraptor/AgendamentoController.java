package br.gov.jfrj.siga.pp.vraptor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.pp.models.Agendamentos;
import br.gov.jfrj.siga.pp.models.Locais;
import br.gov.jfrj.siga.pp.models.Peritos;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/agendamento")
public class AgendamentoController extends PpController {

    public AgendamentoController (HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }

    @Path("/hoje")
    public void hoje(String selFiltraSala) { 
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
		if (objUsuario != null) {
			// busca locais em funcao do forum do usuario
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
			// Usa COD_FORUM para buscar e montar string de criterio para SELECT ... IN (criterioSalas)
			// 	ONDE criterioSalas = "MF1, MF2, MF3"
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			// System.out.println(selFiltraSala);
			if(selFiltraSala!=null && (!selFiltraSala.equals("")) ){
				criterioSalas = "'"+selFiltraSala+"'";
			}
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			// pega a data de hoje
			Date hoje = new Date();
			String dtt = df.format(hoje);
			// Em caso de nao haver salas em criterioSalas atribui-se '' (string vazia)
            if (criterioSalas.equals("")) {
                criterioSalas = "''";
            }
            // busca todos os agendamentos de hoje em criterioSalas (salas do forum do usuario). 
            List<Agendamentos> listAgendamentos = Agendamentos.AR.find(
                    "data_ag = to_date('" + dtt
                            + "','dd-mm-yy') and localFk in(" + criterioSalas
                            + ") order by hora_ag, cod_local").fetch();
            // Se houver agendamentos em listAgendamentos
            if (!listAgendamentos.isEmpty()) {
                // repete a busca das salas usando COD_FORUM
                List<Locais> listLocais = Locais.AR.find("cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'").fetch();
                // lista auxiliar para filtrar os agendamentos  (ja buscou todos)
                List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
                // varre listAgendamentos
                for (int i = 0; i < listAgendamentos.size(); i++) {
                	// filtra listAgendamentos deixando de fora os locais que nao sao do forum do usuario ?
                    for (int ii = 0; ii < listLocais.size(); ii++) {
                        if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais.get(ii).getCod_local()) {
                            auxAgendamentos.add((Agendamentos) listAgendamentos.get(i));
                        }
                    }
                }
                List<Peritos> listPeritos = new ArrayList<Peritos>();
                //Busca todos os peritos
                listPeritos = (ArrayList<Peritos>) Peritos.AR.findAll();
                // renderiza as listas
                result.include("listAgendamentos", listAgendamentos);
                result.include("listPeritos", listPeritos);
                result.include("dataHoje", dtt);
                result.include("listLocais", listaDeSalas);
            }
		} else {
		    redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null);
		}
    }

    @Path("/hojePrint")
    public void hojePrint(String frm_data_ag) {
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula( matriculaSessao , sesb_pessoaSessao);
		if (objUsuario != null) {
			// busca locais em funcao do forum do usuario.
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
			// monta string de criterio para a clausula WHERE ... IN (criterio).
			for(int j=0; j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			if ((null != frm_data_ag) && !(frm_data_ag.isEmpty())){
				// Busca os agendamentos da data do parametro, e, das salas do forum do usuario.
				List<Agendamentos> listAgendamentos = (List) Agendamentos.AR.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag , localFk" ).fetch();
				// Busca todos os peritos.
				List<Peritos> listPeritos = (List) new ArrayList<Peritos>();
				listPeritos = Peritos.AR.findAll();
				// Passa parametros para a pagina .JSP
				result.include("listAgendamentos", listAgendamentos);
				result.include("listPeritos", listPeritos);
			}
		} else {
		    redirecionaPaginaErro("Usu&aacute;ario sem permiss&atilde;o." , null);
		}
    }
    
    @Path("/amanha")
    public void amanha(String selFiltraSala){
    	String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
		if (objUsuario != null) {
			// busca locais em funcao do forum do usuario
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
			// Usa COD_FORUM para buscar e montar string de criterio para SELECT ... IN (criterioSalas)
			// 	ONDE criterioSalas = "MF1, MF2, MF3"
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			// Em caso de nao haver salas em criterioSalas atribui-se '' (string vazia)
            if (criterioSalas.equals("")) {
                criterioSalas = "''";
            }
            if(selFiltraSala!=null && (!selFiltraSala.equals("")) ){
				criterioSalas = "'"+selFiltraSala+"'";
			}
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			// pega a data de hoje
			Date hoje = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(hoje);
			// determina a data de amanha
			c.add(Calendar.DATE, 1);
			int num_sem = c.get(Calendar.DAY_OF_WEEK);
			String dtt = df.format(c.getTime());
			String dia_sem = "";
			switch(num_sem){
			case 1:
				dia_sem = "Domingo";
				break;
			case 2:
				dia_sem = "Segunda";
				break;
			case 3:
				dia_sem = "Ter&ccedil;a";
				break;
			case 4: 
				dia_sem = "Quarta";
				break;
			case 5:
				dia_sem = "Quinta";
				break;
			case 6:
				dia_sem = "Sexta";
				break;
			case 7:
				dia_sem = "S&aacute;bado";
				break;
			}
			// busca todos os agendamentos de amanha em criterioSalas (so das salas do forum do usuario). 
            List<Agendamentos> listAgendamentos = Agendamentos.AR.find(
                    "data_ag = to_date('" + dtt
                            + "','dd-mm-yy') and localFk in(" + criterioSalas
                            + ") order by hora_ag, cod_local").fetch();
            // Se houver agendamentos em listAgendamentos
            if (!listAgendamentos.isEmpty()) {
            	//Busca todos os peritos
            	List<Peritos> listPeritos = new ArrayList<Peritos>();
                listPeritos = (ArrayList<Peritos>) Peritos.AR.findAll();
                // renderiza as listas
                result.include("listAgendamentos", listAgendamentos);
                result.include("listPeritos", listPeritos);
            	
            }
			result.include("dataAmanha", dtt);
			result.include("diaSemana", dia_sem);
			result.include("listLocais", listaDeSalas);
			} else {
			redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null);
		}
    }
    
    @Path("/amanhaPrint")
    public void amanhaPrint(String frm_data_ag){
    	String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
		if (objUsuario != null) {
			if (!frm_data_ag.isEmpty() && (frm_data_ag != null)){
				// busca locais em funcao do forum do usuario.
				String criterioSalas="";
				List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
				// monta string de criterio para a clausula WHERE ... IN (criterio).
				for(int j=0; j<listaDeSalas.size();j++){
					criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
					if(j+1<listaDeSalas.size()){
						criterioSalas = criterioSalas + ",";
					}
				}
				// Busca os agendamentos da data do parametro, e, das salas do forum do usuario.
				List<Agendamentos> listAgendamentos = (List) Agendamentos.AR.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag , localFk" ).fetch();
				// Busca todos os peritos.
				List<Peritos> listPeritos = (List) new ArrayList<Peritos>();
				listPeritos = Peritos.AR.findAll();
				// passa os objetos para a pagina .JSP
				result.include("listAgendamentos", listAgendamentos);
				result.include("listPeritos", listPeritos);
			}else{
				redirecionaPaginaErro("Parametro data vazio." , null);
			}
			
		} else {
			redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o." , null);
		}
    }
    
    @Path("/print")
    public void print(String frm_data_ag, String frm_sala_ag, String frm_processo_ag, String frm_periciado ){
		if(frm_periciado == "" || frm_periciado == null){
		    redirecionaPaginaErro("Relat&oacute;rio depende de nome de periciado preenchido para ser impresso." , null);
		    return;
		}else if(frm_processo_ag  == "" || frm_processo_ag == null){
		    redirecionaPaginaErro("Relat&oacute;rio depende de n&uacute;mero de processo preenchido para ser impresso." , null);
		    return;
		}else{
		    List listAgendamentos = (List) Agendamentos.AR.find(
		            "data_ag=to_date('"+frm_data_ag.substring(0,10)+"','yy-mm-dd') and localFk.cod_local='"+frm_sala_ag+"' and processo='"+frm_processo_ag+"' and periciado='"+frm_periciado+"'" ).fetch();
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.AR.findAll();
			result.include("frm_processo_ag", frm_processo_ag);
            result.include("listAgendamentos", listAgendamentos);
            result.include("listPeritos", listPeritos);
		}
    }

    @Path("/salaLista")
    public void salaLista(String frm_cod_local, String frm_data_ag){
		String local = "";
		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
		List<Locais> listSalas = new ArrayList();
		// pega usuario do sistema
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
		if (objUsuario != null) {
			// Pega o usuario do sistema, e, filtra os locais(salas) daquele forum onde ele esta.
			listSalas = ((List) Locais.AR.find("forumFk='" + objUsuario.getForumFk().getCod_forum() + "' order by ordem_apresentacao ").fetch()); // isso nÃ£o dÃ¡ erro no caso de retorno vazio.
			List<Agendamentos> listAgendamentosMeusSala = new ArrayList();
			if(!(frm_cod_local==null||frm_data_ag.isEmpty())){
				//lista os agendamentos do dia, e, da lotacao do cadastrante
				listAgendamentosMeusSala = ((List) Agendamentos.AR.find("localFk.cod_local='" + frm_cod_local + "' and data_ag = to_date('" + frm_data_ag + "','yy-mm-dd') order by hora_ag").fetch());
				for(int i=0;i<listSalas.size();i++){
					if(listSalas.get(i).getCod_local().equals(frm_cod_local)){
						local = listSalas.get(i).getLocal();
					}
				}

			}
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.AR.findAll();
            result.include("local", local);
            result.include("listSalas", listSalas);
            result.include("listAgendamentosMeusSala", listAgendamentosMeusSala);
            result.include("lotacaoSessao", lotacaoSessao);
            result.include("listPeritos", listPeritos);
		} else {
		    redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o" , null);
		}
    }

    public void insert(String frm_data_ag,
    			String frm_hora_ag, String frm_cod_local, 
    			String periciado, String perito_juizo, String perito_parte,
    			String orgao, String processo, Integer lote) {
    	String matricula = getCadastrante().getMatricula().toString();
		String sesb_pessoa = getCadastrante().getSesbPessoa().toString();
		String resposta = "";
		Locais auxLocal = Locais.AR.findById(frm_cod_local);
		String hr;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parametro = df.parse(frm_data_ag);
			Agendamentos objAgendamento = new Agendamentos(parametro,
					frm_hora_ag, auxLocal, matricula, sesb_pessoa,  periciado, perito_juizo,
					perito_parte, processo, orgao);
			hr = frm_hora_ag;
			Agendamentos agendamentoEmConflito = null;
			// begin transaction, que, segundo o Da Rocha eh automatico no inicio da action. De fato.
			String hrAux = hr.substring(0, 2);
			String minAux = hr.substring(3, 5);
			if (hr != null && (!hr.isEmpty())) {
				//verifica se tem conflito
				String horaPretendida = null;
				for(int i = 0; i < lote; i++){
					horaPretendida=hrAux+minAux;
					agendamentoEmConflito = Agendamentos.AR.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +horaPretendida+ "' and data_ag=to_date('"+ frm_data_ag +"' , 'yy-mm-dd')"  ).first();
					if (agendamentoEmConflito!=null){
					    redirecionaPaginaErro("Perito nao disponivel no horario de " + agendamentoEmConflito.getHora_ag().substring(0,2)+ "h" + agendamentoEmConflito.getHora_ag().substring(2,4) + "min" , null );
					    return;
					}
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.getIntervalo_atendimento());
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
				}
				// sloop
				hrAux = hr.substring(0, 2);
				minAux = hr.substring(3, 5);
			    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
				EntityManager em = emf.createEntityManager();
				  
				em.getTransaction().begin();
				// persiste o lote de horários 
				for (int i = 0; i < lote; i++) {
					//System.out.println("Hora a persistir: " + hrAux);
					if(hrAux.trim().length() < 2){
						//acerta o tamanho do conteúdo da hora de hrAux
						hrAux="0"+hrAux; 
					}
					objAgendamento.setHora_ag(hrAux + minAux);
					em.persist(objAgendamento);
					em.flush();
		            em.clear();
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.getIntervalo_atendimento());
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
					resposta = "Ok.";
				}
				em.getTransaction().commit();
			    em.close();
				// floop
				// end transaction, que, segundo o Da Rocha eh automatico; no fim
				// da action
			} else {
				resposta = "N&atilde;o Ok.";
			}
		} catch(Exception e) {
			// rollback transaction, que segundo o Da Rocha eh automatico; ocorre em qualquer erro
			//e.printStackTrace();
			String erro = e.getMessage();
			System.out.println("Mensagem de erro: "+erro);
			if (erro.substring(24, 52).equals("ConstraintViolationException")) {
				resposta = "N&atilde;o Ok. O lote n&atilde;o foi agendado.";
			} else {
				resposta = "N&atilde;o Ok. Verifique se preencheu todos os campos do agendamento."+erro;
			}
		} finally {
		    result.include("resposta", resposta);
		    result.include("perito_juizo", perito_juizo);
		}
    }

    @Path("/incluirAjax")
    public void incluirAjax(String fixo_perito_juizo) {
        String fixo_perito_juizo_nome = "";
        String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
        List<Locais> listSalas = new ArrayList<Locais>();
        List<Peritos> listPeritos = new ArrayList<Peritos>();
        // pega dados do usuario logado
        String matriculaSessao = getCadastrante().getMatricula().toString();
        String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
        if (objUsuario != null) {
            // Pega o usuario do sistema, e, filtra os locais(salas) daquele forum onde ele esta.
            listSalas = (List) Locais.AR.find(
                    "cod_forum='" + objUsuario.getForumFk().getCod_forum()
                            + "' order by ordem_apresentacao ").fetch(); // isso nao da erro no caso de retorno vazio.
            listPeritos =  (List) Peritos.AR.find("1=1 order by nome_perito").fetch();
            //   buscar o nome do perito fixo na lista se existir
            if(fixo_perito_juizo!=null){
                for(int i=0;i<listPeritos.size();i++) { 
                    if(listPeritos.get(i).getCpf_perito().trim().equals( fixo_perito_juizo.trim() ) ){
                        fixo_perito_juizo_nome = listPeritos.get(i).getNome_perito();
                    }
                }
            }
            result.include("listSalas", listSalas);
            result.include("lotacaoSessao", lotacaoSessao);
            result.include("fixo_perito_juizo", fixo_perito_juizo);
            result.include("fixo_perito_juizo_nome", fixo_perito_juizo_nome);
            result.include("listPeritos", listPeritos);
        } else {
            redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o", null);
        }
    }
    @Path("/update")
    public void update(String cod_sala, String data_ag, String hora_ag, String processo, String periciado, String perito_juizo, String perito_parte, String orgao_ag){
        processo = verificaCampoEInicializaCasoNull(processo);
        periciado = verificaCampoEInicializaCasoNull(periciado);
        perito_parte = verificaCampoEInicializaCasoNull(perito_parte);
        
        String resultado = "";
		String dtt = data_ag.replaceAll("/", "-");
		Agendamentos agendamentoEmConflito = null;
		try{
			// Devo verificar agendamento conflitante, antes de fazer o UPDATE.
			System.out.println(perito_juizo.trim()+""+dtt+" "+hora_ag.substring(0,2)+hora_ag.substring(3,5));
			agendamentoEmConflito = Agendamentos.AR.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +hora_ag.substring(0,2)+hora_ag.substring(3,5)+ "' and data_ag=to_date('"+ dtt +"', 'dd-mm-yy' ) and localFk<>'"+cod_sala+"'").first();
			if (agendamentoEmConflito!=null) {
				redirecionaPaginaErro("Perito nao disponivel no horario de " + agendamentoEmConflito.getHora_ag().substring(0,2) +"h"+agendamentoEmConflito.getHora_ag().substring(2,4)+"min" , " excluir?data="+dtt);
				return;
			}
		        
			ContextoPersistencia.em().createQuery("update Agendamentos set processo = '"+ processo +"', "+ "periciado='"+ periciado +"', perito_juizo='"+ perito_juizo.trim() +"', perito_parte='"+perito_parte+"', orgao='"+orgao_ag+"' where cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+dtt+"','dd-mm-yy')").executeUpdate();
			ContextoPersistencia.em().flush();
			Agendamentos objAgendamento = (Agendamentos) Agendamentos.AR.find("cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+dtt+"','dd-mm-yy')" ).first();
			if(objAgendamento==null){
				resultado = "N&atilde;o Ok.";
			}else{
				resultado = "Ok.";
			}
		}catch(PersistenceException e){
			e.printStackTrace();
			resultado = "N&atilde;o Ok.";
		}catch(Exception ee){
			ee.printStackTrace();
			resultado = "N&atilde;o Ok.";
		}finally{
		    result.include("resultado", resultado);
		    result.include("data_ag", dtt);
		}
    }

    @Path("/imprime")
    public void imprime(String frm_data_ag){
		String matriculaSessao = getCadastrante().getMatricula().toString();
		String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
		List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
		UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao,sesb_pessoaSessao);
    	if (objUsuario != null) {
    		if(frm_data_ag==null)
    			frm_data_ag = "";
    		else{
    			listAgendamentos = Agendamentos.AR.find( "data_ag=to_date('" + frm_data_ag + "','dd-mm-yy') order by hora_ag, cod_local" ).fetch();
    			DpPessoa p = null;
    			// exclui da lista os agendamentos de outros orgaos
    			for(int i=0;i<listAgendamentos.size();i++){
    				if(!lotacaoSessao.trim().equals(listAgendamentos.get(i).getOrgao())){
     					listAgendamentos.remove(i);
    					i--;
    				}
    			}
    		}
    		List<Peritos> listPeritos = new ArrayList<Peritos>();
    		listPeritos = Peritos.AR.findAll();
            result.include("listAgendamentos", listAgendamentos);
            result.include("listPeritos", listPeritos);
    	} else
    	    redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o" , null);
    	
   	}

    public void delete(String data_ag, String hora_ag, String cod_local) {
    	String resultado = "";
    	// na sessao
		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
    	try {
    		Agendamentos ag = Agendamentos.AR.find(
    				"hora_ag = '" + hora_ag
    						+ "' and localFk.cod_local='" + cod_local
    						+ "' and data_ag = to_date('" + data_ag
    						+ "','dd/mm/yy')").first();
    		// na agenda
    		String lotacao_ag = ag.getOrgao();
    		//----------- deletar isso depois dos testes. ---------------
    		// DpPessoa p = (DpPessoa) DpPessoa.AR.find("orgaoUsuario.idOrgaoUsu = " + getUsuarioIdOrgaoUsu()	+ " and dataFimPessoa is null and matricula='" + matricula_ag + "' and sesb_pessoa='"+sesb_ag+"'").first();
    		// String lotacao_ag = p.getLotacao().getSiglaCompleta();
    		// System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
    		//-----------------------------------------------------------
    		if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
    			ag.delete();
    			ContextoPersistencia.em().flush();
    			resultado = "Ok.";
    		}else{
    		    redirecionaPaginaErro("Esse agendamento n&atilde;o pode ser deletado; pertence a outra vara." , null);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		resultado = "N&atilde;o Ok.";
    	} finally {
    	    String dtt = data_ag.replace("/", "-");
    	    result.include("resultado", resultado);
    	    result.include("dtt", dtt);
    	}
    }

    @Path("/excluir")
    public void excluir(String data, String filtra_forum) {
        // pega matricula do usuario do sistema
        String matriculaSessao = getCadastrante().getMatricula().toString();
        String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        String lotacaoSessao = getLotaTitular().getLotacaoAtual().getSiglaCompleta();
        // busca a permissao do usuario
        UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
        // verifica se existe permissao
        if (objUsuario != null) {
            List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
            // verifica se o formulario submeteu alguma data
            if (data != null) {
                // Busca os agendamentos da data do formulario
            	// System.out.println("-------- filtro --------> "+ filtra_forum);
            	if(filtra_forum!=null && filtra_forum.equals("todos")){   // lista todos os agendamentos
            		listAgendamentos = Agendamentos.AR.find("data_ag = to_date('" + data + "','dd-mm-yy') order by hora_ag , cod_local").fetch();
            	}else if(filtra_forum != null && (!filtra_forum.equals(""))){   // lista somente os meus agendamentos; filtro
            		listAgendamentos = Agendamentos.AR.find("data_ag = to_date('"+ data +"','dd-mm-yy') and orgao = '" + filtra_forum + "' order by hora_ag, cod_local").fetch();
            	}
                // filtra os locais do forum do usuario
                List<Locais> listLocais = Locais.AR.find("cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'").fetch();
                // Verifica se existe local (sala) naquele forum do usuario
                if (!listAgendamentos.isEmpty()) {
                    // para cada agendamento, inlcui na lista a sala que eh do
                    // forum daquele usuario
                    List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
                    for (Integer i = 0; i < listAgendamentos.size(); i++) {
                        // pega o agendamento
                        for (Integer ii = 0; ii < listLocais.size(); ii++) {
                            // varre os locais do forum
                            if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais.get(ii).getCod_local()) {
                                // pertence a lista de agendamentos do forum do usuario
                                auxAgendamentos.add((Agendamentos) listAgendamentos.get(i));
                            }
                        }
                    }
                    listLocais.clear();
                    listAgendamentos.clear();
                    listAgendamentos.addAll(auxAgendamentos);
                    auxAgendamentos.clear();
                }

            }
            if (!listAgendamentos.isEmpty()) {
                List <Peritos> listPeritos = new ArrayList<Peritos>();
                listPeritos = Peritos.AR.findAll();
                // excluir do arraylist, os peritos que nao possuem agendamentos nesta data.
                result.include("listAgendamentos", listAgendamentos);
                result.include("listPeritos", listPeritos);
                result.include("lotacaoSessao", lotacaoSessao);
            } else {
            	result.include("lotacaoSessao", lotacaoSessao);
            }
        } else {
            exception();
        }
    }
    
    @Path("/agendadas")
    public void agendadas(String data) {
        // pega matricula do usuario do sistema
        String matriculaSessao = getCadastrante().getMatricula().toString();
        String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        // busca a permissao do usuario
        UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
        // verifica se existe permissao
        if (objUsuario != null) {
            List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
            // verifica se o formulario submeteu alguma data
            if (data != null) {
                // Busca os agendamentos da data do formulario
                listAgendamentos = Agendamentos.AR.find("data_ag = to_date('" + data + "','dd-mm-yy') order by hora_ag , cod_local").fetch();
                // filtra os locais do forum do usuario
                List<Locais> listLocais = Locais.AR.find("cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'").fetch();
                // Verifica se existe local (sala) naquele forum do usuario
                if (!listAgendamentos.isEmpty()) {
                    // para cada agendamento, inlcui na lista a sala que eh do
                    // forum daquele usuario
                    List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
                    for (Integer i = 0; i < listAgendamentos.size(); i++) {
                        // pega o agendamento
                        for (Integer ii = 0; ii < listLocais.size(); ii++) {
                            // varre os locais do forum
                            if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais.get(ii).getCod_local()) {
                                // pertence a lista de agendamentos do forum do usuario
                                auxAgendamentos.add((Agendamentos) listAgendamentos.get(i));
                            }
                        }
                    }
                    listLocais.clear();
                    listAgendamentos.clear();
                    listAgendamentos.addAll(auxAgendamentos);
                    auxAgendamentos.clear();
                }

            }
            if (!listAgendamentos.isEmpty()) {
                List <Peritos> listPeritos = new ArrayList<Peritos>();
                listPeritos = Peritos.AR.findAll();
                // excluir do arraylist, os peritos que nao possuem agendamentos nesta data.
                result.include("listAgendamentos", listAgendamentos);
                result.include("listPeritos", listPeritos);
            } else {

            }

        } else {
            exception();
        }
    }


    @Path("/atualiza")
    public void atualiza(String cod_sala, String data_ag, String hora_ag) {
    	// pega usuario do sistema
    	String matriculaSessao = getCadastrante().getMatricula().toString();
    	String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
    	UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao );
    	if (objUsuario != null) {
    		// Pega o usuario do sistema, e, busca os locais(salas) daquele forum onde ele esta agendando.
    		Locais objSala = Locais.AR.find("cod_forum='" + objUsuario.getForumFk().getCod_forum() + "' and cod_local='" + cod_sala + "'").first(); // isso nao da erro no caso de retorno vazio?
    		String sala_ag = objSala.getLocal();
    		String lotacaoSessao = getCadastrante().getLotacao().getSiglaCompleta();
    		Agendamentos objAgendamento = Agendamentos.AR.find("cod_local='" + cod_sala + "' and data_ag = to_date('" + data_ag + "','yy-mm-dd') and hora_ag='" + hora_ag + "'").first();
    		String matricula_ag = objAgendamento.getMatricula();
    		String sesb_ag = objAgendamento.getSesb_pessoa();
    		//--------------deletar isso depois dos testes----------
    		// DpPessoa p = (DpPessoa) DpPessoa.AR.find("orgaoUsuario.idOrgaoUsu = " + getUsuarioIdOrgaoUsu() + " and dataFimPessoa is null and matricula='"	+ matricula_ag + "'").first();
    		// String lotacao_ag = p.getLotacao().getIdLotacao().toString();
    		// -----------------------------------------------------
    		String lotacao_ag = objAgendamento.getOrgao();
    		//System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
    		if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
    			String nome_perito_juizo="";
    			String processo = objAgendamento.getProcesso();
    			String periciado = objAgendamento.getPericiado();
    			String perito_juizo = objAgendamento.getPerito_juizo();
    			String perito_parte = objAgendamento.getPerito_parte();
    			String orgao_julgador = objAgendamento.getOrgao();
    			ContextoPersistencia.em().flush();
    			List<Peritos> listPeritos = new ArrayList<Peritos>();
    			listPeritos = Peritos.AR.find("1=1 order by nome_perito").fetch();
    			if(perito_juizo==null){perito_juizo="-";}
    			if(!perito_juizo.trim().equals("-")){
    				for(int i=0;i<listPeritos.size();i++){
    					if(listPeritos.get(i).getCpf_perito().trim().equals(perito_juizo.trim())){
    						nome_perito_juizo = listPeritos.get(i).getNome_perito();
    					}
    				}
    			}
    			result.include("sala_ag", sala_ag);
    			result.include("cod_sala", cod_sala);
    			result.include("data_ag", data_ag);
                result.include("hora_ag", hora_ag);
                result.include("processo", processo);
                result.include("periciado", periciado);
                result.include("perito_juizo", perito_juizo);
                result.include("nome_perito_juizo", nome_perito_juizo);
                result.include("perito_parte", perito_parte);
                result.include("orgao_julgador", orgao_julgador);
                result.include("listPeritos", listPeritos);
    		}else{
    		    redirecionaPaginaErro("Esse agendamento n&atilde;o pode ser modificado; pertence a outra vara." , null);
    		}
    	} else {
    	    redirecionaPaginaErro("Usu&aacute;rio sem permiss&atilde;o" , null);
    	}
    }
    

    @Path("/calendarioVetor")
    public void calendarioVetor(String frm_cod_local) {
        List listDatasLotadas = new ArrayList();
        List listDatasDoMes = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date parametro = new Date();
        Date dt = new Date();
        String dtt = df.format(dt);
        Agendamentos objAgendamento = new Agendamentos(parametro, null, null, null, null, null, null, null, null, null);
        try {
            List<Agendamentos> results = Agendamentos.AR.find(
                    "data_ag >= to_date('" + dtt.trim()
                            + "','dd/MM/yyyy') and cod_local='"
                            + frm_cod_local.trim() + "'  order by data_ag")
                    .fetch();
            // verifica se veio algum agendamento
            if (results.size() != 0) {
                // preenche as datas do local no 'MAS' na agenda
                // CORRENTE
                for (Iterator it = results.iterator(); it.hasNext();) {
                    objAgendamento = (Agendamentos) it.next();
                    listDatasDoMes.add(objAgendamento.getData_ag().toString());
                }
                String dia_ag_ant = "";
                String dia_ag_prox;
                int i = 0;
                // conta os agendamentos de cada dia, do local que veio do form
                for (Iterator it = listDatasDoMes.iterator(); it.hasNext();) {
                    dia_ag_prox = (String) it.next(); // pegou o proximo
                    if (i == 0) {
                        dia_ag_ant = dia_ag_prox;
                    }
                    if (dia_ag_prox.equals(dia_ag_ant)) {
                        i++; // contou a repeticao
                    } else {
                        i = 1;
                        dia_ag_ant = dia_ag_prox;
                    }
                    // se a data estiver lotada, marca
                    if (i >= 49) {
                        listDatasLotadas.add(dia_ag_ant);
                    } // guardou a data lotada
                }
                // veio algum agendamento
                // System.out.println(results.size() + " agendamentos...");
            } else {
                // nao veio nenhum agendamento
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.include("listDatasLotadas", listDatasLotadas);
    }
    
    @Path("/horarioVetor")
    public void horarioVetor(String frm_cod_local, String frm_data_ag) {
        List<String> listHorasLivres = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dt = new Date();
        String dtt = df.format(dt);
        Agendamentos objAgendamento = new Agendamentos(null, null, null, null,
                null, null, null, null, null, null);
        List<Agendamentos> results = new ArrayList<Agendamentos>();
        if (frm_data_ag != null && !frm_data_ag.isEmpty()) {
        	String matriculaSessao = getCadastrante().getMatricula().toString();
            String sesb_pessoaSessao = getCadastrante().getSesbPessoa().toString();
        	UsuarioForum objUsuario = UsuarioForum.findByMatricula(matriculaSessao, sesb_pessoaSessao);
        	if(objUsuario.getForumFk().getCod_forum()!=2){
             listHorasLivres.add("07:00");
             listHorasLivres.add("07:10");
             listHorasLivres.add("07:20");
             listHorasLivres.add("07:30");
             listHorasLivres.add("07:40");
             listHorasLivres.add("07:50");
        	}
            listHorasLivres.add("08:00");
            listHorasLivres.add("08:10");
            listHorasLivres.add("08:20");
            listHorasLivres.add("08:30");
            listHorasLivres.add("08:40");
            listHorasLivres.add("08:50");
            listHorasLivres.add("09:00");
            listHorasLivres.add("09:10");
            listHorasLivres.add("09:20");
            listHorasLivres.add("09:30");
            listHorasLivres.add("09:40");
            listHorasLivres.add("09:50");
            listHorasLivres.add("10:00");
            listHorasLivres.add("10:10");
            listHorasLivres.add("10:20");
            listHorasLivres.add("10:30");
            listHorasLivres.add("10:40");
            listHorasLivres.add("10:50");
            listHorasLivres.add("11:00");
            listHorasLivres.add("11:10");
            listHorasLivres.add("11:20");
            listHorasLivres.add("11:30");
            listHorasLivres.add("11:40");
            listHorasLivres.add("11:50");
            listHorasLivres.add("12:00");
            listHorasLivres.add("12:10");
            listHorasLivres.add("12:20");
            listHorasLivres.add("12:30");
            listHorasLivres.add("12:40");
            listHorasLivres.add("12:50");
            listHorasLivres.add("13:00");
            listHorasLivres.add("13:10");
            listHorasLivres.add("13:20");
            listHorasLivres.add("13:30");
            listHorasLivres.add("13:40");
            listHorasLivres.add("13:50");
            listHorasLivres.add("14:00");
            listHorasLivres.add("14:10");
            listHorasLivres.add("14:20");
            listHorasLivres.add("14:30");
            listHorasLivres.add("14:40");
            listHorasLivres.add("14:50");
            listHorasLivres.add("15:00");
            listHorasLivres.add("15:10");
            listHorasLivres.add("15:20");
            listHorasLivres.add("15:30");
            listHorasLivres.add("15:40");
            listHorasLivres.add("15:50");
            listHorasLivres.add("16:00");
            listHorasLivres.add("16:10");
            listHorasLivres.add("16:20");
            listHorasLivres.add("16:30");
            listHorasLivres.add("16:40");
            listHorasLivres.add("16:50");
            listHorasLivres.add("17:00");
            listHorasLivres.add("17:10");
            listHorasLivres.add("17:20");
            listHorasLivres.add("17:30");
            listHorasLivres.add("17:40");
            listHorasLivres.add("17:50");
            listHorasLivres.add("18:00");
            df.applyPattern("dd-MM-yyyy");
            try {
                dtt = frm_data_ag;
                results = Agendamentos.AR.find(
                        "data_ag = to_date('" + dtt.trim()
                                + "','dd-mm-yy') and cod_local='"
                                + frm_cod_local.trim() + "'").fetch();
                // zera os horarios ocupados na frm_data_ag selecionada, no local frm_cod_local
                String hrr = "";
                for (Iterator it = results.iterator(); it.hasNext();) {
                    objAgendamento = (Agendamentos) it.next();
                    hrr = objAgendamento.getHora_ag();
                    hrr = hrr.substring(0, 2) + ":" + hrr.substring(2, 4);
                    listHorasLivres.set(listHorasLivres.indexOf(hrr), "");
                }
            }catch (Exception e) {
                e.printStackTrace();
                if (e.getMessage().equals("-1")) {
                    listHorasLivres.clear();
                    listHorasLivres.add("Erro de horario invalido na base. Um lote excedeu o ultimo horario permitido, que e 18:00min");
                }
            } finally {
                result.include("listHorasLivres", listHorasLivres);
            }
        }

    }

    private String verificaCampoEInicializaCasoNull(String campo) {
        if(campo == null)
            return "";
        
        return campo;
    }
}
