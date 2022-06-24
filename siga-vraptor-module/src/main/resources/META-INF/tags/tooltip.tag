<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<%@ attribute name="title"%>
<%@ attribute name="descr"%>
<%@ attribute name="explicacao"%>

<c:if test="${not empty explicacao or not empty descr}">
	<c:set var="tooltip">
		<b>${title}</b>
		<c:if test="${not empty descr}">
		<hr /><p style='margin-bottom: 0;'>${descr}</p>
		</c:if>
		<c:if test="${not empty explicacao}">
		<hr /><p style='font-size: 70%; color: gray; margin-bottom: 0;'>${explicacao}</p>
		</c:if>
	</c:set>
	
	data-toggle="tooltip" data-html="true" data-delay="2000" title="${tooltip}" 
</c:if>