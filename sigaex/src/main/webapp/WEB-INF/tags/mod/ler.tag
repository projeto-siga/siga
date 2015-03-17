<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="var"%>
<%@ attribute name="valor"%>

<c:set var="v" value="${valor}" />
<c:if test="${empty v}">
	<c:set var="v" value="${param[var]}" />
</c:if>
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>
