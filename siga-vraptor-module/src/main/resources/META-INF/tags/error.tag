<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ attribute name="name" required="true"%>

<c:if test="${errors != null && !errors.isEmpty()}">
	<c:forEach var="error" items="${errors}">
		<c:if test="${error.category == name}">
			<span style="color: red">${error.message}</span>
		</c:if>
	</c:forEach>

</c:if>