<%@ tag body-content="empty" import="br.gov.jfrj.siga.libs.design.SigaDesign,java.lang.String,java.lang.Boolean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ attribute name="titulo"%>
<%@ attribute name="menu"%>
<%@ attribute name="idpagina"%>
<%@ attribute name="barra"%>
<%@ attribute name="ambiente"%>
<%@ attribute name="popup"%>
<%@ attribute name="meta"%>
<%@ attribute name="pagina_de_erro"%>
<%@ attribute name="onLoad"%>
<%@ attribute name="desabilitarbusca"%>
<%@ attribute name="desabilitarmenu"%>
<%@ attribute name="incluirJs"%>

<c:if test="${not empty titulo}">
	<c:set var="titulo" scope="request" value="${titulo}" />
</c:if>

<c:if test="${not empty pagina_de_erro}">
	<c:set var="pagina_de_erro" scope="request" value="${pagina_de_erro}" />
</c:if>

<c:if test="${not empty menu}">
	<c:set var="menu" scope="request">${menu}</c:set>
</c:if>

<c:if test="${not empty idpagina}">
	<c:set var="idpage" scope="request">${idpagina}</c:set>
</c:if>

<c:if test="${not empty barra}">
	<c:set var="barranav" scope="request">${barra}</c:set>
</c:if>

<c:set var="ambiente">
	<c:if test="${f:resource('isVersionTest') or f:resource('isBaseTest')}">
		<c:if test="${f:resource('isVersionTest')}">SISTEMA</c:if>
		<c:if
			test="${f:resource('isVersionTest') and f:resource('isBaseTest')}"> E </c:if>
		<c:if test="${f:resource('isBaseTest')}">BASE</c:if> DE TESTES
	</c:if>
</c:set>
<c:if test="${not empty ambiente}">
	<c:set var="env" scope="request">${ambiente}</c:set>
</c:if>

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>

<%
	System.out.println("RENATO");
	System.out.println(jspContext.getAttribute("titulo"));
	System.out.println("CRIVANO");
	
	String titulo = (String) jspContext.getAttribute("titulo");
	System.out.println(titulo.getClass());
%>

<%= SigaDesign.cabecalho(
		(String) jspContext.getAttribute("titulo"),  
		(String) jspContext.getAttribute("ambiente"), 
		(String) jspContext.getAttribute("meta"), 
		(String) jspContext.getAttribute("incluirJs"),
		(String) jspContext.getAttribute("onLoad"),
		"true".equals(jspContext.getAttribute("popup")),
		"Sim".equals(jspContext.getAttribute("desabilitarbusca")),
		"Sim".equals(jspContext.getAttribute("desabilitarmenu")))
%>