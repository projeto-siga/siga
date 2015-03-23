<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
${dtPrevPubl}
<c:if test="${not empty mensagem}">
	<span style="color:#FF0000">(${mensagem})</span>
</c:if>
