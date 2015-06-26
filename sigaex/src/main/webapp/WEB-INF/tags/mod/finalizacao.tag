<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<c:if test="${not empty finalizacao}">
	<!-- finalizacao -->
	<jsp:doBody />
	<!-- /finalizacao -->
</c:if>
