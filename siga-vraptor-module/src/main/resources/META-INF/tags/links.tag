<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="inline"%>
<%@ attribute name="separator"%>
<%@ attribute name="buttons"%>
<%@ attribute name="estilo"%>

<c:remove var="linkSeparator" scope="request" />
<c:remove var="linkInline" scope="request" />

<c:set var="linkBotoes" value="${empty buttons || buttons != false}" scope="request" />
<c:if test="${not empty separator and separator != false}">
	<c:set var="linkSeparator" value="${true}" scope="request" />
</c:if>
<c:if test="${not empty inline and inline != false}">
	<c:set var="linkInline" value="${true}" scope="request" />
</c:if>

<c:if test="${empty linkInline}">
	<p class="gt-table-action-list" style="${estilo}">
</c:if>

<jsp:doBody/>

<c:if test="${empty linkInline}">
	</p>
</c:if>
<c:remove var="linkSeparator" scope="request" />

<c:remove var="linkInline" scope="request" />
