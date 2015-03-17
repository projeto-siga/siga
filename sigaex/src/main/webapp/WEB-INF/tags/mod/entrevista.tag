<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty param.entrevista}">
<!-- entrevista -->
<jsp:doBody />
<!-- /entrevista -->
</c:if>
