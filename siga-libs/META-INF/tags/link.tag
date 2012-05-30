<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="pre"%>
<%@ attribute name="title"%>
<%@ attribute name="pos"%>
<%@ attribute name="url"%>
<%@ attribute name="test"%>
<%@ attribute name="popup"%>
<%@ attribute name="confirm"%>
<%@ attribute name="ajax"%>
<%@ attribute name="idAjax"%>

<c:if test="${not empty confirm}">
	<c:set var="linkConfirm" value="if (confirm('${confirm}')) " />
</c:if>

<c:if test="${empty test or test}">
	<c:if test="${not empty linkSeparator}">|</c:if>
	<c:if test="${empty linkSeparator}">
		<c:if test="${empty linkInline}">
			<div class="buttons">
		</c:if>
		<c:set var="linkSeparator" value="${true}" scope="request" />
	</c:if>
	<c:if test="${empty url}">${title}</c:if>
	${pre}
	<c:if test="${not empty url}">
		<c:choose>
			<c:when test="${not empty popup and popup != false}">
				<a class="button" href="javascript:${linkConfirm}popitup('${url}');">${title}</a>
			</c:when>
			<c:when test="${not empty ajax and ajax != false}">
				<span id="spanAjax_${idAjax}"> 
					<a class="button" href="javascript: SetInnerHTMLFromAjaxResponse('${url}', 'spanAjax_${idAjax}');">${title}</a>
				</span>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty linkConfirm}">
						<a class="button"
							href="javascript:${linkConfirm}location.href='${url}';">${title}</a>
					</c:when>
					<c:otherwise>
						<a class="button once" href="${url}">${title}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:if>
	${pos}
</c:if>
