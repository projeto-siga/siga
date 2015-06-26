<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<c:if test="${empty param.entrevista and empty finalizacao}">
	<!-- documento -->
	<c:forEach var="v" items="${param}">
		<c:set var="key" value="${v.key}" />
		<c:set var="value" value="${v.value}" />
		<%
		request.setAttribute((String) jspContext.getAttribute("key"), jspContext.getAttribute("value"));
		%>
		<c:if test="${not empty param.teste}">
			<c:if test="${f:has(paramValues.vars, key)}">
				<input type="hidden" name="${key}" value="${value}" />
				<input type="hidden" name="vars" value="${key}" />
			</c:if>
		</c:if>
	</c:forEach>
	<jsp:doBody />
	<!-- /documento -->
</c:if>
