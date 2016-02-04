<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<siga:pagina titulo="Erro">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<div style="position:absolute;left:30%;"><img src="/siga/css/famfamfam/icons/bell_error.png" /><h2>${msg}</h2></div>
	<br><br>
	<c:if test="${link != null}">
		<a style="position:absolute;left:15%" class="ui-state-hover" href="${link}">Retorna a tela anterior </a> 
	</c:if>
	<a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">Voltar</a>
</siga:pagina>
