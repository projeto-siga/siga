<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="opcoes"%>
<%@ attribute name="reler"%>
<%@ attribute name="onclick"%>
<%@ attribute name="idAjax"%>

<c:if test="${reler == 'sim'}">
<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>
<c:if test="${reler == 'ajax'}">
<c:set var="jreler"> onchange="javascript: sbmt('${idAjax}');"</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}"></c:set>
</c:if>

<c:forEach var="opcao" items="${fn:split(opcoes,';')}">
	<c:if test="${empty v}">
		<c:set var="v" value="${opcao}" />
	</c:if>
</c:forEach>

<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

${titulo}:
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<select name="${var}" ${jreler} onclick="${onclick}">
			<c:forEach var="opcao" items="${fn:split(opcoes,';')}">
				<c:choose>
					<c:when test="${v == opcao}">
						<option selected value="${opcao}">${opcao}</option>
					</c:when>
					<c:otherwise>
						<option value="${opcao}">${opcao}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>


