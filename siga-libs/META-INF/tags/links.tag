<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="inline"%>
<%@ attribute name="separator"%>

<c:remove var="linkSeparator" scope="request" />
<c:remove var="linkInline" scope="request" />

<c:if test="${not empty separator and separator != false}">
	<c:set var="linkSeparator" value="${true}" scope="request" />
</c:if>
<c:if test="${not empty inline and inline != false}">
	<c:set var="linkInline" value="${true}" scope="request" />
</c:if>

<c:if test="${not empty linkSeparator}">
	<c:if test="${empty linkInline}">
		<div class="buttons">
	</c:if>
</c:if>

<jsp:doBody/>

<c:if test="${not empty linkSeparator}">
	<c:if test="${empty linkInline}">
		</div>
	</c:if>
	<c:remove var="linkSeparator" scope="request" />
</c:if>

<c:remove var="linkInline" scope="request" />
