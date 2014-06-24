<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="titulo" scope="request">Acesso não autorizado</c:set>
<c:import context="/siga" url="/paginas/cabecalho.jsp" />

<center>
<table width="729" border="1" cellspacing="0" cellpadding="0"
	bordercolor="#FFFFFF" bgcolor="#FFFFFF">
	<tr align="left">
		<td>Você não tem acesso à Página</td>
	</tr>
	<tr align="left">
		<td width="100%"><%= request.getRequestURL() %>&nbsp; <br>
<%-- 
<a href="${pageContext.request.contextPath}">Voltar...</a>  
--%>
	</tr>

</table>
</center>
<c:import context="/siga" url="/paginas/rodape.jsp" />
