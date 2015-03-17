<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="descricao" %>
<%@ attribute name="valor" %>

<c:choose>
	<c:when test="${v == 'false'}">
		
	</c:when>
	<c:otherwise>
		<li>${descricao}=${valor}
	</c:otherwise>
</c:choose>
<!-- topico -->
	<input type="hidden" name="${descricao}" value="${valor}"/>
<!-- /topico -->


