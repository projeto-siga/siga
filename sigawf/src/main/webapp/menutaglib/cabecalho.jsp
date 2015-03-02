<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div style="background-color: #9DB1E5; height: 49px;width: 100%;">
<div style="float:left"><img
	src="${pageContext.request.contextPath}/sigalibs/toplogo1_70.png" /></div>
	
	
<div style="float:left">
<p class="cabecalho-title"><strong>Justiça Federal <c:catch><c:if test="${not empty titular.orgaoUsuario.descricao}">
- ${titular.orgaoUsuario.descricao}</c:if></c:catch>
</strong></p>
<p class="cabecalho-subtitle">Sistema Integrado de Gestão Administrativa
<c:if test="${not empty env}"> - <span style="color:red">${env}</span></c:if></p>
</div>


<div style="float:right"><img src="${pageContext.request.contextPath}/sigalibs/toplogo2_70.png"
	alt="Bandeira do Brasil"></div>
</div>

<div id="carregando">Carregando...</div>

