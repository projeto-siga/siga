<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="valor"%>
<%@ attribute name="exibir"%>

<c:set var="v"><jsp:doBody/></c:set>
<%--<c:set var="v"><c:out value="${v}"/></c:set>--%>
<c:if test="${empty v}">
<c:set var="v" value="${valor}"/>
</c:if>

<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>
<c:if test="${exibir == 'sim'}">
${pageScope.titulo}:
</c:if>
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="hidden" name="${var}" value="${v}"/>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>
<c:if test="${exibir == 'sim'}">
	<span class="valor">${v}</span>
</c:if>
