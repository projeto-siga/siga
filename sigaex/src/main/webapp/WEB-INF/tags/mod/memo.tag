<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="colunas"%>
<%@ attribute name="linhas"%>
<%@ attribute name="reler"%>
<%@ attribute name="obrigatorio"%>

<c:if test="${reler == 'sim'}">
	<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<c:if test="${empty v}">
</c:if>
<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:if test="${alerta eq 'Sim' and empty v}">
	<c:forEach var="campo" items="${paramValues.obrigatorios}">
		<c:if test="${campo eq var}">
			<c:set var="vermelho">color:red</c:set>
		</c:if>
	</c:forEach>
</c:if>


<c:if test="${obrigatorio eq 'Sim'}">
	<c:set var="negrito">font-weight:bold</c:set>
	<input type="hidden" name="obrigatorios" value="${var}" />
</c:if>

<div style="padding-top:5px;">
<span style="${negrito};${vermelho}">
${titulo}
<c:if test="${not empty titulo}">:<br/></c:if>
</span>
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<textarea cols="${colunas}" rows="${linhas}" name="${var}"${jreler} >${v}</textarea>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
</div>
