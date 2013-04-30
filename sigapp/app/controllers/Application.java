package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import play.*;
import play.data.binding.As;
import play.db.jpa.JPA;
import play.mvc.*;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.activation.CommandObject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.transaction.synchronization.ExceptionMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import models.*;

public class Application extends Controller {
    public static void index() {
    	// desativado
    	render();
    	//models.Locais role
    }
    public static void home(){
    	// página inicial do sigapmp
    	render();
    }
    public static void sala_incluir(){
    	render();
    }
    public static void sala_insert(Locais objLocal) {
    	if (Locais.findById(objLocal.cod_local) == null){
    		try{
    			objLocal.save();
    			render();
    		}catch (PersistenceException e){
    			if(e.getMessage().substring(24,52).equals("ConstraintViolationException".toString())){
    	    		String msg = "Forum não cadastrado";
    	    		render("application/erro.html",msg);
    	    	}else{
    	    		System.out.println("Erro em geral");
    	    	}
    		}
    	}else{
    		String msg="Sala já existe.";
			render("application/erro.html",msg);
    	}
    }
    public static void salas_listar(){
    	List<Locais> listLocais = Locais.findAll();
    	render(listLocais);
    }
    public static void forum_incluir(){
    	render();
    }
    /*public static void forum_insert(Foruns objForum) throws Exception{
    	if (Foruns.findById(objForum.cod_forum)==null){
    		objForum.save();
    		render();
    	}else{
    		throw new Exception("Forum já cadastrado");
    	}		
    }
	public static void foruns_listar(Integer cod_forum, String descricao_forum){
    	List<Foruns> listForuns = new ArrayList<Foruns>();
		if(cod_forum==null){cod_forum=-1;}
		if(descricao_forum==null){descricao_forum="";}
		if(cod_forum==-1 && descricao_forum.isEmpty()){
    		listForuns = Foruns.findAll();
    		render(listForuns);
    	}else if(cod_forum>=0){
    		Object auxForum = Foruns.findById(cod_forum);
    		listForuns.add((Foruns)auxForum);
    		render(listForuns);
    	}else if (!descricao_forum.isEmpty()){
    		List<Foruns> resultado = JPA.em().createQuery("from Foruns where descricao_forum like '"+descricao_forum+"%'").getResultList();
    		for (Iterator iterator = resultado.iterator(); iterator.hasNext();) {
				listForuns.add((Foruns) iterator.next());
    		}
    		render(listForuns);
    	}	
    }*/
    @Catch
    public static void Excecoes(Exception e){
    	String msg=e.getMessage();
		System.out.println("+++ Excecoes  Exceptions  +++");
		e.printStackTrace();
		render("application/erro.html", msg);
    }
}