<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
${dtPrevPubl}
<c:if test="${not empty descrFeriado}">
	<span style="color:#FF0000">(${descrFeriado})</span>
</c:if>
