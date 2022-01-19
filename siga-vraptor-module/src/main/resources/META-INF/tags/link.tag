<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>
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
<%@ attribute name="modal"%>
<%@ attribute name="descr"%>
<%@ attribute name="explicacao"%>
<%@ attribute name="post"%>

<c:set var="exibirExplicacao" scope="request" value="${libs:podeExibirRegraDeNegocioEmBotoes(titular, lotaTitular)}" />

<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<c:set var="btnClass" value="btn btn-sm btn-light" />
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${empty test or test}">
				<c:set var="btnClass" value="btn btn-sm btn-info text-white" />
			</c:when>
			<c:otherwise>
				<c:set var="btnClass" value="btn btn-sm btn-light text-secondary" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<c:set var="linkId">${title}</c:set>
<c:if test="${(atalho == true) and fn:contains(title, '_')}">
	<c:set var="ious" value="${fn:indexOf(title, '_')}" />
	<c:set var="accesskey"
		value="${fn:toLowerCase(fn:substring(title, ious+1, ious+2))}" />
	<c:set var="linkId">${fn:substring(title, 0, ious)}${fn:substring(title, ious+1, ious+2)}${fn:substring(title, ious+2, -1)}</c:set>
	<c:set var="title">${fn:substring(title, 0, ious)}<u>${fn:substring(title, ious+1, ious+2)}</u>${fn:substring(title, ious+2, -1)}</c:set>
</c:if>
<c:set var="linkId"
	value="${fn:replace(libs:slugify(linkId, true, false),'-nbsp-','-')}" />

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
		<img src="/siga/css/famfamfam/icons/${icon}.png" class="mr-1 mb-1"
			title="${titleImg}">
	</c:set>
</c:if>

<c:if test="${exibirExplicacao and (not empty explicacao or not empty descr)}">
	<c:set var="tooltip">
		<b>${title}</b>
		<c:if test="${not empty descr}">
		<hr /><p style='margin-bottom: 0;'>${descr}</p>
		</c:if>
		<c:if test="${not empty explicacao}">
		<hr /><p style='font-size: 70%; color: gray; margin-bottom: 0;'>${explicacao}</p>
		</c:if>
	</c:set>
</c:if>
<c:choose>
	<c:when test="${empty test or test}">
		<c:if test="${!linkBotoes}">
			<c:if test="${not empty linkSeparator}">
				<span class="gt-separator"> |</span>
			</c:if>
			<c:if test="${empty linkSeparator}">
				<c:if test="${empty linkInline}">
					<p class="gt-table-action-list" style="${estilo}">
				</c:if>
				<c:set var="linkSeparator" value="${true}" scope="request" />
			</c:if>
		</c:if>

		<c:if test="${empty url}">${img}${title}</c:if>
	${pre}
	<c:if test="${not empty url}">
			<c:choose>
				<c:when test="${not empty modal}">
					<a id="${linkId}"
						class="${classe} ${linkBotoes ? btnClass : ''} link-tag"
						<c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
						data-toggle="modal" data-target="#${modal}"
						data-toggle="tooltip" data-html="true" title="${tooltip}" data-delay="2000"
						onclick="if (init${modal}) init${modal}()">${img}${title}</a>
				</c:when>
				<c:when test="${not empty popup and popup != false}">
					<a id="${linkId}"
						class="${classe} ${linkBotoes ? btnClass : ''} link-tag"
						<c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
						href="javascript:${linkConfirm}popitup('${url}');"
						data-toggle="tooltip" data-html="true" data-delay="2000" title="${tooltip}">${img}${title}</a>
				</c:when>
				<c:when test="${not empty ajax and ajax != false}">
					<span id="spanAjax_${idAjax}"> <a id="${linkId}"
						class="${classe} ${linkBotoes ? btnClass : ''} link-tag"
						<c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
						href="javascript: SetInnerHTMLFromAjaxResponse('${url}', 'spanAjax_${idAjax}');"
						data-toggle="tooltip" data-html="true" data-delay="2000" title="${tooltip}">${img}${title}</a>
					</span>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${not empty linkConfirm}">
							<a id="${linkId}"
								class="${classe} ${linkBotoes ? btnClass : ''} link-tag"
								<c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
								href="javascript:${linkConfirm}${post ? 'postToUrl(\''.concat(url).concat('\')') : 'location.href=\''.concat(url).concat('\';')}"
								data-toggle="tooltip" data-html="true" data-delay="2000" title="${tooltip}">${img}${title}</a>
						</c:when>
						<c:otherwise>
							<a id="${linkId}"
								class="${classe} ${linkBotoes ? btnClass : ''} link-tag"
								<c:if test="${not empty accesskey}">accesskey="${accesskey}"</c:if>
								href="${post ? 'javascript:postToUrl(\''.concat(url).concat('\')') : url}"
								data-toggle="tooltip" data-html="true" data-delay="2000" title="${tooltip}">${img}${title}</a>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</c:if>
	${pos}
</c:when>
	<c:otherwise>
		<a id="${linkId}" class="btn btn-sm btn-light text-secondary link-tag link-tag-hidden d-none"
			data-toggle="tooltip" data-html="true" title="${tooltip}">${img}${title}</a>
	</c:otherwise>
</c:choose>
