<%@ tag body-content="scriptless"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<c:if test="${not empty assinatura}">
	<!-- assinatura -->
	<jsp:doBody />
	<!-- /assinatura -->
</c:if>
