<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="pre"%>
<%@ attribute name="icon"%>
<%@ attribute name="title"%>
<%@ attribute name="titleImg"%>
<%@ attribute name="pos"%>
<%@ attribute name="url"%>
<%@ attribute name="test"%>
<%@ attribute name="popup"%>
<%@ attribute name="confirm"%>
<%@ attribute name="ajax"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="classe"%>
<%@ attribute name="estilo"%>
<%@ attribute name="atalho"%>

<c:if test="${(atalho == true) and fn:contains(title, '_')}">
	<c:set var="ious" value="${fn:indexOf(title, '_')}" />
	<c:set var="accesskey" value="${fn:toLowerCase(fn:substring(title, ious+1, ious+2))}" />
	<c:set var="title">${fn:substring(title, 0, ious)}<u>${fn:substring(title, ious+1, ious+2)}</u>${fn:substring(title, ious+2, -1)}</c:set>
</c:if>

<c:if test="${(atalho == true) and fn:contains(title, '_')}">
	<c:set var="ious" value="${fn:indexOf(title, '_')}" />
	<c:set var="title">${fn:substring(title, 0, ious)}<u>${fn:substring(title, ious+1, ious+1)}</u>${fn:substring(title, ious+2, -1)}</c:set>
</c:if>

<c:if test="${not empty confirm}">
	<c:set var="linkConfirm" value="if (confirm('${confirm}')) " />
</c:if>

<c:set var="img" value="" />
<c:if test="${not empty icon}">
	<c:set var="img">
		<img src="/siga/css/famfamfam/icons/${icon}.png"
			style="margin-right: 5px;" title="${titleImg}">
	</c:set>
</c:if>

<c:if test="${empty test or test}">
	<c:if test="${not empty linkSeparator}">
		<span class="gt-separator"> |</span>
	</c:if>
	<c:if test="${empty linkSeparator}">
		<c:if test="${empty linkInline}">
			<p class="gt-table-action-list" style="${estilo}">
		</c:if>
		<c:set var="linkSeparator" value="${true}" scope="request" />
	</c:if>
	<c:if test="${empty url}">${img}${title}</c:if>
	${pre}
	<c:if test="${not empty url}">
		<c:choose>
			<c:when test="${not empty popup and popup != false}">
				<a class="${classe}" <c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
					href="javascript:${linkConfirm}popitup('${url}');">${img}${title}</a>
			</c:when>
			<c:when test="${not empty ajax and ajax != false}">
				<span id="spanAjax_${idAjax}"> <a class="${classe}" <c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
					href="javascript: SetInnerHTMLFromAjaxResponse('${url}', 'spanAjax_${idAjax}');">${img}${title}</a>
				</span>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty linkConfirm}">
						<a class="${classe}" <c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
							href="javascript:${linkConfirm}location.href='${url}';">${img}${title}</a>
					</c:when>
					<c:otherwise>
						<a class="${classe}" <c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if> href="${url}">${img}${title}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:if>
	${pos}
</c:if>
