<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="visivel" %>

<%
	//passa a visibilidade escolhida para topico.tag
	request.setAttribute("v",jspContext.getAttribute("visivel"));
%>

<c:choose>
	<c:when test="${visivel == 'false'}">
		<!-- resumo -->
		<jsp:doBody />
		<!-- /resumo -->
	</c:when>
	<c:otherwise>
		Resumo:
		<ul>
		<!-- resumo -->
		<jsp:doBody />
		<!-- /resumo -->
		</ul>
	</c:otherwise>
</c:choose>
