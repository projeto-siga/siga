<%@ tag body-content="scriptless"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<c:if test="${not empty finalizacao}">
	<!-- finalizacao -->
	<jsp:doBody />
	<!-- /finalizacao -->
</c:if>
