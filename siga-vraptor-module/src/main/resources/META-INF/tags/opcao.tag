<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="texto" required="true" %>

<c:set var="qualquer"><jsp:doBody/></c:set>

<c:if test="${empty selectedOption}">
	<c:set var="selectedOption" value="${id}" scope="request"/>
</c:if> 

<c:if test="${createSelect == true}">
	<option value="${id}" 
		<c:if test="${selectedOption == id}">
			selected
		</c:if>
	>${texto}</option>
</c:if>
<c:if test="${createSpan == true}">
	<span id="${id}" style="display:<c:if test="${selectedOption == id}">visible</c:if><c:if test="${selectedOption != id}">none</c:if>;"><jsp:doBody/></span>
</c:if>
<c:if test="${setNull == true}">
	document.getElementById("${id}").style.display = "none";
</c:if>