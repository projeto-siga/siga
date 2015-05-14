package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import models.Agendamentos;
import models.Foruns;
import models.Locais;
import models.UsuarioForum;
import models.Peritos;

import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Application extends SigaApplication {

	@Before(priority = 1)
	public static void addDefaultsAlways() throws Exception {
		prepararSessao();
	}

	@Before(priority = 2,unless = {"publicKnowledge", "dadosRI"})
	public static void addDefaults() throws Exception {

		try {
			obterCabecalhoEUsuario("rgb(235, 235, 232)");
		} catch (Exception e) {
			tratarExcecoes(e);
		}
	}

	protected static void assertAcesso(String path) throws Exception {
		// SigaApplication.assertAcesso("SR:M√≥dulo de Servi√ßos;" + path);
	}

	@Catch()
	public static void tratarExcecoes(Exception e) {
		SigaApplication.tratarExcecoes(e);
	}

	public static void index() {
		// desativado
		render();
		// models.Locais role
	}

	public static void home() {
		// p·gina inicial do sigapmp
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			try {
				List<Locais> lstLocais = Locais.find("forumFk=" + objUsuario.forumFk.cod_forum	+ "order by ordem_apresentacao ").fetch();
				Foruns objForum = Foruns.find("cod_forum=" + objUsuario.forumFk.cod_forum).first();
				ArrayList vetorForuns = new ArrayList();
				String texto = objForum.mural;
				int i = 0;
				while (texto.length() > 4) {
					vetorForuns.add(texto.substring(0, texto.indexOf("<br>")));
					texto = texto.substring(texto.indexOf("<br>"),
							texto.length());
					texto = texto.substring(4, texto.length());
					i++;
				}
				render(lstLocais, vetorForuns);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Excecoes("Usuario sem permissao", null);
		}
	}

	public static void sala_incluir() {
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			render();
		} else {
			Excecoes("Usuario sem permissao" , null);
		}
	}

	public static void sala_insert(Locais formLocal, int cod_forum) {
		Foruns objForum = new Foruns(cod_forum, " ", " ");
		formLocal.forumFk = objForum;
		String varCodLocal = formLocal.cod_local;
		String resposta = "";
		try {

			System.out.println(JPA.em().createQuery("from Locais where cod_local ='" + varCodLocal+ "'").getSingleResult());
			resposta = "Sala ja existe. Confira o codigo da sala. ";

		} catch (Exception e) {
			try {
				System.out.println(JPA.em().createQuery("from Foruns where cod_forum=" + cod_forum));
				formLocal.save();
				JPA.em().flush();
				resposta = "Ok.";
			} catch (Exception e2) {
				resposta = "Forum da sala nao cadastrado. Confira o codigo do forum.";
			}
		}
		render(resposta);
	}

	public static void salas_listar(String cod_local, String sala,
			String desc_forum) {
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
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
				listLocais = Locais.findAll();
				render(listLocais);
			} else if (!cod_local.isEmpty()) {
				try {
					Object auxLocal = Locais.findById(cod_local);
					if (auxLocal.equals(null)) {
						// retornou nulo
					} else {
						listLocais.add((Locais) auxLocal);
					}
				} finally {
					render(listLocais);
				}
			} else if (!sala.isEmpty()) {
				try {
					listLocais = JPA.em().createQuery(	"from Locais where local like '" + sala	+ "%'").getResultList();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					render(listLocais);
				}
			} else if (!desc_forum.isEmpty()) {
				try {
					List<Foruns> listForuns = new ArrayList<Foruns>();
					Foruns objForum = new Foruns(0, "", "");
					List<Locais> listLocaisAux = new ArrayList<Locais>();
					listForuns = JPA
							.em()
							.createQuery(
									"from Foruns where descricao_forum like'"
											+ desc_forum + "%'")
							.getResultList();
					for (Iterator iterator01 = listForuns.iterator(); iterator01
							.hasNext();) {
						objForum = (Foruns) iterator01.next();
						listLocaisAux = JPA
								.em()
								.createQuery(
										"from Locais where forumFk ="
												+ objForum.cod_forum)
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
					render(listLocais);
				}
			}
		} else {
			Excecoes("Usuario sem permissao", null);
		}
	}

	public static void sala_delete(String cod_sala) {
		String resposta = " ";
		if (!cod_sala.isEmpty()) {
			try {
				resposta = "Ok.";
				Locais.delete("from Locais where cod_local='" + cod_sala + "'");
			} catch (Exception e) {
				e.printStackTrace();
				resposta = "Esta sala possui agendamentos. Delete primeiro os agendamentos referenciados.";
			}
		} else {
			resposta = "N„o Ok.";
		}
		render(resposta);
	}

	public static void forum_incluir() {
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			render();
		} else {
			Excecoes("Usuario sem permissao", null);
		}
	}

	public static void forum_insert(int cod_forum, String descricao,
			String mural) {
		String resposta = "";
		try {
			Foruns objForum = new Foruns(cod_forum, descricao, mural);
			objForum.save();
			JPA.em().flush();
			resposta = "Ok.";
		} catch (PersistenceException e) {
			e.printStackTrace();
			resposta = "N„o Ok.";
			if (e.getMessage().substring(23, 53)
					.equals(".ConstraintViolationException:")) {
				resposta = "FÛrum j· existe. Verifique se o cÛdigo est· correto.";
			}
		} finally {
			render(resposta);
		}
	}

	public static void foruns_listar(Integer cod_forum, String descricao_forum) {
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			List<Foruns> listForuns = new ArrayList<Foruns>();
			if (cod_forum == null) {
				cod_forum = -1;
			}
			if (descricao_forum == null) {
				descricao_forum = "";
			}
			if (cod_forum == -1 && descricao_forum.isEmpty()) {
				listForuns = Foruns.findAll();
				render(listForuns);
			} else if (cod_forum >= 0) {
				Object auxForum = Foruns.findById(cod_forum);
				if (auxForum != null) {
					listForuns.add((Foruns) auxForum);
				}
				render(listForuns);
			} else if (!descricao_forum.isEmpty()) {
				List<Foruns> resultado = JPA
						.em()
						.createQuery(
								"from Foruns where descricao_forum like '"
										+ descricao_forum + "%'")
						.getResultList();
				for (Iterator iterator = resultado.iterator(); iterator
						.hasNext();) {
					listForuns.add((Foruns) iterator.next());
				}
				render(listForuns);
			}
		} else {
			Excecoes("Usuario sem permissao", null);
		}
	}

	public static void forum_delete(String cod_forum) {
		String resposta;
		if (!cod_forum.isEmpty()) {
			resposta = "Ok.";
			try {
				Foruns.delete("from Foruns where cod_forum=" + cod_forum);
			} catch (PersistenceException e) {
				if (e.getMessage().substring(23, 53)
						.equals(".ConstraintViolationException:")) {
					System.out.println(e.getMessage());
					resposta = "Forum possui referÍncia. Delete primeiro as salas referenciadas.";
				} else {
					resposta = "erro.";
				}

			} finally {
				render(resposta);
			}
		} else {
			resposta = "N„o Ok.";
			render(resposta);
		}
	}

	public static void agendamento_incluir_ajax(String fixo_perito_juizo) {
		String fixo_perito_juizo_nome = "";
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		List<Locais> listSalas = new ArrayList();
		List<Peritos> listPeritos = new ArrayList();
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// Pega o usu·rio do sistema, e, busca os locais(salas) daquele
			// forum onde ele est·.
			listSalas = (List) Locais.find(
					"cod_forum='" + objUsuario.forumFk.cod_forum
							+ "' order by ordem_apresentacao ").fetch(); // isso n„o d· erro no caso de retorno vazio.
			listPeritos =  (List) Peritos.find("1=1 order by nome_perito").fetch();
			//   buscar o nome do perito fixo na lista se existir 
			if(fixo_perito_juizo!=null){
			 for(int i=0;i<listPeritos.size();i++) {
				 if(listPeritos.get(i).cpf_perito.trim().equals( fixo_perito_juizo.trim() ) ){
					 fixo_perito_juizo_nome = listPeritos.get(i).nome_perito;
				 }
			 }
			}
			
			render(listSalas,lotacaoSessao, fixo_perito_juizo, fixo_perito_juizo_nome, listPeritos);
		} else {
			Excecoes("Usuario sem permissao", null);
		}
	}

	public static void calendario_vetor(String frm_cod_local) {
		List listDatasLotadas = new ArrayList();
		List listDatasDoMes = new ArrayList();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date parametro = new Date();
		Date dt = new Date();
		String dtt = df.format(dt);
		Agendamentos objAgendamento = new Agendamentos(parametro, null, null,
				null, null, null, null, null, null);
		try {
			List<Agendamentos> results = Agendamentos.find(
					"data_ag >= to_date('" + dtt.trim()
							+ "','dd/MM/yyyy') and cod_local='"
							+ frm_cod_local.trim() + "'  order by data_ag")
					.fetch();
			// verifica se veio algum agendamento
			if (results.size() != 0) {
				// preenche as datas do local no 'M√äS' na agenda
				// CORRENTE
				for (Iterator it = results.iterator(); it.hasNext();) {
					objAgendamento = (Agendamentos) it.next();
					listDatasDoMes.add(objAgendamento.data_ag.toString());
				}
				String dia_ag_ant = "";
				String dia_ag_prox;
				int i = 0;
				// conta os agendamentos de cada dia, do local que
				// veio do form
				for (Iterator it = listDatasDoMes.iterator(); it.hasNext();) {
					dia_ag_prox = (String) it.next(); // pegou o prÛximo
					if (i == 0) {
						dia_ag_ant = dia_ag_prox;
					}
					if (dia_ag_prox.equals(dia_ag_ant)) {
						i++; // contou a repetiÁ„o
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
				// n„o veio nenhum agendamento
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		render(listDatasLotadas);
	}

	public static void horario_vetor(String frm_cod_local, String frm_data_ag) {
		List<String> listHorasLivres = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dt = new Date();
		String dtt = df.format(dt);
		Agendamentos objAgendamento = new Agendamentos(null, null, null, null,
				null, null, null, null, null);
		List<Agendamentos> results = new ArrayList<Agendamentos>();
		if (frm_data_ag != null && !frm_data_ag.isEmpty()) {
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
			df.applyPattern("dd-MM-yyyy");
			try {
				dtt = frm_data_ag;
				results = Agendamentos.find(
						"data_ag = to_date('" + dtt.trim()
								+ "','dd-mm-yy') and cod_local='"
								+ frm_cod_local.trim() + "'").fetch();
				// zera os hor√°rios ocupados na frm_data_ag
				// selecionada, no local frm_cod_local
				String hrr = "";
				for (Iterator it = results.iterator(); it.hasNext();) {
					objAgendamento = (Agendamentos) it.next();
					hrr = objAgendamento.hora_ag;
					hrr = hrr.substring(0, 2) + ":" + hrr.substring(2, 4);
					listHorasLivres.set(listHorasLivres.indexOf(hrr), "");
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage().equals("-1")) {
					listHorasLivres.clear();
					listHorasLivres.add("Erro de hor·rio inv·lido na base.");
				}
			} finally {
				render(listHorasLivres);
			}
		}

	}

	public static void agendamento_insert(String frm_data_ag,
			String frm_hora_ag, String frm_cod_local, String matricula,
			String periciado, String perito_juizo, String perito_parte,
			String orgao, String processo, Integer lote) {
		matricula = cadastrante().getMatricula().toString();
		String resposta = "";
		Locais auxLocal = Locais.findById(frm_cod_local);
		String hr;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parametro = df.parse(frm_data_ag);
			Agendamentos objAgendamento = new Agendamentos(parametro,
					frm_hora_ag, auxLocal, matricula, periciado, perito_juizo,
					perito_parte, processo, orgao);
			hr = frm_hora_ag;
			Agendamentos agendamentoEmConflito = null;
			// begin transaction, que, segundo o Da Rocha √© automatico no inicio da action
			String hrAux = hr.substring(0, 2);
			String minAux = hr.substring(3, 5);
			if (hr != null && (!hr.isEmpty())) {
				//verifica se tem conflito
				String horaPretendida = null;
				for(int i = 0; i < lote; i++){
					horaPretendida=hrAux+minAux;
					agendamentoEmConflito = Agendamentos.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +horaPretendida+ "' and data_ag=to_date('"+ frm_data_ag +"' , 'yy-mm-dd')"  ).first();
					if (agendamentoEmConflito!=null){
						Excecoes("Perito nao disponivel no horario de " + agendamentoEmConflito.hora_ag.substring(0,2)+ "h" + agendamentoEmConflito.hora_ag.substring(2,4) + "min" , null );
					}
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.intervalo_atendimento);
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
				}
				// sloop
				hrAux = hr.substring(0, 2);
				minAux = hr.substring(3, 5);
				for (int i = 0; i < lote; i++) {
					objAgendamento.hora_ag = hrAux + minAux;
					objAgendamento.save();
					JPA.em().flush();
					JPA.em().clear();
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.intervalo_atendimento);
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
					resposta = "Ok.";
				}
				// floop
				// end transaction, que, segundo o Da Rocha √© autom√°tico; no fim
				// da action
			} else {
				resposta = "N„o Ok.";
			}
		} catch (Exception e) {
			// rollback transaction, que segundo o Da Rocha √© autom√°tico; ocorre
			// em qualquer erro
			e.printStackTrace();
			String erro = e.getMessage();
			if (erro.substring(24, 52).equals("ConstraintViolationException")) {
				resposta = "N„o Ok. O lote n„o foi agendado.";
			} else {
				resposta = "N„o Ok. Verifique se preencheu todos os campos do agendamento.";
			}
		} finally {
			render(resposta, perito_juizo);
		}
	}

	public static void agendamento_excluir(String frm_data_ag) {
		// pega matricula do usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		// pega a permiss√£o do usuario
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		// verifica se tem permissao
		if (objUsuario != null) {
			List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
			// verifica se o formul√°rio submeteu alguma data
			if (frm_data_ag != null) {
				// Busca os agendamentos da data do formul√°rio
				listAgendamentos = Agendamentos.find("data_ag = to_date('" + frm_data_ag + "','dd-mm-yy') order by hora_ag , cod_local")
						.fetch();
				// busca os locais do forum do usuario
				List<Locais> listLocais = Locais.find(
						"cod_forum='" + objUsuario.forumFk.cod_forum + "'")
						.fetch();
				// Verifica se existe local naquele forum do usu√°rio
				if (listAgendamentos.size() != 0) {
					// para cada agendamento, inlcui na lista a sala que √© do
					// forum daquele usu·rio
					List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
					for (Integer i = 0; i < listAgendamentos.size(); i++) {
						// pega o agendamento
						for (Integer ii = 0; ii < listLocais.size(); ii++) {
							// varre os locais do forum
							if (listAgendamentos.get(i).localFk.cod_local == listLocais
									.get(ii).cod_local) {
								// pertence √† lista de agendamentos do forum do
								// usuario
								auxAgendamentos
										.add((Agendamentos) listAgendamentos
												.get(i));
							}
						}
					}
					listLocais.clear();
					listAgendamentos.clear();
					listAgendamentos.addAll(auxAgendamentos);
					auxAgendamentos.clear();
				}
			}
			if (listAgendamentos.size() != 0) {
				List <Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.findAll();
				// excluir do arraylist, os peritos que n„o possuem agendamentos nesta data.
				render(listAgendamentos, listPeritos);
			} else {
				render();
			}
		} else {
			Excecoes("Usuario sem permissao" , null);
		}

	}

	public static void agendamento_delete(Agendamentos formAgendamento,
			String cod_local) {
		String resultado = "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtt = df.format(formAgendamento.data_ag);
		try {
			Agendamentos ag = Agendamentos.find(
					"hora_ag = '" + formAgendamento.hora_ag
							+ "' and localFk.cod_local='" + cod_local
							+ "' and data_ag = to_date('" + dtt
							+ "','dd/mm/yy')").first();
			//--------------------------
			String lotacaoSessao = cadastrante().getLotacao().getIdLotacao()
					.toString();
			String matricula_ag = ag.matricula;
			DpPessoa p = (DpPessoa) DpPessoa.find(
					"orgaoUsuario.idOrgaoUsu = "
							+ cadastrante().getOrgaoUsuario().getIdOrgaoUsu()
							+ " and dataFimPessoa is null and matricula='"
							+ matricula_ag + "'").first();
			String lotacao_ag = p.getLotacao().getIdLotacao().toString(); 
			//System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
			if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
			//--------------------------
			ag.delete();
			resultado = "Ok.";
			}else{
				Excecoes("Esse agendamento nao pode ser deletado; pertence a outra vara." , null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultado = "N„o Ok.";
		} finally {
			render(resultado,dtt);
		}
	}

	public static void agendamento_atualiza(String cod_sala, String data_ag, String hora_ag) {
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// Pega o usu·rio do sistema, e, busca os locais(salas) daquele
			// forum onde ele est·.
			Locais objSala = Locais.find("cod_forum='" + objUsuario.forumFk.cod_forum + "' and cod_local='" + cod_sala + "'").first(); // isso n„o d· erro no caso de retorno vazio?
			String sala_ag = objSala.local;
			String lotacaoSessao = cadastrante().getLotacao().getIdLotacao().toString();
			//System.out.println(lotacaoSessao);
			Agendamentos objAgendamento = Agendamentos.find("cod_local='" + cod_sala + "' and data_ag = to_date('" + data_ag + "','yy-mm-dd') and hora_ag='" + hora_ag + "'").first();
			String matricula_ag = objAgendamento.matricula;
			DpPessoa p = (DpPessoa) DpPessoa.find("orgaoUsuario.idOrgaoUsu = " + cadastrante().getOrgaoUsuario().getIdOrgaoUsu() + " and dataFimPessoa is null and matricula='"	+ matricula_ag + "'").first();
			String lotacao_ag = p.getLotacao().getIdLotacao().toString(); 
			//System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
			if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
				String nome_perito_juizo="";
				String processo = objAgendamento.processo;
				String periciado = objAgendamento.periciado;
				String perito_juizo = objAgendamento.perito_juizo;
				String perito_parte = objAgendamento.perito_parte;
				String orgao_julgador = objAgendamento.orgao;
				JPA.em().flush();
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.find("1=1 order by nome_perito").fetch();
				if(perito_juizo==null){perito_juizo="-";}
				if(!perito_juizo.trim().equals("-")){
					for(int i=0;i<listPeritos.size();i++){
						if(listPeritos.get(i).cpf_perito.trim().equals(perito_juizo.trim())){
							nome_perito_juizo = listPeritos.get(i).nome_perito;
						}
					}
				}
				render(sala_ag, cod_sala, data_ag, hora_ag, processo, periciado, perito_juizo, nome_perito_juizo, perito_parte, orgao_julgador, listPeritos);
			}else{
				Excecoes("Esse agendamento nao pode ser modificado; pertence a outra vara." , null);
			}
		} else {
			Excecoes("Usuario sem permissao" , null);
		}
	}
	
	public static void agendamento_update(String cod_sala, String data_ag, String hora_ag, String processo, String periciado, String perito_juizo, String perito_parte, String orgao_ag){
		String resultado = "";
		Agendamentos agendamentoEmConflito = null;
		try{
			// Devo verificar agendamento conflitante, antes de fazer o UPDATE.
			System.out.println(perito_juizo.trim()+""+data_ag+" "+hora_ag.substring(0,2)+hora_ag.substring(3,5));
			agendamentoEmConflito = Agendamentos.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +hora_ag.substring(0,2)+hora_ag.substring(3,5)+ "' and data_ag=to_date('"+ data_ag +"', 'dd-mm-yy' ) and localFk<>'"+cod_sala+"'").first();
			
			if (agendamentoEmConflito!=null){
				Excecoes("Perito nao disponivel no horario de " + agendamentoEmConflito.hora_ag.substring(0,2) +"h"+agendamentoEmConflito.hora_ag.substring(2,4)+"min" , " agendamento_excluir?frm_data_ag="+data_ag);
			}
			JPA.em().createQuery("update Agendamentos set processo = '"+ processo +"', "+ "periciado='"+ periciado +"', perito_juizo='"+ perito_juizo.trim() +"', perito_parte='"+perito_parte+"', orgao='"+orgao_ag+"' where cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+data_ag+"','dd-mm-yy')").executeUpdate();
			JPA.em().flush();
			Agendamentos objAgendamento = (Agendamentos) Agendamentos.find("cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+data_ag+"','dd-mm-yy')" ).first();
			if(objAgendamento==null){
				resultado = "Nao Ok.";
			}else{
				resultado = "Ok.";
			}
		}catch(PersistenceException e){
			e.printStackTrace();
			resultado = "N„o Ok.";
		}catch(Exception ee){
			ee.printStackTrace();
			resultado = "N„o Ok.";
		}finally{
			render(resultado,data_ag);
		}
	}
	public static void agendadas_hoje() {
		// pega usu·rio do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// busca locais em funÁ„o da configuraÁ„o do usu·rio
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.find("forumFk="+objUsuario.forumFk.cod_forum).fetch();
			// monta string de criterio
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).cod_local.toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date hoje = new Date();
			String dtt = df.format(hoje);
			// busca agendamentos de hoje
			if(criterioSalas.equals("")){criterioSalas="''";}
			 List<Agendamentos> listAgendamentos = Agendamentos.find("data_ag = to_date('" + dtt + "','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag, cod_local").fetch();
			 if (listAgendamentos.size() != 0) {
				// busca as salas daquele forum
				List<Locais> listLocais = Locais.find(
						"cod_forum='" + objUsuario.forumFk.cod_forum + "'")
						.fetch();
				// lista auxiliar
				List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
				for (int i = 0; i < listAgendamentos.size(); i++) {
					// varre listAgendamentos
					for (int ii = 0; ii < listLocais.size(); ii++) {
						// compara com cada local do forum do usu√°rio
						if (listAgendamentos.get(i).localFk.cod_local == listLocais
								.get(ii).cod_local) {
							auxAgendamentos.add((Agendamentos) listAgendamentos
									.get(i));
						}
					}
				}
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.findAll();
				render(listAgendamentos, listPeritos);
			} else {
				render();
			}
		} else {
			Excecoes("Usuario sem permissao" , null);
		}

	}
	public static void agendadas_hoje_print(String frm_data_ag) {
		// pega usu·rio do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// busca locais em funÁ„o da configuraÁ„o do usu·rio
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.find("forumFk="+objUsuario.forumFk.cod_forum).fetch();
			// monta string de criterio
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).cod_local.toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			if (frm_data_ag.isEmpty()){
				render();
			}else{
				List listAgendamentos = (List) Agendamentos.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag , localFk" ).fetch();
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.findAll();
				render(listAgendamentos, listPeritos);
			}
		}else {
			Excecoes("Usuario sem permissao" , null);
		}
	}
	public static void agendamento_imprime(String frm_data_ag){
		String matriculaSessao = cadastrante().getMatricula().toString();
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
	if (objUsuario != null) {
		if(frm_data_ag==null){
			frm_data_ag = "";
		}else{
			listAgendamentos = Agendamentos.find( "data_ag=to_date('" + frm_data_ag + "','dd-mm-yy') order by hora_ag, cod_local" ).fetch();
			DpPessoa p = null;
			// deleta os agendamentos de outros org„os
			for(int i=0;i<listAgendamentos.size();i++){
				 p = (DpPessoa) DpPessoa.find("orgaoUsuario.idOrgaoUsu = " + cadastrante().getOrgaoUsuario().getIdOrgaoUsu() 
									+ " and dataFimPessoa is null and matricula='"+ listAgendamentos.get(i).matricula + "'").first(); 
				if(lotacaoSessao.trim().equals(p.getLotacao().getSiglaLotacao().toString().trim())){
					System.out.println("");
				}else{
					listAgendamentos.remove(i);
					i--;
				}
			}
		}
		List<Peritos> listPeritos = new ArrayList<Peritos>();
		listPeritos = Peritos.findAll();
		render(listAgendamentos, listPeritos);
	}else{
		Excecoes("Usuario sem permissao" , null);
	}
	}
	public static void agendamento_print(String frm_data_ag, String frm_sala_ag, String frm_processo_ag, String frm_periciado ){
		List listAgendamentos = (List) Agendamentos.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','yy-mm-dd') and localFk.cod_local='"+frm_sala_ag+"' and processo='"+frm_processo_ag+"' and periciado='"+frm_periciado+"'" ).fetch();
		if(frm_periciado.isEmpty()){
			Excecoes("Relatorio depende de nome de periciado preenchido para ser impresso." , null);
		}else if(frm_processo_ag.isEmpty()){
			Excecoes("Relatorio depende de numero de processo preenchido para ser impresso." , null);
		}else{
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.findAll();
			render(frm_processo_ag, listAgendamentos, listPeritos);
		}
	}
	
	public static void agendamento_sala_lista(String frm_cod_local, String frm_data_ag){
		String local = "";
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		List<Locais> listSalas = new ArrayList();
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// Pega o usu·rio do sistema, e, busca os locais(salas) daquele
			// forum onde ele est·.
			listSalas = ((List) Locais.find("forumFk='" + objUsuario.forumFk.cod_forum + "' order by ordem_apresentacao ").fetch()); // isso n„o d· erro no caso de retorno vazio.
			List<Agendamentos> listAgendamentosMeusSala = new ArrayList();
			if(!(frm_cod_local==null||frm_data_ag.isEmpty())){
				//lista os agendamentos do dia, e, da lotaÁ„o do cadastrante
				listAgendamentosMeusSala = ((List) Agendamentos.find("localFk.cod_local='" + frm_cod_local + "' and data_ag = to_date('" + frm_data_ag + "','yy-mm-dd') order by hora_ag").fetch());
				for(int i=0;i<listSalas.size();i++){
					if(listSalas.get(i).cod_local.equals(frm_cod_local)){
						local = listSalas.get(i).local;
					}
				}
				
			}
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.findAll();
			render(local, listSalas,listAgendamentosMeusSala,lotacaoSessao, listPeritos);
		} else {
			Excecoes("Usuario sem permissao" , null);
		}
	}
	public static void usuario_atualiza(String paramCodForum) throws Exception {
		String mensagem = "";
		// pega usuario do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		String nomeSessao = cadastrante().getNomeAbreviado();
		UsuarioForum objUsuario = UsuarioForum.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			String descricaoForum = "";
			if (paramCodForum != null && !paramCodForum.isEmpty()) {
				Foruns objForum = Foruns.findById(Integer
						.parseInt(paramCodForum));
				descricaoForum = objForum.descricao_forum;
				objUsuario.delete();
				JPA.em().flush();
				JPA.em().clear();
				objUsuario.forumFk = objForum;
				objUsuario.matricula_usu = matriculaSessao;
				objUsuario.nome_usu = nomeSessao;
				try {
					objUsuario.save();
					JPA.em().flush();
					JPA.em().clear();
					mensagem = "Ok.";
				} catch (Exception e) {
					e.printStackTrace();
					mensagem = "N„o Ok.";
				}
			} else {
				paramCodForum = Integer.toString(objUsuario.forumFk.cod_forum);
				Foruns objForum = Foruns.find(
						"cod_forum = " + Integer.parseInt(paramCodForum))
						.first();
				descricaoForum = objForum.descricao_forum;
				JPA.em().flush();
			}
			List<Foruns> outrosForuns = Foruns.find(
					"cod_forum <> " + paramCodForum).fetch();
			render(objUsuario, paramCodForum, descricaoForum, mensagem,
					outrosForuns);
		} else {
			Excecoes("Usuario sem permissao." , null);
		}
	}
	public static void permissao_inclui(String matricula_permitida, String nome_permitido, String forum_permitido ) throws Exception{
		String mensagem = "";
		// pega usu·rio do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		// String nomeSessao = cadastrante().getNomeAbreviado();
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		UsuarioForum objUsuario = UsuarioForum.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null) && ((lotacaoSessao.trim().equals("CSIS") || lotacaoSessao.trim().equals("SESIA")))){
			if((matricula_permitida!=null) && (nome_permitido!=null) && (forum_permitido!=null) && (!matricula_permitida.isEmpty()) && (!nome_permitido.isEmpty()) && (!forum_permitido.isEmpty())){
				Foruns atribForum = (Foruns) Foruns.find("cod_forum='"+forum_permitido+"'").first();
				UsuarioForum usuarioPermitido = new UsuarioForum(matricula_permitida, nome_permitido, atribForum);
				try {
					usuarioPermitido.save();
					JPA.em().flush();
					JPA.em().clear();
					mensagem = "Ok.";
				}catch (Exception e) {
					e.printStackTrace();
					if ((e.getMessage().substring(0,89).equals("a different object with the same identifier value was already associated with the session")) || (e.getMessage().substring(54,89).equals("Could not execute JDBC batch update"))){
						mensagem="Usuario ja tinha permissao.";
					}else{
						mensagem = "Nao Ok.";
					}
				}finally{
					render(mensagem);
				}
			}else{
				mensagem="";
				render(mensagem);
			}
			
		}else{
			Excecoes("Usuario sem permissao." , null );
		}
	}
	
	public static void permissao_exclui(String matricula_proibida){
		String mensagem = "";
		// pega usu·rio do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		UsuarioForum objUsuario = UsuarioForum.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null) && ( (lotacaoSessao.trim().equals("CSIS")||lotacaoSessao.trim().equals("SESIA")) )){ //pode excluir a permiss„o
			List<UsuarioForum> listPermitidos = new ArrayList<UsuarioForum>();
			if((matricula_proibida!=null) && (!matricula_proibida.isEmpty())){ // deleta permissao
				try{
					UsuarioForum objUsuarioProibido = UsuarioForum.find("matricula_usu='"+matricula_proibida+"'").first();
					objUsuarioProibido.delete();
					JPA.em().flush();
					JPA.em().clear();
					mensagem = "Ok";
				}catch(Exception e){
					e.printStackTrace();
					mensagem = "Nao Ok";
				}finally{
					render(mensagem);
				}
			 } else{ // lista permitidos
				try{
					 listPermitidos = (List) UsuarioForum.find(" order by nome_usu ").fetch(); // isso n„o d· erro no caso de retorno vazio.
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					render(listPermitidos);
				}
			}
		}
	}
	public static void perito_incluir(){
		// pega usu·rio do sistema
		String matriculaSessao = cadastrante().getMatricula().toString();
		String lotacaoSessao = cadastrante().getLotacao().getSiglaLotacao();
		UsuarioForum objUsuario = UsuarioForum.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null)){ //pode incluir perito
			render();
		}else{
			Excecoes("Usuario sem permissao." , null);
		}
	}
	
	public static void perito_insert(String cpf_perito, String nome_perito){
		String resposta="";
		try{
			Peritos objPerito = new Peritos(cpf_perito, nome_perito);
			objPerito.save();
			JPA.em().flush();
			resposta="ok";
		}catch(PersistenceException e){
			e.printStackTrace();
			resposta="Nao ok.";
			if (e.getMessage().substring(23, 53)
					.equals(".ConstraintViolationException:")) {
				resposta = "Verifique se o CPF do perito esta correto, ou, se o perito ja esta cadastrado. ";
			}
		}finally{
			render(resposta);
		}
	}
	public static void creditos() {
		render();
	}

	@Catch
	public static void Excecoes(String e , String l) {
		String msg = e;
		String lnk = l;
		render("Application/erro.html", msg, lnk);
	}
}