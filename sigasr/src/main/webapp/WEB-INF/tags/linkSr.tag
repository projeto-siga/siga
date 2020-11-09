<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="acoes" type="java.util.SortedSet"%>

<p class="gt-table-action-list mb-2">
	<c:set var="linkSeparator" value="${false}"/>
	<c:set var="classbtn" value="link-btn btn btn-sm btn-light"/>
	
	<c:forEach items="${acoes}" var="acao">
		<c:set var="linkConfirm" value="${null}"/>
		
		<c:if test="${not empty acao.msgConfirmacao}">
			<c:set var="linkConfirm" value="if (confirm('${acao.msgConfirmacao}')) " />
		</c:if>
		
		<c:set var="img">
			<img src="/siga/css/famfamfam/icons/${acao.icone}.png" class="mr-1 mb-1">
		</c:set>
		
		<c:set var="linkSeparator" value="${true}" />
		
		${acao.pre}
		<c:choose>
			<c:when test="${acao.popup}">
				<a class="${classbtn}" href="javascript:${linkConfirm != null ? linkConfirm : ''}popitup('/sigasr/app/solicitacao/${acao.urlComParams}');">${img}${acao.nome}</a>
			</c:when>
			<c:when test="${acao.ajax}">
				<span id="spanAjax_${acao.url}"></span> 
				<a class="${classbtn}" href="javascript: SetInnerHTMLFromAjaxResponse('${acao.urlComParams}', 'spanAjax_${acao.url}');">${img}${acao.nome}</a>
			</c:when>
			<c:when test="${acao.modal}">
				<a class="${classbtn}" href="javascript: ${acao.url}();">${img}${acao.nome}</a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty linkConfirm}">
						<a class="${classbtn}" href="javascript:${linkConfirm}location.href='/sigasr/app/solicitacao/${acao.urlComParams}';">${img}${acao.nome}</a>
					</c:when>
					<c:otherwise>
						<a class="${classbtn}" href="/sigasr/app/solicitacao/${acao.urlComParams}">${img}${acao.nome}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		${acao.pos}
	</c:forEach>
</p>
