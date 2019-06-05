<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="titulo" scope="request">Acesso não autorizado</c:set>
<c:import context="/siga" url="/WEB-INF/page/principal/cabecalho.jsp" />

<center>
<table class="table table-borderless" width="729" border="1" cellspacing="0" cellpadding="0">
	<tr align="left">
		<td>Ocorreu o erro interno do servidor. Tente novamente.</td>
	</tr>
	<tr align="left">
		<td width="100%"><%= request.getRequestURL() %>&nbsp; <br>
<%-- 
<a href="${pageContext.request.contextPath}">Voltar...</a>  
--%>
	</tr>

</table>
</center>
<c:import context="/siga" url="/WEB-INF/page/principal/rodape.jsp" />
