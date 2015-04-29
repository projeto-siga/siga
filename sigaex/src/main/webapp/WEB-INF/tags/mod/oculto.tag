<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="maxcaracteres"%>
<%@ attribute name="valor"%>

<c:if test="${reler == 'sim'}">
	<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${not empty maxcaracteres}">
	<c:set var="jmaxcaracteres"> maxlength="${maxcaracteres}"</c:set>
</c:if>

<c:set var="v" value="${valor}" />
<c:if test="${empty v}">
	<c:set var="v" value="${param[var]}" />
</c:if>
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="hidden" id="${var}" name="${var}" value="${v}" ${jreler}${jmaxcaracteres}/>
	</c:when>
	<c:otherwise>
		<%--<span class="valor">${v}</span> --%>
	</c:otherwise>
</c:choose>
